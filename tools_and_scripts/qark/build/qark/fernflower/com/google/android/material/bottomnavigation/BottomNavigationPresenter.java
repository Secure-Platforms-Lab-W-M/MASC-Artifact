package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ParcelableSparseArray;

public class BottomNavigationPresenter implements MenuPresenter {
   // $FF: renamed from: id int
   private int field_204;
   private MenuBuilder menu;
   private BottomNavigationMenuView menuView;
   private boolean updateSuspended = false;

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean flagActionItems() {
      return false;
   }

   public int getId() {
      return this.field_204;
   }

   public MenuView getMenuView(ViewGroup var1) {
      return this.menuView;
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
      this.menu = var2;
      this.menuView.initialize(var2);
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof BottomNavigationPresenter.SavedState) {
         this.menuView.tryRestoreSelectedItemId(((BottomNavigationPresenter.SavedState)var1).selectedItemId);
         SparseArray var2 = BadgeUtils.createBadgeDrawablesFromSavedStates(this.menuView.getContext(), ((BottomNavigationPresenter.SavedState)var1).badgeSavedStates);
         this.menuView.setBadgeDrawables(var2);
      }

   }

   public Parcelable onSaveInstanceState() {
      BottomNavigationPresenter.SavedState var1 = new BottomNavigationPresenter.SavedState();
      var1.selectedItemId = this.menuView.getSelectedItemId();
      var1.badgeSavedStates = BadgeUtils.createParcelableBadgeStates(this.menuView.getBadgeDrawables());
      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      return false;
   }

   public void setBottomNavigationMenuView(BottomNavigationMenuView var1) {
      this.menuView = var1;
   }

   public void setCallback(MenuPresenter.Callback var1) {
   }

   public void setId(int var1) {
      this.field_204 = var1;
   }

   public void setUpdateSuspended(boolean var1) {
      this.updateSuspended = var1;
   }

   public void updateMenuView(boolean var1) {
      if (!this.updateSuspended) {
         if (var1) {
            this.menuView.buildMenuView();
         } else {
            this.menuView.updateMenuView();
         }
      }
   }

   static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public BottomNavigationPresenter.SavedState createFromParcel(Parcel var1) {
            return new BottomNavigationPresenter.SavedState(var1);
         }

         public BottomNavigationPresenter.SavedState[] newArray(int var1) {
            return new BottomNavigationPresenter.SavedState[var1];
         }
      };
      ParcelableSparseArray badgeSavedStates;
      int selectedItemId;

      SavedState() {
      }

      SavedState(Parcel var1) {
         this.selectedItemId = var1.readInt();
         this.badgeSavedStates = (ParcelableSparseArray)var1.readParcelable(this.getClass().getClassLoader());
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.selectedItemId);
         var1.writeParcelable(this.badgeSavedStates, 0);
      }
   }
}
