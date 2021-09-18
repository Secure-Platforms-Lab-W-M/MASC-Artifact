package edu.wm.cs.muse.placementCheckerTest;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.operators.ScopeSourceOperator;
import edu.wm.cs.muse.dataleak.schemas.ScopeSourceSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.JavaSourceFromString;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Placementchecker;
import edu.wm.cs.muse.dataleak.support.SystemEnvironmentException;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;
import edu.wm.cs.muse.mdroid.ASTHelper;

public class PlacementCheckerTest extends Placementchecker {
	File expectedOutput;
	String content = null;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	File output = new File("test/output/output.txt");
	
	 @Override
	 public Boolean check(File temp_file) throws IOException {
		  //sets java_home variable
		  try {
			  if (System.getenv("JAVA_HOME")==null) {
					throw new SystemEnvironmentException();
			  }
		   }catch (SystemEnvironmentException e){
				System.out.println("JAVA_HOME not set");
		   }
			System.setProperty("java.home",System.getenv("JAVA_HOME"));
			
			//Sets up compiler to test source code
		    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		    String source_code= FileUtils.readFileToString(temp_file);
		
		    JavaFileObject file = new JavaSourceFromString("",source_code);
		    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
		    CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
		    boolean success = task.call();
		    
		    //Runs through every error that the compiler sees in the compiler
		    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
		   
		    	//System.out.println("Code: "+ diagnostic.getCode() + " with message: " + diagnostic.getMessage(null));
		    	//Has cases for which errors to catch
		    	switch (diagnostic.getCode()){
		    		case "compiler.err.already.defined":
		    			System.out.println("WARNING DATA LEAK IS ALREADY DEFINED");
		    			return false;
		    		case "compiler.err.non-static.cant.be.ref":
		    			return false;
		    		
		    		case "compiler.err.prob.found.req":
		    			return false;
		    	}
		    }
		    return true;
	  }
	

	@Test
	public void test_getTempFileMethod_sample_HelloWorld() throws FileNotFoundException, IOException {
		Placementchecker checker = new Placementchecker();
		prepare_test_files(OperatorType.SCOPESINK, 1);
		rewriter = ASTRewrite.create(root.getAST());
		File temp_file = checker.getTempFile(root, rewriter, "test/input/sample_helloWorld.txt");
		  
		assertEquals(true,FileUtility.testFileEquality(temp_file, new File("test/input/sample_helloWorld.txt")));

	}
	
	@Test
	public void test_getTempFileMethod_hello_world_sink() throws FileNotFoundException, IOException {
		Placementchecker checker = new Placementchecker();
		prepare_test_files(OperatorType.SCOPESINK, 1);
		rewriter = ASTRewrite.create(root.getAST());
		File temp_file = checker.getTempFile(root, rewriter, "test/output/sample_hello_world_sink.txt");
		  
		assertEquals(true,FileUtility.testFileEquality(temp_file, new File("test/output/sample_hello_world_sink.txt")));

	}
	
	@Test
	public void test_getTempFileMethod_mulitilevel_class() throws FileNotFoundException, IOException {
		PlacementCheckerTest checker = new PlacementCheckerTest();
		prepare_test_files(OperatorType.SCOPESINK, 1);
		rewriter = ASTRewrite.create(root.getAST());
		File temp_file = checker.getTempFile(root, rewriter, "test/input/sample_multilevelclass.txt");
		  
		assertEquals(true,FileUtility.testFileEquality(temp_file, new File("test/input/sample_multilevelclass.txt")));

	}
	
	@Test
	public void test_errors_on_errorless_file() throws IOException {
		PlacementCheckerTest checker = new PlacementCheckerTest();
		File temp_file = new File("test/input/sample_multilevelclass.txt");
		assertEquals(true,checker.check(temp_file));
	}
	
	@Test
	public void test_unreachable_static() throws IOException {
		PlacementCheckerTest checker = new PlacementCheckerTest();
		File temp_file = new File("C:\\Users\\jeffr\\git\\Muse\\code\\Muse\\test\\edu\\wm\\cs\\muse\\placementCheckerTest\\sample_unreachable_static.txt");
		assertEquals(false,checker.check(temp_file));
	}
	
	@Test
	public void test_sample_duplicated_var() throws IOException {
		PlacementCheckerTest checker = new PlacementCheckerTest();
		File temp_file = new File("C:\\Users\\jeffr\\git\\Muse\\code\\Muse\\test\\edu\\wm\\cs\\muse\\placementCheckerTest\\sample_duplicated_variable.txt");
		assertEquals(false,checker.check(temp_file));
	}
	
	@Test
	public void test_incompatible_return() throws IOException {
		PlacementCheckerTest checker = new PlacementCheckerTest();
		File temp_file = new File("C:\\Users\\jeffr\\git\\Muse\\code\\Muse\\test\\edu\\wm\\cs\\muse\\placementCheckerTest\\sample_switch_case_no_return.txt");
		assertEquals(false,checker.check(temp_file));
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
