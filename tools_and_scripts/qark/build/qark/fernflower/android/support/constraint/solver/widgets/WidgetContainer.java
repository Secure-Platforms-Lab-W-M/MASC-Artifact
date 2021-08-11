package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
   protected ArrayList mChildren = new ArrayList();

   public WidgetContainer() {
   }

   public WidgetContainer(int var1, int var2) {
      super(var1, var2);
   }

   public WidgetContainer(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public static Rectangle getBounds(ArrayList var0) {
      Rectangle var7 = new Rectangle();
      if (var0.size() == 0) {
         return var7;
      } else {
         int var4 = Integer.MAX_VALUE;
         int var3 = 0;
         int var5 = Integer.MAX_VALUE;
         int var2 = 0;
         int var1 = 0;

         for(int var6 = var0.size(); var1 < var6; ++var1) {
            ConstraintWidget var8 = (ConstraintWidget)var0.get(var1);
            if (var8.getX() < var4) {
               var4 = var8.getX();
            }

            if (var8.getY() < var5) {
               var5 = var8.getY();
            }

            if (var8.getRight() > var3) {
               var3 = var8.getRight();
            }

            if (var8.getBottom() > var2) {
               var2 = var8.getBottom();
            }
         }

         var7.setBounds(var4, var5, var3 - var4, var2 - var5);
         return var7;
      }
   }

   public void add(ConstraintWidget var1) {
      this.mChildren.add(var1);
      if (var1.getParent() != null) {
         ((WidgetContainer)var1.getParent()).remove(var1);
      }

      var1.setParent(this);
   }

   public ConstraintWidget findWidget(float var1, float var2) {
      Object var9 = null;
      int var3 = this.getDrawX();
      int var4 = this.getDrawY();
      int var5 = this.getWidth();
      int var6 = this.getHeight();
      if (var1 >= (float)var3 && var1 <= (float)(var5 + var3) && var2 >= (float)var4 && var2 <= (float)(var6 + var4)) {
         var9 = this;
      }

      var3 = 0;

      for(var4 = this.mChildren.size(); var3 < var4; ++var3) {
         ConstraintWidget var10 = (ConstraintWidget)this.mChildren.get(var3);
         if (var10 instanceof WidgetContainer) {
            var10 = ((WidgetContainer)var10).findWidget(var1, var2);
            if (var10 != null) {
               var9 = var10;
            }
         } else {
            var5 = var10.getDrawX();
            var6 = var10.getDrawY();
            int var7 = var10.getWidth();
            int var8 = var10.getHeight();
            if (var1 >= (float)var5 && var1 <= (float)(var7 + var5) && var2 >= (float)var6 && var2 <= (float)(var8 + var6)) {
               var9 = var10;
            }
         }
      }

      return (ConstraintWidget)var9;
   }

   public ArrayList findWidgets(int var1, int var2, int var3, int var4) {
      ArrayList var5 = new ArrayList();
      Rectangle var6 = new Rectangle();
      var6.setBounds(var1, var2, var3, var4);
      var1 = 0;

      for(var2 = this.mChildren.size(); var1 < var2; ++var1) {
         ConstraintWidget var7 = (ConstraintWidget)this.mChildren.get(var1);
         Rectangle var8 = new Rectangle();
         var8.setBounds(var7.getDrawX(), var7.getDrawY(), var7.getWidth(), var7.getHeight());
         if (var6.intersects(var8)) {
            var5.add(var7);
         }
      }

      return var5;
   }

   public ArrayList getChildren() {
      return this.mChildren;
   }

   public ConstraintWidgetContainer getRootConstraintContainer() {
      ConstraintWidget var2 = this.getParent();
      ConstraintWidgetContainer var1 = null;
      if (this instanceof ConstraintWidgetContainer) {
         var1 = (ConstraintWidgetContainer)this;
      }

      while(true) {
         ConstraintWidget var3 = var2;
         if (var2 == null) {
            return var1;
         }

         var2 = var2.getParent();
         if (var3 instanceof ConstraintWidgetContainer) {
            var1 = (ConstraintWidgetContainer)var3;
         }
      }
   }

   public void layout() {
      this.updateDrawPosition();
      ArrayList var3 = this.mChildren;
      if (var3 != null) {
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var1);
            if (var4 instanceof WidgetContainer) {
               ((WidgetContainer)var4).layout();
            }
         }

      }
   }

   public void remove(ConstraintWidget var1) {
      this.mChildren.remove(var1);
      var1.setParent((ConstraintWidget)null);
   }

   public void removeAllChildren() {
      this.mChildren.clear();
   }

   public void reset() {
      this.mChildren.clear();
      super.reset();
   }

   public void resetGroups() {
      super.resetGroups();
      int var2 = this.mChildren.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         ((ConstraintWidget)this.mChildren.get(var1)).resetGroups();
      }

   }

   public void resetSolverVariables(Cache var1) {
      super.resetSolverVariables(var1);
      int var3 = this.mChildren.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((ConstraintWidget)this.mChildren.get(var2)).resetSolverVariables(var1);
      }

   }

   public void setOffset(int var1, int var2) {
      super.setOffset(var1, var2);
      var2 = this.mChildren.size();

      for(var1 = 0; var1 < var2; ++var1) {
         ((ConstraintWidget)this.mChildren.get(var1)).setOffset(this.getRootX(), this.getRootY());
      }

   }

   public void updateDrawPosition() {
      super.updateDrawPosition();
      ArrayList var3 = this.mChildren;
      if (var3 != null) {
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var1);
            var4.setOffset(this.getDrawX(), this.getDrawY());
            if (!(var4 instanceof ConstraintWidgetContainer)) {
               var4.updateDrawPosition();
            }
         }

      }
   }
}
