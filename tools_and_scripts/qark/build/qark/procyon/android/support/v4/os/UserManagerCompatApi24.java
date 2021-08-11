// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import android.os.UserManager;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class UserManagerCompatApi24
{
    public static boolean isUserUnlocked(final Context context) {
        return ((UserManager)context.getSystemService((Class)UserManager.class)).isUserUnlocked();
    }
}
