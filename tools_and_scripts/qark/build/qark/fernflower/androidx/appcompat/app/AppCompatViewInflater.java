package androidx.appcompat.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.R.styleable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AppCompatViewInflater {
   private static final String LOG_TAG = "AppCompatViewInflater";
   private static final String[] sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
   private static final Map sConstructorMap = new ArrayMap();
   private static final Class[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
   private static final int[] sOnClickAttrs = new int[]{16843375};
   private final Object[] mConstructorArgs = new Object[2];

   private void checkOnClickListener(View var1, AttributeSet var2) {
      Context var3 = var1.getContext();
      if (var3 instanceof ContextWrapper) {
         if (VERSION.SDK_INT < 15 || ViewCompat.hasOnClickListeners(var1)) {
            TypedArray var4 = var3.obtainStyledAttributes(var2, sOnClickAttrs);
            String var5 = var4.getString(0);
            if (var5 != null) {
               var1.setOnClickListener(new AppCompatViewInflater.DeclaredOnClickListener(var1, var5));
            }

            var4.recycle();
         }
      }
   }

   private View createViewByPrefix(Context var1, String var2, String var3) throws ClassNotFoundException, InflateException {
      Constructor var5 = (Constructor)sConstructorMap.get(var2);
      Constructor var4 = var5;
      boolean var10001;
      if (var5 == null) {
         if (var3 != null) {
            try {
               StringBuilder var10 = new StringBuilder();
               var10.append(var3);
               var10.append(var2);
               var3 = var10.toString();
            } catch (Exception var8) {
               var10001 = false;
               return null;
            }
         } else {
            var3 = var2;
         }

         try {
            var4 = Class.forName(var3, false, var1.getClassLoader()).asSubclass(View.class).getConstructor(sConstructorSignature);
            sConstructorMap.put(var2, var4);
         } catch (Exception var7) {
            var10001 = false;
            return null;
         }
      }

      try {
         var4.setAccessible(true);
         View var9 = (View)var4.newInstance(this.mConstructorArgs);
         return var9;
      } catch (Exception var6) {
         var10001 = false;
         return null;
      }
   }

   private View createViewFromTag(Context param1, String param2, AttributeSet param3) {
      // $FF: Couldn't be decompiled
   }

   private static Context themifyContext(Context var0, AttributeSet var1, boolean var2, boolean var3) {
      TypedArray var6 = var0.obtainStyledAttributes(var1, styleable.View, 0, 0);
      int var4 = 0;
      if (var2) {
         var4 = var6.getResourceId(styleable.View_android_theme, 0);
      }

      int var5 = var4;
      if (var3) {
         var5 = var4;
         if (var4 == 0) {
            var4 = var6.getResourceId(styleable.View_theme, 0);
            var5 = var4;
            if (var4 != 0) {
               Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
               var5 = var4;
            }
         }
      }

      var6.recycle();
      Object var7 = var0;
      if (var5 != 0) {
         if (var0 instanceof ContextThemeWrapper) {
            var7 = var0;
            if (((ContextThemeWrapper)var0).getThemeResId() == var5) {
               return (Context)var7;
            }
         }

         var7 = new ContextThemeWrapper(var0, var5);
      }

      return (Context)var7;
   }

   private void verifyNotNull(View var1, String var2) {
      if (var1 == null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(this.getClass().getName());
         var3.append(" asked to inflate view for <");
         var3.append(var2);
         var3.append(">, but returned null");
         throw new IllegalStateException(var3.toString());
      }
   }

   protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context var1, AttributeSet var2) {
      return new AppCompatAutoCompleteTextView(var1, var2);
   }

   protected AppCompatButton createButton(Context var1, AttributeSet var2) {
      return new AppCompatButton(var1, var2);
   }

   protected AppCompatCheckBox createCheckBox(Context var1, AttributeSet var2) {
      return new AppCompatCheckBox(var1, var2);
   }

   protected AppCompatCheckedTextView createCheckedTextView(Context var1, AttributeSet var2) {
      return new AppCompatCheckedTextView(var1, var2);
   }

   protected AppCompatEditText createEditText(Context var1, AttributeSet var2) {
      return new AppCompatEditText(var1, var2);
   }

   protected AppCompatImageButton createImageButton(Context var1, AttributeSet var2) {
      return new AppCompatImageButton(var1, var2);
   }

   protected AppCompatImageView createImageView(Context var1, AttributeSet var2) {
      return new AppCompatImageView(var1, var2);
   }

   protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context var1, AttributeSet var2) {
      return new AppCompatMultiAutoCompleteTextView(var1, var2);
   }

   protected AppCompatRadioButton createRadioButton(Context var1, AttributeSet var2) {
      return new AppCompatRadioButton(var1, var2);
   }

   protected AppCompatRatingBar createRatingBar(Context var1, AttributeSet var2) {
      return new AppCompatRatingBar(var1, var2);
   }

   protected AppCompatSeekBar createSeekBar(Context var1, AttributeSet var2) {
      return new AppCompatSeekBar(var1, var2);
   }

   protected AppCompatSpinner createSpinner(Context var1, AttributeSet var2) {
      return new AppCompatSpinner(var1, var2);
   }

   protected AppCompatTextView createTextView(Context var1, AttributeSet var2) {
      return new AppCompatTextView(var1, var2);
   }

   protected AppCompatToggleButton createToggleButton(Context var1, AttributeSet var2) {
      return new AppCompatToggleButton(var1, var2);
   }

   protected View createView(Context var1, String var2, AttributeSet var3) {
      return null;
   }

   final View createView(View var1, String var2, Context var3, AttributeSet var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      Context var10 = var3;
      if (var5) {
         var10 = var3;
         if (var1 != null) {
            var10 = var1.getContext();
         }
      }

      Context var12;
      label96: {
         if (!var6) {
            var12 = var10;
            if (!var7) {
               break label96;
            }
         }

         var12 = themifyContext(var10, var4, var6, var7);
      }

      var10 = var12;
      if (var8) {
         var10 = TintContextWrapper.wrap(var12);
      }

      byte var9 = -1;
      switch(var2.hashCode()) {
      case -1946472170:
         if (var2.equals("RatingBar")) {
            var9 = 11;
         }
         break;
      case -1455429095:
         if (var2.equals("CheckedTextView")) {
            var9 = 8;
         }
         break;
      case -1346021293:
         if (var2.equals("MultiAutoCompleteTextView")) {
            var9 = 10;
         }
         break;
      case -938935918:
         if (var2.equals("TextView")) {
            var9 = 0;
         }
         break;
      case -937446323:
         if (var2.equals("ImageButton")) {
            var9 = 5;
         }
         break;
      case -658531749:
         if (var2.equals("SeekBar")) {
            var9 = 12;
         }
         break;
      case -339785223:
         if (var2.equals("Spinner")) {
            var9 = 4;
         }
         break;
      case 776382189:
         if (var2.equals("RadioButton")) {
            var9 = 7;
         }
         break;
      case 799298502:
         if (var2.equals("ToggleButton")) {
            var9 = 13;
         }
         break;
      case 1125864064:
         if (var2.equals("ImageView")) {
            var9 = 1;
         }
         break;
      case 1413872058:
         if (var2.equals("AutoCompleteTextView")) {
            var9 = 9;
         }
         break;
      case 1601505219:
         if (var2.equals("CheckBox")) {
            var9 = 6;
         }
         break;
      case 1666676343:
         if (var2.equals("EditText")) {
            var9 = 3;
         }
         break;
      case 2001146706:
         if (var2.equals("Button")) {
            var9 = 2;
         }
      }

      Object var13;
      switch(var9) {
      case 0:
         var13 = this.createTextView(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 1:
         var13 = this.createImageView(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 2:
         var13 = this.createButton(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 3:
         var13 = this.createEditText(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 4:
         var13 = this.createSpinner(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 5:
         var13 = this.createImageButton(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 6:
         var13 = this.createCheckBox(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 7:
         var13 = this.createRadioButton(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 8:
         var13 = this.createCheckedTextView(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 9:
         var13 = this.createAutoCompleteTextView(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 10:
         var13 = this.createMultiAutoCompleteTextView(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 11:
         var13 = this.createRatingBar(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 12:
         var13 = this.createSeekBar(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      case 13:
         var13 = this.createToggleButton(var10, var4);
         this.verifyNotNull((View)var13, var2);
         break;
      default:
         var13 = this.createView(var10, var2, var4);
      }

      Object var11 = var13;
      if (var13 == null) {
         var11 = var13;
         if (var3 != var10) {
            var11 = this.createViewFromTag(var10, var2, var4);
         }
      }

      if (var11 != null) {
         this.checkOnClickListener((View)var11, var4);
      }

      return (View)var11;
   }

   private static class DeclaredOnClickListener implements OnClickListener {
      private final View mHostView;
      private final String mMethodName;
      private Context mResolvedContext;
      private Method mResolvedMethod;

      public DeclaredOnClickListener(View var1, String var2) {
         this.mHostView = var1;
         this.mMethodName = var2;
      }

      private void resolveMethod(Context var1, String var2) {
         while(var1 != null) {
            label45: {
               boolean var10001;
               Method var8;
               try {
                  if (var1.isRestricted()) {
                     break label45;
                  }

                  var8 = var1.getClass().getMethod(this.mMethodName, View.class);
               } catch (NoSuchMethodException var5) {
                  var10001 = false;
                  break label45;
               }

               if (var8 != null) {
                  try {
                     this.mResolvedMethod = var8;
                     this.mResolvedContext = var1;
                     return;
                  } catch (NoSuchMethodException var4) {
                     var10001 = false;
                  }
               }
            }

            if (var1 instanceof ContextWrapper) {
               var1 = ((ContextWrapper)var1).getBaseContext();
            } else {
               var1 = null;
            }
         }

         int var3 = this.mHostView.getId();
         String var6;
         if (var3 == -1) {
            var6 = "";
         } else {
            StringBuilder var7 = new StringBuilder();
            var7.append(" with id '");
            var7.append(this.mHostView.getContext().getResources().getResourceEntryName(var3));
            var7.append("'");
            var6 = var7.toString();
         }

         StringBuilder var9 = new StringBuilder();
         var9.append("Could not find method ");
         var9.append(this.mMethodName);
         var9.append("(View) in a parent or ancestor Context for android:onClick attribute defined on view ");
         var9.append(this.mHostView.getClass());
         var9.append(var6);
         throw new IllegalStateException(var9.toString());
      }

      public void onClick(View var1) {
         if (this.mResolvedMethod == null) {
            this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
         }

         try {
            this.mResolvedMethod.invoke(this.mResolvedContext, var1);
         } catch (IllegalAccessException var2) {
            throw new IllegalStateException("Could not execute non-public method for android:onClick", var2);
         } catch (InvocationTargetException var3) {
            throw new IllegalStateException("Could not execute method for android:onClick", var3);
         }
      }
   }
}
