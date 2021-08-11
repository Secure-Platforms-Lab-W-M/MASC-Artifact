package gnu.java.zrtp;

import java.util.ArrayList;
import java.util.Iterator;

public class ZrtpConfigure {
   public static final int PREFER_NON_NIST = 2;
   public static final int STANDARD = 1;
   private ZrtpConfigure.Data authLengths = new ZrtpConfigure.Data();
   private boolean enableParanoidMode = false;
   private boolean enableSasSignature = false;
   private boolean enableTrustedMitM = false;
   private ZrtpConfigure.Data hashes = new ZrtpConfigure.Data();
   private int policy = 1;
   private ZrtpConfigure.Data publicKeyAlgos = new ZrtpConfigure.Data();
   private ZrtpConfigure.Data sasTypes = new ZrtpConfigure.Data();
   private ZrtpConfigure.Data symCiphers = new ZrtpConfigure.Data();

   public int addAlgo(Enum var1) {
      Class var2 = var1.getDeclaringClass();
      if (var2.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes.addAlgo((Enum)ZrtpConstants.SupportedHashes.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers.addAlgo((Enum)ZrtpConstants.SupportedSymCiphers.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos.addAlgo((Enum)ZrtpConstants.SupportedPubKeys.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes.addAlgo((Enum)ZrtpConstants.SupportedSASTypes.class.cast(var1));
      } else {
         return var2.equals(ZrtpConstants.SupportedAuthLengths.class) ? this.authLengths.addAlgo((Enum)ZrtpConstants.SupportedAuthLengths.class.cast(var1)) : -1;
      }
   }

   public int addAlgoAt(int var1, Enum var2) {
      Class var3 = var2.getDeclaringClass();
      if (var3.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes.addAlgoAt(var1, (Enum)ZrtpConstants.SupportedHashes.class.cast(var2));
      } else if (var3.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers.addAlgoAt(var1, (Enum)ZrtpConstants.SupportedSymCiphers.class.cast(var2));
      } else if (var3.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos.addAlgoAt(var1, (Enum)ZrtpConstants.SupportedPubKeys.class.cast(var2));
      } else if (var3.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes.addAlgoAt(var1, (Enum)ZrtpConstants.SupportedSASTypes.class.cast(var2));
      } else {
         return var3.equals(ZrtpConstants.SupportedAuthLengths.class) ? this.authLengths.addAlgoAt(var1, (Enum)ZrtpConstants.SupportedAuthLengths.class.cast(var2)) : -1;
      }
   }

   public int addAuthLength(ZrtpConstants.SupportedAuthLengths var1) {
      return this.authLengths.addAlgo(var1);
   }

   public int addAuthLengthAt(int var1, ZrtpConstants.SupportedAuthLengths var2) {
      return this.authLengths.addAlgoAt(var1, var2);
   }

   public int addHashAlgo(ZrtpConstants.SupportedHashes var1) {
      return this.hashes.addAlgo(var1);
   }

   public int addHashAlgoAt(int var1, ZrtpConstants.SupportedHashes var2) {
      return this.hashes.addAlgoAt(var1, var2);
   }

   public int addPubKeyAlgo(ZrtpConstants.SupportedPubKeys var1) {
      return this.publicKeyAlgos.addAlgo(var1);
   }

   public int addPubKeyAlgoAt(int var1, ZrtpConstants.SupportedPubKeys var2) {
      return this.publicKeyAlgos.addAlgoAt(var1, var2);
   }

   public int addSasTypeAlgo(ZrtpConstants.SupportedSASTypes var1) {
      return this.sasTypes.addAlgo(var1);
   }

   public int addSasTypeAlgoAt(int var1, ZrtpConstants.SupportedSASTypes var2) {
      return this.sasTypes.addAlgoAt(var1, var2);
   }

   public int addSymCipherAlgo(ZrtpConstants.SupportedSymCiphers var1) {
      return this.symCiphers.addAlgo(var1);
   }

   public int addSymCipherAlgoAt(int var1, ZrtpConstants.SupportedSymCiphers var2) {
      return this.symCiphers.addAlgoAt(var1, var2);
   }

   public Iterable algos(Enum var1) {
      Class var2 = var1.getDeclaringClass();
      if (var2.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes;
      } else if (var2.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers;
      } else if (var2.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos;
      } else if (var2.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes;
      } else {
         return var2.equals(ZrtpConstants.SupportedAuthLengths.class) ? this.authLengths : null;
      }
   }

   public Iterable authLengths() {
      return this.authLengths;
   }

   public void clear() {
      this.hashes.clear();
      this.symCiphers.clear();
      this.publicKeyAlgos.clear();
      this.sasTypes.clear();
      this.authLengths.clear();
   }

   public boolean containsAuthLength(ZrtpConstants.SupportedAuthLengths var1) {
      return this.authLengths.containsAlgo(var1);
   }

   public boolean containsAuthLength(Enum var1) {
      Class var2 = var1.getDeclaringClass();
      if (var2.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes.containsAlgo((Enum)ZrtpConstants.SupportedHashes.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers.containsAlgo((Enum)ZrtpConstants.SupportedSymCiphers.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos.containsAlgo((Enum)ZrtpConstants.SupportedPubKeys.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes.containsAlgo((Enum)ZrtpConstants.SupportedSASTypes.class.cast(var1));
      } else {
         return var2.equals(ZrtpConstants.SupportedAuthLengths.class) && this.authLengths.containsAlgo((Enum)ZrtpConstants.SupportedAuthLengths.class.cast(var1));
      }
   }

   public boolean containsCipherAlgo(ZrtpConstants.SupportedSymCiphers var1) {
      return this.symCiphers.containsAlgo(var1);
   }

   public boolean containsHashAlgo(ZrtpConstants.SupportedHashes var1) {
      return this.hashes.containsAlgo(var1);
   }

   public boolean containsPubKeyAlgo(ZrtpConstants.SupportedPubKeys var1) {
      return this.publicKeyAlgos.containsAlgo(var1);
   }

   public boolean containsSasTypeAlgo(ZrtpConstants.SupportedSASTypes var1) {
      return this.sasTypes.containsAlgo(var1);
   }

   public Enum getAlgoAt(int var1, Enum var2) {
      Class var8 = var2.getDeclaringClass();
      if (var8.equals(ZrtpConstants.SupportedHashes.class)) {
         try {
            var2 = (Enum)var8.cast(this.hashes.getAlgoAt(var1));
            return var2;
         } catch (IndexOutOfBoundsException var3) {
            return null;
         }
      } else if (var8.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         try {
            var2 = (Enum)var8.cast(this.symCiphers.getAlgoAt(var1));
            return var2;
         } catch (IndexOutOfBoundsException var4) {
            return null;
         }
      } else if (var8.equals(ZrtpConstants.SupportedPubKeys.class)) {
         try {
            var2 = (Enum)var8.cast(this.publicKeyAlgos.getAlgoAt(var1));
            return var2;
         } catch (IndexOutOfBoundsException var5) {
            return null;
         }
      } else if (var8.equals(ZrtpConstants.SupportedSASTypes.class)) {
         try {
            var2 = (Enum)var8.cast(this.sasTypes.getAlgoAt(var1));
            return var2;
         } catch (IndexOutOfBoundsException var6) {
            return null;
         }
      } else if (var8.equals(ZrtpConstants.SupportedAuthLengths.class)) {
         try {
            var2 = (Enum)var8.cast(this.authLengths.getAlgoAt(var1));
            return var2;
         } catch (IndexOutOfBoundsException var7) {
            return null;
         }
      } else {
         return null;
      }
   }

   public ZrtpConstants.SupportedAuthLengths getAuthLengthAt(int var1) {
      try {
         ZrtpConstants.SupportedAuthLengths var2 = (ZrtpConstants.SupportedAuthLengths)this.authLengths.getAlgoAt(var1);
         return var2;
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public ZrtpConstants.SupportedHashes getHashAlgoAt(int var1) {
      try {
         ZrtpConstants.SupportedHashes var2 = (ZrtpConstants.SupportedHashes)this.hashes.getAlgoAt(var1);
         return var2;
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public int getNumConfiguredAlgos(Enum var1) {
      Class var2 = var1.getDeclaringClass();
      if (var2.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes.getNumConfiguredAlgos();
      } else if (var2.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers.getNumConfiguredAlgos();
      } else if (var2.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos.getNumConfiguredAlgos();
      } else if (var2.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes.getNumConfiguredAlgos();
      } else {
         return var2.equals(ZrtpConstants.SupportedAuthLengths.class) ? this.authLengths.getNumConfiguredAlgos() : -1;
      }
   }

   public int getNumConfiguredAuthLengths() {
      return this.authLengths.getNumConfiguredAlgos();
   }

   public int getNumConfiguredHashes() {
      return this.hashes.getNumConfiguredAlgos();
   }

   public int getNumConfiguredPubKeys() {
      return this.publicKeyAlgos.getNumConfiguredAlgos();
   }

   public int getNumConfiguredSasTypes() {
      return this.sasTypes.getNumConfiguredAlgos();
   }

   public int getNumConfiguredSymCiphers() {
      return this.symCiphers.getNumConfiguredAlgos();
   }

   public ZrtpConstants.SupportedPubKeys getPubKeyAlgoAt(int var1) {
      try {
         ZrtpConstants.SupportedPubKeys var2 = (ZrtpConstants.SupportedPubKeys)this.publicKeyAlgos.getAlgoAt(var1);
         return var2;
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public ZrtpConstants.SupportedSASTypes getSasTypeAlgoAt(int var1) {
      try {
         ZrtpConstants.SupportedSASTypes var2 = (ZrtpConstants.SupportedSASTypes)this.sasTypes.getAlgoAt(var1);
         return var2;
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public ZrtpConstants.SupportedSymCiphers getSymCipherAlgoAt(int var1) {
      try {
         ZrtpConstants.SupportedSymCiphers var2 = (ZrtpConstants.SupportedSymCiphers)this.symCiphers.getAlgoAt(var1);
         return var2;
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public Iterable hashes() {
      return this.hashes;
   }

   public boolean isParanoidMode() {
      return this.enableParanoidMode;
   }

   public boolean isSasSignature() {
      return this.enableSasSignature;
   }

   public boolean isTrustedMitM() {
      return this.enableTrustedMitM;
   }

   public Iterable publicKeyAlgos() {
      return this.publicKeyAlgos;
   }

   public int removeAlgo(Enum var1) {
      Class var2 = var1.getDeclaringClass();
      if (var2.equals(ZrtpConstants.SupportedHashes.class)) {
         return this.hashes.removeAlgo((Enum)ZrtpConstants.SupportedHashes.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSymCiphers.class)) {
         return this.symCiphers.removeAlgo((Enum)ZrtpConstants.SupportedSymCiphers.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedPubKeys.class)) {
         return this.publicKeyAlgos.removeAlgo((Enum)ZrtpConstants.SupportedPubKeys.class.cast(var1));
      } else if (var2.equals(ZrtpConstants.SupportedSASTypes.class)) {
         return this.sasTypes.removeAlgo((Enum)ZrtpConstants.SupportedSASTypes.class.cast(var1));
      } else {
         return var2.equals(ZrtpConstants.SupportedAuthLengths.class) ? this.authLengths.removeAlgo((Enum)ZrtpConstants.SupportedAuthLengths.class.cast(var1)) : -1;
      }
   }

   public int removeAuthLength(ZrtpConstants.SupportedAuthLengths var1) {
      return this.authLengths.removeAlgo(var1);
   }

   public int removeHashAlgo(ZrtpConstants.SupportedHashes var1) {
      return this.hashes.removeAlgo(var1);
   }

   public int removePubKeyAlgo(ZrtpConstants.SupportedPubKeys var1) {
      return this.publicKeyAlgos.removeAlgo(var1);
   }

   public int removeSasTypeAlgo(ZrtpConstants.SupportedSASTypes var1) {
      return this.sasTypes.removeAlgo(var1);
   }

   public int removeSymCipherAlgo(ZrtpConstants.SupportedSymCiphers var1) {
      return this.symCiphers.removeAlgo(var1);
   }

   public Iterable sasTypes() {
      return this.sasTypes;
   }

   public void setMandatoryOnly() {
      this.clear();
      this.hashes.addAlgo(ZrtpConstants.SupportedHashes.S256);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.AES1);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.DH3K);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.MULT);
      this.sasTypes.addAlgo(ZrtpConstants.SupportedSASTypes.B32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS80);
   }

   public void setMobileConfig() {
      this.clear();
      this.hashes.addAlgo(ZrtpConstants.SupportedHashes.S256);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.TWO1);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.AES1);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.EC25);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.DH2K);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.MULT);
      this.sasTypes.addAlgo(ZrtpConstants.SupportedSASTypes.B32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.SK32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.SK64);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS80);
   }

   public void setParanoidMode(boolean var1) {
      this.enableParanoidMode = var1;
   }

   public void setSasSignature(boolean var1) {
      this.enableSasSignature = var1;
   }

   public void setStandardConfig() {
      this.clear();
      this.hashes.addAlgo(ZrtpConstants.SupportedHashes.S384);
      this.hashes.addAlgo(ZrtpConstants.SupportedHashes.S256);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.TWO3);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.AES3);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.TWO1);
      this.symCiphers.addAlgo(ZrtpConstants.SupportedSymCiphers.AES1);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.EC25);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.DH3K);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.EC38);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.DH2K);
      this.publicKeyAlgos.addAlgo(ZrtpConstants.SupportedPubKeys.MULT);
      this.sasTypes.addAlgo(ZrtpConstants.SupportedSASTypes.B32);
      this.sasTypes.addAlgo(ZrtpConstants.SupportedSASTypes.B256);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.SK32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.SK64);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS32);
      this.authLengths.addAlgo(ZrtpConstants.SupportedAuthLengths.HS80);
   }

   public void setTrustedMitM(boolean var1) {
      this.enableTrustedMitM = var1;
   }

   public Iterable symCiphers() {
      return this.symCiphers;
   }

   private class Data implements Iterable {
      private static final int maxNoOfAlgos = 7;
      private final ArrayList algos;

      private Data() {
         this.algos = new ArrayList(7);
      }

      // $FF: synthetic method
      Data(Object var2) {
         this();
      }

      int addAlgo(Enum var1) {
         if (this.algos.size() >= 7) {
            return 0;
         } else if (this.algos.contains(var1)) {
            return 7 - this.algos.size();
         } else {
            this.algos.add(var1);
            return 7 - this.algos.size();
         }
      }

      int addAlgoAt(int var1, Enum var2) {
         if (this.algos.size() >= 7) {
            return 0;
         } else if (var1 >= 7) {
            return 0;
         } else if (this.algos.contains(var2)) {
            return 7 - this.algos.size();
         } else {
            this.algos.add(var1, var2);
            return 7 - this.algos.size();
         }
      }

      void clear() {
         this.algos.clear();
      }

      boolean containsAlgo(Enum var1) {
         return this.algos.contains(var1);
      }

      Enum getAlgoAt(int var1) {
         return (Enum)this.algos.get(var1);
      }

      int getNumConfiguredAlgos() {
         return this.algos.size();
      }

      public Iterator iterator() {
         return this.algos.iterator();
      }

      int removeAlgo(Enum var1) {
         this.algos.remove(var1);
         return 7 - this.algos.size();
      }
   }
}
