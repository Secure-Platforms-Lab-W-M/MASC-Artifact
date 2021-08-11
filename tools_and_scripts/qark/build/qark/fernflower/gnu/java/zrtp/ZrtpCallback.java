package gnu.java.zrtp;

import java.util.EnumSet;

public interface ZrtpCallback {
   int activateTimer(int var1);

   int cancelTimer();

   boolean checkSASSignature(byte[] var1);

   void handleGoClear();

   boolean sendDataZRTP(byte[] var1);

   void sendInfo(ZrtpCodes.MessageSeverity var1, EnumSet var2);

   void signSAS(byte[] var1);

   void srtpSecretsOff(ZrtpCallback.EnableSecurity var1);

   void srtpSecretsOn(String var1, String var2, boolean var3);

   boolean srtpSecretsReady(ZrtpSrtpSecrets var1, ZrtpCallback.EnableSecurity var2);

   void zrtpAskEnrollment(ZrtpCodes.InfoEnrollment var1);

   void zrtpInformEnrollment(ZrtpCodes.InfoEnrollment var1);

   void zrtpNegotiationFailed(ZrtpCodes.MessageSeverity var1, EnumSet var2);

   void zrtpNotSuppOther();

   public static enum EnableSecurity {
      ForReceiver,
      ForSender;

      static {
         ZrtpCallback.EnableSecurity var0 = new ZrtpCallback.EnableSecurity("ForSender", 1);
         ForSender = var0;
      }
   }

   public static enum Role {
      Initiator,
      Responder;

      static {
         ZrtpCallback.Role var0 = new ZrtpCallback.Role("Initiator", 1);
         Initiator = var0;
      }
   }
}
