package javax.media;

public class CachingControlEvent extends ControllerEvent {
   CachingControl cachingControl;
   long progress;

   public CachingControlEvent(Controller var1, CachingControl var2, long var3) {
      super(var1);
      this.cachingControl = var2;
      this.progress = var3;
   }

   public CachingControl getCachingControl() {
      return this.cachingControl;
   }

   public long getContentProgress() {
      return this.progress;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",cachingControl=");
      var1.append(this.cachingControl);
      var1.append(",progress=");
      var1.append(this.progress);
      var1.append("]");
      return var1.toString();
   }
}
