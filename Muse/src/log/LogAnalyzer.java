package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.support.FileUtility;

/**
 * Log Analyzer helps to detect and prepare Source files only containing true
 * positives
 * 
 * @author Amit Seal Ami
 *
 */
public class LogAnalyzer {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;

	/**
	 * Iterates through the modified file directory and compares the occurrence of
	 * "dataLeak" in the file and the runtime log to remove false positive data leaks.
	 * Then alters the files in mutants folder with the respective changes.
	 * @param args logs to be compared and code to be ammended
	 * @throws IOException IO for logs and code failed
	 * @author Yang Zhang
	 * @throws FileNotFoundException file not found
	 */
	public void runLogAnalysis(String[] args) throws FileNotFoundException, IOException {
		// Incomplete arguments
		if (args.length != 3) {
			printArgumentError();
			return;
		}
		
		testString = FileUtility.readSourceFile(args[0].toString()).toString();
		//modified files directory
		File mod_file_path = new File(args[1].toString());
		File [] mod_files = mod_file_path.listFiles();
		
		//mutant folder directory
		File mutant_file_path = new File(args[2].toString());
		File [] mutated_files = mutant_file_path.listFiles();

		for (File mod_file : mod_files) {
			try {
				if (mod_file.getName().endsWith(".txt")) {
					sourceString = FileUtility.readSourceFile(mod_file.getAbsolutePath()).toString();
					LogAnalyzer log = new LogAnalyzer();
					String new_analyzed_file = log.removeUnusedIndicesFromSource(LogAnalyzer.sourceString,
							log.getIndicesFromLogs(LogAnalyzer.testString));
					System.out.println(new_analyzed_file);
					
					//traverse mutants folder to replace the existing modified code
					//the mutant folder filepath should link straight to the directory 
					//containing the mutated files being analyzed.
					String originalName = mod_file.getName().replaceAll(".txt", ".java");
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
	 * Extracts indices of true positive data leaks
	 * 
	 * @param string source code contents from formatted text file with new lines
	 * @return set of indices based on true positive leaks
	 * @author Amit Seal Ami
	 *
	 */
	public Set<Integer> getIndicesFromLogs(String string) {
		Set<Integer> indices = new HashSet<Integer>();
		String[] lines = string.split("\n");
		for (String line : lines) {
			if (line.contains("leak-")) {
				indices.add(Integer.parseInt(line.split("leak-")[1].split(":")[0]));
			}
		}
		return indices;
	}

	/**
	 * @param string         String content of the source file
	 * @param indicesFromLog extracted indices of dataleaks from log file
	 * @return String content of file that only contains true positive for
	 *         reachability
	 * @author Amit Seal Ami
	 */
	public String removeUnusedIndicesFromSource(String string, Set<Integer> indicesFromLog) {
		String[] lines = string.split("\n");
		String outputLines = "";
		boolean addThrowAwayLine = false;
		for (String line : lines) {
			if (line.contains("String dataLeAk")) {
				int index = Integer.parseInt(line.split("dataLeAk")[1].split(" =")[0]);
				if (indicesFromLog.contains(index)) {
					outputLines += line + "\n";
					addThrowAwayLine = true;
				}
			} else if (line.contains("Object throwawayLeAk")) {
				if (addThrowAwayLine) {
					outputLines += line + "\n";
					addThrowAwayLine = false;
				}
			} else {
				outputLines += line += "\n";
			}
		}
		return outputLines;
	}
	
	private void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
		System.out.println("Argument List:");
		System.out.println("1. Runtime Logs File");
		System.out.println("2. Modified Files Directory");
		System.out.println("3. Mutants path");
	}

	public static void main(String[] args) throws IOException {
		
		new LogAnalyzer().runLogAnalysis(args);

	}

}
