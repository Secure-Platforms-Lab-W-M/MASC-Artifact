// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import okio.ByteString;
import java.nio.charset.Charset;
import okhttp3.internal.Util;

public final class Credentials
{
    private Credentials() {
    }
    
    public static String basic(final String s, final String s2) {
        return basic(s, s2, Util.ISO_8859_1);
    }
    
    public static String basic(String base64, final String s, final Charset charset) {
        base64 = ByteString.encodeString(base64 + ":" + s, charset).base64();
        return "Basic " + base64;
    }
}
