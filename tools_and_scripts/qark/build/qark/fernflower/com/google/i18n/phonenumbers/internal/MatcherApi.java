package com.google.i18n.phonenumbers.internal;

import com.google.i18n.phonenumbers.Phonemetadata;

public interface MatcherApi {
   boolean matchNationalNumber(CharSequence var1, Phonemetadata.PhoneNumberDesc var2, boolean var3);
}
