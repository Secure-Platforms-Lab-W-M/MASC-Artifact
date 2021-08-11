package com.google.android.material.dialog;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MaterialAlertDialogBuilder extends AlertDialog.Builder {
   private static final int DEF_STYLE_ATTR;
   private static final int DEF_STYLE_RES;
   private static final int MATERIAL_ALERT_DIALOG_THEME_OVERLAY;
   private Drawable background;
   private final Rect backgroundInsets;

   static {
      DEF_STYLE_ATTR = attr.alertDialogStyle;
      DEF_STYLE_RES = style.MaterialAlertDialog_MaterialComponents;
      MATERIAL_ALERT_DIALOG_THEME_OVERLAY = attr.materialAlertDialogTheme;
   }

   public MaterialAlertDialogBuilder(Context var1) {
      this(var1, 0);
   }

   public MaterialAlertDialogBuilder(Context var1, int var2) {
      super(createMaterialAlertDialogThemedContext(var1), getOverridingThemeResId(var1, var2));
      Context var5 = this.getContext();
      Theme var6 = var5.getTheme();
      this.backgroundInsets = MaterialDialogs.getDialogBackgroundInsets(var5, DEF_STYLE_ATTR, DEF_STYLE_RES);
      var2 = MaterialColors.getColor(var5, attr.colorSurface, this.getClass().getCanonicalName());
      MaterialShapeDrawable var4 = new MaterialShapeDrawable(var5, (AttributeSet)null, DEF_STYLE_ATTR, DEF_STYLE_RES);
      var4.initializeElevationOverlay(var5);
      var4.setFillColor(ColorStateList.valueOf(var2));
      if (VERSION.SDK_INT >= 28) {
         TypedValue var7 = new TypedValue();
         var6.resolveAttribute(16844145, var7, true);
         float var3 = var7.getDimension(this.getContext().getResources().getDisplayMetrics());
         if (var7.type == 5 && var3 >= 0.0F) {
            var4.setCornerSize(var3);
         }
      }

      this.background = var4;
   }

   private static Context createMaterialAlertDialogThemedContext(Context var0) {
      int var1 = getMaterialAlertDialogThemeOverlay(var0);
      var0 = ThemeEnforcement.createThemedContext(var0, (AttributeSet)null, DEF_STYLE_ATTR, DEF_STYLE_RES);
      return (Context)(var1 == 0 ? var0 : new ContextThemeWrapper(var0, var1));
   }

   private static int getMaterialAlertDialogThemeOverlay(Context var0) {
      TypedValue var1 = MaterialAttributes.resolve(var0, MATERIAL_ALERT_DIALOG_THEME_OVERLAY);
      return var1 == null ? 0 : var1.data;
   }

   private static int getOverridingThemeResId(Context var0, int var1) {
      return var1 == 0 ? getMaterialAlertDialogThemeOverlay(var0) : var1;
   }

   public AlertDialog create() {
      AlertDialog var1 = super.create();
      Window var2 = var1.getWindow();
      View var3 = var2.getDecorView();
      Drawable var4 = this.background;
      if (var4 instanceof MaterialShapeDrawable) {
         ((MaterialShapeDrawable)var4).setElevation(ViewCompat.getElevation(var3));
      }

      var2.setBackgroundDrawable(MaterialDialogs.insetDrawable(this.background, this.backgroundInsets));
      var3.setOnTouchListener(new InsetDialogOnTouchListener(var1, this.backgroundInsets));
      return var1;
   }

   public Drawable getBackground() {
      return this.background;
   }

   public MaterialAlertDialogBuilder setAdapter(ListAdapter var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setAdapter(var1, var2);
   }

   public MaterialAlertDialogBuilder setBackground(Drawable var1) {
      this.background = var1;
      return this;
   }

   public MaterialAlertDialogBuilder setBackgroundInsetBottom(int var1) {
      this.backgroundInsets.bottom = var1;
      return this;
   }

   public MaterialAlertDialogBuilder setBackgroundInsetEnd(int var1) {
      if (VERSION.SDK_INT >= 17 && this.getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
         this.backgroundInsets.left = var1;
         return this;
      } else {
         this.backgroundInsets.right = var1;
         return this;
      }
   }

   public MaterialAlertDialogBuilder setBackgroundInsetStart(int var1) {
      if (VERSION.SDK_INT >= 17 && this.getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
         this.backgroundInsets.right = var1;
         return this;
      } else {
         this.backgroundInsets.left = var1;
         return this;
      }
   }

   public MaterialAlertDialogBuilder setBackgroundInsetTop(int var1) {
      this.backgroundInsets.top = var1;
      return this;
   }

   public MaterialAlertDialogBuilder setCancelable(boolean var1) {
      return (MaterialAlertDialogBuilder)super.setCancelable(var1);
   }

   public MaterialAlertDialogBuilder setCursor(Cursor var1, OnClickListener var2, String var3) {
      return (MaterialAlertDialogBuilder)super.setCursor(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setCustomTitle(View var1) {
      return (MaterialAlertDialogBuilder)super.setCustomTitle(var1);
   }

   public MaterialAlertDialogBuilder setIcon(int var1) {
      return (MaterialAlertDialogBuilder)super.setIcon(var1);
   }

   public MaterialAlertDialogBuilder setIcon(Drawable var1) {
      return (MaterialAlertDialogBuilder)super.setIcon(var1);
   }

   public MaterialAlertDialogBuilder setIconAttribute(int var1) {
      return (MaterialAlertDialogBuilder)super.setIconAttribute(var1);
   }

   public MaterialAlertDialogBuilder setItems(int var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setItems(var1, var2);
   }

   public MaterialAlertDialogBuilder setItems(CharSequence[] var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setItems(var1, var2);
   }

   public MaterialAlertDialogBuilder setMessage(int var1) {
      return (MaterialAlertDialogBuilder)super.setMessage(var1);
   }

   public MaterialAlertDialogBuilder setMessage(CharSequence var1) {
      return (MaterialAlertDialogBuilder)super.setMessage(var1);
   }

   public MaterialAlertDialogBuilder setMultiChoiceItems(int var1, boolean[] var2, OnMultiChoiceClickListener var3) {
      return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setMultiChoiceItems(Cursor var1, String var2, String var3, OnMultiChoiceClickListener var4) {
      return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(var1, var2, var3, var4);
   }

   public MaterialAlertDialogBuilder setMultiChoiceItems(CharSequence[] var1, boolean[] var2, OnMultiChoiceClickListener var3) {
      return (MaterialAlertDialogBuilder)super.setMultiChoiceItems(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setNegativeButton(int var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setNegativeButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setNegativeButton(CharSequence var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setNegativeButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setNegativeButtonIcon(Drawable var1) {
      return (MaterialAlertDialogBuilder)super.setNegativeButtonIcon(var1);
   }

   public MaterialAlertDialogBuilder setNeutralButton(int var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setNeutralButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setNeutralButton(CharSequence var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setNeutralButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setNeutralButtonIcon(Drawable var1) {
      return (MaterialAlertDialogBuilder)super.setNeutralButtonIcon(var1);
   }

   public MaterialAlertDialogBuilder setOnCancelListener(OnCancelListener var1) {
      return (MaterialAlertDialogBuilder)super.setOnCancelListener(var1);
   }

   public MaterialAlertDialogBuilder setOnDismissListener(OnDismissListener var1) {
      return (MaterialAlertDialogBuilder)super.setOnDismissListener(var1);
   }

   public MaterialAlertDialogBuilder setOnItemSelectedListener(OnItemSelectedListener var1) {
      return (MaterialAlertDialogBuilder)super.setOnItemSelectedListener(var1);
   }

   public MaterialAlertDialogBuilder setOnKeyListener(OnKeyListener var1) {
      return (MaterialAlertDialogBuilder)super.setOnKeyListener(var1);
   }

   public MaterialAlertDialogBuilder setPositiveButton(int var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setPositiveButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setPositiveButton(CharSequence var1, OnClickListener var2) {
      return (MaterialAlertDialogBuilder)super.setPositiveButton(var1, var2);
   }

   public MaterialAlertDialogBuilder setPositiveButtonIcon(Drawable var1) {
      return (MaterialAlertDialogBuilder)super.setPositiveButtonIcon(var1);
   }

   public MaterialAlertDialogBuilder setSingleChoiceItems(int var1, int var2, OnClickListener var3) {
      return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setSingleChoiceItems(Cursor var1, int var2, String var3, OnClickListener var4) {
      return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(var1, var2, var3, var4);
   }

   public MaterialAlertDialogBuilder setSingleChoiceItems(ListAdapter var1, int var2, OnClickListener var3) {
      return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setSingleChoiceItems(CharSequence[] var1, int var2, OnClickListener var3) {
      return (MaterialAlertDialogBuilder)super.setSingleChoiceItems(var1, var2, var3);
   }

   public MaterialAlertDialogBuilder setTitle(int var1) {
      return (MaterialAlertDialogBuilder)super.setTitle(var1);
   }

   public MaterialAlertDialogBuilder setTitle(CharSequence var1) {
      return (MaterialAlertDialogBuilder)super.setTitle(var1);
   }

   public MaterialAlertDialogBuilder setView(int var1) {
      return (MaterialAlertDialogBuilder)super.setView(var1);
   }

   public MaterialAlertDialogBuilder setView(View var1) {
      return (MaterialAlertDialogBuilder)super.setView(var1);
   }
}
