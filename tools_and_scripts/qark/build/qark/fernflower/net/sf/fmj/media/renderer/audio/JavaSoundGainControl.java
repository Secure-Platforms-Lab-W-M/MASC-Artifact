package net.sf.fmj.media.renderer.audio;

import net.sf.fmj.media.AbstractGainControl;
import org.atalk.android.util.javax.sound.sampled.BooleanControl;
import org.atalk.android.util.javax.sound.sampled.FloatControl;

class JavaSoundGainControl extends AbstractGainControl {
   private final boolean gainUnitsDb;
   private final FloatControl masterGainControl;
   private final float max;
   private final float min;
   private final BooleanControl muteControl;
   private final float range;

   public JavaSoundGainControl(FloatControl var1, BooleanControl var2) {
      this.masterGainControl = var1;
      this.muteControl = var2;
      if (var1 != null) {
         this.min = var1.getMinimum();
         this.max = var1.getMaximum();
         this.gainUnitsDb = var1.getUnits().equals("dB");
      } else {
         this.max = 0.0F;
         this.min = 0.0F;
         this.gainUnitsDb = false;
      }

      this.range = this.max - this.min;
   }

   public float getDB() {
      FloatControl var1 = this.masterGainControl;
      if (var1 == null) {
         return 0.0F;
      } else {
         return this.gainUnitsDb ? var1.getValue() : levelToDb(this.getLevel());
      }
   }

   public float getLevel() {
      FloatControl var1 = this.masterGainControl;
      if (var1 == null) {
         return 0.0F;
      } else {
         return this.gainUnitsDb ? dBToLevel(var1.getValue()) : (var1.getValue() - this.min) / this.range;
      }
   }

   public boolean getMute() {
      BooleanControl var1 = this.muteControl;
      return var1 == null ? false : var1.getValue();
   }

   public float setDB(float var1) {
      FloatControl var2 = this.masterGainControl;
      if (var2 == null) {
         return 0.0F;
      } else {
         if (this.gainUnitsDb) {
            var2.setValue(var1);
         } else {
            this.setLevel(dBToLevel(var1));
         }

         var1 = this.getDB();
         this.notifyListenersGainChangeEvent();
         return var1;
      }
   }

   public float setLevel(float var1) {
      FloatControl var2 = this.masterGainControl;
      if (var2 == null) {
         return 0.0F;
      } else {
         if (this.gainUnitsDb) {
            var2.setValue(levelToDb(var1));
         } else {
            var2.setValue(this.min + this.range * var1);
         }

         var1 = this.getLevel();
         this.notifyListenersGainChangeEvent();
         return var1;
      }
   }

   public void setMute(boolean var1) {
      BooleanControl var2 = this.muteControl;
      if (var2 != null) {
         var2.setValue(var1);
         this.notifyListenersGainChangeEvent();
      }
   }
}
