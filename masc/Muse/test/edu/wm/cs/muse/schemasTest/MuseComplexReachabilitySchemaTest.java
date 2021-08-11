package edu.wm.cs.muse.schemasTest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import edu.wm.cs.muse.dataleak.schemas.ComplexReachabilitySchema;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.ReachabilityNodeChangeContainers;

/**
 * @author Kyle Gorham
 */

public class MuseComplexReachabilitySchemaTest {

  public enum ComponentType {
		STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD, INTERFACE,
		THIS, SUPER, ANONYMOUS, NULL
	} 
	
	String content = null;
	Muse muse;
	CompilationUnit root;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	ComplexReachabilitySchema complexReachabilitySchema;
	

  /**
   * Test Case: Checks to see if ComplexReachability properly traverse static method
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 3 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_static() {
    try {      
      prepare_test_files(ComponentType.STATICMETHOD);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(3, reachabilityChanges.size());
      
    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: Checks to see if ComplexReachability properly traverses switch 
   * statements
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 4 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_switch() {
    try {      
      prepare_test_files(ComponentType.SWITCH);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(4, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: Checks to see if ComplexReachability properly traverses try statement
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 5 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_try() {
    try {      
      prepare_test_files(ComponentType.TRY);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(5, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: Checks to see if ComplexReachability properly traverses try statement
   * surrounding method
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 6 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_try_method() {
    try {      
      prepare_test_files(ComponentType.TRYMETHOD);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(6, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  /**
   * Test Case: Checks to see if ComplexReachability properly traverse switch statements
   * with methods in them
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 7 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_switch_method() {
    try {      
      prepare_test_files(ComponentType.SWITCHMETHOD);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(7, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
}
  
  /**
   * Test Case: Checks to see if ComplexReachability properly traverse interface
   * method
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 2 changes should be found
   */
  @Test
  public void reachability_operation_on_hello_world_interface() {
    try {      
      prepare_test_files(ComponentType.INTERFACE);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(2, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test Case: Checks to see if ComplexReachability properly traverse static method
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 2 changes should be found
   */
  @Test
  public void complex_reachability_operation_on_hello_world_interface() {
    try {      
      prepare_test_files(ComponentType.INTERFACE);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> complexReachabilityChanges = complexReachabilitySchema.getNodeChanges();
      
      prepare_test_files(ComponentType.INTERFACE);
      execute_muse_complex_reachability();
      ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

      assertEquals(2, reachabilityChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
    
  /**
   * Test Case: Checks to see if ComplexReachability can handle onCreate call on current
   * instance of class
   * 
   * Method under test: visit
   * 
   * Correct Behavior: 3 changes should be found
   */
    @Test
    public void reachability_operation_on_hello_world_this() {
      try {      
        prepare_test_files(ComponentType.THIS);
        execute_muse_complex_reachability();
        ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

        assertEquals(3, reachabilityChanges.size());

      } catch (IOException e) {
        e.printStackTrace();

      } catch (MalformedTreeException e) {
        e.printStackTrace();

      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
    
    /**
     * Test Case: Checks to see if ComplexReachability can handle call on super class
     * 
     * Method under test: visit
     * 
     * Correct Behavior: 3 changes should be found
     */
      @Test
      public void reachability_operation_on_hello_world_super() {
        try {      
          prepare_test_files(ComponentType.SUPER);
          execute_muse_complex_reachability();
          ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

          assertEquals(3, reachabilityChanges.size());

        } catch (IOException e) {
          e.printStackTrace();

        } catch (MalformedTreeException e) {
          e.printStackTrace();

        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }
      
      /**
       * Test Case: Checks to see if ComplexReachability properly traverses anonymous
       * method
       * 
       * Method under test: visit
       * 
       * Correct Behavior: 4 changes should be found
       */
      @Test
      public void reachability_operation_on_hello_world_anonymous() {
        try {      
          prepare_test_files(ComponentType.ANONYMOUS);
          execute_muse_complex_reachability();
          ArrayList<ReachabilityNodeChangeContainers> reachabilityChanges = complexReachabilitySchema.getNodeChanges();

          assertEquals(4, reachabilityChanges.size());

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
  private void execute_muse_complex_reachability() throws BadLocationException, MalformedTreeException, IOException {
		complexReachabilitySchema = new ComplexReachabilitySchema();
	    rewriter = ASTRewrite.create(root.getAST());
	  
	    root.accept(complexReachabilitySchema);
	  }

  private void prepare_test_files(ComponentType component) throws FileNotFoundException, IOException {
    Utility.COUNTER_GLOBAL = 0;

    switch (component) {
    case STATICMETHOD:
    	content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_static_method.txt").toString();
    	//content = FileUtility.readSourceFile("test/input/sink_sample_static_method.txt").toString();
      break;

    case SWITCH:
      content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_switch.txt").toString();
      break;
      
    case SWITCHMETHOD:
      content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_switch_method.txt").toString();
      break;

    case TRY:
      content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_try.txt").toString();
      break;
    
    case TRYMETHOD:
      content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_try_method.txt").toString();
      break;
      
    case INTERFACE:
        content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_interface.txt").toString();
        break;  
        
    case THIS:
        content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_this.txt").toString();
        break;  
        
    case SUPER:
        content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_super.txt").toString();
        break;  
        
    case ANONYMOUS:
        content = FileUtility.readSourceFile("test/input/taintSourceInput/taint_source_sample_anonymous.txt").toString();
        break;
        
	case NULL:
		break;
	default:
		break;
    }

    muse = new Muse();
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