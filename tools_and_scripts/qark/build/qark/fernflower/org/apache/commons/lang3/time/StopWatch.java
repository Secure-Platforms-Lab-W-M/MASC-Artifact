package org.apache.commons.lang3.time;

import java.util.concurrent.TimeUnit;

public class StopWatch {
   private static final long NANO_2_MILLIS = 1000000L;
   private StopWatch.State runningState;
   private StopWatch.SplitState splitState;
   private long startTime;
   private long startTimeMillis;
   private long stopTime;

   public StopWatch() {
      this.runningState = StopWatch.State.UNSTARTED;
      this.splitState = StopWatch.SplitState.UNSPLIT;
   }

   public static StopWatch createStarted() {
      StopWatch var0 = new StopWatch();
      var0.start();
      return var0;
   }

   public long getNanoTime() {
      if (this.runningState != StopWatch.State.STOPPED && this.runningState != StopWatch.State.SUSPENDED) {
         if (this.runningState == StopWatch.State.UNSTARTED) {
            return 0L;
         } else if (this.runningState == StopWatch.State.RUNNING) {
            return System.nanoTime() - this.startTime;
         } else {
            throw new RuntimeException("Illegal running state has occurred.");
         }
      } else {
         return this.stopTime - this.startTime;
      }
   }

   public long getSplitNanoTime() {
      if (this.splitState == StopWatch.SplitState.SPLIT) {
         return this.stopTime - this.startTime;
      } else {
         throw new IllegalStateException("Stopwatch must be split to get the split time. ");
      }
   }

   public long getSplitTime() {
      return this.getSplitNanoTime() / 1000000L;
   }

   public long getStartTime() {
      if (this.runningState != StopWatch.State.UNSTARTED) {
         return this.startTimeMillis;
      } else {
         throw new IllegalStateException("Stopwatch has not been started");
      }
   }

   public long getTime() {
      return this.getNanoTime() / 1000000L;
   }

   public long getTime(TimeUnit var1) {
      return var1.convert(this.getNanoTime(), TimeUnit.NANOSECONDS);
   }

   public boolean isStarted() {
      return this.runningState.isStarted();
   }

   public boolean isStopped() {
      return this.runningState.isStopped();
   }

   public boolean isSuspended() {
      return this.runningState.isSuspended();
   }

   public void reset() {
      this.runningState = StopWatch.State.UNSTARTED;
      this.splitState = StopWatch.SplitState.UNSPLIT;
   }

   public void resume() {
      if (this.runningState == StopWatch.State.SUSPENDED) {
         this.startTime += System.nanoTime() - this.stopTime;
         this.runningState = StopWatch.State.RUNNING;
      } else {
         throw new IllegalStateException("Stopwatch must be suspended to resume. ");
      }
   }

   public void split() {
      if (this.runningState == StopWatch.State.RUNNING) {
         this.stopTime = System.nanoTime();
         this.splitState = StopWatch.SplitState.SPLIT;
      } else {
         throw new IllegalStateException("Stopwatch is not running. ");
      }
   }

   public void start() {
      if (this.runningState != StopWatch.State.STOPPED) {
         if (this.runningState == StopWatch.State.UNSTARTED) {
            this.startTime = System.nanoTime();
            this.startTimeMillis = System.currentTimeMillis();
            this.runningState = StopWatch.State.RUNNING;
         } else {
            throw new IllegalStateException("Stopwatch already started. ");
         }
      } else {
         throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
      }
   }

   public void stop() {
      if (this.runningState != StopWatch.State.RUNNING && this.runningState != StopWatch.State.SUSPENDED) {
         throw new IllegalStateException("Stopwatch is not running. ");
      } else {
         if (this.runningState == StopWatch.State.RUNNING) {
            this.stopTime = System.nanoTime();
         }

         this.runningState = StopWatch.State.STOPPED;
      }
   }

   public void suspend() {
      if (this.runningState == StopWatch.State.RUNNING) {
         this.stopTime = System.nanoTime();
         this.runningState = StopWatch.State.SUSPENDED;
      } else {
         throw new IllegalStateException("Stopwatch must be running to suspend. ");
      }
   }

   public String toSplitString() {
      return DurationFormatUtils.formatDurationHMS(this.getSplitTime());
   }

   public String toString() {
      return DurationFormatUtils.formatDurationHMS(this.getTime());
   }

   public void unsplit() {
      if (this.splitState == StopWatch.SplitState.SPLIT) {
         this.splitState = StopWatch.SplitState.UNSPLIT;
      } else {
         throw new IllegalStateException("Stopwatch has not been split. ");
      }
   }

   private static enum SplitState {
      SPLIT,
      UNSPLIT;

      static {
         StopWatch.SplitState var0 = new StopWatch.SplitState("UNSPLIT", 1);
         UNSPLIT = var0;
      }
   }

   private static enum State {
      RUNNING {
         boolean isStarted() {
            return true;
         }

         boolean isStopped() {
            return false;
         }

         boolean isSuspended() {
            return false;
         }
      },
      STOPPED {
         boolean isStarted() {
            return false;
         }

         boolean isStopped() {
            return true;
         }

         boolean isSuspended() {
            return false;
         }
      },
      SUSPENDED,
      UNSTARTED {
         boolean isStarted() {
            return false;
         }

         boolean isStopped() {
            return true;
         }

         boolean isSuspended() {
            return false;
         }
      };

      static {
         StopWatch.State var0 = new StopWatch.State("SUSPENDED", 3) {
            boolean isStarted() {
               return true;
            }

            boolean isStopped() {
               return false;
            }

            boolean isSuspended() {
               return true;
            }
         };
         SUSPENDED = var0;
      }

      private State() {
      }

      // $FF: synthetic method
      State(Object var3) {
         this();
      }

      abstract boolean isStarted();

      abstract boolean isStopped();

      abstract boolean isSuspended();
   }
}
