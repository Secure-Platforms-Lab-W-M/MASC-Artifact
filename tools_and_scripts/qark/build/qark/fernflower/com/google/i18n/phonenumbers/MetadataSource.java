package com.google.i18n.phonenumbers;

interface MetadataSource {
   Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int var1);

   Phonemetadata.PhoneMetadata getMetadataForRegion(String var1);
}
