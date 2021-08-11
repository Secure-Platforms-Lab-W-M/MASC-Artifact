package com.bumptech.glide.provider;

import com.bumptech.glide.load.ImageHeaderParser;
import java.util.ArrayList;
import java.util.List;

public final class ImageHeaderParserRegistry {
   private final List parsers = new ArrayList();

   public void add(ImageHeaderParser var1) {
      synchronized(this){}

      try {
         this.parsers.add(var1);
      } finally {
         ;
      }

   }

   public List getParsers() {
      synchronized(this){}

      List var1;
      try {
         var1 = this.parsers;
      } finally {
         ;
      }

      return var1;
   }
}
