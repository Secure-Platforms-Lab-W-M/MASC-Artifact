/*
 * Decompiled with CFR 0_124.
 */
package androidx.collection;

public final class CircularIntArray {
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;

    public CircularIntArray() {
        this(8);
    }

    public CircularIntArray(int n) {
        if (n >= 1) {
            if (n <= 1073741824) {
                if (Integer.bitCount(n) != 1) {
                    n = Integer.highestOneBit(n - 1) << 1;
                }
                this.mCapacityBitmask = n - 1;
                this.mElements = new int[n];
                return;
            }
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
        throw new IllegalArgumentException("capacity must be >= 1");
    }

    private void doubleCapacity() {
        int[] arrn = this.mElements;
        int n = arrn.length;
        int n2 = this.mHead;
        int n3 = n - n2;
        int n4 = n << 1;
        if (n4 >= 0) {
            int[] arrn2 = new int[n4];
            System.arraycopy(arrn, n2, arrn2, 0, n3);
            System.arraycopy(this.mElements, 0, arrn2, n3, this.mHead);
            this.mElements = arrn2;
            this.mHead = 0;
            this.mTail = n;
            this.mCapacityBitmask = n4 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(int n) {
        int n2;
        this.mHead = n2 = this.mHead - 1 & this.mCapacityBitmask;
        this.mElements[n2] = n;
        if (n2 == this.mTail) {
            this.doubleCapacity();
        }
    }

    public void addLast(int n) {
        int[] arrn = this.mElements;
        int n2 = this.mTail;
        arrn[n2] = n;
        this.mTail = n = this.mCapacityBitmask & n2 + 1;
        if (n == this.mHead) {
            this.doubleCapacity();
        }
    }

    public void clear() {
        this.mTail = this.mHead;
    }

    public int get(int n) {
        if (n >= 0 && n < this.size()) {
            return this.mElements[this.mHead + n & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            return this.mElements[n];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getLast() {
        int n = this.mHead;
        int n2 = this.mTail;
        if (n != n2) {
            return this.mElements[n2 - 1 & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        if (this.mHead == this.mTail) {
            return true;
        }
        return false;
    }

    public int popFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            int n2 = this.mElements[n];
            this.mHead = n + 1 & this.mCapacityBitmask;
            return n2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int popLast() {
        int n = this.mHead;
        int n2 = this.mTail;
        if (n != n2) {
            n = this.mCapacityBitmask & n2 - 1;
            n2 = this.mElements[n];
            this.mTail = n;
            return n2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            this.mTail = this.mTail - n & this.mCapacityBitmask;
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromStart(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            this.mHead = this.mHead + n & this.mCapacityBitmask;
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}

