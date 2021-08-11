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
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;
import javax.media.protocol.SourceStream;

public class RawPullBufferParser extends RawPullStreamParser {
   static final String NAME = "Raw pull stream parser";

   public String getName() {
      return "Raw pull stream parser";
   }

   public void open() {
      if (this.tracks == null) {
         this.tracks = new Track[this.streams.length];

         for(int var1 = 0; var1 < this.streams.length; ++var1) {
            this.tracks[var1] = new RawPullBufferParser.FrameTrack(this, (PullBufferStream)this.streams[var1]);
         }

      }
   }

   public void setSource(DataSource var1) throws IncompatibleSourceException, IOException {
      StringBuilder var2;
      if (var1 instanceof PullBufferDataSource) {
         PullBufferStream[] var3 = ((PullBufferDataSource)var1).getStreams();
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

   protected boolean supports(SourceStream[] var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1[0] != null) {
         var2 = var3;
         if (var1[0] instanceof PullBufferStream) {
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
      PullBufferStream pbs;
      Integer stateReq = new Integer(0);

      public FrameTrack(Demultiplexer var2, PullBufferStream var3) {
         this.pbs = var3;
         this.format = var3.getFormat();
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
         if (var1.getData() == null) {
            var1.setData(new byte[500]);
         }

         try {
            this.pbs.read(var1);
         } catch (IOException var3) {
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
