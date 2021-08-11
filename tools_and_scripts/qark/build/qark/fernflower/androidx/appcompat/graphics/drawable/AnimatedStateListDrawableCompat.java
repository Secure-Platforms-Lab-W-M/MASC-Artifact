package androidx.appcompat.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.appcompat.resources.R.styleable;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat extends StateListDrawable implements TintAwareDrawable {
   private static final String ELEMENT_ITEM = "item";
   private static final String ELEMENT_TRANSITION = "transition";
   private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
   private static final String LOGTAG = AnimatedStateListDrawableCompat.class.getSimpleName();
   private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
   private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
   private boolean mMutated;
   private AnimatedStateListDrawableCompat.AnimatedStateListState mState;
   private AnimatedStateListDrawableCompat.Transition mTransition;
   private int mTransitionFromIndex;
   private int mTransitionToIndex;

   public AnimatedStateListDrawableCompat() {
      this((AnimatedStateListDrawableCompat.AnimatedStateListState)null, (Resources)null);
   }

   AnimatedStateListDrawableCompat(AnimatedStateListDrawableCompat.AnimatedStateListState var1, Resources var2) {
      super((StateListDrawable.StateListState)null);
      this.mTransitionToIndex = -1;
      this.mTransitionFromIndex = -1;
      this.setConstantState(new AnimatedStateListDrawableCompat.AnimatedStateListState(var1, this, var2));
      this.onStateChange(this.getState());
      this.jumpToCurrentState();
   }

   public static AnimatedStateListDrawableCompat create(Context var0, int var1, Theme var2) {
      XmlPullParserException var16;
      label57: {
         IOException var10000;
         label48: {
            Resources var3;
            XmlResourceParser var4;
            AttributeSet var5;
            boolean var10001;
            try {
               var3 = var0.getResources();
               var4 = var3.getXml(var1);
               var5 = Xml.asAttributeSet(var4);
            } catch (XmlPullParserException var12) {
               var16 = var12;
               var10001 = false;
               break label57;
            } catch (IOException var13) {
               var10000 = var13;
               var10001 = false;
               break label48;
            }

            while(true) {
               try {
                  var1 = var4.next();
               } catch (XmlPullParserException var10) {
                  var16 = var10;
                  var10001 = false;
                  break label57;
               } catch (IOException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break;
               }

               if (var1 == 2 || var1 == 1) {
                  if (var1 == 2) {
                     try {
                        return createFromXmlInner(var0, var3, var4, var5, var2);
                     } catch (XmlPullParserException var6) {
                        var16 = var6;
                        var10001 = false;
                        break label57;
                     } catch (IOException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break;
                     }
                  } else {
                     try {
                        throw new XmlPullParserException("No start tag found");
                     } catch (XmlPullParserException var8) {
                        var16 = var8;
                        var10001 = false;
                        break label57;
                     } catch (IOException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break;
                     }
                  }
               }
            }
         }

         IOException var14 = var10000;
         Log.e(LOGTAG, "parser error", var14);
         return null;
      }

      XmlPullParserException var15 = var16;
      Log.e(LOGTAG, "parser error", var15);
      return null;
   }

   public static AnimatedStateListDrawableCompat createFromXmlInner(Context var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws IOException, XmlPullParserException {
      String var5 = var2.getName();
      if (var5.equals("animated-selector")) {
         AnimatedStateListDrawableCompat var6 = new AnimatedStateListDrawableCompat();
         var6.inflate(var0, var1, var2, var3, var4);
         return var6;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append(var2.getPositionDescription());
         var7.append(": invalid animated-selector tag ");
         var7.append(var5);
         throw new XmlPullParserException(var7.toString());
      }
   }

   private void inflateChildElements(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      int var6 = var3.getDepth() + 1;

      while(true) {
         int var7 = var3.next();
         if (var7 == 1) {
            break;
         }

         int var8 = var3.getDepth();
         if (var8 < var6 && var7 == 3) {
            break;
         }

         if (var7 == 2 && var8 <= var6) {
            if (var3.getName().equals("item")) {
               this.parseItem(var1, var2, var3, var4, var5);
            } else if (var3.getName().equals("transition")) {
               this.parseTransition(var1, var2, var3, var4, var5);
            }
         }
      }

   }

   private void init() {
      this.onStateChange(this.getState());
   }

   private int parseItem(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var9 = TypedArrayUtils.obtainAttributes(var2, var5, var4, styleable.AnimatedStateListDrawableItem);
      int var6 = var9.getResourceId(styleable.AnimatedStateListDrawableItem_android_id, 0);
      Drawable var8 = null;
      int var7 = var9.getResourceId(styleable.AnimatedStateListDrawableItem_android_drawable, -1);
      if (var7 > 0) {
         var8 = ResourceManagerInternal.get().getDrawable(var1, var7);
      }

      var9.recycle();
      int[] var12 = this.extractStateSet(var4);
      Object var10 = var8;
      StringBuilder var11;
      if (var8 == null) {
         while(true) {
            var7 = var3.next();
            if (var7 != 4) {
               if (var7 != 2) {
                  var11 = new StringBuilder();
                  var11.append(var3.getPositionDescription());
                  var11.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                  throw new XmlPullParserException(var11.toString());
               }

               if (var3.getName().equals("vector")) {
                  var10 = VectorDrawableCompat.createFromXmlInner(var2, var3, var4, var5);
               } else if (VERSION.SDK_INT >= 21) {
                  var10 = Drawable.createFromXmlInner(var2, var3, var4, var5);
               } else {
                  var10 = Drawable.createFromXmlInner(var2, var3, var4);
               }
               break;
            }
         }
      }

      if (var10 != null) {
         return this.mState.addStateSet(var12, (Drawable)var10, var6);
      } else {
         var11 = new StringBuilder();
         var11.append(var3.getPositionDescription());
         var11.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
         throw new XmlPullParserException(var11.toString());
      }
   }

   private int parseTransition(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var10 = TypedArrayUtils.obtainAttributes(var2, var5, var4, styleable.AnimatedStateListDrawableTransition);
      int var6 = var10.getResourceId(styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
      int var7 = var10.getResourceId(styleable.AnimatedStateListDrawableTransition_android_toId, -1);
      Drawable var11 = null;
      int var8 = var10.getResourceId(styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
      if (var8 > 0) {
         var11 = ResourceManagerInternal.get().getDrawable(var1, var8);
      }

      boolean var9 = var10.getBoolean(styleable.AnimatedStateListDrawableTransition_android_reversible, false);
      var10.recycle();
      Object var13 = var11;
      StringBuilder var12;
      if (var11 == null) {
         while(true) {
            var8 = var3.next();
            if (var8 != 4) {
               if (var8 != 2) {
                  var12 = new StringBuilder();
                  var12.append(var3.getPositionDescription());
                  var12.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
                  throw new XmlPullParserException(var12.toString());
               }

               if (var3.getName().equals("animated-vector")) {
                  var13 = AnimatedVectorDrawableCompat.createFromXmlInner(var1, var2, var3, var4, var5);
               } else if (VERSION.SDK_INT >= 21) {
                  var13 = Drawable.createFromXmlInner(var2, var3, var4, var5);
               } else {
                  var13 = Drawable.createFromXmlInner(var2, var3, var4);
               }
               break;
            }
         }
      }

      if (var13 != null) {
         if (var6 != -1 && var7 != -1) {
            return this.mState.addTransition(var6, var7, (Drawable)var13, var9);
         } else {
            var12 = new StringBuilder();
            var12.append(var3.getPositionDescription());
            var12.append(": <transition> tag requires 'fromId' & 'toId' attributes");
            throw new XmlPullParserException(var12.toString());
         }
      } else {
         var12 = new StringBuilder();
         var12.append(var3.getPositionDescription());
         var12.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
         throw new XmlPullParserException(var12.toString());
      }
   }

   private boolean selectTransition(int var1) {
      AnimatedStateListDrawableCompat.Transition var8 = this.mTransition;
      int var2;
      if (var8 != null) {
         if (var1 == this.mTransitionToIndex) {
            return true;
         }

         if (var1 == this.mTransitionFromIndex && var8.canReverse()) {
            var8.reverse();
            this.mTransitionToIndex = this.mTransitionFromIndex;
            this.mTransitionFromIndex = var1;
            return true;
         }

         var2 = this.mTransitionToIndex;
         var8.stop();
      } else {
         var2 = this.getCurrentIndex();
      }

      this.mTransition = null;
      this.mTransitionFromIndex = -1;
      this.mTransitionToIndex = -1;
      AnimatedStateListDrawableCompat.AnimatedStateListState var10 = this.mState;
      int var3 = var10.getKeyframeIdAt(var2);
      int var4 = var10.getKeyframeIdAt(var1);
      if (var4 != 0) {
         if (var3 == 0) {
            return false;
         } else {
            int var5 = var10.indexOfTransition(var3, var4);
            if (var5 < 0) {
               return false;
            } else {
               boolean var6 = var10.transitionHasReversibleFlag(var3, var4);
               this.selectDrawable(var5);
               Drawable var9 = this.getCurrent();
               Object var11;
               if (var9 instanceof AnimationDrawable) {
                  boolean var7 = var10.isTransitionReversed(var3, var4);
                  var11 = new AnimatedStateListDrawableCompat.AnimationDrawableTransition((AnimationDrawable)var9, var7, var6);
               } else if (var9 instanceof AnimatedVectorDrawableCompat) {
                  var11 = new AnimatedStateListDrawableCompat.AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat)var9);
               } else {
                  if (!(var9 instanceof Animatable)) {
                     return false;
                  }

                  var11 = new AnimatedStateListDrawableCompat.AnimatableTransition((Animatable)var9);
               }

               ((AnimatedStateListDrawableCompat.Transition)var11).start();
               this.mTransition = (AnimatedStateListDrawableCompat.Transition)var11;
               this.mTransitionFromIndex = var2;
               this.mTransitionToIndex = var1;
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private void updateStateFromTypedArray(TypedArray var1) {
      AnimatedStateListDrawableCompat.AnimatedStateListState var2 = this.mState;
      if (VERSION.SDK_INT >= 21) {
         var2.mChangingConfigurations |= var1.getChangingConfigurations();
      }

      var2.setVariablePadding(var1.getBoolean(styleable.AnimatedStateListDrawableCompat_android_variablePadding, var2.mVariablePadding));
      var2.setConstantSize(var1.getBoolean(styleable.AnimatedStateListDrawableCompat_android_constantSize, var2.mConstantSize));
      var2.setEnterFadeDuration(var1.getInt(styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, var2.mEnterFadeDuration));
      var2.setExitFadeDuration(var1.getInt(styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, var2.mExitFadeDuration));
      this.setDither(var1.getBoolean(styleable.AnimatedStateListDrawableCompat_android_dither, var2.mDither));
   }

   public void addState(int[] var1, Drawable var2, int var3) {
      if (var2 != null) {
         this.mState.addStateSet(var1, var2, var3);
         this.onStateChange(this.getState());
      } else {
         throw new IllegalArgumentException("Drawable must not be null");
      }
   }

   public void addTransition(int var1, int var2, Drawable var3, boolean var4) {
      if (var3 != null) {
         this.mState.addTransition(var1, var2, var3, var4);
      } else {
         throw new IllegalArgumentException("Transition drawable must not be null");
      }
   }

   void clearMutated() {
      super.clearMutated();
      this.mMutated = false;
   }

   AnimatedStateListDrawableCompat.AnimatedStateListState cloneConstantState() {
      return new AnimatedStateListDrawableCompat.AnimatedStateListState(this.mState, this, (Resources)null);
   }

   public void inflate(Context var1, Resources var2, XmlPullParser var3, AttributeSet var4, Theme var5) throws XmlPullParserException, IOException {
      TypedArray var6 = TypedArrayUtils.obtainAttributes(var2, var5, var4, styleable.AnimatedStateListDrawableCompat);
      this.setVisible(var6.getBoolean(styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
      this.updateStateFromTypedArray(var6);
      this.updateDensity(var2);
      var6.recycle();
      this.inflateChildElements(var1, var2, var3, var4, var5);
      this.init();
   }

   public boolean isStateful() {
      return true;
   }

   public void jumpToCurrentState() {
      super.jumpToCurrentState();
      AnimatedStateListDrawableCompat.Transition var1 = this.mTransition;
      if (var1 != null) {
         var1.stop();
         this.mTransition = null;
         this.selectDrawable(this.mTransitionToIndex);
         this.mTransitionToIndex = -1;
         this.mTransitionFromIndex = -1;
      }

   }

   public Drawable mutate() {
      if (!this.mMutated && super.mutate() == this) {
         this.mState.mutate();
         this.mMutated = true;
      }

      return this;
   }

   protected boolean onStateChange(int[] var1) {
      int var2 = this.mState.indexOfKeyframe(var1);
      boolean var3;
      if (var2 == this.getCurrentIndex() || !this.selectTransition(var2) && !this.selectDrawable(var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      Drawable var5 = this.getCurrent();
      boolean var4 = var3;
      if (var5 != null) {
         var4 = var3 | var5.setState(var1);
      }

      return var4;
   }

   void setConstantState(DrawableContainer.DrawableContainerState var1) {
      super.setConstantState(var1);
      if (var1 instanceof AnimatedStateListDrawableCompat.AnimatedStateListState) {
         this.mState = (AnimatedStateListDrawableCompat.AnimatedStateListState)var1;
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      boolean var3 = super.setVisible(var1, var2);
      if (this.mTransition != null && (var3 || var2)) {
         if (var1) {
            this.mTransition.start();
            return var3;
         }

         this.jumpToCurrentState();
      }

      return var3;
   }

   private static class AnimatableTransition extends AnimatedStateListDrawableCompat.Transition {
      // $FF: renamed from: mA android.graphics.drawable.Animatable
      private final Animatable field_42;

      AnimatableTransition(Animatable var1) {
         super(null);
         this.field_42 = var1;
      }

      public void start() {
         this.field_42.start();
      }

      public void stop() {
         this.field_42.stop();
      }
   }

   static class AnimatedStateListState extends StateListDrawable.StateListState {
      private static final long REVERSED_BIT = 4294967296L;
      private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
      SparseArrayCompat mStateIds;
      LongSparseArray mTransitions;

      AnimatedStateListState(AnimatedStateListDrawableCompat.AnimatedStateListState var1, AnimatedStateListDrawableCompat var2, Resources var3) {
         super(var1, var2, var3);
         if (var1 != null) {
            this.mTransitions = var1.mTransitions;
            this.mStateIds = var1.mStateIds;
         } else {
            this.mTransitions = new LongSparseArray();
            this.mStateIds = new SparseArrayCompat();
         }
      }

      private static long generateTransitionKey(int var0, int var1) {
         return (long)var0 << 32 | (long)var1;
      }

      int addStateSet(int[] var1, Drawable var2, int var3) {
         int var4 = super.addStateSet(var1, var2);
         this.mStateIds.put(var4, var3);
         return var4;
      }

      int addTransition(int var1, int var2, Drawable var3, boolean var4) {
         int var5 = super.addChild(var3);
         long var8 = generateTransitionKey(var1, var2);
         long var6 = 0L;
         if (var4) {
            var6 = 8589934592L;
         }

         this.mTransitions.append(var8, (long)var5 | var6);
         if (var4) {
            var8 = generateTransitionKey(var2, var1);
            this.mTransitions.append(var8, (long)var5 | 4294967296L | var6);
            return var5;
         } else {
            return var5;
         }
      }

      int getKeyframeIdAt(int var1) {
         return var1 < 0 ? 0 : (Integer)this.mStateIds.get(var1, 0);
      }

      int indexOfKeyframe(int[] var1) {
         int var2 = super.indexOfStateSet(var1);
         return var2 >= 0 ? var2 : super.indexOfStateSet(StateSet.WILD_CARD);
      }

      int indexOfTransition(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         return (int)(Long)this.mTransitions.get(var3, -1L);
      }

      boolean isTransitionReversed(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         return ((Long)this.mTransitions.get(var3, -1L) & 4294967296L) != 0L;
      }

      void mutate() {
         this.mTransitions = this.mTransitions.clone();
         this.mStateIds = this.mStateIds.clone();
      }

      public Drawable newDrawable() {
         return new AnimatedStateListDrawableCompat(this, (Resources)null);
      }

      public Drawable newDrawable(Resources var1) {
         return new AnimatedStateListDrawableCompat(this, var1);
      }

      boolean transitionHasReversibleFlag(int var1, int var2) {
         long var3 = generateTransitionKey(var1, var2);
         return ((Long)this.mTransitions.get(var3, -1L) & 8589934592L) != 0L;
      }
   }

   private static class AnimatedVectorDrawableTransition extends AnimatedStateListDrawableCompat.Transition {
      private final AnimatedVectorDrawableCompat mAvd;

      AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat var1) {
         super(null);
         this.mAvd = var1;
      }

      public void start() {
         this.mAvd.start();
      }

      public void stop() {
         this.mAvd.stop();
      }
   }

   private static class AnimationDrawableTransition extends AnimatedStateListDrawableCompat.Transition {
      private final ObjectAnimator mAnim;
      private final boolean mHasReversibleFlag;

      AnimationDrawableTransition(AnimationDrawable var1, boolean var2, boolean var3) {
         super(null);
         int var5 = var1.getNumberOfFrames();
         int var4;
         if (var2) {
            var4 = var5 - 1;
         } else {
            var4 = 0;
         }

         if (var2) {
            var5 = 0;
         } else {
            --var5;
         }

         AnimatedStateListDrawableCompat.FrameInterpolator var6 = new AnimatedStateListDrawableCompat.FrameInterpolator(var1, var2);
         ObjectAnimator var7 = ObjectAnimator.ofInt(var1, "currentIndex", new int[]{var4, var5});
         if (VERSION.SDK_INT >= 18) {
            var7.setAutoCancel(true);
         }

         var7.setDuration((long)var6.getTotalDuration());
         var7.setInterpolator(var6);
         this.mHasReversibleFlag = var3;
         this.mAnim = var7;
      }

      public boolean canReverse() {
         return this.mHasReversibleFlag;
      }

      public void reverse() {
         this.mAnim.reverse();
      }

      public void start() {
         this.mAnim.start();
      }

      public void stop() {
         this.mAnim.cancel();
      }
   }

   private static class FrameInterpolator implements TimeInterpolator {
      private int[] mFrameTimes;
      private int mFrames;
      private int mTotalDuration;

      FrameInterpolator(AnimationDrawable var1, boolean var2) {
         this.updateFrames(var1, var2);
      }

      public float getInterpolation(float var1) {
         int var3 = (int)((float)this.mTotalDuration * var1 + 0.5F);
         int var4 = this.mFrames;
         int[] var5 = this.mFrameTimes;

         int var2;
         for(var2 = 0; var2 < var4 && var3 >= var5[var2]; ++var2) {
            var3 -= var5[var2];
         }

         if (var2 < var4) {
            var1 = (float)var3 / (float)this.mTotalDuration;
         } else {
            var1 = 0.0F;
         }

         return (float)var2 / (float)var4 + var1;
      }

      int getTotalDuration() {
         return this.mTotalDuration;
      }

      int updateFrames(AnimationDrawable var1, boolean var2) {
         int var6 = var1.getNumberOfFrames();
         this.mFrames = var6;
         int[] var7 = this.mFrameTimes;
         if (var7 == null || var7.length < var6) {
            this.mFrameTimes = new int[var6];
         }

         var7 = this.mFrameTimes;
         int var4 = 0;

         for(int var3 = 0; var3 < var6; ++var3) {
            int var5;
            if (var2) {
               var5 = var6 - var3 - 1;
            } else {
               var5 = var3;
            }

            var5 = var1.getDuration(var5);
            var7[var3] = var5;
            var4 += var5;
         }

         this.mTotalDuration = var4;
         return var4;
      }
   }

   private abstract static class Transition {
      private Transition() {
      }

      // $FF: synthetic method
      Transition(Object var1) {
         this();
      }

      public boolean canReverse() {
         return false;
      }

      public void reverse() {
      }

      public abstract void start();

      public abstract void stop();
   }
}
