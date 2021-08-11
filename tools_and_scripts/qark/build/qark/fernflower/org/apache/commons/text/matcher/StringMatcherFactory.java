package org.apache.commons.text.matcher;

public final class StringMatcherFactory {
   private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');
   private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('"');
   public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();
   private static final AbstractStringMatcher.NoMatcher NONE_MATCHER = new AbstractStringMatcher.NoMatcher();
   private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher("'\"".toCharArray());
   private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('\'');
   private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');
   private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(" \t\n\r\f".toCharArray());
   private static final AbstractStringMatcher.CharMatcher TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');
   private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();

   private StringMatcherFactory() {
   }

   public StringMatcher charMatcher(char var1) {
      return new AbstractStringMatcher.CharMatcher(var1);
   }

   public StringMatcher charSetMatcher(String var1) {
      if (var1 != null && var1.length() != 0) {
         return (StringMatcher)(var1.length() == 1 ? new AbstractStringMatcher.CharMatcher(var1.charAt(0)) : new AbstractStringMatcher.CharSetMatcher(var1.toCharArray()));
      } else {
         return NONE_MATCHER;
      }
   }

   public StringMatcher charSetMatcher(char... var1) {
      if (var1 != null && var1.length != 0) {
         return (StringMatcher)(var1.length == 1 ? new AbstractStringMatcher.CharMatcher(var1[0]) : new AbstractStringMatcher.CharSetMatcher(var1));
      } else {
         return NONE_MATCHER;
      }
   }

   public StringMatcher commaMatcher() {
      return COMMA_MATCHER;
   }

   public StringMatcher doubleQuoteMatcher() {
      return DOUBLE_QUOTE_MATCHER;
   }

   public StringMatcher noneMatcher() {
      return NONE_MATCHER;
   }

   public StringMatcher quoteMatcher() {
      return QUOTE_MATCHER;
   }

   public StringMatcher singleQuoteMatcher() {
      return SINGLE_QUOTE_MATCHER;
   }

   public StringMatcher spaceMatcher() {
      return SPACE_MATCHER;
   }

   public StringMatcher splitMatcher() {
      return SPLIT_MATCHER;
   }

   public StringMatcher stringMatcher(String var1) {
      return (StringMatcher)(var1 != null && var1.length() != 0 ? new AbstractStringMatcher.StringMatcher(var1) : NONE_MATCHER);
   }

   public StringMatcher tabMatcher() {
      return TAB_MATCHER;
   }

   public StringMatcher trimMatcher() {
      return TRIM_MATCHER;
   }
}
