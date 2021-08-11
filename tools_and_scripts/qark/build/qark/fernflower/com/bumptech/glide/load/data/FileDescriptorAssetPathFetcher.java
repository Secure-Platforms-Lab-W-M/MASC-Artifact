package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.os.ParcelFileDescriptor;
import java.io.IOException;

public class FileDescriptorAssetPathFetcher extends AssetPathFetcher {
   public FileDescriptorAssetPathFetcher(AssetManager var1, String var2) {
      super(var1, var2);
   }

   protected void close(ParcelFileDescriptor var1) throws IOException {
      var1.close();
   }

   public Class getDataClass() {
      return ParcelFileDescriptor.class;
   }

   protected ParcelFileDescriptor loadResource(AssetManager var1, String var2) throws IOException {
      return var1.openFd(var2).getParcelFileDescriptor();
   }
}
