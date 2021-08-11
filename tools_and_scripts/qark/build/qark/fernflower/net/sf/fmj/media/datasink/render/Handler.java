package net.sf.fmj.media.datasink.render;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;
import javax.media.IncompatibleSourceException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.protocol.DataSource;
import net.sf.fmj.ejmf.toolkit.util.PlayerPanel;
import net.sf.fmj.media.AbstractDataSink;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.event.ContainerEvent;
import org.atalk.android.util.java.awt.event.ContainerListener;
import org.atalk.android.util.java.awt.event.WindowAdapter;
import org.atalk.android.util.java.awt.event.WindowEvent;
import org.atalk.android.util.javax.swing.JFrame;

public class Handler extends AbstractDataSink {
   private static final Logger logger;
   private Player player;
   private DataSource source;

   static {
      logger = LoggerSingleton.logger;
   }

   public void close() {
      try {
         this.stop();
      } catch (IOException var5) {
         Logger var2 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
      }
   }

   public String getContentType() {
      DataSource var1 = this.source;
      return var1 != null ? var1.getContentType() : null;
   }

   public Object getControl(String var1) {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("TODO: getControl ");
      var3.append(var1);
      var2.warning(var3.toString());
      return null;
   }

   public Object[] getControls() {
      logger.warning("TODO: getControls");
      return new Object[0];
   }

   public void open() throws IOException, SecurityException {
      Logger var2;
      Level var3;
      StringBuilder var4;
      StringBuilder var7;
      try {
         this.player = Manager.createRealizedPlayer(this.source);
      } catch (NoPlayerException var5) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
         var7 = new StringBuilder();
         var7.append("");
         var7.append(var5);
         throw new IOException(var7.toString());
      } catch (CannotRealizeException var6) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var6);
         var2.log(var3, var4.toString(), var6);
         var7 = new StringBuilder();
         var7.append("");
         var7.append(var6);
         throw new IOException(var7.toString());
      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      this.source = var1;
   }

   public void start() throws IOException {
      if (this.player.getVisualComponent() != null) {
         PlayerPanel var1;
         try {
            var1 = new PlayerPanel(this.player);
         } catch (NoPlayerException var5) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var2.log(var3, var4.toString(), var5);
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(var5);
            throw new IOException(var6.toString());
         }

         var1.addVisualComponent();
         final JFrame var7 = new JFrame("Renderer");
         var7.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
            }
         });
         var1.getMediaPanel().addContainerListener(new ContainerListener() {
            public void componentAdded(ContainerEvent var1) {
               var7.pack();
            }

            public void componentRemoved(ContainerEvent var1) {
               var7.pack();
            }
         });
         var7.getContentPane().add(var1);
         var7.pack();
         var7.setVisible(true);
      }

      this.player.start();
   }

   public void stop() throws IOException {
      Player var1 = this.player;
      if (var1 != null) {
         var1.stop();
      }

   }
}
