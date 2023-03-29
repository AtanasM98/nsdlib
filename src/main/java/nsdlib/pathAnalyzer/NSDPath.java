package nsdlib.pathAnalyzer;

import nsdlib.elements.NSDElement;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.parts.AlternativesRenderPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NSDPath {
    private ArrayList<NSDElement> path;
    private ArrayList<Integer> decisionIndex;
    private int currentIndexPosition;

    public NSDPath()
    {
        path = new ArrayList<>();
        decisionIndex = new ArrayList<>();
        currentIndexPosition = 0;
    }

    public void setDecisionIndex(int indexPosition, int index) {
        if(indexPosition >= decisionIndex.size())
            decisionIndex.add(index);
        else  {
            decisionIndex.set(indexPosition, index);
        }
    }

    public NSDPath(ArrayList<NSDElement> path)
    {
        this.path = path;
    }

    public void addToPath(NSDElement element)
    {
        path.add(element);
    }

    public List<NSDElement> getPath() {
        return Collections.unmodifiableList(this.path);
    }

    public void showPath(RenderColor color)
    {
        currentIndexPosition = 0;
        for (NSDElement element : this.path) {
            element.toRenderPart().setBackground(color);
            if (element instanceof NSDCase || element instanceof NSDDecision) {
                AlternativesRenderPart elementAltRenderPart = (AlternativesRenderPart) element.toRenderPart();
                elementAltRenderPart.setBackgroundCases(RenderColor.WHITE);
                elementAltRenderPart.setBackgroundCase(color, decisionIndex.get(currentIndexPosition));
                currentIndexPosition++;
            }
        }
    }

    public void hidePath()
    {
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
        return Objects.equals(path, nsdPath.path) && Objects.equals(decisionIndex, nsdPath.decisionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, decisionIndex);
    }

    public String toString()
    {
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
