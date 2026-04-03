package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class BICElement {
    @XmlElement(name = "BIC")
    private String bic;

    public BICElement() {}
    public BICElement(String bic) { this.bic = bic; }
    public String getBic() { return bic; }
}