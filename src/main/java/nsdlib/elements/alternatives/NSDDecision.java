package nsdlib.elements.alternatives;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.AlternativesRenderPart;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.RenderPart;


/**
 * Represents a decision element, i.e. an if-then-else construct. The else
 * branch is optional.
 */
public class NSDDecision extends NSDElement
{
    private final NSDContainer<NSDElement> then, otherwise;

    /**
     * @param label The element's label.
     */
    public NSDDecision(String nodeId, String label)
    {
        this(nodeId, label, null, null);
    }

    /**
     * @param label The element's label.
     * @param then The child elements for the "then" branch.
     */
    public NSDDecision(String nodeId, String label, Collection<? extends NSDElement> then)
    {
        this(nodeId, label, then, null);
    }

    /**
     * @param label The element's label.
     * @param then The child elements for the "then" branch.
     * @param otherwise The child elements for the "else" branch.
     */
    public NSDDecision(String nodeId, String label,
                       Collection<? extends NSDElement> then, Collection<? extends NSDElement> otherwise)
    {
        super(nodeId, label);

        this.then = new NSDContainer<>("T", then);
        this.otherwise = new NSDContainer<>("F", otherwise);
    }

    /**
     * @return The child elements for the "then" branch of this decision.
     */
    public NSDContainer<NSDElement> getThen()
    {
        return then;
    }

    /**
     * @return The child elements for the "else" branch of this decision.
     */
    public NSDContainer<NSDElement> getElse()
    {
        return otherwise;
    }

    @Override
    public RenderPart toRenderPart()
    {
        if(renderPart == null) {
            List<String> labels = Arrays.asList(then.getLabel(), otherwise.getLabel());
            List<RenderPart> contents = Arrays.asList(then.toRenderPart(), otherwise.toRenderPart());

            renderPart = new AlternativesRenderPart(this, getLabel(), labels, contents);
        }
        return renderPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NSDDecision that = (NSDDecision) o;
        return then.equals(that.then) && otherwise.equals(that.otherwise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), then, otherwise);
    }
}
