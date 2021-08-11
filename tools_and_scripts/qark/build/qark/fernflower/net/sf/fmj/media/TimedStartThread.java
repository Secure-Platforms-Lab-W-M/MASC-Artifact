package net.sf.fmj.media;

class TimedStartThread extends TimedActionThread {
   public TimedStartThread(BasicController var1, long var2) {
      super(var1, var2);
      StringBuilder var4 = new StringBuilder();
      var4.append(this.getName());
      var4.append(": TimedStartThread");
      this.setName(var4.toString());
   }

   protected void action() {
      this.controller.doStart();
   }

   protected long getTime() {
      return this.controller.getTimeBase().getNanoseconds();
   }
}
