package com.yalantis.ucrop;

import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.yalantis.ucrop.R.color;
import com.yalantis.ucrop.R.dimen;
import com.yalantis.ucrop.R.drawable;
import com.yalantis.ucrop.R.id;
import com.yalantis.ucrop.R.layout;
import com.yalantis.ucrop.R.menu;
import com.yalantis.ucrop.R.string;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropActivity extends AppCompatActivity {
   public static final int ALL = 3;
   public static final CompressFormat DEFAULT_COMPRESS_FORMAT;
   public static final int DEFAULT_COMPRESS_QUALITY = 90;
   public static final int NONE = 0;
   public static final int ROTATE = 2;
   private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
   public static final int SCALE = 1;
   private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
   private static final int TABS_COUNT = 3;
   private static final String TAG = "UCropActivity";
   private int mActiveWidgetColor;
   private int[] mAllowedGestures;
   private View mBlockingView;
   private CompressFormat mCompressFormat;
   private int mCompressQuality;
   private List mCropAspectRatioViews = new ArrayList();
   private GestureCropImageView mGestureCropImageView;
   private TransformImageView.TransformImageListener mImageListener;
   private ViewGroup mLayoutAspectRatio;
   private ViewGroup mLayoutRotate;
   private ViewGroup mLayoutScale;
   private int mLogoColor;
   private OverlayView mOverlayView;
   private int mRootViewBackgroundColor;
   private boolean mShowBottomControls;
   private boolean mShowLoader = true;
   private final OnClickListener mStateClickListener;
   private int mStatusBarColor;
   private TextView mTextViewRotateAngle;
   private TextView mTextViewScalePercent;
   private int mToolbarCancelDrawable;
   private int mToolbarColor;
   private int mToolbarCropDrawable;
   private String mToolbarTitle;
   private int mToolbarWidgetColor;
   private UCropView mUCropView;
   private ViewGroup mWrapperStateAspectRatio;
   private ViewGroup mWrapperStateRotate;
   private ViewGroup mWrapperStateScale;

   static {
      DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
   }

   public UCropActivity() {
      this.mCompressFormat = DEFAULT_COMPRESS_FORMAT;
      this.mCompressQuality = 90;
      this.mAllowedGestures = new int[]{1, 2, 3};
      this.mImageListener = new TransformImageView.TransformImageListener() {
         public void onLoadComplete() {
            UCropActivity.this.mUCropView.animate().alpha(1.0F).setDuration(300L).setInterpolator(new AccelerateInterpolator());
            UCropActivity.this.mBlockingView.setClickable(false);
            UCropActivity.this.mShowLoader = false;
            UCropActivity.this.supportInvalidateOptionsMenu();
         }

         public void onLoadFailure(Exception var1) {
            UCropActivity.this.setResultError(var1);
            UCropActivity.this.finish();
         }

         public void onRotate(float var1) {
            UCropActivity.this.setAngleText(var1);
         }

         public void onScale(float var1) {
            UCropActivity.this.setScaleText(var1);
         }
      };
      this.mStateClickListener = new OnClickListener() {
         public void onClick(View var1) {
            if (!var1.isSelected()) {
               UCropActivity.this.setWidgetState(var1.getId());
            }

         }
      };
   }

   private void addBlockingView() {
      if (this.mBlockingView == null) {
         this.mBlockingView = new View(this);
         LayoutParams var1 = new LayoutParams(-1, -1);
         var1.addRule(3, id.toolbar);
         this.mBlockingView.setLayoutParams(var1);
         this.mBlockingView.setClickable(true);
      }

      ((RelativeLayout)this.findViewById(id.ucrop_photobox)).addView(this.mBlockingView);
   }

   private void initiateRootViews() {
      UCropView var1 = (UCropView)this.findViewById(id.ucrop);
      this.mUCropView = var1;
      this.mGestureCropImageView = var1.getCropImageView();
      this.mOverlayView = this.mUCropView.getOverlayView();
      this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
      ((ImageView)this.findViewById(id.image_view_logo)).setColorFilter(this.mLogoColor, Mode.SRC_ATOP);
      this.findViewById(id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
   }

   private void processOptions(Intent var1) {
      String var7 = var1.getStringExtra("com.yalantis.ucrop.CompressionFormatName");
      CompressFormat var6 = null;
      if (!TextUtils.isEmpty(var7)) {
         var6 = CompressFormat.valueOf(var7);
      }

      if (var6 == null) {
         var6 = DEFAULT_COMPRESS_FORMAT;
      }

      this.mCompressFormat = var6;
      this.mCompressQuality = var1.getIntExtra("com.yalantis.ucrop.CompressionQuality", 90);
      int[] var8 = var1.getIntArrayExtra("com.yalantis.ucrop.AllowedGestures");
      if (var8 != null && var8.length == 3) {
         this.mAllowedGestures = var8;
      }

      this.mGestureCropImageView.setMaxBitmapSize(var1.getIntExtra("com.yalantis.ucrop.MaxBitmapSize", 0));
      this.mGestureCropImageView.setMaxScaleMultiplier(var1.getFloatExtra("com.yalantis.ucrop.MaxScaleMultiplier", 10.0F));
      this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long)var1.getIntExtra("com.yalantis.ucrop.ImageToCropBoundsAnimDuration", 500));
      this.mOverlayView.setFreestyleCropEnabled(var1.getBooleanExtra("com.yalantis.ucrop.FreeStyleCrop", false));
      this.mOverlayView.setDimmedColor(var1.getIntExtra("com.yalantis.ucrop.DimmedLayerColor", this.getResources().getColor(color.ucrop_color_default_dimmed)));
      this.mOverlayView.setCircleDimmedLayer(var1.getBooleanExtra("com.yalantis.ucrop.CircleDimmedLayer", false));
      this.mOverlayView.setShowCropFrame(var1.getBooleanExtra("com.yalantis.ucrop.ShowCropFrame", true));
      this.mOverlayView.setCropFrameColor(var1.getIntExtra("com.yalantis.ucrop.CropFrameColor", this.getResources().getColor(color.ucrop_color_default_crop_frame)));
      this.mOverlayView.setCropFrameStrokeWidth(var1.getIntExtra("com.yalantis.ucrop.CropFrameStrokeWidth", this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width)));
      this.mOverlayView.setShowCropGrid(var1.getBooleanExtra("com.yalantis.ucrop.ShowCropGrid", true));
      this.mOverlayView.setCropGridRowCount(var1.getIntExtra("com.yalantis.ucrop.CropGridRowCount", 2));
      this.mOverlayView.setCropGridColumnCount(var1.getIntExtra("com.yalantis.ucrop.CropGridColumnCount", 2));
      this.mOverlayView.setCropGridColor(var1.getIntExtra("com.yalantis.ucrop.CropGridColor", this.getResources().getColor(color.ucrop_color_default_crop_grid)));
      this.mOverlayView.setCropGridStrokeWidth(var1.getIntExtra("com.yalantis.ucrop.CropGridStrokeWidth", this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width)));
      float var2 = var1.getFloatExtra("com.yalantis.ucrop.AspectRatioX", 0.0F);
      float var3 = var1.getFloatExtra("com.yalantis.ucrop.AspectRatioY", 0.0F);
      int var4 = var1.getIntExtra("com.yalantis.ucrop.AspectRatioSelectedByDefault", 0);
      ArrayList var9 = var1.getParcelableArrayListExtra("com.yalantis.ucrop.AspectRatioOptions");
      if (var2 > 0.0F && var3 > 0.0F) {
         ViewGroup var10 = this.mWrapperStateAspectRatio;
         if (var10 != null) {
            var10.setVisibility(8);
         }

         this.mGestureCropImageView.setTargetAspectRatio(var2 / var3);
      } else if (var9 != null && var4 < var9.size()) {
         this.mGestureCropImageView.setTargetAspectRatio(((AspectRatio)var9.get(var4)).getAspectRatioX() / ((AspectRatio)var9.get(var4)).getAspectRatioY());
      } else {
         this.mGestureCropImageView.setTargetAspectRatio(0.0F);
      }

      var4 = var1.getIntExtra("com.yalantis.ucrop.MaxSizeX", 0);
      int var5 = var1.getIntExtra("com.yalantis.ucrop.MaxSizeY", 0);
      if (var4 > 0 && var5 > 0) {
         this.mGestureCropImageView.setMaxResultImageSizeX(var4);
         this.mGestureCropImageView.setMaxResultImageSizeY(var5);
      }

   }

   private void resetRotation() {
      GestureCropImageView var1 = this.mGestureCropImageView;
      var1.postRotate(-var1.getCurrentAngle());
      this.mGestureCropImageView.setImageToWrapCropBounds();
   }

   private void rotateByAngle(int var1) {
      this.mGestureCropImageView.postRotate((float)var1);
      this.mGestureCropImageView.setImageToWrapCropBounds();
   }

   private void setAllowedGestures(int var1) {
      GestureCropImageView var5 = this.mGestureCropImageView;
      int[] var6 = this.mAllowedGestures;
      int var2 = var6[var1];
      boolean var4 = false;
      boolean var3;
      if (var2 != 3 && var6[var1] != 1) {
         var3 = false;
      } else {
         var3 = true;
      }

      label16: {
         var5.setScaleEnabled(var3);
         var5 = this.mGestureCropImageView;
         var6 = this.mAllowedGestures;
         if (var6[var1] != 3) {
            var3 = var4;
            if (var6[var1] != 2) {
               break label16;
            }
         }

         var3 = true;
      }

      var5.setRotateEnabled(var3);
   }

   private void setAngleText(float var1) {
      TextView var2 = this.mTextViewRotateAngle;
      if (var2 != null) {
         var2.setText(String.format(Locale.getDefault(), "%.1f°", var1));
      }

   }

   private void setImageData(Intent var1) {
      Uri var2 = (Uri)var1.getParcelableExtra("com.yalantis.ucrop.InputUri");
      Uri var3 = (Uri)var1.getParcelableExtra("com.yalantis.ucrop.OutputUri");
      this.processOptions(var1);
      if (var2 != null && var3 != null) {
         try {
            this.mGestureCropImageView.setImageUri(var2, var3);
         } catch (Exception var4) {
            this.setResultError(var4);
            this.finish();
         }

      } else {
         this.setResultError(new NullPointerException(this.getString(string.ucrop_error_input_data_is_absent)));
         this.finish();
      }
   }

   private void setInitialState() {
      if (this.mShowBottomControls) {
         if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
            this.setWidgetState(id.state_aspect_ratio);
         } else {
            this.setWidgetState(id.state_scale);
         }
      } else {
         this.setAllowedGestures(0);
      }
   }

   private void setScaleText(float var1) {
      TextView var2 = this.mTextViewScalePercent;
      if (var2 != null) {
         var2.setText(String.format(Locale.getDefault(), "%d%%", (int)(100.0F * var1)));
      }

   }

   private void setStatusBarColor(int var1) {
      if (VERSION.SDK_INT >= 21) {
         Window var2 = this.getWindow();
         if (var2 != null) {
            var2.addFlags(Integer.MIN_VALUE);
            var2.setStatusBarColor(var1);
         }
      }

   }

   private void setWidgetState(int var1) {
      if (this.mShowBottomControls) {
         ViewGroup var5 = this.mWrapperStateAspectRatio;
         boolean var4;
         if (var1 == id.state_aspect_ratio) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setSelected(var4);
         var5 = this.mWrapperStateRotate;
         if (var1 == id.state_rotate) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setSelected(var4);
         var5 = this.mWrapperStateScale;
         if (var1 == id.state_scale) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setSelected(var4);
         var5 = this.mLayoutAspectRatio;
         int var2 = id.state_aspect_ratio;
         byte var3 = 8;
         byte var6;
         if (var1 == var2) {
            var6 = 0;
         } else {
            var6 = 8;
         }

         var5.setVisibility(var6);
         var5 = this.mLayoutRotate;
         if (var1 == id.state_rotate) {
            var6 = 0;
         } else {
            var6 = 8;
         }

         var5.setVisibility(var6);
         var5 = this.mLayoutScale;
         var6 = var3;
         if (var1 == id.state_scale) {
            var6 = 0;
         }

         var5.setVisibility(var6);
         if (var1 == id.state_scale) {
            this.setAllowedGestures(0);
         } else if (var1 == id.state_rotate) {
            this.setAllowedGestures(1);
         } else {
            this.setAllowedGestures(2);
         }
      }
   }

   private void setupAppBar() {
      this.setStatusBarColor(this.mStatusBarColor);
      Toolbar var1 = (Toolbar)this.findViewById(id.toolbar);
      var1.setBackgroundColor(this.mToolbarColor);
      var1.setTitleTextColor(this.mToolbarWidgetColor);
      TextView var2 = (TextView)var1.findViewById(id.toolbar_title);
      var2.setTextColor(this.mToolbarWidgetColor);
      var2.setText(this.mToolbarTitle);
      Drawable var4 = ContextCompat.getDrawable(this, this.mToolbarCancelDrawable).mutate();
      var4.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
      var1.setNavigationIcon(var4);
      this.setSupportActionBar(var1);
      ActionBar var3 = this.getSupportActionBar();
      if (var3 != null) {
         var3.setDisplayShowTitleEnabled(false);
      }

   }

   private void setupAspectRatioWidget(Intent var1) {
      int var2;
      ArrayList var8;
      label29: {
         var2 = var1.getIntExtra("com.yalantis.ucrop.AspectRatioSelectedByDefault", 0);
         ArrayList var3 = var1.getParcelableArrayListExtra("com.yalantis.ucrop.AspectRatioOptions");
         if (var3 != null) {
            var8 = var3;
            if (!var3.isEmpty()) {
               break label29;
            }
         }

         var2 = 2;
         var8 = new ArrayList();
         var8.add(new AspectRatio((String)null, 1.0F, 1.0F));
         var8.add(new AspectRatio((String)null, 3.0F, 4.0F));
         var8.add(new AspectRatio(this.getString(string.ucrop_label_original).toUpperCase(), 0.0F, 0.0F));
         var8.add(new AspectRatio((String)null, 3.0F, 2.0F));
         var8.add(new AspectRatio((String)null, 16.0F, 9.0F));
      }

      LinearLayout var10 = (LinearLayout)this.findViewById(id.layout_aspect_ratio);
      android.widget.LinearLayout.LayoutParams var4 = new android.widget.LinearLayout.LayoutParams(0, -1);
      var4.weight = 1.0F;
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         AspectRatio var5 = (AspectRatio)var9.next();
         FrameLayout var6 = (FrameLayout)this.getLayoutInflater().inflate(layout.ucrop_aspect_ratio, (ViewGroup)null);
         var6.setLayoutParams(var4);
         AspectRatioTextView var7 = (AspectRatioTextView)var6.getChildAt(0);
         var7.setActiveColor(this.mActiveWidgetColor);
         var7.setAspectRatio(var5);
         var10.addView(var6);
         this.mCropAspectRatioViews.add(var6);
      }

      ((ViewGroup)this.mCropAspectRatioViews.get(var2)).setSelected(true);
      var9 = this.mCropAspectRatioViews.iterator();

      while(var9.hasNext()) {
         ((ViewGroup)var9.next()).setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)var1).getChildAt(0)).getAspectRatio(var1.isSelected()));
               UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
               boolean var2;
               ViewGroup var4;
               if (!var1.isSelected()) {
                  for(Iterator var3 = UCropActivity.this.mCropAspectRatioViews.iterator(); var3.hasNext(); var4.setSelected(var2)) {
                     var4 = (ViewGroup)var3.next();
                     if (var4 == var1) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }
                  }
               }

            }
         });
      }

   }

   private void setupRotateWidget() {
      this.mTextViewRotateAngle = (TextView)this.findViewById(id.text_view_rotate);
      ((HorizontalProgressWheelView)this.findViewById(id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float var1, float var2) {
            UCropActivity.this.mGestureCropImageView.postRotate(var1 / 42.0F);
         }

         public void onScrollEnd() {
            UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)this.findViewById(id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
      this.findViewById(id.wrapper_reset_rotate).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            UCropActivity.this.resetRotation();
         }
      });
      this.findViewById(id.wrapper_rotate_by_angle).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            UCropActivity.this.rotateByAngle(90);
         }
      });
   }

   private void setupScaleWidget() {
      this.mTextViewScalePercent = (TextView)this.findViewById(id.text_view_scale);
      ((HorizontalProgressWheelView)this.findViewById(id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float var1, float var2) {
            if (var1 > 0.0F) {
               UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + (UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F * var1);
            } else {
               UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + (UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F * var1);
            }
         }

         public void onScrollEnd() {
            UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)this.findViewById(id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
   }

   private void setupStatesWrapper() {
      ImageView var1 = (ImageView)this.findViewById(id.image_view_state_scale);
      ImageView var2 = (ImageView)this.findViewById(id.image_view_state_rotate);
      ImageView var3 = (ImageView)this.findViewById(id.image_view_state_aspect_ratio);
      var1.setImageDrawable(new SelectedStateListDrawable(var1.getDrawable(), this.mActiveWidgetColor));
      var2.setImageDrawable(new SelectedStateListDrawable(var2.getDrawable(), this.mActiveWidgetColor));
      var3.setImageDrawable(new SelectedStateListDrawable(var3.getDrawable(), this.mActiveWidgetColor));
   }

   private void setupViews(Intent var1) {
      this.mStatusBarColor = var1.getIntExtra("com.yalantis.ucrop.StatusBarColor", ContextCompat.getColor(this, color.ucrop_color_statusbar));
      this.mToolbarColor = var1.getIntExtra("com.yalantis.ucrop.ToolbarColor", ContextCompat.getColor(this, color.ucrop_color_toolbar));
      this.mActiveWidgetColor = var1.getIntExtra("com.yalantis.ucrop.UcropColorWidgetActive", ContextCompat.getColor(this, color.ucrop_color_widget_active));
      this.mToolbarWidgetColor = var1.getIntExtra("com.yalantis.ucrop.UcropToolbarWidgetColor", ContextCompat.getColor(this, color.ucrop_color_toolbar_widget));
      this.mToolbarCancelDrawable = var1.getIntExtra("com.yalantis.ucrop.UcropToolbarCancelDrawable", drawable.ucrop_ic_cross);
      this.mToolbarCropDrawable = var1.getIntExtra("com.yalantis.ucrop.UcropToolbarCropDrawable", drawable.ucrop_ic_done);
      String var2 = var1.getStringExtra("com.yalantis.ucrop.UcropToolbarTitleText");
      this.mToolbarTitle = var2;
      if (var2 == null) {
         var2 = this.getResources().getString(string.ucrop_label_edit_photo);
      }

      this.mToolbarTitle = var2;
      this.mLogoColor = var1.getIntExtra("com.yalantis.ucrop.UcropLogoColor", ContextCompat.getColor(this, color.ucrop_color_default_logo));
      this.mShowBottomControls = var1.getBooleanExtra("com.yalantis.ucrop.HideBottomControls", false) ^ true;
      this.mRootViewBackgroundColor = var1.getIntExtra("com.yalantis.ucrop.UcropRootViewBackgroundColor", ContextCompat.getColor(this, color.ucrop_color_crop_background));
      this.setupAppBar();
      this.initiateRootViews();
      if (this.mShowBottomControls) {
         ViewGroup var3 = (ViewGroup)this.findViewById(id.ucrop_photobox);
         View.inflate(this, layout.ucrop_controls, var3);
         var3 = (ViewGroup)this.findViewById(id.state_aspect_ratio);
         this.mWrapperStateAspectRatio = var3;
         var3.setOnClickListener(this.mStateClickListener);
         var3 = (ViewGroup)this.findViewById(id.state_rotate);
         this.mWrapperStateRotate = var3;
         var3.setOnClickListener(this.mStateClickListener);
         var3 = (ViewGroup)this.findViewById(id.state_scale);
         this.mWrapperStateScale = var3;
         var3.setOnClickListener(this.mStateClickListener);
         this.mLayoutAspectRatio = (ViewGroup)this.findViewById(id.layout_aspect_ratio);
         this.mLayoutRotate = (ViewGroup)this.findViewById(id.layout_rotate_wheel);
         this.mLayoutScale = (ViewGroup)this.findViewById(id.layout_scale_wheel);
         this.setupAspectRatioWidget(var1);
         this.setupRotateWidget();
         this.setupScaleWidget();
         this.setupStatesWrapper();
      }

   }

   protected void cropAndSaveImage() {
      this.mBlockingView.setClickable(true);
      this.mShowLoader = true;
      this.supportInvalidateOptionsMenu();
      this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
         public void onBitmapCropped(Uri var1, int var2, int var3, int var4, int var5) {
            UCropActivity var6 = UCropActivity.this;
            var6.setResultUri(var1, var6.mGestureCropImageView.getTargetAspectRatio(), var2, var3, var4, var5);
            UCropActivity.this.finish();
         }

         public void onCropFailure(Throwable var1) {
            UCropActivity.this.setResultError(var1);
            UCropActivity.this.finish();
         }
      });
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(layout.ucrop_activity_photobox);
      Intent var2 = this.getIntent();
      this.setupViews(var2);
      this.setImageData(var2);
      this.setInitialState();
      this.addBlockingView();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(menu.ucrop_menu_activity, var1);
      MenuItem var2 = var1.findItem(id.menu_loader);
      Drawable var3 = var2.getIcon();
      if (var3 != null) {
         try {
            var3.mutate();
            var3.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
            var2.setIcon(var3);
         } catch (IllegalStateException var4) {
            Log.i("UCropActivity", String.format("%s - %s", var4.getMessage(), this.getString(string.ucrop_mutate_exception_hint)));
         }

         ((Animatable)var2.getIcon()).start();
      }

      MenuItem var5 = var1.findItem(id.menu_crop);
      Drawable var6 = ContextCompat.getDrawable(this, this.mToolbarCropDrawable);
      if (var6 != null) {
         var6.mutate();
         var6.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
         var5.setIcon(var6);
      }

      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      if (var1.getItemId() == id.menu_crop) {
         this.cropAndSaveImage();
      } else if (var1.getItemId() == 16908332) {
         this.onBackPressed();
      }

      return super.onOptionsItemSelected(var1);
   }

   public boolean onPrepareOptionsMenu(Menu var1) {
      var1.findItem(id.menu_crop).setVisible(this.mShowLoader ^ true);
      var1.findItem(id.menu_loader).setVisible(this.mShowLoader);
      return super.onPrepareOptionsMenu(var1);
   }

   protected void onStop() {
      super.onStop();
      GestureCropImageView var1 = this.mGestureCropImageView;
      if (var1 != null) {
         var1.cancelAllAnimations();
      }

   }

   protected void setResultError(Throwable var1) {
      this.setResult(96, (new Intent()).putExtra("com.yalantis.ucrop.Error", var1));
   }

   protected void setResultUri(Uri var1, float var2, int var3, int var4, int var5, int var6) {
      this.setResult(-1, (new Intent()).putExtra("com.yalantis.ucrop.OutputUri", var1).putExtra("com.yalantis.ucrop.CropAspectRatio", var2).putExtra("com.yalantis.ucrop.ImageWidth", var5).putExtra("com.yalantis.ucrop.ImageHeight", var6).putExtra("com.yalantis.ucrop.OffsetX", var3).putExtra("com.yalantis.ucrop.OffsetY", var4));
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface GestureTypes {
   }
}
