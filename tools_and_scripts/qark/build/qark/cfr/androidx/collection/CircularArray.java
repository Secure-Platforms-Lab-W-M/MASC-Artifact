/*
 * Decompiled with CFR 0_124.
 */
package androidx.collection;

public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int n) {
        if (n >= 1) {
            if (n <= 1073741824) {
                if (Integer.bitCount(n) != 1) {
                    n = Integer.highestOneBit(n - 1) << 1;
                }
                this.mCapacityBitmask = n - 1;
                this.mElements = new Object[n];
                return;
            }
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
        throw new IllegalArgumentException("capacity must be >= 1");
    }

    private void doubleCapacity() {
        E[] arrE = this.mElements;
        int n = arrE.length;
        int n2 = this.mHead;
        int n3 = n - n2;
        int n4 = n << 1;
        if (n4 >= 0) {
            Object[] arrobject = new Object[n4];
            System.arraycopy(arrE, n2, arrobject, 0, n3);
            System.arraycopy(this.mElements, 0, arrobject, n3, this.mHead);
            this.mElements = arrobject;
            this.mHead = 0;
            this.mTail = n;
            this.mCapacityBitmask = n4 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(E e) {
        int n;
        this.mHead = n = this.mHead - 1 & this.mCapacityBitmask;
        this.mElements[n] = e;
        if (n == this.mTail) {
            this.doubleCapacity();
        }
    }

    public void addLast(E e) {
        E[] arrE = this.mElements;
        int n = this.mTail;
        arrE[n] = e;
        this.mTail = n = this.mCapacityBitmask & n + 1;
        if (n == this.mHead) {
            this.doubleCapacity();
        }
    }

    public void clear() {
        this.removeFromStart(this.size());
    }

    public E get(int n) {
        if (n >= 0 && n < this.size()) {
            return this.mElements[this.mHead + n & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            return this.mElements[n];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getLast() {
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

    public E popFirst() {
        int n = this.mHead;
        if (n != this.mTail) {
            E[] arrE = this.mElements;
            E e = arrE[n];
            arrE[n] = null;
            this.mHead = n + 1 & this.mCapacityBitmask;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E popLast() {
        int n = this.mHead;
        int n2 = this.mTail;
        if (n != n2) {
            n = this.mCapacityBitmask & n2 - 1;
            E[] arrE = this.mElements;
            E e = arrE[n];
            arrE[n] = null;
            this.mTail = n;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            int n2;
            int n3 = 0;
            int n4 = this.mTail;
            if (n < n4) {
                n3 = n4 - n;
            }
            for (n4 = n3; n4 < (n2 = this.mTail); ++n4) {
                this.mElements[n4] = null;
            }
            n3 = n2 - n3;
            this.mTail = n2 - n3;
            if ((n -= n3) > 0) {
                this.mTail = n3 = this.mElements.length;
                for (n = n3 -= n; n < this.mTail; ++n) {
                    this.mElements[n] = null;
                }
                this.mTail = n3;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromStart(int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.size()) {
            int n2 = this.mElements.length;
            int n3 = this.mHead;
            int n4 = n2;
            if (n < n2 - n3) {
                n4 = n3 + n;
            }
            for (n2 = this.mHead; n2 < n4; ++n2) {
                this.mElements[n2] = null;
            }
            n2 = this.mHead;
            n3 = n4 - n2;
            n4 = n - n3;
            this.mHead = n2 + n3 & this.mCapacityBitmask;
            if (n4 > 0) {
                for (n = 0; n < n4; ++n) {
                    this.mElements[n] = null;
                }
                this.mHead = n4;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}

