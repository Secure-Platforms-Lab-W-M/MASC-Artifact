package net.sf.fmj.ejmf.toolkit.util;

import java.util.EventObject;

public class SourcedTimerEvent extends EventObject {
   private long time;

   public SourcedTimerEvent(Object var1, long var2) {
      super(var1);
      this.time = var2;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long var1) {
      this.time = var1;
   }
}
