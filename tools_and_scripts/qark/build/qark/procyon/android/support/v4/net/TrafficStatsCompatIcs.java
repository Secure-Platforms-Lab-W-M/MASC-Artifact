// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.net;

import java.net.SocketException;
import java.net.Socket;
import android.net.TrafficStats;

class TrafficStatsCompatIcs
{
    public static void clearThreadStatsTag() {
        TrafficStats.clearThreadStatsTag();
    }
    
    public static int getThreadStatsTag() {
        return TrafficStats.getThreadStatsTag();
    }
    
    public static void incrementOperationCount(final int n) {
        TrafficStats.incrementOperationCount(n);
    }
    
    public static void incrementOperationCount(final int n, final int n2) {
        TrafficStats.incrementOperationCount(n, n2);
    }
    
    public static void setThreadStatsTag(final int threadStatsTag) {
        TrafficStats.setThreadStatsTag(threadStatsTag);
    }
    
    public static void tagSocket(final Socket socket) throws SocketException {
        TrafficStats.tagSocket(socket);
    }
    
    public static void untagSocket(final Socket socket) throws SocketException {
        TrafficStats.untagSocket(socket);
    }
}
