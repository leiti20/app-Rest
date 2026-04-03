package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class DirectDebitTransactionInfo {

    @XmlElement(name = "PmtId")
    private String pmtId;

    @XmlElement(name = "InstdAmt")
    private Amount instdAmt;

    @XmlElement(name = "DrctDbtTx")
    private MandateInfo drctDbtTx;

    @XmlElement(name = "DbtrAgt")
    private AgentId dbtrAgt;

    @XmlElement(name = "Dbtr")
    private NamedElement dbtr;

    @XmlElement(name = "DbtrAcct")
    private AccountId dbtrAcct;

    @XmlElement(name = "RmtInf")
    private String rmtInf;

    public DirectDebitTransactionInfo() {}
    public DirectDebitTransactionInfo(String pmtId, double amount,
            String mndtId, String dtOfSgntr,
            String bic, String dbtrNm,
            String iban, String rmtInf) {
        this.pmtId = pmtId;
        this.instdAmt = new Amount(amount);
        this.drctDbtTx = new MandateInfo(mndtId, dtOfSgntr);
        this.dbtrAgt = new AgentId(bic);
        this.dbtr = new NamedElement(dbtrNm);
        this.dbtrAcct = new AccountId(iban);
        this.rmtInf = rmtInf;
    }
    public String getPmtId() { return pmtId; }
    public Amount getInstdAmt() { return instdAmt; }
    public NamedElement getDbtr() { return dbtr; }
    public String getRmtInf() { return rmtInf; }
}