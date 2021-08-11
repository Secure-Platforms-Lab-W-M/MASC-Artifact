// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okio.Okio;
import okio.Buffer;
import okio.Source;
import okio.ForwardingSource;
import okio.BufferedSource;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.Response;
import java.io.IOException;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.Nullable;

final class OkHttpCall<T> implements Call<T>
{
    @Nullable
    private final Object[] args;
    private volatile boolean canceled;
    @Nullable
    @GuardedBy("this")
    private Throwable creationFailure;
    @GuardedBy("this")
    private boolean executed;
    @Nullable
    @GuardedBy("this")
    private okhttp3.Call rawCall;
    private final ServiceMethod<T, ?> serviceMethod;
    
    OkHttpCall(final ServiceMethod<T, ?> serviceMethod, @Nullable final Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }
    
    private okhttp3.Call createRawCall() throws IOException {
        final okhttp3.Call call = this.serviceMethod.toCall(this.args);
        if (call == null) {
            throw new NullPointerException("Call.Factory returned null.");
        }
        return call;
    }
    
    @Override
    public void cancel() {
        this.canceled = true;
        synchronized (this) {
            final okhttp3.Call rawCall = this.rawCall;
            // monitorexit(this)
            if (rawCall != null) {
                rawCall.cancel();
            }
        }
    }
    
    @Override
    public OkHttpCall<T> clone() {
        return new OkHttpCall<T>(this.serviceMethod, this.args);
    }
    
    @Override
    public void enqueue(final Callback<T> callback) {
        Utils.checkNotNull(callback, "callback == null");
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
        }
        this.executed = true;
        final okhttp3.Call rawCall = this.rawCall;
        final Throwable creationFailure = this.creationFailure;
        okhttp3.Call rawCall2 = rawCall;
        Throwable creationFailure2 = creationFailure;
        while (true) {
            if (rawCall != null) {
                break Label_0083;
            }
            rawCall2 = rawCall;
            if ((creationFailure2 = creationFailure) != null) {
                break Label_0083;
            }
            try {
                rawCall2 = this.createRawCall();
                this.rawCall = rawCall2;
                creationFailure2 = creationFailure;
                // monitorexit(this)
                if (creationFailure2 != null) {
                    callback.onFailure(this, creationFailure2);
                    return;
                }
            }
            catch (Throwable creationFailure2) {
                Utils.throwIfFatal(creationFailure2);
                this.creationFailure = creationFailure2;
                rawCall2 = rawCall;
                continue;
            }
            break;
        }
        if (this.canceled) {
            rawCall2.cancel();
        }
        rawCall2.enqueue(new okhttp3.Callback() {
            final /* synthetic */ OkHttpCall this$0;
            
            private void callFailure(final Throwable t) {
                try {
                    callback.onFailure(OkHttpCall.this, t);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(final okhttp3.Call call, final IOException ex) {
                this.callFailure(ex);
            }
            
            @Override
            public void onResponse(final okhttp3.Call call, final Response response) {
                retrofit2.Response<T> response2;
                try {
                    response2 = OkHttpCall.this.parseResponse(response);
                    final Callback callback = this;
                    final retrofit2.Callback callback2 = callback;
                    final Callback callback3 = this;
                    final OkHttpCall okHttpCall = callback3.this$0;
                    final retrofit2.Response<T> response3 = response2;
                    callback2.onResponse(okHttpCall, response3);
                    return;
                }
                catch (Throwable t) {
                    this.callFailure(t);
                    return;
                }
                try {
                    final Callback callback = this;
                    final retrofit2.Callback callback2 = callback;
                    final Callback callback3 = this;
                    final OkHttpCall okHttpCall = callback3.this$0;
                    final retrofit2.Response<T> response3 = response2;
                    callback2.onResponse(okHttpCall, response3);
                }
                catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public retrofit2.Response<T> execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
        }
        this.executed = true;
        if (this.creationFailure != null) {
            if (this.creationFailure instanceof IOException) {
                throw (IOException)this.creationFailure;
            }
            if (this.creationFailure instanceof RuntimeException) {
                throw (RuntimeException)this.creationFailure;
            }
            throw (Error)this.creationFailure;
        }
        else {
            Label_0101: {
                okhttp3.Call rawCall;
                if ((rawCall = this.rawCall) != null) {
                    break Label_0101;
                }
                try {
                    rawCall = this.createRawCall();
                    this.rawCall = rawCall;
                    // monitorexit(this)
                    if (this.canceled) {
                        rawCall.cancel();
                    }
                    return this.parseResponse(rawCall.execute());
                }
                catch (IOException ex) {}
                catch (RuntimeException rawCall) {
                    goto Label_0128;
                }
                catch (Error rawCall) {
                    goto Label_0128;
                }
            }
        }
    }
    
    @Override
    public boolean isCanceled() {
        boolean b = true;
        if (this.canceled) {
            return true;
        }
        while (true) {
            synchronized (this) {
                if (this.rawCall != null && this.rawCall.isCanceled()) {
                    return b;
                }
            }
            b = false;
            return b;
        }
    }
    
    @Override
    public boolean isExecuted() {
        synchronized (this) {
            return this.executed;
        }
    }
    
    retrofit2.Response<T> parseResponse(Response response) throws IOException {
        final ResponseBody body = response.body();
        final Response build = response.newBuilder().body(new NoContentResponseBody(body.contentType(), body.contentLength())).build();
        final int code = build.code();
        Label_0075: {
            if (code >= 200) {
                if (code < 300) {
                    break Label_0075;
                }
            }
            try {
                return (retrofit2.Response<T>)retrofit2.Response.error(Utils.buffer(body), build);
            }
            finally {
                body.close();
            }
        }
        if (code == 204 || code == 205) {
            body.close();
            return retrofit2.Response.success((T)null, build);
        }
        response = (Response)new ExceptionCatchingRequestBody(body);
        try {
            return retrofit2.Response.success(this.serviceMethod.toResponse((ResponseBody)response), build);
        }
        catch (RuntimeException ex) {
            ((ExceptionCatchingRequestBody)response).throwIfCaught();
            throw ex;
        }
    }
    
    @Override
    public Request request() {
        while (true) {
            Label_0084: {
                synchronized (this) {
                    final okhttp3.Call rawCall = this.rawCall;
                    if (rawCall != null) {
                        final Object rawCall2 = rawCall.request();
                        return (Request)rawCall2;
                    }
                    if (this.creationFailure == null) {
                        break Label_0084;
                    }
                    if (this.creationFailure instanceof IOException) {
                        throw new RuntimeException("Unable to create request.", this.creationFailure);
                    }
                }
                break;
                try {
                    Object rawCall2 = this.createRawCall();
                    this.rawCall = (okhttp3.Call)rawCall2;
                    rawCall2 = ((okhttp3.Call)rawCall2).request();
                    return (Request)rawCall2;
                }
                catch (RuntimeException ex) {}
                catch (IOException creationFailure) {
                    this.creationFailure = creationFailure;
                    throw new RuntimeException("Unable to create request.", creationFailure);
                }
                catch (Error rawCall2) {
                    goto Label_0105;
                }
            }
        }
        if (this.creationFailure instanceof RuntimeException) {
            throw (RuntimeException)this.creationFailure;
        }
        throw (Error)this.creationFailure;
    }
    
    static final class ExceptionCatchingRequestBody extends ResponseBody
    {
        private final ResponseBody delegate;
        IOException thrownException;
        
        ExceptionCatchingRequestBody(final ResponseBody delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void close() {
            this.delegate.close();
        }
        
        @Override
        public long contentLength() {
            return this.delegate.contentLength();
        }
        
        @Override
        public MediaType contentType() {
            return this.delegate.contentType();
        }
        
        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(this.delegate.source()) {
                @Override
                public long read(final Buffer buffer, long read) throws IOException {
                    try {
                        read = super.read(buffer, read);
                        return read;
                    }
                    catch (IOException thrownException) {
                        throw ExceptionCatchingRequestBody.this.thrownException = thrownException;
                    }
                }
            });
        }
        
        void throwIfCaught() throws IOException {
            if (this.thrownException != null) {
                throw this.thrownException;
            }
        }
    }
    
    static final class NoContentResponseBody extends ResponseBody
    {
        private final long contentLength;
        private final MediaType contentType;
        
        NoContentResponseBody(final MediaType contentType, final long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }
        
        @Override
        public long contentLength() {
            return this.contentLength;
        }
        
        @Override
        public MediaType contentType() {
            return this.contentType;
        }
        
        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }
}
