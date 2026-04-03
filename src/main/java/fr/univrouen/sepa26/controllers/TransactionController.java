package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.SepaTransaction;
import fr.univrouen.sepa26.repository.SepaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private SepaTransactionRepository repository;

    // Sauvegarder une transaction
    @PostMapping(value = "/save",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public SepaTransaction saveTransaction(@RequestBody SepaTransaction transaction) {
        transaction = new SepaTransaction(
            transaction.getPmtId(),
            transaction.getAmount(),
            transaction.getCurrency(),
            transaction.getDebtorName(),
            transaction.getDebtorIban(),
            transaction.getCreditorName(),
            transaction.getCreditorIban(),
            transaction.getCreditorBic(),
            transaction.getRemittanceInfo(),
            LocalDateTime.now().toString()
        );
        return repository.save(transaction);
    }

    // Récupérer toutes les transactions
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SepaTransaction> getAllTransactions() {
        return repository.findAll();
    }

    // Récupérer par ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SepaTransaction getTransaction(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    // Supprimer par ID
    @DeleteMapping(value = "/{id}")
    public String deleteTransaction(@PathVariable String id) {
        repository.deleteById(id);
        return "Transaction " + id + " supprimée !";
    }
}