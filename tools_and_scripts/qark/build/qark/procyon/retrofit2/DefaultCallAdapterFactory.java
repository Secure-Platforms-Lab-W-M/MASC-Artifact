// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class DefaultCallAdapterFactory extends Factory
{
    static final Factory INSTANCE;
    
    static {
        INSTANCE = new DefaultCallAdapterFactory();
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
                return call;
            }
            
            @Override
            public Type responseType() {
                return this.val$responseType;
            }
        };
    }
}
