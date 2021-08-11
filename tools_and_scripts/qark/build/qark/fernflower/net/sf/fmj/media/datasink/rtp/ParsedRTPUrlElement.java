package net.sf.fmj.media.datasink.rtp;

public class ParsedRTPUrlElement {
   public static final String AUDIO = "audio";
   public static final String VIDEO = "video";
   public String host;
   public int port;
   public int ttl;
   public String type;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.host);
      var1.append(":");
      var1.append(this.port);
      var1.append("/");
      var1.append(this.type);
      var1.append("/");
      var1.append(this.ttl);
      return var1.toString();
   }
}
