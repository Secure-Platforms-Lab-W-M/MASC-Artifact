package javax.media.rtp.rtcp;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SourceDescription implements Serializable {
   public static final int SOURCE_DESC_CNAME = 1;
   public static final int SOURCE_DESC_EMAIL = 3;
   public static final int SOURCE_DESC_LOC = 5;
   public static final int SOURCE_DESC_NAME = 2;
   public static final int SOURCE_DESC_NOTE = 7;
   public static final int SOURCE_DESC_PHONE = 4;
   public static final int SOURCE_DESC_PRIV = 8;
   public static final int SOURCE_DESC_TOOL = 6;
   private String description;
   private boolean encrypted;
   private int frequency;
   private int type;

   public SourceDescription(int var1, String var2, int var3, boolean var4) {
      this.type = var1;
      this.description = var2;
      this.frequency = var3;
      this.encrypted = var4;
   }

   public static String generateCNAME() {
      String var0;
      try {
         var0 = InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException var2) {
         throw new RuntimeException(var2);
      }

      StringBuilder var1 = new StringBuilder();
      var1.append(System.getProperty("user.name"));
      var1.append('@');
      var1.append(var0);
      return var1.toString();
   }

   public String getDescription() {
      return this.description;
   }

   public boolean getEncrypted() {
      return this.encrypted;
   }

   public int getFrequency() {
      return this.frequency;
   }

   public int getType() {
      return this.type;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }
}
