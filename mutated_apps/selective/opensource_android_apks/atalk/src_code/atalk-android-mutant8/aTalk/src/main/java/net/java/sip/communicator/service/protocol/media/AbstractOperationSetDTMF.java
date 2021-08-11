/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.media;

import net.java.sip.communicator.service.protocol.*;

import org.apache.commons.lang3.StringUtils;
import org.atalk.service.configuration.ConfigurationService;
import org.atalk.service.neomedia.DTMFMethod;
import org.atalk.service.neomedia.codec.Constants;
import org.atalk.service.neomedia.format.MediaFormat;
import org.atalk.util.MediaType;

import java.util.Iterator;

/**
 * Represents a default/base implementation of <tt>OperationSetDTMF</tt> which attempts to make it
 * easier for implementers to provide complete solutions while focusing on implementation-specific
 * functionality.
 *
 * @author Damian Minkov
 */
public abstract class AbstractOperationSetDTMF implements OperationSetDTMF
{
    /**
     * The DTMF method used to send tones.
     */
    protected DTMFMethod dtmfMethod;

    /**
     * The minimal tone duration.
     */
    protected int minimalToneDuration;

    /**
     * The maximal tone duration.
     */
    protected int maximalToneDuration;

    /**
     * The tone volume.
     */
    protected int volume;

    /**
     * Creates the <tt>AbstractOperationSetDTMF</tt> and initialize some settings.
     *
     * @param pps the protocol provider.
     */
    public AbstractOperationSetDTMF(ProtocolProviderService pps)
    {
        this.dtmfMethod = getDTMFMethod(pps);
        this.minimalToneDuration = getMinimalToneDurationSetting(pps);
        this.maximalToneDuration = getMaximalToneDurationSetting();
        this.volume = getVolumeSetting(pps);
    }

    /**
     * Gets the minimal DTMF tone duration for this account.
     *
     * @param pps the Protocol provider service
     * @return The minimal DTMF tone duration for this account.
     */
    private static int getMinimalToneDurationSetting(ProtocolProviderService pps)
    {
        AccountID accountID = pps.getAccountID();
        String minimalToneDurationString = accountID.getAccountPropertyString("DTMF_MINIMAL_TONE_DURATION");
        int minimalToneDuration = OperationSetDTMF.DEFAULT_DTMF_MINIMAL_TONE_DURATION;
        // Check if there is a specific value for this account.
        if (StringUtils.isNotEmpty(minimalToneDurationString)) {
            minimalToneDuration = Integer.valueOf(minimalToneDurationString);
        }
        // Else look at the global property.
        else {
            ConfigurationService cfg = ProtocolProviderActivator.getConfigurationService();
            // Check if there is a custom value for the minimal tone duration.
            if (cfg != null) {
                minimalToneDuration = cfg.getInt(
                        OperationSetDTMF.PROP_MINIMAL_RTP_DTMF_TONE_DURATION, minimalToneDuration);
            }
        }
        return minimalToneDuration;
    }

    /**
     * Gets the maximal DTMF tone duration for this account.
     *
     * @return The maximal DTMF tone duration for this account.
     */
    private static int getMaximalToneDurationSetting()
    {
        int maximalToneDuration = OperationSetDTMF.DEFAULT_DTMF_MAXIMAL_TONE_DURATION;

        // Look at the global property.
        ConfigurationService cfg = ProtocolProviderActivator.getConfigurationService();
        // Check if there is a custom value for the maximal tone duration.
        if (cfg != null) {
            maximalToneDuration = cfg.getInt(OperationSetDTMF.PROP_MAXIMAL_RTP_DTMF_TONE_DURATION,
                    maximalToneDuration);
        }

        return maximalToneDuration;
    }

    /**
     * Returns the corresponding DTMF method used for this account.
     *
     * @param pps the Protocol provider service
     * @return the DTMFEnum corresponding to the DTMF method set for this account.
     */
    private static DTMFMethod getDTMFMethod(ProtocolProviderService pps)
    {
        AccountID accountID = pps.getAccountID();

        String dtmfString = accountID.getAccountPropertyString("DTMF_METHOD");

        // Verifies that the DTMF_METHOD property string is correctly set.
        // If not, sets this account to the "auto" DTMF method and corrects the
        // property string.
        if (dtmfString == null
                || (!dtmfString.equals("AUTO_DTMF") && !dtmfString.equals("RTP_DTMF")
                && !dtmfString.equals("SIP_INFO_DTMF") && !dtmfString.equals("INBAND_DTMF"))) {
            dtmfString = "AUTO_DTMF";
            accountID.putAccountProperty("DTMF_METHOD", dtmfString);
        }

        if (dtmfString.equals("AUTO_DTMF")) {
            return DTMFMethod.AUTO_DTMF;
        }
        else if (dtmfString.equals("RTP_DTMF")) {
            return DTMFMethod.RTP_DTMF;
        }
        else if (dtmfString.equals("SIP_INFO_DTMF")) {
            return DTMFMethod.SIP_INFO_DTMF;
        }
        else // if(dtmfString.equals(INBAND_DTMF"))
        {
            return DTMFMethod.INBAND_DTMF;
        }
    }

    /**
     * Checks whether rfc4733 is negotiated for this call.
     *
     * @param peer the call peer.
     * @return whether we can use rfc4733 in this call.
     */
    protected static boolean isRFC4733Active(MediaAwareCallPeer<?, ?, ?> peer)
    {
        Iterator<MediaFormat> iter = peer.getMediaHandler().getStream(MediaType.AUDIO)
                .getDynamicRTPPayloadTypes().values().iterator();

        while (iter.hasNext()) {
            MediaFormat mediaFormat = iter.next();

            if (Constants.TELEPHONE_EVENT.equals(mediaFormat.getEncoding()))
                return true;
        }
        return false;
    }

    /**
     * Gets the DTMF tone volume for this account.
     *
     * @return The DTMF tone volume for this account.
     */
    private static int getVolumeSetting(ProtocolProviderService pps)
    {
        AccountID accountID = pps.getAccountID();
        String volumeString = accountID.getAccountPropertyString("DTMF_TONE_VOLUME");
        int vol = OperationSetDTMF.DEFAULT_DTMF_TONE_VOLUME;
        // Check if there is a specific value for this account.
        if (StringUtils.isNotEmpty(volumeString)) {
            vol = Integer.parseInt(volumeString);
        }
        return vol;
    }
}
