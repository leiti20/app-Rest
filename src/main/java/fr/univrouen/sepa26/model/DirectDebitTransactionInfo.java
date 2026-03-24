package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitTransactionInfo {

    @XmlElement(name = "PmtId")
    private String pmtId;

    @XmlElement(name = "InstdAmt")
    private Amount instdAmt;

    @XmlElement(name = "Dbtr")
    private NamedElement dbtr;

    @XmlElement(name = "RmtInf")
    private String rmtInf;

    public DirectDebitTransactionInfo() {}
    public DirectDebitTransactionInfo(String pmtId, double amount, String dbtrNm, String rmtInf) {
        this.pmtId = pmtId;
        this.instdAmt = new Amount(amount);
        this.dbtr = new NamedElement(dbtrNm);
        this.rmtInf = rmtInf;
    }
    public String getPmtId() { return pmtId; }
    public Amount getInstdAmt() { return instdAmt; }
    public NamedElement getDbtr() { return dbtr; }
    public String getRmtInf() { return rmtInf; }
}