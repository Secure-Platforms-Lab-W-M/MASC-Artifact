package util.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import util.Utils;

public class HttpHeader {
   public static final int HTTP = 1;
   public static final int HTTPS = 2;
   public static final int OTHER = 3;
   public static final int REQUEST_HEADER = 1;
   public static final int RESPONSE_HEADER = 2;
   public static final int UNKNOWN = 4;
   private static final String[] reqparamSequence = new String[]{"Cache-Control", "Connection", "Date", "Pragma", "Trailer", "Transfer-Encoding", "Upgrade", "Via", "Warning", "Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language", "Authorization", "Expect", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range", "If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent", "Allow", "Content-Encoding", "Content-Language", "Content-Length", "Content-Location", "Content-MD5", "Content-Range", "Content-Type", "Expires", "Last-Modified", "extension-header"};
   private String _first;
   private Vector _keys;
   private HashMap _mapping;
   public String hostEntry;
   public String method;
   public int protocoll;
   public String remote_host_name;
   public int remote_port;
   public int responsecode;
   public boolean tunnelMode;
   private int type;
   public String url;

   private HttpHeader() {
      this.remote_host_name = "";
      this.protocoll = 4;
      this.remote_port = 0;
      this.tunnelMode = false;
      this.responsecode = -1;
      this._keys = null;
      this._mapping = null;
   }

   public HttpHeader(int var1) {
      this.remote_host_name = "";
      this.protocoll = 4;
      this.remote_port = 0;
      this.tunnelMode = false;
      this.responsecode = -1;
      this._keys = null;
      this._mapping = null;
      this._keys = new Vector();
      this._mapping = new HashMap();
      this.type = var1;
   }

   public HttpHeader(InputStream var1, int var2) throws IOException {
      this.remote_host_name = "";
      this.protocoll = 4;
      this.remote_port = 0;
      this.tunnelMode = false;
      this.responsecode = -1;
      this._keys = null;
      this._mapping = null;
      this._keys = new Vector();
      this._mapping = new HashMap();
      if (var2 != 1 && var2 != 2) {
         throw new IOException("INVALID TYPE!");
      } else {
         this.type = var2;
         this._first = Utils.readLineFromStream(var1, true);
         StringBuilder var10;
         if (var2 == 1) {
            this.parseURI();
            if (this.hostEntry != null) {
               this.setValue("Host", this.hostEntry);
            }
         } else {
            if (this._first.length() < 12) {
               var10 = new StringBuilder();
               var10.append("Invalid Response Header:");
               var10.append(this._first);
               throw new IOException(var10.toString());
            }

            if (!this._first.substring(0, 12).toLowerCase().startsWith("http/")) {
               var10 = new StringBuilder();
               var10.append("Invalid Response Header:");
               var10.append(this._first);
               throw new IOException(var10.toString());
            }

            try {
               this.responsecode = Integer.parseInt(this._first.substring(9, 12));
               StringBuilder var4 = new StringBuilder();
               var4.append("HTTP/1.1 ");
               var4.append(this.responsecode);
               var4.append(this._first.substring(12));
               this._first = var4.toString();
            } catch (Exception var9) {
               throw new IOException(var9.getMessage());
            }
         }

         for(String var11 = Utils.readLineFromStream(var1, true); !var11.equals(""); var11 = Utils.readLineFromStream(var1, true)) {
            int var3 = var11.indexOf(": ");
            String var5;
            if (var3 == -1) {
               var3 = var11.indexOf(":");
               if (var3 == -1) {
                  var10 = new StringBuilder();
                  var10.append("Invalid header:");
                  var10.append(var11);
                  throw new IOException(var10.toString());
               }

               var5 = var11.substring(0, var3).trim();
               var11 = var11.substring(var3 + 1).trim();
            } else {
               var5 = var11.substring(0, var3).trim();
               var11 = var11.substring(var3 + 2).trim();
            }

            String var7 = var5.toUpperCase();
            String var6 = (String)this._mapping.get(var7);
            if (var6 == null) {
               this._keys.add(var5);
               this._mapping.put(var7, var11);
            } else if (!var7.equals("CONTENT-LENGTH")) {
               if (!var7.equals("HOST")) {
                  HashMap var12 = this._mapping;
                  StringBuilder var8 = new StringBuilder();
                  var8.append(var6);
                  var8.append("_,_");
                  var8.append(var11);
                  var12.put(var7, var8.toString());
               }
            } else if (!var6.equals(var11)) {
               var10 = new StringBuilder();
               var10.append("Invalid Header! Duplicated Content-Length with different values:");
               var10.append(var6);
               var10.append("<>");
               var10.append(var11);
               var10.append("!");
               throw new IOException(var10.toString());
            }
         }

         if (this.hostEntry == null && var2 == 1) {
            this.hostEntry = this.getValue("Host");
            if (this.hostEntry != null) {
               this.parseHostEntry();
            } else {
               throw new IOException("Bad Request - No Host specified!");
            }
         }
      }
   }

   public HttpHeader(String var1, int var2) throws IOException {
      this((InputStream)(new ByteArrayInputStream(var1.getBytes())), var2);
   }

   private void parseHostEntry() throws IOException {
      if (this.protocoll == 1) {
         this.remote_port = 80;
      } else if (this.protocoll == 2) {
         this.remote_port = 443;
      } else {
         this.remote_port = -1;
      }

      this.remote_host_name = this.hostEntry;
      int var1 = this.hostEntry.lastIndexOf(":");
      if (var1 != -1 && !this.hostEntry.endsWith("]")) {
         try {
            this.remote_port = Integer.parseInt(this.hostEntry.substring(var1 + 1));
         } catch (NumberFormatException var3) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Bad Request - Cannot parse port to int:");
            var2.append(this._first);
            throw new IOException(var2.toString());
         }

         this.remote_host_name = this.hostEntry.substring(0, var1);
      }

      if (this.remote_host_name.startsWith("[") && this.remote_host_name.endsWith("]")) {
         this.remote_host_name = this.remote_host_name.substring(1, this.remote_host_name.length() - 1);
      }

   }

   private void parseURI() throws IOException {
      int var1 = this._first.indexOf(32);
      int var2 = this._first.lastIndexOf(32);
      if (var1 != -1 && var1 != var2) {
         this.method = this._first.substring(0, var1);
         this.url = this._first.substring(var1 + 1, var2);
         this.tunnelMode = this.method.equalsIgnoreCase("CONNECT");
         if (!this.tunnelMode) {
            var1 = this.url.indexOf("://");
            if (var1 != -1) {
               String var4 = this.url.substring(0, var1).toLowerCase();
               if (var4.equals("http")) {
                  this.protocoll = 1;
               } else if (var4.equals("https")) {
                  this.protocoll = 2;
               } else {
                  this.protocoll = 3;
               }

               this.url = this.url.substring(var1 + 3);
               var2 = this.url.indexOf(47);
               var1 = var2;
               if (var2 == -1) {
                  var1 = this.url.length();
               }

               this.hostEntry = this.url.substring(0, var1);
               if (var1 == this.url.length()) {
                  this.url = "/";
               } else {
                  this.url = this.url.substring(var1);
               }
            }
         } else {
            this.hostEntry = this.url;
         }

         if (this.hostEntry != null) {
            this.parseHostEntry();
         }

      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Bad Request:");
         var3.append(this._first);
         throw new IOException(var3.toString());
      }
   }

   public void appendValueToHeaderString(StringBuffer var1, String var2, String var3) {
      String[] var6 = var3.split("_,_");

      for(int var4 = 0; var4 < var6.length; ++var4) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var2);
         var5.append(": ");
         var5.append(var6[var4]);
         var5.append("\r\n");
         var1.append(var5.toString());
      }

   }

   public boolean chunkedTransfer() {
      String var1 = this.getValue("Transfer-Encoding");
      return var1 != null ? ((String)var1).equalsIgnoreCase("chunked") : false;
   }

   public HttpHeader clone() {
      HttpHeader var1 = new HttpHeader();
      var1.remote_host_name = this.remote_host_name;
      var1.hostEntry = this.hostEntry;
      var1.url = this.url;
      var1.method = this.method;
      var1.remote_port = this.remote_port;
      var1.tunnelMode = this.tunnelMode;
      var1.responsecode = this.responsecode;
      var1._first = this._first;
      var1._keys = (Vector)this._keys.clone();
      var1._mapping = (HashMap)this._mapping.clone();
      var1.type = this.type;
      return var1;
   }

   public boolean getConnectionClose() {
      String var1 = this.getValue("Connection");
      if (var1 == null) {
         return false;
      } else {
         return var1.equalsIgnoreCase("close");
      }
   }

   public long getContentLength() {
      if (this.responsecode != 304 && this.responsecode != 204) {
         String var1 = this.getValue("Content-Length");
         return var1 != null ? Long.parseLong(var1) : -1L;
      } else {
         return 0L;
      }
   }

   public String getHeaderString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this._first);
      var1.append("\r\n");
      StringBuffer var4 = new StringBuffer(var1.toString());
      Iterator var2 = this._keys.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.appendValueToHeaderString(var4, var3, (String)this._mapping.get(var3.toUpperCase()));
      }

      var4.append("\r\n");
      return var4.toString();
   }

   public int getResponseCode() {
      if (this.type != 2) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this);
         var1.append(" is not a ResonseHeader!");
         throw new IllegalStateException(var1.toString());
      } else {
         return this.responsecode;
      }
   }

   public String getResponseMessage() {
      if (this.type != 2) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this);
         var1.append(" is not a ResonseHeader!");
         throw new IllegalStateException(var1.toString());
      } else {
         return this._first;
      }
   }

   public String getServerRequestHeader() {
      return this.getServerRequestHeader(false);
   }

   public String getServerRequestHeader(boolean var1) {
      String var3 = this._first;
      if (!this.tunnelMode) {
         StringBuilder var9;
         if (var1) {
            var9 = new StringBuilder();
            var9.append(this.method);
            var9.append(" http://");
            var9.append(this.hostEntry);
            var9.append(this.url);
            var9.append(" HTTP/1.1");
            var3 = var9.toString();
         } else {
            var9 = new StringBuilder();
            var9.append(this.method);
            var9.append(" ");
            var9.append(this.url);
            var9.append(" HTTP/1.1");
            var3 = var9.toString();
         }
      }

      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      var4.append("\r\n");
      StringBuffer var5 = new StringBuffer(var4.toString());
      HashMap var6 = (HashMap)this._mapping.clone();

      String var10;
      for(int var2 = 0; var2 < reqparamSequence.length; ++var2) {
         var10 = reqparamSequence[var2];
         String var7 = (String)var6.remove(var10.toUpperCase());
         if (var7 != null) {
            var3 = var10;
            if (var1) {
               var3 = var10;
               if (var10.toUpperCase().equals("CONNECTION")) {
                  var3 = "Proxy-Connection";
               }
            }

            if (var7.length() > 0) {
               this.appendValueToHeaderString(var5, var3, var7);
            } else {
               var4 = new StringBuilder();
               var4.append(var3);
               var4.append(":\r\n");
               var5.append(var4.toString());
            }
         }
      }

      Iterator var11 = this._keys.iterator();

      while(var11.hasNext()) {
         var10 = (String)var11.next();
         String var8 = (String)var6.remove(var10.toUpperCase());
         if (var8 != null) {
            var3 = var10;
            if (!var1) {
               var3 = var10;
               if (var10.toUpperCase().equals("PROXY-CONNECTION")) {
                  var3 = "Connection";
               }
            }

            if (var8.length() > 0) {
               this.appendValueToHeaderString(var5, var3, var8);
            } else {
               var4 = new StringBuilder();
               var4.append(var3);
               var4.append(":\r\n");
               var5.append(var4.toString());
            }
         }
      }

      var5.append("\r\n");
      return var5.toString();
   }

   public String getValue(String var1) {
      return (String)this._mapping.get(var1.toUpperCase());
   }

   public String removeValue(String var1) {
      this._keys.remove(var1);
      return (String)this._mapping.remove(var1.toUpperCase());
   }

   public void setHostEntry(String var1) throws IOException {
      this.hostEntry = var1;
      this.setValue("Host", var1);
      this.parseHostEntry();
   }

   public void setRequest(String var1) throws IOException {
      this._first = var1;
      this.parseURI();
      this.setValue("Host", this.hostEntry);
   }

   public void setValue(String var1, String var2) {
      if (this.getValue(var1) == null) {
         this._keys.add(var1);
      }

      this._mapping.put(var1.toUpperCase(), var2);
   }
}
