/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.libjitsi;

import org.atalk.impl.libjitsi.LibJitsiImpl;
import org.atalk.impl.libjitsi.LibJitsiOSGiImpl;
import org.atalk.service.audionotifier.AudioNotifierService;
import org.atalk.service.configuration.ConfigurationService;
import org.atalk.service.fileaccess.FileAccessService;
import org.atalk.service.neomedia.MediaService;
import org.atalk.service.resources.ResourceManagementService;
import org.osgi.framework.BundleContext;

import timber.log.Timber;

/**
 * Represents the entry point of the <tt>libjitsi</tt> library.
 * <p>
 * The {@link #start()} method is to be called to initialize/start the use of
 * the library. Respectively, the {@link #stop()} method is to be called to
 * uninitialize/stop the use of the library (i.e. to release the resources
 * acquired by the library during its execution). The <tt>getXXXService()</tt>
 * methods may be called only after the <tt>start()</tt> method returns
 * successfully and before the <tt>stop()</tt> method is called.
 * </p>
 * <p>
 * The <tt>libjitsi</tt> library may be utilized both with and without OSGi. If
 * the library detects during the execution of the <tt>start()</tt> method that
 * (a) the <tt>LibJitsi</tt> class has been loaded as part of an OSGi
 * <tt>Bundle</tt> and (b) successfully retrieves the associated
 * <tt>BundleContext</tt>, it will look for the references to the
 * implementations of the supported service classes in the retrieved
 * <tt>BundleContext</tt>. Otherwise, the library will stand alone without
 * relying on OSGi functionality. In the case of successful detection of OSGi,
 * the library will not register the supported service class instances in the
 * retrieved <tt>BundleContext</tt>.
 * </p>
 *
 * @author Lyubomir Marinov
 * @author Eng Chong Meng
 */
public abstract class LibJitsi
{
    /**
     * The <tt>LibJitsi</tt> instance which is provides the implementation of the <tt>getXXXService</tt> methods.
     */
    private static LibJitsi impl;

    /**
     * Gets the <tt>AudioNotifierService</tt> instance. If no existing
     * <tt>AudioNotifierService</tt> instance is known to the library, tries to
     * initialize a new one. (Such a try to initialize a new instance is
     * performed just once while the library is initialized.)
     *
     * @return the <tt>AudioNotifierService</tt> instance known to the library
     * or <tt>null</tt> if no <tt>AudioNotifierService</tt> instance is known to the library
     */
    public static AudioNotifierService getAudioNotifierService()
    {
        return invokeGetServiceOnImpl(AudioNotifierService.class);
    }

    /**
     * Gets the <tt>ConfigurationService</tt> instance. If no existing
     * <tt>ConfigurationService</tt> instance is known to the library, tries to
     * initialize a new one. (Such a try to initialize a new instance is
     * performed just once while the library is initialized.)
     *
     * @return the <tt>ConfigurationService</tt> instance known to the library
     * or <tt>null</tt> if no <tt>ConfigurationService</tt> instance is known to the library
     */
    public static ConfigurationService getConfigurationService()
    {
        return invokeGetServiceOnImpl(ConfigurationService.class);
    }

    /**
     * Gets the <tt>FileAccessService</tt> instance. If no existing
     * <tt>FileAccessService</tt> instance is known to the library, tries to
     * initialize a new one. (Such a try to initialize a new instance is
     * performed just once while the library is initialized.)
     *
     * @return the <tt>FileAccessService</tt> instance known to the library or
     * <tt>null</tt> if no <tt>FileAccessService</tt> instance is known to the library
     */
    public static FileAccessService getFileAccessService()
    {
        return invokeGetServiceOnImpl(FileAccessService.class);
    }

    /**
     * Gets the <tt>MediaService</tt> instance. If no existing
     * <tt>MediaService</tt> instance is known to the library, tries to
     * initialize a new one. (Such a try to initialize a new instance is
     * performed just once while the library is initialized.)
     *
     * @return the <tt>MediaService</tt> instance known to the library or
     * <tt>null</tt> if no <tt>MediaService</tt> instance is known to the library
     */
    public static MediaService getMediaService()
    {
        return invokeGetServiceOnImpl(MediaService.class);
    }

    /**
     * Gets the <tt>ResourceManagementService</tt> instance. If no existing
     * <tt>ResourceManagementService</tt> instance is known to the library,
     * tries to initialize a new one. (Such a try to initialize a new instance
     * is performed just once while the library is initialized.)
     *
     * @return the <tt>ResourceManagementService</tt> instance known to the
     * library or <tt>null</tt> if no <tt>ResourceManagementService</tt>
     * instance is known to the library.
     */
    public static ResourceManagementService getResourceManagementService()
    {
        return invokeGetServiceOnImpl(ResourceManagementService.class);
    }

    /**
     * Invokes {@link #getService(Class)} on {@link #impl}.
     *
     * @param serviceClass the class of the service to be retrieved.
     * @return a service of the specified type if such a service is associated with the library.
     * @throws IllegalStateException if the library is not currently initialized.
     */
    private static <T> T invokeGetServiceOnImpl(Class<T> serviceClass)
    {
        LibJitsi impl = LibJitsi.impl;
        if (impl == null)
            throw new IllegalStateException("impl");
        else
            return impl.getService(serviceClass);
    }

    /**
     * Starts/initializes the use of the <tt>libjitsi</tt> library.
     */
    public static void start()
    {
        start(null);
    }

    /**
     * Starts/initializes the use of the <tt>libjitsi</tt> library.
     *
     * @param context an OSGi {@link BundleContext}.
     */
    static LibJitsi start(BundleContext context)
    {
        if (null != LibJitsi.impl) {
            Timber.d("LibJitsi already started, using as implementation: %s",
                    impl.getClass().getCanonicalName());
            return impl;
        }

        /*
         * LibJitsi implements multiple backends and tries to choose the most
         * appropriate at run time. For example, an OSGi-aware backend is used
         * if it is detected that an OSGi implementation is available.
         */
        if (context == null) {
            impl = new LibJitsiImpl();
        }
        else {
            impl = new LibJitsiOSGiImpl(context);
        }
        Timber.d("Successfully started LibJitsi using implementation: %s", impl.getClass().getCanonicalName());
        return impl;
    }

    /**
     * Stops/uninitializes the use of the <tt>libjitsi</tt> library.
     */
    public static void stop()
    {
        impl = null;
    }

    /**
     * Initializes a new <tt>LibJitsi</tt> instance.
     */
    protected LibJitsi()
    {
    }

    /**
     * Gets a service of a specific type associated with this implementation of the <tt>libjitsi</tt> library.
     *
     * @param serviceClass the type of the service to be retrieved
     * @return a service of the specified type if there is such an association
     * known to this implementation of the <tt>libjitsi</tt> library; otherwise, <tt>null</tt>
     */
    protected abstract <T> T getService(Class<T> serviceClass);
}
