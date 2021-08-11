/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding.adapters;

import androidx.cardview.widget.CardView;

public class CardViewBindingAdapter {
    public static void setContentPadding(CardView cardView, int n) {
        cardView.setContentPadding(n, n, n, n);
    }

    public static void setContentPaddingBottom(CardView cardView, int n) {
        cardView.setContentPadding(cardView.getContentPaddingLeft(), cardView.getContentPaddingTop(), cardView.getContentPaddingRight(), n);
    }

    public static void setContentPaddingLeft(CardView cardView, int n) {
        cardView.setContentPadding(n, cardView.getContentPaddingTop(), cardView.getContentPaddingRight(), cardView.getContentPaddingBottom());
    }

    public static void setContentPaddingRight(CardView cardView, int n) {
        cardView.setContentPadding(cardView.getContentPaddingLeft(), cardView.getContentPaddingTop(), n, cardView.getContentPaddingBottom());
    }

    public static void setContentPaddingTop(CardView cardView, int n) {
        cardView.setContentPadding(cardView.getContentPaddingLeft(), n, cardView.getContentPaddingRight(), cardView.getContentPaddingBottom());
    }
}

