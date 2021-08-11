/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.TrafficStats
 */
package android.support.v4.net;

import android.net.TrafficStats;
import android.support.annotation.RestrictTo;
import java.net.DatagramSocket;
import java.net.SocketException;

@RestrictTo(value={RestrictTo.Scope.GROUP_ID})
public class TrafficStatsCompatApi24 {
    public static void tagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        TrafficStats.tagDatagramSocket((DatagramSocket)datagramSocket);
    }

    public static void untagDatagramSocket(DatagramSocket datagramSocket) throws SocketException {
        TrafficStats.untagDatagramSocket((DatagramSocket)datagramSocket);
    }
}

