package net.sf.fmj.utility;

public class FPSCounter {
   private int frames;
   private long start;

   public double getFPS() {
      long var1 = System.currentTimeMillis();
      return (double)this.frames * 1000.0D / (double)(var1 - this.start);
   }

   public int getNumFrames() {
      return this.frames;
   }

   public void nextFrame() {
      if (this.start == 0L) {
         this.start = System.currentTimeMillis();
      }

      ++this.frames;
   }

   public void reset() {
      this.start = 0L;
      this.frames = 0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FPS: ");
      var1.append(this.getFPS());
      return var1.toString();
   }
}
