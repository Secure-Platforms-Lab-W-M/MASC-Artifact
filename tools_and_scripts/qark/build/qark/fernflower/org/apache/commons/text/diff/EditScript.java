package org.apache.commons.text.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditScript {
   private final List commands = new ArrayList();
   private int lcsLength = 0;
   private int modifications = 0;

   public void append(DeleteCommand var1) {
      this.commands.add(var1);
      ++this.modifications;
   }

   public void append(InsertCommand var1) {
      this.commands.add(var1);
      ++this.modifications;
   }

   public void append(KeepCommand var1) {
      this.commands.add(var1);
      ++this.lcsLength;
   }

   public int getLCSLength() {
      return this.lcsLength;
   }

   public int getModifications() {
      return this.modifications;
   }

   public void visit(CommandVisitor var1) {
      Iterator var2 = this.commands.iterator();

      while(var2.hasNext()) {
         ((EditCommand)var2.next()).accept(var1);
      }

   }
}
