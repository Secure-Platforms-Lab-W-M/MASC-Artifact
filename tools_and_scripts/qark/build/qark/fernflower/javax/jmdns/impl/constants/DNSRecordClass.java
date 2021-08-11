package javax.jmdns.impl.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DNSRecordClass {
   CLASS_ANY,
   CLASS_CH("ch", 3),
   CLASS_CS("cs", 2),
   CLASS_HS("hs", 4),
   CLASS_IN("in", 1);

   public static final int CLASS_MASK = 32767;
   CLASS_NONE("none", 254);

   public static final int CLASS_UNIQUE = 32768;
   CLASS_UNKNOWN("?", 0);

   public static final boolean NOT_UNIQUE = false;
   public static final boolean UNIQUE = true;
   private static Logger logger;
   private final String _externalName;
   private final int _index;

   static {
      DNSRecordClass var0 = new DNSRecordClass("CLASS_ANY", 6, "any", 255);
      CLASS_ANY = var0;
      logger = LoggerFactory.getLogger(DNSRecordClass.class.getName());
   }

   private DNSRecordClass(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSRecordClass classForIndex(int var0) {
      DNSRecordClass[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSRecordClass var4 = var3[var1];
         if (var4._index == (var0 & 32767)) {
            return var4;
         }
      }

      logger.warn("Could not find record class for index: {}", var0);
      return CLASS_UNKNOWN;
   }

   public static DNSRecordClass classForName(String var0) {
      if (var0 != null) {
         String var3 = var0.toLowerCase();
         DNSRecordClass[] var4 = values();
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            DNSRecordClass var5 = var4[var1];
            if (var5._externalName.equals(var3)) {
               return var5;
            }
         }
      }

      logger.warn("Could not find record class for name: {}", var0);
      return CLASS_UNKNOWN;
   }

   public String externalName() {
      return this._externalName;
   }

   public int indexValue() {
      return this._index;
   }

   public boolean isUnique(int var1) {
      return this != CLASS_UNKNOWN && ('耀' & var1) != 0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.name());
      var1.append(" index ");
      var1.append(this.indexValue());
      return var1.toString();
   }
}
