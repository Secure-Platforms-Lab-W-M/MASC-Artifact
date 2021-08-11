package androidx.core.os;

import java.util.Locale;

interface LocaleListInterface {
   Locale get(int var1);

   Locale getFirstMatch(String[] var1);

   Object getLocaleList();

   int indexOf(Locale var1);

   boolean isEmpty();

   int size();

   String toLanguageTags();
}
