package edu.wm.cs.masc.exhaustive;

import edu.wm.cs.masc.exhaustive.dataleak.support.OperatorType;
import edu.wm.cs.masc.utils.config.PropertiesReader;
import edu.wm.cs.masc.exhaustive.dataleak.operators.ReachabilityOperator;
import edu.wm.cs.masc.exhaustive.dataleak.schemas.ReachabilitySchema;
import edu.wm.cs.masc.exhaustive.dataleak.support.Arguments;
import edu.wm.cs.masc.exhaustive.dataleak.support.FileUtility;
import edu.wm.cs.masc.exhaustive.mdroid.ASTHelper;
import edu.wm.cs.masc.mutation.operators.IOperator;
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

public class MuseRunner {

    static ASTRewrite rewriter;

    /**
     * Uses the properties reader to setup the Muse Arguments class
     * Arguments maintains information for mutation such as
     * input and output directories, app name, and operator type (REACHABILITY)
     *
     * @param reader
     */
    public static void setUpMuse(PropertiesReader reader) {
        File lib4ast = new File("libs4ast/");
        String[] args = {lib4ast.getAbsolutePath(),
                reader.getValueForAKey("appSrc"),
                reader.getValueForAKey("appName"),
                reader.getValueForAKey("outputDir"),
                "REACHABILITY"}; // Hardcode this because it never changes in
        // MASC
        Arguments.extractArguments(args);
    }

    public static Collection<File> getFiles() {
        return FileUtils.listFiles(new File(Arguments.getRootPath()),
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    //    public static void runMuse(HashMap<OperatorType, IOperator> ops)
    //    throws IOException, BadLocationException {
    public static void runMuse() throws IOException, BadLocationException {
        // Iterate through the different mutation types
//        for (OperatorType op : ops.keySet()){

        // Get the mutation
//            String mutation = ops.get(op).mutation();

        Arguments.resetRootPath();

        FileUtility.setupMutantsDirectory();
        Collection<File> files = getFiles();

        // Apply the mutation to each file
        for (File file : files) {

            boolean appNameInPath = file.getCanonicalPath()
                    .contains(Arguments.getAppName()
                            .replace(".", "/"));

            if (file.getName().endsWith(".java")) {
//                        && appNameInPath){
                Arguments.setFileName(file.getName());
                String source = FileUtility.readSourceFile(
                                file.getAbsolutePath())
                        .toString();

                // Creates the abstract syntax tree
                CompilationUnit root = ASTHelper.getAST(source,
                        Arguments.getBinariesFolder(),
                        Arguments.getRootPath());

                // Creates a new instance for describing manipulations of the
                // given AST.
                rewriter = ASTRewrite.create(root.getAST());

                operatorExecution(root, rewriter, source, file);
            }
        }

    }


//    }

    public static void operatorExecution(CompilationUnit root,
                                         ASTRewrite rewriter,
                                         String source, File file)
            throws MalformedTreeException, BadLocationException, IOException {

        ReachabilitySchema reachabilitySchema = new ReachabilitySchema();
        root.accept(reachabilitySchema);
        ReachabilityOperator reachabilityOperator = new ReachabilityOperator(
                rewriter,
                reachabilitySchema.getNodeChanges());
        rewriter = reachabilityOperator.InsertChanges();
        applyChangesToFile(file, source, rewriter);

    }

    private static void applyChangesToFile(File file, String source,
                                           ASTRewrite rewriter)
            throws BadLocationException, IOException {
        Document sourceDoc = new Document(source);

        TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
        // Applies the edit tree rooted by this edit to the given document.
        edits.apply(sourceDoc);
        FileUtils.writeStringToFile(file, sourceDoc.get(),
                Charset.defaultCharset());
    }
}
