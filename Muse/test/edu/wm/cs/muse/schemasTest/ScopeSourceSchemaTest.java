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
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.schemas.ScopeSourceSchema;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;

/**
 * @author Ian Wolff
 */

public class ScopeSourceSchemaTest {

  public enum ComponentType {
		STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD, PRIVATE, PROTECTED,
		ANONYMOUS
	} 
	
	String content = null;
	Muse muse;
	CompilationUnit root;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	ScopeSourceSchema scopeSourceSchema;

  /*
   * There is currently not defined behavior for static classes. This test will need 
   * updating when a behavior is defined.
   */
  @Test
  public void scope_source_operation_on_multilevelclass_static() {
    try {      
      prepare_test_files(ComponentType.STATICMETHOD);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(10, taintChanges.size());
      
    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: tests scope source schema operation on input that has multiple classes and switch 
   * statements.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 24 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_switch() {
    try {      
      prepare_test_files(ComponentType.SWITCH);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: tests scope source schema operation on input that has multiple classes and try
   * statements.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 24 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_try() {
    try {      
      prepare_test_files(ComponentType.TRY);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: tests scope source schema operation on input that has multiple classes and a try
   * method.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 24 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_try_method() {
    try {      
      prepare_test_files(ComponentType.TRYMETHOD);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: tests scope source schema operation on input that has multiple classes and switch
   * cases that have methods in them.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 36 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_switch_method() {
    try {      
      prepare_test_files(ComponentType.SWITCHMETHOD);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(36, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
}
  
  /**
   * Test Case: tests scope source schema operation on input that has multiple classes and private
   * methods.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 10 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_private_method() {
    try {      
      prepare_test_files(ComponentType.PRIVATE);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(10, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test Case: tests scope source schema operation on input with multilevel classes and protected 
   * methods.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 12 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_protected_method() {
    try {      
      prepare_test_files(ComponentType.PROTECTED);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(12, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test Case: tests scope source schema operation on input that has multilevel classes and 
   * an anonymous class.
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 24 scope changes should be found
   */
  @Test
  public void scope_source_operation_on_multilevelclass_anonymous() {
    try {      
      prepare_test_files(ComponentType.ANONYMOUS);
      execute_muse_scope_source();
      ArrayList<SourceNodeChangeContainers> taintChanges = scopeSourceSchema.getNodeChanges();

      assertEquals(28, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes muse
   * @throws BadLocationException
   * @throws MalformedTreeException
   * @throws IOException
   */
  private void execute_muse_scope_source() throws BadLocationException, MalformedTreeException, IOException {
	scopeSourceSchema = new ScopeSourceSchema();
    rewriter = ASTRewrite.create(root.getAST());
  
    root.accept(scopeSourceSchema);
  }

  /*
   * Prepares test files 
   */
  private void prepare_test_files(ComponentType component) throws FileNotFoundException, IOException {
    Utility.COUNTER_GLOBAL = 0;

    switch (component) {
    case STATICMETHOD:
    	content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_static_multilevelclass.txt").toString();
    	//content = FileUtility.readSourceFile("test/input/sink_sample_static_method.txt").toString();
      break;

    case SWITCH:
      content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_switch_multilevelclass.txt").toString();
      break;
      
    case SWITCHMETHOD:
      content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_switch_method_multilevelclass.txt").toString();
      break;

    case TRY:
      content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_try_multilevelclass.txt").toString();
      break;
    
    case TRYMETHOD:
      content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_try_method_multilevelclass.txt").toString();
      break;
      
    case PRIVATE:
        content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_private_multilevelclass.txt").toString();
        break;
        
    case PROTECTED:
        content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_protected_multilevelclass.txt").toString();
        break;
        
    case ANONYMOUS:
        content = FileUtility.readSourceFile("test/input/scopeSourceInput/scope_source_sample_anonymous_multilevelclass.txt").toString();
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