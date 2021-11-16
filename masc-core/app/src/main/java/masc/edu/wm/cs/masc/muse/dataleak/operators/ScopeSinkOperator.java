package edu.wm.cs.muse.dataleak.operators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;


import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Placementchecker;
import edu.wm.cs.muse.dataleak.support.SchemaOperatorUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;

/**
 * The TaintSinkOperator class will insert the sink aspect of the taint mutation
 * scheme on the modified file through comparisons of the parent classes between
 * the field and methods.
 * 
 * @author Yang Zhang, Amit Seal Ami
 */
public class ScopeSinkOperator {

	ArrayList<TaintNodeChangeContainers> fieldChanges;
	ArrayList<SinkNodeChangeContainers> methodChanges;
	ASTRewrite rewriter;
	Placementchecker checker = new Placementchecker();
	File temp_file;
	String source_file;

	public ScopeSinkOperator(ASTRewrite rewriter, ArrayList<TaintNodeChangeContainers> fieldChanges,
			ArrayList<SinkNodeChangeContainers> methodChanges, String source_file) {
		this.rewriter = rewriter;
		this.fieldChanges = fieldChanges;
		this.methodChanges = methodChanges;
		this.source_file = source_file;

	}

	/**
	 * Modifies compares the parent classes between the two nodeChangeContainers,
	 * then inserts the appropriate number of sinks into the correct methods
	 * according to the number of fields.
	 * 
	 * @return ASTRewrite modified ASTRewrite
	 */
	public ASTRewrite InsertChanges() {

		for (TaintNodeChangeContainers fieldChanges : fieldChanges) {
			for (SinkNodeChangeContainers methodChanges : methodChanges) {
				if (methodChanges.node == fieldChanges.node) {
					insertSink((Block) methodChanges.method, methodChanges.index, fieldChanges.fieldHolder,
							methodChanges.propertyDescriptor);
				}
			}
		}

		return rewriter;
	}

	// for sink insertion
	void insertSink(Block node, int index, ArrayList<FieldDeclaration> fieldHolder,
			ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = null;
		if (node == null)
			return;
		//if it is an empty method.
		if(node.statements().size()==0) {
			return;
		}
		String[] rawVarDec = DataLeak.getVariableDeclaration(OperatorType.SCOPESINK).split("%d",2);
		for (int i = 0; i < fieldHolder.size(); i++) {
			try {
				listRewrite = rewriter.getListRewrite(node, nodeProperty);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			String placeholderValue = fieldHolder.get(i).toString().split(rawVarDec[0])[1].split("=")[0];
			MethodDeclaration methodNode = (MethodDeclaration) node.getParent();
			System.out.println(String.format("leak-%s-%s: %s.%s", placeholderValue, index,
					SchemaOperatorUtility.getClassNameOfMethod(node), methodNode.getName()));
			
			String sink = String.format(DataLeak.getSink(OperatorType.SCOPESINK, Integer.parseInt(placeholderValue), index));
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
			
			int placement = 1;
			int statement_counter = 0;
			
			for (Object obj : node.statements()) {
				
				if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
					// will only change placement if the super is at top and there is only one
					// statement
					//System.out.println("SUper found");

					if (statement_counter == 0) {
						placement = 0;
					}
					placement++;
				} else if (obj.toString().startsWith("return ")) {
					// will only change placement if the return is the first statement in node.
					if (statement_counter == 0)
						placement = 0;
				}
				statement_counter++;
			}
			listRewrite.insertAt(placeHolder, placement, null);
			if (!(listRewrite.getParent().getRoot() instanceof Block)) {
				temp_file = checker.getTempFile((CompilationUnit)listRewrite.getParent().getRoot(), rewriter, source_file);
				try {
					if (!checker.check(temp_file))
						listRewrite.remove(placeHolder,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}