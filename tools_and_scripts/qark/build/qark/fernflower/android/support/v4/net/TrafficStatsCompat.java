package android.support.v4.net;

import android.net.TrafficStats;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public final class TrafficStatsCompat {
   private static final TrafficStatsCompat.TrafficStatsCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new TrafficStatsCompat.TrafficStatsCompatApi24Impl();
      } else {
         IMPL = new TrafficStatsCompat.TrafficStatsCompatBaseImpl();
      }
   }

   private TrafficStatsCompat() {
   }

   @Deprecated
   public static void clearThreadStatsTag() {
      TrafficStats.clearThreadStatsTag();
   }

   @Deprecated
   public static int getThreadStatsTag() {
      return TrafficStats.getThreadStatsTag();
   }

   @Deprecated
   public static void incrementOperationCount(int var0) {
      TrafficStats.incrementOperationCount(var0);
   }

   @Deprecated
   public static void incrementOperationCount(int var0, int var1) {
      TrafficStats.incrementOperationCount(var0, var1);
   }

   @Deprecated
   public static void setThreadStatsTag(int var0) {
      TrafficStats.setThreadStatsTag(var0);
   }

   public static void tagDatagramSocket(DatagramSocket var0) throws SocketException {
      IMPL.tagDatagramSocket(var0);
   }

   @Deprecated
   public static void tagSocket(Socket var0) throws SocketException {
      TrafficStats.tagSocket(var0);
   }

   public static void untagDatagramSocket(DatagramSocket var0) throws SocketException {
      IMPL.untagDatagramSocket(var0);
   }

   @Deprecated
   public static void untagSocket(Socket var0) throws SocketException {
      TrafficStats.untagSocket(var0);
   }

   @RequiresApi(24)
   static class TrafficStatsCompatApi24Impl extends TrafficStatsCompat.TrafficStatsCompatBaseImpl {
      public void tagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStats.tagDatagramSocket(var1);
      }

      public void untagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStats.untagDatagramSocket(var1);
      }
   }

   static class TrafficStatsCompatBaseImpl {
      public void tagDatagramSocket(DatagramSocket var1) throws SocketException {
         ParcelFileDescriptor var2 = ParcelFileDescriptor.fromDatagramSocket(var1);
         TrafficStats.tagSocket(new DatagramSocketWrapper(var1, var2.getFileDescriptor()));
         var2.detachFd();
      }

      public void untagDatagramSocket(DatagramSocket var1) throws SocketException {
         ParcelFileDescriptor var2 = ParcelFileDescriptor.fromDatagramSocket(var1);
         TrafficStats.untagSocket(new DatagramSocketWrapper(var1, var2.getFileDescriptor()));
         var2.detachFd();
      }
   }
}
