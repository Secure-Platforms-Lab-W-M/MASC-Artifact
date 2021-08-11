package net.sf.fmj.media;

import java.lang.reflect.Method;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.Control;
import javax.media.Format;
import javax.media.PlugIn;
import javax.media.PlugInManager;
import javax.media.format.VideoFormat;

public abstract class BasicPlugIn implements PlugIn {
   private static final boolean DEBUG = false;
   private static Method forName3ArgsM;
   private static Method getContextClassLoaderM;
   private static boolean jdkInit = false;
   private static ClassLoader systemClassLoader;
   protected Object[] controls = new Control[0];

   private static boolean checkIfJDK12() {
      if (jdkInit) {
         return forName3ArgsM != null;
      } else {
         jdkInit = true;

         try {
            forName3ArgsM = Class.class.getMethod("forName", String.class, Boolean.TYPE, ClassLoader.class);
            systemClassLoader = (ClassLoader)ClassLoader.class.getMethod("getSystemClassLoader").invoke(ClassLoader.class);
            getContextClassLoaderM = Thread.class.getMethod("getContextClassLoader");
            return true;
         } finally {
            forName3ArgsM = null;
            return false;
         }
      }
   }

   public static Class getClassForName(String var0) throws ClassNotFoundException {
      Class var1;
      try {
         var1 = Class.forName(var0);
         return var1;
      } catch (Exception var10) {
         if (!checkIfJDK12()) {
            throw new ClassNotFoundException(var10.getMessage());
         }
      } catch (Error var11) {
         if (!checkIfJDK12()) {
            throw var11;
         }
      }

      try {
         var1 = (Class)forName3ArgsM.invoke(Class.class, var0, new Boolean(true), systemClassLoader);
         return var1;
      } finally {
         try {
            ClassLoader var13 = (ClassLoader)getContextClassLoaderM.invoke(Thread.currentThread());
            Class var12 = (Class)forName3ArgsM.invoke(Class.class, var0, new Boolean(true), var13);
            return var12;
         } catch (Exception var7) {
            throw new ClassNotFoundException(var7.getMessage());
         } catch (Error var8) {
            throw var8;
         }
      }
   }

   public static Format matches(Format var0, Format[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var0.matches(var1[var2])) {
            return var1[var2];
         }
      }

      return null;
   }

   public static boolean plugInExists(String var0, int var1) {
      Vector var2 = PlugInManager.getPlugInList((Format)null, (Format)null, var1);

      for(var1 = 0; var1 < var2.size(); ++var1) {
         if (var0.equals(var2.elementAt(var1))) {
            return true;
         }
      }

      return false;
   }

   protected void error() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" PlugIn error");
      throw new RuntimeException(var1.toString());
   }

   public Object getControl(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Object[] getControls() {
      return this.controls;
   }

   protected Object getInputData(Buffer var1) {
      return var1.getData();
   }

   protected final long getNativeData(Object var1) {
      return 0L;
   }

   protected Object getOutputData(Buffer var1) {
      return var1.getData();
   }

   protected byte[] validateByteArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      byte[] var5;
      if (var3 instanceof byte[]) {
         byte[] var4 = (byte[])((byte[])var3);
         if (var4.length >= var2) {
            return var4;
         }

         var5 = new byte[var2];
         System.arraycopy(var4, 0, var5, 0, var4.length);
      } else {
         var5 = new byte[var2];
      }

      var1.setData(var5);
      return var5;
   }

   protected Object validateData(Buffer var1, int var2, boolean var3) {
      Format var5 = var1.getFormat();
      Class var6 = var5.getDataType();
      int var4 = var2;
      if (var2 < 1) {
         var4 = var2;
         if (var5 != null) {
            var4 = var2;
            if (var5 instanceof VideoFormat) {
               var4 = ((VideoFormat)var5).getMaxDataLength();
            }
         }
      }

      if (var6 == Format.byteArray) {
         return this.validateByteArraySize(var1, var4);
      } else if (var6 == Format.shortArray) {
         return this.validateShortArraySize(var1, var4);
      } else if (var6 == Format.intArray) {
         return this.validateIntArraySize(var1, var4);
      } else {
         System.err.println("Error in validateData");
         return null;
      }
   }

   protected int[] validateIntArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      int[] var5;
      if (var3 instanceof int[]) {
         int[] var4 = (int[])((int[])var3);
         if (var4.length >= var2) {
            return var4;
         }

         var5 = new int[var2];
         System.arraycopy(var4, 0, var5, 0, var4.length);
      } else {
         var5 = new int[var2];
      }

      var1.setData(var5);
      return var5;
   }

   protected short[] validateShortArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      short[] var5;
      if (var3 instanceof short[]) {
         short[] var4 = (short[])((short[])var3);
         if (var4.length >= var2) {
            return var4;
         }

         var5 = new short[var2];
         System.arraycopy(var4, 0, var5, 0, var4.length);
      } else {
         var5 = new short[var2];
      }

      var1.setData(var5);
      return var5;
   }
}
