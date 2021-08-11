// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.Locale;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public final class MediaType
{
    private static final Pattern PARAMETER;
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final Pattern TYPE_SUBTYPE;
    @Nullable
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;
    
    static {
        TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
        PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    }
    
    private MediaType(final String mediaType, final String type, final String subtype, @Nullable final String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }
    
    @Nullable
    public static MediaType parse(final String s) {
        final Matcher matcher = MediaType.TYPE_SUBTYPE.matcher(s);
        if (matcher.lookingAt()) {
            final String lowerCase = matcher.group(1).toLowerCase(Locale.US);
            final String lowerCase2 = matcher.group(2).toLowerCase(Locale.US);
            String s2 = null;
            final Matcher matcher2 = MediaType.PARAMETER.matcher(s);
            String s3;
            for (int i = matcher.end(); i < s.length(); i = matcher2.end(), s2 = s3) {
                matcher2.region(i, s.length());
                if (!matcher2.lookingAt()) {
                    return null;
                }
                final String group = matcher2.group(1);
                s3 = s2;
                if (group != null) {
                    if (!group.equalsIgnoreCase("charset")) {
                        s3 = s2;
                    }
                    else {
                        s3 = matcher2.group(2);
                        if (s3 != null) {
                            if (s3.startsWith("'") && s3.endsWith("'") && s3.length() > 2) {
                                s3 = s3.substring(1, s3.length() - 1);
                            }
                        }
                        else {
                            s3 = matcher2.group(3);
                        }
                        if (s2 != null && !s3.equalsIgnoreCase(s2)) {
                            return null;
                        }
                    }
                }
            }
            return new MediaType(s, lowerCase, lowerCase2, s2);
        }
        return null;
    }
    
    @Nullable
    public Charset charset() {
        return this.charset(null);
    }
    
    @Nullable
    public Charset charset(@Nullable final Charset charset) {
        Charset forName = charset;
        try {
            if (this.charset != null) {
                forName = Charset.forName(this.charset);
            }
            return forName;
        }
        catch (IllegalArgumentException ex) {
            return charset;
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof MediaType && ((MediaType)o).mediaType.equals(this.mediaType);
    }
    
    @Override
    public int hashCode() {
        return this.mediaType.hashCode();
    }
    
    public String subtype() {
        return this.subtype;
    }
    
    @Override
    public String toString() {
        return this.mediaType;
    }
    
    public String type() {
        return this.type;
    }
}
