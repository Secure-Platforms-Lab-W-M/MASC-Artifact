/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol;

/**
 * An Advanced Operation Set defining options to auto answer/forward incoming calls.
 *
 * @author Damian Minkov
 */
public interface OperationSetAdvancedAutoAnswer extends OperationSet
{
	/**
	 * Auto answer conditional account property - field name.
	 */
	public static final String AUTO_ANSWER_COND_NAME_PROP = "AUTO_ANSWER_CONDITIONAL_NAME";

	/**
	 * Auto answer conditional account property - field value.
	 */
	public static final String AUTO_ANSWER_COND_VALUE_PROP = "AUTO_ANSWER_CONDITIONAL_VALUE";

	/**
	 * Auto forward all calls account property.
	 */
	public static final String AUTO_ANSWER_FWD_NUM_PROP = "AUTO_ANSWER_FWD_NUM";

	/**
	 * Sets a specified header and its value if they exist in the incoming call packet this will
	 * activate auto answer. If value is empty or null it will be considered as any (will search
	 * only for a header with that name and ignore the value)
	 * 
	 * @param headerName
	 *        the name of the header to search
	 * @param value
	 *        the value for the header, can be null.
	 */
	public void setAutoAnswerCondition(String headerName, String value);

	/**
	 * Is the auto answer option set to conditionally answer all incoming calls.
	 * 
	 * @return is auto answer set to conditional.
	 */
	public boolean isAutoAnswerConditionSet();

	/**
	 * Returns the name of the header if conditional auto answer is set.
	 * 
	 * @return the name of the header if conditional auto answer is set.
	 */
	public String getAutoAnswerHeaderName();

	/**
	 * Returns the value of the header for the conditional auto answer.
	 * 
	 * @return the value of the header for the conditional auto answer.
	 */
	public String getAutoAnswerHeaderValue();

	/**
	 * Set to automatically forward all calls to the specified number using the same provider.
	 * 
	 * @param numberTo
	 *        number to use for forwarding
	 */
	public void setCallForward(String numberTo);

	/**
	 * Get the value for automatically forward all calls to the specified number using the same
	 * provider..
	 * 
	 * @return numberTo number to use for forwarding
	 */
	public String getCallForward();

	/**
	 * Clear any previous settings.
	 */
	public void clear();
}
