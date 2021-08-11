package javax.media;

public class MediaTimeSetEvent extends ControllerEvent {
   Time mediaTime;

   public MediaTimeSetEvent(Controller var1, Time var2) {
      super(var1);
      this.mediaTime = var2;
   }

   public Time getMediaTime() {
      return this.mediaTime;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",mediaTime=");
      var1.append(this.mediaTime);
      var1.append("]");
      return var1.toString();
   }
}
