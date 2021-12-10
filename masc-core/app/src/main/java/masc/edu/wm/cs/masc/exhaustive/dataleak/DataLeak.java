package masc.edu.wm.cs.masc.exhaustive.dataleak;

import java.util.HashMap;
import masc.edu.wm.cs.masc.exhaustive.dataleak.support.OperatorType;

/**
 * @author liz, Ian Wolff
 *
 *         This class contains all the information pertaining to the data leak
 *         security operator. More specifically, it contains the sink and source
 *         for each sink, source, and reachability operator schema.
 * 
 *         Sample data leak formats: Declaration: String dataLeak{{%d}}; Source:
 *         dataLeak{{%d}} =
 *         java.util.Calendar.getInstance().getTimeZone().getDisplayName();
 *         Sink: android.util.Log.d("leak-{{%d}}", dataLeak{{%d}}); Hop:
 *         dataLeak{{%d}} = dataLeak{{%d}};
 * 
 */
public class DataLeak {
	// default source and sink strings
	private static HashMap<OperatorType, String> sourceLeaks = new HashMap<OperatorType, String>() {

		{
			put(OperatorType.REACHABILITY,
					"String cipherName%d =  \"DES\";");


//					"String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();");
			put(OperatorType.COMPLEXREACHABILITY,
					"String cipherName%d =  \"DES\";");
//					"String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();");
			put(OperatorType.SCOPESOURCE,
					"cipherName%d =  \"DES\";");
//					"dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();");
			put(OperatorType.TAINTSOURCE,
					"cipherName%d =  \"DES\";");
//					"dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();");
		}
	};
	private static HashMap<OperatorType, String> sinkLeaks = new HashMap<OperatorType, String>() {
		{
//			put(OperatorType.REACHABILITY, "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);");
			put(OperatorType.REACHABILITY, "try{\n\t"
//		    		+ "System.out.println(\"cipherName-%d\" + "
					+ "android.util.Log.d(\"cipherName-%d\", "
		    		+ "javax.crypto.Cipher.getInstance(cipherName%d).getAlgorithm());\n}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){\n}");
			put(OperatorType.COMPLEXREACHABILITY, 
					"System.out.println(\"leak-%d\", leAkPath%d);");
			put(OperatorType.SCOPESINK,
					"try{\n\t"
				    		+ "android.util.Log.d(\"cipherName-%d-%d\", "
				    		+ "javax.crypto.Cipher.getInstance(cipherName%d).getAlgorithm());\n}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){\n}");
//					"android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);");
			put(OperatorType.TAINTSINK, "try{\n\t"
		    		+ "android.util.Log.d(\"cipherName-%d-%d\", "
		    		+ "javax.crypto.Cipher.getInstance(cipherName%d).getAlgorithm());\n}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){\n}");
//					"android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);");
		}
	};
	private static HashMap<OperatorType, String> variableDeclarations = new HashMap<OperatorType, String>() {
		{
			put(OperatorType.SCOPESOURCE, 
					"String cipherName%d = \"AES/CBC/PKCS5PADDING\";");
//					"String dataLeAk%d = \"%d\";");
			put(OperatorType.TAINTSOURCE,
					"String cipherName%d = \"AES/CBC/PKCS5PADDING\";");
//					"String dataLeAk%d = \"\";");
			put(OperatorType.COMPLEXREACHABILITY, "String dataLeAk%d = dataLeAk%d");
		}
	};

	private static String taintSinkSourceFinalDecl = 
			"final String cipherName%d = \"AES/CBC/PKCS5PADDING\";";
//			"final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";

	public static String getTaintSinkSourceFinalDecl(int count) {
		return String.format(taintSinkSourceFinalDecl, count);
	}

	private static String[] paths = new String[] {
			"String[] leakArRay%d = new String[] {\"n/a\", dataLeAk%d};\n"
					+ "String leAkPath%d = leakArRay%d[leakArRay%d.length - 1];",
			"java.util.HashMap<String, java.util.HashMap<String, String>> leakMaP%d = new java.util.HashMap<String, java.util.HashMap<String, String>>();\n"
					+ "leakMaP%d.put(\"test\", new java.util.HashMap<String, String>());\n"
					+ "leakMaP%d.get(\"test\").put(\"test\", dataLeAk%d);\n"
					+ "String leAkPath%d = leakMaP%d.get(\"test\").get(\"test\");",
			"StringBuffer leakBuFFer%d = new StringBuffer();" + "for (char chAr%d : dataLeAk%d.toCharArray()) {"
					+ "leakBuFFer%d.append(chAr%d);" + "}" + "String leAkPath%d = leakBuFFer%d.toString();",
			"String leAkPath%d;" + "try {" + "throw new Exception(dataLeAk%d);" + "} catch (Exception leakErRor%d) {"
					+ "leAkPath%d = leakErRor%d.getMessage();" + "}" };

	/**
	 * Sets the source leak string for based on the operator specified
	 * 
	 * @param op           is the operator type
	 * @param sourceString is the string to set
	 */
	public static void setSource(OperatorType op, String sourceString) {
		sourceLeaks.replace(getOperatorSource(op), sourceString);
	}

	/**
	 * Sets the sink leak string for based on the operator specified
	 * 
	 * @param op         is the operator type
	 * @param sinkString is the string to set
	 */
	public static void setSink(OperatorType op, String sinkString) {
		sinkLeaks.replace(getOperatorSink(op), sinkString);
	}

	/**
	 * Sets the variable declaration leak string for based on the operator specified
	 * 
	 * @param op         is the operator type
	 * @param sinkString is the string to set
	 */
	public static void setVariableDeclaration(OperatorType op, String sinkString) {
		variableDeclarations.replace(getOperatorSource(op), sinkString);
	}

	/**
	 * Sets the path strings for the ComplexReachability operator
	 * 
	 * @param pathStrings path strings for the ComplexReachability operator
	 */
	public static void setPaths(String[] pathStrings) {
		paths = pathStrings;
	}

	/**
	 * Returns the path strings for the ComplexReachability operator
	 * 
	 * @return String[] paths
	 */
	public static String[] getPaths() {
		return paths;
	}

	/**
	 * Formats the sink string and returns the correct sink string based on the
	 * operator type specified.
	 * 
	 * @param op         is the operator type
	 * @param identifier the identifier for the source
	 * @return the appropriate source for the operator type specified.
	 */
	public static String getSource(OperatorType op, int identifier) {
		return String.format(sourceLeaks.get(getOperatorSource(op)), identifier);
	}

	/**
	 * Formats the sink string and returns the correct sink string based on the
	 * operator type specified.
	 * 
	 * Note that there is no sink defined for the source operator schema, and null
	 * is returned if the schema operator type is specified. Additionally, the sink
	 * operator requires two identifiers to format its sink.
	 * 
	 * @param op               is the operator type
	 * @param sourceIdentifier the identifier for the source
	 * @param sinkIdentifier   the identifier for the sink
	 * @return the appropriate sink for the operator type specified.
	 */
	public static String getSink(OperatorType op, int sourceIdentifier, int sinkIdentifier) {
		return String.format(sinkLeaks.get(getOperatorSink(op)), sourceIdentifier, sinkIdentifier, sourceIdentifier);
	}

	/**
	 * Returns the correct variable declaration string based on the operator type
	 * specified.
	 * 
	 * @param op is the operator type
	 * @return the appropriate variable declaration for the operator type specified.
	 */
	public static String getVariableDeclaration(OperatorType op) {
		return variableDeclarations.get(getOperatorSource(op));
	}

	/**
	 * Returns an unformatted source string based on the operator type specified.
	 * 
	 * @param op is the operator type
	 * @return String unformatted source string based on the operator type specified
	 */
	public static String getRawSource(OperatorType op) {
		return sourceLeaks.get(getOperatorSource(op));
	}

	/**
	 * Returns an unformatted sink string based on the operator type specified.
	 * 
	 * @param op is the operator type
	 * @return String unformatted sink string based on the operator type specified
	 */
	public static String getRawSink(OperatorType op) {
		return sinkLeaks.get(getOperatorSink(op));
	}

	/**
	 * Formats the leak string and returns it. Accepts only OperatorType
	 * Reachability and ComplexReachabilityOperator
	 * 
	 * @param identifier identifier an instance of the global counter utility used
	 *                   to identify the leak string
	 * @param op         OperatorType
	 * @throws IllegalArgumentException Type must be Operator.REACHABILITY or
	 *                                  Operator.COMPLEXREACHABILITY
	 * @return String the string version of a data leak as used by the reachability
	 *         operator schema.
	 */
	public static String getLeak(OperatorType op, int identifier) throws IllegalArgumentException {
		if (op == OperatorType.REACHABILITY)
			return String.format(sourceLeaks.get(op), identifier) + "\n"
					+ String.format(sinkLeaks.get(op), identifier, identifier, identifier);
		else if (op == OperatorType.COMPLEXREACHABILITY) {

			String[] paths = getPaths();
			String source = getSource(OperatorType.COMPLEXREACHABILITY, identifier);
			String sink = String.format(getRawSink(OperatorType.COMPLEXREACHABILITY), identifier, identifier);

			String leak = source + "\n" + String.format(paths[identifier % paths.length], identifier, identifier,
					identifier, identifier, identifier, identifier, identifier) + "\n" + sink;
			return leak;

		} else {
			throw new IllegalArgumentException("Type must be Operator.REACHABILITY or Operator.COMPLEXREACHABILITY ");
		}
	}

	/**
	 * Hopping logic that adds a level of misdirection that certain security
	 * analysis tools can't follow.
	 * 
	 * @param identifierOne id of the first dataleak string
	 * @param identifierTwo id of the second dataleak string
	 * @return OperatorType the "hop" that results in one dataleak string being set
	 *         equal to another.
	 */
	public static String getHop(int identifierOne, int identifierTwo) {
		return String.format(getVariableDeclaration(OperatorType.COMPLEXREACHABILITY), identifierOne, identifierTwo);
	}

	private static OperatorType getOperatorSource(OperatorType op) {
		if (op == OperatorType.SCOPESINK) {
			op = OperatorType.SCOPESOURCE;
		} else if (op == OperatorType.TAINTSINK) {
			op = OperatorType.TAINTSOURCE;
		}
		return op;
	}

	private static OperatorType getOperatorSink(OperatorType op) {
		if (op == OperatorType.SCOPESOURCE) {
			op = OperatorType.SCOPESINK;
		} else if (op == OperatorType.TAINTSOURCE) {
			op = OperatorType.TAINTSINK;
		}
		return op;
	}

}
