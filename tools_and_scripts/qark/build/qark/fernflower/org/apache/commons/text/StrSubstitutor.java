package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.Validate;

@Deprecated
public class StrSubstitutor {
   public static final char DEFAULT_ESCAPE = '$';
   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
   private boolean disableSubstitutionInValues;
   private boolean enableSubstitutionInVariables;
   private char escapeChar;
   private StrMatcher prefixMatcher;
   private boolean preserveEscapes;
   private StrMatcher suffixMatcher;
   private StrMatcher valueDelimiterMatcher;
   private StrLookup variableResolver;

   public StrSubstitutor() {
      this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1) {
      this(StrLookup.mapLookup(var1), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3) {
      this(StrLookup.mapLookup(var1), var2, var3, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3, char var4) {
      this(StrLookup.mapLookup(var1), var2, var3, var4);
   }

   public StrSubstitutor(Map var1, String var2, String var3, char var4, String var5) {
      this(StrLookup.mapLookup(var1), var2, var3, var4, var5);
   }

   public StrSubstitutor(StrLookup var1) {
      this(var1, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(StrLookup var1, String var2, String var3, char var4) {
      this.preserveEscapes = false;
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
   }

   public StrSubstitutor(StrLookup var1, String var2, String var3, char var4, String var5) {
      this.preserveEscapes = false;
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiter(var5);
   }

   public StrSubstitutor(StrLookup var1, StrMatcher var2, StrMatcher var3, char var4) {
      this(var1, var2, var3, var4, DEFAULT_VALUE_DELIMITER);
   }

   public StrSubstitutor(StrLookup var1, StrMatcher var2, StrMatcher var3, char var4, StrMatcher var5) {
      this.preserveEscapes = false;
      this.setVariableResolver(var1);
      this.setVariablePrefixMatcher(var2);
      this.setVariableSuffixMatcher(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(var5);
   }

   private void checkCyclicSubstitution(String var1, List var2) {
      if (var2.contains(var1)) {
         StrBuilder var3 = new StrBuilder(256);
         var3.append("Infinite loop in property interpolation of ");
         var3.append((String)var2.remove(0));
         var3.append(": ");
         var3.appendWithSeparators((Iterable)var2, "->");
         throw new IllegalStateException(var3.toString());
      }
   }

   public static String replace(Object var0, Map var1) {
      return (new StrSubstitutor(var1)).replace(var0);
   }

   public static String replace(Object var0, Map var1, String var2, String var3) {
      return (new StrSubstitutor(var1, var2, var3)).replace(var0);
   }

   public static String replace(Object var0, Properties var1) {
      if (var1 == null) {
         return var0.toString();
      } else {
         HashMap var2 = new HashMap();
         Enumeration var3 = var1.propertyNames();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            var2.put(var4, var1.getProperty(var4));
         }

         return replace(var0, (Map)var2);
      }
   }

   public static String replaceSystemProperties(Object var0) {
      return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(var0);
   }

   private int substitute(StrBuilder var1, int var2, int var3, List var4) {
      StrMatcher var20 = this.getVariablePrefixMatcher();
      StrMatcher var21 = this.getVariableSuffixMatcher();
      char var12 = this.getEscapeChar();
      StrMatcher var24 = this.getValueDelimiterMatcher();
      boolean var15 = this.isEnableSubstitutionInVariables();
      boolean var16 = this.isDisableSubstitutionInValues();
      boolean var7;
      if (var4 == null) {
         var7 = true;
      } else {
         var7 = false;
      }

      char[] var18 = var1.buffer;
      int var9 = var2 + var3;
      int var10 = 0;
      int var8 = var2;
      byte var5 = 0;
      Object var17 = var4;
      char[] var25 = var18;

      while(true) {
         while(var8 < var9) {
            int var13 = var20.isMatch(var25, var8, var2, var9);
            int var6;
            if (var13 == 0) {
               var6 = var8 + 1;
            } else if (var8 > var2 && var25[var8 - 1] == var12) {
               if (this.preserveEscapes) {
                  ++var8;
                  continue;
               }

               var1.deleteCharAt(var8 - 1);
               var25 = var1.buffer;
               --var10;
               var5 = 1;
               --var9;
               var6 = var8;
            } else {
               var6 = var8 + var13;
               int var11 = 0;

               label97:
               while(true) {
                  while(true) {
                     if (var6 >= var9) {
                        break label97;
                     }

                     int var14;
                     if (var15 && var20.isMatch(var25, var6, var2, var9) != 0) {
                        var14 = var20.isMatch(var25, var6, var2, var9);
                        ++var11;
                        var6 += var14;
                     } else {
                        var14 = var21.isMatch(var25, var6, var2, var9);
                        if (var14 == 0) {
                           ++var6;
                        } else {
                           if (var11 == 0) {
                              String var30 = new String(var25, var8 + var13, var6 - var8 - var13);
                              if (var15) {
                                 StrBuilder var31 = new StrBuilder(var30);
                                 this.substitute(var31, 0, var31.length());
                                 var30 = var31.toString();
                              }

                              String var19;
                              String var33;
                              label82: {
                                 var11 = var6 + var14;
                                 var19 = null;
                                 if (var24 != null) {
                                    char[] var22 = var30.toCharArray();
                                    var13 = 0;

                                    for(var6 = var11; var13 < var22.length && (var15 || var20.isMatch(var22, var13, var13, var22.length) == 0); ++var13) {
                                       if (var24.isMatch(var22, var13) != 0) {
                                          var14 = var24.isMatch(var22, var13);
                                          var33 = var30.substring(0, var13);
                                          var19 = var30.substring(var13 + var14);
                                          break label82;
                                       }
                                    }
                                 } else {
                                    var6 = var11;
                                 }

                                 var33 = var30;
                              }

                              Object var32 = var17;
                              if (var17 == null) {
                                 var32 = new ArrayList();
                                 ((List)var32).add(new String(var25, var2, var3));
                              }

                              this.checkCyclicSubstitution(var33, (List)var32);
                              ((List)var32).add(var33);
                              var33 = this.resolveVariable(var33, var1, var8, var11);
                              String var29 = var33;
                              if (var33 == null) {
                                 var29 = var19;
                              }

                              int var26;
                              byte var27;
                              if (var29 != null) {
                                 var14 = var29.length();
                                 var1.replace(var8, var11, var29);
                                 byte var28 = 1;
                                 var26 = 0;
                                 if (!var16) {
                                    var26 = this.substitute(var1, var8, var14, (List)var32);
                                 }

                                 var8 = var26 + var14 - (var11 - var8);
                                 var26 = var10 + var8;
                                 var25 = var1.buffer;
                                 var6 += var8;
                                 var9 += var8;
                                 var27 = var28;
                              } else {
                                 var27 = var5;
                                 var26 = var10;
                              }

                              ((List)var32).remove(((List)var32).size() - 1);
                              var10 = var26;
                              var5 = var27;
                              var17 = var32;
                              break label97;
                           }

                           --var11;
                           var6 += var14;
                        }
                     }
                  }
               }
            }

            var8 = var6;
         }

         if (var7) {
            return var5;
         }

         return var10;
      }
   }

   public char getEscapeChar() {
      return this.escapeChar;
   }

   public StrMatcher getValueDelimiterMatcher() {
      return this.valueDelimiterMatcher;
   }

   public StrMatcher getVariablePrefixMatcher() {
      return this.prefixMatcher;
   }

   public StrLookup getVariableResolver() {
      return this.variableResolver;
   }

   public StrMatcher getVariableSuffixMatcher() {
      return this.suffixMatcher;
   }

   public boolean isDisableSubstitutionInValues() {
      return this.disableSubstitutionInValues;
   }

   public boolean isEnableSubstitutionInVariables() {
      return this.enableSubstitutionInVariables;
   }

   public boolean isPreserveEscapes() {
      return this.preserveEscapes;
   }

   public String replace(CharSequence var1) {
      return var1 == null ? null : this.replace((CharSequence)var1, 0, var1.length());
   }

   public String replace(CharSequence var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder()).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(String var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = new StrBuilder(var1);
         return !this.substitute(var2, 0, var1.length()) ? var1 : var2.toString();
      }
   }

   public String replace(String var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         return !this.substitute(var4, 0, var3) ? var1.substring(var2, var2 + var3) : var4.toString();
      }
   }

   public String replace(StringBuffer var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder(var1.length())).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(StrBuilder var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = (new StrBuilder(var1.length())).append(var1);
         this.substitute(var1, 0, var1.length());
         return var1.toString();
      }
   }

   public String replace(StrBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         var1 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var1, 0, var3);
         return var1.toString();
      }
   }

   public String replace(char[] var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder(var1.length)).append(var1);
         this.substitute(var2, 0, var1.length);
         return var2.toString();
      }
   }

   public String replace(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public boolean replaceIn(StringBuffer var1) {
      return var1 == null ? false : this.replaceIn((StringBuffer)var1, 0, var1.length());
   }

   public boolean replaceIn(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         if (!this.substitute(var4, 0, var3)) {
            return false;
         } else {
            var1.replace(var2, var2 + var3, var4.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(StringBuilder var1) {
      return var1 == null ? false : this.replaceIn((StringBuilder)var1, 0, var1.length());
   }

   public boolean replaceIn(StringBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         if (!this.substitute(var4, 0, var3)) {
            return false;
         } else {
            var1.replace(var2, var2 + var3, var4.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(StrBuilder var1) {
      return var1 == null ? false : this.substitute(var1, 0, var1.length());
   }

   public boolean replaceIn(StrBuilder var1, int var2, int var3) {
      return var1 == null ? false : this.substitute(var1, var2, var3);
   }

   protected String resolveVariable(String var1, StrBuilder var2, int var3, int var4) {
      StrLookup var5 = this.getVariableResolver();
      return var5 == null ? null : var5.lookup(var1);
   }

   public void setDisableSubstitutionInValues(boolean var1) {
      this.disableSubstitutionInValues = var1;
   }

   public void setEnableSubstitutionInVariables(boolean var1) {
      this.enableSubstitutionInVariables = var1;
   }

   public void setEscapeChar(char var1) {
      this.escapeChar = var1;
   }

   public void setPreserveEscapes(boolean var1) {
      this.preserveEscapes = var1;
   }

   public StrSubstitutor setValueDelimiter(char var1) {
      return this.setValueDelimiterMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setValueDelimiter(String var1) {
      if (var1 != null && var1.length() != 0) {
         return this.setValueDelimiterMatcher(StrMatcher.stringMatcher(var1));
      } else {
         this.setValueDelimiterMatcher((StrMatcher)null);
         return this;
      }
   }

   public StrSubstitutor setValueDelimiterMatcher(StrMatcher var1) {
      this.valueDelimiterMatcher = var1;
      return this;
   }

   public StrSubstitutor setVariablePrefix(char var1) {
      return this.setVariablePrefixMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setVariablePrefix(String var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable prefix must not be null!");
      return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(var1));
   }

   public StrSubstitutor setVariablePrefixMatcher(StrMatcher var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable prefix matcher must not be null!");
      this.prefixMatcher = var1;
      return this;
   }

   public void setVariableResolver(StrLookup var1) {
      this.variableResolver = var1;
   }

   public StrSubstitutor setVariableSuffix(char var1) {
      return this.setVariableSuffixMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setVariableSuffix(String var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable suffix must not be null!");
      return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(var1));
   }

   public StrSubstitutor setVariableSuffixMatcher(StrMatcher var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable suffix matcher must not be null!");
      this.suffixMatcher = var1;
      return this;
   }

   protected boolean substitute(StrBuilder var1, int var2, int var3) {
      return this.substitute(var1, var2, var3, (List)null) > 0;
   }
}
