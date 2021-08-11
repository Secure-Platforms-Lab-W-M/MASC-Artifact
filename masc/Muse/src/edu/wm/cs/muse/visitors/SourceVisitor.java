package edu.wm.cs.muse.visitors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.support.Utility;

class SourceVisitor extends ASTVisitor {
	ASTRewrite rewriter;

	public SourceVisitor(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}

	protected void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
				Utility.COUNTER_GLOBAL);
		Utility.COUNTER_GLOBAL++;
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

	private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"\";", Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement)rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

	public boolean visit(MethodDeclaration method) {
		// Methods
		Block node = method.getBody();
		if (node == null) {
			return true;
		}
		int index = 0;
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				index++;
			}
		}
		ASTNode n = node.getParent();
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
				insertVariable(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
				insertion(node, index, Block.STATEMENTS_PROPERTY);
				try {
					if (Modifier.isStatic(((TypeDeclaration) n).getModifiers())) {
						inStaticContext = true;
					}
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				insertVariable(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
				insertion(node, index, Block.STATEMENTS_PROPERTY);
				inAnonymousClass = true;
				break;
			}
			n = n.getParent();
		}
		return true;
	}
}