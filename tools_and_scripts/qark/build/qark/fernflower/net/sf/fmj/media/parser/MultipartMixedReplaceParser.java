package net.sf.fmj.media.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
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
import javax.media.format.JPEGFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import net.sf.fmj.media.AbstractDemultiplexer;
import net.sf.fmj.media.AbstractTrack;
import net.sf.fmj.media.format.GIFFormat;
import net.sf.fmj.media.format.PNGFormat;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.java.awt.image.ImageObserver;
import org.atalk.android.util.javax.imageio.ImageIO;

public class MultipartMixedReplaceParser extends AbstractDemultiplexer {
   public static final String TIMESTAMP_KEY = "X-FMJ-Timestamp";
   private static final Logger logger;
   private static final String[] supportedFrameContentTypes;
   private PullDataSource source;
   private ContentDescriptor[] supportedInputContentDescriptors = new ContentDescriptor[]{new ContentDescriptor("multipart.x_mixed_replace")};
   private MultipartMixedReplaceParser.PullSourceStreamTrack[] tracks;

   static {
      logger = LoggerSingleton.logger;
      supportedFrameContentTypes = new String[]{"image/jpeg", "image/gif", "image/png"};
   }

   // $FF: synthetic method
   static String access$200(String var0) {
      return toPrintable(var0);
   }

   // $FF: synthetic method
   static boolean access$300(String var0) {
      return isSupportedFrameContentType(var0);
   }

   private static final boolean isSupportedFrameContentType(String var0) {
      String[] var3 = supportedFrameContentTypes;
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         if (var3[var1].equals(var0.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   private static String toPrintable(String var0) {
      return toPrintable(var0, 32);
   }

   private static String toPrintable(String var0, int var1) {
      StringBuilder var4 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length() && var3 < var1; ++var3) {
         char var2 = var0.charAt(var3);
         if (var2 >= ' ' && var2 <= '~') {
            var4.append(var2);
         } else {
            var4.append('.');
         }
      }

      return var4.toString();
   }

   public void close() {
      if (this.tracks != null) {
         int var1 = 0;

         while(true) {
            MultipartMixedReplaceParser.PullSourceStreamTrack[] var2 = this.tracks;
            if (var1 >= var2.length) {
               this.tracks = null;
               break;
            }

            if (var2[var1] != null) {
               var2[var1].deallocate();
               this.tracks[var1] = null;
            }

            ++var1;
         }
      }

      super.close();
   }

   public ContentDescriptor[] getSupportedInputContentDescriptors() {
      return this.supportedInputContentDescriptors;
   }

   public Track[] getTracks() throws IOException, BadHeaderException {
      return this.tracks;
   }

   public boolean isPositionable() {
      return false;
   }

   public boolean isRandomAccess() {
      return super.isRandomAccess();
   }

   public void open() throws ResourceUnavailableException {
      // $FF: Couldn't be decompiled
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      var1.getLocator().getProtocol();
      if (var1 instanceof PullDataSource) {
         this.source = (PullDataSource)var1;
      } else {
         throw new IncompatibleSourceException();
      }
   }

   public void start() throws IOException {
   }

   private abstract class PullSourceStreamTrack extends AbstractTrack {
      private PullSourceStreamTrack() {
      }

      // $FF: synthetic method
      PullSourceStreamTrack(Object var2) {
         this();
      }

      public abstract void deallocate();
   }

   private class VideoTrack extends MultipartMixedReplaceParser.PullSourceStreamTrack {
      private static final int MAX_LINE_LENGTH = 255;
      private final int MAX_IMAGE_SIZE = 1000000;
      private String boundary;
      private final VideoFormat format;
      private String frameContentType;
      private int framesRead;
      private byte[] pushbackBuffer;
      private int pushbackBufferLen;
      private int pushbackBufferOffset;
      private final PullSourceStream stream;

      public VideoTrack(PullSourceStream var2) throws ResourceUnavailableException {
         super(null);
         this.stream = var2;
         Buffer var6 = new Buffer();
         this.readFrame(var6);
         if (!var6.isDiscard() && !var6.isEOM()) {
            BufferedImage var7;
            try {
               var7 = ImageIO.read(new ByteArrayInputStream((byte[])((byte[])var6.getData()), var6.getOffset(), var6.getLength()));
            } catch (IOException var5) {
               Logger var9 = MultipartMixedReplaceParser.logger;
               Level var3 = Level.WARNING;
               StringBuilder var4 = new StringBuilder();
               var4.append("");
               var4.append(var5);
               var9.log(var3, var4.toString(), var5);
               StringBuilder var10 = new StringBuilder();
               var10.append("Error reading image: ");
               var10.append(var5);
               throw new ResourceUnavailableException(var10.toString());
            }

            if (var7 != null) {
               if (this.frameContentType.equals("image/jpeg")) {
                  this.format = new JPEGFormat(new Dimension(var7.getWidth((ImageObserver)null), var7.getHeight((ImageObserver)null)), -1, Format.byteArray, -1.0F, -1, -1);
               } else if (this.frameContentType.equals("image/gif")) {
                  this.format = new GIFFormat(new Dimension(var7.getWidth((ImageObserver)null), var7.getHeight((ImageObserver)null)), -1, Format.byteArray, -1.0F);
               } else if (this.frameContentType.equals("image/png")) {
                  this.format = new PNGFormat(new Dimension(var7.getWidth((ImageObserver)null), var7.getHeight((ImageObserver)null)), -1, Format.byteArray, -1.0F);
               } else {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("Unsupported frame content type: ");
                  var8.append(this.frameContentType);
                  throw new ResourceUnavailableException(var8.toString());
               }
            } else {
               MultipartMixedReplaceParser.logger.log(Level.WARNING, "Failed to read image (ImageIO.read returned null).");
               throw new ResourceUnavailableException();
            }
         } else {
            throw new ResourceUnavailableException("Unable to read first frame");
         }
      }

      private int eatUntil(String var1) throws IOException {
         int var4 = 0;
         byte[] var6 = var1.getBytes();
         byte[] var5 = new byte[var6.length];

         int var2;
         for(int var3 = 0; this.read(var5, var3, 1) >= 0; var3 = var2) {
            ++var4;
            if (var5[var3] == var6[var3]) {
               if (var3 == var6.length - 1) {
                  this.pushback(var5, var3 + 1);
                  return var4 - (var3 + 1);
               }

               var2 = var3 + 1;
            } else {
               var2 = var3;
               if (var3 > 0) {
                  var2 = 0;
               }
            }
         }

         return var4 * -1 - 1;
      }

      private boolean parseProperty(String var1, Properties var2) {
         int var3 = var1.indexOf(58);
         if (var3 < 0) {
            return false;
         } else {
            String var4 = var1.substring(0, var3).trim();
            var1 = var1.substring(var3 + 1).trim();
            var2.setProperty(var4.toUpperCase(), var1);
            return true;
         }
      }

      private void pushback(byte[] var1, int var2) {
         int var3 = this.pushbackBufferLen;
         if (var3 == 0) {
            this.pushbackBuffer = var1;
            this.pushbackBufferLen = var2;
            this.pushbackBufferOffset = 0;
         } else {
            byte[] var4 = new byte[var3 + var2];
            System.arraycopy(this.pushbackBuffer, 0, var4, 0, var3);
            System.arraycopy(var1, 0, var4, this.pushbackBufferLen, var2);
            this.pushbackBuffer = var4;
            this.pushbackBufferLen += var2;
            this.pushbackBufferOffset = 0;
         }
      }

      private int read(byte[] var1, int var2, int var3) throws IOException {
         int var5 = this.pushbackBufferLen;
         if (var5 > 0) {
            int var4 = var5;
            if (var3 < var5) {
               var4 = var3;
            }

            System.arraycopy(this.pushbackBuffer, this.pushbackBufferOffset, var1, var2, var4);
            this.pushbackBufferLen -= var4;
            this.pushbackBufferOffset += var4;
            return var4;
         } else {
            return this.stream.read(var1, var2, var3);
         }
      }

      private byte[] readFully(int var1) throws IOException {
         byte[] var4 = new byte[var1];
         byte var3 = 0;
         int var2 = var1;
         var1 = var3;

         while(true) {
            int var5 = this.read(var4, var1, var2);
            if (var5 < 0) {
               return null;
            }

            if (var5 == var2) {
               return var4;
            }

            var2 -= var5;
            var1 += var5;
         }
      }

      private String readLine(int var1) throws IOException {
         byte[] var3 = new byte[var1];

         for(int var2 = 0; var2 < var1; ++var2) {
            if (this.read(var3, var2, 1) < 0) {
               return null;
            }

            if (var3[var2] == 10) {
               var1 = var2;
               if (var2 > 0) {
                  var1 = var2;
                  if (var3[var2 - 1] == 13) {
                     var1 = var2 - 1;
                  }
               }

               return new String(var3, 0, var1);
            }
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("No newline found in ");
         var4.append(var1);
         var4.append(" bytes");
         throw new MultipartMixedReplaceParser.VideoTrack.MaxLengthExceededException(var4.toString());
      }

      private byte[] readUntil(String var1) throws IOException {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         byte[] var5 = var1.getBytes();
         byte[] var4 = new byte[var5.length];
         int var2 = 0;

         while(var3.size() < 1000000) {
            if (this.read(var4, var2, 1) < 0) {
               return null;
            }

            if (var4[var2] == var5[var2]) {
               if (var2 == var5.length - 1) {
                  this.pushback(var4, var2 + 1);
                  return var3.toByteArray();
               }

               ++var2;
            } else if (var2 > 0) {
               var3.write(var4, 0, var2 + 1);
               var2 = 0;
            } else {
               var3.write(var4, 0, 1);
            }
         }

         throw new IOException("No boundary found in 1000000 bytes.");
      }

      public void deallocate() {
      }

      public Time getDuration() {
         return Duration.DURATION_UNKNOWN;
      }

      public Format getFormat() {
         return this.format;
      }

      public Time mapFrameToTime(int var1) {
         return TIME_UNKNOWN;
      }

      public int mapTimeToFrame(Time var1) {
         return Integer.MAX_VALUE;
      }

      public void readFrame(Buffer param1) {
         // $FF: Couldn't be decompiled
      }

      private class MaxLengthExceededException extends IOException {
         public MaxLengthExceededException(String var2) {
            super(var2);
         }
      }
   }
}
