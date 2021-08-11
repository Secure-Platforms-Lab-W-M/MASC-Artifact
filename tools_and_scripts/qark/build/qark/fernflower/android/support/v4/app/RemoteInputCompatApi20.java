package android.support.v4.app;

import android.app.RemoteInput.Builder;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@RequiresApi(20)
class RemoteInputCompatApi20 {
   private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";

   public static void addDataResultToIntent(RemoteInputCompatBase.RemoteInput var0, Intent var1, Map var2) {
      Intent var4 = getClipDataIntentFromIntent(var1);
      Intent var3 = var4;
      if (var4 == null) {
         var3 = new Intent();
      }

      Iterator var5 = var2.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var8 = (Entry)var5.next();
         String var6 = (String)var8.getKey();
         Uri var7 = (Uri)var8.getValue();
         if (var6 != null) {
            Bundle var10 = var3.getBundleExtra(getExtraResultsKeyForData(var6));
            Bundle var9 = var10;
            if (var10 == null) {
               var9 = new Bundle();
            }

            var9.putString(var0.getResultKey(), var7.toString());
            var3.putExtra(getExtraResultsKeyForData(var6), var9);
         }
      }

      var1.setClipData(ClipData.newIntent("android.remoteinput.results", var3));
   }

   static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] var0, Intent var1, Bundle var2) {
      Bundle var5 = getResultsFromIntent(var1);
      if (var5 != null) {
         var5.putAll(var2);
         var2 = var5;
      }

      int var4 = var0.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         RemoteInputCompatBase.RemoteInput var7 = var0[var3];
         Map var6 = getDataResultsFromIntent(var1, var7.getResultKey());
         android.app.RemoteInput.addResultsToIntent(fromCompat(new RemoteInputCompatBase.RemoteInput[]{var7}), var1, var2);
         if (var6 != null) {
            addDataResultToIntent(var7, var1, var6);
         }
      }

   }

   static android.app.RemoteInput[] fromCompat(RemoteInputCompatBase.RemoteInput[] var0) {
      if (var0 == null) {
         return null;
      } else {
         android.app.RemoteInput[] var2 = new android.app.RemoteInput[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            RemoteInputCompatBase.RemoteInput var3 = var0[var1];
            var2[var1] = (new Builder(var3.getResultKey())).setLabel(var3.getLabel()).setChoices(var3.getChoices()).setAllowFreeFormInput(var3.getAllowFreeFormInput()).addExtras(var3.getExtras()).build();
         }

         return var2;
      }
   }

   private static Intent getClipDataIntentFromIntent(Intent var0) {
      ClipData var2 = var0.getClipData();
      if (var2 == null) {
         return null;
      } else {
         ClipDescription var1 = var2.getDescription();
         if (!var1.hasMimeType("text/vnd.android.intent")) {
            return null;
         } else {
            return !var1.getLabel().equals("android.remoteinput.results") ? null : var2.getItemAt(0).getIntent();
         }
      }
   }

   static Map getDataResultsFromIntent(Intent var0, String var1) {
      var0 = getClipDataIntentFromIntent(var0);
      if (var0 == null) {
         return null;
      } else {
         HashMap var2 = new HashMap();
         Iterator var3 = var0.getExtras().keySet().iterator();

         while(var3.hasNext()) {
            String var5 = (String)var3.next();
            if (var5.startsWith("android.remoteinput.dataTypeResultsData")) {
               String var4 = var5.substring("android.remoteinput.dataTypeResultsData".length());
               if (var4 != null && !var4.isEmpty()) {
                  var5 = var0.getBundleExtra(var5).getString(var1);
                  if (var5 != null && !var5.isEmpty()) {
                     var2.put(var4, Uri.parse(var5));
                  }
               }
            }
         }

         if (var2.isEmpty()) {
            return null;
         } else {
            return var2;
         }
      }
   }

   private static String getExtraResultsKeyForData(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("android.remoteinput.dataTypeResultsData");
      var1.append(var0);
      return var1.toString();
   }

   static Bundle getResultsFromIntent(Intent var0) {
      return android.app.RemoteInput.getResultsFromIntent(var0);
   }

   static RemoteInputCompatBase.RemoteInput[] toCompat(android.app.RemoteInput[] var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      if (var0 == null) {
         return null;
      } else {
         RemoteInputCompatBase.RemoteInput[] var3 = var1.newArray(var0.length);

         for(int var2 = 0; var2 < var0.length; ++var2) {
            android.app.RemoteInput var4 = var0[var2];
            var3[var2] = var1.build(var4.getResultKey(), var4.getLabel(), var4.getChoices(), var4.getAllowFreeFormInput(), var4.getExtras(), (Set)null);
         }

         return var3;
      }
   }
}
