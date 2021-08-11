package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import java.util.ArrayList;

public final class ShareCompat {
   public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
   public static final String EXTRA_CALLING_ACTIVITY_INTEROP = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
   public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
   public static final String EXTRA_CALLING_PACKAGE_INTEROP = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
   private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

   private ShareCompat() {
   }

   public static void configureMenuItem(Menu var0, int var1, ShareCompat.IntentBuilder var2) {
      MenuItem var3 = var0.findItem(var1);
      if (var3 != null) {
         configureMenuItem(var3, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Could not find menu item with id ");
         var4.append(var1);
         var4.append(" in the supplied menu");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public static void configureMenuItem(MenuItem var0, ShareCompat.IntentBuilder var1) {
      ActionProvider var2 = var0.getActionProvider();
      ShareActionProvider var4;
      if (!(var2 instanceof ShareActionProvider)) {
         var4 = new ShareActionProvider(var1.getActivity());
      } else {
         var4 = (ShareActionProvider)var2;
      }

      StringBuilder var3 = new StringBuilder();
      var3.append(".sharecompat_");
      var3.append(var1.getActivity().getClass().getName());
      var4.setShareHistoryFileName(var3.toString());
      var4.setShareIntent(var1.getIntent());
      var0.setActionProvider(var4);
      if (VERSION.SDK_INT < 16 && !var0.hasSubMenu()) {
         var0.setIntent(var1.createChooserIntent());
      }

   }

   public static ComponentName getCallingActivity(Activity var0) {
      ComponentName var2 = var0.getCallingActivity();
      ComponentName var1 = var2;
      if (var2 == null) {
         var2 = (ComponentName)var0.getIntent().getParcelableExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY");
         var1 = var2;
         if (var2 == null) {
            var1 = (ComponentName)var0.getIntent().getParcelableExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY");
         }
      }

      return var1;
   }

   public static String getCallingPackage(Activity var0) {
      String var2 = var0.getCallingPackage();
      String var1 = var2;
      if (var2 == null) {
         var2 = var0.getIntent().getStringExtra("androidx.core.app.EXTRA_CALLING_PACKAGE");
         var1 = var2;
         if (var2 == null) {
            var1 = var0.getIntent().getStringExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE");
         }
      }

      return var1;
   }

   public static class IntentBuilder {
      private Activity mActivity;
      private ArrayList mBccAddresses;
      private ArrayList mCcAddresses;
      private CharSequence mChooserTitle;
      private Intent mIntent;
      private ArrayList mStreams;
      private ArrayList mToAddresses;

      private IntentBuilder(Activity var1) {
         this.mActivity = var1;
         Intent var2 = (new Intent()).setAction("android.intent.action.SEND");
         this.mIntent = var2;
         var2.putExtra("androidx.core.app.EXTRA_CALLING_PACKAGE", var1.getPackageName());
         this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", var1.getPackageName());
         this.mIntent.putExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY", var1.getComponentName());
         this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", var1.getComponentName());
         this.mIntent.addFlags(524288);
      }

      private void combineArrayExtra(String var1, ArrayList var2) {
         String[] var4 = this.mIntent.getStringArrayExtra(var1);
         int var3;
         if (var4 != null) {
            var3 = var4.length;
         } else {
            var3 = 0;
         }

         String[] var5 = new String[var2.size() + var3];
         var2.toArray(var5);
         if (var4 != null) {
            System.arraycopy(var4, 0, var5, var2.size(), var3);
         }

         this.mIntent.putExtra(var1, var5);
      }

      private void combineArrayExtra(String var1, String[] var2) {
         Intent var4 = this.getIntent();
         String[] var5 = var4.getStringArrayExtra(var1);
         int var3;
         if (var5 != null) {
            var3 = var5.length;
         } else {
            var3 = 0;
         }

         String[] var6 = new String[var2.length + var3];
         if (var5 != null) {
            System.arraycopy(var5, 0, var6, 0, var3);
         }

         System.arraycopy(var2, 0, var6, var3, var2.length);
         var4.putExtra(var1, var6);
      }

      public static ShareCompat.IntentBuilder from(Activity var0) {
         return new ShareCompat.IntentBuilder(var0);
      }

      public ShareCompat.IntentBuilder addEmailBcc(String var1) {
         if (this.mBccAddresses == null) {
            this.mBccAddresses = new ArrayList();
         }

         this.mBccAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailBcc(String[] var1) {
         this.combineArrayExtra("android.intent.extra.BCC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailCc(String var1) {
         if (this.mCcAddresses == null) {
            this.mCcAddresses = new ArrayList();
         }

         this.mCcAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailCc(String[] var1) {
         this.combineArrayExtra("android.intent.extra.CC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailTo(String var1) {
         if (this.mToAddresses == null) {
            this.mToAddresses = new ArrayList();
         }

         this.mToAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailTo(String[] var1) {
         this.combineArrayExtra("android.intent.extra.EMAIL", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addStream(Uri var1) {
         Uri var2 = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
         if (this.mStreams == null && var2 == null) {
            return this.setStream(var1);
         } else {
            if (this.mStreams == null) {
               this.mStreams = new ArrayList();
            }

            if (var2 != null) {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
               this.mStreams.add(var2);
            }

            this.mStreams.add(var1);
            return this;
         }
      }

      public Intent createChooserIntent() {
         return Intent.createChooser(this.getIntent(), this.mChooserTitle);
      }

      Activity getActivity() {
         return this.mActivity;
      }

      public Intent getIntent() {
         ArrayList var3 = this.mToAddresses;
         if (var3 != null) {
            this.combineArrayExtra("android.intent.extra.EMAIL", var3);
            this.mToAddresses = null;
         }

         var3 = this.mCcAddresses;
         if (var3 != null) {
            this.combineArrayExtra("android.intent.extra.CC", var3);
            this.mCcAddresses = null;
         }

         var3 = this.mBccAddresses;
         if (var3 != null) {
            this.combineArrayExtra("android.intent.extra.BCC", var3);
            this.mBccAddresses = null;
         }

         var3 = this.mStreams;
         boolean var1 = true;
         if (var3 == null || var3.size() <= 1) {
            var1 = false;
         }

         boolean var2 = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
         if (!var1 && var2) {
            this.mIntent.setAction("android.intent.action.SEND");
            var3 = this.mStreams;
            if (var3 != null && !var3.isEmpty()) {
               this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
            } else {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
            }

            this.mStreams = null;
         }

         if (var1 && !var2) {
            this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
            var3 = this.mStreams;
            if (var3 != null && !var3.isEmpty()) {
               this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
            } else {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
            }
         }

         return this.mIntent;
      }

      public ShareCompat.IntentBuilder setChooserTitle(int var1) {
         return this.setChooserTitle(this.mActivity.getText(var1));
      }

      public ShareCompat.IntentBuilder setChooserTitle(CharSequence var1) {
         this.mChooserTitle = var1;
         return this;
      }

      public ShareCompat.IntentBuilder setEmailBcc(String[] var1) {
         this.mIntent.putExtra("android.intent.extra.BCC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setEmailCc(String[] var1) {
         this.mIntent.putExtra("android.intent.extra.CC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setEmailTo(String[] var1) {
         if (this.mToAddresses != null) {
            this.mToAddresses = null;
         }

         this.mIntent.putExtra("android.intent.extra.EMAIL", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setHtmlText(String var1) {
         this.mIntent.putExtra("android.intent.extra.HTML_TEXT", var1);
         if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
            this.setText(Html.fromHtml(var1));
         }

         return this;
      }

      public ShareCompat.IntentBuilder setStream(Uri var1) {
         if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
            this.mIntent.setAction("android.intent.action.SEND");
         }

         this.mStreams = null;
         this.mIntent.putExtra("android.intent.extra.STREAM", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setSubject(String var1) {
         this.mIntent.putExtra("android.intent.extra.SUBJECT", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setText(CharSequence var1) {
         this.mIntent.putExtra("android.intent.extra.TEXT", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setType(String var1) {
         this.mIntent.setType(var1);
         return this;
      }

      public void startChooser() {
         this.mActivity.startActivity(this.createChooserIntent());
      }
   }

   public static class IntentReader {
      private static final String TAG = "IntentReader";
      private Activity mActivity;
      private ComponentName mCallingActivity;
      private String mCallingPackage;
      private Intent mIntent;
      private ArrayList mStreams;

      private IntentReader(Activity var1) {
         this.mActivity = var1;
         this.mIntent = var1.getIntent();
         this.mCallingPackage = ShareCompat.getCallingPackage(var1);
         this.mCallingActivity = ShareCompat.getCallingActivity(var1);
      }

      public static ShareCompat.IntentReader from(Activity var0) {
         return new ShareCompat.IntentReader(var0);
      }

      private static void withinStyle(StringBuilder var0, CharSequence var1, int var2, int var3) {
         for(; var2 < var3; ++var2) {
            char var4 = var1.charAt(var2);
            if (var4 == '<') {
               var0.append("&lt;");
            } else if (var4 == '>') {
               var0.append("&gt;");
            } else if (var4 == '&') {
               var0.append("&amp;");
            } else if (var4 <= '~' && var4 >= ' ') {
               if (var4 != ' ') {
                  var0.append(var4);
               } else {
                  while(var2 + 1 < var3 && var1.charAt(var2 + 1) == ' ') {
                     var0.append("&nbsp;");
                     ++var2;
                  }

                  var0.append(' ');
               }
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("&#");
               var5.append(var4);
               var5.append(";");
               var0.append(var5.toString());
            }
         }

      }

      public ComponentName getCallingActivity() {
         return this.mCallingActivity;
      }

      public Drawable getCallingActivityIcon() {
         if (this.mCallingActivity == null) {
            return null;
         } else {
            PackageManager var1 = this.mActivity.getPackageManager();

            try {
               Drawable var3 = var1.getActivityIcon(this.mCallingActivity);
               return var3;
            } catch (NameNotFoundException var2) {
               Log.e("IntentReader", "Could not retrieve icon for calling activity", var2);
               return null;
            }
         }
      }

      public Drawable getCallingApplicationIcon() {
         if (this.mCallingPackage == null) {
            return null;
         } else {
            PackageManager var1 = this.mActivity.getPackageManager();

            try {
               Drawable var3 = var1.getApplicationIcon(this.mCallingPackage);
               return var3;
            } catch (NameNotFoundException var2) {
               Log.e("IntentReader", "Could not retrieve icon for calling application", var2);
               return null;
            }
         }
      }

      public CharSequence getCallingApplicationLabel() {
         if (this.mCallingPackage == null) {
            return null;
         } else {
            PackageManager var1 = this.mActivity.getPackageManager();

            try {
               CharSequence var3 = var1.getApplicationLabel(var1.getApplicationInfo(this.mCallingPackage, 0));
               return var3;
            } catch (NameNotFoundException var2) {
               Log.e("IntentReader", "Could not retrieve label for calling application", var2);
               return null;
            }
         }
      }

      public String getCallingPackage() {
         return this.mCallingPackage;
      }

      public String[] getEmailBcc() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
      }

      public String[] getEmailCc() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
      }

      public String[] getEmailTo() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
      }

      public String getHtmlText() {
         String var2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
         String var1 = var2;
         if (var2 == null) {
            CharSequence var3 = this.getText();
            if (var3 instanceof Spanned) {
               return Html.toHtml((Spanned)var3);
            }

            var1 = var2;
            if (var3 != null) {
               if (VERSION.SDK_INT >= 16) {
                  return Html.escapeHtml(var3);
               }

               StringBuilder var4 = new StringBuilder();
               withinStyle(var4, var3, 0, var3.length());
               var1 = var4.toString();
            }
         }

         return var1;
      }

      public Uri getStream() {
         return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
      }

      public Uri getStream(int var1) {
         if (this.mStreams == null && this.isMultipleShare()) {
            this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
         }

         ArrayList var2 = this.mStreams;
         if (var2 != null) {
            return (Uri)var2.get(var1);
         } else if (var1 == 0) {
            return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Stream items available: ");
            var3.append(this.getStreamCount());
            var3.append(" index requested: ");
            var3.append(var1);
            throw new IndexOutOfBoundsException(var3.toString());
         }
      }

      public int getStreamCount() {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }

      public String getSubject() {
         return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
      }

      public CharSequence getText() {
         return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
      }

      public String getType() {
         return this.mIntent.getType();
      }

      public boolean isMultipleShare() {
         return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
      }

      public boolean isShareIntent() {
         String var1 = this.mIntent.getAction();
         return "android.intent.action.SEND".equals(var1) || "android.intent.action.SEND_MULTIPLE".equals(var1);
      }

      public boolean isSingleShare() {
         return "android.intent.action.SEND".equals(this.mIntent.getAction());
      }
   }
}
