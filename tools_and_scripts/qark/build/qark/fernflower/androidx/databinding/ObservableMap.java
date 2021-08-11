package androidx.databinding;

import java.util.Map;

public interface ObservableMap extends Map {
   void addOnMapChangedCallback(ObservableMap.OnMapChangedCallback var1);

   void removeOnMapChangedCallback(ObservableMap.OnMapChangedCallback var1);

   public abstract static class OnMapChangedCallback {
      public abstract void onMapChanged(ObservableMap var1, Object var2);
   }
}
