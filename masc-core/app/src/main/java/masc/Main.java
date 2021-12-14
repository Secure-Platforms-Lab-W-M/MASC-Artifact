package masc;

import masc.edu.wm.cs.masc.barebone.mutationmakers.*;
import masc.edu.wm.cs.masc.config.PropertiesReader;
import masc.edu.wm.cs.masc.operator.RootOperatorType;
import masc.edu.wm.cs.masc.properties.*;
import masc.edu.wm.cs.masc.runners.MuseRunner;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.eclipse.jface.text.BadLocationException;

import java.io.File;
import java.io.IOException;

public class Main {

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
        String scope = reader.getValueForAKey("scope").toUpperCase();

        // Muse
        if (scope.equals("EXHAUSTIVE")){
            runExhaustiveScope(reader);
        }
        // MDroid+
        else if(scope.equals("SIMILARITY")){
            runSelectiveScope(reader);
        }
        // MASC Barebones
        else if(scope.equals("MAIN")){
            runMainScope(reader, path);
        }
        else{
            System.out.println("Unknown Scope: " + scope);
        }
    }

    public static void runSelectiveScope(PropertiesReader reader) throws IOException {
        
        String[] args = {reader.getValueForAKey("lib4ast"),
                reader.getValueForAKey("appSrc"),
                reader.getValueForAKey("appName"),
                reader.getValueForAKey("outputDir"),
                reader.getValueForAKey("operatorsDir"),
                "false"}; // Hardcode this because it never changes in MASC
        edu.wm.cs.mplus.MPlus.runMPlus(args);

    }

    public static void runExhaustiveScope(PropertiesReader reader) throws ConfigurationException,
            IOException, BadLocationException {
        StringOperatorProperties p = new StringOperatorProperties("Cipher.properties");
        StringOperatorMutationMaker m = new StringOperatorMutationMaker(p);
        MuseRunner.setUpMuse(reader);
        m.populateOperators();
        MuseRunner.runMuse(m.operators);
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
