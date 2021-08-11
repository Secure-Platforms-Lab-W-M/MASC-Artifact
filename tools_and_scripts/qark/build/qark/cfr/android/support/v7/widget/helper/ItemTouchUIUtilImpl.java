/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.view.View
 */
package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.View;

class ItemTouchUIUtilImpl {
    ItemTouchUIUtilImpl() {
    }

    static class Api21Impl
    extends BaseImpl {
        Api21Impl() {
        }

        private float findMaxElevation(RecyclerView recyclerView, View view) {
            int n = recyclerView.getChildCount();
            float f = 0.0f;
            for (int i = 0; i < n; ++i) {
                float f2;
                View view2 = recyclerView.getChildAt(i);
                if (view2 == view || (f2 = ViewCompat.getElevation(view2)) <= f) continue;
                f = f2;
            }
            return f;
        }

        @Override
        public void clearView(View view) {
            Object object = view.getTag(R.id.item_touch_helper_previous_elevation);
            if (object != null && object instanceof Float) {
                ViewCompat.setElevation(view, ((Float)object).floatValue());
            }
            view.setTag(R.id.item_touch_helper_previous_elevation, (Object)null);
            super.clearView(view);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView recyclerView, View view, float f, float f2, int n, boolean bl) {
            if (bl && view.getTag(R.id.item_touch_helper_previous_elevation) == null) {
                float f3 = ViewCompat.getElevation(view);
                ViewCompat.setElevation(view, this.findMaxElevation(recyclerView, view) + 1.0f);
                view.setTag(R.id.item_touch_helper_previous_elevation, (Object)Float.valueOf(f3));
            }
            super.onDraw(canvas, recyclerView, view, f, f2, n, bl);
        }
    }

    static class BaseImpl
    implements ItemTouchUIUtil {
        BaseImpl() {
        }

        @Override
        public void clearView(View view) {
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView recyclerView, View view, float f, float f2, int n, boolean bl) {
            view.setTranslationX(f);
            view.setTranslationY(f2);
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, View view, float f, float f2, int n, boolean bl) {
        }

        @Override
        public void onSelected(View view) {
        }
    }

}

