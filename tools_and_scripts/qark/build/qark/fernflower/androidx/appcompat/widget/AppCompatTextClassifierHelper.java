package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.core.util.Preconditions;

final class AppCompatTextClassifierHelper {
   private TextClassifier mTextClassifier;
   private TextView mTextView;

   AppCompatTextClassifierHelper(TextView var1) {
      this.mTextView = (TextView)Preconditions.checkNotNull(var1);
   }

   public TextClassifier getTextClassifier() {
      TextClassifier var1 = this.mTextClassifier;
      if (var1 == null) {
         TextClassificationManager var2 = (TextClassificationManager)this.mTextView.getContext().getSystemService(TextClassificationManager.class);
         return var2 != null ? var2.getTextClassifier() : TextClassifier.NO_OP;
      } else {
         return var1;
      }
   }

   public void setTextClassifier(TextClassifier var1) {
      this.mTextClassifier = var1;
   }
}
