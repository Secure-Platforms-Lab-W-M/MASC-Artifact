package gnu.java.zrtp;

public class ZrtpSrtpSecrets {
   ZrtpConstants.SupportedAuthAlgos authAlgorithm;
   protected int initKeyLen;
   protected int initSaltLen;
   protected byte[] keyInitiator;
   protected byte[] keyResponder;
   protected int respKeyLen;
   protected int respSaltLen;
   protected ZrtpCallback.Role role;
   protected byte[] saltInitiator;
   protected byte[] saltResponder;
   protected int srtpAuthTagLen;
   ZrtpConstants.SupportedSymAlgos symEncAlgorithm;

   protected ZrtpSrtpSecrets() {
   }

   public ZrtpConstants.SupportedAuthAlgos getAuthAlgorithm() {
      return this.authAlgorithm;
   }

   public int getInitKeyLen() {
      return this.initKeyLen;
   }

   public int getInitSaltLen() {
      return this.initSaltLen;
   }

   public byte[] getKeyInitiator() {
      return this.keyInitiator;
   }

   public byte[] getKeyResponder() {
      return this.keyResponder;
   }

   public int getRespKeyLen() {
      return this.respKeyLen;
   }

   public int getRespSaltLen() {
      return this.respSaltLen;
   }

   public ZrtpCallback.Role getRole() {
      return this.role;
   }

   public byte[] getSaltInitiator() {
      return this.saltInitiator;
   }

   public byte[] getSaltResponder() {
      return this.saltResponder;
   }

   public int getSrtpAuthTagLen() {
      return this.srtpAuthTagLen;
   }

   public ZrtpConstants.SupportedSymAlgos getSymEncAlgorithm() {
      return this.symEncAlgorithm;
   }

   public void setInitKeyLen(int var1) {
      this.initKeyLen = var1;
   }

   public void setInitSaltLen(int var1) {
      this.initSaltLen = var1;
   }

   public void setKeyInitiator(byte[] var1) {
      this.keyInitiator = var1;
   }

   public void setKeyResponder(byte[] var1) {
      this.keyResponder = var1;
   }

   public void setRespKeyLen(int var1) {
      this.respKeyLen = var1;
   }

   public void setRespSaltLen(int var1) {
      this.respSaltLen = var1;
   }

   public void setRole(ZrtpCallback.Role var1) {
      this.role = var1;
   }

   public void setSaltInitiator(byte[] var1) {
      this.saltInitiator = var1;
   }

   public void setSaltResponder(byte[] var1) {
      this.saltResponder = var1;
   }

   public void setSrtpAuthTagLen(int var1) {
      this.srtpAuthTagLen = var1;
   }
}
