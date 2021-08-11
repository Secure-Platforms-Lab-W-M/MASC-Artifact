package javax.media;

public class StopTimeChangeEvent extends ControllerEvent {
   Time stopTime;

   public StopTimeChangeEvent(Controller var1, Time var2) {
      super(var1);
      this.stopTime = var2;
   }

   public Time getStopTime() {
      return this.stopTime;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",stopTime=");
      var1.append(this.stopTime);
      var1.append("]");
      return var1.toString();
   }
}
