package android.support.v4.os;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
interface LocaleListInterface {
   boolean equals(Object var1);

   Locale get(int var1);

   @Nullable
   Locale getFirstMatch(String[] var1);

   Object getLocaleList();

   int hashCode();

   @IntRange(
      from = -1L
   )
   int indexOf(Locale var1);

   boolean isEmpty();

   void setLocaleList(@NonNull Locale... var1);

   @IntRange(
      from = 0L
   )
   int size();

   String toLanguageTags();

   String toString();
}
