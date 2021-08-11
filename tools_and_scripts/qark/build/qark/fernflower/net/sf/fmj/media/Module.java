package net.sf.fmj.media;

import javax.media.Controls;
import javax.media.Format;

public interface Module extends Controls {
   void connectorPushed(InputConnector var1);

   InputConnector getInputConnector(String var1);

   String[] getInputConnectorNames();

   String getName();

   OutputConnector getOutputConnector(String var1);

   String[] getOutputConnectorNames();

   boolean isInterrupted();

   void registerInputConnector(String var1, InputConnector var2);

   void registerOutputConnector(String var1, OutputConnector var2);

   void reset();

   void setFormat(Connector var1, Format var2);

   void setModuleListener(ModuleListener var1);

   void setName(String var1);
}
