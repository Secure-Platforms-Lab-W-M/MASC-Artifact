package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator implements Comparator {
   private final StringEncoder stringEncoder;

   @Deprecated
   public StringEncoderComparator() {
      this.stringEncoder = null;
   }

   public StringEncoderComparator(StringEncoder var1) {
      this.stringEncoder = var1;
   }

   public int compare(Object var1, Object var2) {
      try {
         int var3 = ((Comparable)this.stringEncoder.encode(var1)).compareTo((Comparable)this.stringEncoder.encode(var2));
         return var3;
      } catch (EncoderException var4) {
         return 0;
      }
   }
}
