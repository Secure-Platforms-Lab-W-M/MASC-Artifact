package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public abstract class AIntOperatorTest {

    IntOperatorProperties p;
    String expected;
    String template;

    @Test
    public void absoluteValue() {
        expected = String.format(template, "Math.abs(50)");
        assertEquals(expected, new AbsoluteValue(p).mutation().replace("%d", ""));
    }

    @Test
    public void arithmetic() {
        int term1 = 30;
        int term2 = 50 - term1;
        expected = String.format(template, term1 + " + " + term2);
        assertEquals(expected, new Arithmetic(p).mutation().replace("%d", ""));
    }

    @Test
    public void fromString() {
        expected = String.format(template, "Integer.parseInt(\"50\")");
        assertEquals(expected, new FromString(p).mutation().replace("%d", ""));
    }

    @Test
    public void iterationMultipleCall(){
        expected = "for (int i = 0; i < 50; i++){\n\t";
        expected += String.format(template, "i").replace("\n", "\n\t");
        expected += "\n}";
        assertEquals(expected, new IterationMultipleCall(p).mutation().replace("%d", ""));
    }

    @Test
    public void nestedClass(){
        expected = "class NestedClass{\n";
        expected += "\tint getIteration(){\n";
        expected += "\t\treturn 50;\n";
        expected += "\t}\n}\n";
        expected += String.format(template, "new NestedClass().getIteration()");
        assertEquals(expected, new NestedClass(p).mutation().replace("%d", ""));
    }

    @Test
    public void roundValue(){
        expected = String.format(template, "Math.round(50)");
        assertEquals(expected, new RoundValue(p).mutation().replace("%d", ""));
    }

    @Test
    public void valueInVariable(){
        expected = "int iterCount = 50;\n";
        expected += String.format(template, "iterCount");
        assertEquals(expected, new ValueInVariable(p).mutation().replace("%d", ""));
    }

    @Test
    public void valueInVariableArithmetic() {

        int term1 = 30;
        int term2 = 50 - term1;

        expected = "int iterCount = " + term1 + ";\n";
        expected += String.format(template, "iterCount + " + term2);

        assertEquals(expected, new ValueInVariableArithmetic(p).mutation().replace("%d", ""));
    }

    @Test
    public void whileLoopAccumulation() {
        expected = "int i = 0;\n";
        expected += "while (i < 50){\n";
        expected += "\ti++;\n";
        expected += "}\n";
        expected += String.format(template, "i");
        assertEquals(expected, new WhileLoopAccumulation(p).mutation().replace("%d", ""));
    }

    @Test
    public void overflow() {
        expected = String.format(template, "Integer.MAX_VALUE + Integer.MAX_VALUE + 2 + 50");
        assertEquals(expected, new Overflow(p).mutation().replace("%d", ""));
    }
}
