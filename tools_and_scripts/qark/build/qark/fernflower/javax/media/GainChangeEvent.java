package javax.media;

public class GainChangeEvent extends MediaEvent {
   GainControl eventSrc;
   float newDB;
   float newLevel;
   boolean newMute;

   public GainChangeEvent(GainControl var1, boolean var2, float var3, float var4) {
      super(var1);
      this.eventSrc = var1;
      this.newMute = var2;
      this.newDB = var3;
      this.newLevel = var4;
   }

   public float getDB() {
      return this.newDB;
   }

   public float getLevel() {
      return this.newLevel;
   }

   public boolean getMute() {
      return this.newMute;
   }

   public Object getSource() {
      return this.eventSrc;
   }

   public GainControl getSourceGainControl() {
      return this.eventSrc;
   }
}
