package org.apache.commons.text.diff;

public interface CommandVisitor {
   void visitDeleteCommand(Object var1);

   void visitInsertCommand(Object var1);

   void visitKeepCommand(Object var1);
}
