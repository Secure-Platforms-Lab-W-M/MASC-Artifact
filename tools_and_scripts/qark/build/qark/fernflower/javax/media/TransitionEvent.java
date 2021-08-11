package javax.media;

public class TransitionEvent extends ControllerEvent {
   int currentState;
   int previousState;
   int targetState;

   public TransitionEvent(Controller var1, int var2, int var3, int var4) {
      super(var1);
      this.previousState = var2;
      this.currentState = var3;
      this.targetState = var4;
   }

   public int getCurrentState() {
      return this.currentState;
   }

   public int getPreviousState() {
      return this.previousState;
   }

   public int getTargetState() {
      return this.targetState;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",previousState=");
      var1.append(this.previousState);
      var1.append(",currentState=");
      var1.append(this.currentState);
      var1.append(",targetState=");
      var1.append(this.targetState);
      var1.append("]");
      return var1.toString();
   }
}
