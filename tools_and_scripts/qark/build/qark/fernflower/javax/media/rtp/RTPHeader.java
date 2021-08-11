package javax.media.rtp;

import java.io.Serializable;

public class RTPHeader implements Serializable {
   public static final int VALUE_NOT_SET = -1;
   private byte[] extension;
   private boolean extensionPresent;
   private int extensionType = -1;

   public RTPHeader() {
   }

   public RTPHeader(int var1) {
   }

   public RTPHeader(boolean var1, int var2, byte[] var3) {
      this.extensionPresent = var1;
      this.extensionType = var2;
      this.extension = var3;
   }

   public byte[] getExtension() {
      return this.extension;
   }

   public int getExtensionType() {
      return this.extensionType;
   }

   public boolean isExtensionPresent() {
      return this.extensionPresent;
   }

   public void setExtension(byte[] var1) {
      this.extension = var1;
   }

   public void setExtensionPresent(boolean var1) {
      this.extensionPresent = var1;
   }

   public void setExtensionType(int var1) {
      this.extensionType = var1;
   }
}
