// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.text.util;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Collections;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.webkit.WebView;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.Iterator;
import android.support.v4.util.PatternsCompat;
import java.util.ArrayList;
import android.text.style.URLSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify$TransformFilter;
import android.text.util.Linkify$MatchFilter;
import android.text.util.Linkify;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import java.util.regex.Pattern;
import android.text.method.MovementMethod;
import android.text.method.LinkMovementMethod;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.util.Comparator;

public final class LinkifyCompat
{
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;
    
    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>() {
            @Override
            public final int compare(final LinkSpec linkSpec, final LinkSpec linkSpec2) {
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
    
    private static void addLinkMovementMethod(@NonNull final TextView textView) {
        final MovementMethod movementMethod = textView.getMovementMethod();
        if (movementMethod != null && movementMethod instanceof LinkMovementMethod) {
            return;
        }
        if (textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s);
            return;
        }
        addLinks(textView, pattern, s, null, null, null);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s, linkify$MatchFilter, linkify$TransformFilter);
            return;
        }
        addLinks(textView, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final String[] array, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter);
            return;
        }
        final SpannableString value = SpannableString.valueOf(textView.getText());
        if (addLinks((Spannable)value, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter)) {
            textView.setText((CharSequence)value);
            addLinkMovementMethod(textView);
        }
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, n);
        }
        if (n == 0) {
            return false;
        }
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = array.length - 1; i >= 0; --i) {
            spannable.removeSpan((Object)array[i]);
        }
        if ((n & 0x4) != 0x0) {
            Linkify.addLinks(spannable, 4);
        }
        final ArrayList list = new ArrayList<LinkSpec>();
        if ((n & 0x1) != 0x0) {
            gatherLinks(list, spannable, PatternsCompat.AUTOLINK_WEB_URL, new String[] { "http://", "https://", "rtsp://" }, Linkify.sUrlMatchFilter, null);
        }
        if ((n & 0x2) != 0x0) {
            gatherLinks(list, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null);
        }
        if ((n & 0x8) != 0x0) {
            gatherMapLinks(list, spannable);
        }
        pruneOverlaps(list, spannable);
        if (list.size() == 0) {
            return false;
        }
        for (final LinkSpec linkSpec : list) {
            if (linkSpec.frameworkAddedSpan == null) {
                applyLink(linkSpec.url, linkSpec.start, linkSpec.end, spannable);
            }
        }
        return true;
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, pattern, s);
        }
        return addLinks(spannable, pattern, s, null, null, null);
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, pattern, s, linkify$MatchFilter, linkify$TransformFilter);
        }
        return addLinks(spannable, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable String lowerCase, @Nullable String[] empty_STRING, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, pattern, lowerCase, empty_STRING, linkify$MatchFilter, linkify$TransformFilter);
        }
        String s;
        if ((s = lowerCase) == null) {
            s = "";
        }
        if (empty_STRING == null || empty_STRING.length < 1) {
            empty_STRING = LinkifyCompat.EMPTY_STRING;
        }
        final String[] array = new String[empty_STRING.length + 1];
        array[0] = s.toLowerCase(Locale.ROOT);
        for (int i = 0; i < empty_STRING.length; ++i) {
            lowerCase = empty_STRING[i];
            if (lowerCase == null) {
                lowerCase = "";
            }
            else {
                lowerCase = lowerCase.toLowerCase(Locale.ROOT);
            }
            array[i + 1] = lowerCase;
        }
        boolean b = false;
        final Matcher matcher = pattern.matcher((CharSequence)spannable);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            boolean acceptMatch = true;
            if (linkify$MatchFilter != null) {
                acceptMatch = linkify$MatchFilter.acceptMatch((CharSequence)spannable, start, end);
            }
            if (acceptMatch) {
                applyLink(makeUrl(matcher.group(0), array, matcher, linkify$TransformFilter), start, end, spannable);
                b = true;
            }
        }
        return b;
    }
    
    public static final boolean addLinks(@NonNull final TextView textView, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(textView, n);
        }
        if (n == 0) {
            return false;
        }
        final CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            if (addLinks((Spannable)text, n)) {
                addLinkMovementMethod(textView);
                return true;
            }
            return false;
        }
        else {
            final SpannableString value = SpannableString.valueOf(text);
            if (addLinks((Spannable)value, n)) {
                addLinkMovementMethod(textView);
                textView.setText((CharSequence)value);
                return true;
            }
            return false;
        }
    }
    
    private static void applyLink(final String s, final int n, final int n2, final Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(s), n, n2, 33);
    }
    
    private static void gatherLinks(final ArrayList<LinkSpec> list, final Spannable spannable, final Pattern pattern, final String[] array, final Linkify$MatchFilter linkify$MatchFilter, final Linkify$TransformFilter linkify$TransformFilter) {
        final Matcher matcher = pattern.matcher((CharSequence)spannable);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            if (linkify$MatchFilter != null && !linkify$MatchFilter.acceptMatch((CharSequence)spannable, start, end)) {
                continue;
            }
            final LinkSpec linkSpec = new LinkSpec();
            linkSpec.url = makeUrl(matcher.group(0), array, matcher, linkify$TransformFilter);
            linkSpec.start = start;
            linkSpec.end = end;
            list.add(linkSpec);
        }
    }
    
    private static final void gatherMapLinks(final ArrayList<LinkSpec> list, Spannable spannable) {
        spannable = (Spannable)spannable.toString();
        int n = 0;
        try {
            while (true) {
                final String address = WebView.findAddress((String)spannable);
                if (address == null) {
                    return;
                }
                final int index = ((String)spannable).indexOf(address);
                if (index < 0) {
                    return;
                }
                final LinkSpec linkSpec = new LinkSpec();
                final int n2 = index + address.length();
                linkSpec.start = n + index;
                linkSpec.end = n + n2;
                spannable = (Spannable)((String)spannable).substring(n2);
                n += n2;
                try {
                    final String encode = URLEncoder.encode(address, "UTF-8");
                    final StringBuilder sb = new StringBuilder();
                    sb.append("geo:0,0?q=");
                    sb.append(encode);
                    linkSpec.url = sb.toString();
                    list.add(linkSpec);
                }
                catch (UnsupportedEncodingException ex) {}
            }
        }
        catch (UnsupportedOperationException ex2) {}
    }
    
    private static String makeUrl(@NonNull String transformUrl, @NonNull final String[] array, final Matcher matcher, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (linkify$TransformFilter != null) {
            transformUrl = linkify$TransformFilter.transformUrl(matcher, transformUrl);
        }
        final boolean b = false;
        int n = 0;
        boolean b2;
        String string;
        while (true) {
            b2 = b;
            string = transformUrl;
            if (n >= array.length) {
                break;
            }
            if (transformUrl.regionMatches(true, 0, array[n], 0, array[n].length())) {
                b2 = true;
                if (!transformUrl.regionMatches(false, 0, array[n], 0, array[n].length())) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(array[n]);
                    sb.append(transformUrl.substring(array[n].length()));
                    string = sb.toString();
                    break;
                }
                string = transformUrl;
                break;
            }
            else {
                ++n;
            }
        }
        if (!b2 && array.length > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(array[0]);
            sb2.append(string);
            return sb2.toString();
        }
        return string;
    }
    
    private static final void pruneOverlaps(final ArrayList<LinkSpec> list, final Spannable spannable) {
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = 0; i < array.length; ++i) {
            final LinkSpec linkSpec = new LinkSpec();
            linkSpec.frameworkAddedSpan = array[i];
            linkSpec.start = spannable.getSpanStart((Object)array[i]);
            linkSpec.end = spannable.getSpanEnd((Object)array[i]);
            list.add(linkSpec);
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)LinkifyCompat.COMPARATOR);
        int size = list.size();
        int j = 0;
        while (j < size - 1) {
            final LinkSpec linkSpec2 = list.get(j);
            final LinkSpec linkSpec3 = list.get(j + 1);
            int n = -1;
            if (linkSpec2.start <= linkSpec3.start && linkSpec2.end > linkSpec3.start) {
                if (linkSpec3.end <= linkSpec2.end) {
                    n = j + 1;
                }
                else if (linkSpec2.end - linkSpec2.start > linkSpec3.end - linkSpec3.start) {
                    n = j + 1;
                }
                else if (linkSpec2.end - linkSpec2.start < linkSpec3.end - linkSpec3.start) {
                    n = j;
                }
                if (n != -1) {
                    final URLSpan frameworkAddedSpan = list.get(n).frameworkAddedSpan;
                    if (frameworkAddedSpan != null) {
                        spannable.removeSpan((Object)frameworkAddedSpan);
                    }
                    list.remove(n);
                    --size;
                    continue;
                }
            }
            ++j;
        }
    }
    
    private static class LinkSpec
    {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;
        
        LinkSpec() {
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface LinkifyMask {
    }
}
