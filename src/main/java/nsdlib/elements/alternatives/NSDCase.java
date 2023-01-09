package nsdlib.elements.alternatives;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.AlternativesRenderPart;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a case / select / switch element, i.e. multiple possible execution
 * lists of which one is chosen depending on some input or question.
 */
public class NSDCase extends NSDContainer<NSDContainer<NSDElement>>
{
    /**
     * @param label The element's label.
     */
    public NSDCase(String nodeId, String label)
    {
        super(nodeId, label);
    }

    /**
     * @param label The element's label.
     * @param children The element's initial child elements.
     */
    public NSDCase(String nodeId, String label, Collection<? extends NSDContainer<NSDElement>> children)
    {
        super(nodeId, label, children);
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            renderPart = new AlternativesRenderPart(this, getLabel(), getChildLabels(), getChildRenderParts());
        }
        return renderPart;
    }

    private List<String> getChildLabels()
    {
        return stream().map(NSDElement::getLabel).collect(Collectors.toList());
    }
}
