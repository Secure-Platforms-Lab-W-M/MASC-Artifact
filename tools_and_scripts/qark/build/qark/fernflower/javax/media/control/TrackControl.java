package javax.media.control;

import javax.media.Codec;
import javax.media.Controls;
import javax.media.NotConfiguredError;
import javax.media.Renderer;
import javax.media.UnsupportedPlugInException;

public interface TrackControl extends FormatControl, Controls {
   void setCodecChain(Codec[] var1) throws UnsupportedPlugInException, NotConfiguredError;

   void setRenderer(Renderer var1) throws UnsupportedPlugInException, NotConfiguredError;
}
