package edu.wm.cs.mplus.model.location;

public class CryptoMutationPack {
    private String templatePath;
    private String outputFileName;

    public CryptoMutationPack(String templatePath, String outputFileName){
        this.templatePath = templatePath;
        this.outputFileName = outputFileName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public String getOutputFileName() {
        return outputFileName;
    }
}
