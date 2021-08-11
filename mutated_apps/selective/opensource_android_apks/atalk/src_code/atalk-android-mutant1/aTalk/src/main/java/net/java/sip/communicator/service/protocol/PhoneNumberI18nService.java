/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

/**
 * Implements <tt>PhoneNumberI18nService</tt> which aids the parsing, formatting
 * and validating of international phone numbers.
 *
 * @author Lyubomir Marinov
 * @author Vincent Lucas
 * @author Damian Minkov
 * @author Eng Chong Meng
 */
public interface PhoneNumberI18nService
{
    /**
     * Normalizes a <tt>String</tt> which may be a phone number or a identifier by removing useless
     * characters and, if necessary, replacing the alpahe characters in corresponding dial pad
     * numbers.
     *
     * @param possibleNumber a <tt>String</tt> which may represents a phone number or an identifier to normalize.
     * @return a <tt>String</tt> which is a normalized form of the specified <tt>possibleNumber</tt>
     * .
     */
    String normalize(String possibleNumber);

    /**
     * Tries to format the passed phone number into the international format. If
     * parsing fails or the string is not recognized as a valid phone number,
     * the input is returned as is.
     *
     * @param phoneNumber The phone number to format.
     * @return the formatted phone number in the international format.
     */
    String formatForDisplay(String phoneNumber);

    /**
     * Determines whether two <tt>String</tt> phone numbers match.
     *
     * @param aPhoneNumber a <tt>String</tt> which represents a phone number to match to <tt>bPhoneNumber</tt>
     * @param bPhoneNumber a <tt>String</tt> which represents a phone number to match to <tt>aPhoneNumber</tt>
     * @return <tt>true</tt> if the specified <tt>String</tt>s match as phone numbers; otherwise,
     * <tt>false</tt>
     */
    public boolean phoneNumbersMatch(String aPhoneNumber, String bPhoneNumber);

    /**
     * Indicates if the given string is possibly a phone number.
     *
     * @param possibleNumber the string to be verified
     * @return <tt>true</tt> if the possibleNumber is a phone number, <tt>false</tt> - otherwise
     */
    boolean isPhoneNumber(String possibleNumber);
}
