/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.CountryCodeToRegionCodeMap;
import com.google.i18n.phonenumbers.MetadataLoader;
import com.google.i18n.phonenumbers.MetadataManager;
import com.google.i18n.phonenumbers.MetadataSource;
import com.google.i18n.phonenumbers.MultiFileMetadataSourceImpl;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberMatcher;
import com.google.i18n.phonenumbers.Phonemetadata;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.internal.MatcherApi;
import com.google.i18n.phonenumbers.internal.RegexBasedMatcher;
import com.google.i18n.phonenumbers.internal.RegexCache;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtil {
    private static final Map<Character, Character> ALL_PLUS_NUMBER_GROUPING_SYMBOLS;
    private static final Map<Character, Character> ALPHA_MAPPINGS;
    private static final Map<Character, Character> ALPHA_PHONE_MAPPINGS;
    private static final Pattern CAPTURING_DIGIT_PATTERN;
    private static final String CAPTURING_EXTN_DIGITS = "(\\p{Nd}{1,7})";
    private static final String CC_STRING = "$CC";
    private static final String COLOMBIA_MOBILE_TO_FIXED_LINE_PREFIX = "3";
    private static final String DEFAULT_EXTN_PREFIX = " ext. ";
    private static final Map<Character, Character> DIALLABLE_CHAR_MAPPINGS;
    private static final String DIGITS = "\\p{Nd}";
    private static final Pattern EXTN_PATTERN;
    static final String EXTN_PATTERNS_FOR_MATCHING;
    private static final String EXTN_PATTERNS_FOR_PARSING;
    private static final String FG_STRING = "$FG";
    private static final Pattern FIRST_GROUP_ONLY_PREFIX_PATTERN;
    private static final Pattern FIRST_GROUP_PATTERN;
    private static final Set<Integer> GEO_MOBILE_COUNTRIES;
    private static final Set<Integer> GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES;
    private static final int MAX_INPUT_STRING_LENGTH = 250;
    static final int MAX_LENGTH_COUNTRY_CODE = 3;
    static final int MAX_LENGTH_FOR_NSN = 17;
    private static final int MIN_LENGTH_FOR_NSN = 2;
    private static final Map<Integer, String> MOBILE_TOKEN_MAPPINGS;
    private static final int NANPA_COUNTRY_CODE = 1;
    static final Pattern NON_DIGITS_PATTERN;
    private static final String NP_STRING = "$NP";
    static final String PLUS_CHARS = "+\uff0b";
    static final Pattern PLUS_CHARS_PATTERN;
    static final char PLUS_SIGN = '+';
    static final int REGEX_FLAGS = 66;
    public static final String REGION_CODE_FOR_NON_GEO_ENTITY = "001";
    private static final String RFC3966_EXTN_PREFIX = ";ext=";
    private static final String RFC3966_ISDN_SUBADDRESS = ";isub=";
    private static final String RFC3966_PHONE_CONTEXT = ";phone-context=";
    private static final String RFC3966_PREFIX = "tel:";
    private static final String SECOND_NUMBER_START = "[\\\\/] *x";
    static final Pattern SECOND_NUMBER_START_PATTERN;
    private static final Pattern SEPARATOR_PATTERN;
    private static final Pattern SINGLE_INTERNATIONAL_PREFIX;
    private static final char STAR_SIGN = '*';
    private static final String UNKNOWN_REGION = "ZZ";
    private static final String UNWANTED_END_CHARS = "[[\\P{N}&&\\P{L}]&&[^#]]+$";
    static final Pattern UNWANTED_END_CHAR_PATTERN;
    private static final String VALID_ALPHA;
    private static final Pattern VALID_ALPHA_PHONE_PATTERN;
    private static final String VALID_PHONE_NUMBER;
    private static final Pattern VALID_PHONE_NUMBER_PATTERN;
    static final String VALID_PUNCTUATION = "-x\u2010-\u2015\u2212\u30fc\uff0d-\uff0f \u00a0\u00ad\u200b\u2060\u3000()\uff08\uff09\uff3b\uff3d.\\[\\]/~\u2053\u223c\uff5e";
    private static final String VALID_START_CHAR = "[+\uff0b\\p{Nd}]";
    private static final Pattern VALID_START_CHAR_PATTERN;
    private static PhoneNumberUtil instance;
    private static final Logger logger;
    private final Map<Integer, List<String>> countryCallingCodeToRegionCodeMap;
    private final Set<Integer> countryCodesForNonGeographicalRegion = new HashSet<Integer>();
    private final MatcherApi matcherApi = RegexBasedMatcher.create();
    private final MetadataSource metadataSource;
    private final Set<String> nanpaRegions = new HashSet<String>(35);
    private final RegexCache regexCache = new RegexCache(100);
    private final Set<String> supportedRegions = new HashSet<String>(320);

    static {
        logger = Logger.getLogger(PhoneNumberUtil.class.getName());
        Serializable serializable = new HashMap<Integer, String>();
        Serializable serializable2 = 52;
        Serializable serializable3 = Character.valueOf('4');
        serializable.put(serializable2, "1");
        Comparable comparable = 54;
        Object object = Character.valueOf('6');
        serializable.put(comparable, "9");
        MOBILE_TOKEN_MAPPINGS = Collections.unmodifiableMap(serializable);
        serializable = new HashSet();
        serializable.add(86);
        GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES = Collections.unmodifiableSet(serializable);
        Serializable serializable4 = new HashSet<Integer>();
        serializable4.add(serializable2);
        serializable4.add(comparable);
        comparable = Character.valueOf('7');
        serializable4.add(55);
        serializable4.add(62);
        serializable4.addAll((Collection<Integer>)((Object)serializable));
        GEO_MOBILE_COUNTRIES = Collections.unmodifiableSet(serializable4);
        serializable2 = new HashMap();
        serializable = Character.valueOf('0');
        serializable2.put(serializable, serializable);
        serializable = Character.valueOf('1');
        serializable2.put(serializable, serializable);
        serializable = Character.valueOf('2');
        serializable2.put(serializable, serializable);
        serializable4 = Character.valueOf('3');
        serializable2.put(serializable4, serializable4);
        serializable2.put(serializable3, serializable3);
        Character c = Character.valueOf('5');
        serializable2.put(c, c);
        serializable2.put(object, object);
        serializable2.put(comparable, comparable);
        Character c2 = Character.valueOf('8');
        serializable2.put(c2, c2);
        Character c3 = Character.valueOf('9');
        serializable2.put(c3, c3);
        HashMap<Character, Object> hashMap = new HashMap<Character, Object>(40);
        hashMap.put(Character.valueOf('A'), serializable);
        hashMap.put(Character.valueOf('B'), serializable);
        hashMap.put(Character.valueOf('C'), serializable);
        hashMap.put(Character.valueOf('D'), serializable4);
        hashMap.put(Character.valueOf('E'), serializable4);
        hashMap.put(Character.valueOf('F'), serializable4);
        hashMap.put(Character.valueOf('G'), serializable3);
        hashMap.put(Character.valueOf('H'), serializable3);
        hashMap.put(Character.valueOf('I'), serializable3);
        hashMap.put(Character.valueOf('J'), c);
        hashMap.put(Character.valueOf('K'), c);
        hashMap.put(Character.valueOf('L'), c);
        hashMap.put(Character.valueOf('M'), (Character)object);
        hashMap.put(Character.valueOf('N'), (Character)object);
        hashMap.put(Character.valueOf('O'), (Character)object);
        hashMap.put(Character.valueOf('P'), comparable);
        hashMap.put(Character.valueOf('Q'), comparable);
        hashMap.put(Character.valueOf('R'), comparable);
        hashMap.put(Character.valueOf('S'), comparable);
        hashMap.put(Character.valueOf('T'), c2);
        hashMap.put(Character.valueOf('U'), c2);
        hashMap.put(Character.valueOf('V'), c2);
        hashMap.put(Character.valueOf('W'), c3);
        hashMap.put(Character.valueOf('X'), c3);
        hashMap.put(Character.valueOf('Y'), c3);
        hashMap.put(Character.valueOf('Z'), c3);
        ALPHA_MAPPINGS = Collections.unmodifiableMap(hashMap);
        serializable3 = new HashMap(100);
        serializable3.putAll(ALPHA_MAPPINGS);
        serializable3.putAll(serializable2);
        ALPHA_PHONE_MAPPINGS = Collections.unmodifiableMap(serializable3);
        serializable3 = new HashMap();
        serializable3.putAll(serializable2);
        object = Character.valueOf('+');
        serializable3.put(object, object);
        object = Character.valueOf('*');
        serializable3.put(object, object);
        object = Character.valueOf('#');
        serializable3.put(object, object);
        DIALLABLE_CHAR_MAPPINGS = Collections.unmodifiableMap(serializable3);
        serializable3 = new HashMap();
        object = ALPHA_MAPPINGS.keySet().iterator();
        while (object.hasNext()) {
            char c4 = ((Character)object.next()).charValue();
            serializable3.put(Character.valueOf(Character.toLowerCase(c4)), Character.valueOf(c4));
            serializable3.put(Character.valueOf(c4), Character.valueOf(c4));
        }
        serializable3.putAll(serializable2);
        serializable3.put(Character.valueOf('-'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\uff0d'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2010'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2011'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2012'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2013'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2014'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2015'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('\u2212'), Character.valueOf('-'));
        serializable3.put(Character.valueOf('/'), Character.valueOf('/'));
        serializable3.put(Character.valueOf('\uff0f'), Character.valueOf('/'));
        serializable3.put(Character.valueOf(' '), Character.valueOf(' '));
        serializable3.put(Character.valueOf('\u3000'), Character.valueOf(' '));
        serializable3.put(Character.valueOf('\u2060'), Character.valueOf(' '));
        serializable3.put(Character.valueOf('.'), Character.valueOf('.'));
        serializable3.put(Character.valueOf('\uff0e'), Character.valueOf('.'));
        ALL_PLUS_NUMBER_GROUPING_SYMBOLS = Collections.unmodifiableMap(serializable3);
        SINGLE_INTERNATIONAL_PREFIX = Pattern.compile("[\\d]+(?:[~\u2053\u223c\uff5e][\\d]+)?");
        serializable2 = new StringBuilder();
        serializable2.append(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).replaceAll("[, \\[\\]]", ""));
        serializable2.append(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).toLowerCase().replaceAll("[, \\[\\]]", ""));
        VALID_ALPHA = serializable2.toString();
        PLUS_CHARS_PATTERN = Pattern.compile("[+\uff0b]+");
        SEPARATOR_PATTERN = Pattern.compile("[-x\u2010-\u2015\u2212\u30fc\uff0d-\uff0f \u00a0\u00ad\u200b\u2060\u3000()\uff08\uff09\uff3b\uff3d.\\[\\]/~\u2053\u223c\uff5e]+");
        CAPTURING_DIGIT_PATTERN = Pattern.compile("(\\p{Nd})");
        VALID_START_CHAR_PATTERN = Pattern.compile("[+\uff0b\\p{Nd}]");
        SECOND_NUMBER_START_PATTERN = Pattern.compile("[\\\\/] *x");
        UNWANTED_END_CHAR_PATTERN = Pattern.compile("[[\\P{N}&&\\P{L}]&&[^#]]+$");
        VALID_ALPHA_PHONE_PATTERN = Pattern.compile("(?:.*?[A-Za-z]){3}.*");
        serializable2 = new StringBuilder();
        serializable2.append("\\p{Nd}{2}|[+\uff0b]*+(?:[-x\u2010-\u2015\u2212\u30fc\uff0d-\uff0f \u00a0\u00ad\u200b\u2060\u3000()\uff08\uff09\uff3b\uff3d.\\[\\]/~\u2053\u223c\uff5e*]*\\p{Nd}){3,}[-x\u2010-\u2015\u2212\u30fc\uff0d-\uff0f \u00a0\u00ad\u200b\u2060\u3000()\uff08\uff09\uff3b\uff3d.\\[\\]/~\u2053\u223c\uff5e*");
        serializable2.append(VALID_ALPHA);
        serializable2.append("\\p{Nd}");
        serializable2.append("]*");
        VALID_PHONE_NUMBER = serializable2.toString();
        serializable2 = new StringBuilder();
        serializable2.append(",;");
        serializable2.append("x\uff58#\uff03~\uff5e");
        EXTN_PATTERNS_FOR_PARSING = PhoneNumberUtil.createExtnPattern(serializable2.toString());
        EXTN_PATTERNS_FOR_MATCHING = PhoneNumberUtil.createExtnPattern("x\uff58#\uff03~\uff5e");
        serializable2 = new StringBuilder();
        serializable2.append("(?:");
        serializable2.append(EXTN_PATTERNS_FOR_PARSING);
        serializable2.append(")$");
        EXTN_PATTERN = Pattern.compile(serializable2.toString(), 66);
        serializable2 = new StringBuilder();
        serializable2.append(VALID_PHONE_NUMBER);
        serializable2.append("(?:");
        serializable2.append(EXTN_PATTERNS_FOR_PARSING);
        serializable2.append(")?");
        VALID_PHONE_NUMBER_PATTERN = Pattern.compile(serializable2.toString(), 66);
        NON_DIGITS_PATTERN = Pattern.compile("(\\D+)");
        FIRST_GROUP_PATTERN = Pattern.compile("(\\$\\d)");
        FIRST_GROUP_ONLY_PREFIX_PATTERN = Pattern.compile("\\(?\\$1\\)?");
        instance = null;
    }

    PhoneNumberUtil(MetadataSource object, Map<Integer, List<String>> map) {
        this.metadataSource = object;
        this.countryCallingCodeToRegionCodeMap = map;
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            List<String> list = entry.getValue();
            if (list.size() == 1 && "001".equals(list.get(0))) {
                this.countryCodesForNonGeographicalRegion.add(entry.getKey());
                continue;
            }
            this.supportedRegions.addAll(list);
        }
        if (this.supportedRegions.remove("001")) {
            logger.log(Level.WARNING, "invalid metadata (country calling code was mapped to the non-geo entity as well as specific region(s))");
        }
        this.nanpaRegions.addAll((Collection)map.get(1));
    }

    private void buildNationalNumberForParsing(String string2, StringBuilder stringBuilder) {
        int n;
        int n2 = string2.indexOf(";phone-context=");
        if (n2 >= 0) {
            n = ";phone-context=".length() + n2;
            if (n < string2.length() - 1 && string2.charAt(n) == '+') {
                int n3 = string2.indexOf(59, n);
                if (n3 > 0) {
                    stringBuilder.append(string2.substring(n, n3));
                } else {
                    stringBuilder.append(string2.substring(n));
                }
            }
            n = (n = string2.indexOf("tel:")) >= 0 ? "tel:".length() + n : 0;
            stringBuilder.append(string2.substring(n, n2));
        } else {
            stringBuilder.append(PhoneNumberUtil.extractPossibleNumber(string2));
        }
        n = stringBuilder.indexOf(";isub=");
        if (n > 0) {
            stringBuilder.delete(n, stringBuilder.length());
        }
    }

    private boolean checkRegionForParsing(CharSequence charSequence, String string2) {
        if (!(this.isValidRegionCode(string2) || charSequence != null && charSequence.length() != 0 && PLUS_CHARS_PATTERN.matcher(charSequence).lookingAt())) {
            return false;
        }
        return true;
    }

    public static String convertAlphaCharactersInNumber(CharSequence charSequence) {
        return PhoneNumberUtil.normalizeHelper(charSequence, ALPHA_PHONE_MAPPINGS, false);
    }

    private static Phonenumber.PhoneNumber copyCoreFieldsOnly(Phonenumber.PhoneNumber phoneNumber) {
        Phonenumber.PhoneNumber phoneNumber2 = new Phonenumber.PhoneNumber();
        phoneNumber2.setCountryCode(phoneNumber.getCountryCode());
        phoneNumber2.setNationalNumber(phoneNumber.getNationalNumber());
        if (phoneNumber.getExtension().length() > 0) {
            phoneNumber2.setExtension(phoneNumber.getExtension());
        }
        if (phoneNumber.isItalianLeadingZero()) {
            phoneNumber2.setItalianLeadingZero(true);
            phoneNumber2.setNumberOfLeadingZeros(phoneNumber.getNumberOfLeadingZeros());
        }
        return phoneNumber2;
    }

    private static String createExtnPattern(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(";ext=(\\p{Nd}{1,7})|[ \u00a0\\t,]*(?:e?xt(?:ensi(?:o\u0301?|\u00f3))?n?|\uff45?\uff58\uff54\uff4e?|\u0434\u043e\u0431|[");
        stringBuilder.append(string2);
        stringBuilder.append("]|int|anexo|\uff49\uff4e\uff54)[:\\.\uff0e]?[ \u00a0\\t,-]*");
        stringBuilder.append("(\\p{Nd}{1,7})");
        stringBuilder.append("#?|[- ]+(");
        stringBuilder.append("\\p{Nd}");
        stringBuilder.append("{1,5})#");
        return stringBuilder.toString();
    }

    public static PhoneNumberUtil createInstance(MetadataLoader metadataLoader) {
        if (metadataLoader != null) {
            return PhoneNumberUtil.createInstance(new MultiFileMetadataSourceImpl(metadataLoader));
        }
        throw new IllegalArgumentException("metadataLoader could not be null.");
    }

    private static PhoneNumberUtil createInstance(MetadataSource metadataSource) {
        if (metadataSource != null) {
            return new PhoneNumberUtil(metadataSource, CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap());
        }
        throw new IllegalArgumentException("metadataSource could not be null.");
    }

    private static boolean descHasData(Phonemetadata.PhoneNumberDesc phoneNumberDesc) {
        if (!(phoneNumberDesc.hasExampleNumber() || PhoneNumberUtil.descHasPossibleNumberData(phoneNumberDesc) || phoneNumberDesc.hasNationalNumberPattern())) {
            return false;
        }
        return true;
    }

    private static boolean descHasPossibleNumberData(Phonemetadata.PhoneNumberDesc phoneNumberDesc) {
        int n = phoneNumberDesc.getPossibleLengthCount();
        boolean bl = false;
        if (n != 1 || phoneNumberDesc.getPossibleLength(0) != -1) {
            bl = true;
        }
        return bl;
    }

    static CharSequence extractPossibleNumber(CharSequence object) {
        Object object2 = VALID_START_CHAR_PATTERN.matcher((CharSequence)object);
        if (object2.find()) {
            object2 = object.subSequence(object2.start(), object.length());
            Matcher matcher = UNWANTED_END_CHAR_PATTERN.matcher((CharSequence)object2);
            object = object2;
            if (matcher.find()) {
                object = object2.subSequence(0, matcher.start());
            }
            matcher = SECOND_NUMBER_START_PATTERN.matcher((CharSequence)object);
            object2 = object;
            if (matcher.find()) {
                object2 = object.subSequence(0, matcher.start());
            }
            return object2;
        }
        return "";
    }

    private String formatNsn(String string2, Phonemetadata.PhoneMetadata phoneMetadata, PhoneNumberFormat phoneNumberFormat) {
        return this.formatNsn(string2, phoneMetadata, phoneNumberFormat, null);
    }

    private String formatNsn(String string2, Phonemetadata.PhoneMetadata list, PhoneNumberFormat phoneNumberFormat, CharSequence charSequence) {
        list = list.intlNumberFormats().size() != 0 && phoneNumberFormat != PhoneNumberFormat.NATIONAL ? list.intlNumberFormats() : list.numberFormats();
        list = this.chooseFormattingPatternForNumber(list, string2);
        if (list == null) {
            return string2;
        }
        return this.formatNsnUsingPattern(string2, (Phonemetadata.NumberFormat)((Object)list), phoneNumberFormat, charSequence);
    }

    private String formatNsnUsingPattern(String object, Phonemetadata.NumberFormat object2, PhoneNumberFormat phoneNumberFormat, CharSequence charSequence) {
        String string2 = object2.getFormat();
        object = this.regexCache.getPatternForRegex(object2.getPattern()).matcher((CharSequence)object);
        if (phoneNumberFormat == PhoneNumberFormat.NATIONAL && charSequence != null && charSequence.length() > 0 && object2.getDomesticCarrierCodeFormattingRule().length() > 0) {
            object2 = object2.getDomesticCarrierCodeFormattingRule().replace("$CC", charSequence);
            object = object.replaceAll(FIRST_GROUP_PATTERN.matcher(string2).replaceFirst((String)object2));
        } else {
            object2 = object2.getNationalPrefixFormattingRule();
            object = phoneNumberFormat == PhoneNumberFormat.NATIONAL && object2 != null && object2.length() > 0 ? object.replaceAll(FIRST_GROUP_PATTERN.matcher(string2).replaceFirst((String)object2)) : object.replaceAll(string2);
        }
        object2 = object;
        if (phoneNumberFormat == PhoneNumberFormat.RFC3966) {
            object2 = SEPARATOR_PATTERN.matcher((CharSequence)object);
            if (object2.lookingAt()) {
                object = object2.replaceFirst("");
            }
            object2 = object2.reset((CharSequence)object).replaceAll("-");
        }
        return object2;
    }

    static boolean formattingRuleHasFirstGroupOnly(String string2) {
        if (string2.length() != 0 && !FIRST_GROUP_ONLY_PREFIX_PATTERN.matcher(string2).matches()) {
            return false;
        }
        return true;
    }

    private int getCountryCodeForValidRegion(String string2) {
        Serializable serializable = this.getMetadataForRegion(string2);
        if (serializable != null) {
            return serializable.getCountryCode();
        }
        serializable = new StringBuilder();
        serializable.append("Invalid region code: ");
        serializable.append(string2);
        throw new IllegalArgumentException(serializable.toString());
    }

    public static String getCountryMobileToken(int n) {
        if (MOBILE_TOKEN_MAPPINGS.containsKey(n)) {
            return MOBILE_TOKEN_MAPPINGS.get(n);
        }
        return "";
    }

    public static PhoneNumberUtil getInstance() {
        synchronized (PhoneNumberUtil.class) {
            if (instance == null) {
                PhoneNumberUtil.setInstance(PhoneNumberUtil.createInstance(MetadataManager.DEFAULT_METADATA_LOADER));
            }
            PhoneNumberUtil phoneNumberUtil = instance;
            return phoneNumberUtil;
        }
    }

    private Phonemetadata.PhoneMetadata getMetadataForRegionOrCallingCode(int n, String string2) {
        if ("001".equals(string2)) {
            return this.getMetadataForNonGeographicalRegion(n);
        }
        return this.getMetadataForRegion(string2);
    }

    private PhoneNumberType getNumberTypeHelper(String string2, Phonemetadata.PhoneMetadata phoneMetadata) {
        if (!this.isNumberMatchingDesc(string2, phoneMetadata.getGeneralDesc())) {
            return PhoneNumberType.UNKNOWN;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getPremiumRate())) {
            return PhoneNumberType.PREMIUM_RATE;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getTollFree())) {
            return PhoneNumberType.TOLL_FREE;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getSharedCost())) {
            return PhoneNumberType.SHARED_COST;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getVoip())) {
            return PhoneNumberType.VOIP;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getPersonalNumber())) {
            return PhoneNumberType.PERSONAL_NUMBER;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getPager())) {
            return PhoneNumberType.PAGER;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getUan())) {
            return PhoneNumberType.UAN;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getVoicemail())) {
            return PhoneNumberType.VOICEMAIL;
        }
        if (this.isNumberMatchingDesc(string2, phoneMetadata.getFixedLine())) {
            if (phoneMetadata.getSameMobileAndFixedLinePattern()) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            if (this.isNumberMatchingDesc(string2, phoneMetadata.getMobile())) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            return PhoneNumberType.FIXED_LINE;
        }
        if (!phoneMetadata.getSameMobileAndFixedLinePattern() && this.isNumberMatchingDesc(string2, phoneMetadata.getMobile())) {
            return PhoneNumberType.MOBILE;
        }
        return PhoneNumberType.UNKNOWN;
    }

    private String getRegionCodeForNumberFromRegionList(Phonenumber.PhoneNumber object, List<String> object2) {
        object = this.getNationalSignificantNumber((Phonenumber.PhoneNumber)object);
        object2 = object2.iterator();
        while (object2.hasNext()) {
            String string2 = (String)object2.next();
            Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegion(string2);
            if (!(phoneMetadata.hasLeadingDigits() ? this.regexCache.getPatternForRegex(phoneMetadata.getLeadingDigits()).matcher((CharSequence)object).lookingAt() : this.getNumberTypeHelper((String)object, phoneMetadata) != PhoneNumberType.UNKNOWN)) continue;
            return string2;
        }
        return null;
    }

    private Set<PhoneNumberType> getSupportedTypesForMetadata(Phonemetadata.PhoneMetadata phoneMetadata) {
        TreeSet<PhoneNumberType> treeSet = new TreeSet<PhoneNumberType>();
        for (PhoneNumberType phoneNumberType : PhoneNumberType.values()) {
            if (phoneNumberType == PhoneNumberType.FIXED_LINE_OR_MOBILE || phoneNumberType == PhoneNumberType.UNKNOWN || !PhoneNumberUtil.descHasData(this.getNumberDescByType(phoneMetadata, phoneNumberType))) continue;
            treeSet.add(phoneNumberType);
        }
        return Collections.unmodifiableSet(treeSet);
    }

    private boolean hasFormattingPatternForNumber(Phonenumber.PhoneNumber object) {
        int n = object.getCountryCode();
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n));
        boolean bl = false;
        if (phoneMetadata == null) {
            return false;
        }
        object = this.getNationalSignificantNumber((Phonenumber.PhoneNumber)object);
        if (this.chooseFormattingPatternForNumber(phoneMetadata.numberFormats(), (String)object) != null) {
            bl = true;
        }
        return bl;
    }

    private boolean hasValidCountryCallingCode(int n) {
        return this.countryCallingCodeToRegionCodeMap.containsKey(n);
    }

    private boolean isNationalNumberSuffixOfTheOther(Phonenumber.PhoneNumber object, Phonenumber.PhoneNumber object2) {
        if (!(object = String.valueOf(object.getNationalNumber())).endsWith((String)(object2 = String.valueOf(object2.getNationalNumber()))) && !object2.endsWith((String)object)) {
            return false;
        }
        return true;
    }

    private boolean isValidRegionCode(String string2) {
        if (string2 != null && this.supportedRegions.contains(string2)) {
            return true;
        }
        return false;
    }

    static boolean isViablePhoneNumber(CharSequence charSequence) {
        if (charSequence.length() < 2) {
            return false;
        }
        return VALID_PHONE_NUMBER_PATTERN.matcher(charSequence).matches();
    }

    private void maybeAppendFormattedExtension(Phonenumber.PhoneNumber phoneNumber, Phonemetadata.PhoneMetadata phoneMetadata, PhoneNumberFormat phoneNumberFormat, StringBuilder stringBuilder) {
        if (phoneNumber.hasExtension() && phoneNumber.getExtension().length() > 0) {
            if (phoneNumberFormat == PhoneNumberFormat.RFC3966) {
                stringBuilder.append(";ext=");
                stringBuilder.append(phoneNumber.getExtension());
                return;
            }
            if (phoneMetadata.hasPreferredExtnPrefix()) {
                stringBuilder.append(phoneMetadata.getPreferredExtnPrefix());
                stringBuilder.append(phoneNumber.getExtension());
                return;
            }
            stringBuilder.append(" ext. ");
            stringBuilder.append(phoneNumber.getExtension());
        }
    }

    static StringBuilder normalize(StringBuilder stringBuilder) {
        if (VALID_ALPHA_PHONE_PATTERN.matcher(stringBuilder).matches()) {
            stringBuilder.replace(0, stringBuilder.length(), PhoneNumberUtil.normalizeHelper(stringBuilder, ALPHA_PHONE_MAPPINGS, true));
            return stringBuilder;
        }
        stringBuilder.replace(0, stringBuilder.length(), PhoneNumberUtil.normalizeDigitsOnly(stringBuilder));
        return stringBuilder;
    }

    public static String normalizeDiallableCharsOnly(CharSequence charSequence) {
        return PhoneNumberUtil.normalizeHelper(charSequence, DIALLABLE_CHAR_MAPPINGS, true);
    }

    static StringBuilder normalizeDigits(CharSequence charSequence, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); ++i) {
            char c = charSequence.charAt(i);
            int n = Character.digit(c, 10);
            if (n != -1) {
                stringBuilder.append(n);
                continue;
            }
            if (!bl) continue;
            stringBuilder.append(c);
        }
        return stringBuilder;
    }

    public static String normalizeDigitsOnly(CharSequence charSequence) {
        return PhoneNumberUtil.normalizeDigits(charSequence, false).toString();
    }

    private static String normalizeHelper(CharSequence charSequence, Map<Character, Character> map, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); ++i) {
            char c = charSequence.charAt(i);
            Character c2 = map.get(Character.valueOf(Character.toUpperCase(c)));
            if (c2 != null) {
                stringBuilder.append(c2);
                continue;
            }
            if (bl) continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void parseHelper(CharSequence object, String object2, boolean bl, boolean bl2, Phonenumber.PhoneNumber phoneNumber) throws NumberParseException {
        if (object != null) {
            if (object.length() <= 250) {
                CharSequence charSequence = new StringBuilder();
                object = object.toString();
                this.buildNationalNumberForParsing((String)object, (StringBuilder)charSequence);
                if (PhoneNumberUtil.isViablePhoneNumber(charSequence)) {
                    NumberParseException numberParseException2;
                    block23 : {
                        block24 : {
                            int n;
                            if (bl2 && !this.checkRegionForParsing(charSequence, (String)object2)) {
                                throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Missing or invalid default region.");
                            }
                            if (bl) {
                                phoneNumber.setRawInput((String)object);
                            }
                            if ((object = this.maybeStripExtension((StringBuilder)charSequence)).length() > 0) {
                                phoneNumber.setExtension((String)object);
                            }
                            object = this.getMetadataForRegion((String)object2);
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                n = this.maybeExtractCountryCode(charSequence, (Phonemetadata.PhoneMetadata)object, stringBuilder, bl, phoneNumber);
                            }
                            catch (NumberParseException numberParseException2) {
                                Matcher matcher = PLUS_CHARS_PATTERN.matcher(charSequence);
                                if (numberParseException2.getErrorType() != NumberParseException.ErrorType.INVALID_COUNTRY_CODE || !matcher.lookingAt()) break block23;
                                n = this.maybeExtractCountryCode(charSequence.substring(matcher.end()), (Phonemetadata.PhoneMetadata)object, stringBuilder, bl, phoneNumber);
                                if (n == 0) break block24;
                            }
                            if (n != 0) {
                                charSequence = this.getRegionCodeForCountryCode(n);
                                if (!charSequence.equals(object2)) {
                                    object = this.getMetadataForRegionOrCallingCode(n, (String)charSequence);
                                }
                                object2 = object;
                            } else {
                                stringBuilder.append(PhoneNumberUtil.normalize(charSequence));
                                if (object2 != null) {
                                    phoneNumber.setCountryCode(object.getCountryCode());
                                    object2 = object;
                                } else {
                                    object2 = object;
                                    if (bl) {
                                        phoneNumber.clearCountryCodeSource();
                                        object2 = object;
                                    }
                                }
                            }
                            if (stringBuilder.length() >= 2) {
                                object = stringBuilder;
                                if (object2 != null) {
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    charSequence = new StringBuilder(stringBuilder);
                                    this.maybeStripNationalPrefixAndCarrierCode((StringBuilder)charSequence, (Phonemetadata.PhoneMetadata)object2, stringBuilder2);
                                    object2 = this.testNumberLength(charSequence, (Phonemetadata.PhoneMetadata)object2);
                                    object = stringBuilder;
                                    if (object2 != ValidationResult.TOO_SHORT) {
                                        object = stringBuilder;
                                        if (object2 != ValidationResult.IS_POSSIBLE_LOCAL_ONLY) {
                                            object = stringBuilder;
                                            if (object2 != ValidationResult.INVALID_LENGTH) {
                                                object = object2 = charSequence;
                                                if (bl) {
                                                    object = object2;
                                                    if (stringBuilder2.length() > 0) {
                                                        phoneNumber.setPreferredDomesticCarrierCode(stringBuilder2.toString());
                                                        object = object2;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if ((n = object.length()) >= 2) {
                                    if (n <= 17) {
                                        PhoneNumberUtil.setItalianLeadingZerosForPhoneNumber((CharSequence)object, phoneNumber);
                                        phoneNumber.setNationalNumber(Long.parseLong(object.toString()));
                                        return;
                                    }
                                    throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied is too long to be a phone number.");
                                }
                                throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
                            }
                            throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
                        }
                        throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Could not interpret numbers after plus-sign.");
                    }
                    throw new NumberParseException(numberParseException2.getErrorType(), numberParseException2.getMessage());
                }
                throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The string supplied did not seem to be a phone number.");
            }
            throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied was too long to parse.");
        }
        throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The phone number supplied was null.");
    }

    private boolean parsePrefixAsIdd(Pattern object, StringBuilder stringBuilder) {
        if ((object = object.matcher(stringBuilder)).lookingAt()) {
            int n = object.end();
            object = CAPTURING_DIGIT_PATTERN.matcher(stringBuilder.substring(n));
            if (object.find() && PhoneNumberUtil.normalizeDigitsOnly(object.group(1)).equals("0")) {
                return false;
            }
            stringBuilder.delete(0, n);
            return true;
        }
        return false;
    }

    private void prefixNumberWithCountryCallingCode(int n, PhoneNumberFormat phoneNumberFormat, StringBuilder stringBuilder) {
        int n2 = .$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[phoneNumberFormat.ordinal()];
        if (n2 != 1) {
            if (n2 != 2) {
                if (n2 != 3) {
                    return;
                }
                stringBuilder.insert(0, "-").insert(0, n).insert(0, '+').insert(0, "tel:");
                return;
            }
            stringBuilder.insert(0, " ").insert(0, n).insert(0, '+');
            return;
        }
        stringBuilder.insert(0, n).insert(0, '+');
    }

    private boolean rawInputContainsNationalPrefix(String string2, String string3, String string4) {
        if ((string2 = PhoneNumberUtil.normalizeDigitsOnly(string2)).startsWith(string3)) {
            try {
                boolean bl = this.isValidNumber(this.parse(string2.substring(string3.length()), string4));
                return bl;
            }
            catch (NumberParseException numberParseException) {
                return false;
            }
        }
        return false;
    }

    static void setInstance(PhoneNumberUtil phoneNumberUtil) {
        synchronized (PhoneNumberUtil.class) {
            instance = phoneNumberUtil;
            return;
        }
    }

    static void setItalianLeadingZerosForPhoneNumber(CharSequence charSequence, Phonenumber.PhoneNumber phoneNumber) {
        if (charSequence.length() > 1 && charSequence.charAt(0) == '0') {
            int n;
            phoneNumber.setItalianLeadingZero(true);
            for (n = 1; n < charSequence.length() - 1 && charSequence.charAt(n) == '0'; ++n) {
            }
            if (n != 1) {
                phoneNumber.setNumberOfLeadingZeros(n);
            }
        }
    }

    private ValidationResult testNumberLength(CharSequence charSequence, Phonemetadata.PhoneMetadata phoneMetadata) {
        return this.testNumberLength(charSequence, phoneMetadata, PhoneNumberType.UNKNOWN);
    }

    private ValidationResult testNumberLength(CharSequence charSequence, Phonemetadata.PhoneMetadata list, PhoneNumberType object) {
        Object object2 = this.getNumberDescByType((Phonemetadata.PhoneMetadata)((Object)list), (PhoneNumberType)((Object)object));
        List<Integer> list2 = object2.getPossibleLengthList().isEmpty() ? list.getGeneralDesc().getPossibleLengthList() : object2.getPossibleLengthList();
        List<Integer> list3 = object2.getPossibleLengthLocalOnlyList();
        List<Integer> list4 = list2;
        object2 = list3;
        if (object == PhoneNumberType.FIXED_LINE_OR_MOBILE) {
            if (!PhoneNumberUtil.descHasPossibleNumberData(this.getNumberDescByType((Phonemetadata.PhoneMetadata)((Object)list), PhoneNumberType.FIXED_LINE))) {
                return this.testNumberLength(charSequence, (Phonemetadata.PhoneMetadata)((Object)list), PhoneNumberType.MOBILE);
            }
            object = this.getNumberDescByType((Phonemetadata.PhoneMetadata)((Object)list), PhoneNumberType.MOBILE);
            list4 = list2;
            object2 = list3;
            if (PhoneNumberUtil.descHasPossibleNumberData((Phonemetadata.PhoneNumberDesc)object)) {
                list4 = new ArrayList<Integer>(list2);
                list = object.getPossibleLengthList().size() == 0 ? list.getGeneralDesc().getPossibleLengthList() : object.getPossibleLengthList();
                list4.addAll(list);
                Collections.sort(list4);
                if (list3.isEmpty()) {
                    object2 = object.getPossibleLengthLocalOnlyList();
                } else {
                    object2 = new ArrayList<Integer>(list3);
                    object2.addAll(object.getPossibleLengthLocalOnlyList());
                    Collections.sort(object2);
                }
            }
        }
        if (list4.get(0) == -1) {
            return ValidationResult.INVALID_LENGTH;
        }
        int n = charSequence.length();
        if (object2.contains(n)) {
            return ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
        }
        int n2 = list4.get(0);
        if (n2 == n) {
            return ValidationResult.IS_POSSIBLE;
        }
        if (n2 > n) {
            return ValidationResult.TOO_SHORT;
        }
        if (list4.get(list4.size() - 1) < n) {
            return ValidationResult.TOO_LONG;
        }
        if (list4.subList(1, list4.size()).contains(n)) {
            return ValidationResult.IS_POSSIBLE;
        }
        return ValidationResult.INVALID_LENGTH;
    }

    public boolean canBeInternationallyDialled(Phonenumber.PhoneNumber phoneNumber) {
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegion(this.getRegionCodeForNumber(phoneNumber));
        if (phoneMetadata == null) {
            return true;
        }
        return true ^ this.isNumberMatchingDesc(this.getNationalSignificantNumber(phoneNumber), phoneMetadata.getNoInternationalDialling());
    }

    Phonemetadata.NumberFormat chooseFormattingPatternForNumber(List<Phonemetadata.NumberFormat> object, String string2) {
        object = object.iterator();
        while (object.hasNext()) {
            Phonemetadata.NumberFormat numberFormat = (Phonemetadata.NumberFormat)object.next();
            int n = numberFormat.leadingDigitsPatternSize();
            if (n != 0 && !this.regexCache.getPatternForRegex(numberFormat.getLeadingDigitsPattern(n - 1)).matcher(string2).lookingAt() || !this.regexCache.getPatternForRegex(numberFormat.getPattern()).matcher(string2).matches()) continue;
            return numberFormat;
        }
        return null;
    }

    int extractCountryCode(StringBuilder stringBuilder, StringBuilder stringBuilder2) {
        if (stringBuilder.length() != 0) {
            if (stringBuilder.charAt(0) == '0') {
                return 0;
            }
            int n = stringBuilder.length();
            for (int i = 1; i <= 3 && i <= n; ++i) {
                int n2 = Integer.parseInt(stringBuilder.substring(0, i));
                if (!this.countryCallingCodeToRegionCodeMap.containsKey(n2)) continue;
                stringBuilder2.append(stringBuilder.substring(i));
                return n2;
            }
            return 0;
        }
        return 0;
    }

    public Iterable<PhoneNumberMatch> findNumbers(CharSequence charSequence, String string2) {
        return this.findNumbers(charSequence, string2, Leniency.VALID, Long.MAX_VALUE);
    }

    public Iterable<PhoneNumberMatch> findNumbers(final CharSequence charSequence, final String string2, final Leniency leniency, final long l) {
        return new Iterable<PhoneNumberMatch>(){

            @Override
            public Iterator<PhoneNumberMatch> iterator() {
                return new PhoneNumberMatcher(PhoneNumberUtil.this, charSequence, string2, leniency, l);
            }
        };
    }

    public String format(Phonenumber.PhoneNumber phoneNumber, PhoneNumberFormat phoneNumberFormat) {
        StringBuilder stringBuilder;
        if (phoneNumber.getNationalNumber() == 0L && phoneNumber.hasRawInput() && (stringBuilder = phoneNumber.getRawInput()).length() > 0) {
            return stringBuilder;
        }
        stringBuilder = new StringBuilder(20);
        this.format(phoneNumber, phoneNumberFormat, stringBuilder);
        return stringBuilder.toString();
    }

    public void format(Phonenumber.PhoneNumber phoneNumber, PhoneNumberFormat phoneNumberFormat, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        int n = phoneNumber.getCountryCode();
        String string2 = this.getNationalSignificantNumber(phoneNumber);
        if (phoneNumberFormat == PhoneNumberFormat.E164) {
            stringBuilder.append(string2);
            this.prefixNumberWithCountryCallingCode(n, PhoneNumberFormat.E164, stringBuilder);
            return;
        }
        if (!this.hasValidCountryCallingCode(n)) {
            stringBuilder.append(string2);
            return;
        }
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n));
        stringBuilder.append(this.formatNsn(string2, phoneMetadata, phoneNumberFormat));
        this.maybeAppendFormattedExtension(phoneNumber, phoneMetadata, phoneNumberFormat, stringBuilder);
        this.prefixNumberWithCountryCallingCode(n, phoneNumberFormat, stringBuilder);
    }

    public String formatByPattern(Phonenumber.PhoneNumber phoneNumber, PhoneNumberFormat phoneNumberFormat, List<Phonemetadata.NumberFormat> object) {
        int n = phoneNumber.getCountryCode();
        String string2 = this.getNationalSignificantNumber(phoneNumber);
        if (!this.hasValidCountryCallingCode(n)) {
            return string2;
        }
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n));
        StringBuilder stringBuilder = new StringBuilder(20);
        Object object2 = this.chooseFormattingPatternForNumber((List<Phonemetadata.NumberFormat>)object, string2);
        if (object2 == null) {
            stringBuilder.append(string2);
        } else {
            object = Phonemetadata.NumberFormat.newBuilder();
            object.mergeFrom((Phonemetadata.NumberFormat)object2);
            object2 = object2.getNationalPrefixFormattingRule();
            if (object2.length() > 0) {
                String string3 = phoneMetadata.getNationalPrefix();
                if (string3.length() > 0) {
                    object.setNationalPrefixFormattingRule(object2.replace("$NP", string3).replace("$FG", "$1"));
                } else {
                    object.clearNationalPrefixFormattingRule();
                }
            }
            stringBuilder.append(this.formatNsnUsingPattern(string2, (Phonemetadata.NumberFormat)object, phoneNumberFormat));
        }
        this.maybeAppendFormattedExtension(phoneNumber, phoneMetadata, phoneNumberFormat, stringBuilder);
        this.prefixNumberWithCountryCallingCode(n, phoneNumberFormat, stringBuilder);
        return stringBuilder.toString();
    }

    public String formatInOriginalFormat(Phonenumber.PhoneNumber object, String object2) {
        ArrayList<Phonemetadata.NumberFormat> arrayList;
        if (object.hasRawInput() && !this.hasFormattingPatternForNumber((Phonenumber.PhoneNumber)object)) {
            return object.getRawInput();
        }
        if (!object.hasCountryCodeSource()) {
            return this.format((Phonenumber.PhoneNumber)object, PhoneNumberFormat.NATIONAL);
        }
        int n = .$SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[object.getCountryCodeSource().ordinal()];
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    arrayList = this.getRegionCodeForCountryCode(object.getCountryCode());
                    String string2 = this.getNddPrefixForRegion((String)((Object)arrayList), true);
                    object2 = this.format((Phonenumber.PhoneNumber)object, PhoneNumberFormat.NATIONAL);
                    if (string2 != null && string2.length() != 0 && !this.rawInputContainsNationalPrefix(object.getRawInput(), string2, (String)((Object)arrayList))) {
                        arrayList = this.getMetadataForRegion((String)((Object)arrayList));
                        string2 = this.getNationalSignificantNumber((Phonenumber.PhoneNumber)object);
                        if ((arrayList = this.chooseFormattingPatternForNumber(arrayList.numberFormats(), string2)) != null && (n = (string2 = arrayList.getNationalPrefixFormattingRule()).indexOf("$1")) > 0 && PhoneNumberUtil.normalizeDigitsOnly(string2.substring(0, n)).length() != 0) {
                            object2 = Phonemetadata.NumberFormat.newBuilder();
                            object2.mergeFrom((Phonemetadata.NumberFormat)((Object)arrayList));
                            object2.clearNationalPrefixFormattingRule();
                            arrayList = new ArrayList<Phonemetadata.NumberFormat>(1);
                            arrayList.add((Phonemetadata.NumberFormat)object2);
                            object2 = this.formatByPattern((Phonenumber.PhoneNumber)object, PhoneNumberFormat.NATIONAL, arrayList);
                        }
                    }
                } else {
                    object2 = this.format((Phonenumber.PhoneNumber)object, PhoneNumberFormat.INTERNATIONAL).substring(1);
                }
            } else {
                object2 = this.formatOutOfCountryCallingNumber((Phonenumber.PhoneNumber)object, (String)object2);
            }
        } else {
            object2 = this.format((Phonenumber.PhoneNumber)object, PhoneNumberFormat.INTERNATIONAL);
        }
        arrayList = object.getRawInput();
        object = object2;
        if (object2 != null) {
            object = object2;
            if (arrayList.length() > 0) {
                object = object2;
                if (!PhoneNumberUtil.normalizeDiallableCharsOnly((CharSequence)object2).equals(PhoneNumberUtil.normalizeDiallableCharsOnly((CharSequence)((Object)arrayList)))) {
                    object = arrayList;
                }
            }
        }
        return object;
    }

    public String formatNationalNumberWithCarrierCode(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence) {
        int n = phoneNumber.getCountryCode();
        String string2 = this.getNationalSignificantNumber(phoneNumber);
        if (!this.hasValidCountryCallingCode(n)) {
            return string2;
        }
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n));
        StringBuilder stringBuilder = new StringBuilder(20);
        stringBuilder.append(this.formatNsn(string2, phoneMetadata, PhoneNumberFormat.NATIONAL, charSequence));
        this.maybeAppendFormattedExtension(phoneNumber, phoneMetadata, PhoneNumberFormat.NATIONAL, stringBuilder);
        this.prefixNumberWithCountryCallingCode(n, PhoneNumberFormat.NATIONAL, stringBuilder);
        return stringBuilder.toString();
    }

    public String formatNationalNumberWithPreferredCarrierCode(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence) {
        if (phoneNumber.getPreferredDomesticCarrierCode().length() > 0) {
            charSequence = phoneNumber.getPreferredDomesticCarrierCode();
        }
        return this.formatNationalNumberWithCarrierCode(phoneNumber, charSequence);
    }

    String formatNsnUsingPattern(String string2, Phonemetadata.NumberFormat numberFormat, PhoneNumberFormat phoneNumberFormat) {
        return this.formatNsnUsingPattern(string2, numberFormat, phoneNumberFormat, null);
    }

    public String formatNumberForMobileDialing(Phonenumber.PhoneNumber object, String charSequence, boolean bl) {
        int n = object.getCountryCode();
        boolean bl2 = this.hasValidCountryCallingCode(n);
        String string2 = "";
        if (!bl2) {
            if (object.hasRawInput()) {
                string2 = object.getRawInput();
            }
            return string2;
        }
        String string3 = "";
        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber().mergeFrom((Phonenumber.PhoneNumber)object).clearExtension();
        object = this.getRegionCodeForCountryCode(n);
        PhoneNumberType phoneNumberType = this.getNumberType(phoneNumber);
        PhoneNumberType phoneNumberType2 = PhoneNumberType.UNKNOWN;
        boolean bl3 = false;
        boolean bl4 = phoneNumberType != phoneNumberType2;
        if (charSequence.equals(object)) {
            if (phoneNumberType == PhoneNumberType.FIXED_LINE || phoneNumberType == PhoneNumberType.MOBILE || phoneNumberType == PhoneNumberType.FIXED_LINE_OR_MOBILE) {
                bl3 = true;
            }
            if (object.equals("CO") && phoneNumberType == PhoneNumberType.FIXED_LINE) {
                object = this.formatNationalNumberWithCarrierCode(phoneNumber, "3");
            } else if (object.equals("BR") && bl3) {
                object = phoneNumber.getPreferredDomesticCarrierCode().length() > 0 ? this.formatNationalNumberWithPreferredCarrierCode(phoneNumber, "") : string2;
            } else if (bl4 && object.equals("HU")) {
                charSequence = new StringBuilder();
                charSequence.append(this.getNddPrefixForRegion((String)object, true));
                charSequence.append(" ");
                charSequence.append(this.format(phoneNumber, PhoneNumberFormat.NATIONAL));
                object = charSequence.toString();
            } else if (n == 1) {
                object = this.getMetadataForRegion((String)charSequence);
                object = this.canBeInternationallyDialled(phoneNumber) && this.testNumberLength(this.getNationalSignificantNumber(phoneNumber), (Phonemetadata.PhoneMetadata)object) != ValidationResult.TOO_SHORT ? this.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL) : this.format(phoneNumber, PhoneNumberFormat.NATIONAL);
            } else {
                object = (object.equals("001") || (object.equals("MX") || object.equals("CL") || object.equals("UZ")) && bl3) && this.canBeInternationallyDialled(phoneNumber) ? this.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL) : this.format(phoneNumber, PhoneNumberFormat.NATIONAL);
            }
        } else {
            object = string3;
            if (bl4) {
                object = string3;
                if (this.canBeInternationallyDialled(phoneNumber)) {
                    if (bl) {
                        return this.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL);
                    }
                    return this.format(phoneNumber, PhoneNumberFormat.E164);
                }
            }
        }
        if (bl) {
            return object;
        }
        return PhoneNumberUtil.normalizeDiallableCharsOnly((CharSequence)object);
    }

    public String formatOutOfCountryCallingNumber(Phonenumber.PhoneNumber phoneNumber, String string2) {
        if (!this.isValidRegionCode(string2)) {
            Logger logger = PhoneNumberUtil.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to format number from invalid region ");
            stringBuilder.append(string2);
            stringBuilder.append(". International formatting applied.");
            logger.log(level, stringBuilder.toString());
            return this.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL);
        }
        int n = phoneNumber.getCountryCode();
        CharSequence charSequence = this.getNationalSignificantNumber(phoneNumber);
        if (!this.hasValidCountryCallingCode(n)) {
            return charSequence;
        }
        if (n == 1) {
            if (this.isNANPACountry(string2)) {
                string2 = new StringBuilder();
                string2.append(n);
                string2.append(" ");
                string2.append(this.format(phoneNumber, PhoneNumberFormat.NATIONAL));
                return string2.toString();
            }
        } else if (n == this.getCountryCodeForValidRegion(string2)) {
            return this.format(phoneNumber, PhoneNumberFormat.NATIONAL);
        }
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegion(string2);
        string2 = phoneMetadata.getInternationalPrefix();
        Object object = "";
        if (!SINGLE_INTERNATIONAL_PREFIX.matcher(string2).matches()) {
            string2 = object;
            if (phoneMetadata.hasPreferredInternationalPrefix()) {
                string2 = phoneMetadata.getPreferredInternationalPrefix();
            }
        }
        object = this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n));
        charSequence = new StringBuilder(this.formatNsn((String)charSequence, (Phonemetadata.PhoneMetadata)object, PhoneNumberFormat.INTERNATIONAL));
        this.maybeAppendFormattedExtension(phoneNumber, (Phonemetadata.PhoneMetadata)object, PhoneNumberFormat.INTERNATIONAL, (StringBuilder)charSequence);
        if (string2.length() > 0) {
            charSequence.insert(0, " ").insert(0, n).insert(0, " ").insert(0, string2);
        } else {
            this.prefixNumberWithCountryCallingCode(n, PhoneNumberFormat.INTERNATIONAL, (StringBuilder)charSequence);
        }
        return charSequence.toString();
    }

    public String formatOutOfCountryKeepingAlphaChars(Phonenumber.PhoneNumber object, String object2) {
        Object object3 = object.getRawInput();
        if (object3.length() == 0) {
            return this.formatOutOfCountryCallingNumber((Phonenumber.PhoneNumber)object, (String)object2);
        }
        int n = object.getCountryCode();
        if (!this.hasValidCountryCallingCode(n)) {
            return object3;
        }
        object3 = PhoneNumberUtil.normalizeHelper((CharSequence)object3, ALL_PLUS_NUMBER_GROUPING_SYMBOLS, true);
        String string2 = this.getNationalSignificantNumber((Phonenumber.PhoneNumber)object);
        CharSequence charSequence = object3;
        if (string2.length() > 3) {
            int n2 = object3.indexOf(string2.substring(0, 3));
            charSequence = object3;
            if (n2 != -1) {
                charSequence = object3.substring(n2);
            }
        }
        Serializable serializable = this.getMetadataForRegion((String)object2);
        if (n == 1) {
            if (this.isNANPACountry((String)object2)) {
                object = new StringBuilder();
                object.append(n);
                object.append(" ");
                object.append((String)charSequence);
                return object.toString();
            }
        } else if (serializable != null && n == this.getCountryCodeForValidRegion((String)object2)) {
            object = this.chooseFormattingPatternForNumber(serializable.numberFormats(), string2);
            if (object == null) {
                return charSequence;
            }
            object2 = Phonemetadata.NumberFormat.newBuilder();
            object2.mergeFrom((Phonemetadata.NumberFormat)object);
            object2.setPattern("(\\d+)(.*)");
            object2.setFormat("$1$2");
            return this.formatNsnUsingPattern((String)charSequence, (Phonemetadata.NumberFormat)object2, PhoneNumberFormat.NATIONAL);
        }
        object3 = "";
        if (serializable != null && !SINGLE_INTERNATIONAL_PREFIX.matcher((CharSequence)(object3 = serializable.getInternationalPrefix())).matches()) {
            object3 = serializable.getPreferredInternationalPrefix();
        }
        charSequence = new StringBuilder((String)charSequence);
        this.maybeAppendFormattedExtension((Phonenumber.PhoneNumber)object, this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n)), PhoneNumberFormat.INTERNATIONAL, (StringBuilder)charSequence);
        if (object3.length() > 0) {
            charSequence.insert(0, " ").insert(0, n).insert(0, " ").insert(0, (String)object3);
        } else {
            if (!this.isValidRegionCode((String)object2)) {
                object = logger;
                object3 = Level.WARNING;
                serializable = new StringBuilder();
                serializable.append("Trying to format number from invalid region ");
                serializable.append((String)object2);
                serializable.append(". International formatting applied.");
                object.log((Level)object3, serializable.toString());
            }
            this.prefixNumberWithCountryCallingCode(n, PhoneNumberFormat.INTERNATIONAL, (StringBuilder)charSequence);
        }
        return charSequence.toString();
    }

    public AsYouTypeFormatter getAsYouTypeFormatter(String string2) {
        return new AsYouTypeFormatter(string2);
    }

    public int getCountryCodeForRegion(String string2) {
        if (!this.isValidRegionCode(string2)) {
            Logger logger = PhoneNumberUtil.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or missing region code (");
            if (string2 == null) {
                string2 = "null";
            }
            stringBuilder.append(string2);
            stringBuilder.append(") provided.");
            logger.log(level, stringBuilder.toString());
            return 0;
        }
        return this.getCountryCodeForValidRegion(string2);
    }

    public Phonenumber.PhoneNumber getExampleNumber(String string2) {
        return this.getExampleNumberForType(string2, PhoneNumberType.FIXED_LINE);
    }

    public Phonenumber.PhoneNumber getExampleNumberForNonGeoEntity(int n) {
        Object object = this.getMetadataForNonGeographicalRegion(n);
        if (object != null) {
            object = Arrays.asList(object.getMobile(), object.getTollFree(), object.getSharedCost(), object.getVoip(), object.getVoicemail(), object.getUan(), object.getPremiumRate()).iterator();
            while (object.hasNext()) {
                Serializable serializable = object.next();
                if (serializable == null) continue;
                try {
                    if (!serializable.hasExampleNumber()) continue;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("+");
                    stringBuilder.append(n);
                    stringBuilder.append(serializable.getExampleNumber());
                    serializable = this.parse(stringBuilder.toString(), "ZZ");
                    return serializable;
                }
                catch (NumberParseException numberParseException) {
                    logger.log(Level.SEVERE, numberParseException.toString());
                }
            }
        } else {
            object = logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or unknown country calling code provided: ");
            stringBuilder.append(n);
            object.log(level, stringBuilder.toString());
        }
        return null;
    }

    public Phonenumber.PhoneNumber getExampleNumberForType(PhoneNumberType phoneNumberType) {
        Iterator iterator = this.getSupportedRegions().iterator();
        while (iterator.hasNext()) {
            Phonenumber.PhoneNumber phoneNumber = this.getExampleNumberForType(iterator.next(), phoneNumberType);
            if (phoneNumber == null) continue;
            return phoneNumber;
        }
        iterator = this.getSupportedGlobalNetworkCallingCodes().iterator();
        while (iterator.hasNext()) {
            int n = (Integer)iterator.next();
            Phonemetadata.PhoneNumberDesc phoneNumberDesc = this.getNumberDescByType(this.getMetadataForNonGeographicalRegion(n), phoneNumberType);
            try {
                if (!phoneNumberDesc.hasExampleNumber()) continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("+");
                stringBuilder.append(n);
                stringBuilder.append(phoneNumberDesc.getExampleNumber());
                Phonenumber.PhoneNumber phoneNumber = this.parse(stringBuilder.toString(), "ZZ");
                return phoneNumber;
            }
            catch (NumberParseException numberParseException) {
                logger.log(Level.SEVERE, numberParseException.toString());
            }
        }
        return null;
    }

    public Phonenumber.PhoneNumber getExampleNumberForType(String object, PhoneNumberType object2) {
        if (!this.isValidRegionCode((String)object)) {
            object2 = logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or unknown region code provided: ");
            stringBuilder.append((String)object);
            object2.log(level, stringBuilder.toString());
            return null;
        }
        object2 = this.getNumberDescByType(this.getMetadataForRegion((String)object), (PhoneNumberType)((Object)object2));
        try {
            if (object2.hasExampleNumber()) {
                object = this.parse(object2.getExampleNumber(), (String)object);
                return object;
            }
            return null;
        }
        catch (NumberParseException numberParseException) {
            logger.log(Level.SEVERE, numberParseException.toString());
            return null;
        }
    }

    public Phonenumber.PhoneNumber getInvalidExampleNumber(String string2) {
        if (!this.isValidRegionCode(string2)) {
            Logger logger = PhoneNumberUtil.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or unknown region code provided: ");
            stringBuilder.append(string2);
            logger.log(level, stringBuilder.toString());
            return null;
        }
        Object object = this.getNumberDescByType(this.getMetadataForRegion(string2), PhoneNumberType.FIXED_LINE);
        if (!object.hasExampleNumber()) {
            return null;
        }
        object = object.getExampleNumber();
        for (int i = object.length() - 1; i >= 2; --i) {
            Object object2 = object.substring(0, i);
            try {
                object2 = this.parse((CharSequence)object2, string2);
                boolean bl = this.isValidNumber((Phonenumber.PhoneNumber)object2);
                if (bl) continue;
                return object2;
            }
            catch (NumberParseException numberParseException) {
                // empty catch block
            }
        }
        return null;
    }

    public int getLengthOfGeographicalAreaCode(Phonenumber.PhoneNumber phoneNumber) {
        Object object = this.getMetadataForRegion(this.getRegionCodeForNumber(phoneNumber));
        if (object == null) {
            return 0;
        }
        if (!object.hasNationalPrefix() && !phoneNumber.isItalianLeadingZero()) {
            return 0;
        }
        object = this.getNumberType(phoneNumber);
        int n = phoneNumber.getCountryCode();
        if (object == PhoneNumberType.MOBILE && GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES.contains(n)) {
            return 0;
        }
        if (!this.isNumberGeographical((PhoneNumberType)((Object)object), n)) {
            return 0;
        }
        return this.getLengthOfNationalDestinationCode(phoneNumber);
    }

    public int getLengthOfNationalDestinationCode(Phonenumber.PhoneNumber phoneNumber) {
        String[] arrstring;
        if (phoneNumber.hasExtension()) {
            arrstring = new Phonenumber.PhoneNumber();
            arrstring.mergeFrom(phoneNumber);
            arrstring.clearExtension();
        } else {
            arrstring = phoneNumber;
        }
        arrstring = this.format((Phonenumber.PhoneNumber)arrstring, PhoneNumberFormat.INTERNATIONAL);
        arrstring = NON_DIGITS_PATTERN.split((CharSequence)arrstring);
        if (arrstring.length <= 3) {
            return 0;
        }
        if (this.getNumberType(phoneNumber) == PhoneNumberType.MOBILE && !PhoneNumberUtil.getCountryMobileToken(phoneNumber.getCountryCode()).equals("")) {
            return arrstring[2].length() + arrstring[3].length();
        }
        return arrstring[2].length();
    }

    Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int n) {
        if (!this.countryCallingCodeToRegionCodeMap.containsKey(n)) {
            return null;
        }
        return this.metadataSource.getMetadataForNonGeographicalRegion(n);
    }

    Phonemetadata.PhoneMetadata getMetadataForRegion(String string2) {
        if (!this.isValidRegionCode(string2)) {
            return null;
        }
        return this.metadataSource.getMetadataForRegion(string2);
    }

    public String getNationalSignificantNumber(Phonenumber.PhoneNumber phoneNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        if (phoneNumber.isItalianLeadingZero() && phoneNumber.getNumberOfLeadingZeros() > 0) {
            char[] arrc = new char[phoneNumber.getNumberOfLeadingZeros()];
            Arrays.fill(arrc, '0');
            stringBuilder.append(new String(arrc));
        }
        stringBuilder.append(phoneNumber.getNationalNumber());
        return stringBuilder.toString();
    }

    public String getNddPrefixForRegion(String object, boolean bl) {
        Object object2 = this.getMetadataForRegion((String)object);
        if (object2 == null) {
            object2 = logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or missing region code (");
            if (object == null) {
                object = "null";
            }
            stringBuilder.append((String)object);
            stringBuilder.append(") provided.");
            object2.log(level, stringBuilder.toString());
            return null;
        }
        if ((object2 = object2.getNationalPrefix()).length() == 0) {
            return null;
        }
        object = object2;
        if (bl) {
            object = object2.replace("~", "");
        }
        return object;
    }

    Phonemetadata.PhoneNumberDesc getNumberDescByType(Phonemetadata.PhoneMetadata phoneMetadata, PhoneNumberType phoneNumberType) {
        switch (.$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[phoneNumberType.ordinal()]) {
            default: {
                return phoneMetadata.getGeneralDesc();
            }
            case 11: {
                return phoneMetadata.getVoicemail();
            }
            case 10: {
                return phoneMetadata.getUan();
            }
            case 9: {
                return phoneMetadata.getPager();
            }
            case 8: {
                return phoneMetadata.getPersonalNumber();
            }
            case 7: {
                return phoneMetadata.getVoip();
            }
            case 6: {
                return phoneMetadata.getSharedCost();
            }
            case 4: 
            case 5: {
                return phoneMetadata.getFixedLine();
            }
            case 3: {
                return phoneMetadata.getMobile();
            }
            case 2: {
                return phoneMetadata.getTollFree();
            }
            case 1: 
        }
        return phoneMetadata.getPremiumRate();
    }

    public PhoneNumberType getNumberType(Phonenumber.PhoneNumber phoneNumber) {
        Object object = this.getRegionCodeForNumber(phoneNumber);
        object = this.getMetadataForRegionOrCallingCode(phoneNumber.getCountryCode(), (String)object);
        if (object == null) {
            return PhoneNumberType.UNKNOWN;
        }
        return this.getNumberTypeHelper(this.getNationalSignificantNumber(phoneNumber), (Phonemetadata.PhoneMetadata)object);
    }

    public String getRegionCodeForCountryCode(int n) {
        List<String> list = this.countryCallingCodeToRegionCodeMap.get(n);
        if (list == null) {
            return "ZZ";
        }
        return list.get(0);
    }

    public String getRegionCodeForNumber(Phonenumber.PhoneNumber object) {
        int n = object.getCountryCode();
        List<String> list = this.countryCallingCodeToRegionCodeMap.get(n);
        if (list == null) {
            object = logger;
            list = Level.INFO;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Missing/invalid country_code (");
            stringBuilder.append(n);
            stringBuilder.append(")");
            object.log((Level)((Object)list), stringBuilder.toString());
            return null;
        }
        if (list.size() == 1) {
            return (String)list.get(0);
        }
        return this.getRegionCodeForNumberFromRegionList((Phonenumber.PhoneNumber)object, list);
    }

    public List<String> getRegionCodesForCountryCode(int n) {
        List<String> list = this.countryCallingCodeToRegionCodeMap.get(n);
        if (list == null) {
            list = new ArrayList<String>(0);
        }
        return Collections.unmodifiableList(list);
    }

    public Set<Integer> getSupportedCallingCodes() {
        return Collections.unmodifiableSet(this.countryCallingCodeToRegionCodeMap.keySet());
    }

    public Set<Integer> getSupportedGlobalNetworkCallingCodes() {
        return Collections.unmodifiableSet(this.countryCodesForNonGeographicalRegion);
    }

    public Set<String> getSupportedRegions() {
        return Collections.unmodifiableSet(this.supportedRegions);
    }

    public Set<PhoneNumberType> getSupportedTypesForNonGeoEntity(int n) {
        Object object = this.getMetadataForNonGeographicalRegion(n);
        if (object == null) {
            object = logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown country calling code for a non-geographical entity provided: ");
            stringBuilder.append(n);
            object.log(level, stringBuilder.toString());
            return Collections.unmodifiableSet(new TreeSet());
        }
        return this.getSupportedTypesForMetadata((Phonemetadata.PhoneMetadata)object);
    }

    public Set<PhoneNumberType> getSupportedTypesForRegion(String string2) {
        if (!this.isValidRegionCode(string2)) {
            Logger logger = PhoneNumberUtil.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or unknown region code provided: ");
            stringBuilder.append(string2);
            logger.log(level, stringBuilder.toString());
            return Collections.unmodifiableSet(new TreeSet());
        }
        return this.getSupportedTypesForMetadata(this.getMetadataForRegion(string2));
    }

    public boolean isAlphaNumber(CharSequence charSequence) {
        if (!PhoneNumberUtil.isViablePhoneNumber(charSequence)) {
            return false;
        }
        charSequence = new StringBuilder(charSequence);
        this.maybeStripExtension((StringBuilder)charSequence);
        return VALID_ALPHA_PHONE_PATTERN.matcher(charSequence).matches();
    }

    public boolean isMobileNumberPortableRegion(String string2) {
        Object object = this.getMetadataForRegion(string2);
        if (object == null) {
            object = logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid or unknown region code provided: ");
            stringBuilder.append(string2);
            object.log(level, stringBuilder.toString());
            return false;
        }
        return object.isMobileNumberPortableRegion();
    }

    public boolean isNANPACountry(String string2) {
        return this.nanpaRegions.contains(string2);
    }

    public boolean isNumberGeographical(PhoneNumberType phoneNumberType, int n) {
        if (!(phoneNumberType == PhoneNumberType.FIXED_LINE || phoneNumberType == PhoneNumberType.FIXED_LINE_OR_MOBILE || GEO_MOBILE_COUNTRIES.contains(n) && phoneNumberType == PhoneNumberType.MOBILE)) {
            return false;
        }
        return true;
    }

    public boolean isNumberGeographical(Phonenumber.PhoneNumber phoneNumber) {
        return this.isNumberGeographical(this.getNumberType(phoneNumber), phoneNumber.getCountryCode());
    }

    public MatchType isNumberMatch(Phonenumber.PhoneNumber phoneNumber, Phonenumber.PhoneNumber phoneNumber2) {
        phoneNumber = PhoneNumberUtil.copyCoreFieldsOnly(phoneNumber);
        phoneNumber2 = PhoneNumberUtil.copyCoreFieldsOnly(phoneNumber2);
        if (phoneNumber.hasExtension() && phoneNumber2.hasExtension() && !phoneNumber.getExtension().equals(phoneNumber2.getExtension())) {
            return MatchType.NO_MATCH;
        }
        int n = phoneNumber.getCountryCode();
        int n2 = phoneNumber2.getCountryCode();
        if (n != 0 && n2 != 0) {
            if (phoneNumber.exactlySameAs(phoneNumber2)) {
                return MatchType.EXACT_MATCH;
            }
            if (n == n2 && this.isNationalNumberSuffixOfTheOther(phoneNumber, phoneNumber2)) {
                return MatchType.SHORT_NSN_MATCH;
            }
            return MatchType.NO_MATCH;
        }
        phoneNumber.setCountryCode(n2);
        if (phoneNumber.exactlySameAs(phoneNumber2)) {
            return MatchType.NSN_MATCH;
        }
        if (this.isNationalNumberSuffixOfTheOther(phoneNumber, phoneNumber2)) {
            return MatchType.SHORT_NSN_MATCH;
        }
        return MatchType.NO_MATCH;
    }

    public MatchType isNumberMatch(Phonenumber.PhoneNumber object, CharSequence charSequence) {
        try {
            MatchType matchType = this.isNumberMatch((Phonenumber.PhoneNumber)object, this.parse(charSequence, "ZZ"));
            return matchType;
        }
        catch (NumberParseException numberParseException) {
            block7 : {
                if (numberParseException.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                    Object object2 = this.getRegionCodeForCountryCode(object.getCountryCode());
                    try {
                        if (!object2.equals("ZZ")) {
                            if ((object = this.isNumberMatch((Phonenumber.PhoneNumber)object, this.parse(charSequence, (String)object2))) == MatchType.EXACT_MATCH) {
                                return MatchType.NSN_MATCH;
                            }
                            break block7;
                        }
                        object2 = new Phonenumber.PhoneNumber();
                        this.parseHelper(charSequence, null, false, false, (Phonenumber.PhoneNumber)object2);
                        object = this.isNumberMatch((Phonenumber.PhoneNumber)object, (Phonenumber.PhoneNumber)object2);
                        return object;
                    }
                    catch (NumberParseException numberParseException2) {
                        // empty catch block
                    }
                }
                return MatchType.NOT_A_NUMBER;
            }
            return object;
        }
    }

    public MatchType isNumberMatch(CharSequence object, CharSequence charSequence) {
        try {
            MatchType matchType = this.isNumberMatch(this.parse((CharSequence)object, "ZZ"), charSequence);
            return matchType;
        }
        catch (NumberParseException numberParseException) {
            block7 : {
                if (numberParseException.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                    try {
                        MatchType matchType = this.isNumberMatch(this.parse(charSequence, "ZZ"), (CharSequence)object);
                        return matchType;
                    }
                    catch (NumberParseException numberParseException2) {
                        if (numberParseException2.getErrorType() != NumberParseException.ErrorType.INVALID_COUNTRY_CODE) break block7;
                        try {
                            Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
                            Phonenumber.PhoneNumber phoneNumber2 = new Phonenumber.PhoneNumber();
                            this.parseHelper((CharSequence)object, null, false, false, phoneNumber);
                            this.parseHelper(charSequence, null, false, false, phoneNumber2);
                            object = this.isNumberMatch(phoneNumber, phoneNumber2);
                            return object;
                        }
                        catch (NumberParseException numberParseException3) {
                            // empty catch block
                        }
                    }
                }
            }
            return MatchType.NOT_A_NUMBER;
        }
    }

    boolean isNumberMatchingDesc(String string2, Phonemetadata.PhoneNumberDesc phoneNumberDesc) {
        int n = string2.length();
        List<Integer> list = phoneNumberDesc.getPossibleLengthList();
        if (list.size() > 0 && !list.contains(n)) {
            return false;
        }
        return this.matcherApi.matchNationalNumber(string2, phoneNumberDesc, false);
    }

    public boolean isPossibleNumber(Phonenumber.PhoneNumber object) {
        if ((object = this.isPossibleNumberWithReason((Phonenumber.PhoneNumber)object)) != ValidationResult.IS_POSSIBLE && object != ValidationResult.IS_POSSIBLE_LOCAL_ONLY) {
            return false;
        }
        return true;
    }

    public boolean isPossibleNumber(CharSequence charSequence, String string2) {
        try {
            boolean bl = this.isPossibleNumber(this.parse(charSequence, string2));
            return bl;
        }
        catch (NumberParseException numberParseException) {
            return false;
        }
    }

    public boolean isPossibleNumberForType(Phonenumber.PhoneNumber object, PhoneNumberType phoneNumberType) {
        if ((object = this.isPossibleNumberForTypeWithReason((Phonenumber.PhoneNumber)object, phoneNumberType)) != ValidationResult.IS_POSSIBLE && object != ValidationResult.IS_POSSIBLE_LOCAL_ONLY) {
            return false;
        }
        return true;
    }

    public ValidationResult isPossibleNumberForTypeWithReason(Phonenumber.PhoneNumber phoneNumber, PhoneNumberType phoneNumberType) {
        String string2 = this.getNationalSignificantNumber(phoneNumber);
        int n = phoneNumber.getCountryCode();
        if (!this.hasValidCountryCallingCode(n)) {
            return ValidationResult.INVALID_COUNTRY_CODE;
        }
        return this.testNumberLength(string2, this.getMetadataForRegionOrCallingCode(n, this.getRegionCodeForCountryCode(n)), phoneNumberType);
    }

    public ValidationResult isPossibleNumberWithReason(Phonenumber.PhoneNumber phoneNumber) {
        return this.isPossibleNumberForTypeWithReason(phoneNumber, PhoneNumberType.UNKNOWN);
    }

    public boolean isValidNumber(Phonenumber.PhoneNumber phoneNumber) {
        return this.isValidNumberForRegion(phoneNumber, this.getRegionCodeForNumber(phoneNumber));
    }

    public boolean isValidNumberForRegion(Phonenumber.PhoneNumber phoneNumber, String string2) {
        int n = phoneNumber.getCountryCode();
        Phonemetadata.PhoneMetadata phoneMetadata = this.getMetadataForRegionOrCallingCode(n, string2);
        boolean bl = false;
        if (phoneMetadata != null) {
            if (!"001".equals(string2) && n != this.getCountryCodeForValidRegion(string2)) {
                return false;
            }
            if (this.getNumberTypeHelper(this.getNationalSignificantNumber(phoneNumber), phoneMetadata) != PhoneNumberType.UNKNOWN) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    int maybeExtractCountryCode(CharSequence object, Phonemetadata.PhoneMetadata phoneMetadata, StringBuilder stringBuilder, boolean bl, Phonenumber.PhoneNumber phoneNumber) throws NumberParseException {
        if (object.length() == 0) {
            return 0;
        }
        StringBuilder stringBuilder2 = new StringBuilder((CharSequence)object);
        object = "NonMatch";
        if (phoneMetadata != null) {
            object = phoneMetadata.getInternationalPrefix();
        }
        object = this.maybeStripInternationalPrefixAndNormalize(stringBuilder2, (String)object);
        if (bl) {
            phoneNumber.setCountryCodeSource((Phonenumber.PhoneNumber.CountryCodeSource)((Object)object));
        }
        if (object != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            if (stringBuilder2.length() > 2) {
                int n = this.extractCountryCode(stringBuilder2, stringBuilder);
                if (n != 0) {
                    phoneNumber.setCountryCode(n);
                    return n;
                }
                throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Country calling code supplied was not recognised.");
            }
            throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD, "Phone number had an IDD, but after this was not long enough to be a viable phone number.");
        }
        if (phoneMetadata != null) {
            int n = phoneMetadata.getCountryCode();
            object = String.valueOf(n);
            Object object2 = stringBuilder2.toString();
            if (object2.startsWith((String)object)) {
                object = new StringBuilder(object2.substring(object.length()));
                object2 = phoneMetadata.getGeneralDesc();
                this.maybeStripNationalPrefixAndCarrierCode((StringBuilder)object, phoneMetadata, null);
                if (!this.matcherApi.matchNationalNumber(stringBuilder2, (Phonemetadata.PhoneNumberDesc)object2, false) && this.matcherApi.matchNationalNumber((CharSequence)object, (Phonemetadata.PhoneNumberDesc)object2, false) || this.testNumberLength(stringBuilder2, phoneMetadata) == ValidationResult.TOO_LONG) {
                    stringBuilder.append((CharSequence)object);
                    if (bl) {
                        phoneNumber.setCountryCodeSource(Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN);
                    }
                    phoneNumber.setCountryCode(n);
                    return n;
                }
            }
        }
        phoneNumber.setCountryCode(0);
        return 0;
    }

    String maybeStripExtension(StringBuilder stringBuilder) {
        Matcher matcher = EXTN_PATTERN.matcher(stringBuilder);
        if (matcher.find() && PhoneNumberUtil.isViablePhoneNumber(stringBuilder.substring(0, matcher.start()))) {
            int n = matcher.groupCount();
            for (int i = 1; i <= n; ++i) {
                if (matcher.group(i) == null) continue;
                String string2 = matcher.group(i);
                stringBuilder.delete(matcher.start(), stringBuilder.length());
                return string2;
            }
        }
        return "";
    }

    Phonenumber.PhoneNumber.CountryCodeSource maybeStripInternationalPrefixAndNormalize(StringBuilder stringBuilder, String object) {
        if (stringBuilder.length() == 0) {
            return Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
        }
        Matcher matcher = PLUS_CHARS_PATTERN.matcher(stringBuilder);
        if (matcher.lookingAt()) {
            stringBuilder.delete(0, matcher.end());
            PhoneNumberUtil.normalize(stringBuilder);
            return Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN;
        }
        object = this.regexCache.getPatternForRegex((String)object);
        PhoneNumberUtil.normalize(stringBuilder);
        if (this.parsePrefixAsIdd((Pattern)object, stringBuilder)) {
            return Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_IDD;
        }
        return Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
    }

    boolean maybeStripNationalPrefixAndCarrierCode(StringBuilder stringBuilder, Phonemetadata.PhoneMetadata object, StringBuilder stringBuilder2) {
        int n = stringBuilder.length();
        Object object2 = object.getNationalPrefixForParsing();
        if (n != 0) {
            if (object2.length() == 0) {
                return false;
            }
            if ((object2 = this.regexCache.getPatternForRegex((String)object2).matcher(stringBuilder)).lookingAt()) {
                Phonemetadata.PhoneNumberDesc phoneNumberDesc = object.getGeneralDesc();
                boolean bl = this.matcherApi.matchNationalNumber(stringBuilder, phoneNumberDesc, false);
                int n2 = object2.groupCount();
                if ((object = object.getNationalPrefixTransformRule()) != null && object.length() != 0 && object2.group(n2) != null) {
                    StringBuilder stringBuilder3 = new StringBuilder(stringBuilder);
                    stringBuilder3.replace(0, n, object2.replaceFirst((String)object));
                    if (bl && !this.matcherApi.matchNationalNumber(stringBuilder3.toString(), phoneNumberDesc, false)) {
                        return false;
                    }
                    if (stringBuilder2 != null && n2 > 1) {
                        stringBuilder2.append(object2.group(1));
                    }
                    stringBuilder.replace(0, stringBuilder.length(), stringBuilder3.toString());
                    return true;
                }
                if (bl && !this.matcherApi.matchNationalNumber(stringBuilder.substring(object2.end()), phoneNumberDesc, false)) {
                    return false;
                }
                if (stringBuilder2 != null && n2 > 0 && object2.group(n2) != null) {
                    stringBuilder2.append(object2.group(1));
                }
                stringBuilder.delete(0, object2.end());
                return true;
            }
            return false;
        }
        return false;
    }

    public Phonenumber.PhoneNumber parse(CharSequence charSequence, String string2) throws NumberParseException {
        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
        this.parse(charSequence, string2, phoneNumber);
        return phoneNumber;
    }

    public void parse(CharSequence charSequence, String string2, Phonenumber.PhoneNumber phoneNumber) throws NumberParseException {
        this.parseHelper(charSequence, string2, false, true, phoneNumber);
    }

    public Phonenumber.PhoneNumber parseAndKeepRawInput(CharSequence charSequence, String string2) throws NumberParseException {
        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
        this.parseAndKeepRawInput(charSequence, string2, phoneNumber);
        return phoneNumber;
    }

    public void parseAndKeepRawInput(CharSequence charSequence, String string2, Phonenumber.PhoneNumber phoneNumber) throws NumberParseException {
        this.parseHelper(charSequence, string2, true, true, phoneNumber);
    }

    public boolean truncateTooLongNumber(Phonenumber.PhoneNumber phoneNumber) {
        block2 : {
            long l;
            if (this.isValidNumber(phoneNumber)) {
                return true;
            }
            Phonenumber.PhoneNumber phoneNumber2 = new Phonenumber.PhoneNumber();
            phoneNumber2.mergeFrom(phoneNumber);
            long l2 = phoneNumber.getNationalNumber();
            do {
                l = l2 / 10L;
                phoneNumber2.setNationalNumber(l);
                if (this.isPossibleNumberWithReason(phoneNumber2) == ValidationResult.TOO_SHORT || l == 0L) break block2;
                l2 = l;
            } while (!this.isValidNumber(phoneNumber2));
            phoneNumber.setNationalNumber(l);
            return true;
        }
        return false;
    }

    public static abstract class Leniency
    extends Enum<Leniency> {
        private static final /* synthetic */ Leniency[] $VALUES;
        public static final /* enum */ Leniency EXACT_GROUPING;
        public static final /* enum */ Leniency POSSIBLE;
        public static final /* enum */ Leniency STRICT_GROUPING;
        public static final /* enum */ Leniency VALID;

        static {
            Leniency leniency;
            POSSIBLE = new Leniency("POSSIBLE", 0){

                @Override
                boolean verify(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil) {
                    return phoneNumberUtil.isPossibleNumber(phoneNumber);
                }
            };
            VALID = new Leniency("VALID", 1){

                @Override
                boolean verify(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil) {
                    if (phoneNumberUtil.isValidNumber(phoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phoneNumber, charSequence.toString(), phoneNumberUtil)) {
                        return PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phoneNumber, phoneNumberUtil);
                    }
                    return false;
                }
            };
            STRICT_GROUPING = new Leniency("STRICT_GROUPING", 2){

                @Override
                boolean verify(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil) {
                    String string2 = charSequence.toString();
                    if (phoneNumberUtil.isValidNumber(phoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phoneNumber, string2, phoneNumberUtil) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(phoneNumber, string2) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phoneNumber, phoneNumberUtil)) {
                        return PhoneNumberMatcher.checkNumberGroupingIsValid(phoneNumber, charSequence, phoneNumberUtil, new PhoneNumberMatcher.NumberGroupingChecker(){

                            @Override
                            public boolean checkGroups(PhoneNumberUtil phoneNumberUtil, Phonenumber.PhoneNumber phoneNumber, StringBuilder stringBuilder, String[] arrstring) {
                                return PhoneNumberMatcher.allNumberGroupsRemainGrouped(phoneNumberUtil, phoneNumber, stringBuilder, arrstring);
                            }
                        });
                    }
                    return false;
                }

            };
            EXACT_GROUPING = leniency = new Leniency("EXACT_GROUPING", 3){

                @Override
                boolean verify(Phonenumber.PhoneNumber phoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil) {
                    String string2 = charSequence.toString();
                    if (phoneNumberUtil.isValidNumber(phoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phoneNumber, string2, phoneNumberUtil) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(phoneNumber, string2) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phoneNumber, phoneNumberUtil)) {
                        return PhoneNumberMatcher.checkNumberGroupingIsValid(phoneNumber, charSequence, phoneNumberUtil, new PhoneNumberMatcher.NumberGroupingChecker(){

                            @Override
                            public boolean checkGroups(PhoneNumberUtil phoneNumberUtil, Phonenumber.PhoneNumber phoneNumber, StringBuilder stringBuilder, String[] arrstring) {
                                return PhoneNumberMatcher.allNumberGroupsAreExactlyPresent(phoneNumberUtil, phoneNumber, stringBuilder, arrstring);
                            }
                        });
                    }
                    return false;
                }

            };
            $VALUES = new Leniency[]{POSSIBLE, VALID, STRICT_GROUPING, leniency};
        }

        private Leniency() {
        }

        public static Leniency valueOf(String string2) {
            return Enum.valueOf(Leniency.class, string2);
        }

        public static Leniency[] values() {
            return (Leniency[])$VALUES.clone();
        }

        abstract boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3);

    }

    public static final class MatchType
    extends Enum<MatchType> {
        private static final /* synthetic */ MatchType[] $VALUES;
        public static final /* enum */ MatchType EXACT_MATCH;
        public static final /* enum */ MatchType NOT_A_NUMBER;
        public static final /* enum */ MatchType NO_MATCH;
        public static final /* enum */ MatchType NSN_MATCH;
        public static final /* enum */ MatchType SHORT_NSN_MATCH;

        static {
            MatchType matchType;
            NOT_A_NUMBER = new MatchType();
            NO_MATCH = new MatchType();
            SHORT_NSN_MATCH = new MatchType();
            NSN_MATCH = new MatchType();
            EXACT_MATCH = matchType = new MatchType();
            $VALUES = new MatchType[]{NOT_A_NUMBER, NO_MATCH, SHORT_NSN_MATCH, NSN_MATCH, matchType};
        }

        private MatchType() {
        }

        public static MatchType valueOf(String string2) {
            return Enum.valueOf(MatchType.class, string2);
        }

        public static MatchType[] values() {
            return (MatchType[])$VALUES.clone();
        }
    }

    public static final class PhoneNumberFormat
    extends Enum<PhoneNumberFormat> {
        private static final /* synthetic */ PhoneNumberFormat[] $VALUES;
        public static final /* enum */ PhoneNumberFormat E164;
        public static final /* enum */ PhoneNumberFormat INTERNATIONAL;
        public static final /* enum */ PhoneNumberFormat NATIONAL;
        public static final /* enum */ PhoneNumberFormat RFC3966;

        static {
            PhoneNumberFormat phoneNumberFormat;
            E164 = new PhoneNumberFormat();
            INTERNATIONAL = new PhoneNumberFormat();
            NATIONAL = new PhoneNumberFormat();
            RFC3966 = phoneNumberFormat = new PhoneNumberFormat();
            $VALUES = new PhoneNumberFormat[]{E164, INTERNATIONAL, NATIONAL, phoneNumberFormat};
        }

        private PhoneNumberFormat() {
        }

        public static PhoneNumberFormat valueOf(String string2) {
            return Enum.valueOf(PhoneNumberFormat.class, string2);
        }

        public static PhoneNumberFormat[] values() {
            return (PhoneNumberFormat[])$VALUES.clone();
        }
    }

    public static final class PhoneNumberType
    extends Enum<PhoneNumberType> {
        private static final /* synthetic */ PhoneNumberType[] $VALUES;
        public static final /* enum */ PhoneNumberType FIXED_LINE;
        public static final /* enum */ PhoneNumberType FIXED_LINE_OR_MOBILE;
        public static final /* enum */ PhoneNumberType MOBILE;
        public static final /* enum */ PhoneNumberType PAGER;
        public static final /* enum */ PhoneNumberType PERSONAL_NUMBER;
        public static final /* enum */ PhoneNumberType PREMIUM_RATE;
        public static final /* enum */ PhoneNumberType SHARED_COST;
        public static final /* enum */ PhoneNumberType TOLL_FREE;
        public static final /* enum */ PhoneNumberType UAN;
        public static final /* enum */ PhoneNumberType UNKNOWN;
        public static final /* enum */ PhoneNumberType VOICEMAIL;
        public static final /* enum */ PhoneNumberType VOIP;

        static {
            PhoneNumberType phoneNumberType;
            FIXED_LINE = new PhoneNumberType();
            MOBILE = new PhoneNumberType();
            FIXED_LINE_OR_MOBILE = new PhoneNumberType();
            TOLL_FREE = new PhoneNumberType();
            PREMIUM_RATE = new PhoneNumberType();
            SHARED_COST = new PhoneNumberType();
            VOIP = new PhoneNumberType();
            PERSONAL_NUMBER = new PhoneNumberType();
            PAGER = new PhoneNumberType();
            UAN = new PhoneNumberType();
            VOICEMAIL = new PhoneNumberType();
            UNKNOWN = phoneNumberType = new PhoneNumberType();
            $VALUES = new PhoneNumberType[]{FIXED_LINE, MOBILE, FIXED_LINE_OR_MOBILE, TOLL_FREE, PREMIUM_RATE, SHARED_COST, VOIP, PERSONAL_NUMBER, PAGER, UAN, VOICEMAIL, phoneNumberType};
        }

        private PhoneNumberType() {
        }

        public static PhoneNumberType valueOf(String string2) {
            return Enum.valueOf(PhoneNumberType.class, string2);
        }

        public static PhoneNumberType[] values() {
            return (PhoneNumberType[])$VALUES.clone();
        }
    }

    public static final class ValidationResult
    extends Enum<ValidationResult> {
        private static final /* synthetic */ ValidationResult[] $VALUES;
        public static final /* enum */ ValidationResult INVALID_COUNTRY_CODE;
        public static final /* enum */ ValidationResult INVALID_LENGTH;
        public static final /* enum */ ValidationResult IS_POSSIBLE;
        public static final /* enum */ ValidationResult IS_POSSIBLE_LOCAL_ONLY;
        public static final /* enum */ ValidationResult TOO_LONG;
        public static final /* enum */ ValidationResult TOO_SHORT;

        static {
            ValidationResult validationResult;
            IS_POSSIBLE = new ValidationResult();
            IS_POSSIBLE_LOCAL_ONLY = new ValidationResult();
            INVALID_COUNTRY_CODE = new ValidationResult();
            TOO_SHORT = new ValidationResult();
            INVALID_LENGTH = new ValidationResult();
            TOO_LONG = validationResult = new ValidationResult();
            $VALUES = new ValidationResult[]{IS_POSSIBLE, IS_POSSIBLE_LOCAL_ONLY, INVALID_COUNTRY_CODE, TOO_SHORT, INVALID_LENGTH, validationResult};
        }

        private ValidationResult() {
        }

        public static ValidationResult valueOf(String string2) {
            return Enum.valueOf(ValidationResult.class, string2);
        }

        public static ValidationResult[] values() {
            return (ValidationResult[])$VALUES.clone();
        }
    }

}

