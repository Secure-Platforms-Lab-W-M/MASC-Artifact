package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParcelUtils {
   private static final String INNER_BUNDLE_KEY = "a";

   private ParcelUtils() {
   }

   public static VersionedParcelable fromInputStream(InputStream var0) {
      return (new VersionedParcelStream(var0, (OutputStream)null)).readVersionedParcelable();
   }

   public static VersionedParcelable fromParcelable(Parcelable var0) {
      if (var0 instanceof ParcelImpl) {
         return ((ParcelImpl)var0).getVersionedParcel();
      } else {
         throw new IllegalArgumentException("Invalid parcel");
      }
   }

   public static VersionedParcelable getVersionedParcelable(Bundle param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static List getVersionedParcelableList(Bundle var0, String var1) {
      ArrayList var2 = new ArrayList();

      boolean var10001;
      Iterator var5;
      try {
         var0 = (Bundle)var0.getParcelable(var1);
         var0.setClassLoader(ParcelUtils.class.getClassLoader());
         var5 = var0.getParcelableArrayList("a").iterator();
      } catch (RuntimeException var4) {
         var10001 = false;
         return null;
      }

      while(true) {
         try {
            if (!var5.hasNext()) {
               return var2;
            }

            var2.add(fromParcelable((Parcelable)var5.next()));
         } catch (RuntimeException var3) {
            var10001 = false;
            return null;
         }
      }
   }

   public static void putVersionedParcelable(Bundle var0, String var1, VersionedParcelable var2) {
      if (var2 != null) {
         Bundle var3 = new Bundle();
         var3.putParcelable("a", toParcelable(var2));
         var0.putParcelable(var1, var3);
      }
   }

   public static void putVersionedParcelableList(Bundle var0, String var1, List var2) {
      Bundle var3 = new Bundle();
      ArrayList var4 = new ArrayList();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         var4.add(toParcelable((VersionedParcelable)var5.next()));
      }

      var3.putParcelableArrayList("a", var4);
      var0.putParcelable(var1, var3);
   }

   public static void toOutputStream(VersionedParcelable var0, OutputStream var1) {
      VersionedParcelStream var2 = new VersionedParcelStream((InputStream)null, var1);
      var2.writeVersionedParcelable(var0);
      var2.closeField();
   }

   public static Parcelable toParcelable(VersionedParcelable var0) {
      return new ParcelImpl(var0);
   }
}
