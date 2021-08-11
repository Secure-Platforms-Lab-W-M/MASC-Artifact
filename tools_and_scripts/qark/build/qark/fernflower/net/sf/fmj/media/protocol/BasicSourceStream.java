package net.sf.fmj.media.protocol;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.SourceStream;

public class BasicSourceStream implements SourceStream {
   public static final int LENGTH_DISCARD = -2;
   protected ContentDescriptor contentDescriptor = null;
   protected long contentLength = -1L;
   protected Object[] controls = new Object[0];

   public BasicSourceStream() {
   }

   public BasicSourceStream(ContentDescriptor var1, long var2) {
      this.contentDescriptor = var1;
      this.contentLength = var2;
   }

   public boolean endOfStream() {
      return false;
   }

   public ContentDescriptor getContentDescriptor() {
      return this.contentDescriptor;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public Object getControl(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Object[] getControls() {
      return this.controls;
   }
}
