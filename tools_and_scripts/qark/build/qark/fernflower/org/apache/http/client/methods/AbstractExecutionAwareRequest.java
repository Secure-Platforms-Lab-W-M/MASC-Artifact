package org.apache.http.client.methods;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicMarkableReference;
import org.apache.http.HttpRequest;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpParams;

public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest {
   private final AtomicMarkableReference cancellableRef = new AtomicMarkableReference((Object)null, false);

   protected AbstractExecutionAwareRequest() {
   }

   public void abort() {
      while(!this.cancellableRef.isMarked()) {
         Cancellable var1 = (Cancellable)this.cancellableRef.getReference();
         if (this.cancellableRef.compareAndSet(var1, var1, false, true) && var1 != null) {
            var1.cancel();
         }
      }

   }

   public Object clone() throws CloneNotSupportedException {
      AbstractExecutionAwareRequest var1 = (AbstractExecutionAwareRequest)super.clone();
      var1.headergroup = (HeaderGroup)CloneUtils.cloneObject(this.headergroup);
      var1.params = (HttpParams)CloneUtils.cloneObject(this.params);
      return var1;
   }

   @Deprecated
   public void completed() {
      this.cancellableRef.set((Object)null, false);
   }

   public boolean isAborted() {
      return this.cancellableRef.isMarked();
   }

   public void reset() {
      boolean var1;
      Cancellable var2;
      do {
         var1 = this.cancellableRef.isMarked();
         var2 = (Cancellable)this.cancellableRef.getReference();
         if (var2 != null) {
            var2.cancel();
         }
      } while(!this.cancellableRef.compareAndSet(var2, (Object)null, var1, false));

   }

   public void setCancellable(Cancellable var1) {
      Cancellable var2 = (Cancellable)this.cancellableRef.getReference();
      if (!this.cancellableRef.compareAndSet(var2, var1, false, false)) {
         var1.cancel();
      }

   }

   @Deprecated
   public void setConnectionRequest(final ClientConnectionRequest var1) {
      this.setCancellable(new Cancellable() {
         public boolean cancel() {
            var1.abortRequest();
            return true;
         }
      });
   }

   @Deprecated
   public void setReleaseTrigger(final ConnectionReleaseTrigger var1) {
      this.setCancellable(new Cancellable() {
         public boolean cancel() {
            try {
               var1.abortConnection();
               return true;
            } catch (IOException var2) {
               return false;
            }
         }
      });
   }
}
