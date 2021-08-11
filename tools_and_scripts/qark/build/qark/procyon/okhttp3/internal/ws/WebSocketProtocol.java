// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.ws;

import okio.Buffer;
import okio.ByteString;

public final class WebSocketProtocol
{
    static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    static final int B0_FLAG_FIN = 128;
    static final int B0_FLAG_RSV1 = 64;
    static final int B0_FLAG_RSV2 = 32;
    static final int B0_FLAG_RSV3 = 16;
    static final int B0_MASK_OPCODE = 15;
    static final int B1_FLAG_MASK = 128;
    static final int B1_MASK_LENGTH = 127;
    static final int CLOSE_CLIENT_GOING_AWAY = 1001;
    static final long CLOSE_MESSAGE_MAX = 123L;
    static final int CLOSE_NO_STATUS_CODE = 1005;
    static final int OPCODE_BINARY = 2;
    static final int OPCODE_CONTINUATION = 0;
    static final int OPCODE_CONTROL_CLOSE = 8;
    static final int OPCODE_CONTROL_PING = 9;
    static final int OPCODE_CONTROL_PONG = 10;
    static final int OPCODE_FLAG_CONTROL = 8;
    static final int OPCODE_TEXT = 1;
    static final long PAYLOAD_BYTE_MAX = 125L;
    static final int PAYLOAD_LONG = 127;
    static final int PAYLOAD_SHORT = 126;
    static final long PAYLOAD_SHORT_MAX = 65535L;
    
    private WebSocketProtocol() {
        throw new AssertionError((Object)"No instances.");
    }
    
    public static String acceptHeader(final String s) {
        return ByteString.encodeUtf8(s + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
    }
    
    static String closeCodeExceptionMessage(final int n) {
        if (n < 1000 || n >= 5000) {
            return "Code must be in range [1000,5000): " + n;
        }
        if ((n >= 1004 && n <= 1006) || (n >= 1012 && n <= 2999)) {
            return "Code " + n + " is reserved and may not be used.";
        }
        return null;
    }
    
    static void toggleMask(final Buffer.UnsafeCursor unsafeCursor, final byte[] array) {
        int n = 0;
        final int length = array.length;
        do {
            final byte[] data = unsafeCursor.data;
            int n2;
            for (int i = unsafeCursor.start; i < unsafeCursor.end; ++i, n = n2 + 1) {
                n2 = n % length;
                data[i] ^= array[n2];
            }
        } while (unsafeCursor.next() != -1);
    }
    
    static void validateCloseCode(final int n) {
        final String closeCodeExceptionMessage = closeCodeExceptionMessage(n);
        if (closeCodeExceptionMessage != null) {
            throw new IllegalArgumentException(closeCodeExceptionMessage);
        }
    }
}
