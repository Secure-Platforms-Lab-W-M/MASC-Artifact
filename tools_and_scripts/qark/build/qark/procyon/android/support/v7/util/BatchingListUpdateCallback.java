// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.util;

public class BatchingListUpdateCallback implements ListUpdateCallback
{
    private static final int TYPE_ADD = 1;
    private static final int TYPE_CHANGE = 3;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_REMOVE = 2;
    int mLastEventCount;
    Object mLastEventPayload;
    int mLastEventPosition;
    int mLastEventType;
    final ListUpdateCallback mWrapped;
    
    public BatchingListUpdateCallback(final ListUpdateCallback mWrapped) {
        this.mLastEventType = 0;
        this.mLastEventPosition = -1;
        this.mLastEventCount = -1;
        this.mLastEventPayload = null;
        this.mWrapped = mWrapped;
    }
    
    public void dispatchLastEvent() {
        final int mLastEventType = this.mLastEventType;
        if (mLastEventType == 0) {
            return;
        }
        switch (mLastEventType) {
            case 3: {
                this.mWrapped.onChanged(this.mLastEventPosition, this.mLastEventCount, this.mLastEventPayload);
                break;
            }
            case 2: {
                this.mWrapped.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                break;
            }
            case 1: {
                this.mWrapped.onInserted(this.mLastEventPosition, this.mLastEventCount);
                break;
            }
        }
        this.mLastEventPayload = null;
        this.mLastEventType = 0;
    }
    
    @Override
    public void onChanged(final int mLastEventPosition, final int mLastEventCount, final Object mLastEventPayload) {
        if (this.mLastEventType == 3) {
            final int mLastEventPosition2 = this.mLastEventPosition;
            final int mLastEventCount2 = this.mLastEventCount;
            if (mLastEventPosition <= mLastEventPosition2 + mLastEventCount2 && mLastEventPosition + mLastEventCount >= mLastEventPosition2 && this.mLastEventPayload == mLastEventPayload) {
                this.mLastEventPosition = Math.min(mLastEventPosition, mLastEventPosition2);
                this.mLastEventCount = Math.max(mLastEventCount2 + mLastEventPosition2, mLastEventPosition + mLastEventCount) - this.mLastEventPosition;
                return;
            }
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = mLastEventPosition;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventPayload = mLastEventPayload;
        this.mLastEventType = 3;
    }
    
    @Override
    public void onInserted(final int mLastEventPosition, final int mLastEventCount) {
        if (this.mLastEventType == 1) {
            final int mLastEventPosition2 = this.mLastEventPosition;
            if (mLastEventPosition >= mLastEventPosition2) {
                final int mLastEventCount2 = this.mLastEventCount;
                if (mLastEventPosition <= mLastEventPosition2 + mLastEventCount2) {
                    this.mLastEventCount = mLastEventCount2 + mLastEventCount;
                    this.mLastEventPosition = Math.min(mLastEventPosition, mLastEventPosition2);
                    return;
                }
            }
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = mLastEventPosition;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventType = 1;
    }
    
    @Override
    public void onMoved(final int n, final int n2) {
        this.dispatchLastEvent();
        this.mWrapped.onMoved(n, n2);
    }
    
    @Override
    public void onRemoved(final int n, final int mLastEventCount) {
        if (this.mLastEventType == 2) {
            final int mLastEventPosition = this.mLastEventPosition;
            if (mLastEventPosition >= n && mLastEventPosition <= n + mLastEventCount) {
                this.mLastEventCount += mLastEventCount;
                this.mLastEventPosition = n;
                return;
            }
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = n;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventType = 2;
    }
}
