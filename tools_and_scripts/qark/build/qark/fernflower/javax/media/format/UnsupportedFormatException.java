package javax.media.format;

import javax.media.Format;
import javax.media.MediaException;

public class UnsupportedFormatException extends MediaException {
   private final Format unsupportedFormat;

   public UnsupportedFormatException(String var1, Format var2) {
      super(var1);
      this.unsupportedFormat = var2;
   }

   public UnsupportedFormatException(Format var1) {
      this.unsupportedFormat = var1;
   }

   public Format getFailedFormat() {
      return this.unsupportedFormat;
   }
}
