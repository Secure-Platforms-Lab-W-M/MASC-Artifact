package net.sf.fmj.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.registry.Registry;
import net.sf.fmj.utility.LoggerSingleton;

public class MimeManager {
   private static final MimeTable defaultMimeTable;
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
      defaultMimeTable = new MimeTable();
      put("mvr", "application/mvr");
      put("aif", "audio/x_aiff");
      put("aiff", "audio/x_aiff");
      put("midi", "audio/midi");
      put("jmx", "application/x_jmx");
      put("mpv", "video/mpeg");
      put("mpg", "video/mpeg");
      put("wav", "audio/x_wav");
      put("mp3", "audio/mpeg");
      put("mpa", "audio/mpeg");
      put("mp2", "audio/mpeg");
      put("spl", "application/futuresplash");
      put("viv", "video/vivo");
      put("au", "audio/basic");
      put("g729", "audio/g729");
      put("mov", "video/quicktime");
      put("avi", "video/x_msvideo");
      put("g728", "audio/g728");
      put("cda", "audio/cdaudio");
      put("g729a", "audio/g729a");
      put("gsm", "audio/x_gsm");
      put("mid", "audio/midi");
      put("swf", "application/x-shockwave-flash");
      put("rmf", "audio/rmf");
      boolean var0 = false;

      label17: {
         boolean var1;
         try {
            var1 = System.getProperty("net.sf.fmj.utility.JmfRegistry.JMFDefaults", "false").equals("true");
         } catch (SecurityException var3) {
            break label17;
         }

         var0 = var1;
      }

      if (!var0) {
         put("ogg", "audio/ogg");
         put("ogx", "application/ogg");
         put("oga", "audio/ogg");
         put("ogv", "video/ogg");
         put("spx", "audio/ogg");
         put("flac", "application/flac");
         put("anx", "application/annodex");
         put("axa", "audio/annodex");
         put("axv", "video/annodex");
         put("xspf", "application/xspf+xml ");
         put("asf", "video/x-ms-asf");
         put("asx", "video/x-ms-asf");
         put("wma", "audio/x-ms-wma");
         put("wax", "audio/x-ms-wax");
         put("wmv", "video/x-ms-wmv");
         put("wvx", "video/x-ms-wvx");
         put("wm", "video/x-ms-wm");
         put("wmx", "video/x-ms-wmx");
         put("wmz", "application/x-ms-wmz");
         put("wmd", "application/x-ms-wmd");
         put("mpeg4", "video/mpeg");
         put("mp4", "video/mpeg");
         put("3gp", "video/3gpp");
         put("3g2", "video/3gpp");
         put("h264", "video/mp4");
         put("m4v", "video/mp4v");
         put("m2v", "video/mp2p");
         put("vob", "video/mp2p");
         put("ts", "video/x-mpegts");
         put("mpeg", "video/mpeg");
         put("m1v", "video/mpeg");
         put("mjpg", "video/x-mjpeg");
         put("mjpeg", "video/x-mjpeg");
         put("flv", "video/x-flv");
         put("fli", "video/fli");
         put("flc", "video/flc");
         put("flx", "video/flc");
         put("mkv", "video/x-matroska");
         put("mka", "audio/x-matroska");
         put("mpc", "audio/x-musepack");
         put("mp+", "audio/x-musepack");
         put("mpp", "audio/x-musepack");
         put("rm", "application/vnd.rn-realmedia");
         put("ra", "application/vnd.rn-realmedia");
         put("dv", "video/x-dv");
         put("dif", "video/x-dv");
         put("aac", "audio/X-HX-AAC-ADTS");
         put("mj2", "video/mj2");
         put("mjp2", "video/mj2");
         put("mtv", "video/x-amv");
         put("amv", "video/x-amv");
         put("nsv", "application/x-nsv-vp3-mp3");
         put("nuv", "video/x-nuv");
         put("nuv", "application/mxf");
         put("shn", "application/x-shorten");
         put("tta", "audio/x-tta");
         put("voc", "audio/x-voc");
         put("wv", "audio/x-wavpack");
         put("wvp", "audio/x-wavpack");
         put("4xm", "video/x-4xm");
         put("aud", "video/x-wsaud");
         put("apc", "audio/x-apc");
         put("avs", "video/x-avs");
         put("c93", "video/x-c93");
         put("cin", "video/x-dsicin");
         put("cin", "video/x-idcin");
         put("cpk", "video/x-film-cpk");
         put("dts", "audio/x-raw-dts");
         put("dxa", "video/x-dxa");
         put("gxf", "video/x-gxf");
         put("mm", "video/x-mm");
         put("mve", "video/x-wc3-movie");
         put("mve", "video/x-mve");
         put("roq", "video/x-roq");
         put("seq", "video/x-seq");
         put("smk", "video/x-smk");
         put("sol", "audio/x-sol");
         put("str", "audio/x-psxstr");
         put("thp", "video/x-thp");
         put("txd", "video/x-txd");
         put("uv2", "video/x-ea");
         put("vc1", "video/x-raw-vc1");
         put("vid", "video/x-bethsoft-vid");
         put("vmd", "video/x-vmd");
         put("vqa", "video/x-wsvqa");
         put("wve", "video/x-ea");
         put("yuv", "video/x-raw-yuv");
         put("mmr", "multipart/x-mixed-replace");
         put("xmv", "video/xml");
      }

   }

   protected MimeManager() {
   }

   public static final boolean addMimeType(String var0, String var1) {
      var0 = nullSafeToLowerCase(var0);
      var1 = nullSafeToLowerCase(var1);
      if (defaultMimeTable.getMimeType(var0) != null) {
         logger.warning("Cannot override default mime-table entries");
         return false;
      } else {
         Registry.getInstance().addMimeType(var0, var1);
         return true;
      }
   }

   public static void commit() {
      try {
         Registry.getInstance().commit();
      } catch (IOException var4) {
         Logger var1 = logger;
         Level var2 = Level.WARNING;
         StringBuilder var3 = new StringBuilder();
         var3.append("");
         var3.append(var4);
         var1.log(var2, var3.toString(), var4);
      }
   }

   public static final String getDefaultExtension(String var0) {
      var0 = nullSafeToLowerCase(var0);
      String var1 = Registry.getInstance().getDefaultExtension(var0);
      return var1 != null ? var1 : defaultMimeTable.getDefaultExtension(var0);
   }

   public static final Hashtable getDefaultMimeTable() {
      return defaultMimeTable.getMimeTable();
   }

   public static final List getExtensions(String var0) {
      var0 = nullSafeToLowerCase(var0);
      ArrayList var1 = new ArrayList();
      var1.addAll(defaultMimeTable.getExtensions(var0));
      var1.addAll(Registry.getInstance().getExtensions(var0));
      return var1;
   }

   public static final Hashtable getMimeTable() {
      Hashtable var0 = new Hashtable();
      var0.putAll(defaultMimeTable.getMimeTable());
      var0.putAll(Registry.getInstance().getMimeTable());
      return var0;
   }

   public static final String getMimeType(String var0) {
      var0 = nullSafeToLowerCase(var0);
      String var1 = Registry.getInstance().getMimeType(var0);
      return var1 != null ? var1 : defaultMimeTable.getMimeType(var0);
   }

   private static final String nullSafeToLowerCase(String var0) {
      return var0 == null ? var0 : var0.toLowerCase();
   }

   private static void put(String var0, String var1) {
      var0 = nullSafeToLowerCase(var0);
      var1 = nullSafeToLowerCase(var1);
      defaultMimeTable.addMimeType(var0, var1);
   }

   public static final boolean removeMimeType(String var0) {
      var0 = nullSafeToLowerCase(var0);
      return Registry.getInstance().removeMimeType(var0);
   }
}
