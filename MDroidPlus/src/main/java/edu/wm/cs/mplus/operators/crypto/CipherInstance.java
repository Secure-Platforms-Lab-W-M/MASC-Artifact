package edu.wm.cs.mplus.operators.crypto;

public class CipherInstance extends ACryptoMutationOperator {
    @Override
    protected String getMutatedLine() {
        
        String mutatedLine = "Cipher ciPHerExample1 = Cipher.getInstance(\"de$s\".replace(\"$\", \"\"));\n";
        mutatedLine += "Cipher ciPHerExample2 = Cipher.getInstance(\"des\".toUpperCase(Locale.ENGLISH));\n";
        mutatedLine += "Cipher ciPHerExample3 = Cipher.getInstance(\"des\");\n";
        mutatedLine += "Cipher ciPHerExample4 = Cipher.getInstance(\"AES\".replace(\"A\", \"D\"));\n";
        mutatedLine += "Cipher ciPHerExample5 = Cipher.getInstance(new CipherExample().methodA().methodB().getCipherName());\n";
        return mutatedLine;
    }

    @Override
    protected String packageLines() {
        return "import java.util.Locale;\n";
    }

    @Override
    protected String getTemplatePath() {
        return "template/cipher/InterProcedure.txt";
    }

    @Override
    protected String getTemplateBasedFileName() {
        return "CipherExample.java";
    }

}
