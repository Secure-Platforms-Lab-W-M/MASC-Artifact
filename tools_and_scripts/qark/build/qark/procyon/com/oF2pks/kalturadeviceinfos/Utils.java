// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.oF2pks.kalturadeviceinfos;

import java.util.Iterator;
import java.util.TreeSet;
import org.json.JSONException;
import org.json.JSONObject;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;

public class Utils
{
    public static String getProp(String string) {
        while (true) {
            try {
                Log.d("cipherName-135", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-136", Cipher.getInstance("DES").getAlgorithm());
                        final Object invoke = Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, string);
                        if (invoke instanceof String) {
                            return (String)invoke;
                        }
                        string = "<" + invoke + ">";
                        return (String)string;
                    }
                    catch (Exception string) {
                        try {
                            Log.d("cipherName-137", Cipher.getInstance("DES").getAlgorithm());
                            return "<" + string + ">";
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                }
                catch (NoSuchAlgorithmException ex3) {}
                catch (NoSuchPaddingException ex4) {}
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    public static String getZinfo(final String p0, final String p1, final boolean p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: ldc             "DES"
        //     4: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     7: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    10: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    13: pop            
        //    14: ldc             "cipherName-144"
        //    16: ldc             "DES"
        //    18: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    21: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    24: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    27: pop            
        //    28: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //    31: aload_0        
        //    32: invokevirtual   java/lang/Runtime.exec:(Ljava/lang/String;)Ljava/lang/Process;
        //    35: invokevirtual   java/lang/Process.getInputStream:()Ljava/io/InputStream;
        //    38: astore          4
        //    40: new             Ljava/io/BufferedReader;
        //    43: dup            
        //    44: new             Ljava/io/InputStreamReader;
        //    47: dup            
        //    48: aload           4
        //    50: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    53: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    56: astore          5
        //    58: ldc             ""
        //    60: astore_3       
        //    61: aload_3        
        //    62: astore_0       
        //    63: iload_2        
        //    64: ifeq            75
        //    67: aload           5
        //    69: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    72: pop            
        //    73: aload_3        
        //    74: astore_0       
        //    75: aload           5
        //    77: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    80: astore_3       
        //    81: aload_3        
        //    82: ifnull          125
        //    85: ldc             "cipherName-145"
        //    87: ldc             "DES"
        //    89: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    92: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    95: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    98: pop            
        //    99: new             Ljava/lang/StringBuilder;
        //   102: dup            
        //   103: invokespecial   java/lang/StringBuilder.<init>:()V
        //   106: aload_0        
        //   107: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   110: aload_1        
        //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   114: aload_3        
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   118: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   121: astore_0       
        //   122: goto            75
        //   125: aload           4
        //   127: invokevirtual   java/io/InputStream.close:()V
        //   130: aload           5
        //   132: invokevirtual   java/io/BufferedReader.close:()V
        //   135: aload_0        
        //   136: invokevirtual   java/lang/String.length:()I
        //   139: ifeq            144
        //   142: aload_0        
        //   143: areturn        
        //   144: ldc             "Unknow"
        //   146: areturn        
        //   147: astore_0       
        //   148: ldc             "cipherName-146"
        //   150: ldc             "DES"
        //   152: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   155: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   158: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   161: pop            
        //   162: new             Ljava/lang/StringBuilder;
        //   165: dup            
        //   166: invokespecial   java/lang/StringBuilder.<init>:()V
        //   169: ldc             "ERROR: "
        //   171: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   174: aload_0        
        //   175: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   178: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   181: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   184: areturn        
        //   185: astore_1       
        //   186: goto            162
        //   189: astore_1       
        //   190: goto            162
        //   193: astore          6
        //   195: goto            99
        //   198: astore          6
        //   200: goto            99
        //   203: astore_3       
        //   204: goto            28
        //   207: astore_3       
        //   208: goto            28
        //   211: astore_3       
        //   212: goto            14
        //   215: astore_3       
        //   216: goto            14
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      14     211    215    Ljava/security/NoSuchAlgorithmException;
        //  0      14     215    219    Ljavax/crypto/NoSuchPaddingException;
        //  14     28     203    207    Ljava/security/NoSuchAlgorithmException;
        //  14     28     207    211    Ljavax/crypto/NoSuchPaddingException;
        //  14     28     147    193    Ljava/lang/Exception;
        //  28     58     147    193    Ljava/lang/Exception;
        //  67     73     147    193    Ljava/lang/Exception;
        //  75     81     147    193    Ljava/lang/Exception;
        //  85     99     193    198    Ljava/security/NoSuchAlgorithmException;
        //  85     99     198    203    Ljavax/crypto/NoSuchPaddingException;
        //  85     99     147    193    Ljava/lang/Exception;
        //  99     122    147    193    Ljava/lang/Exception;
        //  125    142    147    193    Ljava/lang/Exception;
        //  148    162    185    189    Ljava/security/NoSuchAlgorithmException;
        //  148    162    189    193    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0099:
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
    
    public static JSONObject semicolonJson(String substring, final String s, final String s2) throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-138", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                while (true) {
                    Label_0091: {
                        if (!substring.contains(s2)) {
                            break Label_0091;
                        }
                        try {
                            Log.d("cipherName-139", Cipher.getInstance("DES").getAlgorithm());
                            final int index = substring.indexOf(s2);
                            final int index2 = substring.indexOf(s);
                            jsonObject.put(substring.substring(0, index2), (Object)substring.substring(index2 + 1, index));
                            substring = substring.substring(index + 1);
                            continue;
                            final int index3 = substring.indexOf(s);
                            jsonObject.put(substring.substring(0, index3), (Object)substring.substring(index3 + 1));
                            return jsonObject;
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                }
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
    
    public static JSONObject semicolonSortedJson(String o, final String s, String string) throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-140", Cipher.getInstance("DES").getAlgorithm());
                final TreeSet<String> set = new TreeSet<String>();
                final JSONObject jsonObject = new JSONObject();
                while (true) {
                    Label_0085: {
                        if (!((String)o).contains((CharSequence)string)) {
                            break Label_0085;
                        }
                        try {
                            Log.d("cipherName-141", Cipher.getInstance("DES").getAlgorithm());
                            final int index = ((String)o).indexOf((String)string);
                            set.add(((String)o).substring(0, index));
                            o = ((String)o).substring(index + 1);
                            continue;
                            Label_0102: {
                                try {
                                    Log.d("cipherName-142", Cipher.getInstance("DES").getAlgorithm());
                                    string = ((Iterator<Object>)o).next().toString();
                                    final int index2 = ((String)string).indexOf(s);
                                    jsonObject.put(((String)string).substring(0, index2), (Object)((String)string).substring(index2 + 1));
                                    break Label_0102;
                                    Label_0163: {
                                        return jsonObject;
                                    }
                                }
                                catch (NoSuchAlgorithmException string) {}
                                catch (NoSuchPaddingException string) {}
                                set.add(o);
                                o = set.iterator();
                            }
                        }
                        // iftrue(Label_0163:, !o.hasNext())
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                }
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
}
