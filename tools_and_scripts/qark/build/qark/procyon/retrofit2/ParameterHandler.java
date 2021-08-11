// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.MultipartBody;
import okhttp3.Headers;
import java.util.Map;
import okhttp3.RequestBody;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.io.IOException;
import javax.annotation.Nullable;

abstract class ParameterHandler<T>
{
    abstract void apply(final RequestBuilder p0, @Nullable final T p1) throws IOException;
    
    final ParameterHandler<Object> array() {
        return new ParameterHandler<Object>() {
            @Override
            void apply(final RequestBuilder requestBuilder, @Nullable final Object o) throws IOException {
                if (o != null) {
                    for (int i = 0; i < Array.getLength(o); ++i) {
                        ParameterHandler.this.apply(requestBuilder, Array.get(o, i));
                    }
                }
            }
        };
    }
    
    final ParameterHandler<Iterable<T>> iterable() {
        return new ParameterHandler<Iterable<T>>() {
            @Override
            void apply(final RequestBuilder requestBuilder, @Nullable final Iterable<T> iterable) throws IOException {
                if (iterable != null) {
                    final Iterator<T> iterator = iterable.iterator();
                    while (iterator.hasNext()) {
                        ParameterHandler.this.apply(requestBuilder, iterator.next());
                    }
                }
            }
        };
    }
    
    static final class Body<T> extends ParameterHandler<T>
    {
        private final Converter<T, RequestBody> converter;
        
        Body(final Converter<T, RequestBody> converter) {
            this.converter = converter;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) {
            if (t == null) {
                throw new IllegalArgumentException("Body parameter value must not be null.");
            }
            try {
                requestBuilder.setBody(this.converter.convert(t));
            }
            catch (IOException ex) {
                throw new RuntimeException("Unable to convert " + t + " to RequestBody", ex);
            }
        }
    }
    
    static final class Field<T> extends ParameterHandler<T>
    {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;
        
        Field(final String s, final Converter<T, String> valueConverter, final boolean encoded) {
            this.name = Utils.checkNotNull(s, "name == null");
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) throws IOException {
            if (t != null) {
                final String s = this.valueConverter.convert(t);
                if (s != null) {
                    requestBuilder.addFormField(this.name, s, this.encoded);
                }
            }
        }
    }
    
    static final class FieldMap<T> extends ParameterHandler<Map<String, T>>
    {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;
        
        FieldMap(final Converter<T, String> valueConverter, final boolean encoded) {
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Field map was null.");
            }
            for (final Map.Entry<String, T> entry : map.entrySet()) {
                final String s = entry.getKey();
                if (s == null) {
                    throw new IllegalArgumentException("Field map contained null key.");
                }
                final T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Field map contained null value for key '" + s + "'.");
                }
                final String s2 = this.valueConverter.convert(value);
                if (s2 == null) {
                    throw new IllegalArgumentException("Field map value '" + value + "' converted to null by " + this.valueConverter.getClass().getName() + " for key '" + s + "'.");
                }
                requestBuilder.addFormField(s, s2, this.encoded);
            }
        }
    }
    
    static final class Header<T> extends ParameterHandler<T>
    {
        private final String name;
        private final Converter<T, String> valueConverter;
        
        Header(final String s, final Converter<T, String> valueConverter) {
            this.name = Utils.checkNotNull(s, "name == null");
            this.valueConverter = valueConverter;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) throws IOException {
            if (t != null) {
                final String s = this.valueConverter.convert(t);
                if (s != null) {
                    requestBuilder.addHeader(this.name, s);
                }
            }
        }
    }
    
    static final class HeaderMap<T> extends ParameterHandler<Map<String, T>>
    {
        private final Converter<T, String> valueConverter;
        
        HeaderMap(final Converter<T, String> valueConverter) {
            this.valueConverter = valueConverter;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Header map was null.");
            }
            for (final Map.Entry<String, T> entry : map.entrySet()) {
                final String s = entry.getKey();
                if (s == null) {
                    throw new IllegalArgumentException("Header map contained null key.");
                }
                final T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Header map contained null value for key '" + s + "'.");
                }
                requestBuilder.addHeader(s, this.valueConverter.convert(value));
            }
        }
    }
    
    static final class Part<T> extends ParameterHandler<T>
    {
        private final Converter<T, RequestBody> converter;
        private final Headers headers;
        
        Part(final Headers headers, final Converter<T, RequestBody> converter) {
            this.headers = headers;
            this.converter = converter;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) {
            if (t == null) {
                return;
            }
            try {
                requestBuilder.addPart(this.headers, this.converter.convert(t));
            }
            catch (IOException ex) {
                throw new RuntimeException("Unable to convert " + t + " to RequestBody", ex);
            }
        }
    }
    
    static final class PartMap<T> extends ParameterHandler<Map<String, T>>
    {
        private final String transferEncoding;
        private final Converter<T, RequestBody> valueConverter;
        
        PartMap(final Converter<T, RequestBody> valueConverter, final String transferEncoding) {
            this.valueConverter = valueConverter;
            this.transferEncoding = transferEncoding;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Part map was null.");
            }
            for (final Map.Entry<String, T> entry : map.entrySet()) {
                final String s = entry.getKey();
                if (s == null) {
                    throw new IllegalArgumentException("Part map contained null key.");
                }
                final T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Part map contained null value for key '" + s + "'.");
                }
                requestBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + s + "\"", "Content-Transfer-Encoding", this.transferEncoding), this.valueConverter.convert(value));
            }
        }
    }
    
    static final class Path<T> extends ParameterHandler<T>
    {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;
        
        Path(final String s, final Converter<T, String> valueConverter, final boolean encoded) {
            this.name = Utils.checkNotNull(s, "name == null");
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) throws IOException {
            if (t == null) {
                throw new IllegalArgumentException("Path parameter \"" + this.name + "\" value must not be null.");
            }
            requestBuilder.addPathParam(this.name, this.valueConverter.convert(t), this.encoded);
        }
    }
    
    static final class Query<T> extends ParameterHandler<T>
    {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;
        
        Query(final String s, final Converter<T, String> valueConverter, final boolean encoded) {
            this.name = Utils.checkNotNull(s, "name == null");
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) throws IOException {
            if (t != null) {
                final String s = this.valueConverter.convert(t);
                if (s != null) {
                    requestBuilder.addQueryParam(this.name, s, this.encoded);
                }
            }
        }
    }
    
    static final class QueryMap<T> extends ParameterHandler<Map<String, T>>
    {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;
        
        QueryMap(final Converter<T, String> valueConverter, final boolean encoded) {
            this.valueConverter = valueConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Query map was null.");
            }
            for (final Map.Entry<String, T> entry : map.entrySet()) {
                final String s = entry.getKey();
                if (s == null) {
                    throw new IllegalArgumentException("Query map contained null key.");
                }
                final T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Query map contained null value for key '" + s + "'.");
                }
                final String s2 = this.valueConverter.convert(value);
                if (s2 == null) {
                    throw new IllegalArgumentException("Query map value '" + value + "' converted to null by " + this.valueConverter.getClass().getName() + " for key '" + s + "'.");
                }
                requestBuilder.addQueryParam(s, s2, this.encoded);
            }
        }
    }
    
    static final class QueryName<T> extends ParameterHandler<T>
    {
        private final boolean encoded;
        private final Converter<T, String> nameConverter;
        
        QueryName(final Converter<T, String> nameConverter, final boolean encoded) {
            this.nameConverter = nameConverter;
            this.encoded = encoded;
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final T t) throws IOException {
            if (t == null) {
                return;
            }
            requestBuilder.addQueryParam(this.nameConverter.convert(t), null, this.encoded);
        }
    }
    
    static final class RawPart extends ParameterHandler<MultipartBody.Part>
    {
        static final RawPart INSTANCE;
        
        static {
            INSTANCE = new RawPart();
        }
        
        private RawPart() {
        }
        
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final MultipartBody.Part part) {
            if (part != null) {
                requestBuilder.addPart(part);
            }
        }
    }
    
    static final class RelativeUrl extends ParameterHandler<Object>
    {
        @Override
        void apply(final RequestBuilder requestBuilder, @Nullable final Object relativeUrl) {
            Utils.checkNotNull(relativeUrl, "@Url parameter is null.");
            requestBuilder.setRelativeUrl(relativeUrl);
        }
    }
}
