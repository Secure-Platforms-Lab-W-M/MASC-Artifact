// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.CRC32;

public final class GzipSink implements Sink
{
    private boolean closed;
    private final CRC32 crc;
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private final BufferedSink sink;
    
    public GzipSink(final Sink sink) {
        this.crc = new CRC32();
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.deflater = new Deflater(-1, true);
        this.sink = Okio.buffer(sink);
        this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
        this.writeHeader();
    }
    
    private void updateCrc(final Buffer buffer, long n) {
        int n2;
        for (Segment segment = buffer.head; n > 0L; n -= n2, segment = segment.next) {
            n2 = (int)Math.min(n, segment.limit - segment.pos);
            this.crc.update(segment.data, segment.pos, n2);
        }
    }
    
    private void writeFooter() throws IOException {
        this.sink.writeIntLe((int)this.crc.getValue());
        this.sink.writeIntLe((int)this.deflater.getBytesRead());
    }
    
    private void writeHeader() {
        final Buffer buffer = this.sink.buffer();
        buffer.writeShort(8075);
        buffer.writeByte(8);
        buffer.writeByte(0);
        buffer.writeInt(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
    }
    
    @Override
    public void close() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okio/GzipSink.closed:Z
        //     4: ifeq            8
        //     7: return         
        //     8: aconst_null    
        //     9: astore_2       
        //    10: aload_0        
        //    11: getfield        okio/GzipSink.deflaterSink:Lokio/DeflaterSink;
        //    14: invokevirtual   okio/DeflaterSink.finishDeflate:()V
        //    17: aload_0        
        //    18: invokespecial   okio/GzipSink.writeFooter:()V
        //    21: aload_0        
        //    22: getfield        okio/GzipSink.deflater:Ljava/util/zip/Deflater;
        //    25: invokevirtual   java/util/zip/Deflater.end:()V
        //    28: aload_2        
        //    29: astore_1       
        //    30: aload_0        
        //    31: getfield        okio/GzipSink.sink:Lokio/BufferedSink;
        //    34: invokeinterface okio/BufferedSink.close:()V
        //    39: aload_1        
        //    40: astore_2       
        //    41: aload_0        
        //    42: iconst_1       
        //    43: putfield        okio/GzipSink.closed:Z
        //    46: aload_2        
        //    47: ifnull          7
        //    50: aload_2        
        //    51: invokestatic    okio/Util.sneakyRethrow:(Ljava/lang/Throwable;)V
        //    54: return         
        //    55: astore_2       
        //    56: goto            21
        //    59: astore_3       
        //    60: aload_2        
        //    61: astore_1       
        //    62: aload_2        
        //    63: ifnonnull       30
        //    66: aload_3        
        //    67: astore_1       
        //    68: goto            30
        //    71: astore_3       
        //    72: aload_1        
        //    73: astore_2       
        //    74: aload_1        
        //    75: ifnonnull       41
        //    78: aload_3        
        //    79: astore_2       
        //    80: goto            41
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     21     55     59     Ljava/lang/Throwable;
        //  21     28     59     71     Ljava/lang/Throwable;
        //  30     39     71     83     Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 47, Size: 47
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
    
    public Deflater deflater() {
        return this.deflater;
    }
    
    @Override
    public void flush() throws IOException {
        this.deflaterSink.flush();
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + n);
        }
        if (n == 0L) {
            return;
        }
        this.updateCrc(buffer, n);
        this.deflaterSink.write(buffer, n);
    }
}
