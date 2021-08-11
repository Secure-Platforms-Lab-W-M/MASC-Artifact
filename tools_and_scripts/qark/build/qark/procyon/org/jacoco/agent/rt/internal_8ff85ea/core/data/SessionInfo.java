// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.data;

public class SessionInfo implements Comparable<SessionInfo>
{
    private final long dump;
    private final String id;
    private final long start;
    
    public SessionInfo(final String id, final long start, final long dump) {
        if (id != null) {
            this.id = id;
            this.start = start;
            this.dump = dump;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public int compareTo(final SessionInfo sessionInfo) {
        if (this.dump < sessionInfo.dump) {
            return -1;
        }
        if (this.dump > sessionInfo.dump) {
            return 1;
        }
        return 0;
    }
    
    public long getDumpTimeStamp() {
        return this.dump;
    }
    
    public String getId() {
        return this.id;
    }
    
    public long getStartTimeStamp() {
        return this.start;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SessionInfo[");
        sb.append(this.id);
        sb.append("]");
        return sb.toString();
    }
}
