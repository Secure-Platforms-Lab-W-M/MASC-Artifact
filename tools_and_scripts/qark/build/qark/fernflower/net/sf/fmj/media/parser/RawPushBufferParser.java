package net.sf.fmj.media.parser;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Demultiplexer;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.SourceStream;
import net.sf.fmj.media.BasicPlugIn;
import net.sf.fmj.media.CircularBuffer;
import net.sf.fmj.media.protocol.DelegateDataSource;
import net.sf.fmj.media.rtp.Depacketizer;
import org.atalk.android.util.java.awt.Dimension;

public class RawPushBufferParser extends RawStreamParser {
   public static int[][] MPASampleTbl;
   static final String NAMEBUFFER = "Raw video/audio buffer stream parser";
   static VideoFormat h261Video = new VideoFormat("h261/rtp");
   static VideoFormat h263Video = new VideoFormat("h263/rtp");
   static VideoFormat jpegVideo = new VideoFormat("jpeg/rtp");
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   static VideoFormat mpegVideo = new VideoFormat("mpeg/rtp");
   final float[] MPEGRateTbl = new float[]{0.0F, 23.976F, 24.0F, 25.0F, 29.97F, 30.0F, 50.0F, 59.94F, 60.0F};
   final int[] h261Heights = new int[]{144, 288};
   final int[] h261Widths = new int[]{176, 352};
   final int[] h263Heights = new int[]{0, 96, 144, 288, 576, 1152, 0, 0};
   final int[] h263Widths = new int[]{0, 128, 176, 352, 704, 1408, 0, 0};
   private boolean started = false;

   static {
      int[] var0 = new int[]{44100, 48000, 32000, 0};
      MPASampleTbl = new int[][]{{22050, 24000, 16000, 0}, var0};
   }

   public void close() {
      if (this.source != null) {
         label41: {
            boolean var10001;
            try {
               this.source.stop();
            } catch (Exception var5) {
               var10001 = false;
               break label41;
            }

            int var1 = 0;

            while(true) {
               try {
                  if (var1 >= this.tracks.length) {
                     break;
                  }

                  ((RawPushBufferParser.FrameTrack)this.tracks[var1]).stop();
                  ((RawPushBufferParser.FrameTrack)this.tracks[var1]).close();
               } catch (Exception var4) {
                  var10001 = false;
                  break label41;
               }

               ++var1;
            }

            try {
               this.source.disconnect();
            } catch (Exception var3) {
               var10001 = false;
            }
         }

         this.source = null;
      }

      this.started = false;
   }

   public String getName() {
      return "Raw video/audio buffer stream parser";
   }

   public Track[] getTracks() {
      for(int var1 = 0; var1 < this.tracks.length; ++var1) {
         ((RawPushBufferParser.FrameTrack)this.tracks[var1]).parse();
      }

      return this.tracks;
   }

   boolean isRTPFormat(Format var1) {
      return var1 != null && var1.getEncoding() != null && (var1.getEncoding().endsWith("rtp") || var1.getEncoding().endsWith("RTP"));
   }

   public void open() {
      if (this.tracks == null) {
         this.tracks = new Track[this.streams.length];

         for(int var1 = 0; var1 < this.streams.length; ++var1) {
            this.tracks[var1] = new RawPushBufferParser.FrameTrack(this, (PushBufferStream)this.streams[var1], 1);
         }

      }
   }

   public void reset() {
      for(int var1 = 0; var1 < this.tracks.length; ++var1) {
         ((RawPushBufferParser.FrameTrack)this.tracks[var1]).reset();
      }

   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      StringBuilder var2;
      if (var1 instanceof PushBufferDataSource) {
         PushBufferStream[] var3 = ((PushBufferDataSource)var1).getStreams();
         if (var3 != null) {
            if (var3.length != 0) {
               if (this.supports(var3)) {
                  this.source = var1;
                  this.streams = var3;
               } else {
                  var2 = new StringBuilder();
                  var2.append("DataSource not supported: ");
                  var2.append(var1);
                  throw new IncompatibleSourceException(var2.toString());
               }
            } else {
               throw new IOException("Got a empty stream array from the DataSource");
            }
         } else {
            throw new IOException("Got a null stream from the DataSource");
         }
      } else {
         var2 = new StringBuilder();
         var2.append("DataSource not supported: ");
         var2.append(var1);
         throw new IncompatibleSourceException(var2.toString());
      }
   }

   public void start() throws IOException {
      if (!this.started) {
         for(int var1 = 0; var1 < this.tracks.length; ++var1) {
            ((RawPushBufferParser.FrameTrack)this.tracks[var1]).start();
         }

         this.source.start();
         this.started = true;
      }
   }

   public void stop() {
      int var1 = 0;

      while(true) {
         try {
            if (var1 >= this.tracks.length) {
               this.source.stop();
               break;
            }

            ((RawPushBufferParser.FrameTrack)this.tracks[var1]).stop();
         } catch (Exception var3) {
            break;
         }

         ++var1;
      }

      this.started = false;
   }

   protected boolean supports(SourceStream[] var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1[0] != null) {
         var2 = var3;
         if (var1[0] instanceof PushBufferStream) {
            var2 = true;
         }
      }

      return var2;
   }

   class FrameTrack implements Track, BufferTransferHandler {
      CircularBuffer bufferQ;
      boolean checkDepacketizer = false;
      boolean closed = false;
      Depacketizer depacketizer = null;
      boolean enabled = true;
      Format format = null;
      boolean keyFrameFound = false;
      Object keyFrameLock = new Object();
      TrackListener listener;
      Demultiplexer parser;
      PushBufferStream pbs;
      boolean stopped = true;

      public FrameTrack(Demultiplexer var2, PushBufferStream var3, int var4) {
         this.pbs = var3;
         this.format = var3.getFormat();
         if (RawPushBufferParser.this.source instanceof DelegateDataSource || !RawPushBufferParser.this.isRTPFormat(this.format)) {
            this.keyFrameFound = true;
         }

         this.bufferQ = new CircularBuffer(var4);
         var3.setTransferHandler(this);
      }

      private Depacketizer findDepacketizer(String var1, Format var2) {
         try {
            Object var5 = BasicPlugIn.getClassForName(var1).newInstance();
            if (!(var5 instanceof Depacketizer)) {
               return null;
            } else {
               Depacketizer var6 = (Depacketizer)var5;
               if (var6.setInputFormat(var2) == null) {
                  return null;
               } else {
                  var6.open();
                  return var6;
               }
            }
         } catch (Exception var3) {
            return null;
         } catch (Error var4) {
            return null;
         }
      }

      private boolean findKeyFrame(Buffer param1) {
         // $FF: Couldn't be decompiled
      }

      public void close() {
         // $FF: Couldn't be decompiled
      }

      public boolean findH261Key(Buffer var1) {
         byte[] var3 = (byte[])((byte[])var1.getData());
         if (var3 == null) {
            return false;
         } else {
            int var2 = var1.getOffset();
            if (var3[var2 + 4] == 0 && var3[var2 + 4 + 1] == 1) {
               if ((var3[var2 + 4 + 2] & 252) != 0) {
                  return false;
               } else {
                  var2 = var3[var2 + 4 + 3] >> 3 & 1;
                  VideoFormat var4 = new VideoFormat("h261/rtp", new Dimension(RawPushBufferParser.this.h261Widths[var2], RawPushBufferParser.this.h261Heights[var2]), ((VideoFormat)this.format).getMaxDataLength(), ((VideoFormat)this.format).getDataType(), ((VideoFormat)this.format).getFrameRate());
                  this.format = var4;
                  var1.setFormat(var4);
                  return true;
               }
            } else {
               return false;
            }
         }
      }

      public boolean findH263Key(Buffer var1) {
         byte[] var4 = (byte[])((byte[])var1.getData());
         if (var4 == null) {
            return false;
         } else {
            int var2 = this.getH263PayloadHeaderLength(var4, var1.getOffset());
            int var3 = var1.getOffset();
            if (var4[var3 + var2] == 0 && var4[var3 + var2 + 1] == 0) {
               if ((var4[var3 + var2 + 2] & 252) != 128) {
                  return false;
               } else {
                  var2 = var4[var3 + var2 + 4] >> 2 & 7;
                  VideoFormat var5 = new VideoFormat("h263/rtp", new Dimension(RawPushBufferParser.this.h263Widths[var2], RawPushBufferParser.this.h263Heights[var2]), ((VideoFormat)this.format).getMaxDataLength(), ((VideoFormat)this.format).getDataType(), ((VideoFormat)this.format).getFrameRate());
                  this.format = var5;
                  var1.setFormat(var5);
                  return true;
               }
            } else {
               return false;
            }
         }
      }

      public boolean findH263_1998Key(Buffer var1) {
         byte[] var6 = (byte[])((byte[])var1.getData());
         if (var6 == null) {
            return false;
         } else {
            int var5 = var1.getOffset();
            int var2 = ((var6[var5] & 1) << 5 | (var6[var5 + 1] & 248) >> 3) + 2;
            int var3 = var2;
            if ((var6[var5] & 2) != 0) {
               var3 = var2 + 1;
            }

            byte var4 = -1;
            if (var3 > 5) {
               if ((var6[var5] & 2) == 2 && (var6[var5 + 3] & 252) == 128) {
                  var2 = var5 + 3;
               } else {
                  var2 = var4;
                  if ((var6[var5 + 2] & 252) == 128) {
                     var2 = var5 + 2;
                  }
               }
            } else {
               var2 = var4;
               if ((var6[var5] & 4) == 4) {
                  var2 = var4;
                  if ((var6[var5 + var3] & 252) == 128) {
                     var2 = var5 + var3;
                  }
               }
            }

            if (var2 < 0) {
               return false;
            } else {
               int var7 = var6[var2 + 2] >> 2 & 7;
               var3 = var7;
               if (var7 == 7) {
                  if ((var6[var2 + 3] >> 1 & 7) != 1) {
                     return false;
                  }

                  var3 = var6[var2 + 3] << 2 & 4 | var6[var2 + 4] >> 6 & 3;
               }

               if (var3 < 0) {
                  return false;
               } else {
                  VideoFormat var8 = new VideoFormat("h263-1998/rtp", new Dimension(RawPushBufferParser.this.h263Widths[var3], RawPushBufferParser.this.h263Heights[var3]), ((VideoFormat)this.format).getMaxDataLength(), ((VideoFormat)this.format).getDataType(), ((VideoFormat)this.format).getFrameRate());
                  this.format = var8;
                  var1.setFormat(var8);
                  return true;
               }
            }
         }
      }

      public boolean findJPEGKey(Buffer var1) {
         if ((var1.getFlags() & 2048) == 0) {
            return false;
         } else {
            byte[] var2 = (byte[])((byte[])var1.getData());
            VideoFormat var3 = new VideoFormat("jpeg/rtp", new Dimension((var2[var1.getOffset() + 6] & 255) * 8, (var2[var1.getOffset() + 7] & 255) * 8), ((VideoFormat)this.format).getMaxDataLength(), ((VideoFormat)this.format).getDataType(), ((VideoFormat)this.format).getFrameRate());
            this.format = var3;
            var1.setFormat(var3);
            return true;
         }
      }

      public boolean findMPAKey(Buffer var1) {
         byte[] var5 = (byte[])((byte[])var1.getData());
         if (var5 == null) {
            return false;
         } else {
            int var2 = var1.getOffset();
            if (var1.getLength() < 8) {
               return false;
            } else if (var5[var2 + 2] == 0) {
               if (var5[var2 + 3] != 0) {
                  return false;
               } else {
                  var2 += 4;
                  if ((var5[var2] & 255) == 255 && (var5[var2 + 1] & 246) > 240 && (var5[var2 + 2] & 240) != 240 && (var5[var2 + 2] & 12) != 12) {
                     if ((var5[var2 + 3] & 3) == 2) {
                        return false;
                     } else {
                        byte var3 = var5[var2 + 1];
                        byte var4 = var5[var2 + 2];
                        byte var6;
                        if ((var5[var2 + 3] >> 6 & 3) == 3) {
                           var6 = 1;
                        } else {
                           var6 = 2;
                        }

                        AudioFormat var7 = new AudioFormat("mpegaudio/rtp", (double)RawPushBufferParser.MPASampleTbl[var3 >> 3 & 1][var4 >> 2 & 3], 16, var6, 0, 1);
                        this.format = var7;
                        var1.setFormat(var7);
                        return true;
                     }
                  } else {
                     return false;
                  }
               }
            } else {
               return false;
            }
         }
      }

      public boolean findMPEGKey(Buffer var1) {
         byte[] var8 = (byte[])((byte[])var1.getData());
         if (var8 == null) {
            return false;
         } else {
            int var7 = var1.getOffset();
            if (var1.getLength() < 12) {
               return false;
            } else if ((var8[var7 + 2] & 32) != 32) {
               return false;
            } else if (var8[var7 + 4] == 0 && var8[var7 + 5] == 0 && var8[var7 + 6] == 1) {
               if ((var8[var7 + 7] & 255) != 179) {
                  return false;
               } else {
                  int var3 = var8[var7 + 11] & 15;
                  if (var3 != 0) {
                     if (var3 > 8) {
                        return false;
                     } else {
                        byte var4 = var8[var7 + 8];
                        byte var5 = var8[var7 + 9];
                        byte var6 = var8[var7 + 9];
                        byte var9 = var8[var7 + 10];
                        float var2 = RawPushBufferParser.this.MPEGRateTbl[var3];
                        VideoFormat var10 = new VideoFormat("mpeg/rtp", new Dimension((var4 & 255) << 4 | (var5 & 240) >> 4, (var6 & 15) << 8 | var9 & 255), ((VideoFormat)this.format).getMaxDataLength(), ((VideoFormat)this.format).getDataType(), var2);
                        this.format = var10;
                        var1.setFormat(var10);
                        return true;
                     }
                  } else {
                     return false;
                  }
               }
            } else {
               return false;
            }
         }
      }

      public Time getDuration() {
         return this.parser.getDuration();
      }

      public Format getFormat() {
         return this.format;
      }

      int getH263PayloadHeaderLength(byte[] var1, int var2) {
         byte var3 = var1[var2];
         if ((var3 & 128) != 0) {
            return (var3 & 64) != 0 ? 12 : 8;
         } else {
            return 4;
         }
      }

      public Time getStartTime() {
         return new Time(0L);
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      public Time mapFrameToTime(int var1) {
         return new Time(0L);
      }

      public int mapTimeToFrame(Time var1) {
         return -1;
      }

      public void parse() {
         // $FF: Couldn't be decompiled
      }

      public void readFrame(Buffer param1) {
         // $FF: Couldn't be decompiled
      }

      public void reset() {
      }

      public void setEnabled(boolean var1) {
         if (var1) {
            this.pbs.setTransferHandler(this);
         } else {
            this.pbs.setTransferHandler((BufferTransferHandler)null);
         }

         this.enabled = var1;
      }

      public void setTrackListener(TrackListener var1) {
         this.listener = var1;
      }

      public void start() {
         CircularBuffer var1 = this.bufferQ;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label193: {
            label192: {
               try {
                  this.stopped = false;
                  if (!(RawPushBufferParser.this.source instanceof CaptureDevice)) {
                     break label192;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label193;
               }

               while(true) {
                  try {
                     if (!this.bufferQ.canRead()) {
                        break;
                     }

                     this.bufferQ.read();
                     this.bufferQ.readReport();
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label193;
                  }
               }
            }

            label182:
            try {
               this.bufferQ.notifyAll();
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label182;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }

      public void stop() {
         // $FF: Couldn't be decompiled
      }

      public void transferData(PushBufferStream param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
