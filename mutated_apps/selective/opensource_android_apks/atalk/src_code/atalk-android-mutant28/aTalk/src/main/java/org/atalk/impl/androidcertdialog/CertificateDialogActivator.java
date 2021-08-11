/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.androidcertdialog;

import net.java.sip.communicator.service.certificate.VerifyCertificateDialogService;
import net.java.sip.communicator.util.SimpleServiceActivator;

import org.osgi.framework.BundleContext;

/**
 * Activator of <tt>VerifyCertificateDialogService</tt> Android implementation.
 *
 * @author Pawel Domas
 */
public class CertificateDialogActivator extends SimpleServiceActivator<CertificateDialogServiceImpl>
{
    /**
     * Creates a new instance of CertificateDialogActivator.
     */
    public CertificateDialogActivator()
    {
        super(VerifyCertificateDialogService.class, "Android verify certificate service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CertificateDialogServiceImpl createServiceImpl()
    {
        impl = new CertificateDialogServiceImpl();
        return impl;
    }

    /**
     * Cached instance of service impl.
     */
    public static CertificateDialogServiceImpl impl;

    /**
     * Gets the <tt>VerifyCertDialog</tt> for given <tt>requestId</tt>.
     *
     * @param requestId identifier of the request managed by <tt>CertificateDialogServiceImpl</tt>.
     * @return <tt>VerifyCertDialog</tt> for given <tt>requestId</tt> or <tt>null</tt> if service has been shutdown.
     */
    public static VerifyCertDialog getDialog(Long requestId)
    {
        if (impl != null) {
            return impl.retrieveDialog(requestId);
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext bundleContext)
            throws Exception
    {
        super.stop(bundleContext);
        // Clears service reference
        impl = null;
    }
}
