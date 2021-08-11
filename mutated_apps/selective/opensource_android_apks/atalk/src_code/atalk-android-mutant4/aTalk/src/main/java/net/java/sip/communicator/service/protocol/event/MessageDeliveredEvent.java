/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.sip.communicator.service.protocol.event;

import net.java.sip.communicator.service.protocol.*;

import org.atalk.android.gui.chat.ChatMessage;

import java.util.Date;
import java.util.EventObject;

/**
 * <tt>MessageDeliveredEvent</tt>s confirm successful delivery of an instant message.
 *
 * @author Emil Ivov
 */
public class MessageDeliveredEvent extends EventObject
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The contact that has sent this message.
     */
    private Contact to = null;

    /**
     * The <tt>ContactResource</tt>, to which the message was sent.
     */
    private ContactResource toResource = null;

    /**
     * A timestamp indicating the exact date when the event occurred.
     */
    private final Date timestamp;

    /**
     * The ID of the message being corrected, or null if this was a new message and not a message
     * correction.
     */
    private String correctedMessageUID;

    /**
     * Whether the delivered message is a sms message.
     */
    private boolean smsMessage = false;

    /**
     * Whether the delivered message is encrypted or not.
     */
    private boolean isMessageEncrypted = false;

    /**
     * Constructor.
     *
     * @param source message source
     * @param to the "to" contact
     */
    public MessageDeliveredEvent(IMessage source, Contact to)
    {
        this(source, to, new Date());
    }

    /**
     * Creates a <tt>MessageDeliveredEvent</tt> representing delivery of the <tt>source</tt> message
     * to the specified <tt>to</tt> contact.
     *
     * @param source the <tt>IMessage</tt> whose delivery this event represents.
     * @param to the <tt>Contact</tt> that this message was sent to.
     * @param correctedMessageUID The ID of the message being corrected.
     */
    public MessageDeliveredEvent(IMessage source, Contact to, String correctedMessageUID)
    {
        this(source, to, new Date());
        this.correctedMessageUID = correctedMessageUID;
    }

    /**
     * Creates a <tt>MessageDeliveredEvent</tt> representing delivery of the <tt>source</tt> message
     * to the specified <tt>to</tt> contact.
     *
     * @param source the <tt>IMessage</tt> whose delivery this event represents.
     * @param to the <tt>Contact</tt> that this message was sent to.
     * @param timestamp a date indicating the exact moment when the event occurred
     */
    public MessageDeliveredEvent(IMessage source, Contact to, Date timestamp)
    {
        super(source);
        this.to = to;
        this.timestamp = timestamp;
    }

    /**
     * Creates a <tt>MessageDeliveredEvent</tt> representing delivery of the <tt>source</tt> message
     * to the specified <tt>to</tt> contact.
     *
     * @param source the <tt>IMessage</tt> whose delivery this event represents.
     * @param to the <tt>Contact</tt> that this message was sent to.
     * @param timestamp a date indicating the exact moment when the event occurred
     */
    public MessageDeliveredEvent(IMessage source, Contact to, ContactResource toResource, Date timestamp)
    {
        super(source);
        this.to = to;
        this.toResource = toResource;
        this.timestamp = timestamp;
    }

    /**
     * Creates a <tt>MessageDeliveredEvent</tt> representing delivery of the <tt>source</tt> message
     * to the specified <tt>to</tt> contact.
     *
     * @param source the <tt>IMessage</tt> whose delivery this event represents.
     * @param to the <tt>Contact</tt> that this message was sent to.
     * @param toResource the <tt>Contact</tt> resource that this message was sent to
     */
    public MessageDeliveredEvent(IMessage source, Contact to, ContactResource toResource)
    {
        this(source, to, new Date());
        this.toResource = toResource;
    }

    /**
     * Returns a reference to the <tt>Contact</tt> that <tt>IMessage</tt> was sent to.
     *
     * @return a reference to the <tt>Contact</tt> that has send the <tt>IMessage</tt> whose
     * reception this event represents.
     */
    public Contact getDestinationContact()
    {
        return to;
    }

    /**
     * Returns the message that triggered this event
     *
     * @return the <tt>IMessage</tt> that triggered this event.
     */
    public IMessage getSourceMessage()
    {
        return (IMessage) getSource();
    }

    /**
     * A timestamp indicating the exact date when the event occurred.
     *
     * @return a Date indicating when the event occurred.
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Returns the type of message event represented by this event instance.
     *
     * @return one of the XXX_MESSAGE_DELIVERED fields of this class indicating the type of this
     * event.
     */
    public int getEventType()
    {
        return isSmsMessage() ? ChatMessage.MESSAGE_SMS_OUT : ChatMessage.MESSAGE_OUT;
    }

    /**
     * Returns the ID of the message being corrected, or null if this was a new message and not a
     * message correction.
     *
     * @return the ID of the message being corrected, or null if this was a new message and not a
     * message correction.
     */
    public String getCorrectedMessageUID()
    {
        return correctedMessageUID;
    }

    /**
     * Sets the ID of the message being corrected to the passed ID.
     *
     * @param correctedMessageUID The ID of the message being corrected.
     */
    public void setCorrectedMessageUID(String correctedMessageUID)
    {
        this.correctedMessageUID = correctedMessageUID;
    }

    /**
     * Sets whether the message is a sms one.
     *
     * @param smsMessage whether it is a sms one.
     */
    public void setSmsMessage(boolean smsMessage)
    {
        this.smsMessage = smsMessage;
    }

    /**
     * Returns whether the delivered message is a sms one.
     *
     * @return whether the delivered message is a sms one.
     */
    public boolean isSmsMessage()
    {
        return smsMessage;
    }

    /**
     * Returns a reference to the <tt>ContactResource</tt> that has sent the <tt>IMessage</tt> whose
     * reception this event represents.
     *
     * @return a reference to the <tt>ContactResource</tt> that has sent the <tt>IMessage</tt> whose
     * reception this event represents.
     */
    public ContactResource getContactResource()
    {
        return toResource;
    }

    /**
     * Returns <tt>true</tt> if the message is encrypted and <tt>false</tt> if not.
     *
     * @return <tt>true</tt> if the message is encrypted and <tt>false</tt> if not.
     */
    public boolean isMessageEncrypted()
    {
        return isMessageEncrypted;
    }

    /**
     * Sets the message encrypted flag of the event.
     *
     * @param isMessageEncrypted the value to be set.
     */
    public void setMessageEncrypted(boolean isMessageEncrypted)
    {
        this.isMessageEncrypted = isMessageEncrypted;
    }
}
