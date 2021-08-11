package com.google.i18n.phonenumbers;

import java.util.concurrent.atomic.AtomicReference;

final class SingleFileMetadataSourceImpl implements MetadataSource {
   private final MetadataLoader metadataLoader;
   private final String phoneNumberMetadataFileName;
   private final AtomicReference phoneNumberMetadataRef;

   SingleFileMetadataSourceImpl(MetadataLoader var1) {
      this("/com/google/i18n/phonenumbers/data/SingleFilePhoneNumberMetadataProto", var1);
   }

   SingleFileMetadataSourceImpl(String var1, MetadataLoader var2) {
      this.phoneNumberMetadataRef = new AtomicReference();
      this.phoneNumberMetadataFileName = var1;
      this.metadataLoader = var2;
   }

   public Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int var1) {
      return MetadataManager.getSingleFileMetadataMaps(this.phoneNumberMetadataRef, this.phoneNumberMetadataFileName, this.metadataLoader).get(var1);
   }

   public Phonemetadata.PhoneMetadata getMetadataForRegion(String var1) {
      return MetadataManager.getSingleFileMetadataMaps(this.phoneNumberMetadataRef, this.phoneNumberMetadataFileName, this.metadataLoader).get(var1);
   }
}
