package net.sf.fmj.media.parser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.BadHeaderException;
import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import net.sf.fmj.media.AbstractDemultiplexer;
import net.sf.fmj.media.AbstractTrack;
import net.sf.fmj.utility.LoggerSingleton;

public class GsmParser extends AbstractDemultiplexer {
   private static double GSM_FRAME_RATE = 50.0D;
   private static final Logger logger;
   private PullDataSource source;
   private ContentDescriptor[] supportedInputContentDescriptors = new ContentDescriptor[]{new ContentDescriptor("audio.x_gsm")};
   private GsmParser.PullSourceStreamTrack[] tracks;

   static {
      logger = LoggerSingleton.logger;
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

   public Time setPosition(Time var1, int var2) {
      return null;
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      if (var1 instanceof PullDataSource) {
         this.source = (PullDataSource)var1;
      } else {
         throw new IncompatibleSourceException();
      }
   }

   public void start() throws IOException {
      this.source.start();
      PullSourceStream[] var2 = this.source.getStreams();
      this.tracks = new GsmParser.PullSourceStreamTrack[var2.length];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         this.tracks[var1] = new GsmParser.PullSourceStreamTrack(var2[var1]);
      }

   }

   public void stop() {
      try {
         this.source.stop();
      } catch (IOException var5) {
         Logger var2 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
      }
   }

   private class PullSourceStreamTrack extends AbstractTrack {
      private static final int GSM_FRAME_SIZE = 33;
      private long frameLength;
      private PullSourceStream stream;

      public PullSourceStreamTrack(PullSourceStream var2) {
         this.stream = var2;
         this.frameLength = var2.getContentLength() / 33L;
      }

      public Time getDuration() {
         long var3 = this.frameLength;
         if (var3 < 0L) {
            GsmParser.logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (1)");
            return Duration.DURATION_UNKNOWN;
         } else {
            double var1 = (double)var3 / GsmParser.GSM_FRAME_RATE;
            if (var1 < 0.0D) {
               GsmParser.logger.fine("PullSourceStreamTrack: returning Duration.DURATION_UNKNOWN (2)");
               return Duration.DURATION_UNKNOWN;
            } else {
               var1 = GsmParser.secondsToNanos(var1);
               Logger var5 = GsmParser.logger;
               StringBuilder var6 = new StringBuilder();
               var6.append("PullSourceStreamTrack: returning ");
               var6.append((long)var1);
               var5.fine(var6.toString());
               return new Time((long)var1);
            }
         }
      }

      public Format getFormat() {
         return new AudioFormat("gsm", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray);
      }

      public void readFrame(Buffer var1) {
         if (var1.getData() == null) {
            var1.setData(new byte[16500]);
         }

         byte[] var3 = (byte[])((byte[])var1.getData());

         IOException var10000;
         label29: {
            int var2;
            boolean var10001;
            try {
               var2 = this.stream.read(var3, 0, var3.length);
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break label29;
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
               try {
                  var1.setLength(var2);
                  var1.setOffset(0);
                  return;
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }
         }

         IOException var10 = var10000;
         var1.setEOM(true);
         var1.setDiscard(true);
         var1.setLength(0);
         Logger var9 = GsmParser.logger;
         Level var4 = Level.WARNING;
         StringBuilder var5 = new StringBuilder();
         var5.append("");
         var5.append(var10);
         var9.log(var4, var5.toString(), var10);
      }
   }
}
