package edu.wm.cs.masc.similarity.detectors.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import edu.wm.cs.masc.similarity.detectors.TextBasedDetector;
import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InvalidActivityNameDetector extends TextBasedDetector {

	private static String MANIFEST = "AndroidManifest.xml";

	public InvalidActivityNameDetector() {
		this.type = MutationType.INVALID_ACTIVITY_PATH;
	}

	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
		List<String> activityNames = new ArrayList<String>();

		String path = rootPath + File.separator + MANIFEST;

		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		// Get all activities
		NodeList nodeList = doc.getElementsByTagName("activity");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NamedNodeMap attrMap = node.getAttributes();
			// Loop through attributes
			for (int j = 0; j < attrMap.getLength(); j++) {
				if (attrMap.item(j).getNodeName().equals("android:name")) {
					// Get all activity names
					activityNames.add(attrMap.item(j).getNodeValue());
				}
			}
		}

		// Read Source Code
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(path)));
		String line = null;
		int startLine = 0;
		int startCol = 0;
		int endCol = 0;
		int lineNum = 0;
		boolean isActivityTag = false;
		int currentLine = 0;
		boolean found = false;
		while ((line = reader.readLine()) != null) {

			if (line.contains("<activity")) {
				startLine = currentLine;
				isActivityTag = true;
			}
			if (isActivityTag) {
				for(String name : activityNames){
					if(line.contains("android:name") && line.contains(name)){
						startCol = line.indexOf("\""+name)+1;
						endCol = startCol+name.length();
						lineNum = currentLine;
						found = true;
					}
				}
				if (line.contains(">")) {
					if(found){
						locations.add(buildLocation(path, startLine, currentLine, startCol, endCol, lineNum));
						found = false;
					}
					isActivityTag = false;
				}
			}

			currentLine++;
		}
		reader.close();

		return locations;
	}
	private MutationLocation buildLocation( String path, int startLine, int currentLine, int startCol, int endCol, int lineNum) {

		int endLine = currentLine;
		
		MutationLocation location = new MutationLocation();
		location.setFilePath(path);
		location.setStartLine(startLine);
		location.setEndLine(endLine);
		location.setStartColumn(startCol);
		location.setEndColumn(endCol);
		location.setType(this.getType());
		location.setLine(lineNum);
		return location;
		
		
	}

}
