package javax.media.format;

import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.Format;

public class FormatChangeEvent extends ControllerEvent {
   protected Format newFormat;
   protected Format oldFormat;

   public FormatChangeEvent(Controller var1) {
      super(var1);
   }

   public FormatChangeEvent(Controller var1, Format var2, Format var3) {
      super(var1);
      this.oldFormat = var2;
      this.newFormat = var3;
   }

   public Format getNewFormat() {
      return this.newFormat;
   }

   public Format getOldFormat() {
      return this.oldFormat;
   }
}
