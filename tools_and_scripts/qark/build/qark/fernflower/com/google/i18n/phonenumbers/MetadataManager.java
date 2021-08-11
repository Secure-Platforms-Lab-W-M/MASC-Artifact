package com.google.i18n.phonenumbers;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

final class MetadataManager {
   private static final String ALTERNATE_FORMATS_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberAlternateFormatsProto";
   static final MetadataLoader DEFAULT_METADATA_LOADER = new MetadataLoader() {
      public InputStream loadMetadata(String var1) {
         return MetadataManager.class.getResourceAsStream(var1);
      }
   };
   static final String MULTI_FILE_PHONE_NUMBER_METADATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberMetadataProto";
   private static final String SHORT_NUMBER_METADATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/ShortNumberMetadataProto";
   static final String SINGLE_FILE_PHONE_NUMBER_METADATA_FILE_NAME = "/com/google/i18n/phonenumbers/data/SingleFilePhoneNumberMetadataProto";
   private static final Set alternateFormatsCountryCodes = AlternateFormatsCountryCodeSet.getCountryCodeSet();
   private static final ConcurrentHashMap alternateFormatsMap = new ConcurrentHashMap();
   private static final Logger logger = Logger.getLogger(MetadataManager.class.getName());
   private static final ConcurrentHashMap shortNumberMetadataMap = new ConcurrentHashMap();
   private static final Set shortNumberMetadataRegionCodes = ShortNumbersRegionCodeSet.getRegionCodeSet();

   private MetadataManager() {
   }

   static Phonemetadata.PhoneMetadata getAlternateFormatsForCountry(int var0) {
      return !alternateFormatsCountryCodes.contains(var0) ? null : getMetadataFromMultiFilePrefix(var0, alternateFormatsMap, "/com/google/i18n/phonenumbers/data/PhoneNumberAlternateFormatsProto", DEFAULT_METADATA_LOADER);
   }

   static Phonemetadata.PhoneMetadata getMetadataFromMultiFilePrefix(Object var0, ConcurrentHashMap var1, String var2, MetadataLoader var3) {
      Phonemetadata.PhoneMetadata var4 = (Phonemetadata.PhoneMetadata)var1.get(var0);
      if (var4 != null) {
         return var4;
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append(var2);
         var10.append("_");
         var10.append(var0);
         var2 = var10.toString();
         List var9 = getMetadataFromSingleFileName(var2, var3);
         if (var9.size() > 1) {
            Logger var11 = logger;
            Level var5 = Level.WARNING;
            StringBuilder var6 = new StringBuilder();
            var6.append("more than one metadata in file ");
            var6.append(var2);
            var11.log(var5, var6.toString());
         }

         Phonemetadata.PhoneMetadata var8 = (Phonemetadata.PhoneMetadata)var9.get(0);
         Phonemetadata.PhoneMetadata var7 = (Phonemetadata.PhoneMetadata)var1.putIfAbsent(var0, var8);
         return var7 != null ? var7 : var8;
      }
   }

   private static List getMetadataFromSingleFileName(String var0, MetadataLoader var1) {
      InputStream var2 = var1.loadMetadata(var0);
      StringBuilder var3;
      if (var2 != null) {
         List var4 = loadMetadataAndCloseInput(var2).getMetadataList();
         if (var4.size() != 0) {
            return var4;
         } else {
            var3 = new StringBuilder();
            var3.append("empty metadata: ");
            var3.append(var0);
            throw new IllegalStateException(var3.toString());
         }
      } else {
         var3 = new StringBuilder();
         var3.append("missing metadata: ");
         var3.append(var0);
         throw new IllegalStateException(var3.toString());
      }
   }

   static Phonemetadata.PhoneMetadata getShortNumberMetadataForRegion(String var0) {
      return !shortNumberMetadataRegionCodes.contains(var0) ? null : getMetadataFromMultiFilePrefix(var0, shortNumberMetadataMap, "/com/google/i18n/phonenumbers/data/ShortNumberMetadataProto", DEFAULT_METADATA_LOADER);
   }

   static MetadataManager.SingleFileMetadataMaps getSingleFileMetadataMaps(AtomicReference var0, String var1, MetadataLoader var2) {
      MetadataManager.SingleFileMetadataMaps var3 = (MetadataManager.SingleFileMetadataMaps)var0.get();
      if (var3 != null) {
         return var3;
      } else {
         var0.compareAndSet((Object)null, MetadataManager.SingleFileMetadataMaps.load(var1, var2));
         return (MetadataManager.SingleFileMetadataMaps)var0.get();
      }
   }

   static Set getSupportedShortNumberRegions() {
      return Collections.unmodifiableSet(shortNumberMetadataRegionCodes);
   }

   private static Phonemetadata.PhoneMetadataCollection loadMetadataAndCloseInput(InputStream param0) {
      // $FF: Couldn't be decompiled
   }

   static class SingleFileMetadataMaps {
      private final Map countryCallingCodeToMetadata;
      private final Map regionCodeToMetadata;

      private SingleFileMetadataMaps(Map var1, Map var2) {
         this.regionCodeToMetadata = Collections.unmodifiableMap(var1);
         this.countryCallingCodeToMetadata = Collections.unmodifiableMap(var2);
      }

      static MetadataManager.SingleFileMetadataMaps load(String var0, MetadataLoader var1) {
         List var2 = MetadataManager.getMetadataFromSingleFileName(var0, var1);
         HashMap var5 = new HashMap();
         HashMap var6 = new HashMap();
         Iterator var7 = var2.iterator();

         while(var7.hasNext()) {
            Phonemetadata.PhoneMetadata var3 = (Phonemetadata.PhoneMetadata)var7.next();
            String var4 = var3.getId();
            if ("001".equals(var4)) {
               var6.put(var3.getCountryCode(), var3);
            } else {
               var5.put(var4, var3);
            }
         }

         return new MetadataManager.SingleFileMetadataMaps(var5, var6);
      }

      Phonemetadata.PhoneMetadata get(int var1) {
         return (Phonemetadata.PhoneMetadata)this.countryCallingCodeToMetadata.get(var1);
      }

      Phonemetadata.PhoneMetadata get(String var1) {
         return (Phonemetadata.PhoneMetadata)this.regionCodeToMetadata.get(var1);
      }
   }
}
