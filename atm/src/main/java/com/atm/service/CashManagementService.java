package com.atm.service;

import java.math.BigDecimal;
import java.util.Map;

import com.atm.enums.Denomination;

public interface CashManagementService {

    boolean canDispense(BigDecimal amount);

    Map<Denomination, Integer> dispense(BigDecimal amount);

    void deposit(Map<Denomination, Integer> depositedCash);
}