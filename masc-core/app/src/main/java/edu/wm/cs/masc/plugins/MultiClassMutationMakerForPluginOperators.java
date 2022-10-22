package edu.wm.cs.masc.plugins;

import edu.wm.cs.masc.mutation.builders.restrictive.BuilderInterprocClass;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
import edu.wm.cs.masc.utils.file.FilePack;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.ArrayList;

public class MultiClassMutationMakerForPluginOperators extends MutationMakerForPluginOperators{
    public MultiClassMutationMakerForPluginOperators(String path, String pluginFolderDir) throws ConfigurationException {
        super(path, pluginFolderDir);
    }

    private void initFilePacks(InterprocProperties p) {
//        filePacks.clear();

        String otherClass = p.getOtherClassName();
        FilePack filePack = new FilePack(otherClass, p.getOutputDir(),
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        this.setFilepacks(filePacks);
    }

    ArrayList<FilePack> filePacks;


    public void setFilepacks(ArrayList<FilePack> filePacks) {
        this.filePacks = filePacks;
    }

    @Override
    public void make(AOperatorProperties p) {
        initFilePacks((InterprocProperties) p);
        super.make(p);
        for (IOperator operator: operators) {
            writeDependencyClasses(getName(operator));
        }
    }

    public void writeDependencyClasses(String name) {
        if (filePacks == null || filePacks.size() < 1) {
            throw new IllegalArgumentException(
                    "Need at least one dependency class. Set FilePacks");
        }
        for (FilePack filepack : filePacks) {
            writeOutput(filepack.getPath(), name,
                    filepack.getFilename() + ".java",
                    filepack.getContent());
        }
    }


}
