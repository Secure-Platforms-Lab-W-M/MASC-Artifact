package javax.media.protocol;

import java.io.IOException;
import javax.media.Duration;
import javax.media.MediaLocator;
import javax.media.Time;

public abstract class DataSource implements Controls, Duration {
   private MediaLocator locator;

   public DataSource() {
   }

   public DataSource(MediaLocator var1) {
      this.locator = var1;
   }

   public abstract void connect() throws IOException;

   public abstract void disconnect();

   public abstract String getContentType();

   public abstract Object getControl(String var1);

   public abstract Object[] getControls();

   public abstract Time getDuration();

   public MediaLocator getLocator() {
      return this.locator;
   }

   protected void initCheck() {
      if (this.locator == null) {
         throw new Error("Uninitialized DataSource error.");
      }
   }

   public void setLocator(MediaLocator var1) {
      this.locator = var1;
   }

   public abstract void start() throws IOException;

   public abstract void stop() throws IOException;
}
