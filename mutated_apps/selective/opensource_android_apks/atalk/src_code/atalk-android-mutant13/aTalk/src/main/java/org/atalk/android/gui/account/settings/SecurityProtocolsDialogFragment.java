/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.account.settings;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import net.java.sip.communicator.service.protocol.SecurityAccountRegistration;

import org.atalk.android.R;
import org.atalk.android.gui.widgets.TouchInterceptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The dialog that displays a list of security protocols in {@link SecurityActivity}.
 * It allows user to enable/disable each protocol and set their priority.
 */
public class SecurityProtocolsDialogFragment extends DialogFragment
{
    /**
     * The encryption protocols managed by this dialog.
     */
   // public static final String[] encryptionProtocols = {"ZRTP", "SDES"};

    public static final String ARG_ENCRYPTIONS = "arg_encryptions";

    public static final String ARG_STATUS_MAP = "arg_status_map";

    public static final String STATE_ENCRYPTIONS = "arg_encryptions";

    public static final String STATE_STATUS_MAP = "arg_status_map";

    /**
     * The list model for the protocols
     */
    private ProtocolsAdapter protocolsAdapter;

    /**
     * The listener that will be notified when this dialog is closed
     */
    private DialogClosedListener listener;

    /**
     * Flag indicating if there have been any changes made
     */
    private boolean hasChanges = false;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        listener = (DialogClosedListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (savedInstanceState == null) {
            this.protocolsAdapter = new ProtocolsAdapter((Map<String, Integer>) getArguments().get(ARG_ENCRYPTIONS),
                    (Map<String, Boolean>) getArguments().get(ARG_STATUS_MAP));
        }
        else {
            this.protocolsAdapter = new ProtocolsAdapter(savedInstanceState.getStringArray(STATE_ENCRYPTIONS),
                    (Map<String, Boolean>) savedInstanceState.get(STATE_STATUS_MAP));
        }

        // Builds the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder = builder.setTitle(R.string.service_gui_SEC_PROTOCOLS_TITLE);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.sec_protocols_dialog, null);
        builder.setView(contentView).setPositiveButton(R.string.service_gui_SEC_PROTOCOLS_OK, (dialog, i) -> {
            hasChanges = true;
            dismiss();
        }).setNegativeButton(R.string.service_gui_SEC_PROTOCOLS_CANCEL, (dialog, i) -> {
            hasChanges = false;
            dismiss();
        });

        TouchInterceptor lv = contentView.findViewById(android.R.id.list);
        lv.setAdapter(protocolsAdapter);
        lv.setDropListener(protocolsAdapter);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putSerializable(STATE_ENCRYPTIONS, protocolsAdapter.encryptions);
        outState.putSerializable(STATE_STATUS_MAP, (Serializable) protocolsAdapter.statusMap);
    }

    /**
     * Commits the changes into given {@link SecurityAccountRegistration}
     *
     * @param securityReg the registration object that will hold new security preferences
     */
    public void commit(SecurityAccountRegistration securityReg)
    {
        Map<String, Integer> protocols = new HashMap<>();
        for (int i = 0; i < protocolsAdapter.encryptions.length; i++) {
            protocols.put(protocolsAdapter.encryptions[i], i);
        }
        securityReg.setEncryptionProtocols(protocols);
        securityReg.setEncryptionProtocolStatus(protocolsAdapter.statusMap);
    }

    /**
     * The interface that will be notified when this dialog is closed
     */
    public interface DialogClosedListener
    {
        void onDialogClosed(SecurityProtocolsDialogFragment dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogClosed(this);
        }
    }

    /**
     * Flag indicating whether any changes have been done to security config
     *
     * @return <tt>true</tt> if any changes have been made
     */
    public boolean hasChanges()
    {
        return hasChanges;
    }

    /**
     * List model for security protocols and their priorities
     */
    class ProtocolsAdapter extends BaseAdapter implements TouchInterceptor.DropListener
    {
        /**
         * The array of encryption protocol names
         */
        protected String[] encryptions;
        /**
         * Maps the on/off status to every protocol
         */
        protected Map<String, Boolean> statusMap = new HashMap<>();

        /**
         * Creates a new instance of {@link ProtocolsAdapter}
         *
         * @param encryptions
         * @param statusMap
         */
        ProtocolsAdapter(final Map<String, Integer> encryptions, final Map<String, Boolean> statusMap)
        {
            String[] encInOrder = (String[]) SecurityAccountRegistration.loadEncryptionProtocols(encryptions, statusMap)[0];
            this.encryptions = encInOrder;
            // Fills missing entries
            for (String enc : encryptions.keySet()) {
                if (!statusMap.containsKey(enc))
                    statusMap.put(enc, false);
            }
            this.statusMap = statusMap;
        }

        /**
         * Creates new instance of {@link ProtocolsAdapter}
         *
         * @param encryptions
         * @param statusMap
         */
        ProtocolsAdapter(String[] encryptions, Map<String, Boolean> statusMap)
        {
            this.encryptions = encryptions;
            this.statusMap = statusMap;
        }

        public int getCount()
        {
            return encryptions.length;
        }

        public Object getItem(int i)
        {
            return encryptions[i];
        }

        public long getItemId(int i)
        {
            return i;
        }

        public View getView(int i, View view, ViewGroup viewGroup)
        {
            final String encryption = (String) getItem(i);

            LayoutInflater li = getActivity().getLayoutInflater();
            View v = li.inflate(R.layout.encoding_item, viewGroup, false);

            TextView tv = v.findViewById(android.R.id.text1);
            tv.setText(encryption);

            CheckBox cb = v.findViewById(android.R.id.checkbox);
            cb.setChecked(statusMap.containsKey(encryption) && statusMap.get(encryption));
            cb.setOnCheckedChangeListener((cb1, state) -> {
                statusMap.put(encryption, state);
                hasChanges = true;
            });

            return v;
        }

        /**
         * Implements {@link TouchInterceptor.DropListener}. Method swaps protocols priorities.
         *
         * @param from source item index
         * @param to destination item index
         */
        public void drop(int from, int to)
        {
            hasChanges = true;
            String swap = encryptions[to];
            encryptions[to] = encryptions[from];
            encryptions[from] = swap;

            getActivity().runOnUiThread(this::notifyDataSetChanged);
        }
    }
}
