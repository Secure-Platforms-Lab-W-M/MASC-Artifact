package javax.media.control;

import javax.media.Control;

public interface StreamWriterControl extends Control {
   long getStreamSize();

   boolean setStreamSizeLimit(long var1);
}
