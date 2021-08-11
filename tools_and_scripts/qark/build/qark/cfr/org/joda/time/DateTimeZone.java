/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.convert.ToString
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DateTimeZone$LazyInit
 *  org.joda.time.DateTimeZone$Stub
 *  org.joda.time.IllegalInstantException
 *  org.joda.time.JodaTimePermission
 *  org.joda.time.UTCDateTimeZone
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.FormatUtils
 *  org.joda.time.tz.DefaultNameProvider
 *  org.joda.time.tz.FixedDateTimeZone
 *  org.joda.time.tz.NameProvider
 *  org.joda.time.tz.Provider
 *  org.joda.time.tz.UTCProvider
 *  org.joda.time.tz.ZoneInfoProvider
 */
package org.joda.time;

import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.Permission;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalInstantException;
import org.joda.time.JodaTimePermission;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.UTCDateTimeZone;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;
import org.joda.time.tz.UTCProvider;
import org.joda.time.tz.ZoneInfoProvider;

public abstract class DateTimeZone
implements Serializable {
    private static final int MAX_MILLIS = 86399999;
    public static final DateTimeZone UTC = UTCDateTimeZone.INSTANCE;
    private static final AtomicReference<DateTimeZone> cDefault;
    private static final AtomicReference<NameProvider> cNameProvider;
    private static final AtomicReference<Provider> cProvider;
    private static final long serialVersionUID = 5546345482340108586L;
    private final String iID;

    static {
        cProvider = new AtomicReference();
        cNameProvider = new AtomicReference();
        cDefault = new AtomicReference();
    }

    protected DateTimeZone(String string) {
        if (string != null) {
            this.iID = string;
            return;
        }
        throw new IllegalArgumentException("Id must not be null");
    }

    private static String convertToAsciiNumber(String charSequence) {
        charSequence = new StringBuilder((String)charSequence);
        for (int i = 0; i < charSequence.length(); ++i) {
            int n = Character.digit(charSequence.charAt(i), 10);
            if (n < 0) continue;
            charSequence.setCharAt(i, (char)(n + 48));
        }
        return charSequence.toString();
    }

    private static DateTimeZone fixedOffsetZone(String string, int n) {
        if (n == 0) {
            return UTC;
        }
        return new FixedDateTimeZone(string, null, n, n);
    }

    @FromString
    public static DateTimeZone forID(String string) {
        if (string == null) {
            return DateTimeZone.getDefault();
        }
        if (string.equals("UTC")) {
            return UTC;
        }
        Serializable serializable = DateTimeZone.getProvider().getZone(string);
        if (serializable != null) {
            return serializable;
        }
        if (!string.startsWith("+") && !string.startsWith("-")) {
            serializable = new StringBuilder();
            serializable.append("The datetime zone id '");
            serializable.append(string);
            serializable.append("' is not recognised");
            throw new IllegalArgumentException(serializable.toString());
        }
        int n = DateTimeZone.parseOffset(string);
        if ((long)n == 0L) {
            return UTC;
        }
        return DateTimeZone.fixedOffsetZone(DateTimeZone.printOffset(n), n);
    }

    public static DateTimeZone forOffsetHours(int n) throws IllegalArgumentException {
        return DateTimeZone.forOffsetHoursMinutes(n, 0);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static DateTimeZone forOffsetHoursMinutes(int var0, int var1_1) throws IllegalArgumentException {
        if (var0 == 0 && var1_1 == 0) {
            return DateTimeZone.UTC;
        }
        if (var0 < -23 || var0 > 23) ** GOTO lbl19
        if (var1_1 >= -59 && var1_1 <= 59) {
            if (var0 > 0 && var1_1 < 0) {
                var2_2 = new StringBuilder();
                var2_2.append("Positive hours must not have negative minutes: ");
                var2_2.append(var1_1);
                throw new IllegalArgumentException(var2_2.toString());
            }
            if ((var0 *= 60) < 0) {
                var0 -= Math.abs(var1_1);
            }
        } else {
            var2_3 = new StringBuilder();
            var2_3.append("Minutes out of range: ");
            var2_3.append(var1_1);
            throw new IllegalArgumentException(var2_3.toString());
lbl19: // 1 sources:
            var2_4 = new StringBuilder();
            var2_4.append("Hours out of range: ");
            var2_4.append(var0);
            throw new IllegalArgumentException(var2_4.toString());
            catch (ArithmeticException var2_5) {
                throw new IllegalArgumentException("Offset is too large");
            }
        }
        var0 += var1_1;
        var0 = FieldUtils.safeMultiply((int)var0, (int)60000);
        return DateTimeZone.forOffsetMillis(var0);
    }

    public static DateTimeZone forOffsetMillis(int n) {
        if (n >= -86399999 && n <= 86399999) {
            return DateTimeZone.fixedOffsetZone(DateTimeZone.printOffset(n), n);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Millis out of range: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static DateTimeZone forTimeZone(TimeZone object) {
        if (object == null) {
            return DateTimeZone.getDefault();
        }
        String string = object.getID();
        if (string != null) {
            if (string.equals("UTC")) {
                return UTC;
            }
            object = null;
            String string2 = DateTimeZone.getConvertedId(string);
            Provider provider = DateTimeZone.getProvider();
            if (string2 != null) {
                object = provider.getZone(string2);
            }
            if (object == null) {
                object = provider.getZone(string);
            }
            if (object != null) {
                return object;
            }
            if (string2 == null && (string.startsWith("GMT+") || string.startsWith("GMT-"))) {
                char c;
                int n;
                object = string.substring(3);
                if (object.length() > 2 && (c = object.charAt(1)) > '9' && Character.isDigit(c)) {
                    object = DateTimeZone.convertToAsciiNumber((String)object);
                }
                if ((long)(n = DateTimeZone.parseOffset((String)object)) == 0L) {
                    return UTC;
                }
                return DateTimeZone.fixedOffsetZone(DateTimeZone.printOffset(n), n);
            }
            object = new StringBuilder();
            object.append("The datetime zone id '");
            object.append(string);
            object.append("' is not recognised");
            throw new IllegalArgumentException(object.toString());
        }
        throw new IllegalArgumentException("The TimeZone id must not be null");
    }

    public static Set<String> getAvailableIDs() {
        return DateTimeZone.getProvider().getAvailableIDs();
    }

    private static String getConvertedId(String string) {
        return (String).CONVERSION_MAP.get(string);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static DateTimeZone getDefault() {
        Object object;
        block9 : {
            DateTimeZone dateTimeZone;
            block8 : {
                dateTimeZone = cDefault.get();
                if (dateTimeZone != null) return dateTimeZone;
                object = dateTimeZone;
                try {
                    Object object2 = System.getProperty("user.timezone");
                    if (object2 != null) {
                        object = dateTimeZone;
                        object = object2 = DateTimeZone.forID((String)object2);
                    } else {
                        object = dateTimeZone;
                    }
                    break block8;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    break block9;
                }
                catch (RuntimeException runtimeException) {
                    object = dateTimeZone;
                }
            }
            if (object == null) {
                dateTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault());
                object = dateTimeZone;
            }
        }
        if (object == null) {
            object = UTC;
        }
        if (cDefault.compareAndSet((DateTimeZone)null, (DateTimeZone)object)) return object;
        return cDefault.get();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static NameProvider getDefaultNameProvider() {
        NameProvider nameProvider;
        block6 : {
            nameProvider = null;
            NameProvider nameProvider2 = null;
            try {
                String string = System.getProperty("org.joda.time.DateTimeZone.NameProvider");
                if (string != null) {
                    try {
                        nameProvider = nameProvider2 = (NameProvider)Class.forName(string).newInstance();
                        break block6;
                    }
                    catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }
                nameProvider = nameProvider2;
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        if (nameProvider == null) {
            return new DefaultNameProvider();
        }
        return nameProvider;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Provider getDefaultProvider() {
        String string;
        try {
            string = System.getProperty("org.joda.time.DateTimeZone.Provider");
            if (string != null) {
                try {
                    return DateTimeZone.validateProvider((Provider)Class.forName(string).newInstance());
                }
                catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        try {
            string = System.getProperty("org.joda.time.DateTimeZone.Folder");
            if (string != null) {
                try {
                    return DateTimeZone.validateProvider((Provider)new ZoneInfoProvider(new File(string)));
                }
                catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        try {
            return DateTimeZone.validateProvider((Provider)new ZoneInfoProvider("org/joda/time/tz/data"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new UTCProvider();
        }
    }

    public static NameProvider getNameProvider() {
        NameProvider nameProvider = cNameProvider.get();
        if (nameProvider == null) {
            nameProvider = DateTimeZone.getDefaultNameProvider();
            if (!cNameProvider.compareAndSet((NameProvider)null, nameProvider)) {
                return cNameProvider.get();
            }
            return nameProvider;
        }
        return nameProvider;
    }

    public static Provider getProvider() {
        Provider provider = cProvider.get();
        if (provider == null) {
            provider = DateTimeZone.getDefaultProvider();
            if (!cProvider.compareAndSet((Provider)null, provider)) {
                return cProvider.get();
            }
            return provider;
        }
        return provider;
    }

    private static int parseOffset(String string) {
        return - (int).OFFSET_FORMATTER.parseMillis(string);
    }

    private static String printOffset(int n) {
        StringBuffer stringBuffer = new StringBuffer();
        if (n >= 0) {
            stringBuffer.append('+');
        } else {
            stringBuffer.append('-');
            n = - n;
        }
        int n2 = n / 3600000;
        FormatUtils.appendPaddedInteger((StringBuffer)stringBuffer, (int)n2, (int)2);
        n2 = (n -= n2 * 3600000) / 60000;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger((StringBuffer)stringBuffer, (int)n2, (int)2);
        if ((n -= n2 * 60000) == 0) {
            return stringBuffer.toString();
        }
        n2 = n / 1000;
        stringBuffer.append(':');
        FormatUtils.appendPaddedInteger((StringBuffer)stringBuffer, (int)n2, (int)2);
        if ((n -= n2 * 1000) == 0) {
            return stringBuffer.toString();
        }
        stringBuffer.append('.');
        FormatUtils.appendPaddedInteger((StringBuffer)stringBuffer, (int)n, (int)3);
        return stringBuffer.toString();
    }

    public static void setDefault(DateTimeZone dateTimeZone) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setDefault"));
        }
        if (dateTimeZone != null) {
            cDefault.set(dateTimeZone);
            return;
        }
        throw new IllegalArgumentException("The datetime zone must not be null");
    }

    public static void setNameProvider(NameProvider nameProvider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setNameProvider"));
        }
        if (nameProvider == null) {
            nameProvider = DateTimeZone.getDefaultNameProvider();
        }
        cNameProvider.set(nameProvider);
    }

    public static void setProvider(Provider provider) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission((Permission)new JodaTimePermission("DateTimeZone.setProvider"));
        }
        if (provider == null) {
            provider = DateTimeZone.getDefaultProvider();
        } else {
            DateTimeZone.validateProvider(provider);
        }
        cProvider.set(provider);
    }

    private static Provider validateProvider(Provider provider) {
        Set set = provider.getAvailableIDs();
        if (set != null && set.size() != 0) {
            if (set.contains("UTC")) {
                if (UTC.equals(provider.getZone("UTC"))) {
                    return provider;
                }
                throw new IllegalArgumentException("Invalid UTC zone provided");
            }
            throw new IllegalArgumentException("The provider doesn't support UTC");
        }
        throw new IllegalArgumentException("The provider doesn't have any available ids");
    }

    public long adjustOffset(long l, boolean bl) {
        long l2;
        long l3 = l - 10800000L;
        long l4 = this.getOffset(l3);
        if (l4 <= (l2 = (long)this.getOffset(10800000L + l))) {
            return l;
        }
        l2 = (l3 = this.nextTransition(l3)) - (l4 -= l2);
        if (l >= l2) {
            if (l >= l3 + l4) {
                return l;
            }
            if (l - l2 >= l4) {
                if (bl) {
                    return l;
                }
                return l - l4;
            }
            l3 = l;
            if (bl) {
                l3 = l + l4;
            }
            return l3;
        }
        return l;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public long convertLocalToUTC(long var1_1, boolean var3_2) {
        var4_3 = this.getOffset(var1_1);
        if (var4_3 == (var5_5 = this.getOffset(var10_4 = var1_1 - (long)var4_3)) || !var3_2 && var4_3 >= 0) ** GOTO lbl-1000
        var6_6 = this.nextTransition(var10_4);
        var8_7 = Long.MAX_VALUE;
        if (var6_6 == var10_4) {
            var6_6 = Long.MAX_VALUE;
        }
        if ((var10_4 = this.nextTransition(var12_8 = var1_1 - (long)var5_5)) != var12_8) {
            var8_7 = var10_4;
        }
        if (var6_6 != var8_7) {
            if (var3_2 != false) throw new IllegalInstantException(var1_1, this.getID());
        } else lbl-1000: // 2 sources:
        {
            var4_3 = var5_5;
        }
        var6_6 = var4_3;
        var8_7 = var1_1 - var6_6;
        if ((var1_1 ^ var8_7) >= 0L) return var8_7;
        if ((var1_1 ^ var6_6) < 0L) throw new ArithmeticException("Subtracting time zone offset caused overflow");
        return var8_7;
    }

    public long convertLocalToUTC(long l, boolean bl, long l2) {
        int n = this.getOffset(l2);
        l2 = l - (long)n;
        if (this.getOffset(l2) == n) {
            return l2;
        }
        return this.convertLocalToUTC(l, bl);
    }

    public long convertUTCToLocal(long l) {
        long l2 = this.getOffset(l);
        long l3 = l + l2;
        if ((l ^ l3) < 0L) {
            if ((l ^ l2) < 0L) {
                return l3;
            }
            throw new ArithmeticException("Adding time zone offset caused overflow");
        }
        return l3;
    }

    public abstract boolean equals(Object var1);

    @ToString
    public final String getID() {
        return this.iID;
    }

    public long getMillisKeepLocal(DateTimeZone dateTimeZone, long l) {
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        if (dateTimeZone == this) {
            return l;
        }
        return dateTimeZone.convertLocalToUTC(this.convertUTCToLocal(l), false, l);
    }

    public final String getName(long l) {
        return this.getName(l, null);
    }

    public String getName(long l, Locale object) {
        String string;
        if (object == null) {
            object = Locale.getDefault();
        }
        if ((string = this.getNameKey(l)) == null) {
            return this.iID;
        }
        NameProvider nameProvider = DateTimeZone.getNameProvider();
        object = nameProvider instanceof DefaultNameProvider ? ((DefaultNameProvider)nameProvider).getName((Locale)object, this.iID, string, this.isStandardOffset(l)) : nameProvider.getName((Locale)object, this.iID, string);
        if (object != null) {
            return object;
        }
        return DateTimeZone.printOffset(this.getOffset(l));
    }

    public abstract String getNameKey(long var1);

    public abstract int getOffset(long var1);

    public final int getOffset(ReadableInstant readableInstant) {
        if (readableInstant == null) {
            return this.getOffset(DateTimeUtils.currentTimeMillis());
        }
        return this.getOffset(readableInstant.getMillis());
    }

    public int getOffsetFromLocal(long l) {
        long l2;
        int n;
        int n2 = this.getOffset(l);
        if (n2 != (n = this.getOffset(l2 = l - (long)n2))) {
            if (n2 - n < 0) {
                long l3 = this.nextTransition(l2);
                if (l3 == l2) {
                    l3 = Long.MAX_VALUE;
                }
                l2 = l - (long)n;
                l = this.nextTransition(l2);
                if (l == l2) {
                    l = Long.MAX_VALUE;
                }
                if (l3 != l) {
                    return n2;
                }
                return n;
            }
            return n;
        }
        if (n2 >= 0) {
            l = this.previousTransition(l2);
            if (l < l2) {
                int n3 = this.getOffset(l);
                if (l2 - l <= (long)(n3 - n2)) {
                    return n3;
                }
                return n;
            }
            return n;
        }
        return n;
    }

    public final String getShortName(long l) {
        return this.getShortName(l, null);
    }

    public String getShortName(long l, Locale object) {
        String string;
        if (object == null) {
            object = Locale.getDefault();
        }
        if ((string = this.getNameKey(l)) == null) {
            return this.iID;
        }
        NameProvider nameProvider = DateTimeZone.getNameProvider();
        object = nameProvider instanceof DefaultNameProvider ? ((DefaultNameProvider)nameProvider).getShortName((Locale)object, this.iID, string, this.isStandardOffset(l)) : nameProvider.getShortName((Locale)object, this.iID, string);
        if (object != null) {
            return object;
        }
        return DateTimeZone.printOffset(this.getOffset(l));
    }

    public abstract int getStandardOffset(long var1);

    public int hashCode() {
        return this.getID().hashCode() + 57;
    }

    public abstract boolean isFixed();

    public boolean isLocalDateTimeGap(LocalDateTime localDateTime) {
        if (this.isFixed()) {
            return false;
        }
        try {
            localDateTime.toDateTime(this);
            return false;
        }
        catch (IllegalInstantException illegalInstantException) {
            return true;
        }
    }

    public boolean isStandardOffset(long l) {
        if (this.getOffset(l) == this.getStandardOffset(l)) {
            return true;
        }
        return false;
    }

    public abstract long nextTransition(long var1);

    public abstract long previousTransition(long var1);

    public String toString() {
        return this.getID();
    }

    public TimeZone toTimeZone() {
        return TimeZone.getTimeZone(this.iID);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}

