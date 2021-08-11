package org.jsoup.nodes;

import java.io.IOException;

public class DataNode extends Node {
   private static final String DATA_KEY = "data";

   public DataNode(String var1, String var2) {
      super(var2);
      this.attributes.put("data", var1);
   }

   public static DataNode createFromEncoded(String var0, String var1) {
      return new DataNode(Entities.unescape(var0), var1);
   }

   public String getWholeData() {
      return this.attributes.get("data");
   }

   public String nodeName() {
      return "#data";
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      var1.append(this.getWholeData());
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) {
   }

   public DataNode setWholeData(String var1) {
      this.attributes.put("data", var1);
      return this;
   }

   public String toString() {
      return this.outerHtml();
   }
}
