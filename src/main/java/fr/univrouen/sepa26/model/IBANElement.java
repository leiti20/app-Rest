package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class IBANElement {
    @XmlElement(name = "IBAN")
    private String iban;

    public IBANElement() {}
    public IBANElement(String iban) { this.iban = iban; }
    public String getIban() { return iban; }
}