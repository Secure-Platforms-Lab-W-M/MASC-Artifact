package javax.media;

public class ControllerClosedEvent extends ControllerEvent {
   protected String message;

   public ControllerClosedEvent(Controller var1) {
      super(var1);
   }

   public ControllerClosedEvent(Controller var1, String var2) {
      super(var1);
      this.message = var2;
   }

   public String getMessage() {
      return this.message;
   }
}
