package com.karumi.dexter;

final class PermissionRationaleToken implements PermissionToken {
   private final DexterInstance dexterInstance;
   private boolean isTokenResolved = false;

   PermissionRationaleToken(DexterInstance var1) {
      this.dexterInstance = var1;
   }

   public void cancelPermissionRequest() {
      if (!this.isTokenResolved) {
         this.dexterInstance.onCancelPermissionRequest();
         this.isTokenResolved = true;
      }

   }

   public void continuePermissionRequest() {
      if (!this.isTokenResolved) {
         this.dexterInstance.onContinuePermissionRequest();
         this.isTokenResolved = true;
      }

   }
}
