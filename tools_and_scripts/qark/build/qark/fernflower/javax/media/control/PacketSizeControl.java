package javax.media.control;

import javax.media.Control;

public interface PacketSizeControl extends Control {
   int getPacketSize();

   int setPacketSize(int var1);
}
