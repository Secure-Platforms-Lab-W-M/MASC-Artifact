/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.util.Base64
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.compat.R;
import android.support.v4.provider.FontRequest;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Xml;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class FontResourcesParserCompat {
    private static final int DEFAULT_TIMEOUT_MILLIS = 500;
    public static final int FETCH_STRATEGY_ASYNC = 1;
    public static final int FETCH_STRATEGY_BLOCKING = 0;
    public static final int INFINITE_TIMEOUT_VALUE = -1;
    private static final int ITALIC = 1;
    private static final int NORMAL_WEIGHT = 400;

    @Nullable
    public static FamilyResourceEntry parse(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        int n;
        while ((n = xmlPullParser.next()) != 2 && n != 1) {
        }
        if (n == 2) {
            return FontResourcesParserCompat.readFamilies(xmlPullParser, resources);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static List<List<byte[]>> readCerts(Resources arrayList, @ArrayRes int n) {
        TypedArray typedArray = null;
        ArrayList<List<byte[]>> arrayList2 = null;
        if (n != 0) {
            typedArray = arrayList.obtainTypedArray(n);
            if (typedArray.length() > 0) {
                arrayList2 = new ArrayList<List<byte[]>>();
                boolean bl = typedArray.getResourceId(0, 0) != 0;
                if (bl) {
                    for (n = 0; n < typedArray.length(); ++n) {
                        arrayList2.add(FontResourcesParserCompat.toByteArrayList(arrayList.getStringArray(typedArray.getResourceId(n, 0))));
                    }
                    arrayList = arrayList2;
                } else {
                    arrayList2.add(FontResourcesParserCompat.toByteArrayList(arrayList.getStringArray(n)));
                    arrayList = arrayList2;
                }
            } else {
                arrayList = arrayList2;
            }
            typedArray.recycle();
        } else {
            arrayList = typedArray;
        }
        if (arrayList != null) {
            return arrayList;
        }
        return Collections.emptyList();
    }

    @Nullable
    private static FamilyResourceEntry readFamilies(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return FontResourcesParserCompat.readFamily(xmlPullParser, resources);
        }
        FontResourcesParserCompat.skip(xmlPullParser);
        return null;
    }

    @Nullable
    private static FamilyResourceEntry readFamily(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        Object object = resources.obtainAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.FontFamily);
        String string2 = object.getString(R.styleable.FontFamily_fontProviderAuthority);
        String string3 = object.getString(R.styleable.FontFamily_fontProviderPackage);
        String string4 = object.getString(R.styleable.FontFamily_fontProviderQuery);
        int n = object.getResourceId(R.styleable.FontFamily_fontProviderCerts, 0);
        int n2 = object.getInteger(R.styleable.FontFamily_fontProviderFetchStrategy, 1);
        int n3 = object.getInteger(R.styleable.FontFamily_fontProviderFetchTimeout, 500);
        object.recycle();
        if (string2 != null && string3 != null && string4 != null) {
            while (xmlPullParser.next() != 3) {
                FontResourcesParserCompat.skip(xmlPullParser);
            }
            return new ProviderResourceEntry(new FontRequest(string2, string3, string4, FontResourcesParserCompat.readCerts(resources, n)), n2, n3);
        }
        object = new ArrayList();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() != 2) continue;
            if (xmlPullParser.getName().equals("font")) {
                object.add(FontResourcesParserCompat.readFont(xmlPullParser, resources));
                continue;
            }
            FontResourcesParserCompat.skip(xmlPullParser);
        }
        if (object.isEmpty()) {
            return null;
        }
        return new FontFamilyFilesResourceEntry(object.toArray(new FontFileResourceEntry[object.size()]));
    }

    private static FontFileResourceEntry readFont(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        resources = resources.obtainAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.FontFamilyFont);
        int n = resources.getInt(R.styleable.FontFamilyFont_fontWeight, 400);
        int n2 = resources.getInt(R.styleable.FontFamilyFont_fontStyle, 0);
        boolean bl = true;
        if (1 != n2) {
            bl = false;
        }
        n2 = resources.getResourceId(R.styleable.FontFamilyFont_font, 0);
        String string2 = resources.getString(R.styleable.FontFamilyFont_font);
        resources.recycle();
        while (xmlPullParser.next() != 3) {
            FontResourcesParserCompat.skip(xmlPullParser);
        }
        return new FontFileResourceEntry(string2, n, bl, n2);
    }

    private static void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int n = 1;
        block4 : while (n > 0) {
            switch (xmlPullParser.next()) {
                default: {
                    continue block4;
                }
                case 3: {
                    --n;
                    continue block4;
                }
                case 2: 
            }
            ++n;
        }
    }

    private static List<byte[]> toByteArrayList(String[] arrstring) {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        int n = arrstring.length;
        for (int i = 0; i < n; ++i) {
            arrayList.add(Base64.decode((String)arrstring[i], (int)0));
        }
        return arrayList;
    }

    public static interface FamilyResourceEntry {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface FetchStrategy {
    }

    public static final class FontFamilyFilesResourceEntry
    implements FamilyResourceEntry {
        @NonNull
        private final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(@NonNull FontFileResourceEntry[] arrfontFileResourceEntry) {
            this.mEntries = arrfontFileResourceEntry;
        }

        @NonNull
        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }

    public static final class FontFileResourceEntry {
        @NonNull
        private final String mFileName;
        private boolean mItalic;
        private int mResourceId;
        private int mWeight;

        public FontFileResourceEntry(@NonNull String string2, int n, boolean bl, int n2) {
            this.mFileName = string2;
            this.mWeight = n;
            this.mItalic = bl;
            this.mResourceId = n2;
        }

        @NonNull
        public String getFileName() {
            return this.mFileName;
        }

        public int getResourceId() {
            return this.mResourceId;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static final class ProviderResourceEntry
    implements FamilyResourceEntry {
        @NonNull
        private final FontRequest mRequest;
        private final int mStrategy;
        private final int mTimeoutMs;

        public ProviderResourceEntry(@NonNull FontRequest fontRequest, int n, int n2) {
            this.mRequest = fontRequest;
            this.mStrategy = n;
            this.mTimeoutMs = n2;
        }

        public int getFetchStrategy() {
            return this.mStrategy;
        }

        @NonNull
        public FontRequest getRequest() {
            return this.mRequest;
        }

        public int getTimeout() {
            return this.mTimeoutMs;
        }
    }

}

