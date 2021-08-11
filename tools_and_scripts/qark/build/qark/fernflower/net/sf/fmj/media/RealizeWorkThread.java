package net.sf.fmj.media;

class RealizeWorkThread extends StateTransitionWorkThread {
   public RealizeWorkThread(BasicController var1) {
      this.controller = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(": ");
      var2.append(var1);
      this.setName(var2.toString());
   }

   protected void aborted() {
      this.controller.abortRealize();
   }

   protected void completed() {
      this.controller.completeRealize();
   }

   protected void failed() {
      this.controller.doFailedRealize();
   }

   protected boolean process() {
      return this.controller.doRealize();
   }
}
