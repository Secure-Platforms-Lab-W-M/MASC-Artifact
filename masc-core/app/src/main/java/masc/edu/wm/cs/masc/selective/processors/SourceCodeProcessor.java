package edu.wm.cs.mplus.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import edu.wm.cs.mplus.detectors.ast.locator.Locator;
import edu.wm.cs.mplus.detectors.ast.locator.LocatorFactory;
import edu.wm.cs.mplus.detectors.code.visitors.ASTHelper;
import edu.wm.cs.mplus.detectors.code.visitors.MethodDeclarationVO;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.mplus.detectors.code.visitors.MethodCallVO;
import edu.wm.cs.mplus.operators.OperatorBundle;

public class SourceCodeProcessor {

	private HashSet<String> targetApis ;
	private HashSet<String> targetDeclarations ;

	private HashMap<String, List<Integer>> targetApisAndMutypes ;
	private HashMap<String, List<Integer>> targeDeclarationsAndMutypes ;

	private List<String> activities;
	private List<String> serializableClasses;
	private List<String> parcelableClasses;

	private OperatorBundle operatorBundle;

	private static SourceCodeProcessor instance = null;


	public static SourceCodeProcessor getInstance() {
		if(instance == null) {
			instance = new SourceCodeProcessor(null);
		}
		return instance;
	}


	public SourceCodeProcessor(OperatorBundle operatorBundle){
		this.operatorBundle = operatorBundle;
		targetApis = new HashSet<String>();
		targetDeclarations = new HashSet<String>();
		targetApisAndMutypes = new HashMap<>();
		targeDeclarationsAndMutypes = new HashMap<>();
		activities = new ArrayList<>();
		serializableClasses = new ArrayList<>();
		parcelableClasses = new ArrayList<>();

		//Reading target calls from properties file
		readTargetCallsFromFile();

		//Reading method declarations from properties file
		readTargetDeclarationsFromFile();

		instance = this;
	}

	private void readTargetCallsFromFile() {
		ResourceBundle bundle = ResourceBundle.getBundle("target-apis");
		Set<String> types = bundle.keySet();
		String apiCalls = null;

		for (String type : types) {
			System.out.println(MutationType.valueOf(Integer.parseInt(type)).getName()+ ": "+operatorBundle.isOperatorSelected(type));
			
			//Add only target calls for selected operators
			if(!operatorBundle.isOperatorSelected(type)) {
				continue;
			}
			
			apiCalls = bundle.getString(type);
			String[] calls = apiCalls.split(",");

			for(String apiCall : calls){
				System.out.println("API_ID: "+type+"; API_CALL: "+apiCall);
				targetApis.add(apiCall.trim());

				if(!targetApisAndMutypes.containsKey(apiCall.trim())){
					targetApisAndMutypes.put(apiCall.trim(), new ArrayList<Integer>());
				}
				targetApisAndMutypes.get(apiCall.trim()).add(Integer.parseInt(type));
			}
		}
	}


	private void readTargetDeclarationsFromFile() {
		ResourceBundle bundle = ResourceBundle.getBundle("target-declarations");
		Set<String> types = bundle.keySet();
		String methodDeclarations = null;
		for (String type : types) {
			
			System.out.println(MutationType.valueOf(Integer.parseInt(type)).getName()+ ": "+operatorBundle.isOperatorSelected(type));

			//Add only declarations for selected operators
			if(!operatorBundle.isOperatorSelected(type)) {
				continue;
			}
			
			methodDeclarations = bundle.getString(type);
			
			String[] declarations = methodDeclarations.split(",");
			for (String declaration : declarations) {
				
			
				targetDeclarations.add(declaration.trim());
	
				if(!targeDeclarationsAndMutypes.containsKey(declaration.trim())){
					targeDeclarationsAndMutypes.put(declaration.trim(), new ArrayList<Integer>());
				}
				targeDeclarationsAndMutypes.get(declaration.trim()).add(Integer.parseInt(type));
			}
		}
	}

	public HashMap<MutationType, List<MutationLocation>> processFolder(String folderPath, String binariesFolder, String packageName) throws IOException{
		HashMap<MutationType, List<MutationLocation>> locations = new HashMap<>();
		Collection<File> files = FileUtils.listFiles(new File(folderPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : files) {
			if(file.getName().endsWith(".java") && file.getCanonicalPath().contains(packageName.replace(".", File.separator)) && !file.getName().contains("EmmaInstrumentation.java") && !file.getName().contains("FinishListener.java") && !file.getName().contains("InstrumentedActivity.java") && !file.getName().contains("SMSInstrumentedReceiver.java")){
				HashMap<MutationType, List<MutationLocation>> fileLocations = processFile(file.getAbsolutePath(), folderPath, binariesFolder);
				System.out.println(file.getAbsolutePath());
				appendLocations(fileLocations, locations);
			}
		}
		return locations;

	}

	private static void appendLocations(HashMap<MutationType, List<MutationLocation>> source, HashMap<MutationType, List<MutationLocation>> target){

		for(Entry<MutationType, List<MutationLocation>> entry : source.entrySet()){
			List<MutationLocation> sourceLocations = source.get(entry.getKey());
			List<MutationLocation> targetLocations = target.get(entry.getKey());

			if(targetLocations != null){
				targetLocations.addAll(sourceLocations);
			} else {
				targetLocations = sourceLocations;
			}

			target.put(entry.getKey(), targetLocations);
		}

	}

	public HashMap<MutationType, List<MutationLocation>> findExtraInfoRequired(HashMap<MutationType, List<MutationLocation>> locations){

		//Add serializable and parcelable locations
		addSerializableOrParcelableLocations(serializableClasses, MutationType.NOT_SERIALIZABLE, locations);
		addSerializableOrParcelableLocations(parcelableClasses, MutationType.NOT_PARCELABLE, locations);

		LocatorFactory factory = LocatorFactory.getInstance();
		System.out.println("Activites: "+activities.size());
		System.out.println("----------------------");
		System.out.println("AST LOCATIONS FOUND");
		System.out.println("----------------------");
		for(Entry<MutationType, List<MutationLocation>> entry : locations.entrySet()){
			System.out.println("MutationType: "+entry.getKey());
			System.out.println("Locations: "+entry.getValue().size());
		}

		for(Entry<MutationType, List<MutationLocation>> entry : locations.entrySet()){
			System.out.println(entry.getKey());
			Locator locator = factory.getLocator(entry.getKey());
			List<MutationLocation> exactMutationLocations = locator.findExactLocations(entry.getValue());
			entry.setValue(exactMutationLocations);
		}
		System.out.println("----------------------");
		System.out.println("EXACT LOCATIONS");
		System.out.println("----------------------");
		for(Entry<MutationType, List<MutationLocation>> entry : locations.entrySet()){
			System.out.println("MutationType: "+entry.getKey());
			System.out.println("Locations: "+entry.getValue().size());
		}


		return locations;
	}


	private void addSerializableOrParcelableLocations(List<String> classes, MutationType type, HashMap<MutationType, List<MutationLocation>> locations){
		
		//Add only target calls for selected operators
		if(!operatorBundle.isOperatorSelected(type.getId()+"")) {
			return;
		}
		
		if(classes.size() > 0){
			List<MutationLocation> newLocations = new ArrayList<MutationLocation>();
			for(String path : classes){
				MutationLocation loc = new MutationLocation();
				loc.setFilePath(path);
				newLocations.add(loc);
			}

			locations.put(type, newLocations);
		}
	}



	private  HashMap<MutationType, List<MutationLocation>> processFile(String filePath, String projectPath, String binariesFolder){

		HashMap<MutationType, List<MutationLocation>> mutationLocations = new HashMap<>();
		List<MethodCallVO> targetApiCalls = new ArrayList<MethodCallVO>();
		List<MethodDeclarationVO> targetMethodDeclarations = new ArrayList<MethodDeclarationVO>();
		//List<MethodCallVO> classInstantiations = new ArrayList<MethodCallVO>();

		System.out.println("FilePath: " + filePath);

		List<String> lines = new ArrayList<String>();
		String unitName = filePath.substring(filePath.lastIndexOf(File.separator)+1).replace(".java", "");

		MutationLocation location= null;
		List<Integer> muTypes = null;
		MutationType muType = null;

		try {

			//Reading the source code (file)
			StringBuffer source  = readSourceFile(filePath, lines);

			//Getting AST from file
			CompilationUnit cu = ASTHelper
					.getASTAndBindings(source.toString(), projectPath,binariesFolder, unitName);
//			CompilationUnit cu = ASTHelper.getAST(source.toString(),binariesFolder, projectPath);
			ITypeRoot root= cu.getTypeRoot();

			if(cu.types().size() < 1){
				System.out.println(source.toString());
			}
			TypeDeclaration cuType = (TypeDeclaration)cu.types().get(0);
			StringBuilder activityName = new StringBuilder();
			activityName.append(cu.getPackage().getName());
			activityName.append(".");

			if(cuType.getSuperclassType() != null && cuType.getSuperclassType().toString().endsWith("Activity")){

				activityName.append(cuType.getName().getIdentifier());
				activities.add(activityName.toString());
			}

			activityName = new StringBuilder();
			if(cuType.superInterfaceTypes() != null && cuType.superInterfaceTypes().size() > 0){
				List<Type> interfaces = cuType.superInterfaceTypes();
				for (Type type : interfaces) {
					System.out.println("Interface: "+type);
					if(type.toString().endsWith("Serializable")){
						activityName.append(type.toString());
						serializableClasses.add(filePath);
					}else if(type.toString().endsWith("Parcelable")){
						activityName.append(type.toString());
						parcelableClasses.add(filePath);
					}
				}
			}




			IProblem[] problems = cu.getProblems();


			System.out.println("-------------------------------");
			System.out.println("Parsing "+filePath);
			for (IProblem problem : problems) {
				System.err.println(problem.toString());
			}

			//Collecting API calls to targetApis	
			targetApiCalls.addAll(ASTHelper.getClassInstanceCreationsFromCU(cu, targetApis));
			targetApiCalls.addAll(ASTHelper.getMethodCallsFromCU(cu, targetApis));

			//Collecting methodDeclarations
			targetMethodDeclarations.addAll(ASTHelper.getMethodDeclarationsFromCU(cu, targetDeclarations));

			//Getting locations of the API call in the file
			for (MethodCallVO  call : targetApiCalls) {


				muTypes = targetApisAndMutypes.get(call.getFullName());
				for (Integer type : muTypes) {
					location = buildMutationLocation(filePath, lines, cu, call, type);
					muType = MutationType.valueOf(type);
					if(!mutationLocations.containsKey(muType)){
						mutationLocations.put(muType, new ArrayList<MutationLocation>());
					}
					mutationLocations.get(muType).add(location);
				}


			}

			//Getting locations of the method declaration in the file
			
			for (MethodDeclarationVO  call : targetMethodDeclarations) {

				
				muTypes = targeDeclarationsAndMutypes.get(call.getFullName());
				for (Integer type : muTypes) {
					location = buildMutationLocation(filePath, lines, cu, call, type);
					muType = MutationType.valueOf(type);
					if(!mutationLocations.containsKey(muType)){
						mutationLocations.put(muType, new ArrayList<MutationLocation>());
					}
					mutationLocations.get(muType).add(location);
				}


			}

		} catch (FileNotFoundException e) {
			Logger.getLogger(SourceCodeProcessor.class.getName()).severe(
					" File not found " + filePath);


		} catch (IOException e) {
			Logger.getLogger(SourceCodeProcessor.class.getName()).severe(
					" Error reading/writing file " + filePath);
		} catch (ClassCastException e){
			Logger.getLogger(SourceCodeProcessor.class.getName()).severe(
					" Unable to cast TypeDeclaration " + filePath);
		} catch(Exception e){
			Logger.getLogger(SourceCodeProcessor.class.getName()).severe(
					" Runtime Exception while casting TypeDeclaration " + filePath);
		}

		return mutationLocations;
	}

	private StringBuffer readSourceFile(String filePath,  List<String> lines)
			throws FileNotFoundException, IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;

		while((line = reader.readLine()) != null){
			lines.add(line);
			source.append(line).append("\n");
		}
		reader.close();
		return source;
	}

	private MutationLocation buildMutationLocation(String filePath, List<String> lines, CompilationUnit cu,
			MethodCallVO call, Integer muType) {
		MutationLocation location;
		location = new MutationLocation();
		location.setFilePath(filePath);
		location.setLine(cu.getLineNumber(call.getStart()));
		location.setType(MutationType.valueOf( muType) );
		location.setStartColumn(cu.getColumnNumber(call.getStart()));
		location.setStartLine(location.getLine());
		location.setLength(call.getLength());
		location.setEndLine( computeEndLine(lines, location.getLine(), location.getStartColumn(),location.getLength()));

		//Fix to support mutators that assume 0 as the initial line.
		location.setLine( location.getLine() -1);
		location.setStartLine( location.getStartLine() -1);
		location.setStartColumn(location.getStartColumn() -1 );
		location.setEndLine(location.getEndLine() -1);

		System.out.println(call.getFullName()+" line "+location.getStartLine() +" in "+location.getFilePath());
		return location;
	}


	private MutationLocation buildMutationLocation(String filePath, List<String> lines, CompilationUnit cu,
			MethodDeclarationVO declaration, Integer muType) {
		MutationLocation location;
		location = new MutationLocation();
		location.setFilePath(filePath);
		location.setLine(cu.getLineNumber(declaration.getStart()));
		location.setType(MutationType.valueOf( muType) );
		location.setStartColumn(cu.getColumnNumber(declaration.getStart()));
		location.setStartLine(location.getLine());
		location.setLength(declaration.getLength());
		location.setEndLine( computeEndLine(lines, location.getLine(), location.getStartColumn(),location.getLength()));

		//Fix to support mutators that assume 0 as the initial line.
		location.setLine( location.getLine() -1);
		location.setStartLine( location.getStartLine() -1);
		location.setStartColumn(location.getStartColumn() -1 );
		location.setEndLine(location.getEndLine() -1);

		System.out.println(declaration.getFullName()+" line "+location.getStartLine() +" in "+location.getFilePath());
		return location;
	}


	private int computeEndLine(List<String> lines, int startLine, int startColumn, int length) {

		int endLine = startLine-1;
		int count = 0;

		do{
			endLine+=1;
			count += lines.get(endLine-1).length()-startColumn;	
			startColumn = 0;
		}while(count < length);


		return endLine;
	}



	public List<String> getActivities() {
		return activities;
	}



	public List<String> getSerializableClasses() {
		return serializableClasses;
	}


	public List<String> getParcelableClasses() {
		return parcelableClasses;
	}

}