package net.sf.fmj.ejmf.toolkit.media.event;

import javax.media.ControllerErrorEvent;
import javax.media.Player;

public class ManagedControllerErrorEvent extends ControllerErrorEvent {
   private ControllerErrorEvent event;

   public ManagedControllerErrorEvent(Player var1, ControllerErrorEvent var2) {
      super(var1);
      this.event = var2;
   }

   public ManagedControllerErrorEvent(Player var1, ControllerErrorEvent var2, String var3) {
      super(var1, var3);
      this.event = var2;
   }

   public ControllerErrorEvent getControllerErrorEvent() {
      return this.event;
   }
}
