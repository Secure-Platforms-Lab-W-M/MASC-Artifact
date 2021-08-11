package androidx.lifecycle;

import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.savedstate.SavedStateRegistry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class SavedStateHandle {
   private static final Class[] ACCEPTABLE_CLASSES;
   private static final String KEYS = "keys";
   private static final String VALUES = "values";
   private final Map mLiveDatas = new HashMap();
   final Map mRegular;
   private final SavedStateRegistry.SavedStateProvider mSavedStateProvider = new SavedStateRegistry.SavedStateProvider() {
      public Bundle saveState() {
         Set var3 = SavedStateHandle.this.mRegular.keySet();
         ArrayList var1 = new ArrayList(var3.size());
         ArrayList var2 = new ArrayList(var1.size());
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            String var4 = (String)var5.next();
            var1.add(var4);
            var2.add(SavedStateHandle.this.mRegular.get(var4));
         }

         Bundle var6 = new Bundle();
         var6.putParcelableArrayList("keys", var1);
         var6.putParcelableArrayList("values", var2);
         return var6;
      }
   };

   static {
      Class var2 = Boolean.TYPE;
      Class var3 = Double.TYPE;
      Class var4 = Integer.TYPE;
      Class var5 = Long.TYPE;
      Class var6 = Byte.TYPE;
      Class var7 = Character.TYPE;
      Class var8 = Float.TYPE;
      Class var9 = Short.TYPE;
      Class var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = Size.class;
      } else {
         var0 = Integer.TYPE;
      }

      Class var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = SizeF.class;
      } else {
         var1 = Integer.TYPE;
      }

      ACCEPTABLE_CLASSES = new Class[]{var2, boolean[].class, var3, double[].class, var4, int[].class, var5, long[].class, String.class, String[].class, Binder.class, Bundle.class, var6, byte[].class, var7, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, var8, float[].class, Parcelable.class, Parcelable[].class, Serializable.class, var9, short[].class, SparseArray.class, var0, var1};
   }

   public SavedStateHandle() {
      this.mRegular = new HashMap();
   }

   public SavedStateHandle(Map var1) {
      this.mRegular = new HashMap(var1);
   }

   static SavedStateHandle createHandle(Bundle var0, Bundle var1) {
      if (var0 == null && var1 == null) {
         return new SavedStateHandle();
      } else {
         HashMap var3 = new HashMap();
         if (var1 != null) {
            Iterator var4 = var1.keySet().iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               var3.put(var5, var1.get(var5));
            }
         }

         if (var0 == null) {
            return new SavedStateHandle(var3);
         } else {
            ArrayList var7 = var0.getParcelableArrayList("keys");
            ArrayList var6 = var0.getParcelableArrayList("values");
            if (var7 != null && var6 != null && var7.size() == var6.size()) {
               for(int var2 = 0; var2 < var7.size(); ++var2) {
                  var3.put((String)var7.get(var2), var6.get(var2));
               }

               return new SavedStateHandle(var3);
            } else {
               throw new IllegalStateException("Invalid bundle passed as restored state");
            }
         }
      }
   }

   private MutableLiveData getLiveDataInternal(String var1, boolean var2, Object var3) {
      MutableLiveData var4 = (MutableLiveData)this.mLiveDatas.get(var1);
      if (var4 != null) {
         return var4;
      } else {
         SavedStateHandle.SavingStateLiveData var5;
         if (this.mRegular.containsKey(var1)) {
            var5 = new SavedStateHandle.SavingStateLiveData(this, var1, this.mRegular.get(var1));
         } else if (var2) {
            var5 = new SavedStateHandle.SavingStateLiveData(this, var1, var3);
         } else {
            var5 = new SavedStateHandle.SavingStateLiveData(this, var1);
         }

         this.mLiveDatas.put(var1, var5);
         return var5;
      }
   }

   private static void validateValue(Object var0) {
      if (var0 != null) {
         Class[] var3 = ACCEPTABLE_CLASSES;
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (var3[var1].isInstance(var0)) {
               return;
            }
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("Can't put value with type ");
         var4.append(var0.getClass());
         var4.append(" into saved state");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public boolean contains(String var1) {
      return this.mRegular.containsKey(var1);
   }

   public Object get(String var1) {
      return this.mRegular.get(var1);
   }

   public MutableLiveData getLiveData(String var1) {
      return this.getLiveDataInternal(var1, false, (Object)null);
   }

   public MutableLiveData getLiveData(String var1, Object var2) {
      return this.getLiveDataInternal(var1, true, var2);
   }

   public Set keys() {
      return Collections.unmodifiableSet(this.mRegular.keySet());
   }

   public Object remove(String var1) {
      Object var2 = this.mRegular.remove(var1);
      SavedStateHandle.SavingStateLiveData var3 = (SavedStateHandle.SavingStateLiveData)this.mLiveDatas.remove(var1);
      if (var3 != null) {
         var3.detach();
      }

      return var2;
   }

   SavedStateRegistry.SavedStateProvider savedStateProvider() {
      return this.mSavedStateProvider;
   }

   public void set(String var1, Object var2) {
      validateValue(var2);
      MutableLiveData var3 = (MutableLiveData)this.mLiveDatas.get(var1);
      if (var3 != null) {
         var3.setValue(var2);
      } else {
         this.mRegular.put(var1, var2);
      }
   }

   static class SavingStateLiveData extends MutableLiveData {
      private SavedStateHandle mHandle;
      private String mKey;

      SavingStateLiveData(SavedStateHandle var1, String var2) {
         this.mKey = var2;
         this.mHandle = var1;
      }

      SavingStateLiveData(SavedStateHandle var1, String var2, Object var3) {
         super(var3);
         this.mKey = var2;
         this.mHandle = var1;
      }

      void detach() {
         this.mHandle = null;
      }

      public void setValue(Object var1) {
         SavedStateHandle var2 = this.mHandle;
         if (var2 != null) {
            var2.mRegular.put(this.mKey, var1);
         }

         super.setValue(var1);
      }
   }
}
