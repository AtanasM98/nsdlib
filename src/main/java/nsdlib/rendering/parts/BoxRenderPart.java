package nsdlib.rendering.parts;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;

import java.util.Objects;


/**
 * Render part for a simple instruction box.
 */
public class BoxRenderPart extends RenderPart
{
    private final String label;
    private Size size;

    /**
     * Constructs a new box part with the given label.
     *
     * @param source This part's source element.
     * @param s The box label.
     */
    public BoxRenderPart(NSDElement source, String s)
    {
        super(source);

        this.label = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoxRenderPart that = (BoxRenderPart) o;
        return Objects.equals(label, that.label) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label, size);
    }

    @Override
    public void layout(RenderContext ctx)
    {
        size = ctx.box(label);
    }

    @Override
    public Size getSize()
    {
        return size;
    }

    @Override
    public void setSize(Size s) {
        this.size = s;
    }

    @Override
    public void render(RenderAdapter<?> adapter, int x, int y, int w)
    {
        positionX = x;
        positionY = y;
        width = w;

        adapter.fillRect(x, y, w, size.height, getBackground());

        adapter.drawRect(x, y, w, size.height);
        adapter.drawStringLeft(label, x, y);
    }
}
