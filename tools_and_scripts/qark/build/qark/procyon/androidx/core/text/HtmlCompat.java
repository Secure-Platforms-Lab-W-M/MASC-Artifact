// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.text;

import android.text.Html$TagHandler;
import android.text.Html$ImageGetter;
import android.text.Html;
import android.os.Build$VERSION;
import android.text.Spanned;

public final class HtmlCompat
{
    public static final int FROM_HTML_MODE_COMPACT = 63;
    public static final int FROM_HTML_MODE_LEGACY = 0;
    public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
    public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
    public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;
    
    private HtmlCompat() {
    }
    
    public static Spanned fromHtml(final String s, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return Html.fromHtml(s, n);
        }
        return Html.fromHtml(s);
    }
    
    public static Spanned fromHtml(final String s, final int n, final Html$ImageGetter html$ImageGetter, final Html$TagHandler html$TagHandler) {
        if (Build$VERSION.SDK_INT >= 24) {
            return Html.fromHtml(s, n, html$ImageGetter, html$TagHandler);
        }
        return Html.fromHtml(s, html$ImageGetter, html$TagHandler);
    }
    
    public static String toHtml(final Spanned spanned, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            return Html.toHtml(spanned, n);
        }
        return Html.toHtml(spanned);
    }
}
