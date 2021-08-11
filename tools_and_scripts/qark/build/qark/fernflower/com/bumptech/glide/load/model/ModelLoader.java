package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.util.Preconditions;
import java.util.Collections;
import java.util.List;

public interface ModelLoader {
   ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4);

   boolean handles(Object var1);

   public static class LoadData {
      public final List alternateKeys;
      public final DataFetcher fetcher;
      public final Key sourceKey;

      public LoadData(Key var1, DataFetcher var2) {
         this(var1, Collections.emptyList(), var2);
      }

      public LoadData(Key var1, List var2, DataFetcher var3) {
         this.sourceKey = (Key)Preconditions.checkNotNull(var1);
         this.alternateKeys = (List)Preconditions.checkNotNull(var2);
         this.fetcher = (DataFetcher)Preconditions.checkNotNull(var3);
      }
   }
}
