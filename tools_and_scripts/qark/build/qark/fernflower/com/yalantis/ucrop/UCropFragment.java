package com.yalantis.ucrop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.yalantis.ucrop.R.color;
import com.yalantis.ucrop.R.dimen;
import com.yalantis.ucrop.R.id;
import com.yalantis.ucrop.R.layout;
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

public class UCropFragment extends Fragment {
   public static final int ALL = 3;
   public static final CompressFormat DEFAULT_COMPRESS_FORMAT;
   public static final int DEFAULT_COMPRESS_QUALITY = 90;
   public static final int NONE = 0;
   public static final int ROTATE = 2;
   private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
   public static final int SCALE = 1;
   private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
   private static final int TABS_COUNT = 3;
   public static final String TAG = "UCropFragment";
   private UCropFragmentCallback callback;
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
   private final OnClickListener mStateClickListener;
   private TextView mTextViewRotateAngle;
   private TextView mTextViewScalePercent;
   private UCropView mUCropView;
   private ViewGroup mWrapperStateAspectRatio;
   private ViewGroup mWrapperStateRotate;
   private ViewGroup mWrapperStateScale;

   static {
      DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
   }

   public UCropFragment() {
      this.mCompressFormat = DEFAULT_COMPRESS_FORMAT;
      this.mCompressQuality = 90;
      this.mAllowedGestures = new int[]{1, 2, 3};
      this.mImageListener = new TransformImageView.TransformImageListener() {
         public void onLoadComplete() {
            UCropFragment.this.mUCropView.animate().alpha(1.0F).setDuration(300L).setInterpolator(new AccelerateInterpolator());
            UCropFragment.this.mBlockingView.setClickable(false);
            UCropFragment.this.callback.loadingProgress(false);
         }

         public void onLoadFailure(Exception var1) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(var1));
         }

         public void onRotate(float var1) {
            UCropFragment.this.setAngleText(var1);
         }

         public void onScale(float var1) {
            UCropFragment.this.setScaleText(var1);
         }
      };
      this.mStateClickListener = new OnClickListener() {
         public void onClick(View var1) {
            if (!var1.isSelected()) {
               UCropFragment.this.setWidgetState(var1.getId());
            }

         }
      };
   }

   private void addBlockingView(View var1) {
      if (this.mBlockingView == null) {
         this.mBlockingView = new View(this.getContext());
         LayoutParams var2 = new LayoutParams(-1, -1);
         this.mBlockingView.setLayoutParams(var2);
         this.mBlockingView.setClickable(true);
      }

      ((RelativeLayout)var1.findViewById(id.ucrop_photobox)).addView(this.mBlockingView);
   }

   private void initiateRootViews(View var1) {
      UCropView var2 = (UCropView)var1.findViewById(id.ucrop);
      this.mUCropView = var2;
      this.mGestureCropImageView = var2.getCropImageView();
      this.mOverlayView = this.mUCropView.getOverlayView();
      this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
      ((ImageView)var1.findViewById(id.image_view_logo)).setColorFilter(this.mLogoColor, Mode.SRC_ATOP);
      var1.findViewById(id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
   }

   public static UCropFragment newInstance(Bundle var0) {
      UCropFragment var1 = new UCropFragment();
      var1.setArguments(var0);
      return var1;
   }

   private void processOptions(Bundle var1) {
      String var7 = var1.getString("com.yalantis.ucrop.CompressionFormatName");
      CompressFormat var6 = null;
      if (!TextUtils.isEmpty(var7)) {
         var6 = CompressFormat.valueOf(var7);
      }

      if (var6 == null) {
         var6 = DEFAULT_COMPRESS_FORMAT;
      }

      this.mCompressFormat = var6;
      this.mCompressQuality = var1.getInt("com.yalantis.ucrop.CompressionQuality", 90);
      int[] var8 = var1.getIntArray("com.yalantis.ucrop.AllowedGestures");
      if (var8 != null && var8.length == 3) {
         this.mAllowedGestures = var8;
      }

      this.mGestureCropImageView.setMaxBitmapSize(var1.getInt("com.yalantis.ucrop.MaxBitmapSize", 0));
      this.mGestureCropImageView.setMaxScaleMultiplier(var1.getFloat("com.yalantis.ucrop.MaxScaleMultiplier", 10.0F));
      this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long)var1.getInt("com.yalantis.ucrop.ImageToCropBoundsAnimDuration", 500));
      this.mOverlayView.setFreestyleCropEnabled(var1.getBoolean("com.yalantis.ucrop.FreeStyleCrop", false));
      this.mOverlayView.setDimmedColor(var1.getInt("com.yalantis.ucrop.DimmedLayerColor", this.getResources().getColor(color.ucrop_color_default_dimmed)));
      this.mOverlayView.setCircleDimmedLayer(var1.getBoolean("com.yalantis.ucrop.CircleDimmedLayer", false));
      this.mOverlayView.setShowCropFrame(var1.getBoolean("com.yalantis.ucrop.ShowCropFrame", true));
      this.mOverlayView.setCropFrameColor(var1.getInt("com.yalantis.ucrop.CropFrameColor", this.getResources().getColor(color.ucrop_color_default_crop_frame)));
      this.mOverlayView.setCropFrameStrokeWidth(var1.getInt("com.yalantis.ucrop.CropFrameStrokeWidth", this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width)));
      this.mOverlayView.setShowCropGrid(var1.getBoolean("com.yalantis.ucrop.ShowCropGrid", true));
      this.mOverlayView.setCropGridRowCount(var1.getInt("com.yalantis.ucrop.CropGridRowCount", 2));
      this.mOverlayView.setCropGridColumnCount(var1.getInt("com.yalantis.ucrop.CropGridColumnCount", 2));
      this.mOverlayView.setCropGridColor(var1.getInt("com.yalantis.ucrop.CropGridColor", this.getResources().getColor(color.ucrop_color_default_crop_grid)));
      this.mOverlayView.setCropGridStrokeWidth(var1.getInt("com.yalantis.ucrop.CropGridStrokeWidth", this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width)));
      float var2 = var1.getFloat("com.yalantis.ucrop.AspectRatioX", 0.0F);
      float var3 = var1.getFloat("com.yalantis.ucrop.AspectRatioY", 0.0F);
      int var4 = var1.getInt("com.yalantis.ucrop.AspectRatioSelectedByDefault", 0);
      ArrayList var9 = var1.getParcelableArrayList("com.yalantis.ucrop.AspectRatioOptions");
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

      var4 = var1.getInt("com.yalantis.ucrop.MaxSizeX", 0);
      int var5 = var1.getInt("com.yalantis.ucrop.MaxSizeY", 0);
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

   private void setImageData(Bundle var1) {
      Uri var2 = (Uri)var1.getParcelable("com.yalantis.ucrop.InputUri");
      Uri var3 = (Uri)var1.getParcelable("com.yalantis.ucrop.OutputUri");
      this.processOptions(var1);
      if (var2 != null && var3 != null) {
         try {
            this.mGestureCropImageView.setImageUri(var2, var3);
         } catch (Exception var4) {
            this.callback.onCropFinish(this.getError(var4));
         }

      } else {
         this.callback.onCropFinish(this.getError(new NullPointerException(this.getString(string.ucrop_error_input_data_is_absent))));
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

   private void setupAspectRatioWidget(Bundle var1, View var2) {
      int var3;
      ArrayList var8;
      label29: {
         var3 = var1.getInt("com.yalantis.ucrop.AspectRatioSelectedByDefault", 0);
         ArrayList var4 = var1.getParcelableArrayList("com.yalantis.ucrop.AspectRatioOptions");
         if (var4 != null) {
            var8 = var4;
            if (!var4.isEmpty()) {
               break label29;
            }
         }

         var3 = 2;
         var8 = new ArrayList();
         var8.add(new AspectRatio((String)null, 1.0F, 1.0F));
         var8.add(new AspectRatio((String)null, 3.0F, 4.0F));
         var8.add(new AspectRatio(this.getString(string.ucrop_label_original).toUpperCase(), 0.0F, 0.0F));
         var8.add(new AspectRatio((String)null, 3.0F, 2.0F));
         var8.add(new AspectRatio((String)null, 16.0F, 9.0F));
      }

      LinearLayout var9 = (LinearLayout)var2.findViewById(id.layout_aspect_ratio);
      android.widget.LinearLayout.LayoutParams var11 = new android.widget.LinearLayout.LayoutParams(0, -1);
      var11.weight = 1.0F;
      Iterator var10 = var8.iterator();

      while(var10.hasNext()) {
         AspectRatio var5 = (AspectRatio)var10.next();
         FrameLayout var6 = (FrameLayout)this.getLayoutInflater().inflate(layout.ucrop_aspect_ratio, (ViewGroup)null);
         var6.setLayoutParams(var11);
         AspectRatioTextView var7 = (AspectRatioTextView)var6.getChildAt(0);
         var7.setActiveColor(this.mActiveWidgetColor);
         var7.setAspectRatio(var5);
         var9.addView(var6);
         this.mCropAspectRatioViews.add(var6);
      }

      ((ViewGroup)this.mCropAspectRatioViews.get(var3)).setSelected(true);
      var10 = this.mCropAspectRatioViews.iterator();

      while(var10.hasNext()) {
         ((ViewGroup)var10.next()).setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               UCropFragment.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView)((ViewGroup)var1).getChildAt(0)).getAspectRatio(var1.isSelected()));
               UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
               boolean var2;
               ViewGroup var4;
               if (!var1.isSelected()) {
                  for(Iterator var3 = UCropFragment.this.mCropAspectRatioViews.iterator(); var3.hasNext(); var4.setSelected(var2)) {
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

   private void setupRotateWidget(View var1) {
      this.mTextViewRotateAngle = (TextView)var1.findViewById(id.text_view_rotate);
      ((HorizontalProgressWheelView)var1.findViewById(id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float var1, float var2) {
            UCropFragment.this.mGestureCropImageView.postRotate(var1 / 42.0F);
         }

         public void onScrollEnd() {
            UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)var1.findViewById(id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
      var1.findViewById(id.wrapper_reset_rotate).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            UCropFragment.this.resetRotation();
         }
      });
      var1.findViewById(id.wrapper_rotate_by_angle).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            UCropFragment.this.rotateByAngle(90);
         }
      });
   }

   private void setupScaleWidget(View var1) {
      this.mTextViewScalePercent = (TextView)var1.findViewById(id.text_view_scale);
      ((HorizontalProgressWheelView)var1.findViewById(id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
         public void onScroll(float var1, float var2) {
            if (var1 > 0.0F) {
               UCropFragment.this.mGestureCropImageView.zoomInImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + (UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0F * var1);
            } else {
               UCropFragment.this.mGestureCropImageView.zoomOutImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + (UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0F * var1);
            }
         }

         public void onScrollEnd() {
            UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
         }

         public void onScrollStart() {
            UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
         }
      });
      ((HorizontalProgressWheelView)var1.findViewById(id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
   }

   private void setupStatesWrapper(View var1) {
      ImageView var2 = (ImageView)var1.findViewById(id.image_view_state_scale);
      ImageView var3 = (ImageView)var1.findViewById(id.image_view_state_rotate);
      ImageView var4 = (ImageView)var1.findViewById(id.image_view_state_aspect_ratio);
      var2.setImageDrawable(new SelectedStateListDrawable(var2.getDrawable(), this.mActiveWidgetColor));
      var3.setImageDrawable(new SelectedStateListDrawable(var3.getDrawable(), this.mActiveWidgetColor));
      var4.setImageDrawable(new SelectedStateListDrawable(var4.getDrawable(), this.mActiveWidgetColor));
   }

   public void cropAndSaveImage() {
      this.mBlockingView.setClickable(true);
      this.callback.loadingProgress(true);
      this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
         public void onBitmapCropped(Uri var1, int var2, int var3, int var4, int var5) {
            UCropFragmentCallback var6 = UCropFragment.this.callback;
            UCropFragment var7 = UCropFragment.this;
            var6.onCropFinish(var7.getResult(var1, var7.mGestureCropImageView.getTargetAspectRatio(), var2, var3, var4, var5));
            UCropFragment.this.callback.loadingProgress(false);
         }

         public void onCropFailure(Throwable var1) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(var1));
         }
      });
   }

   protected UCropFragment.UCropResult getError(Throwable var1) {
      return new UCropFragment.UCropResult(96, (new Intent()).putExtra("com.yalantis.ucrop.Error", var1));
   }

   protected UCropFragment.UCropResult getResult(Uri var1, float var2, int var3, int var4, int var5, int var6) {
      return new UCropFragment.UCropResult(-1, (new Intent()).putExtra("com.yalantis.ucrop.OutputUri", var1).putExtra("com.yalantis.ucrop.CropAspectRatio", var2).putExtra("com.yalantis.ucrop.ImageWidth", var5).putExtra("com.yalantis.ucrop.ImageHeight", var6).putExtra("com.yalantis.ucrop.OffsetX", var3).putExtra("com.yalantis.ucrop.OffsetY", var4));
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);

      try {
         this.callback = (UCropFragmentCallback)var1;
      } catch (ClassCastException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.toString());
         var2.append(" must implement UCropFragmentCallback");
         throw new ClassCastException(var2.toString());
      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(layout.ucrop_fragment_photobox, var2, false);
      Bundle var5 = this.getArguments();
      this.setupViews(var4, var5);
      this.setImageData(var5);
      this.setInitialState();
      this.addBlockingView(var4);
      return var4;
   }

   public void setCallback(UCropFragmentCallback var1) {
      this.callback = var1;
   }

   public void setupViews(View var1, Bundle var2) {
      this.mActiveWidgetColor = var2.getInt("com.yalantis.ucrop.UcropColorWidgetActive", ContextCompat.getColor(this.getContext(), color.ucrop_color_widget_active));
      this.mLogoColor = var2.getInt("com.yalantis.ucrop.UcropLogoColor", ContextCompat.getColor(this.getContext(), color.ucrop_color_default_logo));
      this.mShowBottomControls = var2.getBoolean("com.yalantis.ucrop.HideBottomControls", false) ^ true;
      this.mRootViewBackgroundColor = var2.getInt("com.yalantis.ucrop.UcropRootViewBackgroundColor", ContextCompat.getColor(this.getContext(), color.ucrop_color_crop_background));
      this.initiateRootViews(var1);
      this.callback.loadingProgress(true);
      if (this.mShowBottomControls) {
         ViewGroup var3 = (ViewGroup)var1.findViewById(id.ucrop_photobox);
         View.inflate(this.getContext(), layout.ucrop_controls, var3);
         var3 = (ViewGroup)var1.findViewById(id.state_aspect_ratio);
         this.mWrapperStateAspectRatio = var3;
         var3.setOnClickListener(this.mStateClickListener);
         var3 = (ViewGroup)var1.findViewById(id.state_rotate);
         this.mWrapperStateRotate = var3;
         var3.setOnClickListener(this.mStateClickListener);
         var3 = (ViewGroup)var1.findViewById(id.state_scale);
         this.mWrapperStateScale = var3;
         var3.setOnClickListener(this.mStateClickListener);
         this.mLayoutAspectRatio = (ViewGroup)var1.findViewById(id.layout_aspect_ratio);
         this.mLayoutRotate = (ViewGroup)var1.findViewById(id.layout_rotate_wheel);
         this.mLayoutScale = (ViewGroup)var1.findViewById(id.layout_scale_wheel);
         this.setupAspectRatioWidget(var2, var1);
         this.setupRotateWidget(var1);
         this.setupScaleWidget(var1);
         this.setupStatesWrapper(var1);
      }

   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface GestureTypes {
   }

   public class UCropResult {
      public int mResultCode;
      public Intent mResultData;

      public UCropResult(int var2, Intent var3) {
         this.mResultCode = var2;
         this.mResultData = var3;
      }
   }
}
