package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MultiModelLoaderFactory {
   private static final MultiModelLoaderFactory.Factory DEFAULT_FACTORY = new MultiModelLoaderFactory.Factory();
   private static final ModelLoader EMPTY_MODEL_LOADER = new MultiModelLoaderFactory.EmptyModelLoader();
   private final Set alreadyUsedEntries;
   private final List entries;
   private final MultiModelLoaderFactory.Factory factory;
   private final Pools.Pool throwableListPool;

   public MultiModelLoaderFactory(Pools.Pool var1) {
      this(var1, DEFAULT_FACTORY);
   }

   MultiModelLoaderFactory(Pools.Pool var1, MultiModelLoaderFactory.Factory var2) {
      this.entries = new ArrayList();
      this.alreadyUsedEntries = new HashSet();
      this.throwableListPool = var1;
      this.factory = var2;
   }

   private void add(Class var1, Class var2, ModelLoaderFactory var3, boolean var4) {
      MultiModelLoaderFactory.Entry var6 = new MultiModelLoaderFactory.Entry(var1, var2, var3);
      List var7 = this.entries;
      int var5;
      if (var4) {
         var5 = var7.size();
      } else {
         var5 = 0;
      }

      var7.add(var5, var6);
   }

   private ModelLoader build(MultiModelLoaderFactory.Entry var1) {
      return (ModelLoader)Preconditions.checkNotNull(var1.factory.build(this));
   }

   private static ModelLoader emptyModelLoader() {
      return EMPTY_MODEL_LOADER;
   }

   private ModelLoaderFactory getFactory(MultiModelLoaderFactory.Entry var1) {
      return var1.factory;
   }

   void append(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.add(var1, var2, var3, true);
      } finally {
         ;
      }

   }

   public ModelLoader build(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label833: {
         ArrayList var4;
         boolean var10001;
         try {
            var4 = new ArrayList();
         } catch (Throwable var93) {
            var10000 = var93;
            var10001 = false;
            break label833;
         }

         boolean var3 = false;

         Iterator var5;
         try {
            var5 = this.entries.iterator();
         } catch (Throwable var92) {
            var10000 = var92;
            var10001 = false;
            break label833;
         }

         label816:
         while(true) {
            MultiModelLoaderFactory.Entry var6;
            while(true) {
               try {
                  if (!var5.hasNext()) {
                     break label816;
                  }

                  var6 = (MultiModelLoaderFactory.Entry)var5.next();
                  if (!this.alreadyUsedEntries.contains(var6)) {
                     break;
                  }
               } catch (Throwable var96) {
                  var10000 = var96;
                  var10001 = false;
                  break label833;
               }

               var3 = true;
            }

            try {
               if (var6.handles(var1, var2)) {
                  this.alreadyUsedEntries.add(var6);
                  var4.add(this.build(var6));
                  this.alreadyUsedEntries.remove(var6);
               }
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label833;
            }
         }

         try {
            if (var4.size() > 1) {
               MultiModelLoader var99 = this.factory.build(var4, this.throwableListPool);
               return var99;
            }
         } catch (Throwable var95) {
            var10000 = var95;
            var10001 = false;
            break label833;
         }

         ModelLoader var97;
         try {
            if (var4.size() == 1) {
               var97 = (ModelLoader)var4.get(0);
               return var97;
            }
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            break label833;
         }

         if (var3) {
            label782: {
               try {
                  var97 = emptyModelLoader();
               } catch (Throwable var89) {
                  var10000 = var89;
                  var10001 = false;
                  break label782;
               }

               return var97;
            }
         } else {
            label784:
            try {
               throw new Registry.NoModelLoaderAvailableException(var1, var2);
            } catch (Throwable var90) {
               var10000 = var90;
               var10001 = false;
               break label784;
            }
         }
      }

      Throwable var98 = var10000;

      try {
         this.alreadyUsedEntries.clear();
         throw var98;
      } finally {
         ;
      }
   }

   List build(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label205: {
         boolean var10001;
         ArrayList var2;
         Iterator var3;
         try {
            var2 = new ArrayList();
            var3 = this.entries.iterator();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label205;
         }

         label202:
         while(true) {
            MultiModelLoaderFactory.Entry var4;
            while(true) {
               try {
                  if (!var3.hasNext()) {
                     return var2;
                  }

                  var4 = (MultiModelLoaderFactory.Entry)var3.next();
                  if (!this.alreadyUsedEntries.contains(var4)) {
                     break;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label202;
               }
            }

            try {
               if (var4.handles(var1)) {
                  this.alreadyUsedEntries.add(var4);
                  var2.add(this.build(var4));
                  this.alreadyUsedEntries.remove(var4);
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var25 = var10000;

      try {
         this.alreadyUsedEntries.clear();
         throw var25;
      } finally {
         ;
      }
   }

   List getDataClasses(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label97: {
         boolean var10001;
         ArrayList var2;
         Iterator var3;
         try {
            var2 = new ArrayList();
            var3 = this.entries.iterator();
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            break label97;
         }

         while(true) {
            try {
               if (!var3.hasNext()) {
                  return var2;
               }

               MultiModelLoaderFactory.Entry var4 = (MultiModelLoaderFactory.Entry)var3.next();
               if (!var2.contains(var4.dataClass) && var4.handles(var1)) {
                  var2.add(var4.dataClass);
               }
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var11 = var10000;
      throw var11;
   }

   void prepend(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.add(var1, var2, var3, false);
      } finally {
         ;
      }

   }

   List remove(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label91: {
         boolean var10001;
         ArrayList var3;
         Iterator var4;
         try {
            var3 = new ArrayList();
            var4 = this.entries.iterator();
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label91;
         }

         while(true) {
            try {
               if (!var4.hasNext()) {
                  return var3;
               }

               MultiModelLoaderFactory.Entry var5 = (MultiModelLoaderFactory.Entry)var4.next();
               if (var5.handles(var1, var2)) {
                  var4.remove();
                  var3.add(this.getFactory(var5));
               }
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var12 = var10000;
      throw var12;
   }

   List replace(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      List var4;
      try {
         var4 = this.remove(var1, var2);
         this.append(var1, var2, var3);
      } finally {
         ;
      }

      return var4;
   }

   private static class EmptyModelLoader implements ModelLoader {
      EmptyModelLoader() {
      }

      public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
         return null;
      }

      public boolean handles(Object var1) {
         return false;
      }
   }

   private static class Entry {
      final Class dataClass;
      final ModelLoaderFactory factory;
      private final Class modelClass;

      public Entry(Class var1, Class var2, ModelLoaderFactory var3) {
         this.modelClass = var1;
         this.dataClass = var2;
         this.factory = var3;
      }

      public boolean handles(Class var1) {
         return this.modelClass.isAssignableFrom(var1);
      }

      public boolean handles(Class var1, Class var2) {
         return this.handles(var1) && this.dataClass.isAssignableFrom(var2);
      }
   }

   static class Factory {
      public MultiModelLoader build(List var1, Pools.Pool var2) {
         return new MultiModelLoader(var1, var2);
      }
   }
}
