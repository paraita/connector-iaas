/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.connector.iaas.rest;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.proactive.connector.iaas.model.InstanceScript;
import org.ow2.proactive.connector.iaas.model.ScriptResult;
import org.ow2.proactive.connector.iaas.service.InstanceScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aol.micro.server.rest.jackson.JacksonUtil;
import com.google.common.collect.Lists;


@RestController
@RequestMapping(value = "/infrastructures")
public class InstanceScriptRest {

    @Autowired
    private InstanceScriptService instanceScriptService;

    @RequestMapping(value = "{infrastructureId}/instances/scripts", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> executeScript(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("instanceId") String instanceId, @QueryParam("instanceTag") String instanceTag,
            final String instanceScriptJson) {

        InstanceScript instanceScript = JacksonUtil.convertFromJson(instanceScriptJson, InstanceScript.class);

        final List<ScriptResult> scriptResults = Optional.ofNullable(instanceId)
                                                         .map(i -> Lists.newArrayList(instanceScriptService.executeScriptOnInstance(infrastructureId,
                                                                                                                                    instanceId,
                                                                                                                                    instanceScript)))
                                                         .orElseGet(() -> Lists.newArrayList(instanceScriptService.executeScriptOnInstanceTag(infrastructureId,
                                                                                                                                              instanceTag,
                                                                                                                                              instanceScript)));

        return ResponseEntity.ok(scriptResults);
    }

}
