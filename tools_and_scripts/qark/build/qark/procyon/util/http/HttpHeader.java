// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util.http;

import java.util.Iterator;
import java.io.ByteArrayInputStream;
import util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

public class HttpHeader
{
    public static final int HTTP = 1;
    public static final int HTTPS = 2;
    public static final int OTHER = 3;
    public static final int REQUEST_HEADER = 1;
    public static final int RESPONSE_HEADER = 2;
    public static final int UNKNOWN = 4;
    private static final String[] reqparamSequence;
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
    
    static {
        reqparamSequence = new String[] { "Cache-Control", "Connection", "Date", "Pragma", "Trailer", "Transfer-Encoding", "Upgrade", "Via", "Warning", "Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language", "Authorization", "Expect", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range", "If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent", "Allow", "Content-Encoding", "Content-Language", "Content-Length", "Content-Location", "Content-MD5", "Content-Range", "Content-Type", "Expires", "Last-Modified", "extension-header" };
    }
    
    private HttpHeader() {
        this.remote_host_name = "";
        this.protocoll = 4;
        this.remote_port = 0;
        this.tunnelMode = false;
        this.responsecode = -1;
        this._keys = null;
        this._mapping = null;
    }
    
    public HttpHeader(final int type) {
        this.remote_host_name = "";
        this.protocoll = 4;
        this.remote_port = 0;
        this.tunnelMode = false;
        this.responsecode = -1;
        this._keys = null;
        this._mapping = null;
        this._keys = new Vector();
        this._mapping = new HashMap();
        this.type = type;
    }
    
    public HttpHeader(final InputStream inputStream, final int type) throws IOException {
        this.remote_host_name = "";
        this.protocoll = 4;
        this.remote_port = 0;
        this.tunnelMode = false;
        this.responsecode = -1;
        this._keys = null;
        this._mapping = null;
        this._keys = new Vector();
        this._mapping = new HashMap();
        if (type != 1 && type != 2) {
            throw new IOException("INVALID TYPE!");
        }
        this.type = type;
        this._first = Utils.readLineFromStream(inputStream, true);
        Label_0263: {
            if (type == 1) {
                this.parseURI();
                if (this.hostEntry != null) {
                    this.setValue("Host", this.hostEntry);
                    break Label_0263;
                }
                break Label_0263;
            }
            else {
                if (this._first.length() < 12) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid Response Header:");
                    sb.append(this._first);
                    throw new IOException(sb.toString());
                }
                if (!this._first.substring(0, 12).toLowerCase().startsWith("http/")) {
                    break Label_0263;
                }
            }
            try {
                this.responsecode = Integer.parseInt(this._first.substring(9, 12));
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("HTTP/1.1 ");
                sb2.append(this.responsecode);
                sb2.append(this._first.substring(12));
                this._first = sb2.toString();
                for (String s = Utils.readLineFromStream(inputStream, true); !s.equals(""); s = Utils.readLineFromStream(inputStream, true)) {
                    final int index = s.indexOf(": ");
                    String s2;
                    String s3;
                    if (index == -1) {
                        final int index2 = s.indexOf(":");
                        if (index2 == -1) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Invalid header:");
                            sb3.append(s);
                            throw new IOException(sb3.toString());
                        }
                        s2 = s.substring(0, index2).trim();
                        s3 = s.substring(index2 + 1).trim();
                    }
                    else {
                        s2 = s.substring(0, index).trim();
                        s3 = s.substring(index + 2).trim();
                    }
                    final String upperCase = s2.toUpperCase();
                    final String s4 = this._mapping.get(upperCase);
                    if (s4 == null) {
                        this._keys.add(s2);
                        this._mapping.put(upperCase, s3);
                    }
                    else if (!upperCase.equals("CONTENT-LENGTH")) {
                        if (!upperCase.equals("HOST")) {
                            final HashMap mapping = this._mapping;
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append(s4);
                            sb4.append("_,_");
                            sb4.append(s3);
                            mapping.put(upperCase, sb4.toString());
                        }
                    }
                    else if (!s4.equals(s3)) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("Invalid Header! Duplicated Content-Length with different values:");
                        sb5.append(s4);
                        sb5.append("<>");
                        sb5.append(s3);
                        sb5.append("!");
                        throw new IOException(sb5.toString());
                    }
                }
                if (this.hostEntry != null || type != 1) {
                    return;
                }
                this.hostEntry = this.getValue("Host");
                if (this.hostEntry != null) {
                    this.parseHostEntry();
                    return;
                }
                throw new IOException("Bad Request - No Host specified!");
            }
            catch (Exception ex) {
                throw new IOException(ex.getMessage());
            }
        }
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Invalid Response Header:");
        sb6.append(this._first);
        throw new IOException(sb6.toString());
    }
    
    public HttpHeader(final String s, final int n) throws IOException {
        this(new ByteArrayInputStream(s.getBytes()), n);
    }
    
    private void parseHostEntry() throws IOException {
        if (this.protocoll == 1) {
            this.remote_port = 80;
        }
        else if (this.protocoll == 2) {
            this.remote_port = 443;
        }
        else {
            this.remote_port = -1;
        }
        this.remote_host_name = this.hostEntry;
        final int lastIndex = this.hostEntry.lastIndexOf(":");
        if (lastIndex != -1 && !this.hostEntry.endsWith("]")) {
            try {
                this.remote_port = Integer.parseInt(this.hostEntry.substring(lastIndex + 1));
                this.remote_host_name = this.hostEntry.substring(0, lastIndex);
            }
            catch (NumberFormatException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Bad Request - Cannot parse port to int:");
                sb.append(this._first);
                throw new IOException(sb.toString());
            }
        }
        if (this.remote_host_name.startsWith("[") && this.remote_host_name.endsWith("]")) {
            this.remote_host_name = this.remote_host_name.substring(1, this.remote_host_name.length() - 1);
        }
    }
    
    private void parseURI() throws IOException {
        final int index = this._first.indexOf(32);
        final int lastIndex = this._first.lastIndexOf(32);
        if (index != -1 && index != lastIndex) {
            this.method = this._first.substring(0, index);
            this.url = this._first.substring(index + 1, lastIndex);
            if (!(this.tunnelMode = this.method.equalsIgnoreCase("CONNECT"))) {
                final int index2 = this.url.indexOf("://");
                if (index2 != -1) {
                    final String lowerCase = this.url.substring(0, index2).toLowerCase();
                    if (lowerCase.equals("http")) {
                        this.protocoll = 1;
                    }
                    else if (lowerCase.equals("https")) {
                        this.protocoll = 2;
                    }
                    else {
                        this.protocoll = 3;
                    }
                    this.url = this.url.substring(index2 + 3);
                    int n;
                    if ((n = this.url.indexOf(47)) == -1) {
                        n = this.url.length();
                    }
                    this.hostEntry = this.url.substring(0, n);
                    if (n == this.url.length()) {
                        this.url = "/";
                    }
                    else {
                        this.url = this.url.substring(n);
                    }
                }
            }
            else {
                this.hostEntry = this.url;
            }
            if (this.hostEntry != null) {
                this.parseHostEntry();
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad Request:");
        sb.append(this._first);
        throw new IOException(sb.toString());
    }
    
    public void appendValueToHeaderString(final StringBuffer sb, final String s, final String s2) {
        final String[] split = s2.split("_,_");
        for (int i = 0; i < split.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(": ");
            sb2.append(split[i]);
            sb2.append("\r\n");
            sb.append(sb2.toString());
        }
    }
    
    public boolean chunkedTransfer() {
        final String value = this.getValue("Transfer-Encoding");
        return value != null && value.equalsIgnoreCase("chunked");
    }
    
    public HttpHeader clone() {
        final HttpHeader httpHeader = new HttpHeader();
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
        final String value = this.getValue("Connection");
        return value != null && value.equalsIgnoreCase("close");
    }
    
    public long getContentLength() {
        if (this.responsecode == 304 || this.responsecode == 204) {
            return 0L;
        }
        final String value = this.getValue("Content-Length");
        if (value != null) {
            return Long.parseLong(value);
        }
        return -1L;
    }
    
    public String getHeaderString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._first);
        sb.append("\r\n");
        final StringBuffer sb2 = new StringBuffer(sb.toString());
        for (final String s : this._keys) {
            this.appendValueToHeaderString(sb2, s, (String)this._mapping.get(s.toUpperCase()));
        }
        sb2.append("\r\n");
        return sb2.toString();
    }
    
    public int getResponseCode() {
        if (this.type != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this);
            sb.append(" is not a ResonseHeader!");
            throw new IllegalStateException(sb.toString());
        }
        return this.responsecode;
    }
    
    public String getResponseMessage() {
        if (this.type != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this);
            sb.append(" is not a ResonseHeader!");
            throw new IllegalStateException(sb.toString());
        }
        return this._first;
    }
    
    public String getServerRequestHeader() {
        return this.getServerRequestHeader(false);
    }
    
    public String getServerRequestHeader(final boolean b) {
        String s = this._first;
        if (!this.tunnelMode) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.method);
                sb.append(" http://");
                sb.append(this.hostEntry);
                sb.append(this.url);
                sb.append(" HTTP/1.1");
                s = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.method);
                sb2.append(" ");
                sb2.append(this.url);
                sb2.append(" HTTP/1.1");
                s = sb2.toString();
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("\r\n");
        final StringBuffer sb4 = new StringBuffer(sb3.toString());
        final HashMap hashMap = (HashMap)this._mapping.clone();
        for (int i = 0; i < HttpHeader.reqparamSequence.length; ++i) {
            final String s2 = HttpHeader.reqparamSequence[i];
            final String s3 = hashMap.remove(s2.toUpperCase());
            if (s3 != null) {
                String s4 = s2;
                if (b) {
                    s4 = s2;
                    if (s2.toUpperCase().equals("CONNECTION")) {
                        s4 = "Proxy-Connection";
                    }
                }
                if (s3.length() > 0) {
                    this.appendValueToHeaderString(sb4, s4, s3);
                }
                else {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(s4);
                    sb5.append(":\r\n");
                    sb4.append(sb5.toString());
                }
            }
        }
        for (final String s5 : this._keys) {
            final String s6 = hashMap.remove(s5.toUpperCase());
            if (s6 != null) {
                String s7 = s5;
                if (!b) {
                    s7 = s5;
                    if (s5.toUpperCase().equals("PROXY-CONNECTION")) {
                        s7 = "Connection";
                    }
                }
                if (s6.length() > 0) {
                    this.appendValueToHeaderString(sb4, s7, s6);
                }
                else {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(s7);
                    sb6.append(":\r\n");
                    sb4.append(sb6.toString());
                }
            }
        }
        sb4.append("\r\n");
        return sb4.toString();
    }
    
    public String getValue(final String s) {
        return this._mapping.get(s.toUpperCase());
    }
    
    public String removeValue(final String s) {
        this._keys.remove(s);
        return this._mapping.remove(s.toUpperCase());
    }
    
    public void setHostEntry(final String hostEntry) throws IOException {
        this.setValue("Host", this.hostEntry = hostEntry);
        this.parseHostEntry();
    }
    
    public void setRequest(final String first) throws IOException {
        this._first = first;
        this.parseURI();
        this.setValue("Host", this.hostEntry);
    }
    
    public void setValue(final String s, final String s2) {
        if (this.getValue(s) == null) {
            this._keys.add(s);
        }
        this._mapping.put(s.toUpperCase(), s2);
    }
}
