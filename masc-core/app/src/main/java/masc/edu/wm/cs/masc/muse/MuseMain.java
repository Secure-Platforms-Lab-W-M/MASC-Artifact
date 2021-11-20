package masc.edu.wm.cs.masc.muse;

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

public class MuseMain {

    ASTRewrite rewriter;

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
        else if (edu.wm.cs.muse.dataleak.support.Arguments.extractArguments(args[0]) < 0){
            System.out.println("Unable to extract arguments from properties file");
        }
        else{
            System.out.println("Let's run this thing");
            new MuseMain().runMuse(args);
        }
    }

    public Collection<File> getFiles(){
        return FileUtils.listFiles(new File(edu.wm.cs.muse.dataleak.support.Arguments.getRootPath()),
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    public void runMuse(String args[]) throws IOException, BadLocationException {
        edu.wm.cs.muse.dataleak.support.FileUtility.setupMutantsDirectory();
        Collection<File> files = getFiles();
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

                operatorExecution(root, rewriter, source, file);
            }
        }

    }

    public void operatorExecution(CompilationUnit root, ASTRewrite rewriter,
                                  String source, File file)
            throws MalformedTreeException, BadLocationException, IOException {

        File temp_file;
        String newSource;
        CompilationUnit newRoot;

        edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema reachabilitySchema = new edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema();
        root.accept(reachabilitySchema);
        edu.wm.cs.muse.dataleak.operators.ReachabilityOperator reachabilityOperator = new edu.wm.cs.muse.dataleak.operators.ReachabilityOperator(rewriter,
                reachabilitySchema.getNodeChanges());
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
