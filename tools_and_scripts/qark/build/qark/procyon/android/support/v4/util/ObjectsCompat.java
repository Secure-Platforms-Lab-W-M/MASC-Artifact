// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import java.util.Objects;
import android.support.annotation.RequiresApi;
import android.os.Build$VERSION;

public class ObjectsCompat
{
    private static final ImplBase IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ImplBase)new ImplApi19();
            return;
        }
        IMPL = new ImplBase();
    }
    
    private ObjectsCompat() {
    }
    
    public static boolean equals(final Object o, final Object o2) {
        return ObjectsCompat.IMPL.equals(o, o2);
    }
    
    @RequiresApi(19)
    private static class ImplApi19 extends ImplBase
    {
        @Override
        public boolean equals(final Object o, final Object o2) {
            return Objects.equals(o, o2);
        }
    }
    
    private static class ImplBase
    {
        public boolean equals(final Object o, final Object o2) {
            return o == o2 || (o != null && o.equals(o2));
        }
    }
}
