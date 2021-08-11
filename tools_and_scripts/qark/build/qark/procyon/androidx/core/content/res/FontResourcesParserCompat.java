// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.util.Base64;
import androidx.core.provider.FontRequest;
import androidx.core.R$styleable;
import android.util.Xml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import org.xmlpull.v1.XmlPullParser;
import android.util.TypedValue;
import android.os.Build$VERSION;
import android.content.res.TypedArray;

public class FontResourcesParserCompat
{
    private static final int DEFAULT_TIMEOUT_MILLIS = 500;
    public static final int FETCH_STRATEGY_ASYNC = 1;
    public static final int FETCH_STRATEGY_BLOCKING = 0;
    public static final int INFINITE_TIMEOUT_VALUE = -1;
    private static final int ITALIC = 1;
    private static final int NORMAL_WEIGHT = 400;
    
    private FontResourcesParserCompat() {
    }
    
    private static int getType(final TypedArray typedArray, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return typedArray.getType(n);
        }
        final TypedValue typedValue = new TypedValue();
        typedArray.getValue(n, typedValue);
        return typedValue.type;
    }
    
    public static FamilyResourceEntry parse(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 2 && next != 1);
        if (next == 2) {
            return readFamilies(xmlPullParser, resources);
        }
        throw new XmlPullParserException("No start tag found");
    }
    
    public static List<List<byte[]>> readCerts(final Resources resources, int n) {
        if (n == 0) {
            return Collections.emptyList();
        }
        while (true) {
            final TypedArray obtainTypedArray = resources.obtainTypedArray(n);
            while (true) {
                try {
                    if (obtainTypedArray.length() == 0) {
                        return Collections.emptyList();
                    }
                    final ArrayList<List<byte[]>> list = new ArrayList<List<byte[]>>();
                    if (getType(obtainTypedArray, 0) != 1) {
                        list.add(toByteArrayList(resources.getStringArray(n)));
                        return list;
                    }
                    n = 0;
                    if (n >= obtainTypedArray.length()) {
                        return list;
                    }
                    final int resourceId = obtainTypedArray.getResourceId(n, 0);
                    if (resourceId != 0) {
                        list.add(toByteArrayList(resources.getStringArray(resourceId)));
                    }
                }
                finally {
                    obtainTypedArray.recycle();
                }
                ++n;
                continue;
            }
        }
    }
    
    private static FamilyResourceEntry readFamilies(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, (String)null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return readFamily(xmlPullParser, resources);
        }
        skip(xmlPullParser);
        return null;
    }
    
    private static FamilyResourceEntry readFamily(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.FontFamily);
        final String string = obtainAttributes.getString(R$styleable.FontFamily_fontProviderAuthority);
        final String string2 = obtainAttributes.getString(R$styleable.FontFamily_fontProviderPackage);
        final String string3 = obtainAttributes.getString(R$styleable.FontFamily_fontProviderQuery);
        final int resourceId = obtainAttributes.getResourceId(R$styleable.FontFamily_fontProviderCerts, 0);
        final int integer = obtainAttributes.getInteger(R$styleable.FontFamily_fontProviderFetchStrategy, 1);
        final int integer2 = obtainAttributes.getInteger(R$styleable.FontFamily_fontProviderFetchTimeout, 500);
        obtainAttributes.recycle();
        if (string != null && string2 != null && string3 != null) {
            while (xmlPullParser.next() != 3) {
                skip(xmlPullParser);
            }
            return (FamilyResourceEntry)new ProviderResourceEntry(new FontRequest(string, string2, string3, readCerts(resources, resourceId)), integer, integer2);
        }
        final ArrayList<FontFileResourceEntry> list = new ArrayList<FontFileResourceEntry>();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() != 2) {
                continue;
            }
            if (xmlPullParser.getName().equals("font")) {
                list.add(readFont(xmlPullParser, resources));
            }
            else {
                skip(xmlPullParser);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return (FamilyResourceEntry)new FontFamilyFilesResourceEntry((FontFileResourceEntry[])list.toArray(new FontFileResourceEntry[list.size()]));
    }
    
    private static FontFileResourceEntry readFont(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.FontFamilyFont);
        int n;
        if (obtainAttributes.hasValue(R$styleable.FontFamilyFont_fontWeight)) {
            n = R$styleable.FontFamilyFont_fontWeight;
        }
        else {
            n = R$styleable.FontFamilyFont_android_fontWeight;
        }
        final int int1 = obtainAttributes.getInt(n, 400);
        int n2;
        if (obtainAttributes.hasValue(R$styleable.FontFamilyFont_fontStyle)) {
            n2 = R$styleable.FontFamilyFont_fontStyle;
        }
        else {
            n2 = R$styleable.FontFamilyFont_android_fontStyle;
        }
        final boolean b = 1 == obtainAttributes.getInt(n2, 0);
        int n3;
        if (obtainAttributes.hasValue(R$styleable.FontFamilyFont_ttcIndex)) {
            n3 = R$styleable.FontFamilyFont_ttcIndex;
        }
        else {
            n3 = R$styleable.FontFamilyFont_android_ttcIndex;
        }
        int n4;
        if (obtainAttributes.hasValue(R$styleable.FontFamilyFont_fontVariationSettings)) {
            n4 = R$styleable.FontFamilyFont_fontVariationSettings;
        }
        else {
            n4 = R$styleable.FontFamilyFont_android_fontVariationSettings;
        }
        final String string = obtainAttributes.getString(n4);
        final int int2 = obtainAttributes.getInt(n3, 0);
        int n5;
        if (obtainAttributes.hasValue(R$styleable.FontFamilyFont_font)) {
            n5 = R$styleable.FontFamilyFont_font;
        }
        else {
            n5 = R$styleable.FontFamilyFont_android_font;
        }
        final int resourceId = obtainAttributes.getResourceId(n5, 0);
        final String string2 = obtainAttributes.getString(n5);
        obtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            skip(xmlPullParser);
        }
        return new FontFileResourceEntry(string2, int1, b, string, int2, resourceId);
    }
    
    private static void skip(final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int i = 1;
        while (i > 0) {
            final int next = xmlPullParser.next();
            if (next != 2) {
                if (next != 3) {
                    continue;
                }
                --i;
            }
            else {
                ++i;
            }
        }
    }
    
    private static List<byte[]> toByteArrayList(final String[] array) {
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(Base64.decode(array[i], 0));
        }
        return list;
    }
    
    public interface FamilyResourceEntry
    {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface FetchStrategy {
    }
    
    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry
    {
        private final FontFileResourceEntry[] mEntries;
        
        public FontFamilyFilesResourceEntry(final FontFileResourceEntry[] mEntries) {
            this.mEntries = mEntries;
        }
        
        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }
    
    public static final class FontFileResourceEntry
    {
        private final String mFileName;
        private boolean mItalic;
        private int mResourceId;
        private int mTtcIndex;
        private String mVariationSettings;
        private int mWeight;
        
        public FontFileResourceEntry(final String mFileName, final int mWeight, final boolean mItalic, final String mVariationSettings, final int mTtcIndex, final int mResourceId) {
            this.mFileName = mFileName;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
            this.mVariationSettings = mVariationSettings;
            this.mTtcIndex = mTtcIndex;
            this.mResourceId = mResourceId;
        }
        
        public String getFileName() {
            return this.mFileName;
        }
        
        public int getResourceId() {
            return this.mResourceId;
        }
        
        public int getTtcIndex() {
            return this.mTtcIndex;
        }
        
        public String getVariationSettings() {
            return this.mVariationSettings;
        }
        
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
    
    public static final class ProviderResourceEntry implements FamilyResourceEntry
    {
        private final FontRequest mRequest;
        private final int mStrategy;
        private final int mTimeoutMs;
        
        public ProviderResourceEntry(final FontRequest mRequest, final int mStrategy, final int mTimeoutMs) {
            this.mRequest = mRequest;
            this.mStrategy = mStrategy;
            this.mTimeoutMs = mTimeoutMs;
        }
        
        public int getFetchStrategy() {
            return this.mStrategy;
        }
        
        public FontRequest getRequest() {
            return this.mRequest;
        }
        
        public int getTimeout() {
            return this.mTimeoutMs;
        }
    }
}
