/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.neomedia;

import org.atalk.service.configuration.ConfigurationService;
import org.atalk.service.libjitsi.LibJitsi;

import java.net.*;

import timber.log.Timber;

/**
 * Represents a default implementation of <tt>StreamConnector</tt> which is initialized with a
 * specific pair of control and data <tt>DatagramSocket</tt>s and which closes them (if they exist)
 * when its {@link #close()} is invoked.
 *
 * @author Lubomir Marinov
 * @author Eng Chong Meng
 */
public class DefaultStreamConnector implements StreamConnector
{
    /**
     * The default number of binds that a Media Service Implementation should execute in case a port
     * is already bound to (each retry would be on a new random port).
     */
    public static final int BIND_RETRIES_DEFAULT_VALUE = 50;

    /**
     * The name of the property containing the number of binds that a Media Service Implementation
     * should execute in case a port is already bound to (each retry would be on a new port in the
     * allowed boundaries).
     */
    public static final String BIND_RETRIES_PROPERTY_NAME = "media.BIND_RETRIES";

    /**
     * The name of the property that contains the maximum port number that we'd like our RTP managers to bind upon.
     */
    public static final String MAX_PORT_NUMBER_PROPERTY_NAME = "media.MAX_PORT_NUMBER";

    /**
     * The maximum port number <tt>DefaultStreamConnector</tt> instances are to attempt to bind to.
     */
    private static int maxPort = -1;

    /**
     * The name of the property that contains the minimum port number that we'd like our RTP managers to bind upon.
     */
    public static final String MIN_PORT_NUMBER_PROPERTY_NAME = "media.MIN_PORT_NUMBER";

    /**
     * The minimum port number <tt>DefaultStreamConnector</tt> instances are to attempt to bind to.
     */
    private static int minPort = -1;

    /**
     * Creates a new <tt>DatagramSocket</tt> instance which is bound to the specified local
     * <tt>InetAddress</tt> and its port is within the range defined by the
     * <tt>ConfigurationService</tt> properties {@link #MIN_PORT_NUMBER_PROPERTY_NAME} and
     * {@link #MAX_PORT_NUMBER_PROPERTY_NAME}. Attempts at most {@link #BIND_RETRIES_PROPERTY_NAME}
     * times to bind.
     *
     * @param bindAddr the local <tt>InetAddress</tt> the new <tt>DatagramSocket</tt> is to bind to
     * @return a new <tt>DatagramSocket</tt> instance bound to the specified local <tt>InetAddress</tt>
     */
    private static synchronized DatagramSocket createDatagramSocket(InetAddress bindAddr)
    {
        ConfigurationService cfg = LibJitsi.getConfigurationService();
        int bindRetries = BIND_RETRIES_DEFAULT_VALUE;

        if (cfg != null)
            bindRetries = cfg.getInt(BIND_RETRIES_PROPERTY_NAME, bindRetries);
        if (maxPort < 0) {
            maxPort = 6000;
            if (cfg != null)
                maxPort = cfg.getInt(MAX_PORT_NUMBER_PROPERTY_NAME, maxPort);
        }

        for (int i = 0; i < bindRetries; i++) {
            if ((minPort < 0) || (minPort > maxPort)) {
                minPort = 5000;
                if (cfg != null) {
                    minPort = cfg.getInt(MIN_PORT_NUMBER_PROPERTY_NAME, minPort);
                }
            }

            int port = minPort++;

            try {
                return (bindAddr == null) ? new DatagramSocket(port) : new DatagramSocket(port, bindAddr);
            } catch (SocketException se) {
                Timber.w(se, "Retrying a bind because of a failure to bind to address %s and port %d", bindAddr, port);
            }
        }
        return null;
    }

    /**
     * The local <tt>InetAddress</tt> this <tt>StreamConnector</tt> attempts to bind to on demand.
     */
    private final InetAddress bindAddr;

    /**
     * The <tt>DatagramSocket</tt> that a stream should use for control data (e.g. RTCP) traffic.
     */
    protected DatagramSocket controlSocket;

    /**
     * The <tt>DatagramSocket</tt> that a stream should use for data (e.g. RTP) traffic.
     */
    protected DatagramSocket dataSocket;

    /**
     * Whether this <tt>DefaultStreamConnector</tt> uses rtcp-mux.
     */
    protected boolean rtcpmux = false;

    /**
     * Initializes a new <tt>DefaultStreamConnector</tt> instance with no control and data <tt>DatagramSocket</tt>s.
     * <p>
     * Suitable for extenders willing to delay the creation of the control and data sockets. For
     * example, they could override {@link #getControlSocket()} and/or {@link #getDataSocket()} and
     * create them on demand.
     */
    public DefaultStreamConnector()
    {
        this(null, null);
    }

    /**
     * Initializes a new <tt>DefaultStreamConnector</tt> instance with a specific bind
     * <tt>InetAddress</tt>. The new instance is to attempt to bind on demand to the specified
     * <tt>InetAddress</tt> in the port range defined by the <tt>ConfigurationService</tt>
     * properties {@link #MIN_PORT_NUMBER_PROPERTY_NAME} and {@link #MAX_PORT_NUMBER_PROPERTY_NAME}
     * at most {@link #BIND_RETRIES_PROPERTY_NAME} times.
     *
     * @param bindAddr the local <tt>InetAddress</tt> the new instance is to attempt to bind to
     */
    public DefaultStreamConnector(InetAddress bindAddr)
    {
        this.bindAddr = bindAddr;
    }

    /**
     * Initializes a new <tt>DefaultStreamConnector</tt> instance which is to represent a specific
     * pair of control and data <tt>DatagramSocket</tt>s.
     *
     * @param dataSocket the <tt>DatagramSocket</tt> to be used for data (e.g. RTP) traffic
     * @param controlSocket the <tt>DatagramSocket</tt> to be used for control data (e.g. RTCP) traffic
     */
    public DefaultStreamConnector(DatagramSocket dataSocket, DatagramSocket controlSocket)
    {
        this(dataSocket, controlSocket, false);
    }

    /**
     * Initializes a new <tt>DefaultStreamConnector</tt> instance which is to represent a specific
     * pair of control and data <tt>DatagramSocket</tt>s.
     *
     * @param dataSocket the <tt>DatagramSocket</tt> to be used for data (e.g. RTP) traffic
     * @param controlSocket the <tt>DatagramSocket</tt> to be used for control data (e.g. RTCP) traffic
     * @param rtcpmux whether rtcpmux is used.
     */
    public DefaultStreamConnector(DatagramSocket dataSocket, DatagramSocket controlSocket,
            boolean rtcpmux)
    {
        this.controlSocket = controlSocket;
        this.dataSocket = dataSocket;
        this.bindAddr = null;
        this.rtcpmux = rtcpmux;
    }

    /**
     * Releases the resources allocated by this instance in the course of its execution and prepares
     * it to be garbage collected.
     *
     * @see StreamConnector#close()
     */
    @Override
    public void close()
    {
        if (controlSocket != null)
            controlSocket.close();
        if (dataSocket != null)
            dataSocket.close();
    }

    /**
     * Returns a reference to the <tt>DatagramSocket</tt> that a stream should use for control data (e.g. RTCP) traffic.
     *
     * @return a reference to the <tt>DatagramSocket</tt> that a stream should use for control data (e.g. RTCP) traffic.
     * @see StreamConnector#getControlSocket()
     */
    @Override
    public DatagramSocket getControlSocket()
    {
        if ((controlSocket == null) && (bindAddr != null))
            controlSocket = createDatagramSocket(bindAddr);
        return controlSocket;
    }

    /**
     * Returns a reference to the <tt>DatagramSocket</tt> that a stream should use for data (e.g. RTP) traffic.
     *
     * @return a reference to the <tt>DatagramSocket</tt> that a stream should use for data (e.g. RTP) traffic.
     * @see StreamConnector#getDataSocket()
     */
    @Override
    public DatagramSocket getDataSocket()
    {
        if ((dataSocket == null) && (bindAddr != null))
            dataSocket = createDatagramSocket(bindAddr);
        return dataSocket;
    }

    /**
     * Returns a reference to the <tt>Socket</tt> that a stream should use for data (e.g. RTP) traffic.
     *
     * @return a reference to the <tt>Socket</tt> that a stream should use for data (e.g. RTP) traffic.
     */
    @Override
    public Socket getDataTCPSocket()
    {
        return null;
    }

    /**
     * Returns a reference to the <tt>Socket</tt> that a stream should use for control data (e.g. RTCP).
     *
     * @return a reference to the <tt>Socket</tt> that a stream should use for control data (e.g. RTCP).
     */
    @Override
    public Socket getControlTCPSocket()
    {
        return null;
    }

    /**
     * Returns the protocol of this <tt>StreamConnector</tt>.
     *
     * @return the protocol of this <tt>StreamConnector</tt>
     */
    @Override
    public Protocol getProtocol()
    {
        return Protocol.UDP;
    }

    /**
     * Notifies this instance that utilization of its <tt>DatagramSocket</tt>s for data and/or
     * control traffic has started.
     *
     * @see StreamConnector#started()
     */
    @Override
    public void started()
    {
    }

    /**
     * Notifies this instance that utilization of its <tt>DatagramSocket</tt>s for data and/or
     * control traffic has temporarily stopped. This instance should be prepared to be started at a
     * later time again though.
     *
     * @see StreamConnector#stopped()
     */
    @Override
    public void stopped()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRtcpmux()
    {
        return rtcpmux;
    }
}
