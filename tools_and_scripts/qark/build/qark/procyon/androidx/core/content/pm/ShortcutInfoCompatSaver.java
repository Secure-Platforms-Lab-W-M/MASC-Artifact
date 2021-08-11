// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.pm;

import java.util.ArrayList;
import java.util.List;

public abstract class ShortcutInfoCompatSaver<T>
{
    public abstract T addShortcuts(final List<ShortcutInfoCompat> p0);
    
    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList<ShortcutInfoCompat>();
    }
    
    public abstract T removeAllShortcuts();
    
    public abstract T removeShortcuts(final List<String> p0);
    
    public static class NoopImpl extends ShortcutInfoCompatSaver<Void>
    {
        @Override
        public Void addShortcuts(final List<ShortcutInfoCompat> list) {
            return null;
        }
        
        @Override
        public Void removeAllShortcuts() {
            return null;
        }
        
        @Override
        public Void removeShortcuts(final List<String> list) {
            return null;
        }
    }
}
