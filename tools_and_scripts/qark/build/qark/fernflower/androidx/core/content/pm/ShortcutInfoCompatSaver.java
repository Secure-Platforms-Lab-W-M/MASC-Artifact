package androidx.core.content.pm;

import java.util.ArrayList;
import java.util.List;

public abstract class ShortcutInfoCompatSaver {
   public abstract Object addShortcuts(List var1);

   public List getShortcuts() throws Exception {
      return new ArrayList();
   }

   public abstract Object removeAllShortcuts();

   public abstract Object removeShortcuts(List var1);

   public static class NoopImpl extends ShortcutInfoCompatSaver {
      public Void addShortcuts(List var1) {
         return null;
      }

      public Void removeAllShortcuts() {
         return null;
      }

      public Void removeShortcuts(List var1) {
         return null;
      }
   }
}
