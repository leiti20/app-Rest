package fr.univrouen.sepa26.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class SepaTransaction {

    @Id
    private String id;
    private String pmtId;
    private double amount;
    private String currency;
    private String debtorName;
    private String debtorIban;
    private String creditorName;
    private String creditorIban;
    private String creditorBic;
    private String remittanceInfo;
    private String createdAt;

    public SepaTransaction() {}

    public SepaTransaction(String pmtId, double amount, String currency,
                           String debtorName, String debtorIban,
                           String creditorName, String creditorIban,
                           String creditorBic, String remittanceInfo,
                           String createdAt) {
        this.pmtId = pmtId;
        this.amount = amount;
        this.currency = currency;
        this.debtorName = debtorName;
        this.debtorIban = debtorIban;
        this.creditorName = creditorName;
        this.creditorIban = creditorIban;
        this.creditorBic = creditorBic;
        this.remittanceInfo = remittanceInfo;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getPmtId() { return pmtId; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getDebtorName() { return debtorName; }
    public String getDebtorIban() { return debtorIban; }
    public String getCreditorName() { return creditorName; }
    public String getCreditorIban() { return creditorIban; }
    public String getCreditorBic() { return creditorBic; }
    public String getRemittanceInfo() { return remittanceInfo; }
    public String getCreatedAt() { return createdAt; }
}