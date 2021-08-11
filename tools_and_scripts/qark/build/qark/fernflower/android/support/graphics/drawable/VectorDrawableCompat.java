package android.support.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat extends VectorDrawableCommon {
   private static final boolean DBG_VECTOR_DRAWABLE = false;
   static final Mode DEFAULT_TINT_MODE;
   private static final int LINECAP_BUTT = 0;
   private static final int LINECAP_ROUND = 1;
   private static final int LINECAP_SQUARE = 2;
   private static final int LINEJOIN_BEVEL = 2;
   private static final int LINEJOIN_MITER = 0;
   private static final int LINEJOIN_ROUND = 1;
   static final String LOGTAG = "VectorDrawableCompat";
   private static final int MAX_CACHED_BITMAP_SIZE = 2048;
   private static final String SHAPE_CLIP_PATH = "clip-path";
   private static final String SHAPE_GROUP = "group";
   private static final String SHAPE_PATH = "path";
   private static final String SHAPE_VECTOR = "vector";
   private boolean mAllowCaching = true;
   private ConstantState mCachedConstantStateDelegate;
   private ColorFilter mColorFilter;
   private boolean mMutated;
   private PorterDuffColorFilter mTintFilter;
   private final Rect mTmpBounds = new Rect();
   private final float[] mTmpFloats = new float[9];
   private final Matrix mTmpMatrix = new Matrix();
   private VectorDrawableCompat.VectorDrawableCompatState mVectorState;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   VectorDrawableCompat() {
      this.mVectorState = new VectorDrawableCompat.VectorDrawableCompatState();
   }

   VectorDrawableCompat(@NonNull VectorDrawableCompat.VectorDrawableCompatState var1) {
      this.mVectorState = var1;
      this.mTintFilter = this.updateTintFilter(this.mTintFilter, var1.mTint, var1.mTintMode);
   }

   static int applyAlpha(int var0, float var1) {
      return var0 & 16777215 | (int)((float)Color.alpha(var0) * var1) << 24;
   }

   @Nullable
   public static VectorDrawableCompat create(@NonNull Resources var0, @DrawableRes int var1, @Nullable Theme var2) {
      if (VERSION.SDK_INT >= 24) {
         VectorDrawableCompat var15 = new VectorDrawableCompat();
         var15.mDelegateDrawable = ResourcesCompat.getDrawable(var0, var1, var2);
         var15.mCachedConstantStateDelegate = new VectorDrawableCompat.VectorDrawableDelegateState(var15.mDelegateDrawable.getConstantState());
         return var15;
      } else {
         XmlPullParserException var16;
         label64: {
            IOException var10000;
            label51: {
               XmlResourceParser var3;
               AttributeSet var4;
               boolean var10001;
               try {
                  var3 = var0.getXml(var1);
                  var4 = Xml.asAttributeSet(var3);
               } catch (XmlPullParserException var11) {
                  var16 = var11;
                  var10001 = false;
                  break label64;
               } catch (IOException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label51;
               }

               while(true) {
                  try {
                     var1 = var3.next();
                  } catch (XmlPullParserException var9) {
                     var16 = var9;
                     var10001 = false;
                     break label64;
                  } catch (IOException var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }

                  if (var1 == 2 || var1 == 1) {
                     if (var1 == 2) {
                        try {
                           return createFromXmlInner(var0, var3, var4, var2);
                        } catch (XmlPullParserException var5) {
                           var16 = var5;
                           var10001 = false;
                           break label64;
                        } catch (IOException var6) {
                           var10000 = var6;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           throw new XmlPullParserException("No start tag found");
                        } catch (XmlPullParserException var7) {
                           var16 = var7;
                           var10001 = false;
                           break label64;
                        } catch (IOException var8) {
                           var10000 = var8;
                           var10001 = false;
                           break;
                        }
                     }
                  }
               }
            }

            IOException var13 = var10000;
            Log.e("VectorDrawableCompat", "parser error", var13);
            return null;
         }

         XmlPullParserException var14 = var16;
         Log.e("VectorDrawableCompat", "parser error", var14);
         return null;
      }
   }

   public static VectorDrawableCompat createFromXmlInner(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws XmlPullParserException, IOException {
      VectorDrawableCompat var4 = new VectorDrawableCompat();
      var4.inflate(var0, var1, var2, var3);
      return var4;
   }

   private void inflateInternal(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      VectorDrawableCompat.VectorDrawableCompatState var9 = this.mVectorState;
      VectorDrawableCompat.VPathRenderer var10 = var9.mVPathRenderer;
      boolean var5 = true;
      Stack var11 = new Stack();
      var11.push(var10.mRootGroup);
      int var7 = var2.getEventType();

      boolean var6;
      for(int var8 = var2.getDepth(); var7 != 1 && (var2.getDepth() >= var8 + 1 || var7 != 3); var5 = var6) {
         if (var7 == 2) {
            String var13 = var2.getName();
            VectorDrawableCompat.VGroup var12 = (VectorDrawableCompat.VGroup)var11.peek();
            if ("path".equals(var13)) {
               VectorDrawableCompat.VFullPath var16 = new VectorDrawableCompat.VFullPath();
               var16.inflate(var1, var3, var4, var2);
               var12.mChildren.add(var16);
               if (var16.getPathName() != null) {
                  var10.mVGTargetsMap.put(var16.getPathName(), var16);
               }

               var6 = false;
               var9.mChangingConfigurations |= var16.mChangingConfigurations;
            } else {
               label52: {
                  if ("clip-path".equals(var13)) {
                     VectorDrawableCompat.VClipPath var17 = new VectorDrawableCompat.VClipPath();
                     var17.inflate(var1, var3, var4, var2);
                     var12.mChildren.add(var17);
                     if (var17.getPathName() != null) {
                        var10.mVGTargetsMap.put(var17.getPathName(), var17);
                     }

                     var9.mChangingConfigurations |= var17.mChangingConfigurations;
                  } else if ("group".equals(var13)) {
                     VectorDrawableCompat.VGroup var18 = new VectorDrawableCompat.VGroup();
                     var18.inflate(var1, var3, var4, var2);
                     var12.mChildren.add(var18);
                     var11.push(var18);
                     if (var18.getGroupName() != null) {
                        var10.mVGTargetsMap.put(var18.getGroupName(), var18);
                     }

                     var9.mChangingConfigurations |= var18.mChangingConfigurations;
                     var6 = var5;
                     break label52;
                  }

                  var6 = var5;
               }
            }
         } else {
            var6 = var5;
            if (var7 == 3) {
               var6 = var5;
               if ("group".equals(var2.getName())) {
                  var11.pop();
                  var6 = var5;
               }
            }
         }

         var7 = var2.next();
      }

      if (var5) {
         StringBuffer var14 = new StringBuffer();
         if (var14.length() > 0) {
            var14.append(" or ");
         }

         var14.append("path");
         StringBuilder var15 = new StringBuilder();
         var15.append("no ");
         var15.append(var14);
         var15.append(" defined");
         throw new XmlPullParserException(var15.toString());
      }
   }

   private boolean needMirroring() {
      if (VERSION.SDK_INT >= 17) {
         return this.isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1;
      } else {
         return false;
      }
   }

   private static Mode parseTintModeCompat(int var0, Mode var1) {
      if (var0 != 3) {
         if (var0 != 5) {
            if (var0 != 9) {
               switch(var0) {
               case 14:
                  return Mode.MULTIPLY;
               case 15:
                  return Mode.SCREEN;
               case 16:
                  if (VERSION.SDK_INT >= 11) {
                     return Mode.ADD;
                  }

                  return var1;
               default:
                  return var1;
               }
            } else {
               return Mode.SRC_ATOP;
            }
         } else {
            return Mode.SRC_IN;
         }
      } else {
         return Mode.SRC_OVER;
      }
   }

   private void printGroupTree(VectorDrawableCompat.VGroup var1, int var2) {
      String var4 = "";

      int var3;
      StringBuilder var5;
      for(var3 = 0; var3 < var2; ++var3) {
         var5 = new StringBuilder();
         var5.append(var4);
         var5.append("    ");
         var4 = var5.toString();
      }

      var5 = new StringBuilder();
      var5.append(var4);
      var5.append("current group is :");
      var5.append(var1.getGroupName());
      var5.append(" rotation is ");
      var5.append(var1.mRotate);
      Log.v("VectorDrawableCompat", var5.toString());
      var5 = new StringBuilder();
      var5.append(var4);
      var5.append("matrix is :");
      var5.append(var1.getLocalMatrix().toString());
      Log.v("VectorDrawableCompat", var5.toString());

      for(var3 = 0; var3 < var1.mChildren.size(); ++var3) {
         Object var6 = var1.mChildren.get(var3);
         if (var6 instanceof VectorDrawableCompat.VGroup) {
            this.printGroupTree((VectorDrawableCompat.VGroup)var6, var2 + 1);
         } else {
            ((VectorDrawableCompat.VPath)var6).printVPath(var2 + 1);
         }
      }

   }

   private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) throws XmlPullParserException {
      VectorDrawableCompat.VectorDrawableCompatState var4 = this.mVectorState;
      VectorDrawableCompat.VPathRenderer var3 = var4.mVPathRenderer;
      var4.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(var1, var2, "tintMode", 6, -1), Mode.SRC_IN);
      ColorStateList var5 = var1.getColorStateList(1);
      if (var5 != null) {
         var4.mTint = var5;
      }

      var4.mAutoMirrored = TypedArrayUtils.getNamedBoolean(var1, var2, "autoMirrored", 5, var4.mAutoMirrored);
      var3.mViewportWidth = TypedArrayUtils.getNamedFloat(var1, var2, "viewportWidth", 7, var3.mViewportWidth);
      var3.mViewportHeight = TypedArrayUtils.getNamedFloat(var1, var2, "viewportHeight", 8, var3.mViewportHeight);
      StringBuilder var7;
      if (var3.mViewportWidth > 0.0F) {
         if (var3.mViewportHeight > 0.0F) {
            var3.mBaseWidth = var1.getDimension(3, var3.mBaseWidth);
            var3.mBaseHeight = var1.getDimension(2, var3.mBaseHeight);
            if (var3.mBaseWidth > 0.0F) {
               if (var3.mBaseHeight > 0.0F) {
                  var3.setAlpha(TypedArrayUtils.getNamedFloat(var1, var2, "alpha", 4, var3.getAlpha()));
                  String var6 = var1.getString(0);
                  if (var6 != null) {
                     var3.mRootName = var6;
                     var3.mVGTargetsMap.put(var6, var3);
                  }

               } else {
                  var7 = new StringBuilder();
                  var7.append(var1.getPositionDescription());
                  var7.append("<vector> tag requires height > 0");
                  throw new XmlPullParserException(var7.toString());
               }
            } else {
               var7 = new StringBuilder();
               var7.append(var1.getPositionDescription());
               var7.append("<vector> tag requires width > 0");
               throw new XmlPullParserException(var7.toString());
            }
         } else {
            var7 = new StringBuilder();
            var7.append(var1.getPositionDescription());
            var7.append("<vector> tag requires viewportHeight > 0");
            throw new XmlPullParserException(var7.toString());
         }
      } else {
         var7 = new StringBuilder();
         var7.append(var1.getPositionDescription());
         var7.append("<vector> tag requires viewportWidth > 0");
         throw new XmlPullParserException(var7.toString());
      }
   }

   public boolean canApplyTheme() {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.canApplyTheme(this.mDelegateDrawable);
      }

      return false;
   }

   public void draw(Canvas var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.draw(var1);
      } else {
         this.copyBounds(this.mTmpBounds);
         if (this.mTmpBounds.width() > 0) {
            if (this.mTmpBounds.height() > 0) {
               ColorFilter var10 = this.mColorFilter;
               Object var9 = var10;
               if (var10 == null) {
                  var9 = this.mTintFilter;
               }

               var1.getMatrix(this.mTmpMatrix);
               this.mTmpMatrix.getValues(this.mTmpFloats);
               float var2 = Math.abs(this.mTmpFloats[0]);
               float var3 = Math.abs(this.mTmpFloats[4]);
               float var5 = Math.abs(this.mTmpFloats[1]);
               float var4 = Math.abs(this.mTmpFloats[3]);
               if (var5 != 0.0F || var4 != 0.0F) {
                  var2 = 1.0F;
                  var3 = 1.0F;
               }

               int var6 = (int)((float)this.mTmpBounds.width() * var2);
               int var7 = (int)((float)this.mTmpBounds.height() * var3);
               var6 = Math.min(2048, var6);
               var7 = Math.min(2048, var7);
               if (var6 > 0) {
                  if (var7 > 0) {
                     int var8 = var1.save();
                     var1.translate((float)this.mTmpBounds.left, (float)this.mTmpBounds.top);
                     if (this.needMirroring()) {
                        var1.translate((float)this.mTmpBounds.width(), 0.0F);
                        var1.scale(-1.0F, 1.0F);
                     }

                     this.mTmpBounds.offsetTo(0, 0);
                     this.mVectorState.createCachedBitmapIfNeeded(var6, var7);
                     if (!this.mAllowCaching) {
                        this.mVectorState.updateCachedBitmap(var6, var7);
                     } else if (!this.mVectorState.canReuseCache()) {
                        this.mVectorState.updateCachedBitmap(var6, var7);
                        this.mVectorState.updateCacheStates();
                     }

                     this.mVectorState.drawCachedBitmapWithRootAlpha(var1, (ColorFilter)var9, this.mTmpBounds);
                     var1.restoreToCount(var8);
                  }
               }
            }
         }
      }
   }

   public int getAlpha() {
      return this.mDelegateDrawable != null ? DrawableCompat.getAlpha(this.mDelegateDrawable) : this.mVectorState.mVPathRenderer.getRootAlpha();
   }

   public int getChangingConfigurations() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getChangingConfigurations() : super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      if (this.mDelegateDrawable != null && VERSION.SDK_INT >= 24) {
         return new VectorDrawableCompat.VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
      } else {
         this.mVectorState.mChangingConfigurations = this.getChangingConfigurations();
         return this.mVectorState;
      }
   }

   public int getIntrinsicHeight() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicHeight() : (int)this.mVectorState.mVPathRenderer.mBaseHeight;
   }

   public int getIntrinsicWidth() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getIntrinsicWidth() : (int)this.mVectorState.mVPathRenderer.mBaseWidth;
   }

   public int getOpacity() {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.getOpacity() : -3;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public float getPixelSize() {
      VectorDrawableCompat.VectorDrawableCompatState var5 = this.mVectorState;
      if (var5 != null && var5.mVPathRenderer != null && this.mVectorState.mVPathRenderer.mBaseWidth != 0.0F && this.mVectorState.mVPathRenderer.mBaseHeight != 0.0F && this.mVectorState.mVPathRenderer.mViewportHeight != 0.0F && this.mVectorState.mVPathRenderer.mViewportWidth != 0.0F) {
         float var1 = this.mVectorState.mVPathRenderer.mBaseWidth;
         float var2 = this.mVectorState.mVPathRenderer.mBaseHeight;
         float var3 = this.mVectorState.mVPathRenderer.mViewportWidth;
         float var4 = this.mVectorState.mVPathRenderer.mViewportHeight;
         return Math.min(var3 / var1, var4 / var2);
      } else {
         return 1.0F;
      }
   }

   Object getTargetByName(String var1) {
      return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(var1);
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.inflate(var1, var2, var3);
      } else {
         this.inflate(var1, var2, var3, (Theme)null);
      }
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws XmlPullParserException, IOException {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.inflate(this.mDelegateDrawable, var1, var2, var3, var4);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var5 = this.mVectorState;
         var5.mVPathRenderer = new VectorDrawableCompat.VPathRenderer();
         TypedArray var6 = TypedArrayUtils.obtainAttributes(var1, var4, var3, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
         this.updateStateFromTypedArray(var6, var2);
         var6.recycle();
         var5.mChangingConfigurations = this.getChangingConfigurations();
         var5.mCacheDirty = true;
         this.inflateInternal(var1, var2, var3, var4);
         this.mTintFilter = this.updateTintFilter(this.mTintFilter, var5.mTint, var5.mTintMode);
      }
   }

   public void invalidateSelf() {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.invalidateSelf();
      } else {
         super.invalidateSelf();
      }
   }

   public boolean isAutoMirrored() {
      return this.mDelegateDrawable != null ? DrawableCompat.isAutoMirrored(this.mDelegateDrawable) : this.mVectorState.mAutoMirrored;
   }

   public boolean isStateful() {
      if (this.mDelegateDrawable != null) {
         return this.mDelegateDrawable.isStateful();
      } else {
         if (!super.isStateful()) {
            VectorDrawableCompat.VectorDrawableCompatState var1 = this.mVectorState;
            if (var1 == null || var1.mTint == null || !this.mVectorState.mTint.isStateful()) {
               return false;
            }
         }

         return true;
      }
   }

   public Drawable mutate() {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.mutate();
         return this;
      } else {
         if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableCompat.VectorDrawableCompatState(this.mVectorState);
            this.mMutated = true;
         }

         return this;
      }
   }

   protected void onBoundsChange(Rect var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setBounds(var1);
      }

   }

   protected boolean onStateChange(int[] var1) {
      if (this.mDelegateDrawable != null) {
         return this.mDelegateDrawable.setState(var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var2 = this.mVectorState;
         if (var2.mTint != null && var2.mTintMode != null) {
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var2.mTint, var2.mTintMode);
            this.invalidateSelf();
            return true;
         } else {
            return false;
         }
      }
   }

   public void scheduleSelf(Runnable var1, long var2) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.scheduleSelf(var1, var2);
      } else {
         super.scheduleSelf(var1, var2);
      }
   }

   void setAllowCaching(boolean var1) {
      this.mAllowCaching = var1;
   }

   public void setAlpha(int var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setAlpha(var1);
      } else {
         if (this.mVectorState.mVPathRenderer.getRootAlpha() != var1) {
            this.mVectorState.mVPathRenderer.setRootAlpha(var1);
            this.invalidateSelf();
         }

      }
   }

   public void setAutoMirrored(boolean var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setAutoMirrored(this.mDelegateDrawable, var1);
      } else {
         this.mVectorState.mAutoMirrored = var1;
      }
   }

   public void setColorFilter(ColorFilter var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.setColorFilter(var1);
      } else {
         this.mColorFilter = var1;
         this.invalidateSelf();
      }
   }

   public void setTint(int var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTint(this.mDelegateDrawable, var1);
      } else {
         this.setTintList(ColorStateList.valueOf(var1));
      }
   }

   public void setTintList(ColorStateList var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTintList(this.mDelegateDrawable, var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var2 = this.mVectorState;
         if (var2.mTint != var1) {
            var2.mTint = var1;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var1, var2.mTintMode);
            this.invalidateSelf();
         }

      }
   }

   public void setTintMode(Mode var1) {
      if (this.mDelegateDrawable != null) {
         DrawableCompat.setTintMode(this.mDelegateDrawable, var1);
      } else {
         VectorDrawableCompat.VectorDrawableCompatState var2 = this.mVectorState;
         if (var2.mTintMode != var1) {
            var2.mTintMode = var1;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, var2.mTint, var1);
            this.invalidateSelf();
         }

      }
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return this.mDelegateDrawable != null ? this.mDelegateDrawable.setVisible(var1, var2) : super.setVisible(var1, var2);
   }

   public void unscheduleSelf(Runnable var1) {
      if (this.mDelegateDrawable != null) {
         this.mDelegateDrawable.unscheduleSelf(var1);
      } else {
         super.unscheduleSelf(var1);
      }
   }

   PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter var1, ColorStateList var2, Mode var3) {
      return var2 != null && var3 != null ? new PorterDuffColorFilter(var2.getColorForState(this.getState(), 0), var3) : null;
   }

   private static class VClipPath extends VectorDrawableCompat.VPath {
      public VClipPath() {
      }

      public VClipPath(VectorDrawableCompat.VClipPath var1) {
         super(var1);
      }

      private void updateStateFromTypedArray(TypedArray var1) {
         String var2 = var1.getString(0);
         if (var2 != null) {
            this.mPathName = var2;
         }

         String var3 = var1.getString(1);
         if (var3 != null) {
            this.mNodes = PathParser.createNodesFromPathData(var3);
         }

      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         if (TypedArrayUtils.hasAttribute(var4, "pathData")) {
            TypedArray var5 = TypedArrayUtils.obtainAttributes(var1, var3, var2, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
            this.updateStateFromTypedArray(var5);
            var5.recycle();
         }
      }

      public boolean isClipPath() {
         return true;
      }
   }

   private static class VFullPath extends VectorDrawableCompat.VPath {
      float mFillAlpha = 1.0F;
      int mFillColor = 0;
      int mFillRule = 0;
      float mStrokeAlpha = 1.0F;
      int mStrokeColor = 0;
      Cap mStrokeLineCap;
      Join mStrokeLineJoin;
      float mStrokeMiterlimit;
      float mStrokeWidth = 0.0F;
      private int[] mThemeAttrs;
      float mTrimPathEnd = 1.0F;
      float mTrimPathOffset = 0.0F;
      float mTrimPathStart = 0.0F;

      public VFullPath() {
         this.mStrokeLineCap = Cap.BUTT;
         this.mStrokeLineJoin = Join.MITER;
         this.mStrokeMiterlimit = 4.0F;
      }

      public VFullPath(VectorDrawableCompat.VFullPath var1) {
         super(var1);
         this.mStrokeLineCap = Cap.BUTT;
         this.mStrokeLineJoin = Join.MITER;
         this.mStrokeMiterlimit = 4.0F;
         this.mThemeAttrs = var1.mThemeAttrs;
         this.mStrokeColor = var1.mStrokeColor;
         this.mStrokeWidth = var1.mStrokeWidth;
         this.mStrokeAlpha = var1.mStrokeAlpha;
         this.mFillColor = var1.mFillColor;
         this.mFillRule = var1.mFillRule;
         this.mFillAlpha = var1.mFillAlpha;
         this.mTrimPathStart = var1.mTrimPathStart;
         this.mTrimPathEnd = var1.mTrimPathEnd;
         this.mTrimPathOffset = var1.mTrimPathOffset;
         this.mStrokeLineCap = var1.mStrokeLineCap;
         this.mStrokeLineJoin = var1.mStrokeLineJoin;
         this.mStrokeMiterlimit = var1.mStrokeMiterlimit;
      }

      private Cap getStrokeLineCap(int var1, Cap var2) {
         if (var1 != 0) {
            if (var1 != 1) {
               return var1 != 2 ? var2 : Cap.SQUARE;
            } else {
               return Cap.ROUND;
            }
         } else {
            return Cap.BUTT;
         }
      }

      private Join getStrokeLineJoin(int var1, Join var2) {
         if (var1 != 0) {
            if (var1 != 1) {
               return var1 != 2 ? var2 : Join.BEVEL;
            } else {
               return Join.ROUND;
            }
         } else {
            return Join.MITER;
         }
      }

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) {
         this.mThemeAttrs = null;
         if (TypedArrayUtils.hasAttribute(var2, "pathData")) {
            String var3 = var1.getString(0);
            if (var3 != null) {
               this.mPathName = var3;
            }

            var3 = var1.getString(2);
            if (var3 != null) {
               this.mNodes = PathParser.createNodesFromPathData(var3);
            }

            this.mFillColor = TypedArrayUtils.getNamedColor(var1, var2, "fillColor", 1, this.mFillColor);
            this.mFillAlpha = TypedArrayUtils.getNamedFloat(var1, var2, "fillAlpha", 12, this.mFillAlpha);
            this.mStrokeLineCap = this.getStrokeLineCap(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineCap", 8, -1), this.mStrokeLineCap);
            this.mStrokeLineJoin = this.getStrokeLineJoin(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
            this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(var1, var2, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
            this.mStrokeColor = TypedArrayUtils.getNamedColor(var1, var2, "strokeColor", 3, this.mStrokeColor);
            this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(var1, var2, "strokeAlpha", 11, this.mStrokeAlpha);
            this.mStrokeWidth = TypedArrayUtils.getNamedFloat(var1, var2, "strokeWidth", 4, this.mStrokeWidth);
            this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathEnd", 6, this.mTrimPathEnd);
            this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathOffset", 7, this.mTrimPathOffset);
            this.mTrimPathStart = TypedArrayUtils.getNamedFloat(var1, var2, "trimPathStart", 5, this.mTrimPathStart);
            this.mFillRule = TypedArrayUtils.getNamedInt(var1, var2, "fillType", 13, this.mFillRule);
         }
      }

      public void applyTheme(Theme var1) {
         if (this.mThemeAttrs != null) {
            ;
         }
      }

      public boolean canApplyTheme() {
         return this.mThemeAttrs != null;
      }

      float getFillAlpha() {
         return this.mFillAlpha;
      }

      int getFillColor() {
         return this.mFillColor;
      }

      float getStrokeAlpha() {
         return this.mStrokeAlpha;
      }

      int getStrokeColor() {
         return this.mStrokeColor;
      }

      float getStrokeWidth() {
         return this.mStrokeWidth;
      }

      float getTrimPathEnd() {
         return this.mTrimPathEnd;
      }

      float getTrimPathOffset() {
         return this.mTrimPathOffset;
      }

      float getTrimPathStart() {
         return this.mTrimPathStart;
      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         TypedArray var5 = TypedArrayUtils.obtainAttributes(var1, var3, var2, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
         this.updateStateFromTypedArray(var5, var4);
         var5.recycle();
      }

      void setFillAlpha(float var1) {
         this.mFillAlpha = var1;
      }

      void setFillColor(int var1) {
         this.mFillColor = var1;
      }

      void setStrokeAlpha(float var1) {
         this.mStrokeAlpha = var1;
      }

      void setStrokeColor(int var1) {
         this.mStrokeColor = var1;
      }

      void setStrokeWidth(float var1) {
         this.mStrokeWidth = var1;
      }

      void setTrimPathEnd(float var1) {
         this.mTrimPathEnd = var1;
      }

      void setTrimPathOffset(float var1) {
         this.mTrimPathOffset = var1;
      }

      void setTrimPathStart(float var1) {
         this.mTrimPathStart = var1;
      }
   }

   private static class VGroup {
      int mChangingConfigurations;
      final ArrayList mChildren = new ArrayList();
      private String mGroupName = null;
      private final Matrix mLocalMatrix = new Matrix();
      private float mPivotX = 0.0F;
      private float mPivotY = 0.0F;
      float mRotate = 0.0F;
      private float mScaleX = 1.0F;
      private float mScaleY = 1.0F;
      private final Matrix mStackedMatrix = new Matrix();
      private int[] mThemeAttrs;
      private float mTranslateX = 0.0F;
      private float mTranslateY = 0.0F;

      public VGroup() {
      }

      public VGroup(VectorDrawableCompat.VGroup var1, ArrayMap var2) {
         this.mRotate = var1.mRotate;
         this.mPivotX = var1.mPivotX;
         this.mPivotY = var1.mPivotY;
         this.mScaleX = var1.mScaleX;
         this.mScaleY = var1.mScaleY;
         this.mTranslateX = var1.mTranslateX;
         this.mTranslateY = var1.mTranslateY;
         this.mThemeAttrs = var1.mThemeAttrs;
         this.mGroupName = var1.mGroupName;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         String var4 = this.mGroupName;
         if (var4 != null) {
            var2.put(var4, this);
         }

         this.mLocalMatrix.set(var1.mLocalMatrix);
         ArrayList var6 = var1.mChildren;

         for(int var3 = 0; var3 < var6.size(); ++var3) {
            Object var5 = var6.get(var3);
            if (var5 instanceof VectorDrawableCompat.VGroup) {
               var1 = (VectorDrawableCompat.VGroup)var5;
               this.mChildren.add(new VectorDrawableCompat.VGroup(var1, var2));
            } else {
               if (var5 instanceof VectorDrawableCompat.VFullPath) {
                  var5 = new VectorDrawableCompat.VFullPath((VectorDrawableCompat.VFullPath)var5);
               } else {
                  if (!(var5 instanceof VectorDrawableCompat.VClipPath)) {
                     throw new IllegalStateException("Unknown object in the tree!");
                  }

                  var5 = new VectorDrawableCompat.VClipPath((VectorDrawableCompat.VClipPath)var5);
               }

               this.mChildren.add(var5);
               if (((VectorDrawableCompat.VPath)var5).mPathName != null) {
                  var2.put(((VectorDrawableCompat.VPath)var5).mPathName, var5);
               }
            }
         }

      }

      private void updateLocalMatrix() {
         this.mLocalMatrix.reset();
         this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
         this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
         this.mLocalMatrix.postRotate(this.mRotate, 0.0F, 0.0F);
         this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
      }

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) {
         this.mThemeAttrs = null;
         this.mRotate = TypedArrayUtils.getNamedFloat(var1, var2, "rotation", 5, this.mRotate);
         this.mPivotX = var1.getFloat(1, this.mPivotX);
         this.mPivotY = var1.getFloat(2, this.mPivotY);
         this.mScaleX = TypedArrayUtils.getNamedFloat(var1, var2, "scaleX", 3, this.mScaleX);
         this.mScaleY = TypedArrayUtils.getNamedFloat(var1, var2, "scaleY", 4, this.mScaleY);
         this.mTranslateX = TypedArrayUtils.getNamedFloat(var1, var2, "translateX", 6, this.mTranslateX);
         this.mTranslateY = TypedArrayUtils.getNamedFloat(var1, var2, "translateY", 7, this.mTranslateY);
         String var3 = var1.getString(0);
         if (var3 != null) {
            this.mGroupName = var3;
         }

         this.updateLocalMatrix();
      }

      public String getGroupName() {
         return this.mGroupName;
      }

      public Matrix getLocalMatrix() {
         return this.mLocalMatrix;
      }

      public float getPivotX() {
         return this.mPivotX;
      }

      public float getPivotY() {
         return this.mPivotY;
      }

      public float getRotation() {
         return this.mRotate;
      }

      public float getScaleX() {
         return this.mScaleX;
      }

      public float getScaleY() {
         return this.mScaleY;
      }

      public float getTranslateX() {
         return this.mTranslateX;
      }

      public float getTranslateY() {
         return this.mTranslateY;
      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         TypedArray var5 = TypedArrayUtils.obtainAttributes(var1, var3, var2, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
         this.updateStateFromTypedArray(var5, var4);
         var5.recycle();
      }

      public void setPivotX(float var1) {
         if (var1 != this.mPivotX) {
            this.mPivotX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setPivotY(float var1) {
         if (var1 != this.mPivotY) {
            this.mPivotY = var1;
            this.updateLocalMatrix();
         }

      }

      public void setRotation(float var1) {
         if (var1 != this.mRotate) {
            this.mRotate = var1;
            this.updateLocalMatrix();
         }

      }

      public void setScaleX(float var1) {
         if (var1 != this.mScaleX) {
            this.mScaleX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setScaleY(float var1) {
         if (var1 != this.mScaleY) {
            this.mScaleY = var1;
            this.updateLocalMatrix();
         }

      }

      public void setTranslateX(float var1) {
         if (var1 != this.mTranslateX) {
            this.mTranslateX = var1;
            this.updateLocalMatrix();
         }

      }

      public void setTranslateY(float var1) {
         if (var1 != this.mTranslateY) {
            this.mTranslateY = var1;
            this.updateLocalMatrix();
         }

      }
   }

   private static class VPath {
      int mChangingConfigurations;
      protected PathParser.PathDataNode[] mNodes = null;
      String mPathName;

      public VPath() {
      }

      public VPath(VectorDrawableCompat.VPath var1) {
         this.mPathName = var1.mPathName;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mNodes = PathParser.deepCopyNodes(var1.mNodes);
      }

      public void applyTheme(Theme var1) {
      }

      public boolean canApplyTheme() {
         return false;
      }

      public PathParser.PathDataNode[] getPathData() {
         return this.mNodes;
      }

      public String getPathName() {
         return this.mPathName;
      }

      public boolean isClipPath() {
         return false;
      }

      public String nodesToString(PathParser.PathDataNode[] var1) {
         String var4 = " ";

         for(int var2 = 0; var2 < var1.length; ++var2) {
            StringBuilder var5 = new StringBuilder();
            var5.append(var4);
            var5.append(var1[var2].mType);
            var5.append(":");
            var4 = var5.toString();
            float[] var7 = var1[var2].mParams;

            for(int var3 = 0; var3 < var7.length; ++var3) {
               StringBuilder var6 = new StringBuilder();
               var6.append(var4);
               var6.append(var7[var3]);
               var6.append(",");
               var4 = var6.toString();
            }
         }

         return var4;
      }

      public void printVPath(int var1) {
         String var3 = "";

         StringBuilder var4;
         for(int var2 = 0; var2 < var1; ++var2) {
            var4 = new StringBuilder();
            var4.append(var3);
            var4.append("    ");
            var3 = var4.toString();
         }

         var4 = new StringBuilder();
         var4.append(var3);
         var4.append("current path is :");
         var4.append(this.mPathName);
         var4.append(" pathData is ");
         var4.append(this.nodesToString(this.mNodes));
         Log.v("VectorDrawableCompat", var4.toString());
      }

      public void setPathData(PathParser.PathDataNode[] var1) {
         if (!PathParser.canMorph(this.mNodes, var1)) {
            this.mNodes = PathParser.deepCopyNodes(var1);
         } else {
            PathParser.updateNodes(this.mNodes, var1);
         }
      }

      public void toPath(Path var1) {
         var1.reset();
         PathParser.PathDataNode[] var2 = this.mNodes;
         if (var2 != null) {
            PathParser.PathDataNode.nodesToPath(var2, var1);
         }

      }
   }

   private static class VPathRenderer {
      private static final Matrix IDENTITY_MATRIX = new Matrix();
      float mBaseHeight = 0.0F;
      float mBaseWidth = 0.0F;
      private int mChangingConfigurations;
      private Paint mFillPaint;
      private final Matrix mFinalPathMatrix = new Matrix();
      private final Path mPath;
      private PathMeasure mPathMeasure;
      private final Path mRenderPath;
      int mRootAlpha = 255;
      final VectorDrawableCompat.VGroup mRootGroup;
      String mRootName = null;
      private Paint mStrokePaint;
      final ArrayMap mVGTargetsMap = new ArrayMap();
      float mViewportHeight = 0.0F;
      float mViewportWidth = 0.0F;

      public VPathRenderer() {
         this.mRootGroup = new VectorDrawableCompat.VGroup();
         this.mPath = new Path();
         this.mRenderPath = new Path();
      }

      public VPathRenderer(VectorDrawableCompat.VPathRenderer var1) {
         this.mRootGroup = new VectorDrawableCompat.VGroup(var1.mRootGroup, this.mVGTargetsMap);
         this.mPath = new Path(var1.mPath);
         this.mRenderPath = new Path(var1.mRenderPath);
         this.mBaseWidth = var1.mBaseWidth;
         this.mBaseHeight = var1.mBaseHeight;
         this.mViewportWidth = var1.mViewportWidth;
         this.mViewportHeight = var1.mViewportHeight;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mRootAlpha = var1.mRootAlpha;
         this.mRootName = var1.mRootName;
         String var2 = var1.mRootName;
         if (var2 != null) {
            this.mVGTargetsMap.put(var2, this);
         }

      }

      private static float cross(float var0, float var1, float var2, float var3) {
         return var0 * var3 - var1 * var2;
      }

      private void drawGroupTree(VectorDrawableCompat.VGroup var1, Matrix var2, Canvas var3, int var4, int var5, ColorFilter var6) {
         var1.mStackedMatrix.set(var2);
         var1.mStackedMatrix.preConcat(var1.mLocalMatrix);
         var3.save();

         for(int var7 = 0; var7 < var1.mChildren.size(); ++var7) {
            Object var8 = var1.mChildren.get(var7);
            if (var8 instanceof VectorDrawableCompat.VGroup) {
               this.drawGroupTree((VectorDrawableCompat.VGroup)var8, var1.mStackedMatrix, var3, var4, var5, var6);
            } else if (var8 instanceof VectorDrawableCompat.VPath) {
               this.drawPath(var1, (VectorDrawableCompat.VPath)var8, var3, var4, var5, var6);
            }
         }

         var3.restore();
      }

      private void drawPath(VectorDrawableCompat.VGroup var1, VectorDrawableCompat.VPath var2, Canvas var3, int var4, int var5, ColorFilter var6) {
         float var8 = (float)var4 / this.mViewportWidth;
         float var9 = (float)var5 / this.mViewportHeight;
         float var7 = Math.min(var8, var9);
         Matrix var16 = var1.mStackedMatrix;
         this.mFinalPathMatrix.set(var16);
         this.mFinalPathMatrix.postScale(var8, var9);
         var8 = this.getMatrixScale(var16);
         if (var8 != 0.0F) {
            var2.toPath(this.mPath);
            Path var17 = this.mPath;
            this.mRenderPath.reset();
            if (var2.isClipPath()) {
               this.mRenderPath.addPath(var17, this.mFinalPathMatrix);
               var3.clipPath(this.mRenderPath);
            } else {
               VectorDrawableCompat.VFullPath var19 = (VectorDrawableCompat.VFullPath)var2;
               if (var19.mTrimPathStart != 0.0F || var19.mTrimPathEnd != 1.0F) {
                  float var12 = var19.mTrimPathStart;
                  float var13 = var19.mTrimPathOffset;
                  float var10 = var19.mTrimPathEnd;
                  float var11 = var19.mTrimPathOffset;
                  if (this.mPathMeasure == null) {
                     this.mPathMeasure = new PathMeasure();
                  }

                  this.mPathMeasure.setPath(this.mPath, false);
                  var9 = this.mPathMeasure.getLength();
                  var12 = (var12 + var13) % 1.0F * var9;
                  var10 = (var10 + var11) % 1.0F * var9;
                  var17.reset();
                  if (var12 > var10) {
                     this.mPathMeasure.getSegment(var12, var9, var17, true);
                     this.mPathMeasure.getSegment(0.0F, var10, var17, true);
                  } else {
                     this.mPathMeasure.getSegment(var12, var10, var17, true);
                  }

                  var17.rLineTo(0.0F, 0.0F);
               }

               this.mRenderPath.addPath(var17, this.mFinalPathMatrix);
               if (var19.mFillColor != 0) {
                  if (this.mFillPaint == null) {
                     this.mFillPaint = new Paint();
                     this.mFillPaint.setStyle(Style.FILL);
                     this.mFillPaint.setAntiAlias(true);
                  }

                  Paint var14 = this.mFillPaint;
                  var14.setColor(VectorDrawableCompat.applyAlpha(var19.mFillColor, var19.mFillAlpha));
                  var14.setColorFilter(var6);
                  Path var15 = this.mRenderPath;
                  FillType var18;
                  if (var19.mFillRule == 0) {
                     var18 = FillType.WINDING;
                  } else {
                     var18 = FillType.EVEN_ODD;
                  }

                  var15.setFillType(var18);
                  var3.drawPath(this.mRenderPath, var14);
               }

               if (var19.mStrokeColor != 0) {
                  if (this.mStrokePaint == null) {
                     this.mStrokePaint = new Paint();
                     this.mStrokePaint.setStyle(Style.STROKE);
                     this.mStrokePaint.setAntiAlias(true);
                  }

                  Paint var20 = this.mStrokePaint;
                  if (var19.mStrokeLineJoin != null) {
                     var20.setStrokeJoin(var19.mStrokeLineJoin);
                  }

                  if (var19.mStrokeLineCap != null) {
                     var20.setStrokeCap(var19.mStrokeLineCap);
                  }

                  var20.setStrokeMiter(var19.mStrokeMiterlimit);
                  var20.setColor(VectorDrawableCompat.applyAlpha(var19.mStrokeColor, var19.mStrokeAlpha));
                  var20.setColorFilter(var6);
                  var20.setStrokeWidth(var19.mStrokeWidth * var7 * var8);
                  var3.drawPath(this.mRenderPath, var20);
               }

            }
         }
      }

      private float getMatrixScale(Matrix var1) {
         float[] var5 = new float[]{0.0F, 1.0F, 1.0F, 0.0F};
         var1.mapVectors(var5);
         float var2 = (float)Math.hypot((double)var5[0], (double)var5[1]);
         float var4 = (float)Math.hypot((double)var5[2], (double)var5[3]);
         float var3 = cross(var5[0], var5[1], var5[2], var5[3]);
         var4 = Math.max(var2, var4);
         var2 = 0.0F;
         if (var4 > 0.0F) {
            var2 = Math.abs(var3) / var4;
         }

         return var2;
      }

      public void draw(Canvas var1, int var2, int var3, ColorFilter var4) {
         this.drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, var1, var2, var3, var4);
      }

      public float getAlpha() {
         return (float)this.getRootAlpha() / 255.0F;
      }

      public int getRootAlpha() {
         return this.mRootAlpha;
      }

      public void setAlpha(float var1) {
         this.setRootAlpha((int)(255.0F * var1));
      }

      public void setRootAlpha(int var1) {
         this.mRootAlpha = var1;
      }
   }

   private static class VectorDrawableCompatState extends ConstantState {
      boolean mAutoMirrored;
      boolean mCacheDirty;
      boolean mCachedAutoMirrored;
      Bitmap mCachedBitmap;
      int mCachedRootAlpha;
      int[] mCachedThemeAttrs;
      ColorStateList mCachedTint;
      Mode mCachedTintMode;
      int mChangingConfigurations;
      Paint mTempPaint;
      ColorStateList mTint = null;
      Mode mTintMode;
      VectorDrawableCompat.VPathRenderer mVPathRenderer;

      public VectorDrawableCompatState() {
         this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
         this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer();
      }

      public VectorDrawableCompatState(VectorDrawableCompat.VectorDrawableCompatState var1) {
         this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
         if (var1 != null) {
            this.mChangingConfigurations = var1.mChangingConfigurations;
            this.mVPathRenderer = new VectorDrawableCompat.VPathRenderer(var1.mVPathRenderer);
            if (var1.mVPathRenderer.mFillPaint != null) {
               this.mVPathRenderer.mFillPaint = new Paint(var1.mVPathRenderer.mFillPaint);
            }

            if (var1.mVPathRenderer.mStrokePaint != null) {
               this.mVPathRenderer.mStrokePaint = new Paint(var1.mVPathRenderer.mStrokePaint);
            }

            this.mTint = var1.mTint;
            this.mTintMode = var1.mTintMode;
            this.mAutoMirrored = var1.mAutoMirrored;
         }

      }

      public boolean canReuseBitmap(int var1, int var2) {
         return var1 == this.mCachedBitmap.getWidth() && var2 == this.mCachedBitmap.getHeight();
      }

      public boolean canReuseCache() {
         return !this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha();
      }

      public void createCachedBitmapIfNeeded(int var1, int var2) {
         if (this.mCachedBitmap == null || !this.canReuseBitmap(var1, var2)) {
            this.mCachedBitmap = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
            this.mCacheDirty = true;
         }

      }

      public void drawCachedBitmapWithRootAlpha(Canvas var1, ColorFilter var2, Rect var3) {
         Paint var4 = this.getPaint(var2);
         var1.drawBitmap(this.mCachedBitmap, (Rect)null, var3, var4);
      }

      public int getChangingConfigurations() {
         return this.mChangingConfigurations;
      }

      public Paint getPaint(ColorFilter var1) {
         if (!this.hasTranslucentRoot() && var1 == null) {
            return null;
         } else {
            if (this.mTempPaint == null) {
               this.mTempPaint = new Paint();
               this.mTempPaint.setFilterBitmap(true);
            }

            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(var1);
            return this.mTempPaint;
         }
      }

      public boolean hasTranslucentRoot() {
         return this.mVPathRenderer.getRootAlpha() < 255;
      }

      public Drawable newDrawable() {
         return new VectorDrawableCompat(this);
      }

      public Drawable newDrawable(Resources var1) {
         return new VectorDrawableCompat(this);
      }

      public void updateCacheStates() {
         this.mCachedTint = this.mTint;
         this.mCachedTintMode = this.mTintMode;
         this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
         this.mCachedAutoMirrored = this.mAutoMirrored;
         this.mCacheDirty = false;
      }

      public void updateCachedBitmap(int var1, int var2) {
         this.mCachedBitmap.eraseColor(0);
         Canvas var3 = new Canvas(this.mCachedBitmap);
         this.mVPathRenderer.draw(var3, var1, var2, (ColorFilter)null);
      }
   }

   @RequiresApi(24)
   private static class VectorDrawableDelegateState extends ConstantState {
      private final ConstantState mDelegateState;

      public VectorDrawableDelegateState(ConstantState var1) {
         this.mDelegateState = var1;
      }

      public boolean canApplyTheme() {
         return this.mDelegateState.canApplyTheme();
      }

      public int getChangingConfigurations() {
         return this.mDelegateState.getChangingConfigurations();
      }

      public Drawable newDrawable() {
         VectorDrawableCompat var1 = new VectorDrawableCompat();
         var1.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable();
         return var1;
      }

      public Drawable newDrawable(Resources var1) {
         VectorDrawableCompat var2 = new VectorDrawableCompat();
         var2.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(var1);
         return var2;
      }

      public Drawable newDrawable(Resources var1, Theme var2) {
         VectorDrawableCompat var3 = new VectorDrawableCompat();
         var3.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(var1, var2);
         return var3;
      }
   }
}
