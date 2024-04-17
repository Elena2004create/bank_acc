package ru.mirea.Ablautova.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long accountId;
    private UUID userId;
    private Double balance;
}

