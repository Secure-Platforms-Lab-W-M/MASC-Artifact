package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class StrSubstitutor {
   public static final char DEFAULT_ESCAPE = '$';
   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
   private boolean enableSubstitutionInVariables;
   private char escapeChar;
   private StrMatcher prefixMatcher;
   private boolean preserveEscapes;
   private StrMatcher suffixMatcher;
   private StrMatcher valueDelimiterMatcher;
   private StrLookup variableResolver;

   public StrSubstitutor() {
      this((StrLookup)null, (StrMatcher)DEFAULT_PREFIX, (StrMatcher)DEFAULT_SUFFIX, '$');
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
      StrMatcher var22 = this.getVariablePrefixMatcher();
      StrMatcher var19 = this.getVariableSuffixMatcher();
      char var12 = this.getEscapeChar();
      StrMatcher var23 = this.getValueDelimiterMatcher();
      boolean var15 = this.isEnableSubstitutionInVariables();
      int var5;
      if (var4 == null) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      char[] var16 = var1.buffer;
      int var6 = var2 + var3;
      int var8 = var2;
      int var9 = 0;
      byte var7 = 0;
      char[] var24 = var16;
      Object var25 = var4;

      while(true) {
         while(var8 < var6) {
            int var13 = var22.isMatch(var24, var8, var2, var6);
            if (var13 == 0) {
               var5 = var8 + 1;
            } else if (var8 > var2 && var24[var8 - 1] == var12) {
               if (this.preserveEscapes) {
                  ++var8;
                  continue;
               }

               var1.deleteCharAt(var8 - 1);
               var24 = var1.buffer;
               --var9;
               --var6;
               var7 = 1;
               var5 = var8;
            } else {
               var5 = var8 + var13;
               int var10 = 0;

               label91:
               while(true) {
                  while(true) {
                     if (var5 >= var6) {
                        break label91;
                     }

                     int var14;
                     if (var15) {
                        var14 = var22.isMatch(var24, var5, var2, var6);
                        if (var14 != 0) {
                           ++var10;
                           var5 += var14;
                           continue;
                        }
                     }

                     var14 = var19.isMatch(var24, var5, var2, var6);
                     if (var14 == 0) {
                        ++var5;
                     } else {
                        if (var10 == 0) {
                           String var27 = new String(var24, var8 + var13, var5 - var8 - var13);
                           if (var15) {
                              StrBuilder var28 = new StrBuilder(var27);
                              this.substitute(var28, 0, var28.length());
                              var27 = var28.toString();
                           }

                           String var18;
                           String var30;
                           label76: {
                              var13 = var5 + var14;
                              var18 = null;
                              if (var23 != null) {
                                 char[] var20 = var27.toCharArray();
                                 var10 = 0;
                                 var5 = var6;

                                 while(true) {
                                    var6 = var5;
                                    if (var10 >= var20.length) {
                                       break;
                                    }

                                    if (!var15 && var22.isMatch(var20, var10, var10, var20.length) != 0) {
                                       var6 = var5;
                                       break;
                                    }

                                    var6 = var23.isMatch(var20, var10);
                                    if (var6 != 0) {
                                       var30 = var27.substring(0, var10);
                                       var18 = var27.substring(var10 + var6);
                                       break label76;
                                    }

                                    ++var10;
                                 }
                              }

                              var5 = var6;
                              var30 = var27;
                           }

                           Object var29 = var25;
                           if (var25 == null) {
                              var29 = new ArrayList();
                              ((List)var29).add(new String(var24, var2, var3));
                           }

                           this.checkCyclicSubstitution(var30, (List)var29);
                           ((List)var29).add(var30);
                           var30 = this.resolveVariable(var30, var1, var8, var13);
                           String var26 = var30;
                           if (var30 == null) {
                              var26 = var18;
                           }

                           if (var26 != null) {
                              var6 = var26.length();
                              var1.replace(var8, var13, var26);
                              var7 = 1;
                              var10 = this.substitute(var1, var8, var6, (List)var29) + var6 - (var13 - var8);
                              var8 = var13 + var10;
                              var24 = var1.buffer;
                              var6 = var5 + var10;
                              var9 += var10;
                              var5 = var8;
                              var8 = var9;
                           } else {
                              var8 = var9;
                              var6 = var5;
                              var5 = var13;
                           }

                           ((List)var29).remove(((List)var29).size() - 1);
                           var25 = var29;
                           var9 = var8;
                           break label91;
                        }

                        --var10;
                        var5 += var14;
                     }
                  }
               }
            }

            var8 = var5;
         }

         if (var5 != 0) {
            return var7;
         }

         return var9;
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
      if (StringUtils.isEmpty(var1)) {
         this.setValueDelimiterMatcher((StrMatcher)null);
         return this;
      } else {
         return this.setValueDelimiterMatcher(StrMatcher.stringMatcher(var1));
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
      if (var1 != null) {
         return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(var1));
      } else {
         throw new IllegalArgumentException("Variable prefix must not be null!");
      }
   }

   public StrSubstitutor setVariablePrefixMatcher(StrMatcher var1) {
      if (var1 != null) {
         this.prefixMatcher = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Variable prefix matcher must not be null!");
      }
   }

   public void setVariableResolver(StrLookup var1) {
      this.variableResolver = var1;
   }

   public StrSubstitutor setVariableSuffix(char var1) {
      return this.setVariableSuffixMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setVariableSuffix(String var1) {
      if (var1 != null) {
         return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(var1));
      } else {
         throw new IllegalArgumentException("Variable suffix must not be null!");
      }
   }

   public StrSubstitutor setVariableSuffixMatcher(StrMatcher var1) {
      if (var1 != null) {
         this.suffixMatcher = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Variable suffix matcher must not be null!");
      }
   }

   protected boolean substitute(StrBuilder var1, int var2, int var3) {
      return this.substitute(var1, var2, var3, (List)null) > 0;
   }
}
