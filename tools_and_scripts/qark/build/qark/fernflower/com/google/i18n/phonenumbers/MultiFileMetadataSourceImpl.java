package com.google.i18n.phonenumbers;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

final class MultiFileMetadataSourceImpl implements MetadataSource {
   private final ConcurrentHashMap geographicalRegions;
   private final MetadataLoader metadataLoader;
   private final ConcurrentHashMap nonGeographicalRegions;
   private final String phoneNumberMetadataFilePrefix;

   MultiFileMetadataSourceImpl(MetadataLoader var1) {
      this("/com/google/i18n/phonenumbers/data/PhoneNumberMetadataProto", var1);
   }

   MultiFileMetadataSourceImpl(String var1, MetadataLoader var2) {
      this.geographicalRegions = new ConcurrentHashMap();
      this.nonGeographicalRegions = new ConcurrentHashMap();
      this.phoneNumberMetadataFilePrefix = var1;
      this.metadataLoader = var2;
   }

   private boolean isNonGeographical(int var1) {
      List var2 = (List)CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap().get(var1);
      return var2.size() == 1 && "001".equals(var2.get(0));
   }

   public Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int var1) {
      return !this.isNonGeographical(var1) ? null : MetadataManager.getMetadataFromMultiFilePrefix(var1, this.nonGeographicalRegions, this.phoneNumberMetadataFilePrefix, this.metadataLoader);
   }

   public Phonemetadata.PhoneMetadata getMetadataForRegion(String var1) {
      return MetadataManager.getMetadataFromMultiFilePrefix(var1, this.geographicalRegions, this.phoneNumberMetadataFilePrefix, this.metadataLoader);
   }
}
