package androidx.cursoradapter.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
   private SimpleCursorAdapter.CursorToStringConverter mCursorToStringConverter;
   protected int[] mFrom;
   String[] mOriginalFrom;
   private int mStringConversionColumn = -1;
   protected int[] mTo;
   private SimpleCursorAdapter.ViewBinder mViewBinder;

   @Deprecated
   public SimpleCursorAdapter(Context var1, int var2, Cursor var3, String[] var4, int[] var5) {
      super(var1, var2, var3);
      this.mTo = var5;
      this.mOriginalFrom = var4;
      this.findColumns(var3, var4);
   }

   public SimpleCursorAdapter(Context var1, int var2, Cursor var3, String[] var4, int[] var5, int var6) {
      super(var1, var2, var3, var6);
      this.mTo = var5;
      this.mOriginalFrom = var4;
      this.findColumns(var3, var4);
   }

   private void findColumns(Cursor var1, String[] var2) {
      if (var1 == null) {
         this.mFrom = null;
      } else {
         int var4 = var2.length;
         int[] var5 = this.mFrom;
         if (var5 == null || var5.length != var4) {
            this.mFrom = new int[var4];
         }

         for(int var3 = 0; var3 < var4; ++var3) {
            this.mFrom[var3] = var1.getColumnIndexOrThrow(var2[var3]);
         }

      }
   }

   public void bindView(View var1, Context var2, Cursor var3) {
      SimpleCursorAdapter.ViewBinder var9 = this.mViewBinder;
      int var5 = this.mTo.length;
      int[] var10 = this.mFrom;
      int[] var11 = this.mTo;

      for(int var4 = 0; var4 < var5; ++var4) {
         View var8 = var1.findViewById(var11[var4]);
         if (var8 != null) {
            boolean var6 = false;
            if (var9 != null) {
               var6 = var9.setViewValue(var8, var3, var10[var4]);
            }

            if (!var6) {
               String var7 = var3.getString(var10[var4]);
               String var13 = var7;
               if (var7 == null) {
                  var13 = "";
               }

               if (var8 instanceof TextView) {
                  this.setViewText((TextView)var8, var13);
               } else {
                  if (!(var8 instanceof ImageView)) {
                     StringBuilder var12 = new StringBuilder();
                     var12.append(var8.getClass().getName());
                     var12.append(" is not a ");
                     var12.append(" view that can be bounds by this SimpleCursorAdapter");
                     throw new IllegalStateException(var12.toString());
                  }

                  this.setViewImage((ImageView)var8, var13);
               }
            }
         }
      }

   }

   public void changeCursorAndColumns(Cursor var1, String[] var2, int[] var3) {
      this.mOriginalFrom = var2;
      this.mTo = var3;
      this.findColumns(var1, var2);
      super.changeCursor(var1);
   }

   public CharSequence convertToString(Cursor var1) {
      SimpleCursorAdapter.CursorToStringConverter var3 = this.mCursorToStringConverter;
      if (var3 != null) {
         return var3.convertToString(var1);
      } else {
         int var2 = this.mStringConversionColumn;
         return (CharSequence)(var2 > -1 ? var1.getString(var2) : super.convertToString(var1));
      }
   }

   public SimpleCursorAdapter.CursorToStringConverter getCursorToStringConverter() {
      return this.mCursorToStringConverter;
   }

   public int getStringConversionColumn() {
      return this.mStringConversionColumn;
   }

   public SimpleCursorAdapter.ViewBinder getViewBinder() {
      return this.mViewBinder;
   }

   public void setCursorToStringConverter(SimpleCursorAdapter.CursorToStringConverter var1) {
      this.mCursorToStringConverter = var1;
   }

   public void setStringConversionColumn(int var1) {
      this.mStringConversionColumn = var1;
   }

   public void setViewBinder(SimpleCursorAdapter.ViewBinder var1) {
      this.mViewBinder = var1;
   }

   public void setViewImage(ImageView var1, String var2) {
      try {
         var1.setImageResource(Integer.parseInt(var2));
      } catch (NumberFormatException var4) {
         var1.setImageURI(Uri.parse(var2));
      }
   }

   public void setViewText(TextView var1, String var2) {
      var1.setText(var2);
   }

   public Cursor swapCursor(Cursor var1) {
      this.findColumns(var1, this.mOriginalFrom);
      return super.swapCursor(var1);
   }

   public interface CursorToStringConverter {
      CharSequence convertToString(Cursor var1);
   }

   public interface ViewBinder {
      boolean setViewValue(View var1, Cursor var2, int var3);
   }
}
