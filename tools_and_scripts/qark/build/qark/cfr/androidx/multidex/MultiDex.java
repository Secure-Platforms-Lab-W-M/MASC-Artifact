/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  dalvik.system.BaseDexClassLoader
 *  dalvik.system.DexClassLoader
 *  dalvik.system.DexFile
 *  dalvik.system.PathClassLoader
 */
package androidx.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import androidx.multidex.MultiDexExtractor;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;

public final class MultiDex {
    private static final String CODE_CACHE_NAME = "code_cache";
    private static final String CODE_CACHE_SECONDARY_FOLDER_NAME = "secondary-dexes";
    private static final boolean IS_VM_MULTIDEX_CAPABLE;
    private static final int MAX_SUPPORTED_SDK_VERSION = 20;
    private static final int MIN_SDK_VERSION = 4;
    private static final String NO_KEY_PREFIX = "";
    private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
    static final String TAG = "MultiDex";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final Set<File> installedApk;

    static {
        installedApk = new HashSet<File>();
        IS_VM_MULTIDEX_CAPABLE = MultiDex.isVMMultidexCapable(System.getProperty("java.vm.version"));
    }

    private MultiDex() {
    }

    private static void clearOldDexDir(Context object) throws Exception {
        if ((object = new File(object.getFilesDir(), "secondary-dexes")).isDirectory()) {
            Object object2 = new File[]();
            object2.append("Clearing old secondary dex dir (");
            object2.append(object.getPath());
            object2.append(").");
            Log.i((String)"MultiDex", (String)object2.toString());
            object2 = object.listFiles();
            if (object2 == null) {
                object2 = new StringBuilder();
                object2.append("Failed to list secondary dex dir content (");
                object2.append(object.getPath());
                object2.append(").");
                Log.w((String)"MultiDex", (String)object2.toString());
                return;
            }
            for (Object object3 : object2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Trying to delete old file ");
                stringBuilder.append(object3.getPath());
                stringBuilder.append(" of size ");
                stringBuilder.append(object3.length());
                Log.i((String)"MultiDex", (String)stringBuilder.toString());
                if (!object3.delete()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to delete old file ");
                    stringBuilder.append(object3.getPath());
                    Log.w((String)"MultiDex", (String)stringBuilder.toString());
                    continue;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Deleted old file ");
                stringBuilder.append(object3.getPath());
                Log.i((String)"MultiDex", (String)stringBuilder.toString());
            }
            if (!object.delete()) {
                object2 = new StringBuilder();
                object2.append("Failed to delete secondary dex dir ");
                object2.append(object.getPath());
                Log.w((String)"MultiDex", (String)object2.toString());
                return;
            }
            object2 = new StringBuilder();
            object2.append("Deleted old secondary dex dir ");
            object2.append(object.getPath());
            Log.i((String)"MultiDex", (String)object2.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void doInstallation(Context object, File file, File file2, String object2, String string2, boolean bl) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException {
        Set<File> set = installedApk;
        synchronized (set) {
            IOException iOException2;
            block19 : {
                Object object3;
                if (installedApk.contains(file)) {
                    return;
                }
                installedApk.add(file);
                if (Build.VERSION.SDK_INT > 20) {
                    object3 = new StringBuilder();
                    object3.append("MultiDex is not guaranteed to work in SDK version ");
                    object3.append(Build.VERSION.SDK_INT);
                    object3.append(": SDK version higher than ");
                    object3.append(20);
                    object3.append(" should be backed by ");
                    object3.append("runtime with built-in multidex capabilty but it's not the ");
                    object3.append("case here: java.vm.version=\"");
                    object3.append(System.getProperty("java.vm.version"));
                    object3.append("\"");
                    Log.w((String)"MultiDex", (String)object3.toString());
                }
                if ((object3 = MultiDex.getDexClassloader((Context)object)) == null) {
                    return;
                }
                try {
                    MultiDex.clearOldDexDir((Context)object);
                }
                catch (Throwable throwable) {
                    Log.w((String)"MultiDex", (String)"Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", (Throwable)throwable);
                }
                file2 = MultiDex.getDexDir((Context)object, file2, (String)object2);
                object2 = new MultiDexExtractor(file, file2);
                file = null;
                try {
                    List<? extends File> list = object2.load((Context)object, string2, false);
                    try {
                        MultiDex.installSecondaryDexes((ClassLoader)object3, file2, list);
                    }
                    catch (IOException iOException2) {
                        if (!bl) break block19;
                        Log.w((String)"MultiDex", (String)"Failed to install extracted secondary dex files, retrying with forced extraction", (Throwable)iOException2);
                        MultiDex.installSecondaryDexes((ClassLoader)object3, file2, object2.load((Context)object, string2, true));
                    }
                }
                catch (Throwable throwable) {
                    try {
                        object2.close();
                        throw throwable;
                    }
                    catch (IOException iOException3) {
                        // empty catch block
                    }
                    throw throwable;
                }
                try {
                    object2.close();
                    object = file;
                }
                catch (IOException iOException4) {
                    // empty catch block
                }
                if (object != null) throw object;
                return;
            }
            throw iOException2;
        }
    }

    private static void expandFieldArray(Object object, String object2, Object[] arrobject) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        object2 = MultiDex.findField(object, (String)object2);
        Object[] arrobject2 = (Object[])object2.get(object);
        Object[] arrobject3 = (Object[])Array.newInstance(arrobject2.getClass().getComponentType(), arrobject2.length + arrobject.length);
        System.arraycopy(arrobject2, 0, arrobject3, 0, arrobject2.length);
        System.arraycopy(arrobject, 0, arrobject3, arrobject2.length, arrobject.length);
        object2.set(object, arrobject3);
    }

    private static Field findField(Object object, String string2) throws NoSuchFieldException {
        Class class_;
        for (class_ = object.getClass(); class_ != null; class_ = class_.getSuperclass()) {
            try {
                Field field = class_.getDeclaredField(string2);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        class_ = new StringBuilder();
        class_.append("Field ");
        class_.append(string2);
        class_.append(" not found in ");
        class_.append(object.getClass());
        throw new NoSuchFieldException(class_.toString());
    }

    private static /* varargs */ Method findMethod(Object object, String string2, Class<?> ... arrclass) throws NoSuchMethodException {
        Class class_;
        for (class_ = object.getClass(); class_ != null; class_ = class_.getSuperclass()) {
            try {
                Method method = class_.getDeclaredMethod(string2, arrclass);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                continue;
            }
        }
        class_ = new StringBuilder();
        class_.append("Method ");
        class_.append(string2);
        class_.append(" with parameters ");
        class_.append(Arrays.asList(arrclass));
        class_.append(" not found in ");
        class_.append(object.getClass());
        throw new NoSuchMethodException(class_.toString());
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        try {
            context = context.getApplicationInfo();
            return context;
        }
        catch (RuntimeException runtimeException) {
            Log.w((String)"MultiDex", (String)"Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", (Throwable)runtimeException);
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static ClassLoader getDexClassloader(Context object) {
        block5 : {
            block4 : {
                try {
                    object = object.getClassLoader();
                    if (Build.VERSION.SDK_INT < 14) break block4;
                }
                catch (RuntimeException runtimeException) {
                    Log.w((String)"MultiDex", (String)"Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", (Throwable)runtimeException);
                    return null;
                }
                if (object instanceof BaseDexClassLoader) {
                    return object;
                }
                break block5;
            }
            if (object instanceof DexClassLoader) return object;
            if (object instanceof PathClassLoader) {
                return object;
            }
        }
        Log.e((String)"MultiDex", (String)"Context class loader is null or not dex-capable. Must be running in test mode. Skip patching.");
        return null;
    }

    private static File getDexDir(Context object, File file, String string2) throws IOException {
        file = new File(file, "code_cache");
        try {
            MultiDex.mkdirChecked(file);
            object = file;
        }
        catch (IOException iOException) {
            object = new File(object.getFilesDir(), "code_cache");
            MultiDex.mkdirChecked((File)object);
        }
        object = new File((File)object, string2);
        MultiDex.mkdirChecked((File)object);
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void install(Context object) {
        Log.i((String)"MultiDex", (String)"Installing application");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i((String)"MultiDex", (String)"VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build.VERSION.SDK_INT < 4) {
            object = new StringBuilder();
            object.append("MultiDex installation failed. SDK ");
            object.append(Build.VERSION.SDK_INT);
            object.append(" is unsupported. Min SDK version is ");
            object.append(4);
            object.append(".");
            throw new RuntimeException(object.toString());
        }
        try {
            ApplicationInfo applicationInfo = MultiDex.getApplicationInfo((Context)object);
            if (applicationInfo == null) {
                Log.i((String)"MultiDex", (String)"No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            MultiDex.doInstallation((Context)object, new File(applicationInfo.sourceDir), new File(applicationInfo.dataDir), "secondary-dexes", "", true);
        }
        catch (Exception exception) {
            Log.e((String)"MultiDex", (String)"MultiDex installation failure", (Throwable)exception);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MultiDex installation failed (");
            stringBuilder.append(exception.getMessage());
            stringBuilder.append(").");
            throw new RuntimeException(stringBuilder.toString());
        }
        Log.i((String)"MultiDex", (String)"install done");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void installInstrumentation(Context object, Context object2) {
        Log.i((String)"MultiDex", (String)"Installing instrumentation");
        if (IS_VM_MULTIDEX_CAPABLE) {
            Log.i((String)"MultiDex", (String)"VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build.VERSION.SDK_INT < 4) {
            object = new StringBuilder();
            object.append("MultiDex installation failed. SDK ");
            object.append(Build.VERSION.SDK_INT);
            object.append(" is unsupported. Min SDK version is ");
            object.append(4);
            object.append(".");
            throw new RuntimeException(object.toString());
        }
        try {
            Object object3 = MultiDex.getApplicationInfo((Context)object);
            if (object3 == null) {
                Log.i((String)"MultiDex", (String)"No ApplicationInfo available for instrumentation, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            ApplicationInfo applicationInfo = MultiDex.getApplicationInfo((Context)object2);
            if (applicationInfo == null) {
                Log.i((String)"MultiDex", (String)"No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(object.getPackageName());
            stringBuilder.append(".");
            object = stringBuilder.toString();
            File file = new File(applicationInfo.dataDir);
            object3 = new File(object3.sourceDir);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append((String)object);
            stringBuilder2.append("secondary-dexes");
            MultiDex.doInstallation((Context)object2, (File)object3, file, stringBuilder2.toString(), (String)object, false);
            MultiDex.doInstallation((Context)object2, new File(applicationInfo.sourceDir), file, "secondary-dexes", "", false);
        }
        catch (Exception exception) {
            Log.e((String)"MultiDex", (String)"MultiDex installation failure", (Throwable)exception);
            object2 = new StringBuilder();
            object2.append("MultiDex installation failed (");
            object2.append(exception.getMessage());
            object2.append(").");
            throw new RuntimeException(object2.toString());
        }
        Log.i((String)"MultiDex", (String)"Installation done");
    }

    private static void installSecondaryDexes(ClassLoader classLoader, File file, List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, InstantiationException {
        if (!list.isEmpty()) {
            if (Build.VERSION.SDK_INT >= 19) {
                V19.install(classLoader, list, file);
                return;
            }
            if (Build.VERSION.SDK_INT >= 14) {
                V14.install(classLoader, list);
                return;
            }
            V4.install(classLoader, list);
        }
    }

    static boolean isVMMultidexCapable(String string2) {
        boolean bl;
        boolean bl2 = bl = false;
        if (string2 != null) {
            void var5_7;
            StringTokenizer stringTokenizer = new StringTokenizer(string2, ".");
            bl2 = stringTokenizer.hasMoreTokens();
            String string3 = null;
            if (bl2) {
                String stringBuilder = stringTokenizer.nextToken();
            } else {
                Object numberFormatException = null;
            }
            if (stringTokenizer.hasMoreTokens()) {
                string3 = stringTokenizer.nextToken();
            }
            bl2 = bl;
            if (var5_7 != null) {
                bl2 = bl;
                if (string3 != null) {
                    try {
                        int n = Integer.parseInt((String)var5_7);
                        int n2 = Integer.parseInt(string3);
                        bl2 = bl = true;
                        if (n <= 2) {
                            bl2 = n == 2 && n2 >= 1 ? bl : false;
                        }
                    }
                    catch (NumberFormatException numberFormatException) {
                        bl2 = bl;
                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VM with version ");
        stringBuilder.append(string2);
        string2 = bl2 ? " has multidex support" : " does not have multidex support";
        stringBuilder.append(string2);
        Log.i((String)"MultiDex", (String)stringBuilder.toString());
        return bl2;
    }

    private static void mkdirChecked(File file) throws IOException {
        file.mkdir();
        if (!file.isDirectory()) {
            File file2 = file.getParentFile();
            if (file2 == null) {
                file2 = new StringBuilder();
                file2.append("Failed to create dir ");
                file2.append(file.getPath());
                file2.append(". Parent file is null.");
                Log.e((String)"MultiDex", (String)file2.toString());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to create dir ");
                stringBuilder.append(file.getPath());
                stringBuilder.append(". parent file is a dir ");
                stringBuilder.append(file2.isDirectory());
                stringBuilder.append(", a file ");
                stringBuilder.append(file2.isFile());
                stringBuilder.append(", exists ");
                stringBuilder.append(file2.exists());
                stringBuilder.append(", readable ");
                stringBuilder.append(file2.canRead());
                stringBuilder.append(", writable ");
                stringBuilder.append(file2.canWrite());
                Log.e((String)"MultiDex", (String)stringBuilder.toString());
            }
            file2 = new StringBuilder();
            file2.append("Failed to create directory ");
            file2.append(file.getPath());
            throw new IOException(file2.toString());
        }
    }

    private static final class V14 {
        private static final int EXTRACTED_SUFFIX_LENGTH = ".zip".length();
        private final ElementConstructor elementConstructor;

        private V14() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
            ElementConstructor elementConstructor2;
            ElementConstructor elementConstructor2;
            Class class_ = Class.forName("dalvik.system.DexPathList$Element");
            try {
                elementConstructor2 = new ICSElementConstructor(class_);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                try {
                    elementConstructor2 = new JBMR11ElementConstructor(class_);
                }
                catch (NoSuchMethodException noSuchMethodException2) {
                    elementConstructor2 = new JBMR2ElementConstructor(class_);
                }
            }
            this.elementConstructor = elementConstructor2;
        }

        static void install(ClassLoader object, List<? extends File> arrobject) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            object = MultiDex.findField(object, "pathList").get(object);
            arrobject = new V14().makeDexElements((List<? extends File>)arrobject);
            try {
                MultiDex.expandFieldArray(object, "dexElements", arrobject);
                return;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.w((String)"MultiDex", (String)"Failed find field 'dexElements' attempting 'pathElements'", (Throwable)noSuchFieldException);
                MultiDex.expandFieldArray(object, "pathElements", arrobject);
                return;
            }
        }

        private Object[] makeDexElements(List<? extends File> list) throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            Object[] arrobject = new Object[list.size()];
            for (int i = 0; i < arrobject.length; ++i) {
                File file = list.get(i);
                arrobject[i] = this.elementConstructor.newInstance(file, DexFile.loadDex((String)file.getPath(), (String)V14.optimizedPathFor(file), (int)0));
            }
            return arrobject;
        }

        private static String optimizedPathFor(File object) {
            File file = object.getParentFile();
            object = object.getName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(object.substring(0, object.length() - EXTRACTED_SUFFIX_LENGTH));
            stringBuilder.append(".dex");
            return new File(file, stringBuilder.toString()).getPath();
        }

        private static interface ElementConstructor {
            public Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException;
        }

        private static class ICSElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            ICSElementConstructor(Class<?> class_) throws SecurityException, NoSuchMethodException {
                class_ = class_.getConstructor(File.class, ZipFile.class, DexFile.class);
                this.elementConstructor = class_;
                class_.setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
                return this.elementConstructor.newInstance(new Object[]{file, new ZipFile(file), dexFile});
            }
        }

        private static class JBMR11ElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR11ElementConstructor(Class<?> class_) throws SecurityException, NoSuchMethodException {
                class_ = class_.getConstructor(File.class, File.class, DexFile.class);
                this.elementConstructor = class_;
                class_.setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(new Object[]{file, file, dexFile});
            }
        }

        private static class JBMR2ElementConstructor
        implements ElementConstructor {
            private final Constructor<?> elementConstructor;

            JBMR2ElementConstructor(Class<?> class_) throws SecurityException, NoSuchMethodException {
                class_ = class_.getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
                this.elementConstructor = class_;
                class_.setAccessible(true);
            }

            @Override
            public Object newInstance(File file, DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(new Object[]{file, Boolean.FALSE, file, dexFile});
            }
        }

    }

    private static final class V19 {
        private V19() {
        }

        static void install(ClassLoader object, List<? extends File> object2, File arriOException) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
            Object object3 = MultiDex.findField(object, "pathList").get(object);
            ArrayList<IOException> arrayList = new ArrayList<IOException>();
            MultiDex.expandFieldArray(object3, "dexElements", V19.makeDexElements(object3, new ArrayList<File>((Collection<File>)object2), (File)arriOException, arrayList));
            if (arrayList.size() > 0) {
                object = arrayList.iterator();
                while (object.hasNext()) {
                    Log.w((String)"MultiDex", (String)"Exception in makeDexElement", (Throwable)((IOException)object.next()));
                }
                object2 = MultiDex.findField(object3, "dexElementsSuppressedExceptions");
                arriOException = (IOException[])object2.get(object3);
                if (arriOException == null) {
                    object = arrayList.toArray(new IOException[arrayList.size()]);
                } else {
                    object = new IOException[arrayList.size() + arriOException.length];
                    arrayList.toArray((T[])object);
                    System.arraycopy(arriOException, 0, object, arrayList.size(), arriOException.length);
                }
                object2.set(object3, object);
                object = new IOException("I/O exception during makeDexElement");
                object.initCause(arrayList.get(0));
                throw object;
            }
        }

        private static Object[] makeDexElements(Object object, ArrayList<File> arrayList, File file, ArrayList<IOException> arrayList2) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return (Object[])MultiDex.findMethod(object, "makeDexElements", new Class[]{ArrayList.class, File.class, ArrayList.class}).invoke(object, arrayList, file, arrayList2);
        }
    }

    private static final class V4 {
        private V4() {
        }

        static void install(ClassLoader classLoader, List<? extends File> object) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
            int n = object.size();
            Field field = MultiDex.findField(classLoader, "path");
            StringBuilder stringBuilder = new StringBuilder((String)field.get(classLoader));
            Object[] arrobject = new String[n];
            Object[] arrobject2 = new File[n];
            Object[] arrobject3 = new ZipFile[n];
            Object[] arrobject4 = new DexFile[n];
            object = object.listIterator();
            while (object.hasNext()) {
                Serializable serializable = (File)object.next();
                String string2 = serializable.getAbsolutePath();
                stringBuilder.append(':');
                stringBuilder.append(string2);
                n = object.previousIndex();
                arrobject[n] = string2;
                arrobject2[n] = serializable;
                arrobject3[n] = new ZipFile((File)serializable);
                serializable = new StringBuilder();
                serializable.append(string2);
                serializable.append(".dex");
                arrobject4[n] = DexFile.loadDex((String)string2, (String)serializable.toString(), (int)0);
            }
            field.set(classLoader, stringBuilder.toString());
            MultiDex.expandFieldArray(classLoader, "mPaths", arrobject);
            MultiDex.expandFieldArray(classLoader, "mFiles", arrobject2);
            MultiDex.expandFieldArray(classLoader, "mZips", arrobject3);
            MultiDex.expandFieldArray(classLoader, "mDexs", arrobject4);
        }
    }

}

