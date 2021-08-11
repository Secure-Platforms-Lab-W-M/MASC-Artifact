package net.sf.fmj.ejmf.toolkit.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import net.sf.fmj.utility.FmjStartup;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.event.ContainerEvent;
import org.atalk.android.util.java.awt.event.ContainerListener;
import org.atalk.android.util.java.awt.event.WindowAdapter;
import org.atalk.android.util.java.awt.event.WindowEvent;
import org.atalk.android.util.javax.swing.JApplet;
import org.atalk.android.util.javax.swing.JFrame;

public abstract class PlayerDriver extends JApplet {
   private static final Logger logger;
   private JFrame frame;
   private PlayerPanel playerpanel;

   static {
      logger = LoggerSingleton.logger;
   }

   public PlayerDriver() {
      this.getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
   }

   public static void main(PlayerDriver var0, String[] var1) {
      if (var1.length == 0) {
         logger.severe("Media parameter not specified");
      } else {
         MediaLocator var6 = Utility.appArgToMediaLocator(var1[0]);

         Level var2;
         StringBuilder var3;
         Logger var7;
         try {
            var0.initialize(var6);
         } catch (IOException var4) {
            var7 = logger;
            var2 = Level.WARNING;
            var3 = new StringBuilder();
            var3.append("Could not connect to media: ");
            var3.append(var4);
            var7.log(var2, var3.toString(), var4);
            System.exit(1);
         } catch (NoPlayerException var5) {
            var7 = logger;
            var2 = Level.WARNING;
            var3 = new StringBuilder();
            var3.append("Player not found for media: ");
            var3.append(var5);
            var7.log(var2, var3.toString(), var5);
            System.exit(1);
         }
      }
   }

   public abstract void begin();

   public void destroy() {
      super.destroy();
      if (this.getPlayerPanel() != null && this.getPlayerPanel().getPlayer() != null) {
         this.getPlayerPanel().getPlayer().stop();
         this.getPlayerPanel().getPlayer().close();
      }

   }

   public JFrame getFrame() {
      return this.frame;
   }

   public PlayerPanel getPlayerPanel() {
      return this.playerpanel;
   }

   public void init() {
      FmjStartup.initApplet();
      String var1 = this.getParameter("MEDIA");
      if (var1 == null) {
         logger.warning("Error: MEDIA parameter not specified");
      } else {
         MediaLocator var4 = Utility.appletArgToMediaLocator(this, var1);

         try {
            PlayerPanel var5 = new PlayerPanel(var4);
            this.playerpanel = var5;
            var5.getMediaPanel().addContainerListener(new ContainerListener() {
               public void componentAdded(ContainerEvent var1) {
                  PlayerDriver.this.pack();
               }

               public void componentRemoved(ContainerEvent var1) {
                  PlayerDriver.this.pack();
               }
            });
            this.getContentPane().add(this.playerpanel);
            this.pack();
            this.begin();
         } catch (IOException var2) {
            logger.warning("Could not connect to media");
            this.destroy();
         } catch (NoPlayerException var3) {
            logger.warning("Player not found for media");
            this.destroy();
            return;
         }

      }
   }

   public void initialize(MediaLocator var1) throws IOException, NoPlayerException {
      this.playerpanel = new PlayerPanel(var1);
      JFrame var2 = new JFrame(var1.toString());
      this.frame = var2;
      var2.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            System.exit(0);
         }
      });
      this.playerpanel.getMediaPanel().addContainerListener(new ContainerListener() {
         public void componentAdded(ContainerEvent var1) {
            PlayerDriver.this.frame.pack();
         }

         public void componentRemoved(ContainerEvent var1) {
            PlayerDriver.this.frame.pack();
         }
      });
      this.frame.getContentPane().add(this.playerpanel);
      this.frame.pack();
      this.frame.setVisible(true);
      this.begin();
   }

   public void pack() {
      this.setSize(this.getPreferredSize());
      this.validate();
   }

   public void redraw() {
      this.frame.pack();
   }
}
