/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.TrafficStats
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 */
package androidx.core.net;

import android.net.TrafficStats;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import androidx.core.net.DatagramSocketWrapper;
import java.io.FileDescriptor;
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
        if (Build.VERSION.SDK_INT >= 24) {
            TrafficStats.tagDatagramSocket((DatagramSocket)datagramSocket);
            return;
        }
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.fromDatagramSocket((DatagramSocket)datagramSocket);
        TrafficStats.tagSocket((Socket)new DatagramSocketWrapper(datagramSocket, parcelFileDescriptor.getFileDescriptor()));
        parcelFileDescriptor.detachFd();
    }

    @Deprecated
    public static void tagSocket(Socket socket) throws SocketException {
        TrafficStats.tagSocket((Socket)socket);
    }

    public static void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        if (Build.VERSION.SDK_INT >= 24) {
            TrafficStats.untagDatagramSocket((DatagramSocket)datagramSocket);
            return;
        }
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.fromDatagramSocket((DatagramSocket)datagramSocket);
        TrafficStats.untagSocket((Socket)new DatagramSocketWrapper(datagramSocket, parcelFileDescriptor.getFileDescriptor()));
        parcelFileDescriptor.detachFd();
    }

    @Deprecated
    public static void untagSocket(Socket socket) throws SocketException {
        TrafficStats.untagSocket((Socket)socket);
    }
}

