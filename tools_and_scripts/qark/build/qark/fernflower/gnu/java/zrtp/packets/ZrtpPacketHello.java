package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConfigure;
import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ZrtpPacketHello extends ZrtpPacketBase {
   private static final int CLIENT_ID_OFFSET = 16;
   private static final int FLAG_LENGTH_OFFSET = 76;
   private static final int HASH_H3_OFFSET = 32;
   private static final byte HELLO_MITM_FLAG = 32;
   private static final byte SAS_SIGN_FLAG = 64;
   private static final int VARIABLE_OFFSET = 80;
   private static final int VERSION_OFFSET = 12;
   private static final int ZID_OFFSET = 64;
   private static final int ZRTP_HELLO_FIX_LENGTH = 17;
   private int computedLength;
   private byte helloFlags = 0;
   private int helloLength = 84;
   private int nAuth;
   private int nCipher;
   private int nHash;
   private int nPubkey;
   private int nSas;
   private int oAuth;
   private int oCipher;
   private int oHash;
   private int oHmac;
   private int oPubkey;
   private int oSas;
   private ZrtpConstants.SupportedSymCiphers selectedCipher;
   private ZrtpConstants.SupportedHashes selectedHash;

   public ZrtpPacketHello() {
      super((byte[])null);
   }

   public ZrtpPacketHello(byte[] var1) {
      super(var1);
      this.helloFlags = this.packetBuffer[76];
      this.nHash = this.packetBuffer[77] & 7;
      byte var2 = this.packetBuffer[78];
      this.nCipher = (var2 & 112) >> 4;
      this.nAuth = var2 & 7;
      byte var3 = this.packetBuffer[79];
      int var8 = (var3 & 112) >> 4;
      this.nPubkey = var8;
      int var9 = var3 & 7;
      this.nSas = var9;
      this.oHash = 80;
      int var4 = this.nHash;
      int var6 = 80 + var4 * 4;
      this.oCipher = var6;
      int var5 = this.nCipher;
      int var7 = var6 + var5 * 4;
      this.oAuth = var7;
      var6 = this.nAuth;
      var7 += var6 * 4;
      this.oPubkey = var7;
      var7 += var8 * 4;
      this.oSas = var7;
      this.oHmac = var7 + var9 * 4;
      this.computedLength = var4 + var5 + var6 + var8 + var9 + 3 + 17 + 2;
   }

   private ZrtpConstants.SupportedSymCiphers getStrongCipherOffered() {
      byte[] var3 = ZrtpConstants.SupportedSymCiphers.AES3.name;
      byte[] var4 = ZrtpConstants.SupportedSymCiphers.TWO3.name;

      for(int var1 = 0; var1 < this.nCipher; ++var1) {
         int var2 = this.oCipher + var1 * 4;
         if (var3[0] == this.packetBuffer[var2] && var3[1] == this.packetBuffer[var2 + 1] && var3[2] == this.packetBuffer[var2 + 2] && var3[3] == this.packetBuffer[var2 + 3]) {
            return ZrtpConstants.SupportedSymCiphers.AES3;
         }

         if (var4[0] == this.packetBuffer[var2] && var4[1] == this.packetBuffer[var2 + 1] && var4[2] == this.packetBuffer[var2 + 2] && var4[3] == this.packetBuffer[var2 + 3]) {
            return ZrtpConstants.SupportedSymCiphers.TWO3;
         }
      }

      return null;
   }

   private ZrtpConstants.SupportedHashes getStrongHashOffered() {
      byte[] var3 = ZrtpConstants.SupportedHashes.S384.name;

      for(int var1 = 0; var1 < this.nHash; ++var1) {
         int var2 = this.oHash + var1 * 4;
         if (var3[0] == this.packetBuffer[var2] && var3[1] == this.packetBuffer[var2 + 1] && var3[2] == this.packetBuffer[var2 + 2] && var3[3] == this.packetBuffer[var2 + 3]) {
            return ZrtpConstants.SupportedHashes.S384;
         }
      }

      return null;
   }

   public final boolean checkMultiStream() {
      if (this.nPubkey == 0) {
         return true;
      } else {
         byte[] var3 = ZrtpConstants.SupportedPubKeys.MULT.name;

         for(int var1 = 0; var1 < this.nPubkey; ++var1) {
            int var2 = this.oPubkey + var1 * 4;
            if (var3[0] == this.packetBuffer[var2] && var3[1] == this.packetBuffer[var2 + 1] && var3[2] == this.packetBuffer[var2 + 2] && var3[3] == this.packetBuffer[var2 + 3]) {
               return true;
            }
         }

         return false;
      }
   }

   public void configureHello(ZrtpConfigure var1) {
      this.nHash = var1.getNumConfiguredHashes();
      this.nCipher = var1.getNumConfiguredSymCiphers();
      this.nPubkey = var1.getNumConfiguredPubKeys();
      this.nSas = var1.getNumConfiguredSasTypes();
      int var2 = var1.getNumConfiguredAuthLengths();
      this.nAuth = var2;
      int var3 = this.helloLength + 8;
      this.helloLength = var3;
      var3 += this.nHash * 4;
      this.helloLength = var3;
      var3 += this.nCipher * 4;
      this.helloLength = var3;
      var3 += this.nPubkey * 4;
      this.helloLength = var3;
      var3 += this.nSas * 4;
      this.helloLength = var3;
      var2 = var3 + var2 * 4;
      this.helloLength = var2;
      this.packetBuffer = new byte[var2];
      Arrays.fill(this.packetBuffer, (byte)0);
      this.oHash = 80;
      var2 = 80 + this.nHash * 4;
      this.oCipher = var2;
      var2 += this.nCipher * 4;
      this.oAuth = var2;
      var2 += this.nAuth * 4;
      this.oPubkey = var2;
      var2 += this.nPubkey * 4;
      this.oSas = var2;
      this.oHmac = var2 + this.nSas * 4;
      this.setZrtpId();
      this.setLength(this.helloLength / 4 - 1);
      this.setMessageType(ZrtpConstants.HelloMsg);
      this.packetBuffer[76] = this.helloFlags;
      this.packetBuffer[77] = (byte)this.nHash;
      var2 = 0;

      Iterator var4;
      for(var4 = var1.hashes().iterator(); var4.hasNext(); ++var2) {
         this.setHashType(var2, ((ZrtpConstants.SupportedHashes)var4.next()).name);
      }

      this.packetBuffer[78] = (byte)(this.nCipher << 4);
      var2 = 0;

      for(var4 = var1.symCiphers().iterator(); var4.hasNext(); ++var2) {
         this.setCipherType(var2, ((ZrtpConstants.SupportedSymCiphers)var4.next()).name);
      }

      byte[] var6 = this.packetBuffer;
      var6[78] |= (byte)this.nAuth;
      var2 = 0;

      for(var4 = var1.authLengths().iterator(); var4.hasNext(); ++var2) {
         this.setAuthLen(var2, ((ZrtpConstants.SupportedAuthLengths)var4.next()).name);
      }

      this.packetBuffer[79] = (byte)(this.nPubkey << 4);
      var2 = 0;

      for(var4 = var1.publicKeyAlgos().iterator(); var4.hasNext(); ++var2) {
         this.setPubKeyType(var2, ((ZrtpConstants.SupportedPubKeys)var4.next()).name);
      }

      var6 = this.packetBuffer;
      var6[79] |= (byte)this.nSas;
      var2 = 0;

      for(Iterator var5 = var1.sasTypes().iterator(); var5.hasNext(); ++var2) {
         this.setSasType(var2, ((ZrtpConstants.SupportedSASTypes)var5.next()).name);
      }

   }

   public final ZrtpConstants.SupportedAuthLengths findBestAuthLen(ZrtpConfigure var1) {
      if (this.nAuth == 0) {
         return ZrtpConstants.SupportedAuthLengths.HS32;
      } else {
         boolean var3 = false;
         boolean var2 = false;
         ArrayList var11 = new ArrayList(this.nAuth + 1);
         ArrayList var10 = new ArrayList(var1.getNumConfiguredAuthLengths() + 1);

         ZrtpConstants.SupportedAuthLengths var12;
         Iterator var14;
         for(var14 = var1.authLengths().iterator(); var14.hasNext(); var10.add(var12)) {
            var12 = (ZrtpConstants.SupportedAuthLengths)var14.next();
            if (var12 == ZrtpConstants.SupportedAuthLengths.HS32) {
               var3 = true;
            }

            if (var12 == ZrtpConstants.SupportedAuthLengths.HS80) {
               var2 = true;
            }
         }

         if (!var3) {
            var10.add(ZrtpConstants.SupportedAuthLengths.HS32);
         }

         if (!var2) {
            var10.add(ZrtpConstants.SupportedAuthLengths.HS80);
         }

         boolean var4 = false;
         var2 = false;

         for(int var16 = 0; var16 < this.nAuth; ++var16) {
            int var8 = this.oAuth + var16 * 4;
            ZrtpConstants.SupportedAuthLengths[] var15 = ZrtpConstants.SupportedAuthLengths.values();
            int var9 = var15.length;

            boolean var7;
            for(int var5 = 0; var5 < var9; var4 = var7) {
               var12 = var15[var5];
               byte[] var13 = var12.name;
               boolean var6 = var2;
               var7 = var4;
               if (var13[0] == this.packetBuffer[var8]) {
                  var6 = var2;
                  var7 = var4;
                  if (var13[1] == this.packetBuffer[var8 + 1]) {
                     var6 = var2;
                     var7 = var4;
                     if (var13[2] == this.packetBuffer[var8 + 2]) {
                        var6 = var2;
                        var7 = var4;
                        if (var13[3] == this.packetBuffer[var8 + 3]) {
                           var11.add(var12);
                           if (var12 == ZrtpConstants.SupportedAuthLengths.HS32) {
                              var2 = true;
                           }

                           var6 = var2;
                           var7 = var4;
                           if (var12 == ZrtpConstants.SupportedAuthLengths.HS80) {
                              var7 = true;
                              var6 = var2;
                           }
                        }
                     }
                  }
               }

               ++var5;
               var2 = var6;
            }
         }

         if (!var2) {
            var11.add(ZrtpConstants.SupportedAuthLengths.HS32);
         }

         if (!var4) {
            var11.add(ZrtpConstants.SupportedAuthLengths.HS80);
         }

         var14 = var11.iterator();

         while(var14.hasNext()) {
            ZrtpConstants.SupportedAuthLengths var17 = (ZrtpConstants.SupportedAuthLengths)var14.next();
            Iterator var18 = var10.iterator();

            while(var18.hasNext()) {
               ZrtpConstants.SupportedAuthLengths var19 = (ZrtpConstants.SupportedAuthLengths)var18.next();
               if (var17 == var19) {
                  return var19;
               }
            }
         }

         return ZrtpConstants.SupportedAuthLengths.HS32;
      }
   }

   public final ZrtpConstants.SupportedSymCiphers findBestCipher(ZrtpConfigure var1, ZrtpConstants.SupportedPubKeys var2) {
      if (this.nCipher != 0 && var2 != ZrtpConstants.SupportedPubKeys.DH2K) {
         boolean var3 = false;
         ArrayList var9 = new ArrayList(this.nCipher + 1);
         ArrayList var14 = new ArrayList(var1.getNumConfiguredSymCiphers() + 1);

         ZrtpConstants.SupportedSymCiphers var10;
         Iterator var12;
         for(var12 = var1.symCiphers().iterator(); var12.hasNext(); var14.add(var10)) {
            var10 = (ZrtpConstants.SupportedSymCiphers)var12.next();
            if (var10 == ZrtpConstants.SupportedSymCiphers.AES1) {
               var3 = true;
            }
         }

         if (!var3) {
            var14.add(ZrtpConstants.SupportedSymCiphers.AES1);
         }

         boolean var4 = false;

         for(int var15 = 0; var15 < this.nCipher; ++var15) {
            int var7 = this.oCipher + var15 * 4;
            ZrtpConstants.SupportedSymCiphers[] var13 = ZrtpConstants.SupportedSymCiphers.values();
            int var8 = var13.length;

            boolean var6;
            for(int var5 = 0; var5 < var8; var4 = var6) {
               var10 = var13[var5];
               byte[] var11 = var10.name;
               var6 = var4;
               if (var11[0] == this.packetBuffer[var7]) {
                  var6 = var4;
                  if (var11[1] == this.packetBuffer[var7 + 1]) {
                     var6 = var4;
                     if (var11[2] == this.packetBuffer[var7 + 2]) {
                        var6 = var4;
                        if (var11[3] == this.packetBuffer[var7 + 3]) {
                           var9.add(var10);
                           var6 = var4;
                           if (var10 == ZrtpConstants.SupportedSymCiphers.AES1) {
                              var6 = true;
                           }
                        }
                     }
                  }
               }

               ++var5;
            }
         }

         if (!var4) {
            var9.add(ZrtpConstants.SupportedSymCiphers.AES1);
         }

         var12 = var9.iterator();

         while(var12.hasNext()) {
            ZrtpConstants.SupportedSymCiphers var16 = (ZrtpConstants.SupportedSymCiphers)var12.next();
            Iterator var17 = var14.iterator();

            while(var17.hasNext()) {
               ZrtpConstants.SupportedSymCiphers var18 = (ZrtpConstants.SupportedSymCiphers)var17.next();
               if (var16 == var18) {
                  return var18;
               }
            }
         }

         return ZrtpConstants.SupportedSymCiphers.AES1;
      } else {
         return ZrtpConstants.SupportedSymCiphers.AES1;
      }
   }

   public final ZrtpConstants.SupportedHashes findBestHash(ZrtpConfigure var1) {
      if (this.nHash == 0) {
         return ZrtpConstants.SupportedHashes.S256;
      } else {
         boolean var2 = false;
         ArrayList var9 = new ArrayList(this.nHash + 1);
         ArrayList var8 = new ArrayList(var1.getNumConfiguredHashes() + 1);

         ZrtpConstants.SupportedHashes var10;
         Iterator var12;
         for(var12 = var1.hashes().iterator(); var12.hasNext(); var8.add(var10)) {
            var10 = (ZrtpConstants.SupportedHashes)var12.next();
            if (var10 == ZrtpConstants.SupportedHashes.S256) {
               var2 = true;
            }
         }

         if (!var2) {
            var8.add(ZrtpConstants.SupportedHashes.S256);
         }

         boolean var3 = false;

         for(int var14 = 0; var14 < this.nHash; ++var14) {
            int var6 = this.oHash + var14 * 4;
            ZrtpConstants.SupportedHashes[] var13 = ZrtpConstants.SupportedHashes.values();
            int var7 = var13.length;

            boolean var5;
            for(int var4 = 0; var4 < var7; var3 = var5) {
               var10 = var13[var4];
               byte[] var11 = var10.name;
               var5 = var3;
               if (var11[0] == this.packetBuffer[var6]) {
                  var5 = var3;
                  if (var11[1] == this.packetBuffer[var6 + 1]) {
                     var5 = var3;
                     if (var11[2] == this.packetBuffer[var6 + 2]) {
                        var5 = var3;
                        if (var11[3] == this.packetBuffer[var6 + 3]) {
                           var9.add(var10);
                           var5 = var3;
                           if (var10 == ZrtpConstants.SupportedHashes.S256) {
                              var5 = true;
                           }
                        }
                     }
                  }
               }

               ++var4;
            }
         }

         if (!var3) {
            var9.add(ZrtpConstants.SupportedHashes.S256);
         }

         var12 = var9.iterator();

         while(var12.hasNext()) {
            ZrtpConstants.SupportedHashes var15 = (ZrtpConstants.SupportedHashes)var12.next();
            Iterator var16 = var8.iterator();

            while(var16.hasNext()) {
               ZrtpConstants.SupportedHashes var17 = (ZrtpConstants.SupportedHashes)var16.next();
               if (var15 == var17) {
                  return var17;
               }
            }
         }

         return ZrtpConstants.SupportedHashes.S256;
      }
   }

   public final ZrtpConstants.SupportedPubKeys findBestPubkey(ZrtpConfigure var1) {
      if (this.nPubkey == 0) {
         this.selectedHash = ZrtpConstants.SupportedHashes.S256;
         return ZrtpConstants.SupportedPubKeys.DH3K;
      } else {
         ZrtpConstants.SupportedPubKeys[] var6 = new ZrtpConstants.SupportedPubKeys[]{ZrtpConstants.SupportedPubKeys.DH2K, ZrtpConstants.SupportedPubKeys.E255, ZrtpConstants.SupportedPubKeys.EC25, ZrtpConstants.SupportedPubKeys.DH3K, ZrtpConstants.SupportedPubKeys.EC38};
         int var2 = var1.getNumConfiguredPubKeys();
         ArrayList var7 = new ArrayList(var2 + 1);
         ArrayList var8 = new ArrayList(var2 + 1);
         Iterator var9 = var1.publicKeyAlgos().iterator();

         while(true) {
            int var3;
            ZrtpConstants.SupportedPubKeys var10;
            byte[] var11;
            do {
               if (!var9.hasNext()) {
                  for(var2 = 0; var2 < this.nPubkey; ++var2) {
                     var3 = this.oPubkey + var2 * 4;
                     var9 = var8.iterator();

                     while(var9.hasNext()) {
                        var10 = (ZrtpConstants.SupportedPubKeys)var9.next();
                        var11 = var10.name;
                        if (var11[0] == this.packetBuffer[var3] && var11[1] == this.packetBuffer[var3 + 1] && var11[2] == this.packetBuffer[var3 + 2] && var11[3] == this.packetBuffer[var3 + 3]) {
                           var7.add(var10);
                           break;
                        }
                     }
                  }

                  if (var7.size() == 0) {
                     this.selectedHash = ZrtpConstants.SupportedHashes.S256;
                     return ZrtpConstants.SupportedPubKeys.DH3K;
                  }

                  ZrtpConstants.SupportedPubKeys var12;
                  if (var7.size() > 1 && var7.get(0) != var8.get(0)) {
                     ZrtpConstants.SupportedPubKeys var13 = (ZrtpConstants.SupportedPubKeys)var8.get(0);
                     var10 = (ZrtpConstants.SupportedPubKeys)var7.get(0);
                     var2 = 0;
                     int var4 = 0;
                     int var5 = var6.length;

                     for(var3 = 0; var3 < var5 && var6[var3] != var13; ++var3) {
                        ++var2;
                     }

                     var5 = var6.length;

                     for(var3 = 0; var3 < var5 && var6[var3] != var10; ++var3) {
                        ++var4;
                     }

                     if (var2 < var4) {
                        var12 = (ZrtpConstants.SupportedPubKeys)var8.get(0);
                     } else {
                        var12 = (ZrtpConstants.SupportedPubKeys)var7.get(0);
                     }
                  } else {
                     var12 = (ZrtpConstants.SupportedPubKeys)var7.get(0);
                  }

                  if (var12 == ZrtpConstants.SupportedPubKeys.EC38) {
                     this.selectedHash = this.getStrongHashOffered();
                     this.selectedCipher = this.getStrongCipherOffered();
                     return var12;
                  }

                  this.selectedHash = this.findBestHash(var1);
                  return var12;
               }

               var10 = (ZrtpConstants.SupportedPubKeys)var9.next();
            } while(var10 == ZrtpConstants.SupportedPubKeys.MULT);

            var11 = var10.name;

            for(var2 = 0; var2 < this.nPubkey; ++var2) {
               var3 = this.oPubkey + var2 * 4;
               if (var11[0] == this.packetBuffer[var3] && var11[1] == this.packetBuffer[var3 + 1] && var11[2] == this.packetBuffer[var3 + 2] && var11[3] == this.packetBuffer[var3 + 3]) {
                  var8.add(var10);
               }
            }
         }
      }
   }

   public final ZrtpConstants.SupportedSASTypes findBestSASType(ZrtpConfigure var1) {
      if (this.nSas == 0) {
         return ZrtpConstants.SupportedSASTypes.B32;
      } else {
         boolean var2 = false;
         ArrayList var9 = new ArrayList(this.nSas + 1);
         ArrayList var8 = new ArrayList(var1.getNumConfiguredSasTypes() + 1);

         ZrtpConstants.SupportedSASTypes var10;
         Iterator var12;
         for(var12 = var1.sasTypes().iterator(); var12.hasNext(); var8.add(var10)) {
            var10 = (ZrtpConstants.SupportedSASTypes)var12.next();
            if (var10 == ZrtpConstants.SupportedSASTypes.B32) {
               var2 = true;
            }
         }

         if (!var2) {
            var8.add(ZrtpConstants.SupportedSASTypes.B32);
         }

         boolean var3 = false;

         for(int var14 = 0; var14 < this.nSas; ++var14) {
            int var6 = this.oSas + var14 * 4;
            ZrtpConstants.SupportedSASTypes[] var13 = ZrtpConstants.SupportedSASTypes.values();
            int var7 = var13.length;

            boolean var5;
            for(int var4 = 0; var4 < var7; var3 = var5) {
               var10 = var13[var4];
               byte[] var11 = var10.name;
               var5 = var3;
               if (var11[0] == this.packetBuffer[var6]) {
                  var5 = var3;
                  if (var11[1] == this.packetBuffer[var6 + 1]) {
                     var5 = var3;
                     if (var11[2] == this.packetBuffer[var6 + 2]) {
                        var5 = var3;
                        if (var11[3] == this.packetBuffer[var6 + 3]) {
                           var9.add(var10);
                           var5 = var3;
                           if (var10 == ZrtpConstants.SupportedSASTypes.B32) {
                              var5 = true;
                           }
                        }
                     }
                  }
               }

               ++var4;
            }
         }

         if (!var3) {
            var9.add(ZrtpConstants.SupportedSASTypes.B32);
         }

         var12 = var9.iterator();

         while(var12.hasNext()) {
            ZrtpConstants.SupportedSASTypes var15 = (ZrtpConstants.SupportedSASTypes)var12.next();
            Iterator var16 = var8.iterator();

            while(var16.hasNext()) {
               ZrtpConstants.SupportedSASTypes var17 = (ZrtpConstants.SupportedSASTypes)var16.next();
               if (var15 == var17) {
                  return var17;
               }
            }
         }

         return ZrtpConstants.SupportedSASTypes.B32;
      }
   }

   public final byte[] getH3() {
      return ZrtpUtils.readRegion(this.packetBuffer, 32, 32);
   }

   public ZrtpConstants.SupportedSymCiphers getSelectedCipher() {
      return this.selectedCipher;
   }

   public ZrtpConstants.SupportedHashes getSelectedHash() {
      return this.selectedHash;
   }

   public final byte[] getVersion() {
      return ZrtpUtils.readRegion(this.packetBuffer, 12, 4);
   }

   public final int getVersionInt() {
      String var4 = new String(this.getVersion());
      int var2 = 0;
      char var1 = var4.charAt(0);
      if (Character.isDigit(var1)) {
         var2 = Character.digit(var1, 10) * 10;
      }

      var1 = var4.charAt(2);
      int var3 = var2;
      if (Character.isDigit(var1)) {
         var3 = var2 + Character.digit(var1, 10);
      }

      return var3;
   }

   public final byte[] getZid() {
      return ZrtpUtils.readRegion(this.packetBuffer, 64, 12);
   }

   public final boolean isLengthOk() {
      return this.computedLength == this.getLength();
   }

   public final boolean isMitmMode() {
      return (this.helloFlags & 32) == 32;
   }

   public final boolean isSameVersion(byte[] var1) {
      for(int var2 = 0; var2 < 4; ++var2) {
         if (this.packetBuffer[var2 + 12] != var1[var2]) {
            return false;
         }
      }

      return true;
   }

   public final boolean isSasSign() {
      return (this.helloFlags & 64) == 64;
   }

   public final void setAuthLen(int var1, byte[] var2) {
      System.arraycopy(var2, 0, this.packetBuffer, this.oAuth + var1 * 4, 4);
   }

   public final void setCipherType(int var1, byte[] var2) {
      System.arraycopy(var2, 0, this.packetBuffer, this.oCipher + var1 * 4, 4);
   }

   public final void setClientId(String var1) {
      byte[] var4;
      try {
         var4 = var1.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var3) {
         var4 = "GNU ZRTP4J 4.1.0".getBytes();
      }

      int var2;
      if (var4.length > 16) {
         var2 = 16;
      } else {
         var2 = var4.length;
      }

      System.arraycopy(var4, 0, this.packetBuffer, 16, var2);
   }

   public final void setH3(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 32, 32);
   }

   public final void setHMAC(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, this.oHmac, 8);
   }

   public final void setHashType(int var1, byte[] var2) {
      System.arraycopy(var2, 0, this.packetBuffer, this.oHash + var1 * 4, 4);
   }

   public final void setMitmMode() {
      byte[] var1 = this.packetBuffer;
      var1[76] = (byte)(var1[76] | 32);
   }

   public final void setPubKeyType(int var1, byte[] var2) {
      System.arraycopy(var2, 0, this.packetBuffer, this.oPubkey + var1 * 4, 4);
   }

   public final void setSasSign() {
      byte[] var1 = this.packetBuffer;
      var1[76] = (byte)(var1[76] | 64);
   }

   public final void setSasType(int var1, byte[] var2) {
      System.arraycopy(var2, 0, this.packetBuffer, this.oSas + var1 * 4, 4);
   }

   public final void setVersion(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 4);
   }

   public final void setZid(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 64, 12);
   }
}
