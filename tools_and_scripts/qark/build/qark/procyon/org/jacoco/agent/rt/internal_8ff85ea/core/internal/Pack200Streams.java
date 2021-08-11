// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal;

import java.io.FilterInputStream;
import java.util.jar.JarOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;
import java.io.ByteArrayInputStream;
import java.util.jar.Pack200;
import java.io.OutputStream;

public final class Pack200Streams
{
    private Pack200Streams() {
    }
    
    public static void pack(final byte[] array, final OutputStream outputStream) throws IOException {
        Pack200.newPacker().pack(new JarInputStream(new ByteArrayInputStream(array)), outputStream);
    }
    
    public static InputStream unpack(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);
        Pack200.newUnpacker().unpack(new NoCloseInput(inputStream), jarOutputStream);
        jarOutputStream.finish();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
    
    private static class NoCloseInput extends FilterInputStream
    {
        protected NoCloseInput(final InputStream inputStream) {
            super(inputStream);
        }
        
        @Override
        public void close() throws IOException {
        }
    }
}
