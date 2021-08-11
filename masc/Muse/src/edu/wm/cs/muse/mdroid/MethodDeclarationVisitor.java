package edu.wm.cs.muse.mdroid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor{
	
	private HashSet<String> targetDeclarations;
	private HashSet<MethodDeclarationVO> declarations;
	
	
	public MethodDeclarationVisitor(HashSet<String> targetDeclarations){
		this.declarations = new HashSet<>();
		this.targetDeclarations = targetDeclarations;
		
	}
	public boolean visit(MethodDeclaration declaration){
			
		String className = null;
		
		String methodName = declaration.getName().getFullyQualifiedName();
		if(declaration.getParent() instanceof TypeDeclaration){
			TypeDeclaration type = (TypeDeclaration)declaration.getParent();
			className = type.getName().toString();
			
			if(type.getSuperclassType() != null){
				className = type.getSuperclassType().toString();
				
			}
			
		}else if(declaration.getParent() instanceof ClassInstanceCreation){
			ClassInstanceCreation type = (ClassInstanceCreation)declaration.getParent();
			className = type.getType().toString();
			if(type.resolveTypeBinding().getSuperclass() != null){
				className = type.resolveTypeBinding().getSuperclass().toString();
				
			}
		}
		
		StringBuilder declarationName = new StringBuilder();
		if(className != null){
			declarationName.append(className).append(".").append(methodName);
			
			if(targetDeclarations.contains(declarationName.toString())){
				this.declarations.add(new MethodDeclarationVO(className, methodName, declaration.getStartPosition(), declaration.getLength()));
			}
		}
		
		
		
		return true;
	}
	public HashSet<String> getTargetDeclarations() {
		return targetDeclarations;
	}
	public void setTargetDeclarations(HashSet<String> targetDeclarations) {
		this.targetDeclarations = targetDeclarations;
	}
	public HashSet<MethodDeclarationVO> getDeclarations() {
		return this.declarations;
	}
	public void setDeclarations(HashSet<MethodDeclarationVO> declarations) {
		this.declarations = declarations;
	}
	
	
	
	
}
