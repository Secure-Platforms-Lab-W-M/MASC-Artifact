// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import java.io.IOException;
import java.util.jar.Manifest;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.Collection;
import java.util.regex.Pattern;

public class SignatureRemover
{
    private static final String DIGEST_SUFFIX = "-Digest";
    private static final String MANIFEST_MF = "META-INF/MANIFEST.MF";
    private static final Pattern SIGNATURE_FILES;
    private boolean active;
    
    static {
        SIGNATURE_FILES = Pattern.compile("META-INF/[^/]*\\.SF|META-INF/[^/]*\\.DSA|META-INF/[^/]*\\.RSA|META-INF/SIG-[^/]*");
    }
    
    public SignatureRemover() {
        this.active = true;
    }
    
    private void filterManifestEntry(final Collection<Attributes> collection) {
        final Iterator<Attributes> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final Attributes attributes = iterator.next();
            this.filterManifestEntryAttributes(attributes);
            if (attributes.isEmpty()) {
                iterator.remove();
            }
        }
    }
    
    private void filterManifestEntryAttributes(final Attributes attributes) {
        final Iterator<Object> iterator = attributes.keySet().iterator();
        while (iterator.hasNext()) {
            if (String.valueOf(iterator.next()).endsWith("-Digest")) {
                iterator.remove();
            }
        }
    }
    
    public boolean filterEntry(final String s, final InputStream inputStream, final OutputStream outputStream) throws IOException {
        if (this.active && "META-INF/MANIFEST.MF".equals(s)) {
            final Manifest manifest = new Manifest(inputStream);
            this.filterManifestEntry(manifest.getEntries().values());
            manifest.write(outputStream);
            return true;
        }
        return false;
    }
    
    public boolean removeEntry(final String s) {
        return this.active && SignatureRemover.SIGNATURE_FILES.matcher(s).matches();
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
}
