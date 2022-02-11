package edu.wm.cs.masc.exhaustive;

import edu.wm.cs.masc.exhaustive.dataleak.schemas.ReachabilitySchema;
import edu.wm.cs.masc.exhaustive.dataleak.support.FileUtility;
import edu.wm.cs.masc.exhaustive.dataleak.support.Utility;
import edu.wm.cs.masc.exhaustive.dataleak.support.node_containers.ReachabilityNodeChangeContainers;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author Ian Wolff
 */

public class ExhaustiveReachabilitySchemaTest {

    public enum ComponentType {
        STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD, INTERFACE,
        THIS, SUPER, ANONYMOUS, NULL
    }

    String testFilesDirectory = "src/test/resources/input/";
    String content = null;
    CompilationUnit root;
    ASTRewrite rewriter;
    TextEdit edits;
    File processedOutput;
    ReachabilitySchema reachabilitySchema;

    /*
     * There is currently not defined behavior for static classes. This test
     * will need
     * updating when a behavior is defined.
     */
    @Test
    public void reachability_operation_on_hello_world_static()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.STATICMETHOD);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(4, reachabilityChanges.size());


    }

    /**
     * Test Case: Checks reachability schema on input with switch cases
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 5 changes should be found in
     * ReahcabilityNodeChangeContainers
     */
    @Test
    public void reachability_operation_on_hello_world_switch()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.SWITCH);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(5, reachabilityChanges.size());


    }

    /**
     * Test Case: check Reachability schema on a try statement in the input file
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 6 changes should be found in
     * ReachabilityNodeChangeContainer
     */
    @Test
    public void reachability_operation_on_hello_world_try()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.TRY);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(6, reachabilityChanges.size());


    }

    /**
     * Test Case: checks reachability schema on try method in input file
     * <p>
     * Method under test: visit
     * <p>
     * Correct behavior:  7 changes should be found in
     * ReachabilityNodeChangeContainer
     */
    @Test
    public void reachability_operation_on_hello_world_try_method()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.TRYMETHOD);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(7, reachabilityChanges.size());


    }

    /**
     * Test Case: Checks Reachability schema on switch statements with methods
     * in them.
     * <p>
     * Method under test: visit method
     * <p>
     * Correct Behavior: 8 changes should be found in
     * ReachabilityNodeChangeContainer
     */
    @Test
    public void reachability_operation_on_hello_world_switch_method()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.SWITCHMETHOD);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(8, reachabilityChanges.size());


    }

    /**
     * Test Case: checks ReachabilitySchema on interface method in input file
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 3 changes should be found in
     * ReachabilityNodeChangeContainers
     */
    @Test
    public void reachability_operation_on_hello_world_interface()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.INTERFACE);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(3, reachabilityChanges.size());


    }

    /**
     * Test Case: checks how reachabilityschema handles onCreate call on
     * current instance
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 4 changes should be found in
     * ReachabilityNodeChangeContainers
     */
    @Test
    public void reachability_operation_on_hello_world_this()
            throws IOException, BadLocationException {
        prepare_test_files(ComponentType.THIS);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(4, reachabilityChanges.size());


    }

    /**
     * Test Case: checks how reachabilityschema handles onCreate call on
     * super class
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 4 changes should be found in
     * ReachabilityNodeChangeContainers
     */
    @Test
    public void reachability_operation_on_hello_world_super()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.SUPER);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(4, reachabilityChanges.size());


    }

    /**
     * Test Case: checks how reachabilityschema traverses anonymous method
     * <p>
     * Method under test: visit
     * <p>
     * Correct Behavior: 6 changes should be found in
     * ReachabilityNodeChangeContainers
     */
    @Test
    public void reachability_operation_on_hello_world_anonymous()
            throws IOException, BadLocationException {

        prepare_test_files(ComponentType.ANONYMOUS);
        execute_muse_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges =
                reachabilitySchema.getNodeChanges();

        assertEquals(6, reachabilityChanges.size());


    }

    private void execute_muse_reachability()
            throws BadLocationException, MalformedTreeException, IOException {
        reachabilitySchema = new ReachabilitySchema();
        rewriter = ASTRewrite.create(root.getAST());

        root.accept(reachabilitySchema);
    }

    private void prepare_test_files(ComponentType component)
            throws FileNotFoundException, IOException {
        Utility.COUNTER_GLOBAL = 0;

        switch (component) {
            case STATICMETHOD:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput/" +
                                testFilesDirectory +
                                        "/taintSourceInput/" +
                                        "taint_source_sample_static_method" +
                                        ".txt")
                        .toString();
                //content = FileUtility.readSourceFile
                // ("test/input/sink_sample_static_method.txt").toString();
                break;

            case SWITCH:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput/" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_switch.txt")
                        .toString();
                break;

            case SWITCHMETHOD:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_switch_method" +
                                        ".txt")
                        .toString();
                break;

            case TRY:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_try.txt")
                        .toString();
                break;

            case TRYMETHOD:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_try_method.txt")
                        .toString();
                break;

            case INTERFACE:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_interface.txt")
                        .toString();
                break;

            case THIS:
                content = FileUtility.readSourceFile(
//                                "test/input/taintSourceInput" +
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "/taint_source_sample_this.txt")
                        .toString();
                break;

            case SUPER:
                content = FileUtility.readSourceFile(
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_super.txt")
                        .toString();
                break;

            case ANONYMOUS:
                content = FileUtility.readSourceFile(
                                testFilesDirectory +
                                        "taintSourceInput/" +
                                        "taint_source_sample_anonymous.txt")
                        .toString();
                break;

            default:
                break;
        }

        root = getTestAST(content);
    }

    //Taken directly from muse test
    private CompilationUnit getTestAST(String source) {

        HashMap<String, String> options = new HashMap<String, String>();
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(options);

        parser.setSource(source.toCharArray());

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);

        return (CompilationUnit) parser.createAST(new NullProgressMonitor());
    }
}