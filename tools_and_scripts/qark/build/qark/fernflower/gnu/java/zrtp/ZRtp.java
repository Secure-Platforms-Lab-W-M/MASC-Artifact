package gnu.java.zrtp;

import gnu.java.bigintcrypto.BigIntegerCrypto;
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
import gnu.java.zrtp.utils.Base32;
import gnu.java.zrtp.utils.EmojiBase32;
import gnu.java.zrtp.utils.ZrtpFortuna;
import gnu.java.zrtp.utils.ZrtpUtils;
import gnu.java.zrtp.zidfile.ZidFile;
import gnu.java.zrtp.zidfile.ZidRecord;
import java.util.Arrays;
import java.util.EnumSet;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.cryptozrtp.AsymmetricCipherKeyPair;
import org.bouncycastle.cryptozrtp.params.DHPublicKeyParameters;
import org.bouncycastle.cryptozrtp.params.Djb25519PublicKeyParameters;
import org.bouncycastle.cryptozrtp.params.ECDomainParameters;
import org.bouncycastle.cryptozrtp.params.ECPublicKeyParameters;
import org.bouncycastle.mathzrtp.ec.ECPoint;

public class ZRtp {
   static final int MAX_ZRTP_VERSIONS = 2;
   static final int SUPPORTED_ZRTP_VERSIONS = 1;
   private byte[] DHss;
   // $FF: renamed from: H0 byte[]
   private byte[] field_9;
   // $FF: renamed from: H1 byte[]
   private byte[] field_10;
   // $FF: renamed from: H2 byte[]
   private byte[] field_11;
   // $FF: renamed from: H3 byte[]
   private byte[] field_12;
   private String SAS;
   private ZrtpConstants.SupportedAuthLengths authLength;
   private byte[] auxSecretIDi;
   private byte[] auxSecretIDr;
   private ZrtpCallback callback;
   private ZrtpConstants.SupportedSymCiphers cipher;
   private ZrtpConfigure configureAlgos;
   ZrtpPacketHello currentHelloPacket;
   private AsymmetricCipherKeyPair dhKeyPair;
   private AsymmetricCipherKeyPair ecKeyPair;
   private boolean enableMitmEnrollment;
   private boolean enrollmentMode;
   private ZrtpConstants.SupportedHashes hash;
   private Digest hashCtxFunction;
   private Digest hashFunction;
   private Digest hashFunctionImpl;
   private int hashLength;
   private int hashLengthImpl;
   ZRtp.HelloPacketVersion[] helloPackets;
   int highestZrtpVersion;
   private HMac hmacFunction;
   private HMac hmacFunctionImpl;
   private byte[] hmacKeyI;
   private byte[] hmacKeyR;
   private byte[] hvi;
   private int lengthOfMsgData;
   private byte[] messageHash;
   private boolean mitmSeen;
   private boolean multiStream;
   private boolean multiStreamAvailable;
   private ZrtpCallback.Role myRole;
   private byte[] newRs1;
   private boolean paranoidMode;
   private byte[] pbxSecretIDi;
   private byte[] pbxSecretIDr;
   private byte[] pbxSecretTmp;
   private byte[] peerH2;
   private byte[] peerH3;
   private byte[] peerHelloHash;
   private byte[] peerHelloVersion;
   private byte[] peerHvi;
   private boolean peerIsEnrolled;
   private int peerSSRC;
   private byte[] peerZid;
   private ZrtpConstants.SupportedPubKeys pubKey;
   private byte[] pubKeyBytes;
   private byte[] randomIV;
   private byte[] rs1IDi;
   private byte[] rs1IDr;
   private boolean rs1Valid;
   private byte[] rs2IDi;
   private byte[] rs2IDr;
   private boolean rs2Valid;
   // $FF: renamed from: s0 byte[]
   private byte[] field_13;
   private byte[] sasHash;
   private ZrtpConstants.SupportedSASTypes sasType;
   private ZrtpFortuna secRand;
   private boolean signSasSeen;
   private byte[] signatureData;
   private int signatureLength;
   private byte[] srtpKeyI;
   private byte[] srtpKeyR;
   private byte[] srtpSaltI;
   private byte[] srtpSaltR;
   private final ZrtpStateClass stateEngine;
   private byte[] tempMsgBuffer;
   private final byte[] zid;
   ZidRecord zidRec;
   private ZrtpPacketCommit zrtpCommit;
   private ZrtpPacketConf2Ack zrtpConf2Ack;
   private ZrtpPacketConfirm zrtpConfirm1;
   private ZrtpPacketConfirm zrtpConfirm2;
   private ZrtpPacketDHPart zrtpDH1;
   private ZrtpPacketDHPart zrtpDH2;
   private ZrtpPacketError zrtpError;
   private ZrtpPacketErrorAck zrtpErrorAck;
   private ZrtpPacketHelloAck zrtpHelloAck;
   private ZrtpPacketHello zrtpHello_11;
   private ZrtpPacketHello zrtpHello_12;
   private byte[] zrtpKeyI;
   private byte[] zrtpKeyR;
   private ZrtpPacketPingAck zrtpPingAck;
   private ZrtpPacketRelayAck zrtpRelayAck;
   private ZrtpPacketSASRelay zrtpSasRelay;
   private byte[] zrtpSession;

   public ZRtp(byte[] var1, ZrtpCallback var2, String var3, ZrtpConfigure var4) {
      this(var1, var2, var3, var4, false, false);
   }

   public ZRtp(byte[] var1, ZrtpCallback var2, String var3, ZrtpConfigure var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public ZRtp(byte[] var1, ZrtpCallback var2, String var3, ZrtpConfigure var4, boolean var5, boolean var6) {
      this.zid = new byte[12];
      this.callback = null;
      this.dhKeyPair = null;
      this.ecKeyPair = null;
      this.DHss = null;
      this.pubKeyBytes = null;
      this.sasHash = null;
      this.rs1IDr = null;
      this.rs2IDr = null;
      this.auxSecretIDr = null;
      this.pbxSecretIDr = null;
      this.rs1IDi = null;
      this.rs2IDi = null;
      this.auxSecretIDi = null;
      this.pbxSecretIDi = null;
      this.rs1Valid = false;
      this.rs2Valid = false;
      this.hvi = new byte[64];
      this.peerHvi = null;
      this.hashLengthImpl = 32;
      this.hashFunctionImpl = new SHA256Digest();
      this.hmacFunctionImpl = new HMac(new SHA256Digest());
      this.field_9 = new byte[64];
      this.field_10 = new byte[64];
      this.field_11 = new byte[64];
      this.field_12 = new byte[64];
      this.peerHelloHash = new byte[64];
      this.peerHelloVersion = null;
      this.peerH2 = new byte[64];
      this.peerH3 = new byte[64];
      this.messageHash = new byte[64];
      this.field_13 = new byte[64];
      this.newRs1 = null;
      this.hmacKeyI = null;
      this.hmacKeyR = null;
      this.srtpKeyI = null;
      this.srtpSaltI = null;
      this.srtpKeyR = null;
      this.srtpSaltR = null;
      this.zrtpKeyI = null;
      this.zrtpKeyR = null;
      this.zrtpSession = null;
      this.multiStream = false;
      this.multiStreamAvailable = false;
      this.enableMitmEnrollment = false;
      this.mitmSeen = false;
      this.signSasSeen = false;
      this.pbxSecretTmp = null;
      this.enrollmentMode = false;
      this.zrtpHello_11 = new ZrtpPacketHello();
      this.zrtpHello_12 = new ZrtpPacketHello();
      this.zrtpHelloAck = new ZrtpPacketHelloAck();
      this.zrtpConf2Ack = new ZrtpPacketConf2Ack();
      this.zrtpError = new ZrtpPacketError();
      this.zrtpErrorAck = new ZrtpPacketErrorAck();
      this.zrtpDH1 = new ZrtpPacketDHPart();
      this.zrtpDH2 = new ZrtpPacketDHPart();
      this.zrtpCommit = new ZrtpPacketCommit();
      this.zrtpConfirm1 = new ZrtpPacketConfirm();
      this.zrtpConfirm2 = new ZrtpPacketConfirm();
      this.zrtpPingAck = new ZrtpPacketPingAck();
      this.zrtpSasRelay = new ZrtpPacketSASRelay();
      this.zrtpRelayAck = new ZrtpPacketRelayAck();
      this.helloPackets = new ZRtp.HelloPacketVersion[2];
      this.randomIV = new byte[16];
      this.tempMsgBuffer = new byte[1024];
      this.signatureData = null;
      this.signatureLength = 0;
      this.peerSSRC = 0;
      this.paranoidMode = false;
      this.secRand = ZrtpFortuna.getInstance();
      this.configureAlgos = var4;
      this.enableMitmEnrollment = var4.isTrustedMitM();
      this.paranoidMode = var4.isParanoidMode();
      System.arraycopy(var1, 0, this.zid, 0, 12);
      this.callback = var2;
      this.secRand.nextBytes(this.randomIV);
      this.secRand.nextBytes(this.field_9);
      this.hashFunctionImpl.update(this.field_9, 0, 32);
      this.hashFunctionImpl.doFinal(this.field_10, 0);
      this.hashFunctionImpl.update(this.field_10, 0, 32);
      this.hashFunctionImpl.doFinal(this.field_11, 0);
      this.hashFunctionImpl.update(this.field_11, 0, 32);
      this.hashFunctionImpl.doFinal(this.field_12, 0);
      this.zrtpHello_11.configureHello(var4);
      this.zrtpHello_11.setH3(this.field_12);
      this.zrtpHello_11.setZid(this.zid);
      this.zrtpHello_11.setVersion(ZrtpConstants.zrtpVersion_11);
      this.zrtpHello_12.configureHello(var4);
      this.zrtpHello_12.setH3(this.field_12);
      this.zrtpHello_12.setZid(this.zid);
      this.zrtpHello_12.setVersion(ZrtpConstants.zrtpVersion_12);
      if (var5) {
         this.zrtpHello_11.setMitmMode();
         this.zrtpHello_12.setMitmMode();
      }

      if (var6) {
         this.zrtpHello_11.setSasSign();
         this.zrtpHello_12.setSasSign();
      }

      this.helloPackets[0] = new ZRtp.HelloPacketVersion();
      this.helloPackets[0].helloHash = new byte[64];
      this.helloPackets[0].packet = this.zrtpHello_11;
      this.helloPackets[0].version = this.zrtpHello_11.getVersionInt();
      this.setClientId(var3, this.helloPackets[0]);
      this.helloPackets[1] = new ZRtp.HelloPacketVersion();
      this.helloPackets[1].helloHash = new byte[64];
      this.helloPackets[1].packet = this.zrtpHello_12;
      this.helloPackets[1].version = this.zrtpHello_12.getVersionInt();
      this.setClientId(var3, this.helloPackets[1]);
      this.currentHelloPacket = this.helloPackets[0].packet;
      this.stateEngine = new ZrtpStateClass(this);
   }

   private byte[] KDF(byte[] var1, byte[] var2, byte[] var3, int var4) {
      KeyParameter var5 = new KeyParameter(var1, 0, this.hashLength);
      this.hmacFunction.init(var5);
      var1 = ZrtpUtils.int32ToArray(1);
      this.hmacFunction.update(var1, 0, 4);
      this.hmacFunction.update(var2, 0, var2.length);
      this.hmacFunction.update(var3, 0, var3.length);
      var1 = ZrtpUtils.int32ToArray(var4);
      this.hmacFunction.update(var1, 0, 4);
      var1 = new byte[this.hashLength];
      this.hmacFunction.doFinal(var1, 0);
      return var1;
   }

   private byte[] adjustBigBytes(byte[] var1, int var2) {
      byte[] var4;
      if (var1.length > var2 && var1[0] == 0) {
         var4 = new byte[var1.length - 1];
         System.arraycopy(var1, 1, var4, 0, var4.length);
         return var4;
      } else if (var1.length < var2) {
         int var3 = var2 - var1.length;
         var4 = new byte[var2];
         System.arraycopy(var1, 0, var4, var3, var2 - var3);
         return var4;
      } else {
         return null;
      }
   }

   private boolean checkMsgHmac(byte[] var1) {
      int var2 = this.lengthOfMsgData - 8;
      boolean var3 = false;
      KeyParameter var4 = new KeyParameter(var1, 0, 32);
      this.hmacFunctionImpl.init(var4);
      this.hmacFunctionImpl.update(this.tempMsgBuffer, 0, var2);
      var1 = new byte[this.hashLengthImpl];
      this.hmacFunctionImpl.doFinal(var1, 0);
      if (ZrtpUtils.byteArrayCompare(var1, ZrtpUtils.readRegion(this.tempMsgBuffer, var2, 8), 8) == 0) {
         var3 = true;
      }

      return var3;
   }

   private boolean checkPubKey(BigIntegerCrypto var1, ZrtpConstants.SupportedPubKeys var2) {
      boolean var3 = var1.equals(BigIntegerCrypto.ONE);
      boolean var4 = false;
      if (var3) {
         return false;
      } else if (var2 == ZrtpConstants.SupportedPubKeys.DH2K) {
         return var1.equals(ZrtpConstants.P2048MinusOne) ^ true;
      } else {
         var3 = var4;
         if (var2 == ZrtpConstants.SupportedPubKeys.DH3K) {
            var3 = var4;
            if (!var1.equals(ZrtpConstants.P3072MinusOne)) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   private byte[] computeHmac(byte[] var1, int var2, byte[] var3, int var4) {
      KeyParameter var5 = new KeyParameter(var1, 0, var2);
      this.hmacFunction.init(var5);
      this.hmacFunction.update(var3, 0, var4);
      var1 = new byte[this.hashLength];
      this.hmacFunction.doFinal(var1, 0);
      return var1;
   }

   private byte[] computeHmacImpl(byte[] var1, int var2, byte[] var3, int var4) {
      KeyParameter var5 = new KeyParameter(var1, 0, var2);
      this.hmacFunctionImpl.init(var5);
      this.hmacFunctionImpl.update(var3, 0, var4);
      var1 = new byte[this.hashLengthImpl];
      this.hmacFunctionImpl.doFinal(var1, 0);
      return var1;
   }

   private void computeHvi(ZrtpPacketDHPart var1, ZrtpPacketHello var2) {
      this.hashFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
      this.hashFunction.update(var2.getHeaderBase(), 0, var2.getLength() * 4);
      this.hashFunction.doFinal(this.hvi, 0);
   }

   private byte[] computeMsgHmac(byte[] var1, ZrtpPacketBase var2) {
      short var3 = var2.getLength();
      return this.computeHmacImpl(var1, this.hashLengthImpl, var2.getHeaderBase(), (var3 - 2) * 4);
   }

   private void computePBXSecret() {
      byte[] var1 = new byte[this.zid.length + this.peerZid.length];
      byte[] var2;
      if (this.myRole == ZrtpCallback.Role.Responder) {
         var2 = this.peerZid;
         System.arraycopy(var2, 0, var1, 0, var2.length);
         var2 = this.zid;
         System.arraycopy(var2, 0, var1, this.peerZid.length, var2.length);
      } else {
         var2 = this.zid;
         System.arraycopy(var2, 0, var1, 0, var2.length);
         var2 = this.peerZid;
         System.arraycopy(var2, 0, var1, this.zid.length, var2.length);
      }

      this.pbxSecretTmp = this.KDF(this.zrtpSession, ZrtpConstants.zrtpTrustedMitm, var1, 256);
   }

   private void computeSRTPKeys() {
      byte[] var2 = new byte[this.zid.length + this.peerZid.length + this.hashLength];
      byte[] var3;
      if (this.myRole == ZrtpCallback.Role.Responder) {
         var3 = this.peerZid;
         System.arraycopy(var3, 0, var2, 0, var3.length);
         var3 = this.zid;
         System.arraycopy(var3, 0, var2, this.peerZid.length, var3.length);
      } else {
         var3 = this.zid;
         System.arraycopy(var3, 0, var2, 0, var3.length);
         var3 = this.peerZid;
         System.arraycopy(var3, 0, var2, this.zid.length, var3.length);
      }

      System.arraycopy(this.messageHash, 0, var2, this.zid.length + this.peerZid.length, this.hashLength);
      int var1 = this.cipher.keyLength * 8;
      this.srtpKeyI = this.KDF(this.field_13, ZrtpConstants.iniMasterKey, var2, var1);
      this.srtpSaltI = this.KDF(this.field_13, ZrtpConstants.iniMasterSalt, var2, 112);
      this.srtpKeyR = this.KDF(this.field_13, ZrtpConstants.respMasterKey, var2, var1);
      this.srtpSaltR = this.KDF(this.field_13, ZrtpConstants.respMasterSalt, var2, 112);
      this.hmacKeyI = this.KDF(this.field_13, ZrtpConstants.iniHmacKey, var2, this.hashLength * 8);
      this.hmacKeyR = this.KDF(this.field_13, ZrtpConstants.respHmacKey, var2, this.hashLength * 8);
      this.zrtpKeyI = this.KDF(this.field_13, ZrtpConstants.iniZrtpKey, var2, var1);
      this.zrtpKeyR = this.KDF(this.field_13, ZrtpConstants.respZrtpKey, var2, var1);
      if (!this.multiStream) {
         this.newRs1 = this.KDF(this.field_13, ZrtpConstants.retainedSec, var2, 256);
         this.zrtpSession = this.KDF(this.field_13, ZrtpConstants.zrtpSessionKey, var2, this.hashLength * 8);
         this.sasHash = this.KDF(this.field_13, ZrtpConstants.sasString, var2, 256);
         if (this.sasType != ZrtpConstants.SupportedSASTypes.B32 && this.sasType != ZrtpConstants.SupportedSASTypes.B32E) {
            StringBuilder var4 = new StringBuilder();
            var4.append(ZrtpConstants.sas256WordsEven[this.sasHash[0] & 255]);
            var4.append(":");
            var4.append(ZrtpConstants.sas256WordsOdd[this.sasHash[1] & 255]);
            this.SAS = var4.toString();
         } else {
            var2 = new byte[4];
            var3 = this.sasHash;
            var2[0] = var3[0];
            var2[1] = var3[1];
            var2[2] = (byte)(var3[2] & 240);
            var2[3] = 0;
            if (this.sasType == ZrtpConstants.SupportedSASTypes.B32) {
               this.SAS = Base32.binary2ascii(var2, 20);
            } else {
               this.SAS = EmojiBase32.binary2ascii(var2, 20);
            }
         }

         if (this.signSasSeen) {
            this.callback.signSAS(this.sasHash);
         }
      }

   }

   private void computeSharedSecretSet() {
      byte[] var1 = new byte[32];
      if (!this.zidRec.isRs1Valid()) {
         this.secRand.nextBytes(var1);
         this.rs1IDi = this.computeHmac(var1, 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.rs1IDr = this.computeHmac(var1, 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      } else {
         this.rs1Valid = true;
         this.rs1IDi = this.computeHmac(this.zidRec.getRs1(), 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.rs1IDr = this.computeHmac(this.zidRec.getRs1(), 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      }

      if (!this.zidRec.isRs2Valid()) {
         this.secRand.nextBytes(var1);
         this.rs2IDi = this.computeHmac(var1, 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.rs2IDr = this.computeHmac(var1, 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      } else {
         this.rs2Valid = true;
         this.rs2IDi = this.computeHmac(this.zidRec.getRs2(), 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.rs2IDr = this.computeHmac(this.zidRec.getRs2(), 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      }

      this.secRand.nextBytes(var1);
      this.auxSecretIDi = this.computeHmac(var1, 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
      this.auxSecretIDr = this.computeHmac(var1, 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      if (!this.zidRec.isMITMKeyAvailable()) {
         this.secRand.nextBytes(var1);
         this.pbxSecretIDi = this.computeHmac(var1, 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.pbxSecretIDr = this.computeHmac(var1, 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      } else {
         this.pbxSecretIDi = this.computeHmac(this.zidRec.getMiTMData(), 32, ZrtpConstants.initiator, ZrtpConstants.initiator.length);
         this.pbxSecretIDr = this.computeHmac(this.zidRec.getMiTMData(), 32, ZrtpConstants.responder, ZrtpConstants.responder.length);
      }
   }

   private boolean fillPubKey() {
      AsymmetricCipherKeyPair var1;
      byte[] var3;
      if (this.pubKey != ZrtpConstants.SupportedPubKeys.DH2K && this.pubKey != ZrtpConstants.SupportedPubKeys.DH3K) {
         if (this.pubKey != ZrtpConstants.SupportedPubKeys.EC25 && this.pubKey != ZrtpConstants.SupportedPubKeys.EC38) {
            if (this.pubKey == ZrtpConstants.SupportedPubKeys.E255) {
               var1 = this.pubKey.keyPairGen.generateKeyPair();
               this.ecKeyPair = var1;
               this.pubKeyBytes = ((Djb25519PublicKeyParameters)var1.getPublic()).getP();
               return true;
            } else {
               return false;
            }
         } else {
            var1 = this.pubKey.keyPairGen.generateKeyPair();
            this.ecKeyPair = var1;
            var3 = ((ECPublicKeyParameters)var1.getPublic()).getQ().getEncoded();
            byte[] var2 = new byte[this.pubKey.pubKeySize];
            this.pubKeyBytes = var2;
            System.arraycopy(var3, 1, var2, 0, this.pubKey.pubKeySize);
            return true;
         }
      } else {
         var1 = this.pubKey.keyPairGen.generateKeyPair();
         this.dhKeyPair = var1;
         var3 = ((DHPublicKeyParameters)var1.getPublic()).getY().toByteArray();
         this.pubKeyBytes = var3;
         if (var3.length != this.pubKey.pubKeySize) {
            var3 = this.adjustBigBytes(this.pubKeyBytes, this.pubKey.pubKeySize);
            this.pubKeyBytes = var3;
            if (var3 == null) {
               return false;
            }
         }

         return true;
      }
   }

   private void generateKeysInitiator(ZrtpPacketDHPart var1) {
      byte[][] var3 = new byte[3][];
      byte var2 = 0;
      var3[2] = null;
      var3[1] = null;
      var3[0] = null;
      if (ZrtpUtils.byteArrayCompare(this.rs1IDr, var1.getRs1Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs1();
         var2 = 1;
      } else if (ZrtpUtils.byteArrayCompare(this.rs1IDr, var1.getRs2Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs1();
         var2 = 2;
      } else if (ZrtpUtils.byteArrayCompare(this.rs2IDr, var1.getRs1Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs2();
         var2 = 4;
      } else if (ZrtpUtils.byteArrayCompare(this.rs2IDr, var1.getRs2Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs2();
         var2 = 8;
      }

      if (ZrtpUtils.byteArrayCompare(this.pbxSecretIDr, var1.getPbxSecretId(), 8) == 0) {
         var3[2] = this.zidRec.getMiTMData();
      }

      if (var2 == 0) {
         if (!this.rs1Valid && !this.rs2Valid) {
            this.sendInfo(ZrtpCodes.MessageSeverity.Warning, EnumSet.of(ZrtpCodes.WarningCodes.WarningNoRSMatch));
         } else {
            this.sendInfo(ZrtpCodes.MessageSeverity.Warning, EnumSet.of(ZrtpCodes.WarningCodes.WarningNoExpectedRSMatch));
            this.zidRec.resetSasVerified();
         }
      } else {
         this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRSMatchFound));
      }

      byte[] var5 = ZrtpUtils.int32ToArray(1);
      this.hashFunction.update(var5, 0, 4);
      Digest var6 = this.hashFunction;
      byte[] var4 = this.DHss;
      var6.update(var4, 0, var4.length);
      this.hashFunction.update(ZrtpConstants.KDFString, 0, ZrtpConstants.KDFString.length);
      var6 = this.hashFunction;
      var4 = this.zid;
      var6.update(var4, 0, var4.length);
      var6 = this.hashFunction;
      var4 = this.peerZid;
      var6.update(var4, 0, var4.length);
      this.hashFunction.update(this.messageHash, 0, this.hashLength);
      var5 = ZrtpUtils.int32ToArray(32);
      var4 = new byte[4];
      Arrays.fill(var4, (byte)0);

      for(int var7 = 0; var7 < 3; ++var7) {
         if (var3[var7] != null) {
            this.hashFunction.update(var5, 0, var5.length);
            this.hashFunction.update(var3[var7], 0, var3[var7].length);
         } else {
            this.hashFunction.update(var4, 0, var4.length);
         }
      }

      var5 = new byte[64];
      this.field_13 = var5;
      this.hashFunction.doFinal(var5, 0);
      Arrays.fill(this.DHss, (byte)0);
      this.DHss = null;
      this.computeSRTPKeys();
      Arrays.fill(this.field_13, (byte)0);
   }

   private void generateKeysMultiStream() {
      byte[] var1 = new byte[this.zid.length + this.peerZid.length + this.hashLength];
      byte[] var2;
      if (this.myRole == ZrtpCallback.Role.Responder) {
         var2 = this.peerZid;
         System.arraycopy(var2, 0, var1, 0, var2.length);
         var2 = this.zid;
         System.arraycopy(var2, 0, var1, this.peerZid.length, var2.length);
      } else {
         var2 = this.zid;
         System.arraycopy(var2, 0, var1, 0, var2.length);
         var2 = this.peerZid;
         System.arraycopy(var2, 0, var1, this.zid.length, var2.length);
      }

      System.arraycopy(this.messageHash, 0, var1, this.zid.length + this.peerZid.length, this.hashLength);
      this.field_13 = this.KDF(this.zrtpSession, ZrtpConstants.zrtpMsk, var1, this.hashLength * 8);
      this.computeSRTPKeys();
      Arrays.fill(this.field_13, (byte)0);
   }

   private void generateKeysResponder(ZrtpPacketDHPart var1) {
      byte[][] var3 = new byte[3][];
      byte var2 = 0;
      var3[2] = null;
      var3[1] = null;
      var3[0] = null;
      if (ZrtpUtils.byteArrayCompare(this.rs1IDi, var1.getRs1Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs1();
         var2 = 1;
      } else if (ZrtpUtils.byteArrayCompare(this.rs1IDi, var1.getRs2Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs1();
         var2 = 2;
      } else if (ZrtpUtils.byteArrayCompare(this.rs2IDi, var1.getRs1Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs2();
         var2 = 4;
      } else if (ZrtpUtils.byteArrayCompare(this.rs2IDi, var1.getRs2Id(), 8) == 0) {
         var3[0] = this.zidRec.getRs2();
         var2 = 8;
      }

      if (ZrtpUtils.byteArrayCompare(this.pbxSecretIDi, var1.getPbxSecretId(), 8) == 0) {
         var3[2] = this.zidRec.getMiTMData();
      }

      if (var2 == 0) {
         if (!this.rs1Valid && !this.rs2Valid) {
            this.sendInfo(ZrtpCodes.MessageSeverity.Warning, EnumSet.of(ZrtpCodes.WarningCodes.WarningNoRSMatch));
         } else {
            this.sendInfo(ZrtpCodes.MessageSeverity.Warning, EnumSet.of(ZrtpCodes.WarningCodes.WarningNoExpectedRSMatch));
            this.zidRec.resetSasVerified();
         }
      } else {
         this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRSMatchFound));
      }

      byte[] var5 = ZrtpUtils.int32ToArray(1);
      this.hashFunction.update(var5, 0, 4);
      Digest var6 = this.hashFunction;
      byte[] var4 = this.DHss;
      var6.update(var4, 0, var4.length);
      this.hashFunction.update(ZrtpConstants.KDFString, 0, ZrtpConstants.KDFString.length);
      var6 = this.hashFunction;
      var4 = this.peerZid;
      var6.update(var4, 0, var4.length);
      var6 = this.hashFunction;
      var4 = this.zid;
      var6.update(var4, 0, var4.length);
      this.hashFunction.update(this.messageHash, 0, this.hashLength);
      var5 = ZrtpUtils.int32ToArray(32);
      var4 = new byte[4];
      Arrays.fill(var4, (byte)0);

      for(int var7 = 0; var7 < 3; ++var7) {
         if (var3[var7] != null) {
            this.hashFunction.update(var5, 0, var5.length);
            this.hashFunction.update(var3[var7], 0, var3[var7].length);
         } else {
            this.hashFunction.update(var4, 0, var4.length);
         }
      }

      var5 = new byte[64];
      this.field_13 = var5;
      this.hashFunction.doFinal(var5, 0);
      Arrays.fill(this.DHss, (byte)0);
      this.DHss = null;
      this.computeSRTPKeys();
      Arrays.fill(this.field_13, (byte)0);
   }

   private void setClientId(String var1, ZRtp.HelloPacketVersion var2) {
      if (var1.length() < 16) {
         var2.packet.setClientId("                ");
      }

      var2.packet.setClientId(var1);
      int var3 = var2.packet.getLength() * 4;
      byte[] var4 = this.computeHmacImpl(this.field_11, this.hashLengthImpl, var2.packet.getHeaderBase(), var3 - 8);
      var2.packet.setHMAC(var4);
      this.hashFunctionImpl.update(var2.packet.getHeaderBase(), 0, var3);
      this.hashFunctionImpl.doFinal(var2.helloHash, 0);
   }

   private void setNegotiatedHash(ZrtpConstants.SupportedHashes var1) {
      if (var1 == ZrtpConstants.SupportedHashes.S256) {
         this.hashFunction = new SHA256Digest();
         this.hmacFunction = new HMac(new SHA256Digest());
         this.hashCtxFunction = new SHA256Digest();
      } else if (var1 == ZrtpConstants.SupportedHashes.S384) {
         this.hashFunction = new SHA384Digest();
         this.hmacFunction = new HMac(new SHA384Digest());
         this.hashCtxFunction = new SHA384Digest();
      }

      this.hashLength = this.hashFunction.getDigestSize();
   }

   private void storeMsgTemp(ZrtpPacketBase var1) {
      int var2 = var1.getLength() * 4;
      byte[] var3 = this.tempMsgBuffer;
      if (var2 > var3.length) {
         var2 = var3.length;
      }

      Arrays.fill(this.tempMsgBuffer, (byte)0);
      System.arraycopy(var1.getHeaderBase(), 0, this.tempMsgBuffer, 0, var2);
      this.lengthOfMsgData = var2;
   }

   public void SASVerified() {
      if (!this.paranoidMode) {
         this.zidRec.setSasVerified();
         ZidFile.getInstance().saveRecord(this.zidRec);
      }
   }

   public void acceptEnrollment(boolean var1) {
      if (!var1) {
         this.callback.zrtpInformEnrollment(ZrtpCodes.InfoEnrollment.EnrollmentCanceled);
      } else {
         byte[] var2 = this.pbxSecretTmp;
         if (var2 != null) {
            this.zidRec.setMiTMData(var2);
            this.callback.zrtpInformEnrollment(ZrtpCodes.InfoEnrollment.EnrollmentOk);
            ZidFile.getInstance().saveRecord(this.zidRec);
         } else {
            this.callback.zrtpInformEnrollment(ZrtpCodes.InfoEnrollment.EnrollmentFailed);
         }
      }
   }

   protected int activateTimer(int var1) {
      return this.callback.activateTimer(var1);
   }

   protected int cancelTimer() {
      return this.callback.cancelTimer();
   }

   protected int compareCommit(ZrtpPacketCommit var1) {
      return this.multiStream ? ZrtpUtils.byteArrayCompare(this.hvi, var1.getNonce(), 16) : ZrtpUtils.byteArrayCompare(this.hvi, var1.getHvi(), 32);
   }

   public void conf2AckSecure() {
      ZrtpStateClass var1 = this.stateEngine;
      if (var1 != null) {
         ZrtpStateClass.Event var2 = var1.new Event(ZrtpStateClass.EventDataType.ZrtpPacket, this.zrtpConf2Ack.getHeaderBase());
         this.stateEngine.processEvent(var2);
      }

   }

   public int getCurrentProtocolVersion() {
      return this.currentHelloPacket.getVersionInt();
   }

   public String getHelloHash(int var1) {
      String var2 = new String(this.helloPackets[var1].packet.getVersion());
      String var3 = new String(ZrtpUtils.bytesToHexString(this.helloPackets[var1].helloHash, this.hashLengthImpl));
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append(" ");
      var4.append(var3);
      return var4.toString();
   }

   public String[] getHelloHashSep(int var1) {
      return new String[]{new String(this.helloPackets[var1].packet.getVersion()), new String(ZrtpUtils.bytesToHexString(this.helloPackets[var1].helloHash, this.hashLengthImpl))};
   }

   public byte[] getMultiStrParams() {
      Object var2 = null;
      byte[] var1 = (byte[])var2;
      if (this.inState(ZrtpStateClass.ZrtpStates.SecureState)) {
         var1 = (byte[])var2;
         if (!this.multiStream) {
            var1 = new byte[this.hashLength + 1 + 1 + 1];
            var1[0] = (byte)this.hash.ordinal();
            var1[1] = (byte)this.authLength.ordinal();
            var1[2] = (byte)this.cipher.ordinal();
            System.arraycopy(this.zrtpSession, 0, var1, 3, this.hashLength);
         }
      }

      return var1;
   }

   public int getNumberSupportedVersions() {
      return 1;
   }

   public String getPeerHelloHash() {
      if (this.peerHelloVersion == null) {
         return null;
      } else {
         String var1 = new String(this.peerHelloVersion);
         String var2 = new String(ZrtpUtils.bytesToHexString(this.peerHelloHash, this.hashLengthImpl));
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" ");
         var3.append(var2);
         return var3.toString();
      }
   }

   public String[] getPeerHelloHashSep() {
      return this.peerHelloVersion == null ? null : new String[]{new String(this.peerHelloVersion), new String(ZrtpUtils.bytesToHexString(this.peerHelloHash, this.hashLengthImpl))};
   }

   public byte[] getPeerZid() {
      byte[] var1 = new byte[12];
      System.arraycopy(this.peerZid, 0, var1, 0, 12);
      return var1;
   }

   public byte[] getSasHash() {
      return this.sasHash;
   }

   public ZrtpConstants.SupportedSASTypes getSasType() {
      return this.sasType;
   }

   public byte[] getSignatureData() {
      return this.signatureData;
   }

   public int getSignatureLength() {
      return this.signatureLength * 4;
   }

   public long getTimeoutValue() {
      ZrtpStateClass var1 = this.stateEngine;
      return var1 != null ? var1.getTimeoutValue() : -1L;
   }

   public boolean inState(ZrtpStateClass.ZrtpStates var1) {
      ZrtpStateClass var2 = this.stateEngine;
      return var2 != null && var2.isInState(var1);
   }

   public boolean isEnrollmentMode() {
      return this.enrollmentMode;
   }

   public boolean isMultiStream() {
      return this.multiStream;
   }

   public boolean isMultiStreamAvailable() {
      return this.multiStreamAvailable;
   }

   public boolean isPeerEnrolled() {
      return this.peerIsEnrolled;
   }

   protected ZrtpPacketCommit prepareCommit(ZrtpPacketHello var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         byte[] var4 = var1.getZid();
         this.peerZid = var4;
         if (ZrtpUtils.byteArrayCompare(var4, this.zid, 12) == 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.EqualZIDHello;
            return null;
         } else {
            System.arraycopy(var1.getH3(), 0, this.peerH3, 0, 32);
            short var3 = var1.getLength();
            this.hashFunctionImpl.update(var1.getHeaderBase(), 0, var3 * 4);
            this.hashFunctionImpl.doFinal(this.peerHelloHash, 0);
            this.peerHelloVersion = var1.getVersion();
            this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoHelloReceived));
            this.sasType = var1.findBestSASType(this.configureAlgos);
            if (!this.multiStream) {
               this.pubKey = var1.findBestPubkey(this.configureAlgos);
               ZrtpConstants.SupportedHashes var6 = var1.getSelectedHash();
               this.hash = var6;
               if (var6 == null) {
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppHashType;
                  return null;
               } else {
                  ZrtpConstants.SupportedSymCiphers var7 = var1.getSelectedCipher();
                  this.cipher = var7;
                  if (var7 == null) {
                     this.cipher = var1.findBestCipher(this.configureAlgos, this.pubKey);
                  }

                  this.authLength = var1.findBestAuthLen(this.configureAlgos);
                  this.multiStreamAvailable = var1.checkMultiStream();
                  this.setNegotiatedHash(this.hash);
                  if (!this.fillPubKey()) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                     return null;
                  } else {
                     this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoCommitDHGenerated));
                     this.zidRec = ZidFile.getInstance().getRecord(this.peerZid);
                     this.computeSharedSecretSet();
                     if (var1.isMitmMode()) {
                        this.mitmSeen = true;
                     }

                     this.peerIsEnrolled = this.zidRec.isMITMKeyAvailable();
                     this.signSasSeen = var1.isSasSign();
                     this.zrtpDH2.setPubKeyType(this.pubKey);
                     this.zrtpDH2.setMessageType(ZrtpConstants.DHPart2Msg);
                     this.zrtpDH2.setRs1Id(this.rs1IDi);
                     this.zrtpDH2.setRs2Id(this.rs2IDi);
                     this.zrtpDH2.setAuxSecretId(this.auxSecretIDi);
                     this.zrtpDH2.setPbxSecretId(this.pbxSecretIDi);
                     this.zrtpDH2.setPv(this.pubKeyBytes);
                     this.zrtpDH2.setH1(this.field_10);
                     byte[] var5 = this.computeMsgHmac(this.field_9, this.zrtpDH2);
                     this.zrtpDH2.setHMAC(var5);
                     this.computeHvi(this.zrtpDH2, var1);
                     this.zrtpCommit.setZid(this.zid);
                     this.zrtpCommit.setHashType(this.hash.name);
                     this.zrtpCommit.setCipherType(this.cipher.name);
                     this.zrtpCommit.setAuthLen(this.authLength.name);
                     this.zrtpCommit.setPubKeyType(this.pubKey.name);
                     this.zrtpCommit.setSasType(this.sasType.name);
                     this.zrtpCommit.setHvi(this.hvi);
                     this.zrtpCommit.setH2(this.field_11);
                     var5 = this.computeMsgHmac(this.field_10, this.zrtpCommit);
                     this.zrtpCommit.setHMAC(var5);
                     this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
                     var3 = this.zrtpCommit.getLength();
                     this.hashCtxFunction.update(this.zrtpCommit.getHeaderBase(), 0, var3 * 4);
                     this.storeMsgTemp(var1);
                     return this.zrtpCommit;
                  }
               }
            } else if (var1.checkMultiStream()) {
               return this.prepareCommitMultiStream(var1);
            } else {
               var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppPKExchange;
               return null;
            }
         }
      }
   }

   protected ZrtpPacketCommit prepareCommitMultiStream(ZrtpPacketHello var1) {
      byte[] var3 = new byte[16];
      this.hvi = var3;
      this.secRand.nextBytes(var3);
      this.zrtpCommit.setZid(this.zid);
      this.zrtpCommit.setHashType(this.hash.name);
      this.zrtpCommit.setCipherType(this.cipher.name);
      this.zrtpCommit.setAuthLen(this.authLength.name);
      this.zrtpCommit.setPubKeyType(ZrtpConstants.SupportedPubKeys.MULT.name);
      this.zrtpCommit.setSasType(this.sasType.name);
      this.zrtpCommit.setNonce(this.hvi);
      this.zrtpCommit.setH2(this.field_11);
      short var2 = this.zrtpCommit.getLength();
      var3 = this.computeMsgHmac(this.field_10, this.zrtpCommit);
      this.zrtpCommit.setHMACMulti(var3);
      this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
      this.hashCtxFunction.update(this.zrtpCommit.getHeaderBase(), 0, var2 * 4);
      this.storeMsgTemp(var1);
      var2 = var1.getLength();
      this.hashFunctionImpl.update(var1.getHeaderBase(), 0, var2 * 4);
      this.hashFunctionImpl.doFinal(this.peerHelloHash, 0);
      this.peerHelloVersion = var1.getVersion();
      return this.zrtpCommit;
   }

   protected ZrtpPacketConf2Ack prepareConf2Ack(ZrtpPacketConfirm var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRespConf2Received));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         byte[] var5 = var1.getDataToSecure();
         if (ZrtpUtils.byteArrayCompare(this.computeHmac(this.hmacKeyI, this.hashLength, var5, var5.length), var1.getHmac(), 8) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.ConfirmHMACWrong;
            return null;
         } else {
            int var3;
            try {
               this.cipher.cipher.init(false, new ParametersWithIV(new KeyParameter(this.zrtpKeyI, 0, this.cipher.keyLength), var1.getIv()));
               var3 = this.cipher.cipher.processBytes(var5, 0, var5.length, var5, 0);
               this.cipher.cipher.doFinal(var5, var3);
            } catch (Exception var6) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            }

            var1.setDataToSecure(var5);
            if (!this.multiStream) {
               if (!this.checkMsgHmac(var1.getHashH0())) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereDH2HMACFailed));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               var3 = var1.getSignatureLength();
               this.signatureLength = var3;
               if (this.signSasSeen && var3 > 0) {
                  this.signatureData = var1.getSignatureData();
                  this.callback.checkSASSignature(this.sasHash);
               }

               if (!var1.isSASFlag() || this.paranoidMode) {
                  this.zidRec.resetSasVerified();
               }

               boolean var4 = this.zidRec.isSasVerified();
               this.zidRec.setNewRs1(this.newRs1, -1);
               ZidFile.getInstance().saveRecord(this.zidRec);
               if (this.enableMitmEnrollment && var1.isPBXEnrollment()) {
                  this.computePBXSecret();
                  this.callback.zrtpAskEnrollment(ZrtpCodes.InfoEnrollment.EnrollmentRequest);
               }

               ZrtpCallback var7 = this.callback;
               StringBuilder var8 = new StringBuilder();
               var8.append(this.cipher.readable);
               var8.append("/");
               var8.append(this.pubKey);
               var7.srtpSecretsOn(var8.toString(), this.SAS, var4);
            } else {
               var5 = new byte[64];
               this.hashFunctionImpl.update(var1.getHashH0(), 0, 32);
               this.hashFunctionImpl.doFinal(var5, 0);
               if (!this.checkMsgHmac(var5)) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCommitHMACFailed));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               this.callback.srtpSecretsOn(this.cipher.readable, (String)null, true);
            }

            return this.zrtpConf2Ack;
         }
      }
   }

   protected ZrtpPacketConfirm prepareConfirm1(ZrtpPacketDHPart var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRespDH2Received));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         byte[] var4 = new byte[64];
         this.hashFunctionImpl.update(var1.getH1(), 0, 32);
         this.hashFunctionImpl.doFinal(var4, 0);
         if (ZrtpUtils.byteArrayCompare(var4, this.peerH2, 32) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.IgnorePacket;
            return null;
         } else {
            this.computeHvi(var1, this.currentHelloPacket);
            if (ZrtpUtils.byteArrayCompare(this.hvi, this.peerHvi, 32) != 0) {
               var2[0] = ZrtpCodes.ZrtpErrorCodes.DHErrorWrongHVI;
               return null;
            } else if (!this.checkMsgHmac(var1.getH1())) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCommitHMACFailed));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            } else {
               var4 = var1.getPv();
               int var3;
               BigIntegerCrypto var8;
               if (this.pubKey != ZrtpConstants.SupportedPubKeys.DH2K && this.pubKey != ZrtpConstants.SupportedPubKeys.DH3K) {
                  if (this.pubKey != ZrtpConstants.SupportedPubKeys.EC25 && this.pubKey != ZrtpConstants.SupportedPubKeys.EC38) {
                     if (this.pubKey != ZrtpConstants.SupportedPubKeys.E255) {
                        var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                        return null;
                     }

                     var3 = this.pubKey.pubKeySize;
                     this.pubKey.dhContext.init(this.ecKeyPair.getPrivate());
                     var8 = this.pubKey.dhContext.calculateAgreement(new Djb25519PublicKeyParameters(var4));
                     this.DHss = var8.toByteArray();
                     this.pubKey.dhContext.clear();
                     var8.zeroize();
                  } else {
                     byte[] var5 = new byte[var4.length + 1];
                     var5[0] = 4;
                     System.arraycopy(var4, 0, var5, 1, var4.length);
                     ECPoint var10 = this.pubKey.curve.decodePoint(var5);
                     var3 = this.pubKey.pubKeySize / 2;
                     this.pubKey.dhContext.init(this.ecKeyPair.getPrivate());
                     var8 = this.pubKey.dhContext.calculateAgreement(new ECPublicKeyParameters(var10, (ECDomainParameters)null));
                     this.DHss = var8.toByteArray();
                     this.pubKey.dhContext.clear();
                     var8.zeroize();
                  }
               } else {
                  var8 = new BigIntegerCrypto(1, var4);
                  if (!this.checkPubKey(var8, this.pubKey)) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.DHErrorWrongPV;
                     return null;
                  }

                  this.pubKey.dhContext.init(this.dhKeyPair.getPrivate());
                  DHPublicKeyParameters var9 = new DHPublicKeyParameters(var8, this.pubKey.specDh);
                  var3 = this.pubKey.pubKeySize;
                  var8 = this.pubKey.dhContext.calculateAgreement(var9);
                  this.DHss = var8.toByteArray();
                  this.pubKey.dhContext.clear();
                  var8.zeroize();
               }

               var4 = this.DHss;
               if (var4.length != var3) {
                  var4 = this.adjustBigBytes(var4, var3);
                  this.DHss = var4;
                  if (var4 == null) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                     return null;
                  }
               }

               this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
               this.hashCtxFunction.doFinal(this.messageHash, 0);
               this.hashCtxFunction = null;
               this.generateKeysResponder(var1);
               this.zrtpConfirm1.setMessageType(ZrtpConstants.Confirm1Msg);
               if (this.zidRec.isSasVerified() && !this.paranoidMode) {
                  this.zrtpConfirm1.setSASFlag();
               }

               this.zrtpConfirm1.setExpTime(-1);
               this.zrtpConfirm1.setIv(this.randomIV);
               this.zrtpConfirm1.setHashH0(this.field_9);
               if (this.enrollmentMode) {
                  this.computePBXSecret();
                  this.zrtpConfirm1.setPBXEnrollment();
                  this.zidRec.setMiTMData(this.pbxSecretTmp);
               }

               var4 = this.zrtpConfirm1.getDataToSecure();

               try {
                  this.cipher.cipher.init(true, new ParametersWithIV(new KeyParameter(this.zrtpKeyR, 0, this.cipher.keyLength), this.randomIV));
                  var3 = this.cipher.cipher.processBytes(var4, 0, var4.length, var4, 0);
                  this.cipher.cipher.doFinal(var4, var3);
               } catch (Exception var6) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               byte[] var7 = this.computeHmac(this.hmacKeyR, this.hashLength, var4, var4.length);
               this.zrtpConfirm1.setDataToSecure(var4);
               this.zrtpConfirm1.setHmac(var7);
               this.storeMsgTemp(var1);
               return this.zrtpConfirm1;
            }
         }
      }
   }

   protected ZrtpPacketConfirm prepareConfirm1MultiStream(ZrtpPacketCommit var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRespCommitReceived));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         System.arraycopy(var1.getH2(), 0, this.peerH2, 0, 32);
         byte[] var4 = new byte[64];
         this.hashFunctionImpl.update(this.peerH2, 0, 32);
         this.hashFunctionImpl.doFinal(var4, 0);
         if (ZrtpUtils.byteArrayCompare(var4, this.peerH3, 32) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.IgnorePacket;
            return null;
         } else if (!this.checkMsgHmac(this.peerH2)) {
            this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereCommitHMACFailed));
            var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
            return null;
         } else if (var1.getPubKey() != ZrtpConstants.SupportedPubKeys.MULT) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppPKExchange;
            return null;
         } else {
            ZrtpConstants.SupportedSymCiphers var7 = var1.getCipher();
            this.cipher = var7;
            if (var7 == null) {
               var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppCiphertype;
               return null;
            } else {
               ZrtpConstants.SupportedAuthLengths var8 = var1.getAuthlen();
               this.authLength = var8;
               if (var8 == null) {
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppSRTPAuthTag;
                  return null;
               } else {
                  ZrtpConstants.SupportedHashes var9 = var1.getHash();
                  if (var9 == null) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppHashType;
                     return null;
                  } else {
                     if (var9 != this.hash) {
                        this.hash = var9;
                        this.setNegotiatedHash(var9);
                     }

                     this.myRole = ZrtpCallback.Role.Responder;
                     this.hashCtxFunction.reset();
                     this.hashCtxFunction.update(this.currentHelloPacket.getHeaderBase(), 0, this.currentHelloPacket.getLength() * 4);
                     this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
                     this.hashCtxFunction.doFinal(this.messageHash, 0);
                     this.hashCtxFunction = null;
                     this.generateKeysMultiStream();
                     this.zrtpConfirm1.setMessageType(ZrtpConstants.Confirm1Msg);
                     this.zrtpConfirm1.setExpTime(-1);
                     this.zrtpConfirm1.setIv(this.randomIV);
                     this.zrtpConfirm1.setHashH0(this.field_9);
                     var4 = this.zrtpConfirm1.getDataToSecure();

                     try {
                        this.cipher.cipher.init(true, new ParametersWithIV(new KeyParameter(this.zrtpKeyR, 0, this.cipher.keyLength), this.randomIV));
                        int var3 = this.cipher.cipher.processBytes(var4, 0, var4.length, var4, 0);
                        this.cipher.cipher.doFinal(var4, var3);
                     } catch (Exception var5) {
                        this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                        var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                        return null;
                     }

                     byte[] var6 = this.computeHmac(this.hmacKeyR, this.hashLength, var4, var4.length);
                     this.zrtpConfirm1.setDataToSecure(var4);
                     this.zrtpConfirm1.setHmac(var6);
                     this.storeMsgTemp(var1);
                     return this.zrtpConfirm1;
                  }
               }
            }
         }
      }
   }

   protected ZrtpPacketConfirm prepareConfirm2(ZrtpPacketConfirm var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoInitConf1Received));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         byte[] var5 = var1.getDataToSecure();
         if (ZrtpUtils.byteArrayCompare(this.computeHmac(this.hmacKeyR, this.hashLength, var5, var5.length), var1.getHmac(), 8) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.ConfirmHMACWrong;
            return null;
         } else {
            int var3;
            try {
               this.cipher.cipher.init(false, new ParametersWithIV(new KeyParameter(this.zrtpKeyR, 0, this.cipher.keyLength), var1.getIv()));
               var3 = this.cipher.cipher.processBytes(var5, 0, var5.length, var5, 0);
               this.cipher.cipher.doFinal(var5, var3);
            } catch (Exception var7) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            }

            var1.setDataToSecure(var5);
            if (!this.checkMsgHmac(var1.getHashH0())) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereDH1HMACFailed));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            } else {
               var3 = var1.getSignatureLength();
               this.signatureLength = var3;
               if (this.signSasSeen && var3 > 0) {
                  this.signatureData = var1.getSignatureData();
                  this.callback.checkSASSignature(this.sasHash);
               }

               if (!var1.isSASFlag() || this.paranoidMode) {
                  this.zidRec.resetSasVerified();
               }

               boolean var4 = this.zidRec.isSasVerified();
               this.zidRec.setNewRs1(this.newRs1, -1);
               this.zrtpConfirm2.setMessageType(ZrtpConstants.Confirm2Msg);
               this.zrtpConfirm2.setHashH0(this.field_9);
               if (var4) {
                  this.zrtpConfirm2.setSASFlag();
               }

               this.zrtpConfirm2.setExpTime(-1);
               this.zrtpConfirm2.setIv(this.randomIV);
               if (this.enrollmentMode || this.enableMitmEnrollment && var1.isPBXEnrollment()) {
                  this.computePBXSecret();
                  if (this.enrollmentMode) {
                     this.zrtpConfirm2.setPBXEnrollment();
                     this.zidRec.setMiTMData(this.pbxSecretTmp);
                  }
               }

               ZidFile.getInstance().saveRecord(this.zidRec);
               var5 = this.zrtpConfirm2.getDataToSecure();

               try {
                  this.cipher.cipher.init(true, new ParametersWithIV(new KeyParameter(this.zrtpKeyI, 0, this.cipher.keyLength), this.randomIV));
                  var3 = this.cipher.cipher.processBytes(var5, 0, var5.length, var5, 0);
                  this.cipher.cipher.doFinal(var5, var3);
               } catch (Exception var6) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               byte[] var8 = this.computeHmac(this.hmacKeyI, this.hashLength, var5, var5.length);
               this.zrtpConfirm2.setDataToSecure(var5);
               this.zrtpConfirm2.setHmac(var8);
               ZrtpCallback var9 = this.callback;
               StringBuilder var10 = new StringBuilder();
               var10.append(this.cipher.readable);
               var10.append("/");
               var10.append(this.pubKey);
               var9.srtpSecretsOn(var10.toString(), this.SAS, var4);
               if (this.enableMitmEnrollment && var1.isPBXEnrollment()) {
                  this.callback.zrtpAskEnrollment(ZrtpCodes.InfoEnrollment.EnrollmentRequest);
               }

               return this.zrtpConfirm2;
            }
         }
      }
   }

   protected ZrtpPacketConfirm prepareConfirm2MultiStream(ZrtpPacketConfirm var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoInitConf1Received));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         this.hashCtxFunction.doFinal(this.messageHash, 0);
         this.hashCtxFunction = null;
         this.myRole = ZrtpCallback.Role.Initiator;
         this.generateKeysMultiStream();
         byte[] var4 = var1.getDataToSecure();
         if (ZrtpUtils.byteArrayCompare(this.computeHmac(this.hmacKeyR, this.hashLength, var4, var4.length), var1.getHmac(), 8) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.ConfirmHMACWrong;
            return null;
         } else {
            int var3;
            try {
               this.cipher.cipher.init(false, new ParametersWithIV(new KeyParameter(this.zrtpKeyR, 0, this.cipher.keyLength), var1.getIv()));
               var3 = this.cipher.cipher.processBytes(var4, 0, var4.length, var4, 0);
               this.cipher.cipher.doFinal(var4, var3);
            } catch (Exception var6) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            }

            var1.setDataToSecure(var4);
            var4 = new byte[64];
            this.hashFunctionImpl.update(var1.getHashH0(), 0, 32);
            this.hashFunctionImpl.doFinal(var4, 0);
            this.hashFunctionImpl.update(var4, 0, 32);
            this.hashFunctionImpl.doFinal(this.peerH2, 0);
            if (!this.checkMsgHmac(this.peerH2)) {
               this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereHelloHMACFailed));
               var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
               return null;
            } else {
               this.zrtpConfirm2.setMessageType(ZrtpConstants.Confirm2Msg);
               this.zrtpConfirm2.setHashH0(this.field_9);
               this.zrtpConfirm2.setExpTime(-1);
               this.zrtpConfirm2.setIv(this.randomIV);
               byte[] var7 = this.zrtpConfirm2.getDataToSecure();

               try {
                  this.cipher.cipher.init(true, new ParametersWithIV(new KeyParameter(this.zrtpKeyI, 0, this.cipher.keyLength), this.randomIV));
                  var3 = this.cipher.cipher.processBytes(var7, 0, var7.length, var7, 0);
                  this.cipher.cipher.doFinal(var7, var3);
               } catch (Exception var5) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               byte[] var8 = this.computeHmac(this.hmacKeyI, this.hashLength, var7, var7.length);
               this.zrtpConfirm2.setDataToSecure(var7);
               this.zrtpConfirm2.setHmac(var8);
               this.callback.srtpSecretsOn(this.cipher.readable, (String)null, true);
               return this.zrtpConfirm2;
            }
         }
      }
   }

   protected ZrtpPacketDHPart prepareDHPart1(ZrtpPacketCommit var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoRespCommitReceived));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         System.arraycopy(var1.getH2(), 0, this.peerH2, 0, 32);
         byte[] var3 = new byte[64];
         this.hashFunctionImpl.update(this.peerH2, 0, 32);
         this.hashFunctionImpl.doFinal(var3, 0);
         if (ZrtpUtils.byteArrayCompare(var3, this.peerH3, 32) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.IgnorePacket;
            return null;
         } else if (!this.checkMsgHmac(this.peerH2)) {
            this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereHelloHMACFailed));
            var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
            return null;
         } else {
            ZrtpConstants.SupportedSymCiphers var5 = var1.getCipher();
            this.cipher = var5;
            if (var5 == null) {
               var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppCiphertype;
               return null;
            } else {
               ZrtpConstants.SupportedAuthLengths var6 = var1.getAuthlen();
               this.authLength = var6;
               if (var6 == null) {
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppSRTPAuthTag;
                  return null;
               } else {
                  ZrtpConstants.SupportedHashes var7 = var1.getHash();
                  if (var7 == null) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppHashType;
                     return null;
                  } else {
                     if (var7 != this.hash) {
                        this.hash = var7;
                        this.setNegotiatedHash(var7);
                        this.computeSharedSecretSet();
                     }

                     ZrtpConstants.SupportedPubKeys var8 = var1.getPubKey();
                     if (var8 == null) {
                        var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppPKExchange;
                        return null;
                     } else {
                        if (var8 != this.pubKey) {
                           this.pubKey = var8;
                           if (!this.fillPubKey()) {
                              var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                              return null;
                           }
                        }

                        if (this.pubKey == ZrtpConstants.SupportedPubKeys.EC38 && this.hash != ZrtpConstants.SupportedHashes.S384) {
                           var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppHashType;
                           return null;
                        } else {
                           ZrtpConstants.SupportedSASTypes var9 = var1.getSas();
                           this.sasType = var9;
                           if (var9 == null) {
                              var2[0] = ZrtpCodes.ZrtpErrorCodes.UnsuppSASScheme;
                              return null;
                           } else {
                              this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoDH1DHGenerated));
                              this.zrtpDH1.setPubKeyType(this.pubKey);
                              this.zrtpDH1.setMessageType(ZrtpConstants.DHPart1Msg);
                              this.zrtpDH1.setRs1Id(this.rs1IDr);
                              this.zrtpDH1.setRs2Id(this.rs2IDr);
                              this.zrtpDH1.setAuxSecretId(this.auxSecretIDr);
                              this.zrtpDH1.setPbxSecretId(this.pbxSecretIDr);
                              this.zrtpDH1.setPv(this.pubKeyBytes);
                              this.zrtpDH1.setH1(this.field_10);
                              byte[] var4 = this.computeMsgHmac(this.field_9, this.zrtpDH1);
                              this.zrtpDH1.setHMAC(var4);
                              this.myRole = ZrtpCallback.Role.Responder;
                              this.peerHvi = var1.getHvi();
                              this.hashCtxFunction.reset();
                              this.hashCtxFunction.update(this.currentHelloPacket.getHeaderBase(), 0, this.currentHelloPacket.getLength() * 4);
                              this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
                              this.hashCtxFunction.update(this.zrtpDH1.getHeaderBase(), 0, this.zrtpDH1.getLength() * 4);
                              this.storeMsgTemp(var1);
                              return this.zrtpDH1;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected ZrtpPacketDHPart prepareDHPart2(ZrtpPacketDHPart var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      this.sendInfo(ZrtpCodes.MessageSeverity.Info, EnumSet.of(ZrtpCodes.InfoCodes.InfoInitDH1Received));
      if (!var1.isLengthOk()) {
         var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
         return null;
      } else {
         this.hashFunctionImpl.update(var1.getH1(), 0, 32);
         this.hashFunctionImpl.doFinal(this.peerH2, 0);
         byte[] var4 = new byte[64];
         this.hashFunctionImpl.update(this.peerH2, 0, 32);
         this.hashFunctionImpl.doFinal(var4, 0);
         if (ZrtpUtils.byteArrayCompare(var4, this.peerH3, 32) != 0) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.IgnorePacket;
            return null;
         } else if (!this.checkMsgHmac(this.peerH2)) {
            this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereHelloHMACFailed));
            var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
            return null;
         } else {
            var4 = var1.getPv();
            int var3;
            BigIntegerCrypto var6;
            if (this.pubKey != ZrtpConstants.SupportedPubKeys.DH2K && this.pubKey != ZrtpConstants.SupportedPubKeys.DH3K) {
               if (this.pubKey != ZrtpConstants.SupportedPubKeys.EC25 && this.pubKey != ZrtpConstants.SupportedPubKeys.EC38) {
                  if (this.pubKey != ZrtpConstants.SupportedPubKeys.E255) {
                     var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                     return null;
                  }

                  var3 = this.pubKey.pubKeySize;
                  this.pubKey.dhContext.init(this.ecKeyPair.getPrivate());
                  var6 = this.pubKey.dhContext.calculateAgreement(new Djb25519PublicKeyParameters(var4));
                  this.DHss = var6.toByteArray();
                  this.pubKey.dhContext.clear();
                  var6.zeroize();
               } else {
                  byte[] var5 = new byte[var4.length + 1];
                  var5[0] = 4;
                  System.arraycopy(var4, 0, var5, 1, var4.length);
                  ECPoint var8 = this.pubKey.curve.decodePoint(var5);
                  var3 = this.pubKey.pubKeySize / 2;
                  this.pubKey.dhContext.init(this.ecKeyPair.getPrivate());
                  var6 = this.pubKey.dhContext.calculateAgreement(new ECPublicKeyParameters(var8, (ECDomainParameters)null));
                  this.DHss = var6.toByteArray();
                  this.pubKey.dhContext.clear();
                  var6.zeroize();
               }
            } else {
               var6 = new BigIntegerCrypto(1, var4);
               if (!this.checkPubKey(var6, this.pubKey)) {
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.DHErrorWrongPV;
                  return null;
               }

               this.pubKey.dhContext.init(this.dhKeyPair.getPrivate());
               DHPublicKeyParameters var7 = new DHPublicKeyParameters(var6, this.pubKey.specDh);
               var3 = this.pubKey.pubKeySize;
               var6 = this.pubKey.dhContext.calculateAgreement(var7);
               this.DHss = var6.toByteArray();
               this.pubKey.dhContext.clear();
               var6.zeroize();
            }

            var4 = this.DHss;
            if (var4.length != var3) {
               var4 = this.adjustBigBytes(var4, var3);
               this.DHss = var4;
               if (var4 == null) {
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }
            }

            this.myRole = ZrtpCallback.Role.Initiator;
            this.hashCtxFunction.update(var1.getHeaderBase(), 0, var1.getLength() * 4);
            this.hashCtxFunction.update(this.zrtpDH2.getHeaderBase(), 0, this.zrtpDH2.getLength() * 4);
            this.hashCtxFunction.doFinal(this.messageHash, 0);
            this.hashCtxFunction = null;
            this.generateKeysInitiator(var1);
            this.storeMsgTemp(var1);
            return this.zrtpDH2;
         }
      }
   }

   protected ZrtpPacketError prepareError(ZrtpCodes.ZrtpErrorCodes var1) {
      this.zrtpError.setErrorCode(var1.value);
      return this.zrtpError;
   }

   protected ZrtpPacketErrorAck prepareErrorAck(ZrtpPacketError var1) {
      int var3 = var1.getErrorCode();
      ZrtpCodes.ZrtpErrorCodes[] var6 = ZrtpCodes.ZrtpErrorCodes.values();
      int var4 = var6.length;

      for(int var2 = 0; var2 < var4; ++var2) {
         ZrtpCodes.ZrtpErrorCodes var5 = var6[var2];
         if (var5.value == var3) {
            this.sendInfo(ZrtpCodes.MessageSeverity.ZrtpError, EnumSet.of(var5));
            break;
         }
      }

      return this.zrtpErrorAck;
   }

   protected ZrtpPacketHello prepareHello() {
      return this.currentHelloPacket;
   }

   protected ZrtpPacketHelloAck prepareHelloAck() {
      return this.zrtpHelloAck;
   }

   protected ZrtpPacketPingAck preparePingAck(ZrtpPacketPing var1) {
      if (var1.getLength() != 6) {
         return null;
      } else {
         this.zrtpPingAck.setLocalEpHash(this.zid);
         this.zrtpPingAck.setRemoteEpHash(var1.getEpHash());
         this.zrtpPingAck.setPeerSSRC(this.peerSSRC);
         return this.zrtpPingAck;
      }
   }

   protected ZrtpPacketRelayAck prepareRelayAck(ZrtpPacketSASRelay var1, ZrtpCodes.ZrtpErrorCodes[] var2) {
      if (this.mitmSeen && !this.paranoidMode) {
         if (!var1.isLengthOk()) {
            var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
            return null;
         } else {
            byte[] var7;
            byte[] var8;
            if (this.myRole == ZrtpCallback.Role.Responder) {
               var7 = this.hmacKeyI;
               var8 = this.zrtpKeyI;
            } else {
               var7 = this.hmacKeyR;
               var8 = this.zrtpKeyR;
            }

            byte[] var9 = var1.getDataToSecure();
            if (ZrtpUtils.byteArrayCompare(this.computeHmac(var7, this.hashLength, var9, var9.length), var1.getHmac(), 8) != 0) {
               var2[0] = ZrtpCodes.ZrtpErrorCodes.ConfirmHMACWrong;
               return null;
            } else {
               int var3;
               try {
                  this.cipher.cipher.init(false, new ParametersWithIV(new KeyParameter(var8, 0, this.cipher.keyLength), var1.getIv()));
                  var3 = this.cipher.cipher.processBytes(var9, 0, var9.length, var9, 0);
                  this.cipher.cipher.doFinal(var9, var3);
               } catch (Exception var10) {
                  this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
                  var2[0] = ZrtpCodes.ZrtpErrorCodes.CriticalSWError;
                  return null;
               }

               var1.setDataToSecure(var9);
               ZrtpConstants.SupportedSASTypes var14 = var1.getSas();
               byte[] var12 = var1.getTrustedSas();
               boolean var5 = true;
               int var6 = var12.length;
               var3 = 0;

               boolean var4;
               while(true) {
                  var4 = var5;
                  if (var3 >= var6) {
                     break;
                  }

                  if (var12[var3] != 0) {
                     var4 = false;
                     break;
                  }

                  ++var3;
               }

               String var11 = "/SASviaMitM";
               if (var4 || !this.peerIsEnrolled) {
                  var12 = this.sasHash;
                  var11 = "/MitM";
               }

               StringBuilder var15;
               if (var14 != ZrtpConstants.SupportedSASTypes.B32 && var14 != ZrtpConstants.SupportedSASTypes.B32E) {
                  var15 = new StringBuilder();
                  var15.append(ZrtpConstants.sas256WordsEven[var12[0]]);
                  var15.append(":");
                  var15.append(ZrtpConstants.sas256WordsOdd[var12[1]]);
                  this.SAS = var15.toString();
               } else {
                  var8 = new byte[]{var12[0], var12[1], (byte)(var12[2] & 240), 0};
                  if (var14 == ZrtpConstants.SupportedSASTypes.B32) {
                     this.SAS = Base32.binary2ascii(var8, 20);
                  } else {
                     this.SAS = EmojiBase32.binary2ascii(var8, 20);
                  }
               }

               ZrtpCallback var13 = this.callback;
               var15 = new StringBuilder();
               var15.append(this.cipher.readable);
               var15.append("/");
               var15.append(this.pubKey);
               var15.append(var11);
               var13.srtpSecretsOn(var15.toString(), this.SAS, false);
               return this.zrtpRelayAck;
            }
         }
      } else {
         return this.zrtpRelayAck;
      }
   }

   public void processTimeout() {
      ZrtpStateClass var1 = this.stateEngine;
      if (var1 != null) {
         ZrtpStateClass.Event var2 = var1.new Event(ZrtpStateClass.EventDataType.Timer, (byte[])null);
         this.stateEngine.processEvent(var2);
      }

   }

   public void processZrtpMessage(byte[] var1, int var2) {
      this.peerSSRC = var2;
      ZrtpStateClass var3 = this.stateEngine;
      if (var3 != null) {
         ZrtpStateClass.Event var4 = var3.new Event(ZrtpStateClass.EventDataType.ZrtpPacket, var1);
         this.stateEngine.processEvent(var4);
      }

   }

   public void resetSASVerified() {
      this.zidRec.resetSasVerified();
      ZidFile.getInstance().saveRecord(this.zidRec);
   }

   protected void sendInfo(ZrtpCodes.MessageSeverity var1, EnumSet var2) {
      if (var1 == ZrtpCodes.MessageSeverity.Info && var2 == EnumSet.of(ZrtpCodes.InfoCodes.InfoSecureStateOn)) {
         Arrays.fill(this.srtpKeyI, (byte)0);
         Arrays.fill(this.srtpSaltI, (byte)0);
         Arrays.fill(this.srtpKeyR, (byte)0);
         Arrays.fill(this.srtpSaltR, (byte)0);
      }

      this.callback.sendInfo(var1, var2);
   }

   protected boolean sendPacketZRTP(ZrtpPacketBase var1) {
      return var1 != null && this.callback.sendDataZRTP(var1.getHeaderBase());
   }

   public boolean sendSASRelayPacket(byte[] var1, ZrtpConstants.SupportedSASTypes var2) {
      byte[] var4;
      byte[] var5;
      if (this.myRole == ZrtpCallback.Role.Responder) {
         var4 = this.hmacKeyR;
         var5 = this.zrtpKeyR;
      } else {
         var4 = this.hmacKeyI;
         var5 = this.zrtpKeyI;
      }

      this.secRand.nextBytes(this.randomIV);
      this.zrtpSasRelay.setIv(this.randomIV);
      this.zrtpSasRelay.setTrustedSas(var1);
      this.zrtpSasRelay.setSasType(var2.name);
      var1 = this.zrtpSasRelay.getDataToSecure();

      try {
         this.cipher.cipher.init(true, new ParametersWithIV(new KeyParameter(var5, 0, this.cipher.keyLength), this.randomIV));
         int var3 = this.cipher.cipher.processBytes(var1, 0, var1.length, var1, 0);
         this.cipher.cipher.doFinal(var1, var3);
      } catch (Exception var6) {
         this.sendInfo(ZrtpCodes.MessageSeverity.Severe, EnumSet.of(ZrtpCodes.SevereCodes.SevereSecurityException));
         return false;
      }

      byte[] var7 = this.computeHmac(var4, this.hashLength, var1, var1.length);
      this.zrtpSasRelay.setDataToSecure(var1);
      this.zrtpSasRelay.setHmac(var7);
      this.stateEngine.sendSASRelay(this.zrtpSasRelay);
      return true;
   }

   public void setAuxSecret(byte[] var1) {
   }

   public void setEnrollmentMode(boolean var1) {
      this.enrollmentMode = var1;
   }

   public void setMultiStrParams(byte[] var1) {
      ZrtpConstants.SupportedHashes[] var4 = ZrtpConstants.SupportedHashes.values();
      int var3 = var4.length;

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         ZrtpConstants.SupportedHashes var5 = var4[var2];
         if (var5.ordinal() == (var1[0] & 255)) {
            this.hash = var5;
            break;
         }
      }

      this.setNegotiatedHash(this.hash);
      this.zrtpSession = new byte[this.hashLength];
      ZrtpConstants.SupportedAuthLengths[] var6 = ZrtpConstants.SupportedAuthLengths.values();
      var3 = var6.length;

      for(var2 = 0; var2 < var3; ++var2) {
         ZrtpConstants.SupportedAuthLengths var8 = var6[var2];
         if (var8.ordinal() == (var1[1] & 255)) {
            this.authLength = var8;
            break;
         }
      }

      ZrtpConstants.SupportedSymCiphers[] var7 = ZrtpConstants.SupportedSymCiphers.values();
      var3 = var7.length;

      for(var2 = 0; var2 < var3; ++var2) {
         ZrtpConstants.SupportedSymCiphers var9 = var7[var2];
         if (var9.ordinal() == (var1[2] & 255)) {
            this.cipher = var9;
            break;
         }
      }

      System.arraycopy(var1, 3, this.zrtpSession, 0, this.hashLength);
      this.multiStream = true;
      this.stateEngine.setMultiStream(true);
   }

   public void setRs2Valid() {
      ZidRecord var1 = this.zidRec;
      if (var1 != null) {
         var1.setRs2Valid();
         ZidFile.getInstance().saveRecord(this.zidRec);
      }

   }

   public boolean setSignatureData(byte[] var1) {
      if (var1.length % 4 != 0) {
         return false;
      } else {
         ZrtpPacketConfirm var2;
         if (this.myRole == ZrtpCallback.Role.Responder) {
            var2 = this.zrtpConfirm1;
         } else {
            var2 = this.zrtpConfirm2;
         }

         var2.setSignatureLength(var1.length / 4);
         return var2.setSignatureData(var1);
      }
   }

   protected void srtpSecretsOff(ZrtpCallback.EnableSecurity var1) {
      this.callback.srtpSecretsOff(var1);
   }

   protected boolean srtpSecretsReady(ZrtpCallback.EnableSecurity var1) {
      ZrtpSrtpSecrets var2 = new ZrtpSrtpSecrets();
      var2.symEncAlgorithm = this.cipher.algo;
      var2.keyInitiator = this.srtpKeyI;
      var2.initKeyLen = this.cipher.keyLength * 8;
      var2.saltInitiator = this.srtpSaltI;
      var2.initSaltLen = 112;
      var2.keyResponder = this.srtpKeyR;
      var2.respKeyLen = this.cipher.keyLength * 8;
      var2.saltResponder = this.srtpSaltR;
      var2.respSaltLen = 112;
      var2.authAlgorithm = this.authLength.algo;
      var2.srtpAuthTagLen = this.authLength.length;
      var2.setRole(this.myRole);
      return this.callback.srtpSecretsReady(var2, var1);
   }

   public void startZrtpEngine() {
      ZrtpStateClass var1 = this.stateEngine;
      if (var1 != null && var1.isInState(ZrtpStateClass.ZrtpStates.Initial)) {
         var1 = this.stateEngine;
         ZrtpStateClass.Event var2 = var1.new Event(ZrtpStateClass.EventDataType.ZrtpInitial, (byte[])null);
         this.stateEngine.processEvent(var2);
      }

   }

   public void stopZrtp() {
      ZrtpStateClass var1 = this.stateEngine;
      if (var1 != null) {
         ZrtpStateClass.Event var2 = var1.new Event(ZrtpStateClass.EventDataType.ZrtpClose, (byte[])null);
         this.stateEngine.processEvent(var2);
      }

   }

   protected boolean verifyH2(ZrtpPacketCommit var1) {
      byte[] var3 = new byte[64];
      Digest var4 = this.hashFunctionImpl;
      byte[] var5 = var1.getH2();
      boolean var2 = false;
      var4.update(var5, 0, 32);
      this.hashFunctionImpl.doFinal(var3, 0);
      if (ZrtpUtils.byteArrayCompare(var3, this.peerH3, 32) == 0) {
         var2 = true;
      }

      return var2;
   }

   protected void zrtpNegotiationFailed(ZrtpCodes.MessageSeverity var1, EnumSet var2) {
      this.callback.zrtpNegotiationFailed(var1, var2);
   }

   protected void zrtpNotSuppOther() {
      this.callback.zrtpNotSuppOther();
   }

   static class HelloPacketVersion {
      byte[] helloHash;
      ZrtpPacketHello packet;
      int version;
   }
}
