package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MandateInfo {
    @XmlElement(name = "MndtRltdInf")
    private MandateRelatedInfo mndtRltdInf;

    public MandateInfo() {}
    public MandateInfo(String mndtId, String dtOfSgntr) {
        this.mndtRltdInf = new MandateRelatedInfo(mndtId, dtOfSgntr);
    }
    public MandateRelatedInfo getMndtRltdInf() { return mndtRltdInf; }
}