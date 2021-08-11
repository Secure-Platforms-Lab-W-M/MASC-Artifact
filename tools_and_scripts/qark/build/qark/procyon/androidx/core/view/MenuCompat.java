// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.MenuItem;
import android.os.Build$VERSION;
import androidx.core.internal.view.SupportMenu;
import android.view.Menu;

public final class MenuCompat
{
    private MenuCompat() {
    }
    
    public static void setGroupDividerEnabled(final Menu menu, final boolean b) {
        if (menu instanceof SupportMenu) {
            ((SupportMenu)menu).setGroupDividerEnabled(b);
            return;
        }
        if (Build$VERSION.SDK_INT >= 28) {
            menu.setGroupDividerEnabled(b);
        }
    }
    
    @Deprecated
    public static void setShowAsAction(final MenuItem menuItem, final int showAsAction) {
        menuItem.setShowAsAction(showAsAction);
    }
}
