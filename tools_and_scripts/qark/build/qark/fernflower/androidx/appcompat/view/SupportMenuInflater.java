package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import androidx.appcompat.R.styleable;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater extends MenuInflater {
   static final Class[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
   static final Class[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
   static final String LOG_TAG = "SupportMenuInflater";
   static final int NO_ID = 0;
   private static final String XML_GROUP = "group";
   private static final String XML_ITEM = "item";
   private static final String XML_MENU = "menu";
   final Object[] mActionProviderConstructorArguments;
   final Object[] mActionViewConstructorArguments;
   Context mContext;
   private Object mRealOwner;

   static {
      Class[] var0 = new Class[]{Context.class};
      ACTION_VIEW_CONSTRUCTOR_SIGNATURE = var0;
      ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = var0;
   }

   public SupportMenuInflater(Context var1) {
      super(var1);
      this.mContext = var1;
      Object[] var2 = new Object[]{var1};
      this.mActionViewConstructorArguments = var2;
      this.mActionProviderConstructorArguments = var2;
   }

   private Object findRealOwner(Object var1) {
      if (var1 instanceof Activity) {
         return var1;
      } else {
         return var1 instanceof ContextWrapper ? this.findRealOwner(((ContextWrapper)var1).getBaseContext()) : var1;
      }
   }

   private void parseMenu(XmlPullParser var1, AttributeSet var2, Menu var3) throws XmlPullParserException, IOException {
      SupportMenuInflater.MenuState var10 = new SupportMenuInflater.MenuState(var3);
      int var4 = var1.getEventType();
      boolean var6 = false;
      String var9 = null;

      String var13;
      while(true) {
         if (var4 == 2) {
            var13 = var1.getName();
            if (!var13.equals("menu")) {
               StringBuilder var12 = new StringBuilder();
               var12.append("Expecting menu, got ");
               var12.append(var13);
               throw new RuntimeException(var12.toString());
            }

            var4 = var1.next();
            break;
         }

         int var5 = var1.next();
         var4 = var5;
         if (var5 == 1) {
            var4 = var5;
            break;
         }
      }

      boolean var14 = false;

      boolean var7;
      for(int var8 = var4; !var14; var14 = var7) {
         if (var8 == 1) {
            throw new RuntimeException("Unexpected end of document");
         }

         boolean var15;
         if (var8 != 2) {
            if (var8 != 3) {
               var15 = var6;
               var13 = var9;
               var7 = var14;
            } else {
               String var11 = var1.getName();
               if (var6 && var11.equals(var9)) {
                  var15 = false;
                  var13 = null;
                  var7 = var14;
               } else if (var11.equals("group")) {
                  var10.resetGroup();
                  var15 = var6;
                  var13 = var9;
                  var7 = var14;
               } else if (var11.equals("item")) {
                  var15 = var6;
                  var13 = var9;
                  var7 = var14;
                  if (!var10.hasAddedItem()) {
                     if (var10.itemActionProvider != null && var10.itemActionProvider.hasSubMenu()) {
                        var10.addSubMenuItem();
                        var15 = var6;
                        var13 = var9;
                        var7 = var14;
                     } else {
                        var10.addItem();
                        var15 = var6;
                        var13 = var9;
                        var7 = var14;
                     }
                  }
               } else {
                  var15 = var6;
                  var13 = var9;
                  var7 = var14;
                  if (var11.equals("menu")) {
                     var7 = true;
                     var15 = var6;
                     var13 = var9;
                  }
               }
            }
         } else if (var6) {
            var15 = var6;
            var13 = var9;
            var7 = var14;
         } else {
            var13 = var1.getName();
            if (var13.equals("group")) {
               var10.readGroup(var2);
               var15 = var6;
               var13 = var9;
               var7 = var14;
            } else if (var13.equals("item")) {
               var10.readItem(var2);
               var15 = var6;
               var13 = var9;
               var7 = var14;
            } else if (var13.equals("menu")) {
               this.parseMenu(var1, var2, var10.addSubMenuItem());
               var15 = var6;
               var13 = var9;
               var7 = var14;
            } else {
               var15 = true;
               var7 = var14;
            }
         }

         var8 = var1.next();
         var6 = var15;
         var9 = var13;
      }

   }

   Object getRealOwner() {
      if (this.mRealOwner == null) {
         this.mRealOwner = this.findRealOwner(this.mContext);
      }

      return this.mRealOwner;
   }

   public void inflate(int param1, Menu param2) {
      // $FF: Couldn't be decompiled
   }

   private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
      private static final Class[] PARAM_TYPES = new Class[]{MenuItem.class};
      private Method mMethod;
      private Object mRealOwner;

      public InflatedOnMenuItemClickListener(Object var1, String var2) {
         this.mRealOwner = var1;
         Class var3 = var1.getClass();

         try {
            this.mMethod = var3.getMethod(var2, PARAM_TYPES);
         } catch (Exception var5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Couldn't resolve menu item onClick handler ");
            var4.append(var2);
            var4.append(" in class ");
            var4.append(var3.getName());
            InflateException var6 = new InflateException(var4.toString());
            var6.initCause(var5);
            throw var6;
         }
      }

      public boolean onMenuItemClick(MenuItem var1) {
         try {
            if (this.mMethod.getReturnType() == Boolean.TYPE) {
               return (Boolean)this.mMethod.invoke(this.mRealOwner, var1);
            } else {
               this.mMethod.invoke(this.mRealOwner, var1);
               return true;
            }
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   private class MenuState {
      private static final int defaultGroupId = 0;
      private static final int defaultItemCategory = 0;
      private static final int defaultItemCheckable = 0;
      private static final boolean defaultItemChecked = false;
      private static final boolean defaultItemEnabled = true;
      private static final int defaultItemId = 0;
      private static final int defaultItemOrder = 0;
      private static final boolean defaultItemVisible = true;
      private int groupCategory;
      private int groupCheckable;
      private boolean groupEnabled;
      private int groupId;
      private int groupOrder;
      private boolean groupVisible;
      ActionProvider itemActionProvider;
      private String itemActionProviderClassName;
      private String itemActionViewClassName;
      private int itemActionViewLayout;
      private boolean itemAdded;
      private int itemAlphabeticModifiers;
      private char itemAlphabeticShortcut;
      private int itemCategoryOrder;
      private int itemCheckable;
      private boolean itemChecked;
      private CharSequence itemContentDescription;
      private boolean itemEnabled;
      private int itemIconResId;
      private ColorStateList itemIconTintList = null;
      private Mode itemIconTintMode = null;
      private int itemId;
      private String itemListenerMethodName;
      private int itemNumericModifiers;
      private char itemNumericShortcut;
      private int itemShowAsAction;
      private CharSequence itemTitle;
      private CharSequence itemTitleCondensed;
      private CharSequence itemTooltipText;
      private boolean itemVisible;
      private Menu menu;

      public MenuState(Menu var2) {
         this.menu = var2;
         this.resetGroup();
      }

      private char getShortcut(String var1) {
         return var1 == null ? '\u0000' : var1.charAt(0);
      }

      private Object newInstance(String var1, Class[] var2, Object[] var3) {
         try {
            Constructor var5 = Class.forName(var1, false, SupportMenuInflater.this.mContext.getClassLoader()).getConstructor(var2);
            var5.setAccessible(true);
            Object var6 = var5.newInstance(var3);
            return var6;
         } catch (Exception var4) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot instantiate class: ");
            var7.append(var1);
            Log.w("SupportMenuInflater", var7.toString(), var4);
            return null;
         }
      }

      private void setItem(MenuItem var1) {
         MenuItem var5 = var1.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
         boolean var4;
         if (this.itemCheckable >= 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setCheckable(var4).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
         int var2 = this.itemShowAsAction;
         if (var2 >= 0) {
            var1.setShowAsAction(var2);
         }

         if (this.itemListenerMethodName != null) {
            if (SupportMenuInflater.this.mContext.isRestricted()) {
               throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
            }

            var1.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
         }

         if (var1 instanceof MenuItemImpl) {
            MenuItemImpl var7 = (MenuItemImpl)var1;
         }

         if (this.itemCheckable >= 2) {
            if (var1 instanceof MenuItemImpl) {
               ((MenuItemImpl)var1).setExclusiveCheckable(true);
            } else if (var1 instanceof MenuItemWrapperICS) {
               ((MenuItemWrapperICS)var1).setExclusiveCheckable(true);
            }
         }

         boolean var6 = false;
         String var8 = this.itemActionViewClassName;
         if (var8 != null) {
            var1.setActionView((View)this.newInstance(var8, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
            var6 = true;
         }

         int var3 = this.itemActionViewLayout;
         if (var3 > 0) {
            if (!var6) {
               var1.setActionView(var3);
            } else {
               Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
            }
         }

         ActionProvider var9 = this.itemActionProvider;
         if (var9 != null) {
            MenuItemCompat.setActionProvider(var1, var9);
         }

         MenuItemCompat.setContentDescription(var1, this.itemContentDescription);
         MenuItemCompat.setTooltipText(var1, this.itemTooltipText);
         MenuItemCompat.setAlphabeticShortcut(var1, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
         MenuItemCompat.setNumericShortcut(var1, this.itemNumericShortcut, this.itemNumericModifiers);
         Mode var10 = this.itemIconTintMode;
         if (var10 != null) {
            MenuItemCompat.setIconTintMode(var1, var10);
         }

         ColorStateList var11 = this.itemIconTintList;
         if (var11 != null) {
            MenuItemCompat.setIconTintList(var1, var11);
         }

      }

      public void addItem() {
         this.itemAdded = true;
         this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
      }

      public SubMenu addSubMenuItem() {
         this.itemAdded = true;
         SubMenu var1 = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
         this.setItem(var1.getItem());
         return var1;
      }

      public boolean hasAddedItem() {
         return this.itemAdded;
      }

      public void readGroup(AttributeSet var1) {
         TypedArray var2 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, styleable.MenuGroup);
         this.groupId = var2.getResourceId(styleable.MenuGroup_android_id, 0);
         this.groupCategory = var2.getInt(styleable.MenuGroup_android_menuCategory, 0);
         this.groupOrder = var2.getInt(styleable.MenuGroup_android_orderInCategory, 0);
         this.groupCheckable = var2.getInt(styleable.MenuGroup_android_checkableBehavior, 0);
         this.groupVisible = var2.getBoolean(styleable.MenuGroup_android_visible, true);
         this.groupEnabled = var2.getBoolean(styleable.MenuGroup_android_enabled, true);
         var2.recycle();
      }

      public void readItem(AttributeSet var1) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }

      public void resetGroup() {
         this.groupId = 0;
         this.groupCategory = 0;
         this.groupOrder = 0;
         this.groupCheckable = 0;
         this.groupVisible = true;
         this.groupEnabled = true;
      }
   }
}
