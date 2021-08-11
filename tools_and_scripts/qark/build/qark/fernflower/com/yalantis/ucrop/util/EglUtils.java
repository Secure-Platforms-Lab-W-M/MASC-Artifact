package com.yalantis.ucrop.util;

import android.opengl.EGL14;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build.VERSION;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class EglUtils {
   private static final String TAG = "EglUtils";

   private EglUtils() {
   }

   private static int getMaxTextureEgl10() {
      EGL10 var0 = (EGL10)EGLContext.getEGL();
      EGLDisplay var1 = var0.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      var0.eglInitialize(var1, new int[2]);
      EGLConfig[] var2 = new EGLConfig[1];
      int[] var3 = new int[1];
      var0.eglChooseConfig(var1, new int[]{12351, 12430, 12329, 0, 12339, 1, 12344}, var2, 1, var3);
      if (var3[0] == 0) {
         return 0;
      } else {
         EGLConfig var6 = var2[0];
         EGLSurface var5 = var0.eglCreatePbufferSurface(var1, var6, new int[]{12375, 64, 12374, 64, 12344});
         EGLContext var7 = var0.eglCreateContext(var1, var6, EGL10.EGL_NO_CONTEXT, new int[]{12440, 1, 12344});
         var0.eglMakeCurrent(var1, var5, var5, var7);
         int[] var4 = new int[1];
         GLES10.glGetIntegerv(3379, var4, 0);
         var0.eglMakeCurrent(var1, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
         var0.eglDestroySurface(var1, var5);
         var0.eglDestroyContext(var1, var7);
         var0.eglTerminate(var1);
         return var4[0];
      }
   }

   private static int getMaxTextureEgl14() {
      android.opengl.EGLDisplay var0 = EGL14.eglGetDisplay(0);
      int[] var1 = new int[2];
      EGL14.eglInitialize(var0, var1, 0, var1, 1);
      android.opengl.EGLConfig[] var4 = new android.opengl.EGLConfig[1];
      int[] var2 = new int[1];
      EGL14.eglChooseConfig(var0, new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344}, 0, var4, 0, 1, var2, 0);
      if (var2[0] == 0) {
         return 0;
      } else {
         android.opengl.EGLConfig var6 = var4[0];
         android.opengl.EGLSurface var5 = EGL14.eglCreatePbufferSurface(var0, var6, new int[]{12375, 64, 12374, 64, 12344}, 0);
         android.opengl.EGLContext var7 = EGL14.eglCreateContext(var0, var6, EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
         EGL14.eglMakeCurrent(var0, var5, var5, var7);
         int[] var3 = new int[1];
         GLES20.glGetIntegerv(3379, var3, 0);
         EGL14.eglMakeCurrent(var0, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
         EGL14.eglDestroySurface(var0, var5);
         EGL14.eglDestroyContext(var0, var7);
         EGL14.eglTerminate(var0);
         return var3[0];
      }
   }

   public static int getMaxTextureSize() {
      try {
         if (VERSION.SDK_INT >= 17) {
            return getMaxTextureEgl14();
         } else {
            int var0 = getMaxTextureEgl10();
            return var0;
         }
      } catch (Exception var2) {
         Log.d("EglUtils", "getMaxTextureSize: ", var2);
         return 0;
      }
   }
}
