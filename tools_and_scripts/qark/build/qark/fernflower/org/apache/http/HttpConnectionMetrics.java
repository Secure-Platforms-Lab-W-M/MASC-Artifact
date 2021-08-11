package org.apache.http;

public interface HttpConnectionMetrics {
   Object getMetric(String var1);

   long getReceivedBytesCount();

   long getRequestCount();

   long getResponseCount();

   long getSentBytesCount();

   void reset();
}
