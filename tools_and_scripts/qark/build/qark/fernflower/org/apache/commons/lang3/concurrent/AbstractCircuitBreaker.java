package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCircuitBreaker implements CircuitBreaker {
   public static final String PROPERTY_NAME = "open";
   private final PropertyChangeSupport changeSupport;
   protected final AtomicReference state;

   public AbstractCircuitBreaker() {
      this.state = new AtomicReference(AbstractCircuitBreaker.State.CLOSED);
      this.changeSupport = new PropertyChangeSupport(this);
   }

   protected static boolean isOpen(AbstractCircuitBreaker.State var0) {
      return var0 == AbstractCircuitBreaker.State.OPEN;
   }

   public void addChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   protected void changeState(AbstractCircuitBreaker.State var1) {
      if (this.state.compareAndSet(var1.oppositeState(), var1)) {
         this.changeSupport.firePropertyChange("open", isOpen(var1) ^ true, isOpen(var1));
      }

   }

   public abstract boolean checkState();

   public void close() {
      this.changeState(AbstractCircuitBreaker.State.CLOSED);
   }

   public abstract boolean incrementAndCheckState(Object var1);

   public boolean isClosed() {
      return this.isOpen() ^ true;
   }

   public boolean isOpen() {
      return isOpen((AbstractCircuitBreaker.State)this.state.get());
   }

   public void open() {
      this.changeState(AbstractCircuitBreaker.State.OPEN);
   }

   public void removeChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   protected static enum State {
      CLOSED {
         public AbstractCircuitBreaker.State oppositeState() {
            return OPEN;
         }
      },
      OPEN;

      static {
         AbstractCircuitBreaker.State var0 = new AbstractCircuitBreaker.State("OPEN", 1) {
            public AbstractCircuitBreaker.State oppositeState() {
               return CLOSED;
            }
         };
         OPEN = var0;
      }

      private State() {
      }

      // $FF: synthetic method
      State(Object var3) {
         this();
      }

      public abstract AbstractCircuitBreaker.State oppositeState();
   }
}
