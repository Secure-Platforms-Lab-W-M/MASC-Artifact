package gnu.java.zrtp;

import gnu.java.zrtp.packets.ZrtpPacketBase;
import gnu.java.zrtp.packets.ZrtpPacketCommit;
import gnu.java.zrtp.packets.ZrtpPacketConf2Ack;
import gnu.java.zrtp.packets.ZrtpPacketConfirm;
import gnu.java.zrtp.packets.ZrtpPacketDHPart;
import gnu.java.zrtp.packets.ZrtpPacketError;
import gnu.java.zrtp.packets.ZrtpPacketErrorAck;
import gnu.java.zrtp.packets.ZrtpPacketHello;
import gnu.java.zrtp.packets.ZrtpPacketHelloAck;
import gnu.java.zrtp.packets.ZrtpPacketPing;
import gnu.java.zrtp.packets.ZrtpPacketPingAck;
import gnu.java.zrtp.packets.ZrtpPacketRelayAck;
import gnu.java.zrtp.packets.ZrtpPacketSASRelay;
import java.util.EnumSet;

public class ZrtpStateClass {
   private static final int MESSAGE_OFFSET = 4;
   private ZrtpPacketCommit commitPkt = null;
   private ZrtpStateClass.Event event;
   private ZrtpStateClass.ZrtpStates inState;
   private boolean multiStream = false;
   private ZRtp parent;
   private ZrtpStateClass.SecureSubStates secSubstate;
   private ZrtpPacketBase sentPacket = null;
   private int sentVersion;
   // $FF: renamed from: t1 gnu.java.zrtp.ZrtpStateClass$ZrtpTimer
   private ZrtpStateClass.ZrtpTimer field_117;
   // $FF: renamed from: t2 gnu.java.zrtp.ZrtpStateClass$ZrtpTimer
   private ZrtpStateClass.ZrtpTimer field_118;

   protected ZrtpStateClass(ZRtp var1) {
      this.secSubstate = ZrtpStateClass.SecureSubStates.Normal;
      this.parent = var1;
      this.field_117 = new ZrtpStateClass.ZrtpTimer(50, 20, 200);
      this.field_118 = new ZrtpStateClass.ZrtpTimer(150, 10, 600);
      this.inState = ZrtpStateClass.ZrtpStates.Initial;
   }

   private int cancelTimer() {
      return this.parent.cancelTimer();
   }

   private void evDetect() {
      ZrtpCodes.ZrtpErrorCodes[] var4 = new ZrtpCodes.ZrtpErrorCodes[1];
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 3) {
               if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
                  this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
               }

               this.sentPacket = null;
               this.inState = ZrtpStateClass.ZrtpStates.Initial;
               return;
            }

            this.cancelTimer();
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
               return;
            }

            if (this.startTimer(this.field_117) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
               return;
            }
         } else {
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
               return;
            }

            if (this.nextTimer(this.field_117) <= 0) {
               this.commitPkt = null;
               this.parent.zrtpNotSuppOther();
               this.inState = ZrtpStateClass.ZrtpStates.Detect;
               return;
            }
         }
      } else {
         byte[] var5 = this.event.packet;
         char var7 = Character.toLowerCase((char)var5[4]);
         char var2 = Character.toLowerCase((char)var5[11]);
         if (var7 == 'h' && var2 == 'k') {
            this.cancelTimer();
            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.AckDetected;
            return;
         }

         if (var7 == 'h' && var2 == ' ') {
            ZrtpPacketHello var11 = new ZrtpPacketHello(var5);
            this.cancelTimer();
            int var3 = var11.getVersionInt();
            var1 = this.sentVersion;
            if (var3 > var1) {
               if (this.startTimer(this.field_117) <= 0) {
                  this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
               }

               return;
            }

            if (var3 != var1) {
               ZRtp.HelloPacketVersion[] var9 = this.parent.helloPackets;
               var1 = 0;

               int var8;
               while(true) {
                  var8 = var1;
                  if (var1 >= 2) {
                     break;
                  }

                  var8 = var1;
                  if (var9[var1].packet == this.parent.currentHelloPacket) {
                     break;
                  }

                  ++var1;
               }

               while(var8 >= 0 && var9[var8].version > var3) {
                  --var8;
               }

               if (var8 < 0) {
                  this.sendErrorPacket(ZrtpCodes.ZrtpErrorCodes.UnsuppZRTPVersion);
                  return;
               }

               this.parent.currentHelloPacket = var9[var8].packet;
               this.sentVersion = this.parent.currentHelloPacket.getVersionInt();
               ZrtpPacketHello var10 = this.parent.currentHelloPacket;
               this.sentPacket = var10;
               if (!this.parent.sendPacketZRTP(var10)) {
                  this.sendFailed();
                  return;
               }

               if (this.startTimer(this.field_117) <= 0) {
                  this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
                  return;
               }

               return;
            }

            ZrtpPacketHelloAck var6 = this.parent.prepareHelloAck();
            if (!this.parent.sendPacketZRTP(var6)) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCannotSend));
               return;
            }

            this.commitPkt = this.parent.prepareCommit(var11, var4);
            this.inState = ZrtpStateClass.ZrtpStates.AckSent;
            if (this.commitPkt == null) {
               this.sendErrorPacket(var4[0]);
               return;
            }

            if (this.startTimer(this.field_117) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            }

            this.field_117.setMaxResend(60);
         }
      }

   }

   private int nextTimer(ZrtpStateClass.ZrtpTimer var1) {
      int var2 = var1.nextTimer();
      return var2 < 0 ? var2 : this.parent.activateTimer(var2);
   }

   private void sendErrorPacket(ZrtpCodes.ZrtpErrorCodes var1) {
      this.cancelTimer();
      ZrtpPacketError var2 = this.parent.prepareError(var1);
      this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.ZrtpError, EnumSet.of(var1));
      this.sentPacket = var2;
      this.inState = ZrtpStateClass.ZrtpStates.WaitErrorAck;
      if (!this.parent.sendPacketZRTP(var2) || this.startTimer(this.field_118) <= 0) {
         this.sendFailed();
      }

   }

   private void sendFailed() {
      this.sentPacket = null;
      this.inState = ZrtpStateClass.ZrtpStates.Initial;
      this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCannotSend));
   }

   private int startTimer(ZrtpStateClass.ZrtpTimer var1) {
      return this.parent.activateTimer(var1.startTimer());
   }

   private void timerFailed(ZrtpCodes.SevereCodes var1) {
      this.sentPacket = null;
      this.inState = ZrtpStateClass.ZrtpStates.Initial;
      this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(var1));
   }

   protected void dispatchEvent() {
      switch(null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$ZrtpStates[this.inState.ordinal()]) {
      case 1:
         this.evInitial();
         return;
      case 2:
         this.evDetect();
         return;
      case 3:
         this.evAckDetected();
         return;
      case 4:
         this.evAckSent();
         return;
      case 5:
         this.evWaitCommit();
         return;
      case 6:
         this.evCommitSent();
         return;
      case 7:
         this.evWaitDHPart2();
         return;
      case 8:
         this.evWaitConfirm1();
         return;
      case 9:
         this.evWaitConfirm2();
         return;
      case 10:
         this.evWaitConfAck();
         return;
      case 11:
         this.evWaitClearAck();
         return;
      case 12:
         this.evSecureState();
         return;
      case 13:
         this.evWaitErrorAck();
         return;
      default:
      }
   }

   protected void evAckDetected() {
      ZrtpCodes.ZrtpErrorCodes[] var3 = new ZrtpCodes.ZrtpErrorCodes[1];
      if (null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()] != 1) {
         if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
            this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
         }

         this.inState = ZrtpStateClass.ZrtpStates.Initial;
      } else {
         byte[] var4 = this.event.packet;
         char var1 = Character.toLowerCase((char)var4[4]);
         char var2 = Character.toLowerCase((char)var4[11]);
         if (var1 == 'h' && var2 == ' ') {
            ZrtpPacketHello var6 = new ZrtpPacketHello(var4);
            if (this.parent.prepareCommit(var6, var3) == null) {
               this.sendErrorPacket(var3[0]);
               return;
            }

            ZrtpPacketHelloAck var5 = this.parent.prepareHelloAck();
            this.inState = ZrtpStateClass.ZrtpStates.WaitCommit;
            this.sentPacket = var5;
            if (!this.parent.sendPacketZRTP(var5)) {
               this.sendFailed();
            }
         }

      }
   }

   protected void evAckSent() {
      ZrtpCodes.ZrtpErrorCodes[] var3 = new ZrtpCodes.ZrtpErrorCodes[1];
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
            }

            this.commitPkt = null;
            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            return;
         }

         if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return;
         }

         if (this.nextTimer(this.field_117) <= 0) {
            this.parent.zrtpNotSuppOther();
            this.commitPkt = null;
            this.inState = ZrtpStateClass.ZrtpStates.Detect;
            return;
         }
      } else {
         byte[] var4 = this.event.packet;
         char var5 = Character.toLowerCase((char)var4[4]);
         char var2 = Character.toLowerCase((char)var4[11]);
         if (var5 == 'h' && var2 == 'k') {
            this.cancelTimer();
            this.sentPacket = this.commitPkt;
            this.commitPkt = null;
            this.inState = ZrtpStateClass.ZrtpStates.CommitSent;
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
               return;
            }

            if (this.startTimer(this.field_118) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            }

            return;
         }

         if (var5 == 'h' && var2 == ' ') {
            ZrtpPacketHelloAck var6 = this.parent.prepareHelloAck();
            if (!this.parent.sendPacketZRTP(var6)) {
               this.inState = ZrtpStateClass.ZrtpStates.Detect;
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCannotSend));
            }

            return;
         }

         if (var5 == 'c') {
            this.cancelTimer();
            ZrtpPacketCommit var7 = new ZrtpPacketCommit(var4);
            if (!this.multiStream) {
               ZrtpPacketDHPart var8 = this.parent.prepareDHPart1(var7, var3);
               if (var8 == null) {
                  if (var3[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                     this.sendErrorPacket(var3[0]);
                  }

                  return;
               }

               this.commitPkt = null;
               this.inState = ZrtpStateClass.ZrtpStates.WaitDHPart2;
               this.sentPacket = var8;
            } else {
               ZrtpPacketConfirm var9 = this.parent.prepareConfirm1MultiStream(var7, var3);
               if (var9 == null) {
                  if (var3[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                     this.sendErrorPacket(var3[0]);
                  }

                  return;
               }

               this.sentPacket = var9;
               this.inState = ZrtpStateClass.ZrtpStates.WaitConfirm2;
            }

            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
            }
         }
      }

   }

   protected void evCommitSent() {
      ZrtpCodes.ZrtpErrorCodes[] var3 = new ZrtpCodes.ZrtpErrorCodes[1];
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
            }

            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            return;
         }

         if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return;
         }

         if (this.nextTimer(this.field_118) <= 0) {
            this.timerFailed(ZrtpCodes.SevereCodes.SevereTooMuchRetries);
            return;
         }
      } else {
         byte[] var4 = this.event.packet;
         char var6 = Character.toLowerCase((char)var4[4]);
         char var2 = Character.toLowerCase((char)var4[11]);
         if (var6 == 'h' && (var2 == 'k' || var2 == ' ')) {
            return;
         }

         ZrtpPacketConfirm var7;
         if (var6 == 'c' && var2 == ' ') {
            ZrtpPacketCommit var8 = new ZrtpPacketCommit(var4);
            if (!this.parent.verifyH2(var8)) {
               return;
            }

            this.cancelTimer();
            if (this.parent.compareCommit(var8) < 0) {
               if (!this.multiStream) {
                  ZrtpPacketDHPart var9 = this.parent.prepareDHPart1(var8, var3);
                  if (var9 == null) {
                     if (var3[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                        this.sendErrorPacket(var3[0]);
                     }

                     return;
                  }

                  this.inState = ZrtpStateClass.ZrtpStates.WaitDHPart2;
                  this.sentPacket = var9;
               } else {
                  var7 = this.parent.prepareConfirm1MultiStream(var8, var3);
                  if (var7 == null) {
                     if (var3[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                        this.sendErrorPacket(var3[0]);
                     }

                     return;
                  }

                  this.sentPacket = var7;
                  this.inState = ZrtpStateClass.ZrtpStates.WaitConfirm2;
               }

               if (!this.parent.sendPacketZRTP(this.sentPacket)) {
                  this.sendFailed();
                  return;
               }
            } else if (this.startTimer(this.field_118) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            }

            return;
         }

         if (var6 == 'd') {
            ZrtpPacketDHPart var5 = new ZrtpPacketDHPart(var4);
            var5 = this.parent.prepareDHPart2(var5, var3);
            if (var5 == null) {
               if (var3[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                  this.sendErrorPacket(var3[0]);
               }

               return;
            }

            this.cancelTimer();
            this.sentPacket = var5;
            this.inState = ZrtpStateClass.ZrtpStates.WaitConfirm1;
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
               return;
            }

            if (this.startTimer(this.field_118) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            }
         }

         if (this.multiStream && var6 == 'c' && var2 == '1') {
            this.cancelTimer();
            var7 = new ZrtpPacketConfirm(var4);
            var7 = this.parent.prepareConfirm2MultiStream(var7, var3);
            if (var7 == null) {
               this.sendErrorPacket(var3[0]);
               return;
            }

            this.inState = ZrtpStateClass.ZrtpStates.WaitConfAck;
            this.sentPacket = var7;
            if (!this.parent.sendPacketZRTP(var7)) {
               this.sendFailed();
               return;
            }

            if (this.startTimer(this.field_118) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            }

            if (!this.parent.srtpSecretsReady(ZrtpCallback.EnableSecurity.ForReceiver)) {
               this.parent.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
               this.sendErrorPacket(ZrtpCodes.ZrtpErrorCodes.CriticalSWError);
               return;
            }
         }
      }

   }

   protected void evInitial() {
      if (this.event.type == ZrtpStateClass.EventDataType.ZrtpInitial) {
         ZrtpPacketHello var1 = this.parent.prepareHello();
         this.sentVersion = var1.getVersionInt();
         this.sentPacket = var1;
         if (!this.parent.sendPacketZRTP(var1)) {
            this.sendFailed();
            return;
         }

         if (this.startTimer(this.field_117) <= 0) {
            this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
            return;
         }

         this.inState = ZrtpStateClass.ZrtpStates.Detect;
      }

   }

   protected void evSecureState() {
      if (this.secSubstate != ZrtpStateClass.SecureSubStates.WaitSasRelayAck || !this.subEvWaitRelayAck()) {
         int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
         if (var1 != 1) {
            if (var1 != 2) {
               if (this.event.type != ZrtpStateClass.EventDataType.ErrorPkt) {
                  this.sentPacket = null;
                  this.inState = ZrtpStateClass.ZrtpStates.Initial;
                  this.parent.srtpSecretsOff(ZrtpCallback.EnableSecurity.ForSender);
                  this.parent.srtpSecretsOff(ZrtpCallback.EnableSecurity.ForReceiver);
                  if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
                     this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
                  }

                  this.parent.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoSecureStateOff));
               }
            }
         } else {
            byte[] var3 = this.event.packet;
            char var4 = Character.toLowerCase((char)var3[4]);
            char var2 = Character.toLowerCase((char)var3[11]);
            if (var4 == 'c' && var2 == '2') {
               ZrtpPacketBase var5 = this.sentPacket;
               if (var5 != null && !this.parent.sendPacketZRTP(var5)) {
                  this.sentPacket = null;
                  this.inState = ZrtpStateClass.ZrtpStates.Initial;
                  this.parent.srtpSecretsOff(ZrtpCallback.EnableSecurity.ForSender);
                  this.parent.srtpSecretsOff(ZrtpCallback.EnableSecurity.ForReceiver);
                  this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCannotSend));
               }

            }
         }
      }
   }

   protected void evWaitClearAck() {
   }

   protected void evWaitCommit() {
      ZrtpCodes.ZrtpErrorCodes[] var2 = new ZrtpCodes.ZrtpErrorCodes[1];
      if (null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()] != 1) {
         if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
            this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
         }

         this.commitPkt = null;
         this.sentPacket = null;
         this.inState = ZrtpStateClass.ZrtpStates.Initial;
      } else {
         byte[] var3 = this.event.packet;
         char var1 = Character.toLowerCase((char)var3[4]);
         if (var1 == 'h') {
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
            }

         } else {
            if (var1 == 'c') {
               ZrtpPacketCommit var4 = new ZrtpPacketCommit(var3);
               if (!this.multiStream) {
                  ZrtpPacketDHPart var5 = this.parent.prepareDHPart1(var4, var2);
                  if (var5 == null) {
                     if (var2[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                        this.sendErrorPacket(var2[0]);
                     }

                     return;
                  }

                  this.sentPacket = var5;
                  this.inState = ZrtpStateClass.ZrtpStates.WaitDHPart2;
               } else {
                  ZrtpPacketConfirm var6 = this.parent.prepareConfirm1MultiStream(var4, var2);
                  if (var6 == null) {
                     if (var2[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                        this.sendErrorPacket(var2[0]);
                     }

                     return;
                  }

                  this.sentPacket = var6;
                  this.inState = ZrtpStateClass.ZrtpStates.WaitConfirm2;
               }

               if (!this.parent.sendPacketZRTP(this.sentPacket)) {
                  this.sendFailed();
               }
            }

         }
      }
   }

   protected void evWaitConfAck() {
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
            }

            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            this.parent.srtpSecretsOff(ZrtpCallback.EnableSecurity.ForReceiver);
            return;
         }

         if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return;
         }

         if (this.nextTimer(this.field_118) <= 0) {
            this.timerFailed(ZrtpCodes.SevereCodes.SevereTooMuchRetries);
            return;
         }
      } else if (Character.toLowerCase((char)this.event.packet[4]) == 'c') {
         this.cancelTimer();
         this.sentPacket = null;
         if (!this.parent.srtpSecretsReady(ZrtpCallback.EnableSecurity.ForSender)) {
            this.parent.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
            this.sendErrorPacket(ZrtpCodes.ZrtpErrorCodes.CriticalSWError);
            return;
         }

         this.inState = ZrtpStateClass.ZrtpStates.SecureState;
         this.parent.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoSecureStateOn));
      }

   }

   protected void evWaitConfirm1() {
      ZrtpCodes.ZrtpErrorCodes[] var3 = new ZrtpCodes.ZrtpErrorCodes[1];
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
            }

            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            return;
         }

         if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return;
         }

         if (this.nextTimer(this.field_118) <= 0) {
            this.timerFailed(ZrtpCodes.SevereCodes.SevereTooMuchRetries);
            return;
         }
      } else {
         byte[] var4 = this.event.packet;
         char var5 = Character.toLowerCase((char)var4[4]);
         char var2 = Character.toLowerCase((char)var4[11]);
         if (var5 == 'c' && var2 == '1') {
            this.cancelTimer();
            ZrtpPacketConfirm var6 = new ZrtpPacketConfirm(var4);
            var6 = this.parent.prepareConfirm2(var6, var3);
            if (var6 == null) {
               this.sendErrorPacket(var3[0]);
               return;
            }

            this.inState = ZrtpStateClass.ZrtpStates.WaitConfAck;
            this.sentPacket = var6;
            if (!this.parent.sendPacketZRTP(var6)) {
               this.sendFailed();
               return;
            }

            if (this.startTimer(this.field_118) <= 0) {
               this.timerFailed(ZrtpCodes.SevereCodes.SevereNoTimer);
               return;
            }

            if (!this.parent.srtpSecretsReady(ZrtpCallback.EnableSecurity.ForReceiver)) {
               this.parent.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
               this.sendErrorPacket(ZrtpCodes.ZrtpErrorCodes.CriticalSWError);
               return;
            }
         }
      }

   }

   protected void evWaitConfirm2() {
      ZrtpCodes.ZrtpErrorCodes[] var3 = new ZrtpCodes.ZrtpErrorCodes[1];
      if (null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()] != 1) {
         if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
            this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
         }

         this.sentPacket = null;
         this.inState = ZrtpStateClass.ZrtpStates.Initial;
      } else {
         byte[] var4 = this.event.packet;
         char var1 = Character.toLowerCase((char)var4[4]);
         char var2 = Character.toLowerCase((char)var4[11]);
         if (var1 == 'd' || this.multiStream && var1 == 'c' && var2 == ' ') {
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
            }

         } else if (var1 == 'c' && var2 == '2') {
            ZrtpPacketConfirm var5 = new ZrtpPacketConfirm(var4);
            ZrtpPacketConf2Ack var6 = this.parent.prepareConf2Ack(var5, var3);
            if (var6 == null) {
               this.sendErrorPacket(var3[0]);
            } else {
               this.sentPacket = var6;
               if (!this.parent.sendPacketZRTP(var6)) {
                  this.sendFailed();
               } else if (this.parent.srtpSecretsReady(ZrtpCallback.EnableSecurity.ForSender) && this.parent.srtpSecretsReady(ZrtpCallback.EnableSecurity.ForReceiver)) {
                  this.inState = ZrtpStateClass.ZrtpStates.SecureState;
                  this.parent.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoSecureStateOn));
               } else {
                  this.parent.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                  this.sendErrorPacket(ZrtpCodes.ZrtpErrorCodes.CriticalSWError);
               }
            }
         }
      }
   }

   protected void evWaitDHPart2() {
      ZrtpCodes.ZrtpErrorCodes[] var2 = new ZrtpCodes.ZrtpErrorCodes[1];
      if (null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()] != 1) {
         if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
            this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
         }

         this.sentPacket = null;
         this.inState = ZrtpStateClass.ZrtpStates.Initial;
      } else {
         byte[] var3 = this.event.packet;
         char var1 = Character.toLowerCase((char)var3[4]);
         if (var1 == 'c') {
            if (!this.parent.sendPacketZRTP(this.sentPacket)) {
               this.sendFailed();
            }

         } else {
            if (var1 == 'd') {
               ZrtpPacketDHPart var4 = new ZrtpPacketDHPart(var3);
               ZrtpPacketConfirm var5 = this.parent.prepareConfirm1(var4, var2);
               if (var5 == null) {
                  if (var2[0] != ZrtpCodes.ZrtpErrorCodes.IgnorePacket) {
                     this.sendErrorPacket(var2[0]);
                  }

                  return;
               }

               this.inState = ZrtpStateClass.ZrtpStates.WaitConfirm2;
               this.sentPacket = var5;
               if (!this.parent.sendPacketZRTP(var5)) {
                  this.sendFailed();
               }
            }

         }
      }
   }

   protected void evWaitErrorAck() {
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (this.event.type != ZrtpStateClass.EventDataType.ZrtpClose) {
               this.parent.zrtpNegotiationFailed(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereProtocolError));
            }

            this.sentPacket = null;
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            return;
         }

         if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return;
         }

         if (this.nextTimer(this.field_118) <= 0) {
            this.timerFailed(ZrtpCodes.SevereCodes.SevereTooMuchRetries);
            return;
         }
      } else {
         byte[] var3 = this.event.packet;
         char var4 = Character.toLowerCase((char)var3[4]);
         char var2 = Character.toLowerCase((char)var3[11]);
         if (var4 == 'e' && var2 == 'k') {
            this.cancelTimer();
            this.inState = ZrtpStateClass.ZrtpStates.Initial;
            this.sentPacket = null;
         }
      }

   }

   public long getTimeoutValue() {
      long var3 = 0L;
      int var2 = 0;
      int var1 = this.field_117.start;

      do {
         var3 += (long)var1;
         var1 += var1;
         if (var1 > this.field_117.capping) {
            var1 = this.field_117.capping;
         }

         ++var2;
      } while(var2 < this.field_117.maxResend);

      return var3;
   }

   protected boolean isInState(ZrtpStateClass.ZrtpStates var1) {
      return var1 == this.inState;
   }

   protected boolean isMultiStream() {
      return this.multiStream;
   }

   protected void processEvent(ZrtpStateClass.Event var1) {
      synchronized(this){}

      Throwable var10000;
      label638: {
         boolean var10001;
         label639: {
            char var2;
            char var3;
            char var4;
            byte[] var5;
            label632: {
               try {
                  this.event = var1;
                  if (var1.type == ZrtpStateClass.EventDataType.ZrtpPacket) {
                     var5 = this.event.packet;
                     var2 = Character.toLowerCase((char)var5[4]);
                     var3 = Character.toLowerCase((char)var5[8]);
                     var4 = Character.toLowerCase((char)var5[11]);
                     break label632;
                  }
               } catch (Throwable var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label638;
               }

               try {
                  if (this.event.type == ZrtpStateClass.EventDataType.ZrtpClose) {
                     this.cancelTimer();
                  }
                  break label639;
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label638;
               }
            }

            if (var2 == 'e' && var3 == 'r' && var4 == ' ') {
               try {
                  this.cancelTimer();
                  ZrtpPacketError var66 = new ZrtpPacketError(var5);
                  ZrtpPacketErrorAck var67 = this.parent.prepareErrorAck(var66);
                  this.parent.sendPacketZRTP(var67);
                  this.event.type = ZrtpStateClass.EventDataType.ErrorPkt;
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label638;
               }
            } else {
               if (var2 == 'p' && var3 == ' ' && var4 == ' ') {
                  ZrtpPacketPingAck var65;
                  try {
                     ZrtpPacketPing var64 = new ZrtpPacketPing(var5);
                     var65 = this.parent.preparePingAck(var64);
                  } catch (Throwable var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label638;
                  }

                  if (var65 != null) {
                     try {
                        this.parent.sendPacketZRTP(var65);
                     } catch (Throwable var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label638;
                     }
                  }

                  return;
               }

               if (var2 == 's' && var4 == 'y') {
                  try {
                     ZrtpCodes.ZrtpErrorCodes[] var62 = new ZrtpCodes.ZrtpErrorCodes[1];
                     ZrtpPacketSASRelay var69 = new ZrtpPacketSASRelay(var5);
                     ZrtpPacketRelayAck var63 = this.parent.prepareRelayAck(var69, var62);
                     this.parent.sendPacketZRTP(var63);
                  } catch (Throwable var57) {
                     var10000 = var57;
                     var10001 = false;
                     break label638;
                  }

                  return;
               }
            }
         }

         try {
            this.dispatchEvent();
         } catch (Throwable var58) {
            var10000 = var58;
            var10001 = false;
            break label638;
         }

         return;
      }

      Throwable var68 = var10000;
      throw var68;
   }

   protected void sendSASRelay(ZrtpPacketSASRelay var1) {
      this.cancelTimer();
      this.sentPacket = var1;
      this.secSubstate = ZrtpStateClass.SecureSubStates.WaitSasRelayAck;
      if (!this.parent.sendPacketZRTP(var1) || this.startTimer(this.field_118) <= 0) {
         this.sendFailed();
      }

   }

   protected void setMultiStream(boolean var1) {
      this.multiStream = var1;
   }

   protected void stopZrtpStates() {
      if (this.inState != ZrtpStateClass.ZrtpStates.Initial) {
         this.cancelTimer();
         this.event = new ZrtpStateClass.Event(ZrtpStateClass.EventDataType.ZrtpClose, (byte[])null);
         this.dispatchEvent();
      }

   }

   protected boolean subEvWaitRelayAck() {
      int var1 = null.$SwitchMap$gnu$java$zrtp$ZrtpStateClass$EventDataType[this.event.type.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            return false;
         } else if (!this.parent.sendPacketZRTP(this.sentPacket)) {
            this.sendFailed();
            return false;
         } else {
            return this.nextTimer(this.field_118) > 0;
         }
      } else {
         byte[] var3 = this.event.packet;
         char var4 = Character.toLowerCase((char)var3[4]);
         char var2 = Character.toLowerCase((char)var3[11]);
         if (var4 == 'r' && var2 == 'k') {
            this.cancelTimer();
            this.secSubstate = ZrtpStateClass.SecureSubStates.Normal;
            this.sentPacket = null;
         }

         return true;
      }
   }

   protected class Event {
      private byte[] packet;
      private ZrtpStateClass.EventDataType type;

      public Event(ZrtpStateClass.EventDataType var2, byte[] var3) {
         this.type = var2;
         this.packet = var3;
      }

      protected byte[] getPacket() {
         return this.packet;
      }

      protected ZrtpStateClass.EventDataType getType() {
         return this.type;
      }
   }

   protected static enum EventDataType {
      ErrorPkt,
      Timer,
      ZrtpClose,
      ZrtpInitial,
      ZrtpPacket;

      static {
         ZrtpStateClass.EventDataType var0 = new ZrtpStateClass.EventDataType("ErrorPkt", 4);
         ErrorPkt = var0;
      }
   }

   public static enum SecureSubStates {
      Normal,
      WaitSasRelayAck,
      numberofSecureSubStates;

      static {
         ZrtpStateClass.SecureSubStates var0 = new ZrtpStateClass.SecureSubStates("numberofSecureSubStates", 2);
         numberofSecureSubStates = var0;
      }
   }

   public static enum ZrtpStates {
      AckDetected,
      AckSent,
      CommitSent,
      Detect,
      Initial,
      SecureState,
      WaitClearAck,
      WaitCommit,
      WaitConfAck,
      WaitConfirm1,
      WaitConfirm2,
      WaitDHPart2,
      WaitErrorAck,
      numberOfStates;

      static {
         ZrtpStateClass.ZrtpStates var0 = new ZrtpStateClass.ZrtpStates("numberOfStates", 13);
         numberOfStates = var0;
      }
   }

   private class ZrtpTimer {
      int capping;
      int counter;
      int maxResend;
      int start;
      int time;

      ZrtpTimer(int var2, int var3, int var4) {
         this.start = var2;
         this.capping = var4;
         this.maxResend = var3;
      }

      int nextTimer() {
         int var1 = this.time;
         int var2 = var1 + var1;
         this.time = var2;
         int var3 = this.capping;
         var1 = var2;
         if (var2 > var3) {
            var1 = var3;
         }

         this.time = var1;
         var2 = this.counter + 1;
         this.counter = var2;
         return var2 > this.maxResend ? -1 : var1;
      }

      void setMaxResend(int var1) {
         this.maxResend = var1;
      }

      int startTimer() {
         int var1 = this.start;
         this.time = var1;
         this.counter = 0;
         return var1;
      }
   }
}
