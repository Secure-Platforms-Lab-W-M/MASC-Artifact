package net.sf.fmj.media.rtp.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class RTPPacketParser {
   public RTPPacket parse(Packet var1) throws BadFormatException {
      RTPPacket var26 = new RTPPacket(var1);
      DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var26.data, var26.offset, var26.length));

      boolean var10001;
      int var3;
      try {
         var3 = var5.readUnsignedByte();
      } catch (EOFException var24) {
         var10001 = false;
         throw new BadFormatException("Unexpected end of RTP packet");
      } catch (IOException var25) {
         var10001 = false;
         throw new IllegalArgumentException("Impossible Exception");
      }

      if ((var3 & 192) == 128) {
         if ((var3 & 16) != 0) {
            try {
               var26.extensionPresent = true;
            } catch (EOFException var20) {
               var10001 = false;
               throw new BadFormatException("Unexpected end of RTP packet");
            } catch (IOException var21) {
               var10001 = false;
               throw new IllegalArgumentException("Impossible Exception");
            }
         }

         int var2 = 0;
         if ((var3 & 32) != 0) {
            try {
               var2 = var26.data[var26.offset + var26.length - 1] & 255;
            } catch (EOFException var18) {
               var10001 = false;
               throw new BadFormatException("Unexpected end of RTP packet");
            } catch (IOException var19) {
               var10001 = false;
               throw new IllegalArgumentException("Impossible Exception");
            }
         }

         try {
            var26.payloadType = var5.readUnsignedByte();
            var26.marker = var26.payloadType >> 7;
            var26.payloadType &= 127;
            var26.seqnum = var5.readUnsignedShort();
            var26.timestamp = (long)var5.readInt() & 4294967295L;
            var26.ssrc = var5.readInt();
            var26.csrc = new int[var3 & 15];
         } catch (EOFException var16) {
            var10001 = false;
            throw new BadFormatException("Unexpected end of RTP packet");
         } catch (IOException var17) {
            var10001 = false;
            throw new IllegalArgumentException("Impossible Exception");
         }

         var3 = 0;

         while(true) {
            try {
               if (var3 >= var26.csrc.length) {
                  break;
               }

               var26.csrc[var3] = var5.readInt();
            } catch (EOFException var14) {
               var10001 = false;
               throw new BadFormatException("Unexpected end of RTP packet");
            } catch (IOException var15) {
               var10001 = false;
               throw new IllegalArgumentException("Impossible Exception");
            }

            ++var3;
         }

         int var4;
         try {
            var4 = 0 + (var26.csrc.length << 2) + 12;
         } catch (EOFException var12) {
            var10001 = false;
            throw new BadFormatException("Unexpected end of RTP packet");
         } catch (IOException var13) {
            var10001 = false;
            throw new IllegalArgumentException("Impossible Exception");
         }

         var3 = var4;

         label84: {
            try {
               if (!var26.extensionPresent) {
                  break label84;
               }

               var26.extensionType = var5.readUnsignedShort();
               var3 = var5.readUnsignedShort() << 2;
               var26.extension = new byte[var3];
               var5.readFully(var26.extension);
            } catch (EOFException var10) {
               var10001 = false;
               throw new BadFormatException("Unexpected end of RTP packet");
            } catch (IOException var11) {
               var10001 = false;
               throw new IllegalArgumentException("Impossible Exception");
            }

            var3 = var4 + var3 + 4;
         }

         try {
            var26.payloadlength = var26.length - (var3 + var2);
            if (var26.payloadlength >= 0) {
               var26.payloadoffset = var26.offset + var3;
               return var26;
            }
         } catch (EOFException var8) {
            var10001 = false;
            throw new BadFormatException("Unexpected end of RTP packet");
         } catch (IOException var9) {
            var10001 = false;
            throw new IllegalArgumentException("Impossible Exception");
         }

         try {
            throw new BadFormatException();
         } catch (EOFException var6) {
            var10001 = false;
            throw new BadFormatException("Unexpected end of RTP packet");
         } catch (IOException var7) {
            var10001 = false;
         }
      } else {
         try {
            throw new BadFormatException();
         } catch (EOFException var22) {
            var10001 = false;
            throw new BadFormatException("Unexpected end of RTP packet");
         } catch (IOException var23) {
            var10001 = false;
         }
      }

      throw new IllegalArgumentException("Impossible Exception");
   }
}
