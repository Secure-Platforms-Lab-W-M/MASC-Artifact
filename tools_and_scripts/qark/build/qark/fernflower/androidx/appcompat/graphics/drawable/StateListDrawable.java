package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.StateSet;
import androidx.appcompat.resources.R.styleable;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class StateListDrawable extends DrawableContainer {
   private static final boolean DEBUG = false;
   private static final String TAG = "StateListDrawable";
   private boolean mMutated;
   private StateListDrawable.StateListState mStateListState;

   StateListDrawable() {
      this((StateListDrawable.StateListState)null, (Resources)null);
   }

   StateListDrawable(StateListDrawable.StateListState var1) {
      if (var1 != null) {
         this.setConstantState(var1);
      }

   }

   StateListDrawable(StateListDrawable.StateListState var1, Resources var2) {
      this.setConstantState(new StateListDrawable.StateListState(var1, this, var2));
      this.onStateChange(this.getState());
   }

   private void inflateChildElements(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      StateListDrawable.StateListState var11 = this.mStateListState;
      int var6 = var3.getDepth() + 1;

      while(true) {
         int var7;
         int var8;
         do {
            do {
               do {
                  var7 = var3.next();
                  if (var7 == 1) {
                     return;
                  }

                  var8 = var3.getDepth();
                  if (var8 < var6 && var7 == 3) {
                     return;
                  }
               } while(var7 != 2);
            } while(var8 > var6);
         } while(!var3.getName().equals("item"));

         TypedArray var10 = TypedArrayUtils.obtainAttributes(var2, var5, var4, styleable.StateListDrawableItem);
         Drawable var9 = null;
         var7 = var10.getResourceId(styleable.StateListDrawableItem_android_drawable, -1);
         if (var7 > 0) {
            var9 = ResourceManagerInternal.get().getDrawable(var1, var7);
         }

         var10.recycle();
         int[] var12 = this.extractStateSet(var4);
         Drawable var14 = var9;
         if (var9 == null) {
            while(true) {
               var7 = var3.next();
               if (var7 != 4) {
                  if (var7 != 2) {
                     StringBuilder var13 = new StringBuilder();
                     var13.append(var3.getPositionDescription());
                     var13.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                     throw new XmlPullParserException(var13.toString());
                  }

                  if (VERSION.SDK_INT >= 21) {
                     var14 = Drawable.createFromXmlInner(var2, var3, var4, var5);
                  } else {
                     var14 = Drawable.createFromXmlInner(var2, var3, var4);
                  }
                  break;
               }
            }
         }

         var11.addStateSet(var12, var14);
      }
   }

   private void updateStateFromTypedArray(TypedArray var1) {
      StateListDrawable.StateListState var2 = this.mStateListState;
      if (VERSION.SDK_INT >= 21) {
         var2.mChangingConfigurations |= var1.getChangingConfigurations();
      }

      var2.mVariablePadding = var1.getBoolean(styleable.StateListDrawable_android_variablePadding, var2.mVariablePadding);
      var2.mConstantSize = var1.getBoolean(styleable.StateListDrawable_android_constantSize, var2.mConstantSize);
      var2.mEnterFadeDuration = var1.getInt(styleable.StateListDrawable_android_enterFadeDuration, var2.mEnterFadeDuration);
      var2.mExitFadeDuration = var1.getInt(styleable.StateListDrawable_android_exitFadeDuration, var2.mExitFadeDuration);
      var2.mDither = var1.getBoolean(styleable.StateListDrawable_android_dither, var2.mDither);
   }

   public void addState(int[] var1, Drawable var2) {
      if (var2 != null) {
         this.mStateListState.addStateSet(var1, var2);
         this.onStateChange(this.getState());
      }

   }

   public void applyTheme(Theme var1) {
      super.applyTheme(var1);
      this.onStateChange(this.getState());
   }

   void clearMutated() {
      super.clearMutated();
      this.mMutated = false;
   }

   StateListDrawable.StateListState cloneConstantState() {
      return new StateListDrawable.StateListState(this.mStateListState, this, (Resources)null);
   }

   int[] extractStateSet(AttributeSet var1) {
      int var3 = 0;
      int var5 = var1.getAttributeCount();
      int[] var6 = new int[var5];

      for(int var2 = 0; var2 < var5; ++var2) {
         int var4 = var1.getAttributeNameResource(var2);
         if (var4 != 0 && var4 != 16842960 && var4 != 16843161) {
            if (!var1.getAttributeBooleanValue(var2, false)) {
               var4 = -var4;
            }

            var6[var3] = var4;
            ++var3;
         }
      }

      return StateSet.trimStateSet(var6, var3);
   }

   int getStateCount() {
      return this.mStateListState.getChildCount();
   }

   Drawable getStateDrawable(int var1) {
      return this.mStateListState.getChild(var1);
   }

   int getStateDrawableIndex(int[] var1) {
      return this.mStateListState.indexOfStateSet(var1);
   }

   StateListDrawable.StateListState getStateListState() {
      return this.mStateListState;
   }

   int[] getStateSet(int var1) {
      return this.mStateListState.mStateSets[var1];
   }

   public void inflate(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, styleable.StateListDrawable);
      this.setVisible(var6.getBoolean(styleable.StateListDrawable_android_visible, true), true);
      this.updateStateFromTypedArray(var6);
      this.updateDensity(var2);
      var6.recycle();
      this.inflateChildElements(var1, var2, var3, var4, var5);
      this.onStateChange(this.getState());
   }

   public boolean isStateful() {
      return true;
   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
         this.mStateListState.mutate();
         this.mMutated = true;
      }

      return this;
   }

   protected boolean onStateChange(int[] var1) {
      boolean var4 = super.onStateChange(var1);
      int var3 = this.mStateListState.indexOfStateSet(var1);
      int var2 = var3;
      if (var3 < 0) {
         var2 = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
      }

      return this.selectDrawable(var2) || var4;
   }

   void setConstantState(DrawableContainer.DrawableContainerState var1) {
      super.setConstantState(var1);
      if (var1 instanceof StateListDrawable.StateListState) {
         this.mStateListState = (StateListDrawable.StateListState)var1;
      }

   }

   static class StateListState extends DrawableContainer.DrawableContainerState {
      int[][] mStateSets;

      StateListState(StateListDrawable.StateListState var1, StateListDrawable var2, Resources var3) {
         super(var1, var2, var3);
         if (var1 != null) {
            this.mStateSets = var1.mStateSets;
         } else {
            this.mStateSets = new int[this.getCapacity()][];
         }
      }

      int addStateSet(int[] var1, Drawable var2) {
         int var3 = this.addChild(var2);
         this.mStateSets[var3] = var1;
         return var3;
      }

      public void growArray(int var1, int var2) {
         super.growArray(var1, var2);
         int[][] var3 = new int[var2][];
         System.arraycopy(this.mStateSets, 0, var3, 0, var1);
         this.mStateSets = var3;
      }

      int indexOfStateSet(int[] var1) {
         int[][] var4 = this.mStateSets;
         int var3 = this.getChildCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            if (StateSet.stateSetMatches(var4[var2], var1)) {
               return var2;
            }
         }

         return -1;
      }

      void mutate() {
         int[][] var2 = this.mStateSets;
         int[][] var3 = new int[var2.length][];

         for(int var1 = var2.length - 1; var1 >= 0; --var1) {
            var2 = this.mStateSets;
            int[] var4;
            if (var2[var1] != null) {
               var4 = (int[])var2[var1].clone();
            } else {
               var4 = null;
            }

            var3[var1] = var4;
         }

         this.mStateSets = var3;
      }

      public Drawable newDrawable() {
         return new StateListDrawable(this, (Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new StateListDrawable(this, var1);
      }
   }
}
