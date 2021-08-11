package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable extends Hashtable {
   private static final int MAX_CHANGES_BEFORE_PURGE = 100;
   private static final int PARTIAL_PURGE_COUNT = 10;
   private static final long serialVersionUID = -1546036869799732453L;
   private int changeCount = 0;
   private final ReferenceQueue queue = new ReferenceQueue();

   private void purge() {
      ArrayList var4 = new ArrayList();
      ReferenceQueue var3 = this.queue;
      synchronized(var3){}

      label240: {
         Throwable var10000;
         boolean var10001;
         while(true) {
            WeakHashtable.WeakKey var5;
            try {
               var5 = (WeakHashtable.WeakKey)this.queue.poll();
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            if (var5 != null) {
               try {
                  var4.add(var5.getReferenced());
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }
            } else {
               try {
                  break label240;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var26 = var10000;

            try {
               throw var26;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               continue;
            }
         }
      }

      int var2 = var4.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         super.remove(var4.get(var1));
      }

   }

   private void purgeOne() {
      ReferenceQueue var1 = this.queue;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label176: {
         WeakHashtable.WeakKey var2;
         try {
            var2 = (WeakHashtable.WeakKey)this.queue.poll();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label176;
         }

         if (var2 != null) {
            try {
               super.remove(var2.getReferenced());
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean containsKey(Object var1) {
      return super.containsKey(new WeakHashtable.Referenced(var1, (WeakHashtable$1)null));
   }

   public Enumeration elements() {
      this.purge();
      return super.elements();
   }

   public Set entrySet() {
      this.purge();
      Set var2 = super.entrySet();
      HashSet var1 = new HashSet();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         java.util.Map.Entry var4 = (java.util.Map.Entry)var5.next();
         Object var3 = ((WeakHashtable.Referenced)var4.getKey()).getValue();
         Object var6 = var4.getValue();
         if (var3 != null) {
            var1.add(new WeakHashtable.Entry(var3, var6, (WeakHashtable$1)null));
         }
      }

      return var1;
   }

   public Object get(Object var1) {
      return super.get(new WeakHashtable.Referenced(var1, (WeakHashtable$1)null));
   }

   public boolean isEmpty() {
      this.purge();
      return super.isEmpty();
   }

   public Set keySet() {
      this.purge();
      Set var2 = super.keySet();
      HashSet var1 = new HashSet();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var3 = ((WeakHashtable.Referenced)var4.next()).getValue();
         if (var3 != null) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Enumeration keys() {
      this.purge();
      return new WeakHashtable$1(this, super.keys());
   }

   public Object put(Object var1, Object var2) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != null) {
         if (var2 != null) {
            label408: {
               int var3;
               try {
                  var3 = this.changeCount;
               } catch (Throwable var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label408;
               }

               int var4 = var3 + 1;

               try {
                  this.changeCount = var4;
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label408;
               }

               if (var3 > 100) {
                  try {
                     this.purge();
                     this.changeCount = 0;
                  } catch (Throwable var56) {
                     var10000 = var56;
                     var10001 = false;
                     break label408;
                  }
               } else if (var4 % 10 == 0) {
                  try {
                     this.purgeOne();
                  } catch (Throwable var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label408;
                  }
               }

               try {
                  var1 = super.put(new WeakHashtable.Referenced(var1, this.queue, (WeakHashtable$1)null), var2);
               } catch (Throwable var54) {
                  var10000 = var54;
                  var10001 = false;
                  break label408;
               }

               return var1;
            }
         } else {
            label402:
            try {
               throw new NullPointerException("Null values are not allowed");
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label402;
            }
         }
      } else {
         label405:
         try {
            throw new NullPointerException("Null keys are not allowed");
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label405;
         }
      }

      Throwable var61 = var10000;
      throw var61;
   }

   public void putAll(Map var1) {
      if (var1 != null) {
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            java.util.Map.Entry var2 = (java.util.Map.Entry)var3.next();
            this.put(var2.getKey(), var2.getValue());
         }
      }

   }

   protected void rehash() {
      this.purge();
      super.rehash();
   }

   public Object remove(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label258: {
         boolean var10001;
         int var2;
         try {
            var2 = this.changeCount;
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label258;
         }

         int var3 = var2 + 1;

         try {
            this.changeCount = var3;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label258;
         }

         if (var2 > 100) {
            try {
               this.purge();
               this.changeCount = 0;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label258;
            }
         } else if (var3 % 10 == 0) {
            try {
               this.purgeOne();
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label258;
            }
         }

         label239:
         try {
            var1 = super.remove(new WeakHashtable.Referenced(var1, (WeakHashtable$1)null));
            return var1;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label239;
         }
      }

      Throwable var34 = var10000;
      throw var34;
   }

   public int size() {
      this.purge();
      return super.size();
   }

   public String toString() {
      this.purge();
      return super.toString();
   }

   public Collection values() {
      this.purge();
      return super.values();
   }

   private static final class Entry implements java.util.Map.Entry {
      private final Object key;
      private final Object value;

      private Entry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      // $FF: synthetic method
      Entry(Object var1, Object var2, WeakHashtable$1 var3) {
         this(var1, var2);
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         boolean var2 = var3;
         if (var1 != null) {
            var2 = var3;
            if (var1 instanceof java.util.Map.Entry) {
               label35: {
                  label26: {
                     java.util.Map.Entry var4 = (java.util.Map.Entry)var1;
                     if (this.getKey() == null) {
                        if (var4.getKey() != null) {
                           break label26;
                        }
                     } else if (!this.getKey().equals(var4.getKey())) {
                        break label26;
                     }

                     if (this.getValue() == null) {
                        if (var4.getValue() == null) {
                           break label35;
                        }
                     } else if (this.getValue().equals(var4.getValue())) {
                        break label35;
                     }
                  }

                  var2 = false;
                  return var2;
               }

               var2 = true;
            }
         }

         return var2;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public int hashCode() {
         Object var3 = this.getKey();
         int var2 = 0;
         int var1;
         if (var3 == null) {
            var1 = 0;
         } else {
            var1 = this.getKey().hashCode();
         }

         if (this.getValue() != null) {
            var2 = this.getValue().hashCode();
         }

         return var1 ^ var2;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException("Entry.setValue is not supported.");
      }
   }

   private static final class Referenced {
      private final int hashCode;
      private final WeakReference reference;

      private Referenced(Object var1) {
         this.reference = new WeakReference(var1);
         this.hashCode = var1.hashCode();
      }

      private Referenced(Object var1, ReferenceQueue var2) {
         this.reference = new WeakHashtable.WeakKey(var1, var2, this, (WeakHashtable$1)null);
         this.hashCode = var1.hashCode();
      }

      // $FF: synthetic method
      Referenced(Object var1, ReferenceQueue var2, WeakHashtable$1 var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      Referenced(Object var1, WeakHashtable$1 var2) {
         this(var1);
      }

      private Object getValue() {
         return this.reference.get();
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         if (var1 instanceof WeakHashtable.Referenced) {
            WeakHashtable.Referenced var6 = (WeakHashtable.Referenced)var1;
            Object var4 = this.getValue();
            Object var5 = var6.getValue();
            if (var4 == null) {
               var3 = true;
               boolean var2;
               if (var5 == null) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               if (!var2 || this.hashCode() != var6.hashCode()) {
                  var3 = false;
               }

               return var3;
            }

            var3 = var4.equals(var5);
         }

         return var3;
      }

      public int hashCode() {
         return this.hashCode;
      }
   }

   private static final class WeakKey extends WeakReference {
      private final WeakHashtable.Referenced referenced;

      private WeakKey(Object var1, ReferenceQueue var2, WeakHashtable.Referenced var3) {
         super(var1, var2);
         this.referenced = var3;
      }

      // $FF: synthetic method
      WeakKey(Object var1, ReferenceQueue var2, WeakHashtable.Referenced var3, WeakHashtable$1 var4) {
         this(var1, var2, var3);
      }

      private WeakHashtable.Referenced getReferenced() {
         return this.referenced;
      }
   }
}
