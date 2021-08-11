package org.jsoup.select;

import org.jsoup.nodes.Node;

public class NodeTraversor {
   private NodeVisitor visitor;

   public NodeTraversor(NodeVisitor var1) {
      this.visitor = var1;
   }

   public void traverse(Node var1) {
      Node var4 = var1;
      int var2 = 0;

      while(var4 != null) {
         this.visitor.head(var4, var2);
         int var3 = var2;
         Node var5 = var4;
         if (var4.childNodeSize() > 0) {
            var4 = var4.childNode(0);
            ++var2;
         } else {
            while(var5.nextSibling() == null && var3 > 0) {
               this.visitor.tail(var5, var3);
               var5 = var5.parentNode();
               --var3;
            }

            this.visitor.tail(var5, var3);
            if (var5 == var1) {
               break;
            }

            var4 = var5.nextSibling();
            var2 = var3;
         }
      }

   }
}
