package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.ReachabilityNodeChangeContainers;

public class ComplexReachability extends ReachabilityOperator {

	public ComplexReachability(ASTRewrite rewriter, ArrayList<ReachabilityNodeChangeContainers> nodeChanges) {
		super(rewriter, nodeChanges);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ASTRewrite InsertChanges() {
		for (int i = 0; i < nodeChanges.size(); i++) {

			ReachabilityNodeChangeContainers nodeChange = nodeChanges.get(i);

			System.out.println(String.format(nodeChange.changedSource, Utility.COUNTER_GLOBAL));

			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(
					DataLeak.getLeak(OperatorType.COMPLEXREACHABILITY, Utility.COUNTER_GLOBAL),
					ASTNode.EMPTY_STATEMENT);

			Utility.COUNTER_GLOBAL++;

			/*
			 * Uses the rewriter to create an AST for the SinkSchema to utilize Then creates
			 * a new instance to manipulate the AST The root node then accepts the schema
			 * visitor on the visit The rewriter implements the specified changes made by
			 * the sink operator
			 */
			ListRewrite listRewrite = rewriter.getListRewrite(nodeChange.node, nodeChange.propertyDescriptor);
			listRewrite.insertAt(placeHolder, nodeChange.index, null);
		}
		return rewriter;
	}
}
