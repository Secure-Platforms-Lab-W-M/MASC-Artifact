package edu.wm.cs.mplus.helper;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import edu.wm.cs.mplus.helper.XMLLocator;
import edu.wm.cs.mplus.model.location.ElementLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLLocatorTest {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub

		File fXmlFile = new File("/home/scratch/mtufano/tmp/xmlfile.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		Node node = doc.getElementsByTagName("staff").item(0);
		Node node2 = doc.getElementsByTagName("firstname").item(1);
		
		ElementLocation location = XMLLocator.getNodeLocation(node, fXmlFile.getAbsolutePath());
		System.out.println("Start: "+location.getStartLine());
		System.out.println("End: "+location.getEndLine());
		
		ElementLocation elementlocation = XMLLocator.getStringElementLocation("low", node2, fXmlFile.getAbsolutePath());
		System.out.println("Line: "+elementlocation.getLine());
		System.out.println("Start Column: "+elementlocation.getStartColumn());
		System.out.println("End Column: "+elementlocation.getEndColumn());

	}
}
	

