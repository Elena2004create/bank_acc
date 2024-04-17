package ru.mirea.Ablautova.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mirea.Ablautova.dto.UserModel;
import ru.mirea.Ablautova.dto.request.CreateAccountRequest;
import ru.mirea.Ablautova.dto.response.AccountResponse;
import ru.mirea.Ablautova.entities.BankAccountEntity;
import ru.mirea.Ablautova.repositories.BankAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mirea.Ablautova.repositories.UsersRepository;
import ru.mirea.Ablautova.security.UserDetailsServiceImpl;

// ...
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final UsersRepository usersRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel authenticatedUser = (UserModel) userDetailsServiceImpl.loadUserByUsername((String)authentication.getPrincipal());

        BankAccountEntity newAccount = new BankAccountEntity();
        newAccount.setUser(usersRepository.findByEmail(authenticatedUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден")));

        newAccount.setBalance(createAccountRequest.getInitialBalance());

        BankAccountEntity savedAccount = bankAccountRepository.save(newAccount);
        return mapEntityToResponse(savedAccount);
    }

    @Override
    public AccountResponse add(Long accountId, Double amount) {
        BankAccountEntity account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));

        account.setBalance(account.getBalance() + amount);
        BankAccountEntity updatedAccount = bankAccountRepository.save(account);
        return mapEntityToResponse(updatedAccount);
    }

    @Override
    public AccountResponse transfer(Long sourceAccountId, Long targetAccountId, Double amount) {
        BankAccountEntity sourceAccount = getAccountById(sourceAccountId);
        BankAccountEntity targetAccount = getAccountById(targetAccountId);

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(targetAccount);

        return mapEntityToResponse(sourceAccount);
    }

    private BankAccountEntity getAccountById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
    }

    @Override
    public AccountResponse getAccount(Long accountId) {
        BankAccountEntity account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
        return mapEntityToResponse(account);
    }

    private AccountResponse mapEntityToResponse(BankAccountEntity accountEntity) {
        return new AccountResponse(accountEntity.getId(), accountEntity.getUser().getId(), accountEntity.getBalance());
    }
}
