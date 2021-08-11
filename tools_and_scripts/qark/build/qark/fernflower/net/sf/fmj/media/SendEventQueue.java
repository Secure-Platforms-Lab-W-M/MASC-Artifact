package net.sf.fmj.media;

import javax.media.ControllerEvent;
import net.sf.fmj.media.util.ThreadedEventQueue;

class SendEventQueue extends ThreadedEventQueue {
   private BasicController controller;

   public SendEventQueue(BasicController var1) {
      this.controller = var1;
   }

   public void processEvent(ControllerEvent var1) {
      this.controller.dispatchEvent(var1);
   }
}
