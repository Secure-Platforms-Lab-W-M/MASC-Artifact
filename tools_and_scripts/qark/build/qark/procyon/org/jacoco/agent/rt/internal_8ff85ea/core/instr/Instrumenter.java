// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.instr;

import org.jacoco.agent.rt.internal_8ff85ea.core.internal.ContentTypeDetector;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesAdapter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr.ClassInstrumenter;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr.ProbeArrayStrategyFactory;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassWriter;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassReader;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.Java9Support;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.Pack200Streams;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr.SignatureRemover;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;

public class Instrumenter
{
    private final IExecutionDataAccessorGenerator accessorGenerator;
    private final SignatureRemover signatureRemover;
    
    public Instrumenter(final IExecutionDataAccessorGenerator accessorGenerator) {
        this.accessorGenerator = accessorGenerator;
        this.signatureRemover = new SignatureRemover();
    }
    
    private void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    private IOException instrumentError(final String s, final RuntimeException ex) {
        final IOException ex2 = new IOException(String.format("Error while instrumenting class %s.", s));
        ex2.initCause(ex);
        return ex2;
    }
    
    private int instrumentGzip(final InputStream inputStream, final OutputStream outputStream, final String s) throws IOException {
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        final int instrumentAll = this.instrumentAll(new GZIPInputStream(inputStream), gzipOutputStream, s);
        gzipOutputStream.finish();
        return instrumentAll;
    }
    
    private int instrumentPack200(final InputStream inputStream, final OutputStream outputStream, final String s) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final int instrumentAll = this.instrumentAll(Pack200Streams.unpack(inputStream), byteArrayOutputStream, s);
        Pack200Streams.pack(byteArrayOutputStream.toByteArray(), outputStream);
        return instrumentAll;
    }
    
    private int instrumentZip(final InputStream inputStream, final OutputStream outputStream, final String s) throws IOException {
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        int n = 0;
        while (true) {
            final ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                break;
            }
            final String name = nextEntry.getName();
            if (this.signatureRemover.removeEntry(name)) {
                continue;
            }
            zipOutputStream.putNextEntry(new ZipEntry(name));
            int n2 = n;
            if (!this.signatureRemover.filterEntry(name, zipInputStream, zipOutputStream)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("@");
                sb.append(name);
                n2 = n + this.instrumentAll(zipInputStream, zipOutputStream, sb.toString());
            }
            zipOutputStream.closeEntry();
            n = n2;
        }
        zipOutputStream.finish();
        return n;
    }
    
    public void instrument(final InputStream inputStream, final OutputStream outputStream, final String s) throws IOException {
        try {
            outputStream.write(this.instrument(Java9Support.readFully(inputStream), s));
        }
        catch (RuntimeException ex) {
            throw this.instrumentError(s, ex);
        }
    }
    
    public byte[] instrument(final InputStream inputStream, final String s) throws IOException {
        try {
            return this.instrument(Java9Support.readFully(inputStream), s);
        }
        catch (RuntimeException ex) {
            throw this.instrumentError(s, ex);
        }
    }
    
    public byte[] instrument(final ClassReader classReader) {
        final ClassWriter classWriter = new ClassWriter(classReader, 0) {
            @Override
            protected String getCommonSuperClass(final String s, final String s2) {
                throw new IllegalStateException();
            }
        };
        classReader.accept(new ClassProbesAdapter(new ClassInstrumenter(ProbeArrayStrategyFactory.createFor(classReader, this.accessorGenerator), classWriter), true), 8);
        return classWriter.toByteArray();
    }
    
    public byte[] instrument(byte[] array, final String s) throws IOException {
        try {
            if (Java9Support.isPatchRequired(array)) {
                array = this.instrument(new ClassReader(Java9Support.downgrade(array)));
                Java9Support.upgrade(array);
                return array;
            }
            array = this.instrument(new ClassReader(array));
            return array;
        }
        catch (RuntimeException ex) {
            throw this.instrumentError(s, ex);
        }
    }
    
    public int instrumentAll(final InputStream inputStream, final OutputStream outputStream, final String s) throws IOException {
        final ContentTypeDetector contentTypeDetector = new ContentTypeDetector(inputStream);
        final int type = contentTypeDetector.getType();
        if (type == -889275714) {
            this.instrument(contentTypeDetector.getInputStream(), outputStream, s);
            return 1;
        }
        if (type == -889270259) {
            return this.instrumentPack200(contentTypeDetector.getInputStream(), outputStream, s);
        }
        if (type == 529203200) {
            return this.instrumentGzip(contentTypeDetector.getInputStream(), outputStream, s);
        }
        if (type != 1347093252) {
            this.copy(contentTypeDetector.getInputStream(), outputStream);
            return 0;
        }
        return this.instrumentZip(contentTypeDetector.getInputStream(), outputStream, s);
    }
    
    public void setRemoveSignatures(final boolean active) {
        this.signatureRemover.setActive(active);
    }
}
