/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.TrafficStats
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 */
package android.support.v4.net;

import android.net.TrafficStats;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v4.net.DatagramSocketWrapper;
import java.io.FileDescriptor;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public final class TrafficStatsCompat {
    private static final TrafficStatsCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 24 ? new TrafficStatsCompatApi24Impl() : new TrafficStatsCompatBaseImpl();

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
    public static void incrementOperationCount(int n) {
        TrafficStats.incrementOperationCount((int)n);
    }

    @Deprecated
    public static void incrementOperationCount(int n, int n2) {
        TrafficStats.incrementOperationCount((int)n, (int)n2);
    }

    @Deprecated
    public static void setThreadStatsTag(int n) {
        TrafficStats.setThreadStatsTag((int)n);
    }

    public static void tagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        IMPL.tagDatagramSocket(datagramSocket);
    }

    @Deprecated
    public static void tagSocket(Socket socket) throws SocketException {
        TrafficStats.tagSocket((Socket)socket);
    }

    public static void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        IMPL.untagDatagramSocket(datagramSocket);
    }

    @Deprecated
    public static void untagSocket(Socket socket) throws SocketException {
        TrafficStats.untagSocket((Socket)socket);
    }

    @RequiresApi(value=24)
    static class TrafficStatsCompatApi24Impl
    extends TrafficStatsCompatBaseImpl {
        TrafficStatsCompatApi24Impl() {
        }

        @Override
        public void tagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
            TrafficStats.tagDatagramSocket((DatagramSocket)datagramSocket);
        }

        @Override
        public void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
            TrafficStats.untagDatagramSocket((DatagramSocket)datagramSocket);
        }
    }

    static class TrafficStatsCompatBaseImpl {
        TrafficStatsCompatBaseImpl() {
        }

        public void tagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.fromDatagramSocket((DatagramSocket)datagramSocket);
            TrafficStats.tagSocket((Socket)new DatagramSocketWrapper(datagramSocket, parcelFileDescriptor.getFileDescriptor()));
            parcelFileDescriptor.detachFd();
        }

        public void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.fromDatagramSocket((DatagramSocket)datagramSocket);
            TrafficStats.untagSocket((Socket)new DatagramSocketWrapper(datagramSocket, parcelFileDescriptor.getFileDescriptor()));
            parcelFileDescriptor.detachFd();
        }
    }

}

