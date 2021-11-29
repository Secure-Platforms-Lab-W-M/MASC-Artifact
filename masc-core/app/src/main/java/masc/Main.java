package masc;

import masc.edu.wm.cs.masc.barebone.mutationmakers.*;
import masc.edu.wm.cs.masc.config.PropertiesReader;
import masc.edu.wm.cs.masc.muse.dataleak.operators.ReachabilityOperator;
import masc.edu.wm.cs.masc.muse.dataleak.schemas.ReachabilitySchema;
import masc.edu.wm.cs.masc.muse.dataleak.support.Arguments;
import masc.edu.wm.cs.masc.muse.dataleak.support.FileUtility;
import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.RootOperatorType;
import masc.edu.wm.cs.masc.properties.*;
import masc.edu.wm.cs.masc.runners.MuseRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception{
        RootOperatorType rootType = RootOperatorType.StringOperator;
        if (args.length == 0){
            System.out.println("No properties file supplied");
        }
        else if (args.length > 1){
            System.out.println("Too many arguments were provided");
        }
        else if (!args[0].endsWith(".properties")){
            System.out.println("Properties file must end with the .properties extension");
        }
//        else if (edu.wm.cs.muse.dataleak.support.Arguments.extractArguments(args[0]) < 0){
//            System.out.println("Unable to extract arguments from properties file");
//        }
        else{
            String path = "new_template.properties";
            PropertiesReader reader = new PropertiesReader(path);
            String type = reader.getValueForAKey("type");
            String scope = reader.getValueForAKey("scope").toUpperCase();

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
            run(scope, reader, m, p);
        }
    }

    public static void run(String scope, PropertiesReader reader,
                           AMutationMaker m, AOperatorProperties p) throws IOException, BadLocationException {
        // Muse
        if (scope.equals("EXHAUSTIVE")){
            MuseRunner.setUpMuse(reader);
            m.populateOperators();
            MuseRunner.runMuse(m.operators);
        }
        // MDroid+
        else if(scope.equals("SIMILARITY")){
            // TO-DO
        }
        // MASC Barebones
        else if(scope.equals("MAIN")){
            m.make(p);
        }
        else{
            System.out.println("Unknown Scope: " + scope);
        }
    }

}
