import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDInstruction;
import nsdlib.elements.NSDRoot;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.elements.loops.NSDForever;
import nsdlib.elements.loops.NSDTestFirstLoop;
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
        NSDRoot diagram = new NSDRoot("CountToTen");

        diagram.addChild(new NSDInstruction("count = 10"));
        diagram.addChild(new NSDInstruction("min(10, count)"));

        NSDDecision nsdDec = new NSDDecision("count < 10");
        nsdDec.getThen().addChild(new NSDInstruction("count + 10"));
        NSDDecision nsdDecNested = new NSDDecision("count > 10");
        nsdDecNested.getThen().addChild(new NSDInstruction("count + 1"));
        nsdDecNested.getElse().addChild(new NSDInstruction("count"));
        nsdDec.getElse().addChild(nsdDecNested);

        diagram.addChild(nsdDec);

        diagram.addChild(new NSDInstruction("\"\""));

        NSDCase altCase = new NSDCase("alt");
        NSDContainer cont1 = new NSDContainer("count > 10");
        cont1.addChild(new NSDInstruction("count + 1"));
        altCase.addChild(cont1);
        NSDContainer cont2 = new NSDContainer("count < 10");
        cont2.addChild(new NSDInstruction("count - 1"));
        altCase.addChild(cont2);
        NSDContainer cont3 = new NSDContainer("count == 10");
        cont3.addChild(new NSDInstruction("count"));
        altCase.addChild(cont3);
        NSDContainer cont4 = new NSDContainer("count == 5");
        cont4.addChild(new NSDInstruction("count"));
        altCase.addChild(cont4);
        diagram.addChild(altCase);

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
