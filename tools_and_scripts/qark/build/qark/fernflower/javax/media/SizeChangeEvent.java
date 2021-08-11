package javax.media;

import javax.media.format.FormatChangeEvent;

public class SizeChangeEvent extends FormatChangeEvent {
   protected int height;
   protected float scale;
   protected int width;

   public SizeChangeEvent(Controller var1, int var2, int var3, float var4) {
      super(var1);
      this.width = var2;
      this.height = var3;
      this.scale = var4;
   }

   public int getHeight() {
      return this.height;
   }

   public float getScale() {
      return this.scale;
   }

   public int getWidth() {
      return this.width;
   }
}
