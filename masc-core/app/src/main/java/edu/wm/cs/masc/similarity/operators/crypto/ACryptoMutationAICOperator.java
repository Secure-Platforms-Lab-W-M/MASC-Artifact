package edu.wm.cs.masc.similarity.operators.crypto;

import edu.wm.cs.masc.similarity.helper.ClassMeta;
import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.CryptoMutationPack;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//abstract inner class operator
public abstract class ACryptoMutationAICOperator implements MutationOperator {
    protected abstract String getMutatedLine();

    // override to add necessary package lines
    protected abstract String packageLines();

    //override to add path to template file which will be inserted as part as external class
    protected abstract List<CryptoMutationPack> getMutationPack();


    // adds external class file used as dependency for mutation
    private void addExternalClassFile(String currentFilePath, String packageLine) {
        List<CryptoMutationPack> packs = getMutationPack();

        if (packs == null) return;
        //loops over packs to create new classes based on templates
        for (CryptoMutationPack pack : packs) {
//            String templateFilePath = "";
//            System.out.println("DEBUG: "+ pack.getTemplatePath());
//            try {
//                templateFilePath = ClassLoader.getSystemResource(pack.getTemplatePath()).toURI().getPath();
//                System.out.println("DEBUG: "+ templateFilePath);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//            List<String> lines = FileHelper.readLines(templateFilePath);
            List<String> lines = FileHelper.readLinesFromResource(pack.getTemplatePath());
            ArrayList<String> newLines = new ArrayList<String>();
            if (packageLine != null)
                newLines.add(packageLine + "\n");
            newLines.addAll(lines);

            String newFilePath = new File(currentFilePath).getParent() + File.separator + pack.getOutputFileName();

            //creates the new file only if it does not exist already
            if (Files.notExists(new File(newFilePath).toPath()))
                FileHelper.writeLines(newFilePath, newLines);
        }

    }

    //adds packages line in case mutation requires new packages
    private void addPackages(List<String> lines, ClassMeta meta) {
        if (this.packageLines() != null) {
            if (meta.getImportIndex() != -1)
                lines.add(meta.getImportIndex(), this.packageLines());
        }
    }

    @Override
    public final boolean performMutation(MutationLocation location) {

        List<String> newLines = new ArrayList<String>();

        List<String> lines = FileHelper.readLines(location.getFilePath());
        ClassMeta meta = new ClassMeta(lines);
        String packageLine = meta.getPackageName();

        //Add lines till the mutation location
        for (int i = 0; i <= location.getEndLine(); i++) {

            newLines.add(lines.get(i));
        }

        // Add necessary imports
        this.addPackages(newLines, meta);

        //Apply mutation
        String linesToConsider = "";
        boolean newLineFlag = false;
        for (int i = location.getStartLine(); i <= location.getEndLine(); i++) {
            if (newLineFlag) {
                linesToConsider += " " + lines.get(i);
            } else {
                linesToConsider += lines.get(i);
                newLineFlag = true;
            }
        }

        String sub1 = linesToConsider.substring(0, location.getStartColumn());
        String sub2 = linesToConsider.substring(location.getEndColumn());
        //String mutatedLine = sub1 + "null"  + sub2;

        //String mutatedLine = sub1 + "Cipher.getInstance(\"AES\".replace(\"A\", \"D\"));"  + sub2;
        String mutatedLine = getMutatedLine();

        newLines.add(mutatedLine);


        //Add lines after the MutationLocation
        for (int i = location.getEndLine() + 1; i < lines.size(); i++) {
            newLines.add(lines.get(i));
        }

        FileHelper.writeLines(location.getFilePath(), newLines);

        //Create new Class file for provided path
        this.addExternalClassFile(location.getFilePath(), packageLine);

        return true;
    }
}
