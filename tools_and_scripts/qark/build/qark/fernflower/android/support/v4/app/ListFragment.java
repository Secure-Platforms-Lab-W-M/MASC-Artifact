package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;

public class ListFragment extends Fragment {
   static final int INTERNAL_EMPTY_ID = 16711681;
   static final int INTERNAL_LIST_CONTAINER_ID = 16711683;
   static final int INTERNAL_PROGRESS_CONTAINER_ID = 16711682;
   ListAdapter mAdapter;
   CharSequence mEmptyText;
   View mEmptyView;
   private final Handler mHandler = new Handler();
   ListView mList;
   View mListContainer;
   boolean mListShown;
   private final OnItemClickListener mOnClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
         ListFragment.this.onListItemClick((ListView)var1, var2, var3, var4);
      }
   };
   View mProgressContainer;
   private final Runnable mRequestFocus = new Runnable() {
      public void run() {
         ListFragment.this.mList.focusableViewAvailable(ListFragment.this.mList);
      }
   };
   TextView mStandardEmptyView;

   private void ensureList() {
      if (this.mList == null) {
         View var1 = this.getView();
         if (var1 != null) {
            if (var1 instanceof ListView) {
               this.mList = (ListView)var1;
            } else {
               this.mStandardEmptyView = (TextView)var1.findViewById(16711681);
               TextView var2 = this.mStandardEmptyView;
               if (var2 == null) {
                  this.mEmptyView = var1.findViewById(16908292);
               } else {
                  var2.setVisibility(8);
               }

               this.mProgressContainer = var1.findViewById(16711682);
               this.mListContainer = var1.findViewById(16711683);
               var1 = var1.findViewById(16908298);
               if (!(var1 instanceof ListView)) {
                  if (var1 == null) {
                     throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                  }

                  throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
               }

               this.mList = (ListView)var1;
               var1 = this.mEmptyView;
               if (var1 != null) {
                  this.mList.setEmptyView(var1);
               } else {
                  CharSequence var3 = this.mEmptyText;
                  if (var3 != null) {
                     this.mStandardEmptyView.setText(var3);
                     this.mList.setEmptyView(this.mStandardEmptyView);
                  }
               }
            }

            this.mListShown = true;
            this.mList.setOnItemClickListener(this.mOnClickListener);
            if (this.mAdapter != null) {
               ListAdapter var4 = this.mAdapter;
               this.mAdapter = null;
               this.setListAdapter(var4);
            } else if (this.mProgressContainer != null) {
               this.setListShown(false, false);
            }

            this.mHandler.post(this.mRequestFocus);
         } else {
            throw new IllegalStateException("Content view not yet created");
         }
      }
   }

   private void setListShown(boolean var1, boolean var2) {
      this.ensureList();
      View var3 = this.mProgressContainer;
      if (var3 != null) {
         if (this.mListShown != var1) {
            this.mListShown = var1;
            if (var1) {
               if (var2) {
                  var3.startAnimation(AnimationUtils.loadAnimation(this.getContext(), 17432577));
                  this.mListContainer.startAnimation(AnimationUtils.loadAnimation(this.getContext(), 17432576));
               } else {
                  var3.clearAnimation();
                  this.mListContainer.clearAnimation();
               }

               this.mProgressContainer.setVisibility(8);
               this.mListContainer.setVisibility(0);
            } else {
               if (var2) {
                  var3.startAnimation(AnimationUtils.loadAnimation(this.getContext(), 17432576));
                  this.mListContainer.startAnimation(AnimationUtils.loadAnimation(this.getContext(), 17432577));
               } else {
                  var3.clearAnimation();
                  this.mListContainer.clearAnimation();
               }

               this.mProgressContainer.setVisibility(0);
               this.mListContainer.setVisibility(8);
            }
         }
      } else {
         throw new IllegalStateException("Can't be used with a custom content view");
      }
   }

   public ListAdapter getListAdapter() {
      return this.mAdapter;
   }

   public ListView getListView() {
      this.ensureList();
      return this.mList;
   }

   public long getSelectedItemId() {
      this.ensureList();
      return this.mList.getSelectedItemId();
   }

   public int getSelectedItemPosition() {
      this.ensureList();
      return this.mList.getSelectedItemPosition();
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Context var6 = this.getContext();
      FrameLayout var5 = new FrameLayout(var6);
      LinearLayout var8 = new LinearLayout(var6);
      var8.setId(16711682);
      var8.setOrientation(1);
      var8.setVisibility(8);
      var8.setGravity(17);
      var8.addView(new ProgressBar(var6, (AttributeSet)null, 16842874), new LayoutParams(-2, -2));
      var5.addView(var8, new LayoutParams(-1, -1));
      FrameLayout var9 = new FrameLayout(var6);
      var9.setId(16711683);
      TextView var4 = new TextView(var6);
      var4.setId(16711681);
      var4.setGravity(17);
      var9.addView(var4, new LayoutParams(-1, -1));
      ListView var7 = new ListView(var6);
      var7.setId(16908298);
      var7.setDrawSelectorOnTop(false);
      var9.addView(var7, new LayoutParams(-1, -1));
      var5.addView(var9, new LayoutParams(-1, -1));
      var5.setLayoutParams(new LayoutParams(-1, -1));
      return var5;
   }

   public void onDestroyView() {
      this.mHandler.removeCallbacks(this.mRequestFocus);
      this.mList = null;
      this.mListShown = false;
      this.mListContainer = null;
      this.mProgressContainer = null;
      this.mEmptyView = null;
      this.mStandardEmptyView = null;
      super.onDestroyView();
   }

   public void onListItemClick(ListView var1, View var2, int var3, long var4) {
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      this.ensureList();
   }

   public void setEmptyText(CharSequence var1) {
      this.ensureList();
      TextView var2 = this.mStandardEmptyView;
      if (var2 != null) {
         var2.setText(var1);
         if (this.mEmptyText == null) {
            this.mList.setEmptyView(this.mStandardEmptyView);
         }

         this.mEmptyText = var1;
      } else {
         throw new IllegalStateException("Can't be used with a custom content view");
      }
   }

   public void setListAdapter(ListAdapter var1) {
      ListAdapter var4 = this.mAdapter;
      boolean var3 = false;
      boolean var2;
      if (var4 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mAdapter = var1;
      ListView var5 = this.mList;
      if (var5 != null) {
         var5.setAdapter(var1);
         if (!this.mListShown && !var2) {
            if (this.getView().getWindowToken() != null) {
               var3 = true;
            }

            this.setListShown(true, var3);
         }
      }

   }

   public void setListShown(boolean var1) {
      this.setListShown(var1, true);
   }

   public void setListShownNoAnimation(boolean var1) {
      this.setListShown(var1, false);
   }

   public void setSelection(int var1) {
      this.ensureList();
      this.mList.setSelection(var1);
   }
}
