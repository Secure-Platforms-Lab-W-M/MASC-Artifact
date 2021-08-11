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
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceStream;
import javax.media.protocol.SourceTransferHandler;
import net.sf.fmj.media.CircularBuffer;

public class RawStreamParser extends RawParser {
   static final String NAME = "Raw stream parser";
   protected SourceStream[] streams;
   protected Track[] tracks = null;

   public void close() {
      if (this.source != null) {
         label41: {
            boolean var10001;
            try {
               this.source.stop();
            } catch (IOException var5) {
               var10001 = false;
               break label41;
            }

            int var1 = 0;

            while(true) {
               try {
                  if (var1 >= this.tracks.length) {
                     break;
                  }

                  ((RawStreamParser.FrameTrack)this.tracks[var1]).stop();
               } catch (IOException var4) {
                  var10001 = false;
                  break label41;
               }

               ++var1;
            }

            try {
               this.source.disconnect();
            } catch (IOException var3) {
               var10001 = false;
            }
         }

         this.source = null;
      }

   }

   public String getName() {
      return "Raw stream parser";
   }

   public Track[] getTracks() {
      return this.tracks;
   }

   public void open() {
      if (this.tracks == null) {
         this.tracks = new Track[this.streams.length];

         for(int var1 = 0; var1 < this.streams.length; ++var1) {
            this.tracks[var1] = new RawStreamParser.FrameTrack(this, (PushSourceStream)this.streams[var1], 5);
         }

      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      StringBuilder var2;
      if (var1 instanceof PushDataSource) {
         PushSourceStream[] var3 = ((PushDataSource)var1).getStreams();
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
      int var1 = 0;

      while(true) {
         Track[] var2 = this.tracks;
         if (var1 >= var2.length) {
            return;
         }

         ((RawStreamParser.FrameTrack)var2[var1]).start();
         ++var1;
      }
   }

   public void stop() {
      // $FF: Couldn't be decompiled
   }

   protected boolean supports(SourceStream[] var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if (var1[0] != null) {
         var2 = var3;
         if (var1[0] instanceof PushSourceStream) {
            var2 = true;
         }
      }

      return var2;
   }

   class FrameTrack implements Track, SourceTransferHandler {
      CircularBuffer bufferQ;
      boolean enabled = true;
      Format format = null;
      TrackListener listener;
      Demultiplexer parser;
      PushSourceStream pss;
      Integer stateReq = new Integer(0);
      boolean stopped = true;

      public FrameTrack(Demultiplexer var2, PushSourceStream var3, int var4) {
         this.pss = var3;
         var3.setTransferHandler(this);
         this.bufferQ = new CircularBuffer(var4);
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

      public void readFrame(Buffer param1) {
         // $FF: Couldn't be decompiled
      }

      public void setEnabled(boolean var1) {
         if (var1) {
            this.pss.setTransferHandler(this);
         } else {
            this.pss.setTransferHandler((SourceTransferHandler)null);
         }

         this.enabled = var1;
      }

      public void setTrackListener(TrackListener var1) {
         this.listener = var1;
      }

      public void start() {
         // $FF: Couldn't be decompiled
      }

      public void stop() {
         // $FF: Couldn't be decompiled
      }

      public void transferData(PushSourceStream param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
