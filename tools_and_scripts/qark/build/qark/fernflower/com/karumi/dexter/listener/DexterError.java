package com.karumi.dexter.listener;

public enum DexterError {
   NO_PERMISSIONS_REQUESTED,
   REQUEST_ONGOING;

   static {
      DexterError var0 = new DexterError("NO_PERMISSIONS_REQUESTED", 1);
      NO_PERMISSIONS_REQUESTED = var0;
   }
}
