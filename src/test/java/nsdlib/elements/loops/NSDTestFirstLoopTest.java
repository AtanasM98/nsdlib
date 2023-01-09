package nsdlib.elements.loops;

import java.util.Arrays;

import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDElement;
import nsdlib.rendering.parts.BraceRenderPart;
import nsdlib.rendering.parts.RenderPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NSDTestFirstLoopTest
{
    private static final NSDContainer<NSDElement> child0 = new NSDContainer<>(null, "c0");
    private static final NSDContainer<NSDElement> child1 = new NSDContainer<>(null, "c1");

    @Test
    public void addsChildrenGivenToConstructor()
    {
        NSDTestFirstLoop obj = new NSDTestFirstLoop(null, "foo");
        assertEquals(0, obj.countChildren());

        obj = new NSDTestFirstLoop(null, "foo", Arrays.asList(child0, child1));
        assertEquals(2, obj.countChildren());
    }

    @Test
    public void convertsToBraceRenderPart()
    {
        NSDTestFirstLoop obj = new NSDTestFirstLoop(null, "foo", Arrays.asList(child0, child1));
        RenderPart part = obj.toRenderPart();

        assertTrue(part instanceof BraceRenderPart);
    }
}
