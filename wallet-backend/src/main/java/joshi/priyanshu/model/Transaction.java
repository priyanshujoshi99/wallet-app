package joshi.priyanshu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String _id;
    private UUID transactionID;
    private String userEmail;
    private String senderEmail;
    private String receiverEmail;
    private double transactionAmount;
    private String transactionType;
    private LocalDateTime transactionDateTime;
    private String transactionNote;
    private double balanceAfterTransaction;
}
