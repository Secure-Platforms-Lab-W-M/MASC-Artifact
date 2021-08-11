package android.support.constraint.solver.widgets;

public class ResolutionDimension extends ResolutionNode {
   float value = 0.0F;

   public void remove() {
      this.state = 2;
   }

   public void reset() {
      super.reset();
      this.value = 0.0F;
   }

   public void resolve(int var1) {
      if (this.state == 0 || this.value != (float)var1) {
         this.value = (float)var1;
         if (this.state == 1) {
            this.invalidate();
         }

         this.didResolve();
      }
   }
}
