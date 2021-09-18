package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;

/**
 * The SourceSchema visits each node the AST tree to find data sources, then
 * inserts a placeholder string through an SourceOperator.
 * 
 * @author Yang Zhang
 */

public class TaintSourceSchema extends ASTVisitor {

	private ArrayList<SourceNodeChangeContainers> nodeChanges;

	public TaintSourceSchema() {
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}

	public ArrayList<SourceNodeChangeContainers> getNodeChanges() {
		return this.nodeChanges;
	};

	/*
	 * Includes an additional integer param to differentiate between insertion and
	 * insertVariable
	 */
	public boolean visit(MethodDeclaration method) {
		
		if (method.getParent().getNodeType() == ASTNode.ENUM_DECLARATION) {
			//abort abort. enum method.
			return true;
		}
		// Methods
		Block methodBody = method.getBody();
		if (methodBody == null) {
			return true;
		}
		int index = 0;
		for (Object obj : methodBody.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				index++;
			}
		}
		ASTNode n = methodBody.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		while (n != null && !inAnonymousClass && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.CATCH_CLAUSE:
				n = n.getParent();
				break;
			case ASTNode.METHOD_DECLARATION:
				try {
					if (Modifier.isStatic(((MethodDeclaration) n).getModifiers())) {
						inStaticContext = true;
					}
				} catch (NullPointerException e) {
				}
				break;

			case ASTNode.TYPE_DECLARATION:
				nodeChanges.add(new SourceNodeChangeContainers(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, // 1,
						INSERTION_TYPE.DECLARATION));
				nodeChanges.add(new SourceNodeChangeContainers(methodBody, index, Block.STATEMENTS_PROPERTY, // 0,
						INSERTION_TYPE.METHOD_BODY));
				try {
					if (Modifier.isStatic(((TypeDeclaration) n).getModifiers())) {
						inStaticContext = true;
					}
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				nodeChanges.add(new SourceNodeChangeContainers(n, 0,
						AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY, /* 1, */ INSERTION_TYPE.DECLARATION));
				nodeChanges.add(new SourceNodeChangeContainers(methodBody, index, Block.STATEMENTS_PROPERTY, // 0,
						INSERTION_TYPE.METHOD_BODY));
				inAnonymousClass = true;
				break;
			}
			n = n.getParent();
		}
		return true;
	}

}
