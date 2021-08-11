// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.ViewGroup;
import android.view.View;
import java.lang.reflect.Method;

class ViewCompatEclairMr1
{
    public static final String TAG = "ViewCompat";
    private static Method sChildrenDrawingOrderMethod;
    
    public static boolean isOpaque(final View view) {
        return view.isOpaque();
    }
    
    public static void setChildrenDrawingOrderEnabled(final ViewGroup p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifnonnull       33
        //     6: ldc             Landroid/view/ViewGroup;.class
        //     8: ldc             "setChildrenDrawingOrderEnabled"
        //    10: iconst_1       
        //    11: anewarray       Ljava/lang/Class;
        //    14: dup            
        //    15: iconst_0       
        //    16: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    19: aastore        
        //    20: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    23: putstatic       android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
        //    26: getstatic       android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
        //    29: iconst_1       
        //    30: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    33: getstatic       android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
        //    36: aload_0        
        //    37: iconst_1       
        //    38: anewarray       Ljava/lang/Object;
        //    41: dup            
        //    42: iconst_0       
        //    43: iload_1        
        //    44: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    47: aastore        
        //    48: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    51: pop            
        //    52: return         
        //    53: astore_2       
        //    54: ldc             "ViewCompat"
        //    56: ldc             "Unable to find childrenDrawingOrderEnabled"
        //    58: aload_2        
        //    59: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    62: pop            
        //    63: goto            26
        //    66: astore_0       
        //    67: ldc             "ViewCompat"
        //    69: ldc             "Unable to invoke childrenDrawingOrderEnabled"
        //    71: aload_0        
        //    72: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    75: pop            
        //    76: return         
        //    77: astore_0       
        //    78: ldc             "ViewCompat"
        //    80: ldc             "Unable to invoke childrenDrawingOrderEnabled"
        //    82: aload_0        
        //    83: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    86: pop            
        //    87: return         
        //    88: astore_0       
        //    89: ldc             "ViewCompat"
        //    91: ldc             "Unable to invoke childrenDrawingOrderEnabled"
        //    93: aload_0        
        //    94: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    97: pop            
        //    98: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  6      26     53     66     Ljava/lang/NoSuchMethodException;
        //  33     52     66     77     Ljava/lang/IllegalAccessException;
        //  33     52     77     88     Ljava/lang/IllegalArgumentException;
        //  33     52     88     99     Ljava/lang/reflect/InvocationTargetException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0033:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
