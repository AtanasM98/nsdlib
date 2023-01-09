package nsdlib.elements;

import java.util.Collection;

import nsdlib.rendering.parts.BoxRenderPart;
import nsdlib.rendering.parts.RenderPart;
import nsdlib.rendering.parts.RootRenderPart;


/**
 * The root element for any NS diagram (structogram).
 */
public class NSDRoot extends NSDContainer<NSDElement>
{
    /**
     * @param label The element's label.\
     * @param nodeId id of expression that creates object.
     */
    public NSDRoot(String nodeId, String label)
    {
        super(nodeId, label);
    }

    /**
     * @param label The element's label.
     * @param children The element's initial child elements.
     */
    public NSDRoot(String nodeId, String label, Collection<? extends NSDElement> children)
    {
        super(nodeId, label, children);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new RootRenderPart(this, getLabel(), getChildRenderParts());
        }
        return renderPart;
    }
}
