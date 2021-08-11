package net.sf.fmj.media.rtp.util;

import java.io.IOException;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceTransferHandler;
import javax.media.rtp.RTPPushDataSource;
import net.sf.fmj.media.CircularBuffer;

public class RTPPacketReceiver implements PacketSource, SourceTransferHandler {
   private static final String PUSH_BUFFER_STREAM_CLASS_NAME = PushBufferStream.class.getName();
   CircularBuffer bufQue = new CircularBuffer(2);
   boolean closed = false;
   boolean dataRead = false;
   RTPPushDataSource rtpsource = null;

   public RTPPacketReceiver(PushSourceStream var1) {
      var1.setTransferHandler(this);
   }

   public RTPPacketReceiver(RTPPushDataSource var1) {
      this.rtpsource = var1;
      var1.getOutputStream().setTransferHandler(this);
   }

   public void closeSource() {
      // $FF: Couldn't be decompiled
   }

   public Packet receiveFrom() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public String sourceString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RTPPacketReceiver for ");
      var1.append(this.rtpsource);
      return var1.toString();
   }

   public void transferData(PushSourceStream param1) {
      // $FF: Couldn't be decompiled
   }
}
