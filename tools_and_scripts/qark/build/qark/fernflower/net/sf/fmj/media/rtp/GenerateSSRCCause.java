package net.sf.fmj.media.rtp;

public enum GenerateSSRCCause {
   CREATE_SEND_STREAM,
   INITIALIZE,
   INIT_SESSION,
   LOCAL_COLLISION,
   REMOVE_SEND_STREAM;

   static {
      GenerateSSRCCause var0 = new GenerateSSRCCause("REMOVE_SEND_STREAM", 4);
      REMOVE_SEND_STREAM = var0;
   }
}
