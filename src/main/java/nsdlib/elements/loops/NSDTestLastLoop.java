package nsdlib.elements.loops;

import java.util.Collection;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.ContainerRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a loop element that first runs its inner elements and then checks
 * the condition, so that the code is always run at least once (i.e. a
 * {@code do while} loop).
 */
public class NSDTestLastLoop extends NSDContainer<NSDElement>
{
    /**
     * @param label The element's label.
     */
    public NSDTestLastLoop(String nodeId, String label)
    {
        super(nodeId, label);
    }

    /**
     * @param label The element's label.
     * @param children The element's initial child elements.
     */
    public NSDTestLastLoop(String nodeId, String label, Collection<? extends NSDElement> children)
    {
        super(nodeId, label, children);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new BraceRenderPart(this, getChildRenderParts(), false, null, true, getLabel());
        }
        return renderPart;
    }
}
