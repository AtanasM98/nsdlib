package nsdlib.pathAnalyzer;

import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.parts.ParallelRenderPart;

import java.util.*;

public class NSDPathAnalyzer
{
    private ArrayList<NSDPath> paths;
    private int currentPathIndex;
    private NSDRoot diagram;

    public NSDPathAnalyzer(NSDRoot diagram)
    {
        this.paths = new ArrayList<>();
        this.diagram = diagram;
    }

    public List<NSDPath> getPaths()
    {
        return Collections.unmodifiableList(paths);
    }

    Map<NSDElement, Integer> visitedAlternativeIndexes;
    int potentialPaths;


    public void analyzePaths()
    {
        visitedAlternativeIndexes = new HashMap<NSDElement, Integer>();
        paths.clear();
        currentPathIndex = 0;
        potentialPaths = 1;
        paths.add(new NSDPath());
        while (currentPathIndex < potentialPaths) {
            analyzePaths(this.diagram);
        }
    }

    private void analyzePaths(NSDContainer<NSDElement> diagram)
    {
        for (int i = 0; i < diagram.countChildren(); i++) {
            NSDElement child = diagram.getChild(i);
            visitedAlternativeIndexes.putIfAbsent(child, 0);
            paths.get(currentPathIndex).addToPath(child);
            if(child instanceof NSDDecision) {
                if(visitedAlternativeIndexes.get(child) == 0) {
                    potentialPaths += 2;
                    analyzePaths(((NSDDecision) child).getThen());
                } else {
                    analyzePaths(((NSDDecision) child).getElse());
                }
            }
            else if (child instanceof NSDCase) {
                if(visitedAlternativeIndexes.get(child) == 0)
                    potentialPaths += ((NSDCase) child).countChildren() - 1;
                Integer caseIndex = visitedAlternativeIndexes.get(child);
                if(caseIndex < ((NSDCase) child).getChildren().size()) {
                    analyzePaths(((NSDCase) child).getChildren().get(caseIndex));
                    visitedAlternativeIndexes.put(child, caseIndex + 1);
                }
            }
            else if (child instanceof NSDContainer) {
                analyzePaths((NSDContainer<NSDElement>) child);
            } else if(child instanceof NSDExit) {
                paths.add(new NSDPath());
                currentPathIndex++;
                break;
            }
        }
    }
}
