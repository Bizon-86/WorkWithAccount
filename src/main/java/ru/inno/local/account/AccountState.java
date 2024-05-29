package ru.inno.local.account;

import java.util.Collections;
import java.util.Map;

public class AccountState {
    private final String owner;
    private final Map<Currency, Long> curBal;

    public AccountState(String owner, Map<Currency, Long> curBal) {
        this.owner = owner;
        this.curBal = Collections.unmodifiableMap(curBal);
    }

    public String getOwner() {
        return this.owner;
    }

    public Map<Currency, Long> getCurBal() {
        return this.curBal;
    }
}
