package edu.wm.cs.muse.dataleak.support.node_containers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * A container for storing nodes, index, property descriptor, and insertion type
 * for sources.
 * 
 * @author Yang Zhang
 *
 */

public class SourceNodeChangeContainers {
	/**
	 * @author Amit Seal Ami
	 * 
	 */
	public enum INSERTION_TYPE {

	/**
	 * METHOD_BODY signifies that the node change will take place within a method
	 * body
	 * <p>
	 * numeric value = 0
	 */
	METHOD_BODY(0),

	/**
	 * DECLARATION signifies that the node change will take place as a declaration
	 * <p>
	 * numeric value = 1
	 */
	DECLARATION(1);

		private int numericValue;

		private INSERTION_TYPE(int value) {
			this.numericValue = value;
		}

		public int getNumericValue() {
			return numericValue;
		}

	}

	public ASTNode node;
	public int index;
	public ChildListPropertyDescriptor propertyDescriptor;
	public int count;
	public ASTNode method;
	// public int insertionType;
	public INSERTION_TYPE type;

	public SourceNodeChangeContainers(ASTNode node, int index, ChildListPropertyDescriptor childListPropertyDescriptor,
			/* int insertionType, */ INSERTION_TYPE type) {
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		// this.insertionType = insertionType;
		this.type = type;
//		if (insertionType == type.numericValue) {
//			System.out.println("Values matched: " + insertionType + type);
//		} else {
//			System.out.println("Values DO NOT matched: " + insertionType + type);
//		}
	}

}
