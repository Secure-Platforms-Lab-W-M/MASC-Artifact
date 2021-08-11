/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.os.Process
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public static ByteBuffer copyToDirectBuffer(Context object, Resources object2, int n) {
        block5 : {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, (Resources)object2, n);
            if (bl) break block5;
            object.delete();
            return null;
        }
        try {
            object2 = TypefaceCompatUtil.mmap((File)object);
            return object2;
        }
        finally {
            object.delete();
        }
    }

    public static boolean copyToFile(File file, Resources object, int n) {
        Object object2 = null;
        try {
            object2 = object = object.openRawResource(n);
        }
        catch (Throwable throwable) {
            TypefaceCompatUtil.closeQuietly((Closeable)object2);
            throw throwable;
        }
        boolean bl = TypefaceCompatUtil.copyToFile(file, (InputStream)object);
        TypefaceCompatUtil.closeQuietly((Closeable)object);
        return bl;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean copyToFile(File object, InputStream object2) {
        Throwable throwable222;
        Object object3 = null;
        Object object4 = null;
        StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskWrites();
        object4 = object = new FileOutputStream((File)object, false);
        object3 = object;
        byte[] arrby = new byte[1024];
        do {
            object4 = object;
            object3 = object;
            int n = object2.read(arrby);
            if (n == -1) break;
            object4 = object;
            object3 = object;
            object.write(arrby, 0, n);
        } while (true);
        TypefaceCompatUtil.closeQuietly((Closeable)object);
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        return true;
        {
            catch (Throwable throwable222) {
            }
            catch (IOException iOException) {}
            object4 = object3;
            {
                object2 = new StringBuilder();
                object4 = object3;
                object2.append("Error copying resource contents to temp file: ");
                object4 = object3;
                object2.append(iOException.getMessage());
                object4 = object3;
                Log.e((String)"TypefaceCompatUtil", (String)object2.toString());
            }
            TypefaceCompatUtil.closeQuietly((Closeable)object3);
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            return false;
        }
        TypefaceCompatUtil.closeQuietly((Closeable)object4);
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
        throw throwable222;
    }

    public static File getTempFile(Context object) {
        if ((object = object.getCacheDir()) == null) {
            return null;
        }
        CharSequence charSequence = new StringBuilder();
        charSequence.append(".font");
        charSequence.append(Process.myPid());
        charSequence.append("-");
        charSequence.append(Process.myTid());
        charSequence.append("-");
        charSequence = charSequence.toString();
        for (int i = 0; i < 100; ++i) {
            Serializable serializable = new StringBuilder();
            serializable.append((String)charSequence);
            serializable.append(i);
            serializable = new File((File)object, serializable.toString());
            try {
                boolean bl = serializable.createNewFile();
                if (!bl) continue;
                return serializable;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    public static ByteBuffer mmap(Context var0, CancellationSignal var1_3, Uri var2_6) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 6 blocks at once
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:397)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:475)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private static ByteBuffer mmap(File var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:397)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:475)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }
}

