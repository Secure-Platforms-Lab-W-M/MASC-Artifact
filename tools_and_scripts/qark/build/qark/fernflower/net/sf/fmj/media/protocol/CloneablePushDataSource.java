package net.sf.fmj.media.protocol;

import java.io.IOException;
import javax.media.Time;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceCloneable;

public class CloneablePushDataSource extends PushDataSource implements SourceCloneable {
   private SuperCloneableDataSource superClass;

   public CloneablePushDataSource(PushDataSource var1) {
      this.superClass = new SuperCloneableDataSource(var1);
   }

   public void connect() throws IOException {
      this.superClass.connect();
   }

   public DataSource createClone() {
      return this.superClass.createClone();
   }

   public void disconnect() {
      this.superClass.disconnect();
   }

   public String getContentType() {
      return this.superClass.getContentType();
   }

   public Object getControl(String var1) {
      return this.superClass.getControl(var1);
   }

   public Object[] getControls() {
      return this.superClass.getControls();
   }

   public Time getDuration() {
      return this.superClass.getDuration();
   }

   public PushSourceStream[] getStreams() {
      if (this.superClass.streams == null) {
         SuperCloneableDataSource var2 = this.superClass;
         var2.streams = new PushSourceStream[var2.streamsAdapters.length];

         for(int var1 = 0; var1 < this.superClass.streamsAdapters.length; ++var1) {
            this.superClass.streams[var1] = this.superClass.streamsAdapters[var1].getAdapter();
         }
      }

      return (PushSourceStream[])((PushSourceStream[])this.superClass.streams);
   }

   public void start() throws IOException {
      this.superClass.start();
   }

   public void stop() throws IOException {
      this.superClass.stop();
   }
}
