/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.pm.PackageManager
 *  android.graphics.drawable.BitmapDrawable
 *  android.util.Log
 */
package de.szalkowski.activitylauncher;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import de.szalkowski.activitylauncher.MyPackageInfo;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MyActivityInfo
implements Comparable<MyActivityInfo> {
    protected ComponentName component_name;
    protected BitmapDrawable icon;
    protected int icon_resource;
    protected String icon_resource_name;
    protected String name;

    /*
     * Exception decompiling
     */
    public MyActivityInfo(ComponentName var1_1, PackageManager var2_5) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 12[CATCHBLOCK]
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
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int compareTo(MyActivityInfo myActivityInfo) {
        int n;
        try {
            Log.d((String)"cipherName-144", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if ((n = this.name.compareTo(myActivityInfo.name)) == 0) return this.component_name.compareTo(myActivityInfo.component_name);
        return n;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        try {
            Log.d((String)"cipherName-145", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!object.getClass().equals(MyPackageInfo.class)) {
            Log.d((String)"cipherName-146", (String)Cipher.getInstance("DES").getAlgorithm());
            return false;
        }
        object = (MyActivityInfo)object;
        return this.component_name.equals((Object)object.component_name);
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return false;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ComponentName getComponentName() {
        try {
            Log.d((String)"cipherName-140", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.component_name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.component_name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.component_name;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public BitmapDrawable getIcon() {
        try {
            Log.d((String)"cipherName-141", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.icon;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icon;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icon;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getIconResouceName() {
        try {
            Log.d((String)"cipherName-143", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.icon_resource_name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icon_resource_name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icon_resource_name;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getName() {
        try {
            Log.d((String)"cipherName-142", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.name;
        }
    }
}

