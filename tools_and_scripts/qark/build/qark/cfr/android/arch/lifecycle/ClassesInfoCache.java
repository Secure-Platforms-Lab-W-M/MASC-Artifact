/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ClassesInfoCache {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance = new ClassesInfoCache();
    private final Map<Class, CallbackInfo> mCallbackMap = new HashMap<Class, CallbackInfo>();
    private final Map<Class, Boolean> mHasLifecycleMethods = new HashMap<Class, Boolean>();

    ClassesInfoCache() {
    }

    /*
     * Enabled aggressive block sorting
     */
    private CallbackInfo createInfo(Class class_, @Nullable Method[] object) {
        int n;
        Object object2 = class_.getSuperclass();
        HashMap<MethodReference, Lifecycle.Event> hashMap = new HashMap<MethodReference, Lifecycle.Event>();
        if (object2 != null && (object2 = this.getInfo((Class)object2)) != null) {
            hashMap.putAll(object2.mHandlerToEvent);
        }
        object2 = class_.getInterfaces();
        int n2 = object2.length;
        for (n = 0; n < n2; ++n) {
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : this.getInfo((Class)object2[n]).mHandlerToEvent.entrySet()) {
                this.verifyAndPutHandler(hashMap, entry.getKey(), entry.getValue(), class_);
            }
        }
        if (object == null) {
            object = this.getDeclaredMethods(class_);
        }
        boolean bl = false;
        int n3 = object.length;
        n2 = 0;
        do {
            if (n2 >= n3) {
                object = new CallbackInfo(hashMap);
                this.mCallbackMap.put(class_, (CallbackInfo)object);
                this.mHasLifecycleMethods.put(class_, bl);
                return object;
            }
            object2 = object[n2];
            OnLifecycleEvent onLifecycleEvent = object2.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                bl = true;
                Class<?>[] arrclass = object2.getParameterTypes();
                n = 0;
                if (arrclass.length > 0) {
                    n = 1;
                    if (!arrclass[0].isAssignableFrom(LifecycleOwner.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                }
                Lifecycle.Event event = onLifecycleEvent.value();
                if (arrclass.length > 1) {
                    n = 2;
                    if (!arrclass[1].isAssignableFrom(Lifecycle.Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (event != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                }
                if (arrclass.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                this.verifyAndPutHandler(hashMap, new MethodReference(n, (Method)object2), event, class_);
            }
            ++n2;
        } while (true);
    }

    private Method[] getDeclaredMethods(Class arrmethod) {
        try {
            arrmethod = arrmethod.getDeclaredMethods();
            return arrmethod;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", noClassDefFoundError);
        }
    }

    private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> object, MethodReference methodReference, Lifecycle.Event event, Class class_) {
        Lifecycle.Event event2 = object.get(methodReference);
        if (event2 != null && event != event2) {
            object = methodReference.mMethod;
            throw new IllegalArgumentException("Method " + object.getName() + " in " + class_.getName() + " already declared with different @OnLifecycleEvent value: previous" + " value " + (Object)((Object)event2) + ", new value " + (Object)((Object)event));
        }
        if (event2 == null) {
            object.put((MethodReference)methodReference, (Lifecycle.Event)event);
        }
    }

    CallbackInfo getInfo(Class class_) {
        CallbackInfo callbackInfo = this.mCallbackMap.get(class_);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return this.createInfo(class_, null);
    }

    boolean hasLifecycleMethods(Class class_) {
        if (this.mHasLifecycleMethods.containsKey(class_)) {
            return this.mHasLifecycleMethods.get(class_);
        }
        Method[] arrmethod = this.getDeclaredMethods(class_);
        int n = arrmethod.length;
        for (int i = 0; i < n; ++i) {
            if (arrmethod[i].getAnnotation(OnLifecycleEvent.class) == null) continue;
            this.createInfo(class_, arrmethod);
            return true;
        }
        this.mHasLifecycleMethods.put(class_, false);
        return false;
    }

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> arrayList) {
            this.mHandlerToEvent = arrayList;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : arrayList.entrySet()) {
                Lifecycle.Event event = entry.getValue();
                List<MethodReference> list = this.mEventToHandlers.get((Object)event);
                arrayList = list;
                if (list == null) {
                    arrayList = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, arrayList);
                }
                arrayList.add(entry.getKey());
            }
        }

        private static void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    list.get(i).invokeCallback(lifecycleOwner, event, object);
                }
            }
        }

        void invokeCallbacks(LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)event), lifecycleOwner, event, object);
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)Lifecycle.Event.ON_ANY), lifecycleOwner, event, object);
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int n, Method method) {
            this.mCallType = n;
            this.mMethod = method;
            this.mMethod.setAccessible(true);
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block5 : {
                block4 : {
                    if (this == object) break block4;
                    if (object == null || this.getClass() != object.getClass()) {
                        return false;
                    }
                    object = (MethodReference)object;
                    if (this.mCallType != object.mCallType || !this.mMethod.getName().equals(object.mMethod.getName())) break block5;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.mCallType * 31 + this.mMethod.getName().hashCode();
        }

        /*
         * Exception decompiling
         */
        void invokeCallback(LifecycleOwner var1_1, Lifecycle.Event var2_4, Object var3_5) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
            // org.benf.cfr.reader.Main.doJar(Main.java:134)
            // org.benf.cfr.reader.Main.main(Main.java:189)
            throw new IllegalStateException("Decompilation failed");
        }
    }

}

