package nsdlib.pathAnalyzer;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDExit;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.parts.AlternativesRenderPart;

import java.util.*;

public class NSDPath {
    private ArrayList<NSDElement> path;
    private Map<NSDElement, Integer> decisionIndex;

    public NSDPath() {
        path = new ArrayList<>();
        decisionIndex = new HashMap<NSDElement, Integer>();
    }

    public NSDPath(NSDPath copy) {
        this(copy.path, copy.decisionIndex);
    }

    public NSDPath(ArrayList<NSDElement> path, Map<NSDElement, Integer> decisionIndex) {
        this.path = new ArrayList<>(path);
        this.decisionIndex = new HashMap<>(decisionIndex);
    }

    public boolean finished() {
        if(!this.path.isEmpty()) {
            if(this.path.get(this.path.size() - 1) instanceof NSDExit)
                return true;
        }
        return false;
    }

    public void setDecisionIndex(NSDElement element, int index) {
        decisionIndex.put(element, index);
    }

    public NSDPath(ArrayList<NSDElement> path)
    {
        this.path = path;
    }

    public void addToPath(NSDElement element) {
        path.add(element);
    }

    public void addToPath(ArrayList<NSDElement> element) {
        path.addAll(element);
    }

    public void appendPath(NSDPath newPath) {
        this.path.addAll(newPath.path);
        this.decisionIndex.putAll(newPath.decisionIndex);
    }

    public List<NSDElement> getPath() {
        return Collections.unmodifiableList(this.path);
    }

    public void showPath(RenderColor color) {
        for (NSDElement element : this.path) {
            element.toRenderPart().setBackground(color);
            if (element instanceof NSDCase || element instanceof NSDDecision) {
                AlternativesRenderPart elementAltRenderPart = (AlternativesRenderPart) element.toRenderPart();
                //elementAltRenderPart.setBackgroundCases(RenderColor.WHITE);
                elementAltRenderPart.setBackgroundCase(color, decisionIndex.get(element));
            }
        }
    }

    public void hidePath() {
        for (NSDElement element: this.path)
        {
            element.toRenderPart().setBackground(RenderColor.WHITE);
            if(element.toRenderPart() instanceof AlternativesRenderPart) {
                ((AlternativesRenderPart) element.toRenderPart()).setBackgroundCases(RenderColor.WHITE);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NSDPath nsdPath = (NSDPath) o;
        boolean pathEquals = Objects.equals(path, nsdPath.path);
        boolean decisionIndexEquals = Objects.equals(decisionIndex, nsdPath.decisionIndex);
        return pathEquals && decisionIndexEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, decisionIndex);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.path.size(); i++) {
            if(i < this.path.size() - 1){
                sb.append(this.path.get(i).toString()).append(", ");
            }else{
                sb.append(this.path.get(i).toString());
            }
        }
        return sb.toString();
    }
}
