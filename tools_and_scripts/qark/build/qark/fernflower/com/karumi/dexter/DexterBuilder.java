package com.karumi.dexter;

import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.Collection;

public interface DexterBuilder {
   void check();

   DexterBuilder onSameThread();

   DexterBuilder withErrorListener(PermissionRequestErrorListener var1);

   public interface MultiPermissionListener {
      DexterBuilder withListener(MultiplePermissionsListener var1);
   }

   public interface Permission {
      DexterBuilder.SinglePermissionListener withPermission(String var1);

      DexterBuilder.MultiPermissionListener withPermissions(Collection var1);

      DexterBuilder.MultiPermissionListener withPermissions(String... var1);
   }

   public interface SinglePermissionListener {
      DexterBuilder withListener(PermissionListener var1);
   }
}
