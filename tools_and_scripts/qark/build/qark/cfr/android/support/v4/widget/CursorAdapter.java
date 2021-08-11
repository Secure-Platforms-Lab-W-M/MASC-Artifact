/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.ContentObserver
 *  android.database.Cursor
 *  android.database.DataSetObserver
 *  android.os.Handler
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.Filter
 *  android.widget.FilterQueryProvider
 *  android.widget.Filterable
 */
package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.CursorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter
extends BaseAdapter
implements Filterable,
CursorFilter.CursorFilterClient {
    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 1;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected boolean mAutoRequery;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected ChangeObserver mChangeObserver;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected Context mContext;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected Cursor mCursor;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected CursorFilter mCursorFilter;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected DataSetObserver mDataSetObserver;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected boolean mDataValid;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected FilterQueryProvider mFilterQueryProvider;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected int mRowIDColumn;

    @Deprecated
    public CursorAdapter(Context context, Cursor cursor) {
        this.init(context, cursor, 1);
    }

    public CursorAdapter(Context context, Cursor cursor, int n) {
        this.init(context, cursor, n);
    }

    public CursorAdapter(Context context, Cursor cursor, boolean bl) {
        int n = bl ? 1 : 2;
        this.init(context, cursor, n);
    }

    public abstract void bindView(View var1, Context var2, Cursor var3);

    @Override
    public void changeCursor(Cursor cursor) {
        if ((cursor = this.swapCursor(cursor)) != null) {
            cursor.close();
            return;
        }
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        if (cursor == null) {
            return "";
        }
        return cursor.toString();
    }

    public int getCount() {
        Cursor cursor;
        if (this.mDataValid && (cursor = this.mCursor) != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public Cursor getCursor() {
        return this.mCursor;
    }

    public View getDropDownView(int n, View view, ViewGroup viewGroup) {
        if (this.mDataValid) {
            this.mCursor.moveToPosition(n);
            if (view == null) {
                view = this.newDropDownView(this.mContext, this.mCursor, viewGroup);
            }
            this.bindView(view, this.mContext, this.mCursor);
            return view;
        }
        return null;
    }

    public Filter getFilter() {
        if (this.mCursorFilter == null) {
            this.mCursorFilter = new CursorFilter(this);
        }
        return this.mCursorFilter;
    }

    public FilterQueryProvider getFilterQueryProvider() {
        return this.mFilterQueryProvider;
    }

    public Object getItem(int n) {
        Cursor cursor;
        if (this.mDataValid && (cursor = this.mCursor) != null) {
            cursor.moveToPosition(n);
            return this.mCursor;
        }
        return null;
    }

    public long getItemId(int n) {
        Cursor cursor;
        if (this.mDataValid && (cursor = this.mCursor) != null) {
            if (cursor.moveToPosition(n)) {
                return this.mCursor.getLong(this.mRowIDColumn);
            }
            return 0L;
        }
        return 0L;
    }

    public View getView(int n, View object, ViewGroup viewGroup) {
        if (this.mDataValid) {
            if (this.mCursor.moveToPosition(n)) {
                if (object == null) {
                    object = this.newView(this.mContext, this.mCursor, viewGroup);
                }
                this.bindView((View)object, this.mContext, this.mCursor);
                return object;
            }
            object = new StringBuilder();
            object.append("couldn't move cursor to position ");
            object.append(n);
            throw new IllegalStateException(object.toString());
        }
        throw new IllegalStateException("this should only be called when the cursor is valid");
    }

    public boolean hasStableIds() {
        return true;
    }

    void init(Context object, Cursor cursor, int n) {
        boolean bl = false;
        if ((n & 1) == 1) {
            n |= 2;
            this.mAutoRequery = true;
        } else {
            this.mAutoRequery = false;
        }
        if (cursor != null) {
            bl = true;
        }
        this.mCursor = cursor;
        this.mDataValid = bl;
        this.mContext = object;
        int n2 = bl ? cursor.getColumnIndexOrThrow("_id") : -1;
        this.mRowIDColumn = n2;
        if ((n & 2) == 2) {
            this.mChangeObserver = new ChangeObserver();
            this.mDataSetObserver = new MyDataSetObserver();
        } else {
            this.mChangeObserver = null;
            this.mDataSetObserver = null;
        }
        if (bl) {
            object = this.mChangeObserver;
            if (object != null) {
                cursor.registerContentObserver((ContentObserver)object);
            }
            if ((object = this.mDataSetObserver) != null) {
                cursor.registerDataSetObserver((DataSetObserver)object);
                return;
            }
            return;
        }
    }

    @Deprecated
    protected void init(Context context, Cursor cursor, boolean bl) {
        int n = bl ? 1 : 2;
        this.init(context, cursor, n);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.newView(context, cursor, viewGroup);
    }

    public abstract View newView(Context var1, Cursor var2, ViewGroup var3);

    protected void onContentChanged() {
        Cursor cursor;
        if (this.mAutoRequery && (cursor = this.mCursor) != null && !cursor.isClosed()) {
            this.mDataValid = this.mCursor.requery();
            return;
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence charSequence) {
        FilterQueryProvider filterQueryProvider = this.mFilterQueryProvider;
        if (filterQueryProvider != null) {
            return filterQueryProvider.runQuery(charSequence);
        }
        return this.mCursor;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        this.mFilterQueryProvider = filterQueryProvider;
    }

    public Cursor swapCursor(Cursor cursor) {
        ChangeObserver changeObserver;
        if (cursor == this.mCursor) {
            return null;
        }
        Cursor cursor2 = this.mCursor;
        if (cursor2 != null) {
            changeObserver = this.mChangeObserver;
            if (changeObserver != null) {
                cursor2.unregisterContentObserver((ContentObserver)changeObserver);
            }
            if ((changeObserver = this.mDataSetObserver) != null) {
                cursor2.unregisterDataSetObserver((DataSetObserver)changeObserver);
            }
        }
        this.mCursor = cursor;
        if (cursor != null) {
            changeObserver = this.mChangeObserver;
            if (changeObserver != null) {
                cursor.registerContentObserver((ContentObserver)changeObserver);
            }
            if ((changeObserver = this.mDataSetObserver) != null) {
                cursor.registerDataSetObserver((DataSetObserver)changeObserver);
            }
            this.mRowIDColumn = cursor.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
            return cursor2;
        }
        this.mRowIDColumn = -1;
        this.mDataValid = false;
        this.notifyDataSetInvalidated();
        return cursor2;
    }

    private class ChangeObserver
    extends ContentObserver {
        ChangeObserver() {
            super(new Handler());
        }

        public boolean deliverSelfNotifications() {
            return true;
        }

        public void onChange(boolean bl) {
            CursorAdapter.this.onContentChanged();
        }
    }

    private class MyDataSetObserver
    extends DataSetObserver {
        MyDataSetObserver() {
        }

        public void onChanged() {
            CursorAdapter cursorAdapter = CursorAdapter.this;
            cursorAdapter.mDataValid = true;
            cursorAdapter.notifyDataSetChanged();
        }

        public void onInvalidated() {
            CursorAdapter cursorAdapter = CursorAdapter.this;
            cursorAdapter.mDataValid = false;
            cursorAdapter.notifyDataSetInvalidated();
        }
    }

}

