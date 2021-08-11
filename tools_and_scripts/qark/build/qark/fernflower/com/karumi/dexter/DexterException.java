package com.karumi.dexter;

import com.karumi.dexter.listener.DexterError;

final class DexterException extends IllegalStateException {
   final DexterError error;

   DexterException(String var1, DexterError var2) {
      super(var1);
      this.error = var2;
   }
}
