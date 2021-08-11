// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint$Align;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

public class Placeholder extends View
{
    private View mContent;
    private int mContentId;
    private int mEmptyVisibility;
    
    public Placeholder(final Context context) {
        super(context);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(null);
    }
    
    public Placeholder(final Context context, final AttributeSet set) {
        super(context, set);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(set);
    }
    
    public Placeholder(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(set);
    }
    
    public Placeholder(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n);
        this.mContentId = -1;
        this.mContent = null;
        this.mEmptyVisibility = 4;
        this.init(set);
    }
    
    private void init(final AttributeSet set) {
        super.setVisibility(this.mEmptyVisibility);
        this.mContentId = -1;
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_placeholder);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_placeholder_content) {
                    this.mContentId = obtainStyledAttributes.getResourceId(index, this.mContentId);
                }
                else if (index == R.styleable.ConstraintLayout_placeholder_emptyVisibility) {
                    this.mEmptyVisibility = obtainStyledAttributes.getInt(index, this.mEmptyVisibility);
                }
            }
        }
    }
    
    public View getContent() {
        return this.mContent;
    }
    
    public int getEmptyVisibility() {
        return this.mEmptyVisibility;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.isInEditMode()) {
            canvas.drawRGB(223, 223, 223);
            final Paint paint = new Paint();
            paint.setARGB(255, 210, 210, 210);
            paint.setTextAlign(Paint$Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
            final Rect rect = new Rect();
            canvas.getClipBounds(rect);
            paint.setTextSize((float)rect.height());
            final int height = rect.height();
            final int width = rect.width();
            paint.setTextAlign(Paint$Align.LEFT);
            paint.getTextBounds("?", 0, "?".length(), rect);
            canvas.drawText("?", width / 2.0f - rect.width() / 2.0f - rect.left, height / 2.0f + rect.height() / 2.0f - rect.bottom, paint);
        }
    }
    
    public void setContentId(final int mContentId) {
        if (this.mContentId == mContentId) {
            return;
        }
        final View mContent = this.mContent;
        if (mContent != null) {
            mContent.setVisibility(0);
            ((ConstraintLayout.LayoutParams)this.mContent.getLayoutParams()).isInPlaceholder = false;
            this.mContent = null;
        }
        if ((this.mContentId = mContentId) == -1) {
            return;
        }
        final View viewById = ((View)this.getParent()).findViewById(mContentId);
        if (viewById != null) {
            viewById.setVisibility(8);
        }
    }
    
    public void setEmptyVisibility(final int mEmptyVisibility) {
        this.mEmptyVisibility = mEmptyVisibility;
    }
    
    public void updatePostMeasure(final ConstraintLayout constraintLayout) {
        if (this.mContent == null) {
            return;
        }
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        final ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams)this.mContent.getLayoutParams();
        layoutParams2.widget.setVisibility(0);
        layoutParams.widget.setWidth(layoutParams2.widget.getWidth());
        layoutParams.widget.setHeight(layoutParams2.widget.getHeight());
        layoutParams2.widget.setVisibility(8);
    }
    
    public void updatePreLayout(final ConstraintLayout constraintLayout) {
        if (this.mContentId == -1) {
            if (!this.isInEditMode()) {
                this.setVisibility(this.mEmptyVisibility);
            }
        }
        this.mContent = constraintLayout.findViewById(this.mContentId);
        final View mContent = this.mContent;
        if (mContent != null) {
            ((ConstraintLayout.LayoutParams)mContent.getLayoutParams()).isInPlaceholder = true;
            this.mContent.setVisibility(0);
            this.setVisibility(0);
        }
    }
}
