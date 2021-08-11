package net.sf.fmj.media.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;

public class Resource {
   static String AUDIO_FORMAT_KEY;
   static String AUDIO_HIT_KEY;
   static String AUDIO_INPUT_KEY;
   static String AUDIO_SIZE_KEY;
   static int AUDIO_TBL_SIZE;
   static String MISC_FORMAT_KEY;
   static String MISC_HIT_KEY;
   static String MISC_INPUT_KEY;
   static String MISC_SIZE_KEY;
   static int MISC_TBL_SIZE;
   private static final String USERHOME = "user.home";
   static String VIDEO_FORMAT_KEY;
   static String VIDEO_HIT_KEY;
   static String VIDEO_INPUT_KEY;
   static String VIDEO_SIZE_KEY;
   static int VIDEO_TBL_SIZE;
   static FormatTable audioFmtTbl;
   private static String filename = null;
   static Object fmtTblSync;
   private static Hashtable hash = null;
   static FormatTable miscFmtTbl;
   static boolean needSaving;
   private static String userhome = null;
   private static final int versionNumber = 200;
   static FormatTable videoFmtTbl;

   static {
      hash = new Hashtable();
      boolean var0 = true;
      if (true) {
         try {
            userhome = System.getProperty("user.home");
         } catch (Exception var3) {
            userhome = null;
            var0 = false;
         }
      }

      if (userhome == null) {
         var0 = false;
      }

      InputStream var1 = null;
      if (var0) {
         InputStream var2 = findResourceFile();
         var1 = var2;
         if (var2 == null) {
         }
      }

      if (!readResource(var1)) {
         hash = new Hashtable();
      }

      fmtTblSync = new Object();
      AUDIO_TBL_SIZE = 40;
      VIDEO_TBL_SIZE = 20;
      MISC_TBL_SIZE = 10;
      AUDIO_SIZE_KEY = "ATS";
      AUDIO_INPUT_KEY = "AI.";
      AUDIO_FORMAT_KEY = "AF.";
      AUDIO_HIT_KEY = "AH.";
      VIDEO_SIZE_KEY = "VTS";
      VIDEO_INPUT_KEY = "VI.";
      VIDEO_FORMAT_KEY = "VF.";
      VIDEO_HIT_KEY = "VH.";
      MISC_SIZE_KEY = "MTS";
      MISC_INPUT_KEY = "MI.";
      MISC_FORMAT_KEY = "MF.";
      MISC_HIT_KEY = "MH.";
      needSaving = false;
   }

   public static final boolean commit() throws IOException {
      synchronized(Resource.class){}
      return true;
   }

   public static final void destroy() {
      synchronized(Resource.class){}

      Throwable var10000;
      label129: {
         String var0;
         boolean var10001;
         try {
            var0 = filename;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label129;
         }

         if (var0 == null) {
            return;
         }

         boolean var7 = false;

         try {
            var7 = true;
            (new File(filename)).delete();
            var7 = false;
         } finally {
            if (var7) {
               label115:
               try {
                  filename = null;
                  break label115;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label129;
               }
            }
         }

         return;
      }

      Throwable var14 = var10000;
      throw var14;
   }

   private static final InputStream findResourceFile() {
      label138: {
         synchronized(Resource.class){}

         Throwable var10000;
         label139: {
            String var0;
            boolean var10001;
            try {
               var0 = userhome;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label139;
            }

            if (var0 == null) {
               return null;
            }

            boolean var7 = false;

            try {
               var7 = true;
               StringBuilder var14 = new StringBuilder();
               var14.append(userhome);
               var14.append(File.separator);
               var14.append(".fmj.resource");
               filename = var14.toString();
               FileInputStream var15 = getResourceStream(new File(filename));
               var7 = false;
            } finally {
               if (var7) {
                  try {
                     filename = null;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label139;
                  }

                  return null;
               }
            }

         }

         Throwable var16 = var10000;
         throw var16;
      }
   }

   public static final Object get(String var0) {
      synchronized(Resource.class){}
      Object var3;
      if (var0 != null) {
         try {
            var3 = hash.get(var0);
         } finally {
            ;
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   public static final Format[] getDB(Format var0) {
      Object var1 = fmtTblSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label285: {
         try {
            if (audioFmtTbl == null) {
               initDB();
            }
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label285;
         }

         Format[] var33;
         try {
            if (var0 instanceof AudioFormat) {
               var33 = audioFmtTbl.get(var0);
               return var33;
            }
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label285;
         }

         try {
            if (var0 instanceof VideoFormat) {
               var33 = videoFmtTbl.get(var0);
               return var33;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label285;
         }

         label270:
         try {
            var33 = miscFmtTbl.get(var0);
            return var33;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            break label270;
         }
      }

      while(true) {
         Throwable var32 = var10000;

         try {
            throw var32;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   private static final FileInputStream getResourceStream(File var0) throws IOException {
      try {
         if (!var0.exists()) {
            return null;
         } else {
            FileInputStream var3 = new FileInputStream(var0.getPath());
            return var3;
         }
      } finally {
         ;
      }
   }

   static final void initDB() {
      // $FF: Couldn't be decompiled
   }

   private static final void loadDB() {
      Object var2 = fmtTblSync;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label3588: {
         int var0;
         Object var3;
         label3583: {
            try {
               var3 = get(AUDIO_SIZE_KEY);
               if (var3 instanceof Integer) {
                  var0 = (Integer)var3;
                  break label3583;
               }
            } catch (Throwable var311) {
               var10000 = var311;
               var10001 = false;
               break label3588;
            }

            var0 = 0;
         }

         int var1 = var0;

         try {
            if (var0 > AUDIO_TBL_SIZE) {
               System.err.println("Resource file is corrupted");
               var1 = AUDIO_TBL_SIZE;
            }
         } catch (Throwable var310) {
            var10000 = var310;
            var10001 = false;
            break label3588;
         }

         try {
            audioFmtTbl.last = var1;
         } catch (Throwable var309) {
            var10000 = var309;
            var10001 = false;
            break label3588;
         }

         var0 = 0;

         StringBuilder var4;
         StringBuilder var5;
         StringBuilder var312;
         Object var313;
         Object var314;
         while(var0 < var1) {
            label3564: {
               try {
                  var312 = new StringBuilder();
                  var312.append(AUDIO_INPUT_KEY);
                  var312.append(var0);
                  var3 = get(var312.toString());
                  var4 = new StringBuilder();
                  var4.append(AUDIO_FORMAT_KEY);
                  var4.append(var0);
                  var313 = get(var4.toString());
                  var5 = new StringBuilder();
                  var5.append(AUDIO_HIT_KEY);
                  var5.append(var0);
                  var314 = get(var5.toString());
                  if (!(var3 instanceof Format) || !(var313 instanceof Format[]) || !(var314 instanceof Integer)) {
                     break label3564;
                  }

                  audioFmtTbl.keys[var0] = (Format)var3;
                  audioFmtTbl.table[var0] = (Format[])((Format[])var313);
                  audioFmtTbl.hits[var0] = (Integer)var314;
               } catch (Throwable var308) {
                  var10000 = var308;
                  var10001 = false;
                  break label3588;
               }

               ++var0;
               continue;
            }

            try {
               System.err.println("Resource file is corrupted");
               audioFmtTbl.last = 0;
               break;
            } catch (Throwable var307) {
               var10000 = var307;
               var10001 = false;
               break label3588;
            }
         }

         label3549: {
            try {
               var3 = get(VIDEO_SIZE_KEY);
               if (var3 instanceof Integer) {
                  var0 = (Integer)var3;
                  break label3549;
               }
            } catch (Throwable var306) {
               var10000 = var306;
               var10001 = false;
               break label3588;
            }

            var0 = 0;
         }

         var1 = var0;

         try {
            if (var0 > VIDEO_TBL_SIZE) {
               System.err.println("Resource file is corrupted");
               var1 = VIDEO_TBL_SIZE;
            }
         } catch (Throwable var305) {
            var10000 = var305;
            var10001 = false;
            break label3588;
         }

         try {
            videoFmtTbl.last = var1;
         } catch (Throwable var304) {
            var10000 = var304;
            var10001 = false;
            break label3588;
         }

         for(var0 = 0; var0 < var1; ++var0) {
            try {
               var312 = new StringBuilder();
               var312.append(VIDEO_INPUT_KEY);
               var312.append(var0);
               var3 = get(var312.toString());
               var4 = new StringBuilder();
               var4.append(VIDEO_FORMAT_KEY);
               var4.append(var0);
               var313 = get(var4.toString());
               var5 = new StringBuilder();
               var5.append(VIDEO_HIT_KEY);
               var5.append(var0);
               var314 = get(var5.toString());
               if (var3 instanceof Format && var313 instanceof Format[] && var314 instanceof Integer) {
                  videoFmtTbl.keys[var0] = (Format)var3;
                  videoFmtTbl.table[var0] = (Format[])((Format[])var313);
                  videoFmtTbl.hits[var0] = (Integer)var314;
                  continue;
               }
            } catch (Throwable var303) {
               var10000 = var303;
               var10001 = false;
               break label3588;
            }

            try {
               System.err.println("Resource file is corrupted");
               videoFmtTbl.last = 0;
               break;
            } catch (Throwable var302) {
               var10000 = var302;
               var10001 = false;
               break label3588;
            }
         }

         label3516: {
            try {
               var3 = get(MISC_SIZE_KEY);
               if (var3 instanceof Integer) {
                  var0 = (Integer)var3;
                  break label3516;
               }
            } catch (Throwable var301) {
               var10000 = var301;
               var10001 = false;
               break label3588;
            }

            var0 = 0;
         }

         var1 = var0;

         try {
            if (var0 > MISC_TBL_SIZE) {
               System.err.println("Resource file is corrupted");
               var1 = MISC_TBL_SIZE;
            }
         } catch (Throwable var300) {
            var10000 = var300;
            var10001 = false;
            break label3588;
         }

         try {
            miscFmtTbl.last = var1;
         } catch (Throwable var299) {
            var10000 = var299;
            var10001 = false;
            break label3588;
         }

         for(var0 = 0; var0 < var1; ++var0) {
            try {
               var312 = new StringBuilder();
               var312.append(MISC_INPUT_KEY);
               var312.append(var0);
               var3 = get(var312.toString());
               var4 = new StringBuilder();
               var4.append(MISC_FORMAT_KEY);
               var4.append(var0);
               var313 = get(var4.toString());
               var5 = new StringBuilder();
               var5.append(MISC_HIT_KEY);
               var5.append(var0);
               var314 = get(var5.toString());
               if (var3 instanceof Format && var313 instanceof Format[] && var314 instanceof Integer) {
                  miscFmtTbl.keys[var0] = (Format)var3;
                  miscFmtTbl.table[var0] = (Format[])((Format[])var313);
                  miscFmtTbl.hits[var0] = (Integer)var314;
                  continue;
               }
            } catch (Throwable var298) {
               var10000 = var298;
               var10001 = false;
               break label3588;
            }

            try {
               System.err.println("Resource file is corrupted");
               miscFmtTbl.last = 0;
               break;
            } catch (Throwable var297) {
               var10000 = var297;
               var10001 = false;
               break label3588;
            }
         }

         label3484:
         try {
            return;
         } catch (Throwable var296) {
            var10000 = var296;
            var10001 = false;
            break label3484;
         }
      }

      while(true) {
         Throwable var315 = var10000;

         try {
            throw var315;
         } catch (Throwable var295) {
            var10000 = var295;
            var10001 = false;
            continue;
         }
      }
   }

   public static final void purgeDB() {
      Object var0 = fmtTblSync;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            if (audioFmtTbl == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            audioFmtTbl = new FormatTable(AUDIO_TBL_SIZE);
            videoFmtTbl = new FormatTable(VIDEO_TBL_SIZE);
            miscFmtTbl = new FormatTable(MISC_TBL_SIZE);
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static final Format[] putDB(Format var0, Format[] var1) {
      Object var3 = fmtTblSync;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label567: {
         Format[] var4;
         try {
            var0 = var0.relax();
            var4 = new Format[var1.length];
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label567;
         }

         int var2 = 0;

         while(true) {
            try {
               if (var2 >= var1.length) {
                  break;
               }

               var4[var2] = var1[var2].relax();
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label567;
            }

            ++var2;
         }

         label568: {
            try {
               if (var0 instanceof AudioFormat) {
                  audioFmtTbl.save(var0, var4);
                  break label568;
               }
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label567;
            }

            try {
               if (var0 instanceof VideoFormat) {
                  videoFmtTbl.save(var0, var4);
                  break label568;
               }
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label567;
            }

            try {
               miscFmtTbl.save(var0, var4);
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label567;
            }
         }

         label531:
         try {
            needSaving = true;
            return var4;
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label531;
         }
      }

      while(true) {
         Throwable var61 = var10000;

         try {
            throw var61;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            continue;
         }
      }
   }

   private static final boolean readResource(InputStream param0) {
      // $FF: Couldn't be decompiled
   }

   public static final boolean remove(String var0) {
      synchronized(Resource.class){}
      if (var0 != null) {
         try {
            if (!hash.containsKey(var0)) {
               return false;
            }

            hash.remove(var0);
         } finally {
            ;
         }

         return true;
      } else {
         return false;
      }
   }

   public static final void removeGroup(String var0) {
      synchronized(Resource.class){}

      label262: {
         Throwable var10000;
         label266: {
            boolean var10001;
            Vector var2;
            try {
               var2 = new Vector();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label266;
            }

            if (var0 != null) {
               Enumeration var3;
               try {
                  var3 = hash.keys();
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label266;
               }

               label255:
               while(true) {
                  try {
                     String var4;
                     do {
                        if (!var3.hasMoreElements()) {
                           break label255;
                        }

                        var4 = (String)var3.nextElement();
                     } while(!var4.startsWith(var0));

                     var2.addElement(var4);
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label266;
                  }
               }
            }

            int var1 = 0;

            while(true) {
               try {
                  if (var1 >= var2.size()) {
                     break label262;
                  }

                  hash.remove(var2.elementAt(var1));
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }

               ++var1;
            }
         }

         Throwable var25 = var10000;
         throw var25;
      }

   }

   public static final void reset() {
      synchronized(Resource.class){}

      try {
         hash = new Hashtable();
      } finally {
         ;
      }

   }

   public static final void saveDB() {
      Object var1 = fmtTblSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label1037: {
         try {
            if (!needSaving) {
               return;
            }
         } catch (Throwable var112) {
            var10000 = var112;
            var10001 = false;
            break label1037;
         }

         try {
            reset();
            set(AUDIO_SIZE_KEY, new Integer(audioFmtTbl.last));
         } catch (Throwable var111) {
            var10000 = var111;
            var10001 = false;
            break label1037;
         }

         int var0 = 0;

         StringBuilder var2;
         while(true) {
            try {
               if (var0 >= audioFmtTbl.last) {
                  break;
               }

               var2 = new StringBuilder();
               var2.append(AUDIO_INPUT_KEY);
               var2.append(var0);
               set(var2.toString(), audioFmtTbl.keys[var0]);
               var2 = new StringBuilder();
               var2.append(AUDIO_FORMAT_KEY);
               var2.append(var0);
               set(var2.toString(), audioFmtTbl.table[var0]);
               var2 = new StringBuilder();
               var2.append(AUDIO_HIT_KEY);
               var2.append(var0);
               set(var2.toString(), new Integer(audioFmtTbl.hits[var0]));
            } catch (Throwable var110) {
               var10000 = var110;
               var10001 = false;
               break label1037;
            }

            ++var0;
         }

         try {
            set(VIDEO_SIZE_KEY, new Integer(videoFmtTbl.last));
         } catch (Throwable var109) {
            var10000 = var109;
            var10001 = false;
            break label1037;
         }

         var0 = 0;

         while(true) {
            try {
               if (var0 >= videoFmtTbl.last) {
                  break;
               }

               var2 = new StringBuilder();
               var2.append(VIDEO_INPUT_KEY);
               var2.append(var0);
               set(var2.toString(), videoFmtTbl.keys[var0]);
               var2 = new StringBuilder();
               var2.append(VIDEO_FORMAT_KEY);
               var2.append(var0);
               set(var2.toString(), videoFmtTbl.table[var0]);
               var2 = new StringBuilder();
               var2.append(VIDEO_HIT_KEY);
               var2.append(var0);
               set(var2.toString(), new Integer(videoFmtTbl.hits[var0]));
            } catch (Throwable var108) {
               var10000 = var108;
               var10001 = false;
               break label1037;
            }

            ++var0;
         }

         try {
            set(MISC_SIZE_KEY, new Integer(miscFmtTbl.last));
         } catch (Throwable var107) {
            var10000 = var107;
            var10001 = false;
            break label1037;
         }

         var0 = 0;

         while(true) {
            try {
               if (var0 >= miscFmtTbl.last) {
                  break;
               }

               var2 = new StringBuilder();
               var2.append(MISC_INPUT_KEY);
               var2.append(var0);
               set(var2.toString(), miscFmtTbl.keys[var0]);
               var2 = new StringBuilder();
               var2.append(MISC_FORMAT_KEY);
               var2.append(var0);
               set(var2.toString(), miscFmtTbl.table[var0]);
               var2 = new StringBuilder();
               var2.append(MISC_HIT_KEY);
               var2.append(var0);
               set(var2.toString(), new Integer(miscFmtTbl.hits[var0]));
            } catch (Throwable var106) {
               var10000 = var106;
               var10001 = false;
               break label1037;
            }

            ++var0;
         }

         label987:
         try {
            commit();
         } finally {
            break label987;
         }

         label990:
         try {
            needSaving = false;
            return;
         } catch (Throwable var105) {
            var10000 = var105;
            var10001 = false;
            break label990;
         }
      }

      while(true) {
         Throwable var113 = var10000;

         try {
            throw var113;
         } catch (Throwable var103) {
            var10000 = var103;
            var10001 = false;
            continue;
         }
      }
   }

   public static final boolean set(String var0, Object var1) {
      synchronized(Resource.class){}
      if (var0 != null && var1 != null) {
         try {
            hash.put(var0, var1);
         } finally {
            ;
         }

         return true;
      } else {
         return false;
      }
   }
}
