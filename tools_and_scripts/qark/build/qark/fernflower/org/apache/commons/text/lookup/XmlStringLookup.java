package org.apache.commons.text.lookup;

import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

final class XmlStringLookup extends AbstractStringLookup {
   static final XmlStringLookup INSTANCE = new XmlStringLookup();

   private XmlStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = var1.split(SPLIT_STR);
         if (var2.length == 2) {
            String var5 = var2[0];
            var1 = this.substringAfter(var1, ':');

            try {
               String var3 = XPathFactory.newInstance().newXPath().evaluate(var1, new InputSource(Files.newInputStream(Paths.get(var5))));
               return var3;
            } catch (Exception var4) {
               throw IllegalArgumentExceptions.format(var4, "Error looking up XML document [%s] and XPath [%s].", var5, var1);
            }
         } else {
            throw IllegalArgumentExceptions.format("Bad XML key format [%s]; expected format is DocumentPath:XPath.", var1);
         }
      }
   }
}
