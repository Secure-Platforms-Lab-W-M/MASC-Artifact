package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;;

/**
 * The TaintSinkSchema will traverse the nodes of the rewritten AST for
 * declarations, then correctly implement a sink insertion to all the methods in
 * the class. The file must be modified by the TaintSchema process before
 * TaintSink Schema can function correctly.
 * 
 * @author Yang Zhang
 */

public class ScopeSinkSchema extends ASTVisitor {
	ASTNode parent;
	ASTNode classRetainer = null;
	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	int index = 0;
	int methodIndex = 0;
	ArrayList<FieldDeclaration> fieldHolder = new ArrayList<FieldDeclaration>();
	ArrayList<FieldDeclaration> previousFieldHolder = new ArrayList<FieldDeclaration>();

	public ScopeSinkSchema() {
		taintNodeChanges = new ArrayList<TaintNodeChangeContainers>();
		nodeChanges = new ArrayList<SinkNodeChangeContainers>();
	}

	public ArrayList<TaintNodeChangeContainers> getFieldNodeChanges() {
		return this.taintNodeChanges;
	};

	public ArrayList<SinkNodeChangeContainers> getMethodNodeChanges() {
		return this.nodeChanges;
	};

	// action flow is: this method visitor will catch all the methods, then take
	// note
	// of the classes they belong in. This will be used to compare with field visit
	// and insert a sink for every field declaration in every method where the
	// classes
	// match up.
	public boolean visit(MethodDeclaration method) {
		if (Modifier.isStatic(method.getModifiers())) {
			return true;
		}
		parent = method.getParent();
		int throwaway = 0;

		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {

			nodeChanges.add(new SinkNodeChangeContainers(parent, Utility.COUNTER_GLOBAL_TSINK++, throwaway,
					Block.STATEMENTS_PROPERTY, method.getBody(), 0));
			// get parent's fields with findField
			parent = parent.getParent();

		}

		return true;
	}

	// use field visit to get a array of fields for each class. If it is in a
	// subclass, add all fields in the class before it to the array. Pass to
	// the taintNodeChanges container, then in conjunction with the methods part
	// insert the sinks
	public boolean visit(FieldDeclaration field) {
		String vd = DataLeak.getVariableDeclaration(OperatorType.SCOPESINK);
		// the type and the name of the variable declaration (e.g. "String dataLeAk")
		String vdType = vd.split("%d")[0];
		// the name of the variable declaration (e.g. "dataLeAk")
		String vdName = vdType.split(" ")[1];

// The getParent loop was found unnecessary, getParent will always find a TYPE_DECLARATION
		parent = field.getParent();

//		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION && parent.getParent() == null) {
		// some class segment was completed before this one. This is a new class chain
		if (parent == classRetainer) {
			// check for strings of the declaration, e.g. "String dataLeAk%d"

			if (field.toString().startsWith(vdType)) {
				fieldHolder.add(field);
				previousFieldHolder.add(field);
			}

			ArrayList<FieldDeclaration> fieldDecl = new ArrayList<FieldDeclaration>(fieldHolder);

			taintNodeChanges.add(new TaintNodeChangeContainers(parent, fieldDecl, index, Block.STATEMENTS_PROPERTY, 0));
			// keep track of outer classes
			fieldHolder.clear();
			classRetainer = parent;
		}

		// it has switched to a subclass, must push all fields to keep correct hierarchy

		// A new FieldDeclaration ArrayList called previousFieldHolder was made in order
		// to add all the sink strings from an earlier outer class into the subclass. It
		// will add the same field as fieldHolder as it traverses
		// the tree, but does NOT get cleared if the parent of the method is in the same
		// class as the previous method.
		if (parent != classRetainer) {

			if (classRetainer != null) {
				
				if (field.toString().contains(vdName)
						&& field.toString().substring(0, 15).compareTo(vdType) == 0) {
					previousFieldHolder.add(field);
				}
				classRetainer = parent;
			}
			else {
				
				if (field.toString().contains(vdName)
						&& field.toString().substring(0, 15).compareTo(vdType) == 0) {
					previousFieldHolder.add(field);
				}
				classRetainer = parent;
			}

			ArrayList<FieldDeclaration> fieldDecl = new ArrayList<FieldDeclaration>(fieldHolder);

			taintNodeChanges
					.add(new TaintNodeChangeContainers(classRetainer, fieldDecl, index, Block.STATEMENTS_PROPERTY, 0));
			classRetainer = parent;

			for (int fieldCount = 0; fieldCount < previousFieldHolder.size(); fieldCount++) {
				fieldHolder.add(previousFieldHolder.get(fieldCount));
			}
		}

		index++;

		return true;
	}
}
