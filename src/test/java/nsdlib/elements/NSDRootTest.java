package nsdlib.elements;

import java.util.Arrays;

import nsdlib.rendering.parts.RenderPart;
import nsdlib.rendering.parts.RootRenderPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NSDRootTest
{
    private static final NSDContainer<NSDElement> child0 = new NSDContainer<>(null, "c0");
    private static final NSDContainer<NSDElement> child1 = new NSDContainer<>(null, "c1");

    @Test
    public void addsChildrenGivenToConstructor()
    {
        NSDRoot obj = new NSDRoot(null, "foo");
        assertEquals(0, obj.countChildren());

        obj = new NSDRoot(null, "foo", Arrays.asList(child0, child1));
        assertEquals(2, obj.countChildren());
    }

    @Test
    public void convertsToRootRenderPart()
    {
        NSDRoot obj = new NSDRoot(null, "foo", Arrays.asList(child0, child1));
        RenderPart part = obj.toRenderPart();

        assertTrue(part instanceof RootRenderPart);
    }
}
