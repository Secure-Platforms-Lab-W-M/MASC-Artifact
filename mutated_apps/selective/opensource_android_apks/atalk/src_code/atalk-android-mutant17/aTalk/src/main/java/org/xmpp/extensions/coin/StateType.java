/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.xmpp.extensions.coin;

/**
 * Status type.
 *
 * @author Sebastien Vincent
 * @author Eng Chong Meng
 */
public enum StateType
{
    /**
     * Full state.
     */
    full,

    /**
     * Partial state.
     */
    partial,

    /**
     * Deleted state.
     */
    deleted;

    /**
     * Returns a <tt>StateType</tt>.
     *
     * @param typeStr the <tt>String</tt> that we'd like to parse.
     * @return a StateType.
     * @throws IllegalArgumentException in case <tt>typeStr</tt> is not a valid <tt>EndPointType</tt>.
     */
    public static StateType fromString(String typeStr)
            throws IllegalArgumentException
    {
        for (StateType value : values()) {
            if (value.toString().equals(typeStr))
                return value;
        }
        throw new IllegalArgumentException(typeStr + " is not a valid reason");
    }
}
