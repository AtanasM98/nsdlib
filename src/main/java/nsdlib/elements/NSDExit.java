package nsdlib.elements;

import nsdlib.rendering.parts.BoxRenderPart;
import nsdlib.rendering.parts.ExitRenderPart;
import nsdlib.rendering.parts.RenderPart;

public class NSDExit extends NSDElement
{
    public NSDExit(String label)
    {
        super("", label);
    }

    @Override
    public RenderPart toRenderPart() {
        if(renderPart == null) {
            renderPart = new ExitRenderPart(this, this.getLabel());
        }
        return renderPart;
    }
}
