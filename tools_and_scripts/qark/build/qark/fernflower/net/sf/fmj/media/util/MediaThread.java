package net.sf.fmj.media.util;

import java.io.PrintStream;

public class MediaThread extends Thread {
   private static int audioPriority = 5;
   private static int controlPriority = 9;
   private static final boolean debug = false;
   private static int defaultMaxPriority;
   private static int networkPriority;
   static boolean securityPrivilege = true;
   private static ThreadGroup threadGroup;
   private static int videoNetworkPriority;
   private static int videoPriority = 3;
   private String androidThreadPriority;

   static {
      int var0 = 5 + 1;
      networkPriority = var0;
      videoNetworkPriority = var0 - 1;
      defaultMaxPriority = 4;
      boolean var3 = false;

      label32:
      try {
         var3 = true;
         defaultMaxPriority = Thread.currentThread().getPriority();
         defaultMaxPriority = Thread.currentThread().getThreadGroup().getMaxPriority();
         var3 = false;
      } finally {
         if (var3) {
            securityPrivilege = false;
            var0 = defaultMaxPriority;
            controlPriority = var0;
            audioPriority = var0;
            videoPriority = var0 - 1;
            networkPriority = var0;
            videoNetworkPriority = var0;
            break label32;
         }
      }

      if (securityPrivilege) {
         threadGroup = getRootThreadGroup();
      } else {
         threadGroup = null;
      }
   }

   public MediaThread() {
      this("FMJ Thread");
   }

   public MediaThread(Runnable var1) {
      this(var1, "FMJ Thread");
   }

   public MediaThread(Runnable var1, String var2) {
      super(threadGroup, var1, var2);
   }

   public MediaThread(String var1) {
      super(threadGroup, var1);
   }

   private void checkPriority(String var1, int var2, boolean var3, int var4) {
      if (var2 != var4) {
         PrintStream var5 = System.out;
         StringBuilder var6 = new StringBuilder();
         var6.append("MediaThread: ");
         var6.append(var1);
         var6.append(" privilege? ");
         var6.append(var3);
         var6.append("  ask pri: ");
         var6.append(var2);
         var6.append(" got pri:  ");
         var6.append(var4);
         var5.println(var6.toString());
      }

   }

   public static int getAudioPriority() {
      return audioPriority;
   }

   public static int getControlPriority() {
      return controlPriority;
   }

   public static int getNetworkPriority() {
      return networkPriority;
   }

   private static ThreadGroup getRootThreadGroup() {
      // $FF: Couldn't be decompiled
   }

   public static int getVideoNetworkPriority() {
      return videoNetworkPriority;
   }

   public static int getVideoPriority() {
      return videoPriority;
   }

   private void useAndroidThreadPriority(String var1) {
      this.androidThreadPriority = var1;
   }

   private void usePriority(int var1) {
      try {
         this.setPriority(var1);
      } finally {
         return;
      }
   }

   public void run() {
      if (this.androidThreadPriority != null) {
         label259: {
            Throwable var10000;
            label262: {
               boolean var10001;
               String var3;
               try {
                  var3 = System.getProperty("os.name");
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label262;
               }

               if (var3 == null) {
                  break label259;
               }

               try {
                  if (!var3.startsWith("Linux")) {
                     break label259;
                  }

                  var3 = System.getProperty("java.vm.name");
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label262;
               }

               if (var3 == null) {
                  break label259;
               }

               int var1;
               int var2;
               try {
                  if (!var3.equalsIgnoreCase("Dalvik")) {
                     break label259;
                  }

                  Class var24 = Class.forName("android.os.Process");
                  var1 = var24.getField(this.androidThreadPriority).getInt((Object)null);
                  var24.getMethod("setThreadPriority", Integer.class).invoke((Object)null, var1);
                  var2 = 10 - Math.round((float)(var1 + 20) / 40.0F * 10.0F);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label262;
               }

               if (var2 < 1) {
                  var1 = 1;
               } else {
                  var1 = var2;
                  if (var2 > 10) {
                     var1 = 10;
                  }
               }

               label246:
               try {
                  this.setPriority(var1);
                  break label259;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label246;
               }
            }

            Throwable var25 = var10000;
            if (var25 instanceof ThreadDeath) {
               throw (ThreadDeath)var25;
            }
         }
      }

      super.run();
   }

   public void useAudioPriority() {
      this.usePriority(audioPriority);
      this.useAndroidThreadPriority("THREAD_PRIORITY_URGENT_AUDIO");
   }

   public void useControlPriority() {
      this.usePriority(controlPriority);
   }

   public void useNetworkPriority() {
      this.usePriority(networkPriority);
   }

   public void useVideoNetworkPriority() {
      this.usePriority(videoNetworkPriority);
   }

   public void useVideoPriority() {
      this.usePriority(videoPriority);
      this.useAndroidThreadPriority("THREAD_PRIORITY_URGENT_DISPLAY");
   }
}
