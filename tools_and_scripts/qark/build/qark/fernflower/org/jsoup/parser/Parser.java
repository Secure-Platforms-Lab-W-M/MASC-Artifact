package org.jsoup.parser;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Parser {
   private static final int DEFAULT_MAX_ERRORS = 0;
   private ParseErrorList errors;
   private int maxErrors = 0;
   private TreeBuilder treeBuilder;

   public Parser(TreeBuilder var1) {
      this.treeBuilder = var1;
   }

   public static Parser htmlParser() {
      return new Parser(new HtmlTreeBuilder());
   }

   public static Document parse(String var0, String var1) {
      return (new HtmlTreeBuilder()).parse(var0, var1, ParseErrorList.noTracking());
   }

   public static Document parseBodyFragment(String var0, String var1) {
      Document var4 = Document.createShell(var1);
      Element var5 = var4.body();
      List var6 = parseFragment(var0, var5, var1);
      Node[] var7 = (Node[])var6.toArray(new Node[var6.size()]);

      int var2;
      for(var2 = var7.length - 1; var2 > 0; --var2) {
         var7[var2].remove();
      }

      int var3 = var7.length;

      for(var2 = 0; var2 < var3; ++var2) {
         var5.appendChild(var7[var2]);
      }

      return var4;
   }

   public static Document parseBodyFragmentRelaxed(String var0, String var1) {
      return parse(var0, var1);
   }

   public static List parseFragment(String var0, Element var1, String var2) {
      return (new HtmlTreeBuilder()).parseFragment(var0, var1, var2, ParseErrorList.noTracking());
   }

   public static List parseXmlFragment(String var0, String var1) {
      return (new XmlTreeBuilder()).parseFragment(var0, var1, ParseErrorList.noTracking());
   }

   public static String unescapeEntities(String var0, boolean var1) {
      return (new Tokeniser(new CharacterReader(var0), ParseErrorList.noTracking())).unescapeEntities(var1);
   }

   public static Parser xmlParser() {
      return new Parser(new XmlTreeBuilder());
   }

   public List getErrors() {
      return this.errors;
   }

   public TreeBuilder getTreeBuilder() {
      return this.treeBuilder;
   }

   public boolean isTrackErrors() {
      return this.maxErrors > 0;
   }

   public Document parseInput(String var1, String var2) {
      ParseErrorList var3;
      if (this.isTrackErrors()) {
         var3 = ParseErrorList.tracking(this.maxErrors);
      } else {
         var3 = ParseErrorList.noTracking();
      }

      this.errors = var3;
      return this.treeBuilder.parse(var1, var2, this.errors);
   }

   public Parser setTrackErrors(int var1) {
      this.maxErrors = var1;
      return this;
   }

   public Parser setTreeBuilder(TreeBuilder var1) {
      this.treeBuilder = var1;
      return this;
   }
}
