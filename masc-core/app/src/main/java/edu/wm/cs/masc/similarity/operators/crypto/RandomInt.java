package edu.wm.cs.masc.similarity.operators.crypto;

public class RandomInt extends ACryptoMutationOperator {
    @Override
    protected String getMutatedLine() {
        String mutatedLine = "byte[] cIpherBytes = \"Seed\".getBytes(StandardCharsets.UTF_8);\n";
        mutatedLine+= "int tHreadLocalRandom1 = ThreadLocalRandom.current().nextInt();\n";
        mutatedLine+= "SecureRandom seCureRandom1 = new SecureRandom();\n";
        mutatedLine+= "seCureRandom1.setSeed(cIpherBytes);\n";
        mutatedLine+= "int secUreRandomint = seCureRandom1.nextInt();\n";
        return mutatedLine;
    }

    @Override
    protected String packageLines() {
        return "import java.nio.charset.StandardCharsets;\n" +
                "import java.security.SecureRandom;\n" +
                "import java.util.Random;\n" +
                "import java.util.concurrent.ThreadLocalRandom;";
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
