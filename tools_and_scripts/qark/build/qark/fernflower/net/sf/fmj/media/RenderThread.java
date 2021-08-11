package net.sf.fmj.media;

import net.sf.fmj.media.util.LoopThread;

class RenderThread extends LoopThread {
   BasicRendererModule module;

   public RenderThread(BasicRendererModule var1) {
      this.module = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(": ");
      var2.append(var1.renderer);
      this.setName(var2.toString());
      this.useVideoPriority();
   }

   protected boolean process() {
      return this.module.doProcess();
   }
}
