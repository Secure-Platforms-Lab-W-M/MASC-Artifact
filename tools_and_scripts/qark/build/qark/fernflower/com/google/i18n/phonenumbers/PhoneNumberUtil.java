package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.internal.MatcherApi;
import com.google.i18n.phonenumbers.internal.RegexBasedMatcher;
import com.google.i18n.phonenumbers.internal.RegexCache;
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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtil {
   private static final Map ALL_PLUS_NUMBER_GROUPING_SYMBOLS;
   private static final Map ALPHA_MAPPINGS;
   private static final Map ALPHA_PHONE_MAPPINGS;
   private static final Pattern CAPTURING_DIGIT_PATTERN;
   private static final String CAPTURING_EXTN_DIGITS = "(\\p{Nd}{1,7})";
   private static final String CC_STRING = "$CC";
   private static final String COLOMBIA_MOBILE_TO_FIXED_LINE_PREFIX = "3";
   private static final String DEFAULT_EXTN_PREFIX = " ext. ";
   private static final Map DIALLABLE_CHAR_MAPPINGS;
   private static final String DIGITS = "\\p{Nd}";
   private static final Pattern EXTN_PATTERN;
   static final String EXTN_PATTERNS_FOR_MATCHING;
   private static final String EXTN_PATTERNS_FOR_PARSING;
   private static final String FG_STRING = "$FG";
   private static final Pattern FIRST_GROUP_ONLY_PREFIX_PATTERN;
   private static final Pattern FIRST_GROUP_PATTERN;
   private static final Set GEO_MOBILE_COUNTRIES;
   private static final Set GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES;
   private static final int MAX_INPUT_STRING_LENGTH = 250;
   static final int MAX_LENGTH_COUNTRY_CODE = 3;
   static final int MAX_LENGTH_FOR_NSN = 17;
   private static final int MIN_LENGTH_FOR_NSN = 2;
   private static final Map MOBILE_TOKEN_MAPPINGS;
   private static final int NANPA_COUNTRY_CODE = 1;
   static final Pattern NON_DIGITS_PATTERN;
   private static final String NP_STRING = "$NP";
   static final String PLUS_CHARS = "+＋";
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
   static final String VALID_PUNCTUATION = "-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～";
   private static final String VALID_START_CHAR = "[+＋\\p{Nd}]";
   private static final Pattern VALID_START_CHAR_PATTERN;
   private static PhoneNumberUtil instance;
   private static final Logger logger = Logger.getLogger(PhoneNumberUtil.class.getName());
   private final Map countryCallingCodeToRegionCodeMap;
   private final Set countryCodesForNonGeographicalRegion = new HashSet();
   private final MatcherApi matcherApi = RegexBasedMatcher.create();
   private final MetadataSource metadataSource;
   private final Set nanpaRegions = new HashSet(35);
   private final RegexCache regexCache = new RegexCache(100);
   private final Set supportedRegions = new HashSet(320);

   static {
      HashMap var5 = new HashMap();
      Integer var1 = 52;
      Character var2 = '4';
      var5.put(var1, "1");
      Integer var4 = 54;
      Character var3 = '6';
      var5.put(var4, "9");
      MOBILE_TOKEN_MAPPINGS = Collections.unmodifiableMap(var5);
      HashSet var16 = new HashSet();
      var16.add(86);
      GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES = Collections.unmodifiableSet(var16);
      HashSet var6 = new HashSet();
      var6.add(var1);
      var6.add(var4);
      Character var14 = '7';
      var6.add(55);
      var6.add(62);
      var6.addAll(var16);
      GEO_MOBILE_COUNTRIES = Collections.unmodifiableSet(var6);
      HashMap var11 = new HashMap();
      Character var17 = '0';
      var11.put(var17, var17);
      var17 = '1';
      var11.put(var17, var17);
      var17 = '2';
      var11.put(var17, var17);
      Character var18 = '3';
      var11.put(var18, var18);
      var11.put(var2, var2);
      Character var7 = '5';
      var11.put(var7, var7);
      var11.put(var3, var3);
      var11.put(var14, var14);
      Character var8 = '8';
      var11.put(var8, var8);
      Character var9 = '9';
      var11.put(var9, var9);
      HashMap var10 = new HashMap(40);
      var10.put('A', var17);
      var10.put('B', var17);
      var10.put('C', var17);
      var10.put('D', var18);
      var10.put('E', var18);
      var10.put('F', var18);
      var10.put('G', var2);
      var10.put('H', var2);
      var10.put('I', var2);
      var10.put('J', var7);
      var10.put('K', var7);
      var10.put('L', var7);
      var10.put('M', var3);
      var10.put('N', var3);
      var10.put('O', var3);
      var10.put('P', var14);
      var10.put('Q', var14);
      var10.put('R', var14);
      var10.put('S', var14);
      var10.put('T', var8);
      var10.put('U', var8);
      var10.put('V', var8);
      var10.put('W', var9);
      var10.put('X', var9);
      var10.put('Y', var9);
      var10.put('Z', var9);
      ALPHA_MAPPINGS = Collections.unmodifiableMap(var10);
      HashMap var13 = new HashMap(100);
      var13.putAll(ALPHA_MAPPINGS);
      var13.putAll(var11);
      ALPHA_PHONE_MAPPINGS = Collections.unmodifiableMap(var13);
      var13 = new HashMap();
      var13.putAll(var11);
      var3 = '+';
      var13.put(var3, var3);
      var3 = '*';
      var13.put(var3, var3);
      var3 = '#';
      var13.put(var3, var3);
      DIALLABLE_CHAR_MAPPINGS = Collections.unmodifiableMap(var13);
      var13 = new HashMap();
      Iterator var15 = ALPHA_MAPPINGS.keySet().iterator();

      while(var15.hasNext()) {
         char var0 = (Character)var15.next();
         var13.put(Character.toLowerCase(var0), var0);
         var13.put(var0, var0);
      }

      var13.putAll(var11);
      var13.put('-', '-');
      var13.put('－', '-');
      var13.put('‐', '-');
      var13.put('‑', '-');
      var13.put('‒', '-');
      var13.put('–', '-');
      var13.put('—', '-');
      var13.put('―', '-');
      var13.put('−', '-');
      var13.put('/', '/');
      var13.put('／', '/');
      var13.put(' ', ' ');
      var13.put('　', ' ');
      var13.put('\u2060', ' ');
      var13.put('.', '.');
      var13.put('．', '.');
      ALL_PLUS_NUMBER_GROUPING_SYMBOLS = Collections.unmodifiableMap(var13);
      SINGLE_INTERNATIONAL_PREFIX = Pattern.compile("[\\d]+(?:[~⁓∼～][\\d]+)?");
      StringBuilder var12 = new StringBuilder();
      var12.append(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).replaceAll("[, \\[\\]]", ""));
      var12.append(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).toLowerCase().replaceAll("[, \\[\\]]", ""));
      VALID_ALPHA = var12.toString();
      PLUS_CHARS_PATTERN = Pattern.compile("[+＋]+");
      SEPARATOR_PATTERN = Pattern.compile("[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～]+");
      CAPTURING_DIGIT_PATTERN = Pattern.compile("(\\p{Nd})");
      VALID_START_CHAR_PATTERN = Pattern.compile("[+＋\\p{Nd}]");
      SECOND_NUMBER_START_PATTERN = Pattern.compile("[\\\\/] *x");
      UNWANTED_END_CHAR_PATTERN = Pattern.compile("[[\\P{N}&&\\P{L}]&&[^#]]+$");
      VALID_ALPHA_PHONE_PATTERN = Pattern.compile("(?:.*?[A-Za-z]){3}.*");
      var12 = new StringBuilder();
      var12.append("\\p{Nd}{2}|[+＋]*+(?:[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～*]*\\p{Nd}){3,}[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～*");
      var12.append(VALID_ALPHA);
      var12.append("\\p{Nd}");
      var12.append("]*");
      VALID_PHONE_NUMBER = var12.toString();
      var12 = new StringBuilder();
      var12.append(",;");
      var12.append("xｘ#＃~～");
      EXTN_PATTERNS_FOR_PARSING = createExtnPattern(var12.toString());
      EXTN_PATTERNS_FOR_MATCHING = createExtnPattern("xｘ#＃~～");
      var12 = new StringBuilder();
      var12.append("(?:");
      var12.append(EXTN_PATTERNS_FOR_PARSING);
      var12.append(")$");
      EXTN_PATTERN = Pattern.compile(var12.toString(), 66);
      var12 = new StringBuilder();
      var12.append(VALID_PHONE_NUMBER);
      var12.append("(?:");
      var12.append(EXTN_PATTERNS_FOR_PARSING);
      var12.append(")?");
      VALID_PHONE_NUMBER_PATTERN = Pattern.compile(var12.toString(), 66);
      NON_DIGITS_PATTERN = Pattern.compile("(\\D+)");
      FIRST_GROUP_PATTERN = Pattern.compile("(\\$\\d)");
      FIRST_GROUP_ONLY_PREFIX_PATTERN = Pattern.compile("\\(?\\$1\\)?");
      instance = null;
   }

   PhoneNumberUtil(MetadataSource var1, Map var2) {
      this.metadataSource = var1;
      this.countryCallingCodeToRegionCodeMap = var2;
      Iterator var5 = var2.entrySet().iterator();

      while(true) {
         while(var5.hasNext()) {
            Entry var3 = (Entry)var5.next();
            List var4 = (List)var3.getValue();
            if (var4.size() == 1 && "001".equals(var4.get(0))) {
               this.countryCodesForNonGeographicalRegion.add(var3.getKey());
            } else {
               this.supportedRegions.addAll(var4);
            }
         }

         if (this.supportedRegions.remove("001")) {
            logger.log(Level.WARNING, "invalid metadata (country calling code was mapped to the non-geo entity as well as specific region(s))");
         }

         this.nanpaRegions.addAll((Collection)var2.get(1));
         return;
      }
   }

   private void buildNationalNumberForParsing(String var1, StringBuilder var2) {
      int var4 = var1.indexOf(";phone-context=");
      int var3;
      if (var4 >= 0) {
         var3 = ";phone-context=".length() + var4;
         if (var3 < var1.length() - 1 && var1.charAt(var3) == '+') {
            int var5 = var1.indexOf(59, var3);
            if (var5 > 0) {
               var2.append(var1.substring(var3, var5));
            } else {
               var2.append(var1.substring(var3));
            }
         }

         var3 = var1.indexOf("tel:");
         if (var3 >= 0) {
            var3 += "tel:".length();
         } else {
            var3 = 0;
         }

         var2.append(var1.substring(var3, var4));
      } else {
         var2.append(extractPossibleNumber(var1));
      }

      var3 = var2.indexOf(";isub=");
      if (var3 > 0) {
         var2.delete(var3, var2.length());
      }

   }

   private boolean checkRegionForParsing(CharSequence var1, String var2) {
      return this.isValidRegionCode(var2) || var1 != null && var1.length() != 0 && PLUS_CHARS_PATTERN.matcher(var1).lookingAt();
   }

   public static String convertAlphaCharactersInNumber(CharSequence var0) {
      return normalizeHelper(var0, ALPHA_PHONE_MAPPINGS, false);
   }

   private static Phonenumber.PhoneNumber copyCoreFieldsOnly(Phonenumber.PhoneNumber var0) {
      Phonenumber.PhoneNumber var1 = new Phonenumber.PhoneNumber();
      var1.setCountryCode(var0.getCountryCode());
      var1.setNationalNumber(var0.getNationalNumber());
      if (var0.getExtension().length() > 0) {
         var1.setExtension(var0.getExtension());
      }

      if (var0.isItalianLeadingZero()) {
         var1.setItalianLeadingZero(true);
         var1.setNumberOfLeadingZeros(var0.getNumberOfLeadingZeros());
      }

      return var1;
   }

   private static String createExtnPattern(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(";ext=(\\p{Nd}{1,7})|[  \\t,]*(?:e?xt(?:ensi(?:ó?|ó))?n?|ｅ?ｘｔｎ?|доб|[");
      var1.append(var0);
      var1.append("]|int|anexo|ｉｎｔ)[:\\.．]?[  \\t,-]*");
      var1.append("(\\p{Nd}{1,7})");
      var1.append("#?|[- ]+(");
      var1.append("\\p{Nd}");
      var1.append("{1,5})#");
      return var1.toString();
   }

   public static PhoneNumberUtil createInstance(MetadataLoader var0) {
      if (var0 != null) {
         return createInstance((MetadataSource)(new MultiFileMetadataSourceImpl(var0)));
      } else {
         throw new IllegalArgumentException("metadataLoader could not be null.");
      }
   }

   private static PhoneNumberUtil createInstance(MetadataSource var0) {
      if (var0 != null) {
         return new PhoneNumberUtil(var0, CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap());
      } else {
         throw new IllegalArgumentException("metadataSource could not be null.");
      }
   }

   private static boolean descHasData(Phonemetadata.PhoneNumberDesc var0) {
      return var0.hasExampleNumber() || descHasPossibleNumberData(var0) || var0.hasNationalNumberPattern();
   }

   private static boolean descHasPossibleNumberData(Phonemetadata.PhoneNumberDesc var0) {
      int var1 = var0.getPossibleLengthCount();
      boolean var2 = false;
      if (var1 != 1 || var0.getPossibleLength(0) != -1) {
         var2 = true;
      }

      return var2;
   }

   static CharSequence extractPossibleNumber(CharSequence var0) {
      Matcher var1 = VALID_START_CHAR_PATTERN.matcher(var0);
      if (var1.find()) {
         CharSequence var3 = var0.subSequence(var1.start(), var0.length());
         Matcher var2 = UNWANTED_END_CHAR_PATTERN.matcher(var3);
         var0 = var3;
         if (var2.find()) {
            var0 = var3.subSequence(0, var2.start());
         }

         var2 = SECOND_NUMBER_START_PATTERN.matcher(var0);
         var3 = var0;
         if (var2.find()) {
            var3 = var0.subSequence(0, var2.start());
         }

         return var3;
      } else {
         return "";
      }
   }

   private String formatNsn(String var1, Phonemetadata.PhoneMetadata var2, PhoneNumberUtil.PhoneNumberFormat var3) {
      return this.formatNsn(var1, var2, var3, (CharSequence)null);
   }

   private String formatNsn(String var1, Phonemetadata.PhoneMetadata var2, PhoneNumberUtil.PhoneNumberFormat var3, CharSequence var4) {
      List var5;
      if (var2.intlNumberFormats().size() != 0 && var3 != PhoneNumberUtil.PhoneNumberFormat.NATIONAL) {
         var5 = var2.intlNumberFormats();
      } else {
         var5 = var2.numberFormats();
      }

      Phonemetadata.NumberFormat var6 = this.chooseFormattingPatternForNumber(var5, var1);
      return var6 == null ? var1 : this.formatNsnUsingPattern(var1, var6, var3, var4);
   }

   private String formatNsnUsingPattern(String var1, Phonemetadata.NumberFormat var2, PhoneNumberUtil.PhoneNumberFormat var3, CharSequence var4) {
      String var5 = var2.getFormat();
      Matcher var6 = this.regexCache.getPatternForRegex(var2.getPattern()).matcher(var1);
      String var7;
      if (var3 == PhoneNumberUtil.PhoneNumberFormat.NATIONAL && var4 != null && var4.length() > 0 && var2.getDomesticCarrierCodeFormattingRule().length() > 0) {
         var7 = var2.getDomesticCarrierCodeFormattingRule().replace("$CC", var4);
         var1 = var6.replaceAll(FIRST_GROUP_PATTERN.matcher(var5).replaceFirst(var7));
      } else {
         var7 = var2.getNationalPrefixFormattingRule();
         if (var3 == PhoneNumberUtil.PhoneNumberFormat.NATIONAL && var7 != null && var7.length() > 0) {
            var1 = var6.replaceAll(FIRST_GROUP_PATTERN.matcher(var5).replaceFirst(var7));
         } else {
            var1 = var6.replaceAll(var5);
         }
      }

      var7 = var1;
      if (var3 == PhoneNumberUtil.PhoneNumberFormat.RFC3966) {
         Matcher var8 = SEPARATOR_PATTERN.matcher(var1);
         if (var8.lookingAt()) {
            var1 = var8.replaceFirst("");
         }

         var7 = var8.reset(var1).replaceAll("-");
      }

      return var7;
   }

   static boolean formattingRuleHasFirstGroupOnly(String var0) {
      return var0.length() == 0 || FIRST_GROUP_ONLY_PREFIX_PATTERN.matcher(var0).matches();
   }

   private int getCountryCodeForValidRegion(String var1) {
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForRegion(var1);
      if (var2 != null) {
         return var2.getCountryCode();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid region code: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public static String getCountryMobileToken(int var0) {
      return MOBILE_TOKEN_MAPPINGS.containsKey(var0) ? (String)MOBILE_TOKEN_MAPPINGS.get(var0) : "";
   }

   public static PhoneNumberUtil getInstance() {
      synchronized(PhoneNumberUtil.class){}

      PhoneNumberUtil var0;
      try {
         if (instance == null) {
            setInstance(createInstance(MetadataManager.DEFAULT_METADATA_LOADER));
         }

         var0 = instance;
      } finally {
         ;
      }

      return var0;
   }

   private Phonemetadata.PhoneMetadata getMetadataForRegionOrCallingCode(int var1, String var2) {
      return "001".equals(var2) ? this.getMetadataForNonGeographicalRegion(var1) : this.getMetadataForRegion(var2);
   }

   private PhoneNumberUtil.PhoneNumberType getNumberTypeHelper(String var1, Phonemetadata.PhoneMetadata var2) {
      if (!this.isNumberMatchingDesc(var1, var2.getGeneralDesc())) {
         return PhoneNumberUtil.PhoneNumberType.UNKNOWN;
      } else if (this.isNumberMatchingDesc(var1, var2.getPremiumRate())) {
         return PhoneNumberUtil.PhoneNumberType.PREMIUM_RATE;
      } else if (this.isNumberMatchingDesc(var1, var2.getTollFree())) {
         return PhoneNumberUtil.PhoneNumberType.TOLL_FREE;
      } else if (this.isNumberMatchingDesc(var1, var2.getSharedCost())) {
         return PhoneNumberUtil.PhoneNumberType.SHARED_COST;
      } else if (this.isNumberMatchingDesc(var1, var2.getVoip())) {
         return PhoneNumberUtil.PhoneNumberType.VOIP;
      } else if (this.isNumberMatchingDesc(var1, var2.getPersonalNumber())) {
         return PhoneNumberUtil.PhoneNumberType.PERSONAL_NUMBER;
      } else if (this.isNumberMatchingDesc(var1, var2.getPager())) {
         return PhoneNumberUtil.PhoneNumberType.PAGER;
      } else if (this.isNumberMatchingDesc(var1, var2.getUan())) {
         return PhoneNumberUtil.PhoneNumberType.UAN;
      } else if (this.isNumberMatchingDesc(var1, var2.getVoicemail())) {
         return PhoneNumberUtil.PhoneNumberType.VOICEMAIL;
      } else if (this.isNumberMatchingDesc(var1, var2.getFixedLine())) {
         if (var2.getSameMobileAndFixedLinePattern()) {
            return PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE;
         } else {
            return this.isNumberMatchingDesc(var1, var2.getMobile()) ? PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE : PhoneNumberUtil.PhoneNumberType.FIXED_LINE;
         }
      } else {
         return !var2.getSameMobileAndFixedLinePattern() && this.isNumberMatchingDesc(var1, var2.getMobile()) ? PhoneNumberUtil.PhoneNumberType.MOBILE : PhoneNumberUtil.PhoneNumberType.UNKNOWN;
      }
   }

   private String getRegionCodeForNumberFromRegionList(Phonenumber.PhoneNumber var1, List var2) {
      String var5 = this.getNationalSignificantNumber(var1);
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         String var3 = (String)var6.next();
         Phonemetadata.PhoneMetadata var4 = this.getMetadataForRegion(var3);
         if (var4.hasLeadingDigits()) {
            if (this.regexCache.getPatternForRegex(var4.getLeadingDigits()).matcher(var5).lookingAt()) {
               return var3;
            }
         } else if (this.getNumberTypeHelper(var5, var4) != PhoneNumberUtil.PhoneNumberType.UNKNOWN) {
            return var3;
         }
      }

      return null;
   }

   private Set getSupportedTypesForMetadata(Phonemetadata.PhoneMetadata var1) {
      TreeSet var4 = new TreeSet();
      PhoneNumberUtil.PhoneNumberType[] var5 = PhoneNumberUtil.PhoneNumberType.values();
      int var3 = var5.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         PhoneNumberUtil.PhoneNumberType var6 = var5[var2];
         if (var6 != PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE && var6 != PhoneNumberUtil.PhoneNumberType.UNKNOWN && descHasData(this.getNumberDescByType(var1, var6))) {
            var4.add(var6);
         }
      }

      return Collections.unmodifiableSet(var4);
   }

   private boolean hasFormattingPatternForNumber(Phonenumber.PhoneNumber var1) {
      int var2 = var1.getCountryCode();
      Phonemetadata.PhoneMetadata var4 = this.getMetadataForRegionOrCallingCode(var2, this.getRegionCodeForCountryCode(var2));
      boolean var3 = false;
      if (var4 == null) {
         return false;
      } else {
         String var5 = this.getNationalSignificantNumber(var1);
         if (this.chooseFormattingPatternForNumber(var4.numberFormats(), var5) != null) {
            var3 = true;
         }

         return var3;
      }
   }

   private boolean hasValidCountryCallingCode(int var1) {
      return this.countryCallingCodeToRegionCodeMap.containsKey(var1);
   }

   private boolean isNationalNumberSuffixOfTheOther(Phonenumber.PhoneNumber var1, Phonenumber.PhoneNumber var2) {
      String var3 = String.valueOf(var1.getNationalNumber());
      String var4 = String.valueOf(var2.getNationalNumber());
      return var3.endsWith(var4) || var4.endsWith(var3);
   }

   private boolean isValidRegionCode(String var1) {
      return var1 != null && this.supportedRegions.contains(var1);
   }

   static boolean isViablePhoneNumber(CharSequence var0) {
      return var0.length() < 2 ? false : VALID_PHONE_NUMBER_PATTERN.matcher(var0).matches();
   }

   private void maybeAppendFormattedExtension(Phonenumber.PhoneNumber var1, Phonemetadata.PhoneMetadata var2, PhoneNumberUtil.PhoneNumberFormat var3, StringBuilder var4) {
      if (var1.hasExtension() && var1.getExtension().length() > 0) {
         if (var3 == PhoneNumberUtil.PhoneNumberFormat.RFC3966) {
            var4.append(";ext=");
            var4.append(var1.getExtension());
            return;
         }

         if (var2.hasPreferredExtnPrefix()) {
            var4.append(var2.getPreferredExtnPrefix());
            var4.append(var1.getExtension());
            return;
         }

         var4.append(" ext. ");
         var4.append(var1.getExtension());
      }

   }

   static StringBuilder normalize(StringBuilder var0) {
      if (VALID_ALPHA_PHONE_PATTERN.matcher(var0).matches()) {
         var0.replace(0, var0.length(), normalizeHelper(var0, ALPHA_PHONE_MAPPINGS, true));
         return var0;
      } else {
         var0.replace(0, var0.length(), normalizeDigitsOnly(var0));
         return var0;
      }
   }

   public static String normalizeDiallableCharsOnly(CharSequence var0) {
      return normalizeHelper(var0, DIALLABLE_CHAR_MAPPINGS, true);
   }

   static StringBuilder normalizeDigits(CharSequence var0, boolean var1) {
      StringBuilder var5 = new StringBuilder(var0.length());

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var2 = var0.charAt(var3);
         int var4 = Character.digit(var2, 10);
         if (var4 != -1) {
            var5.append(var4);
         } else if (var1) {
            var5.append(var2);
         }
      }

      return var5;
   }

   public static String normalizeDigitsOnly(CharSequence var0) {
      return normalizeDigits(var0, false).toString();
   }

   private static String normalizeHelper(CharSequence var0, Map var1, boolean var2) {
      StringBuilder var5 = new StringBuilder(var0.length());

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         char var3 = var0.charAt(var4);
         Character var6 = (Character)var1.get(Character.toUpperCase(var3));
         if (var6 != null) {
            var5.append(var6);
         } else if (!var2) {
            var5.append(var3);
         }
      }

      return var5.toString();
   }

   private void parseHelper(CharSequence var1, String var2, boolean var3, boolean var4, Phonenumber.PhoneNumber var5) throws NumberParseException {
      if (var1 == null) {
         throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The phone number supplied was null.");
      } else if (var1.length() <= 250) {
         StringBuilder var8 = new StringBuilder();
         String var12 = var1.toString();
         this.buildNationalNumberForParsing(var12, var8);
         if (!isViablePhoneNumber(var8)) {
            throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The string supplied did not seem to be a phone number.");
         } else if (var4 && !this.checkRegionForParsing(var8, var2)) {
            throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Missing or invalid default region.");
         } else {
            if (var3) {
               var5.setRawInput(var12);
            }

            var12 = this.maybeStripExtension(var8);
            if (var12.length() > 0) {
               var5.setExtension(var12);
            }

            Phonemetadata.PhoneMetadata var13 = this.getMetadataForRegion(var2);
            StringBuilder var7 = new StringBuilder();

            int var6;
            try {
               var6 = this.maybeExtractCountryCode(var8, var13, var7, var3, var5);
            } catch (NumberParseException var11) {
               Matcher var10 = PLUS_CHARS_PATTERN.matcher(var8);
               if (var11.getErrorType() != NumberParseException.ErrorType.INVALID_COUNTRY_CODE || !var10.lookingAt()) {
                  throw new NumberParseException(var11.getErrorType(), var11.getMessage());
               }

               var6 = this.maybeExtractCountryCode(var8.substring(var10.end()), var13, var7, var3, var5);
               if (var6 == 0) {
                  throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Could not interpret numbers after plus-sign.");
               }
            }

            Phonemetadata.PhoneMetadata var14;
            if (var6 != 0) {
               String var17 = this.getRegionCodeForCountryCode(var6);
               if (!var17.equals(var2)) {
                  var13 = this.getMetadataForRegionOrCallingCode(var6, var17);
               }

               var14 = var13;
            } else {
               var7.append(normalize(var8));
               if (var2 != null) {
                  var5.setCountryCode(var13.getCountryCode());
                  var14 = var13;
               } else {
                  var14 = var13;
                  if (var3) {
                     var5.clearCountryCodeSource();
                     var14 = var13;
                  }
               }
            }

            if (var7.length() >= 2) {
               StringBuilder var15 = var7;
               if (var14 != null) {
                  StringBuilder var9 = new StringBuilder();
                  var8 = new StringBuilder(var7);
                  this.maybeStripNationalPrefixAndCarrierCode(var8, var14, var9);
                  PhoneNumberUtil.ValidationResult var16 = this.testNumberLength(var8, var14);
                  var15 = var7;
                  if (var16 != PhoneNumberUtil.ValidationResult.TOO_SHORT) {
                     var15 = var7;
                     if (var16 != PhoneNumberUtil.ValidationResult.IS_POSSIBLE_LOCAL_ONLY) {
                        var15 = var7;
                        if (var16 != PhoneNumberUtil.ValidationResult.INVALID_LENGTH) {
                           var15 = var8;
                           if (var3) {
                              var15 = var8;
                              if (var9.length() > 0) {
                                 var5.setPreferredDomesticCarrierCode(var9.toString());
                                 var15 = var8;
                              }
                           }
                        }
                     }
                  }
               }

               var6 = var15.length();
               if (var6 >= 2) {
                  if (var6 <= 17) {
                     setItalianLeadingZerosForPhoneNumber(var15, var5);
                     var5.setNationalNumber(Long.parseLong(var15.toString()));
                  } else {
                     throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied is too long to be a phone number.");
                  }
               } else {
                  throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
               }
            } else {
               throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
            }
         }
      } else {
         throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied was too long to parse.");
      }
   }

   private boolean parsePrefixAsIdd(Pattern var1, StringBuilder var2) {
      Matcher var4 = var1.matcher(var2);
      if (var4.lookingAt()) {
         int var3 = var4.end();
         var4 = CAPTURING_DIGIT_PATTERN.matcher(var2.substring(var3));
         if (var4.find() && normalizeDigitsOnly(var4.group(1)).equals("0")) {
            return false;
         } else {
            var2.delete(0, var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void prefixNumberWithCountryCallingCode(int var1, PhoneNumberUtil.PhoneNumberFormat var2, StringBuilder var3) {
      int var4 = null.$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[var2.ordinal()];
      if (var4 != 1) {
         if (var4 != 2) {
            if (var4 == 3) {
               var3.insert(0, "-").insert(0, var1).insert(0, '+').insert(0, "tel:");
            }
         } else {
            var3.insert(0, " ").insert(0, var1).insert(0, '+');
         }
      } else {
         var3.insert(0, var1).insert(0, '+');
      }
   }

   private boolean rawInputContainsNationalPrefix(String var1, String var2, String var3) {
      var1 = normalizeDigitsOnly(var1);
      if (var1.startsWith(var2)) {
         try {
            boolean var4 = this.isValidNumber(this.parse(var1.substring(var2.length()), var3));
            return var4;
         } catch (NumberParseException var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   static void setInstance(PhoneNumberUtil var0) {
      synchronized(PhoneNumberUtil.class){}

      try {
         instance = var0;
      } finally {
         ;
      }

   }

   static void setItalianLeadingZerosForPhoneNumber(CharSequence var0, Phonenumber.PhoneNumber var1) {
      if (var0.length() > 1 && var0.charAt(0) == '0') {
         var1.setItalianLeadingZero(true);

         int var2;
         for(var2 = 1; var2 < var0.length() - 1 && var0.charAt(var2) == '0'; ++var2) {
         }

         if (var2 != 1) {
            var1.setNumberOfLeadingZeros(var2);
         }
      }

   }

   private PhoneNumberUtil.ValidationResult testNumberLength(CharSequence var1, Phonemetadata.PhoneMetadata var2) {
      return this.testNumberLength(var1, var2, PhoneNumberUtil.PhoneNumberType.UNKNOWN);
   }

   private PhoneNumberUtil.ValidationResult testNumberLength(CharSequence var1, Phonemetadata.PhoneMetadata var2, PhoneNumberUtil.PhoneNumberType var3) {
      Phonemetadata.PhoneNumberDesc var7 = this.getNumberDescByType(var2, var3);
      List var6;
      if (var7.getPossibleLengthList().isEmpty()) {
         var6 = var2.getGeneralDesc().getPossibleLengthList();
      } else {
         var6 = var7.getPossibleLengthList();
      }

      List var9 = var7.getPossibleLengthLocalOnlyList();
      Object var8 = var6;
      Object var12 = var9;
      if (var3 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE) {
         if (!descHasPossibleNumberData(this.getNumberDescByType(var2, PhoneNumberUtil.PhoneNumberType.FIXED_LINE))) {
            return this.testNumberLength(var1, var2, PhoneNumberUtil.PhoneNumberType.MOBILE);
         }

         Phonemetadata.PhoneNumberDesc var11 = this.getNumberDescByType(var2, PhoneNumberUtil.PhoneNumberType.MOBILE);
         var8 = var6;
         var12 = var9;
         if (descHasPossibleNumberData(var11)) {
            var8 = new ArrayList(var6);
            List var10;
            if (var11.getPossibleLengthList().size() == 0) {
               var10 = var2.getGeneralDesc().getPossibleLengthList();
            } else {
               var10 = var11.getPossibleLengthList();
            }

            ((List)var8).addAll(var10);
            Collections.sort((List)var8);
            if (var9.isEmpty()) {
               var12 = var11.getPossibleLengthLocalOnlyList();
            } else {
               var12 = new ArrayList(var9);
               ((List)var12).addAll(var11.getPossibleLengthLocalOnlyList());
               Collections.sort((List)var12);
            }
         }
      }

      if ((Integer)((List)var8).get(0) == -1) {
         return PhoneNumberUtil.ValidationResult.INVALID_LENGTH;
      } else {
         int var4 = var1.length();
         if (((List)var12).contains(var4)) {
            return PhoneNumberUtil.ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
         } else {
            int var5 = (Integer)((List)var8).get(0);
            if (var5 == var4) {
               return PhoneNumberUtil.ValidationResult.IS_POSSIBLE;
            } else if (var5 > var4) {
               return PhoneNumberUtil.ValidationResult.TOO_SHORT;
            } else if ((Integer)((List)var8).get(((List)var8).size() - 1) < var4) {
               return PhoneNumberUtil.ValidationResult.TOO_LONG;
            } else {
               return ((List)var8).subList(1, ((List)var8).size()).contains(var4) ? PhoneNumberUtil.ValidationResult.IS_POSSIBLE : PhoneNumberUtil.ValidationResult.INVALID_LENGTH;
            }
         }
      }
   }

   public boolean canBeInternationallyDialled(Phonenumber.PhoneNumber var1) {
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForRegion(this.getRegionCodeForNumber(var1));
      return var2 == null ? true : true ^ this.isNumberMatchingDesc(this.getNationalSignificantNumber(var1), var2.getNoInternationalDialling());
   }

   Phonemetadata.NumberFormat chooseFormattingPatternForNumber(List var1, String var2) {
      Iterator var5 = var1.iterator();

      int var3;
      Phonemetadata.NumberFormat var4;
      do {
         do {
            if (!var5.hasNext()) {
               return null;
            }

            var4 = (Phonemetadata.NumberFormat)var5.next();
            var3 = var4.leadingDigitsPatternSize();
         } while(var3 != 0 && !this.regexCache.getPatternForRegex(var4.getLeadingDigitsPattern(var3 - 1)).matcher(var2).lookingAt());
      } while(!this.regexCache.getPatternForRegex(var4.getPattern()).matcher(var2).matches());

      return var4;
   }

   int extractCountryCode(StringBuilder var1, StringBuilder var2) {
      if (var1.length() == 0) {
         return 0;
      } else if (var1.charAt(0) == '0') {
         return 0;
      } else {
         int var4 = var1.length();

         for(int var3 = 1; var3 <= 3 && var3 <= var4; ++var3) {
            int var5 = Integer.parseInt(var1.substring(0, var3));
            if (this.countryCallingCodeToRegionCodeMap.containsKey(var5)) {
               var2.append(var1.substring(var3));
               return var5;
            }
         }

         return 0;
      }
   }

   public Iterable findNumbers(CharSequence var1, String var2) {
      return this.findNumbers(var1, var2, PhoneNumberUtil.Leniency.VALID, Long.MAX_VALUE);
   }

   public Iterable findNumbers(final CharSequence var1, final String var2, final PhoneNumberUtil.Leniency var3, final long var4) {
      return new Iterable() {
         public Iterator iterator() {
            return new PhoneNumberMatcher(PhoneNumberUtil.this, var1, var2, var3, var4);
         }
      };
   }

   public String format(Phonenumber.PhoneNumber var1, PhoneNumberUtil.PhoneNumberFormat var2) {
      if (var1.getNationalNumber() == 0L && var1.hasRawInput()) {
         String var3 = var1.getRawInput();
         if (var3.length() > 0) {
            return var3;
         }
      }

      StringBuilder var4 = new StringBuilder(20);
      this.format(var1, var2, var4);
      return var4.toString();
   }

   public void format(Phonenumber.PhoneNumber var1, PhoneNumberUtil.PhoneNumberFormat var2, StringBuilder var3) {
      var3.setLength(0);
      int var4 = var1.getCountryCode();
      String var5 = this.getNationalSignificantNumber(var1);
      if (var2 == PhoneNumberUtil.PhoneNumberFormat.E164) {
         var3.append(var5);
         this.prefixNumberWithCountryCallingCode(var4, PhoneNumberUtil.PhoneNumberFormat.E164, var3);
      } else if (!this.hasValidCountryCallingCode(var4)) {
         var3.append(var5);
      } else {
         Phonemetadata.PhoneMetadata var6 = this.getMetadataForRegionOrCallingCode(var4, this.getRegionCodeForCountryCode(var4));
         var3.append(this.formatNsn(var5, var6, var2));
         this.maybeAppendFormattedExtension(var1, var6, var2, var3);
         this.prefixNumberWithCountryCallingCode(var4, var2, var3);
      }
   }

   public String formatByPattern(Phonenumber.PhoneNumber var1, PhoneNumberUtil.PhoneNumberFormat var2, List var3) {
      int var4 = var1.getCountryCode();
      String var5 = this.getNationalSignificantNumber(var1);
      if (!this.hasValidCountryCallingCode(var4)) {
         return var5;
      } else {
         Phonemetadata.PhoneMetadata var6 = this.getMetadataForRegionOrCallingCode(var4, this.getRegionCodeForCountryCode(var4));
         StringBuilder var7 = new StringBuilder(20);
         Phonemetadata.NumberFormat var8 = this.chooseFormattingPatternForNumber(var3, var5);
         if (var8 == null) {
            var7.append(var5);
         } else {
            Phonemetadata.NumberFormat.Builder var10 = Phonemetadata.NumberFormat.newBuilder();
            var10.mergeFrom(var8);
            String var11 = var8.getNationalPrefixFormattingRule();
            if (var11.length() > 0) {
               String var9 = var6.getNationalPrefix();
               if (var9.length() > 0) {
                  var10.setNationalPrefixFormattingRule(var11.replace("$NP", var9).replace("$FG", "$1"));
               } else {
                  var10.clearNationalPrefixFormattingRule();
               }
            }

            var7.append(this.formatNsnUsingPattern(var5, var10, var2));
         }

         this.maybeAppendFormattedExtension(var1, var6, var2, var7);
         this.prefixNumberWithCountryCallingCode(var4, var2, var7);
         return var7.toString();
      }
   }

   public String formatInOriginalFormat(Phonenumber.PhoneNumber var1, String var2) {
      if (var1.hasRawInput() && !this.hasFormattingPatternForNumber(var1)) {
         return var1.getRawInput();
      } else if (!var1.hasCountryCodeSource()) {
         return this.format(var1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
      } else {
         int var3 = null.$SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[var1.getCountryCodeSource().ordinal()];
         String var4;
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  var4 = this.getRegionCodeForCountryCode(var1.getCountryCode());
                  String var5 = this.getNddPrefixForRegion(var4, true);
                  var2 = this.format(var1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
                  if (var5 != null && var5.length() != 0 && !this.rawInputContainsNationalPrefix(var1.getRawInput(), var5, var4)) {
                     Phonemetadata.PhoneMetadata var8 = this.getMetadataForRegion(var4);
                     var5 = this.getNationalSignificantNumber(var1);
                     Phonemetadata.NumberFormat var9 = this.chooseFormattingPatternForNumber(var8.numberFormats(), var5);
                     if (var9 != null) {
                        var5 = var9.getNationalPrefixFormattingRule();
                        var3 = var5.indexOf("$1");
                        if (var3 > 0 && normalizeDigitsOnly(var5.substring(0, var3)).length() != 0) {
                           Phonemetadata.NumberFormat.Builder var7 = Phonemetadata.NumberFormat.newBuilder();
                           var7.mergeFrom(var9);
                           var7.clearNationalPrefixFormattingRule();
                           ArrayList var10 = new ArrayList(1);
                           var10.add(var7);
                           var2 = this.formatByPattern(var1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL, var10);
                        }
                     }
                  }
               } else {
                  var2 = this.format(var1, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).substring(1);
               }
            } else {
               var2 = this.formatOutOfCountryCallingNumber(var1, var2);
            }
         } else {
            var2 = this.format(var1, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
         }

         var4 = var1.getRawInput();
         String var6 = var2;
         if (var2 != null) {
            var6 = var2;
            if (var4.length() > 0) {
               var6 = var2;
               if (!normalizeDiallableCharsOnly(var2).equals(normalizeDiallableCharsOnly(var4))) {
                  var6 = var4;
               }
            }
         }

         return var6;
      }
   }

   public String formatNationalNumberWithCarrierCode(Phonenumber.PhoneNumber var1, CharSequence var2) {
      int var3 = var1.getCountryCode();
      String var4 = this.getNationalSignificantNumber(var1);
      if (!this.hasValidCountryCallingCode(var3)) {
         return var4;
      } else {
         Phonemetadata.PhoneMetadata var5 = this.getMetadataForRegionOrCallingCode(var3, this.getRegionCodeForCountryCode(var3));
         StringBuilder var6 = new StringBuilder(20);
         var6.append(this.formatNsn(var4, var5, PhoneNumberUtil.PhoneNumberFormat.NATIONAL, var2));
         this.maybeAppendFormattedExtension(var1, var5, PhoneNumberUtil.PhoneNumberFormat.NATIONAL, var6);
         this.prefixNumberWithCountryCallingCode(var3, PhoneNumberUtil.PhoneNumberFormat.NATIONAL, var6);
         return var6.toString();
      }
   }

   public String formatNationalNumberWithPreferredCarrierCode(Phonenumber.PhoneNumber var1, CharSequence var2) {
      if (var1.getPreferredDomesticCarrierCode().length() > 0) {
         var2 = var1.getPreferredDomesticCarrierCode();
      }

      return this.formatNationalNumberWithCarrierCode(var1, (CharSequence)var2);
   }

   String formatNsnUsingPattern(String var1, Phonemetadata.NumberFormat var2, PhoneNumberUtil.PhoneNumberFormat var3) {
      return this.formatNsnUsingPattern(var1, var2, var3, (CharSequence)null);
   }

   public String formatNumberForMobileDialing(Phonenumber.PhoneNumber var1, String var2, boolean var3) {
      int var6 = var1.getCountryCode();
      boolean var7 = this.hasValidCountryCallingCode(var6);
      String var8 = "";
      if (!var7) {
         if (var1.hasRawInput()) {
            var8 = var1.getRawInput();
         }

         return var8;
      } else {
         String var9 = "";
         Phonenumber.PhoneNumber var10 = (new Phonenumber.PhoneNumber()).mergeFrom(var1).clearExtension();
         String var13 = this.getRegionCodeForCountryCode(var6);
         PhoneNumberUtil.PhoneNumberType var11 = this.getNumberType(var10);
         PhoneNumberUtil.PhoneNumberType var12 = PhoneNumberUtil.PhoneNumberType.UNKNOWN;
         boolean var5 = false;
         boolean var4;
         if (var11 != var12) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var2.equals(var13)) {
            if (var11 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE || var11 == PhoneNumberUtil.PhoneNumberType.MOBILE || var11 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE) {
               var5 = true;
            }

            if (var13.equals("CO") && var11 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE) {
               var13 = this.formatNationalNumberWithCarrierCode(var10, "3");
            } else if (var13.equals("BR") && var5) {
               if (var10.getPreferredDomesticCarrierCode().length() > 0) {
                  var13 = this.formatNationalNumberWithPreferredCarrierCode(var10, "");
               } else {
                  var13 = var8;
               }
            } else if (var4 && var13.equals("HU")) {
               StringBuilder var15 = new StringBuilder();
               var15.append(this.getNddPrefixForRegion(var13, true));
               var15.append(" ");
               var15.append(this.format(var10, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
               var13 = var15.toString();
            } else if (var6 == 1) {
               Phonemetadata.PhoneMetadata var14 = this.getMetadataForRegion(var2);
               if (this.canBeInternationallyDialled(var10) && this.testNumberLength(this.getNationalSignificantNumber(var10), var14) != PhoneNumberUtil.ValidationResult.TOO_SHORT) {
                  var13 = this.format(var10, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
               } else {
                  var13 = this.format(var10, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
               }
            } else if ((var13.equals("001") || (var13.equals("MX") || var13.equals("CL") || var13.equals("UZ")) && var5) && this.canBeInternationallyDialled(var10)) {
               var13 = this.format(var10, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            } else {
               var13 = this.format(var10, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            }
         } else {
            var13 = var9;
            if (var4) {
               var13 = var9;
               if (this.canBeInternationallyDialled(var10)) {
                  if (var3) {
                     return this.format(var10, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                  }

                  return this.format(var10, PhoneNumberUtil.PhoneNumberFormat.E164);
               }
            }
         }

         return var3 ? var13 : normalizeDiallableCharsOnly(var13);
      }
   }

   public String formatOutOfCountryCallingNumber(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.isValidRegionCode(var2)) {
         Logger var9 = logger;
         Level var11 = Level.WARNING;
         StringBuilder var12 = new StringBuilder();
         var12.append("Trying to format number from invalid region ");
         var12.append(var2);
         var12.append(". International formatting applied.");
         var9.log(var11, var12.toString());
         return this.format(var1, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
      } else {
         int var3 = var1.getCountryCode();
         String var5 = this.getNationalSignificantNumber(var1);
         if (!this.hasValidCountryCallingCode(var3)) {
            return var5;
         } else {
            if (var3 == 1) {
               if (this.isNANPACountry(var2)) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append(var3);
                  var7.append(" ");
                  var7.append(this.format(var1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                  return var7.toString();
               }
            } else if (var3 == this.getCountryCodeForValidRegion(var2)) {
               return this.format(var1, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            }

            Phonemetadata.PhoneMetadata var6 = this.getMetadataForRegion(var2);
            var2 = var6.getInternationalPrefix();
            String var4 = "";
            if (!SINGLE_INTERNATIONAL_PREFIX.matcher(var2).matches()) {
               var2 = var4;
               if (var6.hasPreferredInternationalPrefix()) {
                  var2 = var6.getPreferredInternationalPrefix();
               }
            }

            Phonemetadata.PhoneMetadata var8 = this.getMetadataForRegionOrCallingCode(var3, this.getRegionCodeForCountryCode(var3));
            StringBuilder var10 = new StringBuilder(this.formatNsn(var5, var8, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            this.maybeAppendFormattedExtension(var1, var8, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL, var10);
            if (var2.length() > 0) {
               var10.insert(0, " ").insert(0, var3).insert(0, " ").insert(0, var2);
            } else {
               this.prefixNumberWithCountryCallingCode(var3, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL, var10);
            }

            return var10.toString();
         }
      }
   }

   public String formatOutOfCountryKeepingAlphaChars(Phonenumber.PhoneNumber var1, String var2) {
      String var5 = var1.getRawInput();
      if (var5.length() == 0) {
         return this.formatOutOfCountryCallingNumber(var1, var2);
      } else {
         int var3 = var1.getCountryCode();
         if (!this.hasValidCountryCallingCode(var3)) {
            return var5;
         } else {
            var5 = normalizeHelper(var5, ALL_PLUS_NUMBER_GROUPING_SYMBOLS, true);
            String var8 = this.getNationalSignificantNumber(var1);
            String var6 = var5;
            if (var8.length() > 3) {
               int var4 = var5.indexOf(var8.substring(0, 3));
               var6 = var5;
               if (var4 != -1) {
                  var6 = var5.substring(var4);
               }
            }

            Phonemetadata.PhoneMetadata var7 = this.getMetadataForRegion(var2);
            if (var3 == 1) {
               if (this.isNANPACountry(var2)) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append(var3);
                  var9.append(" ");
                  var9.append(var6);
                  return var9.toString();
               }
            } else if (var7 != null && var3 == this.getCountryCodeForValidRegion(var2)) {
               Phonemetadata.NumberFormat var11 = this.chooseFormattingPatternForNumber(var7.numberFormats(), var8);
               if (var11 == null) {
                  return var6;
               }

               Phonemetadata.NumberFormat.Builder var12 = Phonemetadata.NumberFormat.newBuilder();
               var12.mergeFrom(var11);
               var12.setPattern("(\\d+)(.*)");
               var12.setFormat("$1$2");
               return this.formatNsnUsingPattern(var6, var12, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            }

            var5 = "";
            if (var7 != null) {
               var5 = var7.getInternationalPrefix();
               if (!SINGLE_INTERNATIONAL_PREFIX.matcher(var5).matches()) {
                  var5 = var7.getPreferredInternationalPrefix();
               }
            }

            StringBuilder var15 = new StringBuilder(var6);
            this.maybeAppendFormattedExtension(var1, this.getMetadataForRegionOrCallingCode(var3, this.getRegionCodeForCountryCode(var3)), PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL, var15);
            if (var5.length() > 0) {
               var15.insert(0, " ").insert(0, var3).insert(0, " ").insert(0, var5);
            } else {
               if (!this.isValidRegionCode(var2)) {
                  Logger var10 = logger;
                  Level var13 = Level.WARNING;
                  StringBuilder var14 = new StringBuilder();
                  var14.append("Trying to format number from invalid region ");
                  var14.append(var2);
                  var14.append(". International formatting applied.");
                  var10.log(var13, var14.toString());
               }

               this.prefixNumberWithCountryCallingCode(var3, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL, var15);
            }

            return var15.toString();
         }
      }
   }

   public AsYouTypeFormatter getAsYouTypeFormatter(String var1) {
      return new AsYouTypeFormatter(var1);
   }

   public int getCountryCodeForRegion(String var1) {
      if (!this.isValidRegionCode(var1)) {
         Logger var2 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid or missing region code (");
         if (var1 == null) {
            var1 = "null";
         }

         var4.append(var1);
         var4.append(") provided.");
         var2.log(var3, var4.toString());
         return 0;
      } else {
         return this.getCountryCodeForValidRegion(var1);
      }
   }

   public Phonenumber.PhoneNumber getExampleNumber(String var1) {
      return this.getExampleNumberForType(var1, PhoneNumberUtil.PhoneNumberType.FIXED_LINE);
   }

   public Phonenumber.PhoneNumber getExampleNumberForNonGeoEntity(int var1) {
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForNonGeographicalRegion(var1);
      StringBuilder var4;
      if (var2 != null) {
         Iterator var6 = Arrays.asList(var2.getMobile(), var2.getTollFree(), var2.getSharedCost(), var2.getVoip(), var2.getVoicemail(), var2.getUan(), var2.getPremiumRate()).iterator();

         while(var6.hasNext()) {
            Phonemetadata.PhoneNumberDesc var3 = (Phonemetadata.PhoneNumberDesc)var6.next();
            if (var3 != null) {
               try {
                  if (var3.hasExampleNumber()) {
                     var4 = new StringBuilder();
                     var4.append("+");
                     var4.append(var1);
                     var4.append(var3.getExampleNumber());
                     Phonenumber.PhoneNumber var8 = this.parse(var4.toString(), "ZZ");
                     return var8;
                  }
               } catch (NumberParseException var5) {
                  logger.log(Level.SEVERE, var5.toString());
               }
            }
         }
      } else {
         Logger var7 = logger;
         Level var9 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("Invalid or unknown country calling code provided: ");
         var4.append(var1);
         var7.log(var9, var4.toString());
      }

      return null;
   }

   public Phonenumber.PhoneNumber getExampleNumberForType(PhoneNumberUtil.PhoneNumberType var1) {
      Iterator var3 = this.getSupportedRegions().iterator();

      Phonenumber.PhoneNumber var4;
      while(var3.hasNext()) {
         var4 = this.getExampleNumberForType((String)var3.next(), var1);
         if (var4 != null) {
            return var4;
         }
      }

      var3 = this.getSupportedGlobalNetworkCallingCodes().iterator();

      while(var3.hasNext()) {
         int var2 = (Integer)var3.next();
         Phonemetadata.PhoneNumberDesc var7 = this.getNumberDescByType(this.getMetadataForNonGeographicalRegion(var2), var1);

         try {
            if (var7.hasExampleNumber()) {
               StringBuilder var5 = new StringBuilder();
               var5.append("+");
               var5.append(var2);
               var5.append(var7.getExampleNumber());
               var4 = this.parse(var5.toString(), "ZZ");
               return var4;
            }
         } catch (NumberParseException var6) {
            logger.log(Level.SEVERE, var6.toString());
         }
      }

      return null;
   }

   public Phonenumber.PhoneNumber getExampleNumberForType(String var1, PhoneNumberUtil.PhoneNumberType var2) {
      if (!this.isValidRegionCode(var1)) {
         Logger var8 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid or unknown region code provided: ");
         var4.append(var1);
         var8.log(var3, var4.toString());
         return null;
      } else {
         Phonemetadata.PhoneNumberDesc var7 = this.getNumberDescByType(this.getMetadataForRegion(var1), var2);

         try {
            if (var7.hasExampleNumber()) {
               Phonenumber.PhoneNumber var6 = this.parse(var7.getExampleNumber(), var1);
               return var6;
            } else {
               return null;
            }
         } catch (NumberParseException var5) {
            logger.log(Level.SEVERE, var5.toString());
            return null;
         }
      }
   }

   public Phonenumber.PhoneNumber getInvalidExampleNumber(String var1) {
      if (!this.isValidRegionCode(var1)) {
         Logger var9 = logger;
         Level var11 = Level.WARNING;
         StringBuilder var6 = new StringBuilder();
         var6.append("Invalid or unknown region code provided: ");
         var6.append(var1);
         var9.log(var11, var6.toString());
         return null;
      } else {
         Phonemetadata.PhoneNumberDesc var4 = this.getNumberDescByType(this.getMetadataForRegion(var1), PhoneNumberUtil.PhoneNumberType.FIXED_LINE);
         if (!var4.hasExampleNumber()) {
            return null;
         } else {
            String var8 = var4.getExampleNumber();

            for(int var2 = var8.length() - 1; var2 >= 2; --var2) {
               String var5 = var8.substring(0, var2);

               boolean var3;
               Phonenumber.PhoneNumber var10;
               try {
                  var10 = this.parse(var5, var1);
                  var3 = this.isValidNumber(var10);
               } catch (NumberParseException var7) {
                  continue;
               }

               if (!var3) {
                  return var10;
               }
            }

            return null;
         }
      }
   }

   public int getLengthOfGeographicalAreaCode(Phonenumber.PhoneNumber var1) {
      Phonemetadata.PhoneMetadata var3 = this.getMetadataForRegion(this.getRegionCodeForNumber(var1));
      if (var3 == null) {
         return 0;
      } else if (!var3.hasNationalPrefix() && !var1.isItalianLeadingZero()) {
         return 0;
      } else {
         PhoneNumberUtil.PhoneNumberType var4 = this.getNumberType(var1);
         int var2 = var1.getCountryCode();
         if (var4 == PhoneNumberUtil.PhoneNumberType.MOBILE && GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES.contains(var2)) {
            return 0;
         } else {
            return !this.isNumberGeographical(var4, var2) ? 0 : this.getLengthOfNationalDestinationCode(var1);
         }
      }
   }

   public int getLengthOfNationalDestinationCode(Phonenumber.PhoneNumber var1) {
      Phonenumber.PhoneNumber var2;
      if (var1.hasExtension()) {
         var2 = new Phonenumber.PhoneNumber();
         var2.mergeFrom(var1);
         var2.clearExtension();
      } else {
         var2 = var1;
      }

      String var3 = this.format(var2, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
      String[] var4 = NON_DIGITS_PATTERN.split(var3);
      if (var4.length <= 3) {
         return 0;
      } else {
         return this.getNumberType(var1) == PhoneNumberUtil.PhoneNumberType.MOBILE && !getCountryMobileToken(var1.getCountryCode()).equals("") ? var4[2].length() + var4[3].length() : var4[2].length();
      }
   }

   Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int var1) {
      return !this.countryCallingCodeToRegionCodeMap.containsKey(var1) ? null : this.metadataSource.getMetadataForNonGeographicalRegion(var1);
   }

   Phonemetadata.PhoneMetadata getMetadataForRegion(String var1) {
      return !this.isValidRegionCode(var1) ? null : this.metadataSource.getMetadataForRegion(var1);
   }

   public String getNationalSignificantNumber(Phonenumber.PhoneNumber var1) {
      StringBuilder var2 = new StringBuilder();
      if (var1.isItalianLeadingZero() && var1.getNumberOfLeadingZeros() > 0) {
         char[] var3 = new char[var1.getNumberOfLeadingZeros()];
         Arrays.fill(var3, '0');
         var2.append(new String(var3));
      }

      var2.append(var1.getNationalNumber());
      return var2.toString();
   }

   public String getNddPrefixForRegion(String var1, boolean var2) {
      Phonemetadata.PhoneMetadata var3 = this.getMetadataForRegion(var1);
      if (var3 == null) {
         Logger var7 = logger;
         Level var4 = Level.WARNING;
         StringBuilder var5 = new StringBuilder();
         var5.append("Invalid or missing region code (");
         if (var1 == null) {
            var1 = "null";
         }

         var5.append(var1);
         var5.append(") provided.");
         var7.log(var4, var5.toString());
         return null;
      } else {
         String var6 = var3.getNationalPrefix();
         if (var6.length() == 0) {
            return null;
         } else {
            var1 = var6;
            if (var2) {
               var1 = var6.replace("~", "");
            }

            return var1;
         }
      }
   }

   Phonemetadata.PhoneNumberDesc getNumberDescByType(Phonemetadata.PhoneMetadata var1, PhoneNumberUtil.PhoneNumberType var2) {
      switch(null.$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[var2.ordinal()]) {
      case 1:
         return var1.getPremiumRate();
      case 2:
         return var1.getTollFree();
      case 3:
         return var1.getMobile();
      case 4:
      case 5:
         return var1.getFixedLine();
      case 6:
         return var1.getSharedCost();
      case 7:
         return var1.getVoip();
      case 8:
         return var1.getPersonalNumber();
      case 9:
         return var1.getPager();
      case 10:
         return var1.getUan();
      case 11:
         return var1.getVoicemail();
      default:
         return var1.getGeneralDesc();
      }
   }

   public PhoneNumberUtil.PhoneNumberType getNumberType(Phonenumber.PhoneNumber var1) {
      String var2 = this.getRegionCodeForNumber(var1);
      Phonemetadata.PhoneMetadata var3 = this.getMetadataForRegionOrCallingCode(var1.getCountryCode(), var2);
      return var3 == null ? PhoneNumberUtil.PhoneNumberType.UNKNOWN : this.getNumberTypeHelper(this.getNationalSignificantNumber(var1), var3);
   }

   public String getRegionCodeForCountryCode(int var1) {
      List var2 = (List)this.countryCallingCodeToRegionCodeMap.get(var1);
      return var2 == null ? "ZZ" : (String)var2.get(0);
   }

   public String getRegionCodeForNumber(Phonenumber.PhoneNumber var1) {
      int var2 = var1.getCountryCode();
      List var3 = (List)this.countryCallingCodeToRegionCodeMap.get(var2);
      if (var3 == null) {
         Logger var5 = logger;
         Level var6 = Level.INFO;
         StringBuilder var4 = new StringBuilder();
         var4.append("Missing/invalid country_code (");
         var4.append(var2);
         var4.append(")");
         var5.log(var6, var4.toString());
         return null;
      } else {
         return var3.size() == 1 ? (String)var3.get(0) : this.getRegionCodeForNumberFromRegionList(var1, var3);
      }
   }

   public List getRegionCodesForCountryCode(int var1) {
      Object var2 = (List)this.countryCallingCodeToRegionCodeMap.get(var1);
      if (var2 == null) {
         var2 = new ArrayList(0);
      }

      return Collections.unmodifiableList((List)var2);
   }

   public Set getSupportedCallingCodes() {
      return Collections.unmodifiableSet(this.countryCallingCodeToRegionCodeMap.keySet());
   }

   public Set getSupportedGlobalNetworkCallingCodes() {
      return Collections.unmodifiableSet(this.countryCodesForNonGeographicalRegion);
   }

   public Set getSupportedRegions() {
      return Collections.unmodifiableSet(this.supportedRegions);
   }

   public Set getSupportedTypesForNonGeoEntity(int var1) {
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForNonGeographicalRegion(var1);
      if (var2 == null) {
         Logger var5 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("Unknown country calling code for a non-geographical entity provided: ");
         var4.append(var1);
         var5.log(var3, var4.toString());
         return Collections.unmodifiableSet(new TreeSet());
      } else {
         return this.getSupportedTypesForMetadata(var2);
      }
   }

   public Set getSupportedTypesForRegion(String var1) {
      if (!this.isValidRegionCode(var1)) {
         Logger var2 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid or unknown region code provided: ");
         var4.append(var1);
         var2.log(var3, var4.toString());
         return Collections.unmodifiableSet(new TreeSet());
      } else {
         return this.getSupportedTypesForMetadata(this.getMetadataForRegion(var1));
      }
   }

   public boolean isAlphaNumber(CharSequence var1) {
      if (!isViablePhoneNumber(var1)) {
         return false;
      } else {
         StringBuilder var2 = new StringBuilder(var1);
         this.maybeStripExtension(var2);
         return VALID_ALPHA_PHONE_PATTERN.matcher(var2).matches();
      }
   }

   public boolean isMobileNumberPortableRegion(String var1) {
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForRegion(var1);
      if (var2 == null) {
         Logger var5 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid or unknown region code provided: ");
         var4.append(var1);
         var5.log(var3, var4.toString());
         return false;
      } else {
         return var2.isMobileNumberPortableRegion();
      }
   }

   public boolean isNANPACountry(String var1) {
      return this.nanpaRegions.contains(var1);
   }

   public boolean isNumberGeographical(PhoneNumberUtil.PhoneNumberType var1, int var2) {
      return var1 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE || var1 == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE || GEO_MOBILE_COUNTRIES.contains(var2) && var1 == PhoneNumberUtil.PhoneNumberType.MOBILE;
   }

   public boolean isNumberGeographical(Phonenumber.PhoneNumber var1) {
      return this.isNumberGeographical(this.getNumberType(var1), var1.getCountryCode());
   }

   public PhoneNumberUtil.MatchType isNumberMatch(Phonenumber.PhoneNumber var1, Phonenumber.PhoneNumber var2) {
      var1 = copyCoreFieldsOnly(var1);
      var2 = copyCoreFieldsOnly(var2);
      if (var1.hasExtension() && var2.hasExtension() && !var1.getExtension().equals(var2.getExtension())) {
         return PhoneNumberUtil.MatchType.NO_MATCH;
      } else {
         int var3 = var1.getCountryCode();
         int var4 = var2.getCountryCode();
         if (var3 != 0 && var4 != 0) {
            if (var1.exactlySameAs(var2)) {
               return PhoneNumberUtil.MatchType.EXACT_MATCH;
            } else {
               return var3 == var4 && this.isNationalNumberSuffixOfTheOther(var1, var2) ? PhoneNumberUtil.MatchType.SHORT_NSN_MATCH : PhoneNumberUtil.MatchType.NO_MATCH;
            }
         } else {
            var1.setCountryCode(var4);
            if (var1.exactlySameAs(var2)) {
               return PhoneNumberUtil.MatchType.NSN_MATCH;
            } else {
               return this.isNationalNumberSuffixOfTheOther(var1, var2) ? PhoneNumberUtil.MatchType.SHORT_NSN_MATCH : PhoneNumberUtil.MatchType.NO_MATCH;
            }
         }
      }
   }

   public PhoneNumberUtil.MatchType isNumberMatch(Phonenumber.PhoneNumber var1, CharSequence var2) {
      try {
         PhoneNumberUtil.MatchType var8 = this.isNumberMatch(var1, this.parse(var2, "ZZ"));
         return var8;
      } catch (NumberParseException var5) {
         if (var5.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
            String var3 = this.getRegionCodeForCountryCode(var1.getCountryCode());

            try {
               PhoneNumberUtil.MatchType var6;
               if (!var3.equals("ZZ")) {
                  var6 = this.isNumberMatch(var1, this.parse(var2, var3));
                  if (var6 == PhoneNumberUtil.MatchType.EXACT_MATCH) {
                     return PhoneNumberUtil.MatchType.NSN_MATCH;
                  }

                  return var6;
               }

               Phonenumber.PhoneNumber var7 = new Phonenumber.PhoneNumber();
               this.parseHelper(var2, (String)null, false, false, var7);
               var6 = this.isNumberMatch(var1, var7);
               return var6;
            } catch (NumberParseException var4) {
            }
         }

         return PhoneNumberUtil.MatchType.NOT_A_NUMBER;
      }
   }

   public PhoneNumberUtil.MatchType isNumberMatch(CharSequence var1, CharSequence var2) {
      PhoneNumberUtil.MatchType var9;
      try {
         var9 = this.isNumberMatch(this.parse(var1, "ZZ"), var2);
         return var9;
      } catch (NumberParseException var7) {
         if (var7.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
            try {
               var9 = this.isNumberMatch(this.parse(var2, "ZZ"), var1);
               return var9;
            } catch (NumberParseException var6) {
               if (var6.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                  try {
                     Phonenumber.PhoneNumber var3 = new Phonenumber.PhoneNumber();
                     Phonenumber.PhoneNumber var4 = new Phonenumber.PhoneNumber();
                     this.parseHelper(var1, (String)null, false, false, var3);
                     this.parseHelper(var2, (String)null, false, false, var4);
                     PhoneNumberUtil.MatchType var8 = this.isNumberMatch(var3, var4);
                     return var8;
                  } catch (NumberParseException var5) {
                  }
               }
            }
         }

         return PhoneNumberUtil.MatchType.NOT_A_NUMBER;
      }
   }

   boolean isNumberMatchingDesc(String var1, Phonemetadata.PhoneNumberDesc var2) {
      int var3 = var1.length();
      List var4 = var2.getPossibleLengthList();
      return var4.size() > 0 && !var4.contains(var3) ? false : this.matcherApi.matchNationalNumber(var1, var2, false);
   }

   public boolean isPossibleNumber(Phonenumber.PhoneNumber var1) {
      PhoneNumberUtil.ValidationResult var2 = this.isPossibleNumberWithReason(var1);
      return var2 == PhoneNumberUtil.ValidationResult.IS_POSSIBLE || var2 == PhoneNumberUtil.ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
   }

   public boolean isPossibleNumber(CharSequence var1, String var2) {
      try {
         boolean var3 = this.isPossibleNumber(this.parse(var1, var2));
         return var3;
      } catch (NumberParseException var4) {
         return false;
      }
   }

   public boolean isPossibleNumberForType(Phonenumber.PhoneNumber var1, PhoneNumberUtil.PhoneNumberType var2) {
      PhoneNumberUtil.ValidationResult var3 = this.isPossibleNumberForTypeWithReason(var1, var2);
      return var3 == PhoneNumberUtil.ValidationResult.IS_POSSIBLE || var3 == PhoneNumberUtil.ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
   }

   public PhoneNumberUtil.ValidationResult isPossibleNumberForTypeWithReason(Phonenumber.PhoneNumber var1, PhoneNumberUtil.PhoneNumberType var2) {
      String var4 = this.getNationalSignificantNumber(var1);
      int var3 = var1.getCountryCode();
      return !this.hasValidCountryCallingCode(var3) ? PhoneNumberUtil.ValidationResult.INVALID_COUNTRY_CODE : this.testNumberLength(var4, this.getMetadataForRegionOrCallingCode(var3, this.getRegionCodeForCountryCode(var3)), var2);
   }

   public PhoneNumberUtil.ValidationResult isPossibleNumberWithReason(Phonenumber.PhoneNumber var1) {
      return this.isPossibleNumberForTypeWithReason(var1, PhoneNumberUtil.PhoneNumberType.UNKNOWN);
   }

   public boolean isValidNumber(Phonenumber.PhoneNumber var1) {
      return this.isValidNumberForRegion(var1, this.getRegionCodeForNumber(var1));
   }

   public boolean isValidNumberForRegion(Phonenumber.PhoneNumber var1, String var2) {
      int var3 = var1.getCountryCode();
      Phonemetadata.PhoneMetadata var5 = this.getMetadataForRegionOrCallingCode(var3, var2);
      boolean var4 = false;
      if (var5 != null) {
         if (!"001".equals(var2) && var3 != this.getCountryCodeForValidRegion(var2)) {
            return false;
         } else {
            if (this.getNumberTypeHelper(this.getNationalSignificantNumber(var1), var5) != PhoneNumberUtil.PhoneNumberType.UNKNOWN) {
               var4 = true;
            }

            return var4;
         }
      } else {
         return false;
      }
   }

   int maybeExtractCountryCode(CharSequence var1, Phonemetadata.PhoneMetadata var2, StringBuilder var3, boolean var4, Phonenumber.PhoneNumber var5) throws NumberParseException {
      if (var1.length() == 0) {
         return 0;
      } else {
         StringBuilder var7 = new StringBuilder(var1);
         String var9 = "NonMatch";
         if (var2 != null) {
            var9 = var2.getInternationalPrefix();
         }

         Phonenumber.PhoneNumber.CountryCodeSource var10 = this.maybeStripInternationalPrefixAndNormalize(var7, var9);
         if (var4) {
            var5.setCountryCodeSource(var10);
         }

         int var6;
         if (var10 != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            if (var7.length() > 2) {
               var6 = this.extractCountryCode(var7, var3);
               if (var6 != 0) {
                  var5.setCountryCode(var6);
                  return var6;
               } else {
                  throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Country calling code supplied was not recognised.");
               }
            } else {
               throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD, "Phone number had an IDD, but after this was not long enough to be a viable phone number.");
            }
         } else {
            if (var2 != null) {
               var6 = var2.getCountryCode();
               var9 = String.valueOf(var6);
               String var8 = var7.toString();
               if (var8.startsWith(var9)) {
                  StringBuilder var11 = new StringBuilder(var8.substring(var9.length()));
                  Phonemetadata.PhoneNumberDesc var12 = var2.getGeneralDesc();
                  this.maybeStripNationalPrefixAndCarrierCode(var11, var2, (StringBuilder)null);
                  if (!this.matcherApi.matchNationalNumber(var7, var12, false) && this.matcherApi.matchNationalNumber(var11, var12, false) || this.testNumberLength(var7, var2) == PhoneNumberUtil.ValidationResult.TOO_LONG) {
                     var3.append(var11);
                     if (var4) {
                        var5.setCountryCodeSource(Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN);
                     }

                     var5.setCountryCode(var6);
                     return var6;
                  }
               }
            }

            var5.setCountryCode(0);
            return 0;
         }
      }
   }

   String maybeStripExtension(StringBuilder var1) {
      Matcher var4 = EXTN_PATTERN.matcher(var1);
      if (var4.find() && isViablePhoneNumber(var1.substring(0, var4.start()))) {
         int var2 = 1;

         for(int var3 = var4.groupCount(); var2 <= var3; ++var2) {
            if (var4.group(var2) != null) {
               String var5 = var4.group(var2);
               var1.delete(var4.start(), var1.length());
               return var5;
            }
         }
      }

      return "";
   }

   Phonenumber.PhoneNumber.CountryCodeSource maybeStripInternationalPrefixAndNormalize(StringBuilder var1, String var2) {
      if (var1.length() == 0) {
         return Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
      } else {
         Matcher var3 = PLUS_CHARS_PATTERN.matcher(var1);
         if (var3.lookingAt()) {
            var1.delete(0, var3.end());
            normalize(var1);
            return Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN;
         } else {
            Pattern var4 = this.regexCache.getPatternForRegex(var2);
            normalize(var1);
            return this.parsePrefixAsIdd(var4, var1) ? Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_IDD : Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
         }
      }
   }

   boolean maybeStripNationalPrefixAndCarrierCode(StringBuilder var1, Phonemetadata.PhoneMetadata var2, StringBuilder var3) {
      int var4 = var1.length();
      String var7 = var2.getNationalPrefixForParsing();
      if (var4 != 0) {
         if (var7.length() == 0) {
            return false;
         } else {
            Matcher var11 = this.regexCache.getPatternForRegex(var7).matcher(var1);
            if (var11.lookingAt()) {
               Phonemetadata.PhoneNumberDesc var8 = var2.getGeneralDesc();
               boolean var6 = this.matcherApi.matchNationalNumber(var1, var8, false);
               int var5 = var11.groupCount();
               String var10 = var2.getNationalPrefixTransformRule();
               if (var10 != null && var10.length() != 0 && var11.group(var5) != null) {
                  StringBuilder var9 = new StringBuilder(var1);
                  var9.replace(0, var4, var11.replaceFirst(var10));
                  if (var6 && !this.matcherApi.matchNationalNumber(var9.toString(), var8, false)) {
                     return false;
                  } else {
                     if (var3 != null && var5 > 1) {
                        var3.append(var11.group(1));
                     }

                     var1.replace(0, var1.length(), var9.toString());
                     return true;
                  }
               } else if (var6 && !this.matcherApi.matchNationalNumber(var1.substring(var11.end()), var8, false)) {
                  return false;
               } else {
                  if (var3 != null && var5 > 0 && var11.group(var5) != null) {
                     var3.append(var11.group(1));
                  }

                  var1.delete(0, var11.end());
                  return true;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public Phonenumber.PhoneNumber parse(CharSequence var1, String var2) throws NumberParseException {
      Phonenumber.PhoneNumber var3 = new Phonenumber.PhoneNumber();
      this.parse(var1, var2, var3);
      return var3;
   }

   public void parse(CharSequence var1, String var2, Phonenumber.PhoneNumber var3) throws NumberParseException {
      this.parseHelper(var1, var2, false, true, var3);
   }

   public Phonenumber.PhoneNumber parseAndKeepRawInput(CharSequence var1, String var2) throws NumberParseException {
      Phonenumber.PhoneNumber var3 = new Phonenumber.PhoneNumber();
      this.parseAndKeepRawInput(var1, var2, var3);
      return var3;
   }

   public void parseAndKeepRawInput(CharSequence var1, String var2, Phonenumber.PhoneNumber var3) throws NumberParseException {
      this.parseHelper(var1, var2, true, true, var3);
   }

   public boolean truncateTooLongNumber(Phonenumber.PhoneNumber var1) {
      if (this.isValidNumber(var1)) {
         return true;
      } else {
         Phonenumber.PhoneNumber var6 = new Phonenumber.PhoneNumber();
         var6.mergeFrom(var1);
         long var2 = var1.getNationalNumber();

         long var4;
         do {
            var4 = var2 / 10L;
            var6.setNationalNumber(var4);
            if (this.isPossibleNumberWithReason(var6) == PhoneNumberUtil.ValidationResult.TOO_SHORT || var4 == 0L) {
               return false;
            }

            var2 = var4;
         } while(!this.isValidNumber(var6));

         var1.setNationalNumber(var4);
         return true;
      }
   }

   public static enum Leniency {
      EXACT_GROUPING,
      POSSIBLE {
         boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3) {
            return var3.isPossibleNumber(var1);
         }
      },
      STRICT_GROUPING {
         boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3) {
            String var4 = var2.toString();
            return var3.isValidNumber(var1) && PhoneNumberMatcher.containsOnlyValidXChars(var1, var4, var3) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(var1, var4) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(var1, var3) ? PhoneNumberMatcher.checkNumberGroupingIsValid(var1, var2, var3, new PhoneNumberMatcher.NumberGroupingChecker() {
               public boolean checkGroups(PhoneNumberUtil var1, Phonenumber.PhoneNumber var2, StringBuilder var3, String[] var4) {
                  return PhoneNumberMatcher.allNumberGroupsRemainGrouped(var1, var2, var3, var4);
               }
            }) : false;
         }
      },
      VALID {
         boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3) {
            return var3.isValidNumber(var1) && PhoneNumberMatcher.containsOnlyValidXChars(var1, var2.toString(), var3) ? PhoneNumberMatcher.isNationalPrefixPresentIfRequired(var1, var3) : false;
         }
      };

      static {
         PhoneNumberUtil.Leniency var0 = new PhoneNumberUtil.Leniency("EXACT_GROUPING", 3) {
            boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3) {
               String var4 = var2.toString();
               return var3.isValidNumber(var1) && PhoneNumberMatcher.containsOnlyValidXChars(var1, var4, var3) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(var1, var4) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(var1, var3) ? PhoneNumberMatcher.checkNumberGroupingIsValid(var1, var2, var3, new PhoneNumberMatcher.NumberGroupingChecker() {
                  public boolean checkGroups(PhoneNumberUtil var1, Phonenumber.PhoneNumber var2, StringBuilder var3, String[] var4) {
                     return PhoneNumberMatcher.allNumberGroupsAreExactlyPresent(var1, var2, var3, var4);
                  }
               }) : false;
            }
         };
         EXACT_GROUPING = var0;
      }

      private Leniency() {
      }

      // $FF: synthetic method
      Leniency(Object var3) {
         this();
      }

      abstract boolean verify(Phonenumber.PhoneNumber var1, CharSequence var2, PhoneNumberUtil var3);
   }

   public static enum MatchType {
      EXACT_MATCH,
      NOT_A_NUMBER,
      NO_MATCH,
      NSN_MATCH,
      SHORT_NSN_MATCH;

      static {
         PhoneNumberUtil.MatchType var0 = new PhoneNumberUtil.MatchType("EXACT_MATCH", 4);
         EXACT_MATCH = var0;
      }
   }

   public static enum PhoneNumberFormat {
      E164,
      INTERNATIONAL,
      NATIONAL,
      RFC3966;

      static {
         PhoneNumberUtil.PhoneNumberFormat var0 = new PhoneNumberUtil.PhoneNumberFormat("RFC3966", 3);
         RFC3966 = var0;
      }
   }

   public static enum PhoneNumberType {
      FIXED_LINE,
      FIXED_LINE_OR_MOBILE,
      MOBILE,
      PAGER,
      PERSONAL_NUMBER,
      PREMIUM_RATE,
      SHARED_COST,
      TOLL_FREE,
      UAN,
      UNKNOWN,
      VOICEMAIL,
      VOIP;

      static {
         PhoneNumberUtil.PhoneNumberType var0 = new PhoneNumberUtil.PhoneNumberType("UNKNOWN", 11);
         UNKNOWN = var0;
      }
   }

   public static enum ValidationResult {
      INVALID_COUNTRY_CODE,
      INVALID_LENGTH,
      IS_POSSIBLE,
      IS_POSSIBLE_LOCAL_ONLY,
      TOO_LONG,
      TOO_SHORT;

      static {
         PhoneNumberUtil.ValidationResult var0 = new PhoneNumberUtil.ValidationResult("TOO_LONG", 5);
         TOO_LONG = var0;
      }
   }
}
