package net.sf.fmj.media;

class PrefetchWorkThread extends StateTransitionWorkThread {
   public PrefetchWorkThread(BasicController var1) {
      this.controller = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(": ");
      var2.append(var1);
      this.setName(var2.toString());
   }

   protected void aborted() {
      this.controller.abortPrefetch();
   }

   protected void completed() {
      this.controller.completePrefetch();
   }

   protected void failed() {
      this.controller.doFailedPrefetch();
   }

   protected boolean process() {
      return this.controller.doPrefetch();
   }
}
