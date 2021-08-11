package androidx.core.text;

import android.os.Build.VERSION;
import android.text.Html;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;

public final class HtmlCompat {
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

   public static Spanned fromHtml(String var0, int var1) {
      return VERSION.SDK_INT >= 24 ? Html.fromHtml(var0, var1) : Html.fromHtml(var0);
   }

   public static Spanned fromHtml(String var0, int var1, ImageGetter var2, TagHandler var3) {
      return VERSION.SDK_INT >= 24 ? Html.fromHtml(var0, var1, var2, var3) : Html.fromHtml(var0, var2, var3);
   }

   public static String toHtml(Spanned var0, int var1) {
      return VERSION.SDK_INT >= 24 ? Html.toHtml(var0, var1) : Html.toHtml(var0);
   }
}
