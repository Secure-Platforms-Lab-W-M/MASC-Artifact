package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class Snapshot {
   private ArrayList mConnections = new ArrayList();
   private int mHeight;
   private int mWidth;
   // $FF: renamed from: mX int
   private int field_6;
   // $FF: renamed from: mY int
   private int field_7;

   public Snapshot(ConstraintWidget var1) {
      this.field_6 = var1.getX();
      this.field_7 = var1.getY();
      this.mWidth = var1.getWidth();
      this.mHeight = var1.getHeight();
      ArrayList var5 = var1.getAnchors();
      int var2 = 0;

      for(int var3 = var5.size(); var2 < var3; ++var2) {
         ConstraintAnchor var4 = (ConstraintAnchor)var5.get(var2);
         this.mConnections.add(new Snapshot.Connection(var4));
      }

   }

   public void applyTo(ConstraintWidget var1) {
      var1.setX(this.field_6);
      var1.setY(this.field_7);
      var1.setWidth(this.mWidth);
      var1.setHeight(this.mHeight);
      int var2 = 0;

      for(int var3 = this.mConnections.size(); var2 < var3; ++var2) {
         ((Snapshot.Connection)this.mConnections.get(var2)).applyTo(var1);
      }

   }

   public void updateFrom(ConstraintWidget var1) {
      this.field_6 = var1.getX();
      this.field_7 = var1.getY();
      this.mWidth = var1.getWidth();
      this.mHeight = var1.getHeight();
      int var3 = this.mConnections.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         ((Snapshot.Connection)this.mConnections.get(var2)).updateFrom(var1);
      }

   }

   static class Connection {
      private ConstraintAnchor mAnchor;
      private int mCreator;
      private int mMargin;
      private ConstraintAnchor.Strength mStrengh;
      private ConstraintAnchor mTarget;

      public Connection(ConstraintAnchor var1) {
         this.mAnchor = var1;
         this.mTarget = var1.getTarget();
         this.mMargin = var1.getMargin();
         this.mStrengh = var1.getStrength();
         this.mCreator = var1.getConnectionCreator();
      }

      public void applyTo(ConstraintWidget var1) {
         var1.getAnchor(this.mAnchor.getType()).connect(this.mTarget, this.mMargin, this.mStrengh, this.mCreator);
      }

      public void updateFrom(ConstraintWidget var1) {
         this.mAnchor = var1.getAnchor(this.mAnchor.getType());
         ConstraintAnchor var2 = this.mAnchor;
         if (var2 != null) {
            this.mTarget = var2.getTarget();
            this.mMargin = this.mAnchor.getMargin();
            this.mStrengh = this.mAnchor.getStrength();
            this.mCreator = this.mAnchor.getConnectionCreator();
         } else {
            this.mTarget = null;
            this.mMargin = 0;
            this.mStrengh = ConstraintAnchor.Strength.STRONG;
            this.mCreator = 0;
         }
      }
   }
}
