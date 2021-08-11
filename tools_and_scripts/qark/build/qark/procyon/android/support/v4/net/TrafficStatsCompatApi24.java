// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.net;

import java.net.SocketException;
import android.net.TrafficStats;
import java.net.DatagramSocket;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.GROUP_ID })
public class TrafficStatsCompatApi24
{
    public static void tagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
        TrafficStats.tagDatagramSocket(datagramSocket);
    }
    
    public static void untagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
        TrafficStats.untagDatagramSocket(datagramSocket);
    }
}
