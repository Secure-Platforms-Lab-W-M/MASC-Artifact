package net.sf.fmj.ejmf.toolkit.util;

import java.io.IOException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import org.atalk.android.util.java.awt.BorderLayout;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.javax.swing.JPanel;

public class PlayerPanel extends JPanel {
   private static final String LOADLABEL = "Loading Media...";
   private static final Object _CPLOC = "South";
   private static final Object _VISLOC = "Center";
   private JPanel mediaPanel;
   private Player player;

   public PlayerPanel(MediaLocator var1) throws IOException, NoPlayerException {
      this.player = Manager.createPlayer(var1);
      this.setLayout(new BorderLayout());
      JPanel var2 = new JPanel();
      this.mediaPanel = var2;
      var2.setLayout(new BorderLayout());
   }

   public PlayerPanel(Player var1) throws IOException, NoPlayerException {
      this.player = var1;
      this.setLayout(new BorderLayout());
      JPanel var2 = new JPanel();
      this.mediaPanel = var2;
      var2.setLayout(new BorderLayout());
   }

   private Component addComponent(Component var1, Object var2) {
      if (var1 == null) {
         return var1;
      } else {
         this.mediaPanel.add(var1, var2);
         return var1;
      }
   }

   public Component addControlComponent() {
      return this.addControlComponent(this.player.getControlPanelComponent());
   }

   public Component addControlComponent(Component var1) {
      this.addMediaPanel();
      return this.addComponent(var1, _CPLOC);
   }

   public void addMediaPanel() {
      if (!this.isAncestorOf(this.mediaPanel)) {
         this.add(this.mediaPanel, "Center");
      }

   }

   public Component addVisualComponent() {
      return this.addVisualComponent(this.player.getVisualComponent());
   }

   public Component addVisualComponent(Component var1) {
      this.addMediaPanel();
      return this.addComponent(var1, _VISLOC);
   }

   public JPanel getMediaPanel() {
      return this.mediaPanel;
   }

   public Player getPlayer() {
      return this.player;
   }
}
