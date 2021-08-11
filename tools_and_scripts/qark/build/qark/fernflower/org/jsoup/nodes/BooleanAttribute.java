package org.jsoup.nodes;

public class BooleanAttribute extends Attribute {
   public BooleanAttribute(String var1) {
      super(var1, "");
   }

   protected boolean isBooleanAttribute() {
      return true;
   }
}
