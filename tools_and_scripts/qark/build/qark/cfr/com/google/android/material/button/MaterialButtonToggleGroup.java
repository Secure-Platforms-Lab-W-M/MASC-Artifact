/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$style
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.button;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

public class MaterialButtonToggleGroup
extends LinearLayout {
    private static final String LOG_TAG = MaterialButtonToggleGroup.class.getSimpleName();
    private int checkedId;
    private final CheckedStateTracker checkedStateTracker;
    private Integer[] childOrder;
    private final Comparator<MaterialButton> childOrderComparator;
    private final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    private final List<CornerData> originalCornerData = new ArrayList<CornerData>();
    private final PressedStateTracker pressedStateTracker;
    private boolean singleSelection;
    private boolean skipCheckedStateTracker;

    public MaterialButtonToggleGroup(Context context) {
        this(context, null);
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.materialButtonToggleGroupStyle);
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.checkedStateTracker = new CheckedStateTracker();
        this.pressedStateTracker = new PressedStateTracker();
        this.onButtonCheckedListeners = new LinkedHashSet();
        this.childOrderComparator = new Comparator<MaterialButton>(){

            @Override
            public int compare(MaterialButton materialButton, MaterialButton materialButton2) {
                int n = Boolean.valueOf(materialButton.isChecked()).compareTo(materialButton2.isChecked());
                if (n != 0) {
                    return n;
                }
                n = Boolean.valueOf(materialButton.isPressed()).compareTo(materialButton2.isPressed());
                if (n != 0) {
                    return n;
                }
                return Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild((View)materialButton)).compareTo(MaterialButtonToggleGroup.this.indexOfChild((View)materialButton2));
            }
        };
        this.skipCheckedStateTracker = false;
        context = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.MaterialButtonToggleGroup, n, R.style.Widget_MaterialComponents_MaterialButtonToggleGroup, new int[0]);
        this.setSingleSelection(context.getBoolean(R.styleable.MaterialButtonToggleGroup_singleSelection, false));
        this.checkedId = context.getResourceId(R.styleable.MaterialButtonToggleGroup_checkedButton, -1);
        this.setChildrenDrawingOrderEnabled(true);
        context.recycle();
    }

    private void adjustChildMarginsAndUpdateLayout() {
        int n = this.getFirstVisibleChildIndex();
        if (n == -1) {
            return;
        }
        for (int i = n + 1; i < this.getChildCount(); ++i) {
            MaterialButton materialButton = this.getChildButton(i);
            MaterialButton materialButton2 = this.getChildButton(i - 1);
            int n2 = Math.min(materialButton.getStrokeWidth(), materialButton2.getStrokeWidth());
            materialButton2 = this.buildLayoutParams((View)materialButton);
            if (this.getOrientation() == 0) {
                MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)materialButton2, 0);
                MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)materialButton2, - n2);
            } else {
                materialButton2.bottomMargin = 0;
                materialButton2.topMargin = - n2;
            }
            materialButton.setLayoutParams((ViewGroup.LayoutParams)materialButton2);
        }
        this.resetChildMargins(n);
    }

    private LinearLayout.LayoutParams buildLayoutParams(View view) {
        if ((view = view.getLayoutParams()) instanceof LinearLayout.LayoutParams) {
            return (LinearLayout.LayoutParams)view;
        }
        return new LinearLayout.LayoutParams(view.width, view.height);
    }

    private void checkForced(int n) {
        this.setCheckedStateForView(n, true);
        this.updateCheckedStates(n, true);
        this.setCheckedId(n);
    }

    private void dispatchOnButtonChecked(int n, boolean bl) {
        Iterator<OnButtonCheckedListener> iterator = this.onButtonCheckedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onButtonChecked(this, n, bl);
        }
    }

    private MaterialButton getChildButton(int n) {
        return (MaterialButton)this.getChildAt(n);
    }

    private int getFirstVisibleChildIndex() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            if (!this.isChildVisible(i)) continue;
            return i;
        }
        return -1;
    }

    private int getLastVisibleChildIndex() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            if (!this.isChildVisible(i)) continue;
            return i;
        }
        return -1;
    }

    private CornerData getNewCornerData(int n, int n2, int n3) {
        int n4 = this.getChildCount();
        CornerData cornerData = this.originalCornerData.get(n);
        boolean bl = true;
        if (n4 == 1) {
            return cornerData;
        }
        if (this.getOrientation() != 0) {
            bl = false;
        }
        if (n == n2) {
            if (bl) {
                return CornerData.start(cornerData, (View)this);
            }
            return CornerData.top(cornerData);
        }
        if (n == n3) {
            if (bl) {
                return CornerData.end(cornerData, (View)this);
            }
            return CornerData.bottom(cornerData);
        }
        return null;
    }

    private boolean isChildVisible(int n) {
        if (this.getChildAt(n).getVisibility() != 8) {
            return true;
        }
        return false;
    }

    private void resetChildMargins(int n) {
        if (this.getChildCount() != 0) {
            if (n == -1) {
                return;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.getChildButton(n).getLayoutParams();
            if (this.getOrientation() == 1) {
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                return;
            }
            MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)layoutParams, 0);
            MarginLayoutParamsCompat.setMarginStart((ViewGroup.MarginLayoutParams)layoutParams, 0);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            return;
        }
    }

    private void setCheckedId(int n) {
        this.checkedId = n;
        this.dispatchOnButtonChecked(n, true);
    }

    private void setCheckedStateForView(int n, boolean bl) {
        View view = this.findViewById(n);
        if (view instanceof MaterialButton) {
            this.skipCheckedStateTracker = true;
            ((MaterialButton)view).setChecked(bl);
            this.skipCheckedStateTracker = false;
        }
    }

    private void setGeneratedIdIfNeeded(MaterialButton materialButton) {
        if (materialButton.getId() == -1) {
            materialButton.setId(ViewCompat.generateViewId());
        }
    }

    private void setupButtonChild(MaterialButton materialButton) {
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        materialButton.setCheckable(true);
        materialButton.addOnCheckedChangeListener(this.checkedStateTracker);
        materialButton.setOnPressedChangeListenerInternal(this.pressedStateTracker);
        materialButton.setShouldDrawSurfaceColorStroke(true);
    }

    private static void updateBuilderWithCornerData(ShapeAppearanceModel.Builder builder, CornerData cornerData) {
        if (cornerData == null) {
            builder.setAllCornerSizes(0.0f);
            return;
        }
        builder.setTopLeftCornerSize(cornerData.topLeft).setBottomLeftCornerSize(cornerData.bottomLeft).setTopRightCornerSize(cornerData.topRight).setBottomRightCornerSize(cornerData.bottomRight);
    }

    private void updateCheckedStates(int n, boolean bl) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            MaterialButton materialButton = this.getChildButton(i);
            if (!materialButton.isChecked() || !this.singleSelection || !bl || materialButton.getId() == n) continue;
            this.setCheckedStateForView(materialButton.getId(), false);
            this.dispatchOnButtonChecked(materialButton.getId(), false);
        }
    }

    private void updateChildOrder() {
        TreeMap<MaterialButton, Integer> treeMap = new TreeMap<MaterialButton, Integer>(this.childOrderComparator);
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            treeMap.put(this.getChildButton(i), i);
        }
        this.childOrder = treeMap.values().toArray(new Integer[0]);
    }

    public void addOnButtonCheckedListener(OnButtonCheckedListener onButtonCheckedListener) {
        this.onButtonCheckedListeners.add(onButtonCheckedListener);
    }

    public void addView(View object, int n, ViewGroup.LayoutParams layoutParams) {
        if (!(object instanceof MaterialButton)) {
            Log.e((String)LOG_TAG, (String)"Child views must be of type MaterialButton.");
            return;
        }
        super.addView((View)object, n, layoutParams);
        object = (MaterialButton)object;
        this.setGeneratedIdIfNeeded((MaterialButton)object);
        this.setupButtonChild((MaterialButton)object);
        if (object.isChecked()) {
            this.updateCheckedStates(object.getId(), true);
            this.setCheckedId(object.getId());
        }
        object = object.getShapeAppearanceModel();
        this.originalCornerData.add(new CornerData(object.getTopLeftCornerSize(), object.getBottomLeftCornerSize(), object.getTopRightCornerSize(), object.getBottomRightCornerSize()));
    }

    public void check(int n) {
        if (n == this.checkedId) {
            return;
        }
        this.checkForced(n);
    }

    public void clearChecked() {
        this.skipCheckedStateTracker = true;
        for (int i = 0; i < this.getChildCount(); ++i) {
            MaterialButton materialButton = this.getChildButton(i);
            materialButton.setChecked(false);
            this.dispatchOnButtonChecked(materialButton.getId(), false);
        }
        this.skipCheckedStateTracker = false;
        this.setCheckedId(-1);
    }

    public void clearOnButtonCheckedListeners() {
        this.onButtonCheckedListeners.clear();
    }

    protected void dispatchDraw(Canvas canvas) {
        this.updateChildOrder();
        super.dispatchDraw(canvas);
    }

    public CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    public int getCheckedButtonId() {
        if (this.singleSelection) {
            return this.checkedId;
        }
        return -1;
    }

    public List<Integer> getCheckedButtonIds() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < this.getChildCount(); ++i) {
            MaterialButton materialButton = this.getChildButton(i);
            if (!materialButton.isChecked()) continue;
            arrayList.add(materialButton.getId());
        }
        return arrayList;
    }

    protected int getChildDrawingOrder(int n, int n2) {
        Integer[] arrinteger = this.childOrder;
        if (arrinteger != null && n2 < arrinteger.length) {
            return arrinteger[n2];
        }
        Log.w((String)LOG_TAG, (String)"Child order wasn't updated");
        return n2;
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int n = this.checkedId;
        if (n != -1) {
            this.checkForced(n);
        }
    }

    protected void onMeasure(int n, int n2) {
        this.updateChildShapes();
        this.adjustChildMarginsAndUpdateLayout();
        super.onMeasure(n, n2);
    }

    public void onViewRemoved(View view) {
        int n;
        super.onViewRemoved(view);
        if (view instanceof MaterialButton) {
            ((MaterialButton)view).removeOnCheckedChangeListener(this.checkedStateTracker);
            ((MaterialButton)view).setOnPressedChangeListenerInternal(null);
        }
        if ((n = this.indexOfChild(view)) >= 0) {
            this.originalCornerData.remove(n);
        }
        this.updateChildShapes();
        this.adjustChildMarginsAndUpdateLayout();
    }

    public void removeOnButtonCheckedListener(OnButtonCheckedListener onButtonCheckedListener) {
        this.onButtonCheckedListeners.remove(onButtonCheckedListener);
    }

    public void setSingleSelection(int n) {
        this.setSingleSelection(this.getResources().getBoolean(n));
    }

    public void setSingleSelection(boolean bl) {
        if (this.singleSelection != bl) {
            this.singleSelection = bl;
            this.clearChecked();
        }
    }

    public void uncheck(int n) {
        this.setCheckedStateForView(n, false);
        this.updateCheckedStates(n, false);
        this.checkedId = -1;
        this.dispatchOnButtonChecked(n, false);
    }

    void updateChildShapes() {
        int n = this.getChildCount();
        int n2 = this.getFirstVisibleChildIndex();
        int n3 = this.getLastVisibleChildIndex();
        for (int i = 0; i < n; ++i) {
            MaterialButton materialButton = this.getChildButton(i);
            if (materialButton.getVisibility() == 8) continue;
            ShapeAppearanceModel.Builder builder = materialButton.getShapeAppearanceModel().toBuilder();
            MaterialButtonToggleGroup.updateBuilderWithCornerData(builder, this.getNewCornerData(i, n2, n3));
            materialButton.setShapeAppearanceModel(builder.build());
        }
    }

    private class CheckedStateTracker
    implements MaterialButton.OnCheckedChangeListener {
        private CheckedStateTracker() {
        }

        @Override
        public void onCheckedChanged(MaterialButton materialButton, boolean bl) {
            if (MaterialButtonToggleGroup.this.skipCheckedStateTracker) {
                return;
            }
            if (MaterialButtonToggleGroup.this.singleSelection) {
                MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
                int n = bl ? materialButton.getId() : -1;
                materialButtonToggleGroup.checkedId = n;
            }
            MaterialButtonToggleGroup.this.dispatchOnButtonChecked(materialButton.getId(), bl);
            MaterialButtonToggleGroup.this.updateCheckedStates(materialButton.getId(), bl);
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

    private static class CornerData {
        private static final CornerSize noCorner = new AbsoluteCornerSize(0.0f);
        CornerSize bottomLeft;
        CornerSize bottomRight;
        CornerSize topLeft;
        CornerSize topRight;

        CornerData(CornerSize cornerSize, CornerSize cornerSize2, CornerSize cornerSize3, CornerSize cornerSize4) {
            this.topLeft = cornerSize;
            this.topRight = cornerSize3;
            this.bottomRight = cornerSize4;
            this.bottomLeft = cornerSize2;
        }

        public static CornerData bottom(CornerData cornerData) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, cornerData.bottomLeft, cornerSize, cornerData.bottomRight);
        }

        public static CornerData end(CornerData cornerData, View view) {
            if (ViewUtils.isLayoutRtl(view)) {
                return CornerData.left(cornerData);
            }
            return CornerData.right(cornerData);
        }

        public static CornerData left(CornerData object) {
            CornerSize cornerSize = object.topLeft;
            object = object.bottomLeft;
            CornerSize cornerSize2 = noCorner;
            return new CornerData(cornerSize, (CornerSize)object, cornerSize2, cornerSize2);
        }

        public static CornerData right(CornerData cornerData) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, cornerSize, cornerData.topRight, cornerData.bottomRight);
        }

        public static CornerData start(CornerData cornerData, View view) {
            if (ViewUtils.isLayoutRtl(view)) {
                return CornerData.right(cornerData);
            }
            return CornerData.left(cornerData);
        }

        public static CornerData top(CornerData cornerData) {
            CornerSize cornerSize = cornerData.topLeft;
            CornerSize cornerSize2 = noCorner;
            return new CornerData(cornerSize, cornerSize2, cornerData.topRight, cornerSize2);
        }
    }

    public static interface OnButtonCheckedListener {
        public void onButtonChecked(MaterialButtonToggleGroup var1, int var2, boolean var3);
    }

    private class PressedStateTracker
    implements MaterialButton.OnPressedChangeListener {
        private PressedStateTracker() {
        }

        @Override
        public void onPressedChanged(MaterialButton materialButton, boolean bl) {
            MaterialButtonToggleGroup.this.updateCheckedStates(materialButton.getId(), materialButton.isChecked());
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

}

