// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget
{
    protected ArrayList<ConstraintWidget> mChildren;
    
    public WidgetContainer() {
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public static Rectangle getBounds(final ArrayList<ConstraintWidget> list) {
        final Rectangle rectangle = new Rectangle();
        if (list.size() == 0) {
            return rectangle;
        }
        int x = Integer.MAX_VALUE;
        int right = 0;
        int y = Integer.MAX_VALUE;
        int bottom = 0;
        for (int i = 0; i < list.size(); ++i) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (constraintWidget.getX() < x) {
                x = constraintWidget.getX();
            }
            if (constraintWidget.getY() < y) {
                y = constraintWidget.getY();
            }
            if (constraintWidget.getRight() > right) {
                right = constraintWidget.getRight();
            }
            if (constraintWidget.getBottom() > bottom) {
                bottom = constraintWidget.getBottom();
            }
        }
        rectangle.setBounds(x, y, right - x, bottom - y);
        return rectangle;
    }
    
    public void add(final ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer)constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }
    
    public ConstraintWidget findWidget(final float n, final float n2) {
        ConstraintWidget constraintWidget = null;
        final int drawX = this.getDrawX();
        final int drawY = this.getDrawY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (n >= drawX && n <= width + drawX && n2 >= drawY && n2 <= height + drawY) {
            constraintWidget = this;
        }
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget2 = this.mChildren.get(i);
            if (constraintWidget2 instanceof WidgetContainer) {
                final ConstraintWidget widget = ((WidgetContainer)constraintWidget2).findWidget(n, n2);
                if (widget != null) {
                    constraintWidget = widget;
                }
            }
            else {
                final int drawX2 = constraintWidget2.getDrawX();
                final int drawY2 = constraintWidget2.getDrawY();
                final int width2 = constraintWidget2.getWidth();
                final int height2 = constraintWidget2.getHeight();
                if (n >= drawX2 && n <= width2 + drawX2 && n2 >= drawY2 && n2 <= height2 + drawY2) {
                    constraintWidget = constraintWidget2;
                }
            }
        }
        return constraintWidget;
    }
    
    public ArrayList<ConstraintWidget> findWidgets(int i, int size, final int n, final int n2) {
        final ArrayList<ConstraintWidget> list = new ArrayList<ConstraintWidget>();
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, size, n, n2);
        ConstraintWidget constraintWidget;
        Rectangle rectangle2;
        for (i = 0, size = this.mChildren.size(); i < size; ++i) {
            constraintWidget = this.mChildren.get(i);
            rectangle2 = new Rectangle();
            rectangle2.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
            if (rectangle.intersects(rectangle2)) {
                list.add(constraintWidget);
            }
        }
        return list;
    }
    
    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }
    
    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget constraintWidget = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
        }
        while (true) {
            final ConstraintWidget constraintWidget2 = constraintWidget;
            if (constraintWidget2 == null) {
                break;
            }
            constraintWidget = constraintWidget2.getParent();
            if (!(constraintWidget2 instanceof ConstraintWidgetContainer)) {
                continue;
            }
            constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget2;
        }
        return constraintWidgetContainer;
    }
    
    public void layout() {
        this.updateDrawPosition();
        final ArrayList<ConstraintWidget> mChildren = this.mChildren;
        if (mChildren == null) {
            return;
        }
        for (int size = mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
    }
    
    public void remove(final ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.setParent(null);
    }
    
    public void removeAllChildren() {
        this.mChildren.clear();
    }
    
    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }
    
    @Override
    public void resetGroups() {
        super.resetGroups();
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetGroups();
        }
    }
    
    @Override
    public void resetSolverVariables(final Cache cache) {
        super.resetSolverVariables(cache);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }
    
    @Override
    public void setOffset(int i, int size) {
        super.setOffset(i, size);
        for (size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).setOffset(this.getRootX(), this.getRootY());
        }
    }
    
    @Override
    public void updateDrawPosition() {
        super.updateDrawPosition();
        final ArrayList<ConstraintWidget> mChildren = this.mChildren;
        if (mChildren == null) {
            return;
        }
        for (int size = mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.setOffset(this.getDrawX(), this.getDrawY());
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                constraintWidget.updateDrawPosition();
            }
        }
    }
}
