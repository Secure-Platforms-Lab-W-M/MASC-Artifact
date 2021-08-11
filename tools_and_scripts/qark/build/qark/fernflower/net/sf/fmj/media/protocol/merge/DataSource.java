package net.sf.fmj.media.protocol.merge;

import java.io.IOException;
import javax.media.Duration;
import javax.media.Time;

public class DataSource extends javax.media.protocol.DataSource {
   public void connect() throws IOException {
   }

   public void disconnect() {
   }

   public String getContentType() {
      return "merge";
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      return new Object[0];
   }

   public Time getDuration() {
      return Duration.DURATION_UNKNOWN;
   }

   public void start() throws IOException {
   }

   public void stop() throws IOException {
   }
}
