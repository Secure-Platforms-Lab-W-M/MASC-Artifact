package javax.media.rtp;

import java.io.IOException;
import javax.media.Time;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;

@Deprecated
public class RTPPushDataSource extends PushDataSource {
   public RTPPushDataSource() {
      throw new UnsupportedOperationException();
   }

   public void connect() throws IOException {
      throw new UnsupportedOperationException();
   }

   public void disconnect() {
      throw new UnsupportedOperationException();
   }

   public String getContentType() {
      throw new UnsupportedOperationException();
   }

   public Object getControl(String var1) {
      throw new UnsupportedOperationException();
   }

   public Object[] getControls() {
      throw new UnsupportedOperationException();
   }

   public Time getDuration() {
      throw new UnsupportedOperationException();
   }

   public OutputDataStream getInputStream() {
      throw new UnsupportedOperationException();
   }

   public PushSourceStream getOutputStream() {
      throw new UnsupportedOperationException();
   }

   public PushSourceStream[] getStreams() {
      throw new UnsupportedOperationException();
   }

   protected void initCheck() {
      throw new UnsupportedOperationException();
   }

   public boolean isStarted() {
      throw new UnsupportedOperationException();
   }

   public void setChild(DataSource var1) {
      throw new UnsupportedOperationException();
   }

   public void setContentType(String var1) {
      throw new UnsupportedOperationException();
   }

   public void setInputStream(OutputDataStream var1) {
      throw new UnsupportedOperationException();
   }

   public void setOutputStream(PushSourceStream var1) {
      throw new UnsupportedOperationException();
   }

   public void start() throws IOException {
      throw new UnsupportedOperationException();
   }

   public void stop() throws IOException {
      throw new UnsupportedOperationException();
   }
}
