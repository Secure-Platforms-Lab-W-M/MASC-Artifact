package edu.wm.cs.masc.similarity.operators.crypto;

public class MessageDigest extends ACryptoMutationOperator{
    @Override
    protected String getMutatedLine() {
        return "MessageDigest cryptoDigest;\n" +
                "        try {\n" +
                "            cryptoDigest = MessageDigest.getInstance(\"SHA-256\".replace(\"SHA-256\", \"md5\"));\n" +
                "            System.out.println(cryptoDigest.getAlgorithm());\n" +
                "\n" +
                "        } catch (NoSuchAlgorithmException e) {\n" +
                "            System.out.println(\"Error\");\n" +
                "        }";
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
