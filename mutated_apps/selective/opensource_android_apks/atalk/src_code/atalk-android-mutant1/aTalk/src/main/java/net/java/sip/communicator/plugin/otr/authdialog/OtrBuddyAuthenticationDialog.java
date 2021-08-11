/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.plugin.otr.authdialog;

import net.java.sip.communicator.plugin.desktoputil.SIPCommDialog;
import net.java.sip.communicator.plugin.desktoputil.TransparentPanel;
import net.java.sip.communicator.plugin.otr.OtrActivator;
import net.java.sip.communicator.plugin.otr.OtrContactManager.OtrContact;
import net.java.sip.communicator.plugin.otr.authdialog.FingerprintAuthenticationPanel.ActionComboBoxItem;
import net.java.sip.communicator.service.protocol.Contact;

import org.atalk.android.util.java.awt.*;
import org.atalk.android.util.java.awt.event.*;
import org.atalk.android.util.javax.swing.*;

import java.security.PublicKey;

/**
 * @author George Politis
 * @author Marin Dzhigarov
 */
@SuppressWarnings("serial")
public class OtrBuddyAuthenticationDialog extends SIPCommDialog
{
    private final OtrContact otrContact;

    /**
     * The {@link OtrBuddyAuthenticationDialog} ctor.
     *
     * @param otrContact The {@link Contact} this
     * {@link OtrBuddyAuthenticationDialog} refers to.
     */
    public OtrBuddyAuthenticationDialog(OtrContact otrContact)
    {
        super(false);
        this.otrContact = otrContact;

        initComponents();
    }

    /**
     * Initializes the {@link OtrBuddyAuthenticationDialog} components.
     */
    private void initComponents()
    {
        this.setTitle(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.TITLE"));
        TransparentPanel mainPanel = new TransparentPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setPreferredSize(new Dimension(350, 400));

        JTextArea generalInformation = new CustomTextArea();
        generalInformation.setText(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATION_INFO"));
        mainPanel.add(generalInformation);

        mainPanel.add(Box.createVerticalStrut(10));

        // Add authentication method label and combo box.
        final String am[] = new String[]{
                OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATION_METHOD_QUESTION"),
                OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATION_METHOD_SECRET"),
                OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATION_METHOD_FINGERPRINT")};

        final JComboBox authenticationMethodComboBox = new JComboBox(am);
        JTextArea authMethodLabel = new CustomTextArea();
        authMethodLabel.setText(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATION_METHOD"));
        mainPanel.add(authMethodLabel);
        mainPanel.add(authenticationMethodComboBox);
        mainPanel.add(Box.createVerticalStrut(10));

        // Add authentication panels in a card layout so that the user can
        // use the combo box to switch between authentication methods.
        final JPanel authenticationPanel = new TransparentPanel(new CardLayout());
        final FingerprintAuthenticationPanel fingerprintPanel = new FingerprintAuthenticationPanel(otrContact);
        final SecretQuestionAuthenticationPanel secretQuestionPanel = new SecretQuestionAuthenticationPanel();
        final SharedSecretAuthenticationPanel sharedSecretPanel = new SharedSecretAuthenticationPanel();
        authenticationPanel.add(secretQuestionPanel, am[0]);
        authenticationPanel.add(sharedSecretPanel, am[1]);
        authenticationPanel.add(fingerprintPanel, am[2]);

        authenticationMethodComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    CardLayout cl = (CardLayout) (authenticationPanel.getLayout());
                    cl.show(authenticationPanel, (String) e.getItem());
                }
            }
        });
        authenticationMethodComboBox.setSelectedIndex(0);
        mainPanel.add(authenticationPanel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0;
        c.gridwidth = 1;

        // Buttons panel.
        JPanel buttonPanel = new TransparentPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JButton helpButton = new JButton(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.HELP"));
        helpButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                OtrActivator.scOtrEngine.launchHelp();
            }
        });

        buttonPanel.add(helpButton, c);

        // Provide space between help and the other two button, not sure if this
        // is optimal..
        c.weightx = 1.0;
        buttonPanel.add(new JLabel(), c);
        c.weightx = 0.0;

        JButton cancelButton = new JButton(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.CANCEL"));
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        buttonPanel.add(cancelButton, c);

        JButton authenticateButton = new JButton(OtrActivator.resourceService.getI18NString("plugin.otr.authbuddydialog.AUTHENTICATE_BUDDY"));
        authenticateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String authenticationMethod =
                        (String) authenticationMethodComboBox.getSelectedItem();
                if (authenticationMethod.equals(am[0])) {
                    String secret = secretQuestionPanel.getSecret();
                    String question = secretQuestionPanel.getQuestion();

                    OtrActivator.scOtrEngine.initSmp(otrContact, question, secret);
                    dispose();
                }
                else if (authenticationMethod.equals(am[1])) {
                    String secret = sharedSecretPanel.getSecret();
                    String question = null;

                    OtrActivator.scOtrEngine.initSmp(otrContact, question, secret);
                    dispose();
                }
                else if (authenticationMethod.equals(am[2])) {
                    ActionComboBoxItem actionItem = (ActionComboBoxItem) fingerprintPanel.getCbAction().getSelectedItem();
                    PublicKey pubKey = OtrActivator.scOtrEngine.getRemotePublicKey(otrContact);
                    String fingerprint = OtrActivator.scOtrKeyManager.getFingerprintFromPublicKey(pubKey);
                    switch (actionItem.action) {
                        case I_HAVE:
                            OtrActivator.scOtrKeyManager.verify(otrContact, fingerprint);
                            break;
                        case I_HAVE_NOT:
                            OtrActivator.scOtrKeyManager.unverify(otrContact, fingerprint);
                            break;
                    }
                    dispose();
                }
            }
        });
        buttonPanel.add(authenticateButton, c);

        this.getContentPane().add(mainPanel, BorderLayout.NORTH);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
    }
}
