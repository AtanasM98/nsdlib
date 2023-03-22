package nsdlib.rendering.parts;

import java.util.*;

import nsdlib.elements.NSDContainer;
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

    /**
     * Constructs a new container part with the given orientation and children.
     *
     * @param orientation Whether this is a horizontal or vertical layout.
     * @param children This container's child parts.
     */
    public ContainerRenderPart(NSDContainer source, Orientation orientation, Collection<? extends RenderPart> children)
    {
        super(source);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerRenderPart that = (ContainerRenderPart) o;
        if(children != null && size != null && that.children != null && that.size != null)
            return orientation == that.orientation && children.equals(that.children) && size.equals(that.size);
        else
            return orientation == that.orientation && children == that.children && size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientation, children, size);
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
        Size labelSize = new Size(0, 0);
        NSDElement source = getSource();
        if(source != null){
            labelSize = ctx.box(source.getLabel());
        }
        int childMaxWidth = labelSize.width, childMaxHeight = 0;
        int totalWidth = 0;
        int totalHeight = 0;

        for (RenderPart e : children) {
            e.layout(ctx);
            Size eSize = e.getSize();

            childMaxWidth = Math.max(childMaxWidth, eSize.width);
            childMaxHeight = Math.max(childMaxHeight, eSize.height);

            totalWidth += eSize.width;
            totalHeight += eSize.height;
        }

        int width, height;
        if (orientation == Orientation.HORIZONTAL) {
            width = totalWidth;
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
        positionX = x;
        positionY = y;
        width = w;
        int[] childWidths = createChildWidths(w);
        if (children.isEmpty()) {
            return;
        }
        setSize(new Size(w, size.height));
        adapter.fillRect(x, y, w, size.height, getBackground());
        adapter.drawRect(x, y, w, size.height);

        for(int i = 0; i < children.size(); i++) {
            children.get(i).render(adapter, x, y, childWidths[i]);
            if (orientation == Orientation.HORIZONTAL) {
                x += childWidths[i];
                adapter.drawLine(x, y, x, y + size.height);
            } else {
                y += children.get(i).getSize().height;
            }
        }
    }

    public int[] createChildWidths(int w) {
        boolean biggerThanHalf = false;
        int[] childWidths = new int[children.size()];
        int horChildWidthTotal = 0;

        int minIndex = 0;
        int minWidth = Integer.MAX_VALUE;
        for (int i = 0; i < children.size(); i++) {
            if(orientation == Orientation.HORIZONTAL) {
                horChildWidthTotal += children.get(i).getSize().width;
                int childWidth = children.get(i).getSize().width;
                if(minWidth > childWidth) {
                    minWidth = childWidth;
                    minIndex = i;
                }
                childWidths[i] = childWidth;
                if(childWidth >= (w / children.size()))
                    biggerThanHalf = true;
            }
            else {
                childWidths[i] = width;
            }
        }
        if(orientation == Orientation.HORIZONTAL && children.size() > 0) {
            if (horChildWidthTotal < w) {
                if(biggerThanHalf)
                    childWidths[minIndex] += w - horChildWidthTotal;
                else
                    Arrays.fill(childWidths, w / children.size());
            }
        }
        return childWidths;
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
