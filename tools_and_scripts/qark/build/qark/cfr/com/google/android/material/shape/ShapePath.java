/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Path
 *  android.graphics.RectF
 */
package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ShapePath {
    protected static final float ANGLE_LEFT = 180.0f;
    private static final float ANGLE_UP = 270.0f;
    @Deprecated
    public float currentShadowAngle;
    @Deprecated
    public float endShadowAngle;
    @Deprecated
    public float endX;
    @Deprecated
    public float endY;
    private final List<PathOperation> operations = new ArrayList<PathOperation>();
    private final List<ShadowCompatOperation> shadowCompatOperations = new ArrayList<ShadowCompatOperation>();
    @Deprecated
    public float startX;
    @Deprecated
    public float startY;

    public ShapePath() {
        this.reset(0.0f, 0.0f);
    }

    public ShapePath(float f, float f2) {
        this.reset(f, f2);
    }

    private void addConnectingShadowIfNecessary(float f) {
        if (this.getCurrentShadowAngle() == f) {
            return;
        }
        float f2 = (f - this.getCurrentShadowAngle() + 360.0f) % 360.0f;
        if (f2 > 180.0f) {
            return;
        }
        PathArcOperation pathArcOperation = new PathArcOperation(this.getEndX(), this.getEndY(), this.getEndX(), this.getEndY());
        pathArcOperation.setStartAngle(this.getCurrentShadowAngle());
        pathArcOperation.setSweepAngle(f2);
        this.shadowCompatOperations.add(new ArcShadowOperation(pathArcOperation));
        this.setCurrentShadowAngle(f);
    }

    private void addShadowCompatOperation(ShadowCompatOperation shadowCompatOperation, float f, float f2) {
        this.addConnectingShadowIfNecessary(f);
        this.shadowCompatOperations.add(shadowCompatOperation);
        this.setCurrentShadowAngle(f2);
    }

    private float getCurrentShadowAngle() {
        return this.currentShadowAngle;
    }

    private float getEndShadowAngle() {
        return this.endShadowAngle;
    }

    private void setCurrentShadowAngle(float f) {
        this.currentShadowAngle = f;
    }

    private void setEndShadowAngle(float f) {
        this.endShadowAngle = f;
    }

    private void setEndX(float f) {
        this.endX = f;
    }

    private void setEndY(float f) {
        this.endY = f;
    }

    private void setStartX(float f) {
        this.startX = f;
    }

    private void setStartY(float f) {
        this.startY = f;
    }

    public void addArc(float f, float f2, float f3, float f4, float f5, float f6) {
        Object object = new PathArcOperation(f, f2, f3, f4);
        ((PathArcOperation)object).setStartAngle(f5);
        ((PathArcOperation)object).setSweepAngle(f6);
        this.operations.add((PathArcOperation)object);
        object = new ArcShadowOperation((PathArcOperation)object);
        float f7 = f5 + f6;
        boolean bl = f6 < 0.0f;
        float f8 = bl ? (f5 + 180.0f) % 360.0f : f5;
        if (bl) {
            f7 = (180.0f + f7) % 360.0f;
        }
        this.addShadowCompatOperation((ShadowCompatOperation)object, f8, f7);
        this.setEndX((f + f3) * 0.5f + (f3 - f) / 2.0f * (float)Math.cos(Math.toRadians(f5 + f6)));
        this.setEndY((f2 + f4) * 0.5f + (f4 - f2) / 2.0f * (float)Math.sin(Math.toRadians(f5 + f6)));
    }

    public void applyToPath(Matrix matrix, Path path) {
        int n = this.operations.size();
        for (int i = 0; i < n; ++i) {
            this.operations.get(i).applyToPath(matrix, path);
        }
    }

    ShadowCompatOperation createShadowCompatOperation(Matrix matrix) {
        this.addConnectingShadowIfNecessary(this.getEndShadowAngle());
        return new ShadowCompatOperation(new ArrayList<ShadowCompatOperation>(this.shadowCompatOperations), matrix){
            final /* synthetic */ List val$operations;
            final /* synthetic */ Matrix val$transform;
            {
                this.val$operations = list;
                this.val$transform = matrix;
            }

            @Override
            public void draw(Matrix object, ShadowRenderer shadowRenderer, int n, Canvas canvas) {
                object = this.val$operations.iterator();
                while (object.hasNext()) {
                    ((ShadowCompatOperation)object.next()).draw(this.val$transform, shadowRenderer, n, canvas);
                }
            }
        };
    }

    float getEndX() {
        return this.endX;
    }

    float getEndY() {
        return this.endY;
    }

    float getStartX() {
        return this.startX;
    }

    float getStartY() {
        return this.startY;
    }

    public void lineTo(float f, float f2) {
        Object object = new PathLineOperation();
        ((PathLineOperation)object).x = f;
        ((PathLineOperation)object).y = f2;
        this.operations.add((PathLineOperation)object);
        object = new LineShadowOperation((PathLineOperation)object, this.getEndX(), this.getEndY());
        this.addShadowCompatOperation((ShadowCompatOperation)object, object.getAngle() + 270.0f, object.getAngle() + 270.0f);
        this.setEndX(f);
        this.setEndY(f2);
    }

    public void quadToPoint(float f, float f2, float f3, float f4) {
        PathQuadOperation pathQuadOperation = new PathQuadOperation();
        pathQuadOperation.setControlX(f);
        pathQuadOperation.setControlY(f2);
        pathQuadOperation.setEndX(f3);
        pathQuadOperation.setEndY(f4);
        this.operations.add(pathQuadOperation);
        this.setEndX(f3);
        this.setEndY(f4);
    }

    public void reset(float f, float f2) {
        this.reset(f, f2, 270.0f, 0.0f);
    }

    public void reset(float f, float f2, float f3, float f4) {
        this.setStartX(f);
        this.setStartY(f2);
        this.setEndX(f);
        this.setEndY(f2);
        this.setCurrentShadowAngle(f3);
        this.setEndShadowAngle((f3 + f4) % 360.0f);
        this.operations.clear();
        this.shadowCompatOperations.clear();
    }

    static class ArcShadowOperation
    extends ShadowCompatOperation {
        private final PathArcOperation operation;

        public ArcShadowOperation(PathArcOperation pathArcOperation) {
            this.operation = pathArcOperation;
        }

        @Override
        public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int n, Canvas canvas) {
            float f = this.operation.getStartAngle();
            float f2 = this.operation.getSweepAngle();
            shadowRenderer.drawCornerShadow(canvas, matrix, new RectF(this.operation.getLeft(), this.operation.getTop(), this.operation.getRight(), this.operation.getBottom()), n, f, f2);
        }
    }

    static class LineShadowOperation
    extends ShadowCompatOperation {
        private final PathLineOperation operation;
        private final float startX;
        private final float startY;

        public LineShadowOperation(PathLineOperation pathLineOperation, float f, float f2) {
            this.operation = pathLineOperation;
            this.startX = f;
            this.startY = f2;
        }

        @Override
        public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int n, Canvas canvas) {
            float f = this.operation.y;
            float f2 = this.startY;
            float f3 = this.operation.x;
            float f4 = this.startX;
            RectF rectF = new RectF(0.0f, 0.0f, (float)Math.hypot(f - f2, f3 - f4), 0.0f);
            matrix = new Matrix(matrix);
            matrix.preTranslate(this.startX, this.startY);
            matrix.preRotate(this.getAngle());
            shadowRenderer.drawEdgeShadow(canvas, matrix, rectF, n);
        }

        float getAngle() {
            return (float)Math.toDegrees(Math.atan((this.operation.y - this.startY) / (this.operation.x - this.startX)));
        }
    }

    public static class PathArcOperation
    extends PathOperation {
        private static final RectF rectF = new RectF();
        @Deprecated
        public float bottom;
        @Deprecated
        public float left;
        @Deprecated
        public float right;
        @Deprecated
        public float startAngle;
        @Deprecated
        public float sweepAngle;
        @Deprecated
        public float top;

        public PathArcOperation(float f, float f2, float f3, float f4) {
            this.setLeft(f);
            this.setTop(f2);
            this.setRight(f3);
            this.setBottom(f4);
        }

        private float getBottom() {
            return this.bottom;
        }

        private float getLeft() {
            return this.left;
        }

        private float getRight() {
            return this.right;
        }

        private float getStartAngle() {
            return this.startAngle;
        }

        private float getSweepAngle() {
            return this.sweepAngle;
        }

        private float getTop() {
            return this.top;
        }

        private void setBottom(float f) {
            this.bottom = f;
        }

        private void setLeft(float f) {
            this.left = f;
        }

        private void setRight(float f) {
            this.right = f;
        }

        private void setStartAngle(float f) {
            this.startAngle = f;
        }

        private void setSweepAngle(float f) {
            this.sweepAngle = f;
        }

        private void setTop(float f) {
            this.top = f;
        }

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            rectF.set(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            path.arcTo(rectF, this.getStartAngle(), this.getSweepAngle(), false);
            path.transform(matrix);
        }
    }

    public static class PathLineOperation
    extends PathOperation {
        private float x;
        private float y;

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.x, this.y);
            path.transform(matrix);
        }
    }

    public static abstract class PathOperation {
        protected final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix var1, Path var2);
    }

    public static class PathQuadOperation
    extends PathOperation {
        @Deprecated
        public float controlX;
        @Deprecated
        public float controlY;
        @Deprecated
        public float endX;
        @Deprecated
        public float endY;

        private float getControlX() {
            return this.controlX;
        }

        private float getControlY() {
            return this.controlY;
        }

        private float getEndX() {
            return this.endX;
        }

        private float getEndY() {
            return this.endY;
        }

        private void setControlX(float f) {
            this.controlX = f;
        }

        private void setControlY(float f) {
            this.controlY = f;
        }

        private void setEndX(float f) {
            this.endX = f;
        }

        private void setEndY(float f) {
            this.endY = f;
        }

        @Override
        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.quadTo(this.getControlX(), this.getControlY(), this.getEndX(), this.getEndY());
            path.transform(matrix);
        }
    }

    static abstract class ShadowCompatOperation {
        static final Matrix IDENTITY_MATRIX = new Matrix();

        ShadowCompatOperation() {
        }

        public abstract void draw(Matrix var1, ShadowRenderer var2, int var3, Canvas var4);

        public final void draw(ShadowRenderer shadowRenderer, int n, Canvas canvas) {
            this.draw(IDENTITY_MATRIX, shadowRenderer, n, canvas);
        }
    }

}

