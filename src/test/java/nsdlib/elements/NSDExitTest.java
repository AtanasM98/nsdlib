package nsdlib.elements;

import nsdlib.rendering.parts.ExitRenderPart;
import nsdlib.rendering.parts.RenderPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NSDExitTest {    @Test
public void convertsToBoxRenderPart()
    {
        NSDExit obj = new NSDExit(null, "return");
        RenderPart part = obj.toRenderPart();

        assertTrue(part instanceof ExitRenderPart);
    }
}
