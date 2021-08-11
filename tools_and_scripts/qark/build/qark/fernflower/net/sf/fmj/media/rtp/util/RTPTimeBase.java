package net.sf.fmj.media.rtp.util;

import java.util.Vector;
import javax.media.Time;
import javax.media.TimeBase;
import net.sf.fmj.media.Log;

public class RTPTimeBase implements TimeBase {
   static int SSRC_UNDEFINED = 0;
   static Vector timeBases = new Vector();
   String cname;
   RTPTimeReporter master = null;
   long offset = 0L;
   boolean offsetUpdatable = true;
   long origin = 0L;
   Vector reporters = new Vector();

   RTPTimeBase(String var1) {
      this.cname = var1;
   }

   public static RTPTimeBase find(RTPTimeReporter var0, String var1) {
      Vector var5 = timeBases;
      synchronized(var5){}
      RTPTimeBase var4 = null;
      int var2 = 0;

      while(true) {
         RTPTimeBase var3 = var4;

         Throwable var10000;
         boolean var10001;
         label462: {
            label468: {
               try {
                  if (var2 < timeBases.size()) {
                     var3 = (RTPTimeBase)timeBases.elementAt(var2);
                     if (var3.cname == null || !var3.cname.equals(var1)) {
                        break label468;
                     }
                  }
               } catch (Throwable var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label462;
               }

               var4 = var3;
               if (var3 == null) {
                  try {
                     StringBuilder var49 = new StringBuilder();
                     var49.append("Created RTP time base for session: ");
                     var49.append(var1);
                     var49.append("\n");
                     Log.comment(var49.toString());
                     var4 = new RTPTimeBase(var1);
                     timeBases.addElement(var4);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label462;
                  }
               }

               if (var0 != null) {
                  try {
                     if (var4.getMaster() == null) {
                        var4.setMaster(var0);
                     }
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label462;
                  }

                  try {
                     var4.reporters.addElement(var0);
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label462;
                  }
               }

               try {
                  return var4;
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label462;
               }
            }

            ++var2;
            continue;
         }

         while(true) {
            Throwable var48 = var10000;

            try {
               throw var48;
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static RTPTimeBase getMapper(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static RTPTimeBase getMapperUpdatable(String var0) {
      Vector var1 = timeBases;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            RTPTimeBase var14 = find((RTPTimeReporter)null, var0);
            if (var14.offsetUpdatable) {
               var14.offsetUpdatable = false;
               return var14;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            return null;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static void remove(RTPTimeReporter param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static void returnMapperUpdatable(RTPTimeBase param0) {
      // $FF: Couldn't be decompiled
   }

   public RTPTimeReporter getMaster() {
      synchronized(this){}

      RTPTimeReporter var1;
      try {
         var1 = this.master;
      } finally {
         ;
      }

      return var1;
   }

   public long getNanoseconds() {
      synchronized(this){}
      boolean var5 = false;

      long var1;
      try {
         var5 = true;
         if (this.master != null) {
            var1 = this.master.getRTPTime();
            var5 = false;
            return var1;
         }

         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      var1 = 0L;
      return var1;
   }

   public long getOffset() {
      return this.offset;
   }

   public long getOrigin() {
      return this.origin;
   }

   public Time getTime() {
      return new Time(this.getNanoseconds());
   }

   public void setMaster(RTPTimeReporter var1) {
      synchronized(this){}

      try {
         this.master = var1;
      } finally {
         ;
      }

   }

   public void setOffset(long var1) {
      synchronized(this){}

      try {
         this.offset = var1;
      } finally {
         ;
      }

   }

   public void setOrigin(long var1) {
      synchronized(this){}

      try {
         this.origin = var1;
      } finally {
         ;
      }

   }
}
