@XmlSchema(
    namespace = "http://univ.fr/sepa",
    xmlns = {
        @XmlNs(prefix = "", namespaceURI = "http://univ.fr/sepa"),
        @XmlNs(prefix = "xsi", namespaceURI = "http://www.w3.org/2001/XMLSchema-instance")
    },
    elementFormDefault = XmlNsForm.QUALIFIED
)
package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;