package com.nineoldandroids.util;

public abstract class Property {
   private final String mName;
   private final Class mType;

   public Property(Class var1, String var2) {
      this.mName = var2;
      this.mType = var1;
   }

   // $FF: renamed from: of (java.lang.Class, java.lang.Class, java.lang.String) com.nineoldandroids.util.Property
   public static Property method_2(Class var0, Class var1, String var2) {
      return new ReflectiveProperty(var0, var1, var2);
   }

   public abstract Object get(Object var1);

   public String getName() {
      return this.mName;
   }

   public Class getType() {
      return this.mType;
   }

   public boolean isReadOnly() {
      return false;
   }

   public void set(Object var1, Object var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("Property ");
      var3.append(this.getName());
      var3.append(" is read-only");
      throw new UnsupportedOperationException(var3.toString());
   }
}
