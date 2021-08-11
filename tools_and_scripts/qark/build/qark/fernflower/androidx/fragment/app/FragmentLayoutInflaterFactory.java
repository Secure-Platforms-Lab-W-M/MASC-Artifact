package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater.Factory2;
import androidx.fragment.R.styleable;

class FragmentLayoutInflaterFactory implements Factory2 {
   private static final String TAG = "FragmentManager";
   private final FragmentManager mFragmentManager;

   FragmentLayoutInflaterFactory(FragmentManager var1) {
      this.mFragmentManager = var1;
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      if (FragmentContainerView.class.getName().equals(var2)) {
         return new FragmentContainerView(var3, var4, this.mFragmentManager);
      } else {
         boolean var8 = "fragment".equals(var2);
         Fragment var13 = null;
         if (!var8) {
            return null;
         } else {
            String var10 = var4.getAttributeValue((String)null, "class");
            TypedArray var11 = var3.obtainStyledAttributes(var4, styleable.Fragment);
            String var9 = var10;
            if (var10 == null) {
               var9 = var11.getString(styleable.Fragment_android_name);
            }

            int var7 = var11.getResourceId(styleable.Fragment_android_id, -1);
            var10 = var11.getString(styleable.Fragment_android_tag);
            var11.recycle();
            if (var9 == null) {
               return null;
            } else if (!FragmentFactory.isFragmentClass(var3.getClassLoader(), var9)) {
               return null;
            } else {
               int var5;
               if (var1 != null) {
                  var5 = var1.getId();
               } else {
                  var5 = 0;
               }

               StringBuilder var14;
               if (var5 == -1 && var7 == -1 && var10 == null) {
                  var14 = new StringBuilder();
                  var14.append(var4.getPositionDescription());
                  var14.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
                  var14.append(var9);
                  throw new IllegalArgumentException(var14.toString());
               } else {
                  if (var7 != -1) {
                     var13 = this.mFragmentManager.findFragmentById(var7);
                  }

                  Fragment var12 = var13;
                  if (var13 == null) {
                     var12 = var13;
                     if (var10 != null) {
                        var12 = this.mFragmentManager.findFragmentByTag(var10);
                     }
                  }

                  var13 = var12;
                  if (var12 == null) {
                     var13 = var12;
                     if (var5 != -1) {
                        var13 = this.mFragmentManager.findFragmentById(var5);
                     }
                  }

                  if (FragmentManager.isLoggingEnabled(2)) {
                     var14 = new StringBuilder();
                     var14.append("onCreateView: id=0x");
                     var14.append(Integer.toHexString(var7));
                     var14.append(" fname=");
                     var14.append(var9);
                     var14.append(" existing=");
                     var14.append(var13);
                     Log.v("FragmentManager", var14.toString());
                  }

                  if (var13 == null) {
                     var13 = this.mFragmentManager.getFragmentFactory().instantiate(var3.getClassLoader(), var9);
                     var13.mFromLayout = true;
                     int var6;
                     if (var7 != 0) {
                        var6 = var7;
                     } else {
                        var6 = var5;
                     }

                     var13.mFragmentId = var6;
                     var13.mContainerId = var5;
                     var13.mTag = var10;
                     var13.mInLayout = true;
                     var13.mFragmentManager = this.mFragmentManager;
                     var13.mHost = this.mFragmentManager.mHost;
                     var13.onInflate(this.mFragmentManager.mHost.getContext(), var4, var13.mSavedFragmentState);
                     this.mFragmentManager.addFragment(var13);
                     this.mFragmentManager.moveToState(var13);
                  } else {
                     if (var13.mInLayout) {
                        var14 = new StringBuilder();
                        var14.append(var4.getPositionDescription());
                        var14.append(": Duplicate id 0x");
                        var14.append(Integer.toHexString(var7));
                        var14.append(", tag ");
                        var14.append(var10);
                        var14.append(", or parent id 0x");
                        var14.append(Integer.toHexString(var5));
                        var14.append(" with another fragment for ");
                        var14.append(var9);
                        throw new IllegalArgumentException(var14.toString());
                     }

                     var13.mInLayout = true;
                     var13.mHost = this.mFragmentManager.mHost;
                     var13.onInflate(this.mFragmentManager.mHost.getContext(), var4, var13.mSavedFragmentState);
                  }

                  if (this.mFragmentManager.mCurState < 1 && var13.mFromLayout) {
                     this.mFragmentManager.moveToState(var13, 1);
                  } else {
                     this.mFragmentManager.moveToState(var13);
                  }

                  if (var13.mView != null) {
                     if (var7 != 0) {
                        var13.mView.setId(var7);
                     }

                     if (var13.mView.getTag() == null) {
                        var13.mView.setTag(var10);
                     }

                     return var13.mView;
                  } else {
                     var14 = new StringBuilder();
                     var14.append("Fragment ");
                     var14.append(var9);
                     var14.append(" did not create a view.");
                     throw new IllegalStateException(var14.toString());
                  }
               }
            }
         }
      }
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      return this.onCreateView((View)null, var1, var2, var3);
   }
}
