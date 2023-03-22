package nsdlib.rendering.parts;

import java.util.Arrays;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDInstruction;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.renderer.RenderContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AlternativesRenderPartTest
{
    @Test
    public void findsForSource()
    {
        NSDElement source0 = new NSDInstruction(null, "source0");
        MockRenderPart child0 = new MockRenderPart(source0);

        NSDElement source1 = new NSDInstruction(null, "source1");
        MockRenderPart child1 = new MockRenderPart(source1);

        NSDElement sourceObj = new NSDInstruction(null, "sourceObj");
        AlternativesRenderPart obj = new AlternativesRenderPart(sourceObj, "condition",
                Arrays.asList("left", "right"), Arrays.asList(child0, child1));

        assertSame(obj, obj.findForSource(sourceObj));
        assertSame(child0, obj.findForSource(source0));
        assertSame(child1, obj.findForSource(source1));
        assertNull(obj.findForSource(new NSDInstruction(null, "other")));
    }

    @Test
    public void callsLayoutForChildren()
    {
        RenderContext ctx = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);

        MockRenderPart child0 = new MockRenderPart();
        MockRenderPart child1 = new MockRenderPart();

        AlternativesRenderPart obj = new AlternativesRenderPart(null, "condition",
                Arrays.asList("left", "right"), Arrays.asList(child0, child1));

        obj.layout(ctx);

        assertTrue(child0.layoutCalled);
        assertTrue(child1.layoutCalled);
    }

    @Test
    public void calculatesSizeWithLargeHeading()
    {
        RenderContext ctx = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);

        MockRenderPart child0 = new MockRenderPart();
        child0.sizeToUse = new Size(0, 20);
        MockRenderPart child1 = new MockRenderPart();
        child1.sizeToUse = new Size(0, 20);

        AlternativesRenderPart obj = new AlternativesRenderPart(null, "this really long string has a length of 42",
                Arrays.asList("L", "R"), Arrays.asList(child0, child1));

        obj.layout(ctx);

        Size size = obj.getSize();
        // number-of-children * (pad + label + pad)
        assertEquals(2 * (8 + ((42 * 5) + 42 / 2) + 8), size.width);
        // 2 * (pad + label + pad) + content
        assertEquals(2 * (10 + 8 + 10) + 20, size.height);
    }

    @Test
    public void calculatesSizeWithLargeChildren()
    {
        RenderContext ctx = new RenderContext(20, 10, (s) -> s.length() * 5, (s) -> 8);

        MockRenderPart child0 = new MockRenderPart();
        MockRenderPart child1 = new MockRenderPart();

        AlternativesRenderPart obj = new AlternativesRenderPart(null, "label",
                Arrays.asList("L", "R"), Arrays.asList(child0, child1));

        obj.layout(ctx);

        Size size = obj.getSize();
        // max(child0.width, child1.width) for both children
        assertEquals(134, size.width);
        // 2 * (pad + label + pad) + max(child0.height, child1.height)
        assertEquals(2 * (10 + 8 + 10) + 20, size.height);
    }

    @Test
    public void rendersBackground()
    {
        RenderContext ctx = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);
        MockRenderAdapter adapter = new MockRenderAdapter(ctx);

        MockRenderPart child0 = new MockRenderPart();
        MockRenderPart child1 = new MockRenderPart();

        AlternativesRenderPart obj = new AlternativesRenderPart(null, "condition",
                Arrays.asList("left", "right"), Arrays.asList(child0, child1));
        obj.setBackground(new RenderColor(0xFF, 0, 0));
        obj.layout(ctx);

        obj.render(adapter, 0, 0, obj.getSize().width);

        assertTrue(adapter.fillRectCalled);
    }

    @Test
    public void rendersChildren()
    {
        RenderContext ctx = new RenderContext(20, 10, (s) -> s.length() * 5, (s) -> 8);
        MockRenderAdapter adapter = new MockRenderAdapter(ctx);

        MockRenderPart child0 = new MockRenderPart();
        MockRenderPart child1 = new MockRenderPart();

        AlternativesRenderPart obj = new AlternativesRenderPart(null, "condition",
                Arrays.asList("left", "right"), Arrays.asList(child0, child1));
        obj.layout(ctx);

        obj.render(adapter, 0, 0, obj.getSize().width);

        assertTrue(child0.renderCalled);
        assertTrue(child1.renderCalled);

        assertEquals(89, child1.renderX);
        assertEquals(child1.renderY, child0.renderY);
        assertEquals(89, child0.renderW);
        assertEquals(89, child1.renderW);
    }
}
