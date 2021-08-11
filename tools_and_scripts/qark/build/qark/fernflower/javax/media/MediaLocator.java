package javax.media;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class MediaLocator implements Serializable {
   private static final long serialVersionUID = -6747425113475481405L;
   private String locatorString;

   public MediaLocator(String var1) {
      if (var1 != null) {
         this.locatorString = var1;
      } else {
         throw new NullPointerException("locatorString");
      }
   }

   public MediaLocator(URL var1) {
      this.locatorString = var1.toExternalForm();
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (!(var1 instanceof MediaLocator)) {
         return false;
      } else {
         MediaLocator var4 = (MediaLocator)var1;
         String var3 = this.locatorString;
         if (var3 == null) {
            if (var4.locatorString == null) {
               return true;
            }
         } else {
            var2 = var3.equals(var4.locatorString);
         }

         return var2;
      }
   }

   public String getProtocol() {
      int var1 = this.locatorString.indexOf(58);
      return var1 < 0 ? "" : this.locatorString.substring(0, var1);
   }

   public String getRemainder() {
      int var1 = this.locatorString.indexOf(58);
      return var1 < 0 ? "" : this.locatorString.substring(var1 + 1);
   }

   public URL getURL() throws MalformedURLException {
      return new URL(this.locatorString);
   }

   public int hashCode() {
      String var1 = this.locatorString;
      return var1 == null ? 0 : var1.hashCode();
   }

   public String toExternalForm() {
      return this.locatorString;
   }

   public String toString() {
      return this.locatorString;
   }
}
