package edu.wm.cs.mplus.helper;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import edu.wm.cs.mplus.model.location.ElementLocation;

public class XMLLocator {
	
	
	
	public static ElementLocation getStringElementLocation(String element, Node node, String filePath){
		
		ElementLocation elementLocation = new ElementLocation();
		ElementLocation nodeLocation = getNodeLocation(node, filePath);
		List<String> sourceLines = FileHelper.readLines(filePath);
		
		int start = nodeLocation.getStartLine();
		int end = nodeLocation.getEndLine();
		
		
		for(int i=start; i< end+1; i++){
			String currLine = sourceLines.get(i);
			if(currLine.contains(element)){
				int startColumn = currLine.indexOf(element);
				int endColumn = startColumn + element.length();

				elementLocation.setLine(i);
				elementLocation.setStartColumn(startColumn);
				elementLocation.setEndColumn(endColumn);
				
				return elementLocation;
			}
		}
		
		return null;
	}
	
	
	
	public static ElementLocation getNodeLocation(Node node, String filePath){
		
		ElementLocation location = new ElementLocation();
		
		List<String> sourceLines = FileHelper.readLines(filePath);
		List<String> nodeLines = getNodeSource(node);
		
		for(int i=0; i< sourceLines.size(); i++){
			String currLine = sourceLines.get(i);
			if(currLine.contains(nodeLines.get(0))){
				//If there is a match on the first line, check exact match for the remaining lines
				if(checkMatch(sourceLines, nodeLines, i)){
					location.setStartLine(i);
					int end = i + nodeLines.size() - 1;
					location.setEndLine(end);
				}
			}
		}
		
		
		return location;
	}
	
	
	
	
	public static boolean checkMatch(List<String> sourceLines, List<String> nodeLines, int index){
		
		//The previous match (contain) is enough
		if(nodeLines.size() == 1){
			return true;
		}
		
		//Check the remaining lines for exact match
		int start = index+1;
		int end = index + nodeLines.size();
		int nodeLineIndex = 1;
		
		for(int i = start; i < end; i++){
			String nodeLine = nodeLines.get(nodeLineIndex);
			String sourceLine = sourceLines.get(i);
			
			if(!nodeLine.equals(sourceLine)){
				return false;
			}
			nodeLineIndex++;
		}
		
		return true;
	}
	
	
	public static List<String> getNodeSource(Node node) {
	    DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
	    LSSerializer lsSerializer = lsImpl.createLSSerializer();
	    lsSerializer.getDomConfig().setParameter("xml-declaration", false);
	    StringBuilder sb = new StringBuilder();
	    sb.append(lsSerializer.writeToString(node));
	    
	    String sourceNode = sb.toString();
	    System.out.println(sourceNode);
	    String lines[] = sourceNode.split("\\r?\\n");
	    List<String> sourceLines = Arrays.asList(lines);
	    return sourceLines; 
	}
	
	
	
	public static String innerXml(Node node) {
	    DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
	    LSSerializer lsSerializer = lsImpl.createLSSerializer();
	    lsSerializer.getDomConfig().setParameter("xml-declaration", false);
	    NodeList childNodes = node.getChildNodes();
	    StringBuilder sb = new StringBuilder();
	    sb.append(lsSerializer.writeToString(node));
	    /*for (int i = 0; i < childNodes.getLength(); i++) {
	       sb.append(lsSerializer.writeToString(childNodes.item(i)));
	    }*/
	    return sb.toString(); 
	}

}
