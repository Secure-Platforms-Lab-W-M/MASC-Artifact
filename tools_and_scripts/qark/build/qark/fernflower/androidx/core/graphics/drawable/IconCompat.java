package androidx.core.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Preconditions;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

public class IconCompat extends CustomVersionedParcelable {
   private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25F;
   private static final int AMBIENT_SHADOW_ALPHA = 30;
   private static final float BLUR_FACTOR = 0.010416667F;
   static final Mode DEFAULT_TINT_MODE;
   private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
   private static final String EXTRA_INT1 = "int1";
   private static final String EXTRA_INT2 = "int2";
   private static final String EXTRA_OBJ = "obj";
   private static final String EXTRA_TINT_LIST = "tint_list";
   private static final String EXTRA_TINT_MODE = "tint_mode";
   private static final String EXTRA_TYPE = "type";
   private static final float ICON_DIAMETER_FACTOR = 0.9166667F;
   private static final int KEY_SHADOW_ALPHA = 61;
   private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334F;
   private static final String TAG = "IconCompat";
   public static final int TYPE_UNKNOWN = -1;
   public byte[] mData = null;
   public int mInt1 = 0;
   public int mInt2 = 0;
   Object mObj1;
   public Parcelable mParcelable = null;
   public ColorStateList mTintList = null;
   Mode mTintMode;
   public String mTintModeStr;
   public int mType = -1;

   static {
      DEFAULT_TINT_MODE = Mode.SRC_IN;
   }

   public IconCompat() {
      this.mTintMode = DEFAULT_TINT_MODE;
      this.mTintModeStr = null;
   }

   private IconCompat(int var1) {
      this.mTintMode = DEFAULT_TINT_MODE;
      this.mTintModeStr = null;
      this.mType = var1;
   }

   public static IconCompat createFromBundle(Bundle var0) {
      int var1 = var0.getInt("type");
      IconCompat var2 = new IconCompat(var1);
      var2.mInt1 = var0.getInt("int1");
      var2.mInt2 = var0.getInt("int2");
      if (var0.containsKey("tint_list")) {
         var2.mTintList = (ColorStateList)var0.getParcelable("tint_list");
      }

      if (var0.containsKey("tint_mode")) {
         var2.mTintMode = Mode.valueOf(var0.getString("tint_mode"));
      }

      label30: {
         if (var1 != -1 && var1 != 1) {
            if (var1 == 2) {
               break label30;
            }

            if (var1 == 3) {
               var2.mObj1 = var0.getByteArray("obj");
               return var2;
            }

            if (var1 == 4) {
               break label30;
            }

            if (var1 != 5) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown type ");
               var3.append(var1);
               Log.w("IconCompat", var3.toString());
               return null;
            }
         }

         var2.mObj1 = var0.getParcelable("obj");
         return var2;
      }

      var2.mObj1 = var0.getString("obj");
      return var2;
   }

   public static IconCompat createFromIcon(Context var0, Icon var1) {
      Preconditions.checkNotNull(var1);
      int var2 = getType(var1);
      IconCompat var5;
      if (var2 != 2) {
         if (var2 != 4) {
            var5 = new IconCompat(-1);
            var5.mObj1 = var1;
            return var5;
         } else {
            return createWithContentUri(getUri(var1));
         }
      } else {
         String var3 = getResPackage(var1);

         try {
            var5 = createWithResource(getResources(var0, var3), var3, getResId(var1));
            return var5;
         } catch (NotFoundException var4) {
            throw new IllegalArgumentException("Icon resource cannot be found");
         }
      }
   }

   public static IconCompat createFromIcon(Icon var0) {
      Preconditions.checkNotNull(var0);
      int var1 = getType(var0);
      if (var1 != 2) {
         if (var1 != 4) {
            IconCompat var2 = new IconCompat(-1);
            var2.mObj1 = var0;
            return var2;
         } else {
            return createWithContentUri(getUri(var0));
         }
      } else {
         return createWithResource((Resources)null, getResPackage(var0), getResId(var0));
      }
   }

   static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap var0, boolean var1) {
      int var5 = (int)((float)Math.min(var0.getWidth(), var0.getHeight()) * 0.6666667F);
      Bitmap var6 = Bitmap.createBitmap(var5, var5, Config.ARGB_8888);
      Canvas var7 = new Canvas(var6);
      Paint var8 = new Paint(3);
      float var2 = (float)var5 * 0.5F;
      float var3 = 0.9166667F * var2;
      if (var1) {
         float var4 = (float)var5 * 0.010416667F;
         var8.setColor(0);
         var8.setShadowLayer(var4, 0.0F, (float)var5 * 0.020833334F, 1023410176);
         var7.drawCircle(var2, var2, var3, var8);
         var8.setShadowLayer(var4, 0.0F, 0.0F, 503316480);
         var7.drawCircle(var2, var2, var3, var8);
         var8.clearShadowLayer();
      }

      var8.setColor(-16777216);
      BitmapShader var9 = new BitmapShader(var0, TileMode.CLAMP, TileMode.CLAMP);
      Matrix var10 = new Matrix();
      var10.setTranslate((float)(-(var0.getWidth() - var5) / 2), (float)(-(var0.getHeight() - var5) / 2));
      var9.setLocalMatrix(var10);
      var8.setShader(var9);
      var7.drawCircle(var2, var2, var3, var8);
      var7.setBitmap((Bitmap)null);
      return var6;
   }

   public static IconCompat createWithAdaptiveBitmap(Bitmap var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(5);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Bitmap must not be null.");
      }
   }

   public static IconCompat createWithBitmap(Bitmap var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(1);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Bitmap must not be null.");
      }
   }

   public static IconCompat createWithContentUri(Uri var0) {
      if (var0 != null) {
         return createWithContentUri(var0.toString());
      } else {
         throw new IllegalArgumentException("Uri must not be null.");
      }
   }

   public static IconCompat createWithContentUri(String var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(4);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Uri must not be null.");
      }
   }

   public static IconCompat createWithData(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         IconCompat var3 = new IconCompat(3);
         var3.mObj1 = var0;
         var3.mInt1 = var1;
         var3.mInt2 = var2;
         return var3;
      } else {
         throw new IllegalArgumentException("Data must not be null.");
      }
   }

   public static IconCompat createWithResource(Context var0, int var1) {
      if (var0 != null) {
         return createWithResource(var0.getResources(), var0.getPackageName(), var1);
      } else {
         throw new IllegalArgumentException("Context must not be null.");
      }
   }

   public static IconCompat createWithResource(Resources var0, String var1, int var2) {
      if (var1 != null) {
         if (var2 != 0) {
            IconCompat var3 = new IconCompat(2);
            var3.mInt1 = var2;
            if (var0 != null) {
               try {
                  var3.mObj1 = var0.getResourceName(var2);
                  return var3;
               } catch (NotFoundException var4) {
                  throw new IllegalArgumentException("Icon resource cannot be found");
               }
            } else {
               var3.mObj1 = var1;
               return var3;
            }
         } else {
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
         }
      } else {
         throw new IllegalArgumentException("Package must not be null.");
      }
   }

   private static int getResId(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getResId();
      } else {
         try {
            int var1 = (Integer)var0.getClass().getMethod("getResId").invoke(var0);
            return var1;
         } catch (IllegalAccessException var2) {
            Log.e("IconCompat", "Unable to get icon resource", var2);
            return 0;
         } catch (InvocationTargetException var3) {
            Log.e("IconCompat", "Unable to get icon resource", var3);
            return 0;
         } catch (NoSuchMethodException var4) {
            Log.e("IconCompat", "Unable to get icon resource", var4);
            return 0;
         }
      }
   }

   private static String getResPackage(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getResPackage();
      } else {
         try {
            String var4 = (String)var0.getClass().getMethod("getResPackage").invoke(var0);
            return var4;
         } catch (IllegalAccessException var1) {
            Log.e("IconCompat", "Unable to get icon package", var1);
            return null;
         } catch (InvocationTargetException var2) {
            Log.e("IconCompat", "Unable to get icon package", var2);
            return null;
         } catch (NoSuchMethodException var3) {
            Log.e("IconCompat", "Unable to get icon package", var3);
            return null;
         }
      }
   }

   private static Resources getResources(Context var0, String var1) {
      if ("android".equals(var1)) {
         return Resources.getSystem();
      } else {
         PackageManager var5 = var0.getPackageManager();

         NameNotFoundException var10000;
         label31: {
            boolean var10001;
            ApplicationInfo var2;
            try {
               var2 = var5.getApplicationInfo(var1, 8192);
            } catch (NameNotFoundException var4) {
               var10000 = var4;
               var10001 = false;
               break label31;
            }

            if (var2 == null) {
               return null;
            }

            try {
               Resources var7 = var5.getResourcesForApplication(var2);
               return var7;
            } catch (NameNotFoundException var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         NameNotFoundException var6 = var10000;
         Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", var1), var6);
         return null;
      }
   }

   private static int getType(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getType();
      } else {
         StringBuilder var3;
         try {
            int var1 = (Integer)var0.getClass().getMethod("getType").invoke(var0);
            return var1;
         } catch (IllegalAccessException var4) {
            var3 = new StringBuilder();
            var3.append("Unable to get icon type ");
            var3.append(var0);
            Log.e("IconCompat", var3.toString(), var4);
            return -1;
         } catch (InvocationTargetException var5) {
            var3 = new StringBuilder();
            var3.append("Unable to get icon type ");
            var3.append(var0);
            Log.e("IconCompat", var3.toString(), var5);
            return -1;
         } catch (NoSuchMethodException var6) {
            var3 = new StringBuilder();
            var3.append("Unable to get icon type ");
            var3.append(var0);
            Log.e("IconCompat", var3.toString(), var6);
            return -1;
         }
      }
   }

   private static Uri getUri(Icon var0) {
      if (VERSION.SDK_INT >= 28) {
         return var0.getUri();
      } else {
         try {
            Uri var4 = (Uri)var0.getClass().getMethod("getUri").invoke(var0);
            return var4;
         } catch (IllegalAccessException var1) {
            Log.e("IconCompat", "Unable to get icon uri", var1);
            return null;
         } catch (InvocationTargetException var2) {
            Log.e("IconCompat", "Unable to get icon uri", var2);
            return null;
         } catch (NoSuchMethodException var3) {
            Log.e("IconCompat", "Unable to get icon uri", var3);
            return null;
         }
      }
   }

   private Drawable loadDrawableInner(Context var1) {
      int var2 = this.mType;
      if (var2 == 1) {
         return new BitmapDrawable(var1.getResources(), (Bitmap)this.mObj1);
      } else {
         if (var2 != 2) {
            if (var2 == 3) {
               return new BitmapDrawable(var1.getResources(), BitmapFactory.decodeByteArray((byte[])((byte[])this.mObj1), this.mInt1, this.mInt2));
            }

            if (var2 != 4) {
               if (var2 == 5) {
                  return new BitmapDrawable(var1.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
               }
            } else {
               Uri var5 = Uri.parse((String)this.mObj1);
               String var6 = var5.getScheme();
               InputStream var4 = null;
               Object var3 = null;
               StringBuilder var14;
               if (!"content".equals(var6) && !"file".equals(var6)) {
                  try {
                     var3 = new FileInputStream(new File((String)this.mObj1));
                  } catch (FileNotFoundException var7) {
                     var14 = new StringBuilder();
                     var14.append("Unable to load image from path: ");
                     var14.append(var5);
                     Log.w("IconCompat", var14.toString(), var7);
                     var3 = var4;
                  }
               } else {
                  label66: {
                     try {
                        var4 = var1.getContentResolver().openInputStream(var5);
                     } catch (Exception var8) {
                        var14 = new StringBuilder();
                        var14.append("Unable to load image from URI: ");
                        var14.append(var5);
                        Log.w("IconCompat", var14.toString(), var8);
                        break label66;
                     }

                     var3 = var4;
                  }
               }

               if (var3 != null) {
                  return new BitmapDrawable(var1.getResources(), BitmapFactory.decodeStream((InputStream)var3));
               }
            }
         } else {
            String var11 = this.getResPackage();
            String var12 = var11;
            if (TextUtils.isEmpty(var11)) {
               var12 = var1.getPackageName();
            }

            Resources var13 = getResources(var1, var12);

            try {
               Drawable var10 = ResourcesCompat.getDrawable(var13, this.mInt1, var1.getTheme());
               return var10;
            } catch (RuntimeException var9) {
               Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", this.mInt1, this.mObj1), var9);
            }
         }

         return null;
      }
   }

   private static String typeToString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 3) {
               if (var0 != 4) {
                  return var0 != 5 ? "UNKNOWN" : "BITMAP_MASKABLE";
               } else {
                  return "URI";
               }
            } else {
               return "DATA";
            }
         } else {
            return "RESOURCE";
         }
      } else {
         return "BITMAP";
      }
   }

   public void addToShortcutIntent(Intent var1, Drawable var2, Context var3) {
      this.checkResource(var3);
      int var4 = this.mType;
      Bitmap var15;
      if (var4 == 1) {
         Bitmap var16 = (Bitmap)this.mObj1;
         var15 = var16;
         if (var2 != null) {
            var15 = var16.copy(var16.getConfig(), true);
         }
      } else if (var4 != 2) {
         if (var4 != 5) {
            throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
         }

         var15 = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
      } else {
         label80: {
            NameNotFoundException var10000;
            label70: {
               boolean var10001;
               try {
                  var3 = var3.createPackageContext(this.getResPackage(), 0);
               } catch (NameNotFoundException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label70;
               }

               if (var2 == null) {
                  try {
                     var1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(var3, this.mInt1));
                     return;
                  } catch (NameNotFoundException var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               } else {
                  label66: {
                     Drawable var6;
                     label65: {
                        label82: {
                           label62:
                           try {
                              var6 = ContextCompat.getDrawable(var3, this.mInt1);
                              if (var6.getIntrinsicWidth() > 0 && var6.getIntrinsicHeight() > 0) {
                                 break label62;
                              }
                              break label82;
                           } catch (NameNotFoundException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label66;
                           }

                           try {
                              var15 = Bitmap.createBitmap(var6.getIntrinsicWidth(), var6.getIntrinsicHeight(), Config.ARGB_8888);
                              break label65;
                           } catch (NameNotFoundException var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label66;
                           }
                        }

                        try {
                           var4 = ((ActivityManager)var3.getSystemService("activity")).getLauncherLargeIconSize();
                           var15 = Bitmap.createBitmap(var4, var4, Config.ARGB_8888);
                        } catch (NameNotFoundException var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label66;
                        }
                     }

                     try {
                        var6.setBounds(0, 0, var15.getWidth(), var15.getHeight());
                        var6.draw(new Canvas(var15));
                        break label80;
                     } catch (NameNotFoundException var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }
               }
            }

            NameNotFoundException var13 = var10000;
            StringBuilder var14 = new StringBuilder();
            var14.append("Can't find package ");
            var14.append(this.mObj1);
            throw new IllegalArgumentException(var14.toString(), var13);
         }
      }

      if (var2 != null) {
         var4 = var15.getWidth();
         int var5 = var15.getHeight();
         var2.setBounds(var4 / 2, var5 / 2, var4, var5);
         var2.draw(new Canvas(var15));
      }

      var1.putExtra("android.intent.extra.shortcut.ICON", var15);
   }

   public void checkResource(Context var1) {
      if (this.mType == 2) {
         String var5 = (String)this.mObj1;
         if (!var5.contains(":")) {
            return;
         }

         String var4 = var5.split(":", -1)[1];
         String var3 = var4.split("/", -1)[0];
         var4 = var4.split("/", -1)[1];
         var5 = var5.split(":", -1)[0];
         int var2 = getResources(var1, var5).getIdentifier(var4, var3, var5);
         if (this.mInt1 != var2) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Id has changed for ");
            var6.append(var5);
            var6.append("/");
            var6.append(var4);
            Log.i("IconCompat", var6.toString());
            this.mInt1 = var2;
         }
      }

   }

   public Bitmap getBitmap() {
      if (this.mType == -1 && VERSION.SDK_INT >= 23) {
         Object var3 = this.mObj1;
         return var3 instanceof Bitmap ? (Bitmap)var3 : null;
      } else {
         int var1 = this.mType;
         if (var1 == 1) {
            return (Bitmap)this.mObj1;
         } else if (var1 == 5) {
            return createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("called getBitmap() on ");
            var2.append(this);
            throw new IllegalStateException(var2.toString());
         }
      }
   }

   public int getResId() {
      if (this.mType == -1 && VERSION.SDK_INT >= 23) {
         return getResId((Icon)this.mObj1);
      } else if (this.mType == 2) {
         return this.mInt1;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("called getResId() on ");
         var1.append(this);
         throw new IllegalStateException(var1.toString());
      }
   }

   public String getResPackage() {
      if (this.mType == -1 && VERSION.SDK_INT >= 23) {
         return getResPackage((Icon)this.mObj1);
      } else if (this.mType == 2) {
         return ((String)this.mObj1).split(":", -1)[0];
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("called getResPackage() on ");
         var1.append(this);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int getType() {
      return this.mType == -1 && VERSION.SDK_INT >= 23 ? getType((Icon)this.mObj1) : this.mType;
   }

   public Uri getUri() {
      return this.mType == -1 && VERSION.SDK_INT >= 23 ? getUri((Icon)this.mObj1) : Uri.parse((String)this.mObj1);
   }

   public Drawable loadDrawable(Context var1) {
      this.checkResource(var1);
      if (VERSION.SDK_INT >= 23) {
         return this.toIcon().loadDrawable(var1);
      } else {
         Drawable var2 = this.loadDrawableInner(var1);
         if (var2 != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
            var2.mutate();
            DrawableCompat.setTintList(var2, this.mTintList);
            DrawableCompat.setTintMode(var2, this.mTintMode);
         }

         return var2;
      }
   }

   public void onPostParceling() {
      this.mTintMode = Mode.valueOf(this.mTintModeStr);
      int var1 = this.mType;
      Parcelable var2;
      if (var1 == -1) {
         var2 = this.mParcelable;
         if (var2 != null) {
            this.mObj1 = var2;
         } else {
            throw new IllegalArgumentException("Invalid icon");
         }
      } else {
         if (var1 != 1) {
            label39: {
               if (var1 != 2) {
                  if (var1 == 3) {
                     this.mObj1 = this.mData;
                     return;
                  }

                  if (var1 != 4) {
                     if (var1 != 5) {
                        return;
                     }
                     break label39;
                  }
               }

               this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
               return;
            }
         }

         var2 = this.mParcelable;
         if (var2 != null) {
            this.mObj1 = var2;
         } else {
            byte[] var3 = this.mData;
            this.mObj1 = var3;
            this.mType = 3;
            this.mInt1 = 0;
            this.mInt2 = var3.length;
         }
      }
   }

   public void onPreParceling(boolean var1) {
      this.mTintModeStr = this.mTintMode.name();
      int var2 = this.mType;
      if (var2 != -1) {
         if (var2 != 1) {
            if (var2 == 2) {
               this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
               return;
            }

            if (var2 == 3) {
               this.mData = (byte[])((byte[])this.mObj1);
               return;
            }

            if (var2 == 4) {
               this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
               return;
            }

            if (var2 != 5) {
               return;
            }
         }

         if (var1) {
            Bitmap var3 = (Bitmap)this.mObj1;
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            var3.compress(CompressFormat.PNG, 90, var4);
            this.mData = var4.toByteArray();
         } else {
            this.mParcelable = (Parcelable)this.mObj1;
         }
      } else if (!var1) {
         this.mParcelable = (Parcelable)this.mObj1;
      } else {
         throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
      }
   }

   public IconCompat setTint(int var1) {
      return this.setTintList(ColorStateList.valueOf(var1));
   }

   public IconCompat setTintList(ColorStateList var1) {
      this.mTintList = var1;
      return this;
   }

   public IconCompat setTintMode(Mode var1) {
      this.mTintMode = var1;
      return this;
   }

   public Bundle toBundle() {
      Bundle var2 = new Bundle();
      int var1 = this.mType;
      if (var1 != -1) {
         label36: {
            label35: {
               if (var1 != 1) {
                  if (var1 == 2) {
                     break label35;
                  }

                  if (var1 == 3) {
                     var2.putByteArray("obj", (byte[])((byte[])this.mObj1));
                     break label36;
                  }

                  if (var1 == 4) {
                     break label35;
                  }

                  if (var1 != 5) {
                     throw new IllegalArgumentException("Invalid icon");
                  }
               }

               var2.putParcelable("obj", (Bitmap)this.mObj1);
               break label36;
            }

            var2.putString("obj", (String)this.mObj1);
         }
      } else {
         var2.putParcelable("obj", (Parcelable)this.mObj1);
      }

      var2.putInt("type", this.mType);
      var2.putInt("int1", this.mInt1);
      var2.putInt("int2", this.mInt2);
      ColorStateList var3 = this.mTintList;
      if (var3 != null) {
         var2.putParcelable("tint_list", var3);
      }

      Mode var4 = this.mTintMode;
      if (var4 != DEFAULT_TINT_MODE) {
         var2.putString("tint_mode", var4.name());
      }

      return var2;
   }

   public Icon toIcon() {
      int var1 = this.mType;
      if (var1 != -1) {
         Icon var2;
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 4) {
                     if (var1 != 5) {
                        throw new IllegalArgumentException("Unknown type");
                     }

                     if (VERSION.SDK_INT >= 26) {
                        var2 = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                     } else {
                        var2 = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                     }
                  } else {
                     var2 = Icon.createWithContentUri((String)this.mObj1);
                  }
               } else {
                  var2 = Icon.createWithData((byte[])((byte[])this.mObj1), this.mInt1, this.mInt2);
               }
            } else {
               var2 = Icon.createWithResource(this.getResPackage(), this.mInt1);
            }
         } else {
            var2 = Icon.createWithBitmap((Bitmap)this.mObj1);
         }

         ColorStateList var3 = this.mTintList;
         if (var3 != null) {
            var2.setTintList(var3);
         }

         Mode var4 = this.mTintMode;
         if (var4 != DEFAULT_TINT_MODE) {
            var2.setTintMode(var4);
         }

         return var2;
      } else {
         return (Icon)this.mObj1;
      }
   }

   public String toString() {
      if (this.mType == -1) {
         return String.valueOf(this.mObj1);
      } else {
         StringBuilder var2;
         label37: {
            var2 = (new StringBuilder("Icon(typ=")).append(typeToString(this.mType));
            int var1 = this.mType;
            if (var1 != 1) {
               if (var1 == 2) {
                  var2.append(" pkg=");
                  var2.append(this.getResPackage());
                  var2.append(" id=");
                  var2.append(String.format("0x%08x", this.getResId()));
                  break label37;
               }

               if (var1 == 3) {
                  var2.append(" len=");
                  var2.append(this.mInt1);
                  if (this.mInt2 != 0) {
                     var2.append(" off=");
                     var2.append(this.mInt2);
                  }
                  break label37;
               }

               if (var1 == 4) {
                  var2.append(" uri=");
                  var2.append(this.mObj1);
                  break label37;
               }

               if (var1 != 5) {
                  break label37;
               }
            }

            var2.append(" size=");
            var2.append(((Bitmap)this.mObj1).getWidth());
            var2.append("x");
            var2.append(((Bitmap)this.mObj1).getHeight());
         }

         if (this.mTintList != null) {
            var2.append(" tint=");
            var2.append(this.mTintList);
         }

         if (this.mTintMode != DEFAULT_TINT_MODE) {
            var2.append(" mode=");
            var2.append(this.mTintMode);
         }

         var2.append(")");
         return var2.toString();
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface IconType {
   }
}
