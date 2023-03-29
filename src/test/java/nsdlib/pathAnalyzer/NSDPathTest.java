package nsdlib.pathAnalyzer;

import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.parts.AlternativesRenderPart;
import nsdlib.rendering.parts.RootRenderPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NSDPathTest {
    NSDRoot diagram = new NSDRoot("", "MockRoot");

    NSDCase testCase = new NSDCase("", "Testing case");
    NSDContainer<NSDElement> test1 = new NSDContainer<>("Case 1");
    NSDContainer<NSDElement> test2 = new NSDContainer<>("Case 2");
    NSDContainer<NSDElement> test3 = new NSDContainer<>("Case 3");
    NSDContainer<NSDElement> test4 = new NSDContainer<>("Case 4");
    NSDInstruction testI1 = new NSDInstruction("", "Test instruction 1");
    NSDInstruction testI2 = new NSDInstruction("", "Test instruction 2");
    NSDInstruction testI3 = new NSDInstruction("", "Test instruction 3");
    NSDInstruction testI4 = new NSDInstruction("", "Test instruction 4");
    NSDInstruction testI5 = new NSDInstruction("", "Test instruction 5");
    NSDInstruction testI6 = new NSDInstruction("", "Test instruction 6");
    NSDDecision testDecision = new NSDDecision("", "Check");
    NSDInstruction checkI1 = new NSDInstruction("", "Check 1");
    NSDInstruction checkI2 = new NSDInstruction("", "Check 2");
    NSDCase testCase2 = new NSDCase("", "Testing case 2");
    NSDContainer<NSDElement> test5 = new NSDContainer<>("Case 5");
    NSDContainer<NSDElement> test6 = new NSDContainer<>("Case 6");
    NSDContainer<NSDElement> test7 = new NSDContainer<>("Case 7");
    NSDExit return1 = new NSDExit("Return");
    NSDExit return2 = new NSDExit("Return");

    public NSDRoot buildMockDiagram()
    {
        testCase.addChild(test1);
        testCase.addChild(test2);
        testCase.addChild(test3);
        testCase.addChild(test4);
        test1.addChild(testI1);
        test2.addChild(testI2);
        test3.addChild(testI3);
        test4.addChild(testI4);

        testDecision.getElse().addChild(checkI2);
        testDecision.getThen().addChild(checkI1);

        test1.addChild(testDecision);

        testCase2.addChild(test5);
        testCase2.addChild(test6);
        testCase2.addChild(test7);

        test5.addChild(testI5);
        test6.addChild(testI6);
        test7.addChild(return1);
        test2.addChild(testCase2);

        diagram.addChild(testCase);
        diagram.addChild(return2);

        return diagram;
    }

    @Test
    public void showsPath()
    {
        NSDPath testPath = new NSDPath();
        testPath.addToPath(testCase);
        testPath.addToPath(testI1);
        testPath.addToPath(testDecision);
        testPath.addToPath(checkI1);
        testPath.addToPath(return2);
        testPath.setDecisionIndex(0, 0);
        testPath.setDecisionIndex(1, 0);

        testPath.showPath(RenderColor.GREEN);

        assertEquals(RenderColor.GREEN, testCase.toRenderPart().getBackground());
        assertEquals(RenderColor.GREEN,
                ((AlternativesRenderPart) testCase.toRenderPart()).getBackgroundCase(0));
        assertEquals(RenderColor.GREEN, testI1.toRenderPart().getBackground());
        assertEquals(RenderColor.GREEN, testDecision.toRenderPart().getBackground());
        assertEquals(RenderColor.GREEN, ((AlternativesRenderPart) testDecision.toRenderPart()).getBackgroundCase(0));
        assertEquals(RenderColor.GREEN, checkI1.toRenderPart().getBackground());
        assertEquals(RenderColor.GREEN, return2.toRenderPart().getBackground());
    }
}
