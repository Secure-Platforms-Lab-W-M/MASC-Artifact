package edu.wm.cs.masc.pluginArchitecture;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.SafeReplaceWithUnsafe;
import edu.wm.cs.masc.mutation.properties.*;
import edu.wm.cs.masc.plugins.MutationMakerForPluginOperators;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import sun.tools.jstat.Operator;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PluginArchitectureTest {
    @Test
    public void pluginStringOperator() throws ConfigurationException {
        String propName = "Cipher.properties";
        String expected = "javax.crypto.Cipher.getInstance(\"aes\");";

        String path = "src/test/resources/" + propName;
        StringOperatorProperties p = new StringOperatorProperties(path);
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path, "src/test/resources/");
        pluginOperatorsMutationMaker.populateOperators(p);

        assertEquals(1, pluginOperatorsMutationMaker.operators.size());
        String obtainedResult = pluginOperatorsMutationMaker.operators.get(0).mutation();
        assertEquals(expected, obtainedResult);
    }

    @Test
    public void pluginIntOperator() throws ConfigurationException {
        String propName = "IntOperator.properties";
        String expected = "byte[] salt = {80,45,56};\n" +
                "javax.crypto.spec.PBEKeySpec(\"very_secure\".toCharArray(), salt, 30 + 20);";

        String path = "src/test/resources/" + propName;
        AOperatorProperties p = new IntOperatorProperties(path);
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path, "src/test/resources/");
        pluginOperatorsMutationMaker.populateOperators(p);

        assertEquals(2, pluginOperatorsMutationMaker.operators.size());

        String obtainedResult = pluginOperatorsMutationMaker.operators.get(0).mutation().replace("%d", "");
        assertEquals(expected, obtainedResult);
    }

    @Test
    public void pluginByteOperator() throws ConfigurationException {
        String propName = "ByteOperator.properties";
        String expected = "String cryptoTemp=\"\";\n" +
                "for(int i = 65; i < 75; i++){\n" +
                "    cryptoTemp += (char) i;\n" +
                "}\n" +
                "javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);";

        String path = "src/test/resources/" + propName;
        AOperatorProperties p = new ByteOperatorProperties(path);
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path, "src/test/resources/");
        pluginOperatorsMutationMaker.populateOperators(p);

        assertEquals(1, pluginOperatorsMutationMaker.operators.size());

        String obtainedResult = pluginOperatorsMutationMaker.operators.get(0).mutation().replace("%d", "");
        assertEquals(expected, obtainedResult);
    }

    @Test
    public void pluginInterProcOperator() throws ConfigurationException {
        String propName = "InterprocCipherTryCatch.properties";
        String expected = "try {\n" +
                "javax.crypto.Cipher cryptoVariable = javax.crypto.Cipher.getInstance(new CipherPack().A().B().getpropertyName());\n" +
                "System.out.println(cryptoVariable.getAlgorithm());\n" +
                "} catch (Exception e) {\n" +
                "System.out.println(\"Error\");\n" +
                "}";

        String path = "src/test/resources/" + propName;
        AOperatorProperties p = new InterprocProperties(path);
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path, "src/test/resources/");
        pluginOperatorsMutationMaker.populateOperators(p);

        assertEquals(1, pluginOperatorsMutationMaker.operators.size());

        String obtainedResult = pluginOperatorsMutationMaker.operators.get(0).mutation().replace("%d", "");
        assertEquals(expected, obtainedResult);
    }

    @Test
    public void pluginFlexibleOperator() throws ConfigurationException {
        String propName = "X509TrustManager.properties";
        String expected = "\n" +
                "new CryptoTestExt(){\n" +
                "public java.security.cert.X509Certificate[] getAcceptedIssuers() {\n" +
                "\treturn null;\n" +
                "}\n" +
                "\n" +
                "public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {\n" +
                "}\n" +
                "\n" +
                "public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {\n" +
                "}\n" +
                "\n" +
                "\n" +
                "};";

        String path = "src/test/resources/" + propName;
        AOperatorProperties p = new FlexibleOperatorProperties(path);
        MutationMakerForPluginOperators pluginOperatorsMutationMaker = new MutationMakerForPluginOperators(path, "src/test/resources/");
        pluginOperatorsMutationMaker.populateOperators(p);

        assertEquals(1, pluginOperatorsMutationMaker.operators.size());

        String obtainedResult = pluginOperatorsMutationMaker.operators.get(0).mutation().replace("%d", "");
        assertEquals(expected, obtainedResult);
    }



}
