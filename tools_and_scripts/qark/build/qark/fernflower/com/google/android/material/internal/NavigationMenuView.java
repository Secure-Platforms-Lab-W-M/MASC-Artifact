package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
