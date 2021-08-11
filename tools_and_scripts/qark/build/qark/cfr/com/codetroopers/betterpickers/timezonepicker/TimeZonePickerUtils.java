/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.Spannable
 *  android.text.Spannable$Factory
 *  android.text.format.Time
 *  android.text.style.ForegroundColorSpan
 *  android.util.Log
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$array
 */
package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.Spannable;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import com.codetroopers.betterpickers.R;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZonePickerUtils {
    public static final int DST_SYMBOL_COLOR = -4210753;
    public static final int GMT_TEXT_COLOR = -7829368;
    private static final String TAG = "TimeZonePickerUtils";
    private static final Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance();
    private Locale mDefaultLocale;
    private String[] mOverrideIds;
    private String[] mOverrideLabels;

    public TimeZonePickerUtils(Context context) {
        this.cacheOverrides(context);
    }

    public static void appendGmtOffset(StringBuilder stringBuilder, int n) {
        stringBuilder.append("GMT");
        if (n < 0) {
            stringBuilder.append('-');
        } else {
            stringBuilder.append('+');
        }
        n = Math.abs(n);
        stringBuilder.append((long)n / 3600000L);
        n = n / 60000 % 60;
        if (n != 0) {
            stringBuilder.append(':');
            if (n < 10) {
                stringBuilder.append('0');
            }
            stringBuilder.append(n);
        }
    }

    private CharSequence buildGmtDisplayName(TimeZone timeZone, long l, boolean bl) {
        Time time = new Time(timeZone.getID());
        time.set(l);
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl2 = time.isDst != 0;
        stringBuilder.append(this.getDisplayName(timeZone, bl2));
        stringBuilder.append("  ");
        int n = timeZone.getOffset(l);
        int n2 = stringBuilder.length();
        TimeZonePickerUtils.appendGmtOffset(stringBuilder, n);
        int n3 = stringBuilder.length();
        n = 0;
        int n4 = 0;
        if (timeZone.useDaylightTime()) {
            stringBuilder.append(" ");
            n = stringBuilder.length();
            stringBuilder.append(TimeZonePickerUtils.getDstSymbol());
            n4 = stringBuilder.length();
        }
        time = mSpannableFactory.newSpannable((CharSequence)stringBuilder);
        if (bl) {
            time.setSpan((Object)new ForegroundColorSpan(-7829368), n2, n3, 33);
        }
        if (timeZone.useDaylightTime()) {
            time.setSpan((Object)new ForegroundColorSpan(-4210753), n, n4, 33);
        }
        return time;
    }

    private void cacheOverrides(Context context) {
        context = context.getResources();
        this.mOverrideIds = context.getStringArray(R.array.timezone_rename_ids);
        this.mOverrideLabels = context.getStringArray(R.array.timezone_rename_labels);
    }

    private String getDisplayName(TimeZone timeZone, boolean bl) {
        if (this.mOverrideIds != null && this.mOverrideLabels != null) {
            for (int i = 0; i < this.mOverrideIds.length; ++i) {
                if (!timeZone.getID().equals(this.mOverrideIds[i])) continue;
                String[] arrstring = this.mOverrideLabels;
                if (arrstring.length > i) {
                    return arrstring[i];
                }
                arrstring = new StringBuilder();
                arrstring.append("timezone_rename_ids len=");
                arrstring.append(this.mOverrideIds.length);
                arrstring.append(" timezone_rename_labels len=");
                arrstring.append(this.mOverrideLabels.length);
                Log.e((String)"TimeZonePickerUtils", (String)arrstring.toString());
                break;
            }
            return timeZone.getDisplayName(bl, 1, Locale.getDefault());
        }
        return timeZone.getDisplayName(bl, 1, Locale.getDefault());
    }

    public static char getDstSymbol() {
        if (Build.VERSION.SDK_INT >= 16) {
            return '\u2600';
        }
        return '*';
    }

    public CharSequence getGmtDisplayName(Context context, String object, long l, boolean bl) {
        if ((object = TimeZone.getTimeZone((String)object)) == null) {
            return null;
        }
        Locale locale = Locale.getDefault();
        if (!locale.equals(this.mDefaultLocale)) {
            this.mDefaultLocale = locale;
            this.cacheOverrides(context);
        }
        return this.buildGmtDisplayName((TimeZone)object, l, bl);
    }
}

