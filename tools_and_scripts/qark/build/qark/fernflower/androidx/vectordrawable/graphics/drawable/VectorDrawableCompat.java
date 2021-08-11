package androidx.vectordrawable.graphics.drawable;

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
import android.graphics.Shader;
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
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.collection.ArrayMap;
import androidx.core.content.res.ComplexColorCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.core.graphics.drawable.DrawableCompat;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

   VectorDrawableCompat(VectorDrawableCompat.VectorDrawableCompatState var1) {
      this.mVectorState = var1;
      this.mTintFilter = this.updateTintFilter(this.mTintFilter, var1.mTint, var1.mTintMode);
   }

   static int applyAlpha(int var0, float var1) {
      return var0 & 16777215 | (int)((float)Color.alpha(var0) * var1) << 24;
   }

   public static VectorDrawableCompat create(Resources var0, int var1, Theme var2) {
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
      ArrayDeque var11 = new ArrayDeque();
      var11.push(var10.mRootGroup);
      int var7 = var2.getEventType();

      boolean var6;
      for(int var8 = var2.getDepth(); var7 != 1 && (var2.getDepth() >= var8 + 1 || var7 != 3); var5 = var6) {
         if (var7 == 2) {
            String var13 = var2.getName();
            VectorDrawableCompat.VGroup var12 = (VectorDrawableCompat.VGroup)var11.peek();
            if ("path".equals(var13)) {
               VectorDrawableCompat.VFullPath var14 = new VectorDrawableCompat.VFullPath();
               var14.inflate(var1, var3, var4, var2);
               var12.mChildren.add(var14);
               if (var14.getPathName() != null) {
                  var10.mVGTargetsMap.put(var14.getPathName(), var14);
               }

               var6 = false;
               var9.mChangingConfigurations |= var14.mChangingConfigurations;
            } else {
               label48: {
                  if ("clip-path".equals(var13)) {
                     VectorDrawableCompat.VClipPath var15 = new VectorDrawableCompat.VClipPath();
                     var15.inflate(var1, var3, var4, var2);
                     var12.mChildren.add(var15);
                     if (var15.getPathName() != null) {
                        var10.mVGTargetsMap.put(var15.getPathName(), var15);
                     }

                     var9.mChangingConfigurations |= var15.mChangingConfigurations;
                  } else if ("group".equals(var13)) {
                     VectorDrawableCompat.VGroup var16 = new VectorDrawableCompat.VGroup();
                     var16.inflate(var1, var3, var4, var2);
                     var12.mChildren.add(var16);
                     var11.push(var16);
                     if (var16.getGroupName() != null) {
                        var10.mVGTargetsMap.put(var16.getGroupName(), var16);
                     }

                     var9.mChangingConfigurations |= var16.mChangingConfigurations;
                     var6 = var5;
                     break label48;
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
         throw new XmlPullParserException("no path defined");
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
                  return Mode.ADD;
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
         VectorDrawableCompat.VObject var6 = (VectorDrawableCompat.VObject)var1.mChildren.get(var3);
         if (var6 instanceof VectorDrawableCompat.VGroup) {
            this.printGroupTree((VectorDrawableCompat.VGroup)var6, var2 + 1);
         } else {
            ((VectorDrawableCompat.VPath)var6).printVPath(var2 + 1);
         }
      }

   }

   private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2, Theme var3) throws XmlPullParserException {
      VectorDrawableCompat.VectorDrawableCompatState var5 = this.mVectorState;
      VectorDrawableCompat.VPathRenderer var4 = var5.mVPathRenderer;
      var5.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(var1, var2, "tintMode", 6, -1), Mode.SRC_IN);
      ColorStateList var8 = TypedArrayUtils.getNamedColorStateList(var1, var2, var3, "tint", 1);
      if (var8 != null) {
         var5.mTint = var8;
      }

      var5.mAutoMirrored = TypedArrayUtils.getNamedBoolean(var1, var2, "autoMirrored", 5, var5.mAutoMirrored);
      var4.mViewportWidth = TypedArrayUtils.getNamedFloat(var1, var2, "viewportWidth", 7, var4.mViewportWidth);
      var4.mViewportHeight = TypedArrayUtils.getNamedFloat(var1, var2, "viewportHeight", 8, var4.mViewportHeight);
      StringBuilder var7;
      if (var4.mViewportWidth > 0.0F) {
         if (var4.mViewportHeight > 0.0F) {
            var4.mBaseWidth = var1.getDimension(3, var4.mBaseWidth);
            var4.mBaseHeight = var1.getDimension(2, var4.mBaseHeight);
            if (var4.mBaseWidth > 0.0F) {
               if (var4.mBaseHeight > 0.0F) {
                  var4.setAlpha(TypedArrayUtils.getNamedFloat(var1, var2, "alpha", 4, var4.getAlpha()));
                  String var6 = var1.getString(0);
                  if (var6 != null) {
                     var4.mRootName = var6;
                     var4.mVGTargetsMap.put(var6, var4);
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

   public ColorFilter getColorFilter() {
      return this.mDelegateDrawable != null ? DrawableCompat.getColorFilter(this.mDelegateDrawable) : this.mColorFilter;
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
         this.updateStateFromTypedArray(var6, var2, var4);
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
            if (var1 == null || !var1.isStateful() && (this.mVectorState.mTint == null || !this.mVectorState.mTint.isStateful())) {
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
         boolean var3 = false;
         VectorDrawableCompat.VectorDrawableCompatState var4 = this.mVectorState;
         boolean var2 = var3;
         if (var4.mTint != null) {
            var2 = var3;
            if (var4.mTintMode != null) {
               this.mTintFilter = this.updateTintFilter(this.mTintFilter, var4.mTint, var4.mTintMode);
               this.invalidateSelf();
               var2 = true;
            }
         }

         var3 = var2;
         if (var4.isStateful()) {
            var3 = var2;
            if (var4.onStateChanged(var1)) {
               this.invalidateSelf();
               var3 = true;
            }
         }

         return var3;
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
      VClipPath() {
      }

      VClipPath(VectorDrawableCompat.VClipPath var1) {
         super(var1);
      }

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2) {
         String var3 = var1.getString(0);
         if (var3 != null) {
            this.mPathName = var3;
         }

         var3 = var1.getString(1);
         if (var3 != null) {
            this.mNodes = PathParser.createNodesFromPathData(var3);
         }

         this.mFillRule = TypedArrayUtils.getNamedInt(var1, var2, "fillType", 2, 0);
      }

      public void inflate(Resources var1, AttributeSet var2, Theme var3, XmlPullParser var4) {
         if (TypedArrayUtils.hasAttribute(var4, "pathData")) {
            TypedArray var5 = TypedArrayUtils.obtainAttributes(var1, var3, var2, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
            this.updateStateFromTypedArray(var5, var4);
            var5.recycle();
         }
      }

      public boolean isClipPath() {
         return true;
      }
   }

   private static class VFullPath extends VectorDrawableCompat.VPath {
      float mFillAlpha = 1.0F;
      ComplexColorCompat mFillColor;
      float mStrokeAlpha = 1.0F;
      ComplexColorCompat mStrokeColor;
      Cap mStrokeLineCap;
      Join mStrokeLineJoin;
      float mStrokeMiterlimit;
      float mStrokeWidth = 0.0F;
      private int[] mThemeAttrs;
      float mTrimPathEnd = 1.0F;
      float mTrimPathOffset = 0.0F;
      float mTrimPathStart = 0.0F;

      VFullPath() {
         this.mStrokeLineCap = Cap.BUTT;
         this.mStrokeLineJoin = Join.MITER;
         this.mStrokeMiterlimit = 4.0F;
      }

      VFullPath(VectorDrawableCompat.VFullPath var1) {
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

      private void updateStateFromTypedArray(TypedArray var1, XmlPullParser var2, Theme var3) {
         this.mThemeAttrs = null;
         if (TypedArrayUtils.hasAttribute(var2, "pathData")) {
            String var4 = var1.getString(0);
            if (var4 != null) {
               this.mPathName = var4;
            }

            var4 = var1.getString(2);
            if (var4 != null) {
               this.mNodes = PathParser.createNodesFromPathData(var4);
            }

            this.mFillColor = TypedArrayUtils.getNamedComplexColor(var1, var2, var3, "fillColor", 1, 0);
            this.mFillAlpha = TypedArrayUtils.getNamedFloat(var1, var2, "fillAlpha", 12, this.mFillAlpha);
            this.mStrokeLineCap = this.getStrokeLineCap(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineCap", 8, -1), this.mStrokeLineCap);
            this.mStrokeLineJoin = this.getStrokeLineJoin(TypedArrayUtils.getNamedInt(var1, var2, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
            this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(var1, var2, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
            this.mStrokeColor = TypedArrayUtils.getNamedComplexColor(var1, var2, var3, "strokeColor", 3, 0);
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
         return this.mFillColor.getColor();
      }

      float getStrokeAlpha() {
         return this.mStrokeAlpha;
      }

      int getStrokeColor() {
         return this.mStrokeColor.getColor();
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
         this.updateStateFromTypedArray(var5, var4, var3);
         var5.recycle();
      }

      public boolean isStateful() {
         return this.mFillColor.isStateful() || this.mStrokeColor.isStateful();
      }

      public boolean onStateChanged(int[] var1) {
         return this.mFillColor.onStateChanged(var1) | this.mStrokeColor.onStateChanged(var1);
      }

      void setFillAlpha(float var1) {
         this.mFillAlpha = var1;
      }

      void setFillColor(int var1) {
         this.mFillColor.setColor(var1);
      }

      void setStrokeAlpha(float var1) {
         this.mStrokeAlpha = var1;
      }

      void setStrokeColor(int var1) {
         this.mStrokeColor.setColor(var1);
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

   private static class VGroup extends VectorDrawableCompat.VObject {
      int mChangingConfigurations;
      final ArrayList mChildren = new ArrayList();
      private String mGroupName = null;
      final Matrix mLocalMatrix = new Matrix();
      private float mPivotX = 0.0F;
      private float mPivotY = 0.0F;
      float mRotate = 0.0F;
      private float mScaleX = 1.0F;
      private float mScaleY = 1.0F;
      final Matrix mStackedMatrix = new Matrix();
      private int[] mThemeAttrs;
      private float mTranslateX = 0.0F;
      private float mTranslateY = 0.0F;

      public VGroup() {
         super(null);
      }

      public VGroup(VectorDrawableCompat.VGroup var1, ArrayMap var2) {
         super(null);
         this.mRotate = var1.mRotate;
         this.mPivotX = var1.mPivotX;
         this.mPivotY = var1.mPivotY;
         this.mScaleX = var1.mScaleX;
         this.mScaleY = var1.mScaleY;
         this.mTranslateX = var1.mTranslateX;
         this.mTranslateY = var1.mTranslateY;
         this.mThemeAttrs = var1.mThemeAttrs;
         String var4 = var1.mGroupName;
         this.mGroupName = var4;
         this.mChangingConfigurations = var1.mChangingConfigurations;
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

      public boolean isStateful() {
         for(int var1 = 0; var1 < this.mChildren.size(); ++var1) {
            if (((VectorDrawableCompat.VObject)this.mChildren.get(var1)).isStateful()) {
               return true;
            }
         }

         return false;
      }

      public boolean onStateChanged(int[] var1) {
         boolean var3 = false;

         for(int var2 = 0; var2 < this.mChildren.size(); ++var2) {
            var3 |= ((VectorDrawableCompat.VObject)this.mChildren.get(var2)).onStateChanged(var1);
         }

         return var3;
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

   private abstract static class VObject {
      private VObject() {
      }

      // $FF: synthetic method
      VObject(Object var1) {
         this();
      }

      public boolean isStateful() {
         return false;
      }

      public boolean onStateChanged(int[] var1) {
         return false;
      }
   }

   private abstract static class VPath extends VectorDrawableCompat.VObject {
      protected static final int FILL_TYPE_WINDING = 0;
      int mChangingConfigurations;
      int mFillRule = 0;
      protected PathParser.PathDataNode[] mNodes = null;
      String mPathName;

      public VPath() {
         super(null);
      }

      public VPath(VectorDrawableCompat.VPath var1) {
         super(null);
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
      Paint mFillPaint;
      private final Matrix mFinalPathMatrix = new Matrix();
      Boolean mIsStateful = null;
      private final Path mPath;
      private PathMeasure mPathMeasure;
      private final Path mRenderPath;
      int mRootAlpha = 255;
      final VectorDrawableCompat.VGroup mRootGroup;
      String mRootName = null;
      Paint mStrokePaint;
      final ArrayMap mVGTargetsMap;
      float mViewportHeight = 0.0F;
      float mViewportWidth = 0.0F;

      public VPathRenderer() {
         this.mVGTargetsMap = new ArrayMap();
         this.mRootGroup = new VectorDrawableCompat.VGroup();
         this.mPath = new Path();
         this.mRenderPath = new Path();
      }

      public VPathRenderer(VectorDrawableCompat.VPathRenderer var1) {
         ArrayMap var2 = new ArrayMap();
         this.mVGTargetsMap = var2;
         this.mRootGroup = new VectorDrawableCompat.VGroup(var1.mRootGroup, var2);
         this.mPath = new Path(var1.mPath);
         this.mRenderPath = new Path(var1.mRenderPath);
         this.mBaseWidth = var1.mBaseWidth;
         this.mBaseHeight = var1.mBaseHeight;
         this.mViewportWidth = var1.mViewportWidth;
         this.mViewportHeight = var1.mViewportHeight;
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mRootAlpha = var1.mRootAlpha;
         this.mRootName = var1.mRootName;
         String var3 = var1.mRootName;
         if (var3 != null) {
            this.mVGTargetsMap.put(var3, this);
         }

         this.mIsStateful = var1.mIsStateful;
      }

      private static float cross(float var0, float var1, float var2, float var3) {
         return var0 * var3 - var1 * var2;
      }

      private void drawGroupTree(VectorDrawableCompat.VGroup var1, Matrix var2, Canvas var3, int var4, int var5, ColorFilter var6) {
         var1.mStackedMatrix.set(var2);
         var1.mStackedMatrix.preConcat(var1.mLocalMatrix);
         var3.save();

         for(int var7 = 0; var7 < var1.mChildren.size(); ++var7) {
            VectorDrawableCompat.VObject var8 = (VectorDrawableCompat.VObject)var1.mChildren.get(var7);
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
            Path var14 = this.mPath;
            this.mRenderPath.reset();
            FillType var20;
            if (var2.isClipPath()) {
               Path var21 = this.mRenderPath;
               if (var2.mFillRule == 0) {
                  var20 = FillType.WINDING;
               } else {
                  var20 = FillType.EVEN_ODD;
               }

               var21.setFillType(var20);
               this.mRenderPath.addPath(var14, this.mFinalPathMatrix);
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
                  var14.reset();
                  if (var12 > var10) {
                     this.mPathMeasure.getSegment(var12, var9, var14, true);
                     this.mPathMeasure.getSegment(0.0F, var10, var14, true);
                  } else {
                     this.mPathMeasure.getSegment(var12, var10, var14, true);
                  }

                  var14.rLineTo(0.0F, 0.0F);
               }

               this.mRenderPath.addPath(var14, this.mFinalPathMatrix);
               ComplexColorCompat var17;
               Shader var18;
               Paint var22;
               if (var19.mFillColor.willDraw()) {
                  var17 = var19.mFillColor;
                  if (this.mFillPaint == null) {
                     var22 = new Paint(1);
                     this.mFillPaint = var22;
                     var22.setStyle(Style.FILL);
                  }

                  var22 = this.mFillPaint;
                  if (var17.isGradient()) {
                     var18 = var17.getShader();
                     var18.setLocalMatrix(this.mFinalPathMatrix);
                     var22.setShader(var18);
                     var22.setAlpha(Math.round(var19.mFillAlpha * 255.0F));
                  } else {
                     var22.setShader((Shader)null);
                     var22.setAlpha(255);
                     var22.setColor(VectorDrawableCompat.applyAlpha(var17.getColor(), var19.mFillAlpha));
                  }

                  var22.setColorFilter(var6);
                  Path var15 = this.mRenderPath;
                  if (var19.mFillRule == 0) {
                     var20 = FillType.WINDING;
                  } else {
                     var20 = FillType.EVEN_ODD;
                  }

                  var15.setFillType(var20);
                  var3.drawPath(this.mRenderPath, var22);
               }

               if (var19.mStrokeColor.willDraw()) {
                  var17 = var19.mStrokeColor;
                  if (this.mStrokePaint == null) {
                     var22 = new Paint(1);
                     this.mStrokePaint = var22;
                     var22.setStyle(Style.STROKE);
                  }

                  var22 = this.mStrokePaint;
                  if (var19.mStrokeLineJoin != null) {
                     var22.setStrokeJoin(var19.mStrokeLineJoin);
                  }

                  if (var19.mStrokeLineCap != null) {
                     var22.setStrokeCap(var19.mStrokeLineCap);
                  }

                  var22.setStrokeMiter(var19.mStrokeMiterlimit);
                  if (var17.isGradient()) {
                     var18 = var17.getShader();
                     var18.setLocalMatrix(this.mFinalPathMatrix);
                     var22.setShader(var18);
                     var22.setAlpha(Math.round(var19.mStrokeAlpha * 255.0F));
                  } else {
                     var22.setShader((Shader)null);
                     var22.setAlpha(255);
                     var22.setColor(VectorDrawableCompat.applyAlpha(var17.getColor(), var19.mStrokeAlpha));
                  }

                  var22.setColorFilter(var6);
                  var22.setStrokeWidth(var19.mStrokeWidth * var7 * var8);
                  var3.drawPath(this.mRenderPath, var22);
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

      public boolean isStateful() {
         if (this.mIsStateful == null) {
            this.mIsStateful = this.mRootGroup.isStateful();
         }

         return this.mIsStateful;
      }

      public boolean onStateChanged(int[] var1) {
         return this.mRootGroup.onStateChanged(var1);
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
               Paint var2 = new Paint();
               this.mTempPaint = var2;
               var2.setFilterBitmap(true);
            }

            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(var1);
            return this.mTempPaint;
         }
      }

      public boolean hasTranslucentRoot() {
         return this.mVPathRenderer.getRootAlpha() < 255;
      }

      public boolean isStateful() {
         return this.mVPathRenderer.isStateful();
      }

      public Drawable newDrawable() {
         return new VectorDrawableCompat(this);
      }

      public Drawable newDrawable(Resources var1) {
         return new VectorDrawableCompat(this);
      }

      public boolean onStateChanged(int[] var1) {
         boolean var2 = this.mVPathRenderer.onStateChanged(var1);
         this.mCacheDirty |= var2;
         return var2;
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
