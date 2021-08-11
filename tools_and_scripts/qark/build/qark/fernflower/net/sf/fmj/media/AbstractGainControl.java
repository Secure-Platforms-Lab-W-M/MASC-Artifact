package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.List;
import javax.media.GainChangeEvent;
import javax.media.GainChangeListener;
import javax.media.GainControl;
import org.atalk.android.util.java.awt.Component;

public abstract class AbstractGainControl implements GainControl {
   private final List listeners = new ArrayList();
   private boolean mute;
   private float savedLevelDuringMute;

   protected static float dBToLevel(float var0) {
      return (float)Math.pow(10.0D, (double)var0 / 20.0D);
   }

   protected static float levelToDb(float var0) {
      return (float)(Math.log10((double)var0) * 20.0D);
   }

   public void addGainChangeListener(GainChangeListener param1) {
      // $FF: Couldn't be decompiled
   }

   public Component getControlComponent() {
      return null;
   }

   public float getDB() {
      return levelToDb(this.getLevel());
   }

   public boolean getMute() {
      return this.mute;
   }

   protected float getSavedLevelDuringMute() {
      return this.savedLevelDuringMute;
   }

   protected void notifyListenersGainChangeEvent() {
      this.notifyListenersGainChangeEvent(new GainChangeEvent(this, this.getMute(), this.getDB(), this.getLevel()));
   }

   protected void notifyListenersGainChangeEvent(GainChangeEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void removeGainChangeListener(GainChangeListener param1) {
      // $FF: Couldn't be decompiled
   }

   public float setDB(float var1) {
      this.setLevel(dBToLevel(var1));
      var1 = this.getDB();
      this.notifyListenersGainChangeEvent();
      return var1;
   }

   public void setMute(boolean var1) {
      if (var1 != this.mute) {
         if (var1) {
            this.savedLevelDuringMute = this.getLevel();
            this.setLevel(0.0F);
            this.mute = true;
         } else {
            this.setLevel(this.savedLevelDuringMute);
            this.mute = false;
         }

         this.notifyListenersGainChangeEvent();
      }
   }
}
