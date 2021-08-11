// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.nio.charset.Charset;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.io.IOException;

final class RealBufferedSink implements BufferedSink
{
    public final Buffer buffer;
    boolean closed;
    public final Sink sink;
    
    RealBufferedSink(final Sink sink) {
        this.buffer = new Buffer();
        if (sink == null) {
            throw new NullPointerException("sink == null");
        }
        this.sink = sink;
    }
    
    @Override
    public Buffer buffer() {
        return this.buffer;
    }
    
    @Override
    public void close() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okio/RealBufferedSink.closed:Z
        //     4: ifeq            8
        //     7: return         
        //     8: aconst_null    
        //     9: astore_2       
        //    10: aload_2        
        //    11: astore_1       
        //    12: aload_0        
        //    13: getfield        okio/RealBufferedSink.buffer:Lokio/Buffer;
        //    16: getfield        okio/Buffer.size:J
        //    19: lconst_0       
        //    20: lcmp           
        //    21: ifle            46
        //    24: aload_0        
        //    25: getfield        okio/RealBufferedSink.sink:Lokio/Sink;
        //    28: aload_0        
        //    29: getfield        okio/RealBufferedSink.buffer:Lokio/Buffer;
        //    32: aload_0        
        //    33: getfield        okio/RealBufferedSink.buffer:Lokio/Buffer;
        //    36: getfield        okio/Buffer.size:J
        //    39: invokeinterface okio/Sink.write:(Lokio/Buffer;J)V
        //    44: aload_2        
        //    45: astore_1       
        //    46: aload_0        
        //    47: getfield        okio/RealBufferedSink.sink:Lokio/Sink;
        //    50: invokeinterface okio/Sink.close:()V
        //    55: aload_1        
        //    56: astore_2       
        //    57: aload_0        
        //    58: iconst_1       
        //    59: putfield        okio/RealBufferedSink.closed:Z
        //    62: aload_2        
        //    63: ifnull          7
        //    66: aload_2        
        //    67: invokestatic    okio/Util.sneakyRethrow:(Ljava/lang/Throwable;)V
        //    70: return         
        //    71: astore_1       
        //    72: goto            46
        //    75: astore_3       
        //    76: aload_1        
        //    77: astore_2       
        //    78: aload_1        
        //    79: ifnonnull       57
        //    82: aload_3        
        //    83: astore_2       
        //    84: goto            57
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     44     71     75     Ljava/lang/Throwable;
        //  46     55     75     87     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0046:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public BufferedSink emit() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        final long size = this.buffer.size();
        if (size > 0L) {
            this.sink.write(this.buffer, size);
        }
        return this;
    }
    
    @Override
    public BufferedSink emitCompleteSegments() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        final long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
        if (completeSegmentByteCount > 0L) {
            this.sink.write(this.buffer, completeSegmentByteCount);
        }
        return this;
    }
    
    @Override
    public void flush() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size > 0L) {
            this.sink.write(this.buffer, this.buffer.size);
        }
        this.sink.flush();
    }
    
    @Override
    public boolean isOpen() {
        return !this.closed;
    }
    
    @Override
    public OutputStream outputStream() {
        return new OutputStream() {
            @Override
            public void close() throws IOException {
                RealBufferedSink.this.close();
            }
            
            @Override
            public void flush() throws IOException {
                if (!RealBufferedSink.this.closed) {
                    RealBufferedSink.this.flush();
                }
            }
            
            @Override
            public String toString() {
                return RealBufferedSink.this + ".outputStream()";
            }
            
            @Override
            public void write(final int n) throws IOException {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.writeByte((int)(byte)n);
                RealBufferedSink.this.emitCompleteSegments();
            }
            
            @Override
            public void write(final byte[] array, final int n, final int n2) throws IOException {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.write(array, n, n2);
                RealBufferedSink.this.emitCompleteSegments();
            }
        };
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public String toString() {
        return "buffer(" + this.sink + ")";
    }
    
    @Override
    public int write(final ByteBuffer byteBuffer) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        final int write = this.buffer.write(byteBuffer);
        this.emitCompleteSegments();
        return write;
    }
    
    @Override
    public BufferedSink write(final ByteString byteString) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(byteString);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink write(final Source source, long n) throws IOException {
        while (n > 0L) {
            final long read = source.read(this.buffer, n);
            if (read == -1L) {
                throw new EOFException();
            }
            n -= read;
            this.emitCompleteSegments();
        }
        return this;
    }
    
    @Override
    public BufferedSink write(final byte[] array) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(array);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink write(final byte[] array, final int n, final int n2) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(array, n, n2);
        return this.emitCompleteSegments();
    }
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(buffer, n);
        this.emitCompleteSegments();
    }
    
    @Override
    public long writeAll(final Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long n = 0L;
        while (true) {
            final long read = source.read(this.buffer, 8192L);
            if (read == -1L) {
                break;
            }
            n += read;
            this.emitCompleteSegments();
        }
        return n;
    }
    
    @Override
    public BufferedSink writeByte(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeByte(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeDecimalLong(final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeDecimalLong(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeHexadecimalUnsignedLong(final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeHexadecimalUnsignedLong(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeInt(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeInt(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeIntLe(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeIntLe(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeLong(final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLong(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeLongLe(final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLongLe(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeShort(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShort(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeShortLe(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShortLe(n);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeString(final String s, final int n, final int n2, final Charset charset) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(s, n, n2, charset);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeString(final String s, final Charset charset) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(s, charset);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeUtf8(final String s) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(s);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeUtf8(final String s, final int n, final int n2) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(s, n, n2);
        return this.emitCompleteSegments();
    }
    
    @Override
    public BufferedSink writeUtf8CodePoint(final int n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8CodePoint(n);
        return this.emitCompleteSegments();
    }
}
