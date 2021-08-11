package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.internal.view.SupportMenuItem;

public class ActionMenuItem implements SupportMenuItem {
   private static final int CHECKABLE = 1;
   private static final int CHECKED = 2;
   private static final int ENABLED = 16;
   private static final int EXCLUSIVE = 4;
   private static final int HIDDEN = 8;
   private static final int NO_ICON = 0;
   private final int mCategoryOrder;
   private OnMenuItemClickListener mClickListener;
   private CharSequence mContentDescription;
   private Context mContext;
   private int mFlags = 16;
   private final int mGroup;
   private boolean mHasIconTint = false;
   private boolean mHasIconTintMode = false;
   private Drawable mIconDrawable;
   private int mIconResId = 0;
   private ColorStateList mIconTintList = null;
   private Mode mIconTintMode = null;
   private final int mId;
   private Intent mIntent;
   private final int mOrdering;
   private char mShortcutAlphabeticChar;
   private int mShortcutAlphabeticModifiers = 4096;
   private char mShortcutNumericChar;
   private int mShortcutNumericModifiers = 4096;
   private CharSequence mTitle;
   private CharSequence mTitleCondensed;
   private CharSequence mTooltipText;

   public ActionMenuItem(Context var1, int var2, int var3, int var4, int var5, CharSequence var6) {
      this.mContext = var1;
      this.mId = var3;
      this.mGroup = var2;
      this.mCategoryOrder = var4;
      this.mOrdering = var5;
      this.mTitle = var6;
   }

   private void applyIconTint() {
      if (this.mIconDrawable != null && (this.mHasIconTint || this.mHasIconTintMode)) {
         Drawable var1 = DrawableCompat.wrap(this.mIconDrawable);
         this.mIconDrawable = var1;
         var1 = var1.mutate();
         this.mIconDrawable = var1;
         if (this.mHasIconTint) {
            DrawableCompat.setTintList(var1, this.mIconTintList);
         }

         if (this.mHasIconTintMode) {
            DrawableCompat.setTintMode(this.mIconDrawable, this.mIconTintMode);
         }
      }

   }

   public boolean collapseActionView() {
      return false;
   }

   public boolean expandActionView() {
      return false;
   }

   public ActionProvider getActionProvider() {
      throw new UnsupportedOperationException();
   }

   public View getActionView() {
      return null;
   }

   public int getAlphabeticModifiers() {
      return this.mShortcutAlphabeticModifiers;
   }

   public char getAlphabeticShortcut() {
      return this.mShortcutAlphabeticChar;
   }

   public CharSequence getContentDescription() {
      return this.mContentDescription;
   }

   public int getGroupId() {
      return this.mGroup;
   }

   public Drawable getIcon() {
      return this.mIconDrawable;
   }

   public ColorStateList getIconTintList() {
      return this.mIconTintList;
   }

   public Mode getIconTintMode() {
      return this.mIconTintMode;
   }

   public Intent getIntent() {
      return this.mIntent;
   }

   public int getItemId() {
      return this.mId;
   }

   public ContextMenuInfo getMenuInfo() {
      return null;
   }

   public int getNumericModifiers() {
      return this.mShortcutNumericModifiers;
   }

   public char getNumericShortcut() {
      return this.mShortcutNumericChar;
   }

   public int getOrder() {
      return this.mOrdering;
   }

   public SubMenu getSubMenu() {
      return null;
   }

   public androidx.core.view.ActionProvider getSupportActionProvider() {
      return null;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public CharSequence getTitleCondensed() {
      CharSequence var1 = this.mTitleCondensed;
      return var1 != null ? var1 : this.mTitle;
   }

   public CharSequence getTooltipText() {
      return this.mTooltipText;
   }

   public boolean hasSubMenu() {
      return false;
   }

   public boolean invoke() {
      OnMenuItemClickListener var1 = this.mClickListener;
      if (var1 != null && var1.onMenuItemClick(this)) {
         return true;
      } else {
         Intent var2 = this.mIntent;
         if (var2 != null) {
            this.mContext.startActivity(var2);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean isActionViewExpanded() {
      return false;
   }

   public boolean isCheckable() {
      return (this.mFlags & 1) != 0;
   }

   public boolean isChecked() {
      return (this.mFlags & 2) != 0;
   }

   public boolean isEnabled() {
      return (this.mFlags & 16) != 0;
   }

   public boolean isVisible() {
      return (this.mFlags & 8) == 0;
   }

   public boolean requiresActionButton() {
      return true;
   }

   public boolean requiresOverflow() {
      return false;
   }

   public MenuItem setActionProvider(ActionProvider var1) {
      throw new UnsupportedOperationException();
   }

   public SupportMenuItem setActionView(int var1) {
      throw new UnsupportedOperationException();
   }

   public SupportMenuItem setActionView(View var1) {
      throw new UnsupportedOperationException();
   }

   public MenuItem setAlphabeticShortcut(char var1) {
      this.mShortcutAlphabeticChar = Character.toLowerCase(var1);
      return this;
   }

   public MenuItem setAlphabeticShortcut(char var1, int var2) {
      this.mShortcutAlphabeticChar = Character.toLowerCase(var1);
      this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(var2);
      return this;
   }

   public MenuItem setCheckable(boolean var1) {
      this.mFlags = this.mFlags & -2 | var1;
      return this;
   }

   public MenuItem setChecked(boolean var1) {
      int var3 = this.mFlags;
      byte var2;
      if (var1) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      this.mFlags = var3 & -3 | var2;
      return this;
   }

   public SupportMenuItem setContentDescription(CharSequence var1) {
      this.mContentDescription = var1;
      return this;
   }

   public MenuItem setEnabled(boolean var1) {
      int var3 = this.mFlags;
      byte var2;
      if (var1) {
         var2 = 16;
      } else {
         var2 = 0;
      }

      this.mFlags = var3 & -17 | var2;
      return this;
   }

   public ActionMenuItem setExclusiveCheckable(boolean var1) {
      int var3 = this.mFlags;
      byte var2;
      if (var1) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      this.mFlags = var3 & -5 | var2;
      return this;
   }

   public MenuItem setIcon(int var1) {
      this.mIconResId = var1;
      this.mIconDrawable = ContextCompat.getDrawable(this.mContext, var1);
      this.applyIconTint();
      return this;
   }

   public MenuItem setIcon(Drawable var1) {
      this.mIconDrawable = var1;
      this.mIconResId = 0;
      this.applyIconTint();
      return this;
   }

   public MenuItem setIconTintList(ColorStateList var1) {
      this.mIconTintList = var1;
      this.mHasIconTint = true;
      this.applyIconTint();
      return this;
   }

   public MenuItem setIconTintMode(Mode var1) {
      this.mIconTintMode = var1;
      this.mHasIconTintMode = true;
      this.applyIconTint();
      return this;
   }

   public MenuItem setIntent(Intent var1) {
      this.mIntent = var1;
      return this;
   }

   public MenuItem setNumericShortcut(char var1) {
      this.mShortcutNumericChar = var1;
      return this;
   }

   public MenuItem setNumericShortcut(char var1, int var2) {
      this.mShortcutNumericChar = var1;
      this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(var2);
      return this;
   }

   public MenuItem setOnActionExpandListener(OnActionExpandListener var1) {
      throw new UnsupportedOperationException();
   }

   public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener var1) {
      this.mClickListener = var1;
      return this;
   }

   public MenuItem setShortcut(char var1, char var2) {
      this.mShortcutNumericChar = var1;
      this.mShortcutAlphabeticChar = Character.toLowerCase(var2);
      return this;
   }

   public MenuItem setShortcut(char var1, char var2, int var3, int var4) {
      this.mShortcutNumericChar = var1;
      this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(var3);
      this.mShortcutAlphabeticChar = Character.toLowerCase(var2);
      this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(var4);
      return this;
   }

   public void setShowAsAction(int var1) {
   }

   public SupportMenuItem setShowAsActionFlags(int var1) {
      this.setShowAsAction(var1);
      return this;
   }

   public SupportMenuItem setSupportActionProvider(androidx.core.view.ActionProvider var1) {
      throw new UnsupportedOperationException();
   }

   public MenuItem setTitle(int var1) {
      this.mTitle = this.mContext.getResources().getString(var1);
      return this;
   }

   public MenuItem setTitle(CharSequence var1) {
      this.mTitle = var1;
      return this;
   }

   public MenuItem setTitleCondensed(CharSequence var1) {
      this.mTitleCondensed = var1;
      return this;
   }

   public SupportMenuItem setTooltipText(CharSequence var1) {
      this.mTooltipText = var1;
      return this;
   }

   public MenuItem setVisible(boolean var1) {
      int var3 = this.mFlags;
      byte var2 = 8;
      if (var1) {
         var2 = 0;
      }

      this.mFlags = var3 & 8 | var2;
      return this;
   }
}
