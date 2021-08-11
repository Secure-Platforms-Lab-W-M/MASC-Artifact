package net.java.sip.communicator.plugin.otr.authdialog;

import org.atalk.android.util.java.awt.Color;
import org.atalk.android.util.javax.swing.JTextArea;

/**
 * A special {@link JTextArea} for use in the OTR authentication panels.
 * It is meant to be used for fingerprint representation and general
 * information display.
 *
 * @author George Politis
 */
public class CustomTextArea extends JTextArea {
	public CustomTextArea() {
		this.setBackground(new Color(0, 0, 0, 0));
		this.setOpaque(false);
		this.setColumns(20);
		this.setEditable(false);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
	}
}