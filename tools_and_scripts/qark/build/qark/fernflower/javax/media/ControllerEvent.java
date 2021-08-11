package javax.media;

public class ControllerEvent extends MediaEvent {
   Controller eventSrc;

   public ControllerEvent(Controller var1) {
      super(var1);
      this.eventSrc = var1;
   }

   public Object getSource() {
      return this.eventSrc;
   }

   public Controller getSourceController() {
      return this.eventSrc;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.eventSrc);
      var1.append("]");
      return var1.toString();
   }
}
