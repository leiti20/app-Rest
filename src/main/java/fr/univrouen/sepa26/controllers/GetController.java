package fr.univrouen.sepa26.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import fr.univrouen.sepa26.model.*;
import java.util.Arrays;

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

	    // Transactions
	    DirectDebitTransactionInfo tx1 = new DirectDebitTransactionInfo(
	        "REF OPE AAAA", 1100.07, "Mr Debiteur N1", "Facture N1");
	    DirectDebitTransactionInfo tx2 = new DirectDebitTransactionInfo(
	        "REF OPE BBBB", 2150.08, "Mr Debiteur N2", "Facture N2");

	    // Lot
	    PaymentInformation pmt = new PaymentInformation(
	        "REF Remise 123", 2, 3250.15,
	        "2009-09-10", "Societe XX",
	        Arrays.asList(tx1, tx2));

	    // Header
	    GroupHeader grpHdr = new GroupHeader(
	        "MSGID-123456", "2009-09-04T14:25:00",
	        2, 3250.15, "Societe XX");

	    return new CstmrDrctDbtInitn(grpHdr, Arrays.asList(pmt));
	}
}
