package edu.wm.cs.mplus.detectors.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wm.cs.mplus.detectors.TextBasedDetector;
import edu.wm.cs.mplus.helper.IntegerGenerator;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.DifferentActivityMutationLocation;
import edu.wm.cs.mplus.model.location.MutationLocation;

public class WrongMainActivityDetector extends TextBasedDetector {

	private static String MANIFEST = "AndroidManifest.xml";
	private static String MAIN_ACTION = "android.intent.action.MAIN";

	public WrongMainActivityDetector() {
		this.type = MutationType.WRONG_MAIN_ACTIVITY;
	}

	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		List<MutationLocation> locations = new ArrayList<MutationLocation>();

		String mainActivity = "";
		List<String> otherActivities = new ArrayList<String>();

		String path = rootPath + File.separator + MANIFEST;

		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();


		NodeList nodeList = doc.getElementsByTagName("activity");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			Node nameAttr = node.getAttributes().getNamedItem("android:name");

			if(nameAttr != null){
				String activityName = nameAttr.getNodeValue();

				Element activityElement = (Element) node;
				NodeList actions = activityElement.getElementsByTagName("action");
				if(isMainActivity(actions)){
					mainActivity = activityName;
				} else {
					otherActivities.add(activityName);
				}
			}
		}

		//Main activity not found!
		if(mainActivity.equals("")){
			return locations;
		}

		// Read Source Code
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		int startLine = 0;
		int startCol = 0;
		int endCol = 0;
		int lineNum = 0;

		boolean isActivityTag = false;
		boolean isMainActivity = false;
		int currentLine = 1;
		while ((line = reader.readLine()) != null) {

			if (line.contains("<activity")) {
				startLine = currentLine;
				isActivityTag = true;
			}
			if (isActivityTag) {
				if(line.contains("android:name") && line.contains(mainActivity)){
					startCol = line.indexOf("\""+mainActivity)+1;
					endCol = startCol+mainActivity.length();
					isMainActivity = true;
					lineNum = currentLine;
				}
				if (line.contains(">")) {
					if(isMainActivity){
						locations.add(buildLocation(path, startLine, currentLine, startCol, endCol, lineNum, otherActivities));
						
						
						isMainActivity =false;
					}
					isActivityTag = false;
				}
			}

			currentLine++;
		}
		reader.close();

		return locations;
	}


	private static boolean isMainActivity(NodeList actions){

		for (int i = 0; i < actions.getLength(); i++) {
			Node node = actions.item(i);
			Node nameAttr = node.getAttributes().getNamedItem("android:name");

			if(nameAttr != null){
				String actionValue = nameAttr.getNodeValue();
				if(actionValue.equals(MAIN_ACTION)){
					return true;
				}
			}
		}

		return false;
	}


	private MutationLocation buildLocation( String path, int startLine, int currentLine, int startCol, int endCol, int lineNum, List<String> otherActivities) {

		int endLine = currentLine;

		DifferentActivityMutationLocation location = new DifferentActivityMutationLocation();
		location.setFilePath(path);
		location.setStartLine(startLine-1);
		location.setEndLine(endLine-1);
		location.setStartColumn(startCol);
		location.setEndColumn(endCol);
		location.setType(this.getType());
		location.setLine(lineNum-1);
		
		//Select random other activity to replace the main one
		int randomIndex = IntegerGenerator.generateRandomInt(0, otherActivities.size()-1);
		location.setOtherActivity(otherActivities.get(randomIndex));
		
		return location;

	}

}
