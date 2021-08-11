package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.yalantis.ucrop.R.id;
import com.yalantis.ucrop.R.layout;
import com.yalantis.ucrop.R.styleable;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

public class UCropView extends FrameLayout {
   private GestureCropImageView mGestureCropImageView;
   private final OverlayView mViewOverlay;

   public UCropView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public UCropView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      LayoutInflater.from(var1).inflate(layout.ucrop_view, this, true);
      this.mGestureCropImageView = (GestureCropImageView)this.findViewById(id.image_view_crop);
      this.mViewOverlay = (OverlayView)this.findViewById(id.view_overlay);
      TypedArray var4 = var1.obtainStyledAttributes(var2, styleable.ucrop_UCropView);
      this.mViewOverlay.processStyledAttributes(var4);
      this.mGestureCropImageView.processStyledAttributes(var4);
      var4.recycle();
      this.setListenersToViews();
   }

   private void setListenersToViews() {
      this.mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
         public void onCropAspectRatioChanged(float var1) {
            UCropView.this.mViewOverlay.setTargetAspectRatio(var1);
         }
      });
      this.mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
         public void onCropRectUpdated(RectF var1) {
            UCropView.this.mGestureCropImageView.setCropRect(var1);
         }
      });
   }

   public GestureCropImageView getCropImageView() {
      return this.mGestureCropImageView;
   }

   public OverlayView getOverlayView() {
      return this.mViewOverlay;
   }

   public void resetCropImageView() {
      this.removeView(this.mGestureCropImageView);
      this.mGestureCropImageView = new GestureCropImageView(this.getContext());
      this.setListenersToViews();
      this.mGestureCropImageView.setCropRect(this.getOverlayView().getCropViewRect());
      this.addView(this.mGestureCropImageView, 0);
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }
}
