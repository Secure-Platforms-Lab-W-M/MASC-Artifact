package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader {
   boolean mAbandoned = false;
   boolean mContentChanged = false;
   Context mContext;
   int mId;
   Loader.OnLoadCompleteListener mListener;
   Loader.OnLoadCanceledListener mOnLoadCanceledListener;
   boolean mProcessingChange = false;
   boolean mReset = true;
   boolean mStarted = false;

   public Loader(Context var1) {
      this.mContext = var1.getApplicationContext();
   }

   public void abandon() {
      this.mAbandoned = true;
      this.onAbandon();
   }

   public boolean cancelLoad() {
      return this.onCancelLoad();
   }

   public void commitContentChanged() {
      this.mProcessingChange = false;
   }

   public String dataToString(Object var1) {
      StringBuilder var2 = new StringBuilder(64);
      DebugUtils.buildShortClassTag(var1, var2);
      var2.append("}");
      return var2.toString();
   }

   public void deliverCancellation() {
      Loader.OnLoadCanceledListener var1 = this.mOnLoadCanceledListener;
      if (var1 != null) {
         var1.onLoadCanceled(this);
      }

   }

   public void deliverResult(Object var1) {
      Loader.OnLoadCompleteListener var2 = this.mListener;
      if (var2 != null) {
         var2.onLoadComplete(this, var1);
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      var3.print(var1);
      var3.print("mId=");
      var3.print(this.mId);
      var3.print(" mListener=");
      var3.println(this.mListener);
      if (this.mStarted || this.mContentChanged || this.mProcessingChange) {
         var3.print(var1);
         var3.print("mStarted=");
         var3.print(this.mStarted);
         var3.print(" mContentChanged=");
         var3.print(this.mContentChanged);
         var3.print(" mProcessingChange=");
         var3.println(this.mProcessingChange);
      }

      if (this.mAbandoned || this.mReset) {
         var3.print(var1);
         var3.print("mAbandoned=");
         var3.print(this.mAbandoned);
         var3.print(" mReset=");
         var3.println(this.mReset);
      }

   }

   public void forceLoad() {
      this.onForceLoad();
   }

   public Context getContext() {
      return this.mContext;
   }

   public int getId() {
      return this.mId;
   }

   public boolean isAbandoned() {
      return this.mAbandoned;
   }

   public boolean isReset() {
      return this.mReset;
   }

   public boolean isStarted() {
      return this.mStarted;
   }

   protected void onAbandon() {
   }

   protected boolean onCancelLoad() {
      return false;
   }

   public void onContentChanged() {
      if (this.mStarted) {
         this.forceLoad();
      } else {
         this.mContentChanged = true;
      }
   }

   protected void onForceLoad() {
   }

   protected void onReset() {
   }

   protected void onStartLoading() {
   }

   protected void onStopLoading() {
   }

   public void registerListener(int var1, Loader.OnLoadCompleteListener var2) {
      if (this.mListener == null) {
         this.mListener = var2;
         this.mId = var1;
      } else {
         throw new IllegalStateException("There is already a listener registered");
      }
   }

   public void registerOnLoadCanceledListener(Loader.OnLoadCanceledListener var1) {
      if (this.mOnLoadCanceledListener == null) {
         this.mOnLoadCanceledListener = var1;
      } else {
         throw new IllegalStateException("There is already a listener registered");
      }
   }

   public void reset() {
      this.onReset();
      this.mReset = true;
      this.mStarted = false;
      this.mAbandoned = false;
      this.mContentChanged = false;
      this.mProcessingChange = false;
   }

   public void rollbackContentChanged() {
      if (this.mProcessingChange) {
         this.onContentChanged();
      }

   }

   public final void startLoading() {
      this.mStarted = true;
      this.mReset = false;
      this.mAbandoned = false;
      this.onStartLoading();
   }

   public void stopLoading() {
      this.mStarted = false;
      this.onStopLoading();
   }

   public boolean takeContentChanged() {
      boolean var1 = this.mContentChanged;
      this.mContentChanged = false;
      this.mProcessingChange |= var1;
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(64);
      DebugUtils.buildShortClassTag(this, var1);
      var1.append(" id=");
      var1.append(this.mId);
      var1.append("}");
      return var1.toString();
   }

   public void unregisterListener(Loader.OnLoadCompleteListener var1) {
      Loader.OnLoadCompleteListener var2 = this.mListener;
      if (var2 != null) {
         if (var2 == var1) {
            this.mListener = null;
         } else {
            throw new IllegalArgumentException("Attempting to unregister the wrong listener");
         }
      } else {
         throw new IllegalStateException("No listener register");
      }
   }

   public void unregisterOnLoadCanceledListener(Loader.OnLoadCanceledListener var1) {
      Loader.OnLoadCanceledListener var2 = this.mOnLoadCanceledListener;
      if (var2 != null) {
         if (var2 == var1) {
            this.mOnLoadCanceledListener = null;
         } else {
            throw new IllegalArgumentException("Attempting to unregister the wrong listener");
         }
      } else {
         throw new IllegalStateException("No listener register");
      }
   }

   public final class ForceLoadContentObserver extends ContentObserver {
      public ForceLoadContentObserver() {
         super(new Handler());
      }

      public boolean deliverSelfNotifications() {
         return true;
      }

      public void onChange(boolean var1) {
         Loader.this.onContentChanged();
      }
   }

   public interface OnLoadCanceledListener {
      void onLoadCanceled(Loader var1);
   }

   public interface OnLoadCompleteListener {
      void onLoadComplete(Loader var1, Object var2);
   }
}
