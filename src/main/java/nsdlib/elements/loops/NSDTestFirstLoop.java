package nsdlib.elements.loops;

import java.util.Collection;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a loop element that first checks its condition before the inner
 * elements are run (i.e. a {@code while} loop).
 */
public class NSDTestFirstLoop extends NSDContainer<NSDElement>
{
    /**
     * @param label The element's label.
     */
    public NSDTestFirstLoop(String nodeId, String label)
    {
        super(nodeId, label);
    }

    /**
     * @param label The element's label.
     * @param children The element's initial child elements.
     */
    public NSDTestFirstLoop(String nodeId, String label, Collection<? extends NSDElement> children)
    {
        super(nodeId, label, children);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new BraceRenderPart(this, getChildRenderParts(), true, getLabel(), false, null);
        }
        return renderPart;
    }
}
