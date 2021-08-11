package net.sf.fmj.media.protocol;

import java.io.IOException;
import java.util.Vector;
import javax.media.Time;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceStream;

class SuperCloneableDataSource extends DataSource {
   private Vector clones = new Vector();
   protected DataSource input;
   public SourceStream[] streams = null;
   public CloneableSourceStreamAdapter[] streamsAdapters;

   SuperCloneableDataSource(DataSource var1) {
      this.input = var1;
      Object var3 = null;
      if (var1 instanceof PullDataSource) {
         var3 = ((PullDataSource)var1).getStreams();
      }

      if (var1 instanceof PushDataSource) {
         var3 = ((PushDataSource)var1).getStreams();
      }

      if (var1 instanceof PullBufferDataSource) {
         var3 = ((PullBufferDataSource)var1).getStreams();
      }

      if (var1 instanceof PushBufferDataSource) {
         var3 = ((PushBufferDataSource)var1).getStreams();
      }

      this.streamsAdapters = new CloneableSourceStreamAdapter[((Object[])var3).length];

      for(int var2 = 0; var2 < ((Object[])var3).length; ++var2) {
         this.streamsAdapters[var2] = new CloneableSourceStreamAdapter((SourceStream)((Object[])var3)[var2]);
      }

   }

   public void connect() throws IOException {
      this.input.connect();
   }

   DataSource createClone() {
      DataSource var1 = this.input;
      Object var3;
      if (!(var1 instanceof PullDataSource) && !(var1 instanceof PushDataSource)) {
         var3 = new SuperCloneableDataSource.PushBufferDataSourceSlave();
      } else {
         var3 = new SuperCloneableDataSource.PushDataSourceSlave();
      }

      this.clones.addElement(var3);

      try {
         ((DataSource)var3).connect();
         return (DataSource)var3;
      } catch (IOException var2) {
         return null;
      }
   }

   public void disconnect() {
      this.input.disconnect();
   }

   public String getContentType() {
      return this.input.getContentType();
   }

   public Object getControl(String var1) {
      return this.input.getControl(var1);
   }

   public Object[] getControls() {
      return this.input.getControls();
   }

   public Time getDuration() {
      return this.input.getDuration();
   }

   public void start() throws IOException {
      this.input.start();
   }

   public void stop() throws IOException {
      this.input.stop();
   }

   class PushBufferDataSourceSlave extends PushBufferDataSource {
      PushBufferStream[] streams = null;

      public PushBufferDataSourceSlave() {
         this.streams = new PushBufferStream[SuperCloneableDataSource.this.streamsAdapters.length];
         int var2 = 0;

         while(true) {
            PushBufferStream[] var3 = this.streams;
            if (var2 >= var3.length) {
               return;
            }

            var3[var2] = (PushBufferStream)SuperCloneableDataSource.this.streamsAdapters[var2].createSlave();
            ++var2;
         }
      }

      public void connect() throws IOException {
         int var1 = 0;

         while(true) {
            PushBufferStream[] var2 = this.streams;
            if (var1 >= var2.length) {
               return;
            }

            ((SourceStreamSlave)var2[var1]).connect();
            ++var1;
         }
      }

      public void disconnect() {
         int var1 = 0;

         while(true) {
            PushBufferStream[] var2 = this.streams;
            if (var1 >= var2.length) {
               return;
            }

            ((SourceStreamSlave)var2[var1]).disconnect();
            ++var1;
         }
      }

      public String getContentType() {
         return SuperCloneableDataSource.this.input.getContentType();
      }

      public Object getControl(String var1) {
         return SuperCloneableDataSource.this.input.getControl(var1);
      }

      public Object[] getControls() {
         return SuperCloneableDataSource.this.input.getControls();
      }

      public Time getDuration() {
         return SuperCloneableDataSource.this.input.getDuration();
      }

      public PushBufferStream[] getStreams() {
         return this.streams;
      }

      public void start() throws IOException {
      }

      public void stop() throws IOException {
      }
   }

   class PushDataSourceSlave extends PushDataSource {
      PushSourceStream[] streams = null;

      public PushDataSourceSlave() {
         this.streams = new PushSourceStream[SuperCloneableDataSource.this.streamsAdapters.length];
         int var2 = 0;

         while(true) {
            PushSourceStream[] var3 = this.streams;
            if (var2 >= var3.length) {
               return;
            }

            var3[var2] = (PushSourceStream)SuperCloneableDataSource.this.streamsAdapters[var2].createSlave();
            ++var2;
         }
      }

      public void connect() throws IOException {
         int var1 = 0;

         while(true) {
            PushSourceStream[] var2 = this.streams;
            if (var1 >= var2.length) {
               return;
            }

            ((SourceStreamSlave)var2[var1]).connect();
            ++var1;
         }
      }

      public void disconnect() {
         int var1 = 0;

         while(true) {
            PushSourceStream[] var2 = this.streams;
            if (var1 >= var2.length) {
               return;
            }

            ((SourceStreamSlave)var2[var1]).disconnect();
            ++var1;
         }
      }

      public String getContentType() {
         return SuperCloneableDataSource.this.input.getContentType();
      }

      public Object getControl(String var1) {
         return SuperCloneableDataSource.this.input.getControl(var1);
      }

      public Object[] getControls() {
         return SuperCloneableDataSource.this.input.getControls();
      }

      public Time getDuration() {
         return SuperCloneableDataSource.this.input.getDuration();
      }

      public PushSourceStream[] getStreams() {
         return this.streams;
      }

      public void start() throws IOException {
      }

      public void stop() throws IOException {
      }
   }
}
