package com.lti.utils.synchronization;

public class MessageDrivenThread extends CloseableThread {
   private MessageDrivenThreadListener listener;
   // $FF: renamed from: q com.lti.utils.synchronization.ProducerConsumerQueue
   private final ProducerConsumerQueue field_17 = new ProducerConsumerQueue();

   public MessageDrivenThread(ThreadGroup var1, String var2) {
      super(var1, var2);
   }

   public MessageDrivenThread(ThreadGroup var1, String var2, MessageDrivenThreadListener var3) {
      super(var1, var2);
      this.listener = var3;
   }

   protected void doMessageReceived(Object var1) {
      MessageDrivenThreadListener var2 = this.listener;
      if (var2 != null) {
         var2.onMessage(this, var1);
      }

   }

   public void post(Object var1) throws InterruptedException {
      this.field_17.put(var1);
   }

   public void run() {
      while(true) {
         boolean var4 = false;

         try {
            var4 = true;
            if (!this.isClosing()) {
               this.doMessageReceived(this.field_17.get());
               var4 = false;
               continue;
            }

            var4 = false;
         } catch (InterruptedException var5) {
            var4 = false;
         } finally {
            if (var4) {
               this.setClosed();
            }
         }

         this.setClosed();
         return;
      }
   }

   public void setListener(MessageDrivenThreadListener var1) {
      this.listener = var1;
   }
}
