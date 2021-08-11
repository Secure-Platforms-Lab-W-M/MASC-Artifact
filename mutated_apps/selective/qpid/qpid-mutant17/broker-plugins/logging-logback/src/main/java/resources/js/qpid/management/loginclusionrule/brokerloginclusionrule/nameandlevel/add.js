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
define(["dojo/dom",
        "dojo/query",
        "dojo/_base/array",
        "dijit/registry",
        "qpid/common/util",
        "dojo/parser",
        "dojo/text!loginclusionrule/nameandlevel/add.html",
        "dojo/domReady!"], function (dom, query, array, registry, util, parser, template)
{
    var addLogInclusionRule = {
        show: function (data)
        {
            var that = this;
            this.metadata = data.metadata;
            this.containerNode = data.containerNode;
            data.containerNode.innerHTML = template;
            return parser.parse(this.containerNode)
                .then(function (instances)
                {
                    var logLevelWidget = registry.byId("addLogInclusionRule.level");
                    var validValues = that.metadata.getMetaData(data.category, data.type).attributes.level.validValues;
                    var validValueStore = util.makeTypeStore(validValues);
                    logLevelWidget.set("store", validValueStore);
                });
        }
    };

    return addLogInclusionRule;
});
