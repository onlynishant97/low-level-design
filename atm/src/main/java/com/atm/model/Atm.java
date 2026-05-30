package com.atm.model;

import com.atm.enums.AtmStatus;
import com.atm.enums.Denomination;
import com.atm.hardware.CashDeposit;
import com.atm.service.BankingService;
import com.atm.service.CardService;
import com.atm.service.CashManagementService;
import com.atm.state.AtmState;
import com.atm.state.IdleState;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class Atm {

    private final String atmId;
    private AtmStatus status;
    private AtmState currentState;
    private final CashManagementService cashManagementService;
    private final BankingService bankingService;
    private CardService cardService;
    private final CashDeposit cashDeposit;

    public Atm(String atmId, CashManagementService cashManagementService,
               BankingService bankingService, CardService cardService, CashDeposit cashDeposit) {
        this.atmId = atmId;
        this.status = AtmStatus.ACTIVE;
        this.bankingService = bankingService;
        this.cashManagementService = cashManagementService;
        this.cardService = cardService;
        this.currentState = new IdleState(this);
        this.cashDeposit = cashDeposit;
    }

    // STATE TRANSITION METHODS
    public void insertCard(Card card) {
        currentState.insertCard(card);
    }

    public void authenticate(String pin) {
        currentState.authenticate(pin);
    }

    public void withdraw(BigDecimal amount) {
        currentState.withdraw(amount);
    }

    public void deposit(Map<Denomination, Integer> cash) {
        currentState.deposit(cash);
    }

    public BigDecimal checkBalance() {
        return currentState.checkBalance();
    }

    public void ejectCard() {
        currentState.ejectCard();
    }

}