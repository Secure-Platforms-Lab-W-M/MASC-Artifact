// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import javax.annotation.Nullable;

final class Segment
{
    static final int SHARE_MINIMUM = 1024;
    static final int SIZE = 8192;
    final byte[] data;
    int limit;
    Segment next;
    boolean owner;
    int pos;
    Segment prev;
    boolean shared;
    
    Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }
    
    Segment(final byte[] data, final int pos, final int limit, final boolean shared, final boolean owner) {
        this.data = data;
        this.pos = pos;
        this.limit = limit;
        this.shared = shared;
        this.owner = owner;
    }
    
    public void compact() {
        if (this.prev == this) {
            throw new IllegalStateException();
        }
        if (this.prev.owner) {
            final int n = this.limit - this.pos;
            final int limit = this.prev.limit;
            int pos;
            if (this.prev.shared) {
                pos = 0;
            }
            else {
                pos = this.prev.pos;
            }
            if (n <= 8192 - limit + pos) {
                this.writeTo(this.prev, n);
                this.pop();
                SegmentPool.recycle(this);
            }
        }
    }
    
    @Nullable
    public Segment pop() {
        Segment next;
        if (this.next != this) {
            next = this.next;
        }
        else {
            next = null;
        }
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
        return next;
    }
    
    public Segment push(final Segment segment) {
        segment.prev = this;
        segment.next = this.next;
        this.next.prev = segment;
        return this.next = segment;
    }
    
    Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true, false);
    }
    
    public Segment split(final int n) {
        if (n <= 0 || n > this.limit - this.pos) {
            throw new IllegalArgumentException();
        }
        Segment segment;
        if (n >= 1024) {
            segment = this.sharedCopy();
        }
        else {
            segment = SegmentPool.take();
            System.arraycopy(this.data, this.pos, segment.data, 0, n);
        }
        segment.limit = segment.pos + n;
        this.pos += n;
        this.prev.push(segment);
        return segment;
    }
    
    Segment unsharedCopy() {
        return new Segment(this.data.clone(), this.pos, this.limit, false, true);
    }
    
    public void writeTo(final Segment segment, final int n) {
        if (!segment.owner) {
            throw new IllegalArgumentException();
        }
        if (segment.limit + n > 8192) {
            if (segment.shared) {
                throw new IllegalArgumentException();
            }
            if (segment.limit + n - segment.pos > 8192) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(segment.data, segment.pos, segment.data, 0, segment.limit - segment.pos);
            segment.limit -= segment.pos;
            segment.pos = 0;
        }
        System.arraycopy(this.data, this.pos, segment.data, segment.limit, n);
        segment.limit += n;
        this.pos += n;
    }
}
