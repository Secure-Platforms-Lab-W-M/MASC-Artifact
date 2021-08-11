// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import java.text.ParsePosition;
import java.util.Date;
import okhttp3.internal.Util;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;

public final class HttpDate
{
    private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS;
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
    public static final long MAX_DATE = 253402300799999L;
    private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT;
    
    static {
        STANDARD_DATE_FORMAT = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                simpleDateFormat.setLenient(false);
                simpleDateFormat.setTimeZone(Util.UTC);
                return simpleDateFormat;
            }
        };
        BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z" };
        BROWSER_COMPATIBLE_DATE_FORMATS = new DateFormat[HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length];
    }
    
    private HttpDate() {
    }
    
    public static String format(final Date date) {
        return HttpDate.STANDARD_DATE_FORMAT.get().format(date);
    }
    
    public static Date parse(final String s) {
        Date parse;
        if (s.length() == 0) {
            parse = null;
        }
        else {
            final ParsePosition parsePosition = new ParsePosition(0);
            parse = HttpDate.STANDARD_DATE_FORMAT.get().parse(s, parsePosition);
            if (parsePosition.getIndex() != s.length()) {
                while (true) {
                    final String[] browser_COMPATIBLE_DATE_FORMAT_STRINGS = HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
                    // monitorenter(browser_COMPATIBLE_DATE_FORMAT_STRINGS)
                    int n = 0;
                    while (true) {
                        try {
                            final int length = HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length;
                            if (n >= length) {
                                break;
                            }
                            DateFormat dateFormat;
                            if ((dateFormat = HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS[n]) == null) {
                                dateFormat = new SimpleDateFormat(HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS[n], Locale.US);
                                dateFormat.setTimeZone(Util.UTC);
                                HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS[n] = dateFormat;
                            }
                            parsePosition.setIndex(0);
                            final Date parse2 = dateFormat.parse(s, parsePosition);
                            if (parsePosition.getIndex() != 0) {
                                return parse2;
                            }
                        }
                        finally {
                        }
                        // monitorexit(browser_COMPATIBLE_DATE_FORMAT_STRINGS)
                        ++n;
                        continue;
                    }
                }
                // monitorexit(browser_COMPATIBLE_DATE_FORMAT_STRINGS)
                return null;
            }
        }
        return parse;
    }
}
