package org.apache.http.impl.client;

import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class ClientParamsStack extends AbstractHttpParams {
   protected final HttpParams applicationParams;
   protected final HttpParams clientParams;
   protected final HttpParams overrideParams;
   protected final HttpParams requestParams;

   public ClientParamsStack(ClientParamsStack var1) {
      this(var1.getApplicationParams(), var1.getClientParams(), var1.getRequestParams(), var1.getOverrideParams());
   }

   public ClientParamsStack(ClientParamsStack var1, HttpParams var2, HttpParams var3, HttpParams var4, HttpParams var5) {
      if (var2 == null) {
         var2 = var1.getApplicationParams();
      }

      if (var3 == null) {
         var3 = var1.getClientParams();
      }

      if (var4 == null) {
         var4 = var1.getRequestParams();
      }

      if (var5 == null) {
         var5 = var1.getOverrideParams();
      }

      this(var2, var3, var4, var5);
   }

   public ClientParamsStack(HttpParams var1, HttpParams var2, HttpParams var3, HttpParams var4) {
      super();
      this.applicationParams = var1;
      this.clientParams = var2;
      this.requestParams = var3;
      this.overrideParams = var4;
   }

   public HttpParams copy() {
      return this;
   }

   public final HttpParams getApplicationParams() {
      return this.applicationParams;
   }

   public final HttpParams getClientParams() {
      return this.clientParams;
   }

   public final HttpParams getOverrideParams() {
      return this.overrideParams;
   }

   public Object getParameter(String var1) {
      Args.notNull(var1, "Parameter name");
      Object var3 = null;
      HttpParams var2 = this.overrideParams;
      if (var2 != null) {
         var3 = var2.getParameter(var1);
      }

      Object var5 = var3;
      HttpParams var4;
      if (var3 == null) {
         var4 = this.requestParams;
         var5 = var3;
         if (var4 != null) {
            var5 = var4.getParameter(var1);
         }
      }

      var3 = var5;
      if (var5 == null) {
         var4 = this.clientParams;
         var3 = var5;
         if (var4 != null) {
            var3 = var4.getParameter(var1);
         }
      }

      var5 = var3;
      if (var3 == null) {
         var4 = this.applicationParams;
         var5 = var3;
         if (var4 != null) {
            var5 = var4.getParameter(var1);
         }
      }

      return var5;
   }

   public final HttpParams getRequestParams() {
      return this.requestParams;
   }

   public boolean removeParameter(String var1) {
      throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
   }

   public HttpParams setParameter(String var1, Object var2) throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
   }
}
