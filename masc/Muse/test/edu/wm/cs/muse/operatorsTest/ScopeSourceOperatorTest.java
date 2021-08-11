package edu.wm.cs.muse.operatorsTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

import edu.wm.cs.muse.dataleak.operators.ScopeSourceOperator;
import edu.wm.cs.muse.dataleak.schemas.ScopeSourceSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;
import edu.wm.cs.muse.mdroid.ASTHelper;

public class ScopeSourceOperatorTest {
	private ASTRewrite rewriter;
	private String source;
	private CompilationUnit root;
	private MethodDeclaration node;
	private ArrayList<SourceNodeChangeContainers> nodeChanges;
	private ScopeSourceOperator scopeSourceOperator;
	private ScopeSourceSchema scopeSourceSchema;
	private SourceNodeChangeContainers container;
	
	/**
	 * Default source file is sample_multilevelclass.txt.
	 */
	@Before
	public void setUp() {
		try {
			source = FileUtility.readSourceFile("test/input/sample_multilevelclass.txt").toString();
		}
		catch (IOException e) {
			System.err.println(String.format("ERROR PROCESSING \"%s\": %s", "test/input/sample_multilevelclass.txt", e.getMessage()));
			return;
		}
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
		root = ASTHelper.getTestingAST(source, Arguments.getRootPath());
		rewriter = ASTRewrite.create(root.getAST());
		scopeSourceSchema = new ScopeSourceSchema();
		root.accept(scopeSourceSchema);
	}

	/**
	 * Test case: Verifies the behavior of the operator when given a SoureNodeChangeContainers
	 * with a null ChildListPropertyDescriptor and a declaration insertion type.
	 * 
	 * Method under test: InsertChanges()
	 * 
	 * Correct behavior: The rewriter should be returned with no changes.
	 */
	@Test
	public void insertion_at_null_declaration_node() {
		//create a null container with declaration insertion type
		SourceNodeChangeContainers nullDeclarationNode = new SourceNodeChangeContainers(node, 0, null, INSERTION_TYPE.DECLARATION);
		nodeChanges.add(nullDeclarationNode);
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		String output = scopeSourceOperator.InsertChanges().toString();
		//check that the returned rewriter is  equal to the original rewriter
		
		assertEquals(rewriter.toString(),output);
	}

	/**
	 * Test case: Verifies the behavior of the operator when given a SoureNodeChangeContainers
	 * with a null ChildListPropertyDescriptor and a method body insertion type.
	 * 
	 * Method under test: InsertChanges()
	 * 
	 * Correct behavior: The rewriter should be returned with no changes.
	 */
	@Test
	public void insertion_at_null_methodBody_node() {
		//create a null container with method body insertion type
		SourceNodeChangeContainers nullMethodBodyNode = new SourceNodeChangeContainers(node, 1, null, INSERTION_TYPE.METHOD_BODY);
		nodeChanges.add(nullMethodBodyNode);
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		String output = scopeSourceOperator.InsertChanges().toString();
		//check that the returned rewriter is  equal to the original rewriter
		assertEquals(rewriter.toString(),output);
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
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		String output = scopeSourceOperator.InsertChanges().toString();
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
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		String output = scopeSourceOperator.InsertChanges().toString();
		//accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[5];

		//first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		
		assertEquals(expectedOutput, outputAtInsertion);
	}
	
	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a method body insertion type that begins with "this()".
	 * 
	 * Method under test: InsertChanges(), insertInMethodBody()
	 * 
	 * Correct behavior: The rewriter should be returned with no changes.
	 * 
	 * Note: The "this()" statements does not appear to be recognized by the parser
	 * and is not being passed. This will need fixing.
	 */
	@Test
	public void insert_this_methodBody_nodeChange() {
		nodeChanges.add(createNodeChanges("this(x = 1);",INSERTION_TYPE.METHOD_BODY));		
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		//rewriter should not be changed
		String output = scopeSourceOperator.InsertChanges().toString();
		assertEquals(rewriter.toString(),output);
	}
	
	/**
	 * Test case: Verifies the behavior of the operator when given a single node change with
	 * a method body insertion type that begins with "super()".
	 * 
	 * Method under test: InsertChanges(), insertInMethodBody()
	 * 
	 * Correct behavior: The rewriter should be returned with no changes.
	 */
	@Test
	public void insert_super_methodBody_nodeChange() {
		nodeChanges.add(createNodeChanges("super.test();",INSERTION_TYPE.METHOD_BODY));
		scopeSourceOperator = new ScopeSourceOperator(rewriter, nodeChanges,"test/input/sample_multilevelclass.txt");
		//rewriter should not be changed
		String output = scopeSourceOperator.InsertChanges().toString();
		assertEquals(rewriter.toString(),output);
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
	    ASTNode node =  (ASTNode)tempParser.createAST(new NullProgressMonitor());
	    node =  ASTNode.copySubtree(testAST, node);
	    //create a new container with the test input and insertion type
		SourceNodeChangeContainers newContainer = new SourceNodeChangeContainers(node, 0,Block.STATEMENTS_PROPERTY, insertionType);
		System.out.println(node.toString());
		return newContainer;
	}
}
