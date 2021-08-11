/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.graphics.RectF
 */
package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapePath;

public class ShapeAppearancePathProvider {
    private final ShapePath[] cornerPaths = new ShapePath[4];
    private final Matrix[] cornerTransforms = new Matrix[4];
    private final Matrix[] edgeTransforms = new Matrix[4];
    private final PointF pointF = new PointF();
    private final float[] scratch = new float[2];
    private final float[] scratch2 = new float[2];
    private final ShapePath shapePath = new ShapePath();

    public ShapeAppearancePathProvider() {
        for (int i = 0; i < 4; ++i) {
            this.cornerPaths[i] = new ShapePath();
            this.cornerTransforms[i] = new Matrix();
            this.edgeTransforms[i] = new Matrix();
        }
    }

    private float angleOfEdge(int n) {
        return (n + 1) * 90;
    }

    private void appendCornerPath(ShapeAppearancePathSpec shapeAppearancePathSpec, int n) {
        this.scratch[0] = this.cornerPaths[n].getStartX();
        this.scratch[1] = this.cornerPaths[n].getStartY();
        this.cornerTransforms[n].mapPoints(this.scratch);
        if (n == 0) {
            Path path = shapeAppearancePathSpec.path;
            float[] arrf = this.scratch;
            path.moveTo(arrf[0], arrf[1]);
        } else {
            Path path = shapeAppearancePathSpec.path;
            float[] arrf = this.scratch;
            path.lineTo(arrf[0], arrf[1]);
        }
        this.cornerPaths[n].applyToPath(this.cornerTransforms[n], shapeAppearancePathSpec.path);
        if (shapeAppearancePathSpec.pathListener != null) {
            shapeAppearancePathSpec.pathListener.onCornerPathCreated(this.cornerPaths[n], this.cornerTransforms[n], n);
        }
    }

    private void appendEdgePath(ShapeAppearancePathSpec shapeAppearancePathSpec, int n) {
        int n2 = (n + 1) % 4;
        this.scratch[0] = this.cornerPaths[n].getEndX();
        this.scratch[1] = this.cornerPaths[n].getEndY();
        this.cornerTransforms[n].mapPoints(this.scratch);
        this.scratch2[0] = this.cornerPaths[n2].getStartX();
        this.scratch2[1] = this.cornerPaths[n2].getStartY();
        this.cornerTransforms[n2].mapPoints(this.scratch2);
        float[] arrf = this.scratch;
        float f = arrf[0];
        float[] arrf2 = this.scratch2;
        f = Math.max((float)Math.hypot(f - arrf2[0], arrf[1] - arrf2[1]) - 0.001f, 0.0f);
        float f2 = this.getEdgeCenterForIndex(shapeAppearancePathSpec.bounds, n);
        this.shapePath.reset(0.0f, 0.0f);
        this.getEdgeTreatmentForIndex(n, shapeAppearancePathSpec.shapeAppearanceModel).getEdgePath(f, f2, shapeAppearancePathSpec.interpolation, this.shapePath);
        this.shapePath.applyToPath(this.edgeTransforms[n], shapeAppearancePathSpec.path);
        if (shapeAppearancePathSpec.pathListener != null) {
            shapeAppearancePathSpec.pathListener.onEdgePathCreated(this.shapePath, this.edgeTransforms[n], n);
        }
    }

    private void getCoordinatesOfCorner(int n, RectF rectF, PointF pointF) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    pointF.set(rectF.right, rectF.top);
                    return;
                }
                pointF.set(rectF.left, rectF.top);
                return;
            }
            pointF.set(rectF.left, rectF.bottom);
            return;
        }
        pointF.set(rectF.right, rectF.bottom);
    }

    private CornerSize getCornerSizeForIndex(int n, ShapeAppearanceModel shapeAppearanceModel) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    return shapeAppearanceModel.getTopRightCornerSize();
                }
                return shapeAppearanceModel.getTopLeftCornerSize();
            }
            return shapeAppearanceModel.getBottomLeftCornerSize();
        }
        return shapeAppearanceModel.getBottomRightCornerSize();
    }

    private CornerTreatment getCornerTreatmentForIndex(int n, ShapeAppearanceModel shapeAppearanceModel) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    return shapeAppearanceModel.getTopRightCorner();
                }
                return shapeAppearanceModel.getTopLeftCorner();
            }
            return shapeAppearanceModel.getBottomLeftCorner();
        }
        return shapeAppearanceModel.getBottomRightCorner();
    }

    private float getEdgeCenterForIndex(RectF rectF, int n) {
        this.scratch[0] = this.cornerPaths[n].endX;
        this.scratch[1] = this.cornerPaths[n].endY;
        this.cornerTransforms[n].mapPoints(this.scratch);
        if (n != 1 && n != 3) {
            return Math.abs(rectF.centerY() - this.scratch[1]);
        }
        return Math.abs(rectF.centerX() - this.scratch[0]);
    }

    private EdgeTreatment getEdgeTreatmentForIndex(int n, ShapeAppearanceModel shapeAppearanceModel) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    return shapeAppearanceModel.getRightEdge();
                }
                return shapeAppearanceModel.getTopEdge();
            }
            return shapeAppearanceModel.getLeftEdge();
        }
        return shapeAppearanceModel.getBottomEdge();
    }

    private void setCornerPathAndTransform(ShapeAppearancePathSpec shapeAppearancePathSpec, int n) {
        CornerSize cornerSize = this.getCornerSizeForIndex(n, shapeAppearancePathSpec.shapeAppearanceModel);
        this.getCornerTreatmentForIndex(n, shapeAppearancePathSpec.shapeAppearanceModel).getCornerPath(this.cornerPaths[n], 90.0f, shapeAppearancePathSpec.interpolation, shapeAppearancePathSpec.bounds, cornerSize);
        float f = this.angleOfEdge(n);
        this.cornerTransforms[n].reset();
        this.getCoordinatesOfCorner(n, shapeAppearancePathSpec.bounds, this.pointF);
        this.cornerTransforms[n].setTranslate(this.pointF.x, this.pointF.y);
        this.cornerTransforms[n].preRotate(f);
    }

    private void setEdgePathAndTransform(int n) {
        this.scratch[0] = this.cornerPaths[n].getEndX();
        this.scratch[1] = this.cornerPaths[n].getEndY();
        this.cornerTransforms[n].mapPoints(this.scratch);
        float f = this.angleOfEdge(n);
        this.edgeTransforms[n].reset();
        Matrix matrix = this.edgeTransforms[n];
        float[] arrf = this.scratch;
        matrix.setTranslate(arrf[0], arrf[1]);
        this.edgeTransforms[n].preRotate(f);
    }

    public void calculatePath(ShapeAppearanceModel shapeAppearanceModel, float f, RectF rectF, Path path) {
        this.calculatePath(shapeAppearanceModel, f, rectF, null, path);
    }

    public void calculatePath(ShapeAppearanceModel object, float f, RectF rectF, PathListener pathListener, Path path) {
        int n;
        path.rewind();
        object = new ShapeAppearancePathSpec((ShapeAppearanceModel)object, f, rectF, pathListener, path);
        for (n = 0; n < 4; ++n) {
            this.setCornerPathAndTransform((ShapeAppearancePathSpec)object, n);
            this.setEdgePathAndTransform(n);
        }
        for (n = 0; n < 4; ++n) {
            this.appendCornerPath((ShapeAppearancePathSpec)object, n);
            this.appendEdgePath((ShapeAppearancePathSpec)object, n);
        }
        path.close();
    }

    public static interface PathListener {
        public void onCornerPathCreated(ShapePath var1, Matrix var2, int var3);

        public void onEdgePathCreated(ShapePath var1, Matrix var2, int var3);
    }

    static final class ShapeAppearancePathSpec {
        public final RectF bounds;
        public final float interpolation;
        public final Path path;
        public final PathListener pathListener;
        public final ShapeAppearanceModel shapeAppearanceModel;

        ShapeAppearancePathSpec(ShapeAppearanceModel shapeAppearanceModel, float f, RectF rectF, PathListener pathListener, Path path) {
            this.pathListener = pathListener;
            this.shapeAppearanceModel = shapeAppearanceModel;
            this.interpolation = f;
            this.bounds = rectF;
            this.path = path;
        }
    }

}

