package android.support.v4.view;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerAdapter {
   public static final int POSITION_NONE = -2;
   public static final int POSITION_UNCHANGED = -1;
   private final DataSetObservable mObservable = new DataSetObservable();
   private DataSetObserver mViewPagerObserver;

   @Deprecated
   public void destroyItem(View var1, int var2, Object var3) {
      throw new UnsupportedOperationException("Required method destroyItem was not overridden");
   }

   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      this.destroyItem((View)var1, var2, var3);
   }

   @Deprecated
   public void finishUpdate(View var1) {
   }

   public void finishUpdate(ViewGroup var1) {
      this.finishUpdate((View)var1);
   }

   public abstract int getCount();

   public int getItemPosition(Object var1) {
      return -1;
   }

   public CharSequence getPageTitle(int var1) {
      return null;
   }

   public float getPageWidth(int var1) {
      return 1.0F;
   }

   @Deprecated
   public Object instantiateItem(View var1, int var2) {
      throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
   }

   public Object instantiateItem(ViewGroup var1, int var2) {
      return this.instantiateItem((View)var1, var2);
   }

   public abstract boolean isViewFromObject(View var1, Object var2);

   public void notifyDataSetChanged() {
      synchronized(this){}

      label136: {
         Throwable var10000;
         boolean var10001;
         label131: {
            try {
               if (this.mViewPagerObserver != null) {
                  this.mViewPagerObserver.onChanged();
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label131;
            }

            label128:
            try {
               break label136;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label128;
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

      this.mObservable.notifyChanged();
   }

   public void registerDataSetObserver(DataSetObserver var1) {
      this.mObservable.registerObserver(var1);
   }

   public void restoreState(Parcelable var1, ClassLoader var2) {
   }

   public Parcelable saveState() {
      return null;
   }

   @Deprecated
   public void setPrimaryItem(View var1, int var2, Object var3) {
   }

   public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
      this.setPrimaryItem((View)var1, var2, var3);
   }

   void setViewPagerObserver(DataSetObserver param1) {
      // $FF: Couldn't be decompiled
   }

   @Deprecated
   public void startUpdate(View var1) {
   }

   public void startUpdate(ViewGroup var1) {
      this.startUpdate((View)var1);
   }

   public void unregisterDataSetObserver(DataSetObserver var1) {
      this.mObservable.unregisterObserver(var1);
   }
}
