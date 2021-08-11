/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.gui;

import net.java.sip.communicator.service.contactsource.*;
import net.java.sip.communicator.service.gui.event.ContactListListener;
import net.java.sip.communicator.service.gui.event.MetaContactQueryListener;

import org.atalk.android.util.java.awt.Component;

import java.util.Collection;
import java.util.List;

/**
 * The <tt>ContactList</tt> interface represents a contact list. All contact
 * list components that need to be available as a service could implement this interface.
 *
 * @author Yana Stamcheva
 */
public interface ContactList extends ContactQueryListener, MetaContactQueryListener
{
    /**
     * Returns the actual component corresponding to the contact list.
     *
     * @return the actual component corresponding to the contact list
     */
    Component getComponent();

    /**
     * Returns the list of registered contact sources to search in.
     *
     * @return the list of registered contact sources to search in
     */
    Collection<UIContactSource> getContactSources();

    /**
     * Returns the <tt>ExternalContactSource</tt> corresponding to the given <tt>ContactSourceService</tt>.
     *
     * @param contactSource the <tt>ContactSourceService</tt>, which
     * corresponding external source implementation we're looking for
     * @return the <tt>ExternalContactSource</tt> corresponding to the given <tt>ContactSourceService</tt>
     */
    UIContactSource getContactSource(ContactSourceService contactSource);

    /**
     * Adds the given contact source to the list of available contact sources.
     *
     * @param contactSource the <tt>ContactSourceService</tt>
     */
    void addContactSource(ContactSourceService contactSource);

    /**
     * Removes the given contact source from the list of available contact sources.
     *
     * @param contactSource
     */
    void removeContactSource(ContactSourceService contactSource);

    /**
     * Removes all stored contact sources.
     */
    void removeAllContactSources();

    /**
     * Sets the default filter to the given <tt>filter</tt>.
     *
     * @param filter the <tt>ContactListFilter</tt> to set as default
     */
    void setDefaultFilter(ContactListFilter filter);

    /**
     * Gets the default filter for this contact list.
     *
     * @return the default filter for this contact list
     */
    ContactListFilter getDefaultFilter();

    /**
     * Returns all <tt>UIContactSource</tt>s of the given type.
     *
     * @param type the type of sources we're looking for
     * @return a list of all <tt>UIContactSource</tt>s of the given type
     */
    List<UIContactSource> getContactSources(int type);

    /**
     * Adds the given group to this list.
     *
     * @param group the <tt>UIGroup</tt> to add
     * @param isSorted indicates if the contact should be sorted regarding to the <tt>GroupNode</tt> policy
     */
    void addGroup(final UIGroup group, final boolean isSorted);

    /**
     * Removes the given group and its children from the list.
     *
     * @param group the <tt>UIGroup</tt> to remove
     */
    void removeGroup(final UIGroup group);

    /**
     * Adds the given <tt>contact</tt> to this list.
     *
     * @param contact the <tt>UIContact</tt> to add
     * @param group the <tt>UIGroup</tt> to add to
     * @param isContactSorted indicates if the contact should be sorted
     * regarding to the <tt>GroupNode</tt> policy
     * @param isGroupSorted indicates if the group should be sorted regarding to
     * the <tt>GroupNode</tt> policy in case it doesn't exist and should be dded
     */
    void addContact(final UIContact contact,
            final UIGroup group,
            final boolean isContactSorted,
            final boolean isGroupSorted);

    /**
     * Adds the given <tt>contact</tt> to this list.
     *
     * @param query the <tt>ContactQuery</tt> that adds the given contact
     * @param contact the <tt>UIContact</tt> to add
     * @param group the <tt>UIGroup</tt> to add to
     * @param isSorted indicates if the contact should be sorted regarding to
     * the <tt>GroupNode</tt> policy
     */
    void addContact(final ContactQuery query,
            final UIContact contact,
            final UIGroup group,
            final boolean isSorted);

    /**
     * Removes the node corresponding to the given <tt>MetaContact</tt> from this list.
     *
     * @param contact the <tt>UIContact</tt> to remove
     * @param removeEmptyGroup whether we should delete the group if is empty
     */
    void removeContact(final UIContact contact,
            final boolean removeEmptyGroup);

    /**
     * Removes the node corresponding to the given <tt>MetaContact</tt> from
     * this list.
     *
     * @param contact the <tt>UIContact</tt> to remove
     */
    void removeContact(UIContact contact);

    /**
     * Removes all entries in this contact list.
     */
    void removeAll();

    /**
     * Returns a collection of all direct child <tt>UIContact</tt>s of the given
     * <tt>UIGroup</tt>.
     *
     * @param group the parent <tt>UIGroup</tt>
     * @return a collection of all direct child <tt>UIContact</tt>s of the given <tt>UIGroup</tt>
     */
    Collection<UIContact> getContacts(final UIGroup group);

    /**
     * Returns the currently applied filter.
     *
     * @return the currently applied filter
     */
    ContactListFilter getCurrentFilter();

    /**
     * Returns the currently applied filter.
     *
     * @return the currently applied filter
     */
    FilterQuery getCurrentFilterQuery();

    /**
     * Applies the given <tt>filter</tt>.
     *
     * @param filter the <tt>ContactListFilter</tt> to apply.
     * @return the filter query
     */
    FilterQuery applyFilter(ContactListFilter filter);

    /**
     * Applies the default filter.
     *
     * @return the filter query that keeps track of the filtering results
     */
    FilterQuery applyDefaultFilter();

    /**
     * Returns the currently selected <tt>UIContact</tt>. In case of a multiple
     * selection returns the first contact in the selection.
     *
     * @return the currently selected <tt>UIContact</tt> if there's one.
     */
    UIContact getSelectedContact();

    /**
     * Returns the list of selected contacts.
     *
     * @return the list of selected contacts
     */
    List<UIContact> getSelectedContacts();

    /**
     * Returns the currently selected <tt>UIGroup</tt> if there's one.
     *
     * @return the currently selected <tt>UIGroup</tt> if there's one.
     */
    UIGroup getSelectedGroup();

    /**
     * Selects the given <tt>UIContact</tt> in the contact list.
     *
     * @param uiContact the contact to select
     */
    void setSelectedContact(UIContact uiContact);

    /**
     * Selects the given <tt>UIGroup</tt> in the contact list.
     *
     * @param uiGroup the group to select
     */
    void setSelectedGroup(UIGroup uiGroup);

    /**
     * Selects the first found contact node from the beginning of the contact list.
     */
    void selectFirstContact();

    /**
     * Removes the current selection.
     */
    void removeSelection();

    /**
     * Adds a listener for <tt>ContactListEvent</tt>s.
     *
     * @param listener the listener to add
     */
    void addContactListListener(ContactListListener listener);

    /**
     * Removes a listener previously added with <tt>addContactListListener</tt>.
     *
     * @param listener the listener to remove
     */
    void removeContactListListener(ContactListListener listener);

    /**
     * Refreshes the given <tt>UIContact</tt>.
     *
     * @param uiContact the contact to refresh
     */
    void refreshContact(UIContact uiContact);

    /**
     * Indicates if this contact list is empty.
     *
     * @return <tt>true</tt> if this contact list contains no children, otherwise returns <tt>false</tt>
     */
    boolean isEmpty();

    /**
     * Shows/hides buttons shown in contact row.
     *
     * @param isVisible <tt>true</tt> to show contact buttons, <tt>false</tt> - otherwise.
     */
    void setContactButtonsVisible(boolean isVisible);

    /**
     * Shows/hides buttons shown in contact row.
     *
     * return <tt>true</tt> to indicate that contact buttons are shown,
     * <tt>false</tt> - otherwise.
     */
    boolean isContactButtonsVisible();

    /**
     * Enables/disables multiple selection.
     *
     * @param isEnabled <tt>true</tt> to enable multiple selection,
     * <tt>false</tt> - otherwise
     */
    void setMultipleSelectionEnabled(boolean isEnabled);

    /**
     * Enables/disables drag operations on this contact list.
     *
     * @param isEnabled <tt>true</tt> to enable drag operations, <tt>false</tt> otherwise
     */
    void setDragEnabled(boolean isEnabled);

    /**
     * Enables/disables the right mouse click menu.
     *
     * @param isEnabled <tt>true</tt> to enable right button menu, <tt>false</tt> otherwise.
     */
    void setRightButtonMenuEnabled(boolean isEnabled);
}
