package fr.univrouen.sepa26;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExtractionLogicTest {

    private String extractTag(String xml, String tag) {
        if (xml == null || tag == null) return null;
        String open = "<" + tag; 
        int startPos = xml.indexOf(open);
        if (startPos == -1) return null;
        
        int startContent = xml.indexOf(">", startPos);
        if (startContent == -1) return null;
        
        String close = "</" + tag + ">";
        int endContent = xml.indexOf(close, startContent);
        if (endContent == -1) return null;
        
        return xml.substring(startContent + 1, endContent).trim();
    }

    @Test
    public void testFullExtraction() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Document>\n" +
            "  <GrpHdr>\n" +
            "    <CtrlSum>3250.15</CtrlSum>\n" +
            "  </GrpHdr>\n" +
            "  <PmtInf>\n" +
            "    <Cdtr><Nm>Societe XX</Nm></Cdtr>\n" +
            "    <CdtrAcct><Id><IBAN>FR7610041010050500013M02606</IBAN></Id></CdtrAcct>\n" +
            "    <CdtrAgt><FinInstnId><BIC>BANKFRPP</BIC></FinInstnId></CdtrAgt>\n" +
            "    <DrctDbtTxInf>\n" +
            "      <PmtId>REF OPE A123B</PmtId>\n" +
            "      <InstdAmt Ccy=\"EUR\">1100.07</InstdAmt>\n" +
            "      <Dbtr><Nm>Mr Debiteur N1</Nm></Dbtr>\n" +
            "      <DbtrAcct><Id><IBAN>FR763004136210001234567811</IBAN></Id></DbtrAcct>\n" +
            "      <RmtInf>Facture N1</RmtInf>\n" +
            "    </DrctDbtTxInf>\n" +
            "  </PmtInf>\n" +
            "</Document>";

        String dbtr = extractTag(xml, "Dbtr");
        String dbtrAcct = extractTag(xml, "DbtrAcct");
        String cdtr = extractTag(xml, "Cdtr");
        String cdtrAcct = extractTag(xml, "CdtrAcct");
        String cdtrAgt = extractTag(xml, "CdtrAgt");

        assertEquals("Mr Debiteur N1", extractTag(dbtr, "Nm"));
        assertEquals("FR763004136210001234567811", extractTag(dbtrAcct, "IBAN"));
        assertEquals("Societe XX", extractTag(cdtr, "Nm"));
        assertEquals("FR7610041010050500013M02606", extractTag(cdtrAcct, "IBAN"));
        assertEquals("BANKFRPP", extractTag(cdtrAgt, "BIC"));
        assertEquals("Facture N1", extractTag(xml, "RmtInf"));
        
        String amountStr = extractTag(xml, "CtrlSum");
        if (amountStr == null) amountStr = extractTag(xml, "InstdAmt");
        assertEquals("3250.15", amountStr);
        
        // Test fallback for InstdAmt with attribute
        String xml2 = "<DrctDbtTxInf><InstdAmt Ccy=\"EUR\">50.00</InstdAmt></DrctDbtTxInf>";
        String amt2 = extractTag(xml2, "InstdAmt");
        assertEquals("50.00", amt2);
    }
}
