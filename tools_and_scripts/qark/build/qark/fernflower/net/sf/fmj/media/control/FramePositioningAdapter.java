package net.sf.fmj.media.control;

import javax.media.Format;
import javax.media.Player;
import javax.media.Time;
import javax.media.Track;
import javax.media.control.FramePositioningControl;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.Reparentable;
import org.atalk.android.util.java.awt.Component;

public class FramePositioningAdapter implements FramePositioningControl, Reparentable {
   long frameStep = -1L;
   Track master = null;
   Object owner;
   Player player;

   public FramePositioningAdapter(Player var1, Track var2) {
      this.player = var1;
      this.master = var2;
      Format var4 = var2.getFormat();
      if (var4 instanceof VideoFormat) {
         float var3 = ((VideoFormat)var4).getFrameRate();
         if (var3 != -1.0F && var3 != 0.0F) {
            this.frameStep = (long)(1.0E9F / var3);
         }
      }

   }

   public static Track getMasterTrack(Track[] var0) {
      Track var3 = null;

      Track var4;
      for(int var2 = 0; var2 < var0.length; var3 = var4) {
         var4 = var3;
         if (var0[var2] != null) {
            Format var5 = var0[var2].getFormat();
            if (var5 == null) {
               var4 = var3;
            } else if (!(var5 instanceof VideoFormat)) {
               var4 = var3;
            } else {
               var3 = var0[var2];
               float var1 = ((VideoFormat)var5).getFrameRate();
               var4 = var3;
               if (var1 != -1.0F) {
                  var4 = var3;
                  if (var1 != 0.0F) {
                     return var3;
                  }
               }
            }
         }

         ++var2;
      }

      if (var3 != null && var3.mapTimeToFrame(new Time(0L)) != Integer.MAX_VALUE) {
         return var3;
      } else {
         return null;
      }
   }

   public Component getControlComponent() {
      return null;
   }

   public Object getOwner() {
      Object var1 = this.owner;
      return var1 == null ? this : var1;
   }

   public Time mapFrameToTime(int var1) {
      return this.master.mapFrameToTime(var1);
   }

   public int mapTimeToFrame(Time var1) {
      return this.master.mapTimeToFrame(var1);
   }

   public int seek(int var1) {
      Time var2 = this.master.mapFrameToTime(var1);
      if (var2 != null && var2 != FramePositioningControl.TIME_UNKNOWN) {
         this.player.setMediaTime(var2);
         return this.master.mapTimeToFrame(var2);
      } else {
         return Integer.MAX_VALUE;
      }
   }

   public void setOwner(Object var1) {
      this.owner = var1;
   }

   public int skip(int var1) {
      if (this.frameStep != -1L) {
         long var3 = this.player.getMediaNanoseconds();
         long var5 = (long)var1;
         long var7 = this.frameStep;
         this.player.setMediaTime(new Time(var3 + var5 * var7));
         return var1;
      } else {
         int var2 = this.master.mapTimeToFrame(this.player.getMediaTime());
         return var2 != 0 && var2 != Integer.MAX_VALUE ? this.seek(var2 + var1) - var2 : Integer.MAX_VALUE;
      }
   }
}
