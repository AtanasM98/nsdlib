package nsdlib.rendering.parts;

import java.util.Collection;
import java.util.Objects;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.ContainerRenderPart.Orientation;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Render part for parallel processing elements, drawing the top and bottom
 * boxes with the children in between.
 */
public class ParallelRenderPart extends RenderPart implements IContainerHolderRenderPart
{
    private final ContainerRenderPart content;

    private Size size;
    private int decoHeight;

    /**
     * Constructs a new parallel processing part with the given children.
     *
     * @param source This part's source element.
     * @param content This container's child parts.
     */
    public ParallelRenderPart(NSDElement source, Collection<RenderPart> content)
    {
        super(source);

        this.content = new ContainerRenderPart(Orientation.HORIZONTAL, content);
    }

    @Override
    public void setBackground(RenderColor color) {
        this.background = color;
        this.content.setBackground(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParallelRenderPart that = (ParallelRenderPart) o;
        return decoHeight == that.decoHeight && content.equals(that.content) && size.equals(that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, size, decoHeight);
    }

    @Override
    public ContainerRenderPart getContent() { return content; }

    @Override
    public RenderPart findForSource(NSDElement source)
    {
        return source == getSource() ? this : content.findForSource(source);
    }

    @Override
    public void layout(RenderContext ctx)
    {
        content.layout(ctx);

        decoHeight = ctx.getVerticalPadding() * 2;

        Size contentSize = content.getSize();
        int minimumWidth = decoHeight * 3;

        int width = Math.max(minimumWidth, contentSize.width);
        int height = decoHeight * 2 + contentSize.height;

        size = new Size(width, height);
    }

    @Override
    public Size getSize()
    {
        return size;
    }

    @Override
    public void setSize(Size s) {
        size = s;
    }

    @Override
    public void render(RenderAdapter<?> adapter, int x, int y, int w)
    {
        positionX = x;
        positionY = y;
        width = w;
        this.size = new Size(w, size.height);

        adapter.fillRect(x, y, w, decoHeight, getBackground());

        // draw top
        adapter.drawRect(x, y, w, decoHeight);
        adapter.drawLine(x, y + decoHeight, x + decoHeight, y);
        adapter.drawLine(x + w - decoHeight, y, x + w, y + decoHeight);
        y += decoHeight;

        // draw content
        content.render(adapter, x, y, w);
        y += content.getSize().height;

        adapter.fillRect(x, y, w, decoHeight, getBackground());

        // draw bottom
        adapter.drawRect(x, y, w, decoHeight);
        adapter.drawLine(x, y, x + decoHeight, y + decoHeight);
        adapter.drawLine(x + w - decoHeight, y + decoHeight, x + w, y);
    }
}
