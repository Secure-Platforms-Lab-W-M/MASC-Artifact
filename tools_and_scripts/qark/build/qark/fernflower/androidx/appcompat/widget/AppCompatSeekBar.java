package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;
import androidx.appcompat.R.attr;

public class AppCompatSeekBar extends SeekBar {
   private final AppCompatSeekBarHelper mAppCompatSeekBarHelper;

   public AppCompatSeekBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatSeekBar(Context var1, AttributeSet var2) {
      this(var1, var2, attr.seekBarStyle);
   }

   public AppCompatSeekBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      AppCompatSeekBarHelper var4 = new AppCompatSeekBarHelper(this);
      this.mAppCompatSeekBarHelper = var4;
      var4.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      this.mAppCompatSeekBarHelper.drawableStateChanged();
   }

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
