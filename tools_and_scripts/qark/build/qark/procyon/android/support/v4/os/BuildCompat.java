// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import android.os.Build$VERSION;

public class BuildCompat
{
    private BuildCompat() {
    }
    
    @Deprecated
    public static boolean isAtLeastN() {
        return Build$VERSION.SDK_INT >= 24;
    }
    
    @Deprecated
    public static boolean isAtLeastNMR1() {
        return Build$VERSION.SDK_INT >= 25;
    }
    
    public static boolean isAtLeastO() {
        return Build$VERSION.SDK_INT >= 26;
    }
    
    public static boolean isAtLeastOMR1() {
        return Build$VERSION.CODENAME.startsWith("OMR") || isAtLeastP();
    }
    
    public static boolean isAtLeastP() {
        return Build$VERSION.CODENAME.equals("P");
    }
}
