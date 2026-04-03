package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MandateRelatedInfo {
    @XmlElement(name = "MndtId")
    private String mndtId;

    @XmlElement(name = "DtOfSgntr")
    private String dtOfSgntr;

    public MandateRelatedInfo() {}
    public MandateRelatedInfo(String mndtId, String dtOfSgntr) {
        this.mndtId = mndtId;
        this.dtOfSgntr = dtOfSgntr;
    }
    public String getMndtId() { return mndtId; }
    public String getDtOfSgntr() { return dtOfSgntr; }
}