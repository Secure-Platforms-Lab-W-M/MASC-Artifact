package org.joda.time.base;

public abstract class BaseLocal extends AbstractPartial {
   private static final long serialVersionUID = 276453175381783L;

   protected BaseLocal() {
   }

   protected abstract long getLocalMillis();
}
