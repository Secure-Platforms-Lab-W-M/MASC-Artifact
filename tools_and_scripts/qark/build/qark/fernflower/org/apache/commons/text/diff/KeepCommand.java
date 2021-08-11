package org.apache.commons.text.diff;

public class KeepCommand extends EditCommand {
   public KeepCommand(Object var1) {
      super(var1);
   }

   public void accept(CommandVisitor var1) {
      var1.visitKeepCommand(this.getObject());
   }
}
