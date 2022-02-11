package edu.wm.cs.masc.exhaustive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.After;
import org.junit.Test;

import edu.wm.cs.masc.exhaustive.dataleak.support.Arguments;
import edu.wm.cs.masc.exhaustive.dataleak.support.FileUtility;
import edu.wm.cs.masc.exhaustive.dataleak.support.Utility;
import edu.wm.cs.masc.exhaustive.mdroid.ASTHelper;

/*
 * We will be focusing on creating behavior based test cases. AAA pattern, i.e.
 * Arrange the preconditions
 * Act on test Object
 * Assert the results
 * will be utilized.
 */

/**
 * Unit test file of Muse.
 *
 * @author Amit Seal Ami, Liz Weech, Yang Zhang, Scott Murphy
 */
public class ExhaustiveTest {

    File expectedOutput;
    String content = null;
    CompilationUnit root;
    Document sourceDoc;
    ASTRewrite rewriter;
    TextEdit edits;
    File processedOutput;

//    String rootPath = System.getProperty(
//            "user.dir") + "\\src\\test\\java\\mascDeprecated\\edu\\wm\\cs" +
//			"\\mascDeprecated\\muse\\";
    String rootPath = "src/test/resources/input/exhaustive/";
    String outputFilePath = rootPath + "output/Hello.java";
    String inputFilePath = rootPath + "input/Hello.txt";
    String expectedFilePath = rootPath + "expected/Hello.txt";

    // Muse output is written to this file in each test, and compared to the
	// expected output.
    File output = new File(outputFilePath);

    /**
     * This is done after every test to clean up the output.txt file and make
     * sure that the
     * placementchecker doesn't remove leaks from the previous test cases.
     *
     * @throws FileNotFoundException output.txt was not found
     */
    @After
    public void reset() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("output.txt");
        pw.close();

    }

    @Test
    public void reachability_operation_on_hello_world() throws Exception {

        prepare_test_files();
        execute_muse();
        assertTrue(
                FileUtility.testFileEquality(expectedOutput, processedOutput));

    }

    private void prepare_test_files()
            throws FileNotFoundException, IOException {
        Utility.COUNTER_GLOBAL = 0;
        output = new File(outputFilePath);

        content = FileUtility.readSourceFile(inputFilePath).toString();
        expectedOutput = new File(expectedFilePath);

        root = ASTHelper.getTestingAST(content, Arguments.getRootPath());
    }

    private void execute_muse()
            throws BadLocationException, MalformedTreeException, IOException {
        Arguments.setTestMode(true);
        rewriter = ASTRewrite.create(root.getAST());
        sourceDoc = new Document(content);
        MuseRunner.operatorExecution(root, rewriter, sourceDoc.get(), output,
                "System.out.println(\"This is a mutation\");");
        processedOutput = output;
    }


}
