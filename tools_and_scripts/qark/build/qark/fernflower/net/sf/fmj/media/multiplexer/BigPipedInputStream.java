package net.sf.fmj.media.multiplexer;

import java.io.PipedInputStream;

public class BigPipedInputStream extends PipedInputStream {
   public BigPipedInputStream(int var1) {
      this.buffer = new byte[var1];
   }
}
