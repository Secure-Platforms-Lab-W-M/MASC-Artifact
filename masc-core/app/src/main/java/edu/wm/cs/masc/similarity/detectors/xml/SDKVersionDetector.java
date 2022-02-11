package edu.wm.cs.masc.similarity.detectors.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wm.cs.masc.similarity.detectors.TextBasedDetector;

public class SDKVersionDetector extends TextBasedDetector {

	private static String MANIFEST = "AndroidManifest.xml";

	public SDKVersionDetector() {
		this.type = MutationType.SDK_VERSION;
	}

	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
		List<String> sdkVersion = new ArrayList<String>();


		String path = rootPath + File.separator + MANIFEST;

		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		// Get all activities
		NodeList nodeList = doc.getElementsByTagName("uses-sdk");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NamedNodeMap attrMap = node.getAttributes();
			// Loop through attributes
			for (int j = 0; j < attrMap.getLength(); j++) {
				String nodeName = attrMap.item(j).getNodeName();
				// Get all sdk version
				if(nodeName.equals("android:minSdkVersion") || 
						nodeName.equals("android:targetSdkVersion") ||
						nodeName.equals("android:maxSdkVersion")){

					sdkVersion.add(nodeName);
				}
			}
		}

		// Read Source Code
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		int startCol = 0;
		int endCol = 0;
		int lineNum = 0; 

		boolean isActivityTag = false;
		int currentLine = 0;
		while ((line = reader.readLine()) != null) {

			if (line.contains("<uses-sdk")) {
				isActivityTag = true;
			}
			if (isActivityTag) {
				for(String version : sdkVersion){
					if(line.contains(version)){
						int startSDK = line.indexOf(version) + version.length();
						startCol = line.indexOf("\"", startSDK)+1;
						endCol = line.indexOf("\"", startCol);
						lineNum = currentLine;
						locations.add(buildLocation(path, startCol, endCol, lineNum));
					}
				}
				if (line.contains(">")) {
					isActivityTag = false;
				}
			}

			currentLine++;
		}
		reader.close();

		return locations;
	}
	private MutationLocation buildLocation( String path, int startCol, int endCol, int lineNum) {

		MutationLocation location = new MutationLocation();
		location.setFilePath(path);
		location.setStartColumn(startCol);
		location.setEndColumn(endCol);
		location.setType(this.getType());
		location.setLine(lineNum);
		return location;


	}

}
