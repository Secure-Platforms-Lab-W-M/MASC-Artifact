/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.configuration;

import java.beans.PropertyChangeEvent;

/**
 * A PropertyVetoException is thrown when a proposed change to a property represents an unacceptable value.
 *
 * @author Emil Ivov
 */
public class ConfigPropertyVetoException extends RuntimeException
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * A PropertyChangeEvent describing the vetoed change.
     */
    private final PropertyChangeEvent evt;

    /**
     * Constructs a <tt>PropertyVetoException</tt> with a detailed message.
     *
     * @param message Descriptive message
     * @param evt A PropertyChangeEvent describing the vetoed change.
     */
    public ConfigPropertyVetoException(String message, PropertyChangeEvent evt)
    {
        super(message);
        this.evt = evt;
    }

    /**
     * Gets the vetoed <tt>PropertyChangeEvent</tt>.
     *
     * @return A PropertyChangeEvent describing the vetoed change.
     */
    public PropertyChangeEvent getPropertyChangeEvent()
    {
        return evt;
    }
}
