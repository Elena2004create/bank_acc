package ru.mirea.Ablautova.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.Ablautova.entities.BankAccountEntity;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
}