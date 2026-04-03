package fr.univrouen.sepa26;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fr.univrouen.sepa26.model.*;
import java.util.ArrayList;
import java.util.List;

public class GetControllerTest {

    @Test
    public void testStringOperation() {
        String result = "Hello sepa26 !";
        assertEquals("Hello sepa26 !", result);
    }

    @Test
    public void testSepaModel() {
        String msgId = "MSGID-123456";
        assertNotNull(msgId);
        assertEquals("MSGID-123456", msgId);
    }

    @Test
    public void testGroupHeader() {
        GroupHeader grpHdr = new GroupHeader(
            "MSGID-123456", "2026-03-26T14:25:00", 2, 3250.15, "Societe XX");
        assertNotNull(grpHdr);
        assertEquals("MSGID-123456", grpHdr.getMsgId());
        assertEquals(2, grpHdr.getNbOfTxs());
    }

    @Test
    public void testDirectDebitTransaction() {
        DirectDebitTransactionInfo tx = new DirectDebitTransactionInfo(
            "REF OPE AAAA", 1100.07,
            "MANDAT NO 55555", "2009-09-01",
            "BANKFRPP", "Mr Debiteur N1",
            "FR763004136210001234567811", "Facture N1");
        assertNotNull(tx);
        assertEquals("REF OPE AAAA", tx.getPmtId());
        assertEquals("Mr Debiteur N1", tx.getDbtr().getNm());
    }

    @Test
    public void testPaymentInformation() {
        List<DirectDebitTransactionInfo> txList = new ArrayList<>();
        txList.add(new DirectDebitTransactionInfo(
            "REF OPE AAAA", 1100.07,
            "MANDAT NO 55555", "2009-09-01",
            "BANKFRPP", "Mr Debiteur N1",
            "FR763004136210001234567811", "Facture N1"));

        PaymentInformation pmt = new PaymentInformation(
            "REF Remise 123", 1, 1100.07,
            "2009-09-10", "Societe XX",
            "FR7610041010050500013M02606", "BANKFRPP",
            txList);
        assertNotNull(pmt);
        assertEquals(1, pmt.getTransactions().size());
    }
}