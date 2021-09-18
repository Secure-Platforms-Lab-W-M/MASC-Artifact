package edu.wm.cs.muse;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.After;
import org.junit.Test;

import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Utility;
import  edu.wm.cs.muse.mdroid.ASTHelper;

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
 *
 */
public class MuseTest {

	File expectedOutput;
	String content = null;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;

	// Muse output is written to this file in each test, and compared to
	// the expected output.
	File output = new File("test/output/output.txt");
	
	/**
	 * This is done after every test to clean up the output.txt file and make sure that the 
	 * placementchecker doesn't remove leaks from the previous test cases.
	 * @throws FileNotFoundException  output.txt was not found
	 */
	@After
	public void reset() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("output.txt");
		pw.close();
	
	}
	@Test
	public void reachability_operation_on_hello_world() throws Exception {

		try {
			prepare_test_files(OperatorType.REACHABILITY, 1);
			execute_muse(OperatorType.REACHABILITY);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}

	@Test
	public void complex_reachability_operation_on_multi_class() {
		try {

			prepare_test_files(OperatorType.COMPLEXREACHABILITY, 1);
			execute_muse(OperatorType.COMPLEXREACHABILITY);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}
	
	@Test
	public void taint_source_operation_on_hello_world() {
		try {
			prepare_test_files(OperatorType.TAINTSOURCE, 1);
			execute_muse(OperatorType.TAINTSOURCE);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}

	@Test
	public void taint_sink_operation_on_hello_world() {

		try {
			prepare_test_files(OperatorType.TAINTSINK, 1);
			execute_muse(OperatorType.TAINTSINK);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
			 
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}

	}

	@Test
	public void scope_source_operation_on_hello_world() {
		try {

			prepare_test_files(OperatorType.SCOPESOURCE, 1);
			execute_muse(OperatorType.SCOPESOURCE);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}

	}

	@Test
	public void scope_source_operation_on_multi_class() {
		try {

			prepare_test_files(OperatorType.SCOPESOURCE, 2);
			execute_muse(OperatorType.SCOPESOURCE);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}

	}
	
	@Test
	public void scope_sink_operation_on_multi_class() {
		try {
			prepare_test_files(OperatorType.SCOPESINK, 1);
			execute_muse(OperatorType.SCOPESINK);
			assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}

	private void execute_muse(OperatorType operator) throws BadLocationException, MalformedTreeException, IOException {
		Arguments.setTestMode(true);
		rewriter = ASTRewrite.create(root.getAST());
		sourceDoc = new Document(content);
		muse.operatorExecution(root, rewriter, sourceDoc.get(),output, operator);
		processedOutput = output;
	}
	


	private void prepare_test_files(OperatorType operator, int test) throws FileNotFoundException, IOException {
		Utility.COUNTER_GLOBAL = 0;
		output = new File("test/output/output.txt");
	

		switch (operator) {
		case TAINTSINK:
			// the input for the sink test is the output from the source operator
			// this is because the sink operator relies on sources already being inserted in
			// the code base
			content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_sink.txt");
			break;

		case REACHABILITY:
			content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_reachability.txt");
			break;
			
		case COMPLEXREACHABILITY:
			content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_complex_reachability.txt");
			break;
			
		case TAINTSOURCE:
			content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_source.txt");
			break;

		case SCOPESOURCE:
			if (test == 1) {
				content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
				expectedOutput = new File("test/output/sample_hello_world_taint.txt");
			}
			else if (test == 2) {
				content = FileUtility.readSourceFile("test/input/sample_multilevelclass.txt").toString();
				expectedOutput = new File("test/output/sample_multilevelclass_taint.txt");
			}
			break;
			
		case SCOPESINK:
			content = FileUtility.readSourceFile("test/input/sample_multilevelclass.txt").toString();
			expectedOutput = new File("test/output/sample_multilevelclass_taint_sink.txt");
			break;
			
		}
		
		muse = new Muse();
		root = ASTHelper.getTestingAST(content, Arguments.getRootPath());
	}

		private CompilationUnit getTestAST(String source) {
			
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			Map options = JavaCore.getOptions();
			options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
			options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);		
			parser.setCompilerOptions(options);

			parser.setSource(source.toCharArray());

			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);

			return (CompilationUnit) parser.createAST(new NullProgressMonitor());
	  } 
}
