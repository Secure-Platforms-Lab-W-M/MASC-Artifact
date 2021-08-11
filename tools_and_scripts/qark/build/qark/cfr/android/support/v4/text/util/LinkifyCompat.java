/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.method.LinkMovementMethod
 *  android.text.method.MovementMethod
 *  android.text.style.URLSpan
 *  android.text.util.Linkify
 *  android.text.util.Linkify$MatchFilter
 *  android.text.util.Linkify$TransformFilter
 *  android.webkit.WebView
 *  android.widget.TextView
 */
package android.support.v4.text.util;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;

    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>(){

            @Override
            public final int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start > linkSpec2.start) {
                    return 1;
                }
                if (linkSpec.end < linkSpec2.end) {
                    return 1;
                }
                if (linkSpec.end > linkSpec2.end) {
                    return -1;
                }
                return 0;
            }
        };
    }

    private LinkifyCompat() {
    }

    private static void addLinkMovementMethod(@NonNull TextView textView) {
        MovementMethod movementMethod = textView.getMovementMethod();
        if (movementMethod != null && movementMethod instanceof LinkMovementMethod) {
            return;
        }
        if (textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            return;
        }
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2) {
        if (Build.VERSION.SDK_INT >= 26) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2);
            return;
        }
        LinkifyCompat.addLinks(textView, pattern, string2, null, null, null);
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        if (Build.VERSION.SDK_INT >= 26) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
            return;
        }
        LinkifyCompat.addLinks(textView, pattern, string2, null, matchFilter, transformFilter);
    }

    public static final void addLinks(@NonNull TextView textView, @NonNull Pattern pattern, @Nullable String string2, @Nullable String[] arrstring, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        if (Build.VERSION.SDK_INT >= 26) {
            Linkify.addLinks((TextView)textView, (Pattern)pattern, (String)string2, (String[])arrstring, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
            return;
        }
        SpannableString spannableString = SpannableString.valueOf((CharSequence)textView.getText());
        if (LinkifyCompat.addLinks((Spannable)spannableString, pattern, string2, arrstring, matchFilter, transformFilter)) {
            textView.setText((CharSequence)spannableString);
            LinkifyCompat.addLinkMovementMethod(textView);
            return;
        }
    }

    public static final boolean addLinks(@NonNull Spannable spannable, int n) {
        Object object;
        if (Build.VERSION.SDK_INT >= 26) {
            return Linkify.addLinks((Spannable)spannable, (int)n);
        }
        if (n == 0) {
            return false;
        }
        Iterator iterator = (URLSpan[])spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int i = iterator.length - 1; i >= 0; --i) {
            spannable.removeSpan((Object)iterator[i]);
        }
        if ((n & 4) != 0) {
            Linkify.addLinks((Spannable)spannable, (int)4);
        }
        iterator = new ArrayList();
        if ((n & 1) != 0) {
            object = PatternsCompat.AUTOLINK_WEB_URL;
            Linkify.MatchFilter matchFilter = Linkify.sUrlMatchFilter;
            LinkifyCompat.gatherLinks(iterator, spannable, (Pattern)object, new String[]{"http://", "https://", "rtsp://"}, matchFilter, null);
        }
        if ((n & 2) != 0) {
            LinkifyCompat.gatherLinks(iterator, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        }
        if ((n & 8) != 0) {
            LinkifyCompat.gatherMapLinks(iterator, spannable);
        }
        LinkifyCompat.pruneOverlaps(iterator, spannable);
        if (iterator.size() == 0) {
            return false;
        }
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            object = (LinkSpec)iterator.next();
            if (object.frameworkAddedSpan != null) continue;
            LinkifyCompat.applyLink(object.url, object.start, object.end, spannable);
        }
        return true;
    }

    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String string2) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Linkify.addLinks((Spannable)spannable, (Pattern)pattern, (String)string2);
        }
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, null, null);
    }

    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern pattern, @Nullable String string2, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Linkify.addLinks((Spannable)spannable, (Pattern)pattern, (String)string2, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
        }
        return LinkifyCompat.addLinks(spannable, pattern, string2, null, matchFilter, transformFilter);
    }

    public static final boolean addLinks(@NonNull Spannable spannable, @NonNull Pattern object, @Nullable String string2, @Nullable String[] arrstring, @Nullable Linkify.MatchFilter matchFilter, @Nullable Linkify.TransformFilter transformFilter) {
        int n;
        if (Build.VERSION.SDK_INT >= 26) {
            return Linkify.addLinks((Spannable)spannable, (Pattern)object, (String)string2, (String[])arrstring, (Linkify.MatchFilter)matchFilter, (Linkify.TransformFilter)transformFilter);
        }
        String string3 = string2;
        if (string2 == null) {
            string3 = "";
        }
        if (arrstring == null || arrstring.length < 1) {
            arrstring = EMPTY_STRING;
        }
        String[] arrstring2 = new String[arrstring.length + 1];
        arrstring2[0] = string3.toLowerCase(Locale.ROOT);
        for (n = 0; n < arrstring.length; ++n) {
            string2 = arrstring[n];
            string2 = string2 == null ? "" : string2.toLowerCase(Locale.ROOT);
            arrstring2[n + 1] = string2;
        }
        boolean bl = false;
        object = object.matcher((CharSequence)spannable);
        while (object.find()) {
            n = object.start();
            int n2 = object.end();
            boolean bl2 = true;
            if (matchFilter != null) {
                bl2 = matchFilter.acceptMatch((CharSequence)spannable, n, n2);
            }
            if (!bl2) continue;
            LinkifyCompat.applyLink(LinkifyCompat.makeUrl(object.group(0), arrstring2, (Matcher)object, transformFilter), n, n2, spannable);
            bl = true;
        }
        return bl;
    }

    public static final boolean addLinks(@NonNull TextView textView, int n) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Linkify.addLinks((TextView)textView, (int)n);
        }
        if (n == 0) {
            return false;
        }
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            if (LinkifyCompat.addLinks((Spannable)charSequence, n)) {
                LinkifyCompat.addLinkMovementMethod(textView);
                return true;
            }
            return false;
        }
        if (LinkifyCompat.addLinks((Spannable)(charSequence = SpannableString.valueOf((CharSequence)charSequence)), n)) {
            LinkifyCompat.addLinkMovementMethod(textView);
            textView.setText(charSequence);
            return true;
        }
        return false;
    }

    private static void applyLink(String string2, int n, int n2, Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(string2), n, n2, 33);
    }

    private static void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern object, String[] arrstring, Linkify.MatchFilter matchFilter, Linkify.TransformFilter transformFilter) {
        object = object.matcher((CharSequence)spannable);
        while (object.find()) {
            int n = object.start();
            int n2 = object.end();
            if (matchFilter != null && !matchFilter.acceptMatch((CharSequence)spannable, n, n2)) continue;
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.url = LinkifyCompat.makeUrl(object.group(0), arrstring, (Matcher)object, transformFilter);
            linkSpec.start = n;
            linkSpec.end = n2;
            arrayList.add(linkSpec);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final void gatherMapLinks(ArrayList<LinkSpec> arrayList, Spannable object) {
        object = object.toString();
        int n = 0;
        try {
            String string2;
            while ((string2 = WebView.findAddress((String)object)) != null) {
                int n2 = object.indexOf(string2);
                if (n2 < 0) {
                    return;
                }
                LinkSpec linkSpec = new LinkSpec();
                int n3 = n2 + string2.length();
                linkSpec.start = n + n2;
                linkSpec.end = n + n3;
                object = object.substring(n3);
                n += n3;
                try {
                    string2 = URLEncoder.encode(string2, "UTF-8");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("geo:0,0?q=");
                    stringBuilder.append(string2);
                    linkSpec.url = stringBuilder.toString();
                    arrayList.add(linkSpec);
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {}
            }
            return;
        }
        catch (UnsupportedOperationException unsupportedOperationException) {
            return;
        }
    }

    private static String makeUrl(@NonNull String string2, @NonNull String[] arrstring, Matcher object, @Nullable Linkify.TransformFilter transformFilter) {
        boolean bl;
        if (transformFilter != null) {
            string2 = transformFilter.transformUrl((Matcher)object, string2);
        }
        boolean bl2 = false;
        int n = 0;
        do {
            bl = bl2;
            object = string2;
            if (n >= arrstring.length) break;
            if (string2.regionMatches(true, 0, arrstring[n], 0, arrstring[n].length())) {
                bl = true;
                if (!string2.regionMatches(false, 0, arrstring[n], 0, arrstring[n].length())) {
                    object = new StringBuilder();
                    object.append(arrstring[n]);
                    object.append(string2.substring(arrstring[n].length()));
                    object = object.toString();
                    break;
                }
                object = string2;
                break;
            }
            ++n;
        } while (true);
        if (!bl && arrstring.length > 0) {
            string2 = new StringBuilder();
            string2.append(arrstring[0]);
            string2.append((String)object);
            return string2.toString();
        }
        return object;
    }

    private static final void pruneOverlaps(ArrayList<LinkSpec> arrayList, Spannable spannable) {
        LinkSpec linkSpec;
        int n;
        Object object = (URLSpan[])spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (n = 0; n < object.length; ++n) {
            linkSpec = new LinkSpec();
            linkSpec.frameworkAddedSpan = object[n];
            linkSpec.start = spannable.getSpanStart((Object)object[n]);
            linkSpec.end = spannable.getSpanEnd(object[n]);
            arrayList.add(linkSpec);
        }
        Collections.sort(arrayList, COMPARATOR);
        int n2 = arrayList.size();
        int n3 = 0;
        while (n3 < n2 - 1) {
            object = arrayList.get(n3);
            linkSpec = arrayList.get(n3 + 1);
            n = -1;
            if (object.start <= linkSpec.start && object.end > linkSpec.start) {
                if (linkSpec.end <= object.end) {
                    n = n3 + 1;
                } else if (object.end - object.start > linkSpec.end - linkSpec.start) {
                    n = n3 + 1;
                } else if (object.end - object.start < linkSpec.end - linkSpec.start) {
                    n = n3;
                }
                if (n != -1) {
                    object = arrayList.get((int)n).frameworkAddedSpan;
                    if (object != null) {
                        spannable.removeSpan(object);
                    }
                    arrayList.remove(n);
                    --n2;
                    continue;
                }
            }
            ++n3;
        }
    }

    private static class LinkSpec {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;

        LinkSpec() {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface LinkifyMask {
    }

}

