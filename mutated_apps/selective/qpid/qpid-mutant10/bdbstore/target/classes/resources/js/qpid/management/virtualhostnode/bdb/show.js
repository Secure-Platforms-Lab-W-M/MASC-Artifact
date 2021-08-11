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
define(["qpid/common/util", "dojo/domReady!"], function (util)
{
    var fieldNames = ["storePath"];

    function BdbNode(data)
    {
        this.parent = data.parent;
        util.buildUI(data.containerNode, data.parent, "virtualhostnode/bdb/show.html", fieldNames, this);
    }

    BdbNode.prototype.update = function (data)
    {
        util.updateUI(data, fieldNames, this);
        this.parent.editNodeButton.set("disabled", !(data.state == "STOPPED" || data.state == "ERRORED"));
    };

    return BdbNode;
});
