package javax.media.protocol;

public interface SourceStream extends Controls {
   long LENGTH_UNKNOWN = -1L;

   boolean endOfStream();

   ContentDescriptor getContentDescriptor();

   long getContentLength();
}
