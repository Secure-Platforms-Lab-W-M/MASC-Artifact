package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class AsyncListDiffer {
   private static final Executor sMainThreadExecutor = new AsyncListDiffer.MainThreadExecutor();
   final AsyncDifferConfig mConfig;
   private List mList;
   private final List mListeners;
   Executor mMainThreadExecutor;
   int mMaxScheduledGeneration;
   private List mReadOnlyList;
   private final ListUpdateCallback mUpdateCallback;

   public AsyncListDiffer(ListUpdateCallback var1, AsyncDifferConfig var2) {
      this.mListeners = new CopyOnWriteArrayList();
      this.mReadOnlyList = Collections.emptyList();
      this.mUpdateCallback = var1;
      this.mConfig = var2;
      if (var2.getMainThreadExecutor() != null) {
         this.mMainThreadExecutor = var2.getMainThreadExecutor();
      } else {
         this.mMainThreadExecutor = sMainThreadExecutor;
      }
   }

   public AsyncListDiffer(RecyclerView.Adapter var1, DiffUtil.ItemCallback var2) {
      this((ListUpdateCallback)(new AdapterListUpdateCallback(var1)), (AsyncDifferConfig)(new AsyncDifferConfig.Builder(var2)).build());
   }

   private void onCurrentListChanged(List var1, Runnable var2) {
      Iterator var3 = this.mListeners.iterator();

      while(var3.hasNext()) {
         ((AsyncListDiffer.ListListener)var3.next()).onCurrentListChanged(var1, this.mReadOnlyList);
      }

      if (var2 != null) {
         var2.run();
      }

   }

   public void addListListener(AsyncListDiffer.ListListener var1) {
      this.mListeners.add(var1);
   }

   public List getCurrentList() {
      return this.mReadOnlyList;
   }

   void latchList(List var1, DiffUtil.DiffResult var2, Runnable var3) {
      List var4 = this.mReadOnlyList;
      this.mList = var1;
      this.mReadOnlyList = Collections.unmodifiableList(var1);
      var2.dispatchUpdatesTo(this.mUpdateCallback);
      this.onCurrentListChanged(var4, var3);
   }

   public void removeListListener(AsyncListDiffer.ListListener var1) {
      this.mListeners.remove(var1);
   }

   public void submitList(List var1) {
      this.submitList(var1, (Runnable)null);
   }

   public void submitList(final List var1, final Runnable var2) {
      final int var3 = this.mMaxScheduledGeneration + 1;
      this.mMaxScheduledGeneration = var3;
      final List var4 = this.mList;
      if (var1 == var4) {
         if (var2 != null) {
            var2.run();
         }

      } else {
         List var5 = this.mReadOnlyList;
         if (var1 == null) {
            var3 = var4.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, var3);
            this.onCurrentListChanged(var5, var2);
         } else if (var4 == null) {
            this.mList = var1;
            this.mReadOnlyList = Collections.unmodifiableList(var1);
            this.mUpdateCallback.onInserted(0, var1.size());
            this.onCurrentListChanged(var5, var2);
         } else {
            var4 = this.mList;
            this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
               public void run() {
                  final DiffUtil.DiffResult var1x = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                     public boolean areContentsTheSame(int var1x, int var2x) {
                        Object var3x = var4.get(var1x);
                        Object var4x = var1.get(var2x);
                        if (var3x != null && var4x != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(var3x, var4x);
                        } else if (var3x == null && var4x == null) {
                           return true;
                        } else {
                           throw new AssertionError();
                        }
                     }

                     public boolean areItemsTheSame(int var1x, int var2x) {
                        Object var3x = var4.get(var1x);
                        Object var4x = var1.get(var2x);
                        if (var3x != null && var4x != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(var3x, var4x);
                        } else {
                           return var3x == null && var4x == null;
                        }
                     }

                     public Object getChangePayload(int var1x, int var2x) {
                        Object var3x = var4.get(var1x);
                        Object var4x = var1.get(var2x);
                        if (var3x != null && var4x != null) {
                           return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(var3x, var4x);
                        } else {
                           throw new AssertionError();
                        }
                     }

                     public int getNewListSize() {
                        return var1.size();
                     }

                     public int getOldListSize() {
                        return var4.size();
                     }
                  });
                  AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                     public void run() {
                        if (AsyncListDiffer.this.mMaxScheduledGeneration == var3) {
                           AsyncListDiffer.this.latchList(var1, var1x, var2);
                        }

                     }
                  });
               }
            });
         }
      }
   }

   public interface ListListener {
      void onCurrentListChanged(List var1, List var2);
   }

   private static class MainThreadExecutor implements Executor {
      final Handler mHandler = new Handler(Looper.getMainLooper());

      MainThreadExecutor() {
      }

      public void execute(Runnable var1) {
         this.mHandler.post(var1);
      }
   }
}
