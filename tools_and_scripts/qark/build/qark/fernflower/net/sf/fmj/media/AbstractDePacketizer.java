package net.sf.fmj.media;

import javax.media.Buffer;

public abstract class AbstractDePacketizer extends AbstractCodec {
   private static final boolean TRACE = false;

   public int process(Buffer var1, Buffer var2) {
      if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         Object var3 = var2.getData();
         var2.setData(var1.getData());
         var1.setData(var3);
         var2.setLength(var1.getLength());
         var2.setFormat(this.outputFormat);
         var2.setOffset(var1.getOffset());
         return 0;
      }
   }
}
