package org.apache.commons.lang3.concurrent;

public interface CircuitBreaker {
   boolean checkState();

   void close();

   boolean incrementAndCheckState(Object var1);

   boolean isClosed();

   boolean isOpen();

   void open();
}
