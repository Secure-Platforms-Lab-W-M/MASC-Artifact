package org.apache.http.impl.client;

import java.util.concurrent.atomic.AtomicLong;

public final class FutureRequestExecutionMetrics {
   private final AtomicLong activeConnections = new AtomicLong();
   private final FutureRequestExecutionMetrics.DurationCounter failedConnections = new FutureRequestExecutionMetrics.DurationCounter();
   private final FutureRequestExecutionMetrics.DurationCounter requests = new FutureRequestExecutionMetrics.DurationCounter();
   private final AtomicLong scheduledConnections = new AtomicLong();
   private final FutureRequestExecutionMetrics.DurationCounter successfulConnections = new FutureRequestExecutionMetrics.DurationCounter();
   private final FutureRequestExecutionMetrics.DurationCounter tasks = new FutureRequestExecutionMetrics.DurationCounter();

   FutureRequestExecutionMetrics() {
   }

   public long getActiveConnectionCount() {
      return this.activeConnections.get();
   }

   AtomicLong getActiveConnections() {
      return this.activeConnections;
   }

   public long getFailedConnectionAverageDuration() {
      return this.failedConnections.averageDuration();
   }

   public long getFailedConnectionCount() {
      return this.failedConnections.count();
   }

   FutureRequestExecutionMetrics.DurationCounter getFailedConnections() {
      return this.failedConnections;
   }

   public long getRequestAverageDuration() {
      return this.requests.averageDuration();
   }

   public long getRequestCount() {
      return this.requests.count();
   }

   FutureRequestExecutionMetrics.DurationCounter getRequests() {
      return this.requests;
   }

   public long getScheduledConnectionCount() {
      return this.scheduledConnections.get();
   }

   AtomicLong getScheduledConnections() {
      return this.scheduledConnections;
   }

   public long getSuccessfulConnectionAverageDuration() {
      return this.successfulConnections.averageDuration();
   }

   public long getSuccessfulConnectionCount() {
      return this.successfulConnections.count();
   }

   FutureRequestExecutionMetrics.DurationCounter getSuccessfulConnections() {
      return this.successfulConnections;
   }

   public long getTaskAverageDuration() {
      return this.tasks.averageDuration();
   }

   public long getTaskCount() {
      return this.tasks.count();
   }

   FutureRequestExecutionMetrics.DurationCounter getTasks() {
      return this.tasks;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[activeConnections=");
      var1.append(this.activeConnections);
      var1.append(", scheduledConnections=");
      var1.append(this.scheduledConnections);
      var1.append(", successfulConnections=");
      var1.append(this.successfulConnections);
      var1.append(", failedConnections=");
      var1.append(this.failedConnections);
      var1.append(", requests=");
      var1.append(this.requests);
      var1.append(", tasks=");
      var1.append(this.tasks);
      var1.append("]");
      return var1.toString();
   }

   static class DurationCounter {
      private final AtomicLong count = new AtomicLong(0L);
      private final AtomicLong cumulativeDuration = new AtomicLong(0L);

      public long averageDuration() {
         long var3 = this.count.get();
         long var1 = 0L;
         if (var3 > 0L) {
            var1 = this.cumulativeDuration.get() / var3;
         }

         return var1;
      }

      public long count() {
         return this.count.get();
      }

      public void increment(long var1) {
         this.count.incrementAndGet();
         this.cumulativeDuration.addAndGet(System.currentTimeMillis() - var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[count=");
         var1.append(this.count());
         var1.append(", averageDuration=");
         var1.append(this.averageDuration());
         var1.append("]");
         return var1.toString();
      }
   }
}
