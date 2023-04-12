package nsdlib.pathAnalyzer;

import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.elements.loops.NSDTestFirstLoop;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class NSDPathAnalyzer
{
    private ArrayList<NSDPath> paths;
    private final NSDRoot diagram;

    public NSDPathAnalyzer(NSDRoot diagram) {
        this.paths = new ArrayList<>();
        this.diagram = diagram;
    }

    public List<NSDPath> getPaths()
    {
        return Collections.unmodifiableList(paths);
    }

    public void analyzePaths() {
        paths.clear();
        this.paths = analyzePath(diagram);
    }

    private ArrayList<NSDPath> analyzePath(NSDContainer<NSDElement> container) {
        ArrayList<NSDPath> results = new ArrayList<NSDPath>();
        results.add(new NSDPath());
        for (int i = 0; i < container.countChildren(); i++) {
            NSDElement child = container.getChild(i);
            for (NSDPath path: results) {
                if(!path.finished())
                    path.addToPath(child);
            }
            if(child instanceof NSDDecision) {
                analyzeInnerDecision(results, child);
            }
            else if (child instanceof NSDCase) {
                analyzeInnerCase(results, child);
            }
            else if (child instanceof NSDTestFirstLoop) {
                analyzeInnerTestFirstLoop(results, (NSDTestFirstLoop) child);
            }
            else if (child instanceof NSDContainer) {
                analyzeContainer(results, (NSDContainer<NSDElement>) child);
            }
        }
        return results;
    }

    private void analyzeContainer(ArrayList<NSDPath> results, NSDContainer<NSDElement> child) {
        for (NSDPath result : results) {
            if(!result.finished()) {
                ArrayList<NSDPath> foundPaths = analyzePath(child);
                for (NSDPath foundPath : foundPaths) {
                    result.appendPath(foundPath);
                }
            }
        }
    }

    private void analyzeInnerDecision(ArrayList<NSDPath> results, NSDElement child) {
        int resultsSize = results.size();
        List<NSDPath> pathsToRemove = new ArrayList<>();
        for (int i = 0; i < resultsSize; i++) {
            if(!results.get(i).finished()) {
                ArrayList<NSDPath> foundThenPaths = analyzePath(((NSDDecision) child).getThen());
                for (NSDPath foundPath: foundThenPaths) {
                    NSDPath decPath = new NSDPath(results.get(i));
                    decPath.setDecisionIndex(child, 0);
                    decPath.appendPath(foundPath);
                    results.add(decPath);
                }
                ArrayList<NSDPath> foundElsePaths = analyzePath(((NSDDecision) child).getElse());
                for (NSDPath foundPath: foundElsePaths) {
                    NSDPath decPath = new NSDPath(results.get(i));
                    decPath.setDecisionIndex(child, 1);
                    decPath.appendPath(foundPath);
                    results.add(decPath);
                }
                pathsToRemove.add(results.get(i));
            }
        }
        for (NSDPath path : pathsToRemove) {
            results.remove(path);
        }
    }

    private void analyzeInnerCase(ArrayList<NSDPath> results, NSDElement child) {
        int casesCount = ((NSDCase) child).countChildren();
        int resultsSize = results.size();
        List<NSDPath> pathsToRemove = new ArrayList<>();
        for (int i = 0; i < resultsSize; i++) {
            if(!results.get(i).finished()) {
                for (int j = 0; j < casesCount; j++) {
                    ArrayList<NSDPath> foundCasePaths = analyzePath(((NSDCase) child).getChild(j));
                    for (NSDPath foundPath: foundCasePaths) {
                        NSDPath casePath = new NSDPath(results.get(i));
                        casePath.setDecisionIndex(child, j);
                        casePath.appendPath(foundPath);
                        results.add(casePath);
                    }
                }
                pathsToRemove.add(results.get(i));
            }
        }

        for (NSDPath path : pathsToRemove) {
            results.remove(path);
        }
    }

    private void analyzeInnerTestFirstLoop(ArrayList<NSDPath> results, NSDTestFirstLoop child) {
        int resultsSize = results.size();
        for (int j = 0; j < resultsSize; j++) {
            if(!results.get(j).finished()) {
                ArrayList<NSDPath> foundInnerPaths = analyzePath(child);
                for (NSDPath foundPath: foundInnerPaths) {
                    NSDPath decPath = new NSDPath(results.get(j));
                    decPath.appendPath(foundPath);
                    results.add(decPath);
                }
            }
        }
    }
}
