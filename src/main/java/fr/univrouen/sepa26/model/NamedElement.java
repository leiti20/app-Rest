package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class NamedElement {
    @XmlElement(name = "Nm")
    private String nm;

    public NamedElement() {}
    public NamedElement(String nm) { this.nm = nm; }
    public String getNm() { return nm; }
}