package masc.edu.wm.cs.masc.exhaustive.dataleak.support;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;


/**
 * Placement Checker that checks to see if placement of mutation occurs in a valid place
 * @author Jeff Petit-Freres
 *
 */
public class Placementchecker {
	String source_code;
	String source_file;
	public Placementchecker(/*String source_code*/) {
		//this.source_code = source_code;
	}

	/**
	 * Creates temp file based on the root and ast rewriter. This is written to 
	 * temp_file.java
	 * @param astRoot Root of abstract syntax tree
	 * @param rewriter ASTRewriter
	 * @param source_file_name name of source file
	 * @return File temp file
	 */
	public File getTempFile(CompilationUnit astRoot, ASTRewrite rewriter, String source_file_name) {
		File temp_file;
		
		try {
			temp_file = new File("temp_file.java");
			this.source_file = FileUtility.readSourceFile(source_file_name).toString();
			
			try {
			    tempFileWriter(astRoot, rewriter, source_file, temp_file);
				return temp_file;
			} catch (MalformedTreeException e) {
				
				e.printStackTrace();
			} catch (BadLocationException e) {
				
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * This is the method that checks for any compilation errors in source code.
	 * Ignores errors that don't have to do with mutation insertion and compilability
	 * @param temp_file temp file to be checked
	 * @return Boolean result of check
	 * @throws IOException tempfile IO failed
	 */
  public Boolean check(File temp_file) throws IOException {
	  //sets java_home variable
	  try {
		  if (System.getenv("JAVA_HOME")==null) {
				throw new SystemEnvironmentException();
		  }
	   }catch (SystemEnvironmentException e){
			System.out.println("JAVA_HOME not set");
	   }
		System.setProperty("java.home",System.getenv("JAVA_HOME"));
		
		//Sets up compiler to test source code
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    String source_code= FileUtils.readFileToString(temp_file);
	
	    JavaFileObject file = new JavaSourceFromString("",source_code);
	    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
	    CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
	    boolean success = task.call();
	    
	    //Runs through every error that the compiler sees in the compiler
	    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
	   
	    	//System.out.println("Code: "+ diagnostic.getCode() + " with message: " + diagnostic.getMessage(null));
	    	//Has cases for which errors to catch
	    	switch (diagnostic.getCode()){
	    		case "compiler.err.already.defined":
	    			System.out.println("WARNING: Mutation IS ALREADY DEFINED");
	    			//return false;
	    		case "compiler.err.non-static.cant.be.ref":
	    			return false;
	    			

	    		case "compiler.err.prob.found.req":
	    			return false;
	    			
	    	}
	    }
		
	 
	    return true;

  }
  
  /**
   * TempFileWriter from Muse.java. Used to quickly create a temp File from rewriter and ast
   * root
   * @param root CompilationUnit
   * @param rewriter ASTrewrite
   * @param source String
   * @param file mutant
   * @return ASTRewrite mutator
   * @throws MalformedTreeException invalid AST
   * @throws BadLocationException path could not be resolved
   * @throws IOException IOfailes
   */
  public ASTRewrite tempFileWriter(CompilationUnit root, ASTRewrite rewriter, String source, File file)
			throws MalformedTreeException, BadLocationException, IOException {
		File temp_file = new File("output.txt");
		
		// Applies the edit tree rooted by this edit to the given document.
		// edits.apply(sourceDoc);
	
		Document tempDocument = new Document(source);

		TextEdit tempEdits = rewriter.rewriteAST(tempDocument, null);
		tempEdits.apply(tempDocument);
		FileUtils.writeStringToFile(temp_file, tempDocument.get(), false);
		FileUtils.writeStringToFile(file, tempDocument.get(), false);

		// TaintSinkSchema
		source = FileUtility.readSourceFile(temp_file.getAbsolutePath()).toString();
		rewriter = null;
		return rewriter = ASTRewrite.create(root.getAST());
	}

 
}




    




