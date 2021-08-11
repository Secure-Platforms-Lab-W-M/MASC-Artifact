package net.sf.fmj.media;

import javax.media.Format;

public interface Connector {
   int ProtocolPush = 0;
   int ProtocolSafe = 1;

   Object getCircularBuffer();

   Format getFormat();

   Module getModule();

   String getName();

   int getProtocol();

   int getSize();

   void reset();

   void setCircularBuffer(Object var1);

   void setFormat(Format var1);

   void setModule(Module var1);

   void setName(String var1);

   void setProtocol(int var1);

   void setSize(int var1);
}
