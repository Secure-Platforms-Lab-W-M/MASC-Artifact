package edu.wm.cs.mplus.operators.crypto;

public class SSLContextInstance extends ACryptoMutationOperator {

    @Override
    protected String getMutatedLine() {
        return "try {\n" +
                "   SSLContext cryptoContext = SSLContext.getInstance(\"SSL\");\n" +
                "   System.out.println(cryptoContext.getProtocol());\n" +
                "} catch (NoSuchAlgorithmException e) {\n" +
                "   System.out.println(\"Error\");\n" +
                "}";
    }

    @Override
    protected String packageLines() {
        return "import java.security.NoSuchAlgorithmException;\n";
    }

    @Override
    protected String getTemplatePath() {
        return null;
    }

    @Override
    protected String getTemplateBasedFileName() {
        return null;
    }
}
