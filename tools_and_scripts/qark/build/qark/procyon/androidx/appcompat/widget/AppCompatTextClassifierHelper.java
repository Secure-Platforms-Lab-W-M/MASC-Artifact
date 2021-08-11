// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import androidx.core.util.Preconditions;
import android.widget.TextView;
import android.view.textclassifier.TextClassifier;

final class AppCompatTextClassifierHelper
{
    private TextClassifier mTextClassifier;
    private TextView mTextView;
    
    AppCompatTextClassifierHelper(final TextView textView) {
        this.mTextView = Preconditions.checkNotNull(textView);
    }
    
    public TextClassifier getTextClassifier() {
        final TextClassifier mTextClassifier = this.mTextClassifier;
        if (mTextClassifier != null) {
            return mTextClassifier;
        }
        final TextClassificationManager textClassificationManager = (TextClassificationManager)this.mTextView.getContext().getSystemService((Class)TextClassificationManager.class);
        if (textClassificationManager != null) {
            return textClassificationManager.getTextClassifier();
        }
        return TextClassifier.NO_OP;
    }
    
    public void setTextClassifier(final TextClassifier mTextClassifier) {
        this.mTextClassifier = mTextClassifier;
    }
}
