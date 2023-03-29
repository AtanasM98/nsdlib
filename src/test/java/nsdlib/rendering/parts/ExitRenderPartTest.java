package nsdlib.rendering.parts;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDExit;
import nsdlib.elements.NSDInstruction;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExitRenderPartTest {
    @Test
    public void findsForSource()
    {
        NSDElement sourceObj = new NSDExit(null, "sourceObj");
        ExitRenderPart obj = new ExitRenderPart(sourceObj, "box");

        assertSame(obj, obj.findForSource(sourceObj));
        assertNull(obj.findForSource(new NSDExit(null, "other")));
    }

    @Test
    public void calculatesSize()
    {
        RenderContext ctx = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);
        ExitRenderPart obj = new ExitRenderPart(null, "box");

        obj.layout(ctx);

        Size size = obj.getSize();
        // pad + label + pad
        assertEquals(8 + ((3 * 5) + 3 / 2) + 8, size.width);
        // pad + label + pad
        assertEquals(10 + 8 + 10, size.height);
    }

    @Test
    public void rendersBackground()
    {
        RenderContext ctx = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);
        MockRenderAdapter adapter = new MockRenderAdapter(ctx);

        ExitRenderPart obj = new ExitRenderPart(null, "box");
        obj.setBackground(new RenderColor(0xFF, 0, 0));
        obj.layout(ctx);

        obj.render(adapter, 0, 0, obj.getSize().width);

        assertTrue(adapter.fillRectCalled);
    }
}
