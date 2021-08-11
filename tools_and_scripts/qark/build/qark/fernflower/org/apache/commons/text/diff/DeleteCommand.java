package org.apache.commons.text.diff;

public class DeleteCommand extends EditCommand {
   public DeleteCommand(Object var1) {
      super(var1);
   }

   public void accept(CommandVisitor var1) {
      var1.visitDeleteCommand(this.getObject());
   }
}
