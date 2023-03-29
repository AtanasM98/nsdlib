import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.pathAnalyzer.NSDPath;
import nsdlib.pathAnalyzer.NSDPathAnalyzer;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.parts.RootRenderPart;
import nsdlib.rendering.renderer.awt.AwtRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static dreamex.mainMEMX.buildMainMEMXMinimumExecutionQuantity;

public class main {
    public static void main(String[] args) {
        NSDRoot diagramSimple = new NSDRoot("","Minimum Execution Quantity");

        NSDCase testCase = new NSDCase("", "Testing case");
        NSDContainer<NSDElement> test1 = new NSDContainer<>("Case 1");
        NSDContainer<NSDElement> test2 = new NSDContainer<>("Case 2");
        NSDContainer<NSDElement> test3 = new NSDContainer<>("Case 3");
        NSDContainer<NSDElement> test4 = new NSDContainer<>("Case 4");
        testCase.addChild(test1);
        testCase.addChild(test2);
        testCase.addChild(test3);
        testCase.addChild(test4);
        test1.addChild(new NSDInstruction("", "Test instruction 1"));
        test2.addChild(new NSDInstruction("", "Test instruction 2"));
        test3.addChild(new NSDInstruction("", "Test instruction 3"));
        test4.addChild(new NSDInstruction("", "Test instruction 4"));

        NSDDecision testDecision = new NSDDecision("", "Check");
        testDecision.getThen().addChild(new NSDInstruction("", "Check 1"));
        testDecision.getElse().addChild(new NSDInstruction("", "Check 2"));

        test1.addChild(testDecision);

        NSDCase testCase2 = new NSDCase("", "Testing case 2");
        NSDContainer<NSDElement> test5 = new NSDContainer<>("Case 5");
        NSDContainer<NSDElement> test6 = new NSDContainer<>("Case 6");
        NSDContainer<NSDElement> test7 = new NSDContainer<>("Case 7");

        testCase2.addChild(test5);
        testCase2.addChild(test6);
        testCase2.addChild(test7);
        test2.addChild(testCase2);

        test5.addChild(new NSDInstruction("","Test instruction 5"));
        test6.addChild(new NSDInstruction("","Test instruction 6"));
        test7.addChild(new NSDExit("Return"));

        diagramSimple.addChild(testCase);
        diagramSimple.addChild(new NSDExit("Return"));


        NSDRoot diagram = buildMainMEMXMinimumExecutionQuantity();
        diagramSimple.toRenderPart().setBackground(RenderColor.WHITE);

        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagramSimple);
        pathAnalyzer.analyzePaths();

        saveToImage(buildImage(diagramSimple), "image.png");
        int pathCounter = 0;
        for (NSDPath path: pathAnalyzer.getPaths()) {
            path.showPath(RenderColor.PATH);

            saveToImage(buildImage(diagramSimple), "paths/image" + pathCounter++ + ".png");
            path.hidePath();
        }
    }

    public static BufferedImage buildImage(NSDRoot diagram) {
        AwtRenderer renderer = new AwtRenderer();
        // 1. convert (`diagram` is an instance of `NSDRoot`)
        RootRenderPart part = (RootRenderPart) diagram.toRenderPart();
        // 2. layout
        part.layout(renderer.createContext());
        // 3. render
        return renderer.render(part,1);
    }

    public static void saveToImage(BufferedImage img, String pathname) {
        File outputFile = new File(pathname);
        try {
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
