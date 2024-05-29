package ru.inno.local.account;

import java.util.EnumMap;
import java.util.Map;

public class ChangeCurrency implements Command {
    private final Currency newCurrency;
    private final long newBalance;
    private final Map<Currency, Long> prevCurrency;
    private final Account account;

    public ChangeCurrency(Currency newCurrency, long newBalance, Account account) {
        this.newCurrency = newCurrency;
        this.newBalance = newBalance;
        this.account = account;
        this.prevCurrency = new EnumMap<>(Currency.class);
        this.prevCurrency.putAll(account.getCurBal());
    }

    @Override
    public void execute() {
        account.addCurrencyBalance(newCurrency,newBalance);
    }

    @Override
    public Account undo() {
        return new Account(account.getOwner(),prevCurrency);
    }
}
