package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
   private final List bucketPriorityList = new ArrayList();
   private final Map decoders = new HashMap();

   private List getOrAddEntryList(String var1) {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         try {
            if (!this.bucketPriorityList.contains(var1)) {
               this.bucketPriorityList.add(var1);
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label136;
         }

         List var3;
         try {
            var3 = (List)this.decoders.get(var1);
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label136;
         }

         Object var2 = var3;
         if (var3 != null) {
            return (List)var2;
         }

         label124:
         try {
            var2 = new ArrayList();
            this.decoders.put(var1, var2);
            return (List)var2;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label124;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public void append(String var1, ResourceDecoder var2, Class var3, Class var4) {
      synchronized(this){}

      try {
         this.getOrAddEntryList(var1).add(new ResourceDecoderRegistry.Entry(var3, var4, var2));
      } finally {
         ;
      }

   }

   public List getDecoders(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label250: {
         ArrayList var3;
         Iterator var4;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = this.bucketPriorityList.iterator();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label250;
         }

         label247:
         while(true) {
            List var28;
            try {
               if (!var4.hasNext()) {
                  return var3;
               }

               String var5 = (String)var4.next();
               var28 = (List)this.decoders.get(var5);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            if (var28 != null) {
               Iterator var29;
               try {
                  var29 = var28.iterator();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               while(true) {
                  try {
                     if (!var29.hasNext()) {
                        break;
                     }

                     ResourceDecoderRegistry.Entry var6 = (ResourceDecoderRegistry.Entry)var29.next();
                     if (var6.handles(var1, var2)) {
                        var3.add(var6.decoder);
                     }
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label247;
                  }
               }
            }
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public List getResourceClasses(Class var1, Class var2) {
      synchronized(this){}

      Throwable var10000;
      label260: {
         ArrayList var3;
         Iterator var4;
         boolean var10001;
         try {
            var3 = new ArrayList();
            var4 = this.bucketPriorityList.iterator();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label260;
         }

         label257:
         while(true) {
            List var28;
            try {
               if (!var4.hasNext()) {
                  return var3;
               }

               String var5 = (String)var4.next();
               var28 = (List)this.decoders.get(var5);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            if (var28 != null) {
               Iterator var29;
               try {
                  var29 = var28.iterator();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               while(true) {
                  try {
                     if (!var29.hasNext()) {
                        break;
                     }

                     ResourceDecoderRegistry.Entry var6 = (ResourceDecoderRegistry.Entry)var29.next();
                     if (var6.handles(var1, var2) && !var3.contains(var6.resourceClass)) {
                        var3.add(var6.resourceClass);
                     }
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label257;
                  }
               }
            }
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public void prepend(String var1, ResourceDecoder var2, Class var3, Class var4) {
      synchronized(this){}

      try {
         this.getOrAddEntryList(var1).add(0, new ResourceDecoderRegistry.Entry(var3, var4, var2));
      } finally {
         ;
      }

   }

   public void setBucketPriorityList(List var1) {
      synchronized(this){}

      Throwable var10000;
      label249: {
         ArrayList var2;
         Iterator var3;
         boolean var10001;
         try {
            var2 = new ArrayList(this.bucketPriorityList);
            this.bucketPriorityList.clear();
            var3 = var1.iterator();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label249;
         }

         label248:
         while(true) {
            try {
               if (var3.hasNext()) {
                  String var4 = (String)var3.next();
                  this.bucketPriorityList.add(var4);
                  continue;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            Iterator var26;
            try {
               var26 = var2.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            while(true) {
               try {
                  if (!var26.hasNext()) {
                     return;
                  }

                  String var27 = (String)var26.next();
                  if (!var1.contains(var27)) {
                     this.bucketPriorityList.add(var27);
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label248;
               }
            }
         }
      }

      Throwable var25 = var10000;
      throw var25;
   }

   private static class Entry {
      private final Class dataClass;
      final ResourceDecoder decoder;
      final Class resourceClass;

      public Entry(Class var1, Class var2, ResourceDecoder var3) {
         this.dataClass = var1;
         this.resourceClass = var2;
         this.decoder = var3;
      }

      public boolean handles(Class var1, Class var2) {
         return this.dataClass.isAssignableFrom(var1) && var2.isAssignableFrom(this.resourceClass);
      }
   }
}
