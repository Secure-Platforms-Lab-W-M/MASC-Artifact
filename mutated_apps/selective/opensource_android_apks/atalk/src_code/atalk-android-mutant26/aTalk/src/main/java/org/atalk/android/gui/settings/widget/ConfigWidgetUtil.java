/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputType;
import android.util.AttributeSet;

import org.atalk.android.R;
import org.atalk.android.gui.AndroidGUIActivator;
import org.atalk.service.configuration.ConfigurationService;

/**
 * Class that handles common attributes and operations for all configuration widgets.
 *
 * @author Pawel Domas
 */
class ConfigWidgetUtil
{
    /**
     * The parent <tt>Preference</tt> handled by this instance.
     */
    private final Preference parent;

    /**
     * Flag indicates whether configuration property should be stored in separate thread to prevent network on main
     * thread exceptions.
     */
    private boolean useNewThread;

    /**
     * Flag indicates whether value should be mapped to the summary.
     */
    private boolean mapSummary;

    /**
     * Creates new instance of <tt>ConfigWidgetUtil</tt> for given <tt>parent</tt> <tt>Preference</tt>.
     *
     * @param parent the <tt>Preference</tt> that will be handled by this instance.
     */
    ConfigWidgetUtil(Preference parent)
    {
        this.parent = parent;
    }

    /**
     * Creates new instance of <tt>ConfigWidgetUtil</tt> for given <tt>parent</tt> <tt>Preference</tt>.
     *
     * @param parent the <tt>Preference</tt> that will be handled by this instance.
     * @param mapSummary indicates whether value should be displayed as a summary
     */
    ConfigWidgetUtil(Preference parent, boolean mapSummary)
    {
        this.parent = parent;
        this.mapSummary = true;
    }

    /**
     * PArses the attributes. Should be called by parent <tt>Preference</tt>.
     *
     * @param context the Android context
     * @param attrs the attribute set
     */
    void parseAttributes(Context context, AttributeSet attrs)
    {
        TypedArray attArray = context.obtainStyledAttributes(attrs, R.styleable.ConfigWidget);
        useNewThread = attArray.getBoolean(R.styleable.ConfigWidget_storeInNewThread, false);
        mapSummary = attArray.getBoolean(R.styleable.ConfigWidget_mapSummary, mapSummary);
    }

    /**
     * Updates the summary if necessary. Should be called by parent <tt>Preference</tt> on value initialization.
     *
     * @param value the current value
     */
    void updateSummary(Object value)
    {
        if (mapSummary) {
            String text = (value != null) ? value.toString() : "";
            if (parent instanceof EditTextPreference) {
                int inputType = ((EditTextPreference) parent).getEditText().getInputType();

                if ((inputType & InputType.TYPE_MASK_VARIATION) == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    text = text.replaceAll("(?s).", "*");
                }
            }
            parent.setSummary(text);
        }
    }

    /**
     * Persists new value through the <tt>getConfigurationService</tt>.
     *
     * @param value the new value to persist.
     */
    void handlePersistValue(final Object value)
    {
        updateSummary(value);
        Thread store = new Thread()
        {
            @Override
            public void run()
            {
                ConfigurationService confService = AndroidGUIActivator.getConfigurationService();
                if (confService != null)
                    confService.setProperty(parent.getKey(), value);
            }
        };

        if (useNewThread)
            store.start();
        else
            store.run();
    }
}
