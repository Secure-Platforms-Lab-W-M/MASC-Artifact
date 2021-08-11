package org.apache.commons.lang3.concurrent;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class EventCountCircuitBreaker extends AbstractCircuitBreaker {
   private static final Map STRATEGY_MAP = createStrategyMap();
   private final AtomicReference checkIntervalData;
   private final long closingInterval;
   private final int closingThreshold;
   private final long openingInterval;
   private final int openingThreshold;

   public EventCountCircuitBreaker(int var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, var1);
   }

   public EventCountCircuitBreaker(int var1, long var2, TimeUnit var4, int var5) {
      this(var1, var2, var4, var5, var2, var4);
   }

   public EventCountCircuitBreaker(int var1, long var2, TimeUnit var4, int var5, long var6, TimeUnit var8) {
      this.checkIntervalData = new AtomicReference(new EventCountCircuitBreaker.CheckIntervalData(0, 0L));
      this.openingThreshold = var1;
      this.openingInterval = var4.toNanos(var2);
      this.closingThreshold = var5;
      this.closingInterval = var8.toNanos(var6);
   }

   private void changeStateAndStartNewCheckInterval(AbstractCircuitBreaker.State var1) {
      this.changeState(var1);
      this.checkIntervalData.set(new EventCountCircuitBreaker.CheckIntervalData(0, this.now()));
   }

   private static Map createStrategyMap() {
      EnumMap var0 = new EnumMap(AbstractCircuitBreaker.State.class);
      var0.put(AbstractCircuitBreaker.State.CLOSED, new EventCountCircuitBreaker.StateStrategyClosed());
      var0.put(AbstractCircuitBreaker.State.OPEN, new EventCountCircuitBreaker.StateStrategyOpen());
      return var0;
   }

   private EventCountCircuitBreaker.CheckIntervalData nextCheckIntervalData(int var1, EventCountCircuitBreaker.CheckIntervalData var2, AbstractCircuitBreaker.State var3, long var4) {
      return stateStrategy(var3).isCheckIntervalFinished(this, var2, var4) ? new EventCountCircuitBreaker.CheckIntervalData(var1, var4) : var2.increment(var1);
   }

   private boolean performStateCheck(int var1) {
      AbstractCircuitBreaker.State var5;
      EventCountCircuitBreaker.CheckIntervalData var6;
      EventCountCircuitBreaker.CheckIntervalData var7;
      do {
         long var2 = this.now();
         var5 = (AbstractCircuitBreaker.State)this.state.get();
         var6 = (EventCountCircuitBreaker.CheckIntervalData)this.checkIntervalData.get();
         var7 = this.nextCheckIntervalData(var1, var6, var5, var2);
      } while(!this.updateCheckIntervalData(var6, var7));

      AbstractCircuitBreaker.State var4 = var5;
      if (stateStrategy(var5).isStateTransition(this, var6, var7)) {
         var4 = var5.oppositeState();
         this.changeStateAndStartNewCheckInterval(var4);
      }

      return isOpen(var4) ^ true;
   }

   private static EventCountCircuitBreaker.StateStrategy stateStrategy(AbstractCircuitBreaker.State var0) {
      return (EventCountCircuitBreaker.StateStrategy)STRATEGY_MAP.get(var0);
   }

   private boolean updateCheckIntervalData(EventCountCircuitBreaker.CheckIntervalData var1, EventCountCircuitBreaker.CheckIntervalData var2) {
      return var1 == var2 || this.checkIntervalData.compareAndSet(var1, var2);
   }

   public boolean checkState() {
      return this.performStateCheck(0);
   }

   public void close() {
      super.close();
      this.checkIntervalData.set(new EventCountCircuitBreaker.CheckIntervalData(0, this.now()));
   }

   public long getClosingInterval() {
      return this.closingInterval;
   }

   public int getClosingThreshold() {
      return this.closingThreshold;
   }

   public long getOpeningInterval() {
      return this.openingInterval;
   }

   public int getOpeningThreshold() {
      return this.openingThreshold;
   }

   public boolean incrementAndCheckState() {
      return this.incrementAndCheckState(1);
   }

   public boolean incrementAndCheckState(Integer var1) {
      return this.performStateCheck(var1);
   }

   long now() {
      return System.nanoTime();
   }

   public void open() {
      super.open();
      this.checkIntervalData.set(new EventCountCircuitBreaker.CheckIntervalData(0, this.now()));
   }

   private static class CheckIntervalData {
      private final long checkIntervalStart;
      private final int eventCount;

      CheckIntervalData(int var1, long var2) {
         this.eventCount = var1;
         this.checkIntervalStart = var2;
      }

      public long getCheckIntervalStart() {
         return this.checkIntervalStart;
      }

      public int getEventCount() {
         return this.eventCount;
      }

      public EventCountCircuitBreaker.CheckIntervalData increment(int var1) {
         return var1 == 0 ? this : new EventCountCircuitBreaker.CheckIntervalData(this.getEventCount() + var1, this.getCheckIntervalStart());
      }
   }

   private abstract static class StateStrategy {
      private StateStrategy() {
      }

      // $FF: synthetic method
      StateStrategy(Object var1) {
         this();
      }

      protected abstract long fetchCheckInterval(EventCountCircuitBreaker var1);

      public boolean isCheckIntervalFinished(EventCountCircuitBreaker var1, EventCountCircuitBreaker.CheckIntervalData var2, long var3) {
         return var3 - var2.getCheckIntervalStart() > this.fetchCheckInterval(var1);
      }

      public abstract boolean isStateTransition(EventCountCircuitBreaker var1, EventCountCircuitBreaker.CheckIntervalData var2, EventCountCircuitBreaker.CheckIntervalData var3);
   }

   private static class StateStrategyClosed extends EventCountCircuitBreaker.StateStrategy {
      private StateStrategyClosed() {
         super(null);
      }

      // $FF: synthetic method
      StateStrategyClosed(Object var1) {
         this();
      }

      protected long fetchCheckInterval(EventCountCircuitBreaker var1) {
         return var1.getOpeningInterval();
      }

      public boolean isStateTransition(EventCountCircuitBreaker var1, EventCountCircuitBreaker.CheckIntervalData var2, EventCountCircuitBreaker.CheckIntervalData var3) {
         return var3.getEventCount() > var1.getOpeningThreshold();
      }
   }

   private static class StateStrategyOpen extends EventCountCircuitBreaker.StateStrategy {
      private StateStrategyOpen() {
         super(null);
      }

      // $FF: synthetic method
      StateStrategyOpen(Object var1) {
         this();
      }

      protected long fetchCheckInterval(EventCountCircuitBreaker var1) {
         return var1.getClosingInterval();
      }

      public boolean isStateTransition(EventCountCircuitBreaker var1, EventCountCircuitBreaker.CheckIntervalData var2, EventCountCircuitBreaker.CheckIntervalData var3) {
         return var3.getCheckIntervalStart() != var2.getCheckIntervalStart() && var2.getEventCount() < var1.getClosingThreshold();
      }
   }
}
