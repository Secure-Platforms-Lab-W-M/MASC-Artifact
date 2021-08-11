/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.InflateException
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.ArcMotion;
import androidx.transition.AutoTransition;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeClipBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeScroll;
import androidx.transition.ChangeTransform;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.PathMotion;
import androidx.transition.PatternPathMotion;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Styleable;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
    private static final ArrayMap<String, Constructor<?>> CONSTRUCTORS;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private final Context mContext;

    static {
        CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
        CONSTRUCTORS = new ArrayMap();
    }

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private Object createCustom(AttributeSet object, Class<?> class_, String constructor) {
        Constructor constructor2;
        String string2 = object.getAttributeValue(null, "class");
        if (string2 == null) {
            object = new StringBuilder();
            object.append((String)((Object)constructor));
            object.append(" tag must have a 'class' attribute");
            throw new InflateException(object.toString());
        }
        try {
            ArrayMap arrayMap = CONSTRUCTORS;
            // MONITORENTER : arrayMap
        }
        catch (Exception exception) {
            constructor = new StringBuilder();
            constructor.append("Could not instantiate ");
            constructor.append(class_);
            constructor.append(" class ");
            constructor.append(string2);
            throw new InflateException(constructor.toString(), (Throwable)exception);
        }
        constructor = constructor2 = CONSTRUCTORS.get(string2);
        if (constructor2 == null) {
            Class class_2 = Class.forName(string2, false, this.mContext.getClassLoader()).asSubclass(class_);
            constructor = constructor2;
            if (class_2 != null) {
                constructor = class_2.getConstructor(CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                CONSTRUCTORS.put(string2, constructor);
            }
        }
        object = constructor.newInstance(new Object[]{this.mContext, object});
        // MONITOREXIT : arrayMap
        return object;
    }

    private Transition createTransitionFromXml(XmlPullParser xmlPullParser, AttributeSet object, Transition transition) throws XmlPullParserException, IOException {
        int n;
        Object object2 = null;
        int n2 = xmlPullParser.getDepth();
        TransitionSet transitionSet = transition instanceof TransitionSet ? (TransitionSet)transition : null;
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            block27 : {
                block28 : {
                    Object object3;
                    block11 : {
                        block25 : {
                            block26 : {
                                block23 : {
                                    block24 : {
                                        block22 : {
                                            block21 : {
                                                block20 : {
                                                    block19 : {
                                                        block18 : {
                                                            block17 : {
                                                                block16 : {
                                                                    block15 : {
                                                                        block14 : {
                                                                            block13 : {
                                                                                block12 : {
                                                                                    block10 : {
                                                                                        if (n != 2) continue;
                                                                                        object3 = xmlPullParser.getName();
                                                                                        if (!"fade".equals(object3)) break block10;
                                                                                        object2 = new Fade(this.mContext, (AttributeSet)object);
                                                                                        break block11;
                                                                                    }
                                                                                    if (!"changeBounds".equals(object3)) break block12;
                                                                                    object2 = new ChangeBounds(this.mContext, (AttributeSet)object);
                                                                                    break block11;
                                                                                }
                                                                                if (!"slide".equals(object3)) break block13;
                                                                                object2 = new Slide(this.mContext, (AttributeSet)object);
                                                                                break block11;
                                                                            }
                                                                            if (!"explode".equals(object3)) break block14;
                                                                            object2 = new Explode(this.mContext, (AttributeSet)object);
                                                                            break block11;
                                                                        }
                                                                        if (!"changeImageTransform".equals(object3)) break block15;
                                                                        object2 = new ChangeImageTransform(this.mContext, (AttributeSet)object);
                                                                        break block11;
                                                                    }
                                                                    if (!"changeTransform".equals(object3)) break block16;
                                                                    object2 = new ChangeTransform(this.mContext, (AttributeSet)object);
                                                                    break block11;
                                                                }
                                                                if (!"changeClipBounds".equals(object3)) break block17;
                                                                object2 = new ChangeClipBounds(this.mContext, (AttributeSet)object);
                                                                break block11;
                                                            }
                                                            if (!"autoTransition".equals(object3)) break block18;
                                                            object2 = new AutoTransition(this.mContext, (AttributeSet)object);
                                                            break block11;
                                                        }
                                                        if (!"changeScroll".equals(object3)) break block19;
                                                        object2 = new ChangeScroll(this.mContext, (AttributeSet)object);
                                                        break block11;
                                                    }
                                                    if (!"transitionSet".equals(object3)) break block20;
                                                    object2 = new TransitionSet(this.mContext, (AttributeSet)object);
                                                    break block11;
                                                }
                                                if (!"transition".equals(object3)) break block21;
                                                object2 = (Transition)this.createCustom((AttributeSet)object, Transition.class, "transition");
                                                break block11;
                                            }
                                            if (!"targets".equals(object3)) break block22;
                                            this.getTargetIds(xmlPullParser, (AttributeSet)object, transition);
                                            break block11;
                                        }
                                        if (!"arcMotion".equals(object3)) break block23;
                                        if (transition == null) break block24;
                                        transition.setPathMotion(new ArcMotion(this.mContext, (AttributeSet)object));
                                        break block11;
                                    }
                                    throw new RuntimeException("Invalid use of arcMotion element");
                                }
                                if (!"pathMotion".equals(object3)) break block25;
                                if (transition == null) break block26;
                                transition.setPathMotion((PathMotion)this.createCustom((AttributeSet)object, PathMotion.class, "pathMotion"));
                                break block11;
                            }
                            throw new RuntimeException("Invalid use of pathMotion element");
                        }
                        if (!"patternPathMotion".equals(object3)) break block27;
                        if (transition == null) break block28;
                        transition.setPathMotion(new PatternPathMotion(this.mContext, (AttributeSet)object));
                    }
                    object3 = object2;
                    if (object2 != null) {
                        if (!xmlPullParser.isEmptyElementTag()) {
                            this.createTransitionFromXml(xmlPullParser, (AttributeSet)object, (Transition)object2);
                        }
                        if (transitionSet != null) {
                            transitionSet.addTransition((Transition)object2);
                            object3 = null;
                        } else if (transition == null) {
                            object3 = object2;
                        } else {
                            throw new InflateException("Could not add transition to another transition.");
                        }
                    }
                    object2 = object3;
                    continue;
                }
                throw new RuntimeException("Invalid use of patternPathMotion element");
            }
            object = new StringBuilder();
            object.append("Unknown scene name: ");
            object.append(xmlPullParser.getName());
            throw new RuntimeException(object.toString());
        }
        return object2;
    }

    private TransitionManager createTransitionManagerFromXml(XmlPullParser xmlPullParser, AttributeSet object, ViewGroup viewGroup) throws XmlPullParserException, IOException {
        int n;
        int n2 = xmlPullParser.getDepth();
        TransitionManager transitionManager = null;
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            String string2 = xmlPullParser.getName();
            if (string2.equals("transitionManager")) {
                transitionManager = new TransitionManager();
                continue;
            }
            if (string2.equals("transition") && transitionManager != null) {
                this.loadTransition((AttributeSet)object, xmlPullParser, viewGroup, transitionManager);
                continue;
            }
            object = new StringBuilder();
            object.append("Unknown scene name: ");
            object.append(xmlPullParser.getName());
            throw new RuntimeException(object.toString());
        }
        return transitionManager;
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void getTargetIds(XmlPullParser xmlPullParser, AttributeSet object, Transition transition) throws XmlPullParserException, IOException {
        n2 = xmlPullParser.getDepth();
        do {
            block5 : {
                block9 : {
                    block8 : {
                        block7 : {
                            block6 : {
                                if ((n = xmlPullParser.next()) == 3) {
                                    if (xmlPullParser.getDepth() <= n2) return;
                                }
                                if (n == 1) return;
                                if (n != 2) continue;
                                if (!xmlPullParser.getName().equals("target")) {
                                    object = new StringBuilder();
                                    object.append("Unknown scene name: ");
                                    object.append(xmlPullParser.getName());
                                    throw new RuntimeException(object.toString());
                                }
                                typedArray = this.mContext.obtainStyledAttributes((AttributeSet)object, Styleable.TRANSITION_TARGET);
                                n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "targetId", 1, 0);
                                if (n == 0) break block6;
                                transition.addTarget(n);
                                break block5;
                            }
                            n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "excludeId", 2, 0);
                            if (n == 0) break block7;
                            transition.excludeTarget(n, true);
                            break block5;
                        }
                        string2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "targetName", 4);
                        if (string2 == null) break block8;
                        transition.addTarget(string2);
                        break block5;
                    }
                    string2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "excludeName", 5);
                    if (string2 == null) break block9;
                    transition.excludeTarget(string2, true);
                    break block5;
                }
                string4 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "excludeClass", 3);
                if (string4 == null) ** GOTO lbl39
                string2 = string4;
                transition.excludeTarget(Class.forName(string4), true);
                break block5;
lbl39: // 1 sources:
                string2 = string4;
                string4 = string3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "targetClass", 0);
                if (string3 == null) break block5;
                string2 = string4;
                transition.addTarget(Class.forName(string4));
            }
            typedArray.recycle();
        } while (true);
        catch (ClassNotFoundException classNotFoundException) {
            typedArray.recycle();
            object = new StringBuilder();
            object.append("Could not create ");
            object.append(string2);
            throw new RuntimeException(object.toString(), classNotFoundException);
        }
    }

    private void loadTransition(AttributeSet object, XmlPullParser object2, ViewGroup object3, TransitionManager transitionManager) throws Resources.NotFoundException {
        TypedArray typedArray = this.mContext.obtainStyledAttributes((AttributeSet)object, Styleable.TRANSITION_MANAGER);
        int n = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)object2, "transition", 2, -1);
        int n2 = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)object2, "fromScene", 0, -1);
        Object var7_8 = null;
        object = n2 < 0 ? null : Scene.getSceneForLayout((ViewGroup)object3, n2, this.mContext);
        n2 = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)object2, "toScene", 1, -1);
        object2 = n2 < 0 ? var7_8 : Scene.getSceneForLayout((ViewGroup)object3, n2, this.mContext);
        if (n >= 0 && (object3 = this.inflateTransition(n)) != null) {
            if (object2 != null) {
                if (object == null) {
                    transitionManager.setTransition((Scene)object2, (Transition)object3);
                } else {
                    transitionManager.setTransition((Scene)object, (Scene)object2, (Transition)object3);
                }
            } else {
                object = new StringBuilder();
                object.append("No toScene for transition ID ");
                object.append(n);
                throw new RuntimeException(object.toString());
            }
        }
        typedArray.recycle();
    }

    /*
     * Exception decompiling
     */
    public Transition inflateTransition(int var1_1) {
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

    /*
     * Exception decompiling
     */
    public TransitionManager inflateTransitionManager(int var1_1, ViewGroup var2_2) {
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
}

