// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

public class Rectangle
{
    public int height;
    public int width;
    public int x;
    public int y;
    
    public boolean contains(int y, final int n) {
        final int x = this.x;
        if (y >= x && y < x + this.width) {
            y = this.y;
            if (n >= y && n < y + this.height) {
                return true;
            }
        }
        return false;
    }
    
    public int getCenterX() {
        return (this.x + this.width) / 2;
    }
    
    public int getCenterY() {
        return (this.y + this.height) / 2;
    }
    
    void grow(final int n, final int n2) {
        this.x -= n;
        this.y -= n2;
        this.width += n * 2;
        this.height += n2 * 2;
    }
    
    boolean intersects(final Rectangle rectangle) {
        final int x = this.x;
        final int x2 = rectangle.x;
        if (x >= x2 && x < x2 + rectangle.width) {
            final int y = this.y;
            final int y2 = rectangle.y;
            if (y >= y2 && y < y2 + rectangle.height) {
                return true;
            }
        }
        return false;
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
