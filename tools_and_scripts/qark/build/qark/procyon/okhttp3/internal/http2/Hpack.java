// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Buffer;
import java.util.Collection;
import java.util.Arrays;
import okio.Okio;
import java.util.ArrayList;
import okio.Source;
import okio.BufferedSource;
import java.util.List;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.io.IOException;
import okio.ByteString;
import java.util.Map;

final class Hpack
{
    static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;
    private static final int PREFIX_4_BITS = 15;
    private static final int PREFIX_5_BITS = 31;
    private static final int PREFIX_6_BITS = 63;
    private static final int PREFIX_7_BITS = 127;
    static final Header[] STATIC_HEADER_TABLE;
    
    static {
        STATIC_HEADER_TABLE = new Header[] { new Header(Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "") };
        NAME_TO_FIRST_INDEX = nameToFirstIndex();
    }
    
    private Hpack() {
    }
    
    static ByteString checkLowercase(final ByteString byteString) throws IOException {
        for (int i = 0; i < byteString.size(); ++i) {
            final byte byte1 = byteString.getByte(i);
            if (byte1 >= 65 && byte1 <= 90) {
                throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + byteString.utf8());
            }
        }
        return byteString;
    }
    
    private static Map<ByteString, Integer> nameToFirstIndex() {
        final LinkedHashMap<ByteString, Integer> linkedHashMap = new LinkedHashMap<ByteString, Integer>(Hpack.STATIC_HEADER_TABLE.length);
        for (int i = 0; i < Hpack.STATIC_HEADER_TABLE.length; ++i) {
            if (!linkedHashMap.containsKey(Hpack.STATIC_HEADER_TABLE[i].name)) {
                linkedHashMap.put(Hpack.STATIC_HEADER_TABLE[i].name, i);
            }
        }
        return Collections.unmodifiableMap((Map<? extends ByteString, ? extends Integer>)linkedHashMap);
    }
    
    static final class Reader
    {
        Header[] dynamicTable;
        int dynamicTableByteCount;
        int headerCount;
        private final List<Header> headerList;
        private final int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        int nextHeaderIndex;
        private final BufferedSource source;
        
        Reader(final int headerTableSizeSetting, final int maxDynamicTableByteCount, final Source source) {
            this.headerList = new ArrayList<Header>();
            this.dynamicTable = new Header[8];
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
            this.headerTableSizeSetting = headerTableSizeSetting;
            this.maxDynamicTableByteCount = maxDynamicTableByteCount;
            this.source = Okio.buffer(source);
        }
        
        Reader(final int n, final Source source) {
            this(n, n, source);
        }
        
        private void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
                if (this.maxDynamicTableByteCount != 0) {
                    this.evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
                    return;
                }
                this.clearDynamicTable();
            }
        }
        
        private void clearDynamicTable() {
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }
        
        private int dynamicTableIndex(final int n) {
            return this.nextHeaderIndex + 1 + n;
        }
        
        private int evictToRecoverBytes(int n) {
            int n2 = 0;
            final int n3 = 0;
            if (n > 0) {
                int n4;
                int n5;
                for (n4 = this.dynamicTable.length - 1, n5 = n, n = n3; n4 >= this.nextHeaderIndex && n5 > 0; n5 -= this.dynamicTable[n4].hpackSize, this.dynamicTableByteCount -= this.dynamicTable[n4].hpackSize, --this.headerCount, ++n, --n4) {}
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + n, this.headerCount);
                this.nextHeaderIndex += n;
                n2 = n;
            }
            return n2;
        }
        
        private ByteString getName(final int n) throws IOException {
            if (this.isStaticHeader(n)) {
                return Hpack.STATIC_HEADER_TABLE[n].name;
            }
            final int dynamicTableIndex = this.dynamicTableIndex(n - Hpack.STATIC_HEADER_TABLE.length);
            if (dynamicTableIndex < 0 || dynamicTableIndex >= this.dynamicTable.length) {
                throw new IOException("Header index too large " + (n + 1));
            }
            return this.dynamicTable[dynamicTableIndex].name;
        }
        
        private void insertIntoDynamicTable(int n, final Header header) {
            this.headerList.add(header);
            int hpackSize;
            final int n2 = hpackSize = header.hpackSize;
            if (n != -1) {
                hpackSize = n2 - this.dynamicTable[this.dynamicTableIndex(n)].hpackSize;
            }
            if (hpackSize > this.maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            final int evictToRecoverBytes = this.evictToRecoverBytes(this.dynamicTableByteCount + hpackSize - this.maxDynamicTableByteCount);
            if (n == -1) {
                if (this.headerCount + 1 > this.dynamicTable.length) {
                    final Header[] dynamicTable = new Header[this.dynamicTable.length * 2];
                    System.arraycopy(this.dynamicTable, 0, dynamicTable, this.dynamicTable.length, this.dynamicTable.length);
                    this.nextHeaderIndex = this.dynamicTable.length - 1;
                    this.dynamicTable = dynamicTable;
                }
                n = this.nextHeaderIndex--;
                this.dynamicTable[n] = header;
                ++this.headerCount;
            }
            else {
                this.dynamicTable[n + (this.dynamicTableIndex(n) + evictToRecoverBytes)] = header;
            }
            this.dynamicTableByteCount += hpackSize;
        }
        
        private boolean isStaticHeader(final int n) {
            return n >= 0 && n <= Hpack.STATIC_HEADER_TABLE.length - 1;
        }
        
        private int readByte() throws IOException {
            return this.source.readByte() & 0xFF;
        }
        
        private void readIndexedHeader(final int n) throws IOException {
            if (this.isStaticHeader(n)) {
                this.headerList.add(Hpack.STATIC_HEADER_TABLE[n]);
                return;
            }
            final int dynamicTableIndex = this.dynamicTableIndex(n - Hpack.STATIC_HEADER_TABLE.length);
            if (dynamicTableIndex < 0 || dynamicTableIndex >= this.dynamicTable.length) {
                throw new IOException("Header index too large " + (n + 1));
            }
            this.headerList.add(this.dynamicTable[dynamicTableIndex]);
        }
        
        private void readLiteralHeaderWithIncrementalIndexingIndexedName(final int n) throws IOException {
            this.insertIntoDynamicTable(-1, new Header(this.getName(n), this.readByteString()));
        }
        
        private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
            this.insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
        }
        
        private void readLiteralHeaderWithoutIndexingIndexedName(final int n) throws IOException {
            this.headerList.add(new Header(this.getName(n), this.readByteString()));
        }
        
        private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
            this.headerList.add(new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
        }
        
        public List<Header> getAndResetHeaderList() {
            final ArrayList<Header> list = new ArrayList<Header>(this.headerList);
            this.headerList.clear();
            return list;
        }
        
        int maxDynamicTableByteCount() {
            return this.maxDynamicTableByteCount;
        }
        
        ByteString readByteString() throws IOException {
            final int byte1 = this.readByte();
            int n;
            if ((byte1 & 0x80) == 0x80) {
                n = 1;
            }
            else {
                n = 0;
            }
            final int int1 = this.readInt(byte1, 127);
            if (n != 0) {
                return ByteString.of(Huffman.get().decode(this.source.readByteArray(int1)));
            }
            return this.source.readByteString(int1);
        }
        
        void readHeaders() throws IOException {
            while (!this.source.exhausted()) {
                final int n = this.source.readByte() & 0xFF;
                if (n == 128) {
                    throw new IOException("index == 0");
                }
                if ((n & 0x80) == 0x80) {
                    this.readIndexedHeader(this.readInt(n, 127) - 1);
                }
                else if (n == 64) {
                    this.readLiteralHeaderWithIncrementalIndexingNewName();
                }
                else if ((n & 0x40) == 0x40) {
                    this.readLiteralHeaderWithIncrementalIndexingIndexedName(this.readInt(n, 63) - 1);
                }
                else if ((n & 0x20) == 0x20) {
                    this.maxDynamicTableByteCount = this.readInt(n, 31);
                    if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting) {
                        throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount);
                    }
                    this.adjustDynamicTableByteCount();
                }
                else if (n == 16 || n == 0) {
                    this.readLiteralHeaderWithoutIndexingNewName();
                }
                else {
                    this.readLiteralHeaderWithoutIndexingIndexedName(this.readInt(n, 15) - 1);
                }
            }
        }
        
        int readInt(int n, int n2) throws IOException {
            n &= n2;
            if (n < n2) {
                return n;
            }
            n = 0;
            int byte1;
            while (true) {
                byte1 = this.readByte();
                if ((byte1 & 0x80) == 0x0) {
                    break;
                }
                n2 += (byte1 & 0x7F) << n;
                n += 7;
            }
            return n2 + (byte1 << n);
        }
    }
    
    static final class Writer
    {
        private static final int SETTINGS_HEADER_TABLE_SIZE = 4096;
        private static final int SETTINGS_HEADER_TABLE_SIZE_LIMIT = 16384;
        Header[] dynamicTable;
        int dynamicTableByteCount;
        private boolean emitDynamicTableSizeUpdate;
        int headerCount;
        int headerTableSizeSetting;
        int maxDynamicTableByteCount;
        int nextHeaderIndex;
        private final Buffer out;
        private int smallestHeaderTableSizeSetting;
        private final boolean useCompression;
        
        Writer(final int n, final boolean useCompression, final Buffer out) {
            this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
            this.dynamicTable = new Header[8];
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
            this.headerTableSizeSetting = n;
            this.maxDynamicTableByteCount = n;
            this.useCompression = useCompression;
            this.out = out;
        }
        
        Writer(final Buffer buffer) {
            this(4096, true, buffer);
        }
        
        private void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
                if (this.maxDynamicTableByteCount != 0) {
                    this.evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
                    return;
                }
                this.clearDynamicTable();
            }
        }
        
        private void clearDynamicTable() {
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }
        
        private int evictToRecoverBytes(int n) {
            int n2 = 0;
            final int n3 = 0;
            if (n > 0) {
                int n4;
                int n5;
                for (n4 = this.dynamicTable.length - 1, n5 = n, n = n3; n4 >= this.nextHeaderIndex && n5 > 0; n5 -= this.dynamicTable[n4].hpackSize, this.dynamicTableByteCount -= this.dynamicTable[n4].hpackSize, --this.headerCount, ++n, --n4) {}
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + n, this.headerCount);
                Arrays.fill(this.dynamicTable, this.nextHeaderIndex + 1, this.nextHeaderIndex + 1 + n, null);
                this.nextHeaderIndex += n;
                n2 = n;
            }
            return n2;
        }
        
        private void insertIntoDynamicTable(final Header header) {
            final int hpackSize = header.hpackSize;
            if (hpackSize > this.maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            this.evictToRecoverBytes(this.dynamicTableByteCount + hpackSize - this.maxDynamicTableByteCount);
            if (this.headerCount + 1 > this.dynamicTable.length) {
                final Header[] dynamicTable = new Header[this.dynamicTable.length * 2];
                System.arraycopy(this.dynamicTable, 0, dynamicTable, this.dynamicTable.length, this.dynamicTable.length);
                this.nextHeaderIndex = this.dynamicTable.length - 1;
                this.dynamicTable = dynamicTable;
            }
            this.dynamicTable[this.nextHeaderIndex--] = header;
            ++this.headerCount;
            this.dynamicTableByteCount += hpackSize;
        }
        
        void setHeaderTableSizeSetting(int min) {
            this.headerTableSizeSetting = min;
            min = Math.min(min, 16384);
            if (this.maxDynamicTableByteCount == min) {
                return;
            }
            if (min < this.maxDynamicTableByteCount) {
                this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, min);
            }
            this.emitDynamicTableSizeUpdate = true;
            this.maxDynamicTableByteCount = min;
            this.adjustDynamicTableByteCount();
        }
        
        void writeByteString(ByteString byteString) throws IOException {
            if (this.useCompression && Huffman.get().encodedLength(byteString) < byteString.size()) {
                final Buffer buffer = new Buffer();
                Huffman.get().encode(byteString, buffer);
                byteString = buffer.readByteString();
                this.writeInt(byteString.size(), 127, 128);
                this.out.write(byteString);
                return;
            }
            this.writeInt(byteString.size(), 127, 0);
            this.out.write(byteString);
        }
        
        void writeHeaders(final List<Header> list) throws IOException {
            if (this.emitDynamicTableSizeUpdate) {
                if (this.smallestHeaderTableSizeSetting < this.maxDynamicTableByteCount) {
                    this.writeInt(this.smallestHeaderTableSizeSetting, 31, 32);
                }
                this.emitDynamicTableSizeUpdate = false;
                this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
                this.writeInt(this.maxDynamicTableByteCount, 31, 32);
            }
            for (int i = 0; i < list.size(); ++i) {
                final Header header = list.get(i);
                final ByteString asciiLowercase = header.name.toAsciiLowercase();
                final ByteString value = header.value;
                final int n = -1;
                int n2 = -1;
                final Integer n3 = Hpack.NAME_TO_FIRST_INDEX.get(asciiLowercase);
                int n4 = n;
                if (n3 != null) {
                    final int n5 = n3 + 1;
                    n4 = n;
                    if ((n2 = n5) > 1) {
                        n4 = n;
                        if ((n2 = n5) < 8) {
                            if (Util.equal(Hpack.STATIC_HEADER_TABLE[n5 - 1].value, value)) {
                                n4 = n5;
                                n2 = n5;
                            }
                            else {
                                n4 = n;
                                n2 = n5;
                                if (Util.equal(Hpack.STATIC_HEADER_TABLE[n5].value, value)) {
                                    n4 = n5 + 1;
                                    n2 = n5;
                                }
                            }
                        }
                    }
                }
                int n6 = n4;
                int n7 = n2;
                if (n4 == -1) {
                    int n8 = this.nextHeaderIndex + 1;
                    final int length = this.dynamicTable.length;
                    while (true) {
                        n6 = n4;
                        n7 = n2;
                        if (n8 >= length) {
                            break;
                        }
                        int n9 = n2;
                        if (Util.equal(this.dynamicTable[n8].name, asciiLowercase)) {
                            if (Util.equal(this.dynamicTable[n8].value, value)) {
                                n6 = n8 - this.nextHeaderIndex + Hpack.STATIC_HEADER_TABLE.length;
                                n7 = n2;
                                break;
                            }
                            if ((n9 = n2) == -1) {
                                n9 = n8 - this.nextHeaderIndex + Hpack.STATIC_HEADER_TABLE.length;
                            }
                        }
                        ++n8;
                        n2 = n9;
                    }
                }
                if (n6 != -1) {
                    this.writeInt(n6, 127, 128);
                }
                else if (n7 == -1) {
                    this.out.writeByte(64);
                    this.writeByteString(asciiLowercase);
                    this.writeByteString(value);
                    this.insertIntoDynamicTable(header);
                }
                else if (asciiLowercase.startsWith(Header.PSEUDO_PREFIX) && !Header.TARGET_AUTHORITY.equals(asciiLowercase)) {
                    this.writeInt(n7, 15, 0);
                    this.writeByteString(value);
                }
                else {
                    this.writeInt(n7, 63, 64);
                    this.writeByteString(value);
                    this.insertIntoDynamicTable(header);
                }
            }
        }
        
        void writeInt(int i, final int n, final int n2) {
            if (i < n) {
                this.out.writeByte(n2 | i);
                return;
            }
            this.out.writeByte(n2 | n);
            for (i -= n; i >= 128; i >>>= 7) {
                this.out.writeByte((i & 0x7F) | 0x80);
            }
            this.out.writeByte(i);
        }
    }
}
