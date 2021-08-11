package net.sf.fmj.filtergraph;

import javax.media.Codec;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.PlugIn;
import javax.media.Renderer;

public interface GraphInspector {
   boolean detailMode();

   boolean verify(Codec var1, Format var2, Format var3);

   boolean verify(Multiplexer var1, Format[] var2);

   boolean verify(Renderer var1, Format var2);

   void verifyInputFailed(PlugIn var1, Format var2);

   void verifyOutputFailed(PlugIn var1, Format var2);
}
