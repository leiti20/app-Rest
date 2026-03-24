package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CstmrDrctDbtInitn")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Sepa26 {

    private String Ccy;
    private String MsgId;
    private String CreDtTm;

    public Sepa26(String ccy, String msgid, String credttm) {
        this.Ccy = ccy;
        this.MsgId = msgid;
        this.CreDtTm = credttm;
    }

    public Sepa26() {}

    @XmlAttribute
    public String getCcy() { return Ccy; }

    @XmlElement
    public String getMsgId() { return MsgId; }

    @XmlElement
    public String getCreDtTm() { return CreDtTm; }

    @Override
    public String toString() {
        return ("(" + MsgId + ") Le = " + CreDtTm);
    }
}