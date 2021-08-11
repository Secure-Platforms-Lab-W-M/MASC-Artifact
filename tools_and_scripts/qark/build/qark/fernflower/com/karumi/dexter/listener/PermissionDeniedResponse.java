package com.karumi.dexter.listener;

public final class PermissionDeniedResponse {
   private final boolean permanentlyDenied;
   private final PermissionRequest requestedPermission;

   public PermissionDeniedResponse(PermissionRequest var1, boolean var2) {
      this.requestedPermission = var1;
      this.permanentlyDenied = var2;
   }

   public static PermissionDeniedResponse from(String var0, boolean var1) {
      return new PermissionDeniedResponse(new PermissionRequest(var0), var1);
   }

   public String getPermissionName() {
      return this.requestedPermission.getName();
   }

   public PermissionRequest getRequestedPermission() {
      return this.requestedPermission;
   }

   public boolean isPermanentlyDenied() {
      return this.permanentlyDenied;
   }
}
