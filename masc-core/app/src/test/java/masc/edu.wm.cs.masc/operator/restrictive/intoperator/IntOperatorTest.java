package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import org.junit.Before;
import org.junit.Test;

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
        String[] mutation = new Arithmetic(p).mutation().split("\n");
        Pattern p = Pattern.compile("javax.crypto.spec.PBEKeySpec\\(\"very_secure\", salt, \\-?(\\d)+ \\+ (\\d)+\\);");
        Matcher m = p.matcher(mutation[1]);
        assertEquals(expected, mutation[0]+"\n");
        assertTrue(m.matches());
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
        String[] mutation = new ValueInVariableArithmetic(p).mutation().split("\n");
        Pattern varPattern = Pattern.compile("int iterCount = \\-?(\\d)+;");
        Pattern apiPattern = Pattern.compile("javax.crypto.spec.PBEKeySpec\\(\"very_secure\", salt, iterCount \\+ (\\d)+\\);");
        Matcher m1 = varPattern.matcher(mutation[1]);
        Matcher m2 = apiPattern.matcher(mutation[2]);
        assertEquals(expected, mutation[0]+"\n");
        assertTrue(m1.matches());
        assertTrue(m2.matches());
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
