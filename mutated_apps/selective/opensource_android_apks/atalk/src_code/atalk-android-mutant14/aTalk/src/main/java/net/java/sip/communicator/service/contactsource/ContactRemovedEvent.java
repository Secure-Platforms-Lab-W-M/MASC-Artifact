/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.sip.communicator.service.contactsource;

import java.util.*;

/**
 * The <tt>ContactRemovedEvent</tt> indicates that a
 * <tt>SourceContact</tt> has been removed from the result of a
 * <tt>ContactQuery</tt>.
 * @author Yana Stamcheva
 */
public class ContactRemovedEvent
    extends EventObject
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The contact that has been removed.
     */
    private final SourceContact contact;

    /**
     * Creates a <tt>ContactRemovedEvent</tt> by specifying the contact search
     * source and the removed <tt>searchContact</tt>.
     * @param source the source that triggered this event
     * @param contact the removed contact
     */
    public ContactRemovedEvent(ContactQuery source,
                               SourceContact contact)
    {
        super(source);

        this.contact = contact;
    }

    /**
     * Returns the <tt>ContactQuery</tt> that triggered this event.
     * @return the <tt>ContactQuery</tt> that triggered this event
     */
    public ContactQuery getQuerySource()
    {
        return (ContactQuery) source;
    }

    /**
     * Returns the removed contact.
     * @return the removed contact
     */
    public SourceContact getContact()
    {
        return contact;
    }
}
