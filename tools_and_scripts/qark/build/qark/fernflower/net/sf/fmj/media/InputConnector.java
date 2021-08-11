package net.sf.fmj.media;

import javax.media.Buffer;

public interface InputConnector extends Connector {
   OutputConnector getOutputConnector();

   Buffer getValidBuffer();

   boolean isValidBufferAvailable();

   void readReport();

   void setOutputConnector(OutputConnector var1);
}
