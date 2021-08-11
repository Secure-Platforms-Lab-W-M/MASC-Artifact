package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {
   public static Serializable clone(Serializable var0) {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayInputStream var1 = new ByteArrayInputStream(serialize(var0));

         ClassNotFoundException var38;
         label159: {
            IOException var10000;
            label165: {
               SerializationUtils.ClassLoaderAwareObjectInputStream var32;
               boolean var10001;
               try {
                  var32 = new SerializationUtils.ClassLoaderAwareObjectInputStream(var1, var0.getClass().getClassLoader());
               } catch (ClassNotFoundException var30) {
                  var38 = var30;
                  var10001 = false;
                  break label159;
               } catch (IOException var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label165;
               }

               Serializable var37;
               try {
                  var37 = (Serializable)var32.readObject();
               } catch (Throwable var29) {
                  Throwable var36 = var29;

                  try {
                     var32.close();
                  } catch (Throwable var26) {
                     Throwable var33 = var26;

                     label145:
                     try {
                        var36.addSuppressed(var33);
                        break label145;
                     } catch (ClassNotFoundException var24) {
                        var38 = var24;
                        var10001 = false;
                        break label159;
                     } catch (IOException var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label165;
                     }
                  }

                  try {
                     throw var36;
                  } catch (ClassNotFoundException var22) {
                     var38 = var22;
                     var10001 = false;
                     break label159;
                  } catch (IOException var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label165;
                  }
               }

               try {
                  var32.close();
                  return var37;
               } catch (ClassNotFoundException var27) {
                  var38 = var27;
                  var10001 = false;
                  break label159;
               } catch (IOException var28) {
                  var10000 = var28;
                  var10001 = false;
               }
            }

            IOException var34 = var10000;
            throw new SerializationException("IOException while reading or closing cloned object data", var34);
         }

         ClassNotFoundException var35 = var38;
         throw new SerializationException("ClassNotFoundException while reading cloned object data", var35);
      }
   }

   public static Object deserialize(InputStream var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The InputStream must not be null");

      Object var35;
      ClassNotFoundException var37;
      label183: {
         IOException var10000;
         label177: {
            ObjectInputStream var33;
            boolean var10001;
            try {
               var33 = new ObjectInputStream(var0);
            } catch (ClassNotFoundException var31) {
               var37 = var31;
               var10001 = false;
               break label183;
            } catch (IOException var32) {
               var10000 = var32;
               var10001 = false;
               break label177;
            }

            Object var36;
            try {
               var36 = var33.readObject();
            } catch (Throwable var30) {
               Throwable var2 = var30;

               try {
                  var33.close();
               } catch (Throwable var27) {
                  Throwable var34 = var27;

                  label158:
                  try {
                     var2.addSuppressed(var34);
                     break label158;
                  } catch (ClassNotFoundException var25) {
                     var37 = var25;
                     var10001 = false;
                     break label183;
                  } catch (IOException var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label177;
                  }
               }

               try {
                  throw var2;
               } catch (ClassNotFoundException var23) {
                  var37 = var23;
                  var10001 = false;
                  break label183;
               } catch (IOException var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label177;
               }
            }

            try {
               var33.close();
               return var36;
            } catch (ClassNotFoundException var28) {
               var37 = var28;
               var10001 = false;
               break label183;
            } catch (IOException var29) {
               var10000 = var29;
               var10001 = false;
            }
         }

         var35 = var10000;
         throw new SerializationException((Throwable)var35);
      }

      var35 = var37;
      throw new SerializationException((Throwable)var35);
   }

   public static Object deserialize(byte[] var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The byte[] must not be null");
      return deserialize((InputStream)(new ByteArrayInputStream(var0)));
   }

   public static Serializable roundtrip(Serializable var0) {
      return (Serializable)deserialize(serialize(var0));
   }

   public static void serialize(Serializable var0, OutputStream var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The OutputStream must not be null");

      IOException var10000;
      label143: {
         boolean var10001;
         ObjectOutputStream var23;
         try {
            var23 = new ObjectOutputStream(var1);
         } catch (IOException var20) {
            var10000 = var20;
            var10001 = false;
            break label143;
         }

         try {
            var23.writeObject(var0);
         } catch (Throwable var19) {
            Throwable var21 = var19;

            try {
               var23.close();
            } catch (Throwable var17) {
               Throwable var24 = var17;

               label126:
               try {
                  var21.addSuppressed(var24);
                  break label126;
               } catch (IOException var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label143;
               }
            }

            try {
               throw var21;
            } catch (IOException var15) {
               var10000 = var15;
               var10001 = false;
               break label143;
            }
         }

         try {
            var23.close();
            return;
         } catch (IOException var18) {
            var10000 = var18;
            var10001 = false;
         }
      }

      IOException var22 = var10000;
      throw new SerializationException(var22);
   }

   public static byte[] serialize(Serializable var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(512);
      serialize(var0, var1);
      return var1.toByteArray();
   }

   static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
      private static final Map primitiveTypes;
      private final ClassLoader classLoader;

      static {
         HashMap var0 = new HashMap();
         primitiveTypes = var0;
         var0.put("byte", Byte.TYPE);
         primitiveTypes.put("short", Short.TYPE);
         primitiveTypes.put("int", Integer.TYPE);
         primitiveTypes.put("long", Long.TYPE);
         primitiveTypes.put("float", Float.TYPE);
         primitiveTypes.put("double", Double.TYPE);
         primitiveTypes.put("boolean", Boolean.TYPE);
         primitiveTypes.put("char", Character.TYPE);
         primitiveTypes.put("void", Void.TYPE);
      }

      ClassLoaderAwareObjectInputStream(InputStream var1, ClassLoader var2) throws IOException {
         super(var1);
         this.classLoader = var2;
      }

      protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
         String var5 = var1.getName();

         Class var2;
         try {
            var2 = Class.forName(var5, false, this.classLoader);
            return var2;
         } catch (ClassNotFoundException var4) {
            try {
               var2 = Class.forName(var5, false, Thread.currentThread().getContextClassLoader());
               return var2;
            } catch (ClassNotFoundException var3) {
               Class var6 = (Class)primitiveTypes.get(var5);
               if (var6 != null) {
                  return var6;
               } else {
                  throw var3;
               }
            }
         }
      }
   }
}
