package net.sf.fmj.media;

import javax.media.Controller;
import javax.media.StopEvent;
import javax.media.Time;

public class SeekFailedEvent extends StopEvent {
   public SeekFailedEvent(Controller var1, int var2, int var3, int var4, Time var5) {
      super(var1, var2, var3, var4, var5);
   }
}
