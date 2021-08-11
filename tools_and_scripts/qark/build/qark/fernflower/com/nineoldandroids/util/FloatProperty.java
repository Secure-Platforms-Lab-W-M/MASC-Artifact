package com.nineoldandroids.util;

public abstract class FloatProperty extends Property {
   public FloatProperty(String var1) {
      super(Float.class, var1);
   }

   public final void set(Object var1, Float var2) {
      this.setValue(var1, var2);
   }

   public abstract void setValue(Object var1, float var2);
}
