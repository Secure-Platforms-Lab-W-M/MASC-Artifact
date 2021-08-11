package android.support.constraint.solver.widgets;

public class Rectangle {
   public int height;
   public int width;
   // $FF: renamed from: x int
   public int field_14;
   // $FF: renamed from: y int
   public int field_15;

   public boolean contains(int var1, int var2) {
      int var3 = this.field_14;
      if (var1 >= var3 && var1 < var3 + this.width) {
         var1 = this.field_15;
         if (var2 >= var1 && var2 < var1 + this.height) {
            return true;
         }
      }

      return false;
   }

   public int getCenterX() {
      return (this.field_14 + this.width) / 2;
   }

   public int getCenterY() {
      return (this.field_15 + this.height) / 2;
   }

   void grow(int var1, int var2) {
      this.field_14 -= var1;
      this.field_15 -= var2;
      this.width += var1 * 2;
      this.height += var2 * 2;
   }

   boolean intersects(Rectangle var1) {
      int var2 = this.field_14;
      int var3 = var1.field_14;
      if (var2 >= var3 && var2 < var3 + var1.width) {
         var2 = this.field_15;
         var3 = var1.field_15;
         if (var2 >= var3 && var2 < var3 + var1.height) {
            return true;
         }
      }

      return false;
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.field_14 = var1;
      this.field_15 = var2;
      this.width = var3;
      this.height = var4;
   }
}
