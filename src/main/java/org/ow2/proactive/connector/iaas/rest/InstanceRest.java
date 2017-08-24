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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.proactive.connector.iaas.model.Instance;
import org.ow2.proactive.connector.iaas.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aol.micro.server.rest.jackson.JacksonUtil;


@RestController
@RequestMapping(value = "/infrastructures")
public class InstanceRest {

    @Autowired
    private InstanceService instanceService;

    @RequestMapping(value = "{infrastructureId}/instances", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public ResponseEntity<?> createInstance(@PathParam("infrastructureId") String infrastructureId,
            final String instanceJson) {
        Instance instance = JacksonUtil.convertFromJson(instanceJson, Instance.class);
        return ResponseEntity.ok(instanceService.createInstance(infrastructureId, instance));
    }

    @RequestMapping(value = "{infrastructureId}/instances", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> getInstances(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("instanceId") String instanceId, @QueryParam("instanceTag") String instanceTag,
            @QueryParam("allInstances") Boolean allInstances) {

        if (Optional.ofNullable(instanceId).isPresent()) {
            return ResponseEntity.ok(instanceService.getInstanceById(infrastructureId, instanceId));
        } else if (Optional.ofNullable(instanceTag).isPresent()) {
            return ResponseEntity.ok(instanceService.getInstanceByTag(infrastructureId, instanceTag));
        } else if (Optional.ofNullable(allInstances).isPresent() && allInstances) {
            return ResponseEntity.ok(instanceService.getAllInstances(infrastructureId));
        } else {
            return ResponseEntity.ok(instanceService.getCreatedInstances(infrastructureId));
        }
    }

    @RequestMapping(value = "{infrastructureId}/instances", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> deleteInstance(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("instanceId") String instanceId, @QueryParam("instanceTag") String instanceTag,
            @QueryParam("allCreatedInstances") Boolean allCreatedInstances) {

        if (Optional.ofNullable(instanceId).isPresent()) {
            instanceService.deleteInstance(infrastructureId, instanceId);
        } else if (Optional.ofNullable(instanceTag).isPresent()) {
            instanceService.deleteInstanceByTag(infrastructureId, instanceTag);
        } else if (Optional.ofNullable(allCreatedInstances).isPresent() && allCreatedInstances) {
            instanceService.deleteCreatedInstances(infrastructureId);
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "{infrastructureId}/instances/publicIp", method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> createPublicIp(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("instanceId") String instanceId, @QueryParam("instanceTag") String instanceTag,
            @QueryParam("desiredIp") String optionalDesiredIp) {
        Map<String, String> response = new HashMap<String, String>();
        if (Optional.ofNullable(instanceId).isPresent()) {
            response.put("publicIp",
                         instanceService.addToInstancePublicIp(infrastructureId, instanceId, optionalDesiredIp));
        } else if (Optional.ofNullable(instanceTag).isPresent()) {
            instanceService.addInstancePublicIpByTag(infrastructureId, instanceTag, optionalDesiredIp);
        } else {
            throw new ClientErrorException("The parameter \"instanceId\" and \"instanceTag\" are  missing.",
                                           Response.Status.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "{infrastructureId}/instances/publicIp", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> removePublicIp(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("instanceId") String instanceId, @QueryParam("instanceTag") String instanceTag,
            @QueryParam("desiredIp") String optionalDesiredIp) {

        if (Optional.ofNullable(instanceId).isPresent()) {
            instanceService.removeInstancePublicIp(infrastructureId, instanceId, optionalDesiredIp);
        } else if (Optional.ofNullable(instanceTag).isPresent()) {
            instanceService.removeInstancePublicIpByTag(infrastructureId, instanceTag, optionalDesiredIp);
        } else {
            throw new ClientErrorException("The parameters \"instanceId\" and \"instanceTag\" are missing.",
                                           Response.Status.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

}
