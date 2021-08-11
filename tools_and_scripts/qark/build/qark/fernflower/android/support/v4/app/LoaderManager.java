package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class LoaderManager {
   public static void enableDebugLogging(boolean var0) {
      LoaderManagerImpl.DEBUG = var0;
   }

   public abstract void destroyLoader(int var1);

   public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

   public abstract Loader getLoader(int var1);

   public boolean hasRunningLoaders() {
      return false;
   }

   public abstract Loader initLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3);

   public abstract Loader restartLoader(int var1, Bundle var2, LoaderManager.LoaderCallbacks var3);

   public interface LoaderCallbacks {
      Loader onCreateLoader(int var1, Bundle var2);

      void onLoadFinished(Loader var1, Object var2);

      void onLoaderReset(Loader var1);
   }
}
