// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.cache;

import okio.Buffer;
import java.io.IOException;
import okio.Sink;
import okio.ForwardingSink;

class FaultHidingSink extends ForwardingSink
{
    private boolean hasErrors;
    
    FaultHidingSink(final Sink sink) {
        super(sink);
    }
    
    @Override
    public void close() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.close();
        }
        catch (IOException ex) {
            this.hasErrors = true;
            this.onException(ex);
        }
    }
    
    @Override
    public void flush() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.flush();
        }
        catch (IOException ex) {
            this.hasErrors = true;
            this.onException(ex);
        }
    }
    
    protected void onException(final IOException ex) {
    }
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        if (this.hasErrors) {
            buffer.skip(n);
            return;
        }
        try {
            super.write(buffer, n);
        }
        catch (IOException ex) {
            this.hasErrors = true;
            this.onException(ex);
        }
    }
}
