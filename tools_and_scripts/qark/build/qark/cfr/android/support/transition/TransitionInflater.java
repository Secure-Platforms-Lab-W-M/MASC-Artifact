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
package android.support.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.transition.ArcMotion;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeClipBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeScroll;
import android.support.transition.ChangeTransform;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.transition.PathMotion;
import android.support.transition.PatternPathMotion;
import android.support.transition.Scene;
import android.support.transition.Slide;
import android.support.transition.Styleable;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.ViewGroup;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
    private static final ArrayMap<String, Constructor> CONSTRUCTORS;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private final Context mContext;

    static {
        CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
        CONSTRUCTORS = new ArrayMap();
    }

    private TransitionInflater(@NonNull Context context) {
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private Object createCustom(AttributeSet object, Class class_, String constructor) {
        Class class_2;
        String string2 = object.getAttributeValue(null, "class");
        if (string2 == null) {
            object = new StringBuilder();
            object.append((String)((Object)constructor));
            object.append(" tag must have a 'class' attribute");
            throw new InflateException(object.toString());
        }
        try {
            ArrayMap<String, Constructor> arrayMap = CONSTRUCTORS;
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
        constructor = CONSTRUCTORS.get(string2);
        if (constructor == null && (class_2 = this.mContext.getClassLoader().loadClass(string2).asSubclass(class_)) != null) {
            constructor = class_2.getConstructor(CONSTRUCTOR_SIGNATURE);
            constructor.setAccessible(true);
            CONSTRUCTORS.put(string2, constructor);
        }
        object = constructor.newInstance(new Object[]{this.mContext, object});
        // MONITOREXIT : arrayMap
        return object;
    }

    private Transition createTransitionFromXml(XmlPullParser xmlPullParser, AttributeSet object, Transition transition) throws XmlPullParserException, IOException {
        int n;
        Transition transition2 = null;
        int n2 = xmlPullParser.getDepth();
        TransitionSet transitionSet = transition instanceof TransitionSet ? (TransitionSet)transition : null;
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            block23 : {
                block24 : {
                    block7 : {
                        String string2;
                        block21 : {
                            block22 : {
                                block19 : {
                                    block20 : {
                                        block18 : {
                                            block17 : {
                                                block16 : {
                                                    block15 : {
                                                        block14 : {
                                                            block13 : {
                                                                block12 : {
                                                                    block11 : {
                                                                        block10 : {
                                                                            block9 : {
                                                                                block8 : {
                                                                                    block6 : {
                                                                                        if (n != 2) continue;
                                                                                        string2 = xmlPullParser.getName();
                                                                                        if (!"fade".equals(string2)) break block6;
                                                                                        transition2 = new Fade(this.mContext, (AttributeSet)object);
                                                                                        break block7;
                                                                                    }
                                                                                    if (!"changeBounds".equals(string2)) break block8;
                                                                                    transition2 = new ChangeBounds(this.mContext, (AttributeSet)object);
                                                                                    break block7;
                                                                                }
                                                                                if (!"slide".equals(string2)) break block9;
                                                                                transition2 = new Slide(this.mContext, (AttributeSet)object);
                                                                                break block7;
                                                                            }
                                                                            if (!"explode".equals(string2)) break block10;
                                                                            transition2 = new Explode(this.mContext, (AttributeSet)object);
                                                                            break block7;
                                                                        }
                                                                        if (!"changeImageTransform".equals(string2)) break block11;
                                                                        transition2 = new ChangeImageTransform(this.mContext, (AttributeSet)object);
                                                                        break block7;
                                                                    }
                                                                    if (!"changeTransform".equals(string2)) break block12;
                                                                    transition2 = new ChangeTransform(this.mContext, (AttributeSet)object);
                                                                    break block7;
                                                                }
                                                                if (!"changeClipBounds".equals(string2)) break block13;
                                                                transition2 = new ChangeClipBounds(this.mContext, (AttributeSet)object);
                                                                break block7;
                                                            }
                                                            if (!"autoTransition".equals(string2)) break block14;
                                                            transition2 = new AutoTransition(this.mContext, (AttributeSet)object);
                                                            break block7;
                                                        }
                                                        if (!"changeScroll".equals(string2)) break block15;
                                                        transition2 = new ChangeScroll(this.mContext, (AttributeSet)object);
                                                        break block7;
                                                    }
                                                    if (!"transitionSet".equals(string2)) break block16;
                                                    transition2 = new TransitionSet(this.mContext, (AttributeSet)object);
                                                    break block7;
                                                }
                                                if (!"transition".equals(string2)) break block17;
                                                transition2 = (Transition)this.createCustom((AttributeSet)object, Transition.class, "transition");
                                                break block7;
                                            }
                                            if (!"targets".equals(string2)) break block18;
                                            this.getTargetIds(xmlPullParser, (AttributeSet)object, transition);
                                            break block7;
                                        }
                                        if (!"arcMotion".equals(string2)) break block19;
                                        if (transition == null) break block20;
                                        transition.setPathMotion(new ArcMotion(this.mContext, (AttributeSet)object));
                                        break block7;
                                    }
                                    throw new RuntimeException("Invalid use of arcMotion element");
                                }
                                if (!"pathMotion".equals(string2)) break block21;
                                if (transition == null) break block22;
                                transition.setPathMotion((PathMotion)this.createCustom((AttributeSet)object, PathMotion.class, "pathMotion"));
                                break block7;
                            }
                            throw new RuntimeException("Invalid use of pathMotion element");
                        }
                        if (!"patternPathMotion".equals(string2)) break block23;
                        if (transition == null) break block24;
                        transition.setPathMotion(new PatternPathMotion(this.mContext, (AttributeSet)object));
                    }
                    if (transition2 == null) continue;
                    if (!xmlPullParser.isEmptyElementTag()) {
                        this.createTransitionFromXml(xmlPullParser, (AttributeSet)object, transition2);
                    }
                    if (transitionSet != null) {
                        transitionSet.addTransition(transition2);
                        transition2 = null;
                        continue;
                    }
                    if (transition == null) continue;
                    throw new InflateException("Could not add transition to another transition.");
                }
                throw new RuntimeException("Invalid use of patternPathMotion element");
            }
            object = new StringBuilder();
            object.append("Unknown scene name: ");
            object.append(xmlPullParser.getName());
            throw new RuntimeException(object.toString());
        }
        return transition2;
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

