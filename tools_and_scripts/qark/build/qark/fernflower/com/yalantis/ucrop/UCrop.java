package com.yalantis.ucrop;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import com.yalantis.ucrop.model.AspectRatio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class UCrop {
   public static final String EXTRA_ASPECT_RATIO_X = "com.yalantis.ucrop.AspectRatioX";
   public static final String EXTRA_ASPECT_RATIO_Y = "com.yalantis.ucrop.AspectRatioY";
   public static final String EXTRA_ERROR = "com.yalantis.ucrop.Error";
   public static final String EXTRA_INPUT_URI = "com.yalantis.ucrop.InputUri";
   public static final String EXTRA_MAX_SIZE_X = "com.yalantis.ucrop.MaxSizeX";
   public static final String EXTRA_MAX_SIZE_Y = "com.yalantis.ucrop.MaxSizeY";
   public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "com.yalantis.ucrop.CropAspectRatio";
   public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "com.yalantis.ucrop.ImageHeight";
   public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "com.yalantis.ucrop.ImageWidth";
   public static final String EXTRA_OUTPUT_OFFSET_X = "com.yalantis.ucrop.OffsetX";
   public static final String EXTRA_OUTPUT_OFFSET_Y = "com.yalantis.ucrop.OffsetY";
   public static final String EXTRA_OUTPUT_URI = "com.yalantis.ucrop.OutputUri";
   private static final String EXTRA_PREFIX = "com.yalantis.ucrop";
   public static final int MIN_SIZE = 10;
   public static final int REQUEST_CROP = 69;
   public static final int RESULT_ERROR = 96;
   private Intent mCropIntent = new Intent();
   private Bundle mCropOptionsBundle;

   private UCrop(Uri var1, Uri var2) {
      Bundle var3 = new Bundle();
      this.mCropOptionsBundle = var3;
      var3.putParcelable("com.yalantis.ucrop.InputUri", var1);
      this.mCropOptionsBundle.putParcelable("com.yalantis.ucrop.OutputUri", var2);
   }

   public static Throwable getError(Intent var0) {
      return (Throwable)var0.getSerializableExtra("com.yalantis.ucrop.Error");
   }

   public static Uri getOutput(Intent var0) {
      return (Uri)var0.getParcelableExtra("com.yalantis.ucrop.OutputUri");
   }

   public static float getOutputCropAspectRatio(Intent var0) {
      return (Float)var0.getParcelableExtra("com.yalantis.ucrop.CropAspectRatio");
   }

   public static int getOutputImageHeight(Intent var0) {
      return var0.getIntExtra("com.yalantis.ucrop.ImageHeight", -1);
   }

   public static int getOutputImageWidth(Intent var0) {
      return var0.getIntExtra("com.yalantis.ucrop.ImageWidth", -1);
   }

   // $FF: renamed from: of (android.net.Uri, android.net.Uri) com.yalantis.ucrop.UCrop
   public static UCrop method_26(Uri var0, Uri var1) {
      return new UCrop(var0, var1);
   }

   public UCropFragment getFragment() {
      return UCropFragment.newInstance(this.mCropOptionsBundle);
   }

   public UCropFragment getFragment(Bundle var1) {
      this.mCropOptionsBundle = var1;
      return this.getFragment();
   }

   public Intent getIntent(Context var1) {
      this.mCropIntent.setClass(var1, UCropActivity.class);
      this.mCropIntent.putExtras(this.mCropOptionsBundle);
      return this.mCropIntent;
   }

   public void start(Activity var1) {
      this.start(var1, 69);
   }

   public void start(Activity var1, int var2) {
      var1.startActivityForResult(this.getIntent(var1), var2);
   }

   public void start(Context var1, Fragment var2) {
      this.start(var1, (Fragment)var2, 69);
   }

   public void start(Context var1, Fragment var2, int var3) {
      var2.startActivityForResult(this.getIntent(var1), var3);
   }

   public void start(Context var1, androidx.fragment.app.Fragment var2) {
      this.start(var1, (androidx.fragment.app.Fragment)var2, 69);
   }

   public void start(Context var1, androidx.fragment.app.Fragment var2, int var3) {
      var2.startActivityForResult(this.getIntent(var1), var3);
   }

   public UCrop useSourceImageAspectRatio() {
      this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioX", 0.0F);
      this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioY", 0.0F);
      return this;
   }

   public UCrop withAspectRatio(float var1, float var2) {
      this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioX", var1);
      this.mCropOptionsBundle.putFloat("com.yalantis.ucrop.AspectRatioY", var2);
      return this;
   }

   public UCrop withMaxResultSize(int var1, int var2) {
      int var3 = var1;
      if (var1 < 10) {
         var3 = 10;
      }

      var1 = var2;
      if (var2 < 10) {
         var1 = 10;
      }

      this.mCropOptionsBundle.putInt("com.yalantis.ucrop.MaxSizeX", var3);
      this.mCropOptionsBundle.putInt("com.yalantis.ucrop.MaxSizeY", var1);
      return this;
   }

   public UCrop withOptions(UCrop.Options var1) {
      this.mCropOptionsBundle.putAll(var1.getOptionBundle());
      return this;
   }

   public static class Options {
      public static final String EXTRA_ALLOWED_GESTURES = "com.yalantis.ucrop.AllowedGestures";
      public static final String EXTRA_ASPECT_RATIO_OPTIONS = "com.yalantis.ucrop.AspectRatioOptions";
      public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = "com.yalantis.ucrop.AspectRatioSelectedByDefault";
      public static final String EXTRA_CIRCLE_DIMMED_LAYER = "com.yalantis.ucrop.CircleDimmedLayer";
      public static final String EXTRA_COMPRESSION_FORMAT_NAME = "com.yalantis.ucrop.CompressionFormatName";
      public static final String EXTRA_COMPRESSION_QUALITY = "com.yalantis.ucrop.CompressionQuality";
      public static final String EXTRA_CROP_FRAME_COLOR = "com.yalantis.ucrop.CropFrameColor";
      public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = "com.yalantis.ucrop.CropFrameStrokeWidth";
      public static final String EXTRA_CROP_GRID_COLOR = "com.yalantis.ucrop.CropGridColor";
      public static final String EXTRA_CROP_GRID_COLUMN_COUNT = "com.yalantis.ucrop.CropGridColumnCount";
      public static final String EXTRA_CROP_GRID_ROW_COUNT = "com.yalantis.ucrop.CropGridRowCount";
      public static final String EXTRA_CROP_GRID_STROKE_WIDTH = "com.yalantis.ucrop.CropGridStrokeWidth";
      public static final String EXTRA_DIMMED_LAYER_COLOR = "com.yalantis.ucrop.DimmedLayerColor";
      public static final String EXTRA_FREE_STYLE_CROP = "com.yalantis.ucrop.FreeStyleCrop";
      public static final String EXTRA_HIDE_BOTTOM_CONTROLS = "com.yalantis.ucrop.HideBottomControls";
      public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = "com.yalantis.ucrop.ImageToCropBoundsAnimDuration";
      public static final String EXTRA_MAX_BITMAP_SIZE = "com.yalantis.ucrop.MaxBitmapSize";
      public static final String EXTRA_MAX_SCALE_MULTIPLIER = "com.yalantis.ucrop.MaxScaleMultiplier";
      public static final String EXTRA_SHOW_CROP_FRAME = "com.yalantis.ucrop.ShowCropFrame";
      public static final String EXTRA_SHOW_CROP_GRID = "com.yalantis.ucrop.ShowCropGrid";
      public static final String EXTRA_STATUS_BAR_COLOR = "com.yalantis.ucrop.StatusBarColor";
      public static final String EXTRA_TOOL_BAR_COLOR = "com.yalantis.ucrop.ToolbarColor";
      public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = "com.yalantis.ucrop.UcropColorWidgetActive";
      public static final String EXTRA_UCROP_LOGO_COLOR = "com.yalantis.ucrop.UcropLogoColor";
      public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = "com.yalantis.ucrop.UcropRootViewBackgroundColor";
      public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = "com.yalantis.ucrop.UcropToolbarTitleText";
      public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = "com.yalantis.ucrop.UcropToolbarCancelDrawable";
      public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = "com.yalantis.ucrop.UcropToolbarWidgetColor";
      public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = "com.yalantis.ucrop.UcropToolbarCropDrawable";
      private final Bundle mOptionBundle = new Bundle();

      public Bundle getOptionBundle() {
         return this.mOptionBundle;
      }

      public void setActiveWidgetColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropColorWidgetActive", var1);
      }

      public void setAllowedGestures(int var1, int var2, int var3) {
         this.mOptionBundle.putIntArray("com.yalantis.ucrop.AllowedGestures", new int[]{var1, var2, var3});
      }

      public void setAspectRatioOptions(int var1, AspectRatio... var2) {
         if (var1 <= var2.length) {
            this.mOptionBundle.putInt("com.yalantis.ucrop.AspectRatioSelectedByDefault", var1);
            this.mOptionBundle.putParcelableArrayList("com.yalantis.ucrop.AspectRatioOptions", new ArrayList(Arrays.asList(var2)));
         } else {
            throw new IllegalArgumentException(String.format(Locale.US, "Index [selectedByDefault = %d] cannot be higher than aspect ratio options count [count = %d].", var1, var2.length));
         }
      }

      public void setCircleDimmedLayer(boolean var1) {
         this.mOptionBundle.putBoolean("com.yalantis.ucrop.CircleDimmedLayer", var1);
      }

      public void setCompressionFormat(CompressFormat var1) {
         this.mOptionBundle.putString("com.yalantis.ucrop.CompressionFormatName", var1.name());
      }

      public void setCompressionQuality(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CompressionQuality", var1);
      }

      public void setCropFrameColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropFrameColor", var1);
      }

      public void setCropFrameStrokeWidth(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropFrameStrokeWidth", var1);
      }

      public void setCropGridColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridColor", var1);
      }

      public void setCropGridColumnCount(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridColumnCount", var1);
      }

      public void setCropGridRowCount(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridRowCount", var1);
      }

      public void setCropGridStrokeWidth(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.CropGridStrokeWidth", var1);
      }

      public void setDimmedLayerColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.DimmedLayerColor", var1);
      }

      public void setFreeStyleCropEnabled(boolean var1) {
         this.mOptionBundle.putBoolean("com.yalantis.ucrop.FreeStyleCrop", var1);
      }

      public void setHideBottomControls(boolean var1) {
         this.mOptionBundle.putBoolean("com.yalantis.ucrop.HideBottomControls", var1);
      }

      public void setImageToCropBoundsAnimDuration(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.ImageToCropBoundsAnimDuration", var1);
      }

      public void setLogoColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropLogoColor", var1);
      }

      public void setMaxBitmapSize(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.MaxBitmapSize", var1);
      }

      public void setMaxScaleMultiplier(float var1) {
         this.mOptionBundle.putFloat("com.yalantis.ucrop.MaxScaleMultiplier", var1);
      }

      public void setRootViewBackgroundColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropRootViewBackgroundColor", var1);
      }

      public void setShowCropFrame(boolean var1) {
         this.mOptionBundle.putBoolean("com.yalantis.ucrop.ShowCropFrame", var1);
      }

      public void setShowCropGrid(boolean var1) {
         this.mOptionBundle.putBoolean("com.yalantis.ucrop.ShowCropGrid", var1);
      }

      public void setStatusBarColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.StatusBarColor", var1);
      }

      public void setToolbarCancelDrawable(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarCancelDrawable", var1);
      }

      public void setToolbarColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.ToolbarColor", var1);
      }

      public void setToolbarCropDrawable(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarCropDrawable", var1);
      }

      public void setToolbarTitle(String var1) {
         this.mOptionBundle.putString("com.yalantis.ucrop.UcropToolbarTitleText", var1);
      }

      public void setToolbarWidgetColor(int var1) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.UcropToolbarWidgetColor", var1);
      }

      public void useSourceImageAspectRatio() {
         this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioX", 0.0F);
         this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioY", 0.0F);
      }

      public void withAspectRatio(float var1, float var2) {
         this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioX", var1);
         this.mOptionBundle.putFloat("com.yalantis.ucrop.AspectRatioY", var2);
      }

      public void withMaxResultSize(int var1, int var2) {
         this.mOptionBundle.putInt("com.yalantis.ucrop.MaxSizeX", var1);
         this.mOptionBundle.putInt("com.yalantis.ucrop.MaxSizeY", var2);
      }
   }
}
