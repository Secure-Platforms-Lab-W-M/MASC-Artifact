package androidx.cursoradapter.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceCursorAdapter extends CursorAdapter {
   private int mDropDownLayout;
   private LayoutInflater mInflater;
   private int mLayout;

   @Deprecated
   public ResourceCursorAdapter(Context var1, int var2, Cursor var3) {
      super(var1, var3);
      this.mDropDownLayout = var2;
      this.mLayout = var2;
      this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
   }

   public ResourceCursorAdapter(Context var1, int var2, Cursor var3, int var4) {
      super(var1, var3, var4);
      this.mDropDownLayout = var2;
      this.mLayout = var2;
      this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
   }

   @Deprecated
   public ResourceCursorAdapter(Context var1, int var2, Cursor var3, boolean var4) {
      super(var1, var3, var4);
      this.mDropDownLayout = var2;
      this.mLayout = var2;
      this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
   }

   public View newDropDownView(Context var1, Cursor var2, ViewGroup var3) {
      return this.mInflater.inflate(this.mDropDownLayout, var3, false);
   }

   public View newView(Context var1, Cursor var2, ViewGroup var3) {
      return this.mInflater.inflate(this.mLayout, var3, false);
   }

   public void setDropDownViewResource(int var1) {
      this.mDropDownLayout = var1;
   }

   public void setViewResource(int var1) {
      this.mLayout = var1;
   }
}
