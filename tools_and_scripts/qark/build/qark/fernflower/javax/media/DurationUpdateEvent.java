package javax.media;

public class DurationUpdateEvent extends ControllerEvent {
   Time duration;

   public DurationUpdateEvent(Controller var1, Time var2) {
      super(var1);
      this.duration = var2;
   }

   public Time getDuration() {
      return this.duration;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",duration=");
      var1.append(this.duration);
      var1.append("]");
      return var1.toString();
   }
}
