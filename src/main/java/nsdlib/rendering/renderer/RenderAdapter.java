package nsdlib.rendering.renderer;

import nsdlib.rendering.RenderColor;


/**
 * This class provides methods for library-agnostic rendering of lines,
 * rectangles and strings.
 *
 * <p>
 * An implementation example can be found for AWT's {@code BufferedImage} in
 * {@code nsdlib.rendering.renderer.awt.AwtRenderAdapter}.
 *
 * @param <T> The type of result this adapter is operating on.
 */
public abstract class RenderAdapter<T>
{
    private final RenderContext ctx;

    /**
     * @param ctx The context this adapter is using.
     */
    public RenderAdapter(RenderContext ctx)
    {
        this.ctx = ctx;
    }

    /**
     * Finishes rendering by releasing all potentially acquired resources and
     * returning the render result.
     *
     * @return The render result.
     */
    public abstract T finish();

    /**
     * Draws a line from the given starting point to the given end point.
     *
     * @param x1 The start point's x coordinate.
     * @param y1 The start point's y coordinate.
     * @param x2 The end point's x coordinate.
     * @param y2 The end point's y coordinate.
     */
    public abstract void drawLine(int x1, int y1, int x2, int y2);

    /**
     * Draws a rectangle with the given dimensions. The first corner is given,
     * the second corner is at (x + w, y + h). Note: This means that there is a
     * 1 pixel overlap between calls with successive coordinates, this is
     * intended!
     *
     * @param x The x coordinate of the top-left corner.
     * @param y The y coordinate of the top-left corner.
     * @param w The horizontal distance to the bottom-right corner.
     * @param h The vertical distance to the bottom-right corner.
     */
    public abstract void drawRect(int x, int y, int w, int h);

    /**
     * Draws a polygon with the given lists of coordinates.
     *
     * @param x Set of x parts of the coordinates.
     * @param y Set of y parts of the coordinates.
     * @param n Number of coordinates.
     */
    public abstract void drawPolygon(int[] x, int[] y, int n);

    /**
     * Fills a rectangle with the given color. The coordinates are calculated
     * exactly the same as for {@link #drawRect(int, int, int, int)}.
     *
     * @param x The x coordinate of the top-left corner.
     * @param y The y coordinate of the top-left corner.
     * @param w The horizontal distance to the bottom-right corner.
     * @param h The vertical distance to the bottom-right corner.
     * @param col The color to fill the rectangle with.
     */
    public abstract void fillRect(int x, int y, int w, int h, RenderColor col);

    /**
     * Draws a polygon with the given lists of coordinates and colors it in given color.
     *
     * @param x Set of x parts of the coordinates.
     * @param y Set of y parts of the coordinates.
     * @param n Number of coordinates.
     * @param col Color of polygon.
     */
    public abstract void fillPolygon(int[] x, int[] y, int n, RenderColor col);

    /**
     * Draws a left-aligned string. The given coordinates specify the top-left
     * corner of an imaginary box around the string, which means that the
     * baseline will be located at {@code (x + padH, y + padV + stringHeight)}.
     *
     * @param s The string to draw.
     * @param x The box's x coordinate.
     * @param y The box's y coordinate.
     */
    public void drawStringLeft(String s, int x, int y)
    {
        if (s == null) {
            return;
        }

        String[] stringSplit = s.split("/n");

        x += ctx.getHorizontalPadding();
        y += ctx.getVerticalPadding() + (ctx.stringHeight(s) / stringSplit.length);

        for (String line: stringSplit) {
            drawStringAt(line, x, y);
            y += ctx.stringHeight(line) / 2;
        }
    }

    /**
     * Draws a center-aligned string. The given x coordinate specifies the
     * horizontal center of an imaginary box around the string, while the y
     * coordinate is that box's y coordinate. In other words, the baseline will
     * be located at {@code (x - stringWidth / 2, y + padV + stringHeight)}.
     *
     * @param s The string to draw.
     * @param x The box's horizontal center coordinate.
     * @param y The box's y coordinate.
     */
    public void drawStringCentered(String s, int x, int y)
    {
        if (s == null) {
            return;
        }

        String[] stringSplit = s.split("/n");

        x -= ctx.stringWidth(stringSplit[0]) / 2;
        y += ctx.getVerticalPadding() + ctx.stringHeight(s);

        for (String line: stringSplit) {
            drawStringAt(line, x, y);
            y += ctx.stringHeight(s) / 2;
        }
    }

    /**
     * Draws the string positioned exactly at the given coordinates.
     *
     * @param s The string to draw.
     * @param x The baseline x coordinate.
     * @param y The baseline y coordinate.
     */
    protected abstract void drawStringAt(String s, int x, int y);
}
