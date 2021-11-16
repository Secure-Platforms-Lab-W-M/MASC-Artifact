package edu.wm.cs.muse.dataleak.support.node_containers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * A container for storing nodes, index, property descriptor, count, method and insertion type for sinks.
 * @author Yang Zhang
 *
 */

public class SinkNodeChangeContainers {
	

	public ASTNode node;
	public int index;
	public ChildListPropertyDescriptor propertyDescriptor;
	public int count;
	public ASTNode method;
	public int insertionType;
	
	public SinkNodeChangeContainers(ASTNode node, int index, int count,
			ChildListPropertyDescriptor childListPropertyDescriptor, ASTNode method, int insertion)
	{
		this.node = node;
		this.index = index;
		this.count = count;
		propertyDescriptor = childListPropertyDescriptor;
		this.method = method;
		this.insertionType = insertion;
	}
}
