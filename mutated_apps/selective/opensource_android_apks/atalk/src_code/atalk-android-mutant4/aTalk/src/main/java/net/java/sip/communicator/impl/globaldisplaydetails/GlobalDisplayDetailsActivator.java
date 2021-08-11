/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.globaldisplaydetails;

import net.java.sip.communicator.service.globaldisplaydetails.*;
import net.java.sip.communicator.service.gui.*;
import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.service.protocol.globalstatus.*;
import net.java.sip.communicator.util.*;

import org.atalk.service.configuration.ConfigurationService;
import org.atalk.service.resources.ResourceManagementService;
import org.osgi.framework.*;

/**
 *
 * @author Yana Stamcheva
 */
public class GlobalDisplayDetailsActivator implements BundleActivator, ServiceListener
{
	/**
	 * The bundle context.
	 */
	private static BundleContext bundleContext;

	/**
	 * The service giving access to image and string application resources.
	 */
	private static ResourceManagementService resourcesService;

	/**
	 * The service giving access to the configuration resources.
	 */
	private static ConfigurationService configService;

	/**
	 * The alert UI service.
	 */
	private static AlertUIService alertUIService;

	/**
	 * The UI service.
	 */
	private static UIService uiService;

	/**
	 * The display details implementation.
	 */
	static GlobalDisplayDetailsImpl displayDetailsImpl;

	static GlobalStatusServiceImpl globalStatusService;

	/**
	 * Initialize and start file service
	 *
	 * @param bc
	 *        the <tt>BundleContext</tt>
	 * @throws Exception
	 *         if initializing and starting file service fails
	 */
	public void start(BundleContext bc)
		throws Exception
	{
		bundleContext = bc;

		displayDetailsImpl = new GlobalDisplayDetailsImpl();
		globalStatusService = new GlobalStatusServiceImpl();

		bundleContext.addServiceListener(this);

		handleAlreadyRegisteredProviders();

		bundleContext.registerService(GlobalDisplayDetailsService.class.getName(), displayDetailsImpl, null);

		bundleContext.registerService(GlobalStatusService.class.getName(), globalStatusService, null);
	}

	/**
	 * Searches and processes already registered providers.
	 */
	private void handleAlreadyRegisteredProviders()
	{
		bundleContext.addServiceListener((org.osgi.framework.ServiceListener) this);

		ServiceReference[] ppsRefs = null;
		try {
			ppsRefs = bundleContext.getServiceReferences(ProtocolProviderService.class.getName(), null);
		}
		catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (ppsRefs.length != 0) {
			for (ServiceReference<ProtocolProviderService> ppsRef : ppsRefs) {
				ProtocolProviderService pps = bundleContext.getService(ppsRef);
				handleProviderAdded(pps);
			}
		}
	}

	/**
	 * Used to attach the listeners to existing or just registered protocol provider.
	 *
	 * @param pps
	 *        ProtocolProviderService
	 */
	private void handleProviderAdded(ProtocolProviderService pps)
	{
		pps.addRegistrationStateChangeListener(displayDetailsImpl);
		globalStatusService.handleProviderAdded(pps);
	}

	/**
	 * Stops this bundle.
	 *
	 * @param bundleContext
	 *        the <tt>BundleContext</tt>
	 * @throws Exception
	 *         if the stop operation goes wrong
	 */
	public void stop(BundleContext bundleContext)
		throws Exception
	{
	}

	/**
	 * Returns the <tt>ResourceManagementService</tt>, through which we will access all resources.
	 *
	 * @return the <tt>ResourceManagementService</tt>, through which we will access all resources.
	 */
	public static ResourceManagementService getResources()
	{
		if (resourcesService == null) {
			resourcesService = ServiceUtils.getService(bundleContext, ResourceManagementService.class);
		}
		return resourcesService;
	}

	/**
	 * Returns the <tt>ConfigurationService</tt> obtained from the bundle context.
	 * 
	 * @return the <tt>ConfigurationService</tt> obtained from the bundle context
	 */
	public static ConfigurationService getConfigurationService()
	{
		if (configService == null) {
			configService = ServiceUtils.getService(bundleContext, ConfigurationService.class);
		}
		return configService;
	}

	/**
	 * Returns the <tt>AlertUIService</tt> obtained from the bundle context.
	 * 
	 * @return the <tt>AlertUIService</tt> obtained from the bundle context
	 */
	public static AlertUIService getAlertUIService()
	{
		if (alertUIService == null) {
			alertUIService = ServiceUtils.getService(bundleContext, AlertUIService.class);
		}
		return alertUIService;
	}

	/**
	 * Returns the <tt>UIService</tt> obtained from the bundle context.
	 * 
	 * @return the <tt>UIService</tt> obtained from the bundle context
	 */
	public static UIService getUIService()
	{
		if (uiService == null) {
			uiService = ServiceUtils.getService(bundleContext, UIService.class);
		}
		return uiService;
	}

	/**
	 * Implements the <tt>ServiceListener</tt> method. Verifies whether the passed event concerns a <tt>ProtocolProviderService</tt> and
	 * adds or removes a registration listener.
	 *
	 * @param event
	 *        The <tt>ServiceEvent</tt> object.
	 */
	public void serviceChanged(ServiceEvent event)
	{
		ServiceReference<?> serviceRef = event.getServiceReference();

		// if the event is caused by a bundle being stopped, we don't want to
		// know
		if (serviceRef.getBundle().getState() == Bundle.STOPPING) {
			return;
		}

		Object service = UtilActivator.bundleContext.getService(serviceRef);

		// we don't care if the source service is not a protocol provider
		if (!(service instanceof ProtocolProviderService)) {
			return;
		}

		ProtocolProviderService pps = (ProtocolProviderService) service;

		switch (event.getType()) {
			case ServiceEvent.REGISTERED:
				this.handleProviderAdded(pps);
				break;
			case ServiceEvent.UNREGISTERING:
				pps.removeRegistrationStateChangeListener(displayDetailsImpl);
				globalStatusService.handleProviderRemoved(pps);
				break;
		}
	}
}
