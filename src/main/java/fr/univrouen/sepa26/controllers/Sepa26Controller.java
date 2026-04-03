package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.SepaTransaction;
import fr.univrouen.sepa26.repository.SepaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.univrouen.sepa26.service.XSDValidator;

import java.util.List;

@Controller
public class Sepa26Controller {

    @Autowired
    private SepaTransactionRepository repository;

    // ─── GET /sepa26/resume/xml ───
    @GetMapping(value = "/sepa26/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String resumeXML() {
        List<SepaTransaction> transactions = repository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<transactions>");
        for (SepaTransaction t : transactions) {
            sb.append("<transaction>");
            sb.append("<id>").append(t.getId()).append("</id>");
            sb.append("<CreDtTm>").append(t.getCreatedAt()).append("</CreDtTm>");
            sb.append("<PmtId>").append(t.getPmtId()).append("</PmtId>");
            sb.append("<CtrlSum>").append(t.getAmount()).append("</CtrlSum>");
            sb.append("</transaction>");
        }
        sb.append("</transactions>");
        return sb.toString();
    }

    // ─── GET /sepa/resume/html ───
    @GetMapping("/sepa/resume/html")
    public String resumeHTML(Model model) {
        List<SepaTransaction> transactions = repository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
        model.addAttribute("transactions", transactions);
        return "resume";
    }

    // ─── GET /sepa26/xml/{id} ───
    @GetMapping(value = "/sepa26/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String detailXML(@PathVariable String id) {
        return repository.findById(id).map(t ->
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<transaction>" +
            "<id>" + t.getId() + "</id>" +
            "<PmtId>" + t.getPmtId() + "</PmtId>" +
            "<CreDtTm>" + t.getCreatedAt() + "</CreDtTm>" +
            "<CtrlSum>" + t.getAmount() + "</CtrlSum>" +
            "<currency>" + t.getCurrency() + "</currency>" +
            "<debtorName>" + t.getDebtorName() + "</debtorName>" +
            "<debtorIban>" + t.getDebtorIban() + "</debtorIban>" +
            "<creditorName>" + t.getCreditorName() + "</creditorName>" +
            "<creditorIban>" + t.getCreditorIban() + "</creditorIban>" +
            "<creditorBic>" + t.getCreditorBic() + "</creditorBic>" +
            "<remittanceInfo>" + t.getRemittanceInfo() + "</remittanceInfo>" +
            "</transaction>"
        ).orElse(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<error><id>" + id + "</id><status>ERROR</status></error>"
        );
    }

    // ─── GET /sepa26/html/{id} ───
    @GetMapping("/sepa26/html/{id}")
    public String detailHTML(@PathVariable String id, Model model) {
        return repository.findById(id).map(t -> {
            model.addAttribute("transaction", t);
            return "detail";
        }).orElseGet(() -> {
            model.addAttribute("id", id);
            return "error-transaction";
        });
    }

    // ─── DELETE /sepa26/delete/{id} ───
    @DeleteMapping(value = "/sepa26/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String delete(@PathVariable String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<response><id>" + id + "</id><status>DELETED</status></response>";
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<response><status>ERROR</status></response>";
    }

    // ─── POST /sepa26/insert ───
    @PostMapping(value = "/sepa26/insert",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String insert(@RequestBody SepaTransaction transaction) {
        boolean exists = repository.findByPmtId(transaction.getPmtId()).size() > 0;
        if (exists) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<response><status>ERROR</status></response>";
        }
        SepaTransaction saved = repository.save(transaction);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<response><id>" + saved.getId() + "</id><status>INSERTED</status></response>";
    }
    
    @Autowired
    private XSDValidator xsdValidator;

    @PostMapping(value = "/sepa26/insert",
                 consumes = MediaType.APPLICATION_XML_VALUE,
                 produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String insert(@RequestBody String xmlFlux) {

        // Validation XSD
        String error = xsdValidator.validate(xmlFlux);
        if (error != null) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<response><status>ERROR</status>" +
                   "<description>" + error + "</description></response>";
        }

        // Vérifier doublon sur PmtId
        String pmtId = extractTag(xmlFlux, "PmtId");
        if (pmtId != null && repository.findByPmtId(pmtId).size() > 0) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<response><status>ERROR</status>" +
                   "<description>Transaction déjà existante</description></response>";
        }

        // Sauvegarder
        SepaTransaction t = new SepaTransaction();
        t = new SepaTransaction(
            pmtId,
            Double.parseDouble(extractTag(xmlFlux, "CtrlSum") != null ? extractTag(xmlFlux, "CtrlSum") : "0"),
            "EUR",
            extractTag(xmlFlux, "Nm"),
            "", "", "", "", "",
            java.time.LocalDateTime.now().toString()
        );
        SepaTransaction saved = repository.save(t);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<response><id>" + saved.getId() + "</id><status>INSERTED</status></response>";
    }

    // Utilitaire pour extraire une balise XML
    private String extractTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        int end = xml.indexOf(close);
        if (start == -1 || end == -1) return null;
        return xml.substring(start + open.length(), end).trim();
    }
    
 // ─── GET /sepa26/search
    @GetMapping(value = "/sepa26/search", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String searchTransactions(@RequestParam(required = false) String date,
                                     @RequestParam(required = false) Double sum) {
        try {
            List<SepaTransaction> results;
            if (date != null && sum != null) {
                results = repository.findByCreatedAtGreaterThanEqualAndAmountGreaterThanEqual(date, sum);
            } else if (date != null) {
                results = repository.findByCreatedAtGreaterThanEqual(date);
            } else if (sum != null) {
                results = repository.findByAmountGreaterThanEqual(sum);
            } else {
                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><status>NONE</status></response>";
            }

            if (results.isEmpty()) {
                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><status>NONE</status></response>";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<transactions>\n");
            for (SepaTransaction t : results) {
                sb.append("  <transaction>\n");
                sb.append("    <id>").append(t.getId()).append("</id>\n");
                sb.append("    <CreDtTm>").append(t.getCreatedAt()).append("</CreDtTm>\n");
                sb.append("    <PmtId>").append(t.getPmtId()).append("</PmtId>\n");
                sb.append("    <CtrlSum>").append(t.getAmount()).append("</CtrlSum>\n");
                sb.append("  </transaction>\n");
            }
            sb.append("</transactions>");
            return sb.toString();

        } catch (Exception e) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><status>ERROR</status></response>";
        }
    }
}