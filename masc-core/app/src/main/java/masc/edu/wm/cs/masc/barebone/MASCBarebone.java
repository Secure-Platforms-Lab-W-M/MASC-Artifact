package masc.edu.wm.cs.masc.barebone;

import masc.edu.wm.cs.masc.barebone.mutationmakers.*;
import masc.edu.wm.cs.masc.config.PropertiesReader;
import masc.edu.wm.cs.masc.operator.RootOperatorType;
import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;
import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
import masc.edu.wm.cs.masc.properties.InterprocProperties;
import masc.edu.wm.cs.masc.properties.StringOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class MASCBarebone {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {

                System.out.println(
                        "No arguments provided. Running Cipher.properties by " +
                                "default.");
                run("Cipher.properties");

            } else if (args.length == 1) {
                run(args[0]);
            } else {
                System.out.println(
                        "Please run " + args[0] + "with .properties file as " +
                                "argument.");
            }
        } catch (ConfigurationException e) {
            System.out.println("Something wrong with the properties file.");
            e.printStackTrace();
        }
    }

    private static void run(String path) throws ConfigurationException {

        PropertiesReader reader = new PropertiesReader(path);

        AMutationMaker m = null;
        String type = reader.getValueForAKey("type");
        if (type.equalsIgnoreCase(RootOperatorType.StringOperator.name())) {
            StringOperatorProperties properties =
                    new StringOperatorProperties(
                            path);
            m = new StringOperatorMutationMaker(
                    properties);
            m.make(properties);

        } else if (type
                .equalsIgnoreCase(RootOperatorType.ByteOperator.name())) {
            ByteOperatorProperties properties = new ByteOperatorProperties(
                    path);
            m = new ByteMutationMaker(properties);
            m.make(properties);
        } else if (type.equalsIgnoreCase(RootOperatorType.Interproc.name())) {
            InterprocProperties properties = new InterprocProperties(path);
            m = new InterprocMutationMaker(properties);
            m.make(properties);
        } else if (type.equalsIgnoreCase(RootOperatorType.Flexible.name())) {
            FlexibleOperatorProperties properties =
                    new FlexibleOperatorProperties(
                            path);
            m = new FlexibleMutationMaker(properties);
            m.make(properties);
        }
    }
}