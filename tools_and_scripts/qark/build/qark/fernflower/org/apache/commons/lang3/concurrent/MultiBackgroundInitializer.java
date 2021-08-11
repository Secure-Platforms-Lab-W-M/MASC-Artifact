package org.apache.commons.lang3.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.Validate;

public class MultiBackgroundInitializer extends BackgroundInitializer {
   private final Map childInitializers = new HashMap();

   public MultiBackgroundInitializer() {
   }

   public MultiBackgroundInitializer(ExecutorService var1) {
      super(var1);
   }

   public void addInitializer(String var1, BackgroundInitializer var2) {
      boolean var4 = true;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Name of child initializer must not be null!");
      if (var2 != null) {
         var3 = var4;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Child initializer must not be null!");
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label161: {
         try {
            if (!this.isStarted()) {
               this.childInitializers.put(var1, var2);
               return;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label161;
         }

         label155:
         try {
            throw new IllegalStateException("addInitializer() must not be called after start()!");
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label155;
         }
      }

      while(true) {
         Throwable var17 = var10000;

         try {
            throw var17;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   protected int getTaskCount() {
      int var1 = 1;

      for(Iterator var2 = this.childInitializers.values().iterator(); var2.hasNext(); var1 += ((BackgroundInitializer)var2.next()).getTaskCount()) {
      }

      return var1;
   }

   protected MultiBackgroundInitializer.MultiBackgroundInitializerResults initialize() throws Exception {
      // $FF: Couldn't be decompiled
   }

   public static class MultiBackgroundInitializerResults {
      private final Map exceptions;
      private final Map initializers;
      private final Map resultObjects;

      private MultiBackgroundInitializerResults(Map var1, Map var2, Map var3) {
         this.initializers = var1;
         this.resultObjects = var2;
         this.exceptions = var3;
      }

      // $FF: synthetic method
      MultiBackgroundInitializerResults(Map var1, Map var2, Map var3, Object var4) {
         this(var1, var2, var3);
      }

      private BackgroundInitializer checkName(String var1) {
         BackgroundInitializer var2 = (BackgroundInitializer)this.initializers.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("No child initializer with name ");
            var3.append(var1);
            throw new NoSuchElementException(var3.toString());
         }
      }

      public ConcurrentException getException(String var1) {
         this.checkName(var1);
         return (ConcurrentException)this.exceptions.get(var1);
      }

      public BackgroundInitializer getInitializer(String var1) {
         return this.checkName(var1);
      }

      public Object getResultObject(String var1) {
         this.checkName(var1);
         return this.resultObjects.get(var1);
      }

      public Set initializerNames() {
         return Collections.unmodifiableSet(this.initializers.keySet());
      }

      public boolean isException(String var1) {
         this.checkName(var1);
         return this.exceptions.containsKey(var1);
      }

      public boolean isSuccessful() {
         return this.exceptions.isEmpty();
      }
   }
}
