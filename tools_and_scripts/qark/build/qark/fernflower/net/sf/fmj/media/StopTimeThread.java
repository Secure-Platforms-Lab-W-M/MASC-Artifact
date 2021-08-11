package net.sf.fmj.media;

class StopTimeThread extends TimedActionThread {
   public StopTimeThread(BasicController var1, long var2) {
      super(var1, var2);
      StringBuilder var4 = new StringBuilder();
      var4.append(this.getName());
      var4.append(": StopTimeThread");
      this.setName(var4.toString());
      this.wakeupTime = this.getTime() + var2;
   }

   protected void action() {
      this.controller.stopAtTime();
   }

   protected long getTime() {
      return this.controller.getMediaNanoseconds();
   }
}
