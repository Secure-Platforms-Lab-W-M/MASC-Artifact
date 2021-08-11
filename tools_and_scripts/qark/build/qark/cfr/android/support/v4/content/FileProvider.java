/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.ProviderInfo
 *  android.content.res.XmlResourceParser
 *  android.database.Cursor
 *  android.database.MatrixCursor
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Environment
 *  android.os.ParcelFileDescriptor
 *  android.text.TextUtils
 *  android.webkit.MimeTypeMap
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.GuardedBy;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider
extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy(value="sCache")
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    private static /* varargs */ File buildPath(File file, String ... arrstring) {
        for (String string2 : arrstring) {
            if (string2 == null) continue;
            file = new File(file, string2);
        }
        return file;
    }

    private static Object[] copyOf(Object[] arrobject, int n) {
        Object[] arrobject2 = new Object[n];
        System.arraycopy(arrobject, 0, arrobject2, 0, n);
        return arrobject2;
    }

    private static String[] copyOf(String[] arrstring, int n) {
        String[] arrstring2 = new String[n];
        System.arraycopy(arrstring, 0, arrstring2, 0, n);
        return arrstring2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static PathStrategy getPathStrategy(Context object, String string2) {
        HashMap<String, PathStrategy> hashMap = sCache;
        // MONITORENTER : hashMap
        PathStrategy pathStrategy = sCache.get(string2);
        if (pathStrategy != null) {
            return pathStrategy;
        }
        try {
            object = FileProvider.parsePathStrategy((Context)object, string2);
        }
        catch (XmlPullParserException xmlPullParserException) {
            throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)xmlPullParserException);
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", iOException);
        }
        sCache.put(string2, (PathStrategy)object);
        return object;
    }

    public static Uri getUriForFile(Context context, String string2, File file) {
        return FileProvider.getPathStrategy(context, string2).getUriForFile(file);
    }

    private static int modeToMode(String string2) {
        if ("r".equals(string2)) {
            return 268435456;
        }
        if (!"w".equals(string2) && !"wt".equals(string2)) {
            if ("wa".equals(string2)) {
                return 704643072;
            }
            if ("rw".equals(string2)) {
                return 939524096;
            }
            if ("rwt".equals(string2)) {
                return 1006632960;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid mode: ");
            stringBuilder.append(string2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return 738197504;
    }

    private static PathStrategy parsePathStrategy(Context context, String arrfile) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy((String)arrfile);
        XmlResourceParser xmlResourceParser = context.getPackageManager().resolveContentProvider((String)arrfile, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (xmlResourceParser != null) {
            int n;
            while ((n = xmlResourceParser.next()) != 1) {
                if (n != 2) continue;
                String string2 = xmlResourceParser.getName();
                String string3 = xmlResourceParser.getAttributeValue(null, "name");
                String string4 = xmlResourceParser.getAttributeValue(null, "path");
                File[] arrfile2 = null;
                arrfile = null;
                if ("root-path".equals(string2)) {
                    arrfile = DEVICE_ROOT;
                } else if ("files-path".equals(string2)) {
                    arrfile = context.getFilesDir();
                } else if ("cache-path".equals(string2)) {
                    arrfile = context.getCacheDir();
                } else if ("external-path".equals(string2)) {
                    arrfile = Environment.getExternalStorageDirectory();
                } else if ("external-files-path".equals(string2)) {
                    arrfile2 = ContextCompat.getExternalFilesDirs(context, null);
                    if (arrfile2.length > 0) {
                        arrfile = arrfile2[0];
                    }
                } else if ("external-cache-path".equals(string2)) {
                    arrfile = ContextCompat.getExternalCacheDirs(context);
                    arrfile = arrfile.length > 0 ? arrfile[0] : arrfile2;
                }
                if (arrfile == null) continue;
                simplePathStrategy.addRoot(string3, FileProvider.buildPath((File)arrfile, string4));
            }
            return simplePathStrategy;
        }
        throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (!providerInfo.exported) {
            if (providerInfo.grantUriPermissions) {
                this.mStrategy = FileProvider.getPathStrategy(context, providerInfo.authority);
                return;
            }
            throw new SecurityException("Provider must grant uri permissions");
        }
        throw new SecurityException("Provider must not be exported");
    }

    public int delete(Uri uri, String string2, String[] arrstring) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    public String getType(Uri object) {
        int n = (object = this.mStrategy.getFileForUri((Uri)object)).getName().lastIndexOf(46);
        if (n >= 0) {
            object = object.getName().substring(n + 1);
            object = MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)object);
            if (object != null) {
                return object;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String string2) throws FileNotFoundException {
        return ParcelFileDescriptor.open((File)this.mStrategy.getFileForUri(uri), (int)FileProvider.modeToMode(string2));
    }

    public Cursor query(Uri matrixCursor, String[] arrstring, String arrobject, String[] arrstring2, String string22) {
        matrixCursor = this.mStrategy.getFileForUri((Uri)matrixCursor);
        if (arrstring == null) {
            arrstring = COLUMNS;
        }
        arrstring2 = new String[arrstring.length];
        arrobject = new Object[arrstring.length];
        int n = 0;
        for (String string22 : arrstring) {
            if ("_display_name".equals(string22)) {
                arrstring2[n] = "_display_name";
                arrobject[n] = matrixCursor.getName();
                ++n;
                continue;
            }
            if (!"_size".equals(string22)) continue;
            arrstring2[n] = "_size";
            arrobject[n] = matrixCursor.length();
            ++n;
        }
        matrixCursor = FileProvider.copyOf(arrstring2, n);
        arrstring = FileProvider.copyOf(arrobject, n);
        matrixCursor = new MatrixCursor((String[])matrixCursor, 1);
        matrixCursor.addRow((Object[])arrstring);
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues contentValues, String string2, String[] arrstring) {
        throw new UnsupportedOperationException("No external updates");
    }

    static interface PathStrategy {
        public File getFileForUri(Uri var1);

        public Uri getUriForFile(File var1);
    }

    static class SimplePathStrategy
    implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public SimplePathStrategy(String string2) {
            this.mAuthority = string2;
        }

        public void addRoot(String string2, File file) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                try {
                    File file2 = file.getCanonicalFile();
                    this.mRoots.put(string2, file2);
                    return;
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(file);
                    throw new IllegalArgumentException(stringBuilder.toString(), iOException);
                }
            }
            throw new IllegalArgumentException("Name must not be empty");
        }

        @Override
        public File getFileForUri(Uri object) {
            Object object2 = object.getEncodedPath();
            int n = object2.indexOf(47, 1);
            Object object3 = Uri.decode((String)object2.substring(1, n));
            object2 = Uri.decode((String)object2.substring(n + 1));
            if ((object3 = this.mRoots.get(object3)) != null) {
                object = new File((File)object3, (String)object2);
                try {
                    object2 = object.getCanonicalFile();
                    if (object2.getPath().startsWith(object3.getPath())) {
                        return object2;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(object);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            object3 = new StringBuilder();
            object3.append("Unable to find configured root for ");
            object3.append(object);
            throw new IllegalArgumentException(object3.toString());
        }

        @Override
        public Uri getUriForFile(File object) {
            CharSequence charSequence22;
            try {
                CharSequence charSequence22 = object.getCanonicalPath();
                object = null;
            }
            catch (IOException iOException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(object);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            for (Map.Entry<String, File> object2 : this.mRoots.entrySet()) {
                String string2 = object2.getValue().getPath();
                if (!charSequence22.startsWith(string2) || object != null && string2.length() <= object.getValue().getPath().length()) continue;
                object = object2;
            }
            if (object != null) {
                void var2_8;
                String iOException = ((File)object.getValue()).getPath();
                if (iOException.endsWith("/")) {
                    String stringBuilder = charSequence22.substring(iOException.length());
                } else {
                    String string3 = charSequence22.substring(iOException.length() + 1);
                }
                charSequence22 = new StringBuilder();
                charSequence22.append(Uri.encode((String)object.getKey()));
                charSequence22.append('/');
                charSequence22.append(Uri.encode((String)var2_8, (String)"/"));
                object = charSequence22.toString();
                return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath((String)object).build();
            }
            object = new StringBuilder();
            object.append("Failed to find configured root that contains ");
            object.append((String)charSequence22);
            throw new IllegalArgumentException(object.toString());
        }
    }

}

