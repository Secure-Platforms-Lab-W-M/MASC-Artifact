package edu.wm.cs.mplus.operators.crypto;

public class IvParameterSpec extends ACryptoMutationOperator {
    @Override
    protected String getMutatedLine() {
        String mutatedLine="String cipherVAL=\"\";\n"+
            "for(int i = 0; i<9; i++){\n"+
                "cipherVAL+=(char)(65+i);\n"+
            "}\n"+
            "IvParameterSpec cipherIVSpec = new IvParameterSpec(cipherVAL.getBytes());\n";

        return mutatedLine;
    }

    @Override
    protected String packageLines() {
        return "import javax.crypto.spec.IvParameterSpec;\n";
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
