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

import java.util.Optional;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.proactive.connector.iaas.model.Infrastructure;
import org.ow2.proactive.connector.iaas.service.InfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aol.micro.server.rest.jackson.JacksonUtil;


@RestController
@RequestMapping(value = "/infrastructures")
public class InfrastructureRest {

    @Autowired
    private InfrastructureService infrastructureService;

    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> getAllSupportedInfrastructure() {
        return ResponseEntity.ok(infrastructureService.getAllSupportedInfrastructure());
    }

    @RequestMapping(value = "/{infrastructureId}", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> getInfrastructure(@PathParam("infrastructureId") String infrastructureId) {
        return ResponseEntity.ok(infrastructureService.getInfrastructure(infrastructureId));
    }

    @RequestMapping(value = "/{infrastructureId}", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> deleteInfrastructureById(@PathParam("infrastructureId") String infrastructureId,
            @QueryParam("deleteInstances") Boolean deleteInstances) {
        Optional.ofNullable(infrastructureService.getInfrastructure(infrastructureId)).ifPresent(infrastructure -> {
            if (Optional.ofNullable(deleteInstances).orElse(false)) {
                infrastructureService.deleteInfrastructureWithCreatedInstances(infrastructure);
            } else {
                infrastructureService.deleteInfrastructure(infrastructure);
            }
        });
        return ResponseEntity.ok(infrastructureService.getAllSupportedInfrastructure());
    }

    @RequestMapping(method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<?> registerInfrastructure(final String infrastructureJson) {
        Infrastructure infrastructure = JacksonUtil.convertFromJson(infrastructureJson, Infrastructure.class);
        return ResponseEntity.ok(infrastructureService.registerInfrastructure(infrastructure));
    }

}
