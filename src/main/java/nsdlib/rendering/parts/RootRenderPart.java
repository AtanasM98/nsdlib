package nsdlib.rendering.parts;

import java.util.Collection;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.ContainerRenderPart.Orientation;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Render part for the root element, drawing a labeled box around all of its
 * children.
 */
public class RootRenderPart extends RenderPart implements IContainerHolderRenderPart
{
    private final String label;
    private final ContainerRenderPart content;

    private Size size;
    private int padHorizontal, boxHeight;

    /**
     * Constructs a new root render part with the given label and children.
     *
     * @param source This part's source element.
     * @param label The part's label.
     * @param children This container's child parts.
     */
    public RootRenderPart(NSDElement source, String label, Collection<? extends RenderPart> children)
    {
        super(source);

        this.label = label;
        this.content = new ContainerRenderPart(Orientation.VERTICAL, children);
    }

    @Override
    public ContainerRenderPart getContent() { return content; }

    @Override
    public void setBackground(RenderColor color) {
        this.background = color;
        this.content.setBackground(color);
    }

    @Override
    public RenderPart findForSource(NSDElement source)
    {
        return source == getSource() ? this : content.findForSource(source);
    }

    @Override
    public void layout(RenderContext ctx)
    {
        content.layout(ctx);

        Size box = ctx.box(label);
        Size contentSize = content.getSize();

        padHorizontal = ctx.getHorizontalPadding();
        boxHeight = box.height;

        int width = Math.max(2 * padHorizontal + contentSize.width, box.width);
        int height = boxHeight + contentSize.height + ctx.getVerticalPadding();

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

        adapter.fillRect(x, y, w, boxHeight, getBackground());

        adapter.drawRect(x, y, w, size.height);
        adapter.drawStringLeft(label, x, y);
        y += boxHeight;

        content.render(adapter, x + padHorizontal, y, w - padHorizontal * 2);
    }
}
