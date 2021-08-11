package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ThreadUtils {
   public static final ThreadUtils.AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new ThreadUtils.AlwaysTruePredicate();

   public static Thread findThreadById(long var0) {
      Collection var2 = findThreads(new ThreadUtils.ThreadIdPredicate(var0));
      return var2.isEmpty() ? null : (Thread)var2.iterator().next();
   }

   public static Thread findThreadById(long var0, String var2) {
      boolean var3;
      if (var2 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The thread group name must not be null");
      Thread var4 = findThreadById(var0);
      return var4 != null && var4.getThreadGroup() != null && var4.getThreadGroup().getName().equals(var2) ? var4 : null;
   }

   public static Thread findThreadById(long var0, ThreadGroup var2) {
      boolean var3;
      if (var2 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The thread group must not be null");
      Thread var4 = findThreadById(var0);
      return var4 != null && var2.equals(var4.getThreadGroup()) ? var4 : null;
   }

   public static Collection findThreadGroups(ThreadGroup var0, boolean var1, ThreadUtils.ThreadGroupPredicate var2) {
      boolean var5;
      if (var0 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "The group must not be null");
      if (var2 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "The predicate must not be null");
      int var3 = var0.activeGroupCount();

      while(true) {
         ThreadGroup[] var6 = new ThreadGroup[var3 / 2 + var3 + 1];
         int var4 = var0.enumerate(var6, var1);
         if (var4 < var6.length) {
            ArrayList var7 = new ArrayList(var4);

            for(var3 = 0; var3 < var4; ++var3) {
               if (var2.test(var6[var3])) {
                  var7.add(var6[var3]);
               }
            }

            return Collections.unmodifiableCollection(var7);
         }

         var3 = var4;
      }
   }

   public static Collection findThreadGroups(ThreadUtils.ThreadGroupPredicate var0) {
      return findThreadGroups(getSystemThreadGroup(), true, var0);
   }

   public static Collection findThreadGroupsByName(String var0) {
      return findThreadGroups(new ThreadUtils.NamePredicate(var0));
   }

   public static Collection findThreads(ThreadGroup var0, boolean var1, ThreadUtils.ThreadPredicate var2) {
      boolean var5;
      if (var0 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "The group must not be null");
      if (var2 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "The predicate must not be null");
      int var3 = var0.activeCount();

      while(true) {
         Thread[] var6 = new Thread[var3 / 2 + var3 + 1];
         int var4 = var0.enumerate(var6, var1);
         if (var4 < var6.length) {
            ArrayList var7 = new ArrayList(var4);

            for(var3 = 0; var3 < var4; ++var3) {
               if (var2.test(var6[var3])) {
                  var7.add(var6[var3]);
               }
            }

            return Collections.unmodifiableCollection(var7);
         }

         var3 = var4;
      }
   }

   public static Collection findThreads(ThreadUtils.ThreadPredicate var0) {
      return findThreads(getSystemThreadGroup(), true, var0);
   }

   public static Collection findThreadsByName(String var0) {
      return findThreads(new ThreadUtils.NamePredicate(var0));
   }

   public static Collection findThreadsByName(String var0, String var1) {
      boolean var3 = true;
      boolean var2;
      if (var0 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The thread name must not be null");
      if (var1 != null) {
         var2 = var3;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The thread group name must not be null");
      Collection var4 = findThreadGroups(new ThreadUtils.NamePredicate(var1));
      if (var4.isEmpty()) {
         return Collections.emptyList();
      } else {
         ArrayList var6 = new ArrayList();
         ThreadUtils.NamePredicate var5 = new ThreadUtils.NamePredicate(var0);
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            var6.addAll(findThreads((ThreadGroup)var7.next(), false, var5));
         }

         return Collections.unmodifiableCollection(var6);
      }
   }

   public static Collection findThreadsByName(String var0, ThreadGroup var1) {
      return findThreads(var1, false, new ThreadUtils.NamePredicate(var0));
   }

   public static Collection getAllThreadGroups() {
      return findThreadGroups(ALWAYS_TRUE_PREDICATE);
   }

   public static Collection getAllThreads() {
      return findThreads(ALWAYS_TRUE_PREDICATE);
   }

   public static ThreadGroup getSystemThreadGroup() {
      ThreadGroup var0;
      for(var0 = Thread.currentThread().getThreadGroup(); var0.getParent() != null; var0 = var0.getParent()) {
      }

      return var0;
   }

   private static final class AlwaysTruePredicate implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate {
      private AlwaysTruePredicate() {
      }

      // $FF: synthetic method
      AlwaysTruePredicate(Object var1) {
         this();
      }

      public boolean test(Thread var1) {
         return true;
      }

      public boolean test(ThreadGroup var1) {
         return true;
      }
   }

   public static class NamePredicate implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate {
      private final String name;

      public NamePredicate(String var1) {
         boolean var2;
         if (var1 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         Validate.isTrue(var2, "The name must not be null");
         this.name = var1;
      }

      public boolean test(Thread var1) {
         return var1 != null && var1.getName().equals(this.name);
      }

      public boolean test(ThreadGroup var1) {
         return var1 != null && var1.getName().equals(this.name);
      }
   }

   @FunctionalInterface
   public interface ThreadGroupPredicate {
      boolean test(ThreadGroup var1);
   }

   public static class ThreadIdPredicate implements ThreadUtils.ThreadPredicate {
      private final long threadId;

      public ThreadIdPredicate(long var1) {
         if (var1 > 0L) {
            this.threadId = var1;
         } else {
            throw new IllegalArgumentException("The thread id must be greater than zero");
         }
      }

      public boolean test(Thread var1) {
         return var1 != null && var1.getId() == this.threadId;
      }
   }

   @FunctionalInterface
   public interface ThreadPredicate {
      boolean test(Thread var1);
   }
}
