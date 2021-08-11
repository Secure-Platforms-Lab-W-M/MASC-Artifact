package edu.wm.cs.muse.operatorsTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEdit;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;

import edu.wm.cs.muse.dataleak.operators.TaintSourceOperator;
import edu.wm.cs.muse.dataleak.schemas.TaintSourceSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;
import edu.wm.cs.muse.mdroid.ASTHelper;

/**
 * 
 * @author Ian Wolff
 * 
 * Class to test the functionality of Source operator
 * 
 */
public class TaintSourceOperatorTest {
	private ASTRewrite rewriter;
	private String source;
	private CompilationUnit root;
	private MethodDeclaration node;
	private ArrayList<SourceNodeChangeContainers> nodeChanges;
	private TaintSourceOperator taintSourceOperator;
	private TaintSourceSchema taintSourceSchema;
	private SourceNodeChangeContainers container;
	
	/**
	 * Default source file is sample_helloWorld.txt.
	 */
	@Before
	public void setUp() {
		try {
			source = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
		}
		catch (IOException e) {
			System.err.println(String.format("ERROR PROCESSING \"%s\": %s", "test/input/sample_helloWorld.txt", e.getMessage()));
			return;
		}
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
		root = ASTHelper.getTestingAST(source, Arguments.getRootPath());
		rewriter = ASTRewrite.create(root.getAST());
	}

	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a declaration insertion type.
	 * 
	 * Method under test: InsertChanges(), insertVariableDeclaration()
	 * 
	 * Correct behavior: A single change should be inserted at the first available position
	 */
	@Test
	public void insert_declaration_nodeChange() {	
		nodeChanges.add(createNodeChanges("int methodA(){", INSERTION_TYPE.DECLARATION));	
		taintSourceOperator = new TaintSourceOperator(rewriter, nodeChanges,"test/input/sample_helloWorld.txt");
		String output = taintSourceOperator.InsertChanges().toString();
		//accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[4];
		
		//first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
	}

	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a method body insertion type.
	 * 
	 * Method under test: InsertChanges(), insertInMethodBody()
	 * 
	 * Correct behavior: A single change should be inserted at the first available position
	 */
	@Test
	public void insert_methodBody_nodeChange() {
		nodeChanges.add(createNodeChanges("return 1;", INSERTION_TYPE.METHOD_BODY));	
		taintSourceOperator = new TaintSourceOperator(rewriter, nodeChanges,"test/input/sample_helloWorld.txt");
		String output = taintSourceOperator.InsertChanges().toString();
		//accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[5];

		//first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
	}	
	
	/**
	 * This method creates a single node change to be passed to the operator. 
	 * 
	 * @param inputString: the string that will passed to the parser as a source
	 * @param insertionType: either METHOD_BODY or DECLARATION description
	 * @return a new SourceNodeChangeContainers containing the block and insertion type
	 */
	private SourceNodeChangeContainers createNodeChanges(String inputString, INSERTION_TYPE insertionType) {
		AST testAST = root.getAST();
		//create a temporary parser to generate an AST from the test input
	    ASTParser tempParser = ASTParser.newParser(AST.JLS8);
	    //set the content for the method body from the test input
	    tempParser.setSource(inputString.toCharArray());
	    tempParser.setKind(ASTParser.K_STATEMENTS);
	    tempParser.setResolveBindings(true);
	    tempParser.setBindingsRecovery(true);
	    Block block = (Block) tempParser.createAST(new NullProgressMonitor());
	    block = (Block) ASTNode.copySubtree(testAST, block);
	    //create a new container with the test input and insertion type
	    SourceNodeChangeContainers newContainer = new SourceNodeChangeContainers(block, 0, Block.STATEMENTS_PROPERTY, insertionType);
		return newContainer;
	}
}
