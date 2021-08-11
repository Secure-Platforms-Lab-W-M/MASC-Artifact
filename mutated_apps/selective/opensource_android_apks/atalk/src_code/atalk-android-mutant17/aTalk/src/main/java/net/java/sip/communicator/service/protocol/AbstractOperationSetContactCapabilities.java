/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.ContactCapabilitiesEvent;
import net.java.sip.communicator.service.protocol.event.ContactCapabilitiesListener;

import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;

import java.util.*;

import timber.log.Timber;

/**
 * Represents a default implementation of <tt>OperationSetContactCapabilities</tt> which attempts to
 * make it easier for implementers to provide complete solutions while focusing on
 * implementation-specific functionality.
 *
 * @param <T> the type of the <tt>ProtocolProviderService</tt> implementation providing the
 * <tt>AbstractOperationSetContactCapabilities</tt> implementation
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public abstract class AbstractOperationSetContactCapabilities<T extends ProtocolProviderService>
        implements OperationSetContactCapabilities
{
    /**
     * The list of <tt>ContactCapabilitiesListener</tt>s registered to be notified about changes in
     * the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s.
     */
    private final List<ContactCapabilitiesListener> contactCapabilitiesListeners = new LinkedList<>();

    /**
     * The <tt>ProtocolProviderService</tt> which provides this <tt>OperationSetContactCapabilities</tt>.
     */
    protected final T parentProvider;

    /**
     * Initializes a new <tt>AbstractOperationSetContactCapabilities</tt> instance which is to be
     * provided by a specific <tt>ProtocolProviderService</tt> implementation.
     *
     * @param parentProvider the <tt>ProtocolProviderService</tt> implementation which will provide the new instance
     */
    protected AbstractOperationSetContactCapabilities(T parentProvider)
    {
        if (parentProvider == null)
            throw new NullPointerException("parentProvider");

        this.parentProvider = parentProvider;
    }

    /**
     * Registers a specific <tt>ContactCapabilitiesListener</tt> to be notified about changes in
     * the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s. If the specified
     * <tt>listener</tt> has already been registered, adding it again has no effect.
     *
     * @param listener the <tt>ContactCapabilitiesListener</tt> which is to be notified about changes in the
     * list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s
     * @see OperationSetContactCapabilities#addContactCapabilitiesListener (ContactCapabilitiesListener)
     */
    public void addContactCapabilitiesListener(ContactCapabilitiesListener listener)
    {
        if (listener == null)
            throw new NullPointerException("listener");

        synchronized (contactCapabilitiesListeners) {
            if (!contactCapabilitiesListeners.contains(listener))
                contactCapabilitiesListeners.add(listener);
        }
    }

    /**
     * Fires a new <tt>ContactCapabilitiesEvent</tt> to notify the registered <tt>ContactCapabilitiesListener</tt>s
     * that a specific <tt>Contact</tt> has changed its list of <tt>OperationSet</tt> capabilities.
     *
     * @param sourceContact the <tt>Contact</tt> which is the source/cause of the event to be fired
     * @param jid the contact fullJid
     * @param opSets the new operation sets for the given source contact
     */
    protected void fireContactCapabilitiesEvent(Contact sourceContact, Jid jid, Map<String, ? extends OperationSet> opSets)
    {
        ContactCapabilitiesListener[] listeners;
        synchronized (contactCapabilitiesListeners) {
            listeners = contactCapabilitiesListeners.toArray(new ContactCapabilitiesListener[0]);
        }
        if (listeners.length != 0) {
            ContactCapabilitiesEvent event = new ContactCapabilitiesEvent(sourceContact, jid, opSets);

            for (ContactCapabilitiesListener listener : listeners) {
                if (jid != null) {
                    listener.supportedOperationSetsChanged(event);
                }
                else {
                    Timber.w(new IllegalArgumentException("Cannot fire ContactCapabilitiesEvent with Jid: " + jid));
                }
            }
        }
    }

    /**
     * Gets the <tt>OperationSet</tt> corresponding to the specified <tt>Class</tt> and
     * supported by the specified <tt>Contact</tt>. If the returned value is non-<tt>null</tt>,
     * it indicates  that the <tt>Contact</tt> is considered by the associated protocol provider
     * to possess the <tt>opsetClass</tt> capability. Otherwise, the associated protocol provider
     * considers <tt>contact</tt> to not have the <tt>opsetClass</tt> capability.
     * <tt>AbstractOperationSetContactCapabilities</tt> looks for the name of the specified
     * <tt>opsetClass</tt> in the <tt>Map</tt> returned by
     * {@link #getSupportedOperationSets(Contact)} and returns the associated <tt>OperationSet</tt>.
     * Since the implementation is suboptimal due to the temporary <tt>Map</tt> allocations and
     * lookups, extenders are advised to override
     * {@link #getOperationSet(Contact, Class, boolean)}.
     *
     * @param <U> the type extending <tt>OperationSet</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @param contact the <tt>Contact</tt> for which the <tt>opsetClass</tt> capability is to be queried
     * @param opsetClass the <tt>OperationSet</tt> <tt>Class</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @return the <tt>OperationSet</tt> corresponding to the specified <tt>opsetClass</tt>
     * which is considered by the associated protocol provider to be possessed as a capability by
     * the specified <tt>contact</tt>; otherwise, <tt>null</tt>
     * @see OperationSetContactCapabilities#getOperationSet(Contact, Class)
     */
    public <U extends OperationSet> U getOperationSet(Contact contact, Class<U> opsetClass)
    {
        return getOperationSet(contact, opsetClass, isOnline(contact));
    }

    /**
     * Gets the <tt>OperationSet</tt> corresponding to the specified <tt>Class</tt> and
     * supported by the specified <tt>Contact</tt>. If the returned value is non-<tt>null</tt>,
     * it indicates that the <tt>Contact</tt> is considered by the associated protocol provider
     * to possess the <tt>opsetClass</tt> capability. Otherwise, the associated protocol provider
     * considers <tt>contact</tt> to not have the <tt>opsetClass</tt> capability.
     * <tt>AbstractOperationSetContactCapabilities</tt> looks for the name of the specified
     * <tt>opsetClass</tt> in the <tt>Map</tt> returned by
     * {@link #getSupportedOperationSets(Contact)} and returns the associated <tt>OperationSet</tt>.
     * Since the implementation is suboptimal due to the temporary <tt>Map</tt> allocations and
     * lookups, extenders are advised to override.
     *
     * @param <U> the type extending <tt>OperationSet</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @param contact the <tt>Contact</tt> for which the <tt>opsetClass</tt> capability is to be queried
     * @param opsetClass the <tt>OperationSet</tt> <tt>Class</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @param online <tt>true</tt> if <tt>contact</tt> is online; otherwise, <tt>false</tt>
     * @return the <tt>OperationSet</tt> corresponding to the specified <tt>opsetClass</tt>
     * which is considered by the associated protocol provider to be possessed as a capability by
     * the specified <tt>contact</tt>; otherwise, <tt>null</tt>
     * @see OperationSetContactCapabilities#getOperationSet(Contact, Class)
     */
    @SuppressWarnings("unchecked")
    protected <U extends OperationSet> U getOperationSet(Contact contact, Class<U> opsetClass, boolean online)
    {
        Map<String, OperationSet> supportedOperationSets = getSupportedOperationSets(contact, online);

        if (supportedOperationSets != null) {
            OperationSet opset = supportedOperationSets.get(opsetClass.getName());

            if (opsetClass.isInstance(opset))
                return (U) opset;
        }
        return null;
    }

    /**
     * Gets the <tt>OperationSet</tt>s supported by a specific <tt>Contact</tt>. The returned
     * <tt>OperationSet</tt>s are considered by the associated protocol provider to capabilities
     * possessed by the specified <tt>contact</tt>. The default implementation returns the
     * result of calling {@link ProtocolProviderService#getSupportedOperationSets()} on the
     * associated <tt>ProtocolProviderService</tt> implementation. Extenders have to override the
     * default implementation of {@link #getSupportedOperationSets(Contact, boolean)} in order to
     * provide actual capability detection for the specified <tt>contact</tt>.
     *
     * @param contact the <tt>Contact</tt> for which the supported <tt>OperationSet</tt> capabilities are to be retrieved
     * @return a <tt>Map</tt> listing the <tt>OperationSet</tt>s considered by the associated
     * protocol provider to be supported by the specified <tt>contact</tt> (i.e. to be
     * possessed as capabilities). Each supported <tt>OperationSet</tt> capability is
     * represented by a <tt>Map.Entry</tt> with key equal to the <tt>OperationSet</tt> class
     * name and value equal to the respective <tt>OperationSet</tt> instance
     * @see OperationSetContactCapabilities#getSupportedOperationSets(Contact)
     */
    public Map<String, OperationSet> getSupportedOperationSets(Contact contact)
    {
        return getSupportedOperationSets(contact, isOnline(contact));
    }

    /**
     * Gets the <tt>OperationSet</tt>s supported by a specific <tt>Contact</tt>. The returned
     * <tt>OperationSet</tt>s are considered by the associated protocol provider to capabilities
     * possessed by the specified <tt>contact</tt>. The default implementation returns the
     * result of calling {@link ProtocolProviderService#getSupportedOperationSets()} on the
     * associated <tt>ProtocolProviderService</tt> implementation. Extenders have to override the
     * default implementation in order to provide actual capability detection for the specified <tt>contact</tt>.
     *
     * @param contact the <tt>Contact</tt> for which the supported <tt>OperationSet</tt> capabilities are to
     * be retrieved
     * @param online <tt>true</tt> if <tt>contact</tt> is online; otherwise, <tt>false</tt>
     * @return a <tt>Map</tt> listing the <tt>OperationSet</tt>s considered by the associated
     * protocol provider to be supported by the specified <tt>contact</tt> (i.e. to be
     * possessed as capabilities). Each supported <tt>OperationSet</tt> capability is
     * represented by a <tt>Map.Entry</tt> with key equal to the <tt>OperationSet</tt> class
     * name and value equal to the respective <tt>OperationSet</tt> instance
     * @see OperationSetContactCapabilities#getSupportedOperationSets(Contact)
     */
    protected Map<String, OperationSet> getSupportedOperationSets(Contact contact, boolean online)
    {
        return parentProvider.getSupportedOperationSets();
    }

    /**
     * Determines whether a specific <tt>Contact</tt> is online (in contrast to offline).
     *
     * @param contact the <tt>Contact</tt> which is to be determines whether it is online
     * @return <tt>true</tt> if the specified <tt>contact</tt> is online; otherwise, <tt>false</tt>
     */
    protected boolean isOnline(Contact contact)
    {
        OperationSetPresence opsetPresence = parentProvider.getOperationSet(OperationSetPresence.class);

        if (opsetPresence == null) {
            /*
             * Presence is not implemented so we cannot really know and thus we'll give it the
             * benefit of the doubt and declare it online.
             */
            return true;
        }
        else {
            PresenceStatus presenceStatus = null;
            Throwable exception = null;

            try {
                presenceStatus = opsetPresence.queryContactStatus(contact.getJid().asBareJid());
            } catch (IllegalArgumentException | IllegalStateException | OperationFailedException iaex) {
                exception = iaex;
            }

            if (presenceStatus == null)
                presenceStatus = contact.getPresenceStatus();

            if (presenceStatus == null) {
                if ((exception != null)) {
                    Timber.d(exception, "Failed to query PresenceStatus of Contact %s", contact);
                }
                /*
                 * For whatever reason the PresenceStatus wasn't retrieved, it's a fact that
                 * presence was advertised and the contacts wasn't reported online.
                 */
                return false;
            }
            else
                return presenceStatus.isOnline();
        }
    }

    /**
     * Unregisters a specific <tt>ContactCapabilitiesListener</tt> to no longer be notified about
     * changes in the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s. If the specified
     * <tt>listener</tt> has already been unregistered or has never been registered, removing it has no effect.
     *
     * @param listener the <tt>ContactCapabilitiesListener</tt> which is to no longer be notified about
     * changes in the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s
     * @see OperationSetContactCapabilities#removeContactCapabilitiesListener (ContactCapabilitiesListener)
     */
    public void removeContactCapabilitiesListener(ContactCapabilitiesListener listener)
    {
        if (listener != null) {
            synchronized (contactCapabilitiesListeners) {
                contactCapabilitiesListeners.remove(listener);
            }
        }
    }
}
