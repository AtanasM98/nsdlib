package nsdlib.pathAnalyzer;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.parts.AlternativesRenderPart;

import java.util.ArrayList;

public class NSDPath {
    private ArrayList<NSDElement> path;

    public NSDPath()
    {
        path = new ArrayList<>();
    }

    public NSDPath(ArrayList<NSDElement> path)
    {
        this.path = path;
    }

    public void addToPath(NSDElement element)
    {
        path.add(element);
    }

    public void showPath(RenderColor color)
    {
        for (int i = 0; i < this.path.size(); i++) {
            NSDElement element = this.path.get(i);
            if(element instanceof NSDCase)
            {
                AlternativesRenderPart elementAltRenderPart = (AlternativesRenderPart) element.toRenderPart();
                elementAltRenderPart.setBackgroundCases(RenderColor.WHITE);
                int caseIndex = 0;
                while(!((NSDCase) element).getChildren().get(caseIndex).getChildren().contains(this.path.get(i+1))){
                    caseIndex++;
                }
                elementAltRenderPart.setBackgroundCase(color, caseIndex);
            }
            if(element instanceof NSDDecision) {
                AlternativesRenderPart elementAltRenderPart = (AlternativesRenderPart) element.toRenderPart();
                elementAltRenderPart.setBackgroundCases(RenderColor.WHITE);
                if(((NSDDecision) element).getThen().getChildren().contains(this.path.get(i+1))){
                    elementAltRenderPart.setBackgroundCase(color, 0);
                }else{
                    elementAltRenderPart.setBackgroundCase(color, 0);
                }
            }
            element.toRenderPart().setBackground(color);
        }
    }

    public void hidePath()
    {
        for (NSDElement element: this.path)
        {
            element.toRenderPart().setBackground(RenderColor.TRANSPARENT);
        }
    }

    public NSDPath copyToNewPath()
    {
        return new NSDPath(new ArrayList<>(this.path));
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
