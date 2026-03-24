package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "CstmrDrctDbtInitn")
@XmlAccessorType(XmlAccessType.FIELD)
public class CstmrDrctDbtInitn {

    @XmlElement(name = "GrpHdr")
    private GroupHeader grpHdr;

    @XmlElement(name = "PmtInf")
    private List<PaymentInformation> pmtInf;

    public CstmrDrctDbtInitn() {}
    public CstmrDrctDbtInitn(GroupHeader grpHdr, List<PaymentInformation> pmtInf) {
        this.grpHdr = grpHdr;
        this.pmtInf = pmtInf;
    }
    public GroupHeader getGrpHdr() { return grpHdr; }
    public List<PaymentInformation> getPmtInf() { return pmtInf; }
}