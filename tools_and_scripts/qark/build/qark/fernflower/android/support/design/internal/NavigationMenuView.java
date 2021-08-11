package android.support.design.internal;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenuView extends RecyclerView implements MenuView {
   public NavigationMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationMenuView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NavigationMenuView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.setLayoutManager(new LinearLayoutManager(var1, 1, false));
   }

   public int getWindowAnimations() {
      return 0;
   }

   public void initialize(MenuBuilder var1) {
   }
}
