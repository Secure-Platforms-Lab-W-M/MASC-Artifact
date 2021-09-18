package edu.wm.cs.muse.visitors;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.support.Utility;

public class SinkVisitor extends ASTVisitor {
	ASTRewrite rewriter;

	public SinkVisitor(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}

	Pattern variablePattern = Pattern.compile("(.*String dataLeAk)(\\d+).*"); // the pattern to search for
	HashMap<Integer, Integer> repeatCounts = new HashMap<Integer, Integer>();

	private void insertSink(ASTNode node, int index, int count, ChildListPropertyDescriptor nodeProperty,
			ASTNode method) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		int cur = repeatCounts.containsKey(count) ? repeatCounts.get(count) : -1;
		repeatCounts.put(count, cur + 1);
		String sink = String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", count, repeatCounts.get(count),
				count);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
		String methodName = ((MethodDeclaration) method).getName().toString();
		String className = "";
		method = method.getParent();
		while (method != null) {
			if (method.getNodeType() == ASTNode.TYPE_DECLARATION) {
				className = ((TypeDeclaration) method).getName().toString();
				break;
			} else if (method.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				className = "1";
				break;
			}
			method = method.getParent();
		}
		System.out.println(String.format("leak-%d-%d: %s.%s", count, repeatCounts.get(count), className, methodName));
	}

	private void insertSource(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format(
				"final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
				Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

	@Override
	public boolean visit(MethodDeclaration method) {
		// Methods
		int count = 0;
		int index = 0;
		Block node = method.getBody();
		if (node == null) {
			return true;
		}
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")
					|| obj.toString().startsWith("dataLeAk")) {
				index++;
			}
		}

		ASTNode n = node.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		while (n != null && !inAnonymousClass && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.METHOD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((MethodDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.FIELD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.TYPE_DECLARATION:
				for (Object field : ((TypeDeclaration) n).bodyDeclarations()) {
					if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
						Matcher matcher = variablePattern.matcher(field.toString());
						if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
							count = Integer.valueOf(matcher.group(2));
							insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
						}
					}
				}
				try {
					inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				for (Object field : ((AnonymousClassDeclaration) n).bodyDeclarations()) {
					if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
						Matcher matcher = variablePattern.matcher(field.toString());
						if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
							count = Integer.valueOf(matcher.group(2));
							insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
						}
					}
				}
				inAnonymousClass = true;
				break;
			}
			n = n.getParent();
		}
		while (inAnonymousClass && n != null && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.CATCH_CLAUSE:
				n = n.getParent();
				break;
			case ASTNode.METHOD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((MethodDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.FIELD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;

			case ASTNode.TYPE_DECLARATION:
				Utility.COUNTER_GLOBAL++;
				insertSource(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
				insertSink(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method);
				try {
					inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				Utility.COUNTER_GLOBAL++;
				insertSource(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
				insertSink(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method);
				break;
			}
			n = n.getParent();
		}
		return true;
	}
}