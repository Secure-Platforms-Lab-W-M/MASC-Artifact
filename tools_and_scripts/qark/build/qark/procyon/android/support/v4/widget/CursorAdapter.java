// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.os.Handler;
import android.database.ContentObserver;
import android.widget.Filter;
import android.view.ViewGroup;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.database.DataSetObserver;
import android.database.Cursor;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.widget.Filterable;
import android.widget.BaseAdapter;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilterClient
{
    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 1;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected boolean mAutoRequery;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected ChangeObserver mChangeObserver;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected Context mContext;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected Cursor mCursor;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected CursorFilter mCursorFilter;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected DataSetObserver mDataSetObserver;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected boolean mDataValid;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected FilterQueryProvider mFilterQueryProvider;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected int mRowIDColumn;
    
    @Deprecated
    public CursorAdapter(final Context context, final Cursor cursor) {
        this.init(context, cursor, 1);
    }
    
    public CursorAdapter(final Context context, final Cursor cursor, final int n) {
        this.init(context, cursor, n);
    }
    
    public CursorAdapter(final Context context, final Cursor cursor, final boolean b) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        this.init(context, cursor, n);
    }
    
    public abstract void bindView(final View p0, final Context p1, final Cursor p2);
    
    public void changeCursor(Cursor swapCursor) {
        swapCursor = this.swapCursor(swapCursor);
        if (swapCursor != null) {
            swapCursor.close();
        }
    }
    
    public CharSequence convertToString(final Cursor cursor) {
        if (cursor == null) {
            return "";
        }
        return cursor.toString();
    }
    
    public int getCount() {
        if (this.mDataValid) {
            final Cursor mCursor = this.mCursor;
            if (mCursor != null) {
                return mCursor.getCount();
            }
        }
        return 0;
    }
    
    public Cursor getCursor() {
        return this.mCursor;
    }
    
    public View getDropDownView(final int n, View dropDownView, final ViewGroup viewGroup) {
        if (this.mDataValid) {
            this.mCursor.moveToPosition(n);
            if (dropDownView == null) {
                dropDownView = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            }
            this.bindView(dropDownView, this.mContext, this.mCursor);
            return dropDownView;
        }
        return null;
    }
    
    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            this.mCursorFilter = new CursorFilter((CursorFilter.CursorFilterClient)this);
        }
        return this.mCursorFilter;
    }
    
    public FilterQueryProvider getFilterQueryProvider() {
        return this.mFilterQueryProvider;
    }
    
    public Object getItem(final int n) {
        if (this.mDataValid) {
            final Cursor mCursor = this.mCursor;
            if (mCursor != null) {
                mCursor.moveToPosition(n);
                return this.mCursor;
            }
        }
        return null;
    }
    
    public long getItemId(final int n) {
        if (this.mDataValid) {
            final Cursor mCursor = this.mCursor;
            if (mCursor != null) {
                if (mCursor.moveToPosition(n)) {
                    return this.mCursor.getLong(this.mRowIDColumn);
                }
                return 0L;
            }
        }
        return 0L;
    }
    
    public View getView(final int n, View view, final ViewGroup viewGroup) {
        if (!this.mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (this.mCursor.moveToPosition(n)) {
            if (view == null) {
                view = this.newView(this.mContext, this.mCursor, viewGroup);
            }
            this.bindView(view, this.mContext, this.mCursor);
            return view;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("couldn't move cursor to position ");
        sb.append(n);
        throw new IllegalStateException(sb.toString());
    }
    
    public boolean hasStableIds() {
        return true;
    }
    
    void init(final Context mContext, final Cursor mCursor, int n) {
        boolean mDataValid = false;
        if ((n & 0x1) == 0x1) {
            n |= 0x2;
            this.mAutoRequery = true;
        }
        else {
            this.mAutoRequery = false;
        }
        if (mCursor != null) {
            mDataValid = true;
        }
        this.mCursor = mCursor;
        this.mDataValid = mDataValid;
        this.mContext = mContext;
        int columnIndexOrThrow;
        if (mDataValid) {
            columnIndexOrThrow = mCursor.getColumnIndexOrThrow("_id");
        }
        else {
            columnIndexOrThrow = -1;
        }
        this.mRowIDColumn = columnIndexOrThrow;
        if ((n & 0x2) == 0x2) {
            this.mChangeObserver = new ChangeObserver();
            this.mDataSetObserver = new MyDataSetObserver();
        }
        else {
            this.mChangeObserver = null;
            this.mDataSetObserver = null;
        }
        if (!mDataValid) {
            return;
        }
        final ChangeObserver mChangeObserver = this.mChangeObserver;
        if (mChangeObserver != null) {
            mCursor.registerContentObserver((ContentObserver)mChangeObserver);
        }
        final DataSetObserver mDataSetObserver = this.mDataSetObserver;
        if (mDataSetObserver != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }
    
    @Deprecated
    protected void init(final Context context, final Cursor cursor, final boolean b) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 2;
        }
        this.init(context, cursor, n);
    }
    
    public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        return this.newView(context, cursor, viewGroup);
    }
    
    public abstract View newView(final Context p0, final Cursor p1, final ViewGroup p2);
    
    protected void onContentChanged() {
        if (this.mAutoRequery) {
            final Cursor mCursor = this.mCursor;
            if (mCursor != null && !mCursor.isClosed()) {
                this.mDataValid = this.mCursor.requery();
            }
        }
    }
    
    public Cursor runQueryOnBackgroundThread(final CharSequence charSequence) {
        final FilterQueryProvider mFilterQueryProvider = this.mFilterQueryProvider;
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(charSequence);
        }
        return this.mCursor;
    }
    
    public void setFilterQueryProvider(final FilterQueryProvider mFilterQueryProvider) {
        this.mFilterQueryProvider = mFilterQueryProvider;
    }
    
    public Cursor swapCursor(final Cursor mCursor) {
        if (mCursor == this.mCursor) {
            return null;
        }
        final Cursor mCursor2 = this.mCursor;
        if (mCursor2 != null) {
            final ChangeObserver mChangeObserver = this.mChangeObserver;
            if (mChangeObserver != null) {
                mCursor2.unregisterContentObserver((ContentObserver)mChangeObserver);
            }
            final DataSetObserver mDataSetObserver = this.mDataSetObserver;
            if (mDataSetObserver != null) {
                mCursor2.unregisterDataSetObserver(mDataSetObserver);
            }
        }
        if ((this.mCursor = mCursor) != null) {
            final ChangeObserver mChangeObserver2 = this.mChangeObserver;
            if (mChangeObserver2 != null) {
                mCursor.registerContentObserver((ContentObserver)mChangeObserver2);
            }
            final DataSetObserver mDataSetObserver2 = this.mDataSetObserver;
            if (mDataSetObserver2 != null) {
                mCursor.registerDataSetObserver(mDataSetObserver2);
            }
            this.mRowIDColumn = mCursor.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
            return mCursor2;
        }
        this.mRowIDColumn = -1;
        this.mDataValid = false;
        this.notifyDataSetInvalidated();
        return mCursor2;
    }
    
    private class ChangeObserver extends ContentObserver
    {
        ChangeObserver() {
            super(new Handler());
        }
        
        public boolean deliverSelfNotifications() {
            return true;
        }
        
        public void onChange(final boolean b) {
            CursorAdapter.this.onContentChanged();
        }
    }
    
    private class MyDataSetObserver extends DataSetObserver
    {
        MyDataSetObserver() {
        }
        
        public void onChanged() {
            final CursorAdapter this$0 = CursorAdapter.this;
            this$0.mDataValid = true;
            this$0.notifyDataSetChanged();
        }
        
        public void onInvalidated() {
            final CursorAdapter this$0 = CursorAdapter.this;
            this$0.mDataValid = false;
            this$0.notifyDataSetInvalidated();
        }
    }
}
