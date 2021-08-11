package org.apache.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.io.HttpTransportMetrics;

public class HttpConnectionMetricsImpl implements HttpConnectionMetrics {
   public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
   public static final String REQUEST_COUNT = "http.request-count";
   public static final String RESPONSE_COUNT = "http.response-count";
   public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
   private final HttpTransportMetrics inTransportMetric;
   private Map metricsCache;
   private final HttpTransportMetrics outTransportMetric;
   private long requestCount = 0L;
   private long responseCount = 0L;

   public HttpConnectionMetricsImpl(HttpTransportMetrics var1, HttpTransportMetrics var2) {
      this.inTransportMetric = var1;
      this.outTransportMetric = var2;
   }

   public Object getMetric(String var1) {
      Object var3 = null;
      Map var4 = this.metricsCache;
      if (var4 != null) {
         var3 = var4.get(var1);
      }

      if (var3 == null) {
         if ("http.request-count".equals(var1)) {
            return this.requestCount;
         }

         if ("http.response-count".equals(var1)) {
            return this.responseCount;
         }

         boolean var2 = "http.received-bytes-count".equals(var1);
         Object var5 = null;
         var4 = null;
         Long var6;
         HttpTransportMetrics var7;
         if (var2) {
            var7 = this.inTransportMetric;
            var6 = var4;
            if (var7 != null) {
               var6 = var7.getBytesTransferred();
            }

            return var6;
         }

         if ("http.sent-bytes-count".equals(var1)) {
            var7 = this.outTransportMetric;
            var6 = (Long)var5;
            if (var7 != null) {
               var6 = var7.getBytesTransferred();
            }

            return var6;
         }
      }

      return var3;
   }

   public long getReceivedBytesCount() {
      HttpTransportMetrics var1 = this.inTransportMetric;
      return var1 != null ? var1.getBytesTransferred() : -1L;
   }

   public long getRequestCount() {
      return this.requestCount;
   }

   public long getResponseCount() {
      return this.responseCount;
   }

   public long getSentBytesCount() {
      HttpTransportMetrics var1 = this.outTransportMetric;
      return var1 != null ? var1.getBytesTransferred() : -1L;
   }

   public void incrementRequestCount() {
      ++this.requestCount;
   }

   public void incrementResponseCount() {
      ++this.responseCount;
   }

   public void reset() {
      HttpTransportMetrics var1 = this.outTransportMetric;
      if (var1 != null) {
         var1.reset();
      }

      var1 = this.inTransportMetric;
      if (var1 != null) {
         var1.reset();
      }

      this.requestCount = 0L;
      this.responseCount = 0L;
      this.metricsCache = null;
   }

   public void setMetric(String var1, Object var2) {
      if (this.metricsCache == null) {
         this.metricsCache = new HashMap();
      }

      this.metricsCache.put(var1, var2);
   }
}
