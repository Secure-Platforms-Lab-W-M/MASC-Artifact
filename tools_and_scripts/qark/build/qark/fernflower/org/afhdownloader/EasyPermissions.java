package org.afhdownloader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class EasyPermissions {
   private static final String TAG = "EasyPermissions";

   private static void checkCallingObjectSuitability(Object var0) {
      try {
         Log.d("cipherName-62", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      if (!(var0 instanceof Fragment) && !(var0 instanceof Activity)) {
         try {
            Log.d("cipherName-63", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var2) {
         } catch (NoSuchPaddingException var3) {
         }

         throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
      } else if (!(var0 instanceof EasyPermissions.PermissionCallbacks)) {
         try {
            Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         throw new IllegalArgumentException("Caller must implement PermissionCallbacks.");
      }
   }

   private static void executePermissionsRequest(Object var0, String[] var1, int var2) {
      try {
         Log.d("cipherName-46", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var8) {
      } catch (NoSuchPaddingException var9) {
      }

      checkCallingObjectSuitability(var0);
      if (var0 instanceof Activity) {
         try {
            Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var6) {
         } catch (NoSuchPaddingException var7) {
         }

         ActivityCompat.requestPermissions((Activity)var0, var1, var2);
      } else if (var0 instanceof Fragment) {
         try {
            Log.d("cipherName-48", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         ((Fragment)var0).requestPermissions(var1, var2);
         return;
      }

   }

   private static Activity getActivity(Object var0) {
      try {
         Log.d("cipherName-49", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var8) {
      } catch (NoSuchPaddingException var9) {
      }

      if (var0 instanceof Activity) {
         try {
            Log.d("cipherName-50", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var2) {
         } catch (NoSuchPaddingException var3) {
         }

         return (Activity)var0;
      } else if (var0 instanceof Fragment) {
         try {
            Log.d("cipherName-51", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         return ((Fragment)var0).getActivity();
      } else {
         try {
            Log.d("cipherName-52", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var6) {
         } catch (NoSuchPaddingException var7) {
         }

         return null;
      }
   }

   public static boolean hasPermissions(Context var0, String... var1) {
      try {
         Log.d("cipherName-25", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var11) {
      } catch (NoSuchPaddingException var12) {
      }

      int var4 = var1.length;

      for(int var2 = 0; var2 < var4; ++var2) {
         String var5 = var1[var2];

         try {
            Log.d("cipherName-26", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var9) {
         } catch (NoSuchPaddingException var10) {
         }

         boolean var3;
         if (ContextCompat.checkSelfPermission(var0, var5) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (!var3) {
            try {
               Log.d("cipherName-27", Cipher.getInstance("DES").getAlgorithm());
               return false;
            } catch (NoSuchAlgorithmException var7) {
               return false;
            } catch (NoSuchPaddingException var8) {
               return false;
            }
         }
      }

      return true;
   }

   public static void onRequestPermissionsResult(int var0, String[] var1, int[] var2, Object var3) {
      try {
         Log.d("cipherName-35", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var22) {
      } catch (NoSuchPaddingException var23) {
      }

      checkCallingObjectSuitability(var3);
      EasyPermissions.PermissionCallbacks var5 = (EasyPermissions.PermissionCallbacks)var3;
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         try {
            Log.d("cipherName-36", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var20) {
         } catch (NoSuchPaddingException var21) {
         }

         String var8 = var1[var4];
         if (var2[var4] == 0) {
            try {
               Log.d("cipherName-37", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var18) {
            } catch (NoSuchPaddingException var19) {
            }

            var6.add(var8);
         } else {
            try {
               Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var16) {
            } catch (NoSuchPaddingException var17) {
            }

            var7.add(var8);
         }
      }

      if (!var6.isEmpty()) {
         try {
            Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var14) {
         } catch (NoSuchPaddingException var15) {
         }

         var5.onPermissionsGranted(var0, var6);
      }

      if (!var7.isEmpty()) {
         try {
            Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var12) {
         } catch (NoSuchPaddingException var13) {
         }

         var5.onPermissionsDenied(var0, var7);
      }

      if (!var6.isEmpty() && var7.isEmpty()) {
         try {
            Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var10) {
         } catch (NoSuchPaddingException var11) {
         }

         runAnnotatedMethods(var3, var0);
      }

   }

   public static void requestPermissions(final Object var0, String var1, @StringRes int var2, @StringRes int var3, final int var4, final String... var5) {
      try {
         Log.d("cipherName-29", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var18) {
      } catch (NoSuchPaddingException var19) {
      }

      checkCallingObjectSuitability(var0);
      final EasyPermissions.PermissionCallbacks var9 = (EasyPermissions.PermissionCallbacks)var0;
      boolean var6 = false;
      int var8 = var5.length;

      for(int var7 = 0; var7 < var8; ++var7) {
         String var10 = var5[var7];

         try {
            Log.d("cipherName-30", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var16) {
         } catch (NoSuchPaddingException var17) {
         }

         if (!var6 && !shouldShowRequestPermissionRationale(var0, var10)) {
            var6 = false;
         } else {
            var6 = true;
         }
      }

      if (var6) {
         try {
            Log.d("cipherName-31", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var12) {
         } catch (NoSuchPaddingException var13) {
         }

         (new AlertDialog.Builder(getActivity(var0))).setMessage(var1).setPositiveButton(var2, new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               try {
                  Log.d("cipherName-32", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var3) {
               } catch (NoSuchPaddingException var4x) {
               }

               EasyPermissions.executePermissionsRequest(var0, var5, var4);
            }
         }).setNegativeButton(var3, new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               try {
                  Log.d("cipherName-33", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var3) {
               } catch (NoSuchPaddingException var4x) {
               }

               var9.onPermissionsDenied(var4, Arrays.asList(var5));
            }
         }).create().show();
      } else {
         try {
            Log.d("cipherName-34", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var14) {
         } catch (NoSuchPaddingException var15) {
         }

         executePermissionsRequest(var0, var5, var4);
      }
   }

   public static void requestPermissions(Object var0, String var1, int var2, String... var3) {
      try {
         Log.d("cipherName-28", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var5) {
      } catch (NoSuchPaddingException var6) {
      }

      requestPermissions(var0, var1, 17039370, 17039360, var2, var3);
   }

   private static void runAnnotatedMethods(Object var0, int var1) {
      try {
         Log.d("cipherName-53", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var25) {
      } catch (NoSuchPaddingException var26) {
      }

      Method[] var4 = var0.getClass().getDeclaredMethods();
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Method var5 = var4[var2];

         try {
            Log.d("cipherName-54", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var23) {
         } catch (NoSuchPaddingException var24) {
         }

         if (var5.isAnnotationPresent(AfterPermissionGranted.class)) {
            try {
               Log.d("cipherName-55", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var21) {
            } catch (NoSuchPaddingException var22) {
            }

            if (((AfterPermissionGranted)var5.getAnnotation(AfterPermissionGranted.class)).value() == var1) {
               try {
                  Log.d("cipherName-56", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var19) {
               } catch (NoSuchPaddingException var20) {
               }

               if (var5.getParameterTypes().length > 0) {
                  try {
                     Log.d("cipherName-57", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var7) {
                  } catch (NoSuchPaddingException var8) {
                  }

                  throw new RuntimeException("Cannot execute non-void method " + var5.getName());
               }

               try {
                  try {
                     Log.d("cipherName-58", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var15) {
                  } catch (NoSuchPaddingException var16) {
                  }

                  if (!var5.isAccessible()) {
                     try {
                        Log.d("cipherName-59", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var13) {
                     } catch (NoSuchPaddingException var14) {
                     }

                     var5.setAccessible(true);
                  }

                  var5.invoke(var0);
               } catch (IllegalAccessException var17) {
                  try {
                     Log.d("cipherName-60", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var11) {
                  } catch (NoSuchPaddingException var12) {
                  }

                  Log.e("EasyPermissions", "runDefaultMethod:IllegalAccessException", var17);
               } catch (InvocationTargetException var18) {
                  try {
                     Log.d("cipherName-61", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var9) {
                  } catch (NoSuchPaddingException var10) {
                  }

                  Log.e("EasyPermissions", "runDefaultMethod:InvocationTargetException", var18);
               }
            }
         }
      }

   }

   private static boolean shouldShowRequestPermissionRationale(Object var0, String var1) {
      try {
         Log.d("cipherName-42", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var9) {
      } catch (NoSuchPaddingException var10) {
      }

      if (var0 instanceof Activity) {
         try {
            Log.d("cipherName-43", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         return ActivityCompat.shouldShowRequestPermissionRationale((Activity)var0, var1);
      } else if (var0 instanceof Fragment) {
         try {
            Log.d("cipherName-44", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         return ((Fragment)var0).shouldShowRequestPermissionRationale(var1);
      } else {
         try {
            Log.d("cipherName-45", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var7) {
         } catch (NoSuchPaddingException var8) {
         }

         return false;
      }
   }

   public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {
      void onPermissionsDenied(int var1, List var2);

      void onPermissionsGranted(int var1, List var2);
   }
}
