package edu.wm.cs.muse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.muse.dataleak.operators.ComplexReachability;
import edu.wm.cs.muse.dataleak.operators.ReachabilityOperator;
import edu.wm.cs.muse.dataleak.operators.TaintSinkOperator;
import edu.wm.cs.muse.dataleak.operators.TaintSourceOperator;
import edu.wm.cs.muse.dataleak.operators.ScopeSourceOperator;
import edu.wm.cs.muse.dataleak.operators.ScopeSinkOperator;
import edu.wm.cs.muse.dataleak.schemas.ComplexReachabilitySchema;
import edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema;
import edu.wm.cs.muse.dataleak.schemas.TaintSinkSchema;
import edu.wm.cs.muse.dataleak.schemas.TaintSourceSchema;
import edu.wm.cs.muse.dataleak.schemas.ScopeSourceSchema;
import edu.wm.cs.muse.dataleak.schemas.ScopeSinkSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.mdroid.ASTHelper;
//import log.LeakRemover;
//import log.LogDiff;

import org.apache.commons.cli.CommandLine;

/**
 *
 * @author Richard Bonett
 * @since October 12, 2017
 */
public class Muse {

	ASTRewrite rewriter;
	CommandLine cmd = null;
	// TODO: Does not handle anonymous declarations and try_catch clauses well.
	// currently just ignores such methods.
	// TODO: Make schema for inserting leaks in static methods, since regular
	// operators won't work well with static ones?
	// TODO: does not handle enum well since enum methods are considered static and
	// not detected in java ast as static

	public void runMuse(String[] args) throws MalformedTreeException, BadLocationException {
		FileUtility.setupMutantsDirectory("");
		Collection<File> files = FileUtils.listFiles(new File(Arguments.getRootPath()), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);
		for (File file : files) {
			try {
				if (file.getName().endsWith(".java")
						&& file.getCanonicalPath().contains(Arguments.getAppName().replace(".", "/"))
						&& !file.getName().contains("EmmaInstrumentation.java")
						&& !file.getName().contains("FinishListener.java")
						&& !file.getName().contains("InstrumentedActivity.java")
						&& !file.getName().contains("InstrumentedTest.java")
						&& !file.getName().contains("UnitTest.java")
						&& !file.getName().contains("SMSInstrumentedReceiver.java")) {
					System.out.println("In file: " + file.getName());
					Arguments.setFileName(file.getName());
					// System.out.println("PROCESSING: " + file.getAbsolutePath());
					String source = FileUtility.readSourceFile(file.getAbsolutePath()).toString();
					// Creates an abstract syntax tree.
					CompilationUnit root = ASTHelper.getAST(source, Arguments.getBinariesFolder(),
							Arguments.getRootPath());
					// Creates a new instance for describing manipulations of the given AST.
					rewriter = ASTRewrite.create(root.getAST());
					operatorExecution(root, rewriter, source, file, getOperatorType(Arguments.getOperator()));
				}
			} catch (IOException e) {
				System.err
						.println(String.format("ERROR PROCESSING \"%s\": %s", file.getAbsolutePath(), e.getMessage()));
				return;
			}
		}
	}

	private OperatorType getOperatorType(String inputOperator) {
		// TAINTSOURCE, TAINTSINK, SCOPESOURCE, SCOPESINK, REACHABILITY, and COMPLEXREACHABILITY
		System.out.println("Input operator: " + inputOperator);
		switch (inputOperator) {
		case "TAINTSOURCE":
			return OperatorType.TAINTSOURCE;
		case "TAINTSINK":
			return OperatorType.TAINTSINK;
		case "SCOPESOURCE":
			return OperatorType.SCOPESOURCE;
		case "SCOPESINK":
			return OperatorType.SCOPESINK;
		case "REACHABILITY":
			return OperatorType.REACHABILITY;
		case "COMPLEXREACHABILITY":
			return OperatorType.COMPLEXREACHABILITY;
		default:
			printArgumentError();
			System.exit(-1);
			break;
		}
		return null;
	}

	/**
	 * Converts all modifications recorded by this rewriter into an object
	 * representing the the corresponding text edits to the source of a ITypeRoot
	 * from which the AST was created from. The type root's source itself is not
	 * modified by this method call.
	 * 
	 * @author Amit Seal Ami
	 * @param file   where it will be written
	 * @param source is the content of source
	 * @throws BadLocationException
	 * @throws IOException
	 */
	private void applyChangesToFile(File file, String source, ASTRewrite rewriter)
			throws BadLocationException, IOException {
		Document sourceDoc = new Document(source);

		TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
		// Applies the edit tree rooted by this edit to the given document.
		edits.apply(sourceDoc);
		FileUtils.writeStringToFile(file, sourceDoc.get(), false);
		rewriter = null;
	}

	/**
	 * Uses the rewriter to create an AST for the schema to utilize then creates a
	 * new instance to manipulate the AST. The root node then accepts the schema
	 * visitor on the visit. The rewriter implements the modifications changes made
	 * by the operator to the AST
	 * 
	 * @param root         is the compilation unit based root of the AST
	 * @param rewriter     ASTRewrite object that holds the changes to the AST
	 * @param file			mutant
	 * @param source		source code string
	 * @param operatorType is the type of operator being executed: sink, source,
	 *                     reachability, or taint
	 * @throws IOException IO failed
	 * @throws BadLocationException local file location cannot be found
	 * @throws MalformedTreeException creation of ASTRewrite failed
	 */
	public void operatorExecution(CompilationUnit root, ASTRewrite rewriter, String source, File file,
			OperatorType operatorType) throws MalformedTreeException, BadLocationException, IOException {

		File temp_file;
		String newSource;
		CompilationUnit newRoot;
		switch (operatorType) {
		case TAINTSINK:
			TaintSourceSchema sourceSchema_s = new TaintSourceSchema();
			root.accept(sourceSchema_s);
			TaintSourceOperator sourceOperator_s = new TaintSourceOperator(rewriter, sourceSchema_s.getNodeChanges(),file.getAbsolutePath());
			rewriter = sourceOperator_s.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			String sink_temp_file_path = "test/temp/temp_file.java";
			temp_file = new File(sink_temp_file_path);
			tempFileWriter(root, rewriter, source, temp_file);

			newSource = FileUtility.readSourceFile(sink_temp_file_path).toString();
			newRoot = ASTHelper.getAST(newSource, Arguments.getBinariesFolder(), "test/temp/");
			rewriter = null;
			root = newRoot;
			source = newSource;
			rewriter = ASTRewrite.create(root.getAST());
			
			TaintSinkSchema taintSinkSchema = new TaintSinkSchema();
			root.accept(taintSinkSchema);
			TaintSinkOperator taintSinkOperator = new TaintSinkOperator(rewriter, taintSinkSchema.getNodeChanges(),file.getAbsolutePath());
			rewriter = taintSinkOperator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			Files.delete(temp_file.toPath());
			break;

		case TAINTSOURCE:
			TaintSourceSchema taintSourceSchema = new TaintSourceSchema();
			root.accept(taintSourceSchema);
			TaintSourceOperator taintSourceOperator = new TaintSourceOperator(rewriter, taintSourceSchema.getNodeChanges(),file.getAbsolutePath());
			rewriter = taintSourceOperator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			break;

		case REACHABILITY:
			ReachabilitySchema reachabilitySchema = new ReachabilitySchema();
			root.accept(reachabilitySchema);
			ReachabilityOperator reachabilityOperator = new ReachabilityOperator(rewriter,
					reachabilitySchema.getNodeChanges(),"");
			rewriter = reachabilityOperator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			break;

		case SCOPESOURCE:
			ScopeSourceSchema scopeSourceSchema = new ScopeSourceSchema();
			root.accept(scopeSourceSchema);
			ScopeSourceOperator scopeSourceOperator = new ScopeSourceOperator(rewriter, scopeSourceSchema.getNodeChanges(),file.getAbsolutePath());
			rewriter = scopeSourceOperator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			break;

		case SCOPESINK:
			ScopeSourceSchema taintSchema_ts = new ScopeSourceSchema();
			root.accept(taintSchema_ts);
			ScopeSourceOperator taintOperator_ts = new ScopeSourceOperator(rewriter, taintSchema_ts.getNodeChanges(),file.getAbsolutePath());
			rewriter = taintOperator_ts.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			String taintsink_temp_file_path = "test/temp/temp_file_taintsink.java";
			temp_file = new File(taintsink_temp_file_path);
			tempFileWriter(root, rewriter, source, temp_file);

			newSource = FileUtility.readSourceFile(taintsink_temp_file_path).toString();
			newRoot = ASTHelper.getAST(newSource, Arguments.getBinariesFolder(), "test/temp/");
			rewriter = null;
			root = newRoot;
			source = newSource;
			rewriter = ASTRewrite.create(root.getAST());

			ScopeSinkSchema scopeSinkSchema = new ScopeSinkSchema();
			root.accept(scopeSinkSchema);
			ScopeSinkOperator operator = new ScopeSinkOperator(rewriter, scopeSinkSchema.getFieldNodeChanges(),
					scopeSinkSchema.getMethodNodeChanges(), file.getAbsolutePath());
			rewriter = operator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			Files.delete(temp_file.toPath());
			break;
		case COMPLEXREACHABILITY:
			ComplexReachabilitySchema complexSchema = new ComplexReachabilitySchema();
			root.accept(complexSchema);
			ComplexReachability complexOperator = new ComplexReachability(rewriter, complexSchema.getNodeChanges());
			rewriter = complexOperator.InsertChanges();
			applyChangesToFile(file, source, rewriter);
			break;

		}
	}

	public ASTRewrite tempFileWriter(CompilationUnit root, ASTRewrite rewriter, String source, File file)
			throws MalformedTreeException, BadLocationException, IOException {
		File temp_file = new File("test/temp/temp_file.java");

		// Applies the edit tree rooted by this edit to the given document.
		// edits.apply(sourceDoc);
		Document tempDocument = new Document(source);
		TextEdit tempEdits = rewriter.rewriteAST(tempDocument, null );

		tempEdits.apply(tempDocument);
		FileUtils.writeStringToFile(temp_file, tempDocument.get(), false);
		FileUtils.writeStringToFile(file, tempDocument.get(), false);

		// TaintSinkSchema
		source = FileUtility.readSourceFile(temp_file.getAbsolutePath()).toString();
		rewriter = null;
		root = ASTHelper.getAST(tempDocument.get(), Arguments.getBinariesFolder(), "test/temp/");
		return rewriter = ASTRewrite.create(root.getAST());
	}

	public ASTRewrite tempExecution(CompilationUnit root, ASTRewrite rewriter) {

		TempSchema tempSchema = new TempSchema();
		root.accept(tempSchema);
		return rewriter;
	}
	
	

	private static void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
		System.out.println("Values need to be defined in config.properties in order to run Muse:");
		System.out.println("lib4ast: -----------");
		System.out.println("appSrc: -----------");
		System.out.println("appName: -----------");
		System.out.println("output: -----------");
		System.out.println("operatorType: TAINTSINK, SCOPESINK, REACHABILITY or COMPLEXREACHABILITY");
		System.out.println("source: -----------");
		System.out.println("sink: -----------");
		System.out.println("varDec: -----------");
		System.out.println("Please provide the path to the config.properties file on command line");
	}

//	public static void main(String[] args) throws Exception {
//        // defaults scenario, if the user does not give a keyword and only gives config file, run Muse normally
//		if (args.length == 1) {
//			if (!args[0].endsWith(".properties")) {
//				printArgumentError();
//				// if no config file given, generate one
//				System.out.println("\nNo properties file specified. Generate new file? (y/n)");
//				Scanner scanner = new Scanner(System.in);
//				char response = scanner.next().charAt(0);
//				if (response == 'y') {
//					File source = new File("src/edu/wm/cs/muse/sample_config.properties");
//					File dest = new File(System.getProperty("user.dir") + File.separatorChar + "sample_config.properties");
//					FileUtils.copyFile(source, dest);
//					System.out.println("Properties file created at " + System.getProperty("user.dir") + ".");
//				}
//				return;
//			}
//			if (Arguments.extractArguments(args[0]) < 0) {
//					printArgumentError();
//					System.out.println("Why oh why!");
//					return;
//			}
//			Arguments.extractArguments(args[0]);
//			new Muse().runMuse(args);
//
//	    //if the user does give a keyword, check the keyword and run accordingly
//		} else if (args.length == 2) {
//			if (!args[1].endsWith(".properties")) {
//				printArgumentError();
//				return;
//			}
//			Arguments.extractArguments(args[1]);
//	        switch (args[0]) {
//                case "mutate":
//                    new Muse().runMuse(args);
//                    break;
//
//                case "logAnalyze":
////                	String comparisonPath = new LogDiff().main(args[1]).getAbsolutePath();
////                	String[] removerArgs = {args[1], comparisonPath};
////                	new LeakRemover().main(removerArgs);
//                	break;
//
//				default:
//					new Muse().runMuse(args);
//					break;
//            }
//		} else {
//				printArgumentError();
//				return;
//		}
//	}
}
