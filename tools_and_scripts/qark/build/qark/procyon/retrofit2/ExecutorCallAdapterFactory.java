// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.Request;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

final class ExecutorCallAdapterFactory extends Factory
{
    final Executor callbackExecutor;
    
    ExecutorCallAdapterFactory(final Executor callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }
    
    @Override
    public CallAdapter<?, ?> get(final Type type, final Annotation[] array, final Retrofit retrofit) {
        if (CallAdapter.Factory.getRawType(type) != Call.class) {
            return null;
        }
        return new CallAdapter<Object, Call<?>>() {
            final /* synthetic */ Type val$responseType = Utils.getCallResponseType(type);
            
            @Override
            public Call<Object> adapt(final Call<Object> call) {
                return new ExecutorCallbackCall<Object>(ExecutorCallAdapterFactory.this.callbackExecutor, call);
            }
            
            @Override
            public Type responseType() {
                return this.val$responseType;
            }
        };
    }
    
    static final class ExecutorCallbackCall<T> implements Call<T>
    {
        final Executor callbackExecutor;
        final Call<T> delegate;
        
        ExecutorCallbackCall(final Executor callbackExecutor, final Call<T> delegate) {
            this.callbackExecutor = callbackExecutor;
            this.delegate = delegate;
        }
        
        @Override
        public void cancel() {
            this.delegate.cancel();
        }
        
        @Override
        public Call<T> clone() {
            return new ExecutorCallbackCall(this.callbackExecutor, (Call<Object>)this.delegate.clone());
        }
        
        @Override
        public void enqueue(final Callback<T> callback) {
            Utils.checkNotNull(callback, "callback == null");
            this.delegate.enqueue(new Callback<T>() {
                @Override
                public void onFailure(final Call<T> call, final Throwable t) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(ExecutorCallbackCall.this, t);
                        }
                    });
                }
                
                @Override
                public void onResponse(final Call<T> call, final Response<T> response) {
                    ExecutorCallbackCall.this.callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (ExecutorCallbackCall.this.delegate.isCanceled()) {
                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                                return;
                            }
                            callback.onResponse(ExecutorCallbackCall.this, response);
                        }
                    });
                }
            });
        }
        
        @Override
        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }
        
        @Override
        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }
        
        @Override
        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }
        
        @Override
        public Request request() {
            return this.delegate.request();
        }
    }
}
