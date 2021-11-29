package masc.edu.wm.cs.masc.muse.dataleak.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import masc.edu.wm.cs.masc.muse.dataleak.DataLeak;

/**
 * This class is introduced for better handling of arguments, and adding
 * documentation to each parameter.
 * 
 * @author Amit Seal Ami
 */
/**
 * @author Amit Seal Ami
 *
 */
public class Arguments {
	private static String binariesFolder;
	private static String rootPath;
	private static String appName;
	private static String mutantsFolder;
	private static String operator;
	private static Boolean testmode = false;
	private static String[] argsList;
	private static Properties prop;
	private static HashMap<String, String> leakMap;
	private static String filename;

	private static String originalRootPath;

	/**
	 * private constructor makes sure that no constructor can ever be used.
	 */
	@SuppressWarnings("unused")
	private Arguments() {

	}

	/**
	 * This extracts and assigns arguments to binariesFolder, rootPath, appName,
	 * mutantsFolder
	 * 
	 * @param args contains the arguments provided through command line
	 */
	public Arguments(String args[]) {

	}

	public static void extractArguments(String[] args) {
		binariesFolder = args[0];
		rootPath = args[1];
		originalRootPath = rootPath;
		appName = args[2];
		mutantsFolder = args[3];
		operator = args[4];
		System.out.println(rootPath);
	}

	public static void extractArguments(File file) {
		try {
			String contentString = FileUtility.readSourceFile(file.getAbsolutePath()).toString();
			extractArguments(contentString.split(" "));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int extractArguments(String path) {
		
		try (InputStream input = new FileInputStream(path)) {
			prop = new Properties();
			prop.load(input);		
		} catch (IOException e) {
			System.out.println(e);
			return -1;
		}
		
		try {
			argsList = extractProperties(prop);
		} catch (Exception e) {
			return -1;
		}
		
		extractArguments(argsList);
		
		return 0;
	}
	
	/*
	 * This method extracts the 5 args Muse needs to run and
	 * places them into a string array
	 * 
	 * This array will be sent to the Arguments class
	 */
	private static String[] extractProperties(Properties properties) throws Exception {

		if (properties.getProperty("lib4ast") == null || properties.getProperty("lib4ast").length() == 0) {
			throw new Exception();
		}
		if (properties.getProperty("appSrc") == null || properties.getProperty("appSrc").length() == 0) {
			throw new Exception();
		}
		if (properties.getProperty("appName") == null || properties.getProperty("appName").length() == 0) {
			throw new Exception();
		}
		if (properties.getProperty("outputDir") == null || properties.getProperty("outputDir").length() == 0) {
			throw new Exception();
		}
		if (properties.getProperty("operatorType") == null || properties.getProperty("operatorType").length() == 0) {
			throw new Exception();
		}

		binariesFolder = properties.getProperty("lib4ast");
		rootPath = properties.getProperty("appSrc");
		originalRootPath = rootPath;
		appName = properties.getProperty("appName");
		mutantsFolder = properties.getProperty("outputDir");
		operator = properties.getProperty("operatorType");
		leakMap = new HashMap<String, String>();

		System.out.println(mutantsFolder);

		if (properties.getProperty("source") != null) {
			leakMap.put("source", properties.getProperty("source"));
		}
		if (properties.getProperty("sink") != null) {
			leakMap.put("sink", properties.getProperty("sink"));
		}
		if (properties.getProperty("varDec") != null) {
			leakMap.put("varDec", properties.getProperty("varDec"));
		}
		setLeaks(getOperatorEnumType(operator), leakMap);
		
		return new String[] {binariesFolder, rootPath, appName, mutantsFolder, operator};
	}

//	public static boolean setLeaks(OperatorType op, String leakPath) {
//		try {
//			String[] leakStrings = FileUtility.readSourceFile(leakPath).toString().split("\\n");
//			// first line read in as leak source string or default leak source string if empty
//			if (leakStrings.length > 0 && !leakStrings[0].isEmpty()) {
//				DataLeak.setSource(op, leakStrings[0]);
//			}
//			// second line read in as leak sink string or default leak sink string if empty
//			if (leakStrings.length > 1 && !leakStrings[1].isEmpty()) {
//				DataLeak.setSink(op, leakStrings[1]);
//			}
//			// third line read in as variable declaration string or default variable declaration string if empty
//			if (leakStrings.length > 2 && !leakStrings[2].isEmpty()) {
//				DataLeak.setVariableDeclaration(op, leakStrings[2]);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
	public static boolean setLeaks(OperatorType op, HashMap<String, String> configData) {
		if (configData.get("source") != null) {
			DataLeak.setSource(op, configData.get("source").toString());
		}
		// second line read in as leak sink string or default leak sink string if empty
		if (configData.get("sink") != null) {
			DataLeak.setSink(op, configData.get("sink").toString());
		}
		// third line read in as variable declaration string or default variable declaration string if empty
		if (configData.get("varDec") != null) {
			DataLeak.setVariableDeclaration(op, configData.get("varDec").toString());
		}
		return true;
	}
	
	public static void setRootPath(String rootPath) {
		Arguments.rootPath = rootPath;
	}
	
	public static void setFileName(String filename) {
		Arguments.filename = filename;
	}

	public static void setTestMode(Boolean mode) {
		testmode = mode;
	}

	public static Boolean getTestMode() {
		return testmode;
	}

	public static void resetRootPath(){rootPath = originalRootPath;}
	
	
	/**
	 * @return the folder that contains the binaries related to lib4ast
	 */
	public static String getBinariesFolder() {
		return binariesFolder;
	}


	/**
	 * @return the path where the source files reside. Source files may be under
	 *         sub-directories in this root path
	 */
	public static String getRootPath() {
		return rootPath;
	}


	
	/**
	 * @return file name, which is used for the placementchecker
	 */
	public static String getFileName() {
		return filename;
	}

	/**
	 * @return the name of the app. Is used for creating folder under Mutants Folder
	 *         for app and for other purposes.
	 */
	public static String getAppName() {
		return appName;
	}

	/**
	 * @return returns the path of the folder where the mutated source files will be
	 *         kept.
	 */
	public static String getMutantsFolder() {
		return mutantsFolder;
	}
	
	/**
	 * @return operator specified by the argument
	 * Acceptable options are: 
	 * TAINTSOURCE, TAINTSINK, SCOPESOURCE, SCOPESINK and REACHABILITY
	 */
	public static String getOperator() {
		return operator;
	}
	
	public static OperatorType getOperatorEnumType(String inputOperator) {
		// TAINTSOURCE, TAINTSINK, SCOPESOURCE, SCOPESINK and REACHABILITY
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
			return null;
		}
	}
	
}
