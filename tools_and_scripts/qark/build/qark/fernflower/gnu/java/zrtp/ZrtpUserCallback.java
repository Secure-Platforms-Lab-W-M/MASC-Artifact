package gnu.java.zrtp;

import java.util.EnumSet;

public class ZrtpUserCallback {
   public boolean checkSASSignature(byte[] var1) {
      return true;
   }

   public void confirmGoClear() {
   }

   public void secureOff() {
   }

   public void secureOn(String var1) {
   }

   public void showMessage(ZrtpCodes.MessageSeverity var1, EnumSet var2) {
   }

   public void showSAS(String var1, boolean var2) {
   }

   public void signSAS(byte[] var1) {
   }

   public void zrtpAskEnrollment(ZrtpCodes.InfoEnrollment var1) {
   }

   public void zrtpInformEnrollment(ZrtpCodes.InfoEnrollment var1) {
   }

   public void zrtpNegotiationFailed(ZrtpCodes.MessageSeverity var1, EnumSet var2) {
   }

   public void zrtpNotSuppOther() {
   }
}
