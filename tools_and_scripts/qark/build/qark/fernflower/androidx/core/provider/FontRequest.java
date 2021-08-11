package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

public final class FontRequest {
   private final List mCertificates;
   private final int mCertificatesArray;
   private final String mIdentifier;
   private final String mProviderAuthority;
   private final String mProviderPackage;
   private final String mQuery;

   public FontRequest(String var1, String var2, String var3, int var4) {
      this.mProviderAuthority = (String)Preconditions.checkNotNull(var1);
      this.mProviderPackage = (String)Preconditions.checkNotNull(var2);
      this.mQuery = (String)Preconditions.checkNotNull(var3);
      this.mCertificates = null;
      boolean var5;
      if (var4 != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Preconditions.checkArgument(var5);
      this.mCertificatesArray = var4;
      StringBuilder var6 = new StringBuilder(this.mProviderAuthority);
      var6.append("-");
      var6.append(this.mProviderPackage);
      var6.append("-");
      var6.append(this.mQuery);
      this.mIdentifier = var6.toString();
   }

   public FontRequest(String var1, String var2, String var3, List var4) {
      this.mProviderAuthority = (String)Preconditions.checkNotNull(var1);
      this.mProviderPackage = (String)Preconditions.checkNotNull(var2);
      this.mQuery = (String)Preconditions.checkNotNull(var3);
      this.mCertificates = (List)Preconditions.checkNotNull(var4);
      this.mCertificatesArray = 0;
      StringBuilder var5 = new StringBuilder(this.mProviderAuthority);
      var5.append("-");
      var5.append(this.mProviderPackage);
      var5.append("-");
      var5.append(this.mQuery);
      this.mIdentifier = var5.toString();
   }

   public List getCertificates() {
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
      StringBuilder var3 = new StringBuilder();
      StringBuilder var4 = new StringBuilder();
      var4.append("FontRequest {mProviderAuthority: ");
      var4.append(this.mProviderAuthority);
      var4.append(", mProviderPackage: ");
      var4.append(this.mProviderPackage);
      var4.append(", mQuery: ");
      var4.append(this.mQuery);
      var4.append(", mCertificates:");
      var3.append(var4.toString());

      for(int var1 = 0; var1 < this.mCertificates.size(); ++var1) {
         var3.append(" [");
         List var5 = (List)this.mCertificates.get(var1);

         for(int var2 = 0; var2 < var5.size(); ++var2) {
            var3.append(" \"");
            var3.append(Base64.encodeToString((byte[])var5.get(var2), 0));
            var3.append("\"");
         }

         var3.append(" ]");
      }

      var3.append("}");
      var4 = new StringBuilder();
      var4.append("mCertificatesArray: ");
      var4.append(this.mCertificatesArray);
      var3.append(var4.toString());
      return var3.toString();
   }
}
