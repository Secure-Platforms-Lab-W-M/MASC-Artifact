package javax.media;

public class StopEvent extends TransitionEvent {
   private Time mediaTime;

   public StopEvent(Controller var1, int var2, int var3, int var4, Time var5) {
      super(var1, var2, var3, var4);
      this.mediaTime = var5;
   }

   public Time getMediaTime() {
      return this.mediaTime;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[source=");
      var1.append(this.getSource());
      var1.append(",previousState=");
      var1.append(this.getPreviousState());
      var1.append(",currentState=");
      var1.append(this.getCurrentState());
      var1.append(",targetState=");
      var1.append(this.getTargetState());
      var1.append(",mediaTime=");
      var1.append(this.mediaTime);
      var1.append("]");
      return var1.toString();
   }
}
