package ru.mirea.Ablautova.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private Long sourceAccountId;
    private Long targetAccountId;
    private Double amount;
}

