package nsdlib.elements;

import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.parts.RenderPart;

import java.util.Objects;


/**
 * Base class for all NS diagram elements.
 */
public abstract class NSDElement
{
    protected RenderPart renderPart;
    private String label;

    private final String exprNodeId;

    /**
     * @param label The element's label.
     * @param nodeId The progenitor's node id. // todo think of a better comment
     */
    public NSDElement(String nodeId, String label)
    {
        this.exprNodeId = nodeId;
        this.label = label;
    }

    public String getExprNodeId() {
        return exprNodeId;
    }

    /**
     * @return The element's label.
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Sets the element's label to the given string.
     *
     * @param label The new label.
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    /**
     * Converts this element into an instance of {@link RenderPart}, i.e. into
     * something that can be laid out and rendered.
     *
     * @return A new render part for this element.
     */
    public abstract RenderPart toRenderPart();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NSDElement that = (NSDElement) o;
        if(this.renderPart != null && that.renderPart != null)
            return label.equals(that.label) && this.renderPart.equals(that.renderPart);
        else
            return label.equals(that.label) && this.renderPart == that.renderPart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
