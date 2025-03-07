package nsdlib.rendering.renderer;

import nsdlib.elements.NSDElement;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.RenderPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NSDRendererTest
{
    private static class NSDRendererMock extends NSDRenderer<Object>
    {
        private RenderContext contextToUse;
        private Object resultToUse;

        @Override
        public RenderContext createContext()
        {
            return contextToUse;
        }

        @Override
        public RenderAdapter<Object> createAdapter(RenderContext context, int width, int height, double scale)
        {
            return new RenderAdapter<Object>(context) {
                @Override
                public Object finish()
                {
                    return resultToUse;
                }

                @Override
                public void fillRect(int x, int y, int w, int h, RenderColor col)
                {
                }

                @Override
                public void fillPolygon(int[] x, int[] y, int n, RenderColor col) {

                }

                @Override
                protected void drawStringAt(String s, int x, int y)
                {
                }

                @Override
                public void drawRect(int x, int y, int w, int h)
                {
                }

                @Override
                public void drawPolygon(int[] x, int[] y, int n) {
                }

                @Override
                public void drawLine(int x1, int y1, int x2, int y2)
                {
                }
            };
        }
    }

    private static class RenderPartMock extends RenderPart
    {
        private Size sizeToUse;
        private boolean layoutCalled;
        private boolean renderCalled;
        private RenderAdapter<?> renderAdapter;
        private int renderX, renderY, renderW;

        @Override
        public void layout(RenderContext ctx)
        {
            this.layoutCalled = true;
        }

        @Override
        public Size getSize()
        {
            if (!layoutCalled) {
                fail("getSize() called before layout()");
            }
            return sizeToUse;
        }

        @Override
        public void setSize(Size s) {
            this.sizeToUse = s;
        }

        @Override
        public void render(RenderAdapter<?> adapter, int x, int y, int w)
        {
            this.renderCalled = true;
            this.renderAdapter = adapter;
            this.renderX = x;
            this.renderY = y;
            this.renderW = w;
        }
    }

    private static class NSDElementMock extends NSDElement
    {
        private RenderPartMock renderPartToUse;

        public NSDElementMock()
        {
            super(null, "mock");
        }

        @Override
        public RenderPart toRenderPart()
        {
            return renderPartToUse;
        }
    }

    @Test
    public void callsLayout()
    {
        NSDRendererMock obj = new NSDRendererMock();
        obj.contextToUse = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);

        RenderPartMock part = new RenderPartMock();
        part.sizeToUse = new Size(40, 16);

        obj.render(part, 1);

        assertTrue(part.layoutCalled);
    }

    @Test
    public void callsRenderWithCorrectArguments()
    {
        NSDRendererMock obj = new NSDRendererMock();
        obj.contextToUse = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);

        RenderPartMock part = new RenderPartMock();
        part.sizeToUse = new Size(40, 16);

        obj.render(part, 1);

        assertTrue(part.renderCalled);
        assertNotNull(part.renderAdapter);
        assertEquals(0, part.renderX);
        assertEquals(0, part.renderY);
        assertEquals(40, part.renderW);
    }

    @Test
    public void returnsAdapterFinishResult()
    {
        NSDRendererMock obj = new NSDRendererMock();
        obj.contextToUse = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);
        obj.resultToUse = new Object();

        RenderPartMock part = new RenderPartMock();
        part.sizeToUse = new Size(40, 16);

        assertSame(obj.resultToUse, obj.render(part, 1));
    }

    @Test
    public void convertsElementToRenderPart()
    {
        NSDRendererMock obj = new NSDRendererMock();
        obj.contextToUse = new RenderContext(8, 10, (s) -> s.length() * 5, (s) -> 8);

        RenderPartMock part = new RenderPartMock();
        part.sizeToUse = new Size(40, 16);

        NSDElementMock element = new NSDElementMock();
        element.renderPartToUse = part;

        obj.render(element, 1);

        assertTrue(part.renderCalled);
    }
}
