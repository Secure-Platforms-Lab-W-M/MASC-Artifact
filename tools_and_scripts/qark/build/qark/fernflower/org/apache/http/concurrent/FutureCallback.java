package org.apache.http.concurrent;

public interface FutureCallback {
   void cancelled();

   void completed(Object var1);

   void failed(Exception var1);
}
