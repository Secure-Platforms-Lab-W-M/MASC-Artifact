package org.jsoup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class Jsoup {
   private Jsoup() {
   }

   public static String clean(String var0, String var1, Whitelist var2) {
      Document var3 = parseBodyFragment(var0, var1);
      return (new Cleaner(var2)).clean(var3).body().html();
   }

   public static String clean(String var0, String var1, Whitelist var2, Document.OutputSettings var3) {
      Document var4 = parseBodyFragment(var0, var1);
      var4 = (new Cleaner(var2)).clean(var4);
      var4.outputSettings(var3);
      return var4.body().html();
   }

   public static String clean(String var0, Whitelist var1) {
      return clean(var0, "", var1);
   }

   public static Connection connect(String var0) {
      return HttpConnection.connect(var0);
   }

   public static boolean isValid(String var0, Whitelist var1) {
      Document var2 = parseBodyFragment(var0, "");
      return (new Cleaner(var1)).isValid(var2);
   }

   public static Document parse(File var0, String var1) throws IOException {
      return DataUtil.load(var0, var1, var0.getAbsolutePath());
   }

   public static Document parse(File var0, String var1, String var2) throws IOException {
      return DataUtil.load(var0, var1, var2);
   }

   public static Document parse(InputStream var0, String var1, String var2) throws IOException {
      return DataUtil.load(var0, var1, var2);
   }

   public static Document parse(InputStream var0, String var1, String var2, Parser var3) throws IOException {
      return DataUtil.load(var0, var1, var2, var3);
   }

   public static Document parse(String var0) {
      return Parser.parse(var0, "");
   }

   public static Document parse(String var0, String var1) {
      return Parser.parse(var0, var1);
   }

   public static Document parse(String var0, String var1, Parser var2) {
      return var2.parseInput(var0, var1);
   }

   public static Document parse(URL var0, int var1) throws IOException {
      Connection var2 = HttpConnection.connect(var0);
      var2.timeout(var1);
      return var2.get();
   }

   public static Document parseBodyFragment(String var0) {
      return Parser.parseBodyFragment(var0, "");
   }

   public static Document parseBodyFragment(String var0, String var1) {
      return Parser.parseBodyFragment(var0, var1);
   }
}
