package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class AgentId {
    @XmlElement(name = "FinInstnId")
    private BICElement finInstnId;

    public AgentId() {}
    public AgentId(String bic) { this.finInstnId = new BICElement(bic); }
    public BICElement getFinInstnId() { return finInstnId; }
}