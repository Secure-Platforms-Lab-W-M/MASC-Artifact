package net.sf.fmj.media;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.Time;

public abstract class BasicModule implements Module, StateTransistor {
   protected BasicController controller;
   protected BasicModule.Registry inputConnectors = new BasicModule.Registry();
   protected InputConnector[] inputConnectorsArray;
   protected ModuleListener moduleListener;
   protected String name = null;
   protected BasicModule.Registry outputConnectors = new BasicModule.Registry();
   protected OutputConnector[] outputConnectorsArray;
   protected boolean prefetchFailed = false;
   protected int protocol = 0;
   protected boolean resetted = false;

   public void abortPrefetch() {
   }

   public void abortRealize() {
   }

   public boolean canRun() {
      int var1 = 0;

      while(true) {
         InputConnector[] var2 = this.inputConnectorsArray;
         if (var1 >= var2.length) {
            var1 = 0;

            while(true) {
               OutputConnector[] var3 = this.outputConnectorsArray;
               if (var1 >= var3.length) {
                  return true;
               }

               if (!var3[var1].isEmptyBufferAvailable()) {
                  return false;
               }

               ++var1;
            }
         }

         if (!var2[var1].isValidBufferAvailable()) {
            return false;
         }

         ++var1;
      }
   }

   public void connectorPushed(InputConnector var1) {
      this.process();
   }

   public void doClose() {
   }

   public void doDealloc() {
   }

   public void doFailedPrefetch() {
   }

   public void doFailedRealize() {
   }

   public boolean doPrefetch() {
      this.resetted = false;
      return true;
   }

   public boolean doRealize() {
      return true;
   }

   public void doSetMediaTime(Time var1) {
   }

   public float doSetRate(float var1) {
      return var1;
   }

   public void doStart() {
      this.resetted = false;
   }

   public void doStop() {
   }

   protected void error() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append(" error");
      throw new RuntimeException(var1.toString());
   }

   public Object getControl(String var1) {
      return null;
   }

   public final BasicController getController() {
      return this.controller;
   }

   public Object[] getControls() {
      return null;
   }

   public InputConnector getInputConnector(String var1) {
      return (InputConnector)this.inputConnectors.get(var1);
   }

   public String[] getInputConnectorNames() {
      return this.inputConnectors.getNames();
   }

   public long getLatency() {
      return ((PlaybackEngine)this.controller).getLatency();
   }

   public long getMediaNanoseconds() {
      return this.controller.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      return this.controller.getMediaTime();
   }

   public final String getName() {
      return this.name;
   }

   public OutputConnector getOutputConnector(String var1) {
      return (OutputConnector)this.outputConnectors.get(var1);
   }

   public String[] getOutputConnectorNames() {
      return this.outputConnectors.getNames();
   }

   public int getProtocol() {
      return this.protocol;
   }

   public final int getState() {
      return this.controller.getState();
   }

   public final boolean isInterrupted() {
      BasicController var1 = this.controller;
      return var1 == null ? false : var1.isInterrupted();
   }

   public boolean isThreaded() {
      return true;
   }

   public boolean prefetchFailed() {
      return this.prefetchFailed;
   }

   protected abstract void process();

   public void registerInputConnector(String var1, InputConnector var2) {
      this.inputConnectors.put((String)var1, (Connector)var2);
      var2.setModule(this);
   }

   public void registerOutputConnector(String var1, OutputConnector var2) {
      this.outputConnectors.put((String)var1, (Connector)var2);
      var2.setModule(this);
   }

   public void reset() {
      this.resetted = true;
   }

   public final void setController(BasicController var1) {
      this.controller = var1;
   }

   public void setFormat(Connector var1, Format var2) {
   }

   public void setModuleListener(ModuleListener var1) {
      this.moduleListener = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setProtocol(int var1) {
      this.protocol = var1;
      Connector[] var3 = this.inputConnectors.getConnectors();

      int var2;
      for(var2 = 0; var2 < var3.length; ++var2) {
         var3[var2].setProtocol(var1);
      }

      var3 = this.outputConnectors.getConnectors();

      for(var2 = 0; var2 < var3.length; ++var2) {
         var3[var2].setProtocol(var1);
      }

   }

   protected boolean verifyBuffer(Buffer var1) {
      if (var1.isDiscard()) {
         return true;
      } else {
         Object var2 = var1.getData();
         PrintStream var3;
         StringBuilder var4;
         if (var1.getLength() < 0) {
            var3 = System.err;
            var4 = new StringBuilder();
            var4.append("warning: data length shouldn't be negative: ");
            var4.append(var1.getLength());
            var3.println(var4.toString());
         }

         if (var2 == null) {
            System.err.println("warning: data buffer is null");
            if (var1.getLength() != 0) {
               PrintStream var5 = System.err;
               StringBuilder var6 = new StringBuilder();
               var6.append("buffer advertized length = ");
               var6.append(var1.getLength());
               var6.append(" but data buffer is null!");
               var5.println(var6.toString());
               return false;
            }
         } else if (var2 instanceof byte[]) {
            if (var1.getLength() > ((byte[])((byte[])var2)).length) {
               var3 = System.err;
               var4 = new StringBuilder();
               var4.append("buffer advertized length = ");
               var4.append(var1.getLength());
               var4.append(" but actual length = ");
               var4.append(((byte[])((byte[])var2)).length);
               var3.println(var4.toString());
               return false;
            }
         } else if (var2 instanceof int[] && var1.getLength() > ((int[])((int[])var2)).length) {
            var3 = System.err;
            var4 = new StringBuilder();
            var4.append("buffer advertized length = ");
            var4.append(var1.getLength());
            var4.append(" but actual length = ");
            var4.append(((int[])((int[])var2)).length);
            var3.println(var4.toString());
            return false;
         }

         return true;
      }
   }

   class Registry extends Hashtable {
      Connector def = null;

      Object get(String var1) {
         return var1 == null ? this.def : super.get(var1);
      }

      Connector[] getConnectors() {
         Enumeration var2 = this.elements();
         Connector[] var3 = new Connector[this.size()];

         for(int var1 = 0; var1 < this.size(); ++var1) {
            var3[var1] = (Connector)var2.nextElement();
         }

         return var3;
      }

      String[] getNames() {
         Enumeration var2 = this.keys();
         String[] var3 = new String[this.size()];

         for(int var1 = 0; var1 < this.size(); ++var1) {
            var3[var1] = (String)var2.nextElement();
         }

         return var3;
      }

      public Connector put(String var1, Connector var2) {
         if (!this.containsKey(var1)) {
            if (this.def == null) {
               this.def = var2;
            }

            return (Connector)super.put(var1, var2);
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Connector '");
            var3.append(var1);
            var3.append("' already exists in Module '");
            var3.append(BasicModule.this.getClass().getName());
            var3.append("::");
            var3.append(var1);
            var3.append("'");
            throw new RuntimeException(var3.toString());
         }
      }
   }
}
