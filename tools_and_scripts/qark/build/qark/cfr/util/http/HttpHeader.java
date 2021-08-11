/*
 * Decompiled with CFR 0_124.
 */
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

    public HttpHeader(int n) {
        this.remote_host_name = "";
        this.protocoll = 4;
        this.remote_port = 0;
        this.tunnelMode = false;
        this.responsecode = -1;
        this._keys = null;
        this._mapping = null;
        this._keys = new Vector();
        this._mapping = new HashMap();
        this.type = n;
    }

    public HttpHeader(InputStream object, int n) throws IOException {
        block22 : {
            String string2;
            block21 : {
                block20 : {
                    this.remote_host_name = "";
                    this.protocoll = 4;
                    this.remote_port = 0;
                    this.tunnelMode = false;
                    this.responsecode = -1;
                    this._keys = null;
                    this._mapping = null;
                    this._keys = new Vector();
                    this._mapping = new HashMap();
                    if (n != 1 && n != 2) {
                        throw new IOException("INVALID TYPE!");
                    }
                    this.type = n;
                    this._first = Utils.readLineFromStream((InputStream)object, true);
                    if (n != 1) break block20;
                    this.parseURI();
                    if (this.hostEntry != null) {
                        this.setValue("Host", this.hostEntry);
                    }
                    break block21;
                }
                if (this._first.length() < 12) {
                    object = new StringBuilder();
                    object.append("Invalid Response Header:");
                    object.append(this._first);
                    throw new IOException(object.toString());
                }
                if (!this._first.substring(0, 12).toLowerCase().startsWith("http/")) break block22;
                try {
                    this.responsecode = Integer.parseInt(this._first.substring(9, 12));
                    string2 = new StringBuilder();
                    string2.append("HTTP/1.1 ");
                    string2.append(this.responsecode);
                    string2.append(this._first.substring(12));
                    this._first = string2.toString();
                }
                catch (Exception exception) {
                    throw new IOException(exception.getMessage());
                }
            }
            string2 = Utils.readLineFromStream((InputStream)object, true);
            while (!string2.equals("")) {
                Object object2;
                int n2 = string2.indexOf(": ");
                if (n2 == -1) {
                    n2 = string2.indexOf(":");
                    if (n2 == -1) {
                        object = new StringBuilder();
                        object.append("Invalid header:");
                        object.append(string2);
                        throw new IOException(object.toString());
                    }
                    object2 = string2.substring(0, n2).trim();
                    string2 = string2.substring(n2 + 1).trim();
                } else {
                    object2 = string2.substring(0, n2).trim();
                    string2 = string2.substring(n2 + 2).trim();
                }
                String string3 = object2.toUpperCase();
                String string4 = (String)this._mapping.get(string3);
                if (string4 == null) {
                    this._keys.add(object2);
                    this._mapping.put(string3, string2);
                } else if (!string3.equals("CONTENT-LENGTH")) {
                    if (!string3.equals("HOST")) {
                        object2 = this._mapping;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(string4);
                        stringBuilder.append("_,_");
                        stringBuilder.append(string2);
                        object2.put(string3, stringBuilder.toString());
                    }
                } else if (!string4.equals(string2)) {
                    object = new StringBuilder();
                    object.append("Invalid Header! Duplicated Content-Length with different values:");
                    object.append(string4);
                    object.append("<>");
                    object.append(string2);
                    object.append("!");
                    throw new IOException(object.toString());
                }
                string2 = Utils.readLineFromStream((InputStream)object, true);
            }
            if (this.hostEntry == null && n == 1) {
                this.hostEntry = this.getValue("Host");
                if (this.hostEntry != null) {
                    this.parseHostEntry();
                    return;
                }
                throw new IOException("Bad Request - No Host specified!");
            }
            return;
        }
        object = new StringBuilder();
        object.append("Invalid Response Header:");
        object.append(this._first);
        throw new IOException(object.toString());
    }

    public HttpHeader(String string2, int n) throws IOException {
        this(new ByteArrayInputStream(string2.getBytes()), n);
    }

    private void parseHostEntry() throws IOException {
        this.remote_port = this.protocoll == 1 ? 80 : (this.protocoll == 2 ? 443 : -1);
        this.remote_host_name = this.hostEntry;
        int n = this.hostEntry.lastIndexOf(":");
        if (n != -1 && !this.hostEntry.endsWith("]")) {
            try {
                this.remote_port = Integer.parseInt(this.hostEntry.substring(n + 1));
            }
            catch (NumberFormatException numberFormatException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Bad Request - Cannot parse port to int:");
                stringBuilder.append(this._first);
                throw new IOException(stringBuilder.toString());
            }
            this.remote_host_name = this.hostEntry.substring(0, n);
        }
        if (this.remote_host_name.startsWith("[") && this.remote_host_name.endsWith("]")) {
            this.remote_host_name = this.remote_host_name.substring(1, this.remote_host_name.length() - 1);
        }
    }

    private void parseURI() throws IOException {
        int n = this._first.indexOf(32);
        int n2 = this._first.lastIndexOf(32);
        if (n != -1 && n != n2) {
            this.method = this._first.substring(0, n);
            this.url = this._first.substring(n + 1, n2);
            this.tunnelMode = this.method.equalsIgnoreCase("CONNECT");
            if (!this.tunnelMode) {
                n = this.url.indexOf("://");
                if (n != -1) {
                    String string2 = this.url.substring(0, n).toLowerCase();
                    this.protocoll = string2.equals("http") ? 1 : (string2.equals("https") ? 2 : 3);
                    this.url = this.url.substring(n + 3);
                    n = n2 = this.url.indexOf(47);
                    if (n2 == -1) {
                        n = this.url.length();
                    }
                    this.hostEntry = this.url.substring(0, n);
                    this.url = n == this.url.length() ? "/" : this.url.substring(n);
                }
            } else {
                this.hostEntry = this.url;
            }
            if (this.hostEntry != null) {
                this.parseHostEntry();
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad Request:");
        stringBuilder.append(this._first);
        throw new IOException(stringBuilder.toString());
    }

    public void appendValueToHeaderString(StringBuffer stringBuffer, String string2, String arrstring) {
        arrstring = arrstring.split("_,_");
        for (int i = 0; i < arrstring.length; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(": ");
            stringBuilder.append(arrstring[i]);
            stringBuilder.append("\r\n");
            stringBuffer.append(stringBuilder.toString());
        }
    }

    public boolean chunkedTransfer() {
        String string2 = this.getValue("Transfer-Encoding");
        if (string2 != null) {
            return string2.equalsIgnoreCase("chunked");
        }
        return false;
    }

    public HttpHeader clone() {
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.remote_host_name = this.remote_host_name;
        httpHeader.hostEntry = this.hostEntry;
        httpHeader.url = this.url;
        httpHeader.method = this.method;
        httpHeader.remote_port = this.remote_port;
        httpHeader.tunnelMode = this.tunnelMode;
        httpHeader.responsecode = this.responsecode;
        httpHeader._first = this._first;
        httpHeader._keys = (Vector)this._keys.clone();
        httpHeader._mapping = (HashMap)this._mapping.clone();
        httpHeader.type = this.type;
        return httpHeader;
    }

    public boolean getConnectionClose() {
        String string2 = this.getValue("Connection");
        if (string2 == null) {
            return false;
        }
        if (string2.equalsIgnoreCase("close")) {
            return true;
        }
        return false;
    }

    public long getContentLength() {
        if (this.responsecode != 304 && this.responsecode != 204) {
            String string2 = this.getValue("Content-Length");
            if (string2 != null) {
                return Long.parseLong(string2);
            }
            return -1L;
        }
        return 0L;
    }

    public String getHeaderString() {
        AbstractStringBuilder abstractStringBuilder = new StringBuilder();
        abstractStringBuilder.append(this._first);
        abstractStringBuilder.append("\r\n");
        abstractStringBuilder = new StringBuffer(abstractStringBuilder.toString());
        for (String string2 : this._keys) {
            this.appendValueToHeaderString((StringBuffer)abstractStringBuilder, string2, (String)this._mapping.get(string2.toUpperCase()));
        }
        abstractStringBuilder.append("\r\n");
        return abstractStringBuilder.toString();
    }

    public int getResponseCode() {
        if (this.type != 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this);
            stringBuilder.append(" is not a ResonseHeader!");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return this.responsecode;
    }

    public String getResponseMessage() {
        if (this.type != 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this);
            stringBuilder.append(" is not a ResonseHeader!");
            throw new IllegalStateException(stringBuilder.toString());
        }
        return this._first;
    }

    public String getServerRequestHeader() {
        return this.getServerRequestHeader(false);
    }

    public String getServerRequestHeader(boolean bl) {
        Object object;
        CharSequence charSequence = this._first;
        if (!this.tunnelMode) {
            if (bl) {
                charSequence = new StringBuilder();
                charSequence.append(this.method);
                charSequence.append(" http://");
                charSequence.append(this.hostEntry);
                charSequence.append(this.url);
                charSequence.append(" HTTP/1.1");
                charSequence = charSequence.toString();
            } else {
                charSequence = new StringBuilder();
                charSequence.append(this.method);
                charSequence.append(" ");
                charSequence.append(this.url);
                charSequence.append(" HTTP/1.1");
                charSequence = charSequence.toString();
            }
        }
        CharSequence charSequence2 = new StringBuilder();
        charSequence2.append((String)charSequence);
        charSequence2.append("\r\n");
        StringBuffer stringBuffer = new StringBuffer(charSequence2.toString());
        HashMap hashMap = (HashMap)this._mapping.clone();
        for (int i = 0; i < reqparamSequence.length; ++i) {
            charSequence2 = reqparamSequence[i];
            object = (String)hashMap.remove(charSequence2.toUpperCase());
            if (object == null) continue;
            charSequence = charSequence2;
            if (bl) {
                charSequence = charSequence2;
                if (charSequence2.toUpperCase().equals("CONNECTION")) {
                    charSequence = "Proxy-Connection";
                }
            }
            if (object.length() > 0) {
                this.appendValueToHeaderString(stringBuffer, (String)charSequence, (String)object);
                continue;
            }
            charSequence2 = new StringBuilder();
            charSequence2.append((String)charSequence);
            charSequence2.append(":\r\n");
            stringBuffer.append(charSequence2.toString());
        }
        object = this._keys.iterator();
        while (object.hasNext()) {
            charSequence2 = (String)object.next();
            String string2 = (String)hashMap.remove(charSequence2.toUpperCase());
            if (string2 == null) continue;
            charSequence = charSequence2;
            if (!bl) {
                charSequence = charSequence2;
                if (charSequence2.toUpperCase().equals("PROXY-CONNECTION")) {
                    charSequence = "Connection";
                }
            }
            if (string2.length() > 0) {
                this.appendValueToHeaderString(stringBuffer, (String)charSequence, string2);
                continue;
            }
            charSequence2 = new StringBuilder();
            charSequence2.append((String)charSequence);
            charSequence2.append(":\r\n");
            stringBuffer.append(charSequence2.toString());
        }
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    public String getValue(String string2) {
        return (String)this._mapping.get(string2.toUpperCase());
    }

    public String removeValue(String string2) {
        this._keys.remove(string2);
        return (String)this._mapping.remove(string2.toUpperCase());
    }

    public void setHostEntry(String string2) throws IOException {
        this.hostEntry = string2;
        this.setValue("Host", string2);
        this.parseHostEntry();
    }

    public void setRequest(String string2) throws IOException {
        this._first = string2;
        this.parseURI();
        this.setValue("Host", this.hostEntry);
    }

    public void setValue(String string2, String string3) {
        if (this.getValue(string2) == null) {
            this._keys.add(string2);
        }
        this._mapping.put(string2.toUpperCase(), string3);
    }
}

