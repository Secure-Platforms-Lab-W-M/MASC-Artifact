package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class DoubleMetaphone implements StringEncoder {
   private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[]{"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
   private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[]{"L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
   private static final String[] L_T_K_S_N_M_B_Z = new String[]{"L", "T", "K", "S", "N", "M", "B", "Z"};
   private static final String[] SILENT_START = new String[]{"GN", "KN", "PN", "WR", "PS"};
   private static final String VOWELS = "AEIOUY";
   private int maxCodeLen = 4;

   private String cleanInput(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() == 0 ? null : var1.toUpperCase(Locale.ENGLISH);
      }
   }

   private boolean conditionC0(String var1, int var2) {
      if (contains(var1, var2, 4, "CHIA")) {
         return true;
      } else if (var2 <= 1) {
         return false;
      } else if (this.isVowel(this.charAt(var1, var2 - 2))) {
         return false;
      } else if (!contains(var1, var2 - 1, 3, "ACH")) {
         return false;
      } else {
         char var3 = this.charAt(var1, var2 + 2);
         return var3 != 'I' && var3 != 'E' || contains(var1, var2 - 2, 6, "BACHER", "MACHER");
      }
   }

   private boolean conditionCH0(String var1, int var2) {
      if (var2 != 0) {
         return false;
      } else if (!contains(var1, var2 + 1, 5, "HARAC", "HARIS") && !contains(var1, var2 + 1, 3, "HOR", "HYM", "HIA", "HEM")) {
         return false;
      } else {
         return !contains(var1, 0, 5, "CHORE");
      }
   }

   private boolean conditionCH1(String var1, int var2) {
      return contains(var1, 0, 4, "VAN ", "VON ") || contains(var1, 0, 3, "SCH") || contains(var1, var2 - 2, 6, "ORCHES", "ARCHIT", "ORCHID") || contains(var1, var2 + 2, 1, "T", "S") || (contains(var1, var2 - 1, 1, "A", "O", "U", "E") || var2 == 0) && (contains(var1, var2 + 2, 1, L_R_N_M_B_H_F_V_W_SPACE) || var2 + 1 == var1.length() - 1);
   }

   private boolean conditionL0(String var1, int var2) {
      if (var2 == var1.length() - 3 && contains(var1, var2 - 1, 4, "ILLO", "ILLA", "ALLE")) {
         return true;
      } else {
         return (contains(var1, var1.length() - 2, 2, "AS", "OS") || contains(var1, var1.length() - 1, 1, "A", "O")) && contains(var1, var2 - 1, 4, "ALLE");
      }
   }

   private boolean conditionM0(String var1, int var2) {
      char var3 = this.charAt(var1, var2 + 1);
      boolean var4 = true;
      if (var3 == 'M') {
         return true;
      } else {
         if (contains(var1, var2 - 1, 3, "UMB")) {
            if (var2 + 1 == var1.length() - 1) {
               return var4;
            }

            if (contains(var1, var2 + 2, 2, "ER")) {
               return true;
            }
         }

         var4 = false;
         return var4;
      }
   }

   protected static boolean contains(String var0, int var1, int var2, String... var3) {
      if (var1 >= 0 && var1 + var2 <= var0.length()) {
         var0 = var0.substring(var1, var1 + var2);
         var2 = var3.length;

         for(var1 = 0; var1 < var2; ++var1) {
            if (var0.equals(var3[var1])) {
               return true;
            }
         }
      }

      return false;
   }

   private int handleAEIOUY(DoubleMetaphone.DoubleMetaphoneResult var1, int var2) {
      if (var2 == 0) {
         var1.append('A');
      }

      return var2 + 1;
   }

   private int handleC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.conditionC0(var1, var3)) {
         var2.append('K');
         return var3 + 2;
      } else if (var3 == 0 && contains(var1, var3, 6, "CAESAR")) {
         var2.append('S');
         return var3 + 2;
      } else if (contains(var1, var3, 2, "CH")) {
         return this.handleCH(var1, var2, var3);
      } else if (contains(var1, var3, 2, "CZ") && !contains(var1, var3 - 2, 4, "WICZ")) {
         var2.append('S', 'X');
         return var3 + 2;
      } else if (contains(var1, var3 + 1, 3, "CIA")) {
         var2.append('X');
         return var3 + 3;
      } else if (!contains(var1, var3, 2, "CC") || var3 == 1 && this.charAt(var1, 0) == 'M') {
         if (contains(var1, var3, 2, "CK", "CG", "CQ")) {
            var2.append('K');
            return var3 + 2;
         } else if (contains(var1, var3, 2, "CI", "CE", "CY")) {
            if (contains(var1, var3, 3, "CIO", "CIE", "CIA")) {
               var2.append('S', 'X');
            } else {
               var2.append('S');
            }

            return var3 + 2;
         } else {
            var2.append('K');
            if (contains(var1, var3 + 1, 2, " C", " Q", " G")) {
               return var3 + 3;
            } else {
               return contains(var1, var3 + 1, 1, "C", "K", "Q") && !contains(var1, var3 + 1, 2, "CE", "CI") ? var3 + 2 : var3 + 1;
            }
         }
      } else {
         return this.handleCC(var1, var2, var3);
      }
   }

   private int handleCC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (contains(var1, var3 + 2, 1, "I", "E", "H") && !contains(var1, var3 + 2, 2, "HU")) {
         if ((var3 != 1 || this.charAt(var1, var3 - 1) != 'A') && !contains(var1, var3 - 1, 5, "UCCEE", "UCCES")) {
            var2.append('X');
         } else {
            var2.append("KS");
         }

         return var3 + 3;
      } else {
         var2.append('K');
         return var3 + 2;
      }
   }

   private int handleCH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 > 0 && contains(var1, var3, 4, "CHAE")) {
         var2.append('K', 'X');
         return var3 + 2;
      } else if (this.conditionCH0(var1, var3)) {
         var2.append('K');
         return var3 + 2;
      } else if (this.conditionCH1(var1, var3)) {
         var2.append('K');
         return var3 + 2;
      } else {
         if (var3 > 0) {
            if (contains(var1, 0, 2, "MC")) {
               var2.append('K');
            } else {
               var2.append('X', 'K');
            }
         } else {
            var2.append('X');
         }

         return var3 + 2;
      }
   }

   private int handleD(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (contains(var1, var3, 2, "DG")) {
         if (contains(var1, var3 + 2, 1, "I", "E", "Y")) {
            var2.append('J');
            return var3 + 3;
         } else {
            var2.append("TK");
            return var3 + 2;
         }
      } else if (contains(var1, var3, 2, "DT", "DD")) {
         var2.append('T');
         return var3 + 2;
      } else {
         var2.append('T');
         return var3 + 1;
      }
   }

   private int handleG(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         return this.handleGH(var1, var2, var3);
      } else if (this.charAt(var1, var3 + 1) == 'N') {
         if (var3 == 1 && this.isVowel(this.charAt(var1, 0)) && !var4) {
            var2.append("KN", "N");
         } else if (!contains(var1, var3 + 2, 2, "EY") && this.charAt(var1, var3 + 1) != 'Y' && !var4) {
            var2.append("N", "KN");
         } else {
            var2.append("KN");
         }

         return var3 + 2;
      } else if (contains(var1, var3 + 1, 2, "LI") && !var4) {
         var2.append("KL", "L");
         return var3 + 2;
      } else if (var3 != 0 || this.charAt(var1, var3 + 1) != 'Y' && !contains(var1, var3 + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER)) {
         if ((contains(var1, var3 + 1, 2, "ER") || this.charAt(var1, var3 + 1) == 'Y') && !contains(var1, 0, 6, "DANGER", "RANGER", "MANGER") && !contains(var1, var3 - 1, 1, "E", "I") && !contains(var1, var3 - 1, 3, "RGY", "OGY")) {
            var2.append('K', 'J');
            return var3 + 2;
         } else if (!contains(var1, var3 + 1, 1, "E", "I", "Y") && !contains(var1, var3 - 1, 4, "AGGI", "OGGI")) {
            if (this.charAt(var1, var3 + 1) == 'G') {
               var2.append('K');
               return var3 + 2;
            } else {
               var2.append('K');
               return var3 + 1;
            }
         } else {
            if (!contains(var1, 0, 4, "VAN ", "VON ") && !contains(var1, 0, 3, "SCH") && !contains(var1, var3 + 1, 2, "ET")) {
               if (contains(var1, var3 + 1, 3, "IER")) {
                  var2.append('J');
               } else {
                  var2.append('J', 'K');
               }
            } else {
               var2.append('K');
            }

            return var3 + 2;
         }
      } else {
         var2.append('K', 'J');
         return var3 + 2;
      }
   }

   private int handleGH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 > 0 && !this.isVowel(this.charAt(var1, var3 - 1))) {
         var2.append('K');
         return var3 + 2;
      } else if (var3 == 0) {
         if (this.charAt(var1, var3 + 2) == 'I') {
            var2.append('J');
         } else {
            var2.append('K');
         }

         return var3 + 2;
      } else if (var3 > 1 && contains(var1, var3 - 2, 1, "B", "H", "D") || var3 > 2 && contains(var1, var3 - 3, 1, "B", "H", "D") || var3 > 3 && contains(var1, var3 - 4, 1, "B", "H")) {
         return var3 + 2;
      } else {
         if (var3 > 2 && this.charAt(var1, var3 - 1) == 'U' && contains(var1, var3 - 3, 1, "C", "G", "L", "R", "T")) {
            var2.append('F');
         } else if (var3 > 0 && this.charAt(var1, var3 - 1) != 'I') {
            var2.append('K');
         }

         return var3 + 2;
      }
   }

   private int handleH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if ((var3 == 0 || this.isVowel(this.charAt(var1, var3 - 1))) && this.isVowel(this.charAt(var1, var3 + 1))) {
         var2.append('H');
         return var3 + 2;
      } else {
         return var3 + 1;
      }
   }

   private int handleJ(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (!contains(var1, var3, 4, "JOSE") && !contains(var1, 0, 4, "SAN ")) {
         if (var3 == 0 && !contains(var1, var3, 4, "JOSE")) {
            var2.append('J', 'A');
         } else if (this.isVowel(this.charAt(var1, var3 - 1)) && !var4 && (this.charAt(var1, var3 + 1) == 'A' || this.charAt(var1, var3 + 1) == 'O')) {
            var2.append('J', 'H');
         } else if (var3 == var1.length() - 1) {
            var2.append('J', ' ');
         } else if (!contains(var1, var3 + 1, 1, L_T_K_S_N_M_B_Z) && !contains(var1, var3 - 1, 1, "S", "K", "L")) {
            var2.append('J');
         }

         return this.charAt(var1, var3 + 1) == 'J' ? var3 + 2 : var3 + 1;
      } else {
         if ((var3 != 0 || this.charAt(var1, var3 + 4) != ' ') && var1.length() != 4 && !contains(var1, 0, 4, "SAN ")) {
            var2.append('J', 'H');
         } else {
            var2.append('H');
         }

         return var3 + 1;
      }
   }

   private int handleL(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.charAt(var1, var3 + 1) == 'L') {
         if (this.conditionL0(var1, var3)) {
            var2.appendPrimary('L');
         } else {
            var2.append('L');
         }

         return var3 + 2;
      } else {
         var2.append('L');
         return var3 + 1;
      }
   }

   private int handleP(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         var2.append('F');
         return var3 + 2;
      } else {
         var2.append('P');
         if (contains(var1, var3 + 1, 1, "P", "B")) {
            var3 += 2;
         } else {
            ++var3;
         }

         return var3;
      }
   }

   private int handleR(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (var3 == var1.length() - 1 && !var4 && contains(var1, var3 - 2, 2, "IE") && !contains(var1, var3 - 4, 2, "ME", "MA")) {
         var2.appendAlternate('R');
      } else {
         var2.append('R');
      }

      return this.charAt(var1, var3 + 1) == 'R' ? var3 + 2 : var3 + 1;
   }

   private int handleS(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (contains(var1, var3 - 1, 3, "ISL", "YSL")) {
         return var3 + 1;
      } else if (var3 == 0 && contains(var1, var3, 5, "SUGAR")) {
         var2.append('X', 'S');
         return var3 + 1;
      } else if (contains(var1, var3, 2, "SH")) {
         if (contains(var1, var3 + 1, 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
            var2.append('S');
         } else {
            var2.append('X');
         }

         return var3 + 2;
      } else if (!contains(var1, var3, 3, "SIO", "SIA") && !contains(var1, var3, 4, "SIAN")) {
         if ((var3 != 0 || !contains(var1, var3 + 1, 1, "M", "N", "L", "W")) && !contains(var1, var3 + 1, 1, "Z")) {
            if (contains(var1, var3, 2, "SC")) {
               return this.handleSC(var1, var2, var3);
            } else {
               if (var3 == var1.length() - 1 && contains(var1, var3 - 2, 2, "AI", "OI")) {
                  var2.appendAlternate('S');
               } else {
                  var2.append('S');
               }

               if (contains(var1, var3 + 1, 1, "S", "Z")) {
                  var3 += 2;
               } else {
                  ++var3;
               }

               return var3;
            }
         } else {
            var2.append('S', 'X');
            if (contains(var1, var3 + 1, 1, "Z")) {
               var3 += 2;
            } else {
               ++var3;
            }

            return var3;
         }
      } else {
         if (var4) {
            var2.append('S');
         } else {
            var2.append('S', 'X');
         }

         return var3 + 3;
      }
   }

   private int handleSC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.charAt(var1, var3 + 2) == 'H') {
         if (contains(var1, var3 + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
            if (contains(var1, var3 + 3, 2, "ER", "EN")) {
               var2.append("X", "SK");
            } else {
               var2.append("SK");
            }
         } else if (var3 == 0 && !this.isVowel(this.charAt(var1, 3)) && this.charAt(var1, 3) != 'W') {
            var2.append('X', 'S');
         } else {
            var2.append('X');
         }
      } else if (contains(var1, var3 + 2, 1, "I", "E", "Y")) {
         var2.append('S');
      } else {
         var2.append("SK");
      }

      return var3 + 3;
   }

   private int handleT(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (contains(var1, var3, 4, "TION")) {
         var2.append('X');
         return var3 + 3;
      } else if (contains(var1, var3, 3, "TIA", "TCH")) {
         var2.append('X');
         return var3 + 3;
      } else if (!contains(var1, var3, 2, "TH") && !contains(var1, var3, 3, "TTH")) {
         var2.append('T');
         if (contains(var1, var3 + 1, 1, "T", "D")) {
            var3 += 2;
         } else {
            ++var3;
         }

         return var3;
      } else {
         if (!contains(var1, var3 + 2, 2, "OM", "AM") && !contains(var1, 0, 4, "VAN ", "VON ") && !contains(var1, 0, 3, "SCH")) {
            var2.append('0', 'T');
         } else {
            var2.append('T');
         }

         return var3 + 2;
      }
   }

   private int handleW(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (contains(var1, var3, 2, "WR")) {
         var2.append('R');
         return var3 + 2;
      } else if (var3 == 0 && (this.isVowel(this.charAt(var1, var3 + 1)) || contains(var1, var3, 2, "WH"))) {
         if (this.isVowel(this.charAt(var1, var3 + 1))) {
            var2.append('A', 'F');
         } else {
            var2.append('A');
         }

         return var3 + 1;
      } else if ((var3 != var1.length() - 1 || !this.isVowel(this.charAt(var1, var3 - 1))) && !contains(var1, var3 - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY") && !contains(var1, 0, 3, "SCH")) {
         if (contains(var1, var3, 4, "WICZ", "WITZ")) {
            var2.append("TS", "FX");
            return var3 + 4;
         } else {
            return var3 + 1;
         }
      } else {
         var2.appendAlternate('F');
         return var3 + 1;
      }
   }

   private int handleX(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 == 0) {
         var2.append('S');
         return var3 + 1;
      } else {
         if (var3 != var1.length() - 1 || !contains(var1, var3 - 3, 3, "IAU", "EAU") && !contains(var1, var3 - 2, 2, "AU", "OU")) {
            var2.append("KS");
         }

         if (contains(var1, var3 + 1, 1, "C", "X")) {
            var3 += 2;
         } else {
            ++var3;
         }

         return var3;
      }
   }

   private int handleZ(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         var2.append('J');
         return var3 + 2;
      } else {
         if (contains(var1, var3 + 1, 2, "ZO", "ZI", "ZA") || var4 && var3 > 0 && this.charAt(var1, var3 - 1) != 'T') {
            var2.append("S", "TS");
         } else {
            var2.append('S');
         }

         if (this.charAt(var1, var3 + 1) == 'Z') {
            var3 += 2;
         } else {
            ++var3;
         }

         return var3;
      }
   }

   private boolean isSilentStart(String var1) {
      String[] var4 = SILENT_START;
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var1.startsWith(var4[var2])) {
            return true;
         }
      }

      return false;
   }

   private boolean isSlavoGermanic(String var1) {
      return var1.indexOf(87) > -1 || var1.indexOf(75) > -1 || var1.indexOf("CZ") > -1 || var1.indexOf("WITZ") > -1;
   }

   private boolean isVowel(char var1) {
      return "AEIOUY".indexOf(var1) != -1;
   }

   protected char charAt(String var1, int var2) {
      return var2 >= 0 && var2 < var1.length() ? var1.charAt(var2) : '\u0000';
   }

   public String doubleMetaphone(String var1) {
      return this.doubleMetaphone(var1, false);
   }

   public String doubleMetaphone(String var1, boolean var2) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.doubleMetaphone((String)var1);
      } else {
         throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
      }
   }

   public String encode(String var1) {
      return this.doubleMetaphone(var1);
   }

   public int getMaxCodeLen() {
      return this.maxCodeLen;
   }

   public boolean isDoubleMetaphoneEqual(String var1, String var2) {
      return this.isDoubleMetaphoneEqual(var1, var2, false);
   }

   public boolean isDoubleMetaphoneEqual(String var1, String var2, boolean var3) {
      return StringUtils.equals(this.doubleMetaphone(var1, var3), this.doubleMetaphone(var2, var3));
   }

   public void setMaxCodeLen(int var1) {
      this.maxCodeLen = var1;
   }

   public class DoubleMetaphoneResult {
      private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
      private final int maxLength;
      private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());

      public DoubleMetaphoneResult(int var2) {
         this.maxLength = var2;
      }

      public void append(char var1) {
         this.appendPrimary(var1);
         this.appendAlternate(var1);
      }

      public void append(char var1, char var2) {
         this.appendPrimary(var1);
         this.appendAlternate(var2);
      }

      public void append(String var1) {
         this.appendPrimary(var1);
         this.appendAlternate(var1);
      }

      public void append(String var1, String var2) {
         this.appendPrimary(var1);
         this.appendAlternate(var2);
      }

      public void appendAlternate(char var1) {
         if (this.alternate.length() < this.maxLength) {
            this.alternate.append(var1);
         }

      }

      public void appendAlternate(String var1) {
         int var2 = this.maxLength - this.alternate.length();
         if (var1.length() <= var2) {
            this.alternate.append(var1);
         } else {
            this.alternate.append(var1.substring(0, var2));
         }
      }

      public void appendPrimary(char var1) {
         if (this.primary.length() < this.maxLength) {
            this.primary.append(var1);
         }

      }

      public void appendPrimary(String var1) {
         int var2 = this.maxLength - this.primary.length();
         if (var1.length() <= var2) {
            this.primary.append(var1);
         } else {
            this.primary.append(var1.substring(0, var2));
         }
      }

      public String getAlternate() {
         return this.alternate.toString();
      }

      public String getPrimary() {
         return this.primary.toString();
      }

      public boolean isComplete() {
         return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
      }
   }
}
