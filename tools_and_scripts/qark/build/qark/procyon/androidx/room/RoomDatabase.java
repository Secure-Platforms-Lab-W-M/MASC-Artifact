// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.room;

import java.util.concurrent.Callable;
import androidx.sqlite.db.SimpleSQLiteQuery;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.annotation.CallSuper;
import android.os.Build$VERSION;
import java.util.concurrent.locks.Lock;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import android.os.Looper;
import java.util.concurrent.Executor;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.locks.ReentrantLock;
import androidx.annotation.Nullable;
import java.util.List;
import androidx.annotation.RestrictTo;

public abstract class RoomDatabase
{
    private static final String DB_IMPL_SUFFIX = "_Impl";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int MAX_BIND_PARAMETER_CNT = 999;
    private boolean mAllowMainThreadQueries;
    @Deprecated
    @Nullable
    protected List<RoomDatabase.RoomDatabase$Callback> mCallbacks;
    private final ReentrantLock mCloseLock;
    @Deprecated
    protected volatile SupportSQLiteDatabase mDatabase;
    private final InvalidationTracker mInvalidationTracker;
    private SupportSQLiteOpenHelper mOpenHelper;
    private Executor mQueryExecutor;
    boolean mWriteAheadLoggingEnabled;
    
    public RoomDatabase() {
        this.mCloseLock = new ReentrantLock();
        this.mInvalidationTracker = this.createInvalidationTracker();
    }
    
    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void assertNotMainThread() {
        if (this.mAllowMainThreadQueries) {
            return;
        }
        if (!isMainThread()) {
            return;
        }
        throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
    }
    
    public void beginTransaction() {
        this.assertNotMainThread();
        final SupportSQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        this.mInvalidationTracker.syncTriggers(writableDatabase);
        writableDatabase.beginTransaction();
    }
    
    @WorkerThread
    public abstract void clearAllTables();
    
    public void close() {
        if (this.isOpen()) {
            try {
                this.mCloseLock.lock();
                this.mInvalidationTracker.stopMultiInstanceInvalidation();
                this.mOpenHelper.close();
            }
            finally {
                this.mCloseLock.unlock();
            }
        }
    }
    
    public SupportSQLiteStatement compileStatement(@NonNull final String s) {
        this.assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().compileStatement(s);
    }
    
    @NonNull
    protected abstract InvalidationTracker createInvalidationTracker();
    
    @NonNull
    protected abstract SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration p0);
    
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
    public void init(@NonNull final DatabaseConfiguration databaseConfiguration) {
        this.mOpenHelper = this.createOpenHelper(databaseConfiguration);
        boolean b = false;
        if (Build$VERSION.SDK_INT >= 16) {
            b = (databaseConfiguration.journalMode == RoomDatabase.RoomDatabase$JournalMode.WRITE_AHEAD_LOGGING);
            this.mOpenHelper.setWriteAheadLoggingEnabled(b);
        }
        this.mCallbacks = (List<RoomDatabase.RoomDatabase$Callback>)databaseConfiguration.callbacks;
        this.mQueryExecutor = databaseConfiguration.queryExecutor;
        this.mAllowMainThreadQueries = databaseConfiguration.allowMainThreadQueries;
        this.mWriteAheadLoggingEnabled = b;
        if (databaseConfiguration.multiInstanceInvalidation) {
            this.mInvalidationTracker.startMultiInstanceInvalidation(databaseConfiguration.context, databaseConfiguration.name);
        }
    }
    
    protected void internalInitInvalidationTracker(@NonNull final SupportSQLiteDatabase supportSQLiteDatabase) {
        this.mInvalidationTracker.internalInit(supportSQLiteDatabase);
    }
    
    public boolean isOpen() {
        final SupportSQLiteDatabase mDatabase = this.mDatabase;
        return mDatabase != null && mDatabase.isOpen();
    }
    
    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery) {
        this.assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().query(supportSQLiteQuery);
    }
    
    public Cursor query(final String s, @Nullable final Object[] array) {
        return this.mOpenHelper.getWritableDatabase().query((SupportSQLiteQuery)new SimpleSQLiteQuery(s, array));
    }
    
    public <V> V runInTransaction(@NonNull final Callable<V> callable) {
        this.beginTransaction();
        try {
            try {
                final V call = callable.call();
                this.setTransactionSuccessful();
                this.endTransaction();
                return call;
            }
            finally {}
        }
        catch (Exception ex) {
            throw new RuntimeException("Exception in transaction", ex);
        }
        catch (RuntimeException ex2) {
            throw ex2;
        }
        this.endTransaction();
    }
    
    public void runInTransaction(@NonNull final Runnable runnable) {
        this.beginTransaction();
        try {
            runnable.run();
            this.setTransactionSuccessful();
        }
        finally {
            this.endTransaction();
        }
    }
    
    public void setTransactionSuccessful() {
        this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
    }
}
