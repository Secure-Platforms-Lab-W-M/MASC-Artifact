package edu.wm.cs.masc.plugins;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.properties.CustomGenericOperatorProperties;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
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

public class MutationMakerForAllPluginOperators {
    public ArrayList<IOperator> operators;
    String path;
    // AOperatorProperties operatorProperties;

    public MutationMakerForAllPluginOperators(String path) throws ConfigurationException {
        this.path = path;
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
     private void populateOperators(AOperatorProperties operatorProperties){
         PluginOperatorManager pluginOperatorManager = PluginOperatorManager.getInstance();
         operators = pluginOperatorManager.initializeCustomPlugins(path, operatorProperties);
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

    private String getName(IOperator operator) {
        return operator.getClass().getName();
    }

    private void writeOutput(String path, String name, String fileName,
                            String content) {
        String dir_path = path + File.separator + name + File.separator;
        if (!CustomFileWriter.WriteFile(dir_path, fileName, content)) {
            System.out.println("Something went wrong, check stack trace");
        }
    }

//    private String getOperatorTypeName(IOperator operator){
//        return p.getClassName() + operator.getClass().getName();
//    }

}
