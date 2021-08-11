package net.sf.fmj.media;

import javax.media.Format;

public abstract class BasicConnector implements Connector {
   protected CircularBuffer circularBuffer = null;
   protected Format format = null;
   protected int minSize = 1;
   protected Module module = null;
   protected String name = null;
   protected int protocol = 0;

   public Object getCircularBuffer() {
      return this.circularBuffer;
   }

   public Format getFormat() {
      return this.format;
   }

   public Module getModule() {
      return this.module;
   }

   public String getName() {
      return this.name;
   }

   public int getProtocol() {
      return this.protocol;
   }

   public int getSize() {
      return this.minSize;
   }

   public void print() {
      this.circularBuffer.print();
   }

   public void reset() {
      this.circularBuffer.reset();
   }

   public void setCircularBuffer(Object var1) {
      this.circularBuffer = (CircularBuffer)var1;
   }

   public void setFormat(Format var1) {
      this.module.setFormat(this, var1);
      this.format = var1;
   }

   public void setModule(Module var1) {
      this.module = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setProtocol(int var1) {
      this.protocol = var1;
   }

   public void setSize(int var1) {
      this.minSize = var1;
   }
}
