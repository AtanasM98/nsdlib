package nsdlib.rendering.parts;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;

import java.util.Objects;


/**
 * Base class for every NS part that can be rendered. Before
 * {@link #render(RenderAdapter, int, int, int)} is called, you MUST invoke
 * {@link #layout(RenderContext)}.
 */
public abstract class RenderPart
{
    private final NSDElement source;
    protected RenderColor background;
    protected int positionX;
    protected int positionY;
    protected int width;

    /**
     * Constructs a new part without a source element.
     */
    public RenderPart()
    {
        this(null);
    }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getWidth() { return width; }

    /**
     * Constructs a new part with the given element as its source.
     *
     * @param source This part's source element.
     */
    public RenderPart(NSDElement source)
    {
        this.source = source;
    }

    /**
     * @return This part's source element.
     */
    public NSDElement getSource()
    {
        return source;
    }

    /**
     * Finds the render part belonging to the given source element. Returns
     * {@code null} if no such part is found.
     *
     * @param source The element for which to find the render part.
     * @return The render part.
     */
    public RenderPart findForSource(NSDElement source)
    {
        return source == getSource() ? this : null;
    }

    /**
     * @return This part's background color ({@code null} by default).
     */
    public RenderColor getBackground()
    {
        return background;
    }

    /**
     * Sets this part's background color.
     *
     * @param color The new background color. May be null.
     */
    public void setBackground(RenderColor color)
    {
        this.background = color;
    }

    /**
     * Recursively lays out this part and its children, using the given
     * {@link RenderContext} for measuring.
     *
     * @param ctx The render context.
     */
    public abstract void layout(RenderContext ctx);

    /**
     * @return The amount of space this component requires. Note that it has to
     *         be laid out before this provides anything useful.
     *
     * @see #layout(RenderContext)
     */
    public abstract Size getSize();

    /**
     * @return void
     *
     * @see #layout(RenderContext)
     */
    public abstract void setSize(Size s);

    /**
     * Renders this part and all its child parts to the given adapter. Rendering
     * starts at the point {@code (x, y)} and extends to
     * {@code (x + w, y + getSize().height)}.
     *
     * <p>
     * Before this method is called, the part has to be laid out, preferrably
     * with the same context used by the adapter.
     *
     * @param adapter The adapter to render to.
     * @param x The x coordinate to start rendering at.
     * @param y The y coordinate to start rendering at.
     * @param w The total width the component shall occupy.
     *
     * @see #layout(RenderContext)
     */
    public abstract void render(RenderAdapter<?> adapter, int x, int y, int w);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderPart that = (RenderPart) o;
        return positionX == that.positionX && positionY == that.positionY && width == that.width && Objects.equals(background, that.background);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, background, positionX, positionY, width);
    }
}
