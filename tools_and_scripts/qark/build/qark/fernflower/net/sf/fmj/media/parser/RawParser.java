package net.sf.fmj.media.parser;

import javax.media.Demultiplexer;
import javax.media.Duration;
import javax.media.Time;
import javax.media.Track;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.Positionable;
import net.sf.fmj.media.BasicPlugIn;

public abstract class RawParser extends BasicPlugIn implements Demultiplexer {
   static final String NAME = "Raw parser";
   protected DataSource source;
   ContentDescriptor[] supported = new ContentDescriptor[]{new ContentDescriptor("raw")};

   public Object[] getControls() {
      return this.source.getControls();
   }

   public Time getDuration() {
      DataSource var1 = this.source;
      return var1 == null ? Duration.DURATION_UNKNOWN : var1.getDuration();
   }

   public Time getMediaTime() {
      return Time.TIME_UNKNOWN;
   }

   public String getName() {
      return "Raw parser";
   }

   public ContentDescriptor[] getSupportedInputContentDescriptors() {
      return this.supported;
   }

   public Track[] getTracks() {
      return null;
   }

   public boolean isPositionable() {
      return this.source instanceof Positionable;
   }

   public boolean isRandomAccess() {
      DataSource var1 = this.source;
      return var1 instanceof Positionable && ((Positionable)var1).isRandomAccess();
   }

   public void reset() {
   }

   public Time setPosition(Time var1, int var2) {
      DataSource var3 = this.source;
      return var3 instanceof Positionable ? ((Positionable)var3).setPosition(var1, var2) : var1;
   }
}
