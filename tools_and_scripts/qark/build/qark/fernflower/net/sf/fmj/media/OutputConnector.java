package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Format;

public interface OutputConnector extends Connector {
   Format canConnectTo(InputConnector var1, Format var2);

   Format connectTo(InputConnector var1, Format var2);

   Buffer getEmptyBuffer();

   InputConnector getInputConnector();

   boolean isEmptyBufferAvailable();

   void writeReport();
}
