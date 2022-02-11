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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InvalidColorDetector extends TextBasedDetector {

	private static String COLORS = "colors.xml";
	
	
	public InvalidColorDetector(){
		this.type = MutationType.INVALID_COLOR;
	}
	
	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
		List<String> colorHex = new ArrayList<String>();


		String path = rootPath + File.separator+"res"+File.separator+"values"+File.separator + COLORS;

		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		// Get all activities
		NodeList nodeList = doc.getElementsByTagName("color");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			colorHex.add(node.getTextContent());
 		}

		// Read Source Code
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(path)));
		String line = null;
		int startLine = 0;
		int startCol = 0;
		int endCol = 0;
		int lineNum = 0;
		boolean isColorTag = false;
		int currentLine = 0;
		while ((line = reader.readLine()) != null) {

			if (line.contains("<color")) {
				startLine = currentLine;
				isColorTag = true;
			}
			if (isColorTag) {
				for(String color : colorHex){
					if(line.contains(color)){
						startCol = line.indexOf(color)+1;
						endCol = startCol+color.length()-1;
						lineNum = currentLine;
					}
				}
				if (line.contains(">")) {
					locations.add(buildLocation(path, startLine, currentLine, startCol, endCol, lineNum));
					isColorTag = false;
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
