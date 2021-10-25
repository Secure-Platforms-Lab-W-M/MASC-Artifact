package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
import masc.edu.wm.cs.masc.utility.CustomFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AIOSpecificTest {
    FlexibleOperatorProperties p;
    final static String resource_path = "src/test/resources/operator/flexible/";
    @Before
    public void setUp() throws Exception {
        p = new FlexibleOperatorProperties(
                "src/test/resources/properties/X509TrustManager.properties");
    }

    @Test
    public void mutation() throws IOException {
        String expected = CustomFileReader.readFileAsString(
                resource_path +
                        "outputs/AIOSpecific_output.txt");
        assertEquals(expected, new AIOSpecific(p).mutation());
    }
}