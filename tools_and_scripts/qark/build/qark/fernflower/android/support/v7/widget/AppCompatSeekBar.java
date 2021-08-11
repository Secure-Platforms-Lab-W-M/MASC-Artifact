package android.support.v7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.support.v7.appcompat.R$attr;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class AppCompatSeekBar extends SeekBar {
   private final AppCompatSeekBarHelper mAppCompatSeekBarHelper;

   public AppCompatSeekBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatSeekBar(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.seekBarStyle);
   }

   public AppCompatSeekBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAppCompatSeekBarHelper = new AppCompatSeekBarHelper(this);
      this.mAppCompatSeekBarHelper.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.mAppCompatSeekBarHelper.drawableStateChanged();
   }

   @RequiresApi(11)
   public void jumpDrawablesToCurrentState() {
      super.jumpDrawablesToCurrentState();
      this.mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
   }

   protected void onDraw(Canvas var1) {
      synchronized(this){}

      try {
         super.onDraw(var1);
         this.mAppCompatSeekBarHelper.drawTickMarks(var1);
      } finally {
         ;
      }

   }
}
