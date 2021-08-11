// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.PartMap;
import okhttp3.MultipartBody;
import retrofit2.http.Part;
import retrofit2.http.FieldMap;
import retrofit2.http.Field;
import retrofit2.http.HeaderMap;
import retrofit2.http.Header;
import java.util.Map;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;
import java.lang.reflect.ParameterizedType;
import retrofit2.http.Query;
import retrofit2.http.Path;
import java.net.URI;
import retrofit2.http.Url;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.HTTP;
import retrofit2.http.OPTIONS;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.HEAD;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.io.IOException;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.LinkedHashSet;
import java.util.Set;
import okhttp3.ResponseBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Call;
import okhttp3.HttpUrl;
import java.util.regex.Pattern;

final class ServiceMethod<R, T>
{
    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_NAME_REGEX;
    static final Pattern PARAM_URL_REGEX;
    private final HttpUrl baseUrl;
    private final CallAdapter<R, T> callAdapter;
    private final Call.Factory callFactory;
    private final MediaType contentType;
    private final boolean hasBody;
    private final Headers headers;
    private final String httpMethod;
    private final boolean isFormEncoded;
    private final boolean isMultipart;
    private final ParameterHandler<?>[] parameterHandlers;
    private final String relativeUrl;
    private final Converter<ResponseBody, R> responseConverter;
    
    static {
        PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
        PARAM_NAME_REGEX = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
    }
    
    ServiceMethod(final Builder<R, T> builder) {
        this.callFactory = builder.retrofit.callFactory();
        this.callAdapter = builder.callAdapter;
        this.baseUrl = builder.retrofit.baseUrl();
        this.responseConverter = builder.responseConverter;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.hasBody = builder.hasBody;
        this.isFormEncoded = builder.isFormEncoded;
        this.isMultipart = builder.isMultipart;
        this.parameterHandlers = builder.parameterHandlers;
    }
    
    static Class<?> boxIfPrimitive(final Class<?> clazz) {
        Class<Boolean> clazz2;
        if (Boolean.TYPE == clazz) {
            clazz2 = Boolean.class;
        }
        else {
            if (Byte.TYPE == clazz) {
                return Byte.class;
            }
            if (Character.TYPE == clazz) {
                return Character.class;
            }
            if (Double.TYPE == clazz) {
                return Double.class;
            }
            if (Float.TYPE == clazz) {
                return Float.class;
            }
            if (Integer.TYPE == clazz) {
                return Integer.class;
            }
            if (Long.TYPE == clazz) {
                return Long.class;
            }
            if (Short.TYPE == (clazz2 = (Class<Boolean>)clazz)) {
                return Short.class;
            }
        }
        return clazz2;
    }
    
    static Set<String> parsePathParameters(final String s) {
        final Matcher matcher = ServiceMethod.PARAM_URL_REGEX.matcher(s);
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        while (matcher.find()) {
            set.add(matcher.group(1));
        }
        return set;
    }
    
    T adapt(final retrofit2.Call<R> call) {
        return this.callAdapter.adapt(call);
    }
    
    Call toCall(@Nullable final Object... array) throws IOException {
        final RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
        final ParameterHandler<?>[] parameterHandlers = this.parameterHandlers;
        int length;
        if (array != null) {
            length = array.length;
        }
        else {
            length = 0;
        }
        if (length != parameterHandlers.length) {
            throw new IllegalArgumentException("Argument count (" + length + ") doesn't match expected count (" + parameterHandlers.length + ")");
        }
        for (int i = 0; i < length; ++i) {
            parameterHandlers[i].apply(requestBuilder, array[i]);
        }
        return this.callFactory.newCall(requestBuilder.build());
    }
    
    R toResponse(final ResponseBody responseBody) throws IOException {
        return this.responseConverter.convert(responseBody);
    }
    
    static final class Builder<T, R>
    {
        CallAdapter<T, R> callAdapter;
        MediaType contentType;
        boolean gotBody;
        boolean gotField;
        boolean gotPart;
        boolean gotPath;
        boolean gotQuery;
        boolean gotUrl;
        boolean hasBody;
        Headers headers;
        String httpMethod;
        boolean isFormEncoded;
        boolean isMultipart;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        ParameterHandler<?>[] parameterHandlers;
        final Type[] parameterTypes;
        String relativeUrl;
        Set<String> relativeUrlParamNames;
        Converter<ResponseBody, T> responseConverter;
        Type responseType;
        final Retrofit retrofit;
        
        Builder(final Retrofit retrofit, final Method method) {
            this.retrofit = retrofit;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }
        
        private CallAdapter<T, R> createCallAdapter() {
            final Type genericReturnType = this.method.getGenericReturnType();
            if (Utils.hasUnresolvableType(genericReturnType)) {
                throw this.methodError("Method return type must not include a type variable or wildcard: %s", genericReturnType);
            }
            if (genericReturnType == Void.TYPE) {
                throw this.methodError("Service methods cannot return void.", new Object[0]);
            }
            final Annotation[] annotations = this.method.getAnnotations();
            try {
                return (CallAdapter<T, R>)this.retrofit.callAdapter(genericReturnType, annotations);
            }
            catch (RuntimeException ex) {
                throw this.methodError(ex, "Unable to create call adapter for %s", genericReturnType);
            }
        }
        
        private Converter<ResponseBody, T> createResponseConverter() {
            final Annotation[] annotations = this.method.getAnnotations();
            try {
                return (Converter<ResponseBody, T>)this.retrofit.responseBodyConverter(this.responseType, annotations);
            }
            catch (RuntimeException ex) {
                throw this.methodError(ex, "Unable to create converter for %s", this.responseType);
            }
        }
        
        private RuntimeException methodError(final String s, final Object... array) {
            return this.methodError(null, s, array);
        }
        
        private RuntimeException methodError(final Throwable t, String format, final Object... array) {
            format = String.format(format, array);
            return new IllegalArgumentException(format + "\n    for method " + this.method.getDeclaringClass().getSimpleName() + "." + this.method.getName(), t);
        }
        
        private RuntimeException parameterError(final int n, final String s, final Object... array) {
            return this.methodError(s + " (parameter #" + (n + 1) + ")", array);
        }
        
        private RuntimeException parameterError(final Throwable t, final int n, final String s, final Object... array) {
            return this.methodError(t, s + " (parameter #" + (n + 1) + ")", array);
        }
        
        private Headers parseHeaders(final String[] array) {
            final Headers.Builder builder = new Headers.Builder();
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s = array[i];
                final int index = s.indexOf(58);
                if (index == -1 || index == 0 || index == s.length() - 1) {
                    throw this.methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", s);
                }
                final String substring = s.substring(0, index);
                final String trim = s.substring(index + 1).trim();
                if ("Content-Type".equalsIgnoreCase(substring)) {
                    final MediaType parse = MediaType.parse(trim);
                    if (parse == null) {
                        throw this.methodError("Malformed content type: %s", trim);
                    }
                    this.contentType = parse;
                }
                else {
                    builder.add(substring, trim);
                }
            }
            return builder.build();
        }
        
        private void parseHttpMethodAndPath(String substring, final String relativeUrl, final boolean hasBody) {
            if (this.httpMethod != null) {
                throw this.methodError("Only one HTTP method is allowed. Found: %s and %s.", this.httpMethod, substring);
            }
            this.httpMethod = substring;
            this.hasBody = hasBody;
            if (relativeUrl.isEmpty()) {
                return;
            }
            final int index = relativeUrl.indexOf(63);
            if (index != -1 && index < relativeUrl.length() - 1) {
                substring = relativeUrl.substring(index + 1);
                if (ServiceMethod.PARAM_URL_REGEX.matcher(substring).find()) {
                    throw this.methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", substring);
                }
            }
            this.relativeUrl = relativeUrl;
            this.relativeUrlParamNames = ServiceMethod.parsePathParameters(relativeUrl);
        }
        
        private void parseMethodAnnotation(final Annotation annotation) {
            if (annotation instanceof DELETE) {
                this.parseHttpMethodAndPath("DELETE", ((DELETE)annotation).value(), false);
            }
            else {
                if (annotation instanceof GET) {
                    this.parseHttpMethodAndPath("GET", ((GET)annotation).value(), false);
                    return;
                }
                if (annotation instanceof HEAD) {
                    this.parseHttpMethodAndPath("HEAD", ((HEAD)annotation).value(), false);
                    if (!Void.class.equals(this.responseType)) {
                        throw this.methodError("HEAD method must use Void as response type.", new Object[0]);
                    }
                }
                else {
                    if (annotation instanceof PATCH) {
                        this.parseHttpMethodAndPath("PATCH", ((PATCH)annotation).value(), true);
                        return;
                    }
                    if (annotation instanceof POST) {
                        this.parseHttpMethodAndPath("POST", ((POST)annotation).value(), true);
                        return;
                    }
                    if (annotation instanceof PUT) {
                        this.parseHttpMethodAndPath("PUT", ((PUT)annotation).value(), true);
                        return;
                    }
                    if (annotation instanceof OPTIONS) {
                        this.parseHttpMethodAndPath("OPTIONS", ((OPTIONS)annotation).value(), false);
                        return;
                    }
                    if (annotation instanceof HTTP) {
                        final HTTP http = (HTTP)annotation;
                        this.parseHttpMethodAndPath(http.method(), http.path(), http.hasBody());
                        return;
                    }
                    if (annotation instanceof retrofit2.http.Headers) {
                        final String[] value = ((retrofit2.http.Headers)annotation).value();
                        if (value.length == 0) {
                            throw this.methodError("@Headers annotation is empty.", new Object[0]);
                        }
                        this.headers = this.parseHeaders(value);
                    }
                    else if (annotation instanceof Multipart) {
                        if (this.isFormEncoded) {
                            throw this.methodError("Only one encoding annotation is allowed.", new Object[0]);
                        }
                        this.isMultipart = true;
                    }
                    else if (annotation instanceof FormUrlEncoded) {
                        if (this.isMultipart) {
                            throw this.methodError("Only one encoding annotation is allowed.", new Object[0]);
                        }
                        this.isFormEncoded = true;
                    }
                }
            }
        }
        
        private ParameterHandler<?> parseParameter(final int n, final Type type, final Annotation[] array) {
            ParameterHandler<?> parameterHandler = null;
            for (int length = array.length, i = 0; i < length; ++i) {
                final ParameterHandler<?> parameterAnnotation = this.parseParameterAnnotation(n, type, array, array[i]);
                if (parameterAnnotation != null) {
                    if (parameterHandler != null) {
                        throw this.parameterError(n, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
                    }
                    parameterHandler = parameterAnnotation;
                }
            }
            if (parameterHandler == null) {
                throw this.parameterError(n, "No Retrofit annotation found.", new Object[0]);
            }
            return parameterHandler;
        }
        
        private ParameterHandler<?> parseParameterAnnotation(final int n, Type type, final Annotation[] array, final Annotation annotation) {
            if (annotation instanceof Url) {
                if (this.gotUrl) {
                    throw this.parameterError(n, "Multiple @Url method annotations found.", new Object[0]);
                }
                if (this.gotPath) {
                    throw this.parameterError(n, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                if (this.gotQuery) {
                    throw this.parameterError(n, "A @Url parameter must not come after a @Query", new Object[0]);
                }
                if (this.relativeUrl != null) {
                    throw this.parameterError(n, "@Url cannot be used with @%s URL", this.httpMethod);
                }
                this.gotUrl = true;
                if (type == HttpUrl.class || type == String.class || type == URI.class || (type instanceof Class && "android.net.Uri".equals(((Class)type).getName()))) {
                    return new ParameterHandler.RelativeUrl();
                }
                throw this.parameterError(n, "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.", new Object[0]);
            }
            else if (annotation instanceof Path) {
                if (this.gotQuery) {
                    throw this.parameterError(n, "A @Path parameter must not come after a @Query.", new Object[0]);
                }
                if (this.gotUrl) {
                    throw this.parameterError(n, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                if (this.relativeUrl == null) {
                    throw this.parameterError(n, "@Path can only be used with relative url on @%s", this.httpMethod);
                }
                this.gotPath = true;
                final Path path = (Path)annotation;
                final String value = path.value();
                this.validatePathName(n, value);
                return new ParameterHandler.Path<Object>(value, this.retrofit.stringConverter(type, array), path.encoded());
            }
            else if (annotation instanceof Query) {
                final Query query = (Query)annotation;
                final String value2 = query.value();
                final boolean encoded = query.encoded();
                final Class<?> rawType = Utils.getRawType(type);
                this.gotQuery = true;
                if (Iterable.class.isAssignableFrom(rawType)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw this.parameterError(n, rawType.getSimpleName() + " must include generic type (e.g., " + rawType.getSimpleName() + "<String>)", new Object[0]);
                    }
                    type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                    return new ParameterHandler.Query<Object>(value2, this.retrofit.stringConverter(type, array), encoded).iterable();
                }
                else {
                    if (rawType.isArray()) {
                        return new ParameterHandler.Query<Object>(value2, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawType.getComponentType()), array), encoded).array();
                    }
                    return new ParameterHandler.Query<Object>(value2, this.retrofit.stringConverter(type, array), encoded);
                }
            }
            else if (annotation instanceof QueryName) {
                final boolean encoded2 = ((QueryName)annotation).encoded();
                final Class<?> rawType2 = Utils.getRawType(type);
                this.gotQuery = true;
                if (Iterable.class.isAssignableFrom(rawType2)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw this.parameterError(n, rawType2.getSimpleName() + " must include generic type (e.g., " + rawType2.getSimpleName() + "<String>)", new Object[0]);
                    }
                    type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                    return new ParameterHandler.QueryName<Object>(this.retrofit.stringConverter(type, array), encoded2).iterable();
                }
                else {
                    if (rawType2.isArray()) {
                        return new ParameterHandler.QueryName<Object>(this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawType2.getComponentType()), array), encoded2).array();
                    }
                    return new ParameterHandler.QueryName<Object>(this.retrofit.stringConverter(type, array), encoded2);
                }
            }
            else if (annotation instanceof QueryMap) {
                final Class<?> rawType3 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawType3)) {
                    throw this.parameterError(n, "@QueryMap parameter type must be Map.", new Object[0]);
                }
                type = Utils.getSupertype(type, rawType3, Map.class);
                if (!(type instanceof ParameterizedType)) {
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                final Type parameterUpperBound = Utils.getParameterUpperBound(0, parameterizedType);
                if (String.class != parameterUpperBound) {
                    throw this.parameterError(n, "@QueryMap keys must be of type String: " + parameterUpperBound, new Object[0]);
                }
                type = Utils.getParameterUpperBound(1, parameterizedType);
                return new ParameterHandler.QueryMap<Object>(this.retrofit.stringConverter(type, array), ((QueryMap)annotation).encoded());
            }
            else if (annotation instanceof Header) {
                final String value3 = ((Header)annotation).value();
                final Class<?> rawType4 = Utils.getRawType(type);
                if (Iterable.class.isAssignableFrom(rawType4)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw this.parameterError(n, rawType4.getSimpleName() + " must include generic type (e.g., " + rawType4.getSimpleName() + "<String>)", new Object[0]);
                    }
                    type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                    return new ParameterHandler.Header<Object>(value3, this.retrofit.stringConverter(type, array)).iterable();
                }
                else {
                    if (rawType4.isArray()) {
                        return new ParameterHandler.Header<Object>(value3, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawType4.getComponentType()), array)).array();
                    }
                    return new ParameterHandler.Header<Object>(value3, this.retrofit.stringConverter(type, array));
                }
            }
            else if (annotation instanceof HeaderMap) {
                final Class<?> rawType5 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawType5)) {
                    throw this.parameterError(n, "@HeaderMap parameter type must be Map.", new Object[0]);
                }
                type = Utils.getSupertype(type, rawType5, Map.class);
                if (!(type instanceof ParameterizedType)) {
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                final ParameterizedType parameterizedType2 = (ParameterizedType)type;
                final Type parameterUpperBound2 = Utils.getParameterUpperBound(0, parameterizedType2);
                if (String.class != parameterUpperBound2) {
                    throw this.parameterError(n, "@HeaderMap keys must be of type String: " + parameterUpperBound2, new Object[0]);
                }
                type = Utils.getParameterUpperBound(1, parameterizedType2);
                return new ParameterHandler.HeaderMap<Object>(this.retrofit.stringConverter(type, array));
            }
            else if (annotation instanceof Field) {
                if (!this.isFormEncoded) {
                    throw this.parameterError(n, "@Field parameters can only be used with form encoding.", new Object[0]);
                }
                final Field field = (Field)annotation;
                final String value4 = field.value();
                final boolean encoded3 = field.encoded();
                this.gotField = true;
                final Class<?> rawType6 = Utils.getRawType(type);
                if (Iterable.class.isAssignableFrom(rawType6)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw this.parameterError(n, rawType6.getSimpleName() + " must include generic type (e.g., " + rawType6.getSimpleName() + "<String>)", new Object[0]);
                    }
                    type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                    return new ParameterHandler.Field<Object>(value4, this.retrofit.stringConverter(type, array), encoded3).iterable();
                }
                else {
                    if (rawType6.isArray()) {
                        return new ParameterHandler.Field<Object>(value4, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawType6.getComponentType()), array), encoded3).array();
                    }
                    return new ParameterHandler.Field<Object>(value4, this.retrofit.stringConverter(type, array), encoded3);
                }
            }
            else if (annotation instanceof FieldMap) {
                if (!this.isFormEncoded) {
                    throw this.parameterError(n, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
                }
                final Class<?> rawType7 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawType7)) {
                    throw this.parameterError(n, "@FieldMap parameter type must be Map.", new Object[0]);
                }
                type = Utils.getSupertype(type, rawType7, Map.class);
                if (!(type instanceof ParameterizedType)) {
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                final ParameterizedType parameterizedType3 = (ParameterizedType)type;
                final Type parameterUpperBound3 = Utils.getParameterUpperBound(0, parameterizedType3);
                if (String.class != parameterUpperBound3) {
                    throw this.parameterError(n, "@FieldMap keys must be of type String: " + parameterUpperBound3, new Object[0]);
                }
                type = Utils.getParameterUpperBound(1, parameterizedType3);
                final Converter<?, String> stringConverter = this.retrofit.stringConverter(type, array);
                this.gotField = true;
                return new ParameterHandler.FieldMap<Object>((Converter<Object, String>)stringConverter, ((FieldMap)annotation).encoded());
            }
            else if (annotation instanceof Part) {
                if (!this.isMultipart) {
                    throw this.parameterError(n, "@Part parameters can only be used with multipart encoding.", new Object[0]);
                }
                final Part part = (Part)annotation;
                this.gotPart = true;
                final String value5 = part.value();
                final Class<?> rawType8 = Utils.getRawType(type);
                if (value5.isEmpty()) {
                    if (Iterable.class.isAssignableFrom(rawType8)) {
                        if (!(type instanceof ParameterizedType)) {
                            throw this.parameterError(n, rawType8.getSimpleName() + " must include generic type (e.g., " + rawType8.getSimpleName() + "<String>)", new Object[0]);
                        }
                        if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(Utils.getParameterUpperBound(0, (ParameterizedType)type)))) {
                            throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                        }
                        return ParameterHandler.RawPart.INSTANCE.iterable();
                    }
                    else if (rawType8.isArray()) {
                        if (!MultipartBody.Part.class.isAssignableFrom(rawType8.getComponentType())) {
                            throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                        }
                        return ParameterHandler.RawPart.INSTANCE.array();
                    }
                    else {
                        if (MultipartBody.Part.class.isAssignableFrom(rawType8)) {
                            return ParameterHandler.RawPart.INSTANCE;
                        }
                        throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                    }
                }
                else {
                    final Headers of = Headers.of("Content-Disposition", "form-data; name=\"" + value5 + "\"", "Content-Transfer-Encoding", part.encoding());
                    if (Iterable.class.isAssignableFrom(rawType8)) {
                        if (!(type instanceof ParameterizedType)) {
                            throw this.parameterError(n, rawType8.getSimpleName() + " must include generic type (e.g., " + rawType8.getSimpleName() + "<String>)", new Object[0]);
                        }
                        type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                        if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type))) {
                            throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                        }
                        return new ParameterHandler.Part<Object>(of, this.retrofit.requestBodyConverter(type, array, this.methodAnnotations)).iterable();
                    }
                    else if (rawType8.isArray()) {
                        final Class<?> boxIfPrimitive = ServiceMethod.boxIfPrimitive(rawType8.getComponentType());
                        if (MultipartBody.Part.class.isAssignableFrom(boxIfPrimitive)) {
                            throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                        }
                        return new ParameterHandler.Part<Object>(of, this.retrofit.requestBodyConverter(boxIfPrimitive, array, this.methodAnnotations)).array();
                    }
                    else {
                        if (MultipartBody.Part.class.isAssignableFrom(rawType8)) {
                            throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                        }
                        return new ParameterHandler.Part<Object>(of, this.retrofit.requestBodyConverter(type, array, this.methodAnnotations));
                    }
                }
            }
            else {
                if (!(annotation instanceof PartMap)) {
                    if (annotation instanceof Body) {
                        if (this.isFormEncoded || this.isMultipart) {
                            throw this.parameterError(n, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
                        }
                        if (this.gotBody) {
                            throw this.parameterError(n, "Multiple @Body method annotations found.", new Object[0]);
                        }
                        try {
                            final Converter<?, RequestBody> requestBodyConverter = this.retrofit.requestBodyConverter(type, array, this.methodAnnotations);
                            this.gotBody = true;
                            return new ParameterHandler.Body<Object>((Converter<Object, RequestBody>)requestBodyConverter);
                        }
                        catch (RuntimeException ex) {
                            throw this.parameterError(ex, n, "Unable to create @Body converter for %s", type);
                        }
                    }
                    return null;
                }
                if (!this.isMultipart) {
                    throw this.parameterError(n, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
                }
                this.gotPart = true;
                final Class<?> rawType9 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawType9)) {
                    throw this.parameterError(n, "@PartMap parameter type must be Map.", new Object[0]);
                }
                type = Utils.getSupertype(type, rawType9, Map.class);
                if (!(type instanceof ParameterizedType)) {
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                final ParameterizedType parameterizedType4 = (ParameterizedType)type;
                final Type parameterUpperBound4 = Utils.getParameterUpperBound(0, parameterizedType4);
                if (String.class != parameterUpperBound4) {
                    throw this.parameterError(n, "@PartMap keys must be of type String: " + parameterUpperBound4, new Object[0]);
                }
                type = Utils.getParameterUpperBound(1, parameterizedType4);
                if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type))) {
                    throw this.parameterError(n, "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead.", new Object[0]);
                }
                return new ParameterHandler.PartMap<Object>(this.retrofit.requestBodyConverter(type, array, this.methodAnnotations), ((PartMap)annotation).encoding());
            }
        }
        
        private void validatePathName(final int n, final String s) {
            if (!ServiceMethod.PARAM_NAME_REGEX.matcher(s).matches()) {
                throw this.parameterError(n, "@Path parameter name must match %s. Found: %s", ServiceMethod.PARAM_URL_REGEX.pattern(), s);
            }
            if (!this.relativeUrlParamNames.contains(s)) {
                throw this.parameterError(n, "URL \"%s\" does not contain \"{%s}\".", this.relativeUrl, s);
            }
        }
        
        public ServiceMethod build() {
            this.callAdapter = this.createCallAdapter();
            this.responseType = this.callAdapter.responseType();
            if (this.responseType == Response.class || this.responseType == okhttp3.Response.class) {
                throw this.methodError("'" + Utils.getRawType(this.responseType).getName() + "' is not a valid response body type. Did you mean ResponseBody?", new Object[0]);
            }
            this.responseConverter = this.createResponseConverter();
            final Annotation[] methodAnnotations = this.methodAnnotations;
            for (int length = methodAnnotations.length, i = 0; i < length; ++i) {
                this.parseMethodAnnotation(methodAnnotations[i]);
            }
            if (this.httpMethod == null) {
                throw this.methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
            }
            if (!this.hasBody) {
                if (this.isMultipart) {
                    throw this.methodError("Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                }
                if (this.isFormEncoded) {
                    throw this.methodError("FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                }
            }
            final int length2 = this.parameterAnnotationsArray.length;
            this.parameterHandlers = (ParameterHandler<?>[])new ParameterHandler[length2];
            for (int j = 0; j < length2; ++j) {
                final Type type = this.parameterTypes[j];
                if (Utils.hasUnresolvableType(type)) {
                    throw this.parameterError(j, "Parameter type must not include a type variable or wildcard: %s", type);
                }
                final Annotation[] array = this.parameterAnnotationsArray[j];
                if (array == null) {
                    throw this.parameterError(j, "No Retrofit annotation found.", new Object[0]);
                }
                this.parameterHandlers[j] = this.parseParameter(j, type, array);
            }
            if (this.relativeUrl == null && !this.gotUrl) {
                throw this.methodError("Missing either @%s URL or @Url parameter.", this.httpMethod);
            }
            if (!this.isFormEncoded && !this.isMultipart && !this.hasBody && this.gotBody) {
                throw this.methodError("Non-body HTTP method cannot contain @Body.", new Object[0]);
            }
            if (this.isFormEncoded && !this.gotField) {
                throw this.methodError("Form-encoded method must contain at least one @Field.", new Object[0]);
            }
            if (this.isMultipart && !this.gotPart) {
                throw this.methodError("Multipart method must contain at least one @Part.", new Object[0]);
            }
            return new ServiceMethod((Builder<R, T>)this);
        }
    }
}
