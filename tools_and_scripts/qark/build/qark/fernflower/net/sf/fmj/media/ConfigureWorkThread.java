package net.sf.fmj.media;

class ConfigureWorkThread extends StateTransitionWorkThread {
   public ConfigureWorkThread(BasicController var1) {
      this.controller = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(": ");
      var2.append(var1);
      this.setName(var2.toString());
   }

   protected void aborted() {
      this.controller.abortConfigure();
   }

   protected void completed() {
      this.controller.completeConfigure();
   }

   protected void failed() {
      this.controller.doFailedConfigure();
   }

   protected boolean process() {
      return this.controller.doConfigure();
   }
}
