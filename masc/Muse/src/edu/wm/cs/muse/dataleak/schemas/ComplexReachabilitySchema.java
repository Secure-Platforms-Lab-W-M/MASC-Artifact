package edu.wm.cs.muse.dataleak.schemas;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ComplexReachabilitySchema extends ReachabilitySchema {
	public boolean visit(TypeDeclaration node) {
		// Classes and Interfaces
		if (node.isInterface()) {
			return false;
		}
		return true;
	}

	public boolean visit(AnonymousClassDeclaration node) {
		// Anonymous classes
		return true;
	}
}
