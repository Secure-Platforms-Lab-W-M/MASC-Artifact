package javax.xml.namespace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class QName implements Serializable {
   private static final String emptyString = "".intern();
   private String localPart;
   private String namespaceURI;
   private String prefix;

   public QName(String var1) {
      String var2 = emptyString;
      this(var2, var1, var2);
   }

   public QName(String var1, String var2) {
      this(var1, var2, emptyString);
   }

   public QName(String var1, String var2, String var3) {
      if (var1 == null) {
         var1 = emptyString;
      } else {
         var1 = var1.intern();
      }

      this.namespaceURI = var1;
      if (var2 != null) {
         this.localPart = var2.intern();
         if (var3 != null) {
            this.prefix = var3.intern();
         } else {
            throw new IllegalArgumentException("invalid QName prefix");
         }
      } else {
         throw new IllegalArgumentException("invalid QName local part");
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.namespaceURI = this.namespaceURI.intern();
      this.localPart = this.localPart.intern();
      this.prefix = this.prefix.intern();
   }

   public static QName valueOf(String var0) {
      if (var0 != null && !var0.equals("")) {
         if (var0.charAt(0) == '{') {
            int var1 = var0.indexOf(125);
            if (var1 != -1) {
               if (var1 != var0.length() - 1) {
                  return new QName(var0.substring(1, var1), var0.substring(var1 + 1));
               } else {
                  throw new IllegalArgumentException("invalid QName literal");
               }
            } else {
               throw new IllegalArgumentException("invalid QName literal");
            }
         } else {
            return new QName(var0);
         }
      } else {
         throw new IllegalArgumentException("invalid QName literal");
      }
   }

   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof QName)) {
         return false;
      } else {
         return this.namespaceURI == ((QName)var1).namespaceURI && this.localPart == ((QName)var1).localPart;
      }
   }

   public String getLocalPart() {
      return this.localPart;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public final int hashCode() {
      return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
   }

   public String toString() {
      if (this.namespaceURI == emptyString) {
         return this.localPart;
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append('{');
         var1.append(this.namespaceURI);
         var1.append('}');
         var1.append(this.localPart);
         return var1.toString();
      }
   }
}
