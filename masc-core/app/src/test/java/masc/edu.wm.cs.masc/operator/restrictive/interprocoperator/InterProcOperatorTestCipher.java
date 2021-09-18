package masc.edu.wm.cs.masc.operator.restrictive.interprocoperator;

import masc.edu.wm.cs.masc.operator.restrictive.interprocoperator.InterProcOperator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InterProcOperatorTestCipher {

//    InterProcOperator operator;
//    ArrayList<String> imports;
//
//    @Before
//    public void setup() throws Exception {
//        operator = new InterProcOperator(null,
//                "Cipher",
//                "getInstance",
//                "cryptoVariable",
//                "CipherExample",
//                "cipherName",
//                "AES/GCM/NoPadding",
//                "DES"
//        );
//    }
//
//    @Test
//    public void classBody() {
//        String expected = "public class CipherExample {\n" +
//                "    private String cipherName = \"AES/GCM/NoPadding\";\n" +
//                "    public CipherExample A(){\n" +
//                "        cipherName = \"AES/GCM/NoPadding\";\n" +
//                "        return this;\n" +
//                "    }\n" +
//                "    public CipherExample B(){\n" +
//                "        cipherName = \"DES\";\n" +
//                "        return this;\n" +
//                "    }\n" +
//                "    public String getcipherName(){\n" +
//                "        return cipherName;\n" +
//                "    }\n" +
//                "}";
//        assertEquals(expected, operator.classBody());
//    }
//
//    @Test
//    public void insecure_call() {
//        String expected = "Cipher cryptoVariable = Cipher.getInstance(new " +
//                "CipherExample().A().B().getcipherName());\n";
//        assertEquals(expected, operator.insecure_call());
//
//    }
//
//    @Test
//    public void insecure_call_trycatch() {
//        String expected = "try {\n" +
//                "Cipher cryptoVariable = Cipher.getInstance(new " +
//                "CipherExample().A().B().getcipherName());\n" +
//                "System.out.println(cryptoVariable.getAlgorithm());\n" +
//                "} catch (Exception e) {\n" +
//                "System.out.println(\"Error\");\n" +
//                "}";
//        assertEquals(expected, operator.insecure_call_trycatch());
//    }
}