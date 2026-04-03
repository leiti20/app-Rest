package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInformation {

    @XmlElement(name = "PmtInfId")
    private String pmtInfId;

    @XmlElement(name = "NbOfTxs")
    private int nbOfTxs;

    @XmlElement(name = "CtrlSum")
    private double ctrlSum;

    @XmlElement(name = "ReqdColltnDt")
    private String reqdColltnDt;

    @XmlElement(name = "Cdtr")
    private NamedElement cdtr;

    @XmlElement(name = "CdtrAcct")
    private AccountId cdtrAcct;

    @XmlElement(name = "CdtrAgt")
    private AgentId cdtrAgt;

    @XmlElement(name = "DrctDbtTxInf")
    private List<DirectDebitTransactionInfo> transactions;

    public PaymentInformation() {}
    public PaymentInformation(String pmtInfId, int nbOfTxs, double ctrlSum,
                               String reqdColltnDt, String cdtrNm,
                               String cdtrIban, String cdtrBic,
                               List<DirectDebitTransactionInfo> transactions) {
        this.pmtInfId = pmtInfId;
        this.nbOfTxs = nbOfTxs;
        this.ctrlSum = ctrlSum;
        this.reqdColltnDt = reqdColltnDt;
        this.cdtr = new NamedElement(cdtrNm);
        this.cdtrAcct = new AccountId(cdtrIban);
        this.cdtrAgt = new AgentId(cdtrBic);
        this.transactions = transactions;
    }
    public List<DirectDebitTransactionInfo> getTransactions() { return transactions; }
}