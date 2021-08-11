/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorSet
 *  android.animation.Keyframe
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.TypedValue
 *  android.util.Xml
 *  android.view.InflateException
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AndroidResources;
import android.support.graphics.drawable.AnimationUtilsCompat;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    private static Animator createAnimatorFromXml(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, float f) throws XmlPullParserException, IOException {
        return AnimatorInflaterCompat.createAnimatorFromXml(context, resources, theme, xmlPullParser, Xml.asAttributeSet((XmlPullParser)xmlPullParser), null, 0, f);
    }

    private static Animator createAnimatorFromXml(Context object, Resources object2, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet, AnimatorSet animatorSet, int n, float f) throws XmlPullParserException, IOException {
        int n2;
        int n3 = xmlPullParser.getDepth();
        ObjectAnimator objectAnimator = null;
        Object object3 = null;
        while ((n2 = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n3) {
            block15 : {
                PropertyValuesHolder[] arrpropertyValuesHolder;
                block12 : {
                    block14 : {
                        block13 : {
                            block11 : {
                                if (n2 == 1) break;
                                if (n2 != 2) continue;
                                arrpropertyValuesHolder = xmlPullParser.getName();
                                n2 = 0;
                                if (!arrpropertyValuesHolder.equals("objectAnimator")) break block11;
                                objectAnimator = AnimatorInflaterCompat.loadObjectAnimator((Context)object, (Resources)object2, theme, attributeSet, f, xmlPullParser);
                                break block12;
                            }
                            if (!arrpropertyValuesHolder.equals("animator")) break block13;
                            objectAnimator = AnimatorInflaterCompat.loadAnimator((Context)object, (Resources)object2, theme, attributeSet, null, f, xmlPullParser);
                            break block12;
                        }
                        if (!arrpropertyValuesHolder.equals("set")) break block14;
                        objectAnimator = new AnimatorSet();
                        arrpropertyValuesHolder = TypedArrayUtils.obtainAttributes((Resources)object2, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
                        int n4 = TypedArrayUtils.getNamedInt((TypedArray)arrpropertyValuesHolder, xmlPullParser, "ordering", 0, 0);
                        AnimatorInflaterCompat.createAnimatorFromXml((Context)object, (Resources)object2, theme, xmlPullParser, attributeSet, (AnimatorSet)objectAnimator, n4, f);
                        arrpropertyValuesHolder.recycle();
                        break block12;
                    }
                    if (!arrpropertyValuesHolder.equals("propertyValuesHolder")) break block15;
                    arrpropertyValuesHolder = AnimatorInflaterCompat.loadValues((Context)object, (Resources)object2, theme, xmlPullParser, Xml.asAttributeSet((XmlPullParser)xmlPullParser));
                    if (arrpropertyValuesHolder != null && objectAnimator != null && objectAnimator instanceof ValueAnimator) {
                        ((ValueAnimator)objectAnimator).setValues(arrpropertyValuesHolder);
                    }
                    n2 = 1;
                }
                arrpropertyValuesHolder = object3;
                if (animatorSet != null) {
                    arrpropertyValuesHolder = object3;
                    if (n2 == 0) {
                        arrpropertyValuesHolder = object3;
                        if (object3 == null) {
                            arrpropertyValuesHolder = new ArrayList();
                        }
                        arrpropertyValuesHolder.add(objectAnimator);
                    }
                }
                object3 = arrpropertyValuesHolder;
                continue;
            }
            object = new StringBuilder();
            object.append("Unknown animator name: ");
            object.append(xmlPullParser.getName());
            throw new RuntimeException(object.toString());
        }
        if (animatorSet != null && object3 != null) {
            object = new Animator[object3.size()];
            n2 = 0;
            object2 = object3.iterator();
            while (object2.hasNext()) {
                object[n2] = (Animator)object2.next();
                ++n2;
            }
            if (n == 0) {
                animatorSet.playTogether((Animator[])object);
                return objectAnimator;
            }
            animatorSet.playSequentially((Animator[])object);
        }
        return objectAnimator;
    }

    private static Keyframe createNewKeyframe(Keyframe keyframe, float f) {
        if (keyframe.getType() == Float.TYPE) {
            return Keyframe.ofFloat((float)f);
        }
        if (keyframe.getType() == Integer.TYPE) {
            return Keyframe.ofInt((float)f);
        }
        return Keyframe.ofObject((float)f);
    }

    private static void distributeKeyframes(Keyframe[] arrkeyframe, float f, int n, int n2) {
        f /= (float)(n2 - n + 2);
        while (n <= n2) {
            arrkeyframe[n].setFraction(arrkeyframe[n - 1].getFraction() + f);
            ++n;
        }
    }

    private static void dumpKeyframes(Object[] arrobject, String object) {
        if (arrobject != null) {
            if (arrobject.length == 0) {
                return;
            }
            Log.d((String)"AnimatorInflater", (String)object);
            int n = arrobject.length;
            for (int i = 0; i < n; ++i) {
                Keyframe keyframe = (Keyframe)arrobject[i];
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Keyframe ");
                stringBuilder.append(i);
                stringBuilder.append(": fraction ");
                float f = keyframe.getFraction();
                String string2 = "null";
                object = f < 0.0f ? "null" : Float.valueOf(keyframe.getFraction());
                stringBuilder.append(object);
                stringBuilder.append(", ");
                stringBuilder.append(", value : ");
                object = string2;
                if (keyframe.hasValue()) {
                    object = keyframe.getValue();
                }
                stringBuilder.append(object);
                Log.d((String)"AnimatorInflater", (String)stringBuilder.toString());
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static PropertyValuesHolder getPVH(TypedArray object, int n, int n2, int n3, String charSequence) {
        Object object2 = object.peekValue(n2);
        boolean bl = object2 != null;
        int n4 = bl ? object2.type : 0;
        object2 = object.peekValue(n3);
        boolean bl2 = object2 != null;
        int n5 = bl2 ? object2.type : 0;
        if (n == 4) {
            n = bl && AnimatorInflaterCompat.isColorType(n4) || bl2 && AnimatorInflaterCompat.isColorType(n5) ? 3 : 0;
        }
        boolean bl3 = n == 0;
        if (n == 2) {
            object2 = object.getString(n2);
            object = object.getString(n3);
            PathParser.PathDataNode[] arrpathDataNode = PathParser.createNodesFromPathData((String)object2);
            PathParser.PathDataNode[] arrpathDataNode2 = PathParser.createNodesFromPathData((String)object);
            if (arrpathDataNode == null) {
                if (arrpathDataNode2 == null) return null;
            }
            if (arrpathDataNode != null) {
                PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
                if (arrpathDataNode2 == null) return PropertyValuesHolder.ofObject((String)charSequence, (TypeEvaluator)pathDataEvaluator, (Object[])new Object[]{arrpathDataNode});
                if (PathParser.canMorph(arrpathDataNode, arrpathDataNode2)) {
                    return PropertyValuesHolder.ofObject((String)charSequence, (TypeEvaluator)pathDataEvaluator, (Object[])new Object[]{arrpathDataNode, arrpathDataNode2});
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" Can't morph from ");
                stringBuilder.append((String)object2);
                stringBuilder.append(" to ");
                stringBuilder.append((String)object);
                throw new InflateException(stringBuilder.toString());
            }
            if (arrpathDataNode2 == null) return null;
            return PropertyValuesHolder.ofObject((String)charSequence, (TypeEvaluator)new PathDataEvaluator(), (Object[])new Object[]{arrpathDataNode2});
        }
        object2 = null;
        if (n == 3) {
            object2 = ArgbEvaluator.getInstance();
        }
        if (bl3) {
            if (bl) {
                float f = n4 == 5 ? object.getDimension(n2, 0.0f) : object.getFloat(n2, 0.0f);
                if (bl2) {
                    float f2 = n5 == 5 ? object.getDimension(n3, 0.0f) : object.getFloat(n3, 0.0f);
                    object = PropertyValuesHolder.ofFloat((String)charSequence, (float[])new float[]{f, f2});
                } else {
                    object = PropertyValuesHolder.ofFloat((String)charSequence, (float[])new float[]{f});
                }
            } else {
                float f = n5 == 5 ? object.getDimension(n3, 0.0f) : object.getFloat(n3, 0.0f);
                object = PropertyValuesHolder.ofFloat((String)charSequence, (float[])new float[]{f});
            }
        } else if (bl) {
            n = n4 == 5 ? (int)object.getDimension(n2, 0.0f) : (AnimatorInflaterCompat.isColorType(n4) ? object.getColor(n2, 0) : object.getInt(n2, 0));
            if (bl2) {
                n2 = n5 == 5 ? (int)object.getDimension(n3, 0.0f) : (AnimatorInflaterCompat.isColorType(n5) ? object.getColor(n3, 0) : object.getInt(n3, 0));
                object = PropertyValuesHolder.ofInt((String)charSequence, (int[])new int[]{n, n2});
            } else {
                object = PropertyValuesHolder.ofInt((String)charSequence, (int[])new int[]{n});
            }
        } else {
            if (!bl2) return null;
            n = n5 == 5 ? (int)object.getDimension(n3, 0.0f) : (AnimatorInflaterCompat.isColorType(n5) ? object.getColor(n3, 0) : object.getInt(n3, 0));
            object = PropertyValuesHolder.ofInt((String)charSequence, (int[])new int[]{n});
        }
        if (object == null) return object;
        if (object2 == null) return object;
        object.setEvaluator((TypeEvaluator)object2);
        return object;
    }

    private static int inferValueTypeFromValues(TypedArray typedArray, int n, int n2) {
        TypedValue typedValue = typedArray.peekValue(n);
        int n3 = 1;
        int n4 = 0;
        n = typedValue != null ? 1 : 0;
        int n5 = n != 0 ? typedValue.type : 0;
        typedArray = typedArray.peekValue(n2);
        n2 = typedArray != null ? n3 : 0;
        if (n2 != 0) {
            n4 = typedArray.type;
        }
        if (n != 0 && AnimatorInflaterCompat.isColorType(n5) || n2 != 0 && AnimatorInflaterCompat.isColorType(n4)) {
            return 3;
        }
        return 0;
    }

    private static int inferValueTypeOfKeyframe(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        int n = 0;
        theme = TypedArrayUtils.peekNamedValue((TypedArray)resources, xmlPullParser, "value", 0);
        if (theme != null) {
            n = 1;
        }
        n = n != 0 && AnimatorInflaterCompat.isColorType(theme.type) ? 3 : 0;
        resources.recycle();
        return n;
    }

    private static boolean isColorType(int n) {
        if (n >= 28 && n <= 31) {
            return true;
        }
        return false;
    }

    public static Animator loadAnimator(Context context, @AnimatorRes int n) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 24) {
            return AnimatorInflater.loadAnimator((Context)context, (int)n);
        }
        return AnimatorInflaterCompat.loadAnimator(context, context.getResources(), context.getTheme(), n);
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, @AnimatorRes int n) throws Resources.NotFoundException {
        return AnimatorInflaterCompat.loadAnimator(context, resources, theme, n, 1.0f);
    }

    /*
     * Exception decompiling
     */
    public static Animator loadAnimator(Context var0, Resources var1_4, Resources.Theme var2_5, @AnimatorRes int var3_6, float var4_7) throws Resources.NotFoundException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[CATCHBLOCK]], but top level block is 1[TRYBLOCK]
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

    private static ValueAnimator loadAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, ValueAnimator valueAnimator, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR);
        theme = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        resources = valueAnimator;
        if (valueAnimator == null) {
            resources = new ValueAnimator();
        }
        AnimatorInflaterCompat.parseAnimatorFromTypeArray((ValueAnimator)resources, typedArray, (TypedArray)theme, f, xmlPullParser);
        int n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "interpolator", 0, 0);
        if (n > 0) {
            resources.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, n));
        }
        typedArray.recycle();
        if (theme != null) {
            theme.recycle();
        }
        return resources;
    }

    private static Keyframe loadKeyframe(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, int n, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        theme = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        resources = null;
        float f = TypedArrayUtils.getNamedFloat((TypedArray)theme, xmlPullParser, "fraction", 3, -1.0f);
        attributeSet = TypedArrayUtils.peekNamedValue((TypedArray)theme, xmlPullParser, "value", 0);
        boolean bl = attributeSet != null;
        int n2 = n;
        if (n == 4) {
            n2 = bl && AnimatorInflaterCompat.isColorType(attributeSet.type) ? 3 : 0;
        }
        if (bl) {
            if (n2 != 0) {
                if (n2 == 1 || n2 == 3) {
                    resources = Keyframe.ofInt((float)f, (int)TypedArrayUtils.getNamedInt((TypedArray)theme, xmlPullParser, "value", 0, 0));
                }
            } else {
                resources = Keyframe.ofFloat((float)f, (float)TypedArrayUtils.getNamedFloat((TypedArray)theme, xmlPullParser, "value", 0, 0.0f));
            }
        } else {
            resources = n2 == 0 ? Keyframe.ofFloat((float)f) : Keyframe.ofInt((float)f);
        }
        n = TypedArrayUtils.getNamedResourceId((TypedArray)theme, xmlPullParser, "interpolator", 1, 0);
        if (n > 0) {
            resources.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, n));
        }
        theme.recycle();
        return resources;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        AnimatorInflaterCompat.loadAnimator(context, resources, theme, attributeSet, (ValueAnimator)objectAnimator, f, xmlPullParser);
        return objectAnimator;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static PropertyValuesHolder loadPvh(Context keyframe, Resources keyframe2, Resources.Theme theme, XmlPullParser xmlPullParser, String string2, int n) throws XmlPullParserException, IOException {
        int n2;
        boolean bl = false;
        ArrayList<Keyframe> arrayList = null;
        int n3 = n;
        do {
            n2 = n = xmlPullParser.next();
            if (n == 3 || n2 == 1) break;
            if (!xmlPullParser.getName().equals("keyframe")) continue;
            if (n3 == 4) {
                n3 = AnimatorInflaterCompat.inferValueTypeOfKeyframe((Resources)keyframe2, theme, Xml.asAttributeSet((XmlPullParser)xmlPullParser), xmlPullParser);
            }
            Keyframe keyframe3 = AnimatorInflaterCompat.loadKeyframe((Context)keyframe, (Resources)keyframe2, theme, Xml.asAttributeSet((XmlPullParser)xmlPullParser), n3, xmlPullParser);
            ArrayList<Keyframe> arrayList2 = arrayList;
            if (keyframe3 != null) {
                arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList<Keyframe>();
                }
                arrayList2.add(keyframe3);
            }
            xmlPullParser.next();
            arrayList = arrayList2;
        } while (true);
        if (arrayList == null) return null;
        int n4 = n = arrayList.size();
        if (n <= 0) return null;
        keyframe = (Keyframe)arrayList.get(0);
        keyframe2 = (Keyframe)arrayList.get(n4 - 1);
        float f = keyframe2.getFraction();
        n = n4;
        if (f < 1.0f) {
            if (f < 0.0f) {
                keyframe2.setFraction(1.0f);
                n = n4;
            } else {
                arrayList.add(arrayList.size(), AnimatorInflaterCompat.createNewKeyframe(keyframe2, 1.0f));
                n = n4 + 1;
            }
        }
        f = keyframe.getFraction();
        int n5 = n;
        if (f != 0.0f) {
            if (f < 0.0f) {
                keyframe.setFraction(0.0f);
                n5 = n;
            } else {
                arrayList.add(0, AnimatorInflaterCompat.createNewKeyframe(keyframe, 0.0f));
                n5 = n + 1;
            }
        }
        keyframe = new Keyframe[n5];
        arrayList.toArray((T[])keyframe);
        n = n2;
        for (n4 = 0; n4 < n5; ++n4) {
            keyframe2 = keyframe[n4];
            if (keyframe2.getFraction() >= 0.0f) continue;
            if (n4 == 0) {
                keyframe2.setFraction(0.0f);
                continue;
            }
            if (n4 == n5 - 1) {
                keyframe2.setFraction(1.0f);
                continue;
            }
            int n6 = n4 + 1;
            int n7 = n4;
            n2 = n;
            n = n6;
            while (n < n5 - 1 && keyframe[n].getFraction() < 0.0f) {
                n7 = n++;
            }
            AnimatorInflaterCompat.distributeKeyframes((Keyframe[])keyframe, keyframe[n7 + 1].getFraction() - keyframe[n4 - 1].getFraction(), n4, n7);
            n = n2;
        }
        keyframe = keyframe2 = PropertyValuesHolder.ofKeyframe((String)string2, (Keyframe[])keyframe);
        if (n3 != 3) return keyframe;
        keyframe2.setEvaluator((TypeEvaluator)ArgbEvaluator.getInstance());
        return keyframe2;
    }

    private static PropertyValuesHolder[] loadValues(Context arrpropertyValuesHolder, Resources arrpropertyValuesHolder2, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int n;
        Object object = null;
        while ((n = xmlPullParser.getEventType()) != 3 && n != 1) {
            if (n != 2) {
                xmlPullParser.next();
                continue;
            }
            if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                TypedArray typedArray = TypedArrayUtils.obtainAttributes((Resources)arrpropertyValuesHolder2, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                Object object2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 3);
                PropertyValuesHolder propertyValuesHolder = AnimatorInflaterCompat.loadPvh((Context)arrpropertyValuesHolder, (Resources)arrpropertyValuesHolder2, theme, xmlPullParser, (String)object2, n = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 2, 4));
                if (propertyValuesHolder == null) {
                    propertyValuesHolder = AnimatorInflaterCompat.getPVH(typedArray, n, 0, 1, (String)object2);
                }
                object2 = object;
                if (propertyValuesHolder != null) {
                    object2 = object;
                    if (object == null) {
                        object2 = new ArrayList<PropertyValuesHolder>();
                    }
                    object2.add(propertyValuesHolder);
                }
                typedArray.recycle();
                object = object2;
            }
            xmlPullParser.next();
        }
        arrpropertyValuesHolder = null;
        if (object != null) {
            int n2 = object.size();
            arrpropertyValuesHolder2 = new PropertyValuesHolder[n2];
            n = 0;
            do {
                arrpropertyValuesHolder = arrpropertyValuesHolder2;
                if (n >= n2) break;
                arrpropertyValuesHolder2[n] = (PropertyValuesHolder)object.get(n);
                ++n;
            } while (true);
        }
        return arrpropertyValuesHolder;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator valueAnimator, TypedArray typedArray, TypedArray typedArray2, float f, XmlPullParser xmlPullParser) {
        int n;
        long l = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        long l2 = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int n2 = n = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            n2 = n;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int n3 = n;
                if (n == 4) {
                    n3 = AnimatorInflaterCompat.inferValueTypeFromValues(typedArray, 5, 6);
                }
                PropertyValuesHolder propertyValuesHolder = AnimatorInflaterCompat.getPVH(typedArray, n3, 5, 6, "");
                n2 = n3;
                if (propertyValuesHolder != null) {
                    valueAnimator.setValues(new PropertyValuesHolder[]{propertyValuesHolder});
                    n2 = n3;
                }
            }
        }
        valueAnimator.setDuration(l);
        valueAnimator.setStartDelay(l2);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            AnimatorInflaterCompat.setupObjectAnimator(valueAnimator, typedArray2, n2, f, xmlPullParser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator object, TypedArray typedArray, int n, float f, XmlPullParser object2) {
        object = (ObjectAnimator)object;
        String string2 = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)object2, "pathData", 1);
        if (string2 != null) {
            String string3 = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)object2, "propertyXName", 2);
            object2 = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)object2, "propertyYName", 3);
            if (n == 2 || n == 4) {
                // empty if block
            }
            if (string3 == null && object2 == null) {
                object = new StringBuilder();
                object.append(typedArray.getPositionDescription());
                object.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(object.toString());
            }
            AnimatorInflaterCompat.setupPathMotion(PathParser.createPathFromPathData(string2), (ObjectAnimator)object, 0.5f * f, string3, (String)object2);
            return;
        }
        object.setPropertyName(TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)object2, "propertyName", 0));
    }

    private static void setupPathMotion(Path path, ObjectAnimator objectAnimator, float f, String object, String string2) {
        float[] arrf = new float[](path, false);
        float f2 = 0.0f;
        ArrayList<Float> arrayList = new ArrayList<Float>();
        arrayList.add(Float.valueOf(0.0f));
        do {
            arrayList.add(Float.valueOf(f2 += arrf.getLength()));
        } while (arrf.nextContour());
        path = new PathMeasure(path, false);
        int n = Math.min(100, (int)(f2 / f) + 1);
        float[] arrf2 = new float[n];
        arrf = new float[n];
        float[] arrf3 = new float[2];
        float f3 = f2 / (float)(n - 1);
        f = 0.0f;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            path.getPosTan(f, arrf3, null);
            path.getPosTan(f, arrf3, null);
            arrf2[i] = arrf3[0];
            arrf[i] = arrf3[1];
            f = f2 = f + f3;
            int n3 = n2;
            if (n2 + 1 < arrayList.size()) {
                f = f2;
                n3 = n2;
                if (f2 > ((Float)arrayList.get(n2 + 1)).floatValue()) {
                    f = f2 - ((Float)arrayList.get(n2 + 1)).floatValue();
                    n3 = n2 + 1;
                    path.nextContour();
                }
            }
            n2 = n3;
        }
        path = null;
        arrayList = null;
        if (object != null) {
            path = PropertyValuesHolder.ofFloat((String)object, (float[])arrf2);
        }
        object = arrayList;
        if (string2 != null) {
            object = PropertyValuesHolder.ofFloat((String)string2, (float[])arrf);
        }
        if (path == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{object});
            return;
        }
        if (object == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{path});
            return;
        }
        objectAnimator.setValues(new PropertyValuesHolder[]{path, object});
    }

    private static class PathDataEvaluator
    implements TypeEvaluator<PathParser.PathDataNode[]> {
        private PathParser.PathDataNode[] mNodeArray;

        private PathDataEvaluator() {
        }

        PathDataEvaluator(PathParser.PathDataNode[] arrpathDataNode) {
            this.mNodeArray = arrpathDataNode;
        }

        public PathParser.PathDataNode[] evaluate(float f, PathParser.PathDataNode[] object, PathParser.PathDataNode[] arrpathDataNode) {
            if (PathParser.canMorph((PathParser.PathDataNode[])object, arrpathDataNode)) {
                PathParser.PathDataNode[] arrpathDataNode2 = this.mNodeArray;
                if (arrpathDataNode2 == null || !PathParser.canMorph(arrpathDataNode2, (PathParser.PathDataNode[])object)) {
                    this.mNodeArray = PathParser.deepCopyNodes((PathParser.PathDataNode[])object);
                }
                for (int i = 0; i < object.length; ++i) {
                    this.mNodeArray[i].interpolatePathDataNode((PathParser.PathDataNode)object[i], arrpathDataNode[i], f);
                }
                return this.mNodeArray;
            }
            object = new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            throw object;
        }
    }

}

