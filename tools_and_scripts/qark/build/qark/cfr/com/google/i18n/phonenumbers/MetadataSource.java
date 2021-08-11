/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata;

interface MetadataSource {
    public Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int var1);

    public Phonemetadata.PhoneMetadata getMetadataForRegion(String var1);
}

