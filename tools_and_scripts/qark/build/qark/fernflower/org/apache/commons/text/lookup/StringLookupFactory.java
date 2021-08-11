package org.apache.commons.text.lookup;

import java.util.Map;

public final class StringLookupFactory {
   public static final StringLookupFactory INSTANCE = new StringLookupFactory();
   public static final String KEY_BASE64_DECODER = "base64Decoder";
   public static final String KEY_BASE64_ENCODER = "base64Encoder";
   public static final String KEY_CONST = "const";
   public static final String KEY_DATE = "date";
   public static final String KEY_DNS = "dns";
   public static final String KEY_ENV = "env";
   public static final String KEY_FILE = "file";
   public static final String KEY_JAVA = "java";
   public static final String KEY_LOCALHOST = "localhost";
   public static final String KEY_PROPERTIES = "properties";
   public static final String KEY_RESOURCE_BUNDLE = "resourceBundle";
   public static final String KEY_SCRIPT = "script";
   public static final String KEY_SYS = "sys";
   public static final String KEY_URL = "url";
   public static final String KEY_URL_DECODER = "urlDecoder";
   public static final String KEY_URL_ENCODER = "urlEncoder";
   public static final String KEY_XML = "xml";

   private StringLookupFactory() {
   }

   public static void clear() {
      ConstantStringLookup.clear();
   }

   public void addDefaultStringLookups(Map var1) {
      if (var1 != null) {
         var1.put("base64", Base64DecoderStringLookup.INSTANCE);
         DefaultStringLookup[] var4 = DefaultStringLookup.values();
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            DefaultStringLookup var5 = var4[var2];
            var1.put(InterpolatorStringLookup.toKey(var5.getKey()), var5.getStringLookup());
         }
      }

   }

   public StringLookup base64DecoderStringLookup() {
      return Base64DecoderStringLookup.INSTANCE;
   }

   public StringLookup base64EncoderStringLookup() {
      return Base64EncoderStringLookup.INSTANCE;
   }

   @Deprecated
   public StringLookup base64StringLookup() {
      return Base64DecoderStringLookup.INSTANCE;
   }

   public StringLookup constantStringLookup() {
      return ConstantStringLookup.INSTANCE;
   }

   public StringLookup dateStringLookup() {
      return DateStringLookup.INSTANCE;
   }

   public StringLookup dnsStringLookup() {
      return DnsStringLookup.INSTANCE;
   }

   public StringLookup environmentVariableStringLookup() {
      return EnvironmentVariableStringLookup.INSTANCE;
   }

   public StringLookup fileStringLookup() {
      return FileStringLookup.INSTANCE;
   }

   public StringLookup interpolatorStringLookup() {
      return InterpolatorStringLookup.INSTANCE;
   }

   public StringLookup interpolatorStringLookup(Map var1) {
      return new InterpolatorStringLookup(var1);
   }

   public StringLookup interpolatorStringLookup(Map var1, StringLookup var2, boolean var3) {
      return new InterpolatorStringLookup(var1, var2, var3);
   }

   public StringLookup interpolatorStringLookup(StringLookup var1) {
      return new InterpolatorStringLookup(var1);
   }

   public StringLookup javaPlatformStringLookup() {
      return JavaPlatformStringLookup.INSTANCE;
   }

   public StringLookup localHostStringLookup() {
      return LocalHostStringLookup.INSTANCE;
   }

   public StringLookup mapStringLookup(Map var1) {
      return MapStringLookup.method_44(var1);
   }

   public StringLookup nullStringLookup() {
      return NullStringLookup.INSTANCE;
   }

   public StringLookup propertiesStringLookup() {
      return PropertiesStringLookup.INSTANCE;
   }

   public StringLookup resourceBundleStringLookup() {
      return ResourceBundleStringLookup.INSTANCE;
   }

   public StringLookup resourceBundleStringLookup(String var1) {
      return new ResourceBundleStringLookup(var1);
   }

   public StringLookup scriptStringLookup() {
      return ScriptStringLookup.INSTANCE;
   }

   public StringLookup systemPropertyStringLookup() {
      return SystemPropertyStringLookup.INSTANCE;
   }

   public StringLookup urlDecoderStringLookup() {
      return UrlDecoderStringLookup.INSTANCE;
   }

   public StringLookup urlEncoderStringLookup() {
      return UrlEncoderStringLookup.INSTANCE;
   }

   public StringLookup urlStringLookup() {
      return UrlStringLookup.INSTANCE;
   }

   public StringLookup xmlStringLookup() {
      return XmlStringLookup.INSTANCE;
   }
}
