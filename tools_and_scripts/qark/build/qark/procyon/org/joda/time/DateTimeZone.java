// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import java.io.ObjectStreamException;
import java.util.Locale;
import org.joda.convert.ToString;
import java.security.Permission;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.UTCProvider;
import org.joda.time.tz.ZoneInfoProvider;
import java.io.File;
import org.joda.time.tz.DefaultNameProvider;
import java.util.Set;
import java.util.TimeZone;
import org.joda.time.field.FieldUtils;
import org.joda.convert.FromString;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.Provider;
import org.joda.time.tz.NameProvider;
import java.util.concurrent.atomic.AtomicReference;
import java.io.Serializable;

public abstract class DateTimeZone implements Serializable
{
    private static final int MAX_MILLIS = 86399999;
    public static final DateTimeZone UTC;
    private static final AtomicReference<DateTimeZone> cDefault;
    private static final AtomicReference<NameProvider> cNameProvider;
    private static final AtomicReference<Provider> cProvider;
    private static final long serialVersionUID = 5546345482340108586L;
    private final String iID;
    
    static {
        UTC = UTCDateTimeZone.INSTANCE;
        cProvider = new AtomicReference<Provider>();
        cNameProvider = new AtomicReference<NameProvider>();
        cDefault = new AtomicReference<DateTimeZone>();
    }
    
    protected DateTimeZone(final String iid) {
        if (iid != null) {
            this.iID = iid;
            return;
        }
        throw new IllegalArgumentException("Id must not be null");
    }
    
    private static String convertToAsciiNumber(final String s) {
        final StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); ++i) {
            final int digit = Character.digit(sb.charAt(i), 10);
            if (digit >= 0) {
                sb.setCharAt(i, (char)(digit + 48));
            }
        }
        return sb.toString();
    }
    
    private static DateTimeZone fixedOffsetZone(final String s, final int n) {
        if (n == 0) {
            return DateTimeZone.UTC;
        }
        return (DateTimeZone)new FixedDateTimeZone(s, (String)null, n, n);
    }
    
    @FromString
    public static DateTimeZone forID(final String s) {
        if (s == null) {
            return getDefault();
        }
        if (s.equals("UTC")) {
            return DateTimeZone.UTC;
        }
        final DateTimeZone zone = getProvider().getZone(s);
        if (zone != null) {
            return zone;
        }
        if (!s.startsWith("+") && !s.startsWith("-")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("The datetime zone id '");
            sb.append(s);
            sb.append("' is not recognised");
            throw new IllegalArgumentException(sb.toString());
        }
        final int offset = parseOffset(s);
        if (offset == 0L) {
            return DateTimeZone.UTC;
        }
        return fixedOffsetZone(printOffset(offset), offset);
    }
    
    public static DateTimeZone forOffsetHours(final int n) throws IllegalArgumentException {
        return forOffsetHoursMinutes(n, 0);
    }
    
    public static DateTimeZone forOffsetHoursMinutes(int safeMultiply, final int n) throws IllegalArgumentException {
        if (safeMultiply == 0 && n == 0) {
            return DateTimeZone.UTC;
        }
        Label_0154: {
            if (safeMultiply < -23 || safeMultiply > 23) {
                break Label_0154;
            }
            Label_0121: {
                if (n < -59 || n > 59) {
                    break Label_0121;
                }
                if (safeMultiply > 0 && n < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Positive hours must not have negative minutes: ");
                    sb.append(n);
                    throw new IllegalArgumentException(sb.toString());
                }
                safeMultiply *= 60;
                Label_0099: {
                    if (safeMultiply >= 0) {
                        safeMultiply += n;
                        break Label_0099;
                    }
                    while (true) {
                        while (true) {
                            try {
                                safeMultiply -= Math.abs(n);
                                safeMultiply = FieldUtils.safeMultiply(safeMultiply, 60000);
                                return forOffsetMillis(safeMultiply);
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Minutes out of range: ");
                                sb2.append(n);
                                throw new IllegalArgumentException(sb2.toString());
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("Hours out of range: ");
                                sb3.append(safeMultiply);
                                throw new IllegalArgumentException(sb3.toString());
                                throw new IllegalArgumentException("Offset is too large");
                            }
                            catch (ArithmeticException ex) {}
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public static DateTimeZone forOffsetMillis(final int n) {
        if (n >= -86399999 && n <= 86399999) {
            return fixedOffsetZone(printOffset(n), n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Millis out of range: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DateTimeZone forTimeZone(final TimeZone timeZone) {
        if (timeZone == null) {
            return getDefault();
        }
        final String id = timeZone.getID();
        if (id == null) {
            throw new IllegalArgumentException("The TimeZone id must not be null");
        }
        if (id.equals("UTC")) {
            return DateTimeZone.UTC;
        }
        DateTimeZone dateTimeZone = null;
        final String convertedId = getConvertedId(id);
        final Provider provider = getProvider();
        if (convertedId != null) {
            dateTimeZone = provider.getZone(convertedId);
        }
        if (dateTimeZone == null) {
            dateTimeZone = provider.getZone(id);
        }
        if (dateTimeZone != null) {
            return dateTimeZone;
        }
        if (convertedId != null || (id.startsWith("GMT+") || id.startsWith("GMT-"))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("The datetime zone id '");
            sb.append(id);
            sb.append("' is not recognised");
            throw new IllegalArgumentException(sb.toString());
        }
        String s = id.substring(3);
        if (s.length() > 2) {
            final char char1 = s.charAt(1);
            if (char1 > '9' && Character.isDigit(char1)) {
                s = convertToAsciiNumber(s);
            }
        }
        final int offset = parseOffset(s);
        if (offset == 0L) {
            return DateTimeZone.UTC;
        }
        return fixedOffsetZone(printOffset(offset), offset);
    }
    
    public static Set<String> getAvailableIDs() {
        return (Set<String>)getProvider().getAvailableIDs();
    }
    
    private static String getConvertedId(final String s) {
        return DateTimeZone.DateTimeZone$LazyInit.CONVERSION_MAP.get(s);
    }
    
    public static DateTimeZone getDefault() {
        final DateTimeZone dateTimeZone = DateTimeZone.cDefault.get();
        if (dateTimeZone != null) {
            return dateTimeZone;
        }
        DateTimeZone dateTimeZone2 = dateTimeZone;
        try {
            try {
                final String property = System.getProperty("user.timezone");
                if (property != null) {
                    dateTimeZone2 = dateTimeZone;
                    dateTimeZone2 = forID(property);
                }
                else {
                    dateTimeZone2 = dateTimeZone;
                }
            }
            catch (IllegalArgumentException ex) {}
        }
        catch (RuntimeException ex2) {
            final IllegalArgumentException ex;
            dateTimeZone2 = (DateTimeZone)ex;
        }
        if (dateTimeZone2 == null) {
            dateTimeZone2 = forTimeZone(TimeZone.getDefault());
        }
        if (dateTimeZone2 == null) {
            dateTimeZone2 = DateTimeZone.UTC;
        }
        if (!DateTimeZone.cDefault.compareAndSet(null, dateTimeZone2)) {
            return DateTimeZone.cDefault.get();
        }
        return dateTimeZone2;
    }
    
    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider = null;
        final NameProvider nameProvider2 = null;
        try {
            final String property = System.getProperty("org.joda.time.DateTimeZone.NameProvider");
            Label_0043: {
                if (property != null) {
                    try {
                        nameProvider = (NameProvider)Class.forName(property).newInstance();
                        break Label_0043;
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                nameProvider = nameProvider2;
            }
        }
        catch (SecurityException ex2) {}
        if (nameProvider == null) {
            return (NameProvider)new DefaultNameProvider();
        }
        return nameProvider;
    }
    
    private static Provider getDefaultProvider() {
        try {
            final String property = System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (property != null) {
                try {
                    return validateProvider((Provider)Class.forName(property).newInstance());
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        catch (SecurityException ex4) {}
        try {
            final String property2 = System.getProperty("org.joda.time.DateTimeZone.Folder");
            if (property2 != null) {
                try {
                    return validateProvider((Provider)new ZoneInfoProvider(new File(property2)));
                }
                catch (Exception ex2) {
                    throw new RuntimeException(ex2);
                }
            }
        }
        catch (SecurityException ex5) {}
        try {
            return validateProvider((Provider)new ZoneInfoProvider("org/joda/time/tz/data"));
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
            return (Provider)new UTCProvider();
        }
    }
    
    public static NameProvider getNameProvider() {
        final NameProvider nameProvider = DateTimeZone.cNameProvider.get();
        if (nameProvider != null) {
            return nameProvider;
        }
        final NameProvider defaultNameProvider = getDefaultNameProvider();
        if (!DateTimeZone.cNameProvider.compareAndSet(null, defaultNameProvider)) {
            return DateTimeZone.cNameProvider.get();
        }
        return defaultNameProvider;
    }
    
    public static Provider getProvider() {
        final Provider provider = DateTimeZone.cProvider.get();
        if (provider != null) {
            return provider;
        }
        final Provider defaultProvider = getDefaultProvider();
        if (!DateTimeZone.cProvider.compareAndSet(null, defaultProvider)) {
            return DateTimeZone.cProvider.get();
        }
        return defaultProvider;
    }
    
    private static int parseOffset(final String s) {
        return -(int)DateTimeZone.DateTimeZone$LazyInit.OFFSET_FORMATTER.parseMillis(s);
    }
    
    private static String printOffset(int n) {
        final StringBuffer sb = new StringBuffer();
        if (n >= 0) {
            sb.append('+');
        }
        else {
            sb.append('-');
            n = -n;
        }
        final int n2 = n / 3600000;
        FormatUtils.appendPaddedInteger(sb, n2, 2);
        n -= n2 * 3600000;
        final int n3 = n / 60000;
        sb.append(':');
        FormatUtils.appendPaddedInteger(sb, n3, 2);
        n -= n3 * 60000;
        if (n == 0) {
            return sb.toString();
        }
        final int n4 = n / 1000;
        sb.append(':');
        FormatUtils.appendPaddedInteger(sb, n4, 2);
        n -= n4 * 1000;
        if (n == 0) {
            return sb.toString();
        }
        sb.append('.');
        FormatUtils.appendPaddedInteger(sb, n, 3);
        return sb.toString();
    }
    
    public static void setDefault(final DateTimeZone dateTimeZone) throws SecurityException {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setDefault"));
        }
        if (dateTimeZone != null) {
            DateTimeZone.cDefault.set(dateTimeZone);
            return;
        }
        throw new IllegalArgumentException("The datetime zone must not be null");
    }
    
    public static void setNameProvider(NameProvider defaultNameProvider) throws SecurityException {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setNameProvider"));
        }
        if (defaultNameProvider == null) {
            defaultNameProvider = getDefaultNameProvider();
        }
        DateTimeZone.cNameProvider.set(defaultNameProvider);
    }
    
    public static void setProvider(Provider defaultProvider) throws SecurityException {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setProvider"));
        }
        if (defaultProvider == null) {
            defaultProvider = getDefaultProvider();
        }
        else {
            validateProvider(defaultProvider);
        }
        DateTimeZone.cProvider.set(defaultProvider);
    }
    
    private static Provider validateProvider(final Provider provider) {
        final Set availableIDs = provider.getAvailableIDs();
        if (availableIDs == null || availableIDs.size() == 0) {
            throw new IllegalArgumentException("The provider doesn't have any available ids");
        }
        if (!availableIDs.contains("UTC")) {
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        if (DateTimeZone.UTC.equals(provider.getZone("UTC"))) {
            return provider;
        }
        throw new IllegalArgumentException("Invalid UTC zone provided");
    }
    
    public long adjustOffset(final long n, final boolean b) {
        final long n2 = n - 10800000L;
        final long n3 = this.getOffset(n2);
        final long n4 = this.getOffset(10800000L + n);
        if (n3 <= n4) {
            return n;
        }
        final long n5 = n3 - n4;
        final long nextTransition = this.nextTransition(n2);
        final long n6 = nextTransition - n5;
        if (n < n6) {
            return n;
        }
        if (n >= nextTransition + n5) {
            return n;
        }
        if (n - n6 < n5) {
            long n7 = n;
            if (b) {
                n7 = n + n5;
            }
            return n7;
        }
        if (b) {
            return n;
        }
        return n - n5;
    }
    
    public long convertLocalToUTC(final long n, final boolean b) {
        int offset = this.getOffset(n);
        final long n2 = n - offset;
        final int offset2 = this.getOffset(n2);
        Label_0138: {
            if (offset != offset2) {
                if (b || offset < 0) {
                    long nextTransition = this.nextTransition(n2);
                    long n3 = Long.MAX_VALUE;
                    if (nextTransition == n2) {
                        nextTransition = Long.MAX_VALUE;
                    }
                    final long n4 = n - offset2;
                    final long nextTransition2 = this.nextTransition(n4);
                    if (nextTransition2 != n4) {
                        n3 = nextTransition2;
                    }
                    if (nextTransition != n3) {
                        if (!b) {
                            break Label_0138;
                        }
                        throw new IllegalInstantException(n, this.getID());
                    }
                }
            }
            offset = offset2;
        }
        final long n5 = offset;
        final long n6 = n - n5;
        if ((n ^ n6) >= 0L) {
            return n6;
        }
        if ((n ^ n5) >= 0L) {
            return n6;
        }
        throw new ArithmeticException("Subtracting time zone offset caused overflow");
    }
    
    public long convertLocalToUTC(final long n, final boolean b, long n2) {
        final int offset = this.getOffset(n2);
        n2 = n - offset;
        if (this.getOffset(n2) == offset) {
            return n2;
        }
        return this.convertLocalToUTC(n, b);
    }
    
    public long convertUTCToLocal(final long n) {
        final long n2 = this.getOffset(n);
        final long n3 = n + n2;
        if ((n ^ n3) >= 0L) {
            return n3;
        }
        if ((n ^ n2) < 0L) {
            return n3;
        }
        throw new ArithmeticException("Adding time zone offset caused overflow");
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    @ToString
    public final String getID() {
        return this.iID;
    }
    
    public long getMillisKeepLocal(DateTimeZone default1, final long n) {
        if (default1 == null) {
            default1 = getDefault();
        }
        if (default1 == this) {
            return n;
        }
        return default1.convertLocalToUTC(this.convertUTCToLocal(n), false, n);
    }
    
    public final String getName(final long n) {
        return this.getName(n, null);
    }
    
    public String getName(final long n, Locale default1) {
        if (default1 == null) {
            default1 = Locale.getDefault();
        }
        final String nameKey = this.getNameKey(n);
        if (nameKey == null) {
            return this.iID;
        }
        final NameProvider nameProvider = getNameProvider();
        String s;
        if (nameProvider instanceof DefaultNameProvider) {
            s = ((DefaultNameProvider)nameProvider).getName(default1, this.iID, nameKey, this.isStandardOffset(n));
        }
        else {
            s = nameProvider.getName(default1, this.iID, nameKey);
        }
        if (s != null) {
            return s;
        }
        return printOffset(this.getOffset(n));
    }
    
    public abstract String getNameKey(final long p0);
    
    public abstract int getOffset(final long p0);
    
    public final int getOffset(final ReadableInstant readableInstant) {
        if (readableInstant == null) {
            return this.getOffset(DateTimeUtils.currentTimeMillis());
        }
        return this.getOffset(readableInstant.getMillis());
    }
    
    public int getOffsetFromLocal(long n) {
        final int offset = this.getOffset(n);
        final long n2 = n - offset;
        final int offset2 = this.getOffset(n2);
        if (offset != offset2) {
            if (offset - offset2 >= 0) {
                return offset2;
            }
            long nextTransition = this.nextTransition(n2);
            if (nextTransition == n2) {
                nextTransition = Long.MAX_VALUE;
            }
            final long n3 = n - offset2;
            n = this.nextTransition(n3);
            if (n == n3) {
                n = Long.MAX_VALUE;
            }
            if (nextTransition != n) {
                return offset;
            }
            return offset2;
        }
        else {
            if (offset < 0) {
                return offset2;
            }
            n = this.previousTransition(n2);
            if (n >= n2) {
                return offset2;
            }
            final int offset3 = this.getOffset(n);
            if (n2 - n <= offset3 - offset) {
                return offset3;
            }
            return offset2;
        }
    }
    
    public final String getShortName(final long n) {
        return this.getShortName(n, null);
    }
    
    public String getShortName(final long n, Locale default1) {
        if (default1 == null) {
            default1 = Locale.getDefault();
        }
        final String nameKey = this.getNameKey(n);
        if (nameKey == null) {
            return this.iID;
        }
        final NameProvider nameProvider = getNameProvider();
        String s;
        if (nameProvider instanceof DefaultNameProvider) {
            s = ((DefaultNameProvider)nameProvider).getShortName(default1, this.iID, nameKey, this.isStandardOffset(n));
        }
        else {
            s = nameProvider.getShortName(default1, this.iID, nameKey);
        }
        if (s != null) {
            return s;
        }
        return printOffset(this.getOffset(n));
    }
    
    public abstract int getStandardOffset(final long p0);
    
    @Override
    public int hashCode() {
        return this.getID().hashCode() + 57;
    }
    
    public abstract boolean isFixed();
    
    public boolean isLocalDateTimeGap(final LocalDateTime localDateTime) {
        if (this.isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime(this);
            return false;
        }
        catch (IllegalInstantException ex) {
            return true;
        }
    }
    
    public boolean isStandardOffset(final long n) {
        return this.getOffset(n) == this.getStandardOffset(n);
    }
    
    public abstract long nextTransition(final long p0);
    
    public abstract long previousTransition(final long p0);
    
    @Override
    public String toString() {
        return this.getID();
    }
    
    public TimeZone toTimeZone() {
        return TimeZone.getTimeZone(this.iID);
    }
    
    protected Object writeReplace() throws ObjectStreamException {
        return new DateTimeZone.DateTimeZone$Stub(this.iID);
    }
}
