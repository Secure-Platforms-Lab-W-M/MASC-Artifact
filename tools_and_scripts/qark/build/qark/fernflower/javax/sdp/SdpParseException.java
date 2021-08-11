package javax.sdp;

public class SdpParseException extends SdpException {
   private int mCharOffset;
   private int mLineNumber;

   public SdpParseException(int var1, int var2, String var3) {
      super(var3);
      this.mLineNumber = var1;
      this.mCharOffset = var2;
   }

   public SdpParseException(int var1, int var2, String var3, Throwable var4) {
      super(var3, var4);
      this.mLineNumber = var1;
      this.mCharOffset = var2;
   }

   public int getCharOffset() {
      return this.mCharOffset;
   }

   public int getLineNumber() {
      return this.mLineNumber;
   }

   public String getMessage() {
      return super.getMessage();
   }
}
