package net.sf.fmj.media.parser;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Demultiplexer;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceStream;

public class RawPullStreamParser extends RawParser {
   static final String NAME = "Raw pull stream parser";
   protected SourceStream[] streams;
   protected Track[] tracks = null;

   public void close() {
      if (this.source != null) {
         try {
            this.source.stop();
            this.source.disconnect();
         } catch (IOException var2) {
         }

         this.source = null;
      }

   }

   public String getName() {
      return "Raw pull stream parser";
   }

   public Track[] getTracks() {
      return this.tracks;
   }

   public void open() {
      if (this.tracks == null) {
         this.tracks = new Track[this.streams.length];
         int var1 = 0;

         while(true) {
            SourceStream[] var2 = this.streams;
            if (var1 >= var2.length) {
               return;
            }

            this.tracks[var1] = new RawPullStreamParser.FrameTrack(this, (PullSourceStream)var2[var1]);
            ++var1;
         }
      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      StringBuilder var2;
      if (var1 instanceof PullDataSource) {
         PullSourceStream[] var3 = ((PullDataSource)var1).getStreams();
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
      this.source.start();
   }

   public void stop() {
      try {
         this.source.stop();
      } catch (IOException var2) {
      }
   }

   protected boolean supports(SourceStream[] var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1[0] != null) {
         var2 = var3;
         if (var1[0] instanceof PullSourceStream) {
            var2 = true;
         }
      }

      return var2;
   }

   class FrameTrack implements Track {
      boolean enabled = true;
      Format format = null;
      TrackListener listener;
      Demultiplexer parser;
      PullSourceStream pss;
      Integer stateReq = new Integer(0);

      public FrameTrack(Demultiplexer var2, PullSourceStream var3) {
         this.pss = var3;
      }

      public Time getDuration() {
         return this.parser.getDuration();
      }

      public Format getFormat() {
         return this.format;
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

      public void readFrame(Buffer var1) {
         byte[] var3 = (byte[])((byte[])var1.getData());
         byte[] var2 = var3;
         if (var3 == null) {
            var2 = new byte[500];
            var1.setData(var2);
         }

         try {
            var1.setLength(this.pss.read(var2, 0, var2.length));
         } catch (IOException var4) {
            var1.setDiscard(true);
         }
      }

      public void setEnabled(boolean var1) {
         this.enabled = var1;
      }

      public void setTrackListener(TrackListener var1) {
         this.listener = var1;
      }
   }
}
