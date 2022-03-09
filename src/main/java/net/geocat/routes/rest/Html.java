/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.routes.rest;


import net.geocat.service.html.HtmlCapabilitiesService;
import net.geocat.service.html.HtmlDatasetService;
import net.geocat.service.html.HtmlDiscoverService;
import net.geocat.service.html.HtmlScrapeService;
import net.geocat.service.html.HtmlServiceService;
import net.geocat.service.html.HtmlSummaryService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Html extends RouteBuilder {

    @Value("${geocat.jettyHost}")
    public String jettyHost;

    @Value("${geocat.jettyPort}")
    public Integer jettyPort;


    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        // JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/html/service/")
                .get("/{processID}/{fileId}")
                .route()
                .routeId("rest.rest.html.service")
                .bean(HtmlServiceService.class, "getHtml( ${header.processID}, ${header.fileId} )", BeanScope.Request)
                .setHeader("content-type", constant("text/html"));

        rest("/api/html/capabilities/")
                .get("/{processID}/{fileId}")
                .route()
                .routeId("rest.rest.html.capabilities")
                .bean(HtmlCapabilitiesService.class, "getHtml( ${header.processID}, ${header.fileId} )", BeanScope.Request)
                .setHeader("content-type", constant("text/html")) ;

        rest("/api/html/dataset/")
                .get("/{processID}/{fileId}")
                .route()
                .routeId("rest.rest.html.dataset")
                .bean(HtmlDatasetService.class, "getHtml( ${header.processID}, ${header.fileId} )", BeanScope.Request)

                .setHeader("content-type", constant("text/html"))
        ;

        rest("/api/html/discover/")
                .get("/{fileId}")
                .route()
                .routeId("rest.rest.html.discover")
                .bean(HtmlDiscoverService.class, "getHtml(  ${header.fileId} )", BeanScope.Request)

                .setHeader("content-type", constant("text/html"))
        ;

        rest("/api/html/scrape/")
                .get("/{processID}/{country}")
                .route()
                .routeId("rest.rest.html.scrape")
                .bean(HtmlScrapeService.class, "getHtml( ${header.processID}, ${header.country} )", BeanScope.Request)

                .setHeader("content-type", constant("text/html"))
        ;
        rest("/api/html/summary/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.html.summary")
                .bean(HtmlSummaryService.class, "getHtml( ${header.processID}  )", BeanScope.Request)

                .setHeader("content-type", constant("text/html"))
        ;
    }
}
