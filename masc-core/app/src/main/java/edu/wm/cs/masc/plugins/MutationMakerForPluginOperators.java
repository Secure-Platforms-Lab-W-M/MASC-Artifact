package edu.wm.cs.masc.plugins;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mainScope.mutationmakers.AMutationMaker;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.utils.file.CustomFileWriter;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Is the Abstract Mutation maker that
 * creates instances of mutations for bare-bone layer of MASC.
 * To simplify, it is given the body of mutation, classname and other information,
 * it outputs an instance of mutation in mainScope format.
 */

public class MutationMakerForPluginOperators {
    public ArrayList<IOperator> operators;
    String path;
    String pluginFolderDir;
    // AOperatorProperties operatorProperties;

    public MutationMakerForPluginOperators(String path, String pluginFolderDir) throws ConfigurationException {
        this.path = path;
        this.pluginFolderDir = pluginFolderDir;
        // operatorProperties = new CustomGenericOperatorProperties(path);
    }

    public String getContent(IOperator operator, AOperatorProperties operatorProperties) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(operatorProperties.getClassName());
        System.out.println("Processing: " + getName(operator));
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpecWithException();
        mainMethod.addCode(operator.mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }

    //if multiple types of operators fall under same category, and we want them all to be created.
     public void populateOperators(AOperatorProperties operatorProperties){
         PluginOperatorManager pluginOperatorManager = new PluginOperatorManager(pluginFolderDir);
         operators = pluginOperatorManager.initializePlugins(path, operatorProperties);
     };

    public void make(AOperatorProperties operatorProperties) {
        populateOperators(operatorProperties);
        for (IOperator operator: operators) {
            String content = getContent(operator, operatorProperties);
            writeOutput(operatorProperties.getOutputDir(), getName(operator),
                    operatorProperties.getClassName() + ".java",
                    content.replaceAll("%d", ""));
        }

    }

    public String getName(IOperator operator) {
        return operator.getClass().getName();
    }

    public void writeOutput(String path, String name, String fileName,
                            String content) {
        String dir_path = path + File.separator + name + File.separator;
        if (!CustomFileWriter.WriteFile(dir_path, fileName, content)) {
            System.out.println("Something went wrong, check stack trace");
        }
    }

}


//public class MutationMakerForPluginOperators extends AMutationMaker {
//    String path;
//    String pluginFolderDir;
//    ArrayList<IOperator> operatorList;
//    public HashMap<String, IOperator> operators =
//            new HashMap<>();
//
//    public MutationMakerForPluginOperators(String path, String pluginFolderDir) throws ConfigurationException {
//        this.path = path;
//        this.pluginFolderDir = pluginFolderDir;
//    }
//
//    @Override
//    public void populateOperators() {
//        for(IOperator operator: operatorList) {
//            operators.put(getName(operator), operator);
//        }
//    }
//
//    public void loadOperators(AOperatorProperties operatorProperties){
//         PluginOperatorManager pluginOperatorManager = new PluginOperatorManager(pluginFolderDir);
//        operatorList = pluginOperatorManager.initializePlugins(path, operatorProperties);
//     }
//
//    public void make(AOperatorProperties operatorProperties) {
//        loadOperators(operatorProperties);
//        super.make(operatorProperties);
//    }
//
//    private String getName(IOperator operator) {
//        return operator.getClass().getName();
//    }
//
//
//
//
//
//}