// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.oF2pks.kalturadeviceinfos;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraCharacteristics;
import android.content.pm.ConfigurationInfo;
import android.app.ActivityManager;
import java.security.Provider;
import java.security.Security;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import org.json.JSONArray;
import java.util.Random;
import android.hardware.Camera$CameraInfo;
import android.support.annotation.RequiresApi;
import org.json.JSONException;
import android.provider.Settings$Secure;
import android.os.Build;
import android.os.Build$VERSION;
import android.hardware.Camera;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import org.json.JSONObject;
import android.content.Context;
import java.util.UUID;
import android.media.MediaCodecInfo;
import java.util.Comparator;

class Collector
{
    private static Comparator<MediaCodecInfo> McComparator;
    private static final String TAG = "Collector";
    static final UUID WIDEVINE_UUID;
    private static String sReport;
    private final boolean includeSafetyNet;
    private final Context mContext;
    private final JSONObject mRoot;
    private final int numCameras;
    
    static {
        WIDEVINE_UUID = new UUID(-1301668207276963122L, -6645017420763422227L);
        Collector.McComparator = new Comparator<MediaCodecInfo>() {
            @Override
            public int compare(final MediaCodecInfo mediaCodecInfo, final MediaCodecInfo mediaCodecInfo2) {
                try {
                    Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
                    return mediaCodecInfo.getName().toUpperCase().compareTo(mediaCodecInfo2.getName().toUpperCase());
                }
                catch (NoSuchAlgorithmException ex) {
                    return mediaCodecInfo.getName().toUpperCase().compareTo(mediaCodecInfo2.getName().toUpperCase());
                }
                catch (NoSuchPaddingException ex2) {
                    return mediaCodecInfo.getName().toUpperCase().compareTo(mediaCodecInfo2.getName().toUpperCase());
                }
            }
        };
    }
    
    Collector(final Context mContext, final boolean includeSafetyNet) {
        this.mRoot = new JSONObject();
        this.numCameras = Camera.getNumberOfCameras();
        while (true) {
            try {
                Log.d("cipherName-4", Cipher.getInstance("DES").getAlgorithm());
                this.mContext = mContext;
                this.includeSafetyNet = includeSafetyNet;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private JSONObject androidInfo() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-83", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("RELEASE", (Object)Build$VERSION.RELEASE);
                jsonObject.put("KERNEL", (Object)System.getProperty("os.version"));
                jsonObject.put("SDK_INT", Build$VERSION.SDK_INT);
                jsonObject.put("TAGS", (Object)Build.TAGS);
                jsonObject.put("systemName", (Object)System.getProperty("user.name"));
                jsonObject.put("osName", (Object)System.getProperty("os.name"));
                jsonObject.put("JAVA", (Object)System.getProperty("java.vendor"));
                jsonObject.put("JAVA_url", (Object)System.getProperty("java.vendor.url"));
                jsonObject.put("JAVA_vm_version", (Object)System.getProperty("java.vm.version"));
                jsonObject.put("JAVA_home", (Object)System.getProperty("java.home"));
                jsonObject.put("JAVA_path", (Object)System.getProperty("java.class.path"));
                jsonObject.put("JAVA_version", (Object)System.getProperty("java.version"));
                jsonObject.put("JAVA_runtime", (Object)System.getProperty("java.runtime.version"));
                jsonObject.put("ro.build.flavor", (Object)Utils.getProp("ro.build.flavor"));
                jsonObject.put("ro.crypto.type", (Object)Utils.getProp("ro.crypto.type"));
                jsonObject.put("ALLOWED_GEOLOCATION_ORIGINS", (Object)Settings$Secure.getString(this.mContext.getContentResolver(), "allowed_geolocation_origins"));
                jsonObject.put("LOCATION_MODE", (Object)Settings$Secure.getString(this.mContext.getContentResolver(), "location_mode"));
                Label_0275: {
                    if (Build$VERSION.SDK_INT >= 19) {
                        break Label_0275;
                    }
                    try {
                        Log.d("cipherName-84", Cipher.getInstance("DES").getAlgorithm());
                        jsonObject.put("LOCATION_PROVIDERS_ALLOWED", (Object)Settings$Secure.getString(this.mContext.getContentResolver(), "location_providers_allowed"));
                        Label_0363: {
                            if (Build$VERSION.SDK_INT < 23) {
                                break Label_0363;
                            }
                            try {
                                Log.d("cipherName-85", Cipher.getInstance("DES").getAlgorithm());
                                jsonObject.put("BASE_OS", (Object)Build$VERSION.BASE_OS);
                                jsonObject.put("PREVIEW_SDK_INT", Build$VERSION.PREVIEW_SDK_INT);
                                jsonObject.put("SECURITY_PATCH", (Object)Build$VERSION.SECURITY_PATCH);
                                jsonObject.put("ro.secure", (Object)Utils.getProp("ro.secure"));
                                jsonObject.put("TrebleGetprop", (Object)Utils.getProp("ro.treble.enabled"));
                                jsonObject.put("ABupdateGetprop", (Object)Utils.getProp("ro.build.ab_update"));
                                jsonObject.put("/Vendor", (Object)getZpack("df vendor", "vendor", true));
                                jsonObject.put("/System", (Object)getZpack("df system", "system", true));
                                return jsonObject;
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
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
    
    @RequiresApi(api = 21)
    private JSONObject camera2API() throws JSONException {
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
        //    14: new             Lorg/json/JSONObject;
        //    17: dup            
        //    18: invokespecial   org/json/JSONObject.<init>:()V
        //    21: astore          5
        //    23: ldc             ""
        //    25: astore_3       
        //    26: aload_0        
        //    27: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //    30: ldc             "camera"
        //    32: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //    35: checkcast       Landroid/hardware/camera2/CameraManager;
        //    38: astore          6
        //    40: aload           6
        //    42: ifnonnull       61
        //    45: aload           5
        //    47: ldc_w           "WARNING"
        //    50: ldc_w           "CameraAPi2 removed"
        //    53: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //    56: astore          4
        //    58: aload           4
        //    60: areturn        
        //    61: ldc_w           "cipherName-14"
        //    64: ldc             "DES"
        //    66: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    69: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    72: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    75: pop            
        //    76: aload           6
        //    78: invokevirtual   android/hardware/camera2/CameraManager.getCameraIdList:()[Ljava/lang/String;
        //    81: astore          7
        //    83: aload           7
        //    85: arraylength    
        //    86: istore_2       
        //    87: iconst_0       
        //    88: istore_1       
        //    89: aload           5
        //    91: astore          4
        //    93: iload_1        
        //    94: iload_2        
        //    95: if_icmpge       58
        //    98: aload           7
        //   100: iload_1        
        //   101: aaload         
        //   102: astore          9
        //   104: ldc_w           "cipherName-15"
        //   107: ldc             "DES"
        //   109: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   112: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   115: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   118: pop            
        //   119: new             Ljava/lang/StringBuilder;
        //   122: dup            
        //   123: invokespecial   java/lang/StringBuilder.<init>:()V
        //   126: aload_3        
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: ldc_w           "%"
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: aload           9
        //   138: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   141: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   144: astore          4
        //   146: aload           6
        //   148: aload           9
        //   150: invokevirtual   android/hardware/camera2/CameraManager.getCameraCharacteristics:(Ljava/lang/String;)Landroid/hardware/camera2/CameraCharacteristics;
        //   153: astore          8
        //   155: new             Ljava/lang/StringBuilder;
        //   158: dup            
        //   159: invokespecial   java/lang/StringBuilder.<init>:()V
        //   162: aload           9
        //   164: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   167: ldc_w           "="
        //   170: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   173: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   176: astore_3       
        //   177: aload           8
        //   179: getstatic       android/hardware/camera2/CameraCharacteristics.LENS_FACING:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   182: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   185: checkcast       Ljava/lang/Integer;
        //   188: invokevirtual   java/lang/Integer.intValue:()I
        //   191: tableswitch {
        //                0: 360
        //                1: 384
        //                2: 408
        //          default: 640
        //        }
        //   216: new             Ljava/lang/StringBuilder;
        //   219: dup            
        //   220: invokespecial   java/lang/StringBuilder.<init>:()V
        //   223: aload_3        
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: ldc_w           "<"
        //   230: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   233: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   236: astore_3       
        //   237: aload           8
        //   239: getstatic       android/hardware/camera2/CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   242: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   245: checkcast       Ljava/lang/Integer;
        //   248: invokevirtual   java/lang/Integer.intValue:()I
        //   251: tableswitch {
        //                0: 432
        //                1: 456
        //                2: 480
        //                3: 504
        //                4: 528
        //          default: 643
        //        }
        //   284: aload           5
        //   286: new             Ljava/lang/StringBuilder;
        //   289: dup            
        //   290: invokespecial   java/lang/StringBuilder.<init>:()V
        //   293: aload_3        
        //   294: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   297: ldc_w           ">"
        //   300: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   303: aload           8
        //   305: getstatic       android/hardware/camera2/CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   308: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   311: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   314: ldc_w           "("
        //   317: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   320: aload           8
        //   322: getstatic       android/hardware/camera2/CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   325: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   328: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   331: ldc_w           ")"
        //   334: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   337: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   340: aload_0        
        //   341: aload           8
        //   343: invokespecial   com/oF2pks/kalturadeviceinfos/Collector.tech2API:(Landroid/hardware/camera2/CameraCharacteristics;)Lorg/json/JSONObject;
        //   346: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   349: pop            
        //   350: iload_1        
        //   351: iconst_1       
        //   352: iadd           
        //   353: istore_1       
        //   354: aload           4
        //   356: astore_3       
        //   357: goto            89
        //   360: new             Ljava/lang/StringBuilder;
        //   363: dup            
        //   364: invokespecial   java/lang/StringBuilder.<init>:()V
        //   367: aload_3        
        //   368: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   371: ldc_w           "front"
        //   374: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   377: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   380: astore_3       
        //   381: goto            216
        //   384: new             Ljava/lang/StringBuilder;
        //   387: dup            
        //   388: invokespecial   java/lang/StringBuilder.<init>:()V
        //   391: aload_3        
        //   392: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   395: ldc_w           "back"
        //   398: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   401: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   404: astore_3       
        //   405: goto            216
        //   408: new             Ljava/lang/StringBuilder;
        //   411: dup            
        //   412: invokespecial   java/lang/StringBuilder.<init>:()V
        //   415: aload_3        
        //   416: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   419: ldc_w           "external"
        //   422: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   425: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   428: astore_3       
        //   429: goto            216
        //   432: new             Ljava/lang/StringBuilder;
        //   435: dup            
        //   436: invokespecial   java/lang/StringBuilder.<init>:()V
        //   439: aload_3        
        //   440: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   443: ldc_w           "LIMITED"
        //   446: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   449: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   452: astore_3       
        //   453: goto            284
        //   456: new             Ljava/lang/StringBuilder;
        //   459: dup            
        //   460: invokespecial   java/lang/StringBuilder.<init>:()V
        //   463: aload_3        
        //   464: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   467: ldc_w           "FULL"
        //   470: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   473: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   476: astore_3       
        //   477: goto            284
        //   480: new             Ljava/lang/StringBuilder;
        //   483: dup            
        //   484: invokespecial   java/lang/StringBuilder.<init>:()V
        //   487: aload_3        
        //   488: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   491: ldc_w           "LEGACY"
        //   494: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   497: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   500: astore_3       
        //   501: goto            284
        //   504: new             Ljava/lang/StringBuilder;
        //   507: dup            
        //   508: invokespecial   java/lang/StringBuilder.<init>:()V
        //   511: aload_3        
        //   512: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   515: ldc_w           "LEVEL3"
        //   518: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   521: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   524: astore_3       
        //   525: goto            284
        //   528: new             Ljava/lang/StringBuilder;
        //   531: dup            
        //   532: invokespecial   java/lang/StringBuilder.<init>:()V
        //   535: aload_3        
        //   536: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   539: ldc_w           "EXTERNAL"
        //   542: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   545: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   548: astore_3       
        //   549: goto            284
        //   552: astore_3       
        //   553: ldc_w           "cipherName-16"
        //   556: ldc             "DES"
        //   558: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   561: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   564: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   567: pop            
        //   568: aload_3        
        //   569: invokevirtual   android/hardware/camera2/CameraAccessException.printStackTrace:()V
        //   572: aload           5
        //   574: areturn        
        //   575: astore_3       
        //   576: ldc_w           "cipherName-17"
        //   579: ldc             "DES"
        //   581: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   584: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   587: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   590: pop            
        //   591: aload           5
        //   593: areturn        
        //   594: astore_3       
        //   595: aload           5
        //   597: areturn        
        //   598: astore_3       
        //   599: aload           5
        //   601: areturn        
        //   602: astore          4
        //   604: goto            568
        //   607: astore          4
        //   609: goto            568
        //   612: astore          4
        //   614: goto            119
        //   617: astore          4
        //   619: goto            119
        //   622: astore          4
        //   624: goto            76
        //   627: astore          4
        //   629: goto            76
        //   632: astore_3       
        //   633: goto            14
        //   636: astore_3       
        //   637: goto            14
        //   640: goto            216
        //   643: goto            284
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                            
        //  -----  -----  -----  -----  ------------------------------------------------
        //  0      14     632    636    Ljava/security/NoSuchAlgorithmException;
        //  0      14     636    640    Ljavax/crypto/NoSuchPaddingException;
        //  61     76     622    627    Ljava/security/NoSuchAlgorithmException;
        //  61     76     627    632    Ljavax/crypto/NoSuchPaddingException;
        //  61     76     552    612    Landroid/hardware/camera2/CameraAccessException;
        //  61     76     575    602    Ljava/lang/NullPointerException;
        //  76     87     552    612    Landroid/hardware/camera2/CameraAccessException;
        //  76     87     575    602    Ljava/lang/NullPointerException;
        //  104    119    612    617    Ljava/security/NoSuchAlgorithmException;
        //  104    119    617    622    Ljavax/crypto/NoSuchPaddingException;
        //  104    119    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  104    119    575    602    Ljava/lang/NullPointerException;
        //  119    216    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  119    216    575    602    Ljava/lang/NullPointerException;
        //  216    284    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  216    284    575    602    Ljava/lang/NullPointerException;
        //  284    350    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  284    350    575    602    Ljava/lang/NullPointerException;
        //  360    381    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  360    381    575    602    Ljava/lang/NullPointerException;
        //  384    405    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  384    405    575    602    Ljava/lang/NullPointerException;
        //  408    429    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  408    429    575    602    Ljava/lang/NullPointerException;
        //  432    453    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  432    453    575    602    Ljava/lang/NullPointerException;
        //  456    477    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  456    477    575    602    Ljava/lang/NullPointerException;
        //  480    501    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  480    501    575    602    Ljava/lang/NullPointerException;
        //  504    525    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  504    525    575    602    Ljava/lang/NullPointerException;
        //  528    549    552    612    Landroid/hardware/camera2/CameraAccessException;
        //  528    549    575    602    Ljava/lang/NullPointerException;
        //  553    568    602    607    Ljava/security/NoSuchAlgorithmException;
        //  553    568    607    612    Ljavax/crypto/NoSuchPaddingException;
        //  576    591    594    598    Ljava/security/NoSuchAlgorithmException;
        //  576    591    598    602    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0119:
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
    
    private JSONObject cameraOldAPI() throws JSONException {
        JSONObject jsonObject;
        Camera$CameraInfo camera$CameraInfo;
        int n = 0;
        Camera open;
        String flatten;
        StringBuilder append;
        String s;
        StringBuilder append2;
        String s2;
        Label_0034_Outer:Label_0143_Outer:
        while (true) {
            while (true) {
            Label_0323:
                while (true) {
                Label_0316:
                    while (true) {
                        try {
                            Log.d("cipherName-9", Cipher.getInstance("DES").getAlgorithm());
                            jsonObject = new JSONObject();
                            camera$CameraInfo = new Camera$CameraInfo();
                            n = 0;
                            if (n >= this.numCameras) {
                                return jsonObject;
                            }
                            try {
                                Log.d("cipherName-10", Cipher.getInstance("DES").getAlgorithm());
                                Camera.getCameraInfo(n, camera$CameraInfo);
                                try {
                                    Label_0275: {
                                        while (true) {
                                            try {
                                                Log.d("cipherName-11", Cipher.getInstance("DES").getAlgorithm());
                                                open = Camera.open(n);
                                                flatten = open.getParameters().flatten();
                                                if (Build$VERSION.SDK_INT >= 21) {
                                                    append = new StringBuilder().append(Integer.toString(n)).append("/").append(this.numCameras);
                                                    if (camera$CameraInfo.facing != 0) {
                                                        break Label_0323;
                                                    }
                                                    s = "Back";
                                                    jsonObject.put(append.append(s).toString(), (Object)Utils.semicolonSortedJson(flatten, "=", ";"));
                                                }
                                                else {
                                                    append2 = new StringBuilder().append(Integer.toString(n)).append("/").append(this.numCameras);
                                                    if (camera$CameraInfo.facing != 0) {
                                                        break Label_0275;
                                                    }
                                                    s2 = "Back";
                                                    jsonObject.put(append2.append(s2).toString(), (Object)Utils.semicolonJson(flatten, "=", ";"));
                                                }
                                                open.release();
                                            }
                                            catch (Exception ex) {
                                                try {
                                                    Log.d("cipherName-12", Cipher.getInstance("DES").getAlgorithm());
                                                    jsonObject.put("MISSING permission_CAMERA", (Object)"!Marshmallow and up!");
                                                    break Label_0316;
                                                    s2 = "Front";
                                                    continue;
                                                }
                                                catch (NoSuchAlgorithmException ex2) {}
                                                catch (NoSuchPaddingException ex3) {}
                                            }
                                            break;
                                        }
                                    }
                                }
                                catch (NoSuchAlgorithmException ex4) {}
                                catch (NoSuchPaddingException ex5) {}
                            }
                            catch (NoSuchAlgorithmException ex6) {}
                            catch (NoSuchPaddingException ex7) {}
                        }
                        catch (NoSuchAlgorithmException ex8) {
                            continue Label_0034_Outer;
                        }
                        catch (NoSuchPaddingException ex9) {
                            continue Label_0034_Outer;
                        }
                        break;
                    }
                    ++n;
                    continue Label_0143_Outer;
                }
                s = "Front";
                continue;
            }
        }
    }
    
    private JSONObject classicDrmInfo() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Lorg/json/JSONObject;
        //    18: dup            
        //    19: invokespecial   org/json/JSONObject.<init>:()V
        //    22: astore_2       
        //    23: new             Landroid/drm/DrmManagerClient;
        //    26: dup            
        //    27: aload_0        
        //    28: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //    31: invokespecial   android/drm/DrmManagerClient.<init>:(Landroid/content/Context;)V
        //    34: astore_3       
        //    35: aload_2        
        //    36: ldc_w           "engines"
        //    39: aload_0        
        //    40: aload_3        
        //    41: invokevirtual   android/drm/DrmManagerClient.getAvailableDrmEngines:()[Ljava/lang/String;
        //    44: invokespecial   com/oF2pks/kalturadeviceinfos/Collector.jsonArray:([Ljava/lang/String;)Lorg/json/JSONArray;
        //    47: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //    50: pop            
        //    51: ldc_w           "cipherName-49"
        //    54: ldc             "DES"
        //    56: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    59: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    62: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    65: pop            
        //    66: aload_3        
        //    67: ldc             ""
        //    69: ldc_w           "video/wvm"
        //    72: invokevirtual   android/drm/DrmManagerClient.canHandle:(Ljava/lang/String;Ljava/lang/String;)Z
        //    75: ifeq            201
        //    78: ldc_w           "cipherName-50"
        //    81: ldc             "DES"
        //    83: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    86: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    89: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    92: pop            
        //    93: new             Landroid/drm/DrmInfoRequest;
        //    96: dup            
        //    97: iconst_1       
        //    98: ldc_w           "video/wvm"
        //   101: invokespecial   android/drm/DrmInfoRequest.<init>:(ILjava/lang/String;)V
        //   104: astore          4
        //   106: aload           4
        //   108: ldc_w           "WVPortalKey"
        //   111: ldc_w           "OEM"
        //   114: invokevirtual   android/drm/DrmInfoRequest.put:(Ljava/lang/String;Ljava/lang/Object;)V
        //   117: aload_3        
        //   118: aload           4
        //   120: invokevirtual   android/drm/DrmManagerClient.acquireDrmInfo:(Landroid/drm/DrmInfoRequest;)Landroid/drm/DrmInfo;
        //   123: astore          4
        //   125: aload           4
        //   127: ldc_w           "WVDrmInfoRequestStatusKey"
        //   130: invokevirtual   android/drm/DrmInfo.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   133: checkcast       Ljava/lang/String;
        //   136: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   139: istore_1       
        //   140: iconst_3       
        //   141: anewarray       Ljava/lang/String;
        //   144: dup            
        //   145: iconst_0       
        //   146: ldc_w           "HD_SD"
        //   149: aastore        
        //   150: dup            
        //   151: iconst_1       
        //   152: aconst_null    
        //   153: aastore        
        //   154: dup            
        //   155: iconst_2       
        //   156: ldc_w           "SD"
        //   159: aastore        
        //   160: iload_1        
        //   161: aaload         
        //   162: astore          5
        //   164: aload_2        
        //   165: ldc_w           "widevine"
        //   168: new             Lorg/json/JSONObject;
        //   171: dup            
        //   172: invokespecial   org/json/JSONObject.<init>:()V
        //   175: ldc_w           "version"
        //   178: aload           4
        //   180: ldc_w           "WVDrmInfoRequestVersionKey"
        //   183: invokevirtual   android/drm/DrmInfo.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   186: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   189: ldc_w           "status"
        //   192: aload           5
        //   194: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   197: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   200: pop            
        //   201: aload_3        
        //   202: invokevirtual   android/drm/DrmManagerClient.release:()V
        //   205: aload_2        
        //   206: areturn        
        //   207: astore          4
        //   209: ldc_w           "cipherName-51"
        //   212: ldc             "DES"
        //   214: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   217: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   220: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   223: pop            
        //   224: aload_2        
        //   225: ldc_w           "error"
        //   228: new             Ljava/lang/StringBuilder;
        //   231: dup            
        //   232: invokespecial   java/lang/StringBuilder.<init>:()V
        //   235: aload           4
        //   237: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   240: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   243: bipush          10
        //   245: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   248: aload           4
        //   250: invokestatic    android/util/Log.getStackTraceString:(Ljava/lang/Throwable;)Ljava/lang/String;
        //   253: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   256: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   259: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   262: pop            
        //   263: goto            201
        //   266: astore          5
        //   268: goto            224
        //   271: astore          5
        //   273: goto            224
        //   276: astore          4
        //   278: goto            93
        //   281: astore          4
        //   283: goto            93
        //   286: astore          4
        //   288: goto            66
        //   291: astore          4
        //   293: goto            66
        //   296: astore_2       
        //   297: goto            15
        //   300: astore_2       
        //   301: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     296    300    Ljava/security/NoSuchAlgorithmException;
        //  0      15     300    304    Ljavax/crypto/NoSuchPaddingException;
        //  51     66     286    291    Ljava/security/NoSuchAlgorithmException;
        //  51     66     291    296    Ljavax/crypto/NoSuchPaddingException;
        //  51     66     207    276    Ljava/lang/Exception;
        //  66     78     207    276    Ljava/lang/Exception;
        //  78     93     276    281    Ljava/security/NoSuchAlgorithmException;
        //  78     93     281    286    Ljavax/crypto/NoSuchPaddingException;
        //  78     93     207    276    Ljava/lang/Exception;
        //  93     201    207    276    Ljava/lang/Exception;
        //  209    224    266    271    Ljava/security/NoSuchAlgorithmException;
        //  209    224    271    276    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0093:
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
    
    private JSONObject displayInfo() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Lorg/json/JSONObject;
        //    18: dup            
        //    19: invokespecial   org/json/JSONObject.<init>:()V
        //    22: astore          7
        //    24: aload_0        
        //    25: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //    28: ldc_w           "window"
        //    31: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //    34: checkcast       Landroid/view/WindowManager;
        //    37: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //    42: invokevirtual   android/view/Display.toString:()Ljava/lang/String;
        //    45: astore          6
        //    47: aload           7
        //    49: ldc_w           "ro.sf.lcd_density"
        //    52: ldc_w           "ro.sf.lcd_density"
        //    55: invokestatic    com/oF2pks/kalturadeviceinfos/Utils.getProp:(Ljava/lang/String;)Ljava/lang/String;
        //    58: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //    61: pop            
        //    62: aload           7
        //    64: ldc_w           "RefreshRate"
        //    67: aload_0        
        //    68: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //    71: ldc_w           "window"
        //    74: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //    77: checkcast       Landroid/view/WindowManager;
        //    80: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //    85: invokevirtual   android/view/Display.getRefreshRate:()F
        //    88: f2d            
        //    89: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;D)Lorg/json/JSONObject;
        //    92: pop            
        //    93: aload           6
        //    95: ldc_w           ", real "
        //    98: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   101: istore_2       
        //   102: iload_2        
        //   103: iflt            435
        //   106: ldc_w           "cipherName-43"
        //   109: ldc             "DES"
        //   111: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   114: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   117: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   120: pop            
        //   121: aload           6
        //   123: ldc_w           ", DisplayMetrics{"
        //   126: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   129: istore_3       
        //   130: aload           6
        //   132: astore          5
        //   134: iload_3        
        //   135: iflt            147
        //   138: aload           6
        //   140: iconst_0       
        //   141: iload_3        
        //   142: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   145: astore          5
        //   147: aload           5
        //   149: iload_2        
        //   150: bipush          7
        //   152: iadd           
        //   153: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   156: astore          6
        //   158: aload           7
        //   160: ldc_w           "ScreenSpecs"
        //   163: aload           6
        //   165: iconst_0       
        //   166: aload           6
        //   168: ldc_w           ","
        //   171: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   174: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   177: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   180: pop            
        //   181: aload           7
        //   183: ldc_w           "ViewingMetrics"
        //   186: aload_0        
        //   187: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //   190: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   193: invokevirtual   android/content/res/Resources.getDisplayMetrics:()Landroid/util/DisplayMetrics;
        //   196: invokevirtual   android/util/DisplayMetrics.toString:()Ljava/lang/String;
        //   199: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   202: pop            
        //   203: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   206: bipush          24
        //   208: if_icmplt       672
        //   211: ldc_w           "cipherName-45"
        //   214: ldc             "DES"
        //   216: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   219: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   222: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   225: pop            
        //   226: aload           6
        //   228: ldc_w           ", hdrCapa"
        //   231: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   234: istore_2       
        //   235: aload           6
        //   237: ldc_w           "ilities@"
        //   240: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   243: istore_3       
        //   244: aload           6
        //   246: astore          5
        //   248: iload_2        
        //   249: iflt            294
        //   252: aload           6
        //   254: astore          5
        //   256: iload_3        
        //   257: iflt            294
        //   260: new             Ljava/lang/StringBuilder;
        //   263: dup            
        //   264: invokespecial   java/lang/StringBuilder.<init>:()V
        //   267: aload           6
        //   269: iconst_0       
        //   270: iload_2        
        //   271: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   274: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   277: aload           6
        //   279: iload_3        
        //   280: bipush          16
        //   282: iadd           
        //   283: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   286: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   289: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   292: astore          5
        //   294: aload           7
        //   296: ldc_w           "AllMetrics"
        //   299: aload           5
        //   301: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   304: pop            
        //   305: ldc             ""
        //   307: astore          5
        //   309: iconst_3       
        //   310: anewarray       Ljava/lang/String;
        //   313: astore          8
        //   315: aload           8
        //   317: iconst_0       
        //   318: ldc_w           "DOLBY_VISION"
        //   321: aastore        
        //   322: aload           8
        //   324: iconst_1       
        //   325: ldc_w           "HDR10"
        //   328: aastore        
        //   329: aload           8
        //   331: iconst_2       
        //   332: ldc_w           "HLG"
        //   335: aastore        
        //   336: aload_0        
        //   337: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //   340: ldc_w           "window"
        //   343: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   346: checkcast       Landroid/view/WindowManager;
        //   349: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //   354: invokevirtual   android/view/Display.getHdrCapabilities:()Landroid/view/Display$HdrCapabilities;
        //   357: invokevirtual   android/view/Display$HdrCapabilities.getSupportedHdrTypes:()[I
        //   360: astore          9
        //   362: aload           9
        //   364: arraylength    
        //   365: istore_3       
        //   366: iconst_0       
        //   367: istore_2       
        //   368: iload_2        
        //   369: iload_3        
        //   370: if_icmpge       476
        //   373: aload           9
        //   375: iload_2        
        //   376: iaload         
        //   377: istore          4
        //   379: aload           5
        //   381: astore          6
        //   383: iload           4
        //   385: aload           8
        //   387: arraylength    
        //   388: if_icmpge       424
        //   391: new             Ljava/lang/StringBuilder;
        //   394: dup            
        //   395: invokespecial   java/lang/StringBuilder.<init>:()V
        //   398: aload           5
        //   400: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   403: ldc_w           "+ "
        //   406: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   409: aload           8
        //   411: iload           4
        //   413: iconst_1       
        //   414: isub           
        //   415: aaload         
        //   416: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   419: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   422: astore          6
        //   424: iload_2        
        //   425: iconst_1       
        //   426: iadd           
        //   427: istore_2       
        //   428: aload           6
        //   430: astore          5
        //   432: goto            368
        //   435: ldc_w           "cipherName-44"
        //   438: ldc             "DES"
        //   440: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   443: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   446: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   449: pop            
        //   450: aload           7
        //   452: ldc_w           "Metrics"
        //   455: aload_0        
        //   456: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //   459: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   462: invokevirtual   android/content/res/Resources.getDisplayMetrics:()Landroid/util/DisplayMetrics;
        //   465: invokevirtual   android/util/DisplayMetrics.toString:()Ljava/lang/String;
        //   468: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   471: astore          5
        //   473: aload           5
        //   475: areturn        
        //   476: aload           7
        //   478: ldc_w           "SupportedHdrTypes"
        //   481: aload           5
        //   483: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   486: pop            
        //   487: ldc             ""
        //   489: astore          5
        //   491: aload_0        
        //   492: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //   495: ldc_w           "window"
        //   498: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   501: checkcast       Landroid/view/WindowManager;
        //   504: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //   509: invokevirtual   android/view/Display.getSupportedModes:()[Landroid/view/Display$Mode;
        //   512: astore          6
        //   514: aload           6
        //   516: arraylength    
        //   517: istore_3       
        //   518: iconst_0       
        //   519: istore_2       
        //   520: iload_2        
        //   521: iload_3        
        //   522: if_icmpge       563
        //   525: aload           6
        //   527: iload_2        
        //   528: aaload         
        //   529: astore          8
        //   531: new             Ljava/lang/StringBuilder;
        //   534: dup            
        //   535: invokespecial   java/lang/StringBuilder.<init>:()V
        //   538: aload           5
        //   540: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   543: aload           8
        //   545: invokevirtual   android/view/Display$Mode.toString:()Ljava/lang/String;
        //   548: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   551: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   554: astore          5
        //   556: iload_2        
        //   557: iconst_1       
        //   558: iadd           
        //   559: istore_2       
        //   560: goto            520
        //   563: aload           7
        //   565: ldc_w           "getSupportedModes"
        //   568: aload           5
        //   570: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   573: pop            
        //   574: aload           7
        //   576: astore          5
        //   578: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   581: bipush          21
        //   583: if_icmplt       473
        //   586: ldc_w           "cipherName-46"
        //   589: ldc             "DES"
        //   591: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   594: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   597: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   600: pop            
        //   601: ldc             ""
        //   603: astore          5
        //   605: aload_0        
        //   606: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //   609: ldc_w           "window"
        //   612: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   615: checkcast       Landroid/view/WindowManager;
        //   618: invokeinterface android/view/WindowManager.getDefaultDisplay:()Landroid/view/Display;
        //   623: invokevirtual   android/view/Display.getSupportedRefreshRates:()[F
        //   626: astore          6
        //   628: aload           6
        //   630: arraylength    
        //   631: istore_3       
        //   632: iconst_0       
        //   633: istore_2       
        //   634: iload_2        
        //   635: iload_3        
        //   636: if_icmpge       686
        //   639: aload           6
        //   641: iload_2        
        //   642: faload         
        //   643: fstore_1       
        //   644: new             Ljava/lang/StringBuilder;
        //   647: dup            
        //   648: invokespecial   java/lang/StringBuilder.<init>:()V
        //   651: aload           5
        //   653: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   656: fload_1        
        //   657: invokevirtual   java/lang/StringBuilder.append:(F)Ljava/lang/StringBuilder;
        //   660: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   663: astore          5
        //   665: iload_2        
        //   666: iconst_1       
        //   667: iadd           
        //   668: istore_2       
        //   669: goto            634
        //   672: aload           7
        //   674: ldc_w           "AllMetrics"
        //   677: aload           6
        //   679: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   682: pop            
        //   683: goto            574
        //   686: aload           7
        //   688: ldc_w           "RefreshRates21"
        //   691: aload           5
        //   693: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   696: pop            
        //   697: aload           7
        //   699: areturn        
        //   700: astore          5
        //   702: goto            601
        //   705: astore          5
        //   707: goto            601
        //   710: astore          5
        //   712: goto            226
        //   715: astore          5
        //   717: goto            226
        //   720: astore          5
        //   722: goto            450
        //   725: astore          5
        //   727: goto            450
        //   730: astore          5
        //   732: goto            121
        //   735: astore          5
        //   737: goto            121
        //   740: astore          5
        //   742: goto            15
        //   745: astore          5
        //   747: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     740    745    Ljava/security/NoSuchAlgorithmException;
        //  0      15     745    750    Ljavax/crypto/NoSuchPaddingException;
        //  106    121    730    735    Ljava/security/NoSuchAlgorithmException;
        //  106    121    735    740    Ljavax/crypto/NoSuchPaddingException;
        //  211    226    710    715    Ljava/security/NoSuchAlgorithmException;
        //  211    226    715    720    Ljavax/crypto/NoSuchPaddingException;
        //  435    450    720    725    Ljava/security/NoSuchAlgorithmException;
        //  435    450    725    730    Ljavax/crypto/NoSuchPaddingException;
        //  586    601    700    705    Ljava/security/NoSuchAlgorithmException;
        //  586    601    705    710    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0435:
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
    
    private JSONObject drmInfo() throws JSONException {
        try {
            Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
            return new JSONObject().put("modular", (Object)this.modularDrmInfo()).put("classic", (Object)this.classicDrmInfo());
        }
        catch (NoSuchAlgorithmException ex) {
            return new JSONObject().put("modular", (Object)this.modularDrmInfo()).put("classic", (Object)this.classicDrmInfo());
        }
        catch (NoSuchPaddingException ex2) {
            return new JSONObject().put("modular", (Object)this.modularDrmInfo()).put("classic", (Object)this.classicDrmInfo());
        }
    }
    
    private JSONObject dumpsysL() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-30", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("dumpsys-l", (Object)Utils.getZinfo("dumpsys -l", "/", true).replaceAll("\\.", " "));
                return jsonObject.put("InitSVC", (Object)getZpack("getprop", ".svc.", false).replaceAll("init\\u002Esvc\\u002E", "").replaceAll("\\]: \\[running\\]", "").replaceAll("\\]: \\[stopped\\]", "\u25a0").replaceAll("\\[", "").replaceAll("\\.", " "));
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private static String getCpu() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: ldc_w           "cipherName-98"
        //    18: ldc             "DES"
        //    20: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    23: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    26: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    29: pop            
        //    30: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //    33: ldc_w           "cat /proc/cpuinfo"
        //    36: invokevirtual   java/lang/Runtime.exec:(Ljava/lang/String;)Ljava/lang/Process;
        //    39: invokevirtual   java/lang/Process.getInputStream:()Ljava/io/InputStream;
        //    42: astore_1       
        //    43: new             Ljava/io/BufferedReader;
        //    46: dup            
        //    47: new             Ljava/io/InputStreamReader;
        //    50: dup            
        //    51: aload_1        
        //    52: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    55: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    58: astore_2       
        //    59: aconst_null    
        //    60: astore_0       
        //    61: aload_2        
        //    62: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    65: astore_3       
        //    66: aload_3        
        //    67: ifnull          122
        //    70: ldc_w           "cipherName-99"
        //    73: ldc             "DES"
        //    75: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    78: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    81: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    84: pop            
        //    85: aload_3        
        //    86: ldc_w           "Hardware"
        //    89: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    92: ifeq            102
        //    95: aload_3        
        //    96: bipush          11
        //    98: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   101: areturn        
        //   102: aload_3        
        //   103: ldc_w           "model name"
        //   106: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   109: ifeq            61
        //   112: aload_3        
        //   113: bipush          13
        //   115: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   118: astore_0       
        //   119: goto            61
        //   122: aload_1        
        //   123: invokevirtual   java/io/InputStream.close:()V
        //   126: aload_2        
        //   127: invokevirtual   java/io/BufferedReader.close:()V
        //   130: aload_0        
        //   131: ifnonnull       212
        //   134: ldc_w           "Unknow"
        //   137: areturn        
        //   138: astore_0       
        //   139: ldc_w           "cipherName-100"
        //   142: ldc             "DES"
        //   144: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   147: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   150: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   153: pop            
        //   154: new             Ljava/lang/StringBuilder;
        //   157: dup            
        //   158: invokespecial   java/lang/StringBuilder.<init>:()V
        //   161: ldc_w           "ERROR: "
        //   164: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   167: aload_0        
        //   168: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   171: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   174: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   177: areturn        
        //   178: astore_1       
        //   179: goto            154
        //   182: astore_1       
        //   183: goto            154
        //   186: astore          4
        //   188: goto            85
        //   191: astore          4
        //   193: goto            85
        //   196: astore_0       
        //   197: goto            30
        //   200: astore_0       
        //   201: goto            30
        //   204: astore_0       
        //   205: goto            15
        //   208: astore_0       
        //   209: goto            15
        //   212: aload_0        
        //   213: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     204    208    Ljava/security/NoSuchAlgorithmException;
        //  0      15     208    212    Ljavax/crypto/NoSuchPaddingException;
        //  15     30     196    200    Ljava/security/NoSuchAlgorithmException;
        //  15     30     200    204    Ljavax/crypto/NoSuchPaddingException;
        //  15     30     138    186    Ljava/lang/Exception;
        //  30     59     138    186    Ljava/lang/Exception;
        //  61     66     138    186    Ljava/lang/Exception;
        //  70     85     186    191    Ljava/security/NoSuchAlgorithmException;
        //  70     85     191    196    Ljavax/crypto/NoSuchPaddingException;
        //  70     85     138    186    Ljava/lang/Exception;
        //  85     102    138    186    Ljava/lang/Exception;
        //  102    119    138    186    Ljava/lang/Exception;
        //  122    130    138    186    Ljava/lang/Exception;
        //  139    154    178    182    Ljava/security/NoSuchAlgorithmException;
        //  139    154    182    186    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0085:
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
    
    static String getReport(Context collect, final boolean b) {
        while (true) {
            try {
                Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
                Collector.sReport = null;
                Label_0091: {
                    if (Collector.sReport != null) {
                        break Label_0091;
                    }
                    try {
                        Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
                        collect = (JSONException)new Collector((Context)collect, b).collect();
                        try {
                            try {
                                Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
                                Collector.sReport = ((JSONObject)collect).toString(4);
                                Collector.sReport = Collector.sReport.replace("\\/", "/");
                                return Collector.sReport;
                            }
                            catch (JSONException collect) {
                                try {
                                    Log.d("cipherName-3", Cipher.getInstance("DES").getAlgorithm());
                                    Collector.sReport = "{}";
                                }
                                catch (NoSuchAlgorithmException collect) {}
                                catch (NoSuchPaddingException collect) {}
                            }
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
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
    
    private byte[] getRequestNonce() {
        while (true) {
            try {
                Log.d("cipherName-92", Cipher.getInstance("DES").getAlgorithm());
                final byte[] array = new byte[32];
                new Random().nextBytes(array);
                return array;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private static String getZpack(final String p0, final String p1, final boolean p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: ldc_w           "cipherName-94"
        //    18: ldc             "DES"
        //    20: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    23: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    26: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    29: pop            
        //    30: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //    33: aload_0        
        //    34: invokevirtual   java/lang/Runtime.exec:(Ljava/lang/String;)Ljava/lang/Process;
        //    37: invokevirtual   java/lang/Process.getInputStream:()Ljava/io/InputStream;
        //    40: astore          4
        //    42: new             Ljava/io/BufferedReader;
        //    45: dup            
        //    46: new             Ljava/io/InputStreamReader;
        //    49: dup            
        //    50: aload           4
        //    52: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    55: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    58: astore          5
        //    60: ldc             ""
        //    62: astore_3       
        //    63: aload_3        
        //    64: astore_0       
        //    65: iload_2        
        //    66: ifeq            77
        //    69: aload           5
        //    71: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    74: pop            
        //    75: aload_3        
        //    76: astore_0       
        //    77: aload           5
        //    79: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    82: astore_3       
        //    83: aload_3        
        //    84: ifnull          138
        //    87: ldc_w           "cipherName-95"
        //    90: ldc             "DES"
        //    92: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    95: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    98: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   101: pop            
        //   102: aload_3        
        //   103: aload_1        
        //   104: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   107: ifeq            77
        //   110: new             Ljava/lang/StringBuilder;
        //   113: dup            
        //   114: invokespecial   java/lang/StringBuilder.<init>:()V
        //   117: aload_0        
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: ldc_w           "\n"
        //   124: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   127: aload_3        
        //   128: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   131: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   134: astore_0       
        //   135: goto            77
        //   138: aload           4
        //   140: invokevirtual   java/io/InputStream.close:()V
        //   143: aload           5
        //   145: invokevirtual   java/io/BufferedReader.close:()V
        //   148: aload_0        
        //   149: invokevirtual   java/lang/String.length:()I
        //   152: ifeq            166
        //   155: aload_0        
        //   156: ldc_w           "\\n"
        //   159: ldc_w           " /"
        //   162: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   165: areturn        
        //   166: ldc_w           "Unknow"
        //   169: areturn        
        //   170: astore_0       
        //   171: ldc_w           "cipherName-96"
        //   174: ldc             "DES"
        //   176: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   179: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   182: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   185: pop            
        //   186: new             Ljava/lang/StringBuilder;
        //   189: dup            
        //   190: invokespecial   java/lang/StringBuilder.<init>:()V
        //   193: ldc_w           "ERROR: "
        //   196: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   199: aload_0        
        //   200: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   203: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   206: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   209: areturn        
        //   210: astore_1       
        //   211: goto            186
        //   214: astore_1       
        //   215: goto            186
        //   218: astore          6
        //   220: goto            102
        //   223: astore          6
        //   225: goto            102
        //   228: astore_3       
        //   229: goto            30
        //   232: astore_3       
        //   233: goto            30
        //   236: astore_3       
        //   237: goto            15
        //   240: astore_3       
        //   241: goto            15
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     236    240    Ljava/security/NoSuchAlgorithmException;
        //  0      15     240    244    Ljavax/crypto/NoSuchPaddingException;
        //  15     30     228    232    Ljava/security/NoSuchAlgorithmException;
        //  15     30     232    236    Ljavax/crypto/NoSuchPaddingException;
        //  15     30     170    218    Ljava/lang/Exception;
        //  30     60     170    218    Ljava/lang/Exception;
        //  69     75     170    218    Ljava/lang/Exception;
        //  77     83     170    218    Ljava/lang/Exception;
        //  87     102    218    223    Ljava/security/NoSuchAlgorithmException;
        //  87     102    223    228    Ljavax/crypto/NoSuchPaddingException;
        //  87     102    170    218    Ljava/lang/Exception;
        //  102    135    170    218    Ljava/lang/Exception;
        //  138    166    170    218    Ljava/lang/Exception;
        //  171    186    210    214    Ljava/security/NoSuchAlgorithmException;
        //  171    186    214    218    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0102:
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
    
    @NonNull
    private JSONArray jsonArray(final String[] array) {
        while (true) {
            try {
                Log.d("cipherName-52", Cipher.getInstance("DES").getAlgorithm());
                final JSONArray jsonArray = new JSONArray();
                final int length = array.length;
                int n = 0;
                while (true) {
                    if (n >= length) {
                        return jsonArray;
                    }
                    final String s = array[n];
                    try {
                        Log.d("cipherName-53", Cipher.getInstance("DES").getAlgorithm());
                        Label_0085: {
                            if (TextUtils.isEmpty((CharSequence)s)) {
                                break Label_0085;
                            }
                            try {
                                Log.d("cipherName-54", Cipher.getInstance("DES").getAlgorithm());
                                jsonArray.put((Object)s);
                                ++n;
                                continue;
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
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
    
    private String mediaCodecInfo(MediaCodecInfo string) {
        while (true) {
            try {
                Log.d("cipherName-55", Cipher.getInstance("DES").getAlgorithm());
                final Object o = "";
                final String[] supportedTypes = string.getSupportedTypes();
                int n = 0;
                string = (MediaCodecInfo)o;
                while (true) {
                    if (n >= supportedTypes.length) {
                        return (String)string;
                    }
                    try {
                        Log.d("cipherName-56", Cipher.getInstance("DES").getAlgorithm());
                        string = (MediaCodecInfo)((String)string + "~" + supportedTypes[n]);
                        ++n;
                        continue;
                    }
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex2) {}
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
    
    private JSONObject mediaCodecInfo() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Ljava/util/ArrayList;
        //    18: dup            
        //    19: invokespecial   java/util/ArrayList.<init>:()V
        //    22: astore          6
        //    24: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    27: bipush          21
        //    29: if_icmplt       193
        //    32: ldc_w           "cipherName-58"
        //    35: ldc             "DES"
        //    37: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    40: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    43: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    46: pop            
        //    47: aload           6
        //    49: new             Landroid/media/MediaCodecList;
        //    52: dup            
        //    53: iconst_1       
        //    54: invokespecial   android/media/MediaCodecList.<init>:(I)V
        //    57: invokevirtual   android/media/MediaCodecList.getCodecInfos:()[Landroid/media/MediaCodecInfo;
        //    60: invokestatic    java/util/Collections.addAll:(Ljava/util/Collection;[Ljava/lang/Object;)Z
        //    63: pop            
        //    64: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    67: bipush          21
        //    69: if_icmplt       80
        //    72: aload           6
        //    74: getstatic       com/oF2pks/kalturadeviceinfos/Collector.McComparator:Ljava/util/Comparator;
        //    77: invokestatic    java/util/Collections.sort:(Ljava/util/List;Ljava/util/Comparator;)V
        //    80: new             Lorg/json/JSONObject;
        //    83: dup            
        //    84: invokespecial   org/json/JSONObject.<init>:()V
        //    87: astore_3       
        //    88: new             Lorg/json/JSONObject;
        //    91: dup            
        //    92: invokespecial   org/json/JSONObject.<init>:()V
        //    95: astore          4
        //    97: new             Lorg/json/JSONObject;
        //   100: dup            
        //   101: invokespecial   org/json/JSONObject.<init>:()V
        //   104: astore          5
        //   106: aload           6
        //   108: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   111: astore          6
        //   113: aload           6
        //   115: invokeinterface java/util/Iterator.hasNext:()Z
        //   120: ifeq            286
        //   123: aload           6
        //   125: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   130: checkcast       Landroid/media/MediaCodecInfo;
        //   133: astore          7
        //   135: ldc_w           "cipherName-61"
        //   138: ldc             "DES"
        //   140: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   143: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   146: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   149: pop            
        //   150: aload           7
        //   152: invokevirtual   android/media/MediaCodecInfo.isEncoder:()Z
        //   155: ifeq            251
        //   158: ldc_w           "cipherName-62"
        //   161: ldc             "DES"
        //   163: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   166: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   169: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   172: pop            
        //   173: aload           5
        //   175: aload           7
        //   177: invokevirtual   android/media/MediaCodecInfo.getName:()Ljava/lang/String;
        //   180: aload_0        
        //   181: aload           7
        //   183: invokespecial   com/oF2pks/kalturadeviceinfos/Collector.mediaCodecInfo:(Landroid/media/MediaCodecInfo;)Ljava/lang/String;
        //   186: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   189: pop            
        //   190: goto            113
        //   193: ldc_w           "cipherName-59"
        //   196: ldc             "DES"
        //   198: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   201: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   204: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   207: pop            
        //   208: iconst_0       
        //   209: istore_1       
        //   210: invokestatic    android/media/MediaCodecList.getCodecCount:()I
        //   213: istore_2       
        //   214: iload_1        
        //   215: iload_2        
        //   216: if_icmpge       64
        //   219: ldc_w           "cipherName-60"
        //   222: ldc             "DES"
        //   224: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   227: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   230: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   233: pop            
        //   234: aload           6
        //   236: iload_1        
        //   237: invokestatic    android/media/MediaCodecList.getCodecInfoAt:(I)Landroid/media/MediaCodecInfo;
        //   240: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   243: pop            
        //   244: iload_1        
        //   245: iconst_1       
        //   246: iadd           
        //   247: istore_1       
        //   248: goto            214
        //   251: ldc_w           "cipherName-63"
        //   254: ldc             "DES"
        //   256: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   259: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   262: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   265: pop            
        //   266: aload           4
        //   268: aload           7
        //   270: invokevirtual   android/media/MediaCodecInfo.getName:()Ljava/lang/String;
        //   273: aload_0        
        //   274: aload           7
        //   276: invokespecial   com/oF2pks/kalturadeviceinfos/Collector.mediaCodecInfo:(Landroid/media/MediaCodecInfo;)Ljava/lang/String;
        //   279: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   282: pop            
        //   283: goto            113
        //   286: aload_3        
        //   287: ldc_w           "decoders"
        //   290: aload           4
        //   292: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   295: pop            
        //   296: aload_3        
        //   297: ldc_w           "encoders"
        //   300: aload           5
        //   302: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   305: pop            
        //   306: aload_3        
        //   307: areturn        
        //   308: astore          8
        //   310: goto            266
        //   313: astore          8
        //   315: goto            266
        //   318: astore          8
        //   320: goto            173
        //   323: astore          8
        //   325: goto            173
        //   328: astore          8
        //   330: goto            150
        //   333: astore          8
        //   335: goto            150
        //   338: astore_3       
        //   339: goto            234
        //   342: astore_3       
        //   343: goto            234
        //   346: astore_3       
        //   347: goto            208
        //   350: astore_3       
        //   351: goto            208
        //   354: astore_3       
        //   355: goto            47
        //   358: astore_3       
        //   359: goto            47
        //   362: astore_3       
        //   363: goto            15
        //   366: astore_3       
        //   367: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     362    366    Ljava/security/NoSuchAlgorithmException;
        //  0      15     366    370    Ljavax/crypto/NoSuchPaddingException;
        //  32     47     354    358    Ljava/security/NoSuchAlgorithmException;
        //  32     47     358    362    Ljavax/crypto/NoSuchPaddingException;
        //  135    150    328    333    Ljava/security/NoSuchAlgorithmException;
        //  135    150    333    338    Ljavax/crypto/NoSuchPaddingException;
        //  158    173    318    323    Ljava/security/NoSuchAlgorithmException;
        //  158    173    323    328    Ljavax/crypto/NoSuchPaddingException;
        //  193    208    346    350    Ljava/security/NoSuchAlgorithmException;
        //  193    208    350    354    Ljavax/crypto/NoSuchPaddingException;
        //  219    234    338    342    Ljava/security/NoSuchAlgorithmException;
        //  219    234    342    346    Ljavax/crypto/NoSuchPaddingException;
        //  251    266    308    313    Ljava/security/NoSuchAlgorithmException;
        //  251    266    313    318    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0193:
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
    
    private JSONObject meta() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
                final TimeZone timeZone = TimeZone.getTimeZone("UTC");
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                simpleDateFormat.setTimeZone(timeZone);
                return new JSONObject().put("versionName", (Object)"1.3.1-6").put("versionCode", 6).put("timestamp", (Object)simpleDateFormat.format(new Date()));
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private JSONObject modularDrmInfo() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-65", Cipher.getInstance("DES").getAlgorithm());
                Label_0056: {
                    if (Build$VERSION.SDK_INT < 18) {
                        break Label_0056;
                    }
                    try {
                        Log.d("cipherName-66", Cipher.getInstance("DES").getAlgorithm());
                        return new JSONObject().put("widevine", (Object)this.widevineModularDrmInfo());
                        try {
                            Log.d("cipherName-67", Cipher.getInstance("DES").getAlgorithm());
                            return null;
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
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
    
    private JSONObject rootInfo() throws JSONException {
        int n = 0;
        while (true) {
            try {
                Log.d("cipherName-89", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                final String[] array = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/data/adb/magisk.img", "/data/magisk.img" };
                final JSONArray jsonArray = new JSONArray();
                final int length = array.length;
                while (true) {
                    Label_0198: {
                        if (n >= length) {
                            break Label_0198;
                        }
                        final String s = array[n];
                        try {
                            Log.d("cipherName-90", Cipher.getInstance("DES").getAlgorithm());
                            Label_0191: {
                                if (!new File(s).exists()) {
                                    break Label_0191;
                                }
                                try {
                                    Log.d("cipherName-91", Cipher.getInstance("DES").getAlgorithm());
                                    jsonArray.put((Object)s);
                                    ++n;
                                    continue;
                                    jsonObject.put("existingFiles", (Object)jsonArray);
                                    return jsonObject;
                                }
                                catch (NoSuchAlgorithmException ex) {}
                                catch (NoSuchPaddingException ex2) {}
                            }
                        }
                        catch (NoSuchAlgorithmException ex3) {}
                        catch (NoSuchPaddingException ex4) {}
                    }
                }
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
    
    private JSONObject securityProviders() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-87", Cipher.getInstance("DES").getAlgorithm());
                final Provider[] providers = Security.getProviders();
                final JSONObject jsonObject = new JSONObject();
                int n = 0;
                while (true) {
                    if (n >= providers.length) {
                        return jsonObject;
                    }
                    try {
                        Log.d("cipherName-88", Cipher.getInstance("DES").getAlgorithm());
                        final JSONObject put = new JSONObject().put("Info", (Object)providers[n].getInfo());
                        put.put("Version", (Object)String.valueOf(providers[n].getVersion()));
                        put.put("Class", (Object)providers[n].getClass().getName());
                        jsonObject.put(providers[n].getName(), (Object)put);
                        ++n;
                        continue;
                    }
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex2) {}
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
    
    private JSONObject selfMounts() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Lorg/json/JSONObject;
        //    18: dup            
        //    19: invokespecial   org/json/JSONObject.<init>:()V
        //    22: astore          4
        //    24: new             Lorg/json/JSONObject;
        //    27: dup            
        //    28: invokespecial   org/json/JSONObject.<init>:()V
        //    31: astore_2       
        //    32: iconst_3       
        //    33: anewarray       Ljava/lang/String;
        //    36: astore_1       
        //    37: aload_2        
        //    38: astore_1       
        //    39: ldc_w           "cipherName-24"
        //    42: ldc             "DES"
        //    44: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    47: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    50: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    53: pop            
        //    54: aload_2        
        //    55: astore_1       
        //    56: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //    59: ldc_w           "cat /proc/self/mounts"
        //    62: invokevirtual   java/lang/Runtime.exec:(Ljava/lang/String;)Ljava/lang/Process;
        //    65: invokevirtual   java/lang/Process.getInputStream:()Ljava/io/InputStream;
        //    68: astore_3       
        //    69: aload_2        
        //    70: astore_1       
        //    71: new             Ljava/io/BufferedReader;
        //    74: dup            
        //    75: new             Ljava/io/InputStreamReader;
        //    78: dup            
        //    79: aload_3        
        //    80: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    83: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    86: astore          5
        //    88: aload_2        
        //    89: astore_1       
        //    90: new             Ljava/util/TreeSet;
        //    93: dup            
        //    94: invokespecial   java/util/TreeSet.<init>:()V
        //    97: astore          6
        //    99: aload_2        
        //   100: astore_1       
        //   101: aload           5
        //   103: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   106: astore          7
        //   108: aload           7
        //   110: ifnull          201
        //   113: aload_2        
        //   114: astore_1       
        //   115: ldc_w           "cipherName-25"
        //   118: ldc             "DES"
        //   120: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   123: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   126: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   129: pop            
        //   130: aload_2        
        //   131: astore_1       
        //   132: aload           6
        //   134: aload           7
        //   136: invokeinterface java/util/SortedSet.add:(Ljava/lang/Object;)Z
        //   141: pop            
        //   142: goto            99
        //   145: astore_2       
        //   146: ldc_w           "cipherName-29"
        //   149: ldc             "DES"
        //   151: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   154: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   157: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   160: pop            
        //   161: aload_1        
        //   162: ldc_w           "error"
        //   165: new             Ljava/lang/StringBuilder;
        //   168: dup            
        //   169: invokespecial   java/lang/StringBuilder.<init>:()V
        //   172: aload_2        
        //   173: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   176: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   179: bipush          10
        //   181: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   184: aload_2        
        //   185: invokestatic    android/util/Log.getStackTraceString:(Ljava/lang/Throwable;)Ljava/lang/String;
        //   188: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   191: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   194: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   197: pop            
        //   198: aload           4
        //   200: areturn        
        //   201: aload_2        
        //   202: astore_1       
        //   203: aload_3        
        //   204: invokevirtual   java/io/InputStream.close:()V
        //   207: aload_2        
        //   208: astore_1       
        //   209: aload           5
        //   211: invokevirtual   java/io/BufferedReader.close:()V
        //   214: aload_2        
        //   215: astore_1       
        //   216: aload           6
        //   218: invokeinterface java/util/SortedSet.iterator:()Ljava/util/Iterator;
        //   223: astore          5
        //   225: aload_2        
        //   226: astore_1       
        //   227: aload           5
        //   229: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   234: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   237: ldc_w           " "
        //   240: iconst_3       
        //   241: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   244: astore_3       
        //   245: aload_2        
        //   246: astore_1       
        //   247: aload_2        
        //   248: aload_3        
        //   249: iconst_1       
        //   250: aaload         
        //   251: aload_3        
        //   252: iconst_2       
        //   253: aaload         
        //   254: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   257: pop            
        //   258: aload_3        
        //   259: iconst_0       
        //   260: aaload         
        //   261: astore_3       
        //   262: aload_2        
        //   263: astore_1       
        //   264: aload_3        
        //   265: astore_2       
        //   266: aload           5
        //   268: invokeinterface java/util/Iterator.hasNext:()Z
        //   273: ifeq            436
        //   276: ldc_w           "cipherName-26"
        //   279: ldc             "DES"
        //   281: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   284: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   287: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   290: pop            
        //   291: aload           5
        //   293: invokeinterface java/util/Iterator.hasNext:()Z
        //   298: ifeq            497
        //   301: ldc_w           "cipherName-27"
        //   304: ldc             "DES"
        //   306: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   309: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   312: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   315: pop            
        //   316: aload           5
        //   318: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   323: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   326: ldc_w           " "
        //   329: iconst_3       
        //   330: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   333: astore_3       
        //   334: aload_2        
        //   335: aload_3        
        //   336: iconst_0       
        //   337: aaload         
        //   338: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   341: ifne            418
        //   344: ldc_w           "cipherName-28"
        //   347: ldc             "DES"
        //   349: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   352: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   355: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   358: pop            
        //   359: aload           4
        //   361: new             Ljava/lang/StringBuilder;
        //   364: dup            
        //   365: invokespecial   java/lang/StringBuilder.<init>:()V
        //   368: ldc_w           "\u25a0"
        //   371: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   374: aload_2        
        //   375: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   378: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   381: aload_1        
        //   382: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   385: pop            
        //   386: new             Lorg/json/JSONObject;
        //   389: dup            
        //   390: invokespecial   org/json/JSONObject.<init>:()V
        //   393: astore_2       
        //   394: aload_2        
        //   395: astore_1       
        //   396: aload_2        
        //   397: aload_3        
        //   398: iconst_1       
        //   399: aaload         
        //   400: aload_3        
        //   401: iconst_2       
        //   402: aaload         
        //   403: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   406: pop            
        //   407: aload_3        
        //   408: iconst_0       
        //   409: aaload         
        //   410: astore_3       
        //   411: aload_2        
        //   412: astore_1       
        //   413: aload_3        
        //   414: astore_2       
        //   415: goto            266
        //   418: aload_1        
        //   419: aload_3        
        //   420: iconst_1       
        //   421: aaload         
        //   422: aload_3        
        //   423: iconst_2       
        //   424: aaload         
        //   425: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   428: pop            
        //   429: goto            291
        //   432: astore_2       
        //   433: goto            146
        //   436: aload           4
        //   438: areturn        
        //   439: astore_3       
        //   440: goto            161
        //   443: astore_3       
        //   444: goto            161
        //   447: astore          6
        //   449: goto            359
        //   452: astore          6
        //   454: goto            359
        //   457: astore_3       
        //   458: goto            316
        //   461: astore_3       
        //   462: goto            316
        //   465: astore_3       
        //   466: goto            291
        //   469: astore_3       
        //   470: goto            291
        //   473: astore_1       
        //   474: goto            130
        //   477: astore_1       
        //   478: goto            130
        //   481: astore_1       
        //   482: goto            54
        //   485: astore_1       
        //   486: goto            54
        //   489: astore_1       
        //   490: goto            15
        //   493: astore_1       
        //   494: goto            15
        //   497: aload_2        
        //   498: astore_3       
        //   499: goto            413
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     489    493    Ljava/security/NoSuchAlgorithmException;
        //  0      15     493    497    Ljavax/crypto/NoSuchPaddingException;
        //  39     54     481    485    Ljava/security/NoSuchAlgorithmException;
        //  39     54     485    489    Ljavax/crypto/NoSuchPaddingException;
        //  39     54     145    146    Ljava/lang/Exception;
        //  56     69     145    146    Ljava/lang/Exception;
        //  71     88     145    146    Ljava/lang/Exception;
        //  90     99     145    146    Ljava/lang/Exception;
        //  101    108    145    146    Ljava/lang/Exception;
        //  115    130    473    477    Ljava/security/NoSuchAlgorithmException;
        //  115    130    477    481    Ljavax/crypto/NoSuchPaddingException;
        //  115    130    145    146    Ljava/lang/Exception;
        //  132    142    145    146    Ljava/lang/Exception;
        //  146    161    439    443    Ljava/security/NoSuchAlgorithmException;
        //  146    161    443    447    Ljavax/crypto/NoSuchPaddingException;
        //  203    207    145    146    Ljava/lang/Exception;
        //  209    214    145    146    Ljava/lang/Exception;
        //  216    225    145    146    Ljava/lang/Exception;
        //  227    245    145    146    Ljava/lang/Exception;
        //  247    258    145    146    Ljava/lang/Exception;
        //  266    276    432    436    Ljava/lang/Exception;
        //  276    291    465    469    Ljava/security/NoSuchAlgorithmException;
        //  276    291    469    473    Ljavax/crypto/NoSuchPaddingException;
        //  276    291    432    436    Ljava/lang/Exception;
        //  291    301    432    436    Ljava/lang/Exception;
        //  301    316    457    461    Ljava/security/NoSuchAlgorithmException;
        //  301    316    461    465    Ljavax/crypto/NoSuchPaddingException;
        //  301    316    432    436    Ljava/lang/Exception;
        //  316    344    432    436    Ljava/lang/Exception;
        //  344    359    447    452    Ljava/security/NoSuchAlgorithmException;
        //  344    359    452    457    Ljavax/crypto/NoSuchPaddingException;
        //  344    359    432    436    Ljava/lang/Exception;
        //  359    394    432    436    Ljava/lang/Exception;
        //  396    407    145    146    Ljava/lang/Exception;
        //  418    429    432    436    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private JSONObject systemArch() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                final JSONObject jsonObject2 = new JSONObject();
                jsonObject.put("uname-m", (Object)Utils.getZinfo("uname -m", "", false));
                Label_0137: {
                    if (Build$VERSION.SDK_INT < 21) {
                        break Label_0137;
                    }
                    try {
                        Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
                        jsonObject2.put("SUPPORTED_ABIS", (Object)new JSONArray((Object)Build.SUPPORTED_ABIS));
                        jsonObject2.put("SUPPORTED_32_BIT_ABIS", (Object)new JSONArray((Object)Build.SUPPORTED_32_BIT_ABIS));
                        jsonObject2.put("SUPPORTED_64_BIT_ABIS", (Object)new JSONArray((Object)Build.SUPPORTED_64_BIT_ABIS));
                        return jsonObject.put(System.getProperty("os.arch"), (Object)jsonObject2);
                        try {
                            Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
                            jsonObject2.put("CPU_ABI", (Object)Build.CPU_ABI);
                            jsonObject2.put("CPU_ABI2", (Object)Build.CPU_ABI2);
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
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
    
    private JSONObject systemFL() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Lorg/json/JSONObject;
        //    18: dup            
        //    19: invokespecial   org/json/JSONObject.<init>:()V
        //    22: astore          4
        //    24: new             Ljava/util/TreeSet;
        //    27: dup            
        //    28: invokespecial   java/util/TreeSet.<init>:()V
        //    31: astore          5
        //    33: ldc             ""
        //    35: astore_3       
        //    36: aload_0        
        //    37: getfield        com/oF2pks/kalturadeviceinfos/Collector.mContext:Landroid/content/Context;
        //    40: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    43: astore          7
        //    45: aload           7
        //    47: invokevirtual   android/content/pm/PackageManager.getSystemAvailableFeatures:()[Landroid/content/pm/FeatureInfo;
        //    50: astore          6
        //    52: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    55: bipush          19
        //    57: if_icmplt       156
        //    60: ldc_w           "cipherName-32"
        //    63: ldc             "DES"
        //    65: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    68: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    71: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    74: pop            
        //    75: aload           7
        //    77: invokevirtual   android/content/pm/PackageManager.getSystemSharedLibraryNames:()[Ljava/lang/String;
        //    80: astore          7
        //    82: aload           7
        //    84: invokestatic    java/util/Arrays.sort:([Ljava/lang/Object;)V
        //    87: aload           7
        //    89: arraylength    
        //    90: istore_2       
        //    91: iconst_0       
        //    92: istore_1       
        //    93: iload_1        
        //    94: iload_2        
        //    95: if_icmpge       146
        //    98: aload           7
        //   100: iload_1        
        //   101: aaload         
        //   102: astore          8
        //   104: new             Ljava/lang/StringBuilder;
        //   107: dup            
        //   108: invokespecial   java/lang/StringBuilder.<init>:()V
        //   111: aload_3        
        //   112: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   115: ldc_w           " / "
        //   118: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   121: aload           8
        //   123: ldc_w           "\\."
        //   126: ldc_w           " "
        //   129: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   132: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   135: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   138: astore_3       
        //   139: iload_1        
        //   140: iconst_1       
        //   141: iadd           
        //   142: istore_1       
        //   143: goto            93
        //   146: aload           4
        //   148: ldc_w           "SystemSharedLibraryNames"
        //   151: aload_3        
        //   152: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   155: pop            
        //   156: ldc             ""
        //   158: astore_3       
        //   159: aload           6
        //   161: arraylength    
        //   162: istore_2       
        //   163: iconst_0       
        //   164: istore_1       
        //   165: iload_1        
        //   166: iload_2        
        //   167: if_icmpge       366
        //   170: aload           6
        //   172: iload_1        
        //   173: aaload         
        //   174: astore          7
        //   176: ldc_w           "cipherName-33"
        //   179: ldc             "DES"
        //   181: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   184: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   187: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   190: pop            
        //   191: aload           7
        //   193: ifnull          308
        //   196: aload           7
        //   198: getfield        android/content/pm/FeatureInfo.name:Ljava/lang/String;
        //   201: ifnull          308
        //   204: ldc_w           "cipherName-34"
        //   207: ldc             "DES"
        //   209: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   212: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   215: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   218: pop            
        //   219: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   222: bipush          24
        //   224: if_icmplt       350
        //   227: ldc_w           "cipherName-35"
        //   230: ldc             "DES"
        //   232: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   235: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   238: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   241: pop            
        //   242: aload           7
        //   244: getfield        android/content/pm/FeatureInfo.version:I
        //   247: ifeq            315
        //   250: ldc_w           "cipherName-36"
        //   253: ldc             "DES"
        //   255: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   258: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   261: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   264: pop            
        //   265: aload           5
        //   267: new             Ljava/lang/StringBuilder;
        //   270: dup            
        //   271: invokespecial   java/lang/StringBuilder.<init>:()V
        //   274: aload           7
        //   276: getfield        android/content/pm/FeatureInfo.name:Ljava/lang/String;
        //   279: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   282: ldc_w           "\u25a0"
        //   285: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   288: aload           7
        //   290: getfield        android/content/pm/FeatureInfo.version:I
        //   293: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //   296: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   299: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   302: invokeinterface java/util/SortedSet.add:(Ljava/lang/Object;)Z
        //   307: pop            
        //   308: iload_1        
        //   309: iconst_1       
        //   310: iadd           
        //   311: istore_1       
        //   312: goto            165
        //   315: aload           5
        //   317: new             Ljava/lang/StringBuilder;
        //   320: dup            
        //   321: invokespecial   java/lang/StringBuilder.<init>:()V
        //   324: aload           7
        //   326: getfield        android/content/pm/FeatureInfo.name:Ljava/lang/String;
        //   329: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   332: ldc_w           " °"
        //   335: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   338: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   341: invokeinterface java/util/SortedSet.add:(Ljava/lang/Object;)Z
        //   346: pop            
        //   347: goto            308
        //   350: aload           5
        //   352: aload           7
        //   354: getfield        android/content/pm/FeatureInfo.name:Ljava/lang/String;
        //   357: invokeinterface java/util/SortedSet.add:(Ljava/lang/Object;)Z
        //   362: pop            
        //   363: goto            308
        //   366: aload           5
        //   368: invokeinterface java/util/SortedSet.iterator:()Ljava/util/Iterator;
        //   373: astore          5
        //   375: aload           5
        //   377: invokeinterface java/util/Iterator.hasNext:()Z
        //   382: ifeq            437
        //   385: ldc_w           "cipherName-37"
        //   388: ldc             "DES"
        //   390: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   393: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   396: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   399: pop            
        //   400: new             Ljava/lang/StringBuilder;
        //   403: dup            
        //   404: invokespecial   java/lang/StringBuilder.<init>:()V
        //   407: aload_3        
        //   408: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   411: ldc_w           "/ "
        //   414: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   417: aload           5
        //   419: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   424: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   427: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   430: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   433: astore_3       
        //   434: goto            375
        //   437: aload           4
        //   439: ldc_w           "Features"
        //   442: aload_3        
        //   443: ldc_w           "\\."
        //   446: ldc_w           " "
        //   449: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   452: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   455: areturn        
        //   456: astore          6
        //   458: goto            400
        //   461: astore          6
        //   463: goto            400
        //   466: astore          8
        //   468: goto            265
        //   471: astore          8
        //   473: goto            265
        //   476: astore          8
        //   478: goto            242
        //   481: astore          8
        //   483: goto            242
        //   486: astore          8
        //   488: goto            219
        //   491: astore          8
        //   493: goto            219
        //   496: astore          8
        //   498: goto            191
        //   501: astore          8
        //   503: goto            191
        //   506: astore          8
        //   508: goto            75
        //   511: astore          8
        //   513: goto            75
        //   516: astore_3       
        //   517: goto            15
        //   520: astore_3       
        //   521: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     516    520    Ljava/security/NoSuchAlgorithmException;
        //  0      15     520    524    Ljavax/crypto/NoSuchPaddingException;
        //  60     75     506    511    Ljava/security/NoSuchAlgorithmException;
        //  60     75     511    516    Ljavax/crypto/NoSuchPaddingException;
        //  176    191    496    501    Ljava/security/NoSuchAlgorithmException;
        //  176    191    501    506    Ljavax/crypto/NoSuchPaddingException;
        //  204    219    486    491    Ljava/security/NoSuchAlgorithmException;
        //  204    219    491    496    Ljavax/crypto/NoSuchPaddingException;
        //  227    242    476    481    Ljava/security/NoSuchAlgorithmException;
        //  227    242    481    486    Ljavax/crypto/NoSuchPaddingException;
        //  250    265    466    471    Ljava/security/NoSuchAlgorithmException;
        //  250    265    471    476    Ljavax/crypto/NoSuchPaddingException;
        //  385    400    456    461    Ljava/security/NoSuchAlgorithmException;
        //  385    400    461    466    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private JSONObject systemInfo() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-86", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("Cpu", (Object)getCpu());
                jsonObject.put("BOARD", (Object)Utils.getProp("ro.board.platform"));
                jsonObject.put("HARDWARE", (Object)Build.HARDWARE);
                jsonObject.put("nbrCams", this.numCameras);
                jsonObject.put("KernelFull", (Object)Utils.getZinfo("uname -a", "", false));
                jsonObject.put("CODENAME", (Object)Build$VERSION.CODENAME);
                jsonObject.put("BOOTLOADER", (Object)Build.BOOTLOADER);
                jsonObject.put("ro.boot.bootdevice", (Object)Utils.getProp("ro.boot.bootdevice"));
                jsonObject.put("BRAND", (Object)Build.BRAND);
                jsonObject.put("MODEL", (Object)Build.MODEL);
                jsonObject.put("MANUFACTURER", (Object)Build.MANUFACTURER);
                jsonObject.put("DEVICE", (Object)Build.DEVICE);
                jsonObject.put("ID", (Object)Build.ID);
                jsonObject.put("HOST", (Object)Build.HOST);
                jsonObject.put("PRODUCT", (Object)Build.PRODUCT);
                jsonObject.put("TYPE", (Object)Build.TYPE);
                jsonObject.put("USER", (Object)Build.USER);
                jsonObject.put("DISPLAY", (Object)Build.DISPLAY);
                jsonObject.put("INCREMENTAL", (Object)Build$VERSION.INCREMENTAL);
                jsonObject.put("RadioVersion", (Object)Build.getRadioVersion());
                jsonObject.put("FINGERPRINT", (Object)Build.FINGERPRINT);
                jsonObject.put("FINGERPRINTvendor", (Object)Utils.getProp("ro.vendor.build.fingerprint"));
                final ConfigurationInfo deviceConfigurationInfo = ((ActivityManager)this.mContext.getSystemService("activity")).getDeviceConfigurationInfo();
                jsonObject.put("glEsVersion", (Object)deviceConfigurationInfo.getGlEsVersion());
                jsonObject.put("reqGlEsVersion", (Object)String.valueOf(deviceConfigurationInfo.reqGlEsVersion));
                return jsonObject;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    @RequiresApi(api = 21)
    private JSONObject tech2API(final CameraCharacteristics p0) throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: new             Lorg/json/JSONObject;
        //    18: dup            
        //    19: invokespecial   org/json/JSONObject.<init>:()V
        //    22: astore          9
        //    24: ldc             ""
        //    26: astore          6
        //    28: ldc             ""
        //    30: astore          8
        //    32: aload_1        
        //    33: getstatic       android/hardware/camera2/CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //    36: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //    39: checkcast       [I
        //    42: astore          11
        //    44: iconst_2       
        //    45: anewarray       Ljava/lang/String;
        //    48: astore          10
        //    50: aload           10
        //    52: iconst_0       
        //    53: ldc_w           "OFF"
        //    56: aastore        
        //    57: aload           10
        //    59: iconst_1       
        //    60: ldc_w           "ON"
        //    63: aastore        
        //    64: bipush          13
        //    66: anewarray       Ljava/lang/String;
        //    69: astore          12
        //    71: aload           12
        //    73: iconst_0       
        //    74: ldc_w           "BACKWARD_COMPATIBLE"
        //    77: aastore        
        //    78: aload           12
        //    80: iconst_1       
        //    81: ldc_w           "MANUAL_SENSOR"
        //    84: aastore        
        //    85: aload           12
        //    87: iconst_2       
        //    88: ldc_w           "MANUAL_POST_PROCESSING"
        //    91: aastore        
        //    92: aload           12
        //    94: iconst_3       
        //    95: ldc_w           "RAW"
        //    98: aastore        
        //    99: aload           12
        //   101: iconst_4       
        //   102: ldc_w           "PRIVATE_REPROCESSING"
        //   105: aastore        
        //   106: aload           12
        //   108: iconst_5       
        //   109: ldc_w           "READ_SENSOR_SETTINGS"
        //   112: aastore        
        //   113: aload           12
        //   115: bipush          6
        //   117: ldc_w           "BURST_CAPTURE"
        //   120: aastore        
        //   121: aload           12
        //   123: bipush          7
        //   125: ldc_w           "YUV_REPROCESSING"
        //   128: aastore        
        //   129: aload           12
        //   131: bipush          8
        //   133: ldc_w           "DEPTH_OUTPUT"
        //   136: aastore        
        //   137: aload           12
        //   139: bipush          9
        //   141: ldc_w           "CONSTRAINED_HIGH_SPEED_VIDEO"
        //   144: aastore        
        //   145: aload           12
        //   147: bipush          10
        //   149: ldc_w           "MOTION_TRACKING"
        //   152: aastore        
        //   153: aload           12
        //   155: bipush          11
        //   157: ldc_w           "LOGICAL_MULTI_CAMERA"
        //   160: aastore        
        //   161: aload           12
        //   163: bipush          12
        //   165: ldc_w           "MONOCHROME"
        //   168: aastore        
        //   169: aload           11
        //   171: arraylength    
        //   172: istore          4
        //   174: iconst_0       
        //   175: istore_3       
        //   176: iload_3        
        //   177: iload           4
        //   179: if_icmpge       242
        //   182: aload           11
        //   184: iload_3        
        //   185: iaload         
        //   186: istore          5
        //   188: aload           6
        //   190: astore          7
        //   192: iload           5
        //   194: aload           12
        //   196: arraylength    
        //   197: if_icmpge       231
        //   200: new             Ljava/lang/StringBuilder;
        //   203: dup            
        //   204: invokespecial   java/lang/StringBuilder.<init>:()V
        //   207: aload           6
        //   209: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   212: ldc_w           "+ "
        //   215: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   218: aload           12
        //   220: iload           5
        //   222: aaload         
        //   223: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   226: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   229: astore          7
        //   231: iload_3        
        //   232: iconst_1       
        //   233: iadd           
        //   234: istore_3       
        //   235: aload           7
        //   237: astore          6
        //   239: goto            176
        //   242: aload           9
        //   244: ldc_w           "MAIN_specs"
        //   247: aload           6
        //   249: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   252: pop            
        //   253: ldc             ""
        //   255: astore          6
        //   257: aload_1        
        //   258: getstatic       android/hardware/camera2/CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   261: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   264: checkcast       [Landroid/util/Range;
        //   267: astore          7
        //   269: aload           7
        //   271: arraylength    
        //   272: istore          4
        //   274: iconst_0       
        //   275: istore_3       
        //   276: iload_3        
        //   277: iload           4
        //   279: if_icmpge       320
        //   282: aload           7
        //   284: iload_3        
        //   285: aaload         
        //   286: astore          11
        //   288: new             Ljava/lang/StringBuilder;
        //   291: dup            
        //   292: invokespecial   java/lang/StringBuilder.<init>:()V
        //   295: aload           6
        //   297: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   300: aload           11
        //   302: invokevirtual   android/util/Range.toString:()Ljava/lang/String;
        //   305: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   308: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   311: astore          6
        //   313: iload_3        
        //   314: iconst_1       
        //   315: iadd           
        //   316: istore_3       
        //   317: goto            276
        //   320: aload           9
        //   322: ldc_w           "FPS_ranges"
        //   325: aload           6
        //   327: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   330: pop            
        //   331: ldc             ""
        //   333: astore          6
        //   335: aload_1        
        //   336: getstatic       android/hardware/camera2/CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   339: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   342: checkcast       [F
        //   345: astore          7
        //   347: aload           7
        //   349: ifnull          424
        //   352: ldc_w           "cipherName-19"
        //   355: ldc             "DES"
        //   357: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   360: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   363: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   366: pop            
        //   367: aload           7
        //   369: arraylength    
        //   370: istore          4
        //   372: iconst_0       
        //   373: istore_3       
        //   374: iload_3        
        //   375: iload           4
        //   377: if_icmpge       413
        //   380: aload           7
        //   382: iload_3        
        //   383: faload         
        //   384: fstore_2       
        //   385: new             Ljava/lang/StringBuilder;
        //   388: dup            
        //   389: invokespecial   java/lang/StringBuilder.<init>:()V
        //   392: aload           6
        //   394: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   397: fload_2        
        //   398: invokevirtual   java/lang/StringBuilder.append:(F)Ljava/lang/StringBuilder;
        //   401: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   404: astore          6
        //   406: iload_3        
        //   407: iconst_1       
        //   408: iadd           
        //   409: istore_3       
        //   410: goto            374
        //   413: aload           9
        //   415: ldc_w           "Apertures"
        //   418: aload           6
        //   420: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   423: pop            
        //   424: ldc             ""
        //   426: astore          6
        //   428: aload_1        
        //   429: getstatic       android/hardware/camera2/CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   432: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   435: checkcast       [F
        //   438: astore          7
        //   440: aload           7
        //   442: arraylength    
        //   443: istore          4
        //   445: iconst_0       
        //   446: istore_3       
        //   447: iload_3        
        //   448: iload           4
        //   450: if_icmpge       486
        //   453: aload           7
        //   455: iload_3        
        //   456: faload         
        //   457: fstore_2       
        //   458: new             Ljava/lang/StringBuilder;
        //   461: dup            
        //   462: invokespecial   java/lang/StringBuilder.<init>:()V
        //   465: aload           6
        //   467: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   470: fload_2        
        //   471: invokevirtual   java/lang/StringBuilder.append:(F)Ljava/lang/StringBuilder;
        //   474: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   477: astore          6
        //   479: iload_3        
        //   480: iconst_1       
        //   481: iadd           
        //   482: istore_3       
        //   483: goto            447
        //   486: aload           9
        //   488: ldc_w           "Focal_lenghts"
        //   491: aload           6
        //   493: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   496: pop            
        //   497: ldc             ""
        //   499: astore          6
        //   501: aload_1        
        //   502: getstatic       android/hardware/camera2/CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   505: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   508: checkcast       [I
        //   511: astore          11
        //   513: bipush          6
        //   515: anewarray       Ljava/lang/String;
        //   518: astore          12
        //   520: aload           12
        //   522: iconst_0       
        //   523: ldc_w           "OFF"
        //   526: aastore        
        //   527: aload           12
        //   529: iconst_1       
        //   530: ldc_w           "ON"
        //   533: aastore        
        //   534: aload           12
        //   536: iconst_2       
        //   537: ldc_w           "ON_AUTO_FLASH"
        //   540: aastore        
        //   541: aload           12
        //   543: iconst_3       
        //   544: ldc_w           "ON_ALWAYS_FLASH"
        //   547: aastore        
        //   548: aload           12
        //   550: iconst_4       
        //   551: ldc_w           "ON_AUTO_FLASH_REDEYE"
        //   554: aastore        
        //   555: aload           12
        //   557: iconst_5       
        //   558: ldc_w           "ON_EXTERNAL_FLASH"
        //   561: aastore        
        //   562: aload           11
        //   564: arraylength    
        //   565: istore          4
        //   567: iconst_0       
        //   568: istore_3       
        //   569: iload_3        
        //   570: iload           4
        //   572: if_icmpge       635
        //   575: aload           11
        //   577: iload_3        
        //   578: iaload         
        //   579: istore          5
        //   581: aload           6
        //   583: astore          7
        //   585: iload           5
        //   587: aload           12
        //   589: arraylength    
        //   590: if_icmpge       624
        //   593: new             Ljava/lang/StringBuilder;
        //   596: dup            
        //   597: invokespecial   java/lang/StringBuilder.<init>:()V
        //   600: aload           6
        //   602: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   605: ldc_w           "+ "
        //   608: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   611: aload           12
        //   613: iload           5
        //   615: aaload         
        //   616: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   619: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   622: astore          7
        //   624: iload_3        
        //   625: iconst_1       
        //   626: iadd           
        //   627: istore_3       
        //   628: aload           7
        //   630: astore          6
        //   632: goto            569
        //   635: aload           9
        //   637: ldc_w           "AE_modes"
        //   640: aload           6
        //   642: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   645: pop            
        //   646: ldc             ""
        //   648: astore          6
        //   650: aload_1        
        //   651: getstatic       android/hardware/camera2/CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   654: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   657: checkcast       [I
        //   660: astore          11
        //   662: bipush          6
        //   664: anewarray       Ljava/lang/String;
        //   667: astore          12
        //   669: aload           12
        //   671: iconst_0       
        //   672: ldc_w           "OFF"
        //   675: aastore        
        //   676: aload           12
        //   678: iconst_1       
        //   679: ldc_w           "AUTO"
        //   682: aastore        
        //   683: aload           12
        //   685: iconst_2       
        //   686: ldc_w           "MACRO"
        //   689: aastore        
        //   690: aload           12
        //   692: iconst_3       
        //   693: ldc_w           "CONTINUOUS_VIDEO"
        //   696: aastore        
        //   697: aload           12
        //   699: iconst_4       
        //   700: ldc_w           "CONTINUOUS_PICTURE"
        //   703: aastore        
        //   704: aload           12
        //   706: iconst_5       
        //   707: ldc_w           "EDOF"
        //   710: aastore        
        //   711: aload           11
        //   713: arraylength    
        //   714: istore          4
        //   716: iconst_0       
        //   717: istore_3       
        //   718: iload_3        
        //   719: iload           4
        //   721: if_icmpge       784
        //   724: aload           11
        //   726: iload_3        
        //   727: iaload         
        //   728: istore          5
        //   730: aload           6
        //   732: astore          7
        //   734: iload           5
        //   736: aload           12
        //   738: arraylength    
        //   739: if_icmpge       773
        //   742: new             Ljava/lang/StringBuilder;
        //   745: dup            
        //   746: invokespecial   java/lang/StringBuilder.<init>:()V
        //   749: aload           6
        //   751: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   754: ldc_w           "+ "
        //   757: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   760: aload           12
        //   762: iload           5
        //   764: aaload         
        //   765: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   768: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   771: astore          7
        //   773: iload_3        
        //   774: iconst_1       
        //   775: iadd           
        //   776: istore_3       
        //   777: aload           7
        //   779: astore          6
        //   781: goto            718
        //   784: aload           9
        //   786: ldc_w           "AF_modes"
        //   789: aload           6
        //   791: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   794: pop            
        //   795: ldc             ""
        //   797: astore          6
        //   799: aload_1        
        //   800: getstatic       android/hardware/camera2/CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   803: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   806: checkcast       [I
        //   809: astore          11
        //   811: bipush          9
        //   813: anewarray       Ljava/lang/String;
        //   816: astore          12
        //   818: aload           12
        //   820: iconst_0       
        //   821: ldc_w           "OFF"
        //   824: aastore        
        //   825: aload           12
        //   827: iconst_1       
        //   828: ldc_w           "AUTO"
        //   831: aastore        
        //   832: aload           12
        //   834: iconst_2       
        //   835: ldc_w           "INCANDESCENT"
        //   838: aastore        
        //   839: aload           12
        //   841: iconst_3       
        //   842: ldc_w           "FLUORESCENT"
        //   845: aastore        
        //   846: aload           12
        //   848: iconst_4       
        //   849: ldc_w           "WARM_FLUORESCENT"
        //   852: aastore        
        //   853: aload           12
        //   855: iconst_5       
        //   856: ldc_w           "DAYLIGHT"
        //   859: aastore        
        //   860: aload           12
        //   862: bipush          6
        //   864: ldc_w           "CLOUDY_DAYLIGHT"
        //   867: aastore        
        //   868: aload           12
        //   870: bipush          7
        //   872: ldc_w           "TWILIGHT"
        //   875: aastore        
        //   876: aload           12
        //   878: bipush          8
        //   880: ldc_w           "SHADE"
        //   883: aastore        
        //   884: aload           11
        //   886: arraylength    
        //   887: istore          4
        //   889: iconst_0       
        //   890: istore_3       
        //   891: iload_3        
        //   892: iload           4
        //   894: if_icmpge       957
        //   897: aload           11
        //   899: iload_3        
        //   900: iaload         
        //   901: istore          5
        //   903: aload           6
        //   905: astore          7
        //   907: iload           5
        //   909: aload           12
        //   911: arraylength    
        //   912: if_icmpge       946
        //   915: new             Ljava/lang/StringBuilder;
        //   918: dup            
        //   919: invokespecial   java/lang/StringBuilder.<init>:()V
        //   922: aload           6
        //   924: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   927: ldc_w           "+ "
        //   930: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   933: aload           12
        //   935: iload           5
        //   937: aaload         
        //   938: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   941: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   944: astore          7
        //   946: iload_3        
        //   947: iconst_1       
        //   948: iadd           
        //   949: istore_3       
        //   950: aload           7
        //   952: astore          6
        //   954: goto            891
        //   957: aload           9
        //   959: ldc_w           "AWB_modes"
        //   962: aload           6
        //   964: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   967: pop            
        //   968: ldc             ""
        //   970: astore          6
        //   972: aload_1        
        //   973: getstatic       android/hardware/camera2/CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION:Landroid/hardware/camera2/CameraCharacteristics$Key;
        //   976: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //   979: checkcast       [I
        //   982: astore          11
        //   984: aload           11
        //   986: ifnull          1088
        //   989: ldc_w           "cipherName-20"
        //   992: ldc             "DES"
        //   994: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   997: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //  1000: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //  1003: pop            
        //  1004: aload           11
        //  1006: arraylength    
        //  1007: istore          4
        //  1009: iconst_0       
        //  1010: istore_3       
        //  1011: iload_3        
        //  1012: iload           4
        //  1014: if_icmpge       1077
        //  1017: aload           11
        //  1019: iload_3        
        //  1020: iaload         
        //  1021: istore          5
        //  1023: aload           6
        //  1025: astore          7
        //  1027: iload           5
        //  1029: aload           10
        //  1031: arraylength    
        //  1032: if_icmpge       1066
        //  1035: new             Ljava/lang/StringBuilder;
        //  1038: dup            
        //  1039: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1042: aload           6
        //  1044: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1047: ldc_w           "+ "
        //  1050: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1053: aload           10
        //  1055: iload           5
        //  1057: aaload         
        //  1058: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1061: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1064: astore          7
        //  1066: iload_3        
        //  1067: iconst_1       
        //  1068: iadd           
        //  1069: istore_3       
        //  1070: aload           7
        //  1072: astore          6
        //  1074: goto            1011
        //  1077: aload           9
        //  1079: ldc_w           "OIS_modes"
        //  1082: aload           6
        //  1084: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //  1087: pop            
        //  1088: aload_1        
        //  1089: invokevirtual   android/hardware/camera2/CameraCharacteristics.getKeys:()Ljava/util/List;
        //  1092: invokeinterface java/util/List.listIterator:()Ljava/util/ListIterator;
        //  1097: astore          7
        //  1099: aload           8
        //  1101: astore          6
        //  1103: aload           7
        //  1105: invokeinterface java/util/ListIterator.hasNext:()Z
        //  1110: ifeq            1238
        //  1113: ldc_w           "cipherName-21"
        //  1116: ldc             "DES"
        //  1118: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //  1121: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //  1124: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //  1127: pop            
        //  1128: aload           7
        //  1130: invokeinterface java/util/ListIterator.next:()Ljava/lang/Object;
        //  1135: checkcast       Landroid/hardware/camera2/CameraCharacteristics$Key;
        //  1138: astore          8
        //  1140: aload_1        
        //  1141: aload           8
        //  1143: invokevirtual   android/hardware/camera2/CameraCharacteristics.get:(Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
        //  1146: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1149: astore          10
        //  1151: aload           10
        //  1153: ldc_w           "["
        //  1156: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1159: ifeq            1173
        //  1162: aload           10
        //  1164: ldc_w           "]"
        //  1167: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  1170: ifeq            1189
        //  1173: aload           9
        //  1175: aload           8
        //  1177: invokevirtual   android/hardware/camera2/CameraCharacteristics$Key.getName:()Ljava/lang/String;
        //  1180: aload           10
        //  1182: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //  1185: pop            
        //  1186: goto            1103
        //  1189: ldc_w           "cipherName-22"
        //  1192: ldc             "DES"
        //  1194: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //  1197: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //  1200: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //  1203: pop            
        //  1204: new             Ljava/lang/StringBuilder;
        //  1207: dup            
        //  1208: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1211: aload           6
        //  1213: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1216: ldc_w           "/"
        //  1219: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1222: aload           8
        //  1224: invokevirtual   android/hardware/camera2/CameraCharacteristics$Key.getName:()Ljava/lang/String;
        //  1227: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1230: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1233: astore          6
        //  1235: goto            1103
        //  1238: aload           6
        //  1240: ldc_w           "\\."
        //  1243: ldc_w           " "
        //  1246: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1249: astore_1       
        //  1250: aload_1        
        //  1251: invokevirtual   java/lang/String.length:()I
        //  1254: ifle            1267
        //  1257: aload           9
        //  1259: ldc_w           "UNSOLVEDs"
        //  1262: aload_1        
        //  1263: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //  1266: pop            
        //  1267: aload           9
        //  1269: areturn        
        //  1270: astore          10
        //  1272: goto            1204
        //  1275: astore          10
        //  1277: goto            1204
        //  1280: astore          8
        //  1282: goto            1128
        //  1285: astore          8
        //  1287: goto            1128
        //  1290: astore          7
        //  1292: goto            1004
        //  1295: astore          7
        //  1297: goto            1004
        //  1300: astore          11
        //  1302: goto            367
        //  1305: astore          11
        //  1307: goto            367
        //  1310: astore          6
        //  1312: goto            15
        //  1315: astore          6
        //  1317: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                    
        //  -----  -----  -----  -----  ----------------------------------------
        //  0      15     1310   1315   Ljava/security/NoSuchAlgorithmException;
        //  0      15     1315   1320   Ljavax/crypto/NoSuchPaddingException;
        //  352    367    1300   1305   Ljava/security/NoSuchAlgorithmException;
        //  352    367    1305   1310   Ljavax/crypto/NoSuchPaddingException;
        //  989    1004   1290   1295   Ljava/security/NoSuchAlgorithmException;
        //  989    1004   1295   1300   Ljavax/crypto/NoSuchPaddingException;
        //  1113   1128   1280   1285   Ljava/security/NoSuchAlgorithmException;
        //  1113   1128   1285   1290   Ljavax/crypto/NoSuchPaddingException;
        //  1189   1204   1270   1275   Ljava/security/NoSuchAlgorithmException;
        //  1189   1204   1275   1280   Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private JSONObject trebleInfo() throws JSONException {
        while (true) {
            try {
                Log.d("cipherName-8", Cipher.getInstance("DES").getAlgorithm());
                final String prop = Utils.getProp("ro.vndk.version");
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("TrebleGetprop", (Object)Utils.getProp("ro.treble.enabled"));
                jsonObject.put("ro.vendor.vndk.version", (Object)Utils.getProp("ro.vendor.vndk.version"));
                jsonObject.put("ro.product.first_api_level", (Object)Utils.getProp("ro.product.first_api_level"));
                jsonObject.put("ro.vndk.lite", (Object)Utils.getProp("ro.vndk.lite"));
                jsonObject.put("ro.vndk.version", (Object)prop);
                String string = prop;
                if (!prop.equals("")) {
                    string = "-" + prop;
                }
                jsonObject.put("system/lib/vndk" + string, (Object)Utils.getZinfo("ls /system/lib/vndk" + string, "/", false));
                jsonObject.put("system/lib/vndk-sp" + string, (Object)Utils.getZinfo("ls /system/lib/vndk-sp" + string, "/", false));
                jsonObject.put("vendor/lib/vndk" + string, (Object)Utils.getZinfo("ls /vendor/lib/vndk" + string, "/", false));
                jsonObject.put("vendor/lib/vndk-sp" + string, (Object)Utils.getZinfo("ls /vendor/lib/vndk-sp" + string, "/", false));
                jsonObject.put("system/lib64/vndk" + string, (Object)Utils.getZinfo("ls /system/lib64/vndk" + string, "/", false));
                jsonObject.put("system/lib64/vndk-sp" + string, (Object)Utils.getZinfo("ls /system/lib64/vndk-sp" + string, "/", false));
                jsonObject.put("vendor/lib64/vndk" + string, (Object)Utils.getZinfo("ls /vendor/lib64/vndk" + string, "/", false));
                jsonObject.put("vendor/lib64/vndk-sp" + string, (Object)Utils.getZinfo("ls /vendor/lib64/vndk-sp" + string, "/", false));
                return jsonObject;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    @TargetApi(18)
    private JSONObject widevineModularDrmInfo() throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "DES"
        //     5: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //     8: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    11: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    14: pop            
        //    15: getstatic       com/oF2pks/kalturadeviceinfos/Collector.WIDEVINE_UUID:Ljava/util/UUID;
        //    18: invokestatic    android/media/MediaDrm.isCryptoSchemeSupported:(Ljava/util/UUID;)Z
        //    21: ifne            41
        //    24: ldc_w           "cipherName-69"
        //    27: ldc             "DES"
        //    29: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    32: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    35: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    38: pop            
        //    39: aconst_null    
        //    40: areturn        
        //    41: ldc_w           "cipherName-70"
        //    44: ldc             "DES"
        //    46: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //    49: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //    52: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    55: pop            
        //    56: new             Landroid/media/MediaDrm;
        //    59: dup            
        //    60: getstatic       com/oF2pks/kalturadeviceinfos/Collector.WIDEVINE_UUID:Ljava/util/UUID;
        //    63: invokespecial   android/media/MediaDrm.<init>:(Ljava/util/UUID;)V
        //    66: astore          5
        //    68: new             Lorg/json/JSONArray;
        //    71: dup            
        //    72: invokespecial   org/json/JSONArray.<init>:()V
        //    75: astore          4
        //    77: aload           5
        //    79: new             Lcom/oF2pks/kalturadeviceinfos/Collector$2;
        //    82: dup            
        //    83: aload_0        
        //    84: aload           4
        //    86: invokespecial   com/oF2pks/kalturadeviceinfos/Collector$2.<init>:(Lcom/oF2pks/kalturadeviceinfos/Collector;Lorg/json/JSONArray;)V
        //    89: invokevirtual   android/media/MediaDrm.setOnEventListener:(Landroid/media/MediaDrm$OnEventListener;)V
        //    92: ldc_w           "cipherName-75"
        //    95: ldc             "DES"
        //    97: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   100: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   103: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   106: pop            
        //   107: aload           5
        //   109: aload           5
        //   111: invokevirtual   android/media/MediaDrm.openSession:()[B
        //   114: invokevirtual   android/media/MediaDrm.closeSession:([B)V
        //   117: bipush          15
        //   119: anewarray       Ljava/lang/String;
        //   122: astore          8
        //   124: aload           8
        //   126: iconst_0       
        //   127: ldc             "vendor"
        //   129: aastore        
        //   130: aload           8
        //   132: iconst_1       
        //   133: ldc_w           "version"
        //   136: aastore        
        //   137: aload           8
        //   139: iconst_2       
        //   140: ldc_w           "description"
        //   143: aastore        
        //   144: aload           8
        //   146: iconst_3       
        //   147: ldc_w           "algorithms"
        //   150: aastore        
        //   151: aload           8
        //   153: iconst_4       
        //   154: ldc_w           "securityLevel"
        //   157: aastore        
        //   158: aload           8
        //   160: iconst_5       
        //   161: ldc_w           "systemId"
        //   164: aastore        
        //   165: aload           8
        //   167: bipush          6
        //   169: ldc_w           "privacyMode"
        //   172: aastore        
        //   173: aload           8
        //   175: bipush          7
        //   177: ldc_w           "sessionSharing"
        //   180: aastore        
        //   181: aload           8
        //   183: bipush          8
        //   185: ldc_w           "usageReportingSupport"
        //   188: aastore        
        //   189: aload           8
        //   191: bipush          9
        //   193: ldc_w           "appId"
        //   196: aastore        
        //   197: aload           8
        //   199: bipush          10
        //   201: ldc_w           "origin"
        //   204: aastore        
        //   205: aload           8
        //   207: bipush          11
        //   209: ldc_w           "hdcpLevel"
        //   212: aastore        
        //   213: aload           8
        //   215: bipush          12
        //   217: ldc_w           "maxHdcpLevel"
        //   220: aastore        
        //   221: aload           8
        //   223: bipush          13
        //   225: ldc_w           "maxNumberOfSessions"
        //   228: aastore        
        //   229: aload           8
        //   231: bipush          14
        //   233: ldc_w           "numberOfOpenSessions"
        //   236: aastore        
        //   237: iconst_3       
        //   238: anewarray       Ljava/lang/String;
        //   241: astore          7
        //   243: aload           7
        //   245: iconst_0       
        //   246: ldc_w           "deviceUniqueId"
        //   249: aastore        
        //   250: aload           7
        //   252: iconst_1       
        //   253: ldc_w           "provisioningUniqueId"
        //   256: aastore        
        //   257: aload           7
        //   259: iconst_2       
        //   260: ldc_w           "serviceCertificate"
        //   263: aastore        
        //   264: new             Lorg/json/JSONObject;
        //   267: dup            
        //   268: invokespecial   org/json/JSONObject.<init>:()V
        //   271: astore          6
        //   273: aload           8
        //   275: arraylength    
        //   276: istore_2       
        //   277: iconst_0       
        //   278: istore_1       
        //   279: iload_1        
        //   280: iload_2        
        //   281: if_icmpge       427
        //   284: aload           8
        //   286: iload_1        
        //   287: aaload         
        //   288: astore          9
        //   290: ldc_w           "cipherName-77"
        //   293: ldc             "DES"
        //   295: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   298: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   301: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   304: pop            
        //   305: ldc_w           "cipherName-78"
        //   308: ldc             "DES"
        //   310: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   313: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   316: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   319: pop            
        //   320: aload           5
        //   322: aload           9
        //   324: invokevirtual   android/media/MediaDrm.getPropertyString:(Ljava/lang/String;)Ljava/lang/String;
        //   327: astore_3       
        //   328: aload           6
        //   330: aload           9
        //   332: aload_3        
        //   333: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   336: pop            
        //   337: iload_1        
        //   338: iconst_1       
        //   339: iadd           
        //   340: istore_1       
        //   341: goto            279
        //   344: astore_3       
        //   345: ldc_w           "cipherName-71"
        //   348: ldc             "DES"
        //   350: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   353: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   356: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   359: pop            
        //   360: aconst_null    
        //   361: areturn        
        //   362: astore_3       
        //   363: ldc_w           "cipherName-76"
        //   366: ldc             "DES"
        //   368: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   371: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   374: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   377: pop            
        //   378: aload           4
        //   380: new             Lorg/json/JSONObject;
        //   383: dup            
        //   384: invokespecial   org/json/JSONObject.<init>:()V
        //   387: ldc_w           "Exception(openSession)"
        //   390: aload_3        
        //   391: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //   394: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   397: invokevirtual   org/json/JSONArray.put:(Ljava/lang/Object;)Lorg/json/JSONArray;
        //   400: pop            
        //   401: goto            117
        //   404: astore_3       
        //   405: ldc_w           "cipherName-79"
        //   408: ldc             "DES"
        //   410: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   413: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   416: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   419: pop            
        //   420: ldc_w           "<unknown>"
        //   423: astore_3       
        //   424: goto            328
        //   427: aload           7
        //   429: arraylength    
        //   430: istore_2       
        //   431: iconst_0       
        //   432: istore_1       
        //   433: iload_1        
        //   434: iload_2        
        //   435: if_icmpge       525
        //   438: aload           7
        //   440: iload_1        
        //   441: aaload         
        //   442: astore          8
        //   444: ldc_w           "cipherName-80"
        //   447: ldc             "DES"
        //   449: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   452: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   455: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   458: pop            
        //   459: ldc_w           "cipherName-81"
        //   462: ldc             "DES"
        //   464: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   467: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   470: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   473: pop            
        //   474: aload           5
        //   476: aload           8
        //   478: invokevirtual   android/media/MediaDrm.getPropertyByteArray:(Ljava/lang/String;)[B
        //   481: iconst_2       
        //   482: invokestatic    android/util/Base64.encodeToString:([BI)Ljava/lang/String;
        //   485: astore_3       
        //   486: aload           6
        //   488: aload           8
        //   490: aload_3        
        //   491: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   494: pop            
        //   495: iload_1        
        //   496: iconst_1       
        //   497: iadd           
        //   498: istore_1       
        //   499: goto            433
        //   502: astore_3       
        //   503: ldc_w           "cipherName-82"
        //   506: ldc             "DES"
        //   508: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
        //   511: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
        //   514: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   517: pop            
        //   518: ldc_w           "<unknown>"
        //   521: astore_3       
        //   522: goto            486
        //   525: new             Lorg/json/JSONObject;
        //   528: dup            
        //   529: invokespecial   org/json/JSONObject.<init>:()V
        //   532: astore_3       
        //   533: aload_3        
        //   534: ldc_w           "properties"
        //   537: aload           6
        //   539: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   542: pop            
        //   543: aload_3        
        //   544: ldc_w           "events"
        //   547: aload           4
        //   549: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   552: pop            
        //   553: aload_3        
        //   554: areturn        
        //   555: astore_3       
        //   556: goto            518
        //   559: astore_3       
        //   560: goto            518
        //   563: astore_3       
        //   564: goto            503
        //   567: astore_3       
        //   568: goto            474
        //   571: astore_3       
        //   572: goto            474
        //   575: astore_3       
        //   576: goto            459
        //   579: astore_3       
        //   580: goto            459
        //   583: astore_3       
        //   584: goto            420
        //   587: astore_3       
        //   588: goto            420
        //   591: astore_3       
        //   592: goto            320
        //   595: astore_3       
        //   596: goto            320
        //   599: astore_3       
        //   600: goto            305
        //   603: astore_3       
        //   604: goto            305
        //   607: astore          6
        //   609: goto            378
        //   612: astore          6
        //   614: goto            378
        //   617: astore_3       
        //   618: goto            107
        //   621: astore_3       
        //   622: goto            107
        //   625: astore_3       
        //   626: goto            360
        //   629: astore_3       
        //   630: goto            360
        //   633: astore_3       
        //   634: goto            56
        //   637: astore_3       
        //   638: goto            56
        //   641: astore_3       
        //   642: goto            39
        //   645: astore_3       
        //   646: goto            39
        //   649: astore_3       
        //   650: goto            15
        //   653: astore_3       
        //   654: goto            15
        //    Exceptions:
        //  throws org.json.JSONException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                      
        //  -----  -----  -----  -----  ------------------------------------------
        //  0      15     649    653    Ljava/security/NoSuchAlgorithmException;
        //  0      15     653    657    Ljavax/crypto/NoSuchPaddingException;
        //  24     39     641    645    Ljava/security/NoSuchAlgorithmException;
        //  24     39     645    649    Ljavax/crypto/NoSuchPaddingException;
        //  41     56     633    637    Ljava/security/NoSuchAlgorithmException;
        //  41     56     637    641    Ljavax/crypto/NoSuchPaddingException;
        //  41     56     344    633    Landroid/media/UnsupportedSchemeException;
        //  56     68     344    633    Landroid/media/UnsupportedSchemeException;
        //  92     107    617    621    Ljava/security/NoSuchAlgorithmException;
        //  92     107    621    625    Ljavax/crypto/NoSuchPaddingException;
        //  92     107    362    617    Ljava/lang/Exception;
        //  107    117    362    617    Ljava/lang/Exception;
        //  290    305    599    603    Ljava/security/NoSuchAlgorithmException;
        //  290    305    603    607    Ljavax/crypto/NoSuchPaddingException;
        //  305    320    591    595    Ljava/security/NoSuchAlgorithmException;
        //  305    320    595    599    Ljavax/crypto/NoSuchPaddingException;
        //  305    320    404    591    Ljava/lang/IllegalStateException;
        //  320    328    404    591    Ljava/lang/IllegalStateException;
        //  345    360    625    629    Ljava/security/NoSuchAlgorithmException;
        //  345    360    629    633    Ljavax/crypto/NoSuchPaddingException;
        //  363    378    607    612    Ljava/security/NoSuchAlgorithmException;
        //  363    378    612    617    Ljavax/crypto/NoSuchPaddingException;
        //  405    420    583    587    Ljava/security/NoSuchAlgorithmException;
        //  405    420    587    591    Ljavax/crypto/NoSuchPaddingException;
        //  444    459    575    579    Ljava/security/NoSuchAlgorithmException;
        //  444    459    579    583    Ljavax/crypto/NoSuchPaddingException;
        //  459    474    567    571    Ljava/security/NoSuchAlgorithmException;
        //  459    474    571    575    Ljavax/crypto/NoSuchPaddingException;
        //  459    474    563    567    Ljava/lang/IllegalStateException;
        //  459    474    502    503    Ljava/lang/NullPointerException;
        //  474    486    563    567    Ljava/lang/IllegalStateException;
        //  474    486    502    503    Ljava/lang/NullPointerException;
        //  503    518    555    559    Ljava/security/NoSuchAlgorithmException;
        //  503    518    559    563    Ljavax/crypto/NoSuchPaddingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    JSONObject collect() {
        while (true) {
            try {
                Log.d("cipherName-5", Cipher.getInstance("DES").getAlgorithm());
                final JSONObject[] array = { null };
                try {
                    try {
                        Log.d("cipherName-6", Cipher.getInstance("DES").getAlgorithm());
                        final JSONObject mRoot = this.mRoot;
                        mRoot.put("#META=", (Object)this.meta());
                        mRoot.put("#ARCH=", (Object)this.systemArch());
                        mRoot.put("#DRM=", (Object)this.drmInfo());
                        mRoot.put("#ANDROID", (Object)this.androidInfo());
                        mRoot.put("#SYSTEM=", (Object)this.systemInfo());
                        mRoot.put("#DISPLAY=", (Object)this.displayInfo());
                        mRoot.put("#MEDIA=", (Object)this.mediaCodecInfo());
                        mRoot.put("#SECURITY=", (Object)this.securityProviders());
                        mRoot.put("#CAMERA=", (Object)this.cameraOldAPI());
                        if (Build$VERSION.SDK_INT >= 21) {
                            mRoot.put("#CAMERA2API=", (Object)this.camera2API());
                        }
                        mRoot.put("#LIBS&FEATURES=", (Object)this.systemFL());
                        mRoot.put("#SERVICES&SVC=", (Object)this.dumpsysL());
                        mRoot.put("#ROOT?=", (Object)this.rootInfo());
                        if (Utils.getProp("ro.treble.enabled").equals("true")) {
                            mRoot.put("#TREBLE=", (Object)this.trebleInfo());
                        }
                        mRoot.put("#MOUNTS", (Object)this.selfMounts());
                        return this.mRoot;
                    }
                    catch (JSONException ex) {
                        try {
                            Log.d("cipherName-7", Cipher.getInstance("DES").getAlgorithm());
                            Log.e("Collector", "Error");
                        }
                        catch (NoSuchAlgorithmException ex2) {}
                        catch (NoSuchPaddingException ex3) {}
                    }
                }
                catch (NoSuchAlgorithmException ex4) {}
                catch (NoSuchPaddingException ex5) {}
            }
            catch (NoSuchAlgorithmException ex6) {
                continue;
            }
            catch (NoSuchPaddingException ex7) {
                continue;
            }
            break;
        }
    }
}
