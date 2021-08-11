package android.support.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TransitionValues {
   final ArrayList mTargetedTransitions = new ArrayList();
   public final Map values = new HashMap();
   public View view;

   public boolean equals(Object var1) {
      return var1 instanceof TransitionValues && this.view == ((TransitionValues)var1).view && this.values.equals(((TransitionValues)var1).values);
   }

   public int hashCode() {
      return this.view.hashCode() * 31 + this.values.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("TransitionValues@");
      var1.append(Integer.toHexString(this.hashCode()));
      var1.append(":\n");
      String var5 = var1.toString();
      StringBuilder var2 = new StringBuilder();
      var2.append(var5);
      var2.append("    view = ");
      var2.append(this.view);
      var2.append("\n");
      var5 = var2.toString();
      var2 = new StringBuilder();
      var2.append(var5);
      var2.append("    values:");
      var5 = var2.toString();

      StringBuilder var4;
      for(Iterator var6 = this.values.keySet().iterator(); var6.hasNext(); var5 = var4.toString()) {
         String var3 = (String)var6.next();
         var4 = new StringBuilder();
         var4.append(var5);
         var4.append("    ");
         var4.append(var3);
         var4.append(": ");
         var4.append(this.values.get(var3));
         var4.append("\n");
      }

      return var5;
   }
}
