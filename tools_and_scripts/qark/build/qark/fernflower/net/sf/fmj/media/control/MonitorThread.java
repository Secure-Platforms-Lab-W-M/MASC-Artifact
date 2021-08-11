package net.sf.fmj.media.control;

import net.sf.fmj.media.util.LoopThread;

class MonitorThread extends LoopThread {
   // $FF: renamed from: ad net.sf.fmj.media.control.MonitorAdapter
   MonitorAdapter field_26;

   public MonitorThread(MonitorAdapter var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(" : MonitorAdapter");
      this.setName(var2.toString());
      this.useVideoPriority();
      this.field_26 = var1;
   }

   protected boolean process() {
      return this.field_26.doProcess();
   }
}
