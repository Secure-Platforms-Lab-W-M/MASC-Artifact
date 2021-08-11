package net.sf.fmj.registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.media.CaptureDeviceInfo;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.SerializationUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XMLRegistryIO implements RegistryIO {
   private static final String ATTR_VERSION = "version";
   private static final String ELEMENT_CAPTURE_DEVICES = "capture-devices";
   private static final String ELEMENT_CLASS = "class";
   private static final String ELEMENT_CODECS = "codecs";
   private static final String ELEMENT_CONTENT_PREFIX = "content-prefixes";
   private static final String ELEMENT_DEMUXES = "demuxes";
   private static final String ELEMENT_DEVICE = "device";
   private static final String ELEMENT_DEVICE_FORMAT = "format";
   private static final String ELEMENT_DEVICE_FORMAT_CLASS = "class";
   private static final String ELEMENT_DEVICE_FORMAT_DESCRIPTION = "description";
   private static final String ELEMENT_DEVICE_FORMAT_SERIALIZED = "serialized";
   private static final String ELEMENT_DEVICE_LOCATOR = "locator";
   private static final String ELEMENT_DEVICE_NAME = "name";
   private static final String ELEMENT_EFFECTS = "effects";
   private static final String ELEMENT_MIMETYPE = "type";
   private static final String ELEMENT_MIMETYPES = "mime-types";
   private static final String ELEMENT_MUXES = "muxes";
   private static final String ELEMENT_PLUGINS = "plugins";
   private static final String ELEMENT_PREFIX = "prefix";
   private static final String ELEMENT_PROTO_PREFIX = "protocol-prefixes";
   private static final String ELEMENT_REGISTRY = "registry";
   private static final String ELEMENT_RENDERERS = "renderers";
   private static final Logger logger;
   private static final String version = "0.1";
   private final RegistryContents registryContents;

   static {
      logger = LoggerSingleton.logger;
   }

   public XMLRegistryIO(RegistryContents var1) {
      this.registryContents = var1;
   }

   private Document buildDocument() throws IOException {
      Document var1;
      try {
         var1 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      } catch (ParserConfigurationException var3) {
         IOException var2 = new IOException();
         var2.initCause(var3);
         throw var2;
      }

      Element var4 = var1.createElement("registry");
      var4.setAttribute("version", "0.1");
      var1.appendChild(var4);
      var4.appendChild(this.getPluginsElement(var1));
      var4.appendChild(this.getContentElement(var1));
      var4.appendChild(this.getProtocolElement(var1));
      var4.appendChild(this.getMimeElement(var1));
      var4.appendChild(this.getCaptureDeviceElement(var1));
      return var1;
   }

   private Element getCaptureDeviceElement(Document var1) throws IOException {
      Element var3 = var1.createElement("capture-devices");
      Iterator var4 = this.registryContents.captureDeviceInfoList.iterator();

      while(true) {
         CaptureDeviceInfo var6;
         do {
            if (!var4.hasNext()) {
               return var3;
            }

            var6 = (CaptureDeviceInfo)var4.next();
         } while(var6.getLocator() == null);

         Element var5 = var1.createElement("device");
         Element var7 = var1.createElement("name");
         var7.setTextContent(var6.getName());
         var5.appendChild(var7);
         var7 = var1.createElement("locator");
         var7.setTextContent(var6.getLocator().toExternalForm());
         var5.appendChild(var7);
         Format[] var9 = var6.getFormats();

         for(int var2 = 0; var2 < var9.length; ++var2) {
            var7 = var1.createElement("format");
            Element var8 = var1.createElement("class");
            var8.setTextContent(var9[var2].getClass().getName());
            var7.appendChild(var8);
            var8 = var1.createElement("description");
            var8.setTextContent(var9[var2].toString());
            var7.appendChild(var8);
            var8 = var1.createElement("serialized");
            var8.setTextContent(SerializationUtils.serialize(var9[var2]));
            var7.appendChild(var8);
            var5.appendChild(var7);
         }

         var3.appendChild(var5);
      }
   }

   private Element getChild(Element var1, String var2) {
      NodeList var6 = var1.getChildNodes();
      int var4 = var6.getLength();

      for(int var3 = 0; var3 < var4; ++var3) {
         Node var5 = var6.item(var3);
         if (var5.getNodeType() == 1 && var5.getNodeName().equals(var2)) {
            return (Element)var5;
         }
      }

      return null;
   }

   private List getChildren(Element var1, String var2) {
      NodeList var7 = var1.getChildNodes();
      int var4 = var7.getLength();
      ArrayList var5 = new ArrayList(var4);

      for(int var3 = 0; var3 < var4; ++var3) {
         Node var6 = var7.item(var3);
         if (var6.getNodeType() == 1 && var6.getNodeName().equals(var2)) {
            var5.add((Element)var6);
         }
      }

      return var5;
   }

   private Element getCodecElement(Document var1) {
      return this.getPluginElement(2, "codecs", var1);
   }

   private Element getContentElement(Document var1) {
      Element var2 = var1.createElement("content-prefixes");
      Iterator var3 = this.registryContents.contentPrefixList.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Element var5 = var1.createElement("prefix");
         var5.setTextContent(var4);
         var2.appendChild(var5);
      }

      return var2;
   }

   private Element getDemuxElement(Document var1) {
      return this.getPluginElement(1, "demuxes", var1);
   }

   private Element getEffectElement(Document var1) {
      return this.getPluginElement(3, "effects", var1);
   }

   private Element getMimeElement(Document var1) {
      Element var3 = var1.createElement("mime-types");
      Iterator var4 = this.registryContents.mimeTable.getMimeTypes().iterator();

      while(var4.hasNext()) {
         String var7 = (String)var4.next();
         List var5 = this.registryContents.mimeTable.getExtensions(var7);
         Element var6 = var1.createElement("type");
         var6.setAttribute("value", var7);
         var6.setAttribute("default-ext", this.registryContents.mimeTable.getDefaultExtension(var7));
         var3.appendChild(var6);

         for(int var2 = 0; var2 < var5.size(); ++var2) {
            var7 = (String)var5.get(var2);
            Element var8 = var1.createElement("ext");
            var8.setTextContent(var7);
            var6.appendChild(var8);
         }
      }

      return var3;
   }

   private Element getMuxElement(Document var1) {
      return this.getPluginElement(5, "muxes", var1);
   }

   private Element getPluginElement(int var1, String var2, Document var3) {
      Element var7 = var3.createElement(var2);
      Vector var4 = this.registryContents.plugins[var1 - 1];
      if (var4 != null) {
         Iterator var8 = var4.iterator();

         while(var8.hasNext()) {
            String var5 = (String)var8.next();
            Element var6 = var3.createElement("class");
            var6.setTextContent(var5);
            var7.appendChild(var6);
         }
      }

      return var7;
   }

   private Element getPluginsElement(Document var1) {
      Element var2 = var1.createElement("plugins");
      var2.appendChild(this.getCodecElement(var1));
      var2.appendChild(this.getDemuxElement(var1));
      var2.appendChild(this.getEffectElement(var1));
      var2.appendChild(this.getMuxElement(var1));
      var2.appendChild(this.getRendererElement(var1));
      return var2;
   }

   private Element getProtocolElement(Document var1) {
      Element var2 = var1.createElement("protocol-prefixes");
      Iterator var3 = this.registryContents.protocolPrefixList.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Element var5 = var1.createElement("prefix");
         var5.setTextContent(var4);
         var2.appendChild(var5);
      }

      return var2;
   }

   private Element getRendererElement(Document var1) {
      return this.getPluginElement(4, "renderers", var1);
   }

   private String getTextTrim(Element var1) {
      String var2 = var1.getTextContent();
      return var2 == null ? null : var2.trim();
   }

   private void loadCaptureDevices(Element var1) throws IOException, ClassNotFoundException {
      this.registryContents.captureDeviceInfoList.clear();
      List var8 = this.getChildren(var1, "device");

      for(int var2 = 0; var2 < var8.size(); ++var2) {
         Element var6 = (Element)var8.get(var2);
         Element var4 = this.getChild(var6, "name");
         Element var5 = this.getChild(var6, "locator");
         List var10 = this.getChildren(var6, "format");
         Format[] var7 = new Format[var10.size()];

         for(int var3 = 0; var3 < var10.size(); ++var3) {
            var7[var3] = SerializationUtils.deserialize(this.getTextTrim(this.getChild((Element)var10.get(var3), "serialized")));
         }

         CaptureDeviceInfo var9 = new CaptureDeviceInfo(this.getTextTrim(var4), new MediaLocator(this.getTextTrim(var5)), var7);
         this.registryContents.captureDeviceInfoList.add(var9);
      }

   }

   private void loadContentPrefixes(Element var1) {
      this.registryContents.contentPrefixList.clear();
      List var4 = this.getChildren(var1, "prefix");

      for(int var2 = 0; var2 < var4.size(); ++var2) {
         Element var3 = (Element)var4.get(var2);
         this.registryContents.contentPrefixList.add(this.getTextTrim(var3));
      }

   }

   private void loadDocument(Document var1) throws IOException {
      Element var6 = (Element)var1.getFirstChild();
      String var2 = var6.getAttribute("version");
      Logger var3 = logger;
      StringBuilder var4 = new StringBuilder();
      var4.append("FMJ registry document version ");
      var4.append(var2);
      var3.info(var4.toString());
      this.loadPlugins(this.getChild(var6, "plugins"));
      this.loadContentPrefixes(this.getChild(var6, "content-prefixes"));
      this.loadProtocolPrefixes(this.getChild(var6, "protocol-prefixes"));
      this.loadMimeTypes(this.getChild(var6, "mime-types"));
      var6 = this.getChild(var6, "capture-devices");

      try {
         this.loadCaptureDevices(var6);
      } catch (ClassNotFoundException var5) {
         throw new IOException(var5.getMessage());
      }
   }

   private void loadMimeTypes(Element var1) {
      this.registryContents.mimeTable.clear();
      List var8 = this.getChildren(var1, "type");

      for(int var2 = 0; var2 < var8.size(); ++var2) {
         Element var6 = (Element)var8.get(var2);
         String var4 = var6.getAttribute("value");
         String var5 = var6.getAttribute("default-ext");
         List var9 = this.getChildren(var6, "ext");

         for(int var3 = 0; var3 < var9.size(); ++var3) {
            String var7 = ((Element)var9.get(var3)).getTextContent();
            this.registryContents.mimeTable.addMimeType(var7, var4);
         }

         this.registryContents.mimeTable.addMimeType(var5, var4);
      }

   }

   private void loadPlugins(Element var1) {
      this.loadPlugins(this.getChild(var1, "codecs"), 2);
      this.loadPlugins(this.getChild(var1, "effects"), 3);
      this.loadPlugins(this.getChild(var1, "renderers"), 4);
      this.loadPlugins(this.getChild(var1, "muxes"), 5);
      this.loadPlugins(this.getChild(var1, "demuxes"), 1);
   }

   private void loadPlugins(Element var1, int var2) {
      if (var1 != null) {
         Vector var3 = this.registryContents.plugins[var2 - 1];
         Iterator var4 = this.getChildren(var1, "class").iterator();

         while(var4.hasNext()) {
            var3.add(this.getTextTrim((Element)var4.next()));
         }

      }
   }

   private void loadProtocolPrefixes(Element var1) {
      this.registryContents.protocolPrefixList.clear();
      Iterator var3 = this.getChildren(var1, "prefix").iterator();

      while(var3.hasNext()) {
         Element var2 = (Element)var3.next();
         this.registryContents.protocolPrefixList.add(this.getTextTrim(var2));
      }

   }

   public void load(InputStream var1) throws IOException {
      IOException var2 = null;

      Object var5;
      label19: {
         try {
            this.loadDocument(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(var1));
         } catch (ParserConfigurationException var3) {
            var5 = var3;
            break label19;
         } catch (SAXException var4) {
            var5 = var4;
            break label19;
         }

         var5 = var2;
      }

      if (var5 != null) {
         var2 = new IOException();
         var2.initCause((Throwable)var5);
         throw var2;
      }
   }

   public void write(OutputStream var1) throws IOException {
      DOMSource var2 = new DOMSource(this.buildDocument());
      TransformerFactory var3 = TransformerFactory.newInstance();

      try {
         var3.setAttribute("indent-number", 4);
      } catch (Exception var8) {
      }

      IOException var9;
      Transformer var10;
      try {
         var10 = var3.newTransformer();
      } catch (TransformerConfigurationException var7) {
         var9 = new IOException();
         var9.initCause(var7);
         throw var9;
      }

      try {
         var10.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
      } catch (Exception var6) {
      }

      var10.setOutputProperty("encoding", "UTF-8");
      var10.setOutputProperty("indent", "yes");

      try {
         var10.transform(var2, new StreamResult(new OutputStreamWriter(var1, "UTF-8")));
      } catch (TransformerException var5) {
         var9 = new IOException();
         var9.initCause(var5);
         throw var9;
      }
   }
}
