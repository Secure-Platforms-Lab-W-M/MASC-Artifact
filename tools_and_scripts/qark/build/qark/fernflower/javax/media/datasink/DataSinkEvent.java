package javax.media.datasink;

import javax.media.DataSink;
import javax.media.MediaEvent;

public class DataSinkEvent extends MediaEvent {
   private String message;

   public DataSinkEvent(DataSink var1) {
      super(var1);
      this.message = "";
   }

   public DataSinkEvent(DataSink var1, String var2) {
      super(var1);
      this.message = var2;
   }

   public DataSink getSourceDataSink() {
      return (DataSink)this.getSource();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(DataSinkEvent.class.getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append("] message: ");
      var1.append(this.message);
      return var1.toString();
   }
}
