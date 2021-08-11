package androidx.appcompat.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.appcompat.widget.TintTypedArray;

public final class ExpandedMenuView extends ListView implements MenuBuilder.ItemInvoker, MenuView, OnItemClickListener {
   private static final int[] TINT_ATTRS = new int[]{16842964, 16843049};
   private int mAnimations;
   private MenuBuilder mMenu;

   public ExpandedMenuView(Context var1, AttributeSet var2) {
      this(var1, var2, 16842868);
   }

   public ExpandedMenuView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2);
      this.setOnItemClickListener(this);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, TINT_ATTRS, var3, 0);
      if (var4.hasValue(0)) {
         this.setBackgroundDrawable(var4.getDrawable(0));
      }

      if (var4.hasValue(1)) {
         this.setDivider(var4.getDrawable(1));
      }

      var4.recycle();
   }

   public int getWindowAnimations() {
      return this.mAnimations;
   }

   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.setChildrenDrawingCacheEnabled(false);
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      this.invokeItem((MenuItemImpl)this.getAdapter().getItem(var3));
   }
}
