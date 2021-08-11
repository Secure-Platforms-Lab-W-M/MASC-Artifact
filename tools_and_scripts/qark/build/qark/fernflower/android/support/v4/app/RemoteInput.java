package android.support.v4.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class RemoteInput extends RemoteInputCompatBase.RemoteInput {
   private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
   public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final RemoteInputCompatBase.RemoteInput.Factory FACTORY;
   private static final RemoteInput.Impl IMPL;
   public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
   private static final String TAG = "RemoteInput";
   private final boolean mAllowFreeFormTextInput;
   private final Set mAllowedDataTypes;
   private final CharSequence[] mChoices;
   private final Bundle mExtras;
   private final CharSequence mLabel;
   private final String mResultKey;

   static {
      if (VERSION.SDK_INT >= 20) {
         IMPL = new RemoteInput.ImplApi20();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new RemoteInput.ImplJellybean();
      } else {
         IMPL = new RemoteInput.ImplBase();
      }

      FACTORY = new RemoteInputCompatBase.RemoteInput.Factory() {
         public RemoteInput build(String var1, CharSequence var2, CharSequence[] var3, boolean var4, Bundle var5, Set var6) {
            return new RemoteInput(var1, var2, var3, var4, var5, var6);
         }

         public RemoteInput[] newArray(int var1) {
            return new RemoteInput[var1];
         }
      };
   }

   RemoteInput(String var1, CharSequence var2, CharSequence[] var3, boolean var4, Bundle var5, Set var6) {
      this.mResultKey = var1;
      this.mLabel = var2;
      this.mChoices = var3;
      this.mAllowFreeFormTextInput = var4;
      this.mExtras = var5;
      this.mAllowedDataTypes = var6;
   }

   public static void addDataResultToIntent(RemoteInput var0, Intent var1, Map var2) {
      IMPL.addDataResultToIntent(var0, var1, var2);
   }

   public static void addResultsToIntent(RemoteInput[] var0, Intent var1, Bundle var2) {
      IMPL.addResultsToIntent(var0, var1, var2);
   }

   public static Map getDataResultsFromIntent(Intent var0, String var1) {
      return IMPL.getDataResultsFromIntent(var0, var1);
   }

   public static Bundle getResultsFromIntent(Intent var0) {
      return IMPL.getResultsFromIntent(var0);
   }

   public boolean getAllowFreeFormInput() {
      return this.mAllowFreeFormTextInput;
   }

   public Set getAllowedDataTypes() {
      return this.mAllowedDataTypes;
   }

   public CharSequence[] getChoices() {
      return this.mChoices;
   }

   public Bundle getExtras() {
      return this.mExtras;
   }

   public CharSequence getLabel() {
      return this.mLabel;
   }

   public String getResultKey() {
      return this.mResultKey;
   }

   public boolean isDataOnly() {
      return !this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty();
   }

   public static final class Builder {
      private boolean mAllowFreeFormTextInput = true;
      private final Set mAllowedDataTypes = new HashSet();
      private CharSequence[] mChoices;
      private Bundle mExtras = new Bundle();
      private CharSequence mLabel;
      private final String mResultKey;

      public Builder(String var1) {
         if (var1 != null) {
            this.mResultKey = var1;
         } else {
            throw new IllegalArgumentException("Result key can't be null");
         }
      }

      public RemoteInput.Builder addExtras(Bundle var1) {
         if (var1 != null) {
            this.mExtras.putAll(var1);
         }

         return this;
      }

      public RemoteInput build() {
         return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
      }

      public Bundle getExtras() {
         return this.mExtras;
      }

      public RemoteInput.Builder setAllowDataType(String var1, boolean var2) {
         if (var2) {
            this.mAllowedDataTypes.add(var1);
            return this;
         } else {
            this.mAllowedDataTypes.remove(var1);
            return this;
         }
      }

      public RemoteInput.Builder setAllowFreeFormInput(boolean var1) {
         this.mAllowFreeFormTextInput = var1;
         return this;
      }

      public RemoteInput.Builder setChoices(CharSequence[] var1) {
         this.mChoices = var1;
         return this;
      }

      public RemoteInput.Builder setLabel(CharSequence var1) {
         this.mLabel = var1;
         return this;
      }
   }

   interface Impl {
      void addDataResultToIntent(RemoteInput var1, Intent var2, Map var3);

      void addResultsToIntent(RemoteInput[] var1, Intent var2, Bundle var3);

      Map getDataResultsFromIntent(Intent var1, String var2);

      Bundle getResultsFromIntent(Intent var1);
   }

   @RequiresApi(20)
   static class ImplApi20 implements RemoteInput.Impl {
      public void addDataResultToIntent(RemoteInput var1, Intent var2, Map var3) {
         RemoteInputCompatApi20.addDataResultToIntent(var1, var2, var3);
      }

      public void addResultsToIntent(RemoteInput[] var1, Intent var2, Bundle var3) {
         RemoteInputCompatApi20.addResultsToIntent(var1, var2, var3);
      }

      public Map getDataResultsFromIntent(Intent var1, String var2) {
         return RemoteInputCompatApi20.getDataResultsFromIntent(var1, var2);
      }

      public Bundle getResultsFromIntent(Intent var1) {
         return RemoteInputCompatApi20.getResultsFromIntent(var1);
      }
   }

   static class ImplBase implements RemoteInput.Impl {
      public void addDataResultToIntent(RemoteInput var1, Intent var2, Map var3) {
         Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
      }

      public void addResultsToIntent(RemoteInput[] var1, Intent var2, Bundle var3) {
         Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
      }

      public Map getDataResultsFromIntent(Intent var1, String var2) {
         Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
         return null;
      }

      public Bundle getResultsFromIntent(Intent var1) {
         Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
         return null;
      }
   }

   @RequiresApi(16)
   static class ImplJellybean implements RemoteInput.Impl {
      public void addDataResultToIntent(RemoteInput var1, Intent var2, Map var3) {
         RemoteInputCompatJellybean.addDataResultToIntent(var1, var2, var3);
      }

      public void addResultsToIntent(RemoteInput[] var1, Intent var2, Bundle var3) {
         RemoteInputCompatJellybean.addResultsToIntent(var1, var2, var3);
      }

      public Map getDataResultsFromIntent(Intent var1, String var2) {
         return RemoteInputCompatJellybean.getDataResultsFromIntent(var1, var2);
      }

      public Bundle getResultsFromIntent(Intent var1) {
         return RemoteInputCompatJellybean.getResultsFromIntent(var1);
      }
   }
}
