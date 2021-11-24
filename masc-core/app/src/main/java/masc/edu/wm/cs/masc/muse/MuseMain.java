package masc.edu.wm.cs.masc.muse;

import masc.edu.wm.cs.masc.barebone.mutationmakers.*;
import masc.edu.wm.cs.masc.config.PropertiesReader;
import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.RootOperatorType;
import masc.edu.wm.cs.masc.properties.*;
import org.apache.commons.configuration2.ex.ConfigurationException;
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

public class MuseMain {

    ASTRewrite rewriter;

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

            // Replace this with a case statement
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
            setUpMuse(reader);
            m.populateOperators();
            new MuseMain().runMuse(m.operators);
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

    /**
     * Uses the properties reader to setup the Muse Arguments class
     * Arguments maintains information for mutation such as
     * input and output directories, app name, and operator type (REACHABILITY)
     * @param reader
     */
    public static void setUpMuse(PropertiesReader reader){
        String[] args = {reader.getValueForAKey("lib4ast"),
                reader.getValueForAKey("appSrc"),
                reader.getValueForAKey("appName"),
                reader.getValueForAKey("outputDir"),
                reader.getValueForAKey("operatorType")};
        edu.wm.cs.muse.dataleak.support.Arguments.extractArguments(args);
    }

    public Collection<File> getFiles(){
        return FileUtils.listFiles(new File(edu.wm.cs.muse.dataleak.support.Arguments.getRootPath()),
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    public void runMuse(HashMap<OperatorType, IOperator> ops) throws IOException, BadLocationException {

        // Iterate through the different mutation types
        for (OperatorType op : ops.keySet()){

            // Get the mutation
            String mutation = ops.get(op).mutation();

            edu.wm.cs.muse.dataleak.support.Arguments.resetRootPath();

            edu.wm.cs.muse.dataleak.support.FileUtility.setupMutantsDirectory(op.name());
            Collection<File> files = getFiles();

            // Apply the mutation to each file
            for (File file: files){

                boolean appNameInPath = file.getCanonicalPath()
                        .contains(edu.wm.cs.muse.dataleak.support.Arguments.getAppName()
                                .replace(".", "/"));

                if (file.getName().endsWith(".java") && appNameInPath){
                    edu.wm.cs.muse.dataleak.support.Arguments.setFileName(file.getName());
                    String source = edu.wm.cs.muse.dataleak.support.FileUtility.readSourceFile(file.getAbsolutePath()).toString();

                    // Creates the abstract syntax tree
                    CompilationUnit root = edu.wm.cs.muse.mdroid.ASTHelper.getAST(source, edu.wm.cs.muse.dataleak.support.Arguments.getBinariesFolder(),
                            edu.wm.cs.muse.dataleak.support.Arguments.getRootPath());

                    // Creates a new instance for describing manipulations of the given AST.
                    rewriter = ASTRewrite.create(root.getAST());

                    operatorExecution(root, rewriter, source, file, mutation);
                }
            }
        }


    }

    public void operatorExecution(CompilationUnit root, ASTRewrite rewriter,
                                  String source, File file, String mutation)
            throws MalformedTreeException, BadLocationException, IOException {

        edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema reachabilitySchema = new edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema();
        root.accept(reachabilitySchema);
        edu.wm.cs.muse.dataleak.operators.ReachabilityOperator reachabilityOperator = new edu.wm.cs.muse.dataleak.operators.ReachabilityOperator(rewriter,
                reachabilitySchema.getNodeChanges(), mutation);
        rewriter = reachabilityOperator.InsertChanges();
        applyChangesToFile(file, source, rewriter);

    }

    private void applyChangesToFile(File file, String source, ASTRewrite rewriter)
            throws BadLocationException, IOException {
        Document sourceDoc = new Document(source);

        TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
        // Applies the edit tree rooted by this edit to the given document.
        edits.apply(sourceDoc);
        FileUtils.writeStringToFile(file, sourceDoc.get(), Charset.defaultCharset());
    }
}
