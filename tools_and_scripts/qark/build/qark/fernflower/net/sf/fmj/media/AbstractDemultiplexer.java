package net.sf.fmj.media;

import java.io.IOException;
import javax.media.BadHeaderException;
import javax.media.Demultiplexer;
import javax.media.IncompatibleSourceException;
import javax.media.Time;
import javax.media.Track;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

public abstract class AbstractDemultiplexer extends AbstractPlugIn implements Demultiplexer {
   public Time getDuration() {
      return DURATION_UNKNOWN;
   }

   public Time getMediaTime() {
      return Time.TIME_UNKNOWN;
   }

   public abstract ContentDescriptor[] getSupportedInputContentDescriptors();

   public abstract Track[] getTracks() throws IOException, BadHeaderException;

   public boolean isPositionable() {
      return false;
   }

   public boolean isRandomAccess() {
      return false;
   }

   public Time setPosition(Time var1, int var2) {
      return Time.TIME_UNKNOWN;
   }

   public abstract void setSource(DataSource var1) throws IOException, IncompatibleSourceException;

   public void start() throws IOException {
   }

   public void stop() {
   }
}
