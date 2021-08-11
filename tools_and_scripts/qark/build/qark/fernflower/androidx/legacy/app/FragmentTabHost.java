package androidx.legacy.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import java.util.ArrayList;

@Deprecated
public class FragmentTabHost extends TabHost implements OnTabChangeListener {
   private boolean mAttached;
   private int mContainerId;
   private Context mContext;
   private FragmentManager mFragmentManager;
   private FragmentTabHost.TabInfo mLastTab;
   private OnTabChangeListener mOnTabChangeListener;
   private FrameLayout mRealTabContent;
   private final ArrayList mTabs = new ArrayList();

   @Deprecated
   public FragmentTabHost(Context var1) {
      super(var1, (AttributeSet)null);
      this.initFragmentTabHost(var1, (AttributeSet)null);
   }

   @Deprecated
   public FragmentTabHost(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initFragmentTabHost(var1, var2);
   }

   private FragmentTransaction doTabChanged(String var1, FragmentTransaction var2) {
      FragmentTabHost.TabInfo var4 = null;

      for(int var3 = 0; var3 < this.mTabs.size(); ++var3) {
         FragmentTabHost.TabInfo var5 = (FragmentTabHost.TabInfo)this.mTabs.get(var3);
         if (var5.tag.equals(var1)) {
            var4 = var5;
         }
      }

      if (var4 != null) {
         FragmentTransaction var6 = var2;
         if (this.mLastTab != var4) {
            var6 = var2;
            if (var2 == null) {
               var6 = this.mFragmentManager.beginTransaction();
            }

            FragmentTabHost.TabInfo var8 = this.mLastTab;
            if (var8 != null && var8.fragment != null) {
               var6.detach(this.mLastTab.fragment);
            }

            if (var4 != null) {
               if (var4.fragment == null) {
                  var4.fragment = Fragment.instantiate(this.mContext, var4.clss.getName(), var4.args);
                  var6.add(this.mContainerId, var4.fragment, var4.tag);
               } else {
                  var6.attach(var4.fragment);
               }
            }

            this.mLastTab = var4;
         }

         return var6;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("No tab known for tag ");
         var7.append(var1);
         throw new IllegalStateException(var7.toString());
      }
   }

   private void ensureContent() {
      if (this.mRealTabContent == null) {
         FrameLayout var1 = (FrameLayout)this.findViewById(this.mContainerId);
         this.mRealTabContent = var1;
         if (var1 == null) {
            StringBuilder var2 = new StringBuilder();
            var2.append("No tab content FrameLayout found for id ");
            var2.append(this.mContainerId);
            throw new IllegalStateException(var2.toString());
         }
      }
   }

   private void ensureHierarchy(Context var1) {
      if (this.findViewById(16908307) == null) {
         LinearLayout var2 = new LinearLayout(var1);
         var2.setOrientation(1);
         this.addView(var2, new LayoutParams(-1, -1));
         TabWidget var3 = new TabWidget(var1);
         var3.setId(16908307);
         var3.setOrientation(0);
         var2.addView(var3, new android.widget.LinearLayout.LayoutParams(-1, -2, 0.0F));
         FrameLayout var5 = new FrameLayout(var1);
         var5.setId(16908305);
         var2.addView(var5, new android.widget.LinearLayout.LayoutParams(0, 0, 0.0F));
         FrameLayout var4 = new FrameLayout(var1);
         this.mRealTabContent = var4;
         var4.setId(this.mContainerId);
         var2.addView(var4, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0F));
      }

   }

   private void initFragmentTabHost(Context var1, AttributeSet var2) {
      TypedArray var3 = var1.obtainStyledAttributes(var2, new int[]{16842995}, 0, 0);
      this.mContainerId = var3.getResourceId(0, 0);
      var3.recycle();
      super.setOnTabChangedListener(this);
   }

   @Deprecated
   public void addTab(TabSpec var1, Class var2, Bundle var3) {
      var1.setContent(new FragmentTabHost.DummyTabFactory(this.mContext));
      String var4 = var1.getTag();
      FragmentTabHost.TabInfo var5 = new FragmentTabHost.TabInfo(var4, var2, var3);
      if (this.mAttached) {
         var5.fragment = this.mFragmentManager.findFragmentByTag(var4);
         if (var5.fragment != null && !var5.fragment.isDetached()) {
            FragmentTransaction var6 = this.mFragmentManager.beginTransaction();
            var6.detach(var5.fragment);
            var6.commit();
         }
      }

      this.mTabs.add(var5);
      this.addTab(var1);
   }

   @Deprecated
   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      String var4 = this.getCurrentTabTag();
      FragmentTransaction var2 = null;

      FragmentTransaction var3;
      for(int var1 = 0; var1 < this.mTabs.size(); var2 = var3) {
         FragmentTabHost.TabInfo var5 = (FragmentTabHost.TabInfo)this.mTabs.get(var1);
         var5.fragment = this.mFragmentManager.findFragmentByTag(var5.tag);
         var3 = var2;
         if (var5.fragment != null) {
            var3 = var2;
            if (!var5.fragment.isDetached()) {
               if (var5.tag.equals(var4)) {
                  this.mLastTab = var5;
                  var3 = var2;
               } else {
                  var3 = var2;
                  if (var2 == null) {
                     var3 = this.mFragmentManager.beginTransaction();
                  }

                  var3.detach(var5.fragment);
               }
            }
         }

         ++var1;
      }

      this.mAttached = true;
      var2 = this.doTabChanged(var4, var2);
      if (var2 != null) {
         var2.commit();
         this.mFragmentManager.executePendingTransactions();
      }

   }

   @Deprecated
   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mAttached = false;
   }

   @Deprecated
   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof FragmentTabHost.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         FragmentTabHost.SavedState var2 = (FragmentTabHost.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setCurrentTabByTag(var2.curTab);
      }
   }

   @Deprecated
   protected Parcelable onSaveInstanceState() {
      FragmentTabHost.SavedState var1 = new FragmentTabHost.SavedState(super.onSaveInstanceState());
      var1.curTab = this.getCurrentTabTag();
      return var1;
   }

   @Deprecated
   public void onTabChanged(String var1) {
      if (this.mAttached) {
         FragmentTransaction var2 = this.doTabChanged(var1, (FragmentTransaction)null);
         if (var2 != null) {
            var2.commit();
         }
      }

      OnTabChangeListener var3 = this.mOnTabChangeListener;
      if (var3 != null) {
         var3.onTabChanged(var1);
      }

   }

   @Deprecated
   public void setOnTabChangedListener(OnTabChangeListener var1) {
      this.mOnTabChangeListener = var1;
   }

   @Deprecated
   public void setup() {
      throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
   }

   @Deprecated
   public void setup(Context var1, FragmentManager var2) {
      this.ensureHierarchy(var1);
      super.setup();
      this.mContext = var1;
      this.mFragmentManager = var2;
      this.ensureContent();
   }

   @Deprecated
   public void setup(Context var1, FragmentManager var2, int var3) {
      this.ensureHierarchy(var1);
      super.setup();
      this.mContext = var1;
      this.mFragmentManager = var2;
      this.mContainerId = var3;
      this.ensureContent();
      this.mRealTabContent.setId(var3);
      if (this.getId() == -1) {
         this.setId(16908306);
      }

   }

   static class DummyTabFactory implements TabContentFactory {
      private final Context mContext;

      public DummyTabFactory(Context var1) {
         this.mContext = var1;
      }

      public View createTabContent(String var1) {
         View var2 = new View(this.mContext);
         var2.setMinimumWidth(0);
         var2.setMinimumHeight(0);
         return var2;
      }
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public FragmentTabHost.SavedState createFromParcel(Parcel var1) {
            return new FragmentTabHost.SavedState(var1);
         }

         public FragmentTabHost.SavedState[] newArray(int var1) {
            return new FragmentTabHost.SavedState[var1];
         }
      };
      String curTab;

      SavedState(Parcel var1) {
         super(var1);
         this.curTab = var1.readString();
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("FragmentTabHost.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" curTab=");
         var1.append(this.curTab);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeString(this.curTab);
      }
   }

   static final class TabInfo {
      final Bundle args;
      final Class clss;
      Fragment fragment;
      final String tag;

      TabInfo(String var1, Class var2, Bundle var3) {
         this.tag = var1;
         this.clss = var2;
         this.args = var3;
      }
   }
}
