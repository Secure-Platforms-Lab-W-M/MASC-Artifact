package com.karumi.dexter.listener;

public final class PermissionRequest {
   private final String name;

   public PermissionRequest(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Permission name: ");
      var1.append(this.name);
      return var1.toString();
   }
}
