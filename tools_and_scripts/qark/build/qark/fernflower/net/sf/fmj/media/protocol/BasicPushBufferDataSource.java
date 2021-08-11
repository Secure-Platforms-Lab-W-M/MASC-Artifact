package net.sf.fmj.media.protocol;

import java.io.IOException;
import javax.media.Time;
import javax.media.protocol.PushBufferDataSource;

public abstract class BasicPushBufferDataSource extends PushBufferDataSource {
   protected boolean connected = false;
   protected String contentType = "content/unknown";
   protected Object[] controls = new Object[0];
   protected Time duration;
   protected boolean started = false;

   public BasicPushBufferDataSource() {
      this.duration = DURATION_UNKNOWN;
   }

   public void connect() throws IOException {
      if (!this.connected) {
         this.connected = true;
      }
   }

   public void disconnect() {
      try {
         if (this.started) {
            this.stop();
         }
      } catch (IOException var2) {
      }

      this.connected = false;
   }

   public String getContentType() {
      if (!this.connected) {
         System.err.println("Error: DataSource not connected");
         return null;
      } else {
         return this.contentType;
      }
   }

   public Object getControl(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Object[] getControls() {
      return this.controls;
   }

   public Time getDuration() {
      return this.duration;
   }

   public void start() throws IOException {
      if (this.connected) {
         if (!this.started) {
            this.started = true;
         }
      } else {
         throw new Error("DataSource must be connected before it can be started");
      }
   }

   public void stop() throws IOException {
      if (this.connected) {
         if (this.started) {
            this.started = false;
         }
      }
   }
}
