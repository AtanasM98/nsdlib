package nsdlib.pathAnalyzer;

import nsdlib.elements.*;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.elements.alternatives.NSDDecision;
import nsdlib.elements.loops.NSDTestFirstLoop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NSDPathAnalyzerTest {
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
    public void analyzesPaths()
    {
        NSDRoot diagram = buildMockDiagram();
        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagram);
        pathAnalyzer.analyzePaths();

        NSDPath pathTested1 = new NSDPath();
        pathTested1.addToPath(testCase);
        pathTested1.addToPath(testI1);
        pathTested1.addToPath(testDecision);
        pathTested1.addToPath(checkI1);
        pathTested1.addToPath(return2);
        pathTested1.setDecisionIndex(testCase, 0);
        pathTested1.setDecisionIndex(testDecision, 0);

        NSDPath pathTested2 = new NSDPath();
        pathTested2.addToPath(testCase);
        pathTested2.addToPath(testI2);
        pathTested2.addToPath(testCase2);
        pathTested2.addToPath(return1);
        pathTested2.setDecisionIndex(testCase, 1);
        pathTested2.setDecisionIndex(testCase2, 2);

        NSDPath pathTested3 = new NSDPath();
        pathTested3.addToPath(testCase);
        pathTested3.addToPath(testI3);
        pathTested3.addToPath(return2);
        pathTested3.setDecisionIndex(testCase, 2);

        assertEquals(pathTested1, pathAnalyzer.getPaths().get(0));
        assertEquals(pathTested2, pathAnalyzer.getPaths().get(4));
        assertEquals(pathTested3, pathAnalyzer.getPaths().get(5));
    }
    @Test
    public void analyzesTestFirstLoops() {
        NSDRoot diagram = new NSDRoot("", "diagram");
        NSDTestFirstLoop testFirstLoop = new NSDTestFirstLoop("", "Test loop");
        testFirstLoop.addChild(new NSDInstruction("", "Test Instruction 1"));
        diagram.addChild(testFirstLoop);
        diagram.addChild(new NSDInstruction("", "Test Instruction 2"));

        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagram);
        pathAnalyzer.analyzePaths();

        NSDPath path1 = new NSDPath();
        path1.addToPath(testFirstLoop);
        path1.addToPath(new NSDInstruction("", "Test Instruction 2"));

        NSDPath path2 = new NSDPath();
        path2.addToPath(testFirstLoop);
        path2.addToPath(new NSDInstruction("", "Test Instruction 1"));
        path2.addToPath(new NSDInstruction("", "Test Instruction 2"));

        assertEquals(path1, pathAnalyzer.getPaths().get(0));
        assertEquals(path2, pathAnalyzer.getPaths().get(1));
    }

    @Test
    public void analyzesEmptyDiagram() {
        NSDRoot diagram = new NSDRoot("", "");

        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagram);
        pathAnalyzer.analyzePaths();

        pathAnalyzer.getPaths();

        assertTrue(pathAnalyzer.getPaths().get(0).getPath().isEmpty());
    }

    @Test
    public void analyzesBrokenDiagram1() {
        NSDRoot diagram = new NSDRoot("", "");
        diagram.addChild(new NSDInstruction("", "instr 1"));

        NSDDecision dec = new NSDDecision("", "testDec");
        dec.getThen().addChild(new NSDInstruction("", "instr 2"));

        diagram.addChild(dec);

        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagram);
        pathAnalyzer.analyzePaths();

        pathAnalyzer.getPaths();

        assertEquals(2, pathAnalyzer.getPaths().size());

        assertEquals(dec, pathAnalyzer.getPaths().get(0).getPath().get(1));
        assertEquals(new NSDInstruction("", "instr 2"), pathAnalyzer.getPaths().get(0).getPath().get(2));
    }

    @Test
    public void analyzesBrokenDiagram2() {
        NSDRoot diagram = new NSDRoot("", "broken");
        diagram.addChild(new NSDExit("", "return"));

        NSDDecision dec = new NSDDecision("", "if after return");
        diagram.addChild(dec);
        dec.getThen().addChild(new NSDInstruction("", "then do that"));
        dec.getElse().addChild(new NSDInstruction("", "else do that"));

        NSDPathAnalyzer pathAnalyzer = new NSDPathAnalyzer(diagram);
        pathAnalyzer.analyzePaths();

        assertEquals(1, pathAnalyzer.getPaths().size());
        assertEquals(new NSDExit("", "return"), pathAnalyzer.getPaths().get(0).getPath().get(0));
    }
}
