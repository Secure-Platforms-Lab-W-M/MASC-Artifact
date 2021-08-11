// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.multidex;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import java.lang.reflect.Constructor;
import dalvik.system.DexFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.pm.ApplicationInfo;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.io.IOException;
import android.os.Build$VERSION;
import android.util.Log;
import android.content.Context;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.io.File;
import java.util.Set;

public final class MultiDex
{
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
        IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
    }
    
    private MultiDex() {
    }
    
    private static void clearOldDexDir(final Context context) throws Exception {
        final File file = new File(context.getFilesDir(), "secondary-dexes");
        if (!file.isDirectory()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Clearing old secondary dex dir (");
        sb.append(file.getPath());
        sb.append(").");
        Log.i("MultiDex", sb.toString());
        final File[] listFiles = file.listFiles();
        if (listFiles == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to list secondary dex dir content (");
            sb2.append(file.getPath());
            sb2.append(").");
            Log.w("MultiDex", sb2.toString());
            return;
        }
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file2 = listFiles[i];
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Trying to delete old file ");
            sb3.append(file2.getPath());
            sb3.append(" of size ");
            sb3.append(file2.length());
            Log.i("MultiDex", sb3.toString());
            if (!file2.delete()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Failed to delete old file ");
                sb4.append(file2.getPath());
                Log.w("MultiDex", sb4.toString());
            }
            else {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Deleted old file ");
                sb5.append(file2.getPath());
                Log.i("MultiDex", sb5.toString());
            }
        }
        if (!file.delete()) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Failed to delete secondary dex dir ");
            sb6.append(file.getPath());
            Log.w("MultiDex", sb6.toString());
            return;
        }
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("Deleted old secondary dex dir ");
        sb7.append(file.getPath());
        Log.i("MultiDex", sb7.toString());
    }
    
    private static void doInstallation(Context context, File file, File dexDir, String s, final String s2, final boolean b) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException {
        synchronized (MultiDex.installedApk) {
            if (MultiDex.installedApk.contains(file)) {
                return;
            }
            MultiDex.installedApk.add(file);
            if (Build$VERSION.SDK_INT > 20) {
                final StringBuilder sb = new StringBuilder();
                sb.append("MultiDex is not guaranteed to work in SDK version ");
                sb.append(Build$VERSION.SDK_INT);
                sb.append(": SDK version higher than ");
                sb.append(20);
                sb.append(" should be backed by ");
                sb.append("runtime with built-in multidex capabilty but it's not the ");
                sb.append("case here: java.vm.version=\"");
                sb.append(System.getProperty("java.vm.version"));
                sb.append("\"");
                Log.w("MultiDex", sb.toString());
            }
            try {
                final ClassLoader classLoader = context.getClassLoader();
                if (classLoader == null) {
                    Log.e("MultiDex", "Context class loader is null. Must be running in test mode. Skip patching.");
                    return;
                }
                try {
                    clearOldDexDir(context);
                }
                catch (Throwable t) {
                    Log.w("MultiDex", "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", t);
                }
                dexDir = getDexDir(context, dexDir, s);
                s = (String)new MultiDexExtractor(file, dexDir);
                file = null;
                try {
                    final List<? extends File> load = ((MultiDexExtractor)s).load(context, s2, false);
                    try {
                        installSecondaryDexes(classLoader, dexDir, load);
                    }
                    catch (IOException ex) {
                        if (!b) {
                            throw ex;
                        }
                        Log.w("MultiDex", "Failed to install extracted secondary dex files, retrying with forced extraction", (Throwable)ex);
                        installSecondaryDexes(classLoader, dexDir, ((MultiDexExtractor)s).load(context, s2, true));
                    }
                    try {
                        ((MultiDexExtractor)s).close();
                        context = (Context)file;
                    }
                    catch (IOException ex3) {}
                    if (context == null) {
                        return;
                    }
                    throw context;
                }
                finally {
                    try {
                        ((MultiDexExtractor)s).close();
                    }
                    catch (IOException ex4) {}
                }
            }
            catch (RuntimeException ex2) {
                Log.w("MultiDex", "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", (Throwable)ex2);
            }
        }
    }
    
    private static void expandFieldArray(final Object o, final String s, final Object[] array) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final Field field = findField(o, s);
        final Object[] array2 = (Object[])field.get(o);
        final Object[] array3 = (Object[])Array.newInstance(array2.getClass().getComponentType(), array2.length + array.length);
        System.arraycopy(array2, 0, array3, 0, array2.length);
        System.arraycopy(array, 0, array3, array2.length, array.length);
        field.set(o, array3);
    }
    
    private static Field findField(final Object o, final String s) throws NoSuchFieldException {
        Class<?> clazz = o.getClass();
        while (clazz != null) {
            try {
                final Field declaredField = clazz.getDeclaredField(s);
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                    return declaredField;
                }
                return declaredField;
            }
            catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field ");
        sb.append(s);
        sb.append(" not found in ");
        sb.append(o.getClass());
        throw new NoSuchFieldException(sb.toString());
    }
    
    private static Method findMethod(final Object o, final String s, final Class<?>... array) throws NoSuchMethodException {
        Class<?> clazz = o.getClass();
        while (clazz != null) {
            try {
                final Method declaredMethod = clazz.getDeclaredMethod(s, array);
                if (!declaredMethod.isAccessible()) {
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                }
                return declaredMethod;
            }
            catch (NoSuchMethodException ex) {
                clazz = clazz.getSuperclass();
                continue;
            }
            break;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Method ");
        sb.append(s);
        sb.append(" with parameters ");
        sb.append(Arrays.asList(array));
        sb.append(" not found in ");
        sb.append(o.getClass());
        throw new NoSuchMethodException(sb.toString());
    }
    
    private static ApplicationInfo getApplicationInfo(final Context context) {
        try {
            return context.getApplicationInfo();
        }
        catch (RuntimeException ex) {
            Log.w("MultiDex", "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", (Throwable)ex);
            return null;
        }
    }
    
    private static File getDexDir(Context context, File file, final String s) throws IOException {
        file = new File(file, "code_cache");
        try {
            mkdirChecked(file);
            context = (Context)file;
        }
        catch (IOException ex) {
            context = (Context)new File(context.getFilesDir(), "code_cache");
            mkdirChecked((File)context);
        }
        final File file2 = new File((File)context, s);
        mkdirChecked(file2);
        return file2;
    }
    
    public static void install(final Context context) {
        Log.i("MultiDex", "Installing application");
        if (MultiDex.IS_VM_MULTIDEX_CAPABLE) {
            Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build$VERSION.SDK_INT >= 4) {
            try {
                final ApplicationInfo applicationInfo = getApplicationInfo(context);
                if (applicationInfo == null) {
                    Log.i("MultiDex", "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                doInstallation(context, new File(applicationInfo.sourceDir), new File(applicationInfo.dataDir), "secondary-dexes", "", true);
                Log.i("MultiDex", "install done");
                return;
            }
            catch (Exception ex) {
                Log.e("MultiDex", "MultiDex installation failure", (Throwable)ex);
                final StringBuilder sb = new StringBuilder();
                sb.append("MultiDex installation failed (");
                sb.append(ex.getMessage());
                sb.append(").");
                throw new RuntimeException(sb.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("MultiDex installation failed. SDK ");
        sb2.append(Build$VERSION.SDK_INT);
        sb2.append(" is unsupported. Min SDK version is ");
        sb2.append(4);
        sb2.append(".");
        throw new RuntimeException(sb2.toString());
    }
    
    public static void installInstrumentation(final Context context, final Context context2) {
        Log.i("MultiDex", "Installing instrumentation");
        if (MultiDex.IS_VM_MULTIDEX_CAPABLE) {
            Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
            return;
        }
        if (Build$VERSION.SDK_INT >= 4) {
            try {
                final ApplicationInfo applicationInfo = getApplicationInfo(context);
                if (applicationInfo == null) {
                    Log.i("MultiDex", "No ApplicationInfo available for instrumentation, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                final ApplicationInfo applicationInfo2 = getApplicationInfo(context2);
                if (applicationInfo2 == null) {
                    Log.i("MultiDex", "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(context.getPackageName());
                sb.append(".");
                final String string = sb.toString();
                final File file = new File(applicationInfo2.dataDir);
                final File file2 = new File(applicationInfo.sourceDir);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("secondary-dexes");
                doInstallation(context2, file2, file, sb2.toString(), string, false);
                doInstallation(context2, new File(applicationInfo2.sourceDir), file, "secondary-dexes", "", false);
                Log.i("MultiDex", "Installation done");
                return;
            }
            catch (Exception ex) {
                Log.e("MultiDex", "MultiDex installation failure", (Throwable)ex);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("MultiDex installation failed (");
                sb3.append(ex.getMessage());
                sb3.append(").");
                throw new RuntimeException(sb3.toString());
            }
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("MultiDex installation failed. SDK ");
        sb4.append(Build$VERSION.SDK_INT);
        sb4.append(" is unsupported. Min SDK version is ");
        sb4.append(4);
        sb4.append(".");
        throw new RuntimeException(sb4.toString());
    }
    
    private static void installSecondaryDexes(final ClassLoader classLoader, final File file, final List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, InstantiationException {
        if (list.isEmpty()) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            V19.install(classLoader, list, file);
            return;
        }
        if (Build$VERSION.SDK_INT >= 14) {
            V14.install(classLoader, list);
            return;
        }
        V4.install(classLoader, list);
    }
    
    static boolean isVMMultidexCapable(String s) {
        boolean b = false;
        if (s != null) {
            final Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(s);
            if (matcher.matches()) {
                final boolean b2 = true;
                try {
                    final int int1 = Integer.parseInt(matcher.group(1));
                    final int int2 = Integer.parseInt(matcher.group(2));
                    b = b2;
                    if (int1 <= 2) {
                        b = (int1 == 2 && int2 >= 1 && b2);
                    }
                }
                catch (NumberFormatException ex) {}
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("VM with version ");
        sb.append(s);
        if (b) {
            s = " has multidex support";
        }
        else {
            s = " does not have multidex support";
        }
        sb.append(s);
        Log.i("MultiDex", sb.toString());
        return b;
    }
    
    private static void mkdirChecked(final File file) throws IOException {
        file.mkdir();
        if (!file.isDirectory()) {
            final File parentFile = file.getParentFile();
            if (parentFile == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to create dir ");
                sb.append(file.getPath());
                sb.append(". Parent file is null.");
                Log.e("MultiDex", sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to create dir ");
                sb2.append(file.getPath());
                sb2.append(". parent file is a dir ");
                sb2.append(parentFile.isDirectory());
                sb2.append(", a file ");
                sb2.append(parentFile.isFile());
                sb2.append(", exists ");
                sb2.append(parentFile.exists());
                sb2.append(", readable ");
                sb2.append(parentFile.canRead());
                sb2.append(", writable ");
                sb2.append(parentFile.canWrite());
                Log.e("MultiDex", sb2.toString());
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to create directory ");
            sb3.append(file.getPath());
            throw new IOException(sb3.toString());
        }
    }
    
    private static final class V14
    {
        private static final int EXTRACTED_SUFFIX_LENGTH;
        private final ElementConstructor elementConstructor;
        
        static {
            EXTRACTED_SUFFIX_LENGTH = ".zip".length();
        }
        
        private V14() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
            final Class<?> forName = Class.forName("dalvik.system.DexPathList$Element");
            ElementConstructor elementConstructor;
            try {
                elementConstructor = new ICSElementConstructor(forName);
            }
            catch (NoSuchMethodException ex) {
                try {
                    elementConstructor = new JBMR11ElementConstructor(forName);
                }
                catch (NoSuchMethodException ex2) {
                    elementConstructor = new JBMR2ElementConstructor(forName);
                }
            }
            this.elementConstructor = elementConstructor;
        }
        
        static void install(ClassLoader value, List<? extends File> dexElements) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            value = (ClassLoader)findField(value, "pathList").get(value);
            dexElements = new V14().makeDexElements((List<? extends File>)dexElements);
            try {
                expandFieldArray(value, "dexElements", dexElements);
            }
            catch (NoSuchFieldException ex) {
                Log.w("MultiDex", "Failed find field 'dexElements' attempting 'pathElements'", (Throwable)ex);
                expandFieldArray(value, "pathElements", dexElements);
            }
        }
        
        private Object[] makeDexElements(final List<? extends File> list) throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            final Object[] array = new Object[list.size()];
            for (int i = 0; i < array.length; ++i) {
                final File file = (File)list.get(i);
                array[i] = this.elementConstructor.newInstance(file, DexFile.loadDex(file.getPath(), optimizedPathFor(file), 0));
            }
            return array;
        }
        
        private static String optimizedPathFor(final File file) {
            final File parentFile = file.getParentFile();
            final String name = file.getName();
            final StringBuilder sb = new StringBuilder();
            sb.append(name.substring(0, name.length() - V14.EXTRACTED_SUFFIX_LENGTH));
            sb.append(".dex");
            return new File(parentFile, sb.toString()).getPath();
        }
        
        private interface ElementConstructor
        {
            Object newInstance(final File p0, final DexFile p1) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException;
        }
        
        private static class ICSElementConstructor implements ElementConstructor
        {
            private final Constructor<?> elementConstructor;
            
            ICSElementConstructor(final Class<?> clazz) throws SecurityException, NoSuchMethodException {
                (this.elementConstructor = clazz.getConstructor(File.class, ZipFile.class, DexFile.class)).setAccessible(true);
            }
            
            @Override
            public Object newInstance(final File file, final DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
                return this.elementConstructor.newInstance(file, new ZipFile(file), dexFile);
            }
        }
        
        private static class JBMR11ElementConstructor implements ElementConstructor
        {
            private final Constructor<?> elementConstructor;
            
            JBMR11ElementConstructor(final Class<?> clazz) throws SecurityException, NoSuchMethodException {
                (this.elementConstructor = clazz.getConstructor(File.class, File.class, DexFile.class)).setAccessible(true);
            }
            
            @Override
            public Object newInstance(final File file, final DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(file, file, dexFile);
            }
        }
        
        private static class JBMR2ElementConstructor implements ElementConstructor
        {
            private final Constructor<?> elementConstructor;
            
            JBMR2ElementConstructor(final Class<?> clazz) throws SecurityException, NoSuchMethodException {
                (this.elementConstructor = clazz.getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class)).setAccessible(true);
            }
            
            @Override
            public Object newInstance(final File file, final DexFile dexFile) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return this.elementConstructor.newInstance(file, Boolean.FALSE, file, dexFile);
            }
        }
    }
    
    private static final class V19
    {
        static void install(final ClassLoader classLoader, final List<? extends File> list, final File file) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
            final Object value = findField(classLoader, "pathList").get(classLoader);
            final ArrayList<IOException> list2 = new ArrayList<IOException>();
            expandFieldArray(value, "dexElements", makeDexElements(value, new ArrayList<File>(list), file, list2));
            if (list2.size() > 0) {
                final Iterator<IOException> iterator = list2.iterator();
                while (iterator.hasNext()) {
                    Log.w("MultiDex", "Exception in makeDexElement", (Throwable)iterator.next());
                }
                final Field access$000 = findField(value, "dexElementsSuppressedExceptions");
                final IOException[] array = (IOException[])access$000.get(value);
                IOException[] array2;
                if (array == null) {
                    array2 = list2.toArray(new IOException[list2.size()]);
                }
                else {
                    array2 = new IOException[list2.size() + array.length];
                    list2.toArray(array2);
                    System.arraycopy(array, 0, array2, list2.size(), array.length);
                }
                access$000.set(value, array2);
                final IOException ex = new IOException("I/O exception during makeDexElement");
                ex.initCause(list2.get(0));
                throw ex;
            }
        }
        
        private static Object[] makeDexElements(final Object o, final ArrayList<File> list, final File file, final ArrayList<IOException> list2) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return (Object[])findMethod(o, "makeDexElements", (Class<?>[])new Class[] { ArrayList.class, File.class, ArrayList.class }).invoke(o, list, file, list2);
        }
    }
    
    private static final class V4
    {
        static void install(final ClassLoader classLoader, final List<? extends File> list) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
            final int size = list.size();
            final Field access$000 = findField(classLoader, "path");
            final StringBuilder sb = new StringBuilder((String)access$000.get(classLoader));
            final String[] array = new String[size];
            final File[] array2 = new File[size];
            final ZipFile[] array3 = new ZipFile[size];
            final DexFile[] array4 = new DexFile[size];
            final ListIterator<? extends File> listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                final File file = (File)listIterator.next();
                final String absolutePath = file.getAbsolutePath();
                sb.append(':');
                sb.append(absolutePath);
                final int previousIndex = listIterator.previousIndex();
                array[previousIndex] = absolutePath;
                array2[previousIndex] = file;
                array3[previousIndex] = new ZipFile(file);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(absolutePath);
                sb2.append(".dex");
                array4[previousIndex] = DexFile.loadDex(absolutePath, sb2.toString(), 0);
            }
            access$000.set(classLoader, sb.toString());
            expandFieldArray(classLoader, "mPaths", array);
            expandFieldArray(classLoader, "mFiles", array2);
            expandFieldArray(classLoader, "mZips", array3);
            expandFieldArray(classLoader, "mDexs", array4);
        }
    }
}
