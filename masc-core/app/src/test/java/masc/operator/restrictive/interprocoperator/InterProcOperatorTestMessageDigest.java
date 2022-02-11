package masc.operator.restrictive.interprocoperator;

public class InterProcOperatorTestMessageDigest {
//
//    InterProcOperator operator;
//    ArrayList<String> imports;
//    @Before
//    public void setup() throws Exception{
//        imports = new ArrayList<String>();
//        imports.add("import java.security.MessageDigest;");
//        imports.add("import java.security.NoSuchAlgorithmException;");
//
//        operator = new InterProcOperator(imports,
//                "MessageDigest",
//                "getInstance",
//                "cryptoVariable",
//                "MessageDigestComplex",
//                "digestName",
//                "SHA-256",
//                "MD5"
//                );
//    }
//
//    @Test
//    public void classBody() {
//        String expected = "import java.security.MessageDigest;\n" +
//                "import java.security.NoSuchAlgorithmException;\n" +
//                "public class MessageDigestComplex {\n" +
//                "    private String digestName = \"SHA-256\";\n" +
//                "    public MessageDigestComplex A(){\n" +
//                "        digestName = \"SHA-256\";\n" +
//                "        return this;\n" +
//                "    }\n" +
//                "    public MessageDigestComplex B(){\n" +
//                "        digestName = \"MD5\";\n" +
//                "        return this;\n" +
//                "    }\n" +
//                "    public String getdigestName(){\n" +
//                "        return digestName;\n" +
//                "    }\n" +
//                "}";
//        assertEquals(expected, operator.classBody());
//    }
//
//    @Test
//    public void insecure_call(){
//        String expected = "MessageDigest cryptoVariable = MessageDigest" +
//                ".getInstance(new MessageDigestComplex().A().B().getdigestName());\n";
//        assertEquals(expected,operator.insecure_call());
//    }
//
//    @Test
//    public void insecure_call_trycatch(){
//        String expected = "try {\n" +
//                "MessageDigest cryptoVariable = MessageDigest.getInstance(new " +
//                "MessageDigestComplex().A().B().getdigestName());\n" +
//                "System.out.println(cryptoVariable.getAlgorithm());\n" +
//                "} catch (Exception e) {\n" +
//                "System.out.println(\"Error\");\n" +
//                "}";
//        assertEquals(expected, operator.insecure_call_trycatch());
//    }
}