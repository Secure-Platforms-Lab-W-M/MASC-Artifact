package net.sf.fmj.media;

import net.sf.fmj.media.util.MediaThread;

class PlayThread extends MediaThread {
   BasicPlayer player;

   public PlayThread(BasicPlayer var1) {
      this.player = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getName());
      var2.append(" (PlayThread)");
      this.setName(var2.toString());
      this.useControlPriority();
   }

   public void run() {
      this.player.play();
   }
}
