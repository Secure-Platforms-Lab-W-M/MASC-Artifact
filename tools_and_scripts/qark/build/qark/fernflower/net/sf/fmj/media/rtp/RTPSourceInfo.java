package net.sf.fmj.media.rtp;

import java.util.Vector;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPStream;
import javax.media.rtp.rtcp.SourceDescription;

public abstract class RTPSourceInfo implements Participant {
   private SourceDescription cname;
   RTPSourceInfoCache sic;
   private SSRCInfo[] ssrc;

   RTPSourceInfo(String var1, RTPSourceInfoCache var2) {
      this.cname = new SourceDescription(1, var1, 0, false);
      this.sic = var2;
      this.ssrc = new SSRCInfo[0];
   }

   void addSSRC(SSRCInfo var1) {
      synchronized(this){}
      int var2 = 0;

      while(true) {
         boolean var6 = false;

         SSRCInfo var3;
         try {
            var6 = true;
            if (var2 >= this.ssrc.length) {
               SSRCInfo[] var8 = this.ssrc;
               SSRCInfo[] var4 = new SSRCInfo[this.ssrc.length + 1];
               this.ssrc = var4;
               System.arraycopy(var8, 0, var4, 0, var4.length - 1);
               this.ssrc[this.ssrc.length - 1] = var1;
               var6 = false;
               return;
            }

            var3 = this.ssrc[var2];
            var6 = false;
         } finally {
            if (var6) {
               ;
            }
         }

         if (var3 == var1) {
            return;
         }

         ++var2;
      }
   }

   public String getCNAME() {
      return this.cname.getDescription();
   }

   SourceDescription getCNAMESDES() {
      return this.cname;
   }

   public Vector getReports() {
      Vector var2 = new Vector();
      int var1 = 0;

      while(true) {
         SSRCInfo[] var3 = this.ssrc;
         if (var1 >= var3.length) {
            var2.trimToSize();
            return var2;
         }

         var2.addElement(var3[var1]);
         ++var1;
      }
   }

   RTPStream getSSRCStream(long var1) {
      int var3 = 0;

      while(true) {
         SSRCInfo[] var4 = this.ssrc;
         if (var3 >= var4.length) {
            return null;
         }

         if (var4[var3] instanceof RTPStream && var4[var3].ssrc == (int)var1) {
            return (RTPStream)this.ssrc[var3];
         }

         ++var3;
      }
   }

   public Vector getSourceDescription() {
      SSRCInfo[] var1 = this.ssrc;
      return var1.length == 0 ? new Vector(0) : var1[0].getSourceDescription();
   }

   int getStreamCount() {
      return this.ssrc.length;
   }

   public Vector getStreams() {
      Vector var2 = new Vector();
      int var1 = 0;

      while(true) {
         SSRCInfo[] var3 = this.ssrc;
         if (var1 >= var3.length) {
            var2.trimToSize();
            return var2;
         }

         if (var3[var1].isActive()) {
            var2.addElement(this.ssrc[var1]);
         }

         ++var1;
      }
   }

   void removeSSRC(SSRCInfo var1) {
      synchronized(this){}

      Throwable var10000;
      label210: {
         boolean var10001;
         try {
            if (var1.dsource != null) {
               this.sic.ssrccache.field_173.removeDataSource(var1.dsource);
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label210;
         }

         int var2 = 0;

         while(true) {
            try {
               if (var2 >= this.ssrc.length) {
                  break;
               }

               if (this.ssrc[var2] == var1) {
                  this.ssrc[var2] = this.ssrc[this.ssrc.length - 1];
                  SSRCInfo[] var16 = this.ssrc;
                  SSRCInfo[] var3 = new SSRCInfo[this.ssrc.length - 1];
                  this.ssrc = var3;
                  System.arraycopy(var16, 0, var3, 0, var3.length);
                  break;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label210;
            }

            ++var2;
         }

         label186:
         try {
            if (this.ssrc.length == 0) {
               this.sic.remove(this.cname.getDescription());
            }

            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label186;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }
}
