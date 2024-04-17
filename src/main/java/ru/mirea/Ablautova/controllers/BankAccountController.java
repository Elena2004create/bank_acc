package ru.mirea.Ablautova.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.Ablautova.dto.request.CreateAccountRequest;
import ru.mirea.Ablautova.dto.request.TransferRequest;
import ru.mirea.Ablautova.dto.response.AccountResponse;
import ru.mirea.Ablautova.services.BankAccountService;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        AccountResponse response = bankAccountService.createAccount(createAccountRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/{accountId}")
    public ResponseEntity<AccountResponse> add(@PathVariable Long accountId, @RequestParam Double amount) {
        AccountResponse response = bankAccountService.add(accountId, amount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountResponse> transfer(@RequestBody TransferRequest transferRequest) {
        AccountResponse response = bankAccountService.transfer(
                transferRequest.getSourceAccountId(),
                transferRequest.getTargetAccountId(),
                transferRequest.getAmount()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        AccountResponse response = bankAccountService.getAccount(accountId);
        return ResponseEntity.ok(response);
    }
}
