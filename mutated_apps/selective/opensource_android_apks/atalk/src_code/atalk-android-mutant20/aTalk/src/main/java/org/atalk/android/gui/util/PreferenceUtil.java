/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.util;

import android.preference.*;

/**
 * Utility class exposing methods to operate on <tt>Preference</tt> subclasses.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class PreferenceUtil
{
    /**
     * Sets the <tt>CheckBoxPreference</tt> "checked" property.
     *
     * @param screen the <tt>PreferenceScreen</tt> containing the <tt>CheckBoxPreference</tt> we want to edit.
     * @param prefKey preference key id from <tt>R.string</tt>.
     * @param isChecked the value we want to set to the "checked" property of <tt>CheckBoxPreference</tt>.
     */
    static public void setCheckboxVal(PreferenceScreen screen, String prefKey, boolean isChecked)
    {
        CheckBoxPreference cbPref = (CheckBoxPreference) screen.findPreference(prefKey);
        cbPref.setChecked(isChecked);
    }

    /**
     * Sets the text of <tt>EditTextPreference</tt> identified by given preference key string.
     *
     * @param screen the <tt>PreferenceScreen</tt> containing the <tt>EditTextPreference</tt> we want to edit.
     * @param prefKey preference key id from <tt>R.string</tt>.
     * @param txtValue the text value we want to set on <tt>EditTextPreference</tt>
     */
    public static void setEditTextVal(PreferenceScreen screen, String prefKey, String txtValue)
    {
        EditTextPreference cbPref = (EditTextPreference) screen.findPreference(prefKey);
        cbPref.setText(txtValue);
    }

    /**
     * Sets the value of <tt>ListPreference</tt> identified by given preference key string.
     *
     * @param screen the <tt>PreferenceScreen</tt> containing the <tt>ListPreference</tt> we want to edit.
     * @param prefKey preference key id from <tt>R.string</tt>.
     * @param value the value we want to set on <tt>ListPreference</tt>
     */
    public static void setListVal(PreferenceScreen screen, String prefKey, String value)
    {
        ListPreference lstPref = (ListPreference) screen.findPreference(prefKey);
        lstPref.setValue(value);
    }
}
