package com.bumptech.glide.load.model;

import java.util.Collections;
import java.util.Map;

public interface Headers {
   Headers DEFAULT = (new LazyHeaders.Builder()).build();
   @Deprecated
   Headers NONE = new Headers() {
      public Map getHeaders() {
         return Collections.emptyMap();
      }
   };

   Map getHeaders();
}
