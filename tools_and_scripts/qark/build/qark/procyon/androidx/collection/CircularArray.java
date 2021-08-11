// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.collection;

public final class CircularArray<E>
{
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;
    
    public CircularArray() {
        this(8);
    }
    
    public CircularArray(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        if (n <= 1073741824) {
            if (Integer.bitCount(n) != 1) {
                n = Integer.highestOneBit(n - 1) << 1;
            }
            this.mCapacityBitmask = n - 1;
            this.mElements = (E[])new Object[n];
            return;
        }
        throw new IllegalArgumentException("capacity must be <= 2^30");
    }
    
    private void doubleCapacity() {
        final E[] mElements = this.mElements;
        final int length = mElements.length;
        final int mHead = this.mHead;
        final int n = length - mHead;
        final int n2 = length << 1;
        if (n2 >= 0) {
            final Object[] array = new Object[n2];
            System.arraycopy(mElements, mHead, array, 0, n);
            System.arraycopy(this.mElements, 0, array, n, this.mHead);
            this.mElements = (E[])array;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = n2 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }
    
    public void addFirst(final E e) {
        final int mHead = this.mHead - 1 & this.mCapacityBitmask;
        this.mHead = mHead;
        this.mElements[mHead] = e;
        if (mHead == this.mTail) {
            this.doubleCapacity();
        }
    }
    
    public void addLast(final E e) {
        final E[] mElements = this.mElements;
        final int mTail = this.mTail;
        mElements[mTail] = e;
        final int mTail2 = this.mCapacityBitmask & mTail + 1;
        this.mTail = mTail2;
        if (mTail2 == this.mHead) {
            this.doubleCapacity();
        }
    }
    
    public void clear() {
        this.removeFromStart(this.size());
    }
    
    public E get(final int n) {
        if (n >= 0 && n < this.size()) {
            return this.mElements[this.mHead + n & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public E getFirst() {
        final int mHead = this.mHead;
        if (mHead != this.mTail) {
            return this.mElements[mHead];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public E getLast() {
        final int mHead = this.mHead;
        final int mTail = this.mTail;
        if (mHead != mTail) {
            return this.mElements[mTail - 1 & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }
    
    public E popFirst() {
        final int mHead = this.mHead;
        if (mHead != this.mTail) {
            final E[] mElements = this.mElements;
            final E e = mElements[mHead];
            mElements[mHead] = null;
            this.mHead = (mHead + 1 & this.mCapacityBitmask);
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public E popLast() {
        final int mHead = this.mHead;
        final int mTail = this.mTail;
        if (mHead != mTail) {
            final int mTail2 = this.mCapacityBitmask & mTail - 1;
            final E[] mElements = this.mElements;
            final E e = mElements[mTail2];
            mElements[mTail2] = null;
            this.mTail = mTail2;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public void removeFromEnd(int i) {
        if (i <= 0) {
            return;
        }
        if (i <= this.size()) {
            int n = 0;
            final int mTail = this.mTail;
            if (i < mTail) {
                n = mTail - i;
            }
            int n2 = n;
            int mTail2;
            while (true) {
                mTail2 = this.mTail;
                if (n2 >= mTail2) {
                    break;
                }
                this.mElements[n2] = null;
                ++n2;
            }
            final int n3 = mTail2 - n;
            i -= n3;
            this.mTail = mTail2 - n3;
            if (i > 0) {
                final int length = this.mElements.length;
                this.mTail = length;
                int mTail3;
                for (mTail3 = (i = length - i); i < this.mTail; ++i) {
                    this.mElements[i] = null;
                }
                this.mTail = mTail3;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public void removeFromStart(int i) {
        if (i <= 0) {
            return;
        }
        if (i <= this.size()) {
            final int length = this.mElements.length;
            final int mHead = this.mHead;
            int n = length;
            if (i < length - mHead) {
                n = mHead + i;
            }
            for (int j = this.mHead; j < n; ++j) {
                this.mElements[j] = null;
            }
            final int mHead2 = this.mHead;
            final int n2 = n - mHead2;
            final int mHead3 = i - n2;
            this.mHead = (mHead2 + n2 & this.mCapacityBitmask);
            if (mHead3 > 0) {
                for (i = 0; i < mHead3; ++i) {
                    this.mElements[i] = null;
                }
                this.mHead = mHead3;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}
