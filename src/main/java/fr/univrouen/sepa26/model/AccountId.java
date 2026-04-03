package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountId {
    @XmlElement(name = "Id")
    private IBANElement id;

    public AccountId() {}
    public AccountId(String iban) { this.id = new IBANElement(iban); }
    public IBANElement getId() { return id; }
}