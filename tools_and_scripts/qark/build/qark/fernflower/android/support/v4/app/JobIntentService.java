package android.support.v4.app;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.app.job.JobInfo.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.os.PowerManager.WakeLock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class JobIntentService extends Service {
   static final boolean DEBUG = false;
   static final String TAG = "JobIntentService";
   static final HashMap sClassWorkEnqueuer = new HashMap();
   static final Object sLock = new Object();
   final ArrayList mCompatQueue;
   JobIntentService.WorkEnqueuer mCompatWorkEnqueuer;
   JobIntentService.CommandProcessor mCurProcessor;
   boolean mDestroyed = false;
   boolean mInterruptIfStopped = false;
   JobIntentService.CompatJobEngine mJobImpl;
   boolean mStopped = false;

   public JobIntentService() {
      if (VERSION.SDK_INT >= 26) {
         this.mCompatQueue = null;
      } else {
         this.mCompatQueue = new ArrayList();
      }
   }

   public static void enqueueWork(@NonNull Context param0, @NonNull ComponentName param1, int param2, @NonNull Intent param3) {
      // $FF: Couldn't be decompiled
   }

   public static void enqueueWork(@NonNull Context var0, @NonNull Class var1, int var2, @NonNull Intent var3) {
      enqueueWork(var0, new ComponentName(var0, var1), var2, var3);
   }

   static JobIntentService.WorkEnqueuer getWorkEnqueuer(Context var0, ComponentName var1, boolean var2, int var3) {
      JobIntentService.WorkEnqueuer var5 = (JobIntentService.WorkEnqueuer)sClassWorkEnqueuer.get(var1);
      Object var4 = var5;
      if (var5 == null) {
         Object var6;
         if (VERSION.SDK_INT >= 26) {
            if (!var2) {
               throw new IllegalArgumentException("Can't be here without a job id");
            }

            var6 = new JobIntentService.JobWorkEnqueuer(var0, var1, var3);
         } else {
            var6 = new JobIntentService.CompatWorkEnqueuer(var0, var1);
         }

         sClassWorkEnqueuer.put(var1, var6);
         var4 = var6;
      }

      return (JobIntentService.WorkEnqueuer)var4;
   }

   JobIntentService.GenericWorkItem dequeueWork() {
      JobIntentService.CompatJobEngine var1 = this.mJobImpl;
      if (var1 != null) {
         return var1.dequeueWork();
      } else {
         ArrayList var15 = this.mCompatQueue;
         synchronized(var15){}

         Throwable var10000;
         boolean var10001;
         label137: {
            try {
               if (this.mCompatQueue.size() > 0) {
                  JobIntentService.GenericWorkItem var16 = (JobIntentService.GenericWorkItem)this.mCompatQueue.remove(0);
                  return var16;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label137;
            }

            label131:
            try {
               return null;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label131;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }

   boolean doStopCurrentWork() {
      JobIntentService.CommandProcessor var1 = this.mCurProcessor;
      if (var1 != null) {
         var1.cancel(this.mInterruptIfStopped);
      }

      this.mStopped = true;
      return this.onStopCurrentWork();
   }

   void ensureProcessorRunningLocked(boolean var1) {
      if (this.mCurProcessor == null) {
         this.mCurProcessor = new JobIntentService.CommandProcessor();
         JobIntentService.WorkEnqueuer var2 = this.mCompatWorkEnqueuer;
         if (var2 != null && var1) {
            var2.serviceProcessingStarted();
         }

         this.mCurProcessor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
      }

   }

   public boolean isStopped() {
      return this.mStopped;
   }

   public IBinder onBind(@NonNull Intent var1) {
      JobIntentService.CompatJobEngine var2 = this.mJobImpl;
      return var2 != null ? var2.compatGetBinder() : null;
   }

   public void onCreate() {
      super.onCreate();
      if (VERSION.SDK_INT >= 26) {
         this.mJobImpl = new JobIntentService.JobServiceEngineImpl(this);
         this.mCompatWorkEnqueuer = null;
      } else {
         this.mJobImpl = null;
         this.mCompatWorkEnqueuer = getWorkEnqueuer(this, new ComponentName(this, this.getClass()), false, 0);
      }
   }

   public void onDestroy() {
      // $FF: Couldn't be decompiled
   }

   protected abstract void onHandleWork(@NonNull Intent var1);

   public int onStartCommand(@Nullable Intent var1, int var2, int var3) {
      if (this.mCompatQueue != null) {
         this.mCompatWorkEnqueuer.serviceStartReceived();
         ArrayList var4 = this.mCompatQueue;
         synchronized(var4){}

         Throwable var10000;
         boolean var10001;
         label197: {
            ArrayList var5;
            try {
               var5 = this.mCompatQueue;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label197;
            }

            if (var1 == null) {
               try {
                  var1 = new Intent();
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label197;
               }
            }

            label184:
            try {
               var5.add(new JobIntentService.CompatWorkItem(var1, var3));
               this.ensureProcessorRunningLocked(true);
               return 3;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label184;
            }
         }

         while(true) {
            Throwable var26 = var10000;

            try {
               throw var26;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               continue;
            }
         }
      } else {
         return 2;
      }
   }

   public boolean onStopCurrentWork() {
      return true;
   }

   void processorFinished() {
      ArrayList var1 = this.mCompatQueue;
      if (var1 != null) {
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label235: {
            label242: {
               try {
                  this.mCurProcessor = null;
                  if (this.mCompatQueue != null && this.mCompatQueue.size() > 0) {
                     this.ensureProcessorRunningLocked(false);
                     break label242;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label235;
               }

               try {
                  if (!this.mDestroyed) {
                     this.mCompatWorkEnqueuer.serviceProcessingFinished();
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label235;
               }
            }

            label223:
            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label223;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void setInterruptIfStopped(boolean var1) {
      this.mInterruptIfStopped = var1;
   }

   final class CommandProcessor extends AsyncTask {
      protected Void doInBackground(Void... var1) {
         while(true) {
            JobIntentService.GenericWorkItem var2 = JobIntentService.this.dequeueWork();
            if (var2 == null) {
               return null;
            }

            JobIntentService.this.onHandleWork(var2.getIntent());
            var2.complete();
         }
      }

      protected void onCancelled(Void var1) {
         JobIntentService.this.processorFinished();
      }

      protected void onPostExecute(Void var1) {
         JobIntentService.this.processorFinished();
      }
   }

   interface CompatJobEngine {
      IBinder compatGetBinder();

      JobIntentService.GenericWorkItem dequeueWork();
   }

   static final class CompatWorkEnqueuer extends JobIntentService.WorkEnqueuer {
      private final Context mContext;
      private final WakeLock mLaunchWakeLock;
      boolean mLaunchingService;
      private final WakeLock mRunWakeLock;
      boolean mServiceProcessing;

      CompatWorkEnqueuer(Context var1, ComponentName var2) {
         super(var1, var2);
         this.mContext = var1.getApplicationContext();
         PowerManager var4 = (PowerManager)var1.getSystemService("power");
         StringBuilder var3 = new StringBuilder();
         var3.append(var2.getClassName());
         var3.append(":launch");
         this.mLaunchWakeLock = var4.newWakeLock(1, var3.toString());
         this.mLaunchWakeLock.setReferenceCounted(false);
         var3 = new StringBuilder();
         var3.append(var2.getClassName());
         var3.append(":run");
         this.mRunWakeLock = var4.newWakeLock(1, var3.toString());
         this.mRunWakeLock.setReferenceCounted(false);
      }

      void enqueueWork(Intent var1) {
         var1 = new Intent(var1);
         var1.setComponent(this.mComponentName);
         if (this.mContext.startService(var1) != null) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label144: {
               try {
                  if (!this.mLaunchingService) {
                     this.mLaunchingService = true;
                     if (!this.mServiceProcessing) {
                        this.mLaunchWakeLock.acquire(60000L);
                     }
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label144;
               }

               label141:
               try {
                  return;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label141;
               }
            }

            while(true) {
               Throwable var14 = var10000;

               try {
                  throw var14;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      public void serviceProcessingFinished() {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label197: {
            label196: {
               try {
                  if (!this.mServiceProcessing) {
                     break label196;
                  }

                  if (this.mLaunchingService) {
                     this.mLaunchWakeLock.acquire(60000L);
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label197;
               }

               try {
                  this.mServiceProcessing = false;
                  this.mRunWakeLock.release();
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label197;
               }
            }

            label189:
            try {
               return;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label189;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      }

      public void serviceProcessingStarted() {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (!this.mServiceProcessing) {
                  this.mServiceProcessing = true;
                  this.mRunWakeLock.acquire(600000L);
                  this.mLaunchWakeLock.release();
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }

      public void serviceStartReceived() {
         // $FF: Couldn't be decompiled
      }
   }

   final class CompatWorkItem implements JobIntentService.GenericWorkItem {
      final Intent mIntent;
      final int mStartId;

      CompatWorkItem(Intent var2, int var3) {
         this.mIntent = var2;
         this.mStartId = var3;
      }

      public void complete() {
         JobIntentService.this.stopSelf(this.mStartId);
      }

      public Intent getIntent() {
         return this.mIntent;
      }
   }

   interface GenericWorkItem {
      void complete();

      Intent getIntent();
   }

   @RequiresApi(26)
   static final class JobServiceEngineImpl extends JobServiceEngine implements JobIntentService.CompatJobEngine {
      static final boolean DEBUG = false;
      static final String TAG = "JobServiceEngineImpl";
      final Object mLock = new Object();
      JobParameters mParams;
      final JobIntentService mService;

      JobServiceEngineImpl(JobIntentService var1) {
         super(var1);
         this.mService = var1;
      }

      public IBinder compatGetBinder() {
         return this.getBinder();
      }

      public JobIntentService.GenericWorkItem dequeueWork() {
         // $FF: Couldn't be decompiled
      }

      public boolean onStartJob(JobParameters var1) {
         this.mParams = var1;
         this.mService.ensureProcessorRunningLocked(false);
         return true;
      }

      public boolean onStopJob(JobParameters param1) {
         // $FF: Couldn't be decompiled
      }

      final class WrapperWorkItem implements JobIntentService.GenericWorkItem {
         final JobWorkItem mJobWork;

         WrapperWorkItem(JobWorkItem var2) {
            this.mJobWork = var2;
         }

         public void complete() {
            Object var1 = JobServiceEngineImpl.this.mLock;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label122: {
               try {
                  if (JobServiceEngineImpl.this.mParams != null) {
                     JobServiceEngineImpl.this.mParams.completeWork(this.mJobWork);
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label122;
               }

               label119:
               try {
                  return;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label119;
               }
            }

            while(true) {
               Throwable var2 = var10000;

               try {
                  throw var2;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  continue;
               }
            }
         }

         public Intent getIntent() {
            return this.mJobWork.getIntent();
         }
      }
   }

   @RequiresApi(26)
   static final class JobWorkEnqueuer extends JobIntentService.WorkEnqueuer {
      private final JobInfo mJobInfo;
      private final JobScheduler mJobScheduler;

      JobWorkEnqueuer(Context var1, ComponentName var2, int var3) {
         super(var1, var2);
         this.ensureJobId(var3);
         this.mJobInfo = (new Builder(var3, this.mComponentName)).setOverrideDeadline(0L).build();
         this.mJobScheduler = (JobScheduler)var1.getApplicationContext().getSystemService("jobscheduler");
      }

      void enqueueWork(Intent var1) {
         this.mJobScheduler.enqueue(this.mJobInfo, new JobWorkItem(var1));
      }
   }

   abstract static class WorkEnqueuer {
      final ComponentName mComponentName;
      boolean mHasJobId;
      int mJobId;

      WorkEnqueuer(Context var1, ComponentName var2) {
         this.mComponentName = var2;
      }

      abstract void enqueueWork(Intent var1);

      void ensureJobId(int var1) {
         if (!this.mHasJobId) {
            this.mHasJobId = true;
            this.mJobId = var1;
         } else if (this.mJobId != var1) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Given job ID ");
            var2.append(var1);
            var2.append(" is different than previous ");
            var2.append(this.mJobId);
            throw new IllegalArgumentException(var2.toString());
         }
      }

      public void serviceProcessingFinished() {
      }

      public void serviceProcessingStarted() {
      }

      public void serviceStartReceived() {
      }
   }
}
