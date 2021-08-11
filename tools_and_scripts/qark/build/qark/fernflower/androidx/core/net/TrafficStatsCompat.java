package androidx.core.net;

import android.net.TrafficStats;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public final class TrafficStatsCompat {
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
      if (VERSION.SDK_INT >= 24) {
         TrafficStats.tagDatagramSocket(var0);
      } else {
         ParcelFileDescriptor var1 = ParcelFileDescriptor.fromDatagramSocket(var0);
         TrafficStats.tagSocket(new DatagramSocketWrapper(var0, var1.getFileDescriptor()));
         var1.detachFd();
      }
   }

   @Deprecated
   public static void tagSocket(Socket var0) throws SocketException {
      TrafficStats.tagSocket(var0);
   }

   public static void untagDatagramSocket(DatagramSocket var0) throws SocketException {
      if (VERSION.SDK_INT >= 24) {
         TrafficStats.untagDatagramSocket(var0);
      } else {
         ParcelFileDescriptor var1 = ParcelFileDescriptor.fromDatagramSocket(var0);
         TrafficStats.untagSocket(new DatagramSocketWrapper(var0, var1.getFileDescriptor()));
         var1.detachFd();
      }
   }

   @Deprecated
   public static void untagSocket(Socket var0) throws SocketException {
      TrafficStats.untagSocket(var0);
   }
}
