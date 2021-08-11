package androidx.room;

import android.database.Cursor;
import android.os.Looper;
import android.os.Build.VERSION;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;
import androidx.room.RoomDatabase.JournalMode;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RoomDatabase {
   private static final String DB_IMPL_SUFFIX = "_Impl";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int MAX_BIND_PARAMETER_CNT = 999;
   private boolean mAllowMainThreadQueries;
   @Deprecated
   @Nullable
   protected List mCallbacks;
   private final ReentrantLock mCloseLock = new ReentrantLock();
   @Deprecated
   protected volatile SupportSQLiteDatabase mDatabase;
   private final InvalidationTracker mInvalidationTracker = this.createInvalidationTracker();
   private SupportSQLiteOpenHelper mOpenHelper;
   private Executor mQueryExecutor;
   boolean mWriteAheadLoggingEnabled;

   private static boolean isMainThread() {
      return Looper.getMainLooper().getThread() == Thread.currentThread();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void assertNotMainThread() {
      if (!this.mAllowMainThreadQueries) {
         if (isMainThread()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
         }
      }
   }

   public void beginTransaction() {
      this.assertNotMainThread();
      SupportSQLiteDatabase var1 = this.mOpenHelper.getWritableDatabase();
      this.mInvalidationTracker.syncTriggers(var1);
      var1.beginTransaction();
   }

   @WorkerThread
   public abstract void clearAllTables();

   public void close() {
      if (this.isOpen()) {
         try {
            this.mCloseLock.lock();
            this.mInvalidationTracker.stopMultiInstanceInvalidation();
            this.mOpenHelper.close();
         } finally {
            this.mCloseLock.unlock();
         }

      }
   }

   public SupportSQLiteStatement compileStatement(@NonNull String var1) {
      this.assertNotMainThread();
      return this.mOpenHelper.getWritableDatabase().compileStatement(var1);
   }

   @NonNull
   protected abstract InvalidationTracker createInvalidationTracker();

   @NonNull
   protected abstract SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1);

   public void endTransaction() {
      this.mOpenHelper.getWritableDatabase().endTransaction();
      if (!this.inTransaction()) {
         this.mInvalidationTracker.refreshVersionsAsync();
      }
   }

   Lock getCloseLock() {
      return this.mCloseLock;
   }

   @NonNull
   public InvalidationTracker getInvalidationTracker() {
      return this.mInvalidationTracker;
   }

   @NonNull
   public SupportSQLiteOpenHelper getOpenHelper() {
      return this.mOpenHelper;
   }

   @NonNull
   public Executor getQueryExecutor() {
      return this.mQueryExecutor;
   }

   public boolean inTransaction() {
      return this.mOpenHelper.getWritableDatabase().inTransaction();
   }

   @CallSuper
   public void init(@NonNull DatabaseConfiguration var1) {
      this.mOpenHelper = this.createOpenHelper(var1);
      boolean var2 = false;
      if (VERSION.SDK_INT >= 16) {
         if (var1.journalMode == JournalMode.WRITE_AHEAD_LOGGING) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mOpenHelper.setWriteAheadLoggingEnabled(var2);
      }

      this.mCallbacks = var1.callbacks;
      this.mQueryExecutor = var1.queryExecutor;
      this.mAllowMainThreadQueries = var1.allowMainThreadQueries;
      this.mWriteAheadLoggingEnabled = var2;
      if (var1.multiInstanceInvalidation) {
         this.mInvalidationTracker.startMultiInstanceInvalidation(var1.context, var1.name);
      }
   }

   protected void internalInitInvalidationTracker(@NonNull SupportSQLiteDatabase var1) {
      this.mInvalidationTracker.internalInit(var1);
   }

   public boolean isOpen() {
      SupportSQLiteDatabase var1 = this.mDatabase;
      return var1 != null && var1.isOpen();
   }

   public Cursor query(SupportSQLiteQuery var1) {
      this.assertNotMainThread();
      return this.mOpenHelper.getWritableDatabase().query(var1);
   }

   public Cursor query(String var1, @Nullable Object[] var2) {
      return this.mOpenHelper.getWritableDatabase().query(new SimpleSQLiteQuery(var1, var2));
   }

   public Object runInTransaction(@NonNull Callable var1) {
      this.beginTransaction();

      Object var8;
      try {
         var8 = var1.call();
         this.setTransactionSuccessful();
      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new RuntimeException("Exception in transaction", var6);
      } finally {
         this.endTransaction();
      }

      return var8;
   }

   public void runInTransaction(@NonNull Runnable var1) {
      this.beginTransaction();

      try {
         var1.run();
         this.setTransactionSuccessful();
      } finally {
         this.endTransaction();
      }

   }

   public void setTransactionSuccessful() {
      this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
   }
}
