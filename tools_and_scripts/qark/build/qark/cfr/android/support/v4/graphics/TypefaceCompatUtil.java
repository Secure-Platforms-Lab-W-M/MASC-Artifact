/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.os.Process
 *  android.util.Log
 */
package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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
                return;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=19)
    public static ByteBuffer copyToDirectBuffer(Context object, Resources resources, int n) {
        File file;
        block6 : {
            file = TypefaceCompatUtil.getTempFile((Context)object);
            object = null;
            if (file == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile(file, resources, n);
            if (bl) break block6;
            do {
                return object;
                break;
            } while (true);
        }
        try {
            object = TypefaceCompatUtil.mmap(file);
            return object;
        }
        finally {
            file.delete();
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
            return false;
        }
        TypefaceCompatUtil.closeQuietly((Closeable)object4);
        throw throwable222;
    }

    public static File getTempFile(Context context) {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(".font");
        charSequence.append(Process.myPid());
        charSequence.append("-");
        charSequence.append(Process.myTid());
        charSequence.append("-");
        charSequence = charSequence.toString();
        for (int i = 0; i < 100; ++i) {
            File file = context.getCacheDir();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(i);
            file = new File(file, stringBuilder.toString());
            try {
                boolean bl = file.createNewFile();
                if (!bl) continue;
                return file;
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
    @RequiresApi(value=19)
    public static ByteBuffer mmap(Context var0, CancellationSignal var1_7, Uri var2_10) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [6[TRYBLOCK]], but top level block is 15[CATCHBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
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
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=19)
    private static ByteBuffer mmap(File var0) {
        try {
            var4_5 = new FileInputStream((File)var0);
        }
        catch (IOException var0_4) {
            return null;
        }
        var0 = var4_5.getChannel();
        var1_7 = var0.size();
        var0 = var0.map(FileChannel.MapMode.READ_ONLY, 0L, var1_7);
        var4_5.close();
        return var0;
        catch (Throwable var0_1) {
            var3_8 = null;
        }
        catch (Throwable var3_9) {
            try {
                throw var3_9;
            }
            catch (Throwable var0_2) {
                // empty catch block
            }
        }
        if (var3_8 == null) ** GOTO lbl28
        try {
            var4_5.close();
            throw var0_3;
        }
        catch (Throwable var4_6) {
            var3_8.addSuppressed(var4_6);
            throw var0_3;
        }
lbl28: // 1 sources:
        var4_5.close();
        throw var0_3;
    }
}

