package masc.operator.flexible;

import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.utils.file.CustomFileReader;
import org.junit.Test;


import java.io.IOException;

import static org.junit.Assert.assertEquals;

//import static org.junit.Assert.*;

public class BuilderMASCClassTest {

    final static String resource_path = "src/test/resources/operator.generation/";

    //BareBone_X509TrustManagerCanBypassExt
    //BareBone_X509TrustManagerCanBypass

    @Test
    public void getClassBody() throws IOException {
        String expectedOutput = BuilderMainClass
                .getClassBody("BareBone").build().toString();
        String output = CustomFileReader
                .readFileAsString(resource_path + "MainClassBuilder.getClassBodyBasic.txt");

        assertEquals(expectedOutput, output);

    }

    @Test
    public void getClassBodyFinal() throws IOException {
        String expectedOutput = BuilderMainClass
                .getClassBody("BareBone", true).build().toString();
        String output = CustomFileReader
                .readFileAsString(resource_path + "MainClassBuilder.getClassBodyFinal.txt");

        assertEquals(expectedOutput, output);
    }

    @Test
    public void getClassBodyFalseFinal() throws IOException {
        String expectedOutput = BuilderMainClass
                .getClassBody("BareBone", false).build().toString();
        String output = CustomFileReader
                .readFileAsString(resource_path + "MainClassBuilder.getClassBodyBasic.txt");

        assertEquals(expectedOutput, output);
    }
}