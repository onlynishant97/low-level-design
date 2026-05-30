package com.atm;

import com.atm.enums.Denomination;
import com.atm.hardware.CashDeposit;
import com.atm.hardware.CashDispenser;
import com.atm.model.Atm;
import com.atm.model.Account;
import com.atm.model.Card;
import com.atm.model.CashInventory;
import com.atm.repository.AccountRepository;
import com.atm.repository.InMemoryAccountRepository;
import com.atm.repository.InMemoryCardRepository;
import com.atm.repository.InMemoryTransactionRepository;
import com.atm.service.*;
import com.atm.state.IdleState;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        AccountRepository accountRepo = new InMemoryAccountRepository();
        Account account = new Account("ACC123", new BigDecimal("10000"));
        accountRepo.save(account);

        Card card = new Card("CARD123", "ACC123");


        BankingService bankingService =
                new BankingServiceImpl(accountRepo, new InMemoryTransactionRepository());

        CashInventory cashInventory = new CashInventory();
        cashInventory.addCash(Denomination.TWO_THOUSAND, 5);
        cashInventory.addCash(Denomination.FIVE_HUNDRED, 10);
        cashInventory.addCash(Denomination.TWO_HUNDRED, 50);
        cashInventory.addCash(Denomination.ONE_HUNDRED, 100);
        CashDispenser cashDispenser = new CashDispenser();
        CashDeposit cashDeposit = new CashDeposit();
        CardService cardService = new CardServiceImpl(new InMemoryCardRepository());
        CashManagementService cashManagementService = new CashManagementServiceImpl(cashInventory, cashDispenser);

        Atm atm = new Atm("ATM-1", cashManagementService, bankingService, cardService, cashDeposit);

        // IMPORTANT: set initial state
        atm.setCurrentState(new IdleState(atm));

        atm.insertCard(card);

        atm.authenticate("1234");

        System.out.println("Balance: " +
                atm.checkBalance());

        atm.withdraw(new BigDecimal("500"));

        System.out.println("After withdrawal balance: " +
                atm.checkBalance());

        atm.ejectCard();

    }
}