package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.LogFormatException;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/**
 * Sinks log analyzer requires two string contents. Log and Source.
 * It can be used with the Scope and Sink operators.
 * Based on the log file, it removes the unused log sinks and only keeps the true positive logs.
 * 
 * @author Amit Seal Ami, Ian Wolff
 * 
 */
public class LogAnalyzer_Taint_Scope {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;
	private static Properties prop;
	private static OperatorType op;
	private static File mod_file_path;
	private static File [] mod_files;
	private static File mutant_file_path;
	private static File [] mutated_files;
	
	/**
	 * Iterates through the modified file directory to remove leak sources, sinks,
	 * and variable declarations that do not appear in the log file. 
	 * Then alters the files in mutants folder with the respective changes.
	 * 
	 * @author Yang Zhang, Ian Wolff
	 * @throws LogFormatException improperly formatted log
	 */
	public void runLogAnalysis() throws LogFormatException {
		for (File mod_file : mod_files) {
			try {
				if (mod_file.getName().endsWith(".java")) {
					sourceString = FileUtility.readSourceFile(mod_file.getAbsolutePath()).toString();
					String new_analyzed_file = analyzeSourceString(sourceString, getLogMaps(testString));
					System.out.println(new_analyzed_file);
					
					//traverse mutants folder to replace the existing modified code
					//the mutant folder filepath should link straight to the directory 
					//containing the mutated files being analyzed.
					String originalName = mod_file.getName();
					for (File mutated_file : mutated_files) {
						if (mutated_file.getName().equals(originalName) == true) 
						{
							Document sourceDoc = new Document(new_analyzed_file);
							FileUtils.writeStringToFile(mutated_file, sourceDoc.get(), false);
						}
					}
				}
			} catch (IOException e) {
				System.err.println(String.format("ERROR PROCESSING \"%s\": %s", mod_file.getAbsolutePath(), e.getMessage()));
				return;
			}
		}
	}

	/**
	 * Analyzes the source string and based on input, removes false positive sources, sinks,
	 * and variable declarations for the Taint or Scope operator. 
	 * 
	 * @param string contains the source code in one string, with multiple new line characters.
	 * @param maps {@link log.LogAnalyzer_Taint_Scope#getLogMaps(String) maps} contains the maps of source and sinks
	 * @return String modified source code
	 * @throws LogFormatException Improperly formatted Log
	 * @author Amit Seal Ami, Ian Wolff
	 */
	public static String analyzeSourceString(String string, Map<Integer, Set<Integer>> maps) throws LogFormatException {
		if(string.length()<10) {
			throw new LogFormatException();
		}
		String[] lines = string.split("\n");
		String outputLines = "";
		String[] rawSource = DataLeak.getRawSource(op).split("%d");
		String[] rawSink = DataLeak.getRawSink(op).split("%d");
		String[] rawVarDec = DataLeak.getVariableDeclaration(op).split("%d");

		for (String line : lines) {
			// removes sinks that do not appear in the log
			if (line.trim().startsWith(rawSink[0])) {
				// isolate the "%d-%d" placeholder index values and split it into two indices
				String[] placeholderVals = line.split(Pattern.quote(rawSink[0]))[1].split(Pattern.quote(rawSink[2]))[0].split("-");
				Integer source = Integer.parseInt(placeholderVals[0].trim());
				Integer sink = Integer.parseInt(placeholderVals[1].trim());
				// only output if sink index is used in logs
				if (maps.get(source) != null && maps.get(source).contains(sink)) {
					outputLines += line + "\n";
				}
			} 
			// removes sources that do not appear in the log
			else if (line.trim().startsWith(rawSource[0])) {
				//isolate the "%d" placeholder index value
				String placeholderVal = line.split(Pattern.quote(rawSource[0]))[1].split("=")[0];
				Integer source = Integer.parseInt(placeholderVal.trim());
				//only output if index is used in logs
				if (maps.containsKey(source)) {
					outputLines += line + "\n";
				}
			}
			// removes variable declarations that do not appear in the log
			else if (line.trim().startsWith(rawVarDec[0])) {
				//isolate the "%d" placeholder index value
				String placeholderVal = line.split(Pattern.quote(rawVarDec[0]))[1].split(Pattern.quote(rawVarDec[1]))[0];
				Integer source = Integer.parseInt(placeholderVal.trim());
				//only output if index is used in logs
				if (maps.containsKey(source)) {
					outputLines += line + "\n";
				}
			}
			// output line regularly
			else {
				outputLines += line + "\n";
			}
		}
		return outputLines;
	}
	
	/**
	 * getLogMaps returns a mapping for each source, along with list of its sinks.
	 * @param allLogs receives all logs in a single string
	 * @return mapping from source to list of sinks
	 * @author Amit Seal Ami
	 */
	public static Map<Integer, Set<Integer>> getLogMaps(String allLogs) {
		String[] lines = allLogs.split("\n");
		Map<Integer, Set<Integer>> maps = new HashMap<Integer, Set<Integer>>();
		for (String line : lines) {
			if (line.contains("leak-")) {
				System.out.println(line);
				String[] source_sink = line.split("leak-")[1].split(":")[0].split("-");
				Integer sourceInd = Integer.parseInt(source_sink[0]);
				Integer sinkInd = Integer.parseInt(source_sink[1]);
				if (!maps.containsKey(sourceInd)) {
					maps.put(sourceInd, new HashSet<Integer>());
				}
				Set<Integer> values = maps.get(sourceInd);
				values.add(sinkInd);
				maps.put(sourceInd, values);
			}
		}
		return maps;
	}
	
	/**
	 * Reads in arguments from a properties file and raises an exception if any are missing.
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void prepareArguments(String[] args) throws FileNotFoundException, IOException {
		if (args.length != 1) {
			printArgumentError();
			return;
		}
		//any non option arguments are passed in 
		Arguments.extractArguments(args[0]);
		try (InputStream input = new FileInputStream(args[0])) {
			prop = new Properties();
			prop.load(input);		
		} catch (IOException e) {
			printArgumentError();
			return;
		}
		if (prop.getProperty("logPath") == null || prop.getProperty("logPath").length() == 0) {
			printArgumentError();
			return;
		} else if (prop.getProperty("appSrc") == null || prop.getProperty("appSrc").length() == 0) {
			printArgumentError();
			return;
		} else if (prop.getProperty("output") == null || prop.getProperty("output").length() == 0) {
			printArgumentError();
			return;
		} else if (prop.getProperty("operatorType") == null || prop.getProperty("operatorType").length() == 0) {
			printArgumentError();
			return;
		}
		//path to log file from Muse for input
		testString = FileUtility.readSourceFile(prop.getProperty("logPath")).toString();
		//path to modified file with inserted leaks for input
		mod_file_path = new File(prop.getProperty("appSrc"));
		mod_files = mod_file_path.listFiles();
		//path to mutant folder directory for output
		mutant_file_path = new File(prop.getProperty("output"));
		mutated_files = mutant_file_path.listFiles();	
		op = Arguments.getOperatorEnumType(prop.getProperty("operatorType"));
	}
	
	private static void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
	}

	public static void main(String[] args) throws Exception {
		prepareArguments(args);
		new LogAnalyzer_Taint_Scope().runLogAnalysis();
	}
}
