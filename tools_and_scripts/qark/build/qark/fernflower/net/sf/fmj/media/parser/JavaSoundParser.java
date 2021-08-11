package net.sf.fmj.media.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.BadHeaderException;
import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.ResourceUnavailableException;
import javax.media.Time;
import javax.media.Track;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceCloneable;
import net.sf.fmj.media.AbstractDemultiplexer;
import net.sf.fmj.media.AbstractTrack;
import net.sf.fmj.media.PullSourceStreamInputStream;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.javax.sound.sampled.AudioInputStream;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.UnsupportedAudioFileException;

public class JavaSoundParser extends AbstractDemultiplexer {
   private static final boolean OPEN_IN_SET_SOURCE = true;
   private static final Logger logger;
   private PullDataSource sourceForFormat;
   private PullDataSource sourceForReadFrame;
   private ContentDescriptor[] supportedInputContentDescriptors = new ContentDescriptor[]{new ContentDescriptor("audio.x_wav"), new ContentDescriptor("audio.basic"), new ContentDescriptor("audio.x_aiff"), new ContentDescriptor("audio.mpeg"), new ContentDescriptor("audio.ogg"), new ContentDescriptor("application.ogg")};
   private JavaSoundParser.PullSourceStreamTrack[] tracks;

   static {
      logger = LoggerSingleton.logger;
   }

   private void doOpen() throws IOException, UnsupportedAudioFileException {
      PullDataSource var2 = (PullDataSource)((SourceCloneable)this.sourceForFormat).createClone();
      this.sourceForReadFrame = var2;
      if (var2 == null) {
         throw new IOException("Could not create clone");
      } else {
         var2.start();
         this.sourceForFormat.start();
         PullSourceStream[] var4 = this.sourceForFormat.getStreams();
         PullSourceStream[] var3 = this.sourceForReadFrame.getStreams();
         this.tracks = new JavaSoundParser.PullSourceStreamTrack[var4.length];

         for(int var1 = 0; var1 < var4.length; ++var1) {
            this.tracks[var1] = new JavaSoundParser.PullSourceStreamTrack(var4[var1], var3[var1]);
         }

      }
   }

   private static InputStream markSupportedInputStream(InputStream var0) {
      return (InputStream)(var0.markSupported() ? var0 : new BufferedInputStream(var0));
   }

   private static final double nanosToSeconds(double var0) {
      return var0 / 1.0E9D;
   }

   private static final double secondsToNanos(double var0) {
      return 1.0E9D * var0;
   }

   public ContentDescriptor[] getSupportedInputContentDescriptors() {
      return this.supportedInputContentDescriptors;
   }

   public Track[] getTracks() throws IOException, BadHeaderException {
      return this.tracks;
   }

   public boolean isPositionable() {
      return true;
   }

   public boolean isRandomAccess() {
      return super.isRandomAccess();
   }

   public void open() throws ResourceUnavailableException {
   }

   public Time setPosition(Time var1, int var2) {
      int var3 = 0;

      while(true) {
         JavaSoundParser.PullSourceStreamTrack[] var5 = this.tracks;
         if (var3 >= var5.length) {
            if (var1.getNanoseconds() == 0L) {
               boolean var4 = true;
               var2 = 0;

               boolean var18;
               while(true) {
                  var5 = this.tracks;
                  var18 = var4;
                  if (var2 >= var5.length) {
                     break;
                  }

                  if (var5[var2].getTotalBytesRead() != 0L) {
                     var18 = false;
                     break;
                  }

                  ++var2;
               }

               if (var18) {
                  return var1;
               }
            }

            Level var6;
            StringBuilder var7;
            Logger var20;
            IOException var21;
            label71: {
               UnsupportedAudioFileException var10000;
               label70: {
                  boolean var10001;
                  PullDataSource var19;
                  try {
                     logger.fine("JavaSoundParser: cloning, reconnecting, and restarting source");
                     var19 = (PullDataSource)((SourceCloneable)this.sourceForFormat).createClone();
                     this.sourceForReadFrame = var19;
                  } catch (IOException var14) {
                     var21 = var14;
                     var10001 = false;
                     break label71;
                  } catch (UnsupportedAudioFileException var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label70;
                  }

                  if (var19 != null) {
                     label93: {
                        try {
                           var19.start();
                        } catch (IOException var10) {
                           var21 = var10;
                           var10001 = false;
                           break label71;
                        } catch (UnsupportedAudioFileException var11) {
                           var10000 = var11;
                           var10001 = false;
                           break label93;
                        }

                        var2 = 0;

                        while(true) {
                           try {
                              if (var2 >= this.tracks.length) {
                                 return var1;
                              }

                              this.tracks[var2].setPssForReadFrame(this.sourceForReadFrame.getStreams()[var2]);
                              if (var1.getNanoseconds() > 0L) {
                                 this.tracks[var2].skipNanos(var1.getNanoseconds());
                              }
                           } catch (IOException var8) {
                              var21 = var8;
                              var10001 = false;
                              break label71;
                           } catch (UnsupportedAudioFileException var9) {
                              var10000 = var9;
                              var10001 = false;
                              break;
                           }

                           ++var2;
                        }
                     }
                  } else {
                     try {
                        throw new RuntimeException("Could not create clone");
                     } catch (IOException var12) {
                        var21 = var12;
                        var10001 = false;
                        break label71;
                     } catch (UnsupportedAudioFileException var13) {
                        var10000 = var13;
                        var10001 = false;
                     }
                  }
               }

               UnsupportedAudioFileException var16 = var10000;
               var20 = logger;
               var6 = Level.WARNING;
               var7 = new StringBuilder();
               var7.append("");
               var7.append(var16);
               var20.log(var6, var7.toString(), var16);
               throw new RuntimeException(var16);
            }

            IOException var17 = var21;
            var20 = logger;
            var6 = Level.WARNING;
            var7 = new StringBuilder();
            var7.append("");
            var7.append(var17);
            var20.log(var6, var7.toString(), var17);
            throw new RuntimeException(var17);
         }

         if (!var5[var3].canSkipNanos()) {
            return super.setPosition(var1, var2);
         }

         ++var3;
      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      if (var1 instanceof PullDataSource) {
         if (var1 instanceof SourceCloneable) {
            this.sourceForFormat = (PullDataSource)var1;

            Logger var2;
            Level var3;
            StringBuilder var4;
            try {
               this.doOpen();
            } catch (UnsupportedAudioFileException var5) {
               var2 = logger;
               var3 = Level.INFO;
               var4 = new StringBuilder();
               var4.append("");
               var4.append(var5);
               var2.log(var3, var4.toString());
               StringBuilder var7 = new StringBuilder();
               var7.append("");
               var7.append(var5);
               throw new IncompatibleSourceException(var7.toString());
            } catch (IOException var6) {
               var2 = logger;
               var3 = Level.WARNING;
               var4 = new StringBuilder();
               var4.append("");
               var4.append(var6);
               var2.log(var3, var4.toString(), var6);
               throw var6;
            }
         } else {
            throw new IncompatibleSourceException();
         }
      } else {
         throw new IncompatibleSourceException();
      }
   }

   public void start() throws IOException {
   }

   private class PullSourceStreamTrack extends AbstractTrack {
      private AudioInputStream aisForReadFrame;
      private final AudioFormat format;
      private final long frameLength;
      private final org.atalk.android.util.javax.sound.sampled.AudioFormat javaSoundInputFormat;
      private PullSourceStream pssForReadFrame;
      private PullSourceStreamInputStream pssisForReadFrame;
      private long totalBytesRead = 0L;

      public PullSourceStreamTrack(PullSourceStream var2, PullSourceStream var3) throws UnsupportedAudioFileException, IOException {
         PullSourceStreamInputStream var6 = new PullSourceStreamInputStream(var2);
         AudioInputStream var7 = AudioSystem.getAudioInputStream(JavaSoundParser.markSupportedInputStream(var6));
         this.javaSoundInputFormat = var7.getFormat();
         this.frameLength = var7.getFrameLength();
         this.format = JavaSoundUtils.convertFormat(this.javaSoundInputFormat);
         Logger var4 = JavaSoundParser.logger;
         StringBuilder var5 = new StringBuilder();
         var5.append("JavaSoundParser: java sound format: ");
         var5.append(this.javaSoundInputFormat);
         var4.fine(var5.toString());
         var4 = JavaSoundParser.logger;
         var5 = new StringBuilder();
         var5.append("JavaSoundParser: jmf format: ");
         var5.append(this.format);
         var4.fine(var5.toString());
         var4 = JavaSoundParser.logger;
         var5 = new StringBuilder();
         var5.append("JavaSoundParser: Frame length=");
         var5.append(this.frameLength);
         var4.fine(var5.toString());
         var7.close();
         var6.close();
         this.setPssForReadFrame(var3);
      }

      private long bytesToNanos(long var1) {
         return this.javaSoundInputFormat.getFrameSize() > 0 && this.javaSoundInputFormat.getFrameRate() > 0.0F ? (long)JavaSoundParser.secondsToNanos((double)((float)(var1 / (long)this.javaSoundInputFormat.getFrameSize()) / this.javaSoundInputFormat.getFrameRate())) : -1L;
      }

      private long nanosToBytes(long var1) {
         if (this.javaSoundInputFormat.getFrameSize() > 0 && this.javaSoundInputFormat.getFrameRate() > 0.0F) {
            double var3 = JavaSoundParser.nanosToSeconds((double)var1);
            double var5 = (double)this.javaSoundInputFormat.getFrameRate();
            return (long)((double)this.javaSoundInputFormat.getFrameSize() * var5 * var3);
         } else {
            return -1L;
         }
      }

      public boolean canSkipNanos() {
         return this.javaSoundInputFormat.getFrameSize() > 0 && this.javaSoundInputFormat.getFrameRate() > 0.0F;
      }

      public Time getDuration() {
         long var3 = this.frameLength;
         if (var3 < 0L) {
            JavaSoundParser.logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (1)");
            return Duration.DURATION_UNKNOWN;
         } else {
            double var1 = (double)((float)var3 / this.javaSoundInputFormat.getFrameRate());
            if (var1 < 0.0D) {
               JavaSoundParser.logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (2)");
               return Duration.DURATION_UNKNOWN;
            } else {
               var1 = JavaSoundParser.secondsToNanos(var1);
               Logger var5 = JavaSoundParser.logger;
               StringBuilder var6 = new StringBuilder();
               var6.append("PullSourceStreamTrack: returning ");
               var6.append((long)var1);
               var5.fine(var6.toString());
               return new Time((long)var1);
            }
         }
      }

      public Format getFormat() {
         return this.format;
      }

      public long getTotalBytesRead() {
         return this.totalBytesRead;
      }

      public Time mapFrameToTime(int var1) {
         return TIME_UNKNOWN;
      }

      public int mapTimeToFrame(Time var1) {
         return Integer.MAX_VALUE;
      }

      public void readFrame(Buffer var1) {
         if (var1.getData() == null) {
            var1.setData(new byte[10000]);
         }

         byte[] var3 = (byte[])((byte[])var1.getData());

         IOException var10000;
         label41: {
            int var2;
            boolean var10001;
            try {
               var2 = this.aisForReadFrame.read(var3, 0, var3.length);
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label41;
            }

            if (var2 < 0) {
               try {
                  var1.setEOM(true);
                  var1.setLength(0);
                  return;
               } catch (IOException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            } else {
               label37: {
                  try {
                     if (this.javaSoundInputFormat.getFrameSize() > 0 && this.javaSoundInputFormat.getFrameRate() > 0.0F) {
                        var1.setTimeStamp(this.bytesToNanos(this.totalBytesRead));
                        var1.setDuration(this.bytesToNanos((long)var2));
                     }
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label37;
                  }

                  try {
                     this.totalBytesRead += (long)var2;
                     var1.setLength(var2);
                     var1.setOffset(0);
                     return;
                  } catch (IOException var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               }
            }
         }

         IOException var11 = var10000;
         var1.setEOM(true);
         var1.setDiscard(true);
         var1.setLength(0);
         Logger var10 = JavaSoundParser.logger;
         Level var4 = Level.WARNING;
         StringBuilder var5 = new StringBuilder();
         var5.append("");
         var5.append(var11);
         var10.log(var4, var5.toString(), var11);
      }

      public void setPssForReadFrame(PullSourceStream var1) throws UnsupportedAudioFileException, IOException {
         this.pssForReadFrame = var1;
         PullSourceStreamInputStream var2 = new PullSourceStreamInputStream(var1);
         this.pssisForReadFrame = var2;
         this.aisForReadFrame = AudioSystem.getAudioInputStream(JavaSoundParser.markSupportedInputStream(var2));
         this.totalBytesRead = 0L;
      }

      public long skipNanos(long var1) throws IOException {
         long var3 = this.nanosToBytes(var1);
         if (var3 <= 0L) {
            JavaSoundParser.logger.fine("JavaSoundParser: skipping nanos: 0");
            return 0L;
         } else {
            long var5 = this.aisForReadFrame.skip(var3);
            this.totalBytesRead += var5;
            Logger var7;
            StringBuilder var8;
            if (var5 == var3) {
               var7 = JavaSoundParser.logger;
               var8 = new StringBuilder();
               var8.append("JavaSoundParser: skipping nanos: ");
               var8.append(var1);
               var7.fine(var8.toString());
               return var1;
            } else {
               var1 = this.bytesToNanos(var5);
               var7 = JavaSoundParser.logger;
               var8 = new StringBuilder();
               var8.append("JavaSoundParser: skipping nanos: ");
               var8.append(var1);
               var7.fine(var8.toString());
               return var1;
            }
         }
      }
   }
}
