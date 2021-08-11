package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;

final class LocaleListPlatformWrapper implements LocaleListInterface {
   private final LocaleList mLocaleList;

   LocaleListPlatformWrapper(LocaleList var1) {
      this.mLocaleList = var1;
   }

   public boolean equals(Object var1) {
      return this.mLocaleList.equals(((LocaleListInterface)var1).getLocaleList());
   }

   public Locale get(int var1) {
      return this.mLocaleList.get(var1);
   }

   public Locale getFirstMatch(String[] var1) {
      return this.mLocaleList.getFirstMatch(var1);
   }

   public Object getLocaleList() {
      return this.mLocaleList;
   }

   public int hashCode() {
      return this.mLocaleList.hashCode();
   }

   public int indexOf(Locale var1) {
      return this.mLocaleList.indexOf(var1);
   }

   public boolean isEmpty() {
      return this.mLocaleList.isEmpty();
   }

   public int size() {
      return this.mLocaleList.size();
   }

   public String toLanguageTags() {
      return this.mLocaleList.toLanguageTags();
   }

   public String toString() {
      return this.mLocaleList.toString();
   }
}
