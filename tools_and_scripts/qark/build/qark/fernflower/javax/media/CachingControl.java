package javax.media;

import org.atalk.android.util.java.awt.Component;

public interface CachingControl extends Control {
   long LENGTH_UNKNOWN = Long.MAX_VALUE;

   long getContentLength();

   long getContentProgress();

   Component getControlComponent();

   Component getProgressBarComponent();

   boolean isDownloading();
}
