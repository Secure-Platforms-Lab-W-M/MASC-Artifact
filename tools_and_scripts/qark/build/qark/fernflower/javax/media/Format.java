package javax.media;

import java.io.Serializable;

public class Format implements Cloneable, Serializable {
   public static final int FALSE = 0;
   public static final int NOT_SPECIFIED = -1;
   public static final int TRUE = 1;
   public static final Class byteArray = (new byte[0]).getClass();
   public static final Class formatArray = (new Format[0]).getClass();
   public static final Class intArray = (new int[0]).getClass();
   public static final Class shortArray = (new short[0]).getClass();
   protected Class clz;
   protected Class dataType;
   protected String encoding;
   private long encodingCode;

   public Format(String var1) {
      this.dataType = byteArray;
      this.clz = this.getClass();
      this.encodingCode = 0L;
      this.encoding = var1;
   }

   public Format(String var1, Class var2) {
      this(var1);
      this.dataType = var2;
   }

   private long getEncodingCode(String var1) {
      byte[] var7 = var1.getBytes();
      long var5 = 0L;

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         byte var4 = var7[var2];
         byte var3 = var4;
         if (var4 > 96) {
            var3 = var4;
            if (var4 < 123) {
               var3 = (byte)(var4 - 32);
            }
         }

         var3 = (byte)(var3 - 32);
         if (var3 > 63) {
            return -1L;
         }

         var5 = var5 << 6 | (long)var3;
      }

      return var5;
   }

   public Object clone() {
      Format var1 = new Format(this.encoding);
      var1.copy(this);
      return var1;
   }

   protected void copy(Format var1) {
      this.dataType = var1.dataType;
   }

   public boolean equals(Object var1) {
      if (var1 != null) {
         if (this.clz != ((Format)var1).clz) {
            return false;
         } else {
            String var2 = ((Format)var1).encoding;
            Class var3 = ((Format)var1).dataType;
            if (this.dataType == var3) {
               String var4 = this.encoding;
               if (var4 == var2 || var4 != null && var2 != null && this.isSameEncoding((Format)var1)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public Class getDataType() {
      return this.dataType;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public Format intersects(Format var1) {
      Format var2;
      if (this.clz.isAssignableFrom(var1.clz)) {
         var2 = (Format)var1.clone();
      } else {
         if (!var1.clz.isAssignableFrom(this.clz)) {
            return null;
         }

         var2 = (Format)this.clone();
      }

      if (var2.encoding == null) {
         String var3 = this.encoding;
         if (var3 == null) {
            var3 = var1.encoding;
         }

         var2.encoding = var3;
      }

      if (var2.dataType == null) {
         Class var5 = this.dataType;
         Class var4;
         if (var5 != null) {
            var4 = var5;
         } else {
            var4 = var1.dataType;
         }

         var2.dataType = var4;
      }

      return var2;
   }

   public boolean isSameEncoding(String var1) {
      String var5 = this.encoding;
      boolean var2 = false;
      if (var5 != null) {
         if (var1 == null) {
            return false;
         } else if (var5 == var1) {
            return true;
         } else if (var5.length() > 10) {
            return this.encoding.equalsIgnoreCase(var1);
         } else {
            if (this.encodingCode == 0L) {
               this.encodingCode = this.getEncodingCode(this.encoding);
            }

            if (this.encodingCode < 0L) {
               return this.encoding.equalsIgnoreCase(var1);
            } else {
               long var3 = this.getEncodingCode(var1);
               if (this.encodingCode == var3) {
                  var2 = true;
               }

               return var2;
            }
         }
      } else {
         return false;
      }
   }

   public boolean isSameEncoding(Format var1) {
      String var8 = this.encoding;
      boolean var3 = false;
      boolean var2 = false;
      if (var8 != null && var1 != null) {
         String var9 = var1.encoding;
         if (var9 == null) {
            return false;
         } else if (var8 == var9) {
            return true;
         } else {
            long var4 = this.encodingCode;
            long var6;
            if (var4 > 0L) {
               var6 = var1.encodingCode;
               if (var6 > 0L) {
                  if (var4 == var6) {
                     var2 = true;
                  }

                  return var2;
               }
            }

            if (this.encoding.length() > 10) {
               return this.encoding.equalsIgnoreCase(var1.encoding);
            } else {
               if (this.encodingCode == 0L) {
                  this.encodingCode = this.getEncodingCode(this.encoding);
               }

               var4 = this.encodingCode;
               if (var4 <= 0L) {
                  return this.encoding.equalsIgnoreCase(var1.encoding);
               } else {
                  var6 = var1.encodingCode;
                  if (var6 == 0L) {
                     return var1.isSameEncoding(this);
                  } else {
                     var2 = var3;
                     if (var4 == var6) {
                        var2 = true;
                     }

                     return var2;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean matches(Format var1) {
      if (var1 == null) {
         return false;
      } else {
         if (var1.encoding == null || this.encoding == null || this.isSameEncoding(var1)) {
            Class var2 = var1.dataType;
            if (var2 != null) {
               Class var3 = this.dataType;
               if (var3 != null && var2 != var3) {
                  return false;
               }
            }

            if (this.clz.isAssignableFrom(var1.clz) || var1.clz.isAssignableFrom(this.clz)) {
               return true;
            }
         }

         return false;
      }
   }

   public Format relax() {
      return (Format)this.clone();
   }

   public String toString() {
      return this.getEncoding();
   }
}
