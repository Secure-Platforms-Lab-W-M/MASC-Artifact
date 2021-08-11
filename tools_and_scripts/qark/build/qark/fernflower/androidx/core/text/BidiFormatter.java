package androidx.core.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
   private static final int DEFAULT_FLAGS = 2;
   static final BidiFormatter DEFAULT_LTR_INSTANCE;
   static final BidiFormatter DEFAULT_RTL_INSTANCE;
   static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
   private static final int DIR_LTR = -1;
   private static final int DIR_RTL = 1;
   private static final int DIR_UNKNOWN = 0;
   private static final String EMPTY_STRING = "";
   private static final int FLAG_STEREO_RESET = 2;
   private static final char LRE = '\u202a';
   private static final char LRM = '\u200e';
   private static final String LRM_STRING;
   private static final char PDF = '\u202c';
   private static final char RLE = '\u202b';
   private static final char RLM = '\u200f';
   private static final String RLM_STRING;
   private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
   private final int mFlags;
   private final boolean mIsRtlContext;

   static {
      DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
      LRM_STRING = Character.toString('\u200e');
      RLM_STRING = Character.toString('\u200f');
      DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
      DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
   }

   BidiFormatter(boolean var1, int var2, TextDirectionHeuristicCompat var3) {
      this.mIsRtlContext = var1;
      this.mFlags = var2;
      this.mDefaultTextDirectionHeuristicCompat = var3;
   }

   private static int getEntryDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getEntryDir();
   }

   private static int getExitDir(CharSequence var0) {
      return (new BidiFormatter.DirectionalityEstimator(var0, false)).getExitDir();
   }

   public static BidiFormatter getInstance() {
      return (new BidiFormatter.Builder()).build();
   }

   public static BidiFormatter getInstance(Locale var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   public static BidiFormatter getInstance(boolean var0) {
      return (new BidiFormatter.Builder(var0)).build();
   }

   static boolean isRtlLocale(Locale var0) {
      return TextUtilsCompat.getLayoutDirectionFromLocale(var0) == 1;
   }

   private String markAfter(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      if (this.mIsRtlContext || !var3 && getExitDir(var1) != 1) {
         return !this.mIsRtlContext || var3 && getExitDir(var1) != -1 ? "" : RLM_STRING;
      } else {
         return LRM_STRING;
      }
   }

   private String markBefore(CharSequence var1, TextDirectionHeuristicCompat var2) {
      boolean var3 = var2.isRtl((CharSequence)var1, 0, var1.length());
      if (this.mIsRtlContext || !var3 && getEntryDir(var1) != 1) {
         return !this.mIsRtlContext || var3 && getEntryDir(var1) != -1 ? "" : RLM_STRING;
      } else {
         return LRM_STRING;
      }
   }

   public boolean getStereoReset() {
      return (this.mFlags & 2) != 0;
   }

   public boolean isRtl(CharSequence var1) {
      return this.mDefaultTextDirectionHeuristicCompat.isRtl((CharSequence)var1, 0, var1.length());
   }

   public boolean isRtl(String var1) {
      return this.isRtl((CharSequence)var1);
   }

   public boolean isRtlContext() {
      return this.mIsRtlContext;
   }

   public CharSequence unicodeWrap(CharSequence var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public CharSequence unicodeWrap(CharSequence var1, TextDirectionHeuristicCompat var2, boolean var3) {
      if (var1 == null) {
         return null;
      } else {
         boolean var5 = var2.isRtl((CharSequence)var1, 0, var1.length());
         SpannableStringBuilder var6 = new SpannableStringBuilder();
         if (this.getStereoReset() && var3) {
            if (var5) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var6.append(this.markBefore(var1, var2));
         }

         if (var5 != this.mIsRtlContext) {
            char var4;
            if (var5) {
               var4 = 8235;
            } else {
               var4 = 8234;
            }

            var6.append(var4);
            var6.append(var1);
            var6.append('\u202c');
         } else {
            var6.append(var1);
         }

         if (var3) {
            if (var5) {
               var2 = TextDirectionHeuristicsCompat.RTL;
            } else {
               var2 = TextDirectionHeuristicsCompat.LTR;
            }

            var6.append(this.markAfter(var1, var2));
         }

         return var6;
      }
   }

   public CharSequence unicodeWrap(CharSequence var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public String unicodeWrap(String var1) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2) {
      return this.unicodeWrap(var1, var2, true);
   }

   public String unicodeWrap(String var1, TextDirectionHeuristicCompat var2, boolean var3) {
      return var1 == null ? null : this.unicodeWrap((CharSequence)var1, var2, var3).toString();
   }

   public String unicodeWrap(String var1, boolean var2) {
      return this.unicodeWrap(var1, this.mDefaultTextDirectionHeuristicCompat, var2);
   }

   public static final class Builder {
      private int mFlags;
      private boolean mIsRtlContext;
      private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

      public Builder() {
         this.initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
      }

      public Builder(Locale var1) {
         this.initialize(BidiFormatter.isRtlLocale(var1));
      }

      public Builder(boolean var1) {
         this.initialize(var1);
      }

      private static BidiFormatter getDefaultInstanceFromContext(boolean var0) {
         return var0 ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
      }

      private void initialize(boolean var1) {
         this.mIsRtlContext = var1;
         this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
         this.mFlags = 2;
      }

      public BidiFormatter build() {
         return this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
      }

      public BidiFormatter.Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat var1) {
         this.mTextDirectionHeuristicCompat = var1;
         return this;
      }

      public BidiFormatter.Builder stereoReset(boolean var1) {
         if (var1) {
            this.mFlags |= 2;
            return this;
         } else {
            this.mFlags &= -3;
            return this;
         }
      }
   }

   private static class DirectionalityEstimator {
      private static final byte[] DIR_TYPE_CACHE = new byte[1792];
      private static final int DIR_TYPE_CACHE_SIZE = 1792;
      private int charIndex;
      private final boolean isHtml;
      private char lastChar;
      private final int length;
      private final CharSequence text;

      static {
         for(int var0 = 0; var0 < 1792; ++var0) {
            DIR_TYPE_CACHE[var0] = Character.getDirectionality(var0);
         }

      }

      DirectionalityEstimator(CharSequence var1, boolean var2) {
         this.text = var1;
         this.isHtml = var2;
         this.length = var1.length();
      }

      private static byte getCachedDirectionality(char var0) {
         return var0 < 1792 ? DIR_TYPE_CACHE[var0] : Character.getDirectionality(var0);
      }

      private byte skipEntityBackward() {
         int var2 = this.charIndex;

         char var1;
         do {
            int var3 = this.charIndex;
            if (var3 <= 0) {
               break;
            }

            CharSequence var4 = this.text;
            --var3;
            this.charIndex = var3;
            var1 = var4.charAt(var3);
            this.lastChar = var1;
            if (var1 == '&') {
               return 12;
            }
         } while(var1 != ';');

         this.charIndex = var2;
         this.lastChar = ';';
         return 13;
      }

      private byte skipEntityForward() {
         while(true) {
            int var2 = this.charIndex;
            if (var2 < this.length) {
               CharSequence var3 = this.text;
               this.charIndex = var2 + 1;
               char var1 = var3.charAt(var2);
               this.lastChar = var1;
               if (var1 != ';') {
                  continue;
               }
            }

            return 12;
         }
      }

      private byte skipTagBackward() {
         int var2 = this.charIndex;

         while(true) {
            int var3 = this.charIndex;
            if (var3 <= 0) {
               break;
            }

            CharSequence var5 = this.text;
            --var3;
            this.charIndex = var3;
            char var1 = var5.charAt(var3);
            this.lastChar = var1;
            if (var1 == '<') {
               return 12;
            }

            if (var1 == '>') {
               break;
            }

            if (var1 == '"' || var1 == '\'') {
               char var6 = this.lastChar;

               while(true) {
                  int var4 = this.charIndex;
                  if (var4 <= 0) {
                     break;
                  }

                  var5 = this.text;
                  --var4;
                  this.charIndex = var4;
                  var1 = var5.charAt(var4);
                  this.lastChar = var1;
                  if (var1 == var6) {
                     break;
                  }
               }
            }
         }

         this.charIndex = var2;
         this.lastChar = '>';
         return 13;
      }

      private byte skipTagForward() {
         int var2 = this.charIndex;

         while(true) {
            char var1;
            CharSequence var5;
            do {
               int var3 = this.charIndex;
               if (var3 >= this.length) {
                  this.charIndex = var2;
                  this.lastChar = '<';
                  return 13;
               }

               var5 = this.text;
               this.charIndex = var3 + 1;
               var1 = var5.charAt(var3);
               this.lastChar = var1;
               if (var1 == '>') {
                  return 12;
               }
            } while(var1 != '"' && var1 != '\'');

            char var6 = this.lastChar;

            while(true) {
               int var4 = this.charIndex;
               if (var4 >= this.length) {
                  break;
               }

               var5 = this.text;
               this.charIndex = var4 + 1;
               var1 = var5.charAt(var4);
               this.lastChar = var1;
               if (var1 == var6) {
                  break;
               }
            }
         }
      }

      byte dirTypeBackward() {
         char var3 = this.text.charAt(this.charIndex - 1);
         this.lastChar = var3;
         if (Character.isLowSurrogate(var3)) {
            int var5 = Character.codePointBefore(this.text, this.charIndex);
            this.charIndex -= Character.charCount(var5);
            return Character.getDirectionality(var5);
         } else {
            --this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            byte var1 = var2;
            if (this.isHtml) {
               char var4 = this.lastChar;
               if (var4 == '>') {
                  return this.skipTagBackward();
               }

               var1 = var2;
               if (var4 == ';') {
                  var1 = this.skipEntityBackward();
               }
            }

            return var1;
         }
      }

      byte dirTypeForward() {
         char var3 = this.text.charAt(this.charIndex);
         this.lastChar = var3;
         if (Character.isHighSurrogate(var3)) {
            int var5 = Character.codePointAt(this.text, this.charIndex);
            this.charIndex += Character.charCount(var5);
            return Character.getDirectionality(var5);
         } else {
            ++this.charIndex;
            byte var2 = getCachedDirectionality(this.lastChar);
            byte var1 = var2;
            if (this.isHtml) {
               char var4 = this.lastChar;
               if (var4 == '<') {
                  return this.skipTagForward();
               }

               var1 = var2;
               if (var4 == '&') {
                  var1 = this.skipEntityForward();
               }
            }

            return var1;
         }
      }

      int getEntryDir() {
         this.charIndex = 0;
         int var1 = 0;
         byte var3 = 0;
         int var2 = 0;

         while(this.charIndex < this.length && var2 == 0) {
            byte var4 = this.dirTypeForward();
            if (var4 != 0) {
               if (var4 != 1 && var4 != 2) {
                  if (var4 != 9) {
                     switch(var4) {
                     case 14:
                     case 15:
                        ++var1;
                        var3 = -1;
                        break;
                     case 16:
                     case 17:
                        ++var1;
                        var3 = 1;
                        break;
                     case 18:
                        --var1;
                        var3 = 0;
                        break;
                     default:
                        var2 = var1;
                     }
                  }
               } else {
                  if (var1 == 0) {
                     return 1;
                  }

                  var2 = var1;
               }
            } else {
               if (var1 == 0) {
                  return -1;
               }

               var2 = var1;
            }
         }

         if (var2 == 0) {
            return 0;
         } else if (var3 != 0) {
            return var3;
         } else {
            while(this.charIndex > 0) {
               switch(this.dirTypeBackward()) {
               case 14:
               case 15:
                  if (var2 == var1) {
                     return -1;
                  }

                  --var1;
                  break;
               case 16:
               case 17:
                  if (var2 == var1) {
                     return 1;
                  }

                  --var1;
                  break;
               case 18:
                  ++var1;
               }
            }

            return 0;
         }
      }

      int getExitDir() {
         this.charIndex = this.length;
         int var1 = 0;
         int var2 = 0;

         while(true) {
            while(true) {
               while(this.charIndex > 0) {
                  byte var3 = this.dirTypeBackward();
                  if (var3 != 0) {
                     if (var3 != 1 && var3 != 2) {
                        if (var3 != 9) {
                           switch(var3) {
                           case 14:
                           case 15:
                              if (var2 == var1) {
                                 return -1;
                              }

                              --var1;
                              break;
                           case 16:
                           case 17:
                              if (var2 == var1) {
                                 return 1;
                              }

                              --var1;
                              break;
                           case 18:
                              ++var1;
                              break;
                           default:
                              if (var2 == 0) {
                                 var2 = var1;
                              }
                           }
                        }
                     } else {
                        if (var1 == 0) {
                           return 1;
                        }

                        if (var2 == 0) {
                           var2 = var1;
                        }
                     }
                  } else {
                     if (var1 == 0) {
                        return -1;
                     }

                     if (var2 == 0) {
                        var2 = var1;
                     }
                  }
               }

               return 0;
            }
         }
      }
   }
}
