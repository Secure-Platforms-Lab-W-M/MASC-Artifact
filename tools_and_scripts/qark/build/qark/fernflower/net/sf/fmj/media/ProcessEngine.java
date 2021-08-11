package net.sf.fmj.media;

import java.util.Vector;
import javax.media.Codec;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Owned;
import javax.media.PlugIn;
import javax.media.PlugInManager;
import javax.media.Renderer;
import javax.media.Track;
import javax.media.UnsupportedPlugInException;
import javax.media.control.FrameRateControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import net.sf.fmj.filtergraph.GraphNode;
import net.sf.fmj.filtergraph.SimpleGraphBuilder;
import net.sf.fmj.media.codec.video.colorspace.RGBScaler;
import net.sf.fmj.media.control.ProgressControl;
import net.sf.fmj.media.util.Resource;
import org.atalk.android.util.java.awt.Dimension;

public class ProcessEngine extends PlaybackEngine {
   protected BasicMuxModule muxModule;
   protected ContentDescriptor outputContentDes = null;
   String prefetchError;
   protected GraphNode targetMux;
   protected Format[] targetMuxFormats;
   protected Vector targetMuxNames;
   protected GraphNode[] targetMuxes;

   public ProcessEngine(BasicProcessor var1) {
      super(var1);
      StringBuilder var2 = new StringBuilder();
      var2.append("Failed to prefetch: ");
      var2.append(this);
      this.prefetchError = var2.toString();
      this.targetMuxNames = null;
      this.targetMuxes = null;
      this.targetMux = null;
      this.targetMuxFormats = null;
   }

   boolean connectMux() {
      BasicTrackControl[] var4 = new BasicTrackControl[this.trackControls.length];
      int var2 = 0;
      Multiplexer var5 = (Multiplexer)this.targetMux.plugin;

      int var1;
      int var3;
      for(var1 = 0; var1 < this.trackControls.length; var2 = var3) {
         var3 = var2;
         if (this.trackControls[var1].isEnabled()) {
            var4[var2] = this.trackControls[var1];
            var3 = var2 + 1;
         }

         ++var1;
      }

      try {
         var5.setContentDescriptor(this.outputContentDes);
      } catch (Exception var7) {
         Log.comment("Failed to set the output content descriptor on the multiplexer.");
         return false;
      }

      boolean var9 = false;
      if (var5.setNumTracks(this.targetMuxFormats.length) != this.targetMuxFormats.length) {
         Log.comment("Failed  to set number of tracks on the multiplexer.");
         return false;
      } else {
         var1 = 0;

         boolean var8;
         while(true) {
            Format[] var6 = this.targetMuxFormats;
            var8 = var9;
            if (var1 >= var6.length) {
               break;
            }

            if (var6[var1] == null || var5.setInputFormat(var6[var1], var1) == null) {
               Log.comment("Failed to set input format on the multiplexer.");
               var8 = true;
               break;
            }

            ++var1;
         }

         if (var8) {
            return false;
         } else if (SimpleGraphBuilder.inspector != null && !SimpleGraphBuilder.inspector.verify(var5, this.targetMuxFormats)) {
            return false;
         } else {
            BasicMuxModule var10 = new BasicMuxModule(var5, this.targetMuxFormats);

            for(var1 = 0; var1 < this.targetMuxFormats.length; ++var1) {
               StringBuilder var11 = new StringBuilder();
               var11.append(BasicMuxModule.ConnectorNamePrefix);
               var11.append(var1);
               InputConnector var12 = var10.getInputConnector(var11.toString());
               if (var12 == null) {
                  Log.comment("BasicMuxModule: connector mismatched.");
                  return false;
               }

               var12.setFormat(this.targetMuxFormats[var1]);
               var4[var1].lastOC.setProtocol(var12.getProtocol());
               var4[var1].lastOC.connectTo(var12, this.targetMuxFormats[var1]);
            }

            if (!var10.doRealize()) {
               return false;
            } else {
               var10.setModuleListener(this);
               var10.setController(this);
               this.modules.addElement(var10);
               this.sinks.addElement(var10);
               this.muxModule = var10;
               return true;
            }
         }
      }
   }

   protected boolean doConfigure() {
      if (!this.doConfigure1()) {
         return false;
      } else {
         String[] var2 = this.source.getOutputConnectorNames();
         this.trackControls = new BasicTrackControl[this.tracks.length];

         for(int var1 = 0; var1 < this.tracks.length; ++var1) {
            this.trackControls[var1] = new ProcessEngine.ProcTControl(this, this.tracks[var1], this.source.getOutputConnector(var2[var1]));
         }

         if (!this.doConfigure2()) {
            return false;
         } else {
            this.outputContentDes = new ContentDescriptor("raw");
            this.reenableHintTracks();
            return true;
         }
      }
   }

   protected boolean doPrefetch() {
      synchronized(this){}

      Throwable var10000;
      label249: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.prefetched;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label249;
         }

         if (var1) {
            return true;
         }

         try {
            var1 = this.doPrefetch1();
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label249;
         }

         if (!var1) {
            return false;
         }

         try {
            if (this.muxModule != null && !this.muxModule.doPrefetch()) {
               Log.error(this.prefetchError);
               StringBuilder var23 = new StringBuilder();
               var23.append("  Cannot prefetch the multiplexer: ");
               var23.append(this.muxModule.getMultiplexer());
               var23.append("\n");
               Log.error(var23.toString());
               return false;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label249;
         }

         try {
            var1 = this.doPrefetch2();
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label249;
         }

         return var1;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected boolean doRealize() {
      synchronized(this){}

      Throwable var10000;
      label179: {
         boolean var1;
         boolean var10001;
         try {
            this.targetMuxes = null;
            var1 = super.doRealize1();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label179;
         }

         if (!var1) {
            return false;
         }

         label173: {
            try {
               if (this.targetMux == null || this.connectMux()) {
                  break label173;
               }

               Log.error(this.realizeError);
               Log.error("  Cannot connect the multiplexer\n");
               this.player.processError = this.genericProcessorError;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label179;
            }

            return false;
         }

         try {
            var1 = super.doRealize2();
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label179;
         }

         if (!var1) {
            return false;
         }

         return true;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void doStart() {
      synchronized(this){}

      Throwable var10000;
      label140: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.started;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label140;
         }

         if (var1) {
            return;
         }

         try {
            this.doStart1();
            if (this.muxModule != null) {
               this.muxModule.doStart();
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label140;
         }

         label128:
         try {
            this.doStart2();
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label128;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected void doStop() {
      synchronized(this){}

      Throwable var10000;
      label140: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.started;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label140;
         }

         if (!var1) {
            return;
         }

         try {
            this.doStop1();
            if (this.muxModule != null) {
               this.muxModule.doStop();
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label140;
         }

         label128:
         try {
            this.doStop2();
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label128;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   protected BasicSinkModule findMasterSink() {
      BasicMuxModule var1 = this.muxModule;
      return (BasicSinkModule)(var1 != null && var1.getClock() != null ? this.muxModule : super.findMasterSink());
   }

   protected long getBitRate() {
      BasicMuxModule var1 = this.muxModule;
      return var1 != null ? var1.getBitsWritten() : this.source.getBitsRead();
   }

   public ContentDescriptor getContentDescriptor() throws NotConfiguredError {
      if (this.getState() < 180) {
         StringBuilder var1 = new StringBuilder();
         var1.append("getContentDescriptor ");
         var1.append(NOT_CONFIGURED_ERROR);
         this.throwError(new NotConfiguredError(var1.toString()));
      }

      return this.outputContentDes;
   }

   public DataSource getDataOutput() throws NotRealizedError {
      if (this.getState() < 300) {
         StringBuilder var1 = new StringBuilder();
         var1.append("getDataOutput ");
         var1.append(NOT_REALIZED_ERROR);
         this.throwError(new NotRealizedError(var1.toString()));
      }

      BasicMuxModule var2 = this.muxModule;
      return var2 != null ? var2.getDataOutput() : null;
   }

   BasicMuxModule getMuxModule() {
      return this.muxModule;
   }

   protected PlugIn getPlugIn(BasicModule var1) {
      return (PlugIn)(var1 instanceof BasicMuxModule ? ((BasicMuxModule)var1).getMultiplexer() : super.getPlugIn(var1));
   }

   public ContentDescriptor[] getSupportedContentDescriptors() throws NotConfiguredError {
      if (this.getState() < 180) {
         StringBuilder var6 = new StringBuilder();
         var6.append("getSupportedContentDescriptors ");
         var6.append(NOT_CONFIGURED_ERROR);
         this.throwError(new NotConfiguredError(var6.toString()));
      }

      Vector var7 = PlugInManager.getPlugInList((Format)null, (Format)null, 5);
      Vector var9 = new Vector();

      int var1;
      for(var1 = 0; var1 < var7.size(); ++var1) {
         Format[] var8 = PlugInManager.getSupportedOutputFormats((String)var7.elementAt(var1), 5);
         if (var8 != null) {
            for(int var2 = 0; var2 < var8.length; ++var2) {
               if (var8[var2] instanceof ContentDescriptor) {
                  boolean var5 = false;
                  int var3 = 0;

                  boolean var4;
                  while(true) {
                     var4 = var5;
                     if (var3 >= var9.size()) {
                        break;
                     }

                     if (var9.elementAt(var3).equals(var8[var2])) {
                        var4 = true;
                        break;
                     }

                     ++var3;
                  }

                  if (!var4) {
                     var9.addElement(var8[var2]);
                  }
               }
            }
         }
      }

      ContentDescriptor[] var10 = new ContentDescriptor[var9.size()];

      for(var1 = 0; var1 < var9.size(); ++var1) {
         var10[var1] = (ContentDescriptor)var9.elementAt(var1);
      }

      return var10;
   }

   public TrackControl[] getTrackControls() throws NotConfiguredError {
      if (this.getState() < 180) {
         StringBuilder var1 = new StringBuilder();
         var1.append("getTrackControls ");
         var1.append(NOT_CONFIGURED_ERROR);
         this.throwError(new NotConfiguredError(var1.toString()));
      }

      return this.trackControls;
   }

   boolean isRTPFormat(Format var1) {
      return var1 != null && var1.getEncoding() != null && var1.getEncoding().endsWith("rtp") || var1.getEncoding().endsWith("RTP");
   }

   void reenableHintTracks() {
      for(int var1 = 0; var1 < this.trackControls.length; ++var1) {
         if (this.isRTPFormat(this.trackControls[var1].getOriginalFormat())) {
            this.trackControls[var1].setEnabled(true);
            return;
         }
      }

   }

   protected void resetBitRate() {
      BasicMuxModule var1 = this.muxModule;
      if (var1 != null) {
         var1.resetBitsWritten();
      } else {
         this.source.resetBitsRead();
      }
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) throws NotConfiguredError {
      if (this.getState() < 180) {
         StringBuilder var2 = new StringBuilder();
         var2.append("setContentDescriptor ");
         var2.append(NOT_CONFIGURED_ERROR);
         this.throwError(new NotConfiguredError(var2.toString()));
      }

      if (this.getState() > 180) {
         return null;
      } else {
         if (var1 != null) {
            Vector var3 = PlugInManager.getPlugInList((Format)null, var1, 5);
            if (var3 == null || var3.size() == 0) {
               return null;
            }
         }

         this.outputContentDes = var1;
         return var1;
      }
   }

   class ProcGraphBuilder extends SimpleGraphBuilder {
      Codec[] codecs = null;
      protected ProcessEngine engine;
      Format format = null;
      protected int nodesVisited = 0;
      protected int numTracks = 1;
      Renderer rend = null;
      protected Format targetFormat;
      protected int trackID = 0;

      ProcGraphBuilder(ProcessEngine var2) {
         this.engine = var2;
      }

      GraphNode buildCustomGraph(Format var1) {
         Vector var4 = new Vector();
         var4.addElement(new GraphNode((String)null, (PlugIn)null, var1, (GraphNode)null, 0));
         Log.comment("Custom options specified.");
         this.indent = 1;
         Log.setIndent(this.indent);
         Vector var3 = var4;
         StringBuilder var6;
         GraphNode var9;
         if (this.codecs != null) {
            this.resetTargets();
            int var2 = 0;

            while(true) {
               Codec[] var5 = this.codecs;
               var3 = var4;
               if (var2 >= var5.length) {
                  break;
               }

               if (var5[var2] != null) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("A custom codec is specified: ");
                  var8.append(this.codecs[var2]);
                  Log.comment(var8.toString());
                  this.setTargetPlugin(this.codecs[var2], 2);
                  var9 = this.buildGraph(var4);
                  if (var9 == null) {
                     var6 = new StringBuilder();
                     var6.append("The input format is not compatible with the given codec plugin: ");
                     var6.append(this.codecs[var2]);
                     Log.error(var6.toString());
                     this.indent = 0;
                     Log.setIndent(this.indent);
                     return null;
                  }

                  var9.level = 0;
                  var4 = new Vector();
                  var4.addElement(var9);
               }

               ++var2;
            }
         }

         GraphNode var7;
         if (ProcessEngine.this.outputContentDes != null) {
            this.resetTargets();
            var1 = this.format;
            if (var1 != null) {
               this.targetFormat = var1;
               var6 = new StringBuilder();
               var6.append("An output format is specified: ");
               var6.append(this.format);
               Log.comment(var6.toString());
            }

            if (!this.setDefaultTargetMux()) {
               return null;
            }

            var9 = this.buildGraph(var3);
            var7 = var9;
            if (var9 == null) {
               Log.error("Failed to build a graph for the given custom options.");
               this.indent = 0;
               Log.setIndent(this.indent);
               return null;
            }
         } else {
            var4 = var3;
            if (this.format != null) {
               this.resetTargets();
               this.targetFormat = this.format;
               StringBuilder var10 = new StringBuilder();
               var10.append("An output format is specified: ");
               var10.append(this.format);
               Log.comment(var10.toString());
               var9 = this.buildGraph(var3);
               if (var9 == null) {
                  Log.error("The input format cannot be transcoded to the specified target format.");
                  this.indent = 0;
                  Log.setIndent(this.indent);
                  return null;
               }

               var9.level = 0;
               var4 = new Vector();
               var4.addElement(var9);
               this.targetFormat = null;
            }

            if (this.rend != null) {
               var6 = new StringBuilder();
               var6.append("A custom renderer is specified: ");
               var6.append(this.rend);
               Log.comment(var6.toString());
               this.setTargetPlugin(this.rend, 4);
               var9 = this.buildGraph(var4);
               var7 = var9;
               if (var9 == null) {
                  if (this.format != null) {
                     var6 = new StringBuilder();
                     var6.append("The customed transocoded format is not compatible with the given renderer plugin: ");
                     var6.append(this.rend);
                     Log.error(var6.toString());
                  } else {
                     var6 = new StringBuilder();
                     var6.append("The input format is not compatible with the given renderer plugin: ");
                     var6.append(this.rend);
                     Log.error(var6.toString());
                  }

                  this.indent = 0;
                  Log.setIndent(this.indent);
                  return null;
               }
            } else {
               Format var11 = this.format;
               Format var12 = var11;
               if (var11 == null) {
                  var12 = var1;
               }

               if (!this.setDefaultTargetRenderer(var12)) {
                  return null;
               }

               var9 = this.buildGraph(var4);
               var7 = var9;
               if (var9 == null) {
                  if (this.format != null) {
                     Log.error("Failed to find a renderer that supports the customed transcoded format.");
                  } else {
                     Log.error("Failed to build a graph to render the input format with the given custom options.");
                  }

                  this.indent = 0;
                  Log.setIndent(this.indent);
                  return null;
               }
            }
         }

         this.indent = 0;
         Log.setIndent(this.indent);
         return var7;
      }

      boolean buildCustomGraph(ProcessEngine.ProcTControl var1) {
         this.codecs = var1.codecChainWanted;
         this.rend = var1.rendererWanted;
         Format var4 = var1.formatWanted;
         this.format = var4;
         if (var4 instanceof VideoFormat && var1.getOriginalFormat() instanceof VideoFormat) {
            Dimension var6 = ((VideoFormat)var1.getOriginalFormat()).getSize();
            Dimension var5 = ((VideoFormat)this.format).getSize();
            if (var6 != null && var5 != null && !var6.equals(var5)) {
               RGBScaler var7 = new RGBScaler(var5);
               Codec[] var9 = this.codecs;
               if (var9 != null && var9.length != 0) {
                  this.codecs = new Codec[var1.codecChainWanted.length + 1];
                  int var2;
                  if (!PlaybackEngine.isRawVideo(this.format)) {
                     this.codecs[0] = var7;
                     var2 = 1;
                  } else {
                     this.codecs[var1.codecChainWanted.length] = var7;
                     var2 = 0;
                  }

                  for(int var3 = 0; var3 < var1.codecChainWanted.length; ++var2) {
                     this.codecs[var2] = var1.codecChainWanted[var3];
                     ++var3;
                  }
               } else {
                  var9 = new Codec[1];
                  this.codecs = var9;
                  var9[0] = var7;
               }
            }
         }

         GraphNode var8 = this.buildCustomGraph(var1.getOriginalFormat());
         return var8 != null && this.buildTrackFromGraph(var1, var8) == null;
      }

      boolean buildGraph(BasicTrackControl var1, int var2, int var3) {
         this.trackID = var2;
         this.numTracks = var3;
         if (var1.isCustomized()) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Input: ");
            var4.append(var1.getOriginalFormat());
            Log.comment(var4.toString());
            return this.buildCustomGraph((ProcessEngine.ProcTControl)var1);
         } else {
            return super.buildGraph(var1);
         }
      }

      protected GraphNode buildTrackFromGraph(BasicTrackControl var1, GraphNode var2) {
         return this.engine.buildTrackFromGraph(var1, var2);
      }

      void doGetSupportedOutputFormats(Vector var1, Vector var2) {
         GraphNode var10 = (GraphNode)var1.firstElement();
         var1.removeElementAt(0);
         if (var10.input != null || var10.plugin != null && var10.plugin instanceof Codec) {
            if (var10.plugin == null || verifyInput(var10.plugin, var10.input) != null) {
               int var3;
               int var4;
               Format[] var13;
               Format var15;
               if (var10.plugin != null) {
                  Format[] var8 = var10.getSupportedOutputs(var10.input);
                  if (var8 == null) {
                     return;
                  }

                  if (var8.length == 0) {
                     return;
                  }

                  Format var9;
                  for(var3 = 0; var3 < var8.length; ++var3) {
                     int var7 = var2.size();
                     boolean var6 = false;
                     var4 = 0;

                     boolean var5;
                     while(true) {
                        var5 = var6;
                        if (var4 >= var7) {
                           break;
                        }

                        var9 = (Format)var2.elementAt(var4);
                        if (var9 == var8[var3] || var9.equals(var8[var3])) {
                           var5 = true;
                           break;
                        }

                        ++var4;
                     }

                     if (!var5) {
                        var2.addElement(var8[var3]);
                     }
                  }

                  var9 = var10.input;
                  var13 = var8;
                  var15 = var9;
               } else {
                  Format var14 = var10.input;
                  var15 = null;
                  var13 = new Format[]{var14};
               }

               if (var10.level < this.STAGES) {
                  for(var3 = 0; var3 < var13.length; ++var3) {
                     if ((var15 == null || !var15.equals(var13[var3])) && (var10.plugin == null || verifyOutput(var10.plugin, var13[var3]) != null)) {
                        Vector var16 = PlugInManager.getPlugInList(var13[var3], (Format)null, 2);
                        if (var16 != null && var16.size() != 0) {
                           for(var4 = 0; var4 < var16.size(); ++var4) {
                              GraphNode var11 = getPlugInNode((String)var16.elementAt(var4), 2, this.plugIns);
                              if (var11 != null && !var11.checkAttempted(var13[var3])) {
                                 Format[] var12 = var11.getSupportedInputs();
                                 Format var17 = matches(var13[var3], var12, (PlugIn)null, var11.plugin);
                                 if (var17 != null) {
                                    var1.addElement(new GraphNode(var11, var17, var10, var10.level + 1));
                                    ++this.nodesVisited;
                                 }
                              }
                           }
                        }
                     }
                  }

               }
            }
         } else {
            Log.error("Internal error: doGetSupportedOutputFormats");
         }
      }

      protected GraphNode findTarget(GraphNode var1) {
         Format[] var2;
         Format[] var3;
         if (var1.plugin == null) {
            var2 = new Format[]{var1.input};
         } else if (var1.output != null) {
            var2 = new Format[]{var1.output};
         } else {
            var3 = var1.getSupportedOutputs(var1.input);
            if (var3 == null) {
               return null;
            }

            var2 = var3;
            if (var3.length == 0) {
               return null;
            }
         }

         Format var4 = this.targetFormat;
         var3 = var2;
         if (var4 != null) {
            Format var5 = matches(var2, var4, var1.plugin, (PlugIn)null);
            if (var5 == null) {
               return null;
            }

            if (inspector != null && !inspector.verify((Codec)var1.plugin, var1.input, var5)) {
               return null;
            }

            if (this.targetPlugins == null && ProcessEngine.this.targetMuxes == null) {
               var1.output = var5;
               return var1;
            }

            var3 = new Format[]{var5};
         }

         if (this.targetPlugins != null) {
            var1 = this.verifyTargetPlugins(var1, var3);
            return var1 != null ? var1 : null;
         } else {
            if (ProcessEngine.this.targetMuxes != null) {
               var1 = this.verifyTargetMuxes(var1, var3);
               if (var1 != null) {
                  return var1;
               }
            }

            return null;
         }
      }

      public Format[] getSupportedOutputFormats(Format var1) {
         long var6 = System.currentTimeMillis();
         Vector var9 = new Vector();
         Vector var8 = new Vector();
         var8.addElement(new GraphNode((String)null, (PlugIn)null, var1, (GraphNode)null, 0));
         var9.addElement(var1);
         int var3 = this.nodesVisited;
         boolean var2 = true;
         this.nodesVisited = var3 + 1;

         while(!var8.isEmpty()) {
            this.doGetSupportedOutputFormats(var8, var9);
         }

         Format[] var13 = new Format[var9.size()];
         int var4 = 0;
         int var5 = var13.length - 1;
         AudioFormat var10 = new AudioFormat("mpegaudio/rtp");
         if (!(new AudioFormat("mpegaudio")).matches(var1) && !(new AudioFormat("mpeglayer3")).matches(var1) && !(new VideoFormat("mpeg")).matches(var1)) {
            var2 = false;
         }

         for(var3 = 0; var3 < var13.length; ++var3) {
            Object var11 = var9.elementAt(var3);
            if (!var2 && var10.matches((Format)var11)) {
               var13[var5] = (Format)var11;
               --var5;
            } else {
               var13[var4] = (Format)var11;
               ++var4;
            }
         }

         Log.comment("Getting the supported output formats for:");
         StringBuilder var14 = new StringBuilder();
         var14.append("  ");
         var14.append(var1);
         Log.comment(var14.toString());
         StringBuilder var12 = new StringBuilder();
         var12.append("  # of nodes visited: ");
         var12.append(this.nodesVisited);
         Log.comment(var12.toString());
         var12 = new StringBuilder();
         var12.append("  # of formats supported: ");
         var12.append(var13.length);
         var12.append("\n");
         Log.comment(var12.toString());
         PlaybackEngine.profile("getSupportedOutputFormats", var6);
         return var13;
      }

      public void reset() {
         super.reset();
         this.resetTargets();
      }

      void resetTargets() {
         this.targetFormat = null;
         this.targetPlugins = null;
      }

      boolean setDefaultTargetMux() {
         if (ProcessEngine.this.targetMuxes != null) {
            return true;
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append("An output content type is specified: ");
            var1.append(ProcessEngine.this.outputContentDes);
            Log.comment(var1.toString());
            ProcessEngine var2 = ProcessEngine.this;
            var2.targetMuxNames = PlugInManager.getPlugInList((Format)null, var2.outputContentDes, 5);
            if (ProcessEngine.this.targetMuxNames != null && ProcessEngine.this.targetMuxNames.size() != 0) {
               var2 = ProcessEngine.this;
               var2.targetMuxes = new GraphNode[var2.targetMuxNames.size()];
               ProcessEngine.this.targetMux = null;
               ProcessEngine.this.targetMuxFormats = new Format[this.numTracks];
               this.targetPluginNames = null;
               this.targetPlugins = null;
               return true;
            } else {
               var1 = new StringBuilder();
               var1.append("No multiplexer is found for that content type: ");
               var1.append(ProcessEngine.this.outputContentDes);
               Log.error(var1.toString());
               return false;
            }
         }
      }

      protected boolean setDefaultTargetRenderer(Format var1) {
         if (!super.setDefaultTargetRenderer(var1)) {
            return false;
         } else {
            ProcessEngine.this.targetMuxes = null;
            return true;
         }
      }

      protected boolean setDefaultTargets(Format var1) {
         return ProcessEngine.this.outputContentDes != null ? this.setDefaultTargetMux() : this.setDefaultTargetRenderer(var1);
      }

      void setTargetPlugin(PlugIn var1, int var2) {
         this.targetPlugins = new GraphNode[1];
         this.targetPlugins[0] = new GraphNode(var1, (Format)null, (GraphNode)null, 0);
         this.targetPlugins[0].custom = true;
         this.targetPlugins[0].type = var2;
      }

      GraphNode verifyTargetMuxes(GraphNode var1, Format[] var2) {
         for(int var3 = 0; var3 < ProcessEngine.this.targetMuxes.length; ++var3) {
            GraphNode var6 = ProcessEngine.this.targetMuxes[var3];
            GraphNode var5 = var6;
            if (var6 == null) {
               String var7 = (String)ProcessEngine.this.targetMuxNames.elementAt(var3);
               if (var7 == null) {
                  continue;
               }

               var6 = getPlugInNode(var7, 5, this.plugIns);
               var5 = var6;
               if (var6 == null) {
                  ProcessEngine.this.targetMuxNames.setElementAt((Object)null, var3);
                  continue;
               }

               Multiplexer var8 = (Multiplexer)var6.plugin;
               if (var8.setContentDescriptor(ProcessEngine.this.outputContentDes) == null) {
                  ProcessEngine.this.targetMuxNames.setElementAt((Object)null, var3);
                  continue;
               }

               if (var8.setNumTracks(this.numTracks) != this.numTracks) {
                  ProcessEngine.this.targetMuxNames.setElementAt((Object)null, var3);
                  continue;
               }

               ProcessEngine.this.targetMuxes[var3] = var6;
            }

            if (ProcessEngine.this.targetMux == null || var5 == ProcessEngine.this.targetMux) {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  Format var9 = ((Multiplexer)var5.plugin).setInputFormat(var2[var4], this.trackID);
                  if (var9 != null && (inspector == null || var1.plugin == null || inspector.verify((Codec)var1.plugin, var1.input, var9))) {
                     ProcessEngine.this.targetMux = var5;
                     ProcessEngine.this.targetMuxFormats[this.trackID] = var9;
                     var1.output = var9;
                     return var1;
                  }
               }
            }
         }

         return null;
      }
   }

   class ProcTControl extends BasicTrackControl implements Owned {
      protected Codec[] codecChainWanted = null;
      protected Format formatWanted = null;
      // $FF: renamed from: gb net.sf.fmj.media.ProcessEngine$ProcGraphBuilder
      protected ProcessEngine.ProcGraphBuilder field_51;
      protected Renderer rendererWanted = null;
      protected Format[] supportedFormats = null;

      public ProcTControl(ProcessEngine var2, Track var3, OutputConnector var4) {
         super(var2, var3, var4);
      }

      private Format checkSize(Format var1) {
         if (!(var1 instanceof VideoFormat)) {
            return var1;
         } else {
            VideoFormat var5 = (VideoFormat)var1;
            Dimension var6 = ((VideoFormat)var1).getSize();
            Dimension var7 = var6;
            if (var6 == null) {
               Format var8 = this.getOriginalFormat();
               if (var8 == null) {
                  return var1;
               }

               var6 = ((VideoFormat)var8).getSize();
               var7 = var6;
               if (var6 == null) {
                  return var1;
               }
            }

            int var2 = var7.width;
            int var3 = var7.height;
            if (!var1.matches(new VideoFormat("jpeg/rtp")) && !var1.matches(new VideoFormat("jpeg"))) {
               if (var1.matches(new VideoFormat("h263/rtp")) || var1.matches(new VideoFormat("h263-1998/rtp")) || var1.matches(new VideoFormat("h263"))) {
                  if (var7.width >= 352) {
                     var2 = 352;
                     var3 = 288;
                  } else if (var7.width >= 160) {
                     var2 = 176;
                     var3 = 144;
                  } else {
                     var2 = 128;
                     var3 = 96;
                  }
               }
            } else {
               label71: {
                  if (var7.width % 8 != 0) {
                     var2 = var7.width / 8 * 8;
                  }

                  int var4 = var3;
                  if (var7.height % 8 != 0) {
                     var4 = var7.height / 8 * 8;
                  }

                  if (var2 != 0) {
                     var3 = var4;
                     if (var4 != 0) {
                        break label71;
                     }
                  }

                  var2 = var7.width;
                  var3 = var7.height;
               }
            }

            Format var9;
            if (var2 == var7.width) {
               var9 = var1;
               if (var3 == var7.height) {
                  return var9;
               }
            }

            StringBuilder var10 = new StringBuilder();
            var10.append("setFormat: ");
            var10.append(var1.getEncoding());
            var10.append(": video aspect ratio mismatched.");
            Log.comment(var10.toString());
            var10 = new StringBuilder();
            var10.append("  Scaled from ");
            var10.append(var7.width);
            var10.append("x");
            var10.append(var7.height);
            var10.append(" to ");
            var10.append(var2);
            var10.append("x");
            var10.append(var3);
            var10.append(".\n");
            Log.comment(var10.toString());
            var9 = (new VideoFormat((String)null, new Dimension(var2, var3), -1, (Class)null, -1.0F)).intersects(var1);
            return var9;
         }
      }

      public boolean buildTrack(int var1, int var2) {
         ProcessEngine.ProcGraphBuilder var4 = this.field_51;
         if (var4 == null) {
            this.field_51 = ProcessEngine.this.new ProcGraphBuilder((ProcessEngine)this.engine);
         } else {
            var4.reset();
         }

         boolean var3 = this.field_51.buildGraph(this, var1, var2);
         this.field_51 = null;
         return var3;
      }

      protected FrameRateControl frameRateControl() {
         this.muxModule = ProcessEngine.this.getMuxModule();
         return ProcessEngine.this.frameRateControl;
      }

      public Format getFormat() {
         Format var2 = this.formatWanted;
         Format var1 = var2;
         if (var2 == null) {
            var1 = this.track.getFormat();
         }

         return var1;
      }

      public Object getOwner() {
         return ProcessEngine.this.player;
      }

      public Format[] getSupportedFormats() {
         if (this.supportedFormats == null) {
            Format[] var1 = Resource.getDB(this.track.getFormat());
            this.supportedFormats = var1;
            if (var1 == null) {
               ProcessEngine.ProcGraphBuilder var2 = this.field_51;
               if (var2 == null) {
                  this.field_51 = ProcessEngine.this.new ProcGraphBuilder((ProcessEngine)this.engine);
               } else {
                  var2.reset();
               }

               this.supportedFormats = this.field_51.getSupportedOutputFormats(this.track.getFormat());
               this.supportedFormats = Resource.putDB(this.track.getFormat(), this.supportedFormats);
               PlaybackEngine.needSavingDB = true;
            }
         }

         return ProcessEngine.this.outputContentDes != null ? this.verifyMuxInputs(ProcessEngine.this.outputContentDes, this.supportedFormats) : this.supportedFormats;
      }

      public boolean isCustomized() {
         return this.formatWanted != null || this.codecChainWanted != null || this.rendererWanted != null;
      }

      public boolean isTimeBase() {
         for(int var1 = 0; var1 < this.modules.size(); ++var1) {
            if (this.modules.elementAt(var1) == ProcessEngine.this.masterSink) {
               return true;
            }
         }

         return false;
      }

      public void prError() {
         if (!this.isCustomized()) {
            super.prError();
         } else {
            Log.error("  Cannot build a flow graph with the customized options:");
            StringBuilder var2;
            if (this.formatWanted != null) {
               var2 = new StringBuilder();
               var2.append("    Unable to transcode format: ");
               var2.append(this.getOriginalFormat());
               Log.error(var2.toString());
               var2 = new StringBuilder();
               var2.append("      to: ");
               var2.append(this.getFormat());
               Log.error(var2.toString());
               if (ProcessEngine.this.outputContentDes != null) {
                  var2 = new StringBuilder();
                  var2.append("      outputting to: ");
                  var2.append(ProcessEngine.this.outputContentDes);
                  Log.error(var2.toString());
               }
            }

            if (this.codecChainWanted != null) {
               Log.error("    Unable to add customed codecs: ");

               for(int var1 = 0; var1 < this.codecChainWanted.length; ++var1) {
                  var2 = new StringBuilder();
                  var2.append("      ");
                  var2.append(this.codecChainWanted[var1]);
                  Log.error(var2.toString());
               }
            }

            if (this.rendererWanted != null) {
               var2 = new StringBuilder();
               var2.append("    Unable to add customed renderer: ");
               var2.append(this.rendererWanted);
               Log.error(var2.toString());
            }

            Log.write("\n");
         }
      }

      protected ProgressControl progressControl() {
         return ProcessEngine.this.progressControl;
      }

      public void setCodecChain(Codec[] var1) throws NotConfiguredError, UnsupportedPlugInException {
         if (this.engine.getState() > 180) {
            ProcessEngine.this.throwError(new NotConfiguredError("Cannot set a PlugIn before reaching the configured state."));
         }

         if (var1.length < 1) {
            throw new UnsupportedPlugInException("No codec specified in the array.");
         } else {
            this.codecChainWanted = new Codec[var1.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               this.codecChainWanted[var2] = var1[var2];
            }

         }
      }

      public Format setFormat(Format var1) {
         if (this.engine.getState() > 180) {
            return this.getFormat();
         } else if (var1 != null && !var1.matches(this.track.getFormat())) {
            var1 = this.checkSize(var1);
            this.formatWanted = var1;
            return var1;
         } else {
            return var1;
         }
      }

      public void setRenderer(Renderer var1) throws NotConfiguredError {
         if (this.engine.getState() > 180) {
            ProcessEngine.this.throwError(new NotConfiguredError("Cannot set a PlugIn before reaching the configured state."));
         }

         this.rendererWanted = var1;
         if (var1 instanceof SlowPlugIn) {
            ((SlowPlugIn)var1).forceToUse();
         }

      }

      Format[] verifyMuxInputs(ContentDescriptor var1, Format[] var2) {
         if (var1 != null) {
            if ("raw".equals(var1.getEncoding())) {
               return var2;
            } else {
               Vector var8 = PlugInManager.getPlugInList((Format)null, var1, 5);
               if (var8 != null && var8.size() != 0) {
                  Multiplexer[] var11 = new Multiplexer[var8.size()];
                  int var4 = 0;

                  int var3;
                  int var5;
                  for(var3 = 0; var3 < var8.size(); var4 = var5) {
                     Multiplexer var9 = (Multiplexer)SimpleGraphBuilder.createPlugIn((String)var8.elementAt(var3), 5);
                     var5 = var4;
                     if (var9 != null) {
                        label75: {
                           try {
                              var9.setContentDescriptor(ProcessEngine.this.outputContentDes);
                           } catch (Exception var10) {
                              var5 = var4;
                              break label75;
                           }

                           if (var9.setNumTracks(1) < 1) {
                              var5 = var4;
                           } else {
                              var11[var4] = var9;
                              var5 = var4 + 1;
                           }
                        }
                     }

                     ++var3;
                  }

                  Format[] var13 = new Format[var2.length];
                  int var6 = 0;

                  for(var5 = 0; var5 < var2.length; var6 = var3) {
                     Format var14;
                     if (var4 == 1) {
                        var14 = var11[0].setInputFormat(var2[var5], 0);
                        var3 = var6;
                        if (var14 != null) {
                           var13[var6] = var14;
                           var3 = var6 + 1;
                        }
                     } else {
                        int var7 = 0;

                        while(true) {
                           var3 = var6;
                           if (var7 >= var4) {
                              break;
                           }

                           var14 = var11[var7].setInputFormat(var2[var5], 0);
                           if (var14 != null) {
                              var13[var6] = var14;
                              var3 = var6 + 1;
                              break;
                           }

                           ++var7;
                        }
                     }

                     ++var5;
                  }

                  Format[] var12 = new Format[var6];
                  System.arraycopy(var13, 0, var12, 0, var6);
                  return var12;
               } else {
                  return new Format[0];
               }
            }
         } else {
            return var2;
         }
      }
   }
}
