// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import java.io.IOException;
import java.util.zip.Deflater;

public final class DeflaterSink implements Sink
{
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;
    
    DeflaterSink(final BufferedSink sink, final Deflater deflater) {
        if (sink == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.sink = sink;
        this.deflater = deflater;
    }
    
    public DeflaterSink(final Sink sink, final Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }
    
    @IgnoreJRERequirement
    private void deflate(final boolean b) throws IOException {
        final Buffer buffer = this.sink.buffer();
        Segment writableSegment;
        while (true) {
            writableSegment = buffer.writableSegment(1);
            int n;
            if (b) {
                n = this.deflater.deflate(writableSegment.data, writableSegment.limit, 8192 - writableSegment.limit, 2);
            }
            else {
                n = this.deflater.deflate(writableSegment.data, writableSegment.limit, 8192 - writableSegment.limit);
            }
            if (n > 0) {
                writableSegment.limit += n;
                buffer.size += n;
                this.sink.emitCompleteSegments();
            }
            else {
                if (this.deflater.needsInput()) {
                    break;
                }
                continue;
            }
        }
        if (writableSegment.pos == writableSegment.limit) {
            buffer.head = writableSegment.pop();
            SegmentPool.recycle(writableSegment);
        }
    }
    
    @Override
    public void close() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okio/DeflaterSink.closed:Z
        //     4: ifeq            8
        //     7: return         
        //     8: aconst_null    
        //     9: astore_2       
        //    10: aload_0        
        //    11: invokevirtual   okio/DeflaterSink.finishDeflate:()V
        //    14: aload_0        
        //    15: getfield        okio/DeflaterSink.deflater:Ljava/util/zip/Deflater;
        //    18: invokevirtual   java/util/zip/Deflater.end:()V
        //    21: aload_2        
        //    22: astore_1       
        //    23: aload_0        
        //    24: getfield        okio/DeflaterSink.sink:Lokio/BufferedSink;
        //    27: invokeinterface okio/BufferedSink.close:()V
        //    32: aload_1        
        //    33: astore_2       
        //    34: aload_0        
        //    35: iconst_1       
        //    36: putfield        okio/DeflaterSink.closed:Z
        //    39: aload_2        
        //    40: ifnull          7
        //    43: aload_2        
        //    44: invokestatic    okio/Util.sneakyRethrow:(Ljava/lang/Throwable;)V
        //    47: return         
        //    48: astore_2       
        //    49: goto            14
        //    52: astore_3       
        //    53: aload_2        
        //    54: astore_1       
        //    55: aload_2        
        //    56: ifnonnull       23
        //    59: aload_3        
        //    60: astore_1       
        //    61: goto            23
        //    64: astore_3       
        //    65: aload_1        
        //    66: astore_2       
        //    67: aload_1        
        //    68: ifnonnull       34
        //    71: aload_3        
        //    72: astore_2       
        //    73: goto            34
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     14     48     52     Ljava/lang/Throwable;
        //  14     21     52     64     Ljava/lang/Throwable;
        //  23     32     64     76     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 44, Size: 44
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    void finishDeflate() throws IOException {
        this.deflater.finish();
        this.deflate(false);
    }
    
    @Override
    public void flush() throws IOException {
        this.deflate(true);
        this.sink.flush();
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }
    
    @Override
    public void write(final Buffer buffer, long n) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0L, n);
        while (n > 0L) {
            final Segment head = buffer.head;
            final int n2 = (int)Math.min(n, head.limit - head.pos);
            this.deflater.setInput(head.data, head.pos, n2);
            this.deflate(false);
            buffer.size -= n2;
            head.pos += n2;
            if (head.pos == head.limit) {
                buffer.head = head.pop();
                SegmentPool.recycle(head);
            }
            n -= n2;
        }
    }
}
