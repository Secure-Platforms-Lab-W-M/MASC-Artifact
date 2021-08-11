package org.apache.commons.text.lookup;

public enum DefaultStringLookup {
   BASE64_DECODER("base64Decoder", StringLookupFactory.INSTANCE.base64DecoderStringLookup()),
   BASE64_ENCODER("base64Encoder", StringLookupFactory.INSTANCE.base64EncoderStringLookup()),
   CONST("const", StringLookupFactory.INSTANCE.constantStringLookup()),
   DATE("date", StringLookupFactory.INSTANCE.dateStringLookup()),
   DNS("dns", StringLookupFactory.INSTANCE.dnsStringLookup()),
   ENVIRONMENT("env", StringLookupFactory.INSTANCE.environmentVariableStringLookup()),
   FILE("file", StringLookupFactory.INSTANCE.fileStringLookup()),
   JAVA("java", StringLookupFactory.INSTANCE.javaPlatformStringLookup()),
   LOCAL_HOST("localhost", StringLookupFactory.INSTANCE.localHostStringLookup()),
   PROPERTIES("properties", StringLookupFactory.INSTANCE.propertiesStringLookup()),
   RESOURCE_BUNDLE("resourceBundle", StringLookupFactory.INSTANCE.resourceBundleStringLookup()),
   SCRIPT("script", StringLookupFactory.INSTANCE.scriptStringLookup()),
   SYSTEM_PROPERTIES("sys", StringLookupFactory.INSTANCE.systemPropertyStringLookup()),
   URL("url", StringLookupFactory.INSTANCE.urlStringLookup()),
   URL_DECODER("urlDecoder", StringLookupFactory.INSTANCE.urlDecoderStringLookup()),
   URL_ENCODER("urlEncoder", StringLookupFactory.INSTANCE.urlEncoderStringLookup()),
   XML;

   private final String key;
   private final StringLookup lookup;

   static {
      DefaultStringLookup var0 = new DefaultStringLookup("XML", 16, "xml", StringLookupFactory.INSTANCE.xmlStringLookup());
      XML = var0;
   }

   private DefaultStringLookup(String var3, StringLookup var4) {
      this.key = var3;
      this.lookup = var4;
   }

   public String getKey() {
      return this.key;
   }

   public StringLookup getStringLookup() {
      return this.lookup;
   }
}
