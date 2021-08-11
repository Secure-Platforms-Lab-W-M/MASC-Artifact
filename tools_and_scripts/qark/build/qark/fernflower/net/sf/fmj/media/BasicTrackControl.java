package net.sf.fmj.media;

import java.util.Vector;
import javax.media.Codec;
import javax.media.Control;
import javax.media.Format;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Renderer;
import javax.media.Track;
import javax.media.UnsupportedPlugInException;
import javax.media.control.FrameRateControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.control.ProgressControl;
import net.sf.fmj.media.control.StringControl;
import org.atalk.android.util.java.awt.Component;

public class BasicTrackControl implements TrackControl {
   static final String connectErr = "Cannot set a PlugIn before reaching the configured state.";
   static final String realizeErr = "Cannot get CodecControl before reaching the realized state.";
   PlaybackEngine engine;
   OutputConnector firstOC;
   float lastFrameRate = 0.0F;
   OutputConnector lastOC;
   long lastStatsTime = 0L;
   protected Vector modules = new Vector(7);
   protected BasicMuxModule muxModule = null;
   protected boolean prefetchFailed = false;
   protected boolean rendererFailed = false;
   protected BasicRendererModule rendererModule;
   Track track;

   public BasicTrackControl(PlaybackEngine var1, Track var2, OutputConnector var3) {
      this.engine = var1;
      this.track = var2;
      this.firstOC = var3;
      this.lastOC = var3;
      this.setEnabled(var2.isEnabled());
   }

   public boolean buildTrack(int var1, int var2) {
      return false;
   }

   protected FrameRateControl frameRateControl() {
      return null;
   }

   public Object getControl(String var1) {
      Class var5;
      try {
         var5 = BasicPlugIn.getClassForName(var1);
      } catch (ClassNotFoundException var4) {
         return null;
      }

      Object[] var3 = this.getControls();

      for(int var2 = 0; var2 < var3.length; ++var2) {
         if (var5.isInstance(var3[var2])) {
            return var3[var2];
         }
      }

      return null;
   }

   public Component getControlComponent() {
      return null;
   }

   public Object[] getControls() throws NotRealizedError {
      if (this.engine.getState() < 300) {
         throw new NotRealizedError("Cannot get CodecControl before reaching the realized state.");
      } else {
         OutputConnector var3 = this.firstOC;

         int var1;
         Vector var4;
         Module var7;
         for(var4 = new Vector(); var3 != null; var3 = var7.getOutputConnector((String)null)) {
            InputConnector var6 = var3.getInputConnector();
            if (var6 == null) {
               break;
            }

            var7 = var6.getModule();
            Object[] var5 = var7.getControls();
            if (var5 != null) {
               for(var1 = 0; var1 < var5.length; ++var1) {
                  var4.addElement(var5[var1]);
               }
            }
         }

         int var2 = var4.size();
         Control[] var8 = new Control[var2];

         for(var1 = 0; var1 < var2; ++var1) {
            var8[var1] = (Control)var4.elementAt(var1);
         }

         return var8;
      }
   }

   public Format getFormat() {
      return this.track.getFormat();
   }

   public Format getOriginalFormat() {
      return this.track.getFormat();
   }

   public Format[] getSupportedFormats() {
      return new Format[]{this.track.getFormat()};
   }

   public boolean isCustomized() {
      return false;
   }

   public boolean isEnabled() {
      return this.track.isEnabled();
   }

   public boolean isTimeBase() {
      return false;
   }

   public void prError() {
      StringBuilder var1 = new StringBuilder();
      var1.append("  Unable to handle format: ");
      var1.append(this.getOriginalFormat());
      Log.error(var1.toString());
      Log.write("\n");
   }

   public boolean prefetchTrack() {
      for(int var1 = 0; var1 < this.modules.size(); ++var1) {
         BasicModule var2 = (BasicModule)this.modules.elementAt(var1);
         if (!var2.doPrefetch()) {
            this.setEnabled(false);
            this.prefetchFailed = true;
            if (var2 instanceof BasicRendererModule) {
               this.rendererFailed = true;
            }

            return false;
         }
      }

      if (this.prefetchFailed) {
         this.setEnabled(true);
         this.prefetchFailed = false;
         this.rendererFailed = false;
      }

      return true;
   }

   protected ProgressControl progressControl() {
      return null;
   }

   public void setCodecChain(Codec[] var1) throws NotConfiguredError, UnsupportedPlugInException {
      if (this.engine.getState() <= 180) {
         if (var1.length < 1) {
            throw new UnsupportedPlugInException("No codec specified in the array.");
         }
      } else {
         throw new NotConfiguredError("Cannot set a PlugIn before reaching the configured state.");
      }
   }

   public void setEnabled(boolean var1) {
      this.track.setEnabled(var1);
   }

   public Format setFormat(Format var1) {
      return var1 != null && var1.matches(this.getFormat()) ? this.getFormat() : null;
   }

   public void setRenderer(Renderer var1) throws NotConfiguredError {
      if (this.engine.getState() > 180) {
         throw new NotConfiguredError("Cannot set a PlugIn before reaching the configured state.");
      }
   }

   public void startTrack() {
      for(int var1 = 0; var1 < this.modules.size(); ++var1) {
         ((BasicModule)this.modules.elementAt(var1)).doStart();
      }

   }

   public void stopTrack() {
      for(int var1 = 0; var1 < this.modules.size(); ++var1) {
         ((BasicModule)this.modules.elementAt(var1)).doStop();
      }

   }

   public void updateFormat() {
      if (this.track.isEnabled()) {
         ProgressControl var2 = this.progressControl();
         if (var2 != null) {
            if (this.track.getFormat() instanceof AudioFormat) {
               AudioFormat var3 = (AudioFormat)this.track.getFormat();
               var2.getAudioCodec().setValue(var3.getEncoding());
               StringControl var4 = var2.getAudioProperties();
               String var1;
               if (var3.getChannels() == 1) {
                  var1 = "mono";
               } else {
                  var1 = "stereo";
               }

               StringBuilder var5 = new StringBuilder();
               var5.append(var3.getSampleRate() / 1000.0D);
               var5.append(" KHz, ");
               var5.append(var3.getSampleSizeInBits());
               var5.append("-bit, ");
               var5.append(var1);
               var4.setValue(var5.toString());
            }

            if (this.track.getFormat() instanceof VideoFormat) {
               VideoFormat var6 = (VideoFormat)this.track.getFormat();
               var2.getVideoCodec().setValue(var6.getEncoding());
               StringControl var7 = var2.getVideoProperties();
               if (var6.getSize() != null) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append(var6.getSize().width);
                  var8.append(" X ");
                  var8.append(var6.getSize().height);
                  var7.setValue(var8.toString());
               }
            }

         }
      }
   }

   public void updateRates(long var1) {
      FrameRateControl var5 = this.frameRateControl();
      if (var5 != null) {
         if (this.track.isEnabled() && this.track.getFormat() instanceof VideoFormat) {
            if (this.rendererModule != null || this.muxModule != null) {
               float var3;
               if (var1 == this.lastStatsTime) {
                  var3 = this.lastFrameRate;
               } else {
                  BasicRendererModule var6 = this.rendererModule;
                  int var4;
                  if (var6 != null) {
                     var4 = var6.getFramesPlayed();
                  } else {
                     var4 = this.muxModule.getFramesPlayed();
                  }

                  var3 = (float)var4 / (float)(var1 - this.lastStatsTime) * 1000.0F;
               }

               var5.setFrameRate((float)((int)((this.lastFrameRate + var3) / 2.0F * 10.0F)) / 10.0F);
               this.lastFrameRate = var3;
               this.lastStatsTime = var1;
               BasicRendererModule var7 = this.rendererModule;
               if (var7 != null) {
                  var7.resetFramesPlayed();
               } else {
                  this.muxModule.resetFramesPlayed();
               }
            }
         }
      }
   }
}
