package com.jcraft.jzlib;

import java.io.UnsupportedEncodingException;

public class GZIPHeader implements Cloneable {
   public static final byte OS_AMIGA = 1;
   public static final byte OS_ATARI = 5;
   public static final byte OS_CPM = 9;
   public static final byte OS_MACOS = 7;
   public static final byte OS_MSDOS = 0;
   public static final byte OS_OS2 = 6;
   public static final byte OS_QDOS = 12;
   public static final byte OS_RISCOS = 13;
   public static final byte OS_TOPS20 = 10;
   public static final byte OS_UNIX = 3;
   public static final byte OS_UNKNOWN = -1;
   public static final byte OS_VMCMS = 4;
   public static final byte OS_VMS = 2;
   public static final byte OS_WIN32 = 11;
   public static final byte OS_ZSYSTEM = 8;
   byte[] comment;
   long crc;
   boolean done = false;
   byte[] extra;
   private boolean fhcrc = false;
   int hcrc;
   long mtime = 0L;
   byte[] name;
   // $FF: renamed from: os int
   int field_148 = 255;
   boolean text = false;
   long time;
   int xflags;

   public Object clone() throws CloneNotSupportedException {
      GZIPHeader var1 = (GZIPHeader)super.clone();
      byte[] var2 = var1.extra;
      byte[] var3;
      if (var2 != null) {
         var3 = new byte[var2.length];
         System.arraycopy(var2, 0, var3, 0, var3.length);
         var1.extra = var3;
      }

      var2 = var1.name;
      if (var2 != null) {
         var3 = new byte[var2.length];
         System.arraycopy(var2, 0, var3, 0, var3.length);
         var1.name = var3;
      }

      var2 = var1.comment;
      if (var2 != null) {
         var3 = new byte[var2.length];
         System.arraycopy(var2, 0, var3, 0, var3.length);
         var1.comment = var3;
      }

      return var1;
   }

   public long getCRC() {
      return this.crc;
   }

   public String getComment() {
      if (this.comment == null) {
         return "";
      } else {
         try {
            String var1 = new String(this.comment, "ISO-8859-1");
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new InternalError(var2.toString());
         }
      }
   }

   public long getModifiedTime() {
      return this.mtime;
   }

   public String getName() {
      if (this.name == null) {
         return "";
      } else {
         try {
            String var1 = new String(this.name, "ISO-8859-1");
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new InternalError(var2.toString());
         }
      }
   }

   public int getOS() {
      return this.field_148;
   }

   void put(Deflate var1) {
      int var3 = 0;
      if (this.text) {
         var3 = 0 | 1;
      }

      int var2 = var3;
      if (this.fhcrc) {
         var2 = var3 | 2;
      }

      var3 = var2;
      if (this.extra != null) {
         var3 = var2 | 4;
      }

      var2 = var3;
      if (this.name != null) {
         var2 = var3 | 8;
      }

      var3 = var2;
      if (this.comment != null) {
         var3 = var2 | 16;
      }

      var2 = 0;
      if (var1.level == 1) {
         var2 = 0 | 4;
      } else if (var1.level == 9) {
         var2 = 0 | 2;
      }

      var1.put_short(-29921);
      var1.put_byte((byte)8);
      var1.put_byte((byte)var3);
      var1.put_byte((byte)((int)this.mtime));
      var1.put_byte((byte)((int)(this.mtime >> 8)));
      var1.put_byte((byte)((int)(this.mtime >> 16)));
      var1.put_byte((byte)((int)(this.mtime >> 24)));
      var1.put_byte((byte)var2);
      var1.put_byte((byte)this.field_148);
      byte[] var4 = this.extra;
      if (var4 != null) {
         var1.put_byte((byte)var4.length);
         var1.put_byte((byte)(this.extra.length >> 8));
         var4 = this.extra;
         var1.put_byte(var4, 0, var4.length);
      }

      var4 = this.name;
      if (var4 != null) {
         var1.put_byte(var4, 0, var4.length);
         var1.put_byte((byte)0);
      }

      var4 = this.comment;
      if (var4 != null) {
         var1.put_byte(var4, 0, var4.length);
         var1.put_byte((byte)0);
      }

   }

   public void setCRC(long var1) {
      this.crc = var1;
   }

   public void setComment(String var1) {
      try {
         this.comment = var1.getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("comment must be in ISO-8859-1 ");
         var3.append(this.name);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void setModifiedTime(long var1) {
      this.mtime = var1;
   }

   public void setName(String var1) {
      try {
         this.name = var1.getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("name must be in ISO-8859-1 ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void setOS(int var1) {
      if ((var1 < 0 || var1 > 13) && var1 != 255) {
         StringBuilder var2 = new StringBuilder();
         var2.append("os: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      } else {
         this.field_148 = var1;
      }
   }
}
