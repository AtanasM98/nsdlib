import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDInstruction;
import nsdlib.elements.NSDRoot;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.elements.loops.NSDForever;
import nsdlib.elements.loops.NSDTestFirstLoop;
import nsdlib.elements.loops.NSDTestLastLoop;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.AlternativesRenderPart;
import nsdlib.rendering.parts.ContainerRenderPart;
import nsdlib.rendering.parts.RenderPart;
import nsdlib.rendering.parts.RootRenderPart;
import nsdlib.rendering.renderer.awt.AwtRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        NSDRoot diagram = new NSDRoot("InsertOrder");

        NSDDecision nsdDec = new NSDDecision("order.timestamp <= 0");
        nsdDec.getThen().addChild(new NSDInstruction("count = 0"));
        NSDTestFirstLoop nsdTFL = new NSDTestFirstLoop("orderB.orders.foreach");
        NSDDecision nsdTflDec = new NSDDecision("order.timestamp < it.timestamp || order.amount < it.timestamp");
        nsdTflDec.getThen().addChild(new NSDInstruction("count + 1"));
        nsdTFL.addChild(nsdTflDec);
        nsdDec.getThen().addChild(nsdTFL);
        diagram.addChild(nsdDec);
        diagram.addChild(new NSDInstruction("#OrderBook(orderB.orders.insert(count, order)}"));

        AwtRenderer renderer = new AwtRenderer();

        // 1. convert (`diagram` is an instance of `NSDRoot`)
        RenderPart part = diagram.toRenderPart();

        // 2. layout
        part.layout(renderer.createContext());
        // optional: get the resulting size
        // Size s = part.getSize();

        // 3. render
        BufferedImage img = renderer.render(part,1);

        File outputFile = new File("image.png");
        try {
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
