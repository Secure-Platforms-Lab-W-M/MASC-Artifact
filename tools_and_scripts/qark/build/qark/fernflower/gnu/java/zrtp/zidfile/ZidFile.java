package gnu.java.zrtp.zidfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class ZidFile {
   private static final int IDENTIFIER_LENGTH = 12;
   private static final int ZID_RECORD_LENGTH = 128;
   private static ZidFile instance = null;
   private byte[] associatedZid = null;
   private RandomAccessFile zidFile;

   private ZidFile() {
      this.associatedZid = new byte[12];
   }

   private void createZIDFile(String var1) {
      RandomAccessFile var5;
      try {
         var5 = new RandomAccessFile(var1, "rw");
         this.zidFile = var5;
      } catch (FileNotFoundException var4) {
         this.zidFile = null;
         return;
      }

      if (var5 != null) {
         (new Random()).nextBytes(this.associatedZid);
         ZidRecord var6 = new ZidRecord();
         var6.setIdentifier(this.associatedZid);
         var6.setOwnZIDRecord();

         try {
            this.zidFile.seek(0L);
            this.zidFile.write(var6.getBuffer());
         } catch (IOException var3) {
            try {
               this.zidFile.close();
            } catch (IOException var2) {
               this.zidFile = null;
               return;
            }

            this.zidFile = null;
         }
      }
   }

   public static ZidFile getInstance() {
      synchronized(ZidFile.class){}

      ZidFile var0;
      try {
         if (instance == null) {
            instance = new ZidFile();
         }

         var0 = instance;
      } finally {
         ;
      }

      return var0;
   }

   public void close() {
      // $FF: Couldn't be decompiled
   }

   public ZidRecord getRecord(byte[] param1) {
      // $FF: Couldn't be decompiled
   }

   public byte[] getZid() {
      synchronized(this){}

      byte[] var1;
      try {
         var1 = this.associatedZid;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isOpen() {
      synchronized(this){}
      boolean var4 = false;

      RandomAccessFile var2;
      try {
         var4 = true;
         var2 = this.zidFile;
         var4 = false;
      } finally {
         if (var4) {
            ;
         }
      }

      boolean var1;
      if (var2 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int open(String param1) {
      // $FF: Couldn't be decompiled
   }

   public int saveRecord(ZidRecord var1) {
      synchronized(this){}

      try {
         this.zidFile.seek(var1.getPosition());
         this.zidFile.write(var1.getBuffer());
         return 1;
      } catch (IOException var4) {
      } finally {
         ;
      }

      return -1;
   }
}
