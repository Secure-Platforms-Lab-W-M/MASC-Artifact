package com.karumi.dexter.listener;

public final class PermissionGrantedResponse {
   private final PermissionRequest requestedPermission;

   public PermissionGrantedResponse(PermissionRequest var1) {
      this.requestedPermission = var1;
   }

   public static PermissionGrantedResponse from(String var0) {
      return new PermissionGrantedResponse(new PermissionRequest(var0));
   }

   public String getPermissionName() {
      return this.requestedPermission.getName();
   }

   public PermissionRequest getRequestedPermission() {
      return this.requestedPermission;
   }
}
