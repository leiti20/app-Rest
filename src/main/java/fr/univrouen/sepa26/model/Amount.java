package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Amount {
    @XmlAttribute(name = "Ccy")
    private String ccy = "EUR";

    @XmlValue
    private double value;

    public Amount() {}
    public Amount(double value) { this.value = value; }
    public String getCcy() { return ccy; }
    public double getValue() { return value; }
}