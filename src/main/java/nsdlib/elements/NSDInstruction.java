package nsdlib.elements;

import nsdlib.rendering.parts.BoxRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a standard "process" or instruction block in an NS diagram.
 */
public class NSDInstruction extends NSDElement
{
    /**
     * @param label The element's label.
     */
    public NSDInstruction(String nodeId, String label)
    {
        super(nodeId, label);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new BoxRenderPart(this, getLabel());
        }
        return renderPart;
    }
}
