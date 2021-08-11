/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.UserManager
 */
package android.support.v4.os;

import android.content.Context;
import android.os.UserManager;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@RequiresApi(value=24)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class UserManagerCompatApi24 {
    public static boolean isUserUnlocked(Context context) {
        return ((UserManager)context.getSystemService(UserManager.class)).isUserUnlocked();
    }
}

