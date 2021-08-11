package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
   private static final FileService DEFAULT_SERVICE = new FileService();
   private static final String TAG = "ThumbStreamOpener";
   private final ArrayPool byteArrayPool;
   private final ContentResolver contentResolver;
   private final List parsers;
   private final ThumbnailQuery query;
   private final FileService service;

   ThumbnailStreamOpener(List var1, FileService var2, ThumbnailQuery var3, ArrayPool var4, ContentResolver var5) {
      this.service = var2;
      this.query = var3;
      this.byteArrayPool = var4;
      this.contentResolver = var5;
      this.parsers = var1;
   }

   ThumbnailStreamOpener(List var1, ThumbnailQuery var2, ArrayPool var3, ContentResolver var4) {
      this(var1, DEFAULT_SERVICE, var2, var3, var4);
   }

   private String getPath(Uri param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean isValid(File var1) {
      return this.service.exists(var1) && 0L < this.service.length(var1);
   }

   int getOrientation(Uri param1) {
      // $FF: Couldn't be decompiled
   }

   public InputStream open(Uri var1) throws FileNotFoundException {
      String var2 = this.getPath(var1);
      if (TextUtils.isEmpty(var2)) {
         return null;
      } else {
         File var6 = this.service.get(var2);
         if (!this.isValid(var6)) {
            return null;
         } else {
            Uri var7 = Uri.fromFile(var6);

            try {
               InputStream var3 = this.contentResolver.openInputStream(var7);
               return var3;
            } catch (NullPointerException var5) {
               StringBuilder var4 = new StringBuilder();
               var4.append("NPE opening uri: ");
               var4.append(var1);
               var4.append(" -> ");
               var4.append(var7);
               throw (FileNotFoundException)(new FileNotFoundException(var4.toString())).initCause(var5);
            }
         }
      }
   }
}
