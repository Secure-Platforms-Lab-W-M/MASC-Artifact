package javax.media;

public interface GainControl extends Control {
   void addGainChangeListener(GainChangeListener var1);

   float getDB();

   float getLevel();

   boolean getMute();

   void removeGainChangeListener(GainChangeListener var1);

   float setDB(float var1);

   float setLevel(float var1);

   void setMute(boolean var1);
}
