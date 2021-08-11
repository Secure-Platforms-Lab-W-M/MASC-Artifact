package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher {
   public FileDescriptorLocalUriFetcher(ContentResolver var1, Uri var2) {
      super(var1, var2);
   }

   protected void close(ParcelFileDescriptor var1) throws IOException {
      var1.close();
   }

   public Class getDataClass() {
      return ParcelFileDescriptor.class;
   }

   protected ParcelFileDescriptor loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException {
      AssetFileDescriptor var3 = var2.openAssetFileDescriptor(var1, "r");
      if (var3 != null) {
         return var3.getParcelFileDescriptor();
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("FileDescriptor is null for: ");
         var4.append(var1);
         throw new FileNotFoundException(var4.toString());
      }
   }
}
