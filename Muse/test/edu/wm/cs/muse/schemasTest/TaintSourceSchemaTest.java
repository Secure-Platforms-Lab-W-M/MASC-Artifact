package edu.wm.cs.muse.schemasTest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.schemas.TaintSourceSchema;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;

/**
 * Unit test file of SourceSchema
 * 
 * @author Kyle Gorham
 *
 */
public class TaintSourceSchemaTest {

	public enum ComponentType {
		STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD
	} 
	
	File expectedOutput;
	String content = null;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	TaintSourceSchema taintSourceSchema;

	// Muse output is written to this file in each test, and compared to
	// the expected output.
	File output = new File("test/output/source_schema_output.txt");

	@Test
	public void taint_source_operation_on_hello_world_static() {
		try {
			prepare_test_files(ComponentType.STATICMETHOD);
			execute_muse(OperatorType.TAINTSOURCE);

			ArrayList<SourceNodeChangeContainers> changes = taintSourceSchema.getNodeChanges();
			
			assertEquals(4, changes.size());

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}
	
	
	/** 
	 * Test Case: Is a try catch in the code, checking to make sure the code 
	 * doesn't break if there is one
	 * The purpose of this test is to see how many nodes are sent to the
	 * operator to be changed.
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: There should still be 6 nodes in changes.
	 ***/
	@Test
	public void taint_source_operation_on_hello_world_try() {
		try {
			prepare_test_files(ComponentType.TRY);
			execute_muse(OperatorType.TAINTSOURCE);

			ArrayList<SourceNodeChangeContainers> changes = taintSourceSchema.getNodeChanges();
			
			assertEquals(6, changes.size());
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}
	
	/** 
	 * Test Case: If there is a switch statement in the code makes sure the program
	 * will continue to read the file until the end.
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: There should be 6 nodes in changes, as there are 3 method declarations
	 ***/
	@Test
	public void taint_source_operation_on_hello_world_switch() {
		try {
			prepare_test_files(ComponentType.SWITCH);
			execute_muse(OperatorType.TAINTSOURCE);

			ArrayList<SourceNodeChangeContainers> changes = taintSourceSchema.getNodeChanges();
			
			assertEquals(6, changes.size());
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}
	
	/** 
	 * Test Case: Checks to see what happens if there is a method declaration inside
	 * a switch statement. The one switch statement has multiple methods
	 * There should be 10 nodes in changes, as there are 5 method declarations
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: 2 methods are inside switch, both should be included
	 ***/
	@Test
	public void taint_source_operation_on_hello_world_switch_method() {
		try {
			prepare_test_files(ComponentType.SWITCHMETHOD);
			execute_muse(OperatorType.TAINTSOURCE);

			ArrayList<SourceNodeChangeContainers> changes = taintSourceSchema.getNodeChanges();
			
			assertEquals(10, changes.size());
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}
	
	/** 
	 * Test Case: Place a method inside the try catch to see if handles correctly
	 * The purpose of this test is to see how many nodes are sent to the
	 * operator to be changed.
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: There should still be 8 nodes, as there are 4 method declarations
	 ***/
	@Test
	public void taint_source_operation_on_hello_world_try_method() {
		try {
			prepare_test_files(ComponentType.TRYMETHOD);
			execute_muse(OperatorType.TAINTSOURCE);

			ArrayList<SourceNodeChangeContainers> changes = taintSourceSchema.getNodeChanges();
			
			assertEquals(8, changes.size());
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		} catch (BadLocationException e) {
			e.printStackTrace();

		}
	}

	/**
	 * executes muse
	 * @param operator
	 * @throws BadLocationException
	 * @throws MalformedTreeException
	 * @throws IOException
	 */
	private void execute_muse(OperatorType operator) throws BadLocationException, MalformedTreeException, IOException {
		taintSourceSchema = new TaintSourceSchema();
		rewriter = ASTRewrite.create(root.getAST());
		sourceDoc = new Document(content);
		//muse.operatorExecution(root, rewriter, sourceDoc.get(), output, operator);
		
		root.accept(taintSourceSchema);
		processedOutput = output;
	}

	//Expected output isn't used at the moment, as schemas don't actually change anything in the file.
	//Still here for testing purposes later on
	private void prepare_test_files(ComponentType component) throws FileNotFoundException, IOException {
		Utility.COUNTER_GLOBAL = 0;
		output = new File("test/output/output.txt");

		switch (component) {
		case STATICMETHOD:
			content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_static_method.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_sink.txt");
			break;

		case SWITCH:
			content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_switch.txt").toString();
			expectedOutput = new File("test/output/sample_hello_world_source.txt");
			break;
			
		case SWITCHMETHOD:
			content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_switch_method.txt").toString();
			expectedOutput = new File("test/output/sample_multilevelclass_taint.txt");
			break;

		case TRY:
			content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_try.txt").toString();
			expectedOutput = new File("test/output/sample_multilevelclass_taint.txt");
			break;
		
		case TRYMETHOD:
			content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_try_method.txt").toString();
			expectedOutput = new File("test/output/sample_multilevelclass_taint.txt");
			break;
			
		}
		
		muse = new Muse();
		root = getTestAST(content);
	}

	
	  //Taken directly from muse test
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
