package net.sf.fmj.ejmf.toolkit.controls;

import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.RateChangeEvent;
import org.atalk.android.util.java.awt.BorderLayout;
import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;
import org.atalk.android.util.javax.swing.JLabel;
import org.atalk.android.util.javax.swing.JPanel;
import org.atalk.android.util.javax.swing.JTextField;
import org.atalk.android.util.javax.swing.SwingUtilities;
import org.atalk.android.util.javax.swing.border.CompoundBorder;
import org.atalk.android.util.javax.swing.border.EmptyBorder;
import org.atalk.android.util.javax.swing.border.EtchedBorder;
import org.atalk.android.util.javax.swing.border.TitledBorder;

public class RateControlComponent extends JPanel implements ActionListener, ControllerListener {
   private Controller controller;
   private JTextField rateField = new JTextField(6);

   public RateControlComponent(Controller var1) {
      this.controller = var1;
      this.setUpControlComponent();
      SwingUtilities.invokeLater(new RateControlComponent.LoadRateThread());
      this.rateField.addActionListener(this);
      var1.addControllerListener(this);
   }

   private void loadRate() {
      this.rateField.setText(Float.toString(this.controller.getRate()));
   }

   private void setUpControlComponent() {
      JLabel var1 = new JLabel("Rate:", 4);
      JPanel var2 = new JPanel();
      EmptyBorder var3 = new EmptyBorder(10, 10, 10, 10);
      var2.setBorder(new TitledBorder(new CompoundBorder(new EtchedBorder(), var3), "Rate Control"));
      var2.setLayout(new BorderLayout(10, 10));
      var2.add(var1, "Center");
      var2.add(this.rateField, "East");
      this.add(var2);
   }

   public void actionPerformed(ActionEvent var1) {
      Object var5 = var1.getSource();
      JTextField var3 = this.rateField;
      if (var5 == var3) {
         try {
            float var2 = Float.valueOf(var3.getText());
            this.controller.setRate(var2);
         } catch (NumberFormatException var4) {
         }

         this.loadRate();
      }
   }

   public void controllerUpdate(ControllerEvent var1) {
      if (var1.getSourceController() == this.controller && var1 instanceof RateChangeEvent) {
         SwingUtilities.invokeLater(new RateControlComponent.LoadRateThread());
      }

   }

   class LoadRateThread implements Runnable {
      public void run() {
         RateControlComponent.this.loadRate();
      }
   }
}
