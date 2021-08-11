/*
 * Decompiled with CFR 0_124.
 */
package androidx.core.content.pm;

import androidx.core.content.pm.ShortcutInfoCompat;
import java.util.ArrayList;
import java.util.List;

public abstract class ShortcutInfoCompatSaver<T> {
    public abstract T addShortcuts(List<ShortcutInfoCompat> var1);

    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList<ShortcutInfoCompat>();
    }

    public abstract T removeAllShortcuts();

    public abstract T removeShortcuts(List<String> var1);

    public static class NoopImpl
    extends ShortcutInfoCompatSaver<Void> {
        @Override
        public Void addShortcuts(List<ShortcutInfoCompat> list) {
            return null;
        }

        @Override
        public Void removeAllShortcuts() {
            return null;
        }

        @Override
        public Void removeShortcuts(List<String> list) {
            return null;
        }
    }

}

