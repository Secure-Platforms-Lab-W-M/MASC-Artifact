package net.sf.fmj.media.multiplexer;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import org.atalk.android.util.java.awt.Dimension;

public class RawMux extends AbstractStreamCopyMux {
   public RawMux() {
      super(new ContentDescriptor("raw"));
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat((String)null, -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray), new VideoFormat((String)null, (Dimension)null, -1, Format.byteArray, -1.0F)};
   }
}
