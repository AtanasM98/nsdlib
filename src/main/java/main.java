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
        NSDRoot diagram = new NSDRoot("","InsertOrder");

        NSDCase nsdCase = new NSDCase("","alt");
        NSDContainer caseCont = new NSDContainer<>("");
        NSDContainer caseCont1 = new NSDContainer("count < 10");
        caseCont1.addChild(new NSDInstruction("", "count + 1"));
        NSDContainer caseCont2 = new NSDContainer("count > 10");
        caseCont2.addChild(new NSDInstruction("", "count + 2"));
        NSDContainer caseCont3 = new NSDContainer("count == 10");
        caseCont3.addChild(new NSDInstruction("", "count + 3"));

        nsdCase.addChild(caseCont1);
        nsdCase.addChild(caseCont2);
        nsdCase.addChild(caseCont3);
        diagram.addChild(nsdCase);

        NSDDecision nsdDec = new NSDDecision("", "count < 100");
        nsdDec.getThen().addChild(new NSDInstruction("", "count + 4"));
        nsdDec.getElse().addChild(new NSDInstruction("", "count + 5"));
        diagram.addChild(nsdDec);

        AwtRenderer renderer = new AwtRenderer();

        // 1. convert (`diagram` is an instance of `NSDRoot`)
        RootRenderPart part = (RootRenderPart) diagram.toRenderPart();
        // 2. layout
        part.layout(renderer.createContext());
        // optional: get the resulting size
        // Size s = part.getSize();

        AlternativesRenderPart caseRPart = (AlternativesRenderPart) part.getContent().getChildren().get(0);
        caseRPart.setBackground(new RenderColor(11, 25, 123));
        caseRPart.setBackgroundCase(RenderColor.GREEN, 0);
        caseRPart.setBackgroundCase(RenderColor.RED, 1);
        caseRPart.setBackgroundCase(new RenderColor(123, 11, 25), 2);
        caseRPart.setBackgroundChild(RenderColor.RED, 0);
        caseRPart.setBackgroundChild(RenderColor.GREEN, 1);
        caseRPart.setBackgroundChild(new RenderColor(25, 123, 11), 2);

        AlternativesRenderPart caseRPart1 = (AlternativesRenderPart) part.getContent().getChildren().get(1);
        caseRPart1.setBackground(new RenderColor(11, 25, 123));
        caseRPart1.setBackgroundCase(RenderColor.GREEN, 0);
        caseRPart1.setBackgroundCase(RenderColor.RED, 1);
        caseRPart1.setBackgroundChild(RenderColor.RED, 0);
        caseRPart1.setBackgroundChild(RenderColor.GREEN, 1);

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
