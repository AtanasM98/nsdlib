package nsdlib.rendering.parts;

import java.util.*;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Render part that simply draws its children. The children can be laid out
 * vertically or horizontally.
 */
public class ContainerRenderPart extends RenderPart
{
    private final Orientation orientation;
    private final List<RenderPart> children;

    private Size size;

    /**
     * Constructs a new container part with the given orientation and children.
     *
     * @param orientation Whether this is a horizontal or vertical layout.
     * @param children This container's child parts.
     */
    public ContainerRenderPart(Orientation orientation, Collection<? extends RenderPart> children)
    {
        this.orientation = orientation;
        this.children = Collections.unmodifiableList(new ArrayList<>(children));
    }

    public List<RenderPart> getChildren() { return children; }

    @Override
    public void setBackground(RenderColor color) {
        this.background = color;
        for (RenderPart child:children) {
            child.setBackground(color);
        }
    }

    public void setBackgroundChild(RenderColor color, int index) {
        children.get(index).setBackground(color);
    }

    @Override
    public RenderPart findForSource(NSDElement source)
    {
        if (source == getSource()) {
            return this;
        }
        return children.stream().map(c -> c.findForSource(source))
                .filter(Objects::nonNull).findAny().orElse(null);
    }

    @Override
    public void layout(RenderContext ctx)
    {
        int childMaxWidth = 0, childMaxHeight = 0;
        int totalHeight = 0;

        for (RenderPart e : children) {
            e.layout(ctx);
            Size eSize = e.getSize();

            childMaxWidth = Math.max(childMaxWidth, eSize.width);
            childMaxHeight = Math.max(childMaxHeight, eSize.height);

            totalHeight += eSize.height;
        }

        int width, height;
        if (orientation == Orientation.HORIZONTAL) {
            width = childMaxWidth * children.size();
            height = childMaxHeight;
        } else {
            width = childMaxWidth;
            height = totalHeight;
        }

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
        if (children.isEmpty()) {
            return;
        }

        adapter.fillRect(x, y, w, size.height, getBackground());

        adapter.drawRect(x, y, w, size.height);

        int childWidth = (orientation == Orientation.HORIZONTAL)
                ? (w / children.size())
                : w;

        for (RenderPart e : children) {
            e.render(adapter, x, y, childWidth);

            if (orientation == Orientation.HORIZONTAL) {
                x += childWidth;
                adapter.drawLine(x, y, x, y + size.height);
            } else {
                y += e.getSize().height;
            }
        }
    }

    /**
     * Specifies a container's layout direction.
     */
    public enum Orientation
    {
        /**
         * The container shall lay out its components next to each other,
         * dividing the available width equally among them.
         */
        HORIZONTAL,

        /**
         * The container shall lay out its components vertically, allocating the
         * full width to every one of them.
         */
        VERTICAL
    }
}
