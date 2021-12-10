package masc.edu.wm.cs.masc.exhaustive.dataleak.schemas;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import masc.edu.wm.cs.masc.exhaustive.dataleak.support.node_containers.ReachabilityNodeChangeContainers;

/**
 * Reachability Schema goes through the AST tree for reachability, analyzes the
 * nodes and prepares a list of nodes to be changed.
 * 
 * @author Amit Seal Ami
 */
public class ReachabilitySchema extends ASTVisitor {

	private ArrayList<ReachabilityNodeChangeContainers> nodeChanges;

	public ReachabilitySchema() {
		nodeChanges = new ArrayList<ReachabilityNodeChangeContainers>();
	}

	public ArrayList<ReachabilityNodeChangeContainers> getNodeChanges() {
		return this.nodeChanges;
	};

	public boolean visit(TypeDeclaration node) {
		// Classes and Interfaces
		if (node.isInterface()) {
			return false;
		}
		//don't need to add in class declaration either.
//		String loc = node.getName().toString() + ".<init>";
//		String logMessage = "leak-%d: <" + loc + ">";
//		nodeChanges.add(
//				new ReachabilityNodeChangeContainers(node, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, logMessage));
		return true;
	}

	public boolean visit(AnonymousClassDeclaration node) {
		//Don't need to add in anonymous class declaration, this results in errors for try-catch statements.
		// Anonymous classes
		//		String loc = "1.<init>";
		//		String logMessage = "leak-%d: <" + loc + ">";
		//		nodeChanges.add(new ReachabilityNodeChangeContainers(node, 0,
		//				AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY, logMessage));
		return true;
	}

	public boolean visit(Block node) {
		// Blocks
		int index = 0;
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				index = 1;
			}
		}
		String className = "";
		String methodName = "<init>";
		ASTNode trace = node.getParent();
		while (trace != null) {
			if (trace.getNodeType() == ASTNode.TYPE_DECLARATION) {
				className = ((TypeDeclaration) trace).getName().toString();
				break;
			} else if (trace.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				className = "1";
				break;
			} else if (trace.getNodeType() == ASTNode.METHOD_DECLARATION) {
				methodName = ((MethodDeclaration) trace).getName().toString();
			}
			trace = trace.getParent();
		}
		String loc = className + "." + methodName;
		String logMessage = "mutation-%d: "+ loc ;
		nodeChanges.add(new ReachabilityNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, logMessage));
		return true;
	}
}
