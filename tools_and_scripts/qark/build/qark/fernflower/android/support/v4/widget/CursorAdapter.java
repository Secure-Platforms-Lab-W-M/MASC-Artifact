package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilter.CursorFilterClient {
   @Deprecated
   public static final int FLAG_AUTO_REQUERY = 1;
   public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected boolean mAutoRequery;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected CursorAdapter.ChangeObserver mChangeObserver;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected Context mContext;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected Cursor mCursor;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected CursorFilter mCursorFilter;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected DataSetObserver mDataSetObserver;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected boolean mDataValid;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected FilterQueryProvider mFilterQueryProvider;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected int mRowIDColumn;

   @Deprecated
   public CursorAdapter(Context var1, Cursor var2) {
      this.init(var1, var2, 1);
   }

   public CursorAdapter(Context var1, Cursor var2, int var3) {
      this.init(var1, var2, var3);
   }

   public CursorAdapter(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
   }

   public abstract void bindView(View var1, Context var2, Cursor var3);

   public void changeCursor(Cursor var1) {
      var1 = this.swapCursor(var1);
      if (var1 != null) {
         var1.close();
      }

   }

   public CharSequence convertToString(Cursor var1) {
      return var1 == null ? "" : var1.toString();
   }

   public int getCount() {
      if (this.mDataValid) {
         Cursor var1 = this.mCursor;
         if (var1 != null) {
            return var1.getCount();
         }
      }

      return 0;
   }

   public Cursor getCursor() {
      return this.mCursor;
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         this.mCursor.moveToPosition(var1);
         if (var2 == null) {
            var2 = this.newDropDownView(this.mContext, this.mCursor, var3);
         }

         this.bindView(var2, this.mContext, this.mCursor);
         return var2;
      } else {
         return null;
      }
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

   public Object getItem(int var1) {
      if (this.mDataValid) {
         Cursor var2 = this.mCursor;
         if (var2 != null) {
            var2.moveToPosition(var1);
            return this.mCursor;
         }
      }

      return null;
   }

   public long getItemId(int var1) {
      if (this.mDataValid) {
         Cursor var2 = this.mCursor;
         if (var2 != null) {
            if (var2.moveToPosition(var1)) {
               return this.mCursor.getLong(this.mRowIDColumn);
            }

            return 0L;
         }
      }

      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (this.mDataValid) {
         if (this.mCursor.moveToPosition(var1)) {
            if (var2 == null) {
               var2 = this.newView(this.mContext, this.mCursor, var3);
            }

            this.bindView(var2, this.mContext, this.mCursor);
            return var2;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("couldn't move cursor to position ");
            var4.append(var1);
            throw new IllegalStateException(var4.toString());
         }
      } else {
         throw new IllegalStateException("this should only be called when the cursor is valid");
      }
   }

   public boolean hasStableIds() {
      return true;
   }

   void init(Context var1, Cursor var2, int var3) {
      boolean var5 = false;
      if ((var3 & 1) == 1) {
         var3 |= 2;
         this.mAutoRequery = true;
      } else {
         this.mAutoRequery = false;
      }

      if (var2 != null) {
         var5 = true;
      }

      this.mCursor = var2;
      this.mDataValid = var5;
      this.mContext = var1;
      int var4;
      if (var5) {
         var4 = var2.getColumnIndexOrThrow("_id");
      } else {
         var4 = -1;
      }

      this.mRowIDColumn = var4;
      if ((var3 & 2) == 2) {
         this.mChangeObserver = new CursorAdapter.ChangeObserver();
         this.mDataSetObserver = new CursorAdapter.MyDataSetObserver();
      } else {
         this.mChangeObserver = null;
         this.mDataSetObserver = null;
      }

      if (var5) {
         CursorAdapter.ChangeObserver var6 = this.mChangeObserver;
         if (var6 != null) {
            var2.registerContentObserver(var6);
         }

         DataSetObserver var7 = this.mDataSetObserver;
         if (var7 != null) {
            var2.registerDataSetObserver(var7);
         }
      }

   }

   @Deprecated
   protected void init(Context var1, Cursor var2, boolean var3) {
      byte var4;
      if (var3) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      this.init(var1, var2, var4);
   }

   public View newDropDownView(Context var1, Cursor var2, ViewGroup var3) {
      return this.newView(var1, var2, var3);
   }

   public abstract View newView(Context var1, Cursor var2, ViewGroup var3);

   protected void onContentChanged() {
      if (this.mAutoRequery) {
         Cursor var1 = this.mCursor;
         if (var1 != null && !var1.isClosed()) {
            this.mDataValid = this.mCursor.requery();
         }
      }

   }

   public Cursor runQueryOnBackgroundThread(CharSequence var1) {
      FilterQueryProvider var2 = this.mFilterQueryProvider;
      return var2 != null ? var2.runQuery(var1) : this.mCursor;
   }

   public void setFilterQueryProvider(FilterQueryProvider var1) {
      this.mFilterQueryProvider = var1;
   }

   public Cursor swapCursor(Cursor var1) {
      if (var1 == this.mCursor) {
         return null;
      } else {
         Cursor var2 = this.mCursor;
         CursorAdapter.ChangeObserver var3;
         DataSetObserver var4;
         if (var2 != null) {
            var3 = this.mChangeObserver;
            if (var3 != null) {
               var2.unregisterContentObserver(var3);
            }

            var4 = this.mDataSetObserver;
            if (var4 != null) {
               var2.unregisterDataSetObserver(var4);
            }
         }

         this.mCursor = var1;
         if (var1 != null) {
            var3 = this.mChangeObserver;
            if (var3 != null) {
               var1.registerContentObserver(var3);
            }

            var4 = this.mDataSetObserver;
            if (var4 != null) {
               var1.registerDataSetObserver(var4);
            }

            this.mRowIDColumn = var1.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            this.notifyDataSetChanged();
            return var2;
         } else {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
            this.notifyDataSetInvalidated();
            return var2;
         }
      }
   }

   private class ChangeObserver extends ContentObserver {
      ChangeObserver() {
         super(new Handler());
      }

      public boolean deliverSelfNotifications() {
         return true;
      }

      public void onChange(boolean var1) {
         CursorAdapter.this.onContentChanged();
      }
   }

   private class MyDataSetObserver extends DataSetObserver {
      MyDataSetObserver() {
      }

      public void onChanged() {
         CursorAdapter var1 = CursorAdapter.this;
         var1.mDataValid = true;
         var1.notifyDataSetChanged();
      }

      public void onInvalidated() {
         CursorAdapter var1 = CursorAdapter.this;
         var1.mDataValid = false;
         var1.notifyDataSetInvalidated();
      }
   }
}
