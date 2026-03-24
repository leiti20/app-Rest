package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class GroupHeader {
    @XmlElement(name = "MsgId")
    private String msgId;

    @XmlElement(name = "CreDtTm")
    private String creDtTm;

    @XmlElement(name = "NbOfTxs")
    private int nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private double ctrlSum;

    @XmlElement(name = "InitgPty")
    private NamedElement initgPty;

    public GroupHeader() {}
    public GroupHeader(String msgId, String creDtTm, int nbOfTxs, double ctrlSum, String initgPtyNm) {
        this.msgId = msgId;
        this.creDtTm = creDtTm;
        this.nbOfTxs = nbOfTxs;
        this.ctrlSum = ctrlSum;
        this.initgPty = new NamedElement(initgPtyNm);
    }
    // Getters
    public String getMsgId() { return msgId; }
    public String getCreDtTm() { return creDtTm; }
    public int getNbOfTxs() { return nbOfTxs; }
    public double getCtrlSum() { return ctrlSum; }
    public NamedElement getInitgPty() { return initgPty; }
}