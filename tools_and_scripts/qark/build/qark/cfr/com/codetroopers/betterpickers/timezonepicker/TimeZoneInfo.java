/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.Spannable
 *  android.text.Spannable$Factory
 *  android.text.format.DateUtils
 *  android.text.format.Time
 *  android.text.style.ForegroundColorSpan
 *  android.util.Log
 *  android.util.SparseArray
 */
package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import com.codetroopers.betterpickers.timezonepicker.TimeZonePickerUtils;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneInfo
implements Comparable<TimeZoneInfo> {
    private static final int DST_SYMBOL_COLOR = -4210753;
    private static final int GMT_TEXT_COLOR = -7829368;
    public static int NUM_OF_TRANSITIONS = 0;
    private static final char SEPARATOR = ',';
    private static final String TAG = null;
    public static boolean is24HourFormat;
    private static Formatter mFormatter;
    private static SparseArray<CharSequence> mGmtDisplayNameCache;
    private static long mGmtDisplayNameUpdateTime;
    private static StringBuilder mSB;
    private static final Spannable.Factory mSpannableFactory;
    public static long time;
    public int groupId;
    public String mCountry;
    public String mDisplayName;
    SparseArray<String> mLocalTimeCache = new SparseArray();
    long mLocalTimeCacheReferenceTime = 0L;
    int mRawoffset;
    public long[] mTransitions;
    TimeZone mTz;
    public String mTzId;
    private Time recycledTime = new Time();

    static {
        NUM_OF_TRANSITIONS = 6;
        time = System.currentTimeMillis() / 1000L;
        mSpannableFactory = Spannable.Factory.getInstance();
        mSB = new StringBuilder(50);
        mFormatter = new Formatter(mSB, Locale.getDefault());
        mGmtDisplayNameCache = new SparseArray();
    }

    public TimeZoneInfo(TimeZone timeZone, String string2) {
        this.mTz = timeZone;
        this.mTzId = timeZone.getID();
        this.mCountry = string2;
        this.mRawoffset = timeZone.getRawOffset();
        try {
            this.mTransitions = TimeZoneInfo.getTransitions(timeZone, time);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
    }

    public static long[] copyFromIntArray(int[] arrn) {
        if (arrn == null) {
            return new long[0];
        }
        long[] arrl = new long[arrn.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrl[i] = arrn[i];
        }
        return arrl;
    }

    private static String formatTime(DateFormat dateFormat, int n) {
        return dateFormat.format(new Date((long)n * 1000L));
    }

    private static long[] getTransitions(TimeZone arrl, long l) throws IllegalAccessException, NoSuchFieldException {
        long[] arrl2 = arrl.getClass().getDeclaredField("mTransitions");
        arrl2.setAccessible(true);
        long[] arrl3 = null;
        arrl = Build.VERSION.SDK_INT >= 23 ? (long[])arrl2.get(arrl) : TimeZoneInfo.copyFromIntArray((int[])arrl2.get(arrl));
        if (arrl.length != 0) {
            arrl2 = new long[NUM_OF_TRANSITIONS];
            int n = 0;
            int n2 = 0;
            do {
                arrl3 = arrl2;
                if (n2 >= arrl.length) break;
                if (arrl[n2] >= l) {
                    int n3 = n + 1;
                    arrl2[n] = arrl[n2];
                    if (n3 == NUM_OF_TRANSITIONS) {
                        return arrl2;
                    }
                    n = n3;
                }
                ++n2;
            } while (true);
        }
        return arrl3;
    }

    @Override
    public int compareTo(TimeZoneInfo timeZoneInfo) {
        StringBuilder stringBuilder;
        if (this.getNowOffsetMillis() != timeZoneInfo.getNowOffsetMillis()) {
            if (timeZoneInfo.getNowOffsetMillis() < this.getNowOffsetMillis()) {
                return -1;
            }
            return 1;
        }
        if (this.mCountry == null && timeZoneInfo.mCountry != null) {
            return 1;
        }
        String string2 = timeZoneInfo.mCountry;
        if (string2 == null) {
            return -1;
        }
        int n = this.mCountry.compareTo(string2);
        if (n != 0) {
            return n;
        }
        if (Arrays.equals(this.mTransitions, timeZoneInfo.mTransitions)) {
            string2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Not expected to be comparing tz with the same country, same offset, same dst, same transitions:\n");
            stringBuilder.append(this.toString());
            stringBuilder.append("\n");
            stringBuilder.append(timeZoneInfo.toString());
            Log.e((String)string2, (String)stringBuilder.toString());
        }
        if ((string2 = this.mDisplayName) != null && (stringBuilder = timeZoneInfo.mDisplayName) != null) {
            return string2.compareTo((String)((Object)stringBuilder));
        }
        return this.mTz.getDisplayName(Locale.getDefault()).compareTo(timeZoneInfo.mTz.getDisplayName(Locale.getDefault()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CharSequence getGmtDisplayName(Context context) {
        synchronized (this) {
            CharSequence charSequence;
            long l = System.currentTimeMillis() / 60000L;
            long l2 = 60000L * l;
            int n = this.mTz.getOffset(l2);
            boolean bl = this.mTz.useDaylightTime();
            int n2 = bl ? (int)((long)n + 129600000L) : (int)((long)n - 129600000L);
            if (mGmtDisplayNameUpdateTime != l) {
                mGmtDisplayNameUpdateTime = l;
                mGmtDisplayNameCache.clear();
                charSequence = null;
            } else {
                charSequence = (CharSequence)mGmtDisplayNameCache.get(n2);
            }
            CharSequence charSequence2 = charSequence;
            if (charSequence == null) {
                mSB.setLength(0);
                int n3 = 524288 | 1;
                if (is24HourFormat) {
                    n3 |= 128;
                }
                DateUtils.formatDateRange((Context)context, (Formatter)mFormatter, (long)l2, (long)l2, (int)n3, (String)this.mTzId);
                mSB.append("  ");
                int n4 = mSB.length();
                TimeZonePickerUtils.appendGmtOffset(mSB, n);
                int n5 = mSB.length();
                n3 = 0;
                n = 0;
                if (bl) {
                    mSB.append(' ');
                    n3 = mSB.length();
                    mSB.append(TimeZonePickerUtils.getDstSymbol());
                    n = mSB.length();
                }
                charSequence2 = mSpannableFactory.newSpannable((CharSequence)mSB);
                charSequence2.setSpan((Object)new ForegroundColorSpan(-7829368), n4, n5, 33);
                if (bl) {
                    charSequence2.setSpan((Object)new ForegroundColorSpan(-4210753), n3, n, 33);
                }
                mGmtDisplayNameCache.put(n2, (Object)charSequence2);
            }
            return charSequence2;
        }
    }

    public int getLocalHr(long l) {
        this.recycledTime.timezone = this.mTzId;
        this.recycledTime.set(l);
        return this.recycledTime.hour;
    }

    public String getLocalTime(long l) {
        this.recycledTime.timezone = TimeZone.getDefault().getID();
        this.recycledTime.set(l);
        int n = this.recycledTime.year;
        int n2 = this.recycledTime.yearDay;
        this.recycledTime.timezone = this.mTzId;
        this.recycledTime.set(l);
        String string2 = null;
        int n3 = this.recycledTime.hour * 60 + this.recycledTime.minute;
        if (this.mLocalTimeCacheReferenceTime != l) {
            this.mLocalTimeCacheReferenceTime = l;
            this.mLocalTimeCache.clear();
        } else {
            string2 = (String)this.mLocalTimeCache.get(n3);
        }
        String string3 = string2;
        if (string2 == null) {
            string2 = "%I:%M %p";
            if (n * 366 + n2 != this.recycledTime.year * 366 + this.recycledTime.yearDay) {
                string2 = is24HourFormat ? "%b %d %H:%M" : "%b %d %I:%M %p";
            } else if (is24HourFormat) {
                string2 = "%H:%M";
            }
            string3 = this.recycledTime.format(string2);
            this.mLocalTimeCache.put(n3, (Object)string3);
        }
        return string3;
    }

    public int getNowOffsetMillis() {
        return this.mTz.getOffset(System.currentTimeMillis());
    }

    public boolean hasSameRules(TimeZoneInfo timeZoneInfo) {
        if (this.mRawoffset == timeZoneInfo.mRawoffset && Arrays.equals(this.mTransitions, timeZoneInfo.mTransitions)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String string2 = this.mCountry;
        TimeZone timeZone = this.mTz;
        stringBuilder.append(this.mTzId);
        stringBuilder.append(',');
        stringBuilder.append(timeZone.getDisplayName(false, 1));
        stringBuilder.append(',');
        stringBuilder.append(timeZone.getDisplayName(false, 0));
        stringBuilder.append(',');
        if (timeZone.useDaylightTime()) {
            stringBuilder.append(timeZone.getDisplayName(true, 1));
            stringBuilder.append(',');
            stringBuilder.append(timeZone.getDisplayName(true, 0));
        } else {
            stringBuilder.append(',');
        }
        stringBuilder.append(',');
        stringBuilder.append((float)timeZone.getRawOffset() / 3600000.0f);
        stringBuilder.append(',');
        stringBuilder.append((float)timeZone.getDSTSavings() / 3600000.0f);
        stringBuilder.append(',');
        stringBuilder.append(string2);
        stringBuilder.append(',');
        stringBuilder.append(this.getLocalTime(1357041600000L));
        stringBuilder.append(',');
        stringBuilder.append(this.getLocalTime(1363348800000L));
        stringBuilder.append(',');
        stringBuilder.append(this.getLocalTime(1372680000000L));
        stringBuilder.append(',');
        stringBuilder.append(this.getLocalTime(1383307200000L));
        stringBuilder.append(',');
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }
}

