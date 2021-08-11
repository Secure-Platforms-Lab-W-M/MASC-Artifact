package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TransformationMethod;
import android.text.method.TextKeyListener.Capitalize;
import android.util.Log;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.library.baseAdapters.R.id;

public class TextViewBindingAdapter {
   public static final int DECIMAL = 5;
   public static final int INTEGER = 1;
   public static final int SIGNED = 3;
   private static final String TAG = "TextViewBindingAdapters";

   public static String getTextString(TextView var0) {
      return var0.getText().toString();
   }

   private static boolean haveContentsChanged(CharSequence var0, CharSequence var1) {
      boolean var2;
      if (var0 == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      boolean var3;
      if (var1 == null) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var2 != var3) {
         return true;
      } else if (var0 == null) {
         return false;
      } else {
         int var5 = var0.length();
         if (var5 != var1.length()) {
            return true;
         } else {
            for(int var4 = 0; var4 < var5; ++var4) {
               if (var0.charAt(var4) != var1.charAt(var4)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static void setAutoText(TextView var0, boolean var1) {
      KeyListener var4 = var0.getKeyListener();
      Capitalize var3 = Capitalize.NONE;
      int var2;
      if (var4 != null) {
         var2 = var4.getInputType();
      } else {
         var2 = 0;
      }

      if ((var2 & 4096) != 0) {
         var3 = Capitalize.CHARACTERS;
      } else if ((var2 & 8192) != 0) {
         var3 = Capitalize.WORDS;
      } else if ((var2 & 16384) != 0) {
         var3 = Capitalize.SENTENCES;
      }

      var0.setKeyListener(TextKeyListener.getInstance(var1, var3));
   }

   public static void setBufferType(TextView var0, BufferType var1) {
      var0.setText(var0.getText(), var1);
   }

   public static void setCapitalize(TextView var0, Capitalize var1) {
      boolean var2;
      if (('耀' & var0.getKeyListener().getInputType()) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var0.setKeyListener(TextKeyListener.getInstance(var2, var1));
   }

   public static void setDigits(TextView var0, CharSequence var1) {
      if (var1 != null) {
         var0.setKeyListener(DigitsKeyListener.getInstance(var1.toString()));
      } else {
         if (var0.getKeyListener() instanceof DigitsKeyListener) {
            var0.setKeyListener((KeyListener)null);
         }

      }
   }

   public static void setDrawableBottom(TextView var0, Drawable var1) {
      setIntrinsicBounds(var1);
      Drawable[] var2 = var0.getCompoundDrawables();
      var0.setCompoundDrawables(var2[0], var2[1], var2[2], var1);
   }

   public static void setDrawableEnd(TextView var0, Drawable var1) {
      if (VERSION.SDK_INT < 17) {
         setDrawableRight(var0, var1);
      } else {
         setIntrinsicBounds(var1);
         Drawable[] var2 = var0.getCompoundDrawablesRelative();
         var0.setCompoundDrawablesRelative(var2[0], var2[1], var1, var2[3]);
      }
   }

   public static void setDrawableLeft(TextView var0, Drawable var1) {
      setIntrinsicBounds(var1);
      Drawable[] var2 = var0.getCompoundDrawables();
      var0.setCompoundDrawables(var1, var2[1], var2[2], var2[3]);
   }

   public static void setDrawableRight(TextView var0, Drawable var1) {
      setIntrinsicBounds(var1);
      Drawable[] var2 = var0.getCompoundDrawables();
      var0.setCompoundDrawables(var2[0], var2[1], var1, var2[3]);
   }

   public static void setDrawableStart(TextView var0, Drawable var1) {
      if (VERSION.SDK_INT < 17) {
         setDrawableLeft(var0, var1);
      } else {
         setIntrinsicBounds(var1);
         Drawable[] var2 = var0.getCompoundDrawablesRelative();
         var0.setCompoundDrawablesRelative(var1, var2[1], var2[2], var2[3]);
      }
   }

   public static void setDrawableTop(TextView var0, Drawable var1) {
      setIntrinsicBounds(var1);
      Drawable[] var2 = var0.getCompoundDrawables();
      var0.setCompoundDrawables(var2[0], var1, var2[2], var2[3]);
   }

   public static void setImeActionLabel(TextView var0, int var1) {
      var0.setImeActionLabel(var0.getImeActionLabel(), var1);
   }

   public static void setImeActionLabel(TextView var0, CharSequence var1) {
      var0.setImeActionLabel(var1, var0.getImeActionId());
   }

   public static void setInputMethod(TextView var0, CharSequence var1) {
      StringBuilder var2;
      try {
         var0.setKeyListener((KeyListener)Class.forName(var1.toString()).newInstance());
      } catch (ClassNotFoundException var3) {
         var2 = new StringBuilder();
         var2.append("Could not create input method: ");
         var2.append(var1);
         Log.e("TextViewBindingAdapters", var2.toString(), var3);
      } catch (InstantiationException var4) {
         var2 = new StringBuilder();
         var2.append("Could not create input method: ");
         var2.append(var1);
         Log.e("TextViewBindingAdapters", var2.toString(), var4);
      } catch (IllegalAccessException var5) {
         var2 = new StringBuilder();
         var2.append("Could not create input method: ");
         var2.append(var1);
         Log.e("TextViewBindingAdapters", var2.toString(), var5);
         return;
      }

   }

   private static void setIntrinsicBounds(Drawable var0) {
      if (var0 != null) {
         var0.setBounds(0, 0, var0.getIntrinsicWidth(), var0.getIntrinsicHeight());
      }

   }

   public static void setLineSpacingExtra(TextView var0, float var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setLineSpacing(var1, var0.getLineSpacingMultiplier());
      } else {
         var0.setLineSpacing(var1, 1.0F);
      }
   }

   public static void setLineSpacingMultiplier(TextView var0, float var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setLineSpacing(var0.getLineSpacingExtra(), var1);
      } else {
         var0.setLineSpacing(0.0F, var1);
      }
   }

   public static void setMaxLength(TextView var0, int var1) {
      InputFilter[] var7 = var0.getFilters();
      InputFilter[] var6;
      if (var7 == null) {
         var6 = new InputFilter[]{new LengthFilter(var1)};
      } else {
         boolean var4 = false;
         int var3 = 0;

         boolean var2;
         while(true) {
            var2 = var4;
            if (var3 >= var7.length) {
               break;
            }

            InputFilter var8 = var7[var3];
            if (var8 instanceof LengthFilter) {
               boolean var5 = true;
               var4 = true;
               if (VERSION.SDK_INT >= 21) {
                  if (((LengthFilter)var8).getMax() != var1) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  var4 = var2;
               }

               var2 = var5;
               if (var4) {
                  var7[var3] = new LengthFilter(var1);
                  var2 = var5;
               }
               break;
            }

            ++var3;
         }

         var6 = var7;
         if (!var2) {
            var6 = new InputFilter[var7.length + 1];
            System.arraycopy(var7, 0, var6, 0, var7.length);
            var6[var6.length - 1] = new LengthFilter(var1);
         }
      }

      var0.setFilters(var6);
   }

   public static void setNumeric(TextView var0, int var1) {
      boolean var3 = true;
      boolean var2;
      if ((var1 & 3) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if ((var1 & 5) == 0) {
         var3 = false;
      }

      var0.setKeyListener(DigitsKeyListener.getInstance(var2, var3));
   }

   public static void setPassword(TextView var0, boolean var1) {
      if (var1) {
         var0.setTransformationMethod(PasswordTransformationMethod.getInstance());
      } else {
         if (var0.getTransformationMethod() instanceof PasswordTransformationMethod) {
            var0.setTransformationMethod((TransformationMethod)null);
         }

      }
   }

   public static void setPhoneNumber(TextView var0, boolean var1) {
      if (var1) {
         var0.setKeyListener(DialerKeyListener.getInstance());
      } else {
         if (var0.getKeyListener() instanceof DialerKeyListener) {
            var0.setKeyListener((KeyListener)null);
         }

      }
   }

   public static void setShadowColor(TextView var0, int var1) {
      if (VERSION.SDK_INT >= 16) {
         float var2 = var0.getShadowDx();
         float var3 = var0.getShadowDy();
         var0.setShadowLayer(var0.getShadowRadius(), var2, var3, var1);
      }

   }

   public static void setShadowDx(TextView var0, float var1) {
      if (VERSION.SDK_INT >= 16) {
         int var3 = var0.getShadowColor();
         float var2 = var0.getShadowDy();
         var0.setShadowLayer(var0.getShadowRadius(), var1, var2, var3);
      }

   }

   public static void setShadowDy(TextView var0, float var1) {
      if (VERSION.SDK_INT >= 16) {
         int var3 = var0.getShadowColor();
         float var2 = var0.getShadowDx();
         var0.setShadowLayer(var0.getShadowRadius(), var2, var1, var3);
      }

   }

   public static void setShadowRadius(TextView var0, float var1) {
      if (VERSION.SDK_INT >= 16) {
         int var2 = var0.getShadowColor();
         var0.setShadowLayer(var1, var0.getShadowDx(), var0.getShadowDy(), var2);
      }

   }

   public static void setText(TextView var0, CharSequence var1) {
      CharSequence var2 = var0.getText();
      if (var1 != var2) {
         if (var1 != null || var2.length() != 0) {
            if (var1 instanceof Spanned) {
               if (var1.equals(var2)) {
                  return;
               }
            } else if (!haveContentsChanged(var1, var2)) {
               return;
            }

            var0.setText(var1);
         }
      }
   }

   public static void setTextSize(TextView var0, float var1) {
      var0.setTextSize(0, var1);
   }

   public static void setTextWatcher(TextView var0, final TextViewBindingAdapter.BeforeTextChanged var1, final TextViewBindingAdapter.OnTextChanged var2, final TextViewBindingAdapter.AfterTextChanged var3, final InverseBindingListener var4) {
      TextWatcher var5;
      if (var1 == null && var3 == null && var2 == null && var4 == null) {
         var5 = null;
      } else {
         var5 = new TextWatcher() {
            public void afterTextChanged(Editable var1x) {
               TextViewBindingAdapter.AfterTextChanged var2x = var3;
               if (var2x != null) {
                  var2x.afterTextChanged(var1x);
               }

            }

            public void beforeTextChanged(CharSequence var1x, int var2x, int var3x, int var4x) {
               TextViewBindingAdapter.BeforeTextChanged var5 = var1;
               if (var5 != null) {
                  var5.beforeTextChanged(var1x, var2x, var3x, var4x);
               }

            }

            public void onTextChanged(CharSequence var1x, int var2x, int var3x, int var4x) {
               TextViewBindingAdapter.OnTextChanged var5 = var2;
               if (var5 != null) {
                  var5.onTextChanged(var1x, var2x, var3x, var4x);
               }

               InverseBindingListener var6 = var4;
               if (var6 != null) {
                  var6.onChange();
               }

            }
         };
      }

      TextWatcher var6 = (TextWatcher)ListenerUtil.trackListener(var0, var5, id.textWatcher);
      if (var6 != null) {
         var0.removeTextChangedListener(var6);
      }

      if (var5 != null) {
         var0.addTextChangedListener(var5);
      }

   }

   public interface AfterTextChanged {
      void afterTextChanged(Editable var1);
   }

   public interface BeforeTextChanged {
      void beforeTextChanged(CharSequence var1, int var2, int var3, int var4);
   }

   public interface OnTextChanged {
      void onTextChanged(CharSequence var1, int var2, int var3, int var4);
   }
}
