package javax.media.protocol;

public interface Seekable {
   boolean isRandomAccess();

   long seek(long var1);

   long tell();
}
