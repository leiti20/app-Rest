package fr.univrouen.sepa26.repository;

import fr.univrouen.sepa26.model.SepaTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SepaTransactionRepository extends MongoRepository<SepaTransaction, String> {
    List<SepaTransaction> findByDebtorName(String debtorName);
    List<SepaTransaction> findByPmtId(String pmtId);
    List<SepaTransaction> findByCreatedAtGreaterThanEqual(String date);
    List<SepaTransaction> findByAmountGreaterThanEqual(Double amount);
    List<SepaTransaction> findByCreatedAtGreaterThanEqualAndAmountGreaterThanEqual(String date, Double amount);
}