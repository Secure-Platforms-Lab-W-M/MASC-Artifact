package net.sf.fmj.media.protocol;

public interface RTPSource {
   void flush();

   String getCNAME();

   int getSSRC();

   void prebuffer();

   void setBufferListener(BufferListener var1);
}
