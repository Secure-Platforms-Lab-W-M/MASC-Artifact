package javax.media;

public interface ExtendedCachingControl extends CachingControl {
   void addDownloadProgressListener(DownloadProgressListener var1, int var2);

   Time getBufferSize();

   long getEndOffset();

   long getStartOffset();

   void pauseDownload();

   void removeDownloadProgressListener(DownloadProgressListener var1);

   void resumeDownload();

   void setBufferSize(Time var1);
}
