/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.qpid.server.queue;

import static org.apache.qpid.server.security.access.Operation.INVOKE_METHOD;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.qpid.server.configuration.updater.Task;
import org.apache.qpid.server.util.FixedKeyMapCreator;

final class PriorityQueueImplWithAccessChecking extends PriorityQueueImpl
{
    PriorityQueueImplWithAccessChecking(final Map<String, Object> attributes, final org.apache.qpid.server.virtualhost.QueueManagingVirtualHost<?> queuemanagingvirtualhost)
    {
        super(attributes, queuemanagingvirtualhost);
    }

    public java.util.Map<java.lang.String,java.lang.Object> getStatistics(final java.util.List<java.lang.String> statistics)
    {
        return super.getStatistics(statistics);
    }

    public java.lang.String setContextVariable(final java.lang.String name, final java.lang.String value)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(PriorityQueueImplWithAccessChecking.super.setContextVariable(name, value));
                }
                @Override
                public String getObject()
                {
                    return PriorityQueueImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "setContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name + "," + "value=" + value;
                    }
                    return _args;
                }
            }));

    }

    public java.lang.String removeContextVariable(final java.lang.String name)
    {
        return doSync(doOnConfigThread(new Task<ListenableFuture<java.lang.String>, RuntimeException>()
            {
                private String _args;
                @Override
                public ListenableFuture<java.lang.String> execute()
                {
                    return Futures.<java.lang.String>immediateFuture(PriorityQueueImplWithAccessChecking.super.removeContextVariable(name));
                }
                @Override
                public String getObject()
                {
                    return PriorityQueueImplWithAccessChecking.this.toString();
                }
                @Override
                public String getAction()
                {
                    return "removeContextVariable";
                }
                @Override
                public String getArguments()
                {
                    if (_args == null)
                    {
                        _args = "name=" + name;
                    }
                    return _args;
                }
            }));

    }

    public java.util.Collection<org.apache.qpid.server.model.PublishingLink> getPublishingLinks()
    {
        return super.getPublishingLinks();
    }

    public java.util.Collection<org.apache.qpid.server.queue.QueueConsumer<?,?>> getConsumers()
    {
        return super.getConsumers();
    }

    private static final FixedKeyMapCreator MOVE_MESSAGES_MAP_CREATOR = new FixedKeyMapCreator("destination", "messageIds", "selector", "limit");

    public java.util.List<java.lang.Long> moveMessages(final org.apache.qpid.server.model.Queue<?> destination, final java.util.List<java.lang.Long> messageIds, final java.lang.String selector, final int limit)
    {
        authorise(INVOKE_METHOD("moveMessages"), MOVE_MESSAGES_MAP_CREATOR.createMap(destination, messageIds, selector, limit));

        return super.moveMessages(destination, messageIds, selector, limit);
    }

    private static final FixedKeyMapCreator COPY_MESSAGES_MAP_CREATOR = new FixedKeyMapCreator("destination", "messageIds", "selector", "limit");

    public java.util.List<java.lang.Long> copyMessages(final org.apache.qpid.server.model.Queue<?> destination, final java.util.List<java.lang.Long> messageIds, final java.lang.String selector, final int limit)
    {
        authorise(INVOKE_METHOD("copyMessages"), COPY_MESSAGES_MAP_CREATOR.createMap(destination, messageIds, selector, limit));

        return super.copyMessages(destination, messageIds, selector, limit);
    }

    private static final FixedKeyMapCreator DELETE_MESSAGES_MAP_CREATOR = new FixedKeyMapCreator("messageIds", "selector", "limit");

    public java.util.List<java.lang.Long> deleteMessages(final java.util.List<java.lang.Long> messageIds, final java.lang.String selector, final int limit)
    {
        authorise(INVOKE_METHOD("deleteMessages"), DELETE_MESSAGES_MAP_CREATOR.createMap(messageIds, selector, limit));

        return super.deleteMessages(messageIds, selector, limit);
    }

    public long clearQueue()
    {
        authorise(INVOKE_METHOD("clearQueue"));

        return super.clearQueue();
    }

    private static final FixedKeyMapCreator GET_MESSAGE_CONTENT_MAP_CREATOR = new FixedKeyMapCreator("messageId", "limit", "returnJson", "decompressBeforeLimiting");

    public org.apache.qpid.server.model.Content getMessageContent(final long messageId, final long limit, final boolean returnJson, final boolean decompressBeforeLimiting)
    {
        authorise(INVOKE_METHOD("getMessageContent"), GET_MESSAGE_CONTENT_MAP_CREATOR.createMap(messageId, limit, returnJson, decompressBeforeLimiting));

        return super.getMessageContent(messageId, limit, returnJson, decompressBeforeLimiting);
    }

    private static final FixedKeyMapCreator GET_MESSAGE_INFO_MAP_CREATOR = new FixedKeyMapCreator("first", "last", "includeHeaders");

    public java.util.List<org.apache.qpid.server.message.MessageInfo> getMessageInfo(final int first, final int last, final boolean includeHeaders)
    {
        authorise(INVOKE_METHOD("getMessageInfo"), GET_MESSAGE_INFO_MAP_CREATOR.createMap(first, last, includeHeaders));

        return super.getMessageInfo(first, last, includeHeaders);
    }

    private static final FixedKeyMapCreator GET_MESSAGE_INFO_BY_ID_MAP_CREATOR = new FixedKeyMapCreator("messageId", "includeHeaders");

    public org.apache.qpid.server.message.MessageInfo getMessageInfoById(final long messageId, final boolean includeHeaders)
    {
        authorise(INVOKE_METHOD("getMessageInfoById"), GET_MESSAGE_INFO_BY_ID_MAP_CREATOR.createMap(messageId, includeHeaders));

        return super.getMessageInfoById(messageId, includeHeaders);
    }

    private static final FixedKeyMapCreator REENQUEUE_MESSAGE_FOR_PRIORITY_CHANGE_MAP_CREATOR = new FixedKeyMapCreator("messageId", "newPriority");

    public long reenqueueMessageForPriorityChange(final long messageId, final int newPriority)
    {
        authorise(INVOKE_METHOD("reenqueueMessageForPriorityChange"), REENQUEUE_MESSAGE_FOR_PRIORITY_CHANGE_MAP_CREATOR.createMap(messageId, newPriority));

        return super.reenqueueMessageForPriorityChange(messageId, newPriority);
    }

    private static final FixedKeyMapCreator REENQUEUE_MESSAGES_FOR_PRIORITY_CHANGE_MAP_CREATOR = new FixedKeyMapCreator("selector", "newPriority");

    public java.util.List<java.lang.Long> reenqueueMessagesForPriorityChange(final java.lang.String selector, final int newPriority)
    {
        authorise(INVOKE_METHOD("reenqueueMessagesForPriorityChange"), REENQUEUE_MESSAGES_FOR_PRIORITY_CHANGE_MAP_CREATOR.createMap(selector, newPriority));

        return super.reenqueueMessagesForPriorityChange(selector, newPriority);
    }

}
