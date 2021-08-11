package org.apache.commons.text.diff;

import java.util.ArrayList;
import java.util.List;

public class ReplacementsFinder implements CommandVisitor {
   private final ReplacementsHandler handler;
   private final List pendingDeletions = new ArrayList();
   private final List pendingInsertions = new ArrayList();
   private int skipped = 0;

   public ReplacementsFinder(ReplacementsHandler var1) {
      this.handler = var1;
   }

   public void visitDeleteCommand(Object var1) {
      this.pendingDeletions.add(var1);
   }

   public void visitInsertCommand(Object var1) {
      this.pendingInsertions.add(var1);
   }

   public void visitKeepCommand(Object var1) {
      if (this.pendingDeletions.isEmpty() && this.pendingInsertions.isEmpty()) {
         ++this.skipped;
      } else {
         this.handler.handleReplacement(this.skipped, this.pendingDeletions, this.pendingInsertions);
         this.pendingDeletions.clear();
         this.pendingInsertions.clear();
         this.skipped = 1;
      }
   }
}
