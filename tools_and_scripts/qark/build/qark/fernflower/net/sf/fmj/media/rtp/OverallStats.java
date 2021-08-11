package net.sf.fmj.media.rtp;

import javax.media.rtp.GlobalReceptionStats;

public class OverallStats implements GlobalReceptionStats {
   public static final int BADRTCPPACKET = 13;
   public static final int BADRTPPACKET = 2;
   public static final int BYTESRECD = 1;
   public static final int LOCALCOLL = 3;
   public static final int MALFORMEDBYE = 17;
   public static final int MALFORMEDRR = 15;
   public static final int MALFORMEDSDES = 16;
   public static final int MALFORMEDSR = 18;
   public static final int PACKETRECD = 0;
   public static final int PACKETSLOOPED = 5;
   public static final int REMOTECOLL = 4;
   public static final int RTCPRECD = 11;
   public static final int SRRECD = 12;
   public static final int TRANSMITFAILED = 6;
   public static final int UNKNOWNTYPE = 14;
   private int numBadRTCPPkts = 0;
   private int numBadRTPPkts = 0;
   private int numBytes = 0;
   private int numLocalColl = 0;
   private int numMalformedBye = 0;
   private int numMalformedRR = 0;
   private int numMalformedSDES = 0;
   private int numMalformedSR = 0;
   private int numPackets = 0;
   private int numPktsLooped = 0;
   private int numRTCPRecd = 0;
   private int numRemoteColl = 0;
   private int numSRRecd = 0;
   private int numTransmitFailed = 0;
   private int numUnknownTypes = 0;

   public int getBadRTCPPkts() {
      return this.numBadRTCPPkts;
   }

   public int getBadRTPkts() {
      return this.numBadRTPPkts;
   }

   public int getBytesRecd() {
      return this.numBytes;
   }

   public int getLocalColls() {
      return this.numLocalColl;
   }

   public int getMalformedBye() {
      return this.numMalformedBye;
   }

   public int getMalformedRR() {
      return this.numMalformedRR;
   }

   public int getMalformedSDES() {
      return this.numMalformedSDES;
   }

   public int getMalformedSR() {
      return this.numMalformedSR;
   }

   public int getPacketsLooped() {
      return this.numPktsLooped;
   }

   public int getPacketsRecd() {
      return this.numPackets;
   }

   public int getRTCPRecd() {
      return this.numRTCPRecd;
   }

   public int getRemoteColls() {
      return this.numRemoteColl;
   }

   public int getSRRecd() {
      return this.numSRRecd;
   }

   public int getTransmitFailed() {
      return this.numTransmitFailed;
   }

   public int getUnknownTypes() {
      return this.numUnknownTypes;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Packets Recd ");
      var1.append(this.getPacketsRecd());
      var1.append("\nBytes Recd ");
      var1.append(this.getBytesRecd());
      var1.append("\ngetBadRTP ");
      var1.append(this.getBadRTPkts());
      var1.append("\nLocalColl ");
      var1.append(this.getLocalColls());
      var1.append("\nRemoteColl ");
      var1.append(this.getRemoteColls());
      var1.append("\nPacketsLooped ");
      var1.append(this.getPacketsLooped());
      var1.append("\ngetTransmitFailed ");
      var1.append(this.getTransmitFailed());
      var1.append("\nRTCPRecd ");
      var1.append(this.getTransmitFailed());
      var1.append("\nSRRecd ");
      var1.append(this.getSRRecd());
      var1.append("\nBadRTCPPkts ");
      var1.append(this.getBadRTCPPkts());
      var1.append("\nUnknown ");
      var1.append(this.getUnknownTypes());
      var1.append("\nMalformedRR ");
      var1.append(this.getMalformedRR());
      var1.append("\nMalformedSDES ");
      var1.append(this.getMalformedSDES());
      var1.append("\nMalformedBye ");
      var1.append(this.getMalformedBye());
      var1.append("\nMalformedSR ");
      var1.append(this.getMalformedSR());
      return var1.toString();
   }

   public void update(int var1, int var2) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      switch(var1) {
      case 0:
         try {
            this.numPackets += var2;
            return;
         } catch (Throwable var243) {
            var10000 = var243;
            var10001 = false;
            break;
         }
      case 1:
         try {
            this.numBytes += var2;
            return;
         } catch (Throwable var242) {
            var10000 = var242;
            var10001 = false;
            break;
         }
      case 2:
         try {
            this.numBadRTPPkts += var2;
            return;
         } catch (Throwable var241) {
            var10000 = var241;
            var10001 = false;
            break;
         }
      case 3:
         try {
            this.numLocalColl += var2;
            return;
         } catch (Throwable var240) {
            var10000 = var240;
            var10001 = false;
            break;
         }
      case 4:
         try {
            this.numRemoteColl += var2;
            return;
         } catch (Throwable var239) {
            var10000 = var239;
            var10001 = false;
            break;
         }
      case 5:
         try {
            this.numPktsLooped += var2;
            return;
         } catch (Throwable var238) {
            var10000 = var238;
            var10001 = false;
            break;
         }
      case 6:
         try {
            this.numTransmitFailed += var2;
            return;
         } catch (Throwable var237) {
            var10000 = var237;
            var10001 = false;
            break;
         }
      case 7:
      case 8:
      case 9:
      case 10:
      default:
         return;
      case 11:
         try {
            this.numRTCPRecd += var2;
            return;
         } catch (Throwable var236) {
            var10000 = var236;
            var10001 = false;
            break;
         }
      case 12:
         try {
            this.numSRRecd += var2;
            return;
         } catch (Throwable var235) {
            var10000 = var235;
            var10001 = false;
            break;
         }
      case 13:
         try {
            this.numBadRTPPkts += var2;
            return;
         } catch (Throwable var234) {
            var10000 = var234;
            var10001 = false;
            break;
         }
      case 14:
         try {
            this.numUnknownTypes += var2;
            return;
         } catch (Throwable var233) {
            var10000 = var233;
            var10001 = false;
            break;
         }
      case 15:
         try {
            this.numMalformedRR += var2;
            return;
         } catch (Throwable var232) {
            var10000 = var232;
            var10001 = false;
            break;
         }
      case 16:
         try {
            this.numMalformedSDES += var2;
            return;
         } catch (Throwable var231) {
            var10000 = var231;
            var10001 = false;
            break;
         }
      case 17:
         try {
            this.numMalformedBye += var2;
            return;
         } catch (Throwable var230) {
            var10000 = var230;
            var10001 = false;
            break;
         }
      case 18:
         label1151:
         try {
            this.numMalformedSR += var2;
            return;
         } catch (Throwable var229) {
            var10000 = var229;
            var10001 = false;
            break label1151;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }
}
