package masc.edu.wm.cs.masc.barebone.mutationmakers;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.properties.AOperatorProperties;
import masc.edu.wm.cs.masc.utility.CustomFileWriter;

import java.io.File;
import java.util.HashMap;

/**
 * Is the Abstract Mutation maker that
 * creates instances of mutations for bare-bone layer of MASC.
 * To simplify, it is given the body of mutation, classname and other information,
 * it outputs an instance of mutation in barebone format.
 */

public abstract class AMutationMaker {
    public HashMap<OperatorType, IOperator> operators =
            new HashMap<>();

    public abstract String getContent(OperatorType operatorType);

    //if multiple types of operators fall under same category, and we want them all to be created.
    public abstract void populateOperators();

    public void make(AOperatorProperties p) {
        populateOperators();
        for (OperatorType operatorType : operators.keySet()) {
            String content = getContent(operatorType);
            writeOutput(p.getOutputDir(), operatorType,
                    p.getClassName() + ".java",
                    content);
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
