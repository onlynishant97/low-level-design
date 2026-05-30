package com.atm.state;

import java.math.BigDecimal;
import java.util.Map;

import com.atm.enums.Denomination;
import com.atm.model.Atm;
import com.atm.model.Card;

public class AuthenticatedState extends AbstractAtmState {

    private final Card card;

    public AuthenticatedState(Atm atm, Card card) {
        super(atm);
        this.card = card;
    }

    @Override
    public BigDecimal checkBalance() {
        return atm.getBankingService().getBalance(card.getAccountNumber());
    }

    @Override
    public void withdraw(BigDecimal amount) {
        String accountNumber =
                card.getAccountNumber();

        if (!atm.getCashManagementService().canDispense(amount)) {
            throw new RuntimeException("ATM cannot dispense the requested amount. Please try a different amount.");
        }
        // Debit bank account
        atm.getBankingService().debit(accountNumber, amount);

        try {
            atm.getCashManagementService().dispense(amount);
        } catch (Exception e) {
            // Step 4: Rollback — credit back if dispense fails
            atm.getBankingService().credit(accountNumber, amount);
            throw new RuntimeException("Cash dispensing failed. Your account has been refunded.");
        }
    }

    @Override
    public void deposit(Map<Denomination, Integer> cash) {
        String account = card.getAccountNumber();

        // 1. Accept physical cash
        atm.getCashDeposit().acceptDeposit(cash);

        // 2. Convert to total amount
        BigDecimal total = calculateTotal(cash);

        // 3. Credit bank
        atm.getBankingService().credit(account, total);

        // 4. Update ATM inventory
        atm.getCashManagementService().deposit(cash);
    }

    @Override
    public void ejectCard() {
        atm.setCurrentState(new IdleState(atm));
    }

    private BigDecimal calculateTotal(Map<Denomination, Integer> cash) {

        long total = 0;

        for (Map.Entry<Denomination, Integer> entry : cash.entrySet()) {

            Denomination denomination = entry.getKey();
            Integer count = entry.getValue();

            if (count == null || count <= 0) {
                continue;
            }

            total += (long) denomination.getValue() * count;
        }

        return BigDecimal.valueOf(total);
    }
}