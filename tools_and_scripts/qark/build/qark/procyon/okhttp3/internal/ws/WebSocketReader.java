// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.ws;

import okio.ByteString;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.ProtocolException;
import okio.BufferedSource;
import okio.Buffer;

final class WebSocketReader
{
    boolean closed;
    private final Buffer controlFrameBuffer;
    final FrameCallback frameCallback;
    long frameLength;
    final boolean isClient;
    boolean isControlFrame;
    boolean isFinalFrame;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    private final Buffer messageFrameBuffer;
    int opcode;
    final BufferedSource source;
    
    WebSocketReader(final boolean isClient, final BufferedSource source, final FrameCallback frameCallback) {
        final Buffer.UnsafeCursor unsafeCursor = null;
        this.controlFrameBuffer = new Buffer();
        this.messageFrameBuffer = new Buffer();
        if (source == null) {
            throw new NullPointerException("source == null");
        }
        if (frameCallback == null) {
            throw new NullPointerException("frameCallback == null");
        }
        this.isClient = isClient;
        this.source = source;
        this.frameCallback = frameCallback;
        byte[] maskKey;
        if (isClient) {
            maskKey = null;
        }
        else {
            maskKey = new byte[4];
        }
        this.maskKey = maskKey;
        Buffer.UnsafeCursor maskCursor;
        if (isClient) {
            maskCursor = unsafeCursor;
        }
        else {
            maskCursor = new Buffer.UnsafeCursor();
        }
        this.maskCursor = maskCursor;
    }
    
    private void readControlFrame() throws IOException {
        if (this.frameLength > 0L) {
            this.source.readFully(this.controlFrameBuffer, this.frameLength);
            if (!this.isClient) {
                this.controlFrameBuffer.readAndWriteUnsafe(this.maskCursor);
                this.maskCursor.seek(0L);
                WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        }
        switch (this.opcode) {
            default: {
                throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.opcode));
            }
            case 9: {
                this.frameCallback.onReadPing(this.controlFrameBuffer.readByteString());
            }
            case 10: {
                this.frameCallback.onReadPong(this.controlFrameBuffer.readByteString());
            }
            case 8: {
                int short1 = 1005;
                String utf8 = "";
                final long size = this.controlFrameBuffer.size();
                if (size == 1L) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (size != 0L) {
                    short1 = this.controlFrameBuffer.readShort();
                    utf8 = this.controlFrameBuffer.readUtf8();
                    final String closeCodeExceptionMessage = WebSocketProtocol.closeCodeExceptionMessage(short1);
                    if (closeCodeExceptionMessage != null) {
                        throw new ProtocolException(closeCodeExceptionMessage);
                    }
                }
                this.frameCallback.onReadClose(short1, utf8);
                this.closed = true;
            }
        }
    }
    
    private void readHeader() throws IOException {
        if (this.closed) {
            throw new IOException("closed");
        }
        int n = 0;
    Label_0113_Outer:
        while (true) {
            final long timeoutNanos = this.source.timeout().timeoutNanos();
            this.source.timeout().clearTimeout();
            while (true) {
            Label_0172:
                while (true) {
                    try {
                        n = (this.source.readByte() & 0xFF);
                        this.source.timeout().timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                        this.opcode = (n & 0xF);
                        if ((n & 0x80) != 0x0) {
                            final boolean isFinalFrame = true;
                            this.isFinalFrame = isFinalFrame;
                            if ((n & 0x8) == 0x0) {
                                break Label_0172;
                            }
                            final boolean isControlFrame = true;
                            this.isControlFrame = isControlFrame;
                            if (this.isControlFrame && !this.isFinalFrame) {
                                throw new ProtocolException("Control frames must be final.");
                            }
                            break;
                        }
                    }
                    finally {
                        this.source.timeout().timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    }
                    final boolean isFinalFrame = false;
                    continue Label_0113_Outer;
                }
                final boolean isControlFrame = false;
                continue;
            }
        }
        boolean b;
        if ((n & 0x40) != 0x0) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if ((n & 0x20) != 0x0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        boolean b3;
        if ((n & 0x10) != 0x0) {
            b3 = true;
        }
        else {
            b3 = false;
        }
        if (b || b2 || b3) {
            throw new ProtocolException("Reserved flags are unsupported.");
        }
        final int n2 = this.source.readByte() & 0xFF;
        final boolean b4 = (n2 & 0x80) != 0x0;
        if (b4 == this.isClient) {
            String s;
            if (this.isClient) {
                s = "Server-sent frames must not be masked.";
            }
            else {
                s = "Client-sent frames must be masked.";
            }
            throw new ProtocolException(s);
        }
        this.frameLength = (n2 & 0x7F);
        if (this.frameLength == 126L) {
            this.frameLength = ((long)this.source.readShort() & 0xFFFFL);
        }
        else if (this.frameLength == 127L) {
            this.frameLength = this.source.readLong();
            if (this.frameLength < 0L) {
                throw new ProtocolException("Frame length 0x" + Long.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
            }
        }
        if (this.isControlFrame && this.frameLength > 125L) {
            throw new ProtocolException("Control frame must be less than 125B.");
        }
        if (b4) {
            this.source.readFully(this.maskKey);
        }
    }
    
    private void readMessage() throws IOException {
        while (!this.closed) {
            if (this.frameLength > 0L) {
                this.source.readFully(this.messageFrameBuffer, this.frameLength);
                if (!this.isClient) {
                    this.messageFrameBuffer.readAndWriteUnsafe(this.maskCursor);
                    this.maskCursor.seek(this.messageFrameBuffer.size() - this.frameLength);
                    WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                    this.maskCursor.close();
                }
            }
            if (this.isFinalFrame) {
                return;
            }
            this.readUntilNonControlFrame();
            if (this.opcode != 0) {
                throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(this.opcode));
            }
        }
        throw new IOException("closed");
    }
    
    private void readMessageFrame() throws IOException {
        final int opcode = this.opcode;
        if (opcode != 1 && opcode != 2) {
            throw new ProtocolException("Unknown opcode: " + Integer.toHexString(opcode));
        }
        this.readMessage();
        if (opcode == 1) {
            this.frameCallback.onReadMessage(this.messageFrameBuffer.readUtf8());
            return;
        }
        this.frameCallback.onReadMessage(this.messageFrameBuffer.readByteString());
    }
    
    private void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            this.readHeader();
            if (!this.isControlFrame) {
                break;
            }
            this.readControlFrame();
        }
    }
    
    void processNextFrame() throws IOException {
        this.readHeader();
        if (this.isControlFrame) {
            this.readControlFrame();
            return;
        }
        this.readMessageFrame();
    }
    
    public interface FrameCallback
    {
        void onReadClose(final int p0, final String p1);
        
        void onReadMessage(final String p0) throws IOException;
        
        void onReadMessage(final ByteString p0) throws IOException;
        
        void onReadPing(final ByteString p0);
        
        void onReadPong(final ByteString p0);
    }
}
