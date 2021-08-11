package org.apache.commons.text.diff;

public abstract class EditCommand {
   private final Object object;

   protected EditCommand(Object var1) {
      this.object = var1;
   }

   public abstract void accept(CommandVisitor var1);

   protected Object getObject() {
      return this.object;
   }
}
