package javax.media;

public class RateChangeEvent extends ControllerEvent {
   float rate;

   public RateChangeEvent(Controller var1, float var2) {
      super(var1);
      this.rate = var2;
   }

   public float getRate() {
      return this.rate;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",rate=");
      var1.append(this.rate);
      var1.append("]");
      return var1.toString();
   }
}
