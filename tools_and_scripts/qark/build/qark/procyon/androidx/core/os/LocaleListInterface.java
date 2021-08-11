// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

import java.util.Locale;

interface LocaleListInterface
{
    Locale get(final int p0);
    
    Locale getFirstMatch(final String[] p0);
    
    Object getLocaleList();
    
    int indexOf(final Locale p0);
    
    boolean isEmpty();
    
    int size();
    
    String toLanguageTags();
}
