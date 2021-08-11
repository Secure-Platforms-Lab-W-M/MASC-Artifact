package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.ArrayList;

public class ConstraintLayout extends ViewGroup {
   static final boolean ALLOWS_EMBEDDED = false;
   private static final boolean SIMPLE_LAYOUT = true;
   private static final String TAG = "ConstraintLayout";
   public static final String VERSION = "ConstraintLayout-1.0.0";
   SparseArray mChildrenByIds = new SparseArray();
   private ConstraintSet mConstraintSet = null;
   private boolean mDirtyHierarchy = true;
   ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
   private int mMaxHeight = Integer.MAX_VALUE;
   private int mMaxWidth = Integer.MAX_VALUE;
   private int mMinHeight = 0;
   private int mMinWidth = 0;
   private int mOptimizationLevel = 2;
   private final ArrayList mVariableDimensionsWidgets = new ArrayList(100);

   public ConstraintLayout(Context var1) {
      super(var1);
      this.init((AttributeSet)null);
   }

   public ConstraintLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var2);
   }

   public ConstraintLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var2);
   }

   private final ConstraintWidget getTargetWidget(int var1) {
      if (var1 == 0) {
         return this.mLayoutWidget;
      } else {
         View var2 = (View)this.mChildrenByIds.get(var1);
         if (var2 == this) {
            return this.mLayoutWidget;
         } else {
            return var2 == null ? null : ((ConstraintLayout.LayoutParams)var2.getLayoutParams()).widget;
         }
      }
   }

   private final ConstraintWidget getViewWidget(View var1) {
      if (var1 == this) {
         return this.mLayoutWidget;
      } else {
         return var1 == null ? null : ((ConstraintLayout.LayoutParams)var1.getLayoutParams()).widget;
      }
   }

   private void init(AttributeSet var1) {
      this.mLayoutWidget.setCompanionWidget(this);
      this.mChildrenByIds.put(this.getId(), this);
      this.mConstraintSet = null;
      if (var1 != null) {
         TypedArray var5 = this.getContext().obtainStyledAttributes(var1, R$styleable.ConstraintLayout_Layout);
         int var3 = var5.getIndexCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            int var4 = var5.getIndex(var2);
            if (var4 == R$styleable.ConstraintLayout_Layout_android_minWidth) {
               this.mMinWidth = var5.getDimensionPixelOffset(var4, this.mMinWidth);
            } else if (var4 == R$styleable.ConstraintLayout_Layout_android_minHeight) {
               this.mMinHeight = var5.getDimensionPixelOffset(var4, this.mMinHeight);
            } else if (var4 == R$styleable.ConstraintLayout_Layout_android_maxWidth) {
               this.mMaxWidth = var5.getDimensionPixelOffset(var4, this.mMaxWidth);
            } else if (var4 == R$styleable.ConstraintLayout_Layout_android_maxHeight) {
               this.mMaxHeight = var5.getDimensionPixelOffset(var4, this.mMaxHeight);
            } else if (var4 == R$styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
               this.mOptimizationLevel = var5.getInt(var4, this.mOptimizationLevel);
            } else if (var4 == R$styleable.ConstraintLayout_Layout_constraintSet) {
               var4 = var5.getResourceId(var4, 0);
               this.mConstraintSet = new ConstraintSet();
               this.mConstraintSet.load(this.getContext(), var4);
            }
         }

         var5.recycle();
      }

      this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
   }

   private void internalMeasureChildren(int var1, int var2) {
      int var12 = this.getPaddingTop() + this.getPaddingBottom();
      int var13 = this.getPaddingLeft() + this.getPaddingRight();
      int var14 = this.getChildCount();

      for(int var5 = 0; var5 < var14; ++var5) {
         View var16 = this.getChildAt(var5);
         if (var16.getVisibility() != 8) {
            ConstraintLayout.LayoutParams var17 = (ConstraintLayout.LayoutParams)var16.getLayoutParams();
            ConstraintWidget var18 = var17.widget;
            if (!var17.isGuideline) {
               int var6 = var17.width;
               int var7 = var17.height;
               boolean var15 = var17.horizontalDimensionFixed;
               boolean var4 = true;
               boolean var3 = var4;
               if (!var15) {
                  var3 = var4;
                  if (!var17.verticalDimensionFixed) {
                     label77: {
                        if (!var17.horizontalDimensionFixed) {
                           var3 = var4;
                           if (var17.matchConstraintDefaultWidth == 1) {
                              break label77;
                           }
                        }

                        var3 = var4;
                        if (var17.width != -1) {
                           label72: {
                              if (!var17.verticalDimensionFixed) {
                                 var3 = var4;
                                 if (var17.matchConstraintDefaultHeight == 1) {
                                    break label72;
                                 }

                                 if (var17.height == -1) {
                                    var3 = var4;
                                    break label72;
                                 }
                              }

                              var3 = false;
                           }
                        }
                     }
                  }
               }

               boolean var10 = false;
               boolean var9 = false;
               boolean var11 = false;
               var4 = false;
               if (var3) {
                  if (var6 != 0 && var6 != -1) {
                     var6 = getChildMeasureSpec(var1, var13, var6);
                     var3 = var9;
                  } else {
                     var6 = getChildMeasureSpec(var1, var13, -2);
                     var3 = true;
                  }

                  if (var7 != 0 && var7 != -1) {
                     var7 = getChildMeasureSpec(var2, var12, var7);
                  } else {
                     var7 = getChildMeasureSpec(var2, var12, -2);
                     var4 = true;
                  }

                  var16.measure(var6, var7);
                  var6 = var16.getMeasuredWidth();
                  var7 = var16.getMeasuredHeight();
               } else {
                  var4 = var11;
                  var3 = var10;
               }

               var18.setWidth(var6);
               var18.setHeight(var7);
               if (var3) {
                  var18.setWrapWidth(var6);
               }

               if (var4) {
                  var18.setWrapHeight(var7);
               }

               if (var17.needsBaseline) {
                  int var19 = var16.getBaseline();
                  if (var19 != -1) {
                     var18.setBaselineDistance(var19);
                  }
               }
            }
         }
      }

   }

   private void setChildrenConstraints() {
      ConstraintSet var13 = this.mConstraintSet;
      if (var13 != null) {
         var13.applyToInternal(this);
      }

      int var12 = this.getChildCount();
      this.mLayoutWidget.removeAllChildren();

      for(int var8 = 0; var8 < var12; ++var8) {
         View var15 = this.getChildAt(var8);
         ConstraintWidget var14 = this.getViewWidget(var15);
         if (var14 != null) {
            ConstraintLayout.LayoutParams var17 = (ConstraintLayout.LayoutParams)var15.getLayoutParams();
            var14.reset();
            var14.setVisibility(var15.getVisibility());
            var14.setCompanionWidget(var15);
            this.mLayoutWidget.add(var14);
            if (!var17.verticalDimensionFixed || !var17.horizontalDimensionFixed) {
               this.mVariableDimensionsWidgets.add(var14);
            }

            if (var17.isGuideline) {
               android.support.constraint.solver.widgets.Guideline var18 = (android.support.constraint.solver.widgets.Guideline)var14;
               if (var17.guideBegin != -1) {
                  var18.setGuideBegin(var17.guideBegin);
               }

               if (var17.guideEnd != -1) {
                  var18.setGuideEnd(var17.guideEnd);
               }

               if (var17.guidePercent != -1.0F) {
                  var18.setGuidePercent(var17.guidePercent);
               }
            } else if (var17.resolvedLeftToLeft != -1 || var17.resolvedLeftToRight != -1 || var17.resolvedRightToLeft != -1 || var17.resolvedRightToRight != -1 || var17.topToTop != -1 || var17.topToBottom != -1 || var17.bottomToTop != -1 || var17.bottomToBottom != -1 || var17.baselineToBaseline != -1 || var17.editorAbsoluteX != -1 || var17.editorAbsoluteY != -1 || var17.width == -1 || var17.height == -1) {
               int var4 = var17.resolvedLeftToLeft;
               int var5 = var17.resolvedLeftToRight;
               int var3 = var17.resolvedRightToLeft;
               int var2 = var17.resolvedRightToRight;
               int var6 = var17.resolveGoneLeftMargin;
               int var7 = var17.resolveGoneRightMargin;
               float var1 = var17.resolvedHorizontalBias;
               if (android.os.Build.VERSION.SDK_INT < 17) {
                  label213: {
                     var2 = var17.leftToLeft;
                     var3 = var17.leftToRight;
                     var7 = var17.rightToLeft;
                     var6 = var17.rightToRight;
                     var5 = var17.goneLeftMargin;
                     var4 = var17.goneRightMargin;
                     var1 = var17.horizontalBias;
                     if (var2 == -1 && var3 == -1) {
                        if (var17.startToStart != -1) {
                           var2 = var17.startToStart;
                        } else if (var17.startToEnd != -1) {
                           var3 = var17.startToEnd;
                        }
                     }

                     int var9;
                     int var10;
                     int var11;
                     if (var7 == -1 && var6 == -1) {
                        if (var17.endToStart != -1) {
                           var11 = var17.endToStart;
                           var7 = var3;
                           var9 = var5;
                           var10 = var4;
                           var3 = var11;
                           var4 = var2;
                           var2 = var6;
                           var5 = var7;
                           var6 = var9;
                           var7 = var10;
                           break label213;
                        }

                        if (var17.endToEnd != -1) {
                           var11 = var17.endToEnd;
                           var6 = var3;
                           var9 = var5;
                           var10 = var4;
                           var3 = var7;
                           var4 = var2;
                           var2 = var11;
                           var5 = var6;
                           var6 = var9;
                           var7 = var10;
                           break label213;
                        }
                     }

                     var9 = var3;
                     var10 = var5;
                     var11 = var4;
                     var3 = var7;
                     var4 = var2;
                     var2 = var6;
                     var5 = var9;
                     var6 = var10;
                     var7 = var11;
                  }
               }

               ConstraintWidget var19;
               if (var4 != -1) {
                  var19 = this.getTargetWidget(var4);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.LEFT, var19, ConstraintAnchor.Type.LEFT, var17.leftMargin, var6);
                  }
               } else if (var5 != -1) {
                  var19 = this.getTargetWidget(var5);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.LEFT, var19, ConstraintAnchor.Type.RIGHT, var17.leftMargin, var6);
                  }
               }

               if (var3 != -1) {
                  var19 = this.getTargetWidget(var3);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.RIGHT, var19, ConstraintAnchor.Type.LEFT, var17.rightMargin, var7);
                  }
               } else if (var2 != -1) {
                  var19 = this.getTargetWidget(var2);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.RIGHT, var19, ConstraintAnchor.Type.RIGHT, var17.rightMargin, var7);
                  }
               }

               if (var17.topToTop != -1) {
                  var19 = this.getTargetWidget(var17.topToTop);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.TOP, var19, ConstraintAnchor.Type.TOP, var17.topMargin, var17.goneTopMargin);
                  }
               } else if (var17.topToBottom != -1) {
                  var19 = this.getTargetWidget(var17.topToBottom);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.TOP, var19, ConstraintAnchor.Type.BOTTOM, var17.topMargin, var17.goneTopMargin);
                  }
               }

               if (var17.bottomToTop != -1) {
                  var19 = this.getTargetWidget(var17.bottomToTop);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.BOTTOM, var19, ConstraintAnchor.Type.TOP, var17.bottomMargin, var17.goneBottomMargin);
                  }
               } else if (var17.bottomToBottom != -1) {
                  var19 = this.getTargetWidget(var17.bottomToBottom);
                  if (var19 != null) {
                     var14.immediateConnect(ConstraintAnchor.Type.BOTTOM, var19, ConstraintAnchor.Type.BOTTOM, var17.bottomMargin, var17.goneBottomMargin);
                  }
               }

               if (var17.baselineToBaseline != -1) {
                  View var16 = (View)this.mChildrenByIds.get(var17.baselineToBaseline);
                  var19 = this.getTargetWidget(var17.baselineToBaseline);
                  if (var19 != null && var16 != null && var16.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                     ConstraintLayout.LayoutParams var20 = (ConstraintLayout.LayoutParams)var16.getLayoutParams();
                     var17.needsBaseline = true;
                     var20.needsBaseline = true;
                     var14.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var19.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                     var14.getAnchor(ConstraintAnchor.Type.TOP).reset();
                     var14.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                  }
               }

               if (var1 >= 0.0F && var1 != 0.5F) {
                  var14.setHorizontalBiasPercent(var1);
               }

               if (var17.verticalBias >= 0.0F && var17.verticalBias != 0.5F) {
                  var14.setVerticalBiasPercent(var17.verticalBias);
               }

               if (this.isInEditMode() && (var17.editorAbsoluteX != -1 || var17.editorAbsoluteY != -1)) {
                  var14.setOrigin(var17.editorAbsoluteX, var17.editorAbsoluteY);
               }

               if (!var17.horizontalDimensionFixed) {
                  if (var17.width == -1) {
                     var14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                     var14.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = var17.leftMargin;
                     var14.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = var17.rightMargin;
                  } else {
                     var14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                     var14.setWidth(0);
                  }
               } else {
                  var14.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                  var14.setWidth(var17.width);
               }

               if (!var17.verticalDimensionFixed) {
                  if (var17.height == -1) {
                     var14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                     var14.getAnchor(ConstraintAnchor.Type.TOP).mMargin = var17.topMargin;
                     var14.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = var17.bottomMargin;
                  } else {
                     var14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                     var14.setHeight(0);
                  }
               } else {
                  var14.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                  var14.setHeight(var17.height);
               }

               if (var17.dimensionRatio != null) {
                  var14.setDimensionRatio(var17.dimensionRatio);
               }

               var14.setHorizontalWeight(var17.horizontalWeight);
               var14.setVerticalWeight(var17.verticalWeight);
               var14.setHorizontalChainStyle(var17.horizontalChainStyle);
               var14.setVerticalChainStyle(var17.verticalChainStyle);
               var14.setHorizontalMatchStyle(var17.matchConstraintDefaultWidth, var17.matchConstraintMinWidth, var17.matchConstraintMaxWidth);
               var14.setVerticalMatchStyle(var17.matchConstraintDefaultHeight, var17.matchConstraintMinHeight, var17.matchConstraintMaxHeight);
            }
         }
      }

   }

   private void setSelfDimensionBehaviour(int var1, int var2) {
      int var8 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var5 = MeasureSpec.getMode(var2);
      var2 = MeasureSpec.getSize(var2);
      int var6 = this.getPaddingTop();
      int var7 = this.getPaddingBottom();
      int var9 = this.getPaddingLeft();
      int var10 = this.getPaddingRight();
      ConstraintWidget.DimensionBehaviour var11 = ConstraintWidget.DimensionBehaviour.FIXED;
      ConstraintWidget.DimensionBehaviour var12 = ConstraintWidget.DimensionBehaviour.FIXED;
      byte var4 = 0;
      byte var3 = 0;
      this.getLayoutParams();
      if (var8 != Integer.MIN_VALUE) {
         if (var8 != 0) {
            if (var8 != 1073741824) {
               var1 = var4;
            } else {
               var1 = Math.min(this.mMaxWidth, var1) - (var9 + var10);
            }
         } else {
            var11 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            var1 = var4;
         }
      } else {
         var11 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
      }

      if (var5 != Integer.MIN_VALUE) {
         if (var5 != 0) {
            if (var5 != 1073741824) {
               var2 = var3;
            } else {
               var2 = Math.min(this.mMaxHeight, var2) - (var6 + var7);
            }
         } else {
            var12 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            var2 = var3;
         }
      } else {
         var12 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
      }

      this.mLayoutWidget.setMinWidth(0);
      this.mLayoutWidget.setMinHeight(0);
      this.mLayoutWidget.setHorizontalDimensionBehaviour(var11);
      this.mLayoutWidget.setWidth(var1);
      this.mLayoutWidget.setVerticalDimensionBehaviour(var12);
      this.mLayoutWidget.setHeight(var2);
      this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
      this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
   }

   private void updateHierarchy() {
      int var4 = this.getChildCount();
      boolean var3 = false;
      int var1 = 0;

      boolean var2;
      while(true) {
         var2 = var3;
         if (var1 >= var4) {
            break;
         }

         if (this.getChildAt(var1).isLayoutRequested()) {
            var2 = true;
            break;
         }

         ++var1;
      }

      if (var2) {
         this.mVariableDimensionsWidgets.clear();
         this.setChildrenConstraints();
      }
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (android.os.Build.VERSION.SDK_INT < 14) {
         this.onViewAdded(var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof ConstraintLayout.LayoutParams;
   }

   protected ConstraintLayout.LayoutParams generateDefaultLayoutParams() {
      return new ConstraintLayout.LayoutParams(-2, -2);
   }

   public ConstraintLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ConstraintLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new ConstraintLayout.LayoutParams(var1);
   }

   public int getMaxHeight() {
      return this.mMaxHeight;
   }

   public int getMaxWidth() {
      return this.mMaxWidth;
   }

   public int getMinHeight() {
      return this.mMinHeight;
   }

   public int getMinWidth() {
      return this.mMinWidth;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = this.getChildCount();
      var1 = this.isInEditMode();

      for(var2 = 0; var2 < var3; ++var2) {
         View var6 = this.getChildAt(var2);
         ConstraintLayout.LayoutParams var7 = (ConstraintLayout.LayoutParams)var6.getLayoutParams();
         if (var6.getVisibility() != 8 || var7.isGuideline || var1) {
            ConstraintWidget var8 = var7.widget;
            var4 = var8.getDrawX();
            var5 = var8.getDrawY();
            var6.layout(var4, var5, var8.getWidth() + var4, var8.getHeight() + var5);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var10 = this.getPaddingLeft();
      int var9 = this.getPaddingTop();
      this.mLayoutWidget.setX(var10);
      this.mLayoutWidget.setY(var9);
      this.setSelfDimensionBehaviour(var1, var2);
      if (this.mDirtyHierarchy) {
         this.mDirtyHierarchy = false;
         this.updateHierarchy();
      }

      this.internalMeasureChildren(var1, var2);
      if (this.getChildCount() > 0) {
         this.solveLinearSystem();
      }

      int var3 = 0;
      byte var11 = 0;
      int var7 = this.mVariableDimensionsWidgets.size();
      int var13 = this.getPaddingBottom() + var9;
      int var14 = this.getPaddingRight() + var10;
      int var19;
      if (var7 > 0) {
         boolean var4 = false;
         ConstraintWidget.DimensionBehaviour var15 = this.mLayoutWidget.getHorizontalDimensionBehaviour();
         ConstraintWidget.DimensionBehaviour var16 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
         boolean var5 = true;
         boolean var18;
         if (var15 == var16) {
            var18 = true;
         } else {
            var18 = false;
         }

         if (this.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var5 = false;
         }

         int var8 = 0;
         boolean var6 = var18;

         for(var3 = var11; var8 < var7; ++var8) {
            ConstraintWidget var22 = (ConstraintWidget)this.mVariableDimensionsWidgets.get(var8);
            if (!(var22 instanceof android.support.constraint.solver.widgets.Guideline)) {
               View var23 = (View)var22.getCompanionWidget();
               if (var23 != null && var23.getVisibility() != 8) {
                  ConstraintLayout.LayoutParams var17 = (ConstraintLayout.LayoutParams)var23.getLayoutParams();
                  int var21;
                  if (var17.width == -2) {
                     var21 = getChildMeasureSpec(var1, var14, var17.width);
                  } else {
                     var21 = MeasureSpec.makeMeasureSpec(var22.getWidth(), 1073741824);
                  }

                  int var12;
                  if (var17.height == -2) {
                     var12 = getChildMeasureSpec(var2, var13, var17.height);
                  } else {
                     var12 = MeasureSpec.makeMeasureSpec(var22.getHeight(), 1073741824);
                  }

                  var23.measure(var21, var12);
                  var12 = var23.getMeasuredWidth();
                  var21 = var23.getMeasuredHeight();
                  if (var12 != var22.getWidth()) {
                     var22.setWidth(var12);
                     if (var6 && var22.getRight() > this.mLayoutWidget.getWidth()) {
                        var19 = var22.getRight();
                        var12 = var22.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin();
                        this.mLayoutWidget.setWidth(Math.max(this.mMinWidth, var19 + var12));
                     }

                     var4 = true;
                  }

                  if (var21 != var22.getHeight()) {
                     var22.setHeight(var21);
                     if (var5 && var22.getBottom() > this.mLayoutWidget.getHeight()) {
                        var19 = var22.getBottom();
                        var21 = var22.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin();
                        this.mLayoutWidget.setHeight(Math.max(this.mMinHeight, var19 + var21));
                     }

                     var4 = true;
                  }

                  if (var17.needsBaseline) {
                     var21 = var23.getBaseline();
                     if (var21 != -1 && var21 != var22.getBaselineDistance()) {
                        var22.setBaselineDistance(var21);
                        var4 = true;
                     }
                  }

                  if (android.os.Build.VERSION.SDK_INT >= 11) {
                     var3 = combineMeasuredStates(var3, var23.getMeasuredState());
                  }
               }
            }
         }

         if (var4) {
            this.solveLinearSystem();
         }
      }

      var19 = this.mLayoutWidget.getWidth() + var14;
      int var20 = this.mLayoutWidget.getHeight() + var13;
      if (android.os.Build.VERSION.SDK_INT >= 11) {
         var1 = resolveSizeAndState(var19, var1, var3);
         var2 = resolveSizeAndState(var20, var2, var3 << 16);
         var1 = Math.min(this.mMaxWidth, var1);
         var2 = Math.min(this.mMaxHeight, var2);
         var1 &= 16777215;
         var2 &= 16777215;
         if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
            var1 |= 16777216;
         }

         if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
            var2 |= 16777216;
         }

         this.setMeasuredDimension(var1, var2);
      } else {
         this.setMeasuredDimension(var19, var20);
      }
   }

   public void onViewAdded(View var1) {
      if (android.os.Build.VERSION.SDK_INT >= 14) {
         super.onViewAdded(var1);
      }

      ConstraintWidget var2 = this.getViewWidget(var1);
      if (var1 instanceof Guideline && !(var2 instanceof android.support.constraint.solver.widgets.Guideline)) {
         ConstraintLayout.LayoutParams var3 = (ConstraintLayout.LayoutParams)var1.getLayoutParams();
         var3.widget = new android.support.constraint.solver.widgets.Guideline();
         var3.isGuideline = true;
         ((android.support.constraint.solver.widgets.Guideline)var3.widget).setOrientation(var3.orientation);
         var2 = var3.widget;
      }

      this.mChildrenByIds.put(var1.getId(), var1);
      this.mDirtyHierarchy = true;
   }

   public void onViewRemoved(View var1) {
      if (android.os.Build.VERSION.SDK_INT >= 14) {
         super.onViewRemoved(var1);
      }

      this.mChildrenByIds.remove(var1.getId());
      this.mLayoutWidget.remove(this.getViewWidget(var1));
      this.mDirtyHierarchy = true;
   }

   public void removeView(View var1) {
      super.removeView(var1);
      if (android.os.Build.VERSION.SDK_INT < 14) {
         this.onViewRemoved(var1);
      }
   }

   public void requestLayout() {
      super.requestLayout();
      this.mDirtyHierarchy = true;
   }

   public void setConstraintSet(ConstraintSet var1) {
      this.mConstraintSet = var1;
   }

   public void setId(int var1) {
      this.mChildrenByIds.remove(this.getId());
      super.setId(var1);
      this.mChildrenByIds.put(this.getId(), this);
   }

   public void setMaxHeight(int var1) {
      if (var1 != this.mMaxHeight) {
         this.mMaxHeight = var1;
         this.requestLayout();
      }
   }

   public void setMaxWidth(int var1) {
      if (var1 != this.mMaxWidth) {
         this.mMaxWidth = var1;
         this.requestLayout();
      }
   }

   public void setMinHeight(int var1) {
      if (var1 != this.mMinHeight) {
         this.mMinHeight = var1;
         this.requestLayout();
      }
   }

   public void setMinWidth(int var1) {
      if (var1 != this.mMinWidth) {
         this.mMinWidth = var1;
         this.requestLayout();
      }
   }

   public void setOptimizationLevel(int var1) {
      this.mLayoutWidget.setOptimizationLevel(var1);
   }

   protected void solveLinearSystem() {
      this.mLayoutWidget.layout();
   }

   public static class LayoutParams extends MarginLayoutParams {
      public static final int BASELINE = 5;
      public static final int BOTTOM = 4;
      public static final int CHAIN_PACKED = 2;
      public static final int CHAIN_SPREAD = 0;
      public static final int CHAIN_SPREAD_INSIDE = 1;
      public static final int END = 7;
      public static final int HORIZONTAL = 0;
      public static final int LEFT = 1;
      public static final int MATCH_CONSTRAINT = 0;
      public static final int MATCH_CONSTRAINT_SPREAD = 0;
      public static final int MATCH_CONSTRAINT_WRAP = 1;
      public static final int PARENT_ID = 0;
      public static final int RIGHT = 2;
      public static final int START = 6;
      public static final int TOP = 3;
      public static final int UNSET = -1;
      public static final int VERTICAL = 1;
      public int baselineToBaseline = -1;
      public int bottomToBottom = -1;
      public int bottomToTop = -1;
      public String dimensionRatio = null;
      int dimensionRatioSide = 1;
      float dimensionRatioValue = 0.0F;
      public int editorAbsoluteX = -1;
      public int editorAbsoluteY = -1;
      public int endToEnd = -1;
      public int endToStart = -1;
      public int goneBottomMargin = -1;
      public int goneEndMargin = -1;
      public int goneLeftMargin = -1;
      public int goneRightMargin = -1;
      public int goneStartMargin = -1;
      public int goneTopMargin = -1;
      public int guideBegin = -1;
      public int guideEnd = -1;
      public float guidePercent = -1.0F;
      public float horizontalBias = 0.5F;
      public int horizontalChainStyle = 0;
      boolean horizontalDimensionFixed = true;
      public float horizontalWeight = 0.0F;
      boolean isGuideline = false;
      public int leftToLeft = -1;
      public int leftToRight = -1;
      public int matchConstraintDefaultHeight = 0;
      public int matchConstraintDefaultWidth = 0;
      public int matchConstraintMaxHeight = 0;
      public int matchConstraintMaxWidth = 0;
      public int matchConstraintMinHeight = 0;
      public int matchConstraintMinWidth = 0;
      boolean needsBaseline = false;
      public int orientation = -1;
      int resolveGoneLeftMargin = -1;
      int resolveGoneRightMargin = -1;
      float resolvedHorizontalBias = 0.5F;
      int resolvedLeftToLeft = -1;
      int resolvedLeftToRight = -1;
      int resolvedRightToLeft = -1;
      int resolvedRightToRight = -1;
      public int rightToLeft = -1;
      public int rightToRight = -1;
      public int startToEnd = -1;
      public int startToStart = -1;
      public int topToBottom = -1;
      public int topToTop = -1;
      public float verticalBias = 0.5F;
      public int verticalChainStyle = 0;
      boolean verticalDimensionFixed = true;
      public float verticalWeight = 0.0F;
      ConstraintWidget widget = new ConstraintWidget();

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var15 = var1.obtainStyledAttributes(var2, R$styleable.ConstraintLayout_Layout);
         int var7 = var15.getIndexCount();

         for(int var5 = 0; var5 < var7; ++var5) {
            int var6 = var15.getIndex(var5);
            if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
               this.leftToLeft = var15.getResourceId(var6, this.leftToLeft);
               if (this.leftToLeft == -1) {
                  this.leftToLeft = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
               this.leftToRight = var15.getResourceId(var6, this.leftToRight);
               if (this.leftToRight == -1) {
                  this.leftToRight = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
               this.rightToLeft = var15.getResourceId(var6, this.rightToLeft);
               if (this.rightToLeft == -1) {
                  this.rightToLeft = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
               this.rightToRight = var15.getResourceId(var6, this.rightToRight);
               if (this.rightToRight == -1) {
                  this.rightToRight = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
               this.topToTop = var15.getResourceId(var6, this.topToTop);
               if (this.topToTop == -1) {
                  this.topToTop = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
               this.topToBottom = var15.getResourceId(var6, this.topToBottom);
               if (this.topToBottom == -1) {
                  this.topToBottom = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
               this.bottomToTop = var15.getResourceId(var6, this.bottomToTop);
               if (this.bottomToTop == -1) {
                  this.bottomToTop = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
               this.bottomToBottom = var15.getResourceId(var6, this.bottomToBottom);
               if (this.bottomToBottom == -1) {
                  this.bottomToBottom = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
               this.baselineToBaseline = var15.getResourceId(var6, this.baselineToBaseline);
               if (this.baselineToBaseline == -1) {
                  this.baselineToBaseline = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
               this.editorAbsoluteX = var15.getDimensionPixelOffset(var6, this.editorAbsoluteX);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
               this.editorAbsoluteY = var15.getDimensionPixelOffset(var6, this.editorAbsoluteY);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
               this.guideBegin = var15.getDimensionPixelOffset(var6, this.guideBegin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
               this.guideEnd = var15.getDimensionPixelOffset(var6, this.guideEnd);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
               this.guidePercent = var15.getFloat(var6, this.guidePercent);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_android_orientation) {
               this.orientation = var15.getInt(var6, this.orientation);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
               this.startToEnd = var15.getResourceId(var6, this.startToEnd);
               if (this.startToEnd == -1) {
                  this.startToEnd = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
               this.startToStart = var15.getResourceId(var6, this.startToStart);
               if (this.startToStart == -1) {
                  this.startToStart = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
               this.endToStart = var15.getResourceId(var6, this.endToStart);
               if (this.endToStart == -1) {
                  this.endToStart = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
               this.endToEnd = var15.getResourceId(var6, this.endToEnd);
               if (this.endToEnd == -1) {
                  this.endToEnd = var15.getInt(var6, -1);
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
               this.goneLeftMargin = var15.getDimensionPixelSize(var6, this.goneLeftMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
               this.goneTopMargin = var15.getDimensionPixelSize(var6, this.goneTopMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
               this.goneRightMargin = var15.getDimensionPixelSize(var6, this.goneRightMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
               this.goneBottomMargin = var15.getDimensionPixelSize(var6, this.goneBottomMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
               this.goneStartMargin = var15.getDimensionPixelSize(var6, this.goneStartMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
               this.goneEndMargin = var15.getDimensionPixelSize(var6, this.goneEndMargin);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
               this.horizontalBias = var15.getFloat(var6, this.horizontalBias);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
               this.verticalBias = var15.getFloat(var6, this.verticalBias);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
               this.dimensionRatio = var15.getString(var6);
               this.dimensionRatioValue = Float.NaN;
               this.dimensionRatioSide = -1;
               String var16 = this.dimensionRatio;
               if (var16 != null) {
                  int var8 = var16.length();
                  var6 = this.dimensionRatio.indexOf(44);
                  if (var6 > 0 && var6 < var8 - 1) {
                     var16 = this.dimensionRatio.substring(0, var6);
                     if (var16.equalsIgnoreCase("W")) {
                        this.dimensionRatioSide = 0;
                     } else if (var16.equalsIgnoreCase("H")) {
                        this.dimensionRatioSide = 1;
                     }

                     ++var6;
                  } else {
                     var6 = 0;
                  }

                  int var9 = this.dimensionRatio.indexOf(58);
                  if (var9 >= 0 && var9 < var8 - 1) {
                     var16 = this.dimensionRatio.substring(var6, var9);
                     String var10 = this.dimensionRatio.substring(var9 + 1);
                     if (var16.length() > 0 && var10.length() > 0) {
                        float var3;
                        float var4;
                        boolean var10001;
                        try {
                           var3 = Float.parseFloat(var16);
                           var4 = Float.parseFloat(var10);
                        } catch (NumberFormatException var13) {
                           var10001 = false;
                           continue;
                        }

                        if (var3 > 0.0F && var4 > 0.0F) {
                           try {
                              if (this.dimensionRatioSide == 1) {
                                 this.dimensionRatioValue = Math.abs(var4 / var3);
                                 continue;
                              }
                           } catch (NumberFormatException var14) {
                              var10001 = false;
                              continue;
                           }

                           try {
                              this.dimensionRatioValue = Math.abs(var3 / var4);
                           } catch (NumberFormatException var12) {
                              var10001 = false;
                           }
                        }
                     }
                  } else {
                     var16 = this.dimensionRatio.substring(var6);
                     if (var16.length() > 0) {
                        try {
                           this.dimensionRatioValue = Float.parseFloat(var16);
                        } catch (NumberFormatException var11) {
                        }
                     }
                  }
               }
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight) {
               this.horizontalWeight = var15.getFloat(var6, 0.0F);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintVertical_weight) {
               this.verticalWeight = var15.getFloat(var6, 0.0F);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle) {
               this.horizontalChainStyle = var15.getInt(var6, 0);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle) {
               this.verticalChainStyle = var15.getInt(var6, 0);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintWidth_default) {
               this.matchConstraintDefaultWidth = var15.getInt(var6, 0);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHeight_default) {
               this.matchConstraintDefaultHeight = var15.getInt(var6, 0);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintWidth_min) {
               this.matchConstraintMinWidth = var15.getDimensionPixelSize(var6, this.matchConstraintMinWidth);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintWidth_max) {
               this.matchConstraintMaxWidth = var15.getDimensionPixelSize(var6, this.matchConstraintMaxWidth);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHeight_min) {
               this.matchConstraintMinHeight = var15.getDimensionPixelSize(var6, this.matchConstraintMinHeight);
            } else if (var6 == R$styleable.ConstraintLayout_Layout_layout_constraintHeight_max) {
               this.matchConstraintMaxHeight = var15.getDimensionPixelSize(var6, this.matchConstraintMaxHeight);
            } else if (var6 != R$styleable.ConstraintLayout_Layout_layout_constraintLeft_creator && var6 != R$styleable.ConstraintLayout_Layout_layout_constraintTop_creator && var6 != R$styleable.ConstraintLayout_Layout_layout_constraintRight_creator && var6 != R$styleable.ConstraintLayout_Layout_layout_constraintBottom_creator) {
               var6 = R$styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator;
            }
         }

         var15.recycle();
         this.validate();
      }

      public LayoutParams(ConstraintLayout.LayoutParams var1) {
         super(var1);
         this.guideBegin = var1.guideBegin;
         this.guideEnd = var1.guideEnd;
         this.guidePercent = var1.guidePercent;
         this.leftToLeft = var1.leftToLeft;
         this.leftToRight = var1.leftToRight;
         this.rightToLeft = var1.rightToLeft;
         this.rightToRight = var1.rightToRight;
         this.topToTop = var1.topToTop;
         this.topToBottom = var1.topToBottom;
         this.bottomToTop = var1.bottomToTop;
         this.bottomToBottom = var1.bottomToBottom;
         this.baselineToBaseline = var1.baselineToBaseline;
         this.startToEnd = var1.startToEnd;
         this.startToStart = var1.startToStart;
         this.endToStart = var1.endToStart;
         this.endToEnd = var1.endToEnd;
         this.goneLeftMargin = var1.goneLeftMargin;
         this.goneTopMargin = var1.goneTopMargin;
         this.goneRightMargin = var1.goneRightMargin;
         this.goneBottomMargin = var1.goneBottomMargin;
         this.goneStartMargin = var1.goneStartMargin;
         this.goneEndMargin = var1.goneEndMargin;
         this.horizontalBias = var1.horizontalBias;
         this.verticalBias = var1.verticalBias;
         this.dimensionRatio = var1.dimensionRatio;
         this.dimensionRatioValue = var1.dimensionRatioValue;
         this.dimensionRatioSide = var1.dimensionRatioSide;
         this.horizontalWeight = var1.horizontalWeight;
         this.verticalWeight = var1.verticalWeight;
         this.horizontalChainStyle = var1.horizontalChainStyle;
         this.verticalChainStyle = var1.verticalChainStyle;
         this.matchConstraintDefaultWidth = var1.matchConstraintDefaultWidth;
         this.matchConstraintDefaultHeight = var1.matchConstraintDefaultHeight;
         this.matchConstraintMinWidth = var1.matchConstraintMinWidth;
         this.matchConstraintMaxWidth = var1.matchConstraintMaxWidth;
         this.matchConstraintMinHeight = var1.matchConstraintMinHeight;
         this.matchConstraintMaxHeight = var1.matchConstraintMaxHeight;
         this.editorAbsoluteX = var1.editorAbsoluteX;
         this.editorAbsoluteY = var1.editorAbsoluteY;
         this.orientation = var1.orientation;
         this.horizontalDimensionFixed = var1.horizontalDimensionFixed;
         this.verticalDimensionFixed = var1.verticalDimensionFixed;
         this.needsBaseline = var1.needsBaseline;
         this.isGuideline = var1.isGuideline;
         this.resolvedLeftToLeft = var1.resolvedLeftToLeft;
         this.resolvedLeftToRight = var1.resolvedLeftToRight;
         this.resolvedRightToLeft = var1.resolvedRightToLeft;
         this.resolvedRightToRight = var1.resolvedRightToRight;
         this.resolveGoneLeftMargin = var1.resolveGoneLeftMargin;
         this.resolveGoneRightMargin = var1.resolveGoneRightMargin;
         this.resolvedHorizontalBias = var1.resolvedHorizontalBias;
         this.widget = var1.widget;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      @TargetApi(17)
      public void resolveLayoutDirection(int var1) {
         super.resolveLayoutDirection(var1);
         this.resolvedRightToLeft = -1;
         this.resolvedRightToRight = -1;
         this.resolvedLeftToLeft = -1;
         this.resolvedLeftToRight = -1;
         this.resolveGoneLeftMargin = -1;
         this.resolveGoneRightMargin = -1;
         this.resolveGoneLeftMargin = this.goneLeftMargin;
         this.resolveGoneRightMargin = this.goneRightMargin;
         this.resolvedHorizontalBias = this.horizontalBias;
         int var2 = this.getLayoutDirection();
         boolean var3 = true;
         if (1 != var2) {
            var3 = false;
         }

         if (var3) {
            var1 = this.startToEnd;
            if (var1 != -1) {
               this.resolvedRightToLeft = var1;
            } else {
               var1 = this.startToStart;
               if (var1 != -1) {
                  this.resolvedRightToRight = var1;
               }
            }

            var1 = this.endToStart;
            if (var1 != -1) {
               this.resolvedLeftToRight = var1;
            }

            var1 = this.endToEnd;
            if (var1 != -1) {
               this.resolvedLeftToLeft = var1;
            }

            var1 = this.goneStartMargin;
            if (var1 != -1) {
               this.resolveGoneRightMargin = var1;
            }

            var1 = this.goneEndMargin;
            if (var1 != -1) {
               this.resolveGoneLeftMargin = var1;
            }

            this.resolvedHorizontalBias = 1.0F - this.horizontalBias;
         } else {
            var1 = this.startToEnd;
            if (var1 != -1) {
               this.resolvedLeftToRight = var1;
            }

            var1 = this.startToStart;
            if (var1 != -1) {
               this.resolvedLeftToLeft = var1;
            }

            var1 = this.endToStart;
            if (var1 != -1) {
               this.resolvedRightToLeft = var1;
            }

            var1 = this.endToEnd;
            if (var1 != -1) {
               this.resolvedRightToRight = var1;
            }

            var1 = this.goneStartMargin;
            if (var1 != -1) {
               this.resolveGoneLeftMargin = var1;
            }

            var1 = this.goneEndMargin;
            if (var1 != -1) {
               this.resolveGoneRightMargin = var1;
            }
         }

         if (this.endToStart == -1 && this.endToEnd == -1) {
            var1 = this.rightToLeft;
            if (var1 != -1) {
               this.resolvedRightToLeft = var1;
            } else {
               var1 = this.rightToRight;
               if (var1 != -1) {
                  this.resolvedRightToRight = var1;
               }
            }
         }

         if (this.startToStart == -1 && this.startToEnd == -1) {
            var1 = this.leftToLeft;
            if (var1 != -1) {
               this.resolvedLeftToLeft = var1;
            } else {
               var1 = this.leftToRight;
               if (var1 != -1) {
                  this.resolvedLeftToRight = var1;
               }
            }
         }
      }

      public void validate() {
         this.isGuideline = false;
         this.horizontalDimensionFixed = true;
         this.verticalDimensionFixed = true;
         if (this.width == 0 || this.width == -1) {
            this.horizontalDimensionFixed = false;
         }

         if (this.height == 0 || this.height == -1) {
            this.verticalDimensionFixed = false;
         }

         if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
            this.isGuideline = true;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (!(this.widget instanceof android.support.constraint.solver.widgets.Guideline)) {
               this.widget = new android.support.constraint.solver.widgets.Guideline();
            }

            ((android.support.constraint.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
         }
      }
   }
}
