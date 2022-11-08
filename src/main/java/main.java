import nsdlib.elements.NSDContainer;
import nsdlib.elements.NSDInstruction;
import nsdlib.elements.NSDRoot;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.elements.loops.NSDForever;
import nsdlib.elements.loops.NSDTestFirstLoop;
import nsdlib.rendering.parts.RenderPart;
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

        NSDDecision dec = new NSDDecision("(pick random 1 to 10) = 1");
        {
            dec.getThen().addChild(new NSDInstruction("say \"You're lucky!\""));
            dec.getElse().addChild(new NSDInstruction("say \"Not so lucky.\""));
        }
        diagram.addChild(dec);

        NSDForever forever = new NSDForever(Arrays.asList(
                new NSDInstruction("wait 1 secs")
        ));
        diagram.addChild(forever);

        NSDTestFirstLoop firstLoop = new NSDTestFirstLoop("If bla", Arrays.asList(new NSDInstruction("Jump")));

        diagram.addChild(firstLoop);

        NSDCase nsdCase = new NSDCase("Case ble");
        nsdCase.addChild(new NSDContainer<>("Jump", Arrays.asList(new NSDInstruction("Scream"), new NSDInstruction("Shout"))));
        nsdCase.addChild(new NSDContainer<>("Yell", Arrays.asList(new NSDInstruction("Slap"), new NSDInstruction("Scratch"))));
        nsdCase.addChild(new NSDContainer<>("Crouch", Arrays.asList(new NSDInstruction("Lie down"), new NSDInstruction("Get up"))));

        diagram.addChild(nsdCase);

        AwtRenderer renderer = new AwtRenderer();

        // 1. convert (`diagram` is an instance of `NSDRoot`)
        RenderPart part = diagram.toRenderPart();

        // 2. layout
        part.layout(renderer.createContext());
        // optional: get the resulting size

        // Size s = part.getSize();

        // 3. render
        BufferedImage img = renderer.render(part,50);

        File outputFile = new File("image.png");
        try {
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
