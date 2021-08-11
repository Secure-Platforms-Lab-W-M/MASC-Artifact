package gnu.java.zrtp.utils;

import java.security.SecureRandom;
import org.atalk.bccontrib.prng.FortunaGenerator;
import org.bouncycastle.crypto.prng.RandomGenerator;

public class ZrtpFortuna implements RandomGenerator {
   private static ZrtpFortuna singleInstance = null;
   private FortunaGenerator fortuna = null;

   protected ZrtpFortuna() {
   }

   public static ZrtpFortuna getInstance() {
      synchronized(ZrtpFortuna.class){}

      ZrtpFortuna var0;
      try {
         if (singleInstance == null) {
            var0 = new ZrtpFortuna();
            singleInstance = var0;
            var0.initialize();
         }

         var0 = singleInstance;
      } finally {
         ;
      }

      return var0;
   }

   private void initialize() {
      byte[] var1 = new byte[256];
      (new SecureRandom()).nextBytes(var1);
      this.fortuna = new FortunaGenerator(var1);
   }

   public void addSeedMaterial(int var1, byte[] var2, int var3, int var4) {
      synchronized(this){}

      try {
         this.fortuna.addSeedMaterial(var1, var2, var3, var4);
      } finally {
         ;
      }

   }

   public void addSeedMaterial(long var1) {
      synchronized(this){}

      try {
         this.fortuna.addSeedMaterial(var1);
      } finally {
         ;
      }

   }

   public void addSeedMaterial(byte[] var1) {
      synchronized(this){}

      try {
         this.fortuna.addSeedMaterial(var1);
      } finally {
         ;
      }

   }

   public void addSeedMaterial(byte[] var1, int var2, int var3) {
      synchronized(this){}

      try {
         this.fortuna.addSeedMaterial(var1, var2, var3);
      } finally {
         ;
      }

   }

   public FortunaGenerator getFortuna() {
      return this.fortuna;
   }

   public void nextBytes(byte[] var1) {
      synchronized(this){}

      try {
         this.fortuna.nextBytes(var1);
      } finally {
         ;
      }

   }

   public void nextBytes(byte[] var1, int var2, int var3) {
      synchronized(this){}

      try {
         this.fortuna.nextBytes(var1, var2, var3);
      } finally {
         ;
      }

   }

   public void setFortuna(FortunaGenerator var1) {
      synchronized(this){}

      try {
         this.fortuna = var1;
      } finally {
         ;
      }

   }
}
