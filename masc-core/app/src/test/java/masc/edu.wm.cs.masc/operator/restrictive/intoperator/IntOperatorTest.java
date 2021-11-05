package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import masc.edu.wm.cs.masc.utility.RandomGeneratorFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntOperatorTest {

    IntOperatorProperties p;
    String expected;

    @Before
    public void setUp() throws Exception {
        p = new IntOperatorProperties("src/test/resources/properties/IntOperator.properties");
        expected = "byte[] salt = {80,45,56};\n";
    }

    @Test
    public void absoluteValue() {
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, Math.abs(50));";
        assertEquals(expected, new AbsoluteValue(p).mutation());
    }

    @Test
    public void arithmetic() {
        Random gen = new RandomGeneratorFactory().getGenerator();
        int term1 = (int) (gen.nextDouble() * 2 * 50) - 50;
        int term2 = 50 - term1;
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, ";
        expected += term1 + " + "  + term2 + ");";
        assertEquals(expected, new Arithmetic(p).mutation());
    }

    @Test
    public void fromString() {
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, Integer.parseInt(\"50\"));";
        assertEquals(expected, new FromString(p).mutation());
    }

    @Test
    public void iterationMultipleCall(){
        expected += "for (int i = 0; i < 50; i++){\n\tjavax.crypto.spec.PBEKeySpec(\"very_secure\", salt, i);\n}";
        assertEquals(expected, new IterationMultipleCall(p).mutation());
    }

    @Test
    public void nestedClass(){
        expected += "class NestedClass{\n";
        expected += "\tint getIteration(){\n";
        expected += "\t\treturn 50;\n";
        expected += "\t}\n}\n";
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, new NestedClass().getIteration());";
        assertEquals(expected, new NestedClass(p).mutation());
    }

    @Test
    public void roundValue(){
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, Math.round(50));";
        assertEquals(expected, new RoundValue(p).mutation());
    }

    @Test
    public void squareThenRoot(){
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, Math.sqrt(Math.pow(50, 2)));";
        assertEquals(expected, new SquareThenRoot(p).mutation());
    }

    @Test
    public void valueInVariable(){
        expected += "int iterCount = 50;\n";
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, iterCount);";
        assertEquals(expected, new ValueInVariable(p).mutation());
    }

    @Test
    public void valueInVariableArithmetic() {
        Random gen = new RandomGeneratorFactory().getGenerator();
        int term1 = (int) (gen.nextDouble() * 2 * 50) - 50;
        int term2 = 50 - term1;

        expected += "int iterCount = " + term1 + ";\n";
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, iterCount + " + term2 + ");";

        assertEquals(expected, new ValueInVariableArithmetic(p).mutation());
    }

    @Test
    public void whileLoopAccumulation() {
        expected += "int i = 0;\n";
        expected += "while (i < 50){\n";
        expected += "\ti++;\n";
        expected += "}\n";
        expected += "javax.crypto.spec.PBEKeySpec(\"very_secure\", salt, i);";
        assertEquals(expected, new WhileLoopAccumulation(p).mutation());
    }
}
