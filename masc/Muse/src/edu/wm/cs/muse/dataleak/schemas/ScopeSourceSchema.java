package edu.wm.cs.muse.dataleak.schemas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;;

/**
 * The TaintSchema will traverse the nodes of the AST, and when it reaches a
 * method, it will locate the class(es) the method is in and insert a
 * declaration in the class(es) and a source in the method. Once the schema has
 * completed its course, it will call SinkOperator to insert sinks that reflect
 * the location of the declarations.
 * 
 * @author Yang Zhang, Amit Seal Ami
 */

public class ScopeSourceSchema extends ASTVisitor {
	ASTNode parent;
	private ArrayList<SourceNodeChangeContainers> nodeChanges;
	int index = 0;

	/**
	 * TaintSchema should use the source node container array since they occur at
	 * the same time and use the same parameters. The sink aspect will be included
	 * via the TaintSinkSchema
	 */
	public ScopeSourceSchema() {
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}

	public ArrayList<SourceNodeChangeContainers> getNodeChanges() {
		return this.nodeChanges;
	};

	// Creates a stack of class nodes that have a corresponding methodDeclaration
	// node
	// in order to correctly insert field declarations in the classes and source
	// strings
	// in the method bodies using the TaintOperator.
	
	public boolean visit(MethodDeclaration node) {
		

		if (Modifier.isStatic(node.getModifiers())) {
			return true;
		}
		if (Modifier.isPrivate(node.getModifiers())) {
			System.out.println("Private method: " + node.getName());
			return true;
		}
		Stack<ASTNode> ancestorStack = new Stack<ASTNode>();

		System.out.println(node.getName());
		parent = node.getParent();
		

		while (true) {
			
			if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
				TypeDeclaration parentAsType = (TypeDeclaration) parent;

				// if parent is private/protected, skip.
				if (Modifier.isPrivate(parentAsType.getModifiers())
						|| Modifier.isProtected(parentAsType.getModifiers()))
					return false;
				// if parent is static, skip
				if (Modifier.isStatic(parentAsType.getModifiers()))
					return false;

				ancestorStack.add(parent);
//				parent = parent.getParent();
			} else if (parent.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				// if anonymous, skip processing.
//			return true;
				ancestorStack.add(parent);
//				parent = parent.getParent();
//			break;
			} else if (parent.getNodeType() == ASTNode.ENUM_DECLARATION) {
//			ancestorStack.add(parent);
//			parent = parent.getParent();
//			break;
				return false;
			}

			else if (parent.getParent() == null)
				break;
			parent = parent.getParent();
		}

		for (ASTNode ancestorNode : ancestorStack) {
			
			if (ancestorStack.size() == 0)
				return true;
			if (ancestorNode.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				nodeChanges.add(new SourceNodeChangeContainers(ancestorNode, 0,
						AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY, /* 1, */ INSERTION_TYPE.DECLARATION));
				nodeChanges.add(new SourceNodeChangeContainers(node.getBody(), index, Block.STATEMENTS_PROPERTY, // 0,
						INSERTION_TYPE.METHOD_BODY));
//				continue;
			} else {
				// for declaration
				nodeChanges.add(new SourceNodeChangeContainers(ancestorNode, 0,
						TypeDeclaration.BODY_DECLARATIONS_PROPERTY, INSERTION_TYPE.DECLARATION));
				// for method body
				nodeChanges.add(new SourceNodeChangeContainers(node.getBody(), index, Block.STATEMENTS_PROPERTY,
						INSERTION_TYPE.METHOD_BODY));
			}
			index++;

		}

		return true;
	}

}
