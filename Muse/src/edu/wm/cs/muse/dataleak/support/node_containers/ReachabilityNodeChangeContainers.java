package edu.wm.cs.muse.dataleak.support.node_containers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * It acts as a container for storing node, change, and property descriptor.
 * @author Amit Seal Ami
 *
 */
public class ReachabilityNodeChangeContainers {

	public ASTNode node;
	public int index;
	public ChildListPropertyDescriptor propertyDescriptor;
	public String changedSource = null;
	public ReachabilityNodeChangeContainers(ASTNode node, int index,
			ChildListPropertyDescriptor childListPropertyDescriptor, String changedSource) {
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		this.changedSource = changedSource;
		
		
	}
}
