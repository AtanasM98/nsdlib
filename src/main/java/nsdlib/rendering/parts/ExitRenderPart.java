package nsdlib.rendering.parts;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;

public class ExitRenderPart extends RenderPart
{
    private Size size;
    private final String label;

    public ExitRenderPart(NSDElement source, String s)
    {
        super(source);

        this.label = s;
    }

    @Override
    public void layout(RenderContext ctx) {
        size = ctx.box(label);
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public void setSize(Size s) {
        this.size = s;
    }

    @Override
    public void render(RenderAdapter<?> adapter, int x, int y, int w) {
        positionX = x;
        positionY = y;
        width = w;
        int height = size.height;
        setSize(new Size(w, height));

        adapter.fillRect(x, y, w, height, getBackground());
        adapter.drawRect(x, y, w, height);
        adapter.drawLine(x, y + height / 2, x + height, y);
        adapter.drawLine(x, y + height / 2, x + height, y + height);

        adapter.drawStringLeft(label, x + height, y);
    }
}
