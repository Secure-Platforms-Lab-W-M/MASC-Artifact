package net.sf.fmj.media.parser;

import com.lti.utils.synchronization.CloseableThread;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

class XmlMovieSAXParserThread extends CloseableThread {
   private final XmlMovieSAXHandler handler;
   // $FF: renamed from: is java.io.InputStream
   private final InputStream field_19;

   public XmlMovieSAXParserThread(XmlMovieSAXHandler var1, InputStream var2) {
      this.handler = var1;
      this.field_19 = var2;
   }

   public void run() {
      try {
         try {
            XMLReader var1 = XMLReaderFactory.createXMLReader();
            var1.setContentHandler(this.handler);
            var1.parse(new InputSource(this.field_19));
         } catch (SAXException var6) {
            this.handler.postError(var6);
         } catch (IOException var7) {
            this.handler.postError(var7);
         }
      } catch (InterruptedException var8) {
      } finally {
         this.setClosed();
      }

   }
}
