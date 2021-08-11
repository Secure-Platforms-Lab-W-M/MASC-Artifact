package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.LogFormatException;


public class LeakRemover {
	//RuntimeLogs.txt
	static String comparisonPath;
	private static Properties prop;
	private static OperatorType op;
	private static File mod_file_path;
	private static File [] mod_files;
	//the mutant folder filepath should link straight to the directory 
	//containing the mutated files being analyzed.
	private static File mutant_file_path;
	private static File [] mutated_files;
	private static ArrayList<String> reachabilityPaths = new ArrayList<String>();
	
	/**
	 * Iterates through the modified file directory and compares the occurrence of
	 * "dataLeak" in the file and the runtime log to remove false positive data leaks.
	 * Then alters the files in mutants folder with the respective changes.
	 * @author Yang Zhang, Ian Wolff
	 * @throws LogFormatException improper log format
	 */
	public void removeLeaks() throws LogFormatException {
		for (File mod_file : mod_files) {
			try {
				if (mod_file.getName().endsWith(".java")) {
					String processed_file = null;
					String fileContent = FileUtility.readSourceFile(mod_file.getAbsolutePath()).toString();
					String logContent = FileUtility.readSourceFile(comparisonPath).toString();
					if (op == OperatorType.REACHABILITY || op == OperatorType.COMPLEXREACHABILITY ) {
						processed_file = removeUnusedLeaksFromSet(fileContent, getIndicesFromLog(logContent));
					} else if (op == OperatorType.TAINTSOURCE 
							|| op == OperatorType.TAINTSINK 
							|| op == OperatorType.SCOPESOURCE 
							|| op == OperatorType.SCOPESINK) {
						processed_file = removeUnusedLeaksFromMap(fileContent, getIndexMapsFromLog(logContent));
					}
					String originalName = mod_file.getName();
					System.out.println(processed_file);
					
					// traverse mutant folder to replace the existing modified code
					for (File mutated_file : mutated_files) {
						if (mutated_file.getName().equals(originalName) == true) {
							Document sourceDoc = new Document(processed_file);
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
	 * Extracts the indices of all leaks present in a formatted log file.
	 * 
	 * @param	logString	content of a formatted log file with new line characters
	 * @return	leakInts	set of leak indices contained by the log file
	 * @author Amit Seal Ami
	 *
	 */
	public Set<Integer> getIndicesFromLog(String logString) {
		Set<Integer> leakInts = new HashSet<Integer>();
		String[] log = logString.split("\n");
		for (String line : log) {
			if (line.contains("leak-")) {
				System.out.println(line);
				// isolates index of a leak in format "leak-[[int]]:"
				leakInts.add(Integer.parseInt(line.split("leak-")[1].split(":")[0]));
			}
		}
		return leakInts;
	}
	
	/**
	 * getLogMaps returns a mapping for each source, along with list of its sinks.
	 * @param logContent receives all logs in a single string
	 * @return mapping from source to list of sinks
	 * @author Amit Seal Ami
	 */
	public static Map<Integer, Set<Integer>> getIndexMapsFromLog(String logContent) {
		String[] log = logContent.split("\n");
		Map<Integer, Set<Integer>> maps = new HashMap<Integer, Set<Integer>>();
		for (String line : log) {
			if (line.contains("leak-")) {
				System.out.println(line);
				String[] source_sink = line.split("leak-")[1].split(":")[0].split("-");
				Integer sourceInt = Integer.parseInt(source_sink[0]);
				Integer sinkInt = Integer.parseInt(source_sink[1]);
				if (!maps.containsKey(sourceInt)) {
					maps.put(sourceInt, new HashSet<Integer>());
				}
				Set<Integer> values = maps.get(sourceInt);
				values.add(sinkInt);
				maps.put(sourceInt, values);
			}
		}
		return maps;
	}
	
	/**Analyzes the source string and based on input, removes false positive sources 
	 * and sinks for the reachability operator as well as variable declarations and
	 * paths for the complex reachability operators.
	 * 
	 * @param string           String content of the source file
	 * @param indicesFromLog   extracted indices of dataleaks from log file
	 * @return String content  of file that only contains true positive for
	 *         reachability
	 * @author Amit Seal Ami, Ian Wolff
	 */
	public String removeUnusedLeaksFromSet(String string, Set<Integer> indicesFromLog) {
		String[] mutatedFile = string.split("\n");
		String outputLines = "";
		boolean pathFound;	
		// rawsink and rawSource separate the substrings before and after the "%d" placeholder
		String[] rawSource = DataLeak.getRawSource(op).split("%d");
		String[] rawSink = DataLeak.getRawSink(op).split("%d");
		
		for (String line : mutatedFile) {
			// removes sinks that do not appear in the log
			if (line.contains(rawSink[0])) {
				// isolates the index from the leak string and removes any leftover whitespace
				String leakInt = line.split(
						Pattern.quote(rawSink[0]))[1].split(Pattern.quote(rawSink[1]))[0].trim();
				if (indicesFromLog.contains(Integer.parseInt(leakInt))) {
					outputLines += line + "\n";
				}
				
			// removes sources that do not appear in the log
			} else if (line.contains(rawSource[0])) {
				String leakInt = line.split(
						Pattern.quote(rawSource[0]))[1].split(Pattern.quote(rawSource[1]))[0].trim();
				if (indicesFromLog.contains(Integer.parseInt(leakInt))) {
					outputLines += line + "\n";
				}
				
			// removes additional paths for complex reachability operator
			} else if (op == OperatorType.COMPLEXREACHABILITY) {
				pathFound = false;
				// checks if a line is equivalent to any of the paths
				for(String pathLine : reachabilityPaths) {
					String[] rawPath = pathLine.split("%d");
					// checks to make sure path has not already been found
					if (pathFound == false && line.trim().startsWith(rawPath[0]) && line.contains(rawPath[1])) {
						pathFound = true;
						String leakInt = line.trim().split(
								Pattern.quote(rawPath[0]))[1].split(Pattern.quote(rawPath[1]))[0];
						// includes line only if index appears in the log file
						if (indicesFromLog.contains(Integer.parseInt(leakInt))) {
							outputLines += line + "\n";
						}
					}
				}
				// if no leak is found, output regularly
				if (!pathFound) {
					outputLines += line + "\n";
				}
			// outputs line regularly
			} else {
				outputLines += line += "\n";
			}
		}
		return outputLines;
	}
	
	/**
	 * Analyzes the source string and based on input, removes false positive sources, sinks,
	 * and variable declarations for the Taint or Scope operator. 
	 * 
	 * @param string contains the source code in one string, with multiple new line characters.
	 * @param maps {@link log.LogAnalyzer_Taint_Scope#getLogMaps(String) maps} contains the maps of source and sinks
	 * @return modified source code
	 * @throws LogFormatException Improperly formatted log
	 * @author Amit Seal Ami, Ian Wolff
	 */
	public static String removeUnusedLeaksFromMap(String string, Map<Integer, Set<Integer>> maps) throws LogFormatException {
		if(string.length()<10) {
			throw new LogFormatException();
		}
		String[] lines = string.split("\n");
		String outputLines = "";
		String[] rawSource = DataLeak.getRawSource(op).split("%d");
		String[] rawSink = DataLeak.getRawSink(op).split("%d");
		String[] rawVarDec = DataLeak.getVariableDeclaration(op).split("%d");

		for (String line : lines) {
			if (line.trim().startsWith(rawSink[0])) {
				// isolate the "%d-%d" placeholder index values and split it into two indices
				String[] leakInts = line.split(Pattern.quote(rawSink[0]))[1].split(Pattern.quote(rawSink[2]))[0].split("-");
				Integer sourceInt = Integer.parseInt(leakInts[0].trim());
				Integer sinkInt = Integer.parseInt(leakInts[1].trim());
				// only output if sink index is used in logs
				if (maps.get(sourceInt) != null && maps.get(sourceInt).contains(sinkInt)) {
					outputLines += line + "\n";
				}
			} 
			// removes sources that do not appear in the log
			else if (line.trim().startsWith(rawSource[0])) {
				//isolate the "%d" placeholder index value
				String leakInt = line.split(Pattern.quote(rawSource[0]))[1].split("=")[0];
				Integer sourceInt = Integer.parseInt(leakInt.trim());
				//only output if index is used in logs
				if (maps.containsKey(sourceInt)) {
					outputLines += line + "\n";
				}
			}
			// removes variable declarations that do not appear in the log
			else if (line.trim().startsWith(rawVarDec[0])) {
				//isolate the "%d" placeholder index value
				String leakInt = line.split(Pattern.quote(rawVarDec[0]))[1].split(Pattern.quote(rawVarDec[1]))[0];
				Integer sourceInt = Integer.parseInt(leakInt.trim());
				//only output if index is used in logs
				if (maps.containsKey(sourceInt)) {
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
	 * Reads in arguments from a properties file and raises an exception if any are missing.
	 * @param args 
	 * @param config
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void prepareArguments(String[] args) throws FileNotFoundException, IOException {
		//path to log file from Muse for input
		//any non option arguments are passed in 
		Arguments.extractArguments(args[0]);
		try (InputStream input = new FileInputStream(args[0])) {
			prop = new Properties();
			prop.load(input);		
		} catch (IOException e) {
			printArgumentError();
			return;
		}
		if (prop.getProperty("appSrc") == null || prop.getProperty("appSrc").length() == 0) {
			printArgumentError();
			return;
		} else if (prop.getProperty("operatorType") == null || prop.getProperty("operatorType").length() == 0) {
			printArgumentError();
			return;
		}	
		// path to comparison log from logDiff
		comparisonPath = args[1];
		// path to modified file with inserted leaks for input
		mod_file_path = new File(prop.getProperty("appSrc"));
		mod_files = mod_file_path.listFiles();
		// path to mutant folder directory for output
		mutant_file_path = new File(prop.getProperty("appSrc"));
		mutated_files = mutant_file_path.listFiles();	
		op = Arguments.getOperatorEnumType(prop.getProperty("operatorType"));
		if (op == OperatorType.COMPLEXREACHABILITY) {
			for(String path : DataLeak.getPaths()) {
				String[] pathLines = path.split("\\n");
				for(String pathLine : pathLines) {
					reachabilityPaths.add(pathLine);
				}
			}
		}
		// set leak strings
		if (prop.getProperty("source") != null) {
			DataLeak.setSource(op, prop.getProperty("source"));
		}
		if (prop.getProperty("sink") != null) {
			DataLeak.setSink(op, prop.getProperty("sink"));
		}
		if (prop.getProperty("varDec") != null) {
			DataLeak.setVariableDeclaration(op, prop.getProperty("varDec"));
		}
	}
	
	private static void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
	}

	public void main(String[] args) throws Exception {
		prepareArguments(args);
		new LeakRemover().removeLeaks();
	}
}
