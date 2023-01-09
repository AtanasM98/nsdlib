package nsdlib.elements.loops;

import java.util.Collection;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a loop element that loops forever.
 */
public class NSDForever extends NSDContainer<NSDElement>
{
    /**
     * Constructs an empty forever loop block.
     */
    public NSDForever(String nodeId)
    {
        super(nodeId, (String) null);
    }

    /**
     * @param children The element's initial child elements.
     */
    public NSDForever(String nodeId, Collection<? extends NSDElement> children)
    {
        super(nodeId, null, children);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new BraceRenderPart(this, getChildRenderParts(), true, null, true, null);
        }
        return renderPart;
    }
}
