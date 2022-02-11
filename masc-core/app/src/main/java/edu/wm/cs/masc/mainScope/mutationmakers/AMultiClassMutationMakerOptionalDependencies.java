package edu.wm.cs.masc.mainScope.mutationmakers;

import edu.wm.cs.masc.mutation.builders.flexible.FlexibleFactory;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.utils.file.FilePack;

public abstract class AMultiClassMutationMakerOptionalDependencies
        extends AMultiClassMutationMaker {

    @Override
    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType);
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content);

            setFilepacks(FlexibleFactory.getContent(operatorType, p));
            writeDependencyClasses(operatorType, p);
        }
    }

    public void writeDependencyClasses(OperatorType operatorType,
                                       AOperatorProperties p) {

        if (filePacks == null || filePacks.size() < 1) {
//            if (hasDependencies())
//                throw new IllegalArgumentException(
//                        "Need at least one dependency class");
            return;
        }

        for (FilePack filepack : filePacks) {
            writeOutput(filepack.getPath(), operatorType,
                    filepack.getFilename() + ".java",
                    filepack.getContent());
        }
    }

    protected abstract boolean hasDependencies();
}
