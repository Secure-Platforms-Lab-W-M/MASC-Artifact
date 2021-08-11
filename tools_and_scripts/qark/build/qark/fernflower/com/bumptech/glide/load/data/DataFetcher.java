package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;

public interface DataFetcher {
   void cancel();

   void cleanup();

   Class getDataClass();

   DataSource getDataSource();

   void loadData(Priority var1, DataFetcher.DataCallback var2);

   public interface DataCallback {
      void onDataReady(Object var1);

      void onLoadFailed(Exception var1);
   }
}
