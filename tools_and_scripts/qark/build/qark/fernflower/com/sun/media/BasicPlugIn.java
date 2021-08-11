package com.sun.media;

import java.util.Vector;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.PlugIn;
import javax.media.PlugInManager;
import javax.media.ResourceUnavailableException;

@Deprecated
public abstract class BasicPlugIn implements PlugIn {
   protected Object[] controls = new Object[0];

   public static Class getClassForName(String var0) throws ClassNotFoundException {
      return Class.forName(var0);
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
      if (var0 != null) {
         Vector var2 = PlugInManager.getPlugInList((Format)null, (Format)null, var1);

         for(var1 = 0; var1 < var2.size(); ++var1) {
            if (var0.equals((String)var2.get(var1))) {
               return true;
            }
         }

         return false;
      } else {
         throw null;
      }
   }

   public abstract void close();

   protected void error() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" PlugIn error");
      throw new RuntimeException(var1.toString());
   }

   public Object getControl(String var1) {
      Class var6;
      try {
         var6 = Class.forName(var1);
      } catch (Exception var5) {
         return null;
      }

      Object[] var3 = this.getControls();

      for(int var2 = 0; var2 < var3.length; ++var2) {
         Object var4 = var3[var2];
         if (var6.isInstance(var4)) {
            return var4;
         }
      }

      return null;
   }

   public Object[] getControls() {
      return this.controls;
   }

   protected Object getInputData(Buffer var1) {
      return var1.getData();
   }

   public abstract String getName();

   protected final long getNativeData(Object var1) {
      return 0L;
   }

   protected Object getOutputData(Buffer var1) {
      return var1.getData();
   }

   public abstract void open() throws ResourceUnavailableException;

   public abstract void reset();

   protected byte[] validateByteArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      byte[] var5;
      if (var3 != null && var3.getClass() == byte[].class) {
         var5 = (byte[])((byte[])var3);
      } else {
         var5 = null;
      }

      if (var5 != null && var5.length >= var2) {
         return var5;
      } else {
         byte[] var4 = new byte[var2];
         if (var5 != null) {
            System.arraycopy(var5, 0, var4, 0, var5.length);
         }

         var1.setData(var4);
         return var4;
      }
   }

   protected Object validateData(Buffer var1, int var2, boolean var3) {
      Class var4 = var1.getFormat().getDataType();
      if (var4 == Format.byteArray) {
         return this.validateByteArraySize(var1, var2);
      } else if (var4 == Format.shortArray) {
         return this.validateShortArraySize(var1, var2);
      } else {
         return var4 == Format.intArray ? this.validateIntArraySize(var1, var2) : null;
      }
   }

   protected int[] validateIntArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      int[] var5;
      if (var3 != null && var3.getClass() == int[].class) {
         var5 = (int[])((int[])var3);
      } else {
         var5 = null;
      }

      if (var5 != null && var5.length >= var2) {
         return var5;
      } else {
         int[] var4 = new int[var2];
         if (var5 != null) {
            System.arraycopy(var5, 0, var4, 0, var5.length);
         }

         var1.setData(var4);
         return var4;
      }
   }

   protected short[] validateShortArraySize(Buffer var1, int var2) {
      Object var3 = var1.getData();
      short[] var5;
      if (var3 != null && var3.getClass() == short[].class) {
         var5 = (short[])((short[])var3);
      } else {
         var5 = null;
      }

      if (var5 != null && var5.length >= var2) {
         return var5;
      } else {
         short[] var4 = new short[var2];
         if (var5 != null) {
            System.arraycopy(var5, 0, var4, 0, var5.length);
         }

         var1.setData(var4);
         return var4;
      }
   }
}
