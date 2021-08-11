/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.MetadataManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonemetadata;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PhoneNumberMatcher
implements Iterator<PhoneNumberMatch> {
    private static final Pattern[] INNER_MATCHES;
    private static final Pattern LEAD_CLASS;
    private static final Pattern MATCHING_BRACKETS;
    private static final Pattern PATTERN;
    private static final Pattern PUB_PAGES;
    private static final Pattern SLASH_SEPARATED_DATES;
    private static final Pattern TIME_STAMPS;
    private static final Pattern TIME_STAMPS_SUFFIX;
    private PhoneNumberMatch lastMatch = null;
    private final PhoneNumberUtil.Leniency leniency;
    private long maxTries;
    private final PhoneNumberUtil phoneUtil;
    private final String preferredRegion;
    private int searchIndex = 0;
    private State state = State.NOT_READY;
    private final CharSequence text;

    static {
        PUB_PAGES = Pattern.compile("\\d{1,5}-+\\d{1,5}\\s{0,4}\\(\\d{1,4}");
        SLASH_SEPARATED_DATES = Pattern.compile("(?:(?:[0-3]?\\d/[01]?\\d)|(?:[01]?\\d/[0-3]?\\d))/(?:[12]\\d)?\\d{2}");
        TIME_STAMPS = Pattern.compile("[12]\\d{3}[-/]?[01]\\d[-/]?[0-3]\\d +[0-2]\\d$");
        TIME_STAMPS_SUFFIX = Pattern.compile(":[0-5]\\d");
        INNER_MATCHES = new Pattern[]{Pattern.compile("/+(.*)"), Pattern.compile("(\\([^(]*)"), Pattern.compile("(?:\\p{Z}-|-\\p{Z})\\p{Z}*(.+)"), Pattern.compile("[\u2012-\u2015\uff0d]\\p{Z}*(.+)"), Pattern.compile("\\.+\\p{Z}*([^.]+)"), Pattern.compile("\\p{Z}+(\\P{Z}+)")};
        CharSequence charSequence = new StringBuilder();
        charSequence.append("[^");
        charSequence.append("(\\[\uff08\uff3b");
        charSequence.append(")\\]\uff09\uff3d");
        charSequence.append("]");
        charSequence = charSequence.toString();
        String string2 = PhoneNumberMatcher.limit(0, 3);
        CharSequence charSequence2 = new StringBuilder();
        charSequence2.append("(?:[");
        charSequence2.append("(\\[\uff08\uff3b");
        charSequence2.append("])?(?:");
        charSequence2.append((String)charSequence);
        charSequence2.append("+[");
        charSequence2.append(")\\]\uff09\uff3d");
        charSequence2.append("])?");
        charSequence2.append((String)charSequence);
        charSequence2.append("+(?:[");
        charSequence2.append("(\\[\uff08\uff3b");
        charSequence2.append("]");
        charSequence2.append((String)charSequence);
        charSequence2.append("+[");
        charSequence2.append(")\\]\uff09\uff3d");
        charSequence2.append("])");
        charSequence2.append(string2);
        charSequence2.append((String)charSequence);
        charSequence2.append("*");
        MATCHING_BRACKETS = Pattern.compile(charSequence2.toString());
        charSequence = PhoneNumberMatcher.limit(0, 2);
        charSequence2 = PhoneNumberMatcher.limit(0, 4);
        string2 = PhoneNumberMatcher.limit(0, 20);
        CharSequence charSequence3 = new StringBuilder();
        charSequence3.append("[-x\u2010-\u2015\u2212\u30fc\uff0d-\uff0f \u00a0\u00ad\u200b\u2060\u3000()\uff08\uff09\uff3b\uff3d.\\[\\]/~\u2053\u223c\uff5e]");
        charSequence3.append((String)charSequence2);
        charSequence2 = charSequence3.toString();
        charSequence3 = new StringBuilder();
        charSequence3.append("\\p{Nd}");
        charSequence3.append(PhoneNumberMatcher.limit(1, 20));
        charSequence3 = charSequence3.toString();
        CharSequence charSequence4 = new StringBuilder();
        charSequence4.append("(\\[\uff08\uff3b");
        charSequence4.append("+\uff0b");
        charSequence4 = charSequence4.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append((String)charSequence4);
        stringBuilder.append("]");
        charSequence4 = stringBuilder.toString();
        LEAD_CLASS = Pattern.compile((String)charSequence4);
        stringBuilder = new StringBuilder();
        stringBuilder.append("(?:");
        stringBuilder.append((String)charSequence4);
        stringBuilder.append((String)charSequence2);
        stringBuilder.append(")");
        stringBuilder.append((String)charSequence);
        stringBuilder.append((String)charSequence3);
        stringBuilder.append("(?:");
        stringBuilder.append((String)charSequence2);
        stringBuilder.append((String)charSequence3);
        stringBuilder.append(")");
        stringBuilder.append(string2);
        stringBuilder.append("(?:");
        stringBuilder.append(PhoneNumberUtil.EXTN_PATTERNS_FOR_MATCHING);
        stringBuilder.append(")?");
        PATTERN = Pattern.compile(stringBuilder.toString(), 66);
    }

    PhoneNumberMatcher(PhoneNumberUtil phoneNumberUtil, CharSequence charSequence, String string2, PhoneNumberUtil.Leniency leniency, long l) {
        if (phoneNumberUtil != null && leniency != null) {
            if (l >= 0L) {
                this.phoneUtil = phoneNumberUtil;
                if (charSequence == null) {
                    charSequence = "";
                }
                this.text = charSequence;
                this.preferredRegion = string2;
                this.leniency = leniency;
                this.maxTries = l;
                return;
            }
            throw new IllegalArgumentException();
        }
        throw null;
    }

    static boolean allNumberGroupsAreExactlyPresent(PhoneNumberUtil phoneNumberUtil, Phonenumber.PhoneNumber phoneNumber, StringBuilder arrstring, String[] arrstring2) {
        arrstring = PhoneNumberUtil.NON_DIGITS_PATTERN.split(arrstring.toString());
        int n = phoneNumber.hasExtension() ? arrstring.length - 2 : arrstring.length - 1;
        if (arrstring.length != 1) {
            if (arrstring[n].contains(phoneNumberUtil.getNationalSignificantNumber(phoneNumber))) {
                return true;
            }
            for (int i = arrstring2.length - 1; i > 0 && n >= 0; --i, --n) {
                if (arrstring[n].equals(arrstring2[i])) continue;
                return false;
            }
            if (n >= 0 && arrstring[n].endsWith(arrstring2[0])) {
                return true;
            }
            return false;
        }
        return true;
    }

    static boolean allNumberGroupsRemainGrouped(PhoneNumberUtil object, Phonenumber.PhoneNumber phoneNumber, StringBuilder stringBuilder, String[] arrstring) {
        int n = 0;
        if (phoneNumber.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            String string2 = Integer.toString(phoneNumber.getCountryCode());
            n = stringBuilder.indexOf(string2) + string2.length();
        }
        for (int i = 0; i < arrstring.length; ++i) {
            if ((n = stringBuilder.indexOf(arrstring[i], n)) < 0) {
                return false;
            }
            if (i != 0 || n >= stringBuilder.length() || object.getNddPrefixForRegion(object.getRegionCodeForCountryCode(phoneNumber.getCountryCode()), true) == null || !Character.isDigit(stringBuilder.charAt(n += arrstring[i].length()))) continue;
            object = object.getNationalSignificantNumber(phoneNumber);
            return stringBuilder.substring(n - arrstring[i].length()).startsWith((String)object);
        }
        return stringBuilder.substring(n).contains(phoneNumber.getExtension());
    }

    static boolean checkNumberGroupingIsValid(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, NumberGroupingChecker numberGroupingChecker) {
        if (numberGroupingChecker.checkGroups(phoneNumberUtil, phoneNumber, (StringBuilder)(charSequence = PhoneNumberUtil.normalizeDigits(charSequence, true)), PhoneNumberMatcher.getNationalNumberGroups(phoneNumberUtil, phoneNumber, null))) {
            return true;
        }
        Object object = MetadataManager.getAlternateFormatsForCountry(phoneNumber.getCountryCode());
        if (object != null) {
            object = object.numberFormats().iterator();
            while (object.hasNext()) {
                if (!numberGroupingChecker.checkGroups(phoneNumberUtil, phoneNumber, (StringBuilder)charSequence, PhoneNumberMatcher.getNationalNumberGroups(phoneNumberUtil, phoneNumber, (Phonemetadata.NumberFormat)object.next()))) continue;
                return true;
            }
        }
        return false;
    }

    static boolean containsMoreThanOneSlashInNationalNumber(Phonenumber.PhoneNumber phoneNumber, String string2) {
        int n = string2.indexOf(47);
        if (n < 0) {
            return false;
        }
        int n2 = string2.indexOf(47, n + 1);
        if (n2 < 0) {
            return false;
        }
        boolean bl = phoneNumber.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN || phoneNumber.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN;
        if (bl && PhoneNumberUtil.normalizeDigitsOnly(string2.substring(0, n)).equals(Integer.toString(phoneNumber.getCountryCode()))) {
            return string2.substring(n2 + 1).contains("/");
        }
        return true;
    }

    static boolean containsOnlyValidXChars(Phonenumber.PhoneNumber phoneNumber, String string2, PhoneNumberUtil phoneNumberUtil) {
        int n = 0;
        while (n < string2.length() - 1) {
            int n2;
            block9 : {
                block8 : {
                    char c = string2.charAt(n);
                    if (c == 'x') break block8;
                    n2 = n;
                    if (c != 'X') break block9;
                }
                if ((n2 = (int)string2.charAt(n + 1)) != 120 && n2 != 88) {
                    n2 = n;
                    if (!PhoneNumberUtil.normalizeDigitsOnly(string2.substring(n)).equals(phoneNumber.getExtension())) {
                        return false;
                    }
                } else {
                    n2 = ++n;
                    if (phoneNumberUtil.isNumberMatch(phoneNumber, (CharSequence)string2.substring(n)) != PhoneNumberUtil.MatchType.NSN_MATCH) {
                        return false;
                    }
                }
            }
            n = n2 + 1;
        }
        return true;
    }

    private PhoneNumberMatch extractInnerMatch(CharSequence charSequence, int n) {
        Pattern[] arrpattern = INNER_MATCHES;
        int n2 = arrpattern.length;
        for (int i = 0; i < n2; ++i) {
            Matcher matcher = arrpattern[i].matcher(charSequence);
            boolean bl = true;
            while (matcher.find() && this.maxTries > 0L) {
                PhoneNumberMatch phoneNumberMatch;
                boolean bl2 = bl;
                if (bl) {
                    phoneNumberMatch = this.parseAndVerify(PhoneNumberMatcher.trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, charSequence.subSequence(0, matcher.start())), n);
                    if (phoneNumberMatch != null) {
                        return phoneNumberMatch;
                    }
                    --this.maxTries;
                    bl2 = false;
                }
                if ((phoneNumberMatch = this.parseAndVerify(PhoneNumberMatcher.trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, matcher.group(1)), matcher.start(1) + n)) != null) {
                    return phoneNumberMatch;
                }
                --this.maxTries;
                bl = bl2;
            }
        }
        return null;
    }

    private PhoneNumberMatch extractMatch(CharSequence charSequence, int n) {
        Object object;
        if (SLASH_SEPARATED_DATES.matcher(charSequence).find()) {
            return null;
        }
        if (TIME_STAMPS.matcher(charSequence).find() && TIME_STAMPS_SUFFIX.matcher((CharSequence)(object = this.text.toString().substring(charSequence.length() + n))).lookingAt()) {
            return null;
        }
        object = this.parseAndVerify(charSequence, n);
        if (object != null) {
            return object;
        }
        return this.extractInnerMatch(charSequence, n);
    }

    private PhoneNumberMatch find(int n) {
        Matcher matcher = PATTERN.matcher(this.text);
        while (this.maxTries > 0L && matcher.find(n)) {
            n = matcher.start();
            CharSequence charSequence = this.text.subSequence(n, matcher.end());
            PhoneNumberMatch phoneNumberMatch = this.extractMatch(charSequence = PhoneNumberMatcher.trimAfterFirstMatch(PhoneNumberUtil.SECOND_NUMBER_START_PATTERN, charSequence), n);
            if (phoneNumberMatch != null) {
                return phoneNumberMatch;
            }
            n += charSequence.length();
            --this.maxTries;
        }
        return null;
    }

    private static String[] getNationalNumberGroups(PhoneNumberUtil object, Phonenumber.PhoneNumber phoneNumber, Phonemetadata.NumberFormat numberFormat) {
        if (numberFormat == null) {
            int n;
            object = object.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.RFC3966);
            int n2 = n = object.indexOf(59);
            if (n < 0) {
                n2 = object.length();
            }
            return object.substring(object.indexOf(45) + 1, n2).split("-");
        }
        return object.formatNsnUsingPattern(object.getNationalSignificantNumber(phoneNumber), numberFormat, PhoneNumberUtil.PhoneNumberFormat.RFC3966).split("-");
    }

    private static boolean isInvalidPunctuationSymbol(char c) {
        if (c != '%' && Character.getType(c) != 26) {
            return false;
        }
        return true;
    }

    static boolean isLatinLetter(char c) {
        boolean bl = Character.isLetter(c);
        boolean bl2 = false;
        if (!bl && Character.getType(c) != 6) {
            return false;
        }
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
        if (unicodeBlock.equals(Character.UnicodeBlock.BASIC_LATIN) || unicodeBlock.equals(Character.UnicodeBlock.LATIN_1_SUPPLEMENT) || unicodeBlock.equals(Character.UnicodeBlock.LATIN_EXTENDED_A) || unicodeBlock.equals(Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) || unicodeBlock.equals(Character.UnicodeBlock.LATIN_EXTENDED_B) || unicodeBlock.equals(Character.UnicodeBlock.COMBINING_DIACRITICAL_MARKS)) {
            bl2 = true;
        }
        return bl2;
    }

    static boolean isNationalPrefixPresentIfRequired(Phonenumber.PhoneNumber phoneNumber, PhoneNumberUtil phoneNumberUtil) {
        if (phoneNumber.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            return true;
        }
        Phonemetadata.PhoneMetadata phoneMetadata = phoneNumberUtil.getMetadataForRegion(phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.getCountryCode()));
        if (phoneMetadata == null) {
            return true;
        }
        Object object = phoneNumberUtil.getNationalSignificantNumber(phoneNumber);
        object = phoneNumberUtil.chooseFormattingPatternForNumber(phoneMetadata.numberFormats(), (String)object);
        if (object != null && object.getNationalPrefixFormattingRule().length() > 0) {
            if (object.getNationalPrefixOptionalWhenFormatting()) {
                return true;
            }
            if (PhoneNumberUtil.formattingRuleHasFirstGroupOnly(object.getNationalPrefixFormattingRule())) {
                return true;
            }
            return phoneNumberUtil.maybeStripNationalPrefixAndCarrierCode(new StringBuilder(PhoneNumberUtil.normalizeDigitsOnly(phoneNumber.getRawInput())), phoneMetadata, null);
        }
        return true;
    }

    private static String limit(int n, int n2) {
        if (n >= 0 && n2 > 0 && n2 >= n) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            stringBuilder.append(n);
            stringBuilder.append(",");
            stringBuilder.append(n2);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
        throw new IllegalArgumentException();
    }

    private PhoneNumberMatch parseAndVerify(CharSequence object, int n) {
        block12 : {
            block11 : {
                block7 : {
                    block9 : {
                        char c;
                        block10 : {
                            block8 : {
                                try {
                                    if (!MATCHING_BRACKETS.matcher((CharSequence)object).matches()) break block7;
                                    if (!PUB_PAGES.matcher((CharSequence)object).find()) break block8;
                                    return null;
                                }
                                catch (NumberParseException numberParseException) {
                                    return null;
                                }
                            }
                            if (this.leniency.compareTo(PhoneNumberUtil.Leniency.VALID) < 0) break block9;
                            if (n <= 0) break block10;
                            if (LEAD_CLASS.matcher((CharSequence)object).lookingAt()) break block10;
                            c = this.text.charAt(n - 1);
                            if (PhoneNumberMatcher.isInvalidPunctuationSymbol(c)) break block11;
                            if (!PhoneNumberMatcher.isLatinLetter(c)) break block10;
                            return null;
                        }
                        int n2 = object.length() + n;
                        if (n2 >= this.text.length()) break block9;
                        c = this.text.charAt(n2);
                        if (PhoneNumberMatcher.isInvalidPunctuationSymbol(c)) break block12;
                        if (!PhoneNumberMatcher.isLatinLetter(c)) break block9;
                        return null;
                    }
                    Phonenumber.PhoneNumber phoneNumber = this.phoneUtil.parseAndKeepRawInput((CharSequence)object, this.preferredRegion);
                    if (this.leniency.verify(phoneNumber, (CharSequence)object, this.phoneUtil)) {
                        phoneNumber.clearCountryCodeSource();
                        phoneNumber.clearRawInput();
                        phoneNumber.clearPreferredDomesticCarrierCode();
                        object = new PhoneNumberMatch(n, object.toString(), phoneNumber);
                        return object;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private static CharSequence trimAfterFirstMatch(Pattern object, CharSequence charSequence) {
        Matcher matcher = object.matcher(charSequence);
        object = charSequence;
        if (matcher.find()) {
            object = charSequence.subSequence(0, matcher.start());
        }
        return object;
    }

    @Override
    public boolean hasNext() {
        if (this.state == State.NOT_READY) {
            PhoneNumberMatch phoneNumberMatch;
            this.lastMatch = phoneNumberMatch = this.find(this.searchIndex);
            if (phoneNumberMatch == null) {
                this.state = State.DONE;
            } else {
                this.searchIndex = phoneNumberMatch.end();
                this.state = State.READY;
            }
        }
        if (this.state == State.READY) {
            return true;
        }
        return false;
    }

    @Override
    public PhoneNumberMatch next() {
        if (this.hasNext()) {
            PhoneNumberMatch phoneNumberMatch = this.lastMatch;
            this.lastMatch = null;
            this.state = State.NOT_READY;
            return phoneNumberMatch;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    static interface NumberGroupingChecker {
        public boolean checkGroups(PhoneNumberUtil var1, Phonenumber.PhoneNumber var2, StringBuilder var3, String[] var4);
    }

    private static final class State
    extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES;
        public static final /* enum */ State DONE;
        public static final /* enum */ State NOT_READY;
        public static final /* enum */ State READY;

        static {
            State state;
            NOT_READY = new State();
            READY = new State();
            DONE = state = new State();
            $VALUES = new State[]{NOT_READY, READY, state};
        }

        private State() {
        }

        public static State valueOf(String string2) {
            return Enum.valueOf(State.class, string2);
        }

        public static State[] values() {
            return (State[])$VALUES.clone();
        }
    }

}

