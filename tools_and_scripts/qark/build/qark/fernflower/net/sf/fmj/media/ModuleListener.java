package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Format;

public interface ModuleListener {
   void bufferPrefetched(Module var1);

   void dataBlocked(Module var1, boolean var2);

   void formatChanged(Module var1, Format var2, Format var3);

   void formatChangedFailure(Module var1, Format var2, Format var3);

   void framesBehind(Module var1, float var2, InputConnector var3);

   void internalErrorOccurred(Module var1);

   void markedDataArrived(Module var1, Buffer var2);

   void mediaEnded(Module var1);

   void pluginTerminated(Module var1);

   void resetted(Module var1);

   void stopAtTime(Module var1);
}
