package org.jsoup.parser;

import java.util.ArrayList;

class ParseErrorList extends ArrayList {
   private static final int INITIAL_CAPACITY = 16;
   private final int maxSize;

   ParseErrorList(int var1, int var2) {
      super(var1);
      this.maxSize = var2;
   }

   static ParseErrorList noTracking() {
      return new ParseErrorList(0, 0);
   }

   static ParseErrorList tracking(int var0) {
      return new ParseErrorList(16, var0);
   }

   boolean canAddError() {
      return this.size() < this.maxSize;
   }

   int getMaxSize() {
      return this.maxSize;
   }
}
