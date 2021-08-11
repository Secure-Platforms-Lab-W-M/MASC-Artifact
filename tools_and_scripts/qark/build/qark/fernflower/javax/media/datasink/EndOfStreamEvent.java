package javax.media.datasink;

import javax.media.DataSink;

public class EndOfStreamEvent extends DataSinkEvent {
   public EndOfStreamEvent(DataSink var1) {
      super(var1);
   }

   public EndOfStreamEvent(DataSink var1, String var2) {
      super(var1, var2);
   }
}
