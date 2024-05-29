package ru.inno.local.account;

import java.util.*;

public class Account {
    private String owner;
    private Map<Currency, Long> curBal;
    private final Deque<Command> executedCommands = new ArrayDeque<>();

    public Account(String owner) {
        if (owner == null || owner.isEmpty())
            throw new IllegalArgumentException("Владелец счета не может быть null или пусто!");
        this.owner = owner;
        this.curBal = new EnumMap<>(Currency.class);
    }

    public Account(String owner, Map<Currency,Long> curBal) {
        this(owner);
        this.curBal = curBal;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null || owner.isEmpty())
            throw new IllegalArgumentException("Владелец счета не может быть null или пусто!");
       this.owner = owner;
    }

    // return unmodifiableMap
    public Map<Currency, Long> getCurBal() {
        return Collections.unmodifiableMap(this.curBal);
    }

    public void addCurrencyBalance(Currency currency, long balance) {
        if (balance < 0) throw new IllegalArgumentException("Количество валюты не может быть отрицательно!");
        if (currency == null) throw new IllegalArgumentException("Валюта не может быть пустой!");
        curBal.put(currency, balance);
    }

    public void executeCommand(Command command){
        executedCommands.push(command);
        command.execute();
    }

    public void undoLastCommand(){
        if (canUndo()){
            Command command = executedCommands.pop();
            Account account = command.undo();
            this.setOwner(account.getOwner());
            this.curBal = account.getCurBal();
        }
    }

    public boolean canUndo(){
        return executedCommands.isEmpty() ? false : true;
    }

    public AccountState save (){
        return new AccountState(this.owner,this.getCurBal());
    }

    public static Account restore(AccountState accountState){
        Map<Currency,Long> saveCurBal = new EnumMap<>(Currency.class);
        saveCurBal.putAll(accountState.getCurBal());
        return new Account(accountState.getOwner(), saveCurBal);
    }
}
