package org.apache.commons.text.diff;

public class InsertCommand extends EditCommand {
   public InsertCommand(Object var1) {
      super(var1);
   }

   public void accept(CommandVisitor var1) {
      var1.visitInsertCommand(this.getObject());
   }
}
