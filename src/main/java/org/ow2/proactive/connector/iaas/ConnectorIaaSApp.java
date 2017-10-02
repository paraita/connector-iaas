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
package org.ow2.proactive.connector.iaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.google.common.base.Predicates;

import lombok.NoArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@NoArgsConstructor
@SpringBootApplication(scanBasePackages = { "org.ow2.proactive.connector.iaas" })
@PropertySources({ @PropertySource(value = "classpath:application.properties"),
                   @PropertySource(value = "file:${proactive.home}/config/connector-iaas/application.properties", ignoreResourceNotFound = true) })
@EnableSwagger2
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.ow2.proactive.connector.iaas")
public class ConnectorIaaSApp {

    @Bean
    public Docket connectorIaaSApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                                                      .groupName("connector-iaas")
                                                      .select()
                                                      .apis(RequestHandlerSelectors.any())
                                                      .paths(PathSelectors.any())
                                                      .paths(Predicates.not(PathSelectors.regex("/error")))
                                                      .paths(Predicates.not(PathSelectors.regex("/")))
                                                      .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Connector IaaS Service API")
                                   .description("The purpose of this service is to provide a generic interface to public cloud providers.")
                                   .licenseUrl("https://github.com/ow2-proactive/cloud-automation-service/blob/master/LICENSE")
                                   .version("1.0")
                                   .build();
    }

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(ConnectorIaaSApp.class, args);
    }
}
