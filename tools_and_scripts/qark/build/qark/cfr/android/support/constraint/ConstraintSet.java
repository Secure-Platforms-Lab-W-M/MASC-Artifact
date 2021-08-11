/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 *  android.util.Xml
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.constraint;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.constraint.R;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintSet {
    private static final int ALPHA = 43;
    public static final int BASELINE = 5;
    private static final int BASELINE_TO_BASELINE = 1;
    public static final int BOTTOM = 4;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    private static final boolean DEBUG = false;
    private static final int DIMENSION_RATIO = 5;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    private static final int ELEVATION = 44;
    public static final int END = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    public static final int GONE = 8;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    private static final int HEIGHT_DEFAULT = 55;
    private static final int HEIGHT_MAX = 57;
    private static final int HEIGHT_MIN = 59;
    public static final int HORIZONTAL = 0;
    private static final int HORIZONTAL_BIAS = 20;
    public static final int HORIZONTAL_GUIDELINE = 0;
    private static final int HORIZONTAL_STYLE = 41;
    private static final int HORIZONTAL_WEIGHT = 39;
    public static final int INVISIBLE = 4;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    public static final int LEFT = 1;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    private static final int ORIENTATION = 27;
    public static final int PARENT_ID = 0;
    public static final int RIGHT = 2;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    private static final int ROTATION_X = 45;
    private static final int ROTATION_Y = 46;
    private static final int SCALE_X = 47;
    private static final int SCALE_Y = 48;
    public static final int START = 6;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final String TAG = "ConstraintSet";
    public static final int TOP = 3;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int TRANSFORM_PIVOT_X = 49;
    private static final int TRANSFORM_PIVOT_Y = 50;
    private static final int TRANSLATION_X = 51;
    private static final int TRANSLATION_Y = 52;
    private static final int TRANSLATION_Z = 53;
    public static final int UNSET = -1;
    private static final int UNUSED = 60;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_BIAS = 37;
    public static final int VERTICAL_GUIDELINE = 1;
    private static final int VERTICAL_STYLE = 42;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int VIEW_ID = 38;
    private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
    public static final int VISIBLE = 0;
    private static final int WIDTH_DEFAULT = 54;
    private static final int WIDTH_MAX = 56;
    private static final int WIDTH_MIN = 58;
    public static final int WRAP_CONTENT = -2;
    private static SparseIntArray mapToConstant = new SparseIntArray();
    private HashMap<Integer, Constraint> mConstraints = new HashMap();

    static {
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
        mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
        mapToConstant.append(R.styleable.ConstraintSet_android_orientation, 27);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginTop, 16);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginRight, 14);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginStart, 15);
        mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_chainStyle, 41);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_chainStyle, 42);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_creator, 60);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_creator, 60);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_creator, 60);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_creator, 60);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_creator, 60);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginLeft, 24);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginRight, 28);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginStart, 31);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginEnd, 8);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginTop, 34);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginBottom, 2);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_width, 23);
        mapToConstant.append(R.styleable.ConstraintSet_android_layout_height, 21);
        mapToConstant.append(R.styleable.ConstraintSet_android_visibility, 22);
        mapToConstant.append(R.styleable.ConstraintSet_android_alpha, 43);
        mapToConstant.append(R.styleable.ConstraintSet_android_elevation, 44);
        mapToConstant.append(R.styleable.ConstraintSet_android_rotationX, 45);
        mapToConstant.append(R.styleable.ConstraintSet_android_rotationY, 46);
        mapToConstant.append(R.styleable.ConstraintSet_android_scaleX, 47);
        mapToConstant.append(R.styleable.ConstraintSet_android_scaleY, 48);
        mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotX, 49);
        mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotY, 50);
        mapToConstant.append(R.styleable.ConstraintSet_android_translationX, 51);
        mapToConstant.append(R.styleable.ConstraintSet_android_translationY, 52);
        mapToConstant.append(R.styleable.ConstraintSet_android_translationZ, 53);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_default, 54);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_default, 55);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_max, 56);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_max, 57);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_min, 58);
        mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_min, 59);
        mapToConstant.append(R.styleable.ConstraintSet_android_id, 38);
    }

    private void createHorizontalChain(int n, int n2, int n3, int n4, int[] arrn, float[] arrf, int n5, int n6, int n7) {
        if (arrn.length >= 2) {
            if (arrf != null && arrf.length != arrn.length) {
                throw new IllegalArgumentException("must have 2 or more widgets in a chain");
            }
            if (arrf != null) {
                this.get((int)arrn[0]).verticalWeight = arrf[0];
            }
            this.get((int)arrn[0]).horizontalChainStyle = n5;
            this.connect(arrn[0], n6, n, n2, -1);
            for (n = 1; n < arrn.length; ++n) {
                n2 = arrn[n];
                this.connect(arrn[n], n6, arrn[n - 1], n7, -1);
                this.connect(arrn[n - 1], n7, arrn[n], n6, -1);
                if (arrf == null) continue;
                this.get((int)arrn[n]).horizontalWeight = arrf[n];
            }
            this.connect(arrn[arrn.length - 1], n7, n3, n4, -1);
            return;
        }
        throw new IllegalArgumentException("must have 2 or more widgets in a chain");
    }

    private Constraint fillFromAttributeList(Context context, AttributeSet attributeSet) {
        Constraint constraint = new Constraint();
        context = context.obtainStyledAttributes(attributeSet, R.styleable.ConstraintSet);
        this.populateConstraint(constraint, (TypedArray)context);
        context.recycle();
        return constraint;
    }

    private Constraint get(int n) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        return this.mConstraints.get(n);
    }

    private static int lookupID(TypedArray typedArray, int n, int n2) {
        if ((n2 = typedArray.getResourceId(n, n2)) == -1) {
            return typedArray.getInt(n, -1);
        }
        return n2;
    }

    private void populateConstraint(Constraint constraint, TypedArray typedArray) {
        int n = typedArray.getIndexCount();
        block55 : for (int i = 0; i < n; ++i) {
            StringBuilder stringBuilder;
            int n2 = typedArray.getIndex(i);
            int n3 = mapToConstant.get(n2);
            if (n3 != 60) {
                switch (n3) {
                    default: {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown attribute 0x");
                        stringBuilder.append(Integer.toHexString(n2));
                        stringBuilder.append("   ");
                        stringBuilder.append(mapToConstant.get(n2));
                        Log.w((String)"ConstraintSet", (String)stringBuilder.toString());
                        continue block55;
                    }
                    case 53: {
                        constraint.translationZ = typedArray.getFloat(n2, constraint.translationZ);
                        continue block55;
                    }
                    case 52: {
                        constraint.translationY = typedArray.getFloat(n2, constraint.translationY);
                        continue block55;
                    }
                    case 51: {
                        constraint.translationX = typedArray.getFloat(n2, constraint.translationX);
                        continue block55;
                    }
                    case 50: {
                        constraint.transformPivotY = typedArray.getFloat(n2, constraint.transformPivotY);
                        continue block55;
                    }
                    case 49: {
                        constraint.transformPivotX = typedArray.getFloat(n2, constraint.transformPivotX);
                        continue block55;
                    }
                    case 48: {
                        constraint.scaleY = typedArray.getFloat(n2, constraint.scaleY);
                        continue block55;
                    }
                    case 47: {
                        constraint.scaleX = typedArray.getFloat(n2, constraint.scaleX);
                        continue block55;
                    }
                    case 46: {
                        constraint.rotationY = typedArray.getFloat(n2, constraint.rotationY);
                        continue block55;
                    }
                    case 45: {
                        constraint.rotationX = typedArray.getFloat(n2, constraint.rotationX);
                        continue block55;
                    }
                    case 44: {
                        constraint.applyElevation = true;
                        constraint.elevation = typedArray.getFloat(n2, constraint.elevation);
                        continue block55;
                    }
                    case 43: {
                        constraint.alpha = typedArray.getFloat(n2, constraint.alpha);
                        continue block55;
                    }
                    case 42: {
                        constraint.verticalChainStyle = typedArray.getInt(n2, constraint.verticalChainStyle);
                        continue block55;
                    }
                    case 41: {
                        constraint.horizontalChainStyle = typedArray.getInt(n2, constraint.horizontalChainStyle);
                        continue block55;
                    }
                    case 40: {
                        constraint.verticalWeight = typedArray.getFloat(n2, constraint.verticalWeight);
                        continue block55;
                    }
                    case 39: {
                        constraint.horizontalWeight = typedArray.getFloat(n2, constraint.horizontalWeight);
                        continue block55;
                    }
                    case 38: {
                        constraint.mViewId = typedArray.getResourceId(n2, constraint.mViewId);
                        continue block55;
                    }
                    case 37: {
                        constraint.verticalBias = typedArray.getFloat(n2, constraint.verticalBias);
                        continue block55;
                    }
                    case 36: {
                        constraint.topToTop = ConstraintSet.lookupID(typedArray, n2, constraint.topToTop);
                        continue block55;
                    }
                    case 35: {
                        constraint.topToBottom = ConstraintSet.lookupID(typedArray, n2, constraint.topToBottom);
                        continue block55;
                    }
                    case 34: {
                        constraint.topMargin = typedArray.getDimensionPixelSize(n2, constraint.topMargin);
                        continue block55;
                    }
                    case 33: {
                        constraint.startToStart = ConstraintSet.lookupID(typedArray, n2, constraint.startToStart);
                        continue block55;
                    }
                    case 32: {
                        constraint.startToEnd = ConstraintSet.lookupID(typedArray, n2, constraint.startToEnd);
                        continue block55;
                    }
                    case 31: {
                        constraint.startMargin = typedArray.getDimensionPixelSize(n2, constraint.startMargin);
                        continue block55;
                    }
                    case 30: {
                        constraint.rightToRight = ConstraintSet.lookupID(typedArray, n2, constraint.rightToRight);
                        continue block55;
                    }
                    case 29: {
                        constraint.rightToLeft = ConstraintSet.lookupID(typedArray, n2, constraint.rightToLeft);
                        continue block55;
                    }
                    case 28: {
                        constraint.rightMargin = typedArray.getDimensionPixelSize(n2, constraint.rightMargin);
                        continue block55;
                    }
                    case 27: {
                        constraint.orientation = typedArray.getInt(n2, constraint.orientation);
                        continue block55;
                    }
                    case 26: {
                        constraint.leftToRight = ConstraintSet.lookupID(typedArray, n2, constraint.leftToRight);
                        continue block55;
                    }
                    case 25: {
                        constraint.leftToLeft = ConstraintSet.lookupID(typedArray, n2, constraint.leftToLeft);
                        continue block55;
                    }
                    case 24: {
                        constraint.leftMargin = typedArray.getDimensionPixelSize(n2, constraint.leftMargin);
                        continue block55;
                    }
                    case 23: {
                        constraint.mWidth = typedArray.getLayoutDimension(n2, constraint.mWidth);
                        continue block55;
                    }
                    case 22: {
                        constraint.visibility = typedArray.getInt(n2, constraint.visibility);
                        constraint.visibility = VISIBILITY_FLAGS[constraint.visibility];
                        continue block55;
                    }
                    case 21: {
                        constraint.mHeight = typedArray.getLayoutDimension(n2, constraint.mHeight);
                        continue block55;
                    }
                    case 20: {
                        constraint.horizontalBias = typedArray.getFloat(n2, constraint.horizontalBias);
                        continue block55;
                    }
                    case 19: {
                        constraint.guidePercent = typedArray.getFloat(n2, constraint.guidePercent);
                        continue block55;
                    }
                    case 18: {
                        constraint.guideEnd = typedArray.getDimensionPixelOffset(n2, constraint.guideEnd);
                        continue block55;
                    }
                    case 17: {
                        constraint.guideBegin = typedArray.getDimensionPixelOffset(n2, constraint.guideBegin);
                        continue block55;
                    }
                    case 16: {
                        constraint.goneTopMargin = typedArray.getDimensionPixelSize(n2, constraint.goneTopMargin);
                        continue block55;
                    }
                    case 15: {
                        constraint.goneStartMargin = typedArray.getDimensionPixelSize(n2, constraint.goneStartMargin);
                        continue block55;
                    }
                    case 14: {
                        constraint.goneRightMargin = typedArray.getDimensionPixelSize(n2, constraint.goneRightMargin);
                        continue block55;
                    }
                    case 13: {
                        constraint.goneLeftMargin = typedArray.getDimensionPixelSize(n2, constraint.goneLeftMargin);
                        continue block55;
                    }
                    case 12: {
                        constraint.goneEndMargin = typedArray.getDimensionPixelSize(n2, constraint.goneEndMargin);
                        continue block55;
                    }
                    case 11: {
                        constraint.goneBottomMargin = typedArray.getDimensionPixelSize(n2, constraint.goneBottomMargin);
                        continue block55;
                    }
                    case 10: {
                        constraint.endToStart = ConstraintSet.lookupID(typedArray, n2, constraint.endToStart);
                        continue block55;
                    }
                    case 9: {
                        constraint.bottomToTop = ConstraintSet.lookupID(typedArray, n2, constraint.endToEnd);
                        continue block55;
                    }
                    case 8: {
                        constraint.endMargin = typedArray.getDimensionPixelSize(n2, constraint.endMargin);
                        continue block55;
                    }
                    case 7: {
                        constraint.editorAbsoluteY = typedArray.getDimensionPixelOffset(n2, constraint.editorAbsoluteY);
                        continue block55;
                    }
                    case 6: {
                        constraint.editorAbsoluteX = typedArray.getDimensionPixelOffset(n2, constraint.editorAbsoluteX);
                        continue block55;
                    }
                    case 5: {
                        constraint.dimensionRatio = typedArray.getString(n2);
                        continue block55;
                    }
                    case 4: {
                        constraint.bottomToTop = ConstraintSet.lookupID(typedArray, n2, constraint.bottomToTop);
                        continue block55;
                    }
                    case 3: {
                        constraint.bottomToBottom = ConstraintSet.lookupID(typedArray, n2, constraint.bottomToBottom);
                        continue block55;
                    }
                    case 2: {
                        constraint.bottomMargin = typedArray.getDimensionPixelSize(n2, constraint.bottomMargin);
                        continue block55;
                    }
                    case 1: 
                }
                constraint.baselineToBaseline = ConstraintSet.lookupID(typedArray, n2, constraint.baselineToBaseline);
                continue;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("unused attribute 0x");
            stringBuilder.append(Integer.toHexString(n2));
            stringBuilder.append("   ");
            stringBuilder.append(mapToConstant.get(n2));
            Log.w((String)"ConstraintSet", (String)stringBuilder.toString());
        }
    }

    private String sideToString(int n) {
        switch (n) {
            default: {
                return "undefined";
            }
            case 7: {
                return "end";
            }
            case 6: {
                return "start";
            }
            case 5: {
                return "baseline";
            }
            case 4: {
                return "bottom";
            }
            case 3: {
                return "top";
            }
            case 2: {
                return "right";
            }
            case 1: 
        }
        return "left";
    }

    public void addToHorizontalChain(int n, int n2, int n3) {
        int n4 = n2 == 0 ? 1 : 2;
        this.connect(n, 1, n2, n4, 0);
        n4 = n3 == 0 ? 2 : 1;
        this.connect(n, 2, n3, n4, 0);
        if (n2 != 0) {
            this.connect(n2, 2, n, 1, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 1, n, 2, 0);
            return;
        }
    }

    public void addToHorizontalChainRTL(int n, int n2, int n3) {
        int n4 = n2 == 0 ? 6 : 7;
        this.connect(n, 6, n2, n4, 0);
        n4 = n3 == 0 ? 7 : 6;
        this.connect(n, 7, n3, n4, 0);
        if (n2 != 0) {
            this.connect(n2, 7, n, 6, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 6, n, 7, 0);
            return;
        }
    }

    public void addToVerticalChain(int n, int n2, int n3) {
        int n4 = n2 == 0 ? 3 : 4;
        this.connect(n, 3, n2, n4, 0);
        n4 = n3 == 0 ? 4 : 3;
        this.connect(n, 4, n3, n4, 0);
        if (n2 != 0) {
            this.connect(n2, 4, n, 3, 0);
        }
        if (n2 != 0) {
            this.connect(n3, 3, n, 4, 0);
            return;
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        this.applyToInternal(constraintLayout);
        constraintLayout.setConstraintSet(null);
    }

    void applyToInternal(ConstraintLayout constraintLayout) {
        Object object;
        Object object2;
        Object object3;
        int n = constraintLayout.getChildCount();
        Object object4 = new HashSet<Integer>(this.mConstraints.keySet());
        for (int i = 0; i < n; ++i) {
            object2 = constraintLayout.getChildAt(i);
            int n2 = object2.getId();
            if (!this.mConstraints.containsKey(n2)) continue;
            object4.remove(n2);
            object = this.mConstraints.get(n2);
            object3 = (ConstraintLayout.LayoutParams)object2.getLayoutParams();
            object.applyTo((ConstraintLayout.LayoutParams)((Object)object3));
            object2.setLayoutParams((ViewGroup.LayoutParams)object3);
            object2.setVisibility(object.visibility);
            if (Build.VERSION.SDK_INT < 17) continue;
            object2.setAlpha(object.alpha);
            object2.setRotationX(object.rotationX);
            object2.setRotationY(object.rotationY);
            object2.setScaleX(object.scaleX);
            object2.setScaleY(object.scaleY);
            object2.setPivotX(object.transformPivotX);
            object2.setPivotY(object.transformPivotY);
            object2.setTranslationX(object.translationX);
            object2.setTranslationY(object.translationY);
            if (Build.VERSION.SDK_INT < 21) continue;
            object2.setTranslationZ(object.translationZ);
            if (!object.applyElevation) continue;
            object2.setElevation(object.elevation);
        }
        object4 = object4.iterator();
        while (object4.hasNext()) {
            object3 = (Integer)object4.next();
            object2 = this.mConstraints.get(object3);
            if (!object2.mIsGuideline) continue;
            object = new Guideline(constraintLayout.getContext());
            object.setId(object3.intValue());
            object3 = constraintLayout.generateDefaultLayoutParams();
            object2.applyTo((ConstraintLayout.LayoutParams)((Object)object3));
            constraintLayout.addView((View)object, (ViewGroup.LayoutParams)object3);
        }
    }

    public void center(int n, int n2, int n3, int n4, int n5, int n6, int n7, float f) {
        if (n4 >= 0) {
            if (n7 >= 0) {
                if (f > 0.0f && f <= 1.0f) {
                    if (n3 != 1 && n3 != 2) {
                        if (n3 != 6 && n3 != 7) {
                            this.connect(n, 3, n2, n3, n4);
                            this.connect(n, 4, n5, n6, n7);
                            this.mConstraints.get((Object)Integer.valueOf((int)n)).verticalBias = f;
                            return;
                        }
                        this.connect(n, 6, n2, n3, n4);
                        this.connect(n, 7, n5, n6, n7);
                        this.mConstraints.get((Object)Integer.valueOf((int)n)).horizontalBias = f;
                        return;
                    }
                    this.connect(n, 1, n2, n3, n4);
                    this.connect(n, 2, n5, n6, n7);
                    this.mConstraints.get((Object)Integer.valueOf((int)n)).horizontalBias = f;
                    return;
                }
                throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
            }
            throw new IllegalArgumentException("margin must be > 0");
        }
        throw new IllegalArgumentException("margin must be > 0");
    }

    public void centerHorizontally(int n, int n2) {
        if (n2 == 0) {
            this.center(n, 0, 1, 0, 0, 2, 0, 0.5f);
            return;
        }
        this.center(n, n2, 2, 0, n2, 1, 0, 0.5f);
    }

    public void centerHorizontally(int n, int n2, int n3, int n4, int n5, int n6, int n7, float f) {
        this.connect(n, 1, n2, n3, n4);
        this.connect(n, 2, n5, n6, n7);
        this.mConstraints.get((Object)Integer.valueOf((int)n)).horizontalBias = f;
    }

    public void centerHorizontallyRtl(int n, int n2) {
        if (n2 == 0) {
            this.center(n, 0, 6, 0, 0, 7, 0, 0.5f);
            return;
        }
        this.center(n, n2, 7, 0, n2, 6, 0, 0.5f);
    }

    public void centerHorizontallyRtl(int n, int n2, int n3, int n4, int n5, int n6, int n7, float f) {
        this.connect(n, 6, n2, n3, n4);
        this.connect(n, 7, n5, n6, n7);
        this.mConstraints.get((Object)Integer.valueOf((int)n)).horizontalBias = f;
    }

    public void centerVertically(int n, int n2) {
        if (n2 == 0) {
            this.center(n, 0, 3, 0, 0, 4, 0, 0.5f);
            return;
        }
        this.center(n, n2, 4, 0, n2, 3, 0, 0.5f);
    }

    public void centerVertically(int n, int n2, int n3, int n4, int n5, int n6, int n7, float f) {
        this.connect(n, 3, n2, n3, n4);
        this.connect(n, 4, n5, n6, n7);
        this.mConstraints.get((Object)Integer.valueOf((int)n)).verticalBias = f;
    }

    public void clear(int n) {
        this.mConstraints.remove(n);
    }

    public void clear(int n, int n2) {
        if (this.mConstraints.containsKey(n)) {
            Constraint constraint = this.mConstraints.get(n);
            switch (n2) {
                default: {
                    throw new IllegalArgumentException("unknown constraint");
                }
                case 7: {
                    constraint.endToStart = -1;
                    constraint.endToEnd = -1;
                    constraint.endMargin = -1;
                    constraint.goneEndMargin = -1;
                    return;
                }
                case 6: {
                    constraint.startToEnd = -1;
                    constraint.startToStart = -1;
                    constraint.startMargin = -1;
                    constraint.goneStartMargin = -1;
                    return;
                }
                case 5: {
                    constraint.baselineToBaseline = -1;
                    return;
                }
                case 4: {
                    constraint.bottomToTop = -1;
                    constraint.bottomToBottom = -1;
                    constraint.bottomMargin = -1;
                    constraint.goneBottomMargin = -1;
                    return;
                }
                case 3: {
                    constraint.topToBottom = -1;
                    constraint.topToTop = -1;
                    constraint.topMargin = -1;
                    constraint.goneTopMargin = -1;
                    return;
                }
                case 2: {
                    constraint.rightToRight = -1;
                    constraint.rightToLeft = -1;
                    constraint.rightMargin = -1;
                    constraint.goneRightMargin = -1;
                    return;
                }
                case 1: 
            }
            constraint.leftToRight = -1;
            constraint.leftToLeft = -1;
            constraint.leftMargin = -1;
            constraint.goneLeftMargin = -1;
            return;
        }
    }

    public void clone(Context context, int n) {
        this.clone((ConstraintLayout)LayoutInflater.from((Context)context).inflate(n, null));
    }

    public void clone(ConstraintLayout constraintLayout) {
        int n = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < n; ++i) {
            View view = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            int n2 = view.getId();
            if (!this.mConstraints.containsKey(n2)) {
                this.mConstraints.put(n2, new Constraint());
            }
            Constraint constraint = this.mConstraints.get(n2);
            constraint.fillFrom(n2, layoutParams);
            constraint.visibility = view.getVisibility();
            if (Build.VERSION.SDK_INT < 17) continue;
            constraint.alpha = view.getAlpha();
            constraint.rotationX = view.getRotationX();
            constraint.rotationY = view.getRotationY();
            constraint.scaleX = view.getScaleX();
            constraint.scaleY = view.getScaleY();
            constraint.transformPivotX = view.getPivotX();
            constraint.transformPivotY = view.getPivotY();
            constraint.translationX = view.getTranslationX();
            constraint.translationY = view.getTranslationY();
            if (Build.VERSION.SDK_INT < 21) continue;
            constraint.translationZ = view.getTranslationZ();
            if (!constraint.applyElevation) continue;
            constraint.elevation = view.getElevation();
        }
    }

    public void clone(ConstraintSet constraintSet) {
        this.mConstraints.clear();
        for (Integer n : constraintSet.mConstraints.keySet()) {
            this.mConstraints.put(n, constraintSet.mConstraints.get(n).clone());
        }
    }

    public void connect(int n, int n2, int n3, int n4) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        Object object = this.mConstraints.get(n);
        switch (n2) {
            default: {
                object = new StringBuilder();
                object.append(this.sideToString(n2));
                object.append(" to ");
                object.append(this.sideToString(n4));
                object.append(" unknown");
                throw new IllegalArgumentException(object.toString());
            }
            case 7: {
                if (n4 == 7) {
                    object.endToEnd = n3;
                    object.endToStart = -1;
                    return;
                }
                if (n4 == 6) {
                    object.endToStart = n3;
                    object.endToEnd = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 6: {
                if (n4 == 6) {
                    object.startToStart = n3;
                    object.startToEnd = -1;
                    return;
                }
                if (n4 == 7) {
                    object.startToEnd = n3;
                    object.startToStart = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 5: {
                if (n4 == 5) {
                    object.baselineToBaseline = n3;
                    object.bottomToBottom = -1;
                    object.bottomToTop = -1;
                    object.topToTop = -1;
                    object.topToBottom = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 4: {
                if (n4 == 4) {
                    object.bottomToBottom = n3;
                    object.bottomToTop = -1;
                    object.baselineToBaseline = -1;
                    return;
                }
                if (n4 == 3) {
                    object.bottomToTop = n3;
                    object.bottomToBottom = -1;
                    object.baselineToBaseline = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 3: {
                if (n4 == 3) {
                    object.topToTop = n3;
                    object.topToBottom = -1;
                    object.baselineToBaseline = -1;
                    return;
                }
                if (n4 == 4) {
                    object.topToBottom = n3;
                    object.topToTop = -1;
                    object.baselineToBaseline = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 2: {
                if (n4 == 1) {
                    object.rightToLeft = n3;
                    object.rightToRight = -1;
                    return;
                }
                if (n4 == 2) {
                    object.rightToRight = n3;
                    object.rightToLeft = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 1: 
        }
        if (n4 == 1) {
            object.leftToLeft = n3;
            object.leftToRight = -1;
            return;
        }
        if (n4 == 2) {
            object.leftToRight = n3;
            object.leftToLeft = -1;
            return;
        }
        object = new StringBuilder();
        object.append("left to ");
        object.append(this.sideToString(n4));
        object.append(" undefined");
        throw new IllegalArgumentException(object.toString());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void connect(int n, int n2, int n3, int n4, int n5) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        Object object = this.mConstraints.get(n);
        switch (n2) {
            default: {
                object = new StringBuilder();
                object.append(this.sideToString(n2));
                object.append(" to ");
                object.append(this.sideToString(n4));
                object.append(" unknown");
                throw new IllegalArgumentException(object.toString());
            }
            case 7: {
                if (n4 == 7) {
                    object.endToEnd = n3;
                    object.endToStart = -1;
                } else {
                    if (n4 != 6) {
                        object = new StringBuilder();
                        object.append("right to ");
                        object.append(this.sideToString(n4));
                        object.append(" undefined");
                        throw new IllegalArgumentException(object.toString());
                    }
                    object.endToStart = n3;
                    object.endToEnd = -1;
                }
                object.endMargin = n5;
                return;
            }
            case 6: {
                if (n4 == 6) {
                    object.startToStart = n3;
                    object.startToEnd = -1;
                } else {
                    if (n4 != 7) {
                        object = new StringBuilder();
                        object.append("right to ");
                        object.append(this.sideToString(n4));
                        object.append(" undefined");
                        throw new IllegalArgumentException(object.toString());
                    }
                    object.startToEnd = n3;
                    object.startToStart = -1;
                }
                object.startMargin = n5;
                return;
            }
            case 5: {
                if (n4 == 5) {
                    object.baselineToBaseline = n3;
                    object.bottomToBottom = -1;
                    object.bottomToTop = -1;
                    object.topToTop = -1;
                    object.topToBottom = -1;
                    return;
                }
                object = new StringBuilder();
                object.append("right to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            case 4: {
                if (n4 == 4) {
                    object.bottomToBottom = n3;
                    object.bottomToTop = -1;
                    object.baselineToBaseline = -1;
                } else {
                    if (n4 != 3) {
                        object = new StringBuilder();
                        object.append("right to ");
                        object.append(this.sideToString(n4));
                        object.append(" undefined");
                        throw new IllegalArgumentException(object.toString());
                    }
                    object.bottomToTop = n3;
                    object.bottomToBottom = -1;
                    object.baselineToBaseline = -1;
                }
                object.bottomMargin = n5;
                return;
            }
            case 3: {
                if (n4 == 3) {
                    object.topToTop = n3;
                    object.topToBottom = -1;
                    object.baselineToBaseline = -1;
                } else {
                    if (n4 != 4) {
                        object = new StringBuilder();
                        object.append("right to ");
                        object.append(this.sideToString(n4));
                        object.append(" undefined");
                        throw new IllegalArgumentException(object.toString());
                    }
                    object.topToBottom = n3;
                    object.topToTop = -1;
                    object.baselineToBaseline = -1;
                }
                object.topMargin = n5;
                return;
            }
            case 2: {
                if (n4 == 1) {
                    object.rightToLeft = n3;
                    object.rightToRight = -1;
                } else {
                    if (n4 != 2) {
                        object = new StringBuilder();
                        object.append("right to ");
                        object.append(this.sideToString(n4));
                        object.append(" undefined");
                        throw new IllegalArgumentException(object.toString());
                    }
                    object.rightToRight = n3;
                    object.rightToLeft = -1;
                }
                object.rightMargin = n5;
                return;
            }
            case 1: 
        }
        if (n4 == 1) {
            object.leftToLeft = n3;
            object.leftToRight = -1;
        } else {
            if (n4 != 2) {
                object = new StringBuilder();
                object.append("Left to ");
                object.append(this.sideToString(n4));
                object.append(" undefined");
                throw new IllegalArgumentException(object.toString());
            }
            object.leftToRight = n3;
            object.leftToLeft = -1;
        }
        object.leftMargin = n5;
    }

    public void constrainDefaultHeight(int n, int n2) {
        this.get((int)n).heightDefault = n2;
    }

    public void constrainDefaultWidth(int n, int n2) {
        this.get((int)n).widthDefault = n2;
    }

    public void constrainHeight(int n, int n2) {
        this.get((int)n).mHeight = n2;
    }

    public void constrainMaxHeight(int n, int n2) {
        this.get((int)n).heightMax = n2;
    }

    public void constrainMaxWidth(int n, int n2) {
        this.get((int)n).widthMax = n2;
    }

    public void constrainMinHeight(int n, int n2) {
        this.get((int)n).heightMin = n2;
    }

    public void constrainMinWidth(int n, int n2) {
        this.get((int)n).widthMin = n2;
    }

    public void constrainWidth(int n, int n2) {
        this.get((int)n).mWidth = n2;
    }

    public void create(int n, int n2) {
        Constraint constraint = this.get(n);
        constraint.mIsGuideline = true;
        constraint.orientation = n2;
    }

    public void createHorizontalChain(int n, int n2, int n3, int n4, int[] arrn, float[] arrf, int n5) {
        this.createHorizontalChain(n, n2, n3, n4, arrn, arrf, n5, 1, 2);
    }

    public void createHorizontalChainRtl(int n, int n2, int n3, int n4, int[] arrn, float[] arrf, int n5) {
        this.createHorizontalChain(n, n2, n3, n4, arrn, arrf, n5, 6, 7);
    }

    public void createVerticalChain(int n, int n2, int n3, int n4, int[] arrn, float[] arrf, int n5) {
        if (arrn.length >= 2) {
            if (arrf != null && arrf.length != arrn.length) {
                throw new IllegalArgumentException("must have 2 or more widgets in a chain");
            }
            if (arrf != null) {
                this.get((int)arrn[0]).verticalWeight = arrf[0];
            }
            this.get((int)arrn[0]).verticalChainStyle = n5;
            this.connect(arrn[0], 3, n, n2, 0);
            for (n = 1; n < arrn.length; ++n) {
                n2 = arrn[n];
                this.connect(arrn[n], 3, arrn[n - 1], 4, 0);
                this.connect(arrn[n - 1], 4, arrn[n], 3, 0);
                if (arrf == null) continue;
                this.get((int)arrn[n]).verticalWeight = arrf[n];
            }
            this.connect(arrn[arrn.length - 1], 4, n3, n4, 0);
            return;
        }
        throw new IllegalArgumentException("must have 2 or more widgets in a chain");
    }

    public boolean getApplyElevation(int n) {
        return this.get((int)n).applyElevation;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void load(Context var1_1, int var2_4) {
        var3_5 = var1_1.getResources().getXml(var2_4);
        try {
            var2_4 = var3_5.getEventType();
            ** GOTO lbl14
        }
        catch (IOException var1_2) {
            var1_2.printStackTrace();
            return;
        }
        catch (XmlPullParserException var1_3) {
            var1_3.printStackTrace();
            return;
        }
        {
            var3_5.getName();
            block8 : do {
                var2_4 = var3_5.next();
lbl14: // 2 sources:
                if (var2_4 == 1) return;
                if (var2_4 == 0) continue block7;
                switch (var2_4) {
                    case 2: {
                        var4_6 = var3_5.getName();
                        var5_7 = this.fillFromAttributeList(var1_1, Xml.asAttributeSet((XmlPullParser)var3_5));
                        if (var4_6.equalsIgnoreCase("Guideline")) {
                            var5_7.mIsGuideline = true;
                        }
                        this.mConstraints.put(var5_7.mViewId, var5_7);
                        continue block8;
                    }
                    default: {
                        continue block8;
                    }
                    case 3: 
                }
            } while (true);
        }
    }

    public void removeFromHorizontalChain(int n) {
        if (this.mConstraints.containsKey(n)) {
            Constraint constraint = this.mConstraints.get(n);
            int n2 = constraint.leftToRight;
            int n3 = constraint.rightToLeft;
            if (n2 == -1 && n3 == -1) {
                n3 = constraint.startToEnd;
                int n4 = constraint.endToStart;
                if (n3 != -1 || n4 != -1) {
                    if (n3 != -1 && n4 != -1) {
                        this.connect(n3, 7, n4, 6, 0);
                        this.connect(n4, 6, n2, 7, 0);
                    } else if (n2 != -1 || n4 != -1) {
                        if (constraint.rightToRight != -1) {
                            this.connect(n2, 7, constraint.rightToRight, 7, 0);
                        } else if (constraint.leftToLeft != -1) {
                            this.connect(n4, 6, constraint.leftToLeft, 6, 0);
                        }
                    }
                }
                this.clear(n, 6);
                this.clear(n, 7);
                return;
            }
            if (n2 != -1 && n3 != -1) {
                this.connect(n2, 2, n3, 1, 0);
                this.connect(n3, 1, n2, 2, 0);
            } else if (n2 != -1 || n3 != -1) {
                if (constraint.rightToRight != -1) {
                    this.connect(n2, 2, constraint.rightToRight, 2, 0);
                } else if (constraint.leftToLeft != -1) {
                    this.connect(n3, 1, constraint.leftToLeft, 1, 0);
                }
            }
            this.clear(n, 1);
            this.clear(n, 2);
            return;
        }
    }

    public void removeFromVerticalChain(int n) {
        if (this.mConstraints.containsKey(n)) {
            Constraint constraint = this.mConstraints.get(n);
            int n2 = constraint.topToBottom;
            int n3 = constraint.bottomToTop;
            if (n2 != -1 || n3 != -1) {
                if (n2 != -1 && n3 != -1) {
                    this.connect(n2, 4, n3, 3, 0);
                    this.connect(n3, 3, n2, 4, 0);
                } else if (n2 != -1 || n3 != -1) {
                    if (constraint.bottomToBottom != -1) {
                        this.connect(n2, 4, constraint.bottomToBottom, 4, 0);
                    } else if (constraint.topToTop != -1) {
                        this.connect(n3, 3, constraint.topToTop, 3, 0);
                    }
                }
            }
        }
        this.clear(n, 3);
        this.clear(n, 4);
    }

    public void setAlpha(int n, float f) {
        this.get((int)n).alpha = f;
    }

    public void setApplyElevation(int n, boolean bl) {
        this.get((int)n).applyElevation = bl;
    }

    public void setDimensionRatio(int n, String string2) {
        this.get((int)n).dimensionRatio = string2;
    }

    public void setElevation(int n, float f) {
        this.get((int)n).elevation = f;
        this.get((int)n).applyElevation = true;
    }

    public void setGoneMargin(int n, int n2, int n3) {
        Constraint constraint = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                constraint.goneEndMargin = n3;
                return;
            }
            case 6: {
                constraint.goneStartMargin = n3;
                return;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                constraint.goneBottomMargin = n3;
                return;
            }
            case 3: {
                constraint.goneTopMargin = n3;
                return;
            }
            case 2: {
                constraint.goneRightMargin = n3;
                return;
            }
            case 1: 
        }
        constraint.goneLeftMargin = n3;
    }

    public void setGuidelineBegin(int n, int n2) {
        this.get((int)n).guideBegin = n2;
        this.get((int)n).guideEnd = -1;
        this.get((int)n).guidePercent = -1.0f;
    }

    public void setGuidelineEnd(int n, int n2) {
        this.get((int)n).guideEnd = n2;
        this.get((int)n).guideBegin = -1;
        this.get((int)n).guidePercent = -1.0f;
    }

    public void setGuidelinePercent(int n, float f) {
        this.get((int)n).guidePercent = f;
        this.get((int)n).guideEnd = -1;
        this.get((int)n).guideBegin = -1;
    }

    public void setHorizontalBias(int n, float f) {
        this.get((int)n).horizontalBias = f;
    }

    public void setHorizontalChainStyle(int n, int n2) {
        this.get((int)n).horizontalChainStyle = n2;
    }

    public void setHorizontalWeight(int n, float f) {
        this.get((int)n).horizontalWeight = f;
    }

    public void setMargin(int n, int n2, int n3) {
        Constraint constraint = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 7: {
                constraint.endMargin = n3;
                return;
            }
            case 6: {
                constraint.startMargin = n3;
                return;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 4: {
                constraint.bottomMargin = n3;
                return;
            }
            case 3: {
                constraint.topMargin = n3;
                return;
            }
            case 2: {
                constraint.rightMargin = n3;
                return;
            }
            case 1: 
        }
        constraint.leftMargin = n3;
    }

    public void setRotationX(int n, float f) {
        this.get((int)n).rotationX = f;
    }

    public void setRotationY(int n, float f) {
        this.get((int)n).rotationY = f;
    }

    public void setScaleX(int n, float f) {
        this.get((int)n).scaleX = f;
    }

    public void setScaleY(int n, float f) {
        this.get((int)n).scaleY = f;
    }

    public void setTransformPivot(int n, float f, float f2) {
        Constraint constraint = this.get(n);
        constraint.transformPivotY = f2;
        constraint.transformPivotX = f;
    }

    public void setTransformPivotX(int n, float f) {
        this.get((int)n).transformPivotX = f;
    }

    public void setTransformPivotY(int n, float f) {
        this.get((int)n).transformPivotY = f;
    }

    public void setTranslation(int n, float f, float f2) {
        Constraint constraint = this.get(n);
        constraint.translationX = f;
        constraint.translationY = f2;
    }

    public void setTranslationX(int n, float f) {
        this.get((int)n).translationX = f;
    }

    public void setTranslationY(int n, float f) {
        this.get((int)n).translationY = f;
    }

    public void setTranslationZ(int n, float f) {
        this.get((int)n).translationZ = f;
    }

    public void setVerticalBias(int n, float f) {
        this.get((int)n).verticalBias = f;
    }

    public void setVerticalChainStyle(int n, int n2) {
        this.get((int)n).verticalChainStyle = n2;
    }

    public void setVerticalWeight(int n, float f) {
        this.get((int)n).verticalWeight = f;
    }

    public void setVisibility(int n, int n2) {
        this.get((int)n).visibility = n2;
    }

    private static class Constraint {
        static final int UNSET = -1;
        public float alpha = 1.0f;
        public boolean applyElevation = false;
        public int baselineToBaseline = -1;
        public int bottomMargin = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public String dimensionRatio = null;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public float elevation = 0.0f;
        public int endMargin = -1;
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
        public float guidePercent = -1.0f;
        public int heightDefault = -1;
        public int heightMax = -1;
        public int heightMin = -1;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        public float horizontalWeight = 0.0f;
        public int leftMargin = -1;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int mHeight;
        boolean mIsGuideline = false;
        int mViewId;
        public int mWidth;
        public int orientation = -1;
        public int rightMargin = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public int startMargin = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topMargin = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float transformPivotX = 0.0f;
        public float transformPivotY = 0.0f;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        public float verticalWeight = 0.0f;
        public int visibility = 0;
        public int widthDefault = -1;
        public int widthMax = -1;
        public int widthMin = -1;

        private Constraint() {
        }

        private void fillFrom(int n, ConstraintLayout.LayoutParams layoutParams) {
            this.mViewId = n;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.guidePercent = layoutParams.guidePercent;
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.mWidth = layoutParams.width;
            this.mHeight = layoutParams.height;
            this.leftMargin = layoutParams.leftMargin;
            this.rightMargin = layoutParams.rightMargin;
            this.topMargin = layoutParams.topMargin;
            this.bottomMargin = layoutParams.bottomMargin;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.widthDefault = layoutParams.matchConstraintDefaultWidth;
            this.heightDefault = layoutParams.matchConstraintDefaultHeight;
            this.widthMax = layoutParams.matchConstraintMaxWidth;
            this.heightMax = layoutParams.matchConstraintMaxHeight;
            this.widthMin = layoutParams.matchConstraintMinWidth;
            this.heightMin = layoutParams.matchConstraintMinHeight;
            if (Build.VERSION.SDK_INT >= 17) {
                this.endMargin = layoutParams.getMarginEnd();
                this.startMargin = layoutParams.getMarginStart();
                return;
            }
        }

        public void applyTo(ConstraintLayout.LayoutParams layoutParams) {
            layoutParams.leftToLeft = this.leftToLeft;
            layoutParams.leftToRight = this.leftToRight;
            layoutParams.rightToLeft = this.rightToLeft;
            layoutParams.rightToRight = this.rightToRight;
            layoutParams.topToTop = this.topToTop;
            layoutParams.topToBottom = this.topToBottom;
            layoutParams.bottomToTop = this.bottomToTop;
            layoutParams.bottomToBottom = this.bottomToBottom;
            layoutParams.baselineToBaseline = this.baselineToBaseline;
            layoutParams.startToEnd = this.startToEnd;
            layoutParams.startToStart = this.startToStart;
            layoutParams.endToStart = this.endToStart;
            layoutParams.endToEnd = this.endToEnd;
            layoutParams.leftMargin = this.leftMargin;
            layoutParams.rightMargin = this.rightMargin;
            layoutParams.topMargin = this.topMargin;
            layoutParams.bottomMargin = this.bottomMargin;
            layoutParams.goneStartMargin = this.goneStartMargin;
            layoutParams.goneEndMargin = this.goneEndMargin;
            layoutParams.horizontalBias = this.horizontalBias;
            layoutParams.verticalBias = this.verticalBias;
            layoutParams.dimensionRatio = this.dimensionRatio;
            layoutParams.editorAbsoluteX = this.editorAbsoluteX;
            layoutParams.editorAbsoluteY = this.editorAbsoluteY;
            layoutParams.verticalWeight = this.verticalWeight;
            layoutParams.horizontalWeight = this.horizontalWeight;
            layoutParams.verticalChainStyle = this.verticalChainStyle;
            layoutParams.horizontalChainStyle = this.horizontalChainStyle;
            layoutParams.matchConstraintDefaultWidth = this.widthDefault;
            layoutParams.matchConstraintDefaultHeight = this.heightDefault;
            layoutParams.matchConstraintMaxWidth = this.widthMax;
            layoutParams.matchConstraintMaxHeight = this.heightMax;
            layoutParams.matchConstraintMinWidth = this.widthMin;
            layoutParams.matchConstraintMinHeight = this.heightMin;
            layoutParams.orientation = this.orientation;
            layoutParams.guidePercent = this.guidePercent;
            layoutParams.guideBegin = this.guideBegin;
            layoutParams.guideEnd = this.guideEnd;
            layoutParams.width = this.mWidth;
            layoutParams.height = this.mHeight;
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.setMarginStart(this.startMargin);
                layoutParams.setMarginEnd(this.endMargin);
            }
            layoutParams.validate();
        }

        public Constraint clone() {
            Constraint constraint = new Constraint();
            constraint.mIsGuideline = this.mIsGuideline;
            constraint.mWidth = this.mWidth;
            constraint.mHeight = this.mHeight;
            constraint.guideBegin = this.guideBegin;
            constraint.guideEnd = this.guideEnd;
            constraint.guidePercent = this.guidePercent;
            constraint.leftToLeft = this.leftToLeft;
            constraint.leftToRight = this.leftToRight;
            constraint.rightToLeft = this.rightToLeft;
            constraint.rightToRight = this.rightToRight;
            constraint.topToTop = this.topToTop;
            constraint.topToBottom = this.topToBottom;
            constraint.bottomToTop = this.bottomToTop;
            constraint.bottomToBottom = this.bottomToBottom;
            constraint.baselineToBaseline = this.baselineToBaseline;
            constraint.startToEnd = this.startToEnd;
            constraint.startToStart = this.startToStart;
            constraint.endToStart = this.endToStart;
            constraint.endToEnd = this.endToEnd;
            constraint.horizontalBias = this.horizontalBias;
            constraint.verticalBias = this.verticalBias;
            constraint.dimensionRatio = this.dimensionRatio;
            constraint.editorAbsoluteX = this.editorAbsoluteX;
            constraint.editorAbsoluteY = this.editorAbsoluteY;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.orientation = this.orientation;
            constraint.leftMargin = this.leftMargin;
            constraint.rightMargin = this.rightMargin;
            constraint.topMargin = this.topMargin;
            constraint.bottomMargin = this.bottomMargin;
            constraint.endMargin = this.endMargin;
            constraint.startMargin = this.startMargin;
            constraint.visibility = this.visibility;
            constraint.goneLeftMargin = this.goneLeftMargin;
            constraint.goneTopMargin = this.goneTopMargin;
            constraint.goneRightMargin = this.goneRightMargin;
            constraint.goneBottomMargin = this.goneBottomMargin;
            constraint.goneEndMargin = this.goneEndMargin;
            constraint.goneStartMargin = this.goneStartMargin;
            constraint.verticalWeight = this.verticalWeight;
            constraint.horizontalWeight = this.horizontalWeight;
            constraint.horizontalChainStyle = this.horizontalChainStyle;
            constraint.verticalChainStyle = this.verticalChainStyle;
            constraint.alpha = this.alpha;
            constraint.applyElevation = this.applyElevation;
            constraint.elevation = this.elevation;
            constraint.rotationX = this.rotationX;
            constraint.rotationY = this.rotationY;
            constraint.scaleX = this.scaleX;
            constraint.scaleY = this.scaleY;
            constraint.transformPivotX = this.transformPivotX;
            constraint.transformPivotY = this.transformPivotY;
            constraint.translationX = this.translationX;
            constraint.translationY = this.translationY;
            constraint.translationZ = this.translationZ;
            constraint.widthDefault = this.widthDefault;
            constraint.heightDefault = this.heightDefault;
            constraint.widthMax = this.widthMax;
            constraint.heightMax = this.heightMax;
            constraint.widthMin = this.widthMin;
            constraint.heightMin = this.heightMin;
            return constraint;
        }
    }

}

