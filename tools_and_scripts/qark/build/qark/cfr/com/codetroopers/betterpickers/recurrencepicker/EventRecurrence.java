/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.text.format.Time
 *  android.util.Log
 */
package com.codetroopers.betterpickers.recurrencepicker;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import java.util.HashMap;

public class EventRecurrence {
    private static final boolean ALLOW_LOWER_CASE = true;
    public static final int DAILY = 4;
    public static final int FR = 2097152;
    public static final int HOURLY = 3;
    public static final int MINUTELY = 2;
    public static final int MO = 131072;
    public static final int MONTHLY = 6;
    private static final boolean ONLY_ONE_UNTIL_COUNT = false;
    private static final int PARSED_BYDAY = 128;
    private static final int PARSED_BYHOUR = 64;
    private static final int PARSED_BYMINUTE = 32;
    private static final int PARSED_BYMONTH = 2048;
    private static final int PARSED_BYMONTHDAY = 256;
    private static final int PARSED_BYSECOND = 16;
    private static final int PARSED_BYSETPOS = 4096;
    private static final int PARSED_BYWEEKNO = 1024;
    private static final int PARSED_BYYEARDAY = 512;
    private static final int PARSED_COUNT = 4;
    private static final int PARSED_FREQ = 1;
    private static final int PARSED_INTERVAL = 8;
    private static final int PARSED_UNTIL = 2;
    private static final int PARSED_WKST = 8192;
    public static final int SA = 4194304;
    public static final int SECONDLY = 1;
    public static final int SU = 65536;
    private static String TAG = "EventRecur";
    public static final int TH = 1048576;
    public static final int TU = 262144;
    private static final boolean VALIDATE_UNTIL = false;
    public static final int WE = 524288;
    public static final int WEEKLY = 5;
    public static final int YEARLY = 7;
    private static final HashMap<String, Integer> sParseFreqMap;
    private static HashMap<String, PartParser> sParsePartMap;
    private static final HashMap<String, Integer> sParseWeekdayMap;
    public int[] byday;
    public int bydayCount;
    public int[] bydayNum;
    public int[] byhour;
    public int byhourCount;
    public int[] byminute;
    public int byminuteCount;
    public int[] bymonth;
    public int bymonthCount;
    public int[] bymonthday;
    public int bymonthdayCount;
    public int[] bysecond;
    public int bysecondCount;
    public int[] bysetpos;
    public int bysetposCount;
    public int[] byweekno;
    public int byweeknoCount;
    public int[] byyearday;
    public int byyeardayCount;
    public int count;
    public int freq;
    public int interval;
    public Time startDate;
    public String until;
    public int wkst;

    static {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        sParsePartMap = hashMap;
        hashMap.put("FREQ", new ParseFreq());
        sParsePartMap.put("UNTIL", new ParseUntil());
        sParsePartMap.put("COUNT", new ParseCount());
        sParsePartMap.put("INTERVAL", new ParseInterval());
        sParsePartMap.put("BYSECOND", new ParseBySecond());
        sParsePartMap.put("BYMINUTE", new ParseByMinute());
        sParsePartMap.put("BYHOUR", new ParseByHour());
        sParsePartMap.put("BYDAY", new ParseByDay());
        sParsePartMap.put("BYMONTHDAY", new ParseByMonthDay());
        sParsePartMap.put("BYYEARDAY", new ParseByYearDay());
        sParsePartMap.put("BYWEEKNO", new ParseByWeekNo());
        sParsePartMap.put("BYMONTH", new ParseByMonth());
        sParsePartMap.put("BYSETPOS", new ParseBySetPos());
        sParsePartMap.put("WKST", new ParseWkst());
        hashMap = new HashMap();
        sParseFreqMap = hashMap;
        hashMap.put("SECONDLY", 1);
        sParseFreqMap.put("MINUTELY", 2);
        sParseFreqMap.put("HOURLY", 3);
        sParseFreqMap.put("DAILY", 4);
        sParseFreqMap.put("WEEKLY", 5);
        sParseFreqMap.put("MONTHLY", 6);
        sParseFreqMap.put("YEARLY", 7);
        hashMap = new HashMap();
        sParseWeekdayMap = hashMap;
        hashMap.put("SU", 65536);
        sParseWeekdayMap.put("MO", 131072);
        sParseWeekdayMap.put("TU", 262144);
        sParseWeekdayMap.put("WE", 524288);
        sParseWeekdayMap.put("TH", 1048576);
        sParseWeekdayMap.put("FR", 2097152);
        sParseWeekdayMap.put("SA", 4194304);
    }

    private void appendByDay(StringBuilder stringBuilder, int n) {
        int n2 = this.bydayNum[n];
        if (n2 != 0) {
            stringBuilder.append(n2);
        }
        stringBuilder.append(EventRecurrence.day2String(this.byday[n]));
    }

    private static void appendNumbers(StringBuilder stringBuilder, String string2, int n, int[] arrn) {
        if (n > 0) {
            stringBuilder.append(string2);
            int n2 = n - 1;
            for (n = 0; n < n2; ++n) {
                stringBuilder.append(arrn[n]);
                stringBuilder.append(",");
            }
            stringBuilder.append(arrn[n2]);
        }
    }

    private static boolean arraysEqual(int[] arrn, int n, int[] arrn2, int n2) {
        if (n != n2) {
            return false;
        }
        for (n2 = 0; n2 < n; ++n2) {
            if (arrn[n2] == arrn2[n2]) continue;
            return false;
        }
        return true;
    }

    public static int calendarDay2Day(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("bad day of week: ");
                stringBuilder.append(n);
                throw new RuntimeException(stringBuilder.toString());
            }
            case 7: {
                return 4194304;
            }
            case 6: {
                return 2097152;
            }
            case 5: {
                return 1048576;
            }
            case 4: {
                return 524288;
            }
            case 3: {
                return 262144;
            }
            case 2: {
                return 131072;
            }
            case 1: 
        }
        return 65536;
    }

    public static int day2CalendarDay(int n) {
        if (n != 65536) {
            if (n != 131072) {
                if (n != 262144) {
                    if (n != 524288) {
                        if (n != 1048576) {
                            if (n != 2097152) {
                                if (n == 4194304) {
                                    return 7;
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("bad day of week: ");
                                stringBuilder.append(n);
                                throw new RuntimeException(stringBuilder.toString());
                            }
                            return 6;
                        }
                        return 5;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
        return 1;
    }

    private static String day2String(int n) {
        if (n != 65536) {
            if (n != 131072) {
                if (n != 262144) {
                    if (n != 524288) {
                        if (n != 1048576) {
                            if (n != 2097152) {
                                if (n == 4194304) {
                                    return "SA";
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("bad day argument: ");
                                stringBuilder.append(n);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                            return "FR";
                        }
                        return "TH";
                    }
                    return "WE";
                }
                return "TU";
            }
            return "MO";
        }
        return "SU";
    }

    public static int day2TimeDay(int n) {
        if (n != 65536) {
            if (n != 131072) {
                if (n != 262144) {
                    if (n != 524288) {
                        if (n != 1048576) {
                            if (n != 2097152) {
                                if (n == 4194304) {
                                    return 6;
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("bad day of week: ");
                                stringBuilder.append(n);
                                throw new RuntimeException(stringBuilder.toString());
                            }
                            return 5;
                        }
                        return 4;
                    }
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    private void resetFields() {
        this.until = null;
        this.bysetposCount = 0;
        this.bymonthCount = 0;
        this.byweeknoCount = 0;
        this.byyeardayCount = 0;
        this.bymonthdayCount = 0;
        this.bydayCount = 0;
        this.byhourCount = 0;
        this.byminuteCount = 0;
        this.bysecondCount = 0;
        this.interval = 0;
        this.count = 0;
        this.freq = 0;
    }

    public static int timeDay2Day(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("bad day of week: ");
                stringBuilder.append(n);
                throw new RuntimeException(stringBuilder.toString());
            }
            case 6: {
                return 4194304;
            }
            case 5: {
                return 2097152;
            }
            case 4: {
                return 1048576;
            }
            case 3: {
                return 524288;
            }
            case 2: {
                return 262144;
            }
            case 1: {
                return 131072;
            }
            case 0: 
        }
        return 65536;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventRecurrence)) {
            return false;
        }
        object = (EventRecurrence)object;
        Object object2 = this.startDate;
        if ((object2 == null ? object.startDate == null : Time.compare((Time)object2, (Time)object.startDate) == 0) && this.freq == object.freq && ((object2 = this.until) == null ? object.until == null : object2.equals(object.until)) && this.count == object.count && this.interval == object.interval && this.wkst == object.wkst && EventRecurrence.arraysEqual(this.bysecond, this.bysecondCount, object.bysecond, object.bysecondCount) && EventRecurrence.arraysEqual(this.byminute, this.byminuteCount, object.byminute, object.byminuteCount) && EventRecurrence.arraysEqual(this.byhour, this.byhourCount, object.byhour, object.byhourCount) && EventRecurrence.arraysEqual(this.byday, this.bydayCount, object.byday, object.bydayCount) && EventRecurrence.arraysEqual(this.bydayNum, this.bydayCount, object.bydayNum, object.bydayCount) && EventRecurrence.arraysEqual(this.bymonthday, this.bymonthdayCount, object.bymonthday, object.bymonthdayCount) && EventRecurrence.arraysEqual(this.byyearday, this.byyeardayCount, object.byyearday, object.byyeardayCount) && EventRecurrence.arraysEqual(this.byweekno, this.byweeknoCount, object.byweekno, object.byweeknoCount) && EventRecurrence.arraysEqual(this.bymonth, this.bymonthCount, object.bymonth, object.bymonthCount) && EventRecurrence.arraysEqual(this.bysetpos, this.bysetposCount, object.bysetpos, object.bysetposCount)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public void parse(String charSequence) {
        String string2;
        this.resetFields();
        int n = 0;
        for (String string3 : charSequence.toUpperCase().split(";")) {
            if (TextUtils.isEmpty((CharSequence)string3)) continue;
            int n2 = string3.indexOf(61);
            if (n2 > 0) {
                string2 = string3.substring(0, n2);
                String string4 = string3.substring(n2 + 1);
                if (string4.length() != 0) {
                    PartParser partParser = sParsePartMap.get(string2);
                    if (partParser == null) {
                        if (string2.startsWith("X-")) continue;
                        charSequence = new StringBuilder();
                        charSequence.append("Couldn't find parser for ");
                        charSequence.append(string2);
                        throw new InvalidFormatException(charSequence.toString());
                    }
                    n2 = partParser.parsePart(string4, this);
                    if ((n & n2) == 0) {
                        n |= n2;
                        continue;
                    }
                    charSequence = new StringBuilder();
                    charSequence.append("Part ");
                    charSequence.append(string2);
                    charSequence.append(" was specified twice");
                    throw new InvalidFormatException(charSequence.toString());
                }
                charSequence = new StringBuilder();
                charSequence.append("Missing RHS in ");
                charSequence.append(string3);
                throw new InvalidFormatException(charSequence.toString());
            }
            charSequence = new StringBuilder();
            charSequence.append("Missing LHS in ");
            charSequence.append(string3);
            throw new InvalidFormatException(charSequence.toString());
        }
        if ((n & 8192) == 0) {
            this.wkst = 131072;
        }
        if ((n & 1) != 0) {
            if ((n & 6) == 6) {
                string2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Warning: rrule has both UNTIL and COUNT: ");
                stringBuilder.append((String)charSequence);
                Log.w((String)string2, (String)stringBuilder.toString());
            }
            return;
        }
        throw new InvalidFormatException("Must specify a FREQ value");
    }

    public boolean repeatsMonthlyOnDayCount() {
        if (this.freq != 6) {
            return false;
        }
        if (this.bydayCount == 1) {
            if (this.bymonthdayCount != 0) {
                return false;
            }
            if (this.bydayNum[0] <= 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean repeatsOnEveryWeekDay() {
        if (this.freq != 5) {
            return false;
        }
        int n = this.bydayCount;
        if (n != 5) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            int n2 = this.byday[i];
            if (n2 != 65536) {
                if (n2 != 4194304) continue;
                return false;
            }
            return false;
        }
        return true;
    }

    public void setStartDate(Time time) {
        this.startDate = time;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FREQ=");
        switch (this.freq) {
            default: {
                break;
            }
            case 7: {
                stringBuilder.append("YEARLY");
                break;
            }
            case 6: {
                stringBuilder.append("MONTHLY");
                break;
            }
            case 5: {
                stringBuilder.append("WEEKLY");
                break;
            }
            case 4: {
                stringBuilder.append("DAILY");
                break;
            }
            case 3: {
                stringBuilder.append("HOURLY");
                break;
            }
            case 2: {
                stringBuilder.append("MINUTELY");
                break;
            }
            case 1: {
                stringBuilder.append("SECONDLY");
            }
        }
        if (!TextUtils.isEmpty((CharSequence)this.until)) {
            stringBuilder.append(";UNTIL=");
            stringBuilder.append(this.until);
        }
        if (this.count != 0) {
            stringBuilder.append(";COUNT=");
            stringBuilder.append(this.count);
        }
        if (this.interval != 0) {
            stringBuilder.append(";INTERVAL=");
            stringBuilder.append(this.interval);
        }
        if (this.wkst != 0) {
            stringBuilder.append(";WKST=");
            stringBuilder.append(EventRecurrence.day2String(this.wkst));
        }
        EventRecurrence.appendNumbers(stringBuilder, ";BYSECOND=", this.bysecondCount, this.bysecond);
        EventRecurrence.appendNumbers(stringBuilder, ";BYMINUTE=", this.byminuteCount, this.byminute);
        EventRecurrence.appendNumbers(stringBuilder, ";BYHOUR=", this.byhourCount, this.byhour);
        int n = this.bydayCount;
        if (n > 0) {
            stringBuilder.append(";BYDAY=");
            int n2 = n - 1;
            for (n = 0; n < n2; ++n) {
                this.appendByDay(stringBuilder, n);
                stringBuilder.append(",");
            }
            this.appendByDay(stringBuilder, n2);
        }
        EventRecurrence.appendNumbers(stringBuilder, ";BYMONTHDAY=", this.bymonthdayCount, this.bymonthday);
        EventRecurrence.appendNumbers(stringBuilder, ";BYYEARDAY=", this.byyeardayCount, this.byyearday);
        EventRecurrence.appendNumbers(stringBuilder, ";BYWEEKNO=", this.byweeknoCount, this.byweekno);
        EventRecurrence.appendNumbers(stringBuilder, ";BYMONTH=", this.bymonthCount, this.bymonth);
        EventRecurrence.appendNumbers(stringBuilder, ";BYSETPOS=", this.bysetposCount, this.bysetpos);
        return stringBuilder.toString();
    }

    public static class InvalidFormatException
    extends RuntimeException {
        InvalidFormatException(String string2) {
            super(string2);
        }
    }

    private static class ParseByDay
    extends PartParser {
        private ParseByDay() {
        }

        private static void parseWday(String string2, int[] object, int[] object2, int n) {
            int n2 = string2.length() - 2;
            if (n2 > 0) {
                object2[n] = ParseByDay.parseIntRange(string2.substring(0, n2), -53, 53, false);
                object2 = string2.substring(n2);
            } else {
                object2 = string2;
            }
            object2 = (Integer)sParseWeekdayMap.get(object2);
            if (object2 != null) {
                object[n] = object2.intValue();
                return;
            }
            object = new StringBuilder();
            object.append("Invalid BYDAY value: ");
            object.append(string2);
            throw new InvalidFormatException(object.toString());
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            int n;
            int[] arrn2;
            if (arrn.indexOf(",") < 0) {
                n = 1;
                int[] arrn3 = new int[1];
                arrn2 = new int[1];
                ParseByDay.parseWday((String)arrn, arrn3, arrn2, 0);
                arrn = arrn3;
            } else {
                String[] arrstring = arrn.split(",");
                int n2 = arrstring.length;
                arrn = new int[n2];
                arrn2 = new int[n2];
                for (n = 0; n < n2; ++n) {
                    ParseByDay.parseWday(arrstring[n], arrn, arrn2, n);
                }
                n = n2;
            }
            eventRecurrence.byday = arrn;
            eventRecurrence.bydayNum = arrn2;
            eventRecurrence.bydayCount = n;
            return 128;
        }
    }

    private static class ParseByHour
    extends PartParser {
        private ParseByHour() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByHour.parseNumberList((String)arrn, 0, 23, true);
            eventRecurrence.byhour = arrn;
            eventRecurrence.byhourCount = arrn.length;
            return 64;
        }
    }

    private static class ParseByMinute
    extends PartParser {
        private ParseByMinute() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByMinute.parseNumberList((String)arrn, 0, 59, true);
            eventRecurrence.byminute = arrn;
            eventRecurrence.byminuteCount = arrn.length;
            return 32;
        }
    }

    private static class ParseByMonth
    extends PartParser {
        private ParseByMonth() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByMonth.parseNumberList((String)arrn, 1, 12, false);
            eventRecurrence.bymonth = arrn;
            eventRecurrence.bymonthCount = arrn.length;
            return 2048;
        }
    }

    private static class ParseByMonthDay
    extends PartParser {
        private ParseByMonthDay() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByMonthDay.parseNumberList((String)arrn, -31, 31, false);
            eventRecurrence.bymonthday = arrn;
            eventRecurrence.bymonthdayCount = arrn.length;
            return 256;
        }
    }

    private static class ParseBySecond
    extends PartParser {
        private ParseBySecond() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseBySecond.parseNumberList((String)arrn, 0, 59, true);
            eventRecurrence.bysecond = arrn;
            eventRecurrence.bysecondCount = arrn.length;
            return 16;
        }
    }

    private static class ParseBySetPos
    extends PartParser {
        private ParseBySetPos() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseBySetPos.parseNumberList((String)arrn, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            eventRecurrence.bysetpos = arrn;
            eventRecurrence.bysetposCount = arrn.length;
            return 4096;
        }
    }

    private static class ParseByWeekNo
    extends PartParser {
        private ParseByWeekNo() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByWeekNo.parseNumberList((String)arrn, -53, 53, false);
            eventRecurrence.byweekno = arrn;
            eventRecurrence.byweeknoCount = arrn.length;
            return 1024;
        }
    }

    private static class ParseByYearDay
    extends PartParser {
        private ParseByYearDay() {
        }

        @Override
        public int parsePart(String arrn, EventRecurrence eventRecurrence) {
            arrn = ParseByYearDay.parseNumberList((String)arrn, -366, 366, false);
            eventRecurrence.byyearday = arrn;
            eventRecurrence.byyeardayCount = arrn.length;
            return 512;
        }
    }

    private static class ParseCount
    extends PartParser {
        private ParseCount() {
        }

        @Override
        public int parsePart(String string2, EventRecurrence eventRecurrence) {
            eventRecurrence.count = ParseCount.parseIntRange(string2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (eventRecurrence.count < 0) {
                String string3 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid Count. Forcing COUNT to 1 from ");
                stringBuilder.append(string2);
                Log.d((String)string3, (String)stringBuilder.toString());
                eventRecurrence.count = 1;
            }
            return 4;
        }
    }

    private static class ParseFreq
    extends PartParser {
        private ParseFreq() {
        }

        @Override
        public int parsePart(String string2, EventRecurrence object) {
            Integer n = (Integer)sParseFreqMap.get(string2);
            if (n != null) {
                object.freq = n;
                return 1;
            }
            object = new StringBuilder();
            object.append("Invalid FREQ value: ");
            object.append(string2);
            throw new InvalidFormatException(object.toString());
        }
    }

    private static class ParseInterval
    extends PartParser {
        private ParseInterval() {
        }

        @Override
        public int parsePart(String string2, EventRecurrence eventRecurrence) {
            eventRecurrence.interval = ParseInterval.parseIntRange(string2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if (eventRecurrence.interval < 1) {
                String string3 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid Interval. Forcing INTERVAL to 1 from ");
                stringBuilder.append(string2);
                Log.d((String)string3, (String)stringBuilder.toString());
                eventRecurrence.interval = 1;
            }
            return 8;
        }
    }

    private static class ParseUntil
    extends PartParser {
        private ParseUntil() {
        }

        @Override
        public int parsePart(String string2, EventRecurrence eventRecurrence) {
            eventRecurrence.until = string2;
            return 2;
        }
    }

    private static class ParseWkst
    extends PartParser {
        private ParseWkst() {
        }

        @Override
        public int parsePart(String string2, EventRecurrence object) {
            Integer n = (Integer)sParseWeekdayMap.get(string2);
            if (n != null) {
                object.wkst = n;
                return 8192;
            }
            object = new StringBuilder();
            object.append("Invalid WKST value: ");
            object.append(string2);
            throw new InvalidFormatException(object.toString());
        }
    }

    static abstract class PartParser {
        PartParser() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public static int parseIntRange(String string2, int n, int n2, boolean bl) {
            void var2_6;
            String string3;
            void var1_5;
            String string4;
            block10 : {
                string3 = string2;
                string4 = string2;
                try {
                    if (string2.charAt(0) != '+') break block10;
                    string4 = string2;
                }
                catch (NumberFormatException numberFormatException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid integer value: ");
                    stringBuilder.append(string4);
                    throw new InvalidFormatException(stringBuilder.toString());
                }
                string3 = string2.substring(1);
            }
            string4 = string3;
            int n3 = Integer.parseInt(string3);
            if (n3 >= var1_5 && n3 <= var2_6) {
                void var3_7;
                if (n3 != 0) return n3;
                if (var3_7 != false) {
                    return n3;
                }
            }
            string4 = string3;
            StringBuilder stringBuilder = new StringBuilder();
            string4 = string3;
            stringBuilder.append("Integer value out of range: ");
            string4 = string3;
            stringBuilder.append(string3);
            string4 = string3;
            throw new InvalidFormatException(stringBuilder.toString());
        }

        public static int[] parseNumberList(String arrstring, int n, int n2, boolean bl) {
            if (arrstring.indexOf(",") < 0) {
                return new int[]{PartParser.parseIntRange((String)arrstring, n, n2, bl)};
            }
            arrstring = arrstring.split(",");
            int n3 = arrstring.length;
            int[] arrn = new int[n3];
            for (int i = 0; i < n3; ++i) {
                arrn[i] = PartParser.parseIntRange(arrstring[i], n, n2, bl);
            }
            return arrn;
        }

        public abstract int parsePart(String var1, EventRecurrence var2);
    }

}

