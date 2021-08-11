package org.jsoup.select;

import org.jsoup.nodes.Node;

public interface NodeVisitor {
   void head(Node var1, int var2);

   void tail(Node var1, int var2);
}
