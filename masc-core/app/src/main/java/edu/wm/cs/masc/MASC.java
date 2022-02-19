package edu.wm.cs.masc;

import edu.wm.cs.masc.similarity.MPlus;
import edu.wm.cs.masc.mainScope.mutationmakers.*;
import edu.wm.cs.masc.utils.config.PropertiesReader;
import edu.wm.cs.masc.mutation.operators.RootOperatorType;
import edu.wm.cs.masc.mutation.properties.*;
import edu.wm.cs.masc.exhaustive.MuseRunner;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.eclipse.jface.text.BadLocationException;

import java.io.File;
import java.io.IOException;

public class MASC {

    public static void main(String[] args) throws Exception{

        if (args.length == 0){
            System.out.println("No properties file supplied");
        }
        else if (args.length > 1){
            System.out.println("Too many arguments were provided");
        }
        else if (!args[0].endsWith(".properties")){
            System.out.println("Properties file must end with the .properties extension");
        }
        else{
            String path = args[0];
            runMain(path);
        }
    }

    public static void runMain(String path) throws ConfigurationException, IOException, BadLocationException {

        PropertiesReader reader = new PropertiesReader(path);
        String scope = reader.getValueForAKey("scope");

        // MASC Exhaustive Layer
        if (scope.equalsIgnoreCase("EXHAUSTIVE")){
            runExhaustiveScope(reader);
        }
        // MASC Selective Layer
        else if(scope.equalsIgnoreCase("SELECTIVE")){
            runSelectiveScope(reader);
        }
        // MASC MainScope
        else if(scope.equalsIgnoreCase("MAIN")){
            runMainScope(reader, path);
        }
        else{
            System.out.println("Unknown Scope: " + scope);
        }
    }

    public static void runSelectiveScope(PropertiesReader reader) throws IOException {
        File lib4ast = new File("libs4ast/");
        File opDir = new File("resources/");
        System.out.println(opDir.getAbsolutePath());
        String[] args = {lib4ast.getAbsolutePath(),
                reader.getValueForAKey("appSrc"),
                reader.getValueForAKey("appName"),
                reader.getValueForAKey("outputDir"),
                opDir.getAbsolutePath(),
                "false"}; // Hardcode this because it never changes in MASC
        MPlus.runMPlus(args);

    }

    public static void runExhaustiveScope(PropertiesReader reader) throws ConfigurationException,
            IOException, BadLocationException {
//        StringOperatorProperties p = new StringOperatorProperties(
//                "Cipher.properties");
//        StringOperatorMutationMaker mutationMaker = new StringOperatorMutationMaker(p);
        MuseRunner.setUpMuse(reader);
//        mutationMaker.populateOperators();
//        MuseRunner.runMuse(mutationMaker.operators);
        MuseRunner.runMuse();
    }

    public static void runMainScope(PropertiesReader reader, String path) throws ConfigurationException {
        String type = reader.getValueForAKey("type");
        AMutationMaker m = null;
        AOperatorProperties p = null;

        if (type.equalsIgnoreCase(RootOperatorType.IntOperator.name())){
            p = new IntOperatorProperties(path);
            m = new IntMutationMaker((IntOperatorProperties) p);
        }
        else if (type.equalsIgnoreCase(RootOperatorType.StringOperator.name())) {
            p = new StringOperatorProperties(path);
            m = new StringOperatorMutationMaker((StringOperatorProperties) p);
        }
        else if (type.equalsIgnoreCase(RootOperatorType.ByteOperator.name())) {
            p = new ByteOperatorProperties(path);
            m = new ByteMutationMaker((ByteOperatorProperties) p);
        }
        else if (type.equalsIgnoreCase(RootOperatorType.Interproc.name())) {
            p = new InterprocProperties(path);
            m = new InterprocMutationMaker((InterprocProperties) p);
        }
        else if (type.equalsIgnoreCase(RootOperatorType.Flexible.name())) {
            p = new FlexibleOperatorProperties(path);
            m = new FlexibleMutationMaker((FlexibleOperatorProperties) p);
        }
        else{
            System.out.println("Unknown Operator Type: " + type);
            return;
        }
        m.make(p);
    }

}
