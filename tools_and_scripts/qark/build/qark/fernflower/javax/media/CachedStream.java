package javax.media;

public interface CachedStream {
   void abortRead();

   boolean getEnabledBuffering();

   void setEnabledBuffering(boolean var1);

   boolean willReadBytesBlock(int var1);

   boolean willReadBytesBlock(long var1, int var3);
}
