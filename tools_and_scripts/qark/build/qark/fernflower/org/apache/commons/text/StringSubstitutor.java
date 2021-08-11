package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class StringSubstitutor {
   public static final char DEFAULT_ESCAPE = '$';
   public static final StringMatcher DEFAULT_PREFIX;
   public static final StringMatcher DEFAULT_SUFFIX;
   public static final StringMatcher DEFAULT_VALUE_DELIMITER;
   public static final String DEFAULT_VAR_DEFAULT = ":-";
   public static final String DEFAULT_VAR_END = "}";
   public static final String DEFAULT_VAR_START = "${";
   private boolean disableSubstitutionInValues;
   private boolean enableSubstitutionInVariables;
   private boolean enableUndefinedVariableException;
   private char escapeChar;
   private StringMatcher prefixMatcher;
   private boolean preserveEscapes;
   private StringMatcher suffixMatcher;
   private StringMatcher valueDelimiterMatcher;
   private StringLookup variableResolver;

   static {
      DEFAULT_PREFIX = StringMatcherFactory.INSTANCE.stringMatcher("${");
      DEFAULT_SUFFIX = StringMatcherFactory.INSTANCE.stringMatcher("}");
      DEFAULT_VALUE_DELIMITER = StringMatcherFactory.INSTANCE.stringMatcher(":-");
   }

   public StringSubstitutor() {
      this((StringLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StringSubstitutor(Map var1) {
      this(StringLookupFactory.INSTANCE.mapStringLookup(var1), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StringSubstitutor(Map var1, String var2, String var3) {
      this(StringLookupFactory.INSTANCE.mapStringLookup(var1), var2, var3, '$');
   }

   public StringSubstitutor(Map var1, String var2, String var3, char var4) {
      this(StringLookupFactory.INSTANCE.mapStringLookup(var1), var2, var3, var4);
   }

   public StringSubstitutor(Map var1, String var2, String var3, char var4, String var5) {
      this(StringLookupFactory.INSTANCE.mapStringLookup(var1), var2, var3, var4, var5);
   }

   public StringSubstitutor(StringLookup var1) {
      this(var1, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StringSubstitutor(StringLookup var1, String var2, String var3, char var4) {
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
   }

   public StringSubstitutor(StringLookup var1, String var2, String var3, char var4, String var5) {
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiter(var5);
   }

   public StringSubstitutor(StringLookup var1, StringMatcher var2, StringMatcher var3, char var4) {
      this(var1, var2, var3, var4, DEFAULT_VALUE_DELIMITER);
   }

   public StringSubstitutor(StringLookup var1, StringMatcher var2, StringMatcher var3, char var4, StringMatcher var5) {
      this.setVariableResolver(var1);
      this.setVariablePrefixMatcher(var2);
      this.setVariableSuffixMatcher(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(var5);
   }

   private void checkCyclicSubstitution(String var1, List var2) {
      if (var2.contains(var1)) {
         TextStringBuilder var3 = new TextStringBuilder(256);
         var3.append("Infinite loop in property interpolation of ");
         var3.append((String)var2.remove(0));
         var3.append(": ");
         var3.appendWithSeparators((Iterable)var2, "->");
         throw new IllegalStateException(var3.toString());
      }
   }

   public static StringSubstitutor createInterpolator() {
      return new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup());
   }

   public static String replace(Object var0, Map var1) {
      return (new StringSubstitutor(var1)).replace(var0);
   }

   public static String replace(Object var0, Map var1, String var2, String var3) {
      return (new StringSubstitutor(var1, var2, var3)).replace(var0);
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
      return (new StringSubstitutor(StringLookupFactory.INSTANCE.systemPropertyStringLookup())).replace(var0);
   }

   private int substitute(TextStringBuilder var1, int var2, int var3, List var4) {
      StringMatcher var20 = this.getVariablePrefixMatcher();
      StringMatcher var22 = this.getVariableSuffixMatcher();
      char var12 = this.getEscapeChar();
      StringMatcher var26 = this.getValueDelimiterMatcher();
      boolean var16 = this.isEnableSubstitutionInVariables();
      boolean var17 = this.isDisableSubstitutionInValues();
      boolean var15 = this.isEnableUndefinedVariableException();
      boolean var7;
      if (var4 == null) {
         var7 = true;
      } else {
         var7 = false;
      }

      char[] var18 = var1.buffer;
      int var10 = var2 + var3;
      int var8 = 0;
      int var9 = var2;
      byte var5 = 0;
      Object var19 = var4;
      StringMatcher var27 = var20;

      while(true) {
         while(var9 < var10) {
            int var13 = var27.isMatch(var18, var9, var2, var10);
            int var6;
            char[] var28;
            StringMatcher var34;
            if (var13 == 0) {
               var6 = var9 + 1;
               var28 = var18;
               var34 = var27;
            } else if (var9 > var2 && var18[var9 - 1] == var12) {
               if (this.preserveEscapes) {
                  ++var9;
                  continue;
               }

               var1.deleteCharAt(var9 - 1);
               char[] var38 = var1.buffer;
               --var8;
               var5 = 1;
               --var10;
               var34 = var27;
               var28 = var38;
               var6 = var9;
            } else {
               var6 = var9 + var13;
               int var11 = 0;

               label102:
               while(true) {
                  while(var6 < var10) {
                     int var14;
                     if (var16 && var27.isMatch(var18, var6, var2, var10) != 0) {
                        var14 = var27.isMatch(var18, var6, var2, var10);
                        ++var11;
                        var6 += var14;
                     } else {
                        var14 = var22.isMatch(var18, var6, var2, var10);
                        if (var14 == 0) {
                           ++var6;
                        } else {
                           if (var11 == 0) {
                              String var36 = new String(var18, var9 + var13, var6 - var9 - var13);
                              if (var16) {
                                 TextStringBuilder var37 = new TextStringBuilder(var36);
                                 this.substitute(var37, 0, var37.length());
                                 var36 = var37.toString();
                              }

                              String var21;
                              String var39;
                              label87: {
                                 var11 = var6 + var14;
                                 var21 = null;
                                 if (var26 != null) {
                                    char[] var23 = var36.toCharArray();
                                    var13 = 0;

                                    for(var6 = var11; var13 < var23.length && (var16 || var27.isMatch(var23, var13, var13, var23.length) == 0); ++var13) {
                                       var14 = var23.length;
                                       if (var26.isMatch(var23, var13, 0, var14) != 0) {
                                          var14 = var26.isMatch(var23, var13, 0, var23.length);
                                          var39 = var36.substring(0, var13);
                                          var21 = var36.substring(var13 + var14);
                                          var20 = var27;
                                          break label87;
                                       }
                                    }
                                 } else {
                                    var6 = var11;
                                 }

                                 var39 = var36;
                                 var20 = var27;
                              }

                              Object var29 = var19;
                              if (var19 == null) {
                                 var29 = new ArrayList();
                                 ((List)var29).add(new String(var18, var2, var3));
                              }

                              this.checkCyclicSubstitution(var39, (List)var29);
                              ((List)var29).add(var39);
                              String var25 = this.resolveVariable(var39, var1, var9, var11);
                              String var35 = var25;
                              if (var25 == null) {
                                 var35 = var21;
                              }

                              int var30;
                              byte var31;
                              if (var35 != null) {
                                 var14 = var35.length();
                                 var1.replace(var9, var11, var35);
                                 byte var33 = 1;
                                 var30 = 0;
                                 if (!var17) {
                                    var30 = this.substitute(var1, var9, var14, (List)var29);
                                 }

                                 var9 = var30 + var14 - (var11 - var9);
                                 var30 = var6 + var9;
                                 var8 += var9;
                                 var18 = var1.buffer;
                                 var10 += var9;
                                 var31 = var33;
                              } else {
                                 if (var15) {
                                    throw new IllegalArgumentException(String.format("Cannot resolve variable '%s' (enableSubstitutionInVariables=%s).", var39, this.enableSubstitutionInVariables));
                                 }

                                 var30 = var6;
                                 var31 = var5;
                              }

                              ((List)var29).remove(((List)var29).size() - 1);
                              var19 = var29;
                              var28 = var18;
                              var5 = var31;
                              var6 = var30;
                              var34 = var20;
                              break label102;
                           }

                           --var11;
                           var6 += var14;
                        }
                     }
                  }

                  var34 = var27;
                  var28 = var18;
                  break;
               }
            }

            var18 = var28;
            var27 = var34;
            var9 = var6;
         }

         if (var7) {
            return var5;
         }

         return var8;
      }
   }

   public char getEscapeChar() {
      return this.escapeChar;
   }

   public StringLookup getStringLookup() {
      return this.variableResolver;
   }

   public StringMatcher getValueDelimiterMatcher() {
      return this.valueDelimiterMatcher;
   }

   public StringMatcher getVariablePrefixMatcher() {
      return this.prefixMatcher;
   }

   public StringMatcher getVariableSuffixMatcher() {
      return this.suffixMatcher;
   }

   public boolean isDisableSubstitutionInValues() {
      return this.disableSubstitutionInValues;
   }

   public boolean isEnableSubstitutionInVariables() {
      return this.enableSubstitutionInVariables;
   }

   public boolean isEnableUndefinedVariableException() {
      return this.enableUndefinedVariableException;
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
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1.toString(), var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var2 = (new TextStringBuilder()).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(String var1) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var2 = new TextStringBuilder(var1);
         return !this.substitute(var2, 0, var1.length()) ? var1 : var2.toString();
      }
   }

   public String replace(String var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1, var2, var3);
         return !this.substitute(var4, 0, var3) ? var1.substring(var2, var2 + var3) : var4.toString();
      }
   }

   public String replace(StringBuffer var1) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var2 = (new TextStringBuilder(var1.length())).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(TextStringBuilder var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = (new TextStringBuilder(var1.length())).append(var1);
         this.substitute(var1, 0, var1.length());
         return var1.toString();
      }
   }

   public String replace(TextStringBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         var1 = (new TextStringBuilder(var3)).append(var1, var2, var3);
         this.substitute(var1, 0, var3);
         return var1.toString();
      }
   }

   public String replace(char[] var1) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var2 = (new TextStringBuilder(var1.length)).append(var1);
         this.substitute(var2, 0, var1.length);
         return var2.toString();
      }
   }

   public String replace(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1, var2, var3);
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
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1, var2, var3);
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
         TextStringBuilder var4 = (new TextStringBuilder(var3)).append(var1, var2, var3);
         if (!this.substitute(var4, 0, var3)) {
            return false;
         } else {
            var1.replace(var2, var2 + var3, var4.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(TextStringBuilder var1) {
      return var1 == null ? false : this.substitute(var1, 0, var1.length());
   }

   public boolean replaceIn(TextStringBuilder var1, int var2, int var3) {
      return var1 == null ? false : this.substitute(var1, var2, var3);
   }

   protected String resolveVariable(String var1, TextStringBuilder var2, int var3, int var4) {
      StringLookup var5 = this.getStringLookup();
      return var5 == null ? null : var5.lookup(var1);
   }

   public StringSubstitutor setDisableSubstitutionInValues(boolean var1) {
      this.disableSubstitutionInValues = var1;
      return this;
   }

   public StringSubstitutor setEnableSubstitutionInVariables(boolean var1) {
      this.enableSubstitutionInVariables = var1;
      return this;
   }

   public StringSubstitutor setEnableUndefinedVariableException(boolean var1) {
      this.enableUndefinedVariableException = var1;
      return this;
   }

   public StringSubstitutor setEscapeChar(char var1) {
      this.escapeChar = var1;
      return this;
   }

   public StringSubstitutor setPreserveEscapes(boolean var1) {
      this.preserveEscapes = var1;
      return this;
   }

   public StringSubstitutor setValueDelimiter(char var1) {
      return this.setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringSubstitutor setValueDelimiter(String var1) {
      if (var1 != null && var1.length() != 0) {
         return this.setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(var1));
      } else {
         this.setValueDelimiterMatcher((StringMatcher)null);
         return this;
      }
   }

   public StringSubstitutor setValueDelimiterMatcher(StringMatcher var1) {
      this.valueDelimiterMatcher = var1;
      return this;
   }

   public StringSubstitutor setVariablePrefix(char var1) {
      return this.setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringSubstitutor setVariablePrefix(String var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable prefix must not be null!");
      return this.setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(var1));
   }

   public StringSubstitutor setVariablePrefixMatcher(StringMatcher var1) {
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

   public StringSubstitutor setVariableResolver(StringLookup var1) {
      this.variableResolver = var1;
      return this;
   }

   public StringSubstitutor setVariableSuffix(char var1) {
      return this.setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringSubstitutor setVariableSuffix(String var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Variable suffix must not be null!");
      return this.setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(var1));
   }

   public StringSubstitutor setVariableSuffixMatcher(StringMatcher var1) {
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

   protected boolean substitute(TextStringBuilder var1, int var2, int var3) {
      return this.substitute(var1, var2, var3, (List)null) > 0;
   }
}
