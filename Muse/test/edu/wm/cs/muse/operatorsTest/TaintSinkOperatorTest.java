package edu.wm.cs.muse.operatorsTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;

import edu.wm.cs.muse.dataleak.operators.TaintSinkOperator;
import edu.wm.cs.muse.dataleak.schemas.TaintSinkSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.mdroid.ASTHelper;

/**
 * 
 * @author Scott Murphy
 * 
 *   Test class to test functionality of the Sink Operator
 */

public class TaintSinkOperatorTest {

	private ASTRewrite rewriter;
	private String source;
	private CompilationUnit root;
	private MethodDeclaration method;
	private Block node;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	private TaintSinkOperator taintSinkOperator;
	private TaintSinkSchema taintSinkSchema;
	private SinkNodeChangeContainers container;

	/**
	 * 
	 * Sets up rewriter that is used in every test
	 * Sets up the sinkSchema that will be used to test operator
	 */
	@Before
	public void init() {
		try {
			source = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
		} catch (IOException e) {
			System.err.println(
					String.format("ERROR PROCESSING \"%s\": %s", "test/input/sample_multilevelclass.txt", e.getMessage()));
			return;
		}

		nodeChanges = new ArrayList<SinkNodeChangeContainers>();
		root = ASTHelper.getTestingAST(source, Arguments.getRootPath());
		rewriter = ASTRewrite.create(root.getAST());
		taintSinkSchema = new TaintSinkSchema();
		root.accept(taintSinkSchema);
    }

	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a declaration insertion type. Inserting sink
	 * 
	 * Method under test: InsertChanges(), insertSink()
	 * 
	 * Correct behavior: A single change should be inserted at the first available position
	 */
	@Test
	public void insert_sink_declaration_nodeChange() {
		nodeChanges.add(createNodeChanges("int methodA(){", 0, 1));
		taintSinkOperator = new TaintSinkOperator(rewriter, nodeChanges,"test/input/sample_helloWorld.txt");
		String output = taintSinkOperator.InsertChanges().toString();
		// accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[4];

		// first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
    }
    
    /**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a declaration insertion type. Inserting sink
	 * 
	 * Method under test: InsertChanges(), insertSink()
	 * 
	 * Correct behavior: A single change should be inserted at the first available position
	 */
	@Test
	public void insert_source_declaration_nodeChange() {
		nodeChanges.add(createNodeChanges("int methodA(){", 1, 1));
		taintSinkOperator = new TaintSinkOperator(rewriter, nodeChanges,"test/input/sample_helloWorld.txt");
		String output = taintSinkOperator.InsertChanges().toString();
		// accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[4];

		// first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
	}

	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a method body insertion type. Inserting sink
	 * 
	 * Method under test: InsertChanges(), insertSink()
	 * 
	 * Correct behavior: A single change should be inserted at the first available position
	 */
	@Test
	public void insert_methodBody_nodeChange() {
		nodeChanges.add(createNodeChanges("return 1;", 0, 1));	
		taintSinkOperator = new TaintSinkOperator(rewriter, nodeChanges,"test/input/sample_helloWorld.txt");
		String output = taintSinkOperator.InsertChanges().toString();
		//accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[5];

		//first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
	}

	/**
	 * This method creates a single node change to be passed to the operator.
	 * 
	 * @param inputString:   the string that will passed to the parser as a source
	 * @param insertionType: either METHOD_BODY or DECLARATION description
	 * @return a new SourceNodeChangeContainers containing the block and insertion
	 *         type
	 */
	private SinkNodeChangeContainers createNodeChanges(String inputString, int insertionType, int propertyFlag) {
		SinkNodeChangeContainers newContainer = null;
		
		AST testAST = root.getAST();
		
		MethodDeclaration methodDeclaration = createMethodDeclaration(testAST);

		// create a temporary parser to generate an AST from the test input
		ASTParser tempParser = ASTParser.newParser(AST.JLS8);
		// set the content for the method body from the test input
		tempParser.setSource(inputString.toCharArray());
		tempParser.setKind(ASTParser.K_STATEMENTS);
		tempParser.setResolveBindings(true);
		tempParser.setBindingsRecovery(true);
		Block block = (Block) tempParser.createAST(new NullProgressMonitor());
		block = (Block) ASTNode.copySubtree(testAST, block);
		methodDeclaration.setBody(block);

        // create a new container with the test input and insertion type
        if (propertyFlag == 1) {
            newContainer = new SinkNodeChangeContainers(block, 0, 0, Block.STATEMENTS_PROPERTY, methodDeclaration, insertionType);
        }
        else { //not currently used, here for future use potentially
            newContainer = new SinkNodeChangeContainers(block, 0, 0, null, methodDeclaration, insertionType);
        }
            
		System.out.println(block.toString());
		return newContainer;
	}
	
	private MethodDeclaration createMethodDeclaration(AST testAST) {
		TypeDeclaration typeDeclaration = testAST.newTypeDeclaration();
		typeDeclaration.setName(testAST.newSimpleName("ClassName"));
		CompilationUnit compilationUnit = testAST.newCompilationUnit();
		compilationUnit.types().add(typeDeclaration);
		MethodDeclaration method = compilationUnit.getAST().newMethodDeclaration();
		method.setName(testAST.newSimpleName("MethodName"));
		typeDeclaration.bodyDeclarations().add(method);
		
		return method;
	}

}