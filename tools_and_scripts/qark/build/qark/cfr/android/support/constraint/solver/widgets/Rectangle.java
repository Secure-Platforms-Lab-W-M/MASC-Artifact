/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

public class Rectangle {
    public int height;
    public int width;
    public int x;
    public int y;

    public boolean contains(int n, int n2) {
        int n3 = this.x;
        if (n >= n3 && n < n3 + this.width && n2 >= (n = this.y) && n2 < n + this.height) {
            return true;
        }
        return false;
    }

    public int getCenterX() {
        return (this.x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.y + this.height) / 2;
    }

    void grow(int n, int n2) {
        this.x -= n;
        this.y -= n2;
        this.width += n * 2;
        this.height += n2 * 2;
    }

    boolean intersects(Rectangle rectangle) {
        int n = this.x;
        int n2 = rectangle.x;
        if (n >= n2 && n < n2 + rectangle.width && (n = this.y) >= (n2 = rectangle.y) && n < n2 + rectangle.height) {
            return true;
        }
        return false;
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }
}

