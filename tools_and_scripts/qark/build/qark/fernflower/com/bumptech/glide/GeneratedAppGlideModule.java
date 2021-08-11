package com.bumptech.glide;

import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Set;

abstract class GeneratedAppGlideModule extends AppGlideModule {
   abstract Set getExcludedModuleClasses();

   RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
      return null;
   }
}
