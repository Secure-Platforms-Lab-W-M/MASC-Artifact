package net.sf.fmj.media.rtp;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import javax.media.MediaLocator;

public class RTPMediaLocator extends MediaLocator {
   public static final int PORT_UNDEFINED = -1;
   public static final int SSRC_UNDEFINED = 0;
   public static final int TTL_UNDEFINED = 1;
   String address = "";
   String contentType = "";
   int port = -1;
   long ssrc = 0L;
   int ttl = 1;
   private boolean valid = true;

   public RTPMediaLocator(String var1) throws MalformedURLException {
      super(var1);
      this.parseLocator(var1);
   }

   private void parseLocator(String var1) throws MalformedURLException {
      String var6 = this.getRemainder();
      int var4 = var6.indexOf(":");
      int var5 = var6.indexOf("/", 2);
      int var2 = -1;
      int var3 = -1;
      if (var4 != -1) {
         var2 = var6.indexOf(":", var4 + 1);
      }

      if (var5 != -1) {
         var3 = var6.indexOf("/", var5 + 1);
      }

      if (var4 != -1) {
         this.address = var6.substring(2, var4);
      }

      try {
         InetAddress.getByName(this.address);
      } catch (UnknownHostException var9) {
         throw new MalformedURLException("Valid RTP Session Address must be given");
      }

      if (var4 != -1 && var5 != -1) {
         if (var2 == -1) {
            var1 = var6.substring(var4 + 1, var5);
         } else {
            var1 = var6.substring(var4 + 1, var2);
         }

         try {
            this.port = Integer.valueOf(var1);
         } catch (NumberFormatException var8) {
            throw new MalformedURLException("RTP MediaLocator Port must be a valid integer");
         }

         if (var2 != -1) {
            var1 = var6.substring(var2 + 1, var5);

            try {
               this.ssrc = Long.valueOf(var1);
            } catch (NumberFormatException var7) {
            }
         }

         if (var5 != -1) {
            if (var3 == -1) {
               this.contentType = var6.substring(var5 + 1, var6.length());
            } else {
               this.contentType = var6.substring(var5 + 1, var3);
            }

            if (!this.contentType.equals("audio") && !this.contentType.equals("video")) {
               throw new MalformedURLException("Content Type in URL must be audio or video ");
            }

            StringBuilder var11 = new StringBuilder();
            var11.append("rtp/");
            var11.append(this.contentType);
            this.contentType = var11.toString();
         }

         if (var3 != -1) {
            var1 = var6.substring(var3 + 1, var6.length());

            try {
               this.ttl = Integer.valueOf(var1);
               return;
            } catch (NumberFormatException var10) {
            }
         }

      } else {
         throw new MalformedURLException("RTP MediaLocator is Invalid. Must be of form rtp://addr:port/content/ttl");
      }
   }

   public String getContentType() {
      return this.contentType;
   }

   public long getSSRC() {
      return this.ssrc;
   }

   public String getSessionAddress() {
      return this.address;
   }

   public int getSessionPort() {
      return this.port;
   }

   public int getTTL() {
      return this.ttl;
   }

   public boolean isValid() {
      return this.valid;
   }
}
