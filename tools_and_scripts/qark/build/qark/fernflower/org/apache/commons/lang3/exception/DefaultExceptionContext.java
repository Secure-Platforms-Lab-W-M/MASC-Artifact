package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {
   private static final long serialVersionUID = 20110706L;
   private final List contextValues = new ArrayList();

   public DefaultExceptionContext addContextValue(String var1, Object var2) {
      this.contextValues.add(new ImmutablePair(var1, var2));
      return this;
   }

   public List getContextEntries() {
      return this.contextValues;
   }

   public Set getContextLabels() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.contextValues.iterator();

      while(var2.hasNext()) {
         var1.add((String)((Pair)var2.next()).getKey());
      }

      return var1;
   }

   public List getContextValues(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.contextValues.iterator();

      while(var3.hasNext()) {
         Pair var4 = (Pair)var3.next();
         if (StringUtils.equals(var1, (CharSequence)var4.getKey())) {
            var2.add(var4.getValue());
         }
      }

      return var2;
   }

   public Object getFirstContextValue(String var1) {
      Iterator var2 = this.contextValues.iterator();

      Pair var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Pair)var2.next();
      } while(!StringUtils.equals(var1, (CharSequence)var3.getKey()));

      return var3.getValue();
   }

   public String getFormattedExceptionMessage(String var1) {
      StringBuilder var3 = new StringBuilder(256);
      if (var1 != null) {
         var3.append(var1);
      }

      if (!this.contextValues.isEmpty()) {
         if (var3.length() > 0) {
            var3.append('\n');
         }

         var3.append("Exception Context:\n");
         int var2 = 0;

         for(Iterator var4 = this.contextValues.iterator(); var4.hasNext(); var3.append("]\n")) {
            Pair var7 = (Pair)var4.next();
            var3.append("\t[");
            ++var2;
            var3.append(var2);
            var3.append(':');
            var3.append((String)var7.getKey());
            var3.append("=");
            Object var8 = var7.getValue();
            if (var8 == null) {
               var3.append("null");
            } else {
               try {
                  var1 = var8.toString();
               } catch (Exception var6) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Exception thrown on toString(): ");
                  var5.append(ExceptionUtils.getStackTrace(var6));
                  var1 = var5.toString();
               }

               var3.append(var1);
            }
         }

         var3.append("---------------------------------");
      }

      return var3.toString();
   }

   public DefaultExceptionContext setContextValue(String var1, Object var2) {
      Iterator var3 = this.contextValues.iterator();

      while(var3.hasNext()) {
         if (StringUtils.equals(var1, (CharSequence)((Pair)var3.next()).getKey())) {
            var3.remove();
         }
      }

      this.addContextValue(var1, var2);
      return this;
   }
}
