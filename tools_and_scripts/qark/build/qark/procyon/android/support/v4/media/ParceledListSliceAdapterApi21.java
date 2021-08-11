// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import java.lang.reflect.InvocationTargetException;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import java.lang.reflect.Constructor;

class ParceledListSliceAdapterApi21
{
    private static Constructor sConstructor;
    
    static {
        try {
            ParceledListSliceAdapterApi21.sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
            return;
        }
        catch (NoSuchMethodException ex) {}
        catch (ClassNotFoundException ex2) {}
        final NoSuchMethodException ex;
        ex.printStackTrace();
    }
    
    private ParceledListSliceAdapterApi21() {
    }
    
    static Object newInstance(List<MediaBrowser$MediaItem> instance) {
        try {
            instance = (InvocationTargetException)ParceledListSliceAdapterApi21.sConstructor.newInstance(instance);
            return instance;
        }
        catch (InvocationTargetException instance) {}
        catch (IllegalAccessException instance) {}
        catch (InstantiationException ex) {}
        instance.printStackTrace();
        return null;
    }
}
