package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.internal.MatcherApi;
import com.google.i18n.phonenumbers.internal.RegexBasedMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShortNumberInfo {
   private static final ShortNumberInfo INSTANCE = new ShortNumberInfo(RegexBasedMatcher.create());
   private static final Set REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT;
   private static final Logger logger = Logger.getLogger(ShortNumberInfo.class.getName());
   private final Map countryCallingCodeToRegionCodeMap;
   private final MatcherApi matcherApi;

   static {
      HashSet var0 = new HashSet();
      REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT = var0;
      var0.add("BR");
      REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("CL");
      REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("NI");
   }

   ShortNumberInfo(MatcherApi var1) {
      this.matcherApi = var1;
      this.countryCallingCodeToRegionCodeMap = CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap();
   }

   public static ShortNumberInfo getInstance() {
      return INSTANCE;
   }

   private static String getNationalSignificantNumber(Phonenumber.PhoneNumber var0) {
      StringBuilder var1 = new StringBuilder();
      if (var0.isItalianLeadingZero()) {
         char[] var2 = new char[var0.getNumberOfLeadingZeros()];
         Arrays.fill(var2, '0');
         var1.append(new String(var2));
      }

      var1.append(var0.getNationalNumber());
      return var1.toString();
   }

   private String getRegionCodeForShortNumberFromRegionList(Phonenumber.PhoneNumber var1, List var2) {
      if (var2.size() == 0) {
         return null;
      } else if (var2.size() == 1) {
         return (String)var2.get(0);
      } else {
         String var5 = getNationalSignificantNumber(var1);
         Iterator var6 = var2.iterator();

         String var3;
         Phonemetadata.PhoneMetadata var4;
         do {
            if (!var6.hasNext()) {
               return null;
            }

            var3 = (String)var6.next();
            var4 = MetadataManager.getShortNumberMetadataForRegion(var3);
         } while(var4 == null || !this.matchesPossibleNumberAndNationalNumber(var5, var4.getShortCode()));

         return var3;
      }
   }

   private List getRegionCodesForCountryCode(int var1) {
      Object var2 = (List)this.countryCallingCodeToRegionCodeMap.get(var1);
      if (var2 == null) {
         var2 = new ArrayList(0);
      }

      return Collections.unmodifiableList((List)var2);
   }

   private boolean matchesEmergencyNumberHelper(CharSequence var1, String var2, boolean var3) {
      CharSequence var6 = PhoneNumberUtil.extractPossibleNumber(var1);
      boolean var5 = PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(var6).lookingAt();
      boolean var4 = false;
      if (var5) {
         return false;
      } else {
         Phonemetadata.PhoneMetadata var7 = MetadataManager.getShortNumberMetadataForRegion(var2);
         if (var7 != null) {
            if (!var7.hasEmergency()) {
               return false;
            } else {
               String var8 = PhoneNumberUtil.normalizeDigitsOnly(var6);
               if (var3 && !REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.contains(var2)) {
                  var3 = true;
               } else {
                  var3 = var4;
               }

               return this.matcherApi.matchNationalNumber(var8, var7.getEmergency(), var3);
            }
         } else {
            return false;
         }
      }
   }

   private boolean matchesPossibleNumberAndNationalNumber(String var1, Phonemetadata.PhoneNumberDesc var2) {
      return var2.getPossibleLengthCount() > 0 && !var2.getPossibleLengthList().contains(var1.length()) ? false : this.matcherApi.matchNationalNumber(var1, var2, false);
   }

   private boolean regionDialingFromMatchesNumber(Phonenumber.PhoneNumber var1, String var2) {
      return this.getRegionCodesForCountryCode(var1.getCountryCode()).contains(var2);
   }

   public boolean connectsToEmergencyNumber(String var1, String var2) {
      return this.matchesEmergencyNumberHelper(var1, var2, true);
   }

   String getExampleShortNumber(String var1) {
      Phonemetadata.PhoneMetadata var2 = MetadataManager.getShortNumberMetadataForRegion(var1);
      if (var2 == null) {
         return "";
      } else {
         Phonemetadata.PhoneNumberDesc var3 = var2.getShortCode();
         return var3.hasExampleNumber() ? var3.getExampleNumber() : "";
      }
   }

   String getExampleShortNumberForCost(String var1, ShortNumberInfo.ShortNumberCost var2) {
      Phonemetadata.PhoneMetadata var4 = MetadataManager.getShortNumberMetadataForRegion(var1);
      if (var4 == null) {
         return "";
      } else {
         Phonemetadata.PhoneNumberDesc var5 = null;
         int var3 = null.$SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[var2.ordinal()];
         if (var3 != 1) {
            if (var3 != 3) {
               if (var3 == 4) {
                  var5 = var4.getTollFree();
               }
            } else {
               var5 = var4.getStandardRate();
            }
         } else {
            var5 = var4.getPremiumRate();
         }

         return var5 != null && var5.hasExampleNumber() ? var5.getExampleNumber() : "";
      }
   }

   public ShortNumberInfo.ShortNumberCost getExpectedCost(Phonenumber.PhoneNumber var1) {
      List var3 = this.getRegionCodesForCountryCode(var1.getCountryCode());
      if (var3.size() == 0) {
         return ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
      } else if (var3.size() == 1) {
         return this.getExpectedCostForRegion(var1, (String)var3.get(0));
      } else {
         ShortNumberInfo.ShortNumberCost var4 = ShortNumberInfo.ShortNumberCost.TOLL_FREE;

         ShortNumberInfo.ShortNumberCost var9;
         for(Iterator var5 = var3.iterator(); var5.hasNext(); var4 = var9) {
            var9 = this.getExpectedCostForRegion(var1, (String)var5.next());
            int var2 = null.$SwitchMap$com$google$i18n$phonenumbers$ShortNumberInfo$ShortNumberCost[var9.ordinal()];
            if (var2 == 1) {
               return ShortNumberInfo.ShortNumberCost.PREMIUM_RATE;
            }

            if (var2 != 2) {
               if (var2 != 3) {
                  if (var2 != 4) {
                     Logger var6 = logger;
                     Level var7 = Level.SEVERE;
                     StringBuilder var8 = new StringBuilder();
                     var8.append("Unrecognised cost for region: ");
                     var8.append(var9);
                     var6.log(var7, var8.toString());
                     var9 = var4;
                  } else {
                     var9 = var4;
                  }
               } else {
                  var9 = var4;
                  if (var4 != ShortNumberInfo.ShortNumberCost.UNKNOWN_COST) {
                     var9 = ShortNumberInfo.ShortNumberCost.STANDARD_RATE;
                  }
               }
            } else {
               var9 = ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
            }
         }

         return var4;
      }
   }

   public ShortNumberInfo.ShortNumberCost getExpectedCostForRegion(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.regionDialingFromMatchesNumber(var1, var2)) {
         return ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
      } else {
         Phonemetadata.PhoneMetadata var3 = MetadataManager.getShortNumberMetadataForRegion(var2);
         if (var3 == null) {
            return ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
         } else {
            String var4 = getNationalSignificantNumber(var1);
            if (!var3.getGeneralDesc().getPossibleLengthList().contains(var4.length())) {
               return ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
            } else if (this.matchesPossibleNumberAndNationalNumber(var4, var3.getPremiumRate())) {
               return ShortNumberInfo.ShortNumberCost.PREMIUM_RATE;
            } else if (this.matchesPossibleNumberAndNationalNumber(var4, var3.getStandardRate())) {
               return ShortNumberInfo.ShortNumberCost.STANDARD_RATE;
            } else if (this.matchesPossibleNumberAndNationalNumber(var4, var3.getTollFree())) {
               return ShortNumberInfo.ShortNumberCost.TOLL_FREE;
            } else {
               return this.isEmergencyNumber(var4, var2) ? ShortNumberInfo.ShortNumberCost.TOLL_FREE : ShortNumberInfo.ShortNumberCost.UNKNOWN_COST;
            }
         }
      }
   }

   Set getSupportedRegions() {
      return MetadataManager.getSupportedShortNumberRegions();
   }

   public boolean isCarrierSpecific(Phonenumber.PhoneNumber var1) {
      String var2 = this.getRegionCodeForShortNumberFromRegionList(var1, this.getRegionCodesForCountryCode(var1.getCountryCode()));
      String var3 = getNationalSignificantNumber(var1);
      Phonemetadata.PhoneMetadata var4 = MetadataManager.getShortNumberMetadataForRegion(var2);
      return var4 != null && this.matchesPossibleNumberAndNationalNumber(var3, var4.getCarrierSpecific());
   }

   public boolean isCarrierSpecificForRegion(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.regionDialingFromMatchesNumber(var1, var2)) {
         return false;
      } else {
         String var3 = getNationalSignificantNumber(var1);
         Phonemetadata.PhoneMetadata var4 = MetadataManager.getShortNumberMetadataForRegion(var2);
         return var4 != null && this.matchesPossibleNumberAndNationalNumber(var3, var4.getCarrierSpecific());
      }
   }

   public boolean isEmergencyNumber(CharSequence var1, String var2) {
      return this.matchesEmergencyNumberHelper(var1, var2, false);
   }

   public boolean isPossibleShortNumber(Phonenumber.PhoneNumber var1) {
      List var3 = this.getRegionCodesForCountryCode(var1.getCountryCode());
      int var2 = getNationalSignificantNumber(var1).length();
      Iterator var4 = var3.iterator();

      Phonemetadata.PhoneMetadata var5;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         var5 = MetadataManager.getShortNumberMetadataForRegion((String)var4.next());
      } while(var5 == null || !var5.getGeneralDesc().getPossibleLengthList().contains(var2));

      return true;
   }

   public boolean isPossibleShortNumberForRegion(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.regionDialingFromMatchesNumber(var1, var2)) {
         return false;
      } else {
         Phonemetadata.PhoneMetadata var4 = MetadataManager.getShortNumberMetadataForRegion(var2);
         if (var4 == null) {
            return false;
         } else {
            int var3 = getNationalSignificantNumber(var1).length();
            return var4.getGeneralDesc().getPossibleLengthList().contains(var3);
         }
      }
   }

   public boolean isSmsServiceForRegion(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.regionDialingFromMatchesNumber(var1, var2)) {
         return false;
      } else {
         Phonemetadata.PhoneMetadata var3 = MetadataManager.getShortNumberMetadataForRegion(var2);
         return var3 != null && this.matchesPossibleNumberAndNationalNumber(getNationalSignificantNumber(var1), var3.getSmsServices());
      }
   }

   public boolean isValidShortNumber(Phonenumber.PhoneNumber var1) {
      List var2 = this.getRegionCodesForCountryCode(var1.getCountryCode());
      String var3 = this.getRegionCodeForShortNumberFromRegionList(var1, var2);
      return var2.size() > 1 && var3 != null ? true : this.isValidShortNumberForRegion(var1, var3);
   }

   public boolean isValidShortNumberForRegion(Phonenumber.PhoneNumber var1, String var2) {
      if (!this.regionDialingFromMatchesNumber(var1, var2)) {
         return false;
      } else {
         Phonemetadata.PhoneMetadata var4 = MetadataManager.getShortNumberMetadataForRegion(var2);
         if (var4 == null) {
            return false;
         } else {
            String var3 = getNationalSignificantNumber(var1);
            return !this.matchesPossibleNumberAndNationalNumber(var3, var4.getGeneralDesc()) ? false : this.matchesPossibleNumberAndNationalNumber(var3, var4.getShortCode());
         }
      }
   }

   public static enum ShortNumberCost {
      PREMIUM_RATE,
      STANDARD_RATE,
      TOLL_FREE,
      UNKNOWN_COST;

      static {
         ShortNumberInfo.ShortNumberCost var0 = new ShortNumberInfo.ShortNumberCost("UNKNOWN_COST", 3);
         UNKNOWN_COST = var0;
      }
   }
}
