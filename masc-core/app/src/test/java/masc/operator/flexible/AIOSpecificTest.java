package masc.operator.flexible;

import edu.wm.cs.masc.mutation.operators.flexible.AIOSpecific;
import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
import edu.wm.cs.masc.utils.file.CustomFileReader;
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
                "src/main/resources/X509TrustManager.properties");
    }

    @Test
    public void mutation() throws IOException {
        String expected = CustomFileReader.readFileAsString(
                resource_path +
                        "outputs/AIOSpecific_output.txt");
        assertEquals(expected, new AIOSpecific(p).mutation());
    }
}