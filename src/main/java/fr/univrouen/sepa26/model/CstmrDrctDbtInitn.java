package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class CstmrDrctDbtInitn {

    @XmlElement(name = "CstmrDrctDbtInitn")
    private Content content;

    public CstmrDrctDbtInitn() {}
    public CstmrDrctDbtInitn(GroupHeader grpHdr, List<PaymentInformation> pmtInf) {
        this.content = new Content(grpHdr, pmtInf);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Content {
        @XmlElement(name = "GrpHdr")
        private GroupHeader grpHdr;

        @XmlElement(name = "PmtInf")
        private List<PaymentInformation> pmtInf;

        public Content() {}
        public Content(GroupHeader grpHdr, List<PaymentInformation> pmtInf) {
            this.grpHdr = grpHdr;
            this.pmtInf = pmtInf;
        }
    }
}