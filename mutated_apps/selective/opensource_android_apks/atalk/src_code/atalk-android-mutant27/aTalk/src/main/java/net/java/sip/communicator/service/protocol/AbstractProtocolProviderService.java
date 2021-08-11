/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeEvent;
import net.java.sip.communicator.service.protocol.event.RegistrationStateChangeListener;

import org.atalk.android.plugin.timberlog.TimberLog;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import timber.log.Timber;

/**
 * Implements standard functionality of <tt>ProtocolProviderService</tt> in order to make it easier
 * for implementers to provide complete solutions while focusing on protocol-specific details.
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public abstract class AbstractProtocolProviderService implements ProtocolProviderService
{
    /**
     * A list of all listeners registered for <tt>RegistrationStateChangeEvent</tt>s.
     */
    private final List<RegistrationStateChangeListener> registrationListeners = new ArrayList<>();

    /**
     * The hash table with the operation sets that we support locally.
     */
    private final Map<String, OperationSet> supportedOperationSets = new Hashtable<>();

    /**
     * ProtocolProviderService state synchronization point for events from various threads
     */
    private final Lock loginInitLock = new ReentrantLock();

    /**
     * isResumed true if a previous XMPP session's stream was resumed.
     */
    protected boolean isResumed = false;


    /**
     * Registers the specified listener with this provider so that it would receive notifications on
     * changes of its state or other properties such as its local address and display name.
     *
     * @param listener the listener to register.
     */
    public void addRegistrationStateChangeListener(RegistrationStateChangeListener listener)
    {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        synchronized (registrationListeners) {
            if (!registrationListeners.contains(listener))
                registrationListeners.add(listener);
        }
    }

    /**
     * Adds a specific <tt>OperationSet</tt> implementation to the set of supported
     * <tt>OperationSet</tt>s of this instance. Serves as a type-safe wrapper around
     * {@link #supportedOperationSets} which works with class names instead of <tt>Class</tt> and
     * also shortens the code which performs such additions.
     *
     * @param <T> the exact type of the <tt>OperationSet</tt> implementation to be added
     * @param opsetClass the <tt>Class</tt> of <tt>OperationSet</tt> under the name of which the specified
     * implementation is to be added
     * @param opset the <tt>OperationSet</tt> implementation to be added
     */
    protected <T extends OperationSet> void addSupportedOperationSet(Class<T> opsetClass, T opset)
    {
        supportedOperationSets.put(opsetClass.getName(), opset);
    }

    /**
     * Removes an <tt>OperationSet</tt> implementation from the set of supported
     * <tt>OperationSet</tt>s for this instance.
     *
     * @param <T> the exact type of the <tt>OperationSet</tt> implementation to be added
     * @param opsetClass the <tt>Class</tt> of <tt>OperationSet</tt> under the name of which the specified
     * implementation is to be added
     */
    protected <T extends OperationSet> void removeSupportedOperationSet(Class<T> opsetClass)
    {
        supportedOperationSets.remove(opsetClass.getName());
    }

    /**
     * Removes all <tt>OperationSet</tt> implementation from the set of supported
     * <tt>OperationSet</tt>s for this instance.
     */
    protected void clearSupportedOperationSet()
    {
        supportedOperationSets.clear();
    }

    /**
     * Creates a RegistrationStateChange event corresponding to the specified old and new states
     * and
     * notifies all currently registered listeners.
     *
     * @param oldState the state that the provider had before the change occurred
     * @param newState the state that the provider is currently in.
     * @param reasonCode a value corresponding to one of the REASON_XXX fields of the
     * RegistrationStateChangeEvent class, indicating the reason for this state transition.
     * @param reason a String further explaining the reason code or null if no such explanation is necessary.
     */
    public void fireRegistrationStateChanged(RegistrationState oldState,
            RegistrationState newState, int reasonCode, String reason)
    {
        this.fireRegistrationStateChanged(oldState, newState, reasonCode, reason, false);
    }

    /**
     * Creates a RegistrationStateChange event corresponding to the specified old and new states
     * and notifies all currently registered listeners.
     *
     * @param oldState the state that the provider had before the change occurred
     * @param newState the state that the provider is currently in.
     * @param reasonCode a value corresponding to one of the REASON_XXX fields of the
     * RegistrationStateChangeEvent class, indicating the reason for this state transition.
     * @param reason a String further explaining the reason code or null if no such explanation is necessary.
     * @param userRequest is the event by user request.
     */
    public void fireRegistrationStateChanged(RegistrationState oldState,
            RegistrationState newState, int reasonCode, String reason, boolean userRequest)
    {
        // no change - throws exception to trace the root; otherwise too many unnecessary events
        if (newState == oldState) {
            String msg = "The provider state unchanged: " + newState + ". Reason: " + reason;
            (new Exception(msg)).printStackTrace();
        }
        Timber.d("The provider state changed: %s => %s. Reason: %s", oldState, newState, reason);

        RegistrationStateChangeEvent event
                = new RegistrationStateChangeEvent(this, oldState, newState, reasonCode, reason);
        event.setUserRequest(userRequest);
        RegistrationStateChangeListener[] listeners;
        synchronized (registrationListeners) {
            listeners = registrationListeners.toArray(new RegistrationStateChangeListener[0]);
        }
        Timber.log(TimberLog.FINER, "Dispatching %s to %s listeners.", event, listeners.length);

        for (RegistrationStateChangeListener listener : listeners)
            try {
                listener.registrationStateChanged(event);
            } catch (Throwable throwable) {
                /*
                 * The registration state has already changed and we're not using the
                 * RegistrationStateChangeListeners to veto the change so it doesn't make sense to,
                 * for example, disconnect because one of them malfunctioned.
                 *
                 * Of course, death cannot be ignored.
                 */
                if (throwable instanceof ThreadDeath)
                    throw (ThreadDeath) throwable;
                Timber.e(throwable, "Exception while sending registrationStateChanged event to: %s", listener);
            }
        Timber.log(TimberLog.FINER, "Done.");
    }

    /**
     * Returns the operation set corresponding to the specified class or null if this operation set
     * is not supported by the provider implementation.
     *
     * @param <T> the exact type of the <tt>OperationSet</tt> that we're looking for
     * @param opsetClass the <tt>Class</tt> of the operation set that we're looking for.
     * @return returns an <tt>OperationSet</tt> of the specified <tt>Class</tt> if the underlying
     * implementation supports it; <tt>null</tt>, otherwise.
     */
    @SuppressWarnings("unchecked")
    public <T extends OperationSet> T getOperationSet(Class<T> opsetClass)
    {
        return (T) supportedOperationSets.get(opsetClass.getName());
    }

    /**
     * Returns the protocol display name. This is the name that would be used by the GUI to display
     * the protocol name.
     *
     * @return a String containing the display name of the protocol this service is implementing
     */
    public String getProtocolDisplayName()
    {
        String displayName = getAccountID().getAccountPropertyString(ProtocolProviderFactory.PROTOCOL);
        return (displayName == null) ? getProtocolName() : displayName;
    }

    /**
     * Default implementation that always returns true.
     *
     * @param contactId ignored.
     * @param result ignored
     * @return true
     */
    public boolean validateContactAddress(String contactId, List<String> result)
    {
        return true;
    }

    /**
     * Returns an array containing all operation sets supported by the current implementation. When
     * querying this method users must be prepared to receive any subset of the OperationSet-s
     * defined by this service. They MUST ignore any OperationSet-s that they are not aware of and
     * that may be defined by future version of this service. Such "unknown" OperationSet-s though
     * not encouraged, may also be defined by service implementors.
     *
     * @return a java.util.Map containing instance of all supported operation sets mapped against
     * their class names (e.g. OperationSetPresence.class.getName()) .
     */
    public Map<String, OperationSet> getSupportedOperationSets()
    {
        return new Hashtable<>(supportedOperationSets);
    }

    /**
     * Returns a collection containing all operation sets classes supported by the current
     * implementation. When querying this method users must be prepared to receive any subset of
     * the OperationSet-s defined by this service. They MUST ignore any OperationSet-s that they are
     * not aware of and that may be defined by future versions of this service. Such "unknown"
     * OperationSet-s though not encouraged, may also be defined by service implementors.
     *
     * @return a {@link Collection} containing instances of all supported operation set classes
     * (e.g. <tt>OperationSetPresence.class</tt>.
     */
    @SuppressWarnings("unchecked")
    public Collection<Class<? extends OperationSet>> getSupportedOperationSetClasses()
    {
        Collection<Class<? extends OperationSet>> opSetClasses = new ArrayList<>();
        for (String opSetClassName : getSupportedOperationSets().keySet()) {
            try {
                opSetClasses.add((Class<? extends OperationSet>) getSupportedOperationSets()
                        .get(opSetClassName).getClass().getClassLoader().loadClass(opSetClassName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return opSetClasses;
    }

    /**
     * Indicates whether or not the previous XMPP session's stream was resumed.
     *
     * @return <tt>true</tt> if the previous XMPP session's stream was resumed and <tt>false</tt> otherwise.
     */
    public boolean isResumed()
    {
        return isResumed;
    }

    /**
     * Indicates whether or not this provider is registered
     *
     * @return <tt>true</tt> if the provider is currently registered and <tt>false</tt> otherwise.
     */
    public boolean isRegistered()
    {
        return getRegistrationState().equals(RegistrationState.REGISTERED);
    }

    /**
     * Indicates whether or not this provider must registered when placing outgoing calls.
     *
     * @return <tt>true</tt> if the provider must be registered when placing a call and
     * <tt>false</tt> otherwise.
     */
    public boolean isRegistrationRequiredForCalling()
    {
        return true;
    }

    /**
     * Removes the specified registration state change listener so that it does not receive any
     * further notifications upon changes of the RegistrationState of this provider.
     *
     * @param listener the listener to register for <tt>RegistrationStateChangeEvent</tt>s.
     */
    public void removeRegistrationStateChangeListener(RegistrationStateChangeListener listener)
    {
        synchronized (registrationListeners) {
            registrationListeners.remove(listener);
        }
    }

    /**
     * Clear all registration state change listeners.
     */
    public void clearRegistrationStateChangeListener()
    {
        synchronized (registrationListeners) {
            registrationListeners.clear();
        }
    }

    /**
     * A clear display for ProtocolProvider when its printed in logs.
     *
     * @return the class name and the currently handled account.
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(" + getAccountID() + ")";
    }

    /**
     * Ends the registration of this protocol provider with the current registration service. The
     * default is just to call unregister. Providers that need to differentiate user requests (from
     * the UI) or automatic unregister can override this method.
     *
     * @param userRequest is the unregister by user request.
     * @throws OperationFailedException with the corresponding code it the registration fails for some reason
     * (e.g. a networking error or an implementation problem).
     */
    public void unregister(boolean userRequest)
            throws OperationFailedException
    {
        this.unregister();
    }

    /**
     * Return the lock of pps state
     *
     * @return the Lock for the current PPS synchronization point
     */
    public Lock getLoginInitLock()
    {
        return loginInitLock;
    }
}
