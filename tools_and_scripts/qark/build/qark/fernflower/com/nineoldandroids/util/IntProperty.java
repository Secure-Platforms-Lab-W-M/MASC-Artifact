package com.nineoldandroids.util;

public abstract class IntProperty extends Property {
   public IntProperty(String var1) {
      super(Integer.class, var1);
   }

   public final void set(Object var1, Integer var2) {
      this.set(var1, var2);
   }

   public abstract void setValue(Object var1, int var2);
}
