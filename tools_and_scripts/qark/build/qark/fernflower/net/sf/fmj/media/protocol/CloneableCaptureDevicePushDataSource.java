package net.sf.fmj.media.protocol;

import java.io.IOException;
import javax.media.CaptureDeviceInfo;
import javax.media.Time;
import javax.media.control.FormatControl;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceCloneable;

public class CloneableCaptureDevicePushDataSource extends PushDataSource implements SourceCloneable, CaptureDevice {
   private SuperCloneableDataSource superClass;

   public CloneableCaptureDevicePushDataSource(PushDataSource var1) {
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

   public CaptureDeviceInfo getCaptureDeviceInfo() {
      return ((CaptureDevice)this.superClass.input).getCaptureDeviceInfo();
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

   public FormatControl[] getFormatControls() {
      return ((CaptureDevice)this.superClass.input).getFormatControls();
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
