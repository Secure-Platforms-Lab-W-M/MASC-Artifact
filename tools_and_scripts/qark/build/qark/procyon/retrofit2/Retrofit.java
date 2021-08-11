// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import java.util.Collections;
import okhttp3.OkHttpClient;
import java.util.Collection;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Method;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import okhttp3.Call;
import java.util.List;
import okhttp3.HttpUrl;

public final class Retrofit
{
    final HttpUrl baseUrl;
    final List<CallAdapter.Factory> callAdapterFactories;
    final Call.Factory callFactory;
    @Nullable
    final Executor callbackExecutor;
    final List<Converter.Factory> converterFactories;
    private final Map<Method, ServiceMethod<?, ?>> serviceMethodCache;
    final boolean validateEagerly;
    
    Retrofit(final Call.Factory callFactory, final HttpUrl baseUrl, final List<Converter.Factory> converterFactories, final List<CallAdapter.Factory> callAdapterFactories, @Nullable final Executor callbackExecutor, final boolean validateEagerly) {
        this.serviceMethodCache = new ConcurrentHashMap<Method, ServiceMethod<?, ?>>();
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
        this.converterFactories = converterFactories;
        this.callAdapterFactories = callAdapterFactories;
        this.callbackExecutor = callbackExecutor;
        this.validateEagerly = validateEagerly;
    }
    
    private void eagerlyValidateMethods(final Class<?> clazz) {
        final Platform value = Platform.get();
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int length = declaredMethods.length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (!value.isDefaultMethod(method)) {
                this.loadServiceMethod(method);
            }
        }
    }
    
    public HttpUrl baseUrl() {
        return this.baseUrl;
    }
    
    public CallAdapter<?, ?> callAdapter(final Type type, final Annotation[] array) {
        return this.nextCallAdapter(null, type, array);
    }
    
    public List<CallAdapter.Factory> callAdapterFactories() {
        return this.callAdapterFactories;
    }
    
    public Call.Factory callFactory() {
        return this.callFactory;
    }
    
    @Nullable
    public Executor callbackExecutor() {
        return this.callbackExecutor;
    }
    
    public List<Converter.Factory> converterFactories() {
        return this.converterFactories;
    }
    
    public <T> T create(final Class<T> clazz) {
        Utils.validateServiceInterface(clazz);
        if (this.validateEagerly) {
            this.eagerlyValidateMethods(clazz);
        }
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {
            private final Platform platform = Platform.get();
            
            @Override
            public Object invoke(final Object o, final Method method, @Nullable final Object[] array) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, array);
                }
                if (this.platform.isDefaultMethod(method)) {
                    return this.platform.invokeDefaultMethod(method, clazz, o, array);
                }
                final ServiceMethod<?, ?> loadServiceMethod = Retrofit.this.loadServiceMethod(method);
                return loadServiceMethod.adapt(new OkHttpCall<Object>((ServiceMethod<Object, ?>)loadServiceMethod, array));
            }
        });
    }
    
    ServiceMethod<?, ?> loadServiceMethod(final Method method) {
        final ServiceMethod<?, ?> serviceMethod = this.serviceMethodCache.get(method);
        if (serviceMethod != null) {
            return serviceMethod;
        }
        synchronized (this.serviceMethodCache) {
            ServiceMethod<?, ?> build;
            if ((build = this.serviceMethodCache.get(method)) == null) {
                build = (ServiceMethod<?, ?>)new ServiceMethod.Builder(this, method).build();
                this.serviceMethodCache.put(method, build);
            }
            return build;
        }
    }
    
    public Builder newBuilder() {
        return new Builder(this);
    }
    
    public CallAdapter<?, ?> nextCallAdapter(@Nullable final CallAdapter.Factory factory, final Type type, final Annotation[] array) {
        Utils.checkNotNull(type, "returnType == null");
        Utils.checkNotNull(array, "annotations == null");
        int j;
        int i;
        for (i = (j = this.callAdapterFactories.indexOf(factory) + 1); j < this.callAdapterFactories.size(); ++j) {
            final CallAdapter<?, ?> value = this.callAdapterFactories.get(j).get(type, array, this);
            if (value != null) {
                return value;
            }
        }
        final StringBuilder append = new StringBuilder("Could not locate call adapter for ").append(type).append(".\n");
        if (factory != null) {
            append.append("  Skipped:");
            for (int k = 0; k < i; ++k) {
                append.append("\n   * ").append(this.callAdapterFactories.get(k).getClass().getName());
            }
            append.append('\n');
        }
        append.append("  Tried:");
        while (i < this.callAdapterFactories.size()) {
            append.append("\n   * ").append(this.callAdapterFactories.get(i).getClass().getName());
            ++i;
        }
        throw new IllegalArgumentException(append.toString());
    }
    
    public <T> Converter<T, RequestBody> nextRequestBodyConverter(@Nullable final Converter.Factory factory, final Type type, final Annotation[] array, final Annotation[] array2) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(array, "parameterAnnotations == null");
        Utils.checkNotNull(array2, "methodAnnotations == null");
        int j;
        int i;
        for (i = (j = this.converterFactories.indexOf(factory) + 1); j < this.converterFactories.size(); ++j) {
            final Converter<?, RequestBody> requestBodyConverter = this.converterFactories.get(j).requestBodyConverter(type, array, array2, this);
            if (requestBodyConverter != null) {
                return (Converter<T, RequestBody>)requestBodyConverter;
            }
        }
        final StringBuilder append = new StringBuilder("Could not locate RequestBody converter for ").append(type).append(".\n");
        if (factory != null) {
            append.append("  Skipped:");
            for (int k = 0; k < i; ++k) {
                append.append("\n   * ").append(this.converterFactories.get(k).getClass().getName());
            }
            append.append('\n');
        }
        append.append("  Tried:");
        while (i < this.converterFactories.size()) {
            append.append("\n   * ").append(this.converterFactories.get(i).getClass().getName());
            ++i;
        }
        throw new IllegalArgumentException(append.toString());
    }
    
    public <T> Converter<ResponseBody, T> nextResponseBodyConverter(@Nullable final Converter.Factory factory, final Type type, final Annotation[] array) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(array, "annotations == null");
        int j;
        int i;
        for (i = (j = this.converterFactories.indexOf(factory) + 1); j < this.converterFactories.size(); ++j) {
            final Converter<ResponseBody, ?> responseBodyConverter = this.converterFactories.get(j).responseBodyConverter(type, array, this);
            if (responseBodyConverter != null) {
                return (Converter<ResponseBody, T>)responseBodyConverter;
            }
        }
        final StringBuilder append = new StringBuilder("Could not locate ResponseBody converter for ").append(type).append(".\n");
        if (factory != null) {
            append.append("  Skipped:");
            for (int k = 0; k < i; ++k) {
                append.append("\n   * ").append(this.converterFactories.get(k).getClass().getName());
            }
            append.append('\n');
        }
        append.append("  Tried:");
        while (i < this.converterFactories.size()) {
            append.append("\n   * ").append(this.converterFactories.get(i).getClass().getName());
            ++i;
        }
        throw new IllegalArgumentException(append.toString());
    }
    
    public <T> Converter<T, RequestBody> requestBodyConverter(final Type type, final Annotation[] array, final Annotation[] array2) {
        return this.nextRequestBodyConverter(null, type, array, array2);
    }
    
    public <T> Converter<ResponseBody, T> responseBodyConverter(final Type type, final Annotation[] array) {
        return this.nextResponseBodyConverter(null, type, array);
    }
    
    public <T> Converter<T, String> stringConverter(final Type type, final Annotation[] array) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(array, "annotations == null");
        for (int i = 0; i < this.converterFactories.size(); ++i) {
            final Converter<?, String> stringConverter = this.converterFactories.get(i).stringConverter(type, array, this);
            if (stringConverter != null) {
                return (Converter<T, String>)stringConverter;
            }
        }
        return (Converter<T, String>)BuiltInConverters.ToStringConverter.INSTANCE;
    }
    
    public static final class Builder
    {
        private HttpUrl baseUrl;
        private final List<CallAdapter.Factory> callAdapterFactories;
        @Nullable
        private Call.Factory callFactory;
        @Nullable
        private Executor callbackExecutor;
        private final List<Converter.Factory> converterFactories;
        private final Platform platform;
        private boolean validateEagerly;
        
        public Builder() {
            this(Platform.get());
        }
        
        Builder(final Platform platform) {
            this.converterFactories = new ArrayList<Converter.Factory>();
            this.callAdapterFactories = new ArrayList<CallAdapter.Factory>();
            this.platform = platform;
        }
        
        Builder(final Retrofit retrofit) {
            this.converterFactories = new ArrayList<Converter.Factory>();
            this.callAdapterFactories = new ArrayList<CallAdapter.Factory>();
            this.platform = Platform.get();
            this.callFactory = retrofit.callFactory;
            this.baseUrl = retrofit.baseUrl;
            this.converterFactories.addAll(retrofit.converterFactories);
            this.converterFactories.remove(0);
            this.callAdapterFactories.addAll(retrofit.callAdapterFactories);
            this.callAdapterFactories.remove(this.callAdapterFactories.size() - 1);
            this.callbackExecutor = retrofit.callbackExecutor;
            this.validateEagerly = retrofit.validateEagerly;
        }
        
        public Builder addCallAdapterFactory(final CallAdapter.Factory factory) {
            this.callAdapterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }
        
        public Builder addConverterFactory(final Converter.Factory factory) {
            this.converterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }
        
        public Builder baseUrl(final String s) {
            Utils.checkNotNull(s, "baseUrl == null");
            final HttpUrl parse = HttpUrl.parse(s);
            if (parse == null) {
                throw new IllegalArgumentException("Illegal URL: " + s);
            }
            return this.baseUrl(parse);
        }
        
        public Builder baseUrl(final HttpUrl baseUrl) {
            Utils.checkNotNull(baseUrl, "baseUrl == null");
            final List<String> pathSegments = baseUrl.pathSegments();
            if (!"".equals(pathSegments.get(pathSegments.size() - 1))) {
                throw new IllegalArgumentException("baseUrl must end in /: " + baseUrl);
            }
            this.baseUrl = baseUrl;
            return this;
        }
        
        public Retrofit build() {
            if (this.baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            Call.Factory callFactory;
            if ((callFactory = this.callFactory) == null) {
                callFactory = new OkHttpClient();
            }
            Executor executor;
            if ((executor = this.callbackExecutor) == null) {
                executor = this.platform.defaultCallbackExecutor();
            }
            final ArrayList<CallAdapter.Factory> list = new ArrayList<CallAdapter.Factory>(this.callAdapterFactories);
            list.add(this.platform.defaultCallAdapterFactory(executor));
            final ArrayList<Converter.Factory> list2 = new ArrayList<Converter.Factory>(this.converterFactories.size() + 1);
            list2.add(new BuiltInConverters());
            list2.addAll((Collection<?>)this.converterFactories);
            return new Retrofit(callFactory, this.baseUrl, (List<Converter.Factory>)Collections.unmodifiableList((List<?>)list2), Collections.unmodifiableList((List<? extends CallAdapter.Factory>)list), executor, this.validateEagerly);
        }
        
        public List<CallAdapter.Factory> callAdapterFactories() {
            return this.callAdapterFactories;
        }
        
        public Builder callFactory(final Call.Factory factory) {
            this.callFactory = Utils.checkNotNull(factory, "factory == null");
            return this;
        }
        
        public Builder callbackExecutor(final Executor executor) {
            this.callbackExecutor = Utils.checkNotNull(executor, "executor == null");
            return this;
        }
        
        public Builder client(final OkHttpClient okHttpClient) {
            return this.callFactory(Utils.checkNotNull(okHttpClient, "client == null"));
        }
        
        public List<Converter.Factory> converterFactories() {
            return this.converterFactories;
        }
        
        public Builder validateEagerly(final boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }
    }
}
