/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.xmpp.extensions.coin;

/**
 * Endpoint status type.
 *
 * @author Sebastien Vincent
 */
public enum EndpointStatusType
{
    /**
     * Pending.
     */
    pending("pending"),

    /**
     * Dialing-out.
     */
    dialing_out("dialing-out"),

    /**
     * Dialing-in.
     */
    dialing_in("dialing-in"),

    /**
     * Alerting.
     */
    alerting("alerting"),

    /**
     * On-hold.
     */
    on_hold("on-hold"),

    /**
     * Connected.
     */
    connected("connected"),

    /**
     * Muted via focus.
     */
    muted_via_focus("mute-via-focus"),

    /**
     * Disconnecting.
     */
    disconnecting("disconnecting"),

    /**
     * Disconnected.
     */
    disconnected("disconnected");

    /**
     * The name of this type.
     */
    private final String type;

    /**
     * Creates a <tt>EndPointType</tt> instance with the specified name.
     *
     * @param type type name.
     */
    private EndpointStatusType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the type name.
     *
     * @return type name
     */
    @Override
    public String toString()
    {
        return type;
    }

    /**
     * Returns a <tt>EndPointType</tt>.
     *
     * @param typeStr the <tt>String</tt> that we'd like to parse.
     * @return an EndPointType.
     * @throws IllegalArgumentException in case <tt>typeStr</tt> is not a valid <tt>EndPointType</tt>.
     */
    public static EndpointStatusType fromString(String typeStr)
            throws IllegalArgumentException
    {
        for (EndpointStatusType value : values())
            if (value.toString().equals(typeStr))
                return value;

        throw new IllegalArgumentException(typeStr + " is not a valid reason");
    }
}
