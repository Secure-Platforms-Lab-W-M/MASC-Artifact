/*
 *
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
 *
 */

package org.apache.qpid.server.protocol.v0_8.transport;

import org.apache.qpid.server.QpidException;

/**
 * AMQMethodListener is a listener that receives notifications of AMQP methods. The methods are packaged as events in
 * {@link AMQMethodEvent}.
 *
 * <p>An event listener may be associated with a particular context, usually an AMQP channel, and in addition to
 * receiving method events will be notified of errors on that context. This enables listeners to perform any clean
 * up that they need to do before the context is closed or retried.
 * <p>
 * TODO  Document why the exception is passed to the error method. Is it so that the exception can be passed
 *       from the event handling thread to another thread and rethown from there? It is unusual to pass exceptions as
 *       method arguments, because they have their own mechanism for propagating through the call stack, so some
 *       explanation ought to be provided.
 */
public interface AMQMethodListener
{
    /**
     * Notifies the listener that an AMQP method event has occurred.
     *
     * @param evt The AMQP method event (contains the method and channel).
     *
     * @return true if the handler processes the method frame, false otherwise. Note that this does
     *         not prohibit the method event being delivered to subsequent listeners but can be used to determine if
     *         nobody has dealt with an incoming method frame.
     *
     * @throws QpidException if an error has occurred. This exception may be delivered to all registered listeners using
     *         the error() method (see below) allowing them to perform cleanup if necessary.
     * <p>
     * TODO Consider narrowing the exception.
     */
    <B extends AMQMethodBody> boolean methodReceived(AMQMethodEvent<B> evt) throws QpidException;

    /**
     * Notifies the listener of an error on the event context to which it is listening. The listener should perform
     * any necessary clean-up for the context.
     *
     * @param e The underlying exception that is the source of the error.
     */
    void error(Exception e);
}
