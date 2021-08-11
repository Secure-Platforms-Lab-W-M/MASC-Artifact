/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

public final class FontRequest {
    private final List<List<byte[]>> mCertificates;
    private final int mCertificatesArray;
    private final String mIdentifier;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;

    public FontRequest(String charSequence, String string2, String string3, int n) {
        this.mProviderAuthority = Preconditions.checkNotNull(charSequence);
        this.mProviderPackage = Preconditions.checkNotNull(string2);
        this.mQuery = Preconditions.checkNotNull(string3);
        this.mCertificates = null;
        boolean bl = n != 0;
        Preconditions.checkArgument(bl);
        this.mCertificatesArray = n;
        charSequence = new StringBuilder(this.mProviderAuthority);
        charSequence.append("-");
        charSequence.append(this.mProviderPackage);
        charSequence.append("-");
        charSequence.append(this.mQuery);
        this.mIdentifier = charSequence.toString();
    }

    public FontRequest(String charSequence, String string2, String string3, List<List<byte[]>> list) {
        this.mProviderAuthority = Preconditions.checkNotNull(charSequence);
        this.mProviderPackage = Preconditions.checkNotNull(string2);
        this.mQuery = Preconditions.checkNotNull(string3);
        this.mCertificates = Preconditions.checkNotNull(list);
        this.mCertificatesArray = 0;
        charSequence = new StringBuilder(this.mProviderAuthority);
        charSequence.append("-");
        charSequence.append(this.mProviderPackage);
        charSequence.append("-");
        charSequence.append(this.mQuery);
        this.mIdentifier = charSequence.toString();
    }

    public List<List<byte[]>> getCertificates() {
        return this.mCertificates;
    }

    public int getCertificatesArrayResId() {
        return this.mCertificatesArray;
    }

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public String getProviderAuthority() {
        return this.mProviderAuthority;
    }

    public String getProviderPackage() {
        return this.mProviderPackage;
    }

    public String getQuery() {
        return this.mQuery;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new StringBuilder();
        object.append("FontRequest {mProviderAuthority: ");
        object.append(this.mProviderAuthority);
        object.append(", mProviderPackage: ");
        object.append(this.mProviderPackage);
        object.append(", mQuery: ");
        object.append(this.mQuery);
        object.append(", mCertificates:");
        stringBuilder.append(object.toString());
        for (int i = 0; i < this.mCertificates.size(); ++i) {
            stringBuilder.append(" [");
            object = this.mCertificates.get(i);
            for (int j = 0; j < object.size(); ++j) {
                stringBuilder.append(" \"");
                stringBuilder.append(Base64.encodeToString((byte[])((byte[])object.get(j)), (int)0));
                stringBuilder.append("\"");
            }
            stringBuilder.append(" ]");
        }
        stringBuilder.append("}");
        object = new StringBuilder();
        object.append("mCertificatesArray: ");
        object.append(this.mCertificatesArray);
        stringBuilder.append(object.toString());
        return stringBuilder.toString();
    }
}

