package fr.univrouen.sepa26.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import fr.univrouen.sepa26.model.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetController {
	@GetMapping("/resume")
	public String getListSepa26InXML() {
		return "Envoi de la liste des flux SEPA enregistrés";
	}
	
	@GetMapping("/guid")
	public String getSepa26InXML(@RequestParam(value = "guid") String texte) {
	return ("Détail de la transaction SEPA " + texte);
	}
	
	@GetMapping("/test")
	public String getTest(
	    @RequestParam(value = "nb") String nb,
	    @RequestParam(value = "search") String search) {
	    return "Test :\nguid = " + nb + "\ntitre = " + search;
	}
	
	@RequestMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Sepa26 getXML() {
	    Sepa26 sepa = new Sepa26("123", "Test model", "2026-17-03T08:01:02");
	    return sepa;
	}
	
	@GetMapping(value = "/sepa26", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody CstmrDrctDbtInitn getSepa26() {

	    DirectDebitTransactionInfo tx1 = new DirectDebitTransactionInfo(
	        "REF OPE AAAA", 1100.07,
	        "MANDAT NO 55555", "2009-09-01",
	        "BANKFRPP", "Mr Debiteur N1",
	        "FR763004136210001234567811", "Facture N1");

	    DirectDebitTransactionInfo tx2 = new DirectDebitTransactionInfo(
	        "REF OPE BBBB", 2150.08,
	        "MANDAT NO 666666", "1989-07-03",
	        "BANKGBUL", "Mr Debiteur N2",
	        "GB29NWBK60161331926819", "Facture N2");

	    List<DirectDebitTransactionInfo> txList = new ArrayList<>();
	    txList.add(tx1);
	    txList.add(tx2);

	    PaymentInformation pmt = new PaymentInformation(
	        "REF Remise 123", 2, 3250.15,
	        "2009-09-10", "Societe XX",
	        "FR7610041010050500013M02606", "BANKFRPP",
	        txList);

	    GroupHeader grpHdr = new GroupHeader(
	        "MSGID-123456", "2009-09-04T14:25:00",
	        2, 3250.15, "Societe XX");

	    List<PaymentInformation> pmtList = new ArrayList<>();
	    pmtList.add(pmt);

	    return new CstmrDrctDbtInitn(grpHdr, pmtList);
	}
}
