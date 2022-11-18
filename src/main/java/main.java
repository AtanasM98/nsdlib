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
        NSDRoot diagram = new NSDRoot("When start clicked");

        NSDDecision nsdDec = new NSDDecision("index");
        nsdDec.getThen().addChild(new NSDInstruction("test green"));
        nsdDec.getThen().addChild(new NSDInstruction("test green two"));
        nsdDec.getElse().addChild(new NSDInstruction("no green"));

        diagram.addChild(nsdDec);

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
