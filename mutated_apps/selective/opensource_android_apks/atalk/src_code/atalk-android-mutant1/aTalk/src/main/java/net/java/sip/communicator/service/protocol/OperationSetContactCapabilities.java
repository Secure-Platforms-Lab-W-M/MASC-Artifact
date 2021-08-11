/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

import net.java.sip.communicator.service.protocol.event.ContactCapabilitiesListener;

import java.util.Map;

/**
 * Represents an <tt>OperationSet</tt> to query the <tt>OperationSet</tt>s supported for a specific
 * <tt>Contact</tt>. The <tt>OperationSet</tt>s reported as supported for a specific
 * <tt>Contact</tt> are considered by the associated protocol provider to be capabilities possessed
 * by the <tt>Contact</tt> in question.
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public interface OperationSetContactCapabilities extends OperationSet
{

    /**
     * Registers a specific <tt>ContactCapabilitiesListener</tt> to be notified about changes in the
     * list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s. If the specified
     * <tt>listener</tt> has already been registered, adding it again has no effect.
     *
     * @param listener the <tt>ContactCapabilitiesListener</tt> which is to be notified about changes in the
     * list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s
     */
    void addContactCapabilitiesListener(ContactCapabilitiesListener listener);

    /**
     * Gets the <tt>OperationSet</tt> corresponding to the specified <tt>Class</tt> and supported by
     * the specified <tt>Contact</tt>. If the returned value is non-<tt>null</tt>, it indicates that
     * the <tt>Contact</tt> is considered by the associated protocol provider to possess the
     * <tt>opsetClass</tt> capability. Otherwise, the associated protocol provider considers
     * <tt>contact</tt> to not have the <tt>opsetClass</tt> capability.
     *
     * @param <T> the type extending <tt>OperationSet</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @param contact the <tt>Contact</tt> for which the <tt>opsetClass</tt> capability is to be queried
     * @param opsetClass the <tt>OperationSet</tt> <tt>Class</tt> for which the specified <tt>contact</tt> is
     * to be checked whether it possesses it as a capability
     * @return the <tt>OperationSet</tt> corresponding to the specified <tt>opsetClass</tt> which is
     * considered by the associated protocol provider to be possessed as a capability by the
     * specified <tt>contact</tt>; otherwise, <tt>null</tt>
     */
    <T extends OperationSet> T getOperationSet(Contact contact, Class<T> opsetClass);

    /**
     * Gets the <tt>OperationSet</tt>s supported by a specific <tt>Contact</tt>. The returned
     * <tt>OperationSet</tt>s are considered by the associated protocol provider to capabilities
     * possessed by the specified <tt>contact</tt>.
     *
     * @param contact the <tt>Contact</tt> for which the supported <tt>OperationSet</tt> capabilities are to
     * be retrieved
     * @return a <tt>Map</tt> listing the <tt>OperationSet</tt>s considered by the associated
     * protocol provider to be supported by the specified <tt>contact</tt> (i.e. to be
     * possessed as capabilities). Each supported <tt>OperationSet</tt> capability is
     * represented by a <tt>Map.Entry</tt> with key equal to the <tt>OperationSet</tt> class
     * name and value equal to the respective <tt>OperationSet</tt> instance
     */
    Map<String, OperationSet> getSupportedOperationSets(Contact contact);

    /**
     * Unregisters a specific <tt>ContactCapabilitiesListener</tt> to no longer be notified about
     * changes in the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s. If the
     * specified <tt>listener</tt> has already been unregistered or has never been registered,
     * removing it has no effect.
     *
     * @param listener the <tt>ContactCapabilitiesListener</tt> which is to no longer be notified about
     * changes in the list of <tt>OperationSet</tt> capabilities of <tt>Contact</tt>s
     */
    void removeContactCapabilitiesListener(ContactCapabilitiesListener listener);
}
