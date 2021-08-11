package org.jsoup.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.SerializationException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public abstract class Node implements Cloneable {
   private static final List EMPTY_NODES = Collections.emptyList();
   Attributes attributes;
   String baseUri;
   List childNodes;
   Node parentNode;
   int siblingIndex;

   protected Node() {
      this.childNodes = EMPTY_NODES;
      this.attributes = null;
   }

   protected Node(String var1) {
      this(var1, new Attributes());
   }

   protected Node(String var1, Attributes var2) {
      Validate.notNull(var1);
      Validate.notNull(var2);
      this.childNodes = EMPTY_NODES;
      this.baseUri = var1.trim();
      this.attributes = var2;
   }

   private void addSiblingHtml(int var1, String var2) {
      Validate.notNull(var2);
      Validate.notNull(this.parentNode);
      Element var3;
      if (this.parent() instanceof Element) {
         var3 = (Element)this.parent();
      } else {
         var3 = null;
      }

      List var4 = Parser.parseFragment(var2, var3, this.baseUri());
      this.parentNode.addChildren(var1, (Node[])var4.toArray(new Node[var4.size()]));
   }

   private Element getDeepChild(Element var1) {
      Elements var2 = var1.children();
      if (var2.size() > 0) {
         var1 = this.getDeepChild((Element)var2.get(0));
      }

      return var1;
   }

   private void reindexChildren(int var1) {
      while(var1 < this.childNodes.size()) {
         ((Node)this.childNodes.get(var1)).setSiblingIndex(var1);
         ++var1;
      }

   }

   public String absUrl(String var1) {
      Validate.notEmpty(var1);
      return !this.hasAttr(var1) ? "" : StringUtil.resolve(this.baseUri, this.attr(var1));
   }

   protected void addChildren(int var1, Node... var2) {
      Validate.noNullElements(var2);
      this.ensureChildNodes();

      for(int var3 = var2.length - 1; var3 >= 0; --var3) {
         Node var4 = var2[var3];
         this.reparentChild(var4);
         this.childNodes.add(var1, var4);
         this.reindexChildren(var1);
      }

   }

   protected void addChildren(Node... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Node var4 = var1[var2];
         this.reparentChild(var4);
         this.ensureChildNodes();
         this.childNodes.add(var4);
         var4.setSiblingIndex(this.childNodes.size() - 1);
      }

   }

   public Node after(String var1) {
      this.addSiblingHtml(this.siblingIndex + 1, var1);
      return this;
   }

   public Node after(Node var1) {
      Validate.notNull(var1);
      Validate.notNull(this.parentNode);
      this.parentNode.addChildren(this.siblingIndex + 1, var1);
      return this;
   }

   public String attr(String var1) {
      Validate.notNull(var1);
      if (this.attributes.hasKey(var1)) {
         return this.attributes.get(var1);
      } else {
         return var1.toLowerCase().startsWith("abs:") ? this.absUrl(var1.substring("abs:".length())) : "";
      }
   }

   public Node attr(String var1, String var2) {
      this.attributes.put(var1, var2);
      return this;
   }

   public Attributes attributes() {
      return this.attributes;
   }

   public String baseUri() {
      return this.baseUri;
   }

   public Node before(String var1) {
      this.addSiblingHtml(this.siblingIndex, var1);
      return this;
   }

   public Node before(Node var1) {
      Validate.notNull(var1);
      Validate.notNull(this.parentNode);
      this.parentNode.addChildren(this.siblingIndex, var1);
      return this;
   }

   public Node childNode(int var1) {
      return (Node)this.childNodes.get(var1);
   }

   public final int childNodeSize() {
      return this.childNodes.size();
   }

   public List childNodes() {
      return Collections.unmodifiableList(this.childNodes);
   }

   protected Node[] childNodesAsArray() {
      return (Node[])this.childNodes.toArray(new Node[this.childNodeSize()]);
   }

   public List childNodesCopy() {
      ArrayList var1 = new ArrayList(this.childNodes.size());
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         var1.add(((Node)var2.next()).clone());
      }

      return var1;
   }

   public Node clone() {
      Node var2 = this.doClone((Node)null);
      LinkedList var3 = new LinkedList();
      var3.add(var2);

      while(!var3.isEmpty()) {
         Node var4 = (Node)var3.remove();

         for(int var1 = 0; var1 < var4.childNodes.size(); ++var1) {
            Node var5 = ((Node)var4.childNodes.get(var1)).doClone(var4);
            var4.childNodes.set(var1, var5);
            var3.add(var5);
         }
      }

      return var2;
   }

   protected Node doClone(Node var1) {
      Node var3;
      try {
         var3 = (Node)super.clone();
      } catch (CloneNotSupportedException var5) {
         throw new RuntimeException(var5);
      }

      var3.parentNode = var1;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = this.siblingIndex;
      }

      var3.siblingIndex = var2;
      Attributes var6;
      if (this.attributes != null) {
         var6 = this.attributes.clone();
      } else {
         var6 = null;
      }

      var3.attributes = var6;
      var3.baseUri = this.baseUri;
      var3.childNodes = new ArrayList(this.childNodes.size());
      Iterator var7 = this.childNodes.iterator();

      while(var7.hasNext()) {
         Node var4 = (Node)var7.next();
         var3.childNodes.add(var4);
      }

      return var3;
   }

   protected void ensureChildNodes() {
      if (this.childNodes == EMPTY_NODES) {
         this.childNodes = new ArrayList(4);
      }

   }

   public boolean equals(Object var1) {
      return this == var1;
   }

   Document.OutputSettings getOutputSettings() {
      return this.ownerDocument() != null ? this.ownerDocument().outputSettings() : (new Document("")).outputSettings();
   }

   public boolean hasAttr(String var1) {
      Validate.notNull(var1);
      if (var1.startsWith("abs:")) {
         String var2 = var1.substring("abs:".length());
         if (this.attributes.hasKey(var2) && !this.absUrl(var2).equals("")) {
            return true;
         }
      }

      return this.attributes.hasKey(var1);
   }

   public boolean hasSameValue(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Node var2 = (Node)var1;
         return this.outerHtml().equals(((Node)var1).outerHtml());
      } else {
         return false;
      }
   }

   public Appendable html(Appendable var1) {
      this.outerHtml(var1);
      return var1;
   }

   protected void indent(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      var1.append("\n").append(StringUtil.padding(var3.indentAmount() * var2));
   }

   public Node nextSibling() {
      if (this.parentNode != null) {
         List var2 = this.parentNode.childNodes;
         int var1 = this.siblingIndex + 1;
         if (var2.size() > var1) {
            return (Node)var2.get(var1);
         }
      }

      return null;
   }

   public abstract String nodeName();

   public String outerHtml() {
      StringBuilder var1 = new StringBuilder(128);
      this.outerHtml(var1);
      return var1.toString();
   }

   protected void outerHtml(Appendable var1) {
      (new NodeTraversor(new Node.OuterHtmlVisitor(var1, this.getOutputSettings()))).traverse(this);
   }

   abstract void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException;

   abstract void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) throws IOException;

   public Document ownerDocument() {
      if (this instanceof Document) {
         return (Document)this;
      } else {
         return this.parentNode == null ? null : this.parentNode.ownerDocument();
      }
   }

   public Node parent() {
      return this.parentNode;
   }

   public final Node parentNode() {
      return this.parentNode;
   }

   public Node previousSibling() {
      return this.parentNode != null && this.siblingIndex > 0 ? (Node)this.parentNode.childNodes.get(this.siblingIndex - 1) : null;
   }

   public void remove() {
      Validate.notNull(this.parentNode);
      this.parentNode.removeChild(this);
   }

   public Node removeAttr(String var1) {
      Validate.notNull(var1);
      this.attributes.remove(var1);
      return this;
   }

   protected void removeChild(Node var1) {
      boolean var3;
      if (var1.parentNode == this) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3);
      int var2 = var1.siblingIndex;
      this.childNodes.remove(var2);
      this.reindexChildren(var2);
      var1.parentNode = null;
   }

   protected void reparentChild(Node var1) {
      if (var1.parentNode != null) {
         var1.parentNode.removeChild(var1);
      }

      var1.setParentNode(this);
   }

   protected void replaceChild(Node var1, Node var2) {
      boolean var4;
      if (var1.parentNode == this) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4);
      Validate.notNull(var2);
      if (var2.parentNode != null) {
         var2.parentNode.removeChild(var2);
      }

      int var3 = var1.siblingIndex;
      this.childNodes.set(var3, var2);
      var2.parentNode = this;
      var2.setSiblingIndex(var3);
      var1.parentNode = null;
   }

   public void replaceWith(Node var1) {
      Validate.notNull(var1);
      Validate.notNull(this.parentNode);
      this.parentNode.replaceChild(this, var1);
   }

   public void setBaseUri(final String var1) {
      Validate.notNull(var1);
      this.traverse(new NodeVisitor() {
         public void head(Node var1x, int var2) {
            var1x.baseUri = var1;
         }

         public void tail(Node var1x, int var2) {
         }
      });
   }

   protected void setParentNode(Node var1) {
      if (this.parentNode != null) {
         this.parentNode.removeChild(this);
      }

      this.parentNode = var1;
   }

   protected void setSiblingIndex(int var1) {
      this.siblingIndex = var1;
   }

   public int siblingIndex() {
      return this.siblingIndex;
   }

   public List siblingNodes() {
      Object var1;
      if (this.parentNode == null) {
         var1 = Collections.emptyList();
      } else {
         List var4 = this.parentNode.childNodes;
         ArrayList var2 = new ArrayList(var4.size() - 1);
         Iterator var3 = var4.iterator();

         while(true) {
            var1 = var2;
            if (!var3.hasNext()) {
               break;
            }

            Node var5 = (Node)var3.next();
            if (var5 != this) {
               var2.add(var5);
            }
         }
      }

      return (List)var1;
   }

   public String toString() {
      return this.outerHtml();
   }

   public Node traverse(NodeVisitor var1) {
      Validate.notNull(var1);
      (new NodeTraversor(var1)).traverse(this);
      return this;
   }

   public Node unwrap() {
      Validate.notNull(this.parentNode);
      Node var1;
      if (this.childNodes.size() > 0) {
         var1 = (Node)this.childNodes.get(0);
      } else {
         var1 = null;
      }

      this.parentNode.addChildren(this.siblingIndex, this.childNodesAsArray());
      this.remove();
      return var1;
   }

   public Node wrap(String var1) {
      Validate.notEmpty(var1);
      Element var3;
      if (this.parent() instanceof Element) {
         var3 = (Element)this.parent();
      } else {
         var3 = null;
      }

      List var7 = Parser.parseFragment(var1, var3, this.baseUri());
      Node var5 = (Node)var7.get(0);
      if (var5 != null && var5 instanceof Element) {
         Element var4 = (Element)var5;
         Element var6 = this.getDeepChild(var4);
         this.parentNode.replaceChild(this, var4);
         var6.addChildren(new Node[]{this});
         var5 = this;
         if (var7.size() > 0) {
            int var2 = 0;

            while(true) {
               var5 = this;
               if (var2 >= var7.size()) {
                  break;
               }

               var5 = (Node)var7.get(var2);
               var5.parentNode.removeChild(var5);
               var4.appendChild(var5);
               ++var2;
            }
         }
      } else {
         var5 = null;
      }

      return var5;
   }

   private static class OuterHtmlVisitor implements NodeVisitor {
      private Appendable accum;
      private Document.OutputSettings out;

      OuterHtmlVisitor(Appendable var1, Document.OutputSettings var2) {
         this.accum = var1;
         this.out = var2;
      }

      public void head(Node var1, int var2) {
         try {
            var1.outerHtmlHead(this.accum, var2, this.out);
         } catch (IOException var3) {
            throw new SerializationException(var3);
         }
      }

      public void tail(Node var1, int var2) {
         if (!var1.nodeName().equals("#text")) {
            try {
               var1.outerHtmlTail(this.accum, var2, this.out);
            } catch (IOException var3) {
               throw new SerializationException(var3);
            }
         }

      }
   }
}
