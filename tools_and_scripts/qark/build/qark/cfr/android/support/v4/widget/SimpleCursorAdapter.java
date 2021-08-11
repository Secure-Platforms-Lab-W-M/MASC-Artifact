/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.view.View
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter
extends ResourceCursorAdapter {
    private CursorToStringConverter mCursorToStringConverter;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected int[] mFrom;
    String[] mOriginalFrom;
    private int mStringConversionColumn = -1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected int[] mTo;
    private ViewBinder mViewBinder;

    @Deprecated
    public SimpleCursorAdapter(Context context, int n, Cursor cursor, String[] arrstring, int[] arrn) {
        super(context, n, cursor);
        this.mTo = arrn;
        this.mOriginalFrom = arrstring;
        this.findColumns(cursor, arrstring);
    }

    public SimpleCursorAdapter(Context context, int n, Cursor cursor, String[] arrstring, int[] arrn, int n2) {
        super(context, n, cursor, n2);
        this.mTo = arrn;
        this.mOriginalFrom = arrstring;
        this.findColumns(cursor, arrstring);
    }

    private void findColumns(Cursor cursor, String[] arrstring) {
        if (cursor != null) {
            int n = arrstring.length;
            int[] arrn = this.mFrom;
            if (arrn == null || arrn.length != n) {
                this.mFrom = new int[n];
            }
            for (int i = 0; i < n; ++i) {
                this.mFrom[i] = cursor.getColumnIndexOrThrow(arrstring[i]);
            }
            return;
        }
        this.mFrom = null;
    }

    @Override
    public void bindView(View object, Context object2, Cursor cursor) {
        ViewBinder viewBinder = this.mViewBinder;
        int n = this.mTo.length;
        int[] arrn = this.mFrom;
        int[] arrn2 = this.mTo;
        for (int i = 0; i < n; ++i) {
            View view = object.findViewById(arrn2[i]);
            if (view == null) continue;
            boolean bl = false;
            if (viewBinder != null) {
                bl = viewBinder.setViewValue(view, cursor, arrn[i]);
            }
            if (bl) continue;
            object2 = cursor.getString(arrn[i]);
            if (object2 == null) {
                object2 = "";
            }
            if (view instanceof TextView) {
                this.setViewText((TextView)view, (String)object2);
                continue;
            }
            if (view instanceof ImageView) {
                this.setViewImage((ImageView)view, (String)object2);
                continue;
            }
            object = new StringBuilder();
            object.append(view.getClass().getName());
            object.append(" is not a ");
            object.append(" view that can be bounds by this SimpleCursorAdapter");
            throw new IllegalStateException(object.toString());
        }
    }

    public void changeCursorAndColumns(Cursor cursor, String[] arrstring, int[] arrn) {
        this.mOriginalFrom = arrstring;
        this.mTo = arrn;
        this.findColumns(cursor, this.mOriginalFrom);
        super.changeCursor(cursor);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        CursorToStringConverter cursorToStringConverter = this.mCursorToStringConverter;
        if (cursorToStringConverter != null) {
            return cursorToStringConverter.convertToString(cursor);
        }
        int n = this.mStringConversionColumn;
        if (n > -1) {
            return cursor.getString(n);
        }
        return super.convertToString(cursor);
    }

    public CursorToStringConverter getCursorToStringConverter() {
        return this.mCursorToStringConverter;
    }

    public int getStringConversionColumn() {
        return this.mStringConversionColumn;
    }

    public ViewBinder getViewBinder() {
        return this.mViewBinder;
    }

    public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
        this.mCursorToStringConverter = cursorToStringConverter;
    }

    public void setStringConversionColumn(int n) {
        this.mStringConversionColumn = n;
    }

    public void setViewBinder(ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
    }

    public void setViewImage(ImageView imageView, String string2) {
        try {
            imageView.setImageResource(Integer.parseInt(string2));
            return;
        }
        catch (NumberFormatException numberFormatException) {
            imageView.setImageURI(Uri.parse((String)string2));
            return;
        }
    }

    public void setViewText(TextView textView, String string2) {
        textView.setText((CharSequence)string2);
    }

    @Override
    public Cursor swapCursor(Cursor cursor) {
        this.findColumns(cursor, this.mOriginalFrom);
        return super.swapCursor(cursor);
    }

    public static interface CursorToStringConverter {
        public CharSequence convertToString(Cursor var1);
    }

    public static interface ViewBinder {
        public boolean setViewValue(View var1, Cursor var2, int var3);
    }

}

