package javax.jmdns.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public abstract class DNSEntry {
   private final DNSRecordClass _dnsClass;
   private final String _key;
   private final String _name;
   final Map _qualifiedNameMap;
   private final DNSRecordType _recordType;
   private final String _type;
   private final boolean _unique;

   DNSEntry(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
      this._name = var1;
      this._recordType = var2;
      this._dnsClass = var3;
      this._unique = var4;
      Map var10 = ServiceInfoImpl.decodeQualifiedNameMapForType(this.getName());
      this._qualifiedNameMap = var10;
      String var6 = (String)var10.get(ServiceInfo.Fields.Domain);
      String var8 = (String)this._qualifiedNameMap.get(ServiceInfo.Fields.Protocol);
      var1 = (String)this._qualifiedNameMap.get(ServiceInfo.Fields.Application);
      String var13 = ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Instance)).toLowerCase();
      StringBuilder var7 = new StringBuilder();
      int var5 = var1.length();
      String var11 = "";
      if (var5 > 0) {
         StringBuilder var9 = new StringBuilder();
         var9.append("_");
         var9.append(var1);
         var9.append(".");
         var1 = var9.toString();
      } else {
         var1 = "";
      }

      var7.append(var1);
      StringBuilder var12;
      if (var8.length() > 0) {
         var12 = new StringBuilder();
         var12.append("_");
         var12.append(var8);
         var12.append(".");
         var1 = var12.toString();
      } else {
         var1 = "";
      }

      var7.append(var1);
      var7.append(var6);
      var7.append(".");
      this._type = var7.toString();
      StringBuilder var14 = new StringBuilder();
      var1 = var11;
      if (var13.length() > 0) {
         var12 = new StringBuilder();
         var12.append(var13);
         var12.append(".");
         var1 = var12.toString();
      }

      var14.append(var1);
      var14.append(this._type);
      this._key = var14.toString().toLowerCase();
   }

   public int compareTo(DNSEntry var1) {
      byte[] var4 = this.toByteArray();
      byte[] var5 = var1.toByteArray();
      int var2 = 0;

      for(int var3 = Math.min(var4.length, var5.length); var2 < var3; ++var2) {
         if (var4[var2] > var5[var2]) {
            return 1;
         }

         if (var4[var2] < var5[var2]) {
            return -1;
         }
      }

      return var4.length - var5.length;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 instanceof DNSEntry) {
         DNSEntry var3 = (DNSEntry)var1;
         if (this.getKey().equals(var3.getKey()) && this.getRecordType().equals(var3.getRecordType()) && this.getRecordClass() == var3.getRecordClass()) {
            var2 = true;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public String getKey() {
      String var1 = this._key;
      return var1 != null ? var1 : "";
   }

   public String getName() {
      String var1 = this._name;
      return var1 != null ? var1 : "";
   }

   public Map getQualifiedNameMap() {
      return Collections.unmodifiableMap(this._qualifiedNameMap);
   }

   public DNSRecordClass getRecordClass() {
      DNSRecordClass var1 = this._dnsClass;
      return var1 != null ? var1 : DNSRecordClass.CLASS_UNKNOWN;
   }

   public DNSRecordType getRecordType() {
      DNSRecordType var1 = this._recordType;
      return var1 != null ? var1 : DNSRecordType.TYPE_IGNORE;
   }

   public String getSubtype() {
      String var1 = (String)this.getQualifiedNameMap().get(ServiceInfo.Fields.Subtype);
      return var1 != null ? var1 : "";
   }

   public String getType() {
      String var1 = this._type;
      return var1 != null ? var1 : "";
   }

   public int hashCode() {
      return this.getKey().hashCode() + this.getRecordType().indexValue() + this.getRecordClass().indexValue();
   }

   public boolean isDomainDiscoveryQuery() {
      boolean var2 = ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Application)).equals("dns-sd");
      boolean var1 = false;
      if (!var2) {
         return false;
      } else {
         String var3 = (String)this._qualifiedNameMap.get(ServiceInfo.Fields.Instance);
         if ("b".equals(var3) || "db".equals(var3) || "r".equals(var3) || "dr".equals(var3) || "lb".equals(var3)) {
            var1 = true;
         }

         return var1;
      }
   }

   public abstract boolean isExpired(long var1);

   public boolean isReverseLookup() {
      return this.isV4ReverseLookup() || this.isV6ReverseLookup();
   }

   public boolean isSameEntry(DNSEntry var1) {
      return this.getKey().equals(var1.getKey()) && this.matchRecordType(var1.getRecordType()) && this.matchRecordClass(var1.getRecordClass());
   }

   public boolean isSameRecordClass(DNSEntry var1) {
      return var1 != null && var1.getRecordClass() == this.getRecordClass();
   }

   public boolean isSameType(DNSEntry var1) {
      return var1 != null && var1.getRecordType() == this.getRecordType();
   }

   public boolean isServicesDiscoveryMetaQuery() {
      return ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Application)).equals("dns-sd") && ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Instance)).equals("_services");
   }

   public abstract boolean isStale(long var1);

   public boolean isUnique() {
      return this._unique;
   }

   public boolean isV4ReverseLookup() {
      return ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Domain)).endsWith("in-addr.arpa");
   }

   public boolean isV6ReverseLookup() {
      return ((String)this._qualifiedNameMap.get(ServiceInfo.Fields.Domain)).endsWith("ip6.arpa");
   }

   public boolean matchRecordClass(DNSRecordClass var1) {
      return DNSRecordClass.CLASS_ANY == var1 || DNSRecordClass.CLASS_ANY == this.getRecordClass() || this.getRecordClass().equals(var1);
   }

   public boolean matchRecordType(DNSRecordType var1) {
      return this.getRecordType().equals(var1);
   }

   public boolean sameSubtype(DNSEntry var1) {
      return this.getSubtype().equals(var1.getSubtype());
   }

   protected void toByteArray(DataOutputStream var1) throws IOException {
      var1.write(this.getName().getBytes("UTF8"));
      var1.writeShort(this.getRecordType().indexValue());
      var1.writeShort(this.getRecordClass().indexValue());
   }

   protected byte[] toByteArray() {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);
         this.toByteArray(var2);
         var2.close();
         byte[] var4 = var1.toByteArray();
         return var4;
      } catch (IOException var3) {
         throw new InternalError();
      }
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder(200);
      var2.append('[');
      var2.append(this.getClass().getSimpleName());
      var2.append('@');
      var2.append(System.identityHashCode(this));
      var2.append(" type: ");
      var2.append(this.getRecordType());
      var2.append(", class: ");
      var2.append(this.getRecordClass());
      String var1;
      if (this._unique) {
         var1 = "-unique,";
      } else {
         var1 = ",";
      }

      var2.append(var1);
      var2.append(" name: ");
      var2.append(this._name);
      this.toString(var2);
      var2.append(']');
      return var2.toString();
   }

   protected void toString(StringBuilder var1) {
   }
}
