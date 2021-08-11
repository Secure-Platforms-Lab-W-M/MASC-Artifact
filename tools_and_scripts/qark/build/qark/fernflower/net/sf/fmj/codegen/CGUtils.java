package net.sf.fmj.codegen;

public class CGUtils {
   private static final int MAX_BYTE_PLUS1 = 256;
   private static final int RADIX_16 = 16;

   public static String byteArrayToHexString(byte[] var0) {
      return byteArrayToHexString(var0, var0.length);
   }

   public static String byteArrayToHexString(byte[] var0, int var1) {
      return byteArrayToHexString(var0, var1, 0);
   }

   public static String byteArrayToHexString(byte[] var0, int var1, int var2) {
      StringBuffer var6 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         String var5 = Integer.toHexString(uByteToInt(var0[var2 + var3]));
         String var4 = var5;
         if (var5.length() == 1) {
            StringBuilder var7 = new StringBuilder();
            var7.append("0");
            var7.append(var5);
            var4 = var7.toString();
         }

         var6.append(var4);
      }

      return var6.toString();
   }

   public static String dump(byte[] var0, int var1, int var2) {
      StringBuffer var6 = new StringBuffer();

      int var3;
      for(int var5 = 32; var1 < var2; var5 = var3) {
         int var4 = 0;
         var3 = var5;
         if (var1 + var5 > var2) {
            var3 = var2 - var1;
            var4 = 32 - var3;
         }

         var6.append(byteArrayToHexString(var0, var3, var1));

         for(var5 = 0; var5 < var4; ++var5) {
            var6.append("  ");
         }

         var6.append(" | ");

         for(var4 = 0; var4 < var3; ++var4) {
            byte var7 = var0[var1 + var4];
            if (var7 >= 32 && var7 <= 126) {
               var6.append((char)var7);
            } else {
               var6.append('.');
            }
         }

         var6.append('\n');
         var1 += var3;
      }

      return var6.toString();
   }

   public static byte hexStringToByte(String var0) {
      return (byte)Integer.parseInt(var0, 16);
   }

   public static byte[] hexStringToByteArray(String var0) {
      byte[] var2 = new byte[var0.length() / 2];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = hexStringToByte(var0.substring(var1 * 2, var1 * 2 + 2));
      }

      return var2;
   }

   public static String replaceSpecialJavaStringChars(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var3 = new StringBuffer();

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var1 = var0.charAt(var2);
            if (var1 == '"') {
               var3.append("\\\"");
            } else if (var1 == '\'') {
               var3.append("\\'");
            } else if (var1 == '\\') {
               var3.append("\\\\");
            } else if (var1 == '\r') {
               var3.append("\\r");
            } else if (var1 == '\n') {
               var3.append("\\n");
            } else if (var1 == '\t') {
               var3.append("\\t");
            } else if (var1 == '\f') {
               var3.append("\\f");
            } else if (var1 == '\b') {
               var3.append("\\b");
            } else if (var1 == 0) {
               var3.append("\\000");
            } else {
               var3.append(var1);
            }
         }

         return var3.toString();
      }
   }

   public static String toHexLiteral(int var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("0x");
      var1.append(Integer.toHexString(var0));
      return var1.toString();
   }

   public static String toLiteral(double var0) {
      StringBuilder var2 = new StringBuilder();
      var2.append("");
      var2.append(var0);
      return var2.toString();
   }

   public static String toLiteral(float var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(var0);
      var1.append("f");
      return var1.toString();
   }

   public static String toLiteral(int var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(var0);
      return var1.toString();
   }

   public static String toLiteral(long var0) {
      StringBuilder var2 = new StringBuilder();
      var2.append("");
      var2.append(var0);
      var2.append("L");
      return var2.toString();
   }

   public static String toLiteral(String var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("\"");
         var1.append(replaceSpecialJavaStringChars(var0));
         var1.append("\"");
         return var1.toString();
      }
   }

   public static String toLiteral(byte[] var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append("new byte[] {");

         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            StringBuilder var3 = new StringBuilder();
            var3.append("(byte)");
            var3.append(var0[var1]);
            var2.append(var3.toString());
         }

         var2.append("}");
         return var2.toString();
      }
   }

   public static String toLiteral(int[] var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuffer var4 = new StringBuffer();
         var4.append("new int[] {");
         boolean var2 = true;

         for(int var1 = 0; var1 < var0.length; ++var1) {
            int var3 = var0[var1];
            if (var2) {
               var2 = false;
            } else {
               var4.append(",");
            }

            StringBuilder var5 = new StringBuilder();
            var5.append("");
            var5.append(var3);
            var4.append(var5.toString());
         }

         var4.append("}");
         return var4.toString();
      }
   }

   static String toName(Class var0) {
      if (var0 == Integer.TYPE) {
         return "int";
      } else if (var0 == Boolean.TYPE) {
         return "boolean";
      } else if (var0 == Short.TYPE) {
         return "short";
      } else if (var0 == Byte.TYPE) {
         return "byte";
      } else if (var0 == Character.TYPE) {
         return "char";
      } else if (var0 == Float.TYPE) {
         return "float";
      } else if (var0 == Double.TYPE) {
         return "double";
      } else if (var0 == Long.TYPE) {
         return "long";
      } else if (var0 == byte[].class) {
         return "byte[]";
      } else if (var0 == int[].class) {
         return "int[]";
      } else if (var0 == short[].class) {
         return "short[]";
      } else if (var0 == double[].class) {
         return "double[]";
      } else if (var0 == float[].class) {
         return "float[]";
      } else if (var0 == long[].class) {
         return "long[]";
      } else if (var0 == boolean[].class) {
         return "boolean[]";
      } else if (var0 == char[].class) {
         return "char[]";
      } else {
         StringBuilder var1;
         if (var0.isArray()) {
            var1 = new StringBuilder();
            var1.append("");
            var1.append(toName(var0.getComponentType()));
            var1.append("[]");
            return var1.toString();
         } else {
            var1 = new StringBuilder();
            var1.append("");
            var1.append(var0.getName());
            var1.append("");
            return var1.toString();
         }
      }
   }

   public static String toNameDotClass(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(toName(var0));
         var1.append(".class");
         return var1.toString();
      }
   }

   public static int uByteToInt(byte var0) {
      return var0 >= 0 ? var0 : var0 + 256;
   }
}
