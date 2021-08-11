// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import javax.annotation.Nullable;

final class SegmentPool
{
    static final long MAX_SIZE = 65536L;
    static long byteCount;
    @Nullable
    static Segment next;
    
    private SegmentPool() {
    }
    
    static void recycle(final Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        }
        if (segment.shared) {
            return;
        }
        synchronized (SegmentPool.class) {
            if (SegmentPool.byteCount + 8192L > 65536L) {
                return;
            }
        }
        SegmentPool.byteCount += 8192L;
        final Segment next;
        next.next = SegmentPool.next;
        next.limit = 0;
        next.pos = 0;
        SegmentPool.next = next;
    }
    // monitorexit(SegmentPool.class)
    
    static Segment take() {
        synchronized (SegmentPool.class) {
            if (SegmentPool.next != null) {
                final Segment next = SegmentPool.next;
                SegmentPool.next = next.next;
                next.next = null;
                SegmentPool.byteCount -= 8192L;
                return next;
            }
            // monitorexit(SegmentPool.class)
            return new Segment();
        }
    }
}
