package nsdlib.rendering.renderer.awt;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import nsdlib.rendering.renderer.NSDRenderer;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Renderer subclass for rendering to AWT's {@link BufferedImage}.
 *
 * @see NSDRenderer
 * @see AwtRenderAdapter
 */
public class AwtRenderer extends NSDRenderer<BufferedImage>
{
    static final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final FontMetrics fontMetrics = new Canvas().getFontMetrics(font);

    private static final RenderContext ctx = new RenderContext(20, 10,
            fontMetrics::stringWidth, s -> fontMetrics.getAscent());

    @Override
    public RenderContext createContext()
    {
        return ctx;
    }

    @Override
    public RenderAdapter<BufferedImage> createAdapter(RenderContext context, int width, int height, double scale)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return new AwtRenderAdapter(context, img, scale);
    }
}
