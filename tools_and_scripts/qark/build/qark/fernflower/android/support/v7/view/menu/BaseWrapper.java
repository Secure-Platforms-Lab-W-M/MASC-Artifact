package android.support.v7.view.menu;

class BaseWrapper {
   final Object mWrappedObject;

   BaseWrapper(Object var1) {
      if (var1 != null) {
         this.mWrappedObject = var1;
      } else {
         throw new IllegalArgumentException("Wrapped Object can not be null.");
      }
   }

   public Object getWrappedObject() {
      return this.mWrappedObject;
   }
}
