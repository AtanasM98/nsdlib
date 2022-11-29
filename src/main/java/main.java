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

        NSDDecision nsdDec2 = new NSDDecision("count < 10");
        nsdDec2.getThen().addChild(new NSDInstruction("count + 10"));
        NSDDecision nsdDecNested2 = new NSDDecision("count > 10");
        NSDTestLastLoop tll2 = new NSDTestLastLoop("Do while count < 20");
        tll2.addChild(new NSDInstruction("count + 1"));
        nsdDecNested2.getThen().addChild(tll2);
        nsdDecNested2.getElse().addChild(new NSDInstruction("count"));
        nsdDec2.getElse().addChild(nsdDecNested2);

        diagram.addChild(nsdDec2);

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

        diagram.addChild(new NSDInstruction("numberList = [0, 1, 5, 4]"));
        diagram.addChild(new NSDInstruction("numberF = 4"));

        NSDTestFirstLoop loop = new NSDTestFirstLoop("numberList.toList.map");
        loop.addChild(new NSDInstruction("it.hasValue"));
        loop.addChild(new NSDInstruction("it.oneOf[]"));
        diagram.addChild(loop);

        NSDTestFirstLoop loop2 = new NSDTestFirstLoop("numberList.toList.foreach");
        loop2.addChild(new NSDInstruction("it"));
        diagram.addChild(loop2);

        NSDTestFirstLoop loop3 = new NSDTestFirstLoop("foreach entry in numberList");
        loop3.addChild(new NSDInstruction("count + 1"));
        diagram.addChild(loop3);

        NSDTestLastLoop loop4 = new NSDTestLastLoop("Do while count < 10");
        loop4.addChild(new NSDInstruction("count + 5"));
        diagram.addChild(loop4);

        NSDTestFirstLoop loop5 = new NSDTestFirstLoop("While count < 100");
        loop5.addChild(new NSDInstruction("count + 50"));
        diagram.addChild(loop5);

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
