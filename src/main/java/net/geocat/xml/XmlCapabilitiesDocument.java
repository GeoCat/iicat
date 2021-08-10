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

package net.geocat.xml;

import net.geocat.service.capabilities.DatasetLink;
import net.geocat.xml.helpers.CapabilitiesType;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class XmlCapabilitiesDocument extends XmlDoc {

    boolean hasExtendedCapabilities;
    String metadataUrlRaw;
    CapabilitiesType capabilitiesType;

    List<DatasetLink> datasetLinksList;


    public XmlCapabilitiesDocument(XmlDoc doc, CapabilitiesType type) throws Exception {
        super(doc);
        datasetLinksList = new ArrayList<>();
        this.capabilitiesType = type;
        setup_XmlCapabilitiesDocument();
    }

    public static XmlCapabilitiesDocument create(XmlDoc doc, CapabilitiesType type) throws Exception {
        switch (type) {
            case WFS:
                return new XmlCapabilitiesWFS(doc);
            case WMS:
                return new XmlCapabilitiesWMS(doc);
            case WMTS:
                return new XmlCapabilitiesWMTS(doc);
            case Atom:
                return new XmlCapabilitiesAtom(doc);
            case CSW:
                return new XmlCapabilitiesCSW(doc);
        }
        throw new Exception("XmlCapabilitiesDocument - unknown type");
    }

    private void setup_XmlCapabilitiesDocument() throws Exception {
        setupExtendedCap();
    }

    private void setupExtendedCap() throws Exception {
        //we use this notation because of issues with NS accross servers
        Node n = xpath_node("//*[local-name()='ExtendedCapabilities']");
        Node nnn = xpath_node("//*[local-name()='feed']/*[local-name()='link'][@rel=\"describedby\"]/@href");

        hasExtendedCapabilities = (n != null) || (nnn != null);
        if (!hasExtendedCapabilities)
            return;

        if (n != null) {
            setup_extendedcap(n);
            return;
        }

        if (nnn != null) {
            setup_inspire_atom(nnn);
            return;
        }
    }

    private void setup_extendedcap(Node n) throws Exception {
        if (n != null) {
            Node nn = XmlDoc.xpath_node(n, "//inspire_common:MetadataUrl/inspire_common:URL");
            if (nn != null)
                this.metadataUrlRaw = nn.getTextContent().trim();
        }
    }

    private void setup_inspire_atom(Node n) throws Exception {
        if (n != null) {
            this.metadataUrlRaw = n.getTextContent().trim();
        }
    }

    public boolean isHasExtendedCapabilities() {
        return hasExtendedCapabilities;
    }

    public void setHasExtendedCapabilities(boolean hasExtendedCapabilities) {
        this.hasExtendedCapabilities = hasExtendedCapabilities;
    }

    public String getMetadataUrlRaw() {
        return metadataUrlRaw;
    }

    public void setMetadataUrlRaw(String metadataUrlRaw) {
        this.metadataUrlRaw = metadataUrlRaw;
    }

    public CapabilitiesType getCapabilitiesType() {
        return capabilitiesType;
    }

    public void setCapabilitiesType(CapabilitiesType capabilitiesType) {
        this.capabilitiesType = capabilitiesType;
    }

    public List<DatasetLink> getDatasetLinksList() {
        return datasetLinksList;
    }

    public void setDatasetLinksList(List<DatasetLink> datasetLinksList) {
        this.datasetLinksList = datasetLinksList;
    }
}
