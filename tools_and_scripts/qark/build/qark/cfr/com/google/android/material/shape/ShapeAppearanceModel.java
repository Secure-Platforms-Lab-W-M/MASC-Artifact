/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.ContextThemeWrapper
 *  com.google.android.material.R
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.shape;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import com.google.android.material.R;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.RoundedCornerTreatment;

public class ShapeAppearanceModel {
    public static final CornerSize PILL = new RelativeCornerSize(0.5f);
    EdgeTreatment bottomEdge;
    CornerTreatment bottomLeftCorner;
    CornerSize bottomLeftCornerSize;
    CornerTreatment bottomRightCorner;
    CornerSize bottomRightCornerSize;
    EdgeTreatment leftEdge;
    EdgeTreatment rightEdge;
    EdgeTreatment topEdge;
    CornerTreatment topLeftCorner;
    CornerSize topLeftCornerSize;
    CornerTreatment topRightCorner;
    CornerSize topRightCornerSize;

    public ShapeAppearanceModel() {
        this.topLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        this.topRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        this.bottomRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        this.bottomLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        this.topLeftCornerSize = new AbsoluteCornerSize(0.0f);
        this.topRightCornerSize = new AbsoluteCornerSize(0.0f);
        this.bottomRightCornerSize = new AbsoluteCornerSize(0.0f);
        this.bottomLeftCornerSize = new AbsoluteCornerSize(0.0f);
        this.topEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        this.rightEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        this.bottomEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        this.leftEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
    }

    private ShapeAppearanceModel(Builder builder) {
        this.topLeftCorner = builder.topLeftCorner;
        this.topRightCorner = builder.topRightCorner;
        this.bottomRightCorner = builder.bottomRightCorner;
        this.bottomLeftCorner = builder.bottomLeftCorner;
        this.topLeftCornerSize = builder.topLeftCornerSize;
        this.topRightCornerSize = builder.topRightCornerSize;
        this.bottomRightCornerSize = builder.bottomRightCornerSize;
        this.bottomLeftCornerSize = builder.bottomLeftCornerSize;
        this.topEdge = builder.topEdge;
        this.rightEdge = builder.rightEdge;
        this.bottomEdge = builder.bottomEdge;
        this.leftEdge = builder.leftEdge;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Context context, int n, int n2) {
        return ShapeAppearanceModel.builder(context, n, n2, 0);
    }

    private static Builder builder(Context context, int n, int n2, int n3) {
        return ShapeAppearanceModel.builder(context, n, n2, new AbsoluteCornerSize(n3));
    }

    private static Builder builder(Context context, int n, int n2, CornerSize object) {
        Object object2 = context;
        int n3 = n;
        if (n2 != 0) {
            object2 = new ContextThemeWrapper(context, n);
            n3 = n2;
        }
        context = object2.obtainStyledAttributes(n3, R.styleable.ShapeAppearance);
        try {
            int n4 = context.getInt(R.styleable.ShapeAppearance_cornerFamily, 0);
            n = context.getInt(R.styleable.ShapeAppearance_cornerFamilyTopLeft, n4);
            n2 = context.getInt(R.styleable.ShapeAppearance_cornerFamilyTopRight, n4);
            n3 = context.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomRight, n4);
            n4 = context.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomLeft, n4);
            CornerSize cornerSize = ShapeAppearanceModel.getCornerSize((TypedArray)context, R.styleable.ShapeAppearance_cornerSize, (CornerSize)object);
            object = ShapeAppearanceModel.getCornerSize((TypedArray)context, R.styleable.ShapeAppearance_cornerSizeTopLeft, cornerSize);
            object2 = ShapeAppearanceModel.getCornerSize((TypedArray)context, R.styleable.ShapeAppearance_cornerSizeTopRight, cornerSize);
            CornerSize cornerSize2 = ShapeAppearanceModel.getCornerSize((TypedArray)context, R.styleable.ShapeAppearance_cornerSizeBottomRight, cornerSize);
            cornerSize = ShapeAppearanceModel.getCornerSize((TypedArray)context, R.styleable.ShapeAppearance_cornerSizeBottomLeft, cornerSize);
            object = new Builder().setTopLeftCorner(n, (CornerSize)object).setTopRightCorner(n2, (CornerSize)object2).setBottomRightCorner(n3, cornerSize2).setBottomLeftCorner(n4, cornerSize);
            return object;
        }
        finally {
            context.recycle();
        }
    }

    public static Builder builder(Context context, AttributeSet attributeSet, int n, int n2) {
        return ShapeAppearanceModel.builder(context, attributeSet, n, n2, 0);
    }

    public static Builder builder(Context context, AttributeSet attributeSet, int n, int n2, int n3) {
        return ShapeAppearanceModel.builder(context, attributeSet, n, n2, new AbsoluteCornerSize(n3));
    }

    public static Builder builder(Context context, AttributeSet attributeSet, int n, int n2, CornerSize cornerSize) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.MaterialShape, n, n2);
        n = attributeSet.getResourceId(R.styleable.MaterialShape_shapeAppearance, 0);
        n2 = attributeSet.getResourceId(R.styleable.MaterialShape_shapeAppearanceOverlay, 0);
        attributeSet.recycle();
        return ShapeAppearanceModel.builder(context, n, n2, cornerSize);
    }

    private static CornerSize getCornerSize(TypedArray typedArray, int n, CornerSize cornerSize) {
        TypedValue typedValue = typedArray.peekValue(n);
        if (typedValue == null) {
            return cornerSize;
        }
        if (typedValue.type == 5) {
            return new AbsoluteCornerSize(TypedValue.complexToDimensionPixelSize((int)typedValue.data, (DisplayMetrics)typedArray.getResources().getDisplayMetrics()));
        }
        if (typedValue.type == 6) {
            return new RelativeCornerSize(typedValue.getFraction(1.0f, 1.0f));
        }
        return cornerSize;
    }

    public EdgeTreatment getBottomEdge() {
        return this.bottomEdge;
    }

    public CornerTreatment getBottomLeftCorner() {
        return this.bottomLeftCorner;
    }

    public CornerSize getBottomLeftCornerSize() {
        return this.bottomLeftCornerSize;
    }

    public CornerTreatment getBottomRightCorner() {
        return this.bottomRightCorner;
    }

    public CornerSize getBottomRightCornerSize() {
        return this.bottomRightCornerSize;
    }

    public EdgeTreatment getLeftEdge() {
        return this.leftEdge;
    }

    public EdgeTreatment getRightEdge() {
        return this.rightEdge;
    }

    public EdgeTreatment getTopEdge() {
        return this.topEdge;
    }

    public CornerTreatment getTopLeftCorner() {
        return this.topLeftCorner;
    }

    public CornerSize getTopLeftCornerSize() {
        return this.topLeftCornerSize;
    }

    public CornerTreatment getTopRightCorner() {
        return this.topRightCorner;
    }

    public CornerSize getTopRightCornerSize() {
        return this.topRightCornerSize;
    }

    public boolean isRoundRect(RectF rectF) {
        boolean bl = this.leftEdge.getClass().equals(EdgeTreatment.class) && this.rightEdge.getClass().equals(EdgeTreatment.class) && this.topEdge.getClass().equals(EdgeTreatment.class) && this.bottomEdge.getClass().equals(EdgeTreatment.class);
        float f = this.topLeftCornerSize.getCornerSize(rectF);
        boolean bl2 = this.topRightCornerSize.getCornerSize(rectF) == f && this.bottomLeftCornerSize.getCornerSize(rectF) == f && this.bottomRightCornerSize.getCornerSize(rectF) == f;
        boolean bl3 = this.topRightCorner instanceof RoundedCornerTreatment && this.topLeftCorner instanceof RoundedCornerTreatment && this.bottomRightCorner instanceof RoundedCornerTreatment && this.bottomLeftCorner instanceof RoundedCornerTreatment;
        if (bl && bl2 && bl3) {
            return true;
        }
        return false;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public ShapeAppearanceModel withCornerSize(float f) {
        return this.toBuilder().setAllCornerSizes(f).build();
    }

    public ShapeAppearanceModel withCornerSize(CornerSize cornerSize) {
        return this.toBuilder().setAllCornerSizes(cornerSize).build();
    }

    public ShapeAppearanceModel withTransformedCornerSizes(CornerSizeUnaryOperator cornerSizeUnaryOperator) {
        return this.toBuilder().setTopLeftCornerSize(cornerSizeUnaryOperator.apply(this.getTopLeftCornerSize())).setTopRightCornerSize(cornerSizeUnaryOperator.apply(this.getTopRightCornerSize())).setBottomLeftCornerSize(cornerSizeUnaryOperator.apply(this.getBottomLeftCornerSize())).setBottomRightCornerSize(cornerSizeUnaryOperator.apply(this.getBottomRightCornerSize())).build();
    }

    public static final class Builder {
        private EdgeTreatment bottomEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        private CornerTreatment bottomLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        private CornerSize bottomLeftCornerSize = new AbsoluteCornerSize(0.0f);
        private CornerTreatment bottomRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        private CornerSize bottomRightCornerSize = new AbsoluteCornerSize(0.0f);
        private EdgeTreatment leftEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        private EdgeTreatment rightEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        private EdgeTreatment topEdge = MaterialShapeUtils.createDefaultEdgeTreatment();
        private CornerTreatment topLeftCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        private CornerSize topLeftCornerSize = new AbsoluteCornerSize(0.0f);
        private CornerTreatment topRightCorner = MaterialShapeUtils.createDefaultCornerTreatment();
        private CornerSize topRightCornerSize = new AbsoluteCornerSize(0.0f);

        public Builder() {
        }

        public Builder(ShapeAppearanceModel shapeAppearanceModel) {
            this.topLeftCorner = shapeAppearanceModel.topLeftCorner;
            this.topRightCorner = shapeAppearanceModel.topRightCorner;
            this.bottomRightCorner = shapeAppearanceModel.bottomRightCorner;
            this.bottomLeftCorner = shapeAppearanceModel.bottomLeftCorner;
            this.topLeftCornerSize = shapeAppearanceModel.topLeftCornerSize;
            this.topRightCornerSize = shapeAppearanceModel.topRightCornerSize;
            this.bottomRightCornerSize = shapeAppearanceModel.bottomRightCornerSize;
            this.bottomLeftCornerSize = shapeAppearanceModel.bottomLeftCornerSize;
            this.topEdge = shapeAppearanceModel.topEdge;
            this.rightEdge = shapeAppearanceModel.rightEdge;
            this.bottomEdge = shapeAppearanceModel.bottomEdge;
            this.leftEdge = shapeAppearanceModel.leftEdge;
        }

        private static float compatCornerTreatmentSize(CornerTreatment cornerTreatment) {
            if (cornerTreatment instanceof RoundedCornerTreatment) {
                return ((RoundedCornerTreatment)cornerTreatment).radius;
            }
            if (cornerTreatment instanceof CutCornerTreatment) {
                return ((CutCornerTreatment)cornerTreatment).size;
            }
            return -1.0f;
        }

        public ShapeAppearanceModel build() {
            return new ShapeAppearanceModel(this);
        }

        public Builder setAllCornerSizes(float f) {
            return this.setTopLeftCornerSize(f).setTopRightCornerSize(f).setBottomRightCornerSize(f).setBottomLeftCornerSize(f);
        }

        public Builder setAllCornerSizes(CornerSize cornerSize) {
            return this.setTopLeftCornerSize(cornerSize).setTopRightCornerSize(cornerSize).setBottomRightCornerSize(cornerSize).setBottomLeftCornerSize(cornerSize);
        }

        public Builder setAllCorners(int n, float f) {
            return this.setAllCorners(MaterialShapeUtils.createCornerTreatment(n)).setAllCornerSizes(f);
        }

        public Builder setAllCorners(CornerTreatment cornerTreatment) {
            return this.setTopLeftCorner(cornerTreatment).setTopRightCorner(cornerTreatment).setBottomRightCorner(cornerTreatment).setBottomLeftCorner(cornerTreatment);
        }

        public Builder setAllEdges(EdgeTreatment edgeTreatment) {
            return this.setLeftEdge(edgeTreatment).setTopEdge(edgeTreatment).setRightEdge(edgeTreatment).setBottomEdge(edgeTreatment);
        }

        public Builder setBottomEdge(EdgeTreatment edgeTreatment) {
            this.bottomEdge = edgeTreatment;
            return this;
        }

        public Builder setBottomLeftCorner(int n, float f) {
            return this.setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(n)).setBottomLeftCornerSize(f);
        }

        public Builder setBottomLeftCorner(int n, CornerSize cornerSize) {
            return this.setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(n)).setBottomLeftCornerSize(cornerSize);
        }

        public Builder setBottomLeftCorner(CornerTreatment cornerTreatment) {
            this.bottomLeftCorner = cornerTreatment;
            float f = Builder.compatCornerTreatmentSize(cornerTreatment);
            if (f != -1.0f) {
                this.setBottomLeftCornerSize(f);
            }
            return this;
        }

        public Builder setBottomLeftCornerSize(float f) {
            this.bottomLeftCornerSize = new AbsoluteCornerSize(f);
            return this;
        }

        public Builder setBottomLeftCornerSize(CornerSize cornerSize) {
            this.bottomLeftCornerSize = cornerSize;
            return this;
        }

        public Builder setBottomRightCorner(int n, float f) {
            return this.setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(n)).setBottomRightCornerSize(f);
        }

        public Builder setBottomRightCorner(int n, CornerSize cornerSize) {
            return this.setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(n)).setBottomRightCornerSize(cornerSize);
        }

        public Builder setBottomRightCorner(CornerTreatment cornerTreatment) {
            this.bottomRightCorner = cornerTreatment;
            float f = Builder.compatCornerTreatmentSize(cornerTreatment);
            if (f != -1.0f) {
                this.setBottomRightCornerSize(f);
            }
            return this;
        }

        public Builder setBottomRightCornerSize(float f) {
            this.bottomRightCornerSize = new AbsoluteCornerSize(f);
            return this;
        }

        public Builder setBottomRightCornerSize(CornerSize cornerSize) {
            this.bottomRightCornerSize = cornerSize;
            return this;
        }

        public Builder setLeftEdge(EdgeTreatment edgeTreatment) {
            this.leftEdge = edgeTreatment;
            return this;
        }

        public Builder setRightEdge(EdgeTreatment edgeTreatment) {
            this.rightEdge = edgeTreatment;
            return this;
        }

        public Builder setTopEdge(EdgeTreatment edgeTreatment) {
            this.topEdge = edgeTreatment;
            return this;
        }

        public Builder setTopLeftCorner(int n, float f) {
            return this.setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(n)).setTopLeftCornerSize(f);
        }

        public Builder setTopLeftCorner(int n, CornerSize cornerSize) {
            return this.setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(n)).setTopLeftCornerSize(cornerSize);
        }

        public Builder setTopLeftCorner(CornerTreatment cornerTreatment) {
            this.topLeftCorner = cornerTreatment;
            float f = Builder.compatCornerTreatmentSize(cornerTreatment);
            if (f != -1.0f) {
                this.setTopLeftCornerSize(f);
            }
            return this;
        }

        public Builder setTopLeftCornerSize(float f) {
            this.topLeftCornerSize = new AbsoluteCornerSize(f);
            return this;
        }

        public Builder setTopLeftCornerSize(CornerSize cornerSize) {
            this.topLeftCornerSize = cornerSize;
            return this;
        }

        public Builder setTopRightCorner(int n, float f) {
            return this.setTopRightCorner(MaterialShapeUtils.createCornerTreatment(n)).setTopRightCornerSize(f);
        }

        public Builder setTopRightCorner(int n, CornerSize cornerSize) {
            return this.setTopRightCorner(MaterialShapeUtils.createCornerTreatment(n)).setTopRightCornerSize(cornerSize);
        }

        public Builder setTopRightCorner(CornerTreatment cornerTreatment) {
            this.topRightCorner = cornerTreatment;
            float f = Builder.compatCornerTreatmentSize(cornerTreatment);
            if (f != -1.0f) {
                this.setTopRightCornerSize(f);
            }
            return this;
        }

        public Builder setTopRightCornerSize(float f) {
            this.topRightCornerSize = new AbsoluteCornerSize(f);
            return this;
        }

        public Builder setTopRightCornerSize(CornerSize cornerSize) {
            this.topRightCornerSize = cornerSize;
            return this;
        }
    }

    public static interface CornerSizeUnaryOperator {
        public CornerSize apply(CornerSize var1);
    }

}

