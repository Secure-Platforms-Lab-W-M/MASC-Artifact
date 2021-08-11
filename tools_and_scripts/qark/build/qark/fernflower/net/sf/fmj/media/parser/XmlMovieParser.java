package net.sf.fmj.media.parser;

import java.io.IOException;
import java.util.logging.Logger;
import javax.media.BadHeaderException;
import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.ResourceUnavailableException;
import javax.media.Time;
import javax.media.Track;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import net.sf.fmj.media.AbstractDemultiplexer;
import net.sf.fmj.media.AbstractTrack;
import net.sf.fmj.utility.LoggerSingleton;
import org.xml.sax.SAXException;

public class XmlMovieParser extends AbstractDemultiplexer {
   private static final Logger logger;
   private PullDataSource source;
   private ContentDescriptor[] supportedInputContentDescriptors = new ContentDescriptor[]{new ContentDescriptor("video.xml")};
   private XmlMovieParser.PullSourceStreamTrack[] tracks;
   private XmlMovieSAXHandler xmlMovieSAXHandler;
   private XmlMovieSAXParserThread xmlMovieSAXParserThread;

   static {
      logger = LoggerSingleton.logger;
   }

   public void close() {
      if (this.tracks != null) {
         int var1 = 0;

         while(true) {
            XmlMovieParser.PullSourceStreamTrack[] var2 = this.tracks;
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

   private class VideoTrack extends XmlMovieParser.PullSourceStreamTrack {
      private final Format format;
      private final int track;

      public VideoTrack(int var2, Format var3) throws ResourceUnavailableException {
         super(null);
         this.track = var2;
         this.format = var3;
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

      public void readFrame(Buffer var1) {
         Buffer var2;
         try {
            var2 = XmlMovieParser.this.xmlMovieSAXHandler.readBuffer(this.track);
         } catch (SAXException var3) {
            throw new RuntimeException(var3);
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         } catch (InterruptedException var5) {
            throw new RuntimeException(var5);
         }

         var1.copy(var2);
      }
   }
}
