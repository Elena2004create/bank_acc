package ru.mirea.Ablautova.services;

import ru.mirea.Ablautova.dto.request.CreateAccountRequest;
import ru.mirea.Ablautova.dto.response.AccountResponse;

public interface BankAccountService {
    AccountResponse createAccount(CreateAccountRequest createAccountRequest);
    AccountResponse add(Long accountId, Double amount);
    AccountResponse transfer(Long sourceAccountId, Long targetAccountId, Double amount);
    AccountResponse getAccount(Long accountId);
}
