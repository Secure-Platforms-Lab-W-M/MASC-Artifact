package javax.media;

public class ControllerErrorEvent extends ControllerClosedEvent {
   public ControllerErrorEvent(Controller var1) {
      super(var1);
   }

   public ControllerErrorEvent(Controller var1, String var2) {
      super(var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",message=");
      var1.append(this.message);
      var1.append("]");
      return var1.toString();
   }
}
