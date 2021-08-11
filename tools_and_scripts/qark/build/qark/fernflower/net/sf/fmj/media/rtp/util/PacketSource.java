package net.sf.fmj.media.rtp.util;

import java.io.IOException;

public interface PacketSource {
   void closeSource();

   Packet receiveFrom() throws IOException;

   String sourceString();
}
