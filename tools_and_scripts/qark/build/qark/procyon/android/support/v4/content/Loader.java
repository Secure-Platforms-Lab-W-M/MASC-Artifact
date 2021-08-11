// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.os.Handler;
import android.database.ContentObserver;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.v4.util.DebugUtils;
import android.content.Context;

public class Loader<D>
{
    boolean mAbandoned;
    boolean mContentChanged;
    Context mContext;
    int mId;
    OnLoadCompleteListener<D> mListener;
    OnLoadCanceledListener<D> mOnLoadCanceledListener;
    boolean mProcessingChange;
    boolean mReset;
    boolean mStarted;
    
    public Loader(final Context context) {
        this.mStarted = false;
        this.mAbandoned = false;
        this.mReset = true;
        this.mContentChanged = false;
        this.mProcessingChange = false;
        this.mContext = context.getApplicationContext();
    }
    
    public void abandon() {
        this.mAbandoned = true;
        this.onAbandon();
    }
    
    public boolean cancelLoad() {
        return this.onCancelLoad();
    }
    
    public void commitContentChanged() {
        this.mProcessingChange = false;
    }
    
    public String dataToString(final D n) {
        final StringBuilder sb = new StringBuilder(64);
        DebugUtils.buildShortClassTag(n, sb);
        sb.append("}");
        return sb.toString();
    }
    
    public void deliverCancellation() {
        final OnLoadCanceledListener<D> mOnLoadCanceledListener = this.mOnLoadCanceledListener;
        if (mOnLoadCanceledListener != null) {
            mOnLoadCanceledListener.onLoadCanceled(this);
        }
    }
    
    public void deliverResult(final D n) {
        final OnLoadCompleteListener<D> mListener = this.mListener;
        if (mListener != null) {
            mListener.onLoadComplete(this, n);
        }
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.print(s);
        printWriter.print("mId=");
        printWriter.print(this.mId);
        printWriter.print(" mListener=");
        printWriter.println(this.mListener);
        if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
            printWriter.print(s);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mContentChanged=");
            printWriter.print(this.mContentChanged);
            printWriter.print(" mProcessingChange=");
            printWriter.println(this.mProcessingChange);
        }
        if (!this.mAbandoned && !this.mReset) {
            return;
        }
        printWriter.print(s);
        printWriter.print("mAbandoned=");
        printWriter.print(this.mAbandoned);
        printWriter.print(" mReset=");
        printWriter.println(this.mReset);
    }
    
    public void forceLoad() {
        this.onForceLoad();
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public int getId() {
        return this.mId;
    }
    
    public boolean isAbandoned() {
        return this.mAbandoned;
    }
    
    public boolean isReset() {
        return this.mReset;
    }
    
    public boolean isStarted() {
        return this.mStarted;
    }
    
    protected void onAbandon() {
    }
    
    protected boolean onCancelLoad() {
        return false;
    }
    
    public void onContentChanged() {
        if (this.mStarted) {
            this.forceLoad();
            return;
        }
        this.mContentChanged = true;
    }
    
    protected void onForceLoad() {
    }
    
    protected void onReset() {
    }
    
    protected void onStartLoading() {
    }
    
    protected void onStopLoading() {
    }
    
    public void registerListener(final int mId, final OnLoadCompleteListener<D> mListener) {
        if (this.mListener == null) {
            this.mListener = mListener;
            this.mId = mId;
            return;
        }
        throw new IllegalStateException("There is already a listener registered");
    }
    
    public void registerOnLoadCanceledListener(final OnLoadCanceledListener<D> mOnLoadCanceledListener) {
        if (this.mOnLoadCanceledListener == null) {
            this.mOnLoadCanceledListener = mOnLoadCanceledListener;
            return;
        }
        throw new IllegalStateException("There is already a listener registered");
    }
    
    public void reset() {
        this.onReset();
        this.mReset = true;
        this.mStarted = false;
        this.mAbandoned = false;
        this.mContentChanged = false;
        this.mProcessingChange = false;
    }
    
    public void rollbackContentChanged() {
        if (this.mProcessingChange) {
            this.onContentChanged();
        }
    }
    
    public final void startLoading() {
        this.mStarted = true;
        this.mReset = false;
        this.mAbandoned = false;
        this.onStartLoading();
    }
    
    public void stopLoading() {
        this.mStarted = false;
        this.onStopLoading();
    }
    
    public boolean takeContentChanged() {
        final boolean mContentChanged = this.mContentChanged;
        this.mContentChanged = false;
        this.mProcessingChange |= mContentChanged;
        return mContentChanged;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        DebugUtils.buildShortClassTag(this, sb);
        sb.append(" id=");
        sb.append(this.mId);
        sb.append("}");
        return sb.toString();
    }
    
    public void unregisterListener(final OnLoadCompleteListener<D> onLoadCompleteListener) {
        final OnLoadCompleteListener<D> mListener = this.mListener;
        if (mListener == null) {
            throw new IllegalStateException("No listener register");
        }
        if (mListener == onLoadCompleteListener) {
            this.mListener = null;
            return;
        }
        throw new IllegalArgumentException("Attempting to unregister the wrong listener");
    }
    
    public void unregisterOnLoadCanceledListener(final OnLoadCanceledListener<D> onLoadCanceledListener) {
        final OnLoadCanceledListener<D> mOnLoadCanceledListener = this.mOnLoadCanceledListener;
        if (mOnLoadCanceledListener == null) {
            throw new IllegalStateException("No listener register");
        }
        if (mOnLoadCanceledListener == onLoadCanceledListener) {
            this.mOnLoadCanceledListener = null;
            return;
        }
        throw new IllegalArgumentException("Attempting to unregister the wrong listener");
    }
    
    public final class ForceLoadContentObserver extends ContentObserver
    {
        public ForceLoadContentObserver() {
            super(new Handler());
        }
        
        public boolean deliverSelfNotifications() {
            return true;
        }
        
        public void onChange(final boolean b) {
            Loader.this.onContentChanged();
        }
    }
    
    public interface OnLoadCanceledListener<D>
    {
        void onLoadCanceled(final Loader<D> p0);
    }
    
    public interface OnLoadCompleteListener<D>
    {
        void onLoadComplete(final Loader<D> p0, final D p1);
    }
}
