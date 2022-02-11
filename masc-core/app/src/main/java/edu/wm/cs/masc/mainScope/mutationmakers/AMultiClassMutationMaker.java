package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.utils.file.CustomFileWriter;
import edu.wm.cs.masc.utils.file.FilePack;

import java.io.File;
import java.util.ArrayList;

/**
 * Is abstract mutation maker that involves creating instances of mutation
 * for bare-bone format involving multiple classes for bare-bone layer of MASC
 */
public abstract class AMultiClassMutationMaker extends AMutationMaker {

    ArrayList<FilePack> filePacks;


    public void setFilepacks(ArrayList<FilePack> filePacks) {
        this.filePacks = filePacks;
    }

    @Override
    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType);
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content);
            writeDependencyClasses(operatorType, p);
        }
    }

    public void writeDependencyClasses(OperatorType operatorType, AOperatorProperties p) {
        if (filePacks == null || filePacks.size() < 1) {
            throw new IllegalArgumentException(
                    "Need at least one dependency class. Set FilePacks");
        }
        for (FilePack filepack : filePacks) {
            writeOutput(filepack.getPath(), operatorType,
                    filepack.getFilename() + ".java",
                    filepack.getContent());
        }
    }

    public void writeOutput(String path, OperatorType type, String fileName,
                            String content) {
        String dir_path = path + File.separator + type.name() + File.separator;
        if (!CustomFileWriter.WriteFile(dir_path, fileName, content)) {
            System.out.println("Something went wrong, check stack trace");
        }
    }

}
