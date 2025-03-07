package nsdlib.rendering.renderer.awt;

import java.awt.*;
import java.awt.image.BufferedImage;

import nsdlib.rendering.RenderColor;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Render adapter for rendering to AWT's {@link BufferedImage}.
 *
 * @see RenderAdapter
 * @see AwtRenderer
 */
public class AwtRenderAdapter extends RenderAdapter<BufferedImage>
{
    private final BufferedImage img;
    private final Graphics2D g;

    /**
     * @param ctx The context this adapter is using.
     * @param img The image this adapter shall draw to.
     * @param scale The scaling factor for everything that is drawn.
     */
    public AwtRenderAdapter(RenderContext ctx, BufferedImage img, double scale)
    {
        super(ctx);

        this.img = img;
        this.g = img.createGraphics();

        g.scale(scale, scale);

        g.setFont(AwtRenderer.font);
        g.setColor(Color.BLACK);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Color toAwtColor(RenderColor col)
    {
        return new Color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawRect(int x, int y, int width, int height)
    {
        g.drawRect(x, y, width, height);
    }

    @Override
    public void drawPolygon(int[] x, int[] y, int n) { g.drawPolygon(new Polygon(x, y, n)); }

    @Override
    public void fillRect(int x, int y, int w, int h, RenderColor col)
    {
        if (col == null) {
            return;
        }

        Color prevColor = g.getColor();

        g.setColor(toAwtColor(col));
        g.fillRect(x, y, w + 1, h + 1);

        g.setColor(prevColor);
    }

    @Override
    public void fillPolygon(int[] x, int[] y, int n, RenderColor col)
    {
        if (col == null) {
            return;
        }

        Color prevColor = g.getColor();

        g.setColor(toAwtColor(col));
        Polygon p = new Polygon(x, y, n);
        g.fillPolygon(p);

        g.setColor(prevColor);
    }

    @Override
    protected void drawStringAt(String s, int x, int y)
    {
        g.drawString(s, x, y);
    }

    @Override
    public BufferedImage finish()
    {
        g.dispose();

        return img;
    }
}
