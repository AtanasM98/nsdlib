package nsdlib.pathAnalyzer;

import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
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

    private int currentPathDecisionPosition = 0;

    /*
    * returns a boolean value that specifies whether the analyzer should continue building the path
    * or stop because an exit instruction has been found nested in its elements
    */
    private boolean analyzePaths(NSDContainer<NSDElement> diagram)
    {
        for (int i = 0; i < diagram.countChildren(); i++) {
            NSDElement child = diagram.getChild(i);
            NSDPath nsdPath = paths.get(currentPathIndex);
            nsdPath.addToPath(child);
            if(child instanceof NSDDecision) {
                if(visitedAlternativeIndexes.get(child) == null) {
                    nsdPath.setDecisionIndex(currentPathDecisionPosition, 0);
                    currentPathDecisionPosition++;
                    if(!analyzePaths(((NSDDecision) child).getThen()))
                        return false;
                } else {
                    nsdPath.setDecisionIndex(currentPathDecisionPosition, 1);
                    currentPathDecisionPosition++;
                    if(!analyzePaths(((NSDDecision) child).getElse()))
                        return false;
                }
            }
            else if (child instanceof NSDCase) {
                int caseIndex = visitedAlternativeIndexes.get(child) == null ? 0 :
                        visitedAlternativeIndexes.get(child) + 1;
                if(caseIndex < ((NSDCase) child).countChildren()) {
                    nsdPath.setDecisionIndex(currentPathDecisionPosition, caseIndex);
                    currentPathDecisionPosition++;
                    if(!analyzePaths(((NSDCase) child).getChildren().get(caseIndex)))
                        return false;
                }
            }
            else if (child instanceof NSDContainer) {
                if(!analyzePaths((NSDContainer<NSDElement>) child))
                    break;
            } else if(child instanceof NSDExit) {
                visitToLastDecision(nsdPath);
                currentPathDecisionPosition = 0;
                currentPathIndex++;
                if(currentPathIndex < potentialPaths) {
                    paths.add(new NSDPath());
                }
                return false;
            }
        }
        return true;
    }

    private void visitToLastDecision(NSDPath path) {
        for (int i = path.getPath().size() - 1; i >= 0 ; i--) {
            NSDElement element = path.getPath().get(i);
            if(visitedAlternativeIndexes.putIfAbsent(element, 0) != null) {
                if(element instanceof NSDDecision || element instanceof NSDCase) {
                    int index = visitedAlternativeIndexes.get(element) + 1;
                    visitedAlternativeIndexes.put(element, index);
                    if(element instanceof NSDCase) {
                        if(index < ((NSDContainer) element).countChildren() - 1)
                            break;
                    }
                }
            } else if (element instanceof NSDDecision || element instanceof NSDCase) {
                int childrenSize = 2;
                if(element instanceof NSDCase) {
                    childrenSize = ((NSDContainer) element).countChildren();
                }
                potentialPaths += childrenSize - 1;
                break;
            }
        }
    }
}
