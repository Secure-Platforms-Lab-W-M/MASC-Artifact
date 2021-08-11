// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.view.View$OnClickListener;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.app.AlertDialog$Builder;
import android.content.ComponentName;
import android.util.Log;
import javax.crypto.Cipher;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v4.app.DialogFragment;

public class ShortcutEditDialogFragment extends DialogFragment
{
    protected MyActivityInfo activity;
    protected ImageButton image_icon;
    protected EditText text_class;
    protected EditText text_icon;
    protected EditText text_name;
    protected EditText text_package;
    
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-46", Cipher.getInstance("DES").getAlgorithm());
                this.activity = new MyActivityInfo((ComponentName)this.getArguments().getParcelable("activity"), this.getActivity().getPackageManager());
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
                final View inflate = ((LayoutInflater)this.getActivity().getSystemService("layout_inflater")).inflate(2130903044, (ViewGroup)null);
                (this.text_name = (EditText)inflate.findViewById(2131230723)).setText((CharSequence)this.activity.name);
                (this.text_package = (EditText)inflate.findViewById(2131230726)).setText((CharSequence)this.activity.component_name.getPackageName());
                (this.text_class = (EditText)inflate.findViewById(2131230729)).setText((CharSequence)this.activity.component_name.getClassName());
                (this.text_icon = (EditText)inflate.findViewById(2131230732)).setText((CharSequence)this.activity.icon_resource_name);
                this.text_icon.addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(Editable icon) {
                        while (true) {
                            try {
                                Log.d("cipherName-49", Cipher.getInstance("DES").getAlgorithm());
                                icon = (Editable)IconListAdapter.getIcon(icon.toString(), ShortcutEditDialogFragment.this.getActivity().getPackageManager());
                                ShortcutEditDialogFragment.this.image_icon.setImageDrawable((Drawable)icon);
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
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        try {
                            Log.d("cipherName-48", Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        try {
                            Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                });
                (this.image_icon = (ImageButton)inflate.findViewById(2131230733)).setImageDrawable((Drawable)this.activity.icon);
                this.image_icon.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-50", Cipher.getInstance("DES").getAlgorithm());
                                final IconPickerDialogFragment iconPickerDialogFragment = new IconPickerDialogFragment();
                                iconPickerDialogFragment.attachIconPickerListener((IconPickerDialogFragment.IconPickerListener)new IconPickerDialogFragment.IconPickerListener() {
                                    @Override
                                    public void iconPicked(String icon) {
                                        while (true) {
                                            try {
                                                Log.d("cipherName-51", Cipher.getInstance("DES").getAlgorithm());
                                                ShortcutEditDialogFragment.this.text_icon.setText((CharSequence)icon);
                                                icon = (String)IconListAdapter.getIcon(icon, ShortcutEditDialogFragment.this.getActivity().getPackageManager());
                                                ShortcutEditDialogFragment.this.image_icon.setImageDrawable((Drawable)icon);
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
                                });
                                iconPickerDialogFragment.show(ShortcutEditDialogFragment.this.getFragmentManager(), "icon picker");
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
                });
                alertDialog$Builder.setTitle((CharSequence)this.activity.name).setView(inflate).setIcon((Drawable)this.activity.icon).setPositiveButton(2130968582, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface p0, final int p1) {
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
                        //    14: aload_0        
                        //    15: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    18: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //    21: aload_0        
                        //    22: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    25: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.text_name:Landroid/widget/EditText;
                        //    28: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                        //    31: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                        //    34: putfield        de/szalkowski/activitylauncher/MyActivityInfo.name:Ljava/lang/String;
                        //    37: new             Landroid/content/ComponentName;
                        //    40: dup            
                        //    41: aload_0        
                        //    42: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    45: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.text_package:Landroid/widget/EditText;
                        //    48: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                        //    51: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                        //    54: aload_0        
                        //    55: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    58: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.text_class:Landroid/widget/EditText;
                        //    61: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                        //    64: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                        //    67: invokespecial   android/content/ComponentName.<init>:(Ljava/lang/String;Ljava/lang/String;)V
                        //    70: astore_1       
                        //    71: aload_0        
                        //    72: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    75: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //    78: aload_1        
                        //    79: putfield        de/szalkowski/activitylauncher/MyActivityInfo.component_name:Landroid/content/ComponentName;
                        //    82: aload_0        
                        //    83: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    86: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //    89: aload_0        
                        //    90: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //    93: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.text_icon:Landroid/widget/EditText;
                        //    96: invokevirtual   android/widget/EditText.getText:()Landroid/text/Editable;
                        //    99: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
                        //   102: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource_name:Ljava/lang/String;
                        //   105: aload_0        
                        //   106: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   109: invokevirtual   de/szalkowski/activitylauncher/ShortcutEditDialogFragment.getActivity:()Landroid/support/v4/app/FragmentActivity;
                        //   112: invokevirtual   android/support/v4/app/FragmentActivity.getPackageManager:()Landroid/content/pm/PackageManager;
                        //   115: astore_1       
                        //   116: ldc             "cipherName-53"
                        //   118: ldc             "DES"
                        //   120: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   123: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   126: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   129: pop            
                        //   130: aload_0        
                        //   131: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   134: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   137: getfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource_name:Ljava/lang/String;
                        //   140: astore_3       
                        //   141: aload_3        
                        //   142: iconst_0       
                        //   143: aload_3        
                        //   144: bipush          58
                        //   146: invokevirtual   java/lang/String.indexOf:(I)I
                        //   149: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
                        //   152: astore          4
                        //   154: aload_3        
                        //   155: aload_3        
                        //   156: bipush          58
                        //   158: invokevirtual   java/lang/String.indexOf:(I)I
                        //   161: iconst_1       
                        //   162: iadd           
                        //   163: aload_3        
                        //   164: bipush          47
                        //   166: invokevirtual   java/lang/String.indexOf:(I)I
                        //   169: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
                        //   172: astore          5
                        //   174: aload_3        
                        //   175: aload_3        
                        //   176: bipush          47
                        //   178: invokevirtual   java/lang/String.indexOf:(I)I
                        //   181: iconst_1       
                        //   182: iadd           
                        //   183: aload_3        
                        //   184: invokevirtual   java/lang/String.length:()I
                        //   187: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
                        //   190: astore          6
                        //   192: aload_1        
                        //   193: aload           4
                        //   195: invokevirtual   android/content/pm/PackageManager.getResourcesForApplication:(Ljava/lang/String;)Landroid/content/res/Resources;
                        //   198: astore_3       
                        //   199: aload_0        
                        //   200: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   203: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   206: aload_3        
                        //   207: aload           6
                        //   209: aload           5
                        //   211: aload           4
                        //   213: invokevirtual   android/content/res/Resources.getIdentifier:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
                        //   216: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
                        //   219: aload_0        
                        //   220: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   223: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   226: getfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
                        //   229: ifeq            291
                        //   232: ldc             "cipherName-54"
                        //   234: ldc             "DES"
                        //   236: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   239: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   242: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   245: pop            
                        //   246: aload_0        
                        //   247: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   250: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   253: aload_3        
                        //   254: aload_0        
                        //   255: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   258: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   261: getfield        de/szalkowski/activitylauncher/MyActivityInfo.icon_resource:I
                        //   264: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
                        //   267: checkcast       Landroid/graphics/drawable/BitmapDrawable;
                        //   270: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
                        //   273: aload_0        
                        //   274: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   277: invokevirtual   de/szalkowski/activitylauncher/ShortcutEditDialogFragment.getActivity:()Landroid/support/v4/app/FragmentActivity;
                        //   280: aload_0        
                        //   281: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   284: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   287: invokestatic    org/thirdparty/LauncherIconCreator.createLauncherIcon:(Landroid/content/Context;Lde/szalkowski/activitylauncher/MyActivityInfo;)V
                        //   290: return         
                        //   291: ldc             "cipherName-55"
                        //   293: ldc             "DES"
                        //   295: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   298: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   301: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   304: pop            
                        //   305: aload_0        
                        //   306: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   309: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   312: aload_1        
                        //   313: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
                        //   316: checkcast       Landroid/graphics/drawable/BitmapDrawable;
                        //   319: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
                        //   322: aload_0        
                        //   323: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   326: invokevirtual   de/szalkowski/activitylauncher/ShortcutEditDialogFragment.getActivity:()Landroid/support/v4/app/FragmentActivity;
                        //   329: ldc             2130968590
                        //   331: iconst_1       
                        //   332: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;II)Landroid/widget/Toast;
                        //   335: invokevirtual   android/widget/Toast.show:()V
                        //   338: goto            273
                        //   341: astore_3       
                        //   342: ldc             "cipherName-56"
                        //   344: ldc             "DES"
                        //   346: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   349: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   352: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   355: pop            
                        //   356: aload_0        
                        //   357: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   360: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   363: aload_1        
                        //   364: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
                        //   367: checkcast       Landroid/graphics/drawable/BitmapDrawable;
                        //   370: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
                        //   373: aload_0        
                        //   374: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   377: invokevirtual   de/szalkowski/activitylauncher/ShortcutEditDialogFragment.getActivity:()Landroid/support/v4/app/FragmentActivity;
                        //   380: ldc             2130968590
                        //   382: iconst_1       
                        //   383: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;II)Landroid/widget/Toast;
                        //   386: invokevirtual   android/widget/Toast.show:()V
                        //   389: goto            273
                        //   392: astore_3       
                        //   393: ldc             "cipherName-57"
                        //   395: ldc             "DES"
                        //   397: invokestatic    javax/crypto/Cipher.getInstance:(Ljava/lang/String;)Ljavax/crypto/Cipher;
                        //   400: invokevirtual   javax/crypto/Cipher.getAlgorithm:()Ljava/lang/String;
                        //   403: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                        //   406: pop            
                        //   407: aload_0        
                        //   408: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   411: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment.activity:Lde/szalkowski/activitylauncher/MyActivityInfo;
                        //   414: aload_1        
                        //   415: invokevirtual   android/content/pm/PackageManager.getDefaultActivityIcon:()Landroid/graphics/drawable/Drawable;
                        //   418: checkcast       Landroid/graphics/drawable/BitmapDrawable;
                        //   421: putfield        de/szalkowski/activitylauncher/MyActivityInfo.icon:Landroid/graphics/drawable/BitmapDrawable;
                        //   424: aload_0        
                        //   425: getfield        de/szalkowski/activitylauncher/ShortcutEditDialogFragment$4.this$0:Lde/szalkowski/activitylauncher/ShortcutEditDialogFragment;
                        //   428: invokevirtual   de/szalkowski/activitylauncher/ShortcutEditDialogFragment.getActivity:()Landroid/support/v4/app/FragmentActivity;
                        //   431: ldc             2130968589
                        //   433: iconst_1       
                        //   434: invokestatic    android/widget/Toast.makeText:(Landroid/content/Context;II)Landroid/widget/Toast;
                        //   437: invokevirtual   android/widget/Toast.show:()V
                        //   440: goto            273
                        //   443: astore_3       
                        //   444: goto            407
                        //   447: astore_3       
                        //   448: goto            407
                        //   451: astore_3       
                        //   452: goto            356
                        //   455: astore_3       
                        //   456: goto            356
                        //   459: astore_3       
                        //   460: goto            305
                        //   463: astore_3       
                        //   464: goto            305
                        //   467: astore          4
                        //   469: goto            246
                        //   472: astore          4
                        //   474: goto            246
                        //   477: astore_3       
                        //   478: goto            130
                        //   481: astore_3       
                        //   482: goto            130
                        //   485: astore_1       
                        //   486: goto            14
                        //   489: astore_1       
                        //   490: goto            14
                        //    Exceptions:
                        //  Try           Handler
                        //  Start  End    Start  End    Type                                                     
                        //  -----  -----  -----  -----  ---------------------------------------------------------
                        //  0      14     485    489    Ljava/security/NoSuchAlgorithmException;
                        //  0      14     489    493    Ljavax/crypto/NoSuchPaddingException;
                        //  116    130    477    481    Ljava/security/NoSuchAlgorithmException;
                        //  116    130    481    485    Ljavax/crypto/NoSuchPaddingException;
                        //  116    130    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  116    130    392    451    Ljava/lang/Exception;
                        //  130    232    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  130    232    392    451    Ljava/lang/Exception;
                        //  232    246    467    472    Ljava/security/NoSuchAlgorithmException;
                        //  232    246    472    477    Ljavax/crypto/NoSuchPaddingException;
                        //  232    246    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  232    246    392    451    Ljava/lang/Exception;
                        //  246    273    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  246    273    392    451    Ljava/lang/Exception;
                        //  291    305    459    463    Ljava/security/NoSuchAlgorithmException;
                        //  291    305    463    467    Ljavax/crypto/NoSuchPaddingException;
                        //  291    305    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  291    305    392    451    Ljava/lang/Exception;
                        //  305    338    341    459    Landroid/content/pm/PackageManager$NameNotFoundException;
                        //  305    338    392    451    Ljava/lang/Exception;
                        //  342    356    451    455    Ljava/security/NoSuchAlgorithmException;
                        //  342    356    455    459    Ljavax/crypto/NoSuchPaddingException;
                        //  393    407    443    447    Ljava/security/NoSuchAlgorithmException;
                        //  393    407    447    451    Ljavax/crypto/NoSuchPaddingException;
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0246:
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
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:494)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                }).setNegativeButton(17039360, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-58", Cipher.getInstance("DES").getAlgorithm());
                                ShortcutEditDialogFragment.this.getDialog().cancel();
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
                });
                return (Dialog)alertDialog$Builder.create();
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
}
