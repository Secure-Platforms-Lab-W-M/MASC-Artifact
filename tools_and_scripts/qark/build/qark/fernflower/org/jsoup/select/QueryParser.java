package org.jsoup.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;

class QueryParser {
   private static final String[] AttributeEvals = new String[]{"=", "!=", "^=", "$=", "*=", "~="};
   private static final Pattern NTH_AB = Pattern.compile("((\\+|-)?(\\d+)?)n(\\s*(\\+|-)?\\s*\\d+)?", 2);
   private static final Pattern NTH_B = Pattern.compile("(\\+|-)?(\\d+)");
   private static final String[] combinators = new String[]{",", ">", "+", "~", " "};
   private List evals = new ArrayList();
   private String query;
   // $FF: renamed from: tq org.jsoup.parser.TokenQueue
   private TokenQueue field_21;

   private QueryParser(String var1) {
      this.query = var1;
      this.field_21 = new TokenQueue(var1);
   }

   private void allElements() {
      this.evals.add(new Evaluator.AllElements());
   }

   private void byAttribute() {
      TokenQueue var1 = new TokenQueue(this.field_21.chompBalanced('[', ']'));
      String var2 = var1.consumeToAny(AttributeEvals);
      Validate.notEmpty(var2);
      var1.consumeWhitespace();
      if (var1.isEmpty()) {
         if (var2.startsWith("^")) {
            this.evals.add(new Evaluator.AttributeStarting(var2.substring(1)));
         } else {
            this.evals.add(new Evaluator.Attribute(var2));
         }
      } else if (var1.matchChomp("=")) {
         this.evals.add(new Evaluator.AttributeWithValue(var2, var1.remainder()));
      } else if (var1.matchChomp("!=")) {
         this.evals.add(new Evaluator.AttributeWithValueNot(var2, var1.remainder()));
      } else if (var1.matchChomp("^=")) {
         this.evals.add(new Evaluator.AttributeWithValueStarting(var2, var1.remainder()));
      } else if (var1.matchChomp("$=")) {
         this.evals.add(new Evaluator.AttributeWithValueEnding(var2, var1.remainder()));
      } else if (var1.matchChomp("*=")) {
         this.evals.add(new Evaluator.AttributeWithValueContaining(var2, var1.remainder()));
      } else if (var1.matchChomp("~=")) {
         this.evals.add(new Evaluator.AttributeWithValueMatching(var2, Pattern.compile(var1.remainder())));
      } else {
         throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", new Object[]{this.query, var1.remainder()});
      }
   }

   private void byClass() {
      String var1 = this.field_21.consumeCssIdentifier();
      Validate.notEmpty(var1);
      this.evals.add(new Evaluator.Class(var1.trim().toLowerCase()));
   }

   private void byId() {
      String var1 = this.field_21.consumeCssIdentifier();
      Validate.notEmpty(var1);
      this.evals.add(new Evaluator.class_18(var1));
   }

   private void byTag() {
      String var2 = this.field_21.consumeElementSelector();
      Validate.notEmpty(var2);
      String var1 = var2;
      if (var2.contains("|")) {
         var1 = var2.replace("|", ":");
      }

      this.evals.add(new Evaluator.Tag(var1.trim().toLowerCase()));
   }

   private void combinator(char var1) {
      this.field_21.consumeWhitespace();
      Evaluator var8 = parse(this.consumeSubQuery());
      boolean var3 = false;
      boolean var2;
      Object var4;
      Object var5;
      if (this.evals.size() == 1) {
         Evaluator var7 = (Evaluator)this.evals.get(0);
         var4 = var7;
         var2 = var3;
         var5 = var7;
         if (var7 instanceof CombiningEvaluator.class_17) {
            var4 = var7;
            var2 = var3;
            var5 = var7;
            if (var1 != ',') {
               var4 = ((CombiningEvaluator.class_17)var7).rightMostEvaluator();
               var2 = true;
               var5 = var7;
            }
         }
      } else {
         var4 = new CombiningEvaluator.And(this.evals);
         var5 = var4;
         var2 = var3;
      }

      this.evals.clear();
      if (var1 == '>') {
         var4 = new CombiningEvaluator.And(new Evaluator[]{var8, new StructuralEvaluator.ImmediateParent((Evaluator)var4)});
      } else if (var1 == ' ') {
         var4 = new CombiningEvaluator.And(new Evaluator[]{var8, new StructuralEvaluator.Parent((Evaluator)var4)});
      } else if (var1 == '+') {
         var4 = new CombiningEvaluator.And(new Evaluator[]{var8, new StructuralEvaluator.ImmediatePreviousSibling((Evaluator)var4)});
      } else if (var1 == '~') {
         var4 = new CombiningEvaluator.And(new Evaluator[]{var8, new StructuralEvaluator.PreviousSibling((Evaluator)var4)});
      } else {
         if (var1 != ',') {
            throw new Selector.SelectorParseException("Unknown combinator: " + var1, new Object[0]);
         }

         if (var4 instanceof CombiningEvaluator.class_17) {
            var4 = (CombiningEvaluator.class_17)var4;
            ((CombiningEvaluator.class_17)var4).add(var8);
         } else {
            CombiningEvaluator.class_17 var6 = new CombiningEvaluator.class_17();
            var6.add((Evaluator)var4);
            var6.add(var8);
            var4 = var6;
         }
      }

      if (var2) {
         ((CombiningEvaluator.class_17)var5).replaceRightMostEvaluator((Evaluator)var4);
      } else {
         var5 = var4;
      }

      this.evals.add(var5);
   }

   private int consumeIndex() {
      String var1 = this.field_21.chompTo(")").trim();
      Validate.isTrue(StringUtil.isNumeric(var1), "Index must be numeric");
      return Integer.parseInt(var1);
   }

   private String consumeSubQuery() {
      StringBuilder var1 = new StringBuilder();

      while(!this.field_21.isEmpty()) {
         if (this.field_21.matches("(")) {
            var1.append("(").append(this.field_21.chompBalanced('(', ')')).append(")");
         } else if (this.field_21.matches("[")) {
            var1.append("[").append(this.field_21.chompBalanced('[', ']')).append("]");
         } else {
            if (this.field_21.matchesAny(combinators)) {
               break;
            }

            var1.append(this.field_21.consume());
         }
      }

      return var1.toString();
   }

   private void contains(boolean var1) {
      TokenQueue var3 = this.field_21;
      String var2;
      if (var1) {
         var2 = ":containsOwn";
      } else {
         var2 = ":contains";
      }

      var3.consume(var2);
      var2 = TokenQueue.unescape(this.field_21.chompBalanced('(', ')'));
      Validate.notEmpty(var2, ":contains(text) query must not be empty");
      if (var1) {
         this.evals.add(new Evaluator.ContainsOwnText(var2));
      } else {
         this.evals.add(new Evaluator.ContainsText(var2));
      }
   }

   private void cssNthChild(boolean var1, boolean var2) {
      int var4 = 0;
      int var3 = 1;
      String var5 = this.field_21.chompTo(")").trim().toLowerCase();
      Matcher var6 = NTH_AB.matcher(var5);
      Matcher var7 = NTH_B.matcher(var5);
      if ("odd".equals(var5)) {
         var3 = 2;
         var4 = 1;
      } else if ("even".equals(var5)) {
         var3 = 2;
         var4 = 0;
      } else if (var6.matches()) {
         if (var6.group(3) != null) {
            var3 = Integer.parseInt(var6.group(1).replaceFirst("^\\+", ""));
         }

         if (var6.group(4) != null) {
            var4 = Integer.parseInt(var6.group(4).replaceFirst("^\\+", ""));
         }
      } else {
         if (!var7.matches()) {
            throw new Selector.SelectorParseException("Could not parse nth-index '%s': unexpected format", new Object[]{var5});
         }

         var3 = 0;
         var4 = Integer.parseInt(var7.group().replaceFirst("^\\+", ""));
      }

      if (var2) {
         if (var1) {
            this.evals.add(new Evaluator.IsNthLastOfType(var3, var4));
         } else {
            this.evals.add(new Evaluator.IsNthOfType(var3, var4));
         }
      } else if (var1) {
         this.evals.add(new Evaluator.IsNthLastChild(var3, var4));
      } else {
         this.evals.add(new Evaluator.IsNthChild(var3, var4));
      }
   }

   private void findElements() {
      if (this.field_21.matchChomp("#")) {
         this.byId();
      } else if (this.field_21.matchChomp(".")) {
         this.byClass();
      } else if (this.field_21.matchesWord()) {
         this.byTag();
      } else if (this.field_21.matches("[")) {
         this.byAttribute();
      } else if (this.field_21.matchChomp("*")) {
         this.allElements();
      } else if (this.field_21.matchChomp(":lt(")) {
         this.indexLessThan();
      } else if (this.field_21.matchChomp(":gt(")) {
         this.indexGreaterThan();
      } else if (this.field_21.matchChomp(":eq(")) {
         this.indexEquals();
      } else if (this.field_21.matches(":has(")) {
         this.has();
      } else if (this.field_21.matches(":contains(")) {
         this.contains(false);
      } else if (this.field_21.matches(":containsOwn(")) {
         this.contains(true);
      } else if (this.field_21.matches(":matches(")) {
         this.matches(false);
      } else if (this.field_21.matches(":matchesOwn(")) {
         this.matches(true);
      } else if (this.field_21.matches(":not(")) {
         this.not();
      } else if (this.field_21.matchChomp(":nth-child(")) {
         this.cssNthChild(false, false);
      } else if (this.field_21.matchChomp(":nth-last-child(")) {
         this.cssNthChild(true, false);
      } else if (this.field_21.matchChomp(":nth-of-type(")) {
         this.cssNthChild(false, true);
      } else if (this.field_21.matchChomp(":nth-last-of-type(")) {
         this.cssNthChild(true, true);
      } else if (this.field_21.matchChomp(":first-child")) {
         this.evals.add(new Evaluator.IsFirstChild());
      } else if (this.field_21.matchChomp(":last-child")) {
         this.evals.add(new Evaluator.IsLastChild());
      } else if (this.field_21.matchChomp(":first-of-type")) {
         this.evals.add(new Evaluator.IsFirstOfType());
      } else if (this.field_21.matchChomp(":last-of-type")) {
         this.evals.add(new Evaluator.IsLastOfType());
      } else if (this.field_21.matchChomp(":only-child")) {
         this.evals.add(new Evaluator.IsOnlyChild());
      } else if (this.field_21.matchChomp(":only-of-type")) {
         this.evals.add(new Evaluator.IsOnlyOfType());
      } else if (this.field_21.matchChomp(":empty")) {
         this.evals.add(new Evaluator.IsEmpty());
      } else if (this.field_21.matchChomp(":root")) {
         this.evals.add(new Evaluator.IsRoot());
      } else {
         throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", new Object[]{this.query, this.field_21.remainder()});
      }
   }

   private void has() {
      this.field_21.consume(":has");
      String var1 = this.field_21.chompBalanced('(', ')');
      Validate.notEmpty(var1, ":has(el) subselect must not be empty");
      this.evals.add(new StructuralEvaluator.Has(parse(var1)));
   }

   private void indexEquals() {
      this.evals.add(new Evaluator.IndexEquals(this.consumeIndex()));
   }

   private void indexGreaterThan() {
      this.evals.add(new Evaluator.IndexGreaterThan(this.consumeIndex()));
   }

   private void indexLessThan() {
      this.evals.add(new Evaluator.IndexLessThan(this.consumeIndex()));
   }

   private void matches(boolean var1) {
      TokenQueue var3 = this.field_21;
      String var2;
      if (var1) {
         var2 = ":matchesOwn";
      } else {
         var2 = ":matches";
      }

      var3.consume(var2);
      var2 = this.field_21.chompBalanced('(', ')');
      Validate.notEmpty(var2, ":matches(regex) query must not be empty");
      if (var1) {
         this.evals.add(new Evaluator.MatchesOwn(Pattern.compile(var2)));
      } else {
         this.evals.add(new Evaluator.Matches(Pattern.compile(var2)));
      }
   }

   private void not() {
      this.field_21.consume(":not");
      String var1 = this.field_21.chompBalanced('(', ')');
      Validate.notEmpty(var1, ":not(selector) subselect must not be empty");
      this.evals.add(new StructuralEvaluator.Not(parse(var1)));
   }

   public static Evaluator parse(String var0) {
      return (new QueryParser(var0)).parse();
   }

   Evaluator parse() {
      this.field_21.consumeWhitespace();
      if (this.field_21.matchesAny(combinators)) {
         this.evals.add(new StructuralEvaluator.Root());
         this.combinator(this.field_21.consume());
      } else {
         this.findElements();
      }

      while(!this.field_21.isEmpty()) {
         boolean var1 = this.field_21.consumeWhitespace();
         if (this.field_21.matchesAny(combinators)) {
            this.combinator(this.field_21.consume());
         } else if (var1) {
            this.combinator(' ');
         } else {
            this.findElements();
         }
      }

      if (this.evals.size() == 1) {
         return (Evaluator)this.evals.get(0);
      } else {
         return new CombiningEvaluator.And(this.evals);
      }
   }
}
