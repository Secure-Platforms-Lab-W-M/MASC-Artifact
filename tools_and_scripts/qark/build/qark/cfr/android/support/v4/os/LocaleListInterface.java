/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.os;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.Locale;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
interface LocaleListInterface {
    public boolean equals(Object var1);

    public Locale get(int var1);

    @Nullable
    public Locale getFirstMatch(String[] var1);

    public Object getLocaleList();

    public int hashCode();

    @IntRange(from=-1L)
    public int indexOf(Locale var1);

    public boolean isEmpty();

    public /* varargs */ void setLocaleList(@NonNull Locale ... var1);

    @IntRange(from=0L)
    public int size();

    public String toLanguageTags();

    public String toString();
}

