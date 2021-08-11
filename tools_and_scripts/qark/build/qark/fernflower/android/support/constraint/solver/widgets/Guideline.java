package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class Guideline extends ConstraintWidget {
   public static final int HORIZONTAL = 0;
   public static final int RELATIVE_BEGIN = 1;
   public static final int RELATIVE_END = 2;
   public static final int RELATIVE_PERCENT = 0;
   public static final int RELATIVE_UNKNWON = -1;
   public static final int VERTICAL = 1;
   private ConstraintAnchor mAnchor;
   private Rectangle mHead;
   private int mHeadSize;
   private boolean mIsPositionRelaxed;
   private int mMinimumPosition;
   private int mOrientation;
   protected int mRelativeBegin = -1;
   protected int mRelativeEnd = -1;
   protected float mRelativePercent = -1.0F;

   public Guideline() {
      this.mAnchor = this.mTop;
      this.mOrientation = 0;
      this.mIsPositionRelaxed = false;
      this.mMinimumPosition = 0;
      this.mHead = new Rectangle();
      this.mHeadSize = 8;
      this.mAnchors.clear();
      this.mAnchors.add(this.mAnchor);
   }

   public void addToSolver(LinearSystem var1, int var2) {
      ConstraintWidgetContainer var5 = (ConstraintWidgetContainer)this.getParent();
      if (var5 != null) {
         ConstraintAnchor var3 = var5.getAnchor(ConstraintAnchor.Type.LEFT);
         ConstraintAnchor var4 = var5.getAnchor(ConstraintAnchor.Type.RIGHT);
         if (this.mOrientation == 0) {
            var3 = var5.getAnchor(ConstraintAnchor.Type.TOP);
            var4 = var5.getAnchor(ConstraintAnchor.Type.BOTTOM);
         }

         if (this.mRelativeBegin != -1) {
            var1.addConstraint(LinearSystem.createRowEquals(var1, var1.createObjectVariable(this.mAnchor), var1.createObjectVariable(var3), this.mRelativeBegin, false));
         } else {
            if (this.mRelativeEnd != -1) {
               var1.addConstraint(LinearSystem.createRowEquals(var1, var1.createObjectVariable(this.mAnchor), var1.createObjectVariable(var4), -this.mRelativeEnd, false));
            } else if (this.mRelativePercent != -1.0F) {
               var1.addConstraint(LinearSystem.createRowDimensionPercent(var1, var1.createObjectVariable(this.mAnchor), var1.createObjectVariable(var3), var1.createObjectVariable(var4), this.mRelativePercent, this.mIsPositionRelaxed));
               return;
            }

         }
      }
   }

   public void cyclePosition() {
      if (this.mRelativeBegin != -1) {
         this.inferRelativePercentPosition();
      } else if (this.mRelativePercent != -1.0F) {
         this.inferRelativeEndPosition();
      } else if (this.mRelativeEnd != -1) {
         this.inferRelativeBeginPosition();
      }
   }

   public ConstraintAnchor getAnchor() {
      return this.mAnchor;
   }

   public ConstraintAnchor getAnchor(ConstraintAnchor.Type var1) {
      switch(var1) {
      case LEFT:
      case RIGHT:
         if (this.mOrientation == 1) {
            return this.mAnchor;
         }
         break;
      case TOP:
      case BOTTOM:
         if (this.mOrientation == 0) {
            return this.mAnchor;
         }
      }

      return null;
   }

   public ArrayList getAnchors() {
      return this.mAnchors;
   }

   public Rectangle getHead() {
      Rectangle var5 = this.mHead;
      int var1 = this.getDrawX();
      int var2 = this.mHeadSize;
      int var3 = this.getDrawY();
      int var4 = this.mHeadSize;
      var5.setBounds(var1 - var2, var3 - var4 * 2, var4 * 2, var4 * 2);
      if (this.getOrientation() == 0) {
         var5 = this.mHead;
         var1 = this.getDrawX();
         var2 = this.mHeadSize;
         var3 = this.getDrawY();
         var4 = this.mHeadSize;
         var5.setBounds(var1 - var2 * 2, var3 - var4, var4 * 2, var4 * 2);
      }

      return this.mHead;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public int getRelativeBegin() {
      return this.mRelativeBegin;
   }

   public int getRelativeBehaviour() {
      if (this.mRelativePercent != -1.0F) {
         return 0;
      } else if (this.mRelativeBegin != -1) {
         return 1;
      } else {
         return this.mRelativeEnd != -1 ? 2 : -1;
      }
   }

   public int getRelativeEnd() {
      return this.mRelativeEnd;
   }

   public float getRelativePercent() {
      return this.mRelativePercent;
   }

   public String getType() {
      return "Guideline";
   }

   void inferRelativeBeginPosition() {
      int var1 = this.getX();
      if (this.mOrientation == 0) {
         var1 = this.getY();
      }

      this.setGuideBegin(var1);
   }

   void inferRelativeEndPosition() {
      int var1 = this.getParent().getWidth() - this.getX();
      if (this.mOrientation == 0) {
         var1 = this.getParent().getHeight() - this.getY();
      }

      this.setGuideEnd(var1);
   }

   void inferRelativePercentPosition() {
      float var1 = (float)this.getX() / (float)this.getParent().getWidth();
      if (this.mOrientation == 0) {
         var1 = (float)this.getY() / (float)this.getParent().getHeight();
      }

      this.setGuidePercent(var1);
   }

   public void setDrawOrigin(int var1, int var2) {
      if (this.mOrientation == 1) {
         var1 -= this.mOffsetX;
         if (this.mRelativeBegin != -1) {
            this.setGuideBegin(var1);
         } else if (this.mRelativeEnd != -1) {
            this.setGuideEnd(this.getParent().getWidth() - var1);
         } else if (this.mRelativePercent != -1.0F) {
            this.setGuidePercent((float)var1 / (float)this.getParent().getWidth());
         }

      } else {
         var1 = var2 - this.mOffsetY;
         if (this.mRelativeBegin != -1) {
            this.setGuideBegin(var1);
         } else if (this.mRelativeEnd != -1) {
            this.setGuideEnd(this.getParent().getHeight() - var1);
         } else if (this.mRelativePercent != -1.0F) {
            this.setGuidePercent((float)var1 / (float)this.getParent().getHeight());
         }
      }
   }

   public void setGuideBegin(int var1) {
      if (var1 > -1) {
         this.mRelativePercent = -1.0F;
         this.mRelativeBegin = var1;
         this.mRelativeEnd = -1;
      }
   }

   public void setGuideEnd(int var1) {
      if (var1 > -1) {
         this.mRelativePercent = -1.0F;
         this.mRelativeBegin = -1;
         this.mRelativeEnd = var1;
      }
   }

   public void setGuidePercent(float var1) {
      if (var1 > -1.0F) {
         this.mRelativePercent = var1;
         this.mRelativeBegin = -1;
         this.mRelativeEnd = -1;
      }
   }

   public void setGuidePercent(int var1) {
      this.setGuidePercent((float)var1 / 100.0F);
   }

   public void setMinimumPosition(int var1) {
      this.mMinimumPosition = var1;
   }

   public void setOrientation(int var1) {
      if (this.mOrientation != var1) {
         this.mOrientation = var1;
         this.mAnchors.clear();
         if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
         } else {
            this.mAnchor = this.mTop;
         }

         this.mAnchors.add(this.mAnchor);
      }
   }

   public void setPositionRelaxed(boolean var1) {
      if (this.mIsPositionRelaxed != var1) {
         this.mIsPositionRelaxed = var1;
      }
   }

   public void updateFromSolver(LinearSystem var1, int var2) {
      if (this.getParent() != null) {
         var2 = var1.getObjectVariableValue(this.mAnchor);
         if (this.mOrientation == 1) {
            this.setX(var2);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
         } else {
            this.setX(0);
            this.setY(var2);
            this.setWidth(this.getParent().getWidth());
            this.setHeight(0);
         }
      }
   }
}
