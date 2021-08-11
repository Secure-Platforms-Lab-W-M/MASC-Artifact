package net.sf.fmj.media;

import com.sun.media.controls.BitRateAdapter;
import com.sun.media.controls.FrameRateAdapter;
import java.io.IOException;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.Clock;
import javax.media.ClockStoppedException;
import javax.media.Codec;
import javax.media.Control;
import javax.media.ControllerClosedEvent;
import javax.media.Demultiplexer;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.InternalErrorEvent;
import javax.media.Manager;
import javax.media.MediaTimeSetEvent;
import javax.media.NotRealizedError;
import javax.media.Owned;
import javax.media.PlugIn;
import javax.media.Renderer;
import javax.media.RestartingEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopAtTimeEvent;
import javax.media.StopByRequestEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.Track;
import javax.media.control.BitRateControl;
import javax.media.control.BufferControl;
import javax.media.control.FramePositioningControl;
import javax.media.control.FrameRateControl;
import javax.media.format.AudioFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.renderer.VideoRenderer;
import javax.media.renderer.VisualContainer;
import net.sf.fmj.filtergraph.GraphNode;
import net.sf.fmj.filtergraph.SimpleGraphBuilder;
import net.sf.fmj.media.control.FramePositioningAdapter;
import net.sf.fmj.media.control.ProgressControl;
import net.sf.fmj.media.control.ProgressControlAdapter;
import net.sf.fmj.media.control.StringControlAdapter;
import net.sf.fmj.media.protocol.Streamable;
import net.sf.fmj.media.renderer.audio.AudioRenderer;
import net.sf.fmj.media.util.RTPInfo;
import net.sf.fmj.media.util.Resource;
import org.atalk.android.util.java.awt.Color;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Container;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.FlowLayout;
import org.atalk.android.util.java.awt.Panel;

public class PlaybackEngine extends BasicController implements ModuleListener {
   static String NOT_CONFIGURED_ERROR = "cannot be called before configured";
   static String NOT_REALIZED_ERROR = "cannot be called before realized";
   static String STARTED_ERROR = "cannot be called after started";
   public static boolean TRACE_ON = false;
   static boolean USE_BACKUP = false;
   static boolean USE_MASTER = true;
   protected static boolean needSavingDB = false;
   protected BitRateControl bitRateControl;
   String configError;
   String configInt2Error;
   String configIntError;
   protected Container container = null;
   private boolean dataPathBlocked = false;
   private boolean deallocated = false;
   protected DataSource dsource;
   protected Vector filters;
   protected FramePositioningControl framePositioningControl = null;
   protected FrameRateControl frameRateControl;
   protected String genericProcessorError;
   private boolean internalErrorOccurred = false;
   long lastBitRate;
   long lastStatsTime;
   private long latency = 0L;
   long markedDataStartTime;
   protected BasicSinkModule masterSink = null;
   protected Vector modules;
   String parseError;
   protected Demultiplexer parser;
   protected BasicPlayer player;
   public boolean prefetchEnabled = true;
   String prefetchError;
   boolean prefetchLogged;
   private long prefetchTime;
   protected boolean prefetched = false;
   protected ProgressControl progressControl;
   private float rate = 1.0F;
   protected String realizeError;
   private long realizeTime;
   boolean reportOnce;
   RTPInfo rtpInfo;
   protected Vector sinks;
   protected PlaybackEngine.SlaveClock slaveClock;
   protected BasicSourceModule source;
   protected boolean started = false;
   boolean testedRTP;
   protected String timeBaseError;
   private Time timeBeforeAbortPrefetch = null;
   protected BasicTrackControl[] trackControls = new BasicTrackControl[0];
   protected Track[] tracks;
   private boolean useMoreRenderBuffer = false;
   protected Vector waitEnded;
   protected Vector waitPrefetched;
   protected Vector waitResetted;
   protected Vector waitStopped;

   public PlaybackEngine(BasicPlayer var1) {
      StringBuilder var4 = new StringBuilder();
      var4.append("Failed to configure: ");
      var4.append(this);
      this.configError = var4.toString();
      this.configIntError = "  The configure process is being interrupted.\n";
      this.configInt2Error = "interrupted while the Processor is being configured.";
      this.parseError = "failed to parse the input media.";
      var4 = new StringBuilder();
      var4.append("Failed to realize: ");
      var4.append(this);
      this.realizeError = var4.toString();
      this.timeBaseError = "  Cannot manage the different time bases.\n";
      this.genericProcessorError = "cannot handle the customized options set on the Processor.\nCheck the logs for full details.";
      var4 = new StringBuilder();
      var4.append("Failed to prefetch: ");
      var4.append(this);
      this.prefetchError = var4.toString();
      this.rtpInfo = null;
      this.testedRTP = false;
      this.prefetchLogged = false;
      this.markedDataStartTime = 0L;
      this.reportOnce = false;
      this.lastBitRate = 0L;
      this.lastStatsTime = 0L;
      long var2 = System.currentTimeMillis();
      this.player = var1;
      this.createProgressControl();
      PlaybackEngine.SlaveClock var5 = new PlaybackEngine.SlaveClock();
      this.slaveClock = var5;
      this.setClock(var5);
      this.stopThreadEnabled = false;
      profile("instantiation", var2);
   }

   static boolean isRawVideo(Format var0) {
      return var0 instanceof RGBFormat || var0 instanceof YUVFormat;
   }

   static void profile(String var0, long var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append("Profile: ");
      var3.append(var0);
      var3.append(": ");
      var3.append(System.currentTimeMillis() - var1);
      var3.append(" ms\n");
      Log.profile(var3.toString());
   }

   private void resetEndedList() {
      Vector var3 = this.waitEnded;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label226: {
         int var2;
         try {
            this.waitEnded.removeAllElements();
            var2 = this.sinks.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label226;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               try {
                  this.waitEnded.notifyAll();
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }

            try {
               BasicSinkModule var4 = (BasicSinkModule)this.sinks.elementAt(var1);
               if (!var4.prefetchFailed()) {
                  this.waitEnded.addElement(var4);
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private void resetPrefetchedList() {
      Vector var3 = this.waitPrefetched;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label226: {
         int var2;
         try {
            this.waitPrefetched.removeAllElements();
            var2 = this.sinks.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label226;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               try {
                  this.waitPrefetched.notifyAll();
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }

            try {
               BasicSinkModule var4 = (BasicSinkModule)this.sinks.elementAt(var1);
               if (!var4.prefetchFailed()) {
                  this.waitPrefetched.addElement(var4);
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private void resetResettedList() {
      Vector var3 = this.waitResetted;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label226: {
         int var2;
         try {
            this.waitResetted.removeAllElements();
            var2 = this.sinks.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label226;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               try {
                  this.waitResetted.notifyAll();
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }

            try {
               BasicSinkModule var4 = (BasicSinkModule)this.sinks.elementAt(var1);
               if (!var4.prefetchFailed()) {
                  this.waitResetted.addElement(var4);
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private void resetStoppedList() {
      Vector var3 = this.waitStopped;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label226: {
         int var2;
         try {
            this.waitStopped.removeAllElements();
            var2 = this.sinks.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label226;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               try {
                  this.waitStopped.notifyAll();
                  return;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }

            try {
               BasicSinkModule var4 = (BasicSinkModule)this.sinks.elementAt(var1);
               if (!var4.prefetchFailed()) {
                  this.waitStopped.addElement(var4);
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public static void setMemoryTrace(boolean var0) {
      TRACE_ON = var0;
   }

   protected void abortConfigure() {
      synchronized(this){}

      try {
         if (this.source != null) {
            this.source.abortRealize();
         }
      } finally {
         ;
      }

   }

   protected void abortPrefetch() {
      synchronized(this){}

      Throwable var10000;
      label147: {
         boolean var10001;
         int var2;
         try {
            this.timeBeforeAbortPrefetch = this.getMediaTime();
            this.doReset();
            var2 = this.modules.size();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label147;
         }

         for(int var1 = 0; var1 < var2; ++var1) {
            try {
               ((StateTransistor)this.modules.elementAt(var1)).abortPrefetch();
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label147;
            }
         }

         label130:
         try {
            this.deallocated = true;
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label130;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   protected void abortRealize() {
      synchronized(this){}

      Throwable var10000;
      label92: {
         boolean var10001;
         int var2;
         try {
            var2 = this.modules.size();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label92;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               return;
            }

            try {
               ((StateTransistor)this.modules.elementAt(var1)).abortRealize();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public boolean audioEnabled() {
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return false;
         }

         if (var2[var1].isEnabled() && this.trackControls[var1].getOriginalFormat() instanceof AudioFormat) {
            return true;
         }

         ++var1;
      }
   }

   public void bufferPrefetched(Module var1) {
      if (this.prefetchEnabled) {
         if (var1 instanceof BasicSinkModule) {
            Vector var2 = this.waitPrefetched;
            synchronized(var2){}

            Throwable var10000;
            boolean var10001;
            label455: {
               try {
                  if (this.waitPrefetched.contains(var1)) {
                     this.waitPrefetched.removeElement(var1);
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label455;
               }

               label456: {
                  try {
                     if (!this.waitPrefetched.isEmpty()) {
                        break label456;
                     }

                     this.waitPrefetched.notifyAll();
                     if (!this.prefetchLogged) {
                        profile("prefetch", this.prefetchTime);
                        this.prefetchLogged = true;
                     }
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label455;
                  }

                  try {
                     if (this.getState() != 600 && this.getTargetState() != 600) {
                        this.source.pause();
                     }
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label455;
                  }

                  try {
                     this.prefetched = true;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label455;
                  }
               }

               label431:
               try {
                  return;
               } catch (Throwable var40) {
                  var10000 = var40;
                  var10001 = false;
                  break label431;
               }
            }

            while(true) {
               Throwable var45 = var10000;

               try {
                  throw var45;
               } catch (Throwable var39) {
                  var10000 = var39;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   protected GraphNode buildTrackFromGraph(BasicTrackControl var1, GraphNode var2) {
      BasicModule var9 = null;
      BasicModule var7 = null;
      OutputConnector var8 = null;
      boolean var3 = true;
      Vector var10 = new Vector(5);
      if (var2.plugin == null) {
         return null;
      } else {
         int var5 = 0 + 1;
         Log.setIndent(0);
         GraphNode var6 = var2;

         boolean var4;
         BasicModule var11;
         for(var11 = var9; var6 != null && var6.plugin != null; var3 = var4) {
            var9 = this.createModule(var6, var10);
            var11 = var9;
            if (var9 == null) {
               Log.error("Internal error: buildTrackFromGraph");
               var6.failed = true;
               return var6;
            }

            var4 = var3;
            if (var3) {
               if (var9 instanceof BasicRendererModule) {
                  var1.rendererModule = (BasicRendererModule)var9;
                  if (this.useMoreRenderBuffer && var1.rendererModule.getRenderer() instanceof AudioRenderer) {
                     this.setRenderBufferSize(var1.rendererModule.getRenderer());
                  }
               } else if (var9 instanceof BasicFilterModule) {
                  var1.lastOC = var9.getOutputConnector((String)null);
                  var1.lastOC.setFormat(var6.output);
               }

               var4 = false;
            }

            InputConnector var16 = var9.getInputConnector((String)null);
            var16.setFormat(var6.input);
            if (var7 != null) {
               var8 = var11.getOutputConnector((String)null);
               var16 = var7.getInputConnector((String)null);
               var8.setFormat(var16.getFormat());
            }

            var11.setController(this);
            if (!var11.doRealize()) {
               Log.setIndent(var5);
               var6.failed = true;
               return var6;
            }

            if (var8 != null && var16 != null) {
               this.connectModules(var8, var16, var7);
            }

            var7 = var11;
            var6 = var6.prev;
         }

         BasicModule var12 = var11;

         InputConnector var14;
         do {
            var12.setModuleListener(this);
            this.modules.addElement(var12);
            var1.modules.addElement(var12);
            if (var12 instanceof BasicFilterModule) {
               this.filters.addElement(var12);
            } else if (var12 instanceof BasicSinkModule) {
               this.sinks.addElement(var12);
            }

            OutputConnector var13 = var12.getOutputConnector((String)null);
            if (var13 == null) {
               break;
            }

            var14 = var13.getInputConnector();
            if (var14 == null) {
               break;
            }

            var7 = (BasicModule)var14.getModule();
            var12 = var7;
         } while(var7 != null);

         var1.firstOC.setFormat(var1.getOriginalFormat());
         var14 = var11.getInputConnector((String)null);
         Format var15 = var14.getFormat();
         if (var15 == null || !var15.equals(var1.getOriginalFormat())) {
            var14.setFormat(var1.getOriginalFormat());
         }

         this.connectModules(var1.firstOC, var14, var11);
         Log.setIndent(var5);
         return null;
      }
   }

   protected void connectModules(OutputConnector var1, InputConnector var2, BasicModule var3) {
      if (var3 instanceof BasicRendererModule) {
         var1.setProtocol(var2.getProtocol());
      } else {
         var2.setProtocol(var1.getProtocol());
      }

      var1.connectTo(var2, var2.getFormat());
   }

   protected BasicModule createModule(GraphNode var1, Vector var2) {
      Object var4 = null;
      if (var1.plugin == null) {
         return null;
      } else {
         PlugIn var3;
         PlugIn var5;
         if (var2.contains(var1.plugin)) {
            label37: {
               if (var1.cname != null) {
                  var3 = SimpleGraphBuilder.createPlugIn(var1.cname, -1);
                  var5 = var3;
                  if (var3 != null) {
                     break label37;
                  }
               }

               StringBuilder var6 = new StringBuilder();
               var6.append("Failed to instantiate ");
               var6.append(var1.cname);
               Log.write(var6.toString());
               return null;
            }
         } else {
            var3 = var1.plugin;
            var2.addElement(var3);
            var5 = var3;
         }

         if ((var1.type == -1 || var1.type == 4) && var5 instanceof Renderer) {
            return new BasicRendererModule((Renderer)var5);
         } else {
            BasicFilterModule var7;
            if (var1.type != -1) {
               var7 = (BasicFilterModule)var4;
               if (var1.type != 2) {
                  return var7;
               }
            }

            var7 = (BasicFilterModule)var4;
            if (var5 instanceof Codec) {
               var7 = new BasicFilterModule((Codec)var5);
            }

            return var7;
         }
      }
   }

   public void createProgressControl() {
      StringControlAdapter var1 = new StringControlAdapter();
      var1.setValue(" N/A");
      StringControlAdapter var2 = new StringControlAdapter();
      var2.setValue(" N/A");
      StringControlAdapter var3 = new StringControlAdapter();
      var3.setValue(" N/A");
      StringControlAdapter var4 = new StringControlAdapter();
      var4.setValue(" N/A");
      StringControlAdapter var5 = new StringControlAdapter();
      var5.setValue(" N/A");
      StringControlAdapter var6 = new StringControlAdapter();
      var6.setValue(" N/A");
      this.progressControl = new ProgressControlAdapter(var1, var2, var3, var4, var6, var5);
   }

   protected Component createVisualContainer(Vector var1) {
      Boolean var3 = (Boolean)Manager.getHint(3);
      if (this.container == null) {
         if (var3 != null && var3) {
            this.container = new PlaybackEngine.LightPanel(var1);
         } else {
            this.container = new PlaybackEngine.HeavyPanel(var1);
         }

         this.container.setLayout(new FlowLayout());
         this.container.setBackground(Color.black);

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Component var4 = (Component)var1.elementAt(var2);
            this.container.add(var4);
            var4.setSize(var4.getPreferredSize());
         }
      }

      return this.container;
   }

   public void dataBlocked(Module var1, boolean var2) {
      this.dataPathBlocked = var2;
      if (var2) {
         this.resetPrefetchedList();
         this.resetResettedList();
      }

      if (this.getTargetState() == 600) {
         if (var2) {
            this.localStop();
            this.setTargetState(600);
            this.sendEvent(new RestartingEvent(this, 600, 400, 600, this.getMediaTime()));
         } else {
            this.sendEvent(new StartEvent(this, 500, 600, 600, this.getMediaTime(), this.getTimeBase().getTime()));
         }
      }
   }

   protected void doClose() {
      synchronized(this){}

      Throwable var10000;
      label516: {
         boolean var10001;
         try {
            if (this.modules == null) {
               if (this.source != null) {
                  this.source.doClose();
               }

               return;
            }
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label516;
         }

         try {
            if (this.getState() == 600) {
               this.localStop();
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label516;
         }

         try {
            if (this.getState() == 500) {
               this.doReset();
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label516;
         }

         int var2;
         try {
            var2 = this.modules.size();
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label516;
         }

         for(int var1 = 0; var1 < var2; ++var1) {
            try {
               ((StateTransistor)this.modules.elementAt(var1)).doClose();
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label516;
            }
         }

         try {
            if (needSavingDB) {
               Resource.saveDB();
               needSavingDB = false;
            }
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label516;
         }

         return;
      }

      Throwable var3 = var10000;
      throw var3;
   }

   protected boolean doConfigure() {
      if (!this.doConfigure1()) {
         return false;
      } else {
         String[] var2 = this.source.getOutputConnectorNames();
         this.trackControls = new BasicTrackControl[this.tracks.length];
         int var1 = 0;

         while(true) {
            Track[] var3 = this.tracks;
            if (var1 >= var3.length) {
               return this.doConfigure2();
            }

            this.trackControls[var1] = new PlaybackEngine.PlayerTControl(this, var3[var1], this.source.getOutputConnector(var2[var1]));
            ++var1;
         }
      }
   }

   protected boolean doConfigure1() {
      long var1 = System.currentTimeMillis();
      this.modules = new Vector();
      this.filters = new Vector();
      this.sinks = new Vector();
      this.waitPrefetched = new Vector();
      this.waitStopped = new Vector();
      this.waitEnded = new Vector();
      this.waitResetted = new Vector();
      this.source.setModuleListener(this);
      this.source.setController(this);
      this.modules.addElement(this.source);
      if (!this.source.doRealize()) {
         Log.error(this.configError);
         if (this.source.errMsg != null) {
            StringBuilder var6 = new StringBuilder();
            var6.append("  ");
            var6.append(this.source.errMsg);
            var6.append("\n");
            Log.error(var6.toString());
         }

         this.player.processError = this.parseError;
         return false;
      } else if (this.isInterrupted()) {
         Log.error(this.configError);
         Log.error(this.configIntError);
         this.player.processError = this.configInt2Error;
         return false;
      } else {
         Demultiplexer var3 = this.source.getDemultiplexer();
         this.parser = var3;
         if (var3 == null) {
            Log.error(this.configError);
            Log.error("  Cannot obtain demultiplexer for the source.\n");
            this.player.processError = this.parseError;
            return false;
         } else {
            try {
               this.tracks = var3.getTracks();
            } catch (Exception var5) {
               Log.error(this.configError);
               StringBuilder var4 = new StringBuilder();
               var4.append("  Cannot obtain tracks from the demultiplexer: ");
               var4.append(var5);
               var4.append("\n");
               Log.error(var4.toString());
               this.player.processError = this.parseError;
               return false;
            }

            if (this.isInterrupted()) {
               Log.error(this.configError);
               Log.error(this.configIntError);
               this.player.processError = this.configInt2Error;
               return false;
            } else {
               profile("parsing", var1);
               return true;
            }
         }
      }
   }

   protected boolean doConfigure2() {
      if (this.parser.isPositionable() && this.parser.isRandomAccess()) {
         Track var1 = FramePositioningAdapter.getMasterTrack(this.tracks);
         if (var1 != null) {
            this.framePositioningControl = new FramePositioningAdapter(this.player, var1);
         }
      }

      return true;
   }

   protected void doDeallocate() {
   }

   protected void doFailedPrefetch() {
      synchronized(this){}

      Throwable var10000;
      label147: {
         boolean var10001;
         int var2;
         try {
            var2 = this.modules.size();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label147;
         }

         for(int var1 = 0; var1 < var2; ++var1) {
            try {
               ((StateTransistor)this.modules.elementAt(var1)).doFailedPrefetch();
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label147;
            }
         }

         label130:
         try {
            super.doFailedPrefetch();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label130;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   protected void doFailedRealize() {
      synchronized(this){}

      Throwable var10000;
      label147: {
         boolean var10001;
         int var2;
         try {
            var2 = this.modules.size();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label147;
         }

         for(int var1 = 0; var1 < var2; ++var1) {
            try {
               ((StateTransistor)this.modules.elementAt(var1)).doFailedRealize();
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label147;
            }
         }

         label130:
         try {
            super.doFailedRealize();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label130;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   protected boolean doPrefetch() {
      synchronized(this){}

      boolean var1;
      label101: {
         Throwable var10000;
         label106: {
            boolean var10001;
            boolean var2;
            try {
               var2 = this.prefetched;
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label106;
            }

            var1 = true;
            if (var2) {
               return true;
            }

            try {
               if (!this.doPrefetch1()) {
                  break label101;
               }

               var2 = this.doPrefetch2();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label106;
            }

            if (var2) {
               return var1;
            }
            break label101;
         }

         Throwable var3 = var10000;
         throw var3;
      }

      var1 = false;
      return var1;
   }

   protected boolean doPrefetch1() {
      Time var5 = this.timeBeforeAbortPrefetch;
      if (var5 != null) {
         this.doSetMediaTime(var5);
         this.timeBeforeAbortPrefetch = null;
      }

      this.prefetchTime = System.currentTimeMillis();
      this.resetPrefetchedList();
      if (!this.source.doPrefetch()) {
         Log.error(this.prefetchError);
         if (this.dsource != null) {
            StringBuilder var7 = new StringBuilder();
            var7.append("  Cannot prefetch the source: ");
            var7.append(this.dsource.getLocator());
            var7.append("\n");
            Log.error(var7.toString());
         }

         return false;
      } else {
         boolean var3 = false;
         int var1 = 0;

         while(true) {
            BasicTrackControl[] var6 = this.trackControls;
            if (var1 >= var6.length) {
               if (!var3) {
                  Log.error(this.prefetchError);
                  return false;
               }

               this.player.processError = null;
               return true;
            }

            boolean var4 = var6[var1].prefetchFailed;
            boolean var2;
            if (var4 && this.getState() > 400) {
               var2 = var3;
            } else if (this.trackControls[var1].prefetchTrack()) {
               var3 = true;
               var2 = var3;
               if (var4) {
                  if (!this.manageTimeBases()) {
                     Log.error(this.prefetchError);
                     Log.error(this.timeBaseError);
                     return false;
                  }

                  this.doSetMediaTime(this.getMediaTime());
                  var2 = var3;
               }
            } else {
               this.trackControls[var1].prError();
               if (this.trackControls[var1].isTimeBase() && !this.manageTimeBases()) {
                  Log.error(this.prefetchError);
                  Log.error(this.timeBaseError);
                  this.player.processError = this.timeBaseError;
                  return false;
               }

               var2 = var3;
               if (this.trackControls[var1].getFormat() instanceof AudioFormat) {
                  var2 = var3;
                  if (this.trackControls[var1].rendererFailed) {
                     this.player.processError = "cannot open the audio device.";
                     var2 = var3;
                  }
               }
            }

            ++var1;
            var3 = var2;
         }
      }
   }

   protected boolean doPrefetch2() {
      // $FF: Couldn't be decompiled
   }

   protected boolean doRealize() {
      synchronized(this){}
      boolean var4 = false;

      boolean var1;
      label45: {
         try {
            var4 = true;
            if (!this.doRealize1()) {
               var4 = false;
               break label45;
            }

            var1 = this.doRealize2();
            var4 = false;
         } finally {
            if (var4) {
               ;
            }
         }

         if (var1) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   protected boolean doRealize1() {
      StringBuilder var5 = new StringBuilder();
      var5.append("Building flow graph for: ");
      var5.append(this.dsource.getLocator());
      var5.append("\n");
      Log.comment(var5.toString());
      this.realizeTime = System.currentTimeMillis();
      boolean var1 = false;
      int var3 = 0;
      int var4 = this.getNumTracks();
      int var2 = 0;

      while(true) {
         BasicTrackControl[] var7 = this.trackControls;
         if (var2 >= var7.length) {
            if (!var1) {
               Log.error(this.realizeError);
               BasicPlayer var8 = this.player;
               StringBuilder var6 = new StringBuilder();
               var6.append("input media not supported: ");
               var6.append(this.getCodecList());
               var8.processError = var6.toString();
               return false;
            }

            return true;
         }

         if (var7[var2].isEnabled()) {
            Log.setIndent(0);
            var5 = new StringBuilder();
            var5.append("Building Track: ");
            var5.append(var2);
            Log.comment(var5.toString());
            if (this.trackControls[var2].buildTrack(var3, var4)) {
               var1 = true;
               this.trackControls[var2].setEnabled(true);
            } else {
               if (this.trackControls[var2].isCustomized()) {
                  Log.error(this.realizeError);
                  this.trackControls[var2].prError();
                  this.player.processError = this.genericProcessorError;
                  return false;
               }

               this.trackControls[var2].setEnabled(false);
               var5 = new StringBuilder();
               var5.append("Failed to handle track ");
               var5.append(var2);
               Log.warning(var5.toString());
               this.trackControls[var2].prError();
            }

            if (this.isInterrupted()) {
               Log.error(this.realizeError);
               Log.error("  The graph building process is being interrupted.\n");
               this.player.processError = "interrupted while the player is being constructed.";
               return false;
            }

            ++var3;
            Log.write("\n");
         }

         ++var2;
      }
   }

   protected boolean doRealize2() {
      if (!this.manageTimeBases()) {
         Log.error(this.realizeError);
         Log.error(this.timeBaseError);
         this.player.processError = this.timeBaseError;
         return false;
      } else {
         Log.comment("Here's the completed flow graph:");
         this.traceGraph(this.source);
         Log.write("\n");
         profile("graph building", this.realizeTime);
         this.realizeTime = System.currentTimeMillis();
         this.updateFormats();
         profile("realize, post graph building", this.realizeTime);
         return true;
      }
   }

   protected void doReset() {
      // $FF: Couldn't be decompiled
   }

   protected void doSetMediaTime(Time var1) {
      this.slaveClock.setMediaTime(var1);
      Time var5 = this.source.setPosition(var1, 0);
      Time var4 = var5;
      if (var5 == null) {
         var4 = var1;
      }

      int var3 = this.sinks.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         BasicSinkModule var6 = (BasicSinkModule)this.sinks.elementAt(var2);
         var6.doSetMediaTime(var1);
         var6.setPreroll(var1.getNanoseconds(), var4.getNanoseconds());
      }

   }

   public float doSetRate(float var1) {
      synchronized(this){}
      float var2 = var1;
      if (var1 <= 0.0F) {
         var2 = 1.0F;
      }

      Throwable var10000;
      label468: {
         boolean var10001;
         try {
            var1 = this.rate;
         } catch (Throwable var47) {
            var10000 = var47;
            var10001 = false;
            break label468;
         }

         if (var2 == var1) {
            return var2;
         }

         label454: {
            try {
               if (this.masterSink == null) {
                  var1 = this.getClock().setRate(var2);
                  break label454;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label468;
            }

            try {
               var1 = this.masterSink.doSetRate(var2);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label468;
            }
         }

         int var4;
         try {
            var4 = this.modules.size();
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label468;
         }

         int var3 = 0;

         while(true) {
            if (var3 >= var4) {
               try {
                  this.rate = var1;
                  return var1;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break;
               }
            }

            try {
               BasicModule var5 = (BasicModule)this.modules.elementAt(var3);
               if (var5 != this.masterSink) {
                  var5.doSetRate(var1);
               }
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break;
            }

            ++var3;
         }
      }

      Throwable var48 = var10000;
      throw var48;
   }

   protected void doStart() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.started;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1) {
            return;
         }

         try {
            this.doStart1();
            this.doStart2();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void doStart1() {
      if (this.dsource instanceof CaptureDevice && !this.isRTP()) {
         this.reset();
      }

      this.resetPrefetchedList();
      this.resetStoppedList();
      this.resetEndedList();
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return;
         }

         if (var2[var1].isEnabled()) {
            this.trackControls[var1].startTrack();
         }

         ++var1;
      }
   }

   protected void doStart2() {
      this.source.doStart();
      this.started = true;
      this.prefetched = true;
   }

   protected void doStop() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.started;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (!var1) {
            return;
         }

         try {
            this.doStop1();
            this.doStop2();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void doStop1() {
      this.resetPrefetchedList();
      this.source.doStop();
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return;
         }

         if (var2[var1].isEnabled()) {
            this.trackControls[var1].stopTrack();
         }

         ++var1;
      }
   }

   protected void doStop2() {
      if (!this.prefetchEnabled) {
         this.source.pause();
      }

      this.started = false;
   }

   protected BasicSinkModule findMasterSink() {
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return null;
         }

         if (var2[var1].isEnabled() && this.trackControls[var1].rendererModule != null && this.trackControls[var1].rendererModule.getClock() != null) {
            return this.trackControls[var1].rendererModule;
         }

         ++var1;
      }
   }

   public void formatChanged(Module var1, Format var2, Format var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var1);
      var4.append(": input format changed: ");
      var4.append(var3);
      Log.comment(var4.toString());
      if (var1 instanceof BasicRendererModule && var2 instanceof VideoFormat && var3 instanceof VideoFormat) {
         Dimension var5 = ((VideoFormat)var2).getSize();
         Dimension var6 = ((VideoFormat)var3).getSize();
         if (var6 != null && (var5 == null || !var5.equals(var6))) {
            this.sendEvent(new SizeChangeEvent(this, var6.width, var6.height, 1.0F));
         }
      }

   }

   public void formatChangedFailure(Module var1, Format var2, Format var3) {
      if (!this.internalErrorOccurred) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Internal module ");
         var4.append(var1);
         var4.append(": failed to handle a data format change!");
         this.sendEvent(new InternalErrorEvent(this, var4.toString()));
         this.internalErrorOccurred = true;
         this.close();
      }

   }

   public void framesBehind(Module var1, float var2, InputConnector var3) {
      while(var3 != null) {
         OutputConnector var4 = var3.getOutputConnector();
         if (var4 == null) {
            return;
         }

         var1 = var4.getModule();
         if (var1 == null) {
            return;
         }

         if (!(var1 instanceof BasicFilterModule)) {
            return;
         }

         ((BasicFilterModule)var1).setFramesBehind(var2);
         var3 = var1.getInputConnector((String)null);
      }

   }

   protected long getBitRate() {
      return this.source.getBitsRead();
   }

   public String getCNAME() {
      if (this.rtpInfo == null) {
         RTPInfo var1 = (RTPInfo)this.dsource.getControl(RTPInfo.class.getName());
         this.rtpInfo = var1;
         if (var1 == null) {
            return null;
         }
      }

      return this.rtpInfo.getCNAME();
   }

   String getCodecList() {
      String var2 = "";
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var3 = this.trackControls;
         if (var1 >= var3.length) {
            return var2;
         }

         Format var4 = var3[var1].getOriginalFormat();
         String var6 = var2;
         if (var4 != null) {
            if (var4.getEncoding() == null) {
               var6 = var2;
            } else {
               StringBuilder var7 = new StringBuilder();
               var7.append(var2);
               var7.append(var4.getEncoding());
               var6 = var7.toString();
               StringBuilder var5;
               if (var4 instanceof VideoFormat) {
                  var5 = new StringBuilder();
                  var5.append(var6);
                  var5.append(" video");
                  var2 = var5.toString();
               } else {
                  var2 = var6;
                  if (var4 instanceof AudioFormat) {
                     var5 = new StringBuilder();
                     var5.append(var6);
                     var5.append(" audio");
                     var2 = var5.toString();
                  }
               }

               var6 = var2;
               if (var1 + 1 < this.trackControls.length) {
                  var7 = new StringBuilder();
                  var7.append(var2);
                  var7.append(", ");
                  var6 = var7.toString();
               }
            }
         }

         ++var1;
         var2 = var6;
      }
   }

   public Control[] getControls() {
      Vector var6 = new Vector();
      Vector var5 = this.modules;
      int var1;
      if (var5 == null) {
         var1 = 0;
      } else {
         var1 = var5.size();
      }

      byte var4 = 0;

      int var2;
      int var3;
      for(var2 = 0; var2 < var1; ++var2) {
         Object[] var7 = ((Module)this.modules.elementAt(var2)).getControls();
         if (var7 != null) {
            for(var3 = 0; var3 < var7.length; ++var3) {
               var6.addElement(var7[var3]);
            }
         }
      }

      var3 = var6.size();
      if (this.videoEnabled() && this.frameRateControl == null) {
         this.frameRateControl = new FrameRateAdapter(this.player, 0.0F, 0.0F, 30.0F, false) {
            public Component getControlComponent() {
               return null;
            }

            public Object getOwner() {
               return PlaybackEngine.this.player;
            }

            public float setFrameRate(float var1) {
               this.value = var1;
               return -1.0F;
            }
         };
      }

      if (this.bitRateControl == null) {
         this.bitRateControl = new PlaybackEngine.BitRateA(0, -1, -1, false);
      }

      var2 = var4;
      if (this.frameRateControl != null) {
         var2 = 0 + 1;
      }

      var1 = var2;
      if (this.bitRateControl != null) {
         var1 = var2 + 1;
      }

      var2 = var1;
      if (this.framePositioningControl != null) {
         var2 = var1 + 1;
      }

      Control[] var8 = new Control[var3 + var2 + this.trackControls.length];

      for(var1 = 0; var1 < var3; ++var1) {
         var8[var1] = (Control)var6.elementAt(var1);
      }

      BitRateControl var9 = this.bitRateControl;
      var1 = var3;
      if (var9 != null) {
         var8[var3] = var9;
         var1 = var3 + 1;
      }

      FrameRateControl var10 = this.frameRateControl;
      var2 = var1;
      if (var10 != null) {
         var8[var1] = var10;
         var2 = var1 + 1;
      }

      FramePositioningControl var11 = this.framePositioningControl;
      var1 = var2;
      if (var11 != null) {
         var8[var2] = var11;
         var1 = var2 + 1;
      }

      var2 = 0;

      while(true) {
         BasicTrackControl[] var12 = this.trackControls;
         if (var2 >= var12.length) {
            return var8;
         }

         var8[var1 + var2] = var12[var2];
         ++var2;
      }
   }

   public Time getDuration() {
      return this.source.getDuration();
   }

   public GainControl getGainControl() {
      return (GainControl)this.getControl("javax.media.GainControl");
   }

   public long getLatency() {
      return this.latency;
   }

   int getNumTracks() {
      int var2 = 0;
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var4 = this.trackControls;
         if (var1 >= var4.length) {
            return var2;
         }

         int var3 = var2;
         if (var4[var1].isEnabled()) {
            var3 = var2 + 1;
         }

         ++var1;
         var2 = var3;
      }
   }

   protected PlugIn getPlugIn(BasicModule var1) {
      if (var1 instanceof BasicSourceModule) {
         return ((BasicSourceModule)var1).getDemultiplexer();
      } else if (var1 instanceof BasicFilterModule) {
         return ((BasicFilterModule)var1).getCodec();
      } else {
         return var1 instanceof BasicRendererModule ? ((BasicRendererModule)var1).getRenderer() : null;
      }
   }

   public Time getStartLatency() {
      if (this.state == 100 || this.state == 200) {
         this.throwError(new NotRealizedError("Cannot get start latency from an unrealized controller"));
      }

      return LATENCY_UNKNOWN;
   }

   public TimeBase getTimeBase() {
      return this.getClock().getTimeBase();
   }

   public Component getVisualComponent() {
      Vector var2 = new Vector(1);
      if (this.modules == null) {
         return null;
      } else {
         for(int var1 = 0; var1 < this.modules.size(); ++var1) {
            PlugIn var3 = this.getPlugIn((BasicModule)this.modules.elementAt(var1));
            if (var3 instanceof VideoRenderer) {
               Component var4 = ((VideoRenderer)var3).getComponent();
               if (var4 != null) {
                  var2.addElement(var4);
               }
            }
         }

         if (var2.size() == 0) {
            return null;
         } else if (var2.size() == 1) {
            return (Component)var2.elementAt(0);
         } else {
            return this.createVisualContainer(var2);
         }
      }
   }

   public void internalErrorOccurred(Module var1) {
      if (!this.internalErrorOccurred) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Internal module ");
         var2.append(var1);
         var2.append(" failed!");
         this.sendEvent(new InternalErrorEvent(this, var2.toString()));
         this.internalErrorOccurred = true;
         this.close();
      }

   }

   protected boolean isConfigurable() {
      return true;
   }

   public boolean isRTP() {
      boolean var3 = this.testedRTP;
      boolean var2 = false;
      boolean var1 = false;
      if (var3) {
         if (this.rtpInfo != null) {
            var1 = true;
         }

         return var1;
      } else {
         RTPInfo var4 = (RTPInfo)this.dsource.getControl(RTPInfo.class.getName());
         this.rtpInfo = var4;
         this.testedRTP = true;
         var1 = var2;
         if (var4 != null) {
            var1 = true;
         }

         return var1;
      }
   }

   protected BasicModule lastModule(BasicModule var1) {
      for(OutputConnector var2 = var1.getOutputConnector((String)null); var2 != null; var2 = var1.getOutputConnector((String)null)) {
         InputConnector var3 = var2.getInputConnector();
         if (var3 == null) {
            break;
         }

         var1 = (BasicModule)var3.getModule();
      }

      return var1;
   }

   protected void localStop() {
      synchronized(this){}

      try {
         super.stop();
      } finally {
         ;
      }

   }

   boolean manageTimeBases() {
      this.masterSink = this.findMasterSink();
      return this.updateMasterTimeBase();
   }

   public void markedDataArrived(Module var1, Buffer var2) {
      if (var1 instanceof BasicSourceModule) {
         this.markedDataStartTime = this.getMediaNanoseconds();
      } else {
         long var3 = this.getMediaNanoseconds() - this.markedDataStartTime;
         if (var3 > 0L && var3 < 1000000000L) {
            if (!this.reportOnce) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Computed latency for video: ");
               var5.append(var3 / 1000000L);
               var5.append(" ms\n");
               Log.comment(var5.toString());
               this.reportOnce = true;
            }

            this.latency = (this.latency + var3) / 2L;
         }

      }
   }

   public void mediaEnded(Module var1) {
      if (var1 instanceof BasicSinkModule) {
         Vector var2 = this.waitEnded;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label317: {
            try {
               if (this.waitEnded.contains(var1)) {
                  this.waitEnded.removeElement(var1);
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label317;
            }

            label305: {
               try {
                  if (this.waitEnded.isEmpty()) {
                     this.started = false;
                     this.stopControllerOnly();
                     this.sendEvent(new EndOfMediaEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
                     this.slaveClock.reset(USE_MASTER);
                     break label305;
                  }
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label317;
               }

               try {
                  if (var1 == this.masterSink) {
                     this.slaveClock.reset(USE_BACKUP);
                  }
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label317;
               }
            }

            label296:
            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label296;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void pluginTerminated(Module var1) {
      if (!this.internalErrorOccurred) {
         this.sendEvent(new ControllerClosedEvent(this));
         this.internalErrorOccurred = true;
         this.close();
      }

   }

   protected void reset() {
      synchronized(this){}

      try {
         if (!this.started && this.prefetched && !this.dataPathBlocked) {
            this.doReset();
            return;
         }
      } finally {
         ;
      }

   }

   protected void resetBitRate() {
      this.source.resetBitsRead();
   }

   public void resetted(Module var1) {
      Vector var2 = this.waitResetted;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label196: {
         try {
            if (this.waitResetted.contains(var1)) {
               this.waitResetted.removeElement(var1);
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label196;
         }

         try {
            if (this.waitResetted.isEmpty()) {
               this.waitResetted.notifyAll();
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label196;
         }

         label186:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label186;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public void setMediaTime(Time var1) {
      synchronized(this){}

      Throwable var10000;
      label140: {
         boolean var10001;
         try {
            if (this.state < 300) {
               this.throwError(new NotRealizedError("Cannot set media time on a unrealized controller"));
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label140;
         }

         long var2;
         long var4;
         try {
            var2 = var1.getNanoseconds();
            var4 = this.getMediaNanoseconds();
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label140;
         }

         if (var2 == var4) {
            return;
         }

         try {
            this.reset();
            this.timeBeforeAbortPrefetch = null;
            this.doSetMediaTime(var1);
            this.doPrefetch();
            this.sendEvent(new MediaTimeSetEvent(this, var1));
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label140;
         }

         return;
      }

      Throwable var18 = var10000;
      throw var18;
   }

   public void setProgressControl(ProgressControl var1) {
      this.progressControl = var1;
   }

   protected void setRenderBufferSize(Renderer var1) {
      BufferControl var2 = (BufferControl)var1.getControl(BufferControl.class.getName());
      if (var2 != null) {
         var2.setBufferLength(2000L);
      }

   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      BasicSourceModule var2;
      StringBuilder var3;
      try {
         var2 = BasicSourceModule.createModule(var1);
         this.source = var2;
      } catch (IOException var4) {
         var3 = new StringBuilder();
         var3.append("Input DataSource: ");
         var3.append(var1);
         Log.warning(var3.toString());
         StringBuilder var6 = new StringBuilder();
         var6.append("  Failed with IO exception: ");
         var6.append(var4.getMessage());
         Log.warning(var6.toString());
         throw var4;
      } catch (IncompatibleSourceException var5) {
         var3 = new StringBuilder();
         var3.append("Input DataSource: ");
         var3.append(var1);
         Log.warning(var3.toString());
         Log.warning("  is not compatible with the MediaEngine.");
         Log.warning("  It's likely that the DataSource is required to extend PullDataSource;");
         Log.warning("  and that its source streams implement the Seekable interface ");
         Log.warning("  and with random access capability.");
         throw var5;
      }

      if (var2 != null) {
         var2.setController(this);
         this.dsource = var1;
         if (var1 instanceof Streamable && !((Streamable)var1).isPrefetchable()) {
            this.prefetchEnabled = false;
            this.dataPathBlocked = true;
         }

         if (this.dsource instanceof CaptureDevice) {
            this.prefetchEnabled = false;
         }

      } else {
         throw new IncompatibleSourceException();
      }
   }

   public void setStopTime(Time var1) {
      if (this.getState() < 300) {
         this.throwError(new NotRealizedError("Cannot set stop time on an unrealized controller."));
      }

      if (this.getStopTime() != null && this.getStopTime().getNanoseconds() != var1.getNanoseconds()) {
         this.sendEvent(new StopTimeChangeEvent(this, var1));
      }

      if (this.getState() == 600 && var1 != Clock.RESET && var1.getNanoseconds() < this.getMediaNanoseconds()) {
         this.localStop();
         this.setStopTime(Clock.RESET);
         this.sendEvent(new StopAtTimeEvent(this, this.getState(), 500, this.getTargetState(), this.getMediaTime()));
      } else {
         this.getClock().setStopTime(var1);
         int var3 = this.sinks.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ((BasicSinkModule)this.sinks.elementAt(var2)).setStopTime(var1);
         }

      }
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      this.getClock().setTimeBase(var1);
      Vector var4 = this.sinks;
      if (var4 != null) {
         int var3 = var4.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ((BasicSinkModule)this.sinks.elementAt(var2)).setTimeBase(var1);
         }

      }
   }

   public void stop() {
      synchronized(this){}

      try {
         super.stop();
         this.sendEvent(new StopByRequestEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
      } finally {
         ;
      }

   }

   public void stopAtTime(Module var1) {
      if (var1 instanceof BasicSinkModule) {
         Vector var2 = this.waitStopped;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label468: {
            try {
               if (this.waitStopped.contains(var1)) {
                  this.waitStopped.removeElement(var1);
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label468;
            }

            label458: {
               label472: {
                  label455:
                  try {
                     if (!this.waitStopped.isEmpty() && (this.waitEnded.size() != 1 || !this.waitEnded.contains(var1))) {
                        break label455;
                     }
                     break label472;
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label468;
                  }

                  try {
                     if (var1 == this.masterSink) {
                        this.slaveClock.reset(USE_BACKUP);
                     }
                     break label458;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label468;
                  }
               }

               try {
                  this.started = false;
                  this.stopControllerOnly();
                  this.setStopTime(Clock.RESET);
                  this.sendEvent(new StopAtTimeEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
                  this.slaveClock.reset(USE_MASTER);
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label468;
               }
            }

            label439:
            try {
               return;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label439;
            }
         }

         while(true) {
            Throwable var45 = var10000;

            try {
               throw var45;
            } catch (Throwable var39) {
               var10000 = var39;
               var10001 = false;
               continue;
            }
         }
      }
   }

   void traceGraph(BasicModule var1) {
      String[] var3 = var1.getOutputConnectorNames();

      for(int var2 = 0; var2 < var3.length; ++var2) {
         OutputConnector var4 = var1.getOutputConnector(var3[var2]);
         InputConnector var5 = var4.getInputConnector();
         if (var5 != null) {
            Module var7 = var5.getModule();
            if (var7 != null) {
               StringBuilder var6 = new StringBuilder();
               var6.append("  ");
               var6.append(this.getPlugIn(var1));
               Log.write(var6.toString());
               var6 = new StringBuilder();
               var6.append("     connects to: ");
               var6.append(this.getPlugIn((BasicModule)var7));
               Log.write(var6.toString());
               var6 = new StringBuilder();
               var6.append("     format: ");
               var6.append(var4.getFormat());
               Log.write(var6.toString());
               this.traceGraph((BasicModule)var7);
            }
         }
      }

   }

   public void updateFormats() {
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return;
         }

         var2[var1].updateFormat();
         ++var1;
      }
   }

   boolean updateMasterTimeBase() {
      int var2 = this.sinks.size();
      BasicSinkModule var3 = this.masterSink;
      if (var3 != null) {
         this.slaveClock.setMaster(var3.getClock());
      } else {
         this.slaveClock.setMaster((Clock)null);
      }

      for(int var1 = 0; var1 < var2; ++var1) {
         var3 = (BasicSinkModule)this.sinks.elementAt(var1);
         if (var3 != this.masterSink && !var3.prefetchFailed()) {
            try {
               var3.setTimeBase(this.slaveClock.getTimeBase());
            } catch (IncompatibleTimeBaseException var4) {
               return false;
            }
         }
      }

      return true;
   }

   public void updateRates() {
      if (this.getState() >= 300) {
         long var4 = System.currentTimeMillis();
         long var2;
         if (var4 == this.lastStatsTime) {
            var2 = this.lastBitRate;
         } else {
            var2 = (long)((double)this.getBitRate() * 8.0D / (double)(var4 - this.lastStatsTime) * 1000.0D);
         }

         long var6 = (this.lastBitRate + var2) / 2L;
         BitRateControl var8 = this.bitRateControl;
         if (var8 != null) {
            var8.setBitRate((int)var6);
         }

         this.lastBitRate = var2;
         this.lastStatsTime = var4;
         this.resetBitRate();
         int var1 = 0;

         while(true) {
            BasicTrackControl[] var9 = this.trackControls;
            if (var1 >= var9.length) {
               this.source.checkLatency();
               return;
            }

            var9[var1].updateRates(var4);
            ++var1;
         }
      }
   }

   public boolean videoEnabled() {
      int var1 = 0;

      while(true) {
         BasicTrackControl[] var2 = this.trackControls;
         if (var1 >= var2.length) {
            return false;
         }

         if (var2[var1].isEnabled() && this.trackControls[var1].getOriginalFormat() instanceof VideoFormat) {
            return true;
         }

         ++var1;
      }
   }

   class BitRateA extends BitRateAdapter implements Owned {
      public BitRateA(int var2, int var3, int var4, boolean var5) {
         super(var2, var3, var4, var5);
      }

      public Component getControlComponent() {
         return null;
      }

      public Object getOwner() {
         return PlaybackEngine.this.player;
      }

      public int setBitRate(int var1) {
         this.value = var1;
         return this.value;
      }
   }

   class HeavyPanel extends Panel implements VisualContainer {
      public HeavyPanel(Vector var2) {
      }
   }

   class LightPanel extends Container implements VisualContainer {
      public LightPanel(Vector var2) {
      }
   }

   class PlayerGraphBuilder extends SimpleGraphBuilder {
      protected PlaybackEngine engine;

      PlayerGraphBuilder(PlaybackEngine var2) {
         this.engine = var2;
      }

      protected GraphNode buildTrackFromGraph(BasicTrackControl var1, GraphNode var2) {
         return this.engine.buildTrackFromGraph(var1, var2);
      }
   }

   class PlayerTControl extends BasicTrackControl implements Owned {
      // $FF: renamed from: gb net.sf.fmj.media.PlaybackEngine$PlayerGraphBuilder
      protected PlaybackEngine.PlayerGraphBuilder field_52;

      public PlayerTControl(PlaybackEngine var2, Track var3, OutputConnector var4) {
         super(var2, var3, var4);
      }

      public boolean buildTrack(int var1, int var2) {
         PlaybackEngine.PlayerGraphBuilder var4 = this.field_52;
         if (var4 == null) {
            this.field_52 = PlaybackEngine.this.new PlayerGraphBuilder(this.engine);
         } else {
            var4.reset();
         }

         boolean var3 = this.field_52.buildGraph(this);
         this.field_52 = null;
         return var3;
      }

      protected FrameRateControl frameRateControl() {
         return PlaybackEngine.this.frameRateControl;
      }

      public Object getOwner() {
         return PlaybackEngine.this.player;
      }

      public boolean isTimeBase() {
         for(int var1 = 0; var1 < this.modules.size(); ++var1) {
            if (this.modules.elementAt(var1) == PlaybackEngine.this.masterSink) {
               return true;
            }
         }

         return false;
      }

      protected ProgressControl progressControl() {
         return PlaybackEngine.this.progressControl;
      }
   }

   class SlaveClock implements Clock {
      BasicClock backup;
      Clock current;
      Clock master;

      SlaveClock() {
         BasicClock var2 = new BasicClock();
         this.backup = var2;
         this.current = var2;
      }

      public long getMediaNanoseconds() {
         return this.current.getMediaNanoseconds();
      }

      public Time getMediaTime() {
         return this.current.getMediaTime();
      }

      public float getRate() {
         return this.current.getRate();
      }

      public Time getStopTime() {
         return this.backup.getStopTime();
      }

      public Time getSyncTime() {
         return this.current.getSyncTime();
      }

      public TimeBase getTimeBase() {
         return this.current.getTimeBase();
      }

      public Time mapToTimeBase(Time var1) throws ClockStoppedException {
         return this.current.mapToTimeBase(var1);
      }

      protected void reset(boolean var1) {
         Clock var3 = this.master;
         if (var3 != null && var1) {
            this.current = var3;
         } else {
            if (this.master != null) {
               label356: {
                  BasicClock var35 = this.backup;
                  synchronized(var35){}
                  boolean var2 = false;

                  Throwable var10000;
                  boolean var10001;
                  label355: {
                     label347: {
                        try {
                           if (this.backup.getState() != 1) {
                              break label347;
                           }

                           this.backup.stop();
                        } catch (Throwable var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label355;
                        }

                        var2 = true;
                     }

                     try {
                        this.backup.setMediaTime(this.master.getMediaTime());
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label355;
                     }

                     if (var2) {
                        try {
                           this.backup.syncStart(this.backup.getTimeBase().getTime());
                        } catch (Throwable var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label355;
                        }
                     }

                     label334:
                     try {
                        break label356;
                     } catch (Throwable var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label334;
                     }
                  }

                  while(true) {
                     Throwable var4 = var10000;

                     try {
                        throw var4;
                     } catch (Throwable var30) {
                        var10000 = var30;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            }

            this.current = this.backup;
         }
      }

      public void setMaster(Clock var1) {
         this.master = var1;
         Object var2;
         if (var1 == null) {
            var2 = this.backup;
         } else {
            var2 = var1;
         }

         this.current = (Clock)var2;
         if (var1 != null) {
            try {
               this.backup.setTimeBase(var1.getTimeBase());
               return;
            } catch (IncompatibleTimeBaseException var3) {
            }
         }

      }

      public void setMediaTime(Time var1) {
         BasicClock var2 = this.backup;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label195: {
            label189: {
               try {
                  if (this.backup.getState() == 1) {
                     this.backup.stop();
                     this.backup.setMediaTime(var1);
                     this.backup.syncStart(this.backup.getTimeBase().getTime());
                     break label189;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label195;
               }

               try {
                  this.backup.setMediaTime(var1);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label195;
               }
            }

            label180:
            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label180;
            }
         }

         while(true) {
            Throwable var23 = var10000;

            try {
               throw var23;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }

      public float setRate(float var1) {
         return this.backup.setRate(var1);
      }

      public void setStopTime(Time param1) {
         // $FF: Couldn't be decompiled
      }

      public void setTimeBase(TimeBase param1) throws IncompatibleTimeBaseException {
         // $FF: Couldn't be decompiled
      }

      public void stop() {
         // $FF: Couldn't be decompiled
      }

      public void syncStart(Time var1) {
         BasicClock var2 = this.backup;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (this.backup.getState() != 1) {
                  this.backup.syncStart(var1);
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var15 = var10000;

            try {
               throw var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
