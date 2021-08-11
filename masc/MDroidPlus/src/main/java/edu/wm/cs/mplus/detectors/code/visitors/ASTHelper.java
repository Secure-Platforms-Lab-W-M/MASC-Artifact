package edu.wm.cs.mplus.detectors.code.visitors;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTHelper {
	
public static CompilationUnit getAST(String source, String binariesFolder, String sourceRootFolder) {
		
		
		HashMap options = new HashMap();
		List<String> jars = getJarsInfolder(binariesFolder);
		String[] classPath = new String[jars.size()];
		for(int i = 0; i < classPath.length; i++){
			classPath[i] = binariesFolder+File.separator+jars.get(i);
		}
		String[] srcPath = {sourceRootFolder};
		
		ASTParser parser = ASTParser.newParser(AST.JLS2);
		options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		parser.setCompilerOptions(options);
		
		parser.setSource(source.toCharArray());
		parser.setEnvironment(classPath, srcPath, null, false);
		   
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		
		
		return (CompilationUnit) parser.createAST(null);
	}
	
	
	public static CompilationUnit getASTAndBindings(String source, String projectPath, String binariesFolder, String unitName) {
 	  
	
		List<String> jars = getJarsInfolder(binariesFolder);
		String[] classPath = new String[jars.size()];
		int i = 0;
		for(i = 0; i < classPath.length; i++){
			classPath[i] = binariesFolder+File.separator+jars.get(i);
		}
		//classPath[i] = "/Users/mariolinares/Documents/liminal/projects/Ferias/FeriasDNP/bin/classes/co/gov/dnp/ferias";
		String[] sources = {  projectPath };
//		ASTParser parser = ASTParser.newParser(AST.JLS3);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setEnvironment(classPath, sources, new String[] { "UTF-8" }, true);
		
		Hashtable<String, String> options = JavaCore.getOptions();
//	    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
	    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
	  
		
	    parser.setCompilerOptions(options);
	    parser.setUnitName(unitName);
	    
		return (CompilationUnit) parser.createAST(null);
	}

	public static List<String> getJarsInfolder(String binariesFolder){
		List<String> jars = new ArrayList<String>();
		String[] files = (new File(binariesFolder)).list();
		for (String file : files) {
			if(file.endsWith(".jar")){
				jars.add(file);
			}
		}
		return jars;
	}
	
	public static HashSet<MethodCallVO> getMethodCallsFromCU(CompilationUnit cu, HashSet<String> targetCalls){
		MethodCallVisitor mcVisitor = new MethodCallVisitor();
		mcVisitor.setTargetCalls(targetCalls);
		cu.accept(mcVisitor);
		return  mcVisitor.getCalls();
	}
	
	public static HashSet<MethodDeclarationVO> getMethodDeclarationsFromCU(CompilationUnit cu, HashSet<String> targetDeclarations){
		MethodDeclarationVisitor mdVisitor = new MethodDeclarationVisitor(targetDeclarations);
		cu.accept(mdVisitor);
		return mdVisitor.getDeclarations();
	}
	
	public static HashSet<MethodCallVO> getClassInstanceCreationsFromCU(CompilationUnit cu, HashSet<String> targetCalls){
		ClassInstanceVisitor ciVisitor = new ClassInstanceVisitor();
		ciVisitor.setTargetCalls(targetCalls);
		cu.accept(ciVisitor);
		return  ciVisitor.getCalls();
	}
	
	
	

}
