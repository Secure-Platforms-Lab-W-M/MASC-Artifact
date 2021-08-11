package com.sun.media.codec.audio.ulaw;

public class Packetizer extends net.sf.fmj.media.codec.audio.ulaw.Packetizer {
   protected String PLUGIN_NAME = "ULAW Packetizer";
   protected int packetSize;

   public String getName() {
      return this.PLUGIN_NAME;
   }
}
