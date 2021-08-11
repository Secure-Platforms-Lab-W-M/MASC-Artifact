package android.support.v7.util;

public interface ListUpdateCallback {
   void onChanged(int var1, int var2, Object var3);

   void onInserted(int var1, int var2);

   void onMoved(int var1, int var2);

   void onRemoved(int var1, int var2);
}
