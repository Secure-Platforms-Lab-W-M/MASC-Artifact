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
define(["qpid/common/util",
        "dojo/_base/array",
        "dojo/json",
        "dojo/string",
        "dojo/store/Memory",
        "dojo/dom",
        "dojo/dom-construct",
        "dijit/registry",
        "dojo/domReady!"], function (util, array, json, string, Memory, dom, domConstruct, registry)
{
    return {
        show: function (data)
        {
            var that = this;
            util.parseHtmlIntoDiv(data.containerNode, "virtualhostnode/jdbc/edit.html", function ()
            {
                that._postParse(data);
            });
        },
        _postParse: function (data)
        {
            registry.byId("editVirtualHostNode.connectionUrl")
                .set("regExpGen", util.jdbcUrlOrContextVarRegexp);
            registry.byId("editVirtualHostNode.username")
                .set("regExpGen", util.nameOrContextVarRegexp);

            var typeMetaData = data.metadata.getMetaData("VirtualHostNode", "JDBC");
            var poolTypes = typeMetaData.attributes.connectionPoolType.validValues;
            var poolTypesData = [];
            array.forEach(poolTypes, function (item)
            {
                poolTypesData.push({
                    id: item,
                    name: item
                });
            });
            var poolTypesStore = new Memory({data: poolTypesData});
            var poolTypeControl = registry.byId("editVirtualHostNode.connectionPoolType");
            poolTypeControl.set("store", poolTypesStore);
            poolTypeControl.set("value", data.data.connectionPoolType);

            var passwordControl = registry.byId("editVirtualHostNode.password");
            if (data.data.password)
            {
                passwordControl.set("placeHolder", "*******");
            }

            var poolTypeFieldsDiv = dom.byId("editVirtualHostNode.poolSpecificDiv");
            poolTypeControl.on("change", function (type)
            {
                if (type && string.trim(type) != "")
                {
                    var widgets = registry.findWidgets(poolTypeFieldsDiv);
                    array.forEach(widgets, function (item)
                    {
                        item.destroyRecursive();
                    });
                    domConstruct.empty(poolTypeFieldsDiv);

                    require(["qpid/management/store/pool/" + type.toLowerCase() + "/edit"], function (poolType)
                    {
                        poolType.show({
                            containerNode: poolTypeFieldsDiv,
                            data: data.data,
                            context: data.parent.context
                        })

                        if (!(data.data.state == "STOPPED" || data.data.state == "ERRORED"))
                        {
                            var widgets = registry.findWidgets(poolTypeFieldsDiv);
                            array.forEach(widgets, function (item)
                            {
                                item.set("disabled", true);
                            });
                        }
                    });
                }
            });

            util.applyToWidgets(data.containerNode, "VirtualHostNode", data.data.type, data.data, data.metadata);
        }
    };
});
