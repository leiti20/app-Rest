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

import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

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
                   "<description>" + simplifyError(error) + "</description></response>";
        }

        // Vérifier doublon sur PmtId
        String pmtId = extractTag(xmlFlux, "PmtId");
        if (pmtId != null && repository.findByPmtId(pmtId).size() > 0) {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                   "<response><status>ERROR</status>" +
                   "<description>Transaction déjà existante</description></response>";
        }

        // Sauvegarder
        String dbtr = extractTag(xmlFlux, "Dbtr");
        String dbtrAcct = extractTag(xmlFlux, "DbtrAcct");
        String cdtr = extractTag(xmlFlux, "Cdtr");
        String cdtrAcct = extractTag(xmlFlux, "CdtrAcct");
        String cdtrAgt = extractTag(xmlFlux, "CdtrAgt");

        String amountStr = extractTag(xmlFlux, "CtrlSum");
        if (amountStr == null) amountStr = extractTag(xmlFlux, "InstdAmt");

        SepaTransaction t = new SepaTransaction(
            pmtId,
            Double.parseDouble(amountStr != null ? amountStr : "0"),
            "EUR",
            extractTag(dbtr, "Nm"),
            extractTag(dbtrAcct, "IBAN"),
            extractTag(cdtr, "Nm"),
            extractTag(cdtrAcct, "IBAN"),
            extractTag(cdtrAgt, "BIC"),
            extractTag(xmlFlux, "RmtInf"),
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        SepaTransaction saved = repository.save(t);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<response><id> " + saved.getId() + " </id><status>INSERTED</status></response>";
    }

    // ─── NOUVEAU : Gestion Web (Upload / Edit / Delete) ───

    @GetMapping("/sepa26/upload")
    public String uploadPage() {
        return "upload";
    }

    @GetMapping("/sepa26/transfer")
    public String transferToolPage() {
        return "transfer-tool";
    }

    @PostMapping("/sepa26/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "Fichier vide !");
                return "upload";
            }
            String xmlFlux = new String(file.getBytes(), StandardCharsets.UTF_8);
            
            // On réutilise la logique d'insertion existante
            String response = insert(xmlFlux); 
            if (response.contains("<status>ERROR</status>")) {
                String errorMsg = extractTag(response, "description");
                model.addAttribute("error", errorMsg != null ? errorMsg : "Erreur lors de l'insertion.");
                return "upload";
            }
            
            model.addAttribute("message", "Fichier importé avec succès !");
            return "upload";
            
        } catch (Exception e) {
            model.addAttribute("error", "Erreur serveur : " + e.getMessage());
            return "upload";
        }
    }

    @GetMapping("/sepa26/edit/{id}")
    public String editPage(@PathVariable String id, Model model) {
        Optional<SepaTransaction> t = repository.findById(id);
        if (t.isPresent()) {
            model.addAttribute("transaction", t.get());
            return "edit";
        }
        return "redirect:/sepa/resume/html";
    }

    @PostMapping("/sepa26/update/{id}")
    public String updateTransaction(@PathVariable String id, @ModelAttribute SepaTransaction transaction) {
        transaction.setId(id);
        repository.save(transaction);
        return "redirect:/sepa26/html/" + id;
    }

    @GetMapping("/sepa26/delete-html/{id}")
    public String deleteHtml(@PathVariable String id) {
        repository.deleteById(id);
        return "redirect:/sepa/resume/html";
    }

    // Utilitaire pour extraire une balise XML
    private String extractTag(String xml, String tag) {
        if (xml == null || tag == null) return null;
        String open = "<" + tag; // Peut avoir des attributs : <Tag Ccy="EUR">
        int startPos = xml.indexOf(open);
        if (startPos == -1) return null;
        
        // Trouver la fin de la balise ouvrante
        int startContent = xml.indexOf(">", startPos);
        if (startContent == -1) return null;
        
        String close = "</" + tag + ">";
        int endContent = xml.indexOf(close, startContent);
        if (endContent == -1) return null;
        
        return xml.substring(startContent + 1, endContent).trim();
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

    private String simplifyError(String error) {
        if (error == null) return null;
        
        // Champs manquants
        if (error.contains("cvc-complex-type.2.4.a") || error.contains("cvc-complex-type.2.4.b")) {
            if (error.contains("MsgId")) return "Le champ 'MsgId' (identifiant du message) est manquant.";
            if (error.contains("CreDtTm")) return "La date de création du flux 'CreDtTm' est manquante ou mal placée.";
            if (error.contains("NbOfTxs")) return "Le nombre de transactions 'NbOfTxs' est manquant.";
            if (error.contains("CtrlSum")) return "Le montant total 'CtrlSum' est manquant.";
            if (error.contains("PmtInfId")) return "L'identifiant de paiement 'PmtInfId' est manquant.";
            if (error.contains("InstdAmt")) return "Le montant de la transaction 'InstdAmt' est manquant.";
            return "Structure XML invalide : un élément obligatoire est manquant ou mal ordonné.";
        }
        
        // Valeur fixe invalide (ex: SEPA)
        if (error.contains("cvc-type.3.1.3") && error.contains("fixed")) {
            return "Une valeur spécifique (ex: 'SEPA') n'est pas respectée ou est mal orthographiée.";
        }
        
        // Format invalide (IBAN, BIC, etc)
        if (error.contains("cvc-datatype-valid.1.2.1") || error.contains("cvc-pattern-valid")) {
            if (error.contains("IBANType")) return "Le format de l'IBAN est invalide.";
            if (error.contains("BICType")) return "Le code BIC (identifiant banque) est invalide.";
            if (error.contains("dateTime")) return "Le format de la date et heure est invalide (attendu: YYYY-MM-DDTHH:MM:SS).";
            if (error.contains("date")) return "Le format de la date est invalide (attendu: YYYY-MM-DD).";
            return "Le format d'un des champs (IBAN, BIC, date) est incorrect.";
        }

        // Nettoyage générique
        return error.replace("{\"http://univ.fr/sepa\":", "'").replace("}", "'");
    }
}