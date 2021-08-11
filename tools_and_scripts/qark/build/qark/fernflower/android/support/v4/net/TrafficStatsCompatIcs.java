package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

@TargetApi(14)
@RequiresApi(14)
class TrafficStatsCompatIcs {
   public static void clearThreadStatsTag() {
      TrafficStats.clearThreadStatsTag();
   }

   public static int getThreadStatsTag() {
      return TrafficStats.getThreadStatsTag();
   }

   public static void incrementOperationCount(int var0) {
      TrafficStats.incrementOperationCount(var0);
   }

   public static void incrementOperationCount(int var0, int var1) {
      TrafficStats.incrementOperationCount(var0, var1);
   }

   public static void setThreadStatsTag(int var0) {
      TrafficStats.setThreadStatsTag(var0);
   }

   public static void tagDatagramSocket(DatagramSocket var0) throws SocketException {
      ParcelFileDescriptor var1 = ParcelFileDescriptor.fromDatagramSocket(var0);
      TrafficStats.tagSocket(new DatagramSocketWrapper(var0, var1.getFileDescriptor()));
      var1.detachFd();
   }

   public static void tagSocket(Socket var0) throws SocketException {
      TrafficStats.tagSocket(var0);
   }

   public static void untagDatagramSocket(DatagramSocket var0) throws SocketException {
      ParcelFileDescriptor var1 = ParcelFileDescriptor.fromDatagramSocket(var0);
      TrafficStats.untagSocket(new DatagramSocketWrapper(var0, var1.getFileDescriptor()));
      var1.detachFd();
   }

   public static void untagSocket(Socket var0) throws SocketException {
      TrafficStats.untagSocket(var0);
   }
}
