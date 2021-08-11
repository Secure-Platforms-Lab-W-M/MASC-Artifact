package net.sf.fmj.media.rtp;

import java.util.Hashtable;

public class RTPSourceInfoCache {
   Hashtable cache = new Hashtable(20);
   RTPSourceInfoCache main;
   public SSRCCache ssrccache;

   public RTPSourceInfo get(String var1, boolean var2) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label277: {
         RTPSourceInfo var4;
         try {
            var4 = (RTPSourceInfo)this.cache.get(var1);
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label277;
         }

         Object var3 = var4;
         if (var4 == null) {
            var3 = var4;
            if (!var2) {
               try {
                  var3 = new RTPRemoteSourceInfo(var1, this.main);
                  this.cache.put(var1, var3);
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label277;
               }
            }
         }

         Object var36 = var3;
         if (var3 == null) {
            var36 = var3;
            if (var2) {
               try {
                  var36 = new RTPLocalSourceInfo(var1, this.main);
                  this.cache.put(var1, var36);
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label277;
               }
            }
         }

         label260:
         try {
            return (RTPSourceInfo)var36;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label260;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public Hashtable getCacheTable() {
      return this.cache;
   }

   public RTPSourceInfoCache getMainCache() {
      if (this.main == null) {
         this.main = new RTPSourceInfoCache();
      }

      return this.main;
   }

   public void remove(String var1) {
      this.cache.remove(var1);
   }

   public void setMainCache(RTPSourceInfoCache var1) {
      this.main = var1;
   }

   public void setSSRCCache(SSRCCache var1) {
      this.main.ssrccache = var1;
   }
}
