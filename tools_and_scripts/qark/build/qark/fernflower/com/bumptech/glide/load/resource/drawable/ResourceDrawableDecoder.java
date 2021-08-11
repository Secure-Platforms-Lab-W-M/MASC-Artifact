package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.util.List;

public class ResourceDrawableDecoder implements ResourceDecoder {
   private static final String ANDROID_PACKAGE_NAME = "android";
   private static final int ID_PATH_SEGMENTS = 1;
   private static final int MISSING_RESOURCE_ID = 0;
   private static final int NAME_PATH_SEGMENT_INDEX = 1;
   private static final int NAME_URI_PATH_SEGMENTS = 2;
   private static final int RESOURCE_ID_SEGMENT_INDEX = 0;
   private static final int TYPE_PATH_SEGMENT_INDEX = 0;
   private final Context context;

   public ResourceDrawableDecoder(Context var1) {
      this.context = var1.getApplicationContext();
   }

   private Context findContextForPackage(Uri var1, String var2) {
      if (var2.equals(this.context.getPackageName())) {
         return this.context;
      } else {
         try {
            Context var3 = this.context.createPackageContext(var2, 0);
            return var3;
         } catch (NameNotFoundException var4) {
            if (var2.contains(this.context.getPackageName())) {
               return this.context;
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("Failed to obtain context or unrecognized Uri format for: ");
               var5.append(var1);
               throw new IllegalArgumentException(var5.toString(), var4);
            }
         }
      }
   }

   private int findResourceIdFromResourceIdUri(Uri var1) {
      List var3 = var1.getPathSegments();

      try {
         int var2 = Integer.parseInt((String)var3.get(0));
         return var2;
      } catch (NumberFormatException var5) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unrecognized Uri format: ");
         var4.append(var1);
         throw new IllegalArgumentException(var4.toString(), var5);
      }
   }

   private int findResourceIdFromTypeAndNameResourceUri(Context var1, Uri var2) {
      List var7 = var2.getPathSegments();
      String var5 = var2.getAuthority();
      String var6 = (String)var7.get(0);
      String var9 = (String)var7.get(1);
      int var4 = var1.getResources().getIdentifier(var9, var6, var5);
      int var3 = var4;
      if (var4 == 0) {
         var3 = Resources.getSystem().getIdentifier(var9, var6, "android");
      }

      if (var3 != 0) {
         return var3;
      } else {
         StringBuilder var8 = new StringBuilder();
         var8.append("Failed to find resource id for: ");
         var8.append(var2);
         throw new IllegalArgumentException(var8.toString());
      }
   }

   private int findResourceIdFromUri(Context var1, Uri var2) {
      List var3 = var2.getPathSegments();
      if (var3.size() == 2) {
         return this.findResourceIdFromTypeAndNameResourceUri(var1, var2);
      } else if (var3.size() == 1) {
         return this.findResourceIdFromResourceIdUri(var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unrecognized Uri format: ");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public Resource decode(Uri var1, int var2, int var3, Options var4) {
      Context var5 = this.findContextForPackage(var1, var1.getAuthority());
      var2 = this.findResourceIdFromUri(var5, var1);
      return NonOwnedDrawableResource.newInstance(DrawableDecoderCompat.getDrawable(this.context, var5, var2));
   }

   public boolean handles(Uri var1, Options var2) {
      return var1.getScheme().equals("android.resource");
   }
}
